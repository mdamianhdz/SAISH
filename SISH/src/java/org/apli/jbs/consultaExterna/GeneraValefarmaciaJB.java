package org.apli.jbs.consultaExterna;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.apli.modelbeans.Paciente;
import org.apli.modelbeans.Medicamento;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.apache.log4j.Logger;
import org.apli.jbs.PacienteJB;
import org.apli.jbs.UnidadMedidaJB;
import org.apli.modelbeans.ConceptoIngreso;
import org.apli.modelbeans.EpisodioMedico;
import org.apli.modelbeans.Farmacia;
import org.apli.modelbeans.MaterialCuracion;
import org.apli.modelbeans.Medico;
import org.apli.modelbeans.ServicioPrestado;
import org.apli.modelbeans.UnidadMedida;
import org.primefaces.context.RequestContext;

/**
 *
 * @author MiguelAngel
 */
@ManagedBean(name = "oValeFarmacia")
@SessionScoped
public class GeneraValefarmaciaJB implements Serializable {

    private Paciente oPaciente;
    private String sTipoBusqueda = "";
    private Date dFechaVencimiento;
    private Date dFecha;
    private String sValorBusqueda = "";
    private List<Medicamento> resultadosBusqueda;
    private Farmacia oRequisicion;
    private Medicamento selectedPedido;
    private Medicamento oMedicamento;
    private int nCantidad;
    private float nTotal;
    private boolean bTipoPrincipalPago = false;
    private List<Medicamento> listaPedidos = new ArrayList<Medicamento>();
    private EpisodioMedico oEpisodioMedico;
    /*
     * Variables agregadas por nuevas observaciones en un pedido a farmacia
     */
    private int nHabitacion;
    private int farmaciaAPedir;
    /*
     * Agregadas para registrar en ServicioPrestado
     */
    private List tiposPacientes;
    private boolean quererFactura;
    private String tipoPaciente = "";
    private List<UnidadMedida> listaUnidades;
    private int nQuienPaga;
    private boolean bConEpisodio = false;
    private Medico oMedico;
    private UnidadMedida oUnidadMedida;
    private float nuevoCosto;
    private List<String> folios;
    int nCveReq = 0;
    private static final Logger LOG = Logger.getLogger(GeneraValefarmaciaJB.class.getName());

    //Indica si se buscara un material o medicamento
    //1 material
    //2 medicamento
    private int busquedaDe;

    public GeneraValefarmaciaJB() {
    }

    public void obtenerDatos() throws Exception {
        LOG.trace("Dentro de obtenerDatos();");
        limpiaDatos();
        this.oPaciente = new PacienteJB().getPacienteSesion();
        this.oEpisodioMedico = new EpisodioMedico();
        this.oEpisodioMedico.buscaDiagnosticoActual(this.oPaciente.getFolioPac());
        this.oEpisodioMedico.tieneEpisodioMedicoPaciente(this.oPaciente.getFolioPac());
        this.oEpisodioMedico.buscaTipoPrincEpi(this.oPaciente.getFolioPac());
        this.nHabitacion = 200;
        this.nHabitacion = (this.oEpisodioMedico.getHab() == null || this.oEpisodioMedico.getHab().getHab() == 0) ? 200 : this.oEpisodioMedico.getHab().getHab();
        LOG.trace("Habitacion para enviar a SIENA: [" + this.nHabitacion + "]");

        if (this.oEpisodioMedico.getTipoPrincipal() != 0) {
            this.bTipoPrincipalPago = true;
        }
        if (oEpisodioMedico.getMedTratante() != null) {
            bConEpisodio = true;
        }
        LOG.trace(new StringBuilder().append("TipoPrincipal de pago ").append(this.oEpisodioMedico.getTipoPrincipal()));
        if (this.bTipoPrincipalPago == false) {
            LOG.trace(new StringBuilder()
                    .append("El paciente ")
                    .append(this.oPaciente.getNombreCompleto())
                    .append(" con folio ")
                    .append(this.oPaciente.getFolioPac())
                    .append(" tiene tipo de pago principal")
                    .append(this.oEpisodioMedico.getTipoPrincipal())
                    .toString());
//            FacesMessage msj2 = new FacesMessage("¡Aviso!", "¡El paciente : " + this.oPaciente.getNombreCompleto() + " con folio: " + this.oPaciente.getFolioPac() + " tiene como pago principal: Contado!");
//            RequestContext.getCurrentInstance().showMessageInDialog(msj2);
        }
        this.resultadosBusqueda = new ArrayList<Medicamento>();
        this.dFechaVencimiento = new Date();
        this.dFecha = new Date();

        this.tiposPacientes = new ArrayList();
        this.tiposPacientes.add("Particular");
        this.tiposPacientes.add("Empresa");
        this.listaUnidades = new ArrayList<UnidadMedida>();
        this.listaUnidades = new UnidadMedida().buscatodasunidades();
        UnidadMedidaJB.unidadesMedidas = this.listaUnidades;
        oUnidadMedida = new UnidadMedida();
    }

    public void limpiaDatos() {
        this.oPaciente = null;
        this.sTipoBusqueda = "";
        this.dFechaVencimiento = null;
        this.sValorBusqueda = "";
        this.resultadosBusqueda = null;
        this.oRequisicion = null;
        this.selectedPedido = null;
        this.nCantidad = 0;
        this.listaPedidos = new ArrayList<Medicamento>();
        this.dFechaVencimiento = null;
        this.dFecha = null;
        this.bTipoPrincipalPago = false;
        this.oEpisodioMedico = null;
        this.nTotal = 0;
        this.nHabitacion = 0;
        this.tiposPacientes = new ArrayList();
        this.tipoPaciente = "";
        this.quererFactura = false;
        this.farmaciaAPedir = 1;
        this.listaUnidades = new ArrayList<UnidadMedida>();
        this.nQuienPaga = 0;
        oUnidadMedida = null;
        this.nuevoCosto = 0;
        this.nCveReq = 0;
        this.busquedaDe = 0;
    }

    public void agregaPedido() {
        LOG.trace(this.oUnidadMedida);
        if (null == this.selectedPedido) {
            String rst = new StringBuilder("Medicamento: Error de validacion: selecciona un medicamento o material").toString();
            FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
        } else if (0 == this.nCantidad) {
            String rst = new StringBuilder("Cantidad : Error de validacion: se necesita un valor").toString();
            FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
        } else if (0 == this.nuevoCosto && this.farmaciaAPedir == 2) {
            String rst = new StringBuilder("Costo unitario : Error de validacion: se necesita un valor").toString();
            FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
        } else if (null == this.oUnidadMedida) {
            String rst = new StringBuilder("Unidad de medida : Error de validacion: se necesita un valor").toString();
            FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
        } else {
            this.selectedPedido.setCantidadActualizada(this.nCantidad);
            this.selectedPedido.setUnidadMedida(this.oUnidadMedida);
            if (this.farmaciaAPedir != 1) {
                this.selectedPedido.setCostoUnitario(this.nuevoCosto);
            }
            this.listaPedidos.add(this.selectedPedido);
            actualizaTotal();
            this.selectedPedido = null;
            this.nCantidad = 0;
            this.oUnidadMedida = new UnidadMedida();
        }
    }

    public void actualizaTotal() {
        float tmpTotal = 0;
        for (int i = 0; i < this.listaPedidos.size(); i++) {
            tmpTotal += this.listaPedidos.get(i).getCantidadActualizada() * this.listaPedidos.get(i).getCostoUnitario();
        }
        this.nTotal = tmpTotal;
        LOG.trace(new StringBuilder().append("Total actualizado en: ").append(this.nTotal).toString());
    }

    public void eliminaPedido(String id) {
        for (int i = 0; i < this.listaPedidos.size(); i++) {
            if (id.equals(this.listaPedidos.get(i).getCveMedicamento())) {
                this.listaPedidos.remove(i);
                LOG.trace(new StringBuilder().append("Medicamento eliminado con clave: ").append(id).toString());
                break;
            }
        }
        actualizaTotal();
    }

    public void generaPedido() throws Exception {
        if (this.listaPedidos.isEmpty()) {
            String rst = new StringBuilder("Pedido: Error de validación: no se ha agregado ningún medicamento y/o material").toString();
            FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
        } else if (0 == this.nHabitacion) {
            String rst = new StringBuilder("Pedido: Error de validación: no se ha indicado una habitación").toString();
            FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
        } else if (this.tipoPaciente.equals("1")) {
            String rst = new StringBuilder("Pedido: Error de validación: no se indicado un tipo de paciente").toString();
            FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
        } else {
            LOG.trace("Codigo de farmacia a pedir: " + this.farmaciaAPedir);
            if (this.farmaciaAPedir == 1) {
                this.oRequisicion = new Farmacia();
                this.oRequisicion.setBranch("1");
                this.oRequisicion.setPaciente(this.oPaciente);
                this.oRequisicion.setHabitacion(this.nHabitacion);//Cargar valor
                this.oRequisicion.setPedido(this.listaPedidos);
                LOG.trace(new StringBuilder().append("Tamaño de pedido:").append(this.listaPedidos.size()).toString());
                this.oRequisicion.registrarPedido();
                FacesMessage msj = new FacesMessage((this.oRequisicion.getCveRequisicion() != -1) ? FacesMessage.SEVERITY_INFO : FacesMessage.SEVERITY_ERROR, this.oRequisicion.getMensaje(), "");
                FacesContext.getCurrentInstance().addMessage(null, msj);
                if (this.oRequisicion.getCveRequisicion() == -1) {
                    FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Pedido: Ha ocurrido un error con el sistema de farmacia, realiza el pedido con farmacia externa", "");
                    FacesContext.getCurrentInstance().addMessage(null, msj2);
                } else {
                    this.nCveReq = this.oRequisicion.getCveRequisicion();
                }
            } else {
                this.nCveReq = -999;
            }
            boolean agregados = false;
            if (this.nCveReq != 0) {
                agregados = insertarServicioPrestado();
            }
            String messageSP = (agregados) ? "Se ha realizado correctamente el cargo al paciente " + this.oPaciente.getNombreCompleto() : "No se ha realizado el cargo correctamente";
            FacesMessage msj2 = new FacesMessage((agregados) ? FacesMessage.SEVERITY_INFO : FacesMessage.SEVERITY_ERROR, messageSP, "");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
            if (agregados && this.nCveReq != 0) {
                RequestContext.getCurrentInstance().execute("dlg1.show()");
            }
        }
    }

    public boolean validaUsuario() {
        LOG.trace("this.oPaciente: " + this.oPaciente);
        boolean validacion = false;
        if (this.oPaciente != null) {
            validacion = true;
        }
        LOG.trace("Validacion: " + validacion);
        return validacion;
    }

    public boolean mostrarCosto() {
        boolean validacion = false;
        if (this.farmaciaAPedir == 2) {
            validacion = true;
        }
        return validacion;
    }

    public boolean insertarServicioPrestado() throws Exception {
        boolean validacion = true;
        this.folios = new ArrayList<String>();
        if (this.tipoPaciente.equals("Particular")) {
            this.nQuienPaga = 0;
        } else if (this.tipoPaciente.equals("Empresa")) {
            this.nQuienPaga = 1;
        }
        for (int i = 0; i < this.listaPedidos.size(); i++) {
            ServicioPrestado oServicioPrestado = new ServicioPrestado();
            oServicioPrestado.setPaciente(this.oPaciente);
            oServicioPrestado.setFacturable(this.quererFactura);
            oServicioPrestado.setRegistro(new Date());
            oServicioPrestado.setCostoOriginal(this.listaPedidos.get(i).getCostoUnitario());
            oServicioPrestado.setCostoCobrado(this.listaPedidos.get(i).getCostoUnitario());
            oServicioPrestado.setQuienPaga(this.nQuienPaga);
            if (this.bConEpisodio == true) {
                this.oMedico = this.oEpisodioMedico.getMedTratante();
                this.oMedico.setFolioPers(0);
                oServicioPrestado.setMedico(this.oMedico);
            } else {
                oServicioPrestado.setMedico(this.oMedico);
            }

            oServicioPrestado.setSituacion("N");

            String tipoMetMed = "";
            String cveMedMat = "0";
            MaterialCuracion mc = new MaterialCuracion();
            Medicamento m = new Medicamento();
            ConceptoIngreso oCI = new ConceptoIngreso();
            LOG.trace("Material curacion encontrado: " + mc.buscaMaterialCuracion(this.listaPedidos.get(i).getCveMedicamento()));
            LOG.trace("Medicamento encontrado: " + m.buscaMedicamentoVitrina(this.listaPedidos.get(i).getCveMedicamento()));
            mc = mc.buscaMaterialCuracion(this.listaPedidos.get(i).getCveMedicamento());
            m = m.buscaMedicamentoVitrina(this.listaPedidos.get(i).getCveMedicamento());
            if (mc != null) {
                tipoMetMed = "material";
                oCI.setCveConcep(mc.getConceptoIngreso().getCveConcep());
            } else if (m != null) {
                tipoMetMed = "medicamento";
                LOG.trace(m);
                oCI.setCveConcep(m.getConceptoIngreso().getCveConcep());
            }
            LOG.trace(tipoMetMed);
            oServicioPrestado.setConcepPrestado(oCI);
            oServicioPrestado.setEpisodioMedico(this.oEpisodioMedico);
            oServicioPrestado.setCantidad(this.listaPedidos.get(i).getCantidadActualizada());
            oServicioPrestado.setUniMed(this.listaPedidos.get(i).getUnidadMedida());
            cveMedMat = this.listaPedidos.get(i).getCveMedicamento();//Aplica tanto para MaterialCuracion como para Medicamento

            String msj = oServicioPrestado
                    .insertarServicioPrestado(
                            (tipoMetMed.equals("material")) ? 24 : 25,
                            cveMedMat,
                            (tipoMetMed.equals("material")) ? "13" : "14",
                            tipoMetMed);
            if (msj.indexOf("ERROR") > 0) {
                FacesMessage msj2 = new FacesMessage(msj, "");
                FacesContext.getCurrentInstance().addMessage(null, msj2);
                validacion = false;
                break;

            }
            if (msj.indexOf("historiaclinica_episodiomedico_fk") > 0) {
                FacesMessage msj2 = new FacesMessage("¡El paciente : " + this.oPaciente.getNombreCompleto() + " con folio: " + this.oPaciente.getFolioPac() + " no tiene historia clínica!", "");
                FacesContext.getCurrentInstance().addMessage(null, msj2);
                validacion = false;
                break;
            } else {
                this.folios.add(msj);
            }
            oServicioPrestado.setIdFolio(msj);
            LOG.trace("folios: [" + this.folios + "] msj: [" + msj + "]");
            String msj2 = oServicioPrestado.actualizarRequesicion(this.nCveReq);
            if (msj2.indexOf("ERROR") > 0) {
                FacesMessage msj3 = new FacesMessage(msj2, "");
                FacesContext.getCurrentInstance().addMessage(null, msj3);
                validacion = false;
                break;
            }
        }
        return validacion;
    }

    public void salir() throws IOException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.getExternalContext().redirect("../login/menuOpe.xhtml");
    }

    public void buscarLocal() throws Exception {
        if (this.busquedaDe == 0) {//Buscar medicamento o materia de curacion
            String rst = new StringBuilder("Buscar: Error de validacion: Se necesita un valor").toString();
            FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
        } else if (this.sTipoBusqueda.isEmpty()) {//Busqueda por sustancia activa o descripcion
            String rst = new StringBuilder("Filtro por: Error de validacion: Se necesita un valor").toString();
            FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
        } else if (this.sValorBusqueda.isEmpty()) {
            String rst = new StringBuilder("Busqueda: Error de validacion: Se necesita un valor").toString();
            FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
        } else {
            LOG.trace(new StringBuilder().append("Antes de buscar: con tipoBusqueda=").append(this.sTipoBusqueda)
                    .append(" y valorBusqueda=").append(this.sValorBusqueda).toString());
            if (this.busquedaDe == 1) {//Buscar material de curacion
                if ("1".equals(this.sTipoBusqueda) || "2".equals(this.sTipoBusqueda)) {
                    MaterialCuracion materialCuracion = new MaterialCuracion();
                    this.resultadosBusqueda = materialCuracion.buscaMaterialDeCuracionPorDescripcion(this.sValorBusqueda);
                    if (null == this.resultadosBusqueda) {
                        this.resultadosBusqueda = new ArrayList<Medicamento>();
                        FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, this.oRequisicion.getMensaje(), "");
                        FacesContext.getCurrentInstance().addMessage(null, msj2);
                    }
                } else {
                    LOG.trace(new StringBuilder("A ocurrido un error al detectar el tipo de busqueda"));
                }
            } else if (this.busquedaDe == 2) {//Buscar medicamento
                if ("1".equals(this.sTipoBusqueda)) {
                    this.oMedicamento = new Medicamento();
                    this.resultadosBusqueda = this.oMedicamento.buscaMedicamentoPorSustanciaActiva(this.sValorBusqueda);
                    if (null == this.resultadosBusqueda) {
                        this.resultadosBusqueda = new ArrayList<Medicamento>();
                        FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, this.oRequisicion.getMensaje(), "");
                        FacesContext.getCurrentInstance().addMessage(null, msj2);
                    }
                } else if ("2".equals(this.sTipoBusqueda)) {
                    this.oMedicamento = new Medicamento();
                    this.resultadosBusqueda = this.oMedicamento.buscaMedicamentoPorDescripcion(this.sValorBusqueda);
                    if (null == this.resultadosBusqueda) {
                        this.resultadosBusqueda = new ArrayList<Medicamento>();
                        FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, this.oRequisicion.getMensaje(), "");
                        FacesContext.getCurrentInstance().addMessage(null, msj2);
                    }
                } else {
                    LOG.trace(new StringBuilder("A ocurrido un error al detectar el tipo de busqueda"));
                }
            } else {
                LOG.trace(new StringBuilder("No ha sido posible detectar si se busca un medicamento o material de curacion"));
            }
        }
    }

    public void buscar() {
        if (this.sTipoBusqueda.isEmpty()) {
            String rst = new StringBuilder("Tipo de busqueda: Error de validacion: Se necesita un valor").toString();
            FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
        } else if (this.sValorBusqueda.isEmpty()) {
            String rst = new StringBuilder("Busqueda: Error de validacion: Se necesita un valor").toString();
            FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
        } else {
            LOG.trace(new StringBuilder()
                    .append("Antes de buscar: con tipoBusqueda=")
                    .append(this.sTipoBusqueda)
                    .append(" y valorBusqueda=")
                    .append(this.sValorBusqueda).toString());
            this.oRequisicion = new Farmacia();
            this.oMedicamento = new Medicamento();
            if ("1".equals(this.sTipoBusqueda)) {
                this.oMedicamento.setSustanciaActiva(this.sValorBusqueda);
                this.oRequisicion.setMedicamento(this.oMedicamento);
                this.oRequisicion.setBranch("1");
                this.resultadosBusqueda = this.oRequisicion.buscarPorSustanciaActiva();
                if (null == this.resultadosBusqueda) {
                    this.resultadosBusqueda = new ArrayList<Medicamento>();
                    FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, this.oRequisicion.getMensaje(), "");
                    FacesContext.getCurrentInstance().addMessage(null, msj2);
                }
            } else if ("2".equals(this.sTipoBusqueda)) {
                this.oMedicamento.setNomMedicamento(this.sValorBusqueda);
                this.oRequisicion.setMedicamento(this.oMedicamento);
                this.oRequisicion.setBranch("1");
                this.resultadosBusqueda = this.oRequisicion.buscarPorDescripcion();
                if (null == this.resultadosBusqueda) {
                    this.resultadosBusqueda = new ArrayList<Medicamento>();
                    FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, this.oRequisicion.getMensaje(), "");
                    FacesContext.getCurrentInstance().addMessage(null, msj2);
                }
            } else {
                LOG.trace(new StringBuilder("A ocurrido un error al detectar el tipo de busqueda"));
            }
        }
    }

    public boolean buscarMedicamentoEnPedido(Medicamento m) {
        for (Medicamento medicamento : this.listaPedidos) {
            if (medicamento.getCveMedicamento().equals(m.getCveMedicamento())) {
                return true;
            }
        }
        return false;
    }

    public Paciente getPaciente() {
        return oPaciente;
    }

    public void setPaciente(Paciente oPaciente) {
        this.oPaciente = oPaciente;
    }

    public String getTipoBusqueda() {
        return sTipoBusqueda;
    }

    public void setTipoBusqueda(String tipoBusqueda) {
        this.sTipoBusqueda = tipoBusqueda;
    }

    public Date getFechaVencimiento() {
        return dFechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.dFechaVencimiento = fechaVencimiento;
    }

    public Date getFecha() {
        return dFecha;
    }

    public void setFecha(Date fecha) {
        this.dFecha = fecha;
    }

    public String getValorBusqueda() {
        return sValorBusqueda;
    }

    public void setValorBusqueda(String valorBusqueda) {
        this.sValorBusqueda = valorBusqueda;
    }

    public List<Medicamento> getResultadosBusqueda() {
        return resultadosBusqueda;
    }

    public void setResultadosBusqueda(List<Medicamento> resultadosBusqueda) {
        this.resultadosBusqueda = resultadosBusqueda;
    }

    public Medicamento getSelectedPedido() {
        return selectedPedido;
    }

    public void setSelectedPedido(Medicamento selectedPedido) {
        if (selectedPedido != null) {
            LOG.trace(new StringBuilder().append("Medicamento seleccionado: ").append(selectedPedido.getNomMedicamento()).toString());
        if (buscarMedicamentoEnPedido(selectedPedido)) {
            this.selectedPedido = null;
            String rst = new StringBuilder("Medicamento: El medicamento ya se encuentra en la lista").toString();
            FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
        } else {
                this.selectedPedido = selectedPedido;

//            if (selectedPedido != null && selectedPedido.getExistencia() > 0) {
//                this.selectedPedido = selectedPedido;
//            } else {
//                this.selectedPedido = null;
//                String rst = new StringBuilder().append("Medicamento: Error de validacion: Sin existencia").toString();
//                FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
//                FacesContext.getCurrentInstance().addMessage(null, msj2);
//            }
            }
        }
    }

    public void onRowSelect(SelectEvent event) {
    }

    public void onRowUnselect(UnselectEvent event) {
    }

    public Farmacia getRequisicion() {
        return oRequisicion;
    }

    public void setRequisicion(Farmacia requisicion) {
        this.oRequisicion = requisicion;
    }

    public int getCantidad() {
        return nCantidad;
    }

    public void setCantidad(int cantidad) {
        if (cantidad < 0) {
            this.nCantidad = 0;
        } else {
            if (this.farmaciaAPedir == 2) {
                this.nCantidad = cantidad;
                actualizaTotal();
//            } else if (null != this.selectedPedido && cantidad > this.selectedPedido.getExistencia()) {
//                LOG.trace(new StringBuilder()
//                        .append("Existencia de medicamento seleccionado:")
//                        .append(this.selectedPedido.getExistencia())
//                        .append(" cantidad de pedido:")
//                        .append(cantidad));
//                String rst = new StringBuilder("Busqueda: Error de validacion: no es posible realizar pedido mayor a existencia").toString();
//                FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
//                FacesContext.getCurrentInstance().addMessage(null, msj2);
            } else {
                this.nCantidad = cantidad;
                actualizaTotal();
            }
        }
    }

    public List<Medicamento> getListaPedidos() {
        return listaPedidos;
    }

    public void setListaPedidos(List<Medicamento> listaPedidos) {
        this.listaPedidos = listaPedidos;
    }

    public EpisodioMedico getEpisodioMedico() {
        return oEpisodioMedico;
    }

    public void setEpisodioMedico(EpisodioMedico oEpisodioMedico) {
        this.oEpisodioMedico = oEpisodioMedico;
    }

    public boolean isConEpisodio() {
        return bTipoPrincipalPago;
    }

    public void setConEpisodio(boolean bConEpisodio) {
        this.bTipoPrincipalPago = bConEpisodio;
    }

    public float getTotal() {
        return nTotal;
    }

    public void setTotal(float nTotal) {
        this.nTotal = nTotal;
    }

    public Medicamento getMedicamento() {
        return oMedicamento;
    }

    public void setMedicamento(Medicamento oMedicamento) {
        this.oMedicamento = oMedicamento;
    }

    public int getHabitacion() {
        return nHabitacion;
    }

    public void setHabitacion(int nHabitacion) {
        this.nHabitacion = nHabitacion;
    }

//
//    public String getEmpresa() {
//        return sEmpresa;
//    }
//
//    public void setEmpresa(String sEmpresa) {
//        this.sEmpresa = sEmpresa;
//    }
//
//    public String getCveCliente() {
//        return sCveCliente;
//    }
//
//    public void setCveCliente(String sCveCliente) {
//        this.sCveCliente = sCveCliente;
//    }
//
//    public AreaFisica getAreaFisica() {
//        return oAreaFisica;
//    }
//
//    public void setAreaFisica(AreaFisica oAreaFisica) {
//        this.oAreaFisica = oAreaFisica;
//    }
//
//    public AreaFisica[] getAreasFisicas() {
//        return areasFisicas;
//    }
//
//    public void setAreasFisicas(AreaFisica[] areasFisicas) {
//        this.areasFisicas = areasFisicas;
//    }
    public int getFarmaciaAPedir() {
        return farmaciaAPedir;
    }

    public void setFarmaciaAPedir(int farmaciaAPedir) {
        this.farmaciaAPedir = farmaciaAPedir;
        if (farmaciaAPedir == 2) {//Pedido a farmacia externa, implica evitar validacion de existencia
            for (int i = 0; i < this.listaPedidos.size(); i++) {
                this.listaPedidos.get(i).setPedidoExterno(true);
            }
        } else {
            for (int i = 0; i < this.listaPedidos.size(); i++) {
                this.listaPedidos.get(i).setPedidoExterno(true);//El pedido se hace a SIENA pero antes se valida en SISH, implica no validar de entrada existencia
            }
        }
    }

    public List getTiposPacientes() {
        return tiposPacientes;
    }

    public void setTiposPacientes(List tiposPacientes) {
        this.tiposPacientes = tiposPacientes;
    }

    public boolean isQuererFactura() {
        return quererFactura;
    }

    public void setQuererFactura(boolean quererFactura) {
        this.quererFactura = quererFactura;
    }

    public String getTipoPaciente() {
        return tipoPaciente;
    }

    public void setTipoPaciente(String tipoPaciente) {
        this.tipoPaciente = tipoPaciente;
    }

    public List<UnidadMedida> getListaUnidades() {
        return listaUnidades;
    }

    public void setListaUnidades(List<UnidadMedida> listaUnidades) {
        this.listaUnidades = listaUnidades;
    }

    public int getQuienPaga() {
        return nQuienPaga;
    }

    public void setQuienPaga(int nQuienPaga) {
        this.nQuienPaga = nQuienPaga;
    }

    public boolean getConEpisodioMedico() {
        return bConEpisodio;
    }

    public void setConEpisodioMedico(boolean bConEpisodio) {
        this.bConEpisodio = bConEpisodio;
    }

    public Medico getMedico() {
        return oMedico;
    }

    public void setMedico(Medico oMedico) {
        this.oMedico = oMedico;
    }

    public UnidadMedida getUnidadMedida() {
        return oUnidadMedida;
    }

    public void setUnidadMedida(UnidadMedida unidadMedida) {
        this.oUnidadMedida = unidadMedida;
    }

    public float getNuevoCosto() {
        return nuevoCosto;
    }

    public void setNuevoCosto(float nuevoCosto) {
        this.nuevoCosto = nuevoCosto;
    }

    public List<String> getFolios() {
        return folios;
    }

    public void setFolios(List<String> folios) {
        this.folios = folios;
    }

    public int getnCveReq() {
        return nCveReq;
    }

    public void setnCveReq(int nCveReq) {
        this.nCveReq = nCveReq;
    }

    public int getBusquedaDe() {
        return busquedaDe;
    }

    public void setBusquedaDe(int busquedaDe) {
        this.busquedaDe = busquedaDe;
    }

    @Override
    public String toString() {
        return "GeneraValefarmaciaJB{" + "oPaciente=" + oPaciente + ", sTipoBusqueda=" + sTipoBusqueda + ", dFechaVencimiento=" + dFechaVencimiento + ", dFecha=" + dFecha + ", sValorBusqueda=" + sValorBusqueda + ", resultadosBusqueda=" + resultadosBusqueda + ", oRequisicion=" + oRequisicion + ", selectedPedido=" + selectedPedido + ", oMedicamento=" + oMedicamento + ", nCantidad=" + nCantidad + ", nTotal=" + nTotal + ", bTipoPrincipalPago=" + bTipoPrincipalPago + ", listaPedidos=" + listaPedidos + ", oEpisodioMedico=" + oEpisodioMedico + ", nHabitacion=" + nHabitacion + ", farmaciaAPedir=" + farmaciaAPedir + ", tiposPacientes=" + tiposPacientes + ", quererFactura=" + quererFactura + ", tipoPaciente=" + tipoPaciente + ", listaUnidades=" + listaUnidades + ", nQuienPaga=" + nQuienPaga + ", bConEpisodio=" + bConEpisodio + ", oMedico=" + oMedico + ", oUnidadMedida=" + oUnidadMedida + ", nuevoCosto=" + nuevoCosto + ", folios=" + folios + ", nCveReq=" + nCveReq + ", busquedaDe=" + busquedaDe + '}';
    }

}
