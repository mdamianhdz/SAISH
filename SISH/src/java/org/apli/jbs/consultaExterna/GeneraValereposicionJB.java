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
import org.apache.log4j.Logger;
import org.apli.jbs.PacienteJB;
import org.apli.jbs.UnidadMedidaJB;
import org.apli.modelbeans.AreaFisica;
import org.apli.modelbeans.ConceptoIngreso;
import org.apli.modelbeans.EpisodioMedico;
import org.apli.modelbeans.Farmacia;
import org.apli.modelbeans.InvMatVitrina;
import org.apli.modelbeans.InvMedVitrina;
import org.apli.modelbeans.InventarioVitrina;
import org.apli.modelbeans.MaterialCuracion;
import org.apli.modelbeans.Medicamento;
import org.apli.modelbeans.Medico;
import org.apli.modelbeans.Paciente;
import org.apli.modelbeans.ServicioPrestado;
import org.apli.modelbeans.UnidadMedida;
import org.apli.modelbeans.Vitrina;
import org.primefaces.context.RequestContext;

/**
 *
 * @author MiguelAngel
 */
@ManagedBean(name = "oValereposicion")
@SessionScoped
public class GeneraValereposicionJB implements Serializable {

    private Paciente oPaciente;
    private int nCveVitrina;
    private Vitrina oVitrina;
    private Vitrina[] vitrinas;
    private Date dFecha;
    private int nCantidad;
    private List<Medicamento> listaPedido = new ArrayList<Medicamento>();
    private List<InventarioVitrina> lista;
    private InventarioVitrina oInvVitrina;
    private InvMatVitrina oMatVitrina;
    private InvMatVitrina[] materialesVitrina;
    private InvMedVitrina oMedVitrina;
    private InvMedVitrina[] medicamentosVitrina;
    private String nCveSeleccionMaterial;
    private String nCveSeleccionMedicamento;
    private int nCveTipoSeleccion;
    private Farmacia oRequisicion;
    private AreaFisica oAreaFisica;
    private AreaFisica[] areasFisicas;
    private Date fechaVencimiento;
    private int limiteExistencia;
    private float nTotal;
    /*
     * Variables agregadas por nuevas observaciones en un pedido a farmacia
     */
    private int nHabitacion;
    private String sEmpresa;
    private String sCveCliente;
    private EpisodioMedico oEpisodioMedico;
    /*
     * Nuevos datos para registro en ServicioPrestado
     */
    private int farmaciaAPedir;
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
    boolean agregados = false;
    private static final Logger LOG = Logger.getLogger(GeneraValereposicionJB.class.getName());

    private boolean datosFaltantesObtenidos = false;

    public GeneraValereposicionJB() {
    }

    public void obtenerDatos() throws Exception {
        limpiaDatos();
        this.oPaciente = new PacienteJB().getPacienteSesion();
        this.dFecha = new Date();
        this.fechaVencimiento = new Date();
        this.oVitrina = new Vitrina();
        this.vitrinas = oVitrina.buscaTodos();
        this.oAreaFisica = new AreaFisica();
        this.areasFisicas = oAreaFisica.buscaTodos();
        this.lista = new ArrayList<InventarioVitrina>();
        this.oEpisodioMedico = new EpisodioMedico();
        this.oEpisodioMedico.buscaDiagnosticoActual(this.oPaciente.getFolioPac());
        this.oEpisodioMedico.buscaTipoPrincEpi(this.oPaciente.getFolioPac());
        if (oEpisodioMedico.getMedTratante() != null) {
            bConEpisodio = true;
        }
        this.nHabitacion = (this.oEpisodioMedico.getHab() == null) ? 200 : this.oEpisodioMedico.getHab().getHab();
        this.setCveVitrina(1);

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
        this.oRequisicion = null;
        this.lista = null;
        this.nCantidad = 0;
        this.nCveTipoSeleccion = 0;
        this.nCveSeleccionMaterial = "";
        this.nCveSeleccionMedicamento = "";
        this.listaPedido = new ArrayList<Medicamento>();
        this.materialesVitrina = null;
        this.medicamentosVitrina = null;
        this.nCveVitrina = 0;
        this.dFecha = null;
        this.oAreaFisica = null;
        this.areasFisicas = null;
        this.fechaVencimiento = null;
        this.nTotal = 0;
        this.nHabitacion = 0;
        this.sCveCliente = "";
        this.sEmpresa = "";
        this.tiposPacientes = new ArrayList();
        this.tipoPaciente = "";
        this.quererFactura = false;
        this.farmaciaAPedir = 1;
        this.listaUnidades = new ArrayList<UnidadMedida>();
        this.nQuienPaga = 0;
        this.oUnidadMedida = null;
        this.nuevoCosto = 0;
        this.datosFaltantesObtenidos = false;
    }

    public boolean validaUsuario() {
        boolean validacion = false;
        if (this.oPaciente != null) {
            validacion = true;
        }
        return validacion;
    }

    public boolean mostrarCosto() {
        boolean validacion = false;
        if (this.farmaciaAPedir == 2) {
            validacion = true;
        } else if (this.farmaciaAPedir == 1 && this.nCveReq == -999) {
            validacion = true;
        }
        return validacion;
    }

    public void agregar() throws Exception {
        LOG.trace(new StringBuilder().append("Valor de nCveTipoSeleccion:").append(this.nCveTipoSeleccion).toString());
        LOG.trace(new StringBuilder().append("Valor de nCveVitrina:").append(this.nCveVitrina).toString());
        LOG.trace(new StringBuilder().append("Valor de nCveSeleccionMaterial:").append(this.nCveSeleccionMaterial).toString());
        LOG.trace(new StringBuilder().append("Valor de nCveSeleccionMedicamento:").append(this.nCveSeleccionMedicamento).toString());
        LOG.trace(new StringBuilder().append("Valor de nCantidad:").append(this.nCantidad).toString());

        if (0 == this.nCantidad) {
            String rst = new StringBuilder("Cantidad: Error de validación: se necesita un valor").toString();
            FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
        } else if (0 == this.nCveTipoSeleccion) {
            String rst = new StringBuilder("Material o Medicamento : Error de validación: selecciona un tipo").toString();
            FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
        } else if (0 == this.nCveVitrina) {
            String rst = new StringBuilder("Vitrina : Error de validación: selecciona una vitrina").toString();
            FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
        } else if (1 == this.nCveTipoSeleccion && this.nCveSeleccionMaterial.equals("")) {
            String rst = new StringBuilder("Material : Error de validación: se necesita un valor").toString();
            FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
        } else if (2 == this.nCveTipoSeleccion && this.nCveSeleccionMedicamento.equals("")) {
            String rst = new StringBuilder("Medicamento : Error de validación: se necesita un valor").toString();
            FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
        } else if (this.oUnidadMedida == null) {
            String rst = new StringBuilder("Unidad de medida : Error de validación: se necesita un valor").toString();
            FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
        } else {
            this.oInvVitrina = new InventarioVitrina();
            if (1 == this.nCveTipoSeleccion) {
                if (buscarSeleccionadoEnPedido(new StringBuilder().append(this.nCveSeleccionMaterial).toString())) {
                    String rst = new StringBuilder("Material : Error de validación: el material seleccionado ya se encuentra en la lista").toString();
                    FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
                    FacesContext.getCurrentInstance().addMessage(null, msj2);
                } else if (mayorAExistenciaMaterial()) {
                    String rst = new StringBuilder()
                            .append("Material : Error de validación: solo hay ")
                            .append(this.limiteExistencia)
                            .append(" en existencia")
                            .toString();
                    FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
                    FacesContext.getCurrentInstance().addMessage(null, msj2);
                } else {
                    this.oMatVitrina = new InvMatVitrina();
                    this.oMatVitrina.setCantidad(this.nCantidad);
                    LOG.trace("Vitrina Converter:" + new StringBuilder().append(this.nCveVitrina).toString());
                    this.oMatVitrina.setVitrina(new Vitrina().buscaVitrina(Short.valueOf(new StringBuilder().append(this.nCveVitrina).toString())));
                    this.oMatVitrina.setMaterialCuracion(new MaterialCuracion().buscaMaterialCuracion(new StringBuilder().append(nCveSeleccionMaterial).toString()));
                    this.oMatVitrina.getMaterialCuracion().setUnidadMedida(this.oUnidadMedida);
                    this.oInvVitrina.setMatVitrina(this.oMatVitrina);
                    this.lista.add(this.oInvVitrina);
                }
            } else if (2 == this.nCveTipoSeleccion) {
                if (buscarSeleccionadoEnPedido(new StringBuilder().append(this.nCveSeleccionMedicamento).toString())) {
                    String rst = new StringBuilder("Medicamento : Error de validación: el medicamento seleccionado ya se encuentra en la lista").toString();
                    FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
                    FacesContext.getCurrentInstance().addMessage(null, msj2);
                } else if (mayorAExistenciaMedicamento()) {
                    String rst = new StringBuilder()
                            .append("Medicamento : Error de validación: solo hay ")
                            .append(this.limiteExistencia)
                            .append(" en existencia")
                            .toString();
                    FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
                    FacesContext.getCurrentInstance().addMessage(null, msj2);
                } else {
                    this.oMedVitrina = new InvMedVitrina();
                    this.oMedVitrina.setCantidad(this.nCantidad);
                    this.oMedVitrina.setVitrina(new Vitrina().buscaVitrina(Short.valueOf(new StringBuilder().append(this.nCveVitrina).toString())));
                    this.oMedVitrina.setMedicamento(new Medicamento().buscaMedicamentoVitrina(new StringBuilder().append(this.nCveSeleccionMedicamento).toString()));
                    this.oMedVitrina.getMedicamento().setUnidadMedida(this.oUnidadMedida);
                    this.oInvVitrina.setMedVitrina(this.oMedVitrina);
                    this.lista.add(this.oInvVitrina);
                }
            } else {
                String rst = new StringBuilder("Agregar : Error de validación: no se ha podido agregar el material o medicamento").toString();
                FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
                FacesContext.getCurrentInstance().addMessage(null, msj2);
            }
            this.nCantidad = 0;
            this.nCveTipoSeleccion = 0;
        }
    }

    public boolean mayorAExistenciaMaterial() {
        for (int i = 0; i < this.materialesVitrina.length; i++) {
            if (this.materialesVitrina[i].getMaterialCuracion().getCveMaterial().equals(this.nCveSeleccionMaterial)) {
                if (this.nCantidad > this.materialesVitrina[i].getCantidad()) {
                    this.limiteExistencia = this.materialesVitrina[i].getCantidad();
                    return true;
                }
            }
        }
        return false;
    }

    public boolean mayorAExistenciaMedicamento() {
        for (int i = 0; i < this.medicamentosVitrina.length; i++) {
            if (this.medicamentosVitrina[i].getMedicamento().getCveMedicamento().equals(this.nCveSeleccionMedicamento)) {
                if (this.nCantidad > this.medicamentosVitrina[i].getCantidad()) {
                    this.limiteExistencia = this.medicamentosVitrina[i].getCantidad();
                    return true;
                }
            }
        }
        return false;
    }

    public void eliminaPedido(String id) {
        for (int i = 0; i < this.lista.size(); i++) {
            if (this.lista.get(i).getMatVitrina() != null) {
                if (this.lista.get(i).getMatVitrina().getMaterialCuracion().getCveMaterial().equals(new StringBuilder().append(id).toString())) {
                    this.lista.remove(i);
                    LOG.trace(new StringBuilder().append("Material eliminado con clave: ").append(id).toString());
                    break;
                }
            } else if (this.lista.get(i).getMedVitrina() != null) {
                if (this.lista.get(i).getMedVitrina().getMedicamento().getCveMedicamento().equals(new StringBuilder().append(id).toString())) {
                    this.lista.remove(i);
                    LOG.trace(new StringBuilder().append("Medicamento eliminado con clave: ").append(id).toString());
                    break;
                }
            }
        }
    }

    public void generaVale() throws Exception {
        if (this.lista.isEmpty()) {
            String rst = new StringBuilder("Contenido: Error de validación: no se ha agregado ningún medicamento y/o material").toString();
            FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
        } else if (this.tipoPaciente.equals("1")) {
            String rst = new StringBuilder("Contenido: Error de validación: no se indicado un tipo de paciente").toString();
            FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
        } else {
            realizarPedidoEnFarmacia();
            if (datosFaltantesObtenidos) {//Solo se continuara si se obtuvieron los datos que faltan de SIENA
            this.oInvVitrina = new InventarioVitrina();
            this.oInvVitrina.setInvVitrinas(lista);
            String nRegModificados = this.oInvVitrina.modificarInvVitrina();
            LOG.trace(new StringBuilder().append("Se han modificado ").append(nRegModificados).append(" registros en inventario").toString());
            RequestContext.getCurrentInstance().execute("dlg1.show()");
        }
    }
    }

    public void realizarPedidoEnFarmacia() throws Exception {
        for (InventarioVitrina iv : this.lista) {
            this.oRequisicion = new Farmacia();
            if (iv.getMatVitrina() != null) {
                Medicamento m = new Medicamento();
                Medicamento mTemp;
                m.setCveMedicamento(iv.getMatVitrina().getMaterialCuracion().getCveMaterial());
                m.setNomMedicamento(iv.getMatVitrina().getMaterialCuracion().getDescripcion());
                m.setCantidadActualizada(iv.getMatVitrina().getCantidad());
                m.setUnidadMedida(iv.getMatVitrina().getMaterialCuracion().getUnidadMedida());
                m.setPedidoExterno(false);
                this.oRequisicion.setBranch("1");
                this.oRequisicion.setMedicamento(m);
                mTemp = this.oRequisicion.obtenerDatosFaltantes();
                if (mTemp != null) {
                    this.datosFaltantesObtenidos = true;
                    m.setCostoUnitario(mTemp.getCostoUnitario());
                    m.setLaboratorio(mTemp.getLaboratorio());
                    m.setExistencia(mTemp.getExistencia());
                } else {
                    this.datosFaltantesObtenidos = false;
                    FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, this.oRequisicion.getMensaje(), "");
                    FacesContext.getCurrentInstance().addMessage(null, msj2);
                    return;
                }
                this.listaPedido.add(m);
            } else if (iv.getMedVitrina() != null) {
                Medicamento m = new Medicamento();
                Medicamento mTemp;
                m.setCveMedicamento(iv.getMedVitrina().getMedicamento().getCveMedicamento());
                m.setLaboratorio(iv.getMedVitrina().getMedicamento().getLaboratorio());
                m.setNomMedicamento(iv.getMedVitrina().getMedicamento().getNomMedicamento());
                m.setSustanciaActiva(iv.getMedVitrina().getMedicamento().getSustanciaActiva());
                m.setCantidadActualizada(iv.getMedVitrina().getCantidad());
                m.setUnidadMedida(iv.getMedVitrina().getMedicamento().getUnidadMedida());
                m.setPedidoExterno(false);
                this.oRequisicion.setBranch("1");
                this.oRequisicion.setMedicamento(m);
                mTemp = this.oRequisicion.obtenerDatosFaltantes();
                if (mTemp != null) {
                    this.datosFaltantesObtenidos = true;
                    m.setCostoUnitario(mTemp.getCostoUnitario());
                    m.setLaboratorio(mTemp.getLaboratorio());
                    m.setExistencia(mTemp.getExistencia());
                } else {
                    this.datosFaltantesObtenidos = false;
                    FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, this.oRequisicion.getMensaje(), "");
                    FacesContext.getCurrentInstance().addMessage(null, msj2);
                    return;
                }
                this.listaPedido.add(m);
            }
        }
        LOG.trace(this.lista);
        LOG.trace(this.listaPedido);
        actualizaTotal();

        LOG.trace("Codigo de farmacia a pedir: " + this.farmaciaAPedir);
        if (this.farmaciaAPedir == 1) {
            this.oRequisicion = new Farmacia();
            this.oRequisicion.setBranch("1");
            this.oRequisicion.setPaciente(this.oPaciente);
            this.oRequisicion.setHabitacion(this.nHabitacion);//Cargar valor
            this.oRequisicion.setPedido(this.listaPedido);
            LOG.trace(new StringBuilder().append("Tamaño de pedido:").append(this.listaPedido.size()).toString());
            this.oRequisicion.registrarPedido();
            FacesMessage msj = new FacesMessage((this.oRequisicion.getCveRequisicion() != -1) ? FacesMessage.SEVERITY_INFO : FacesMessage.SEVERITY_ERROR, this.oRequisicion.getMensaje(), "");
            FacesContext.getCurrentInstance().addMessage(null, msj);
            if (this.oRequisicion.getCveRequisicion() == -1) {
                FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Pedido: Ha ocurrido un error con el sistema de farmacia, se realizara el pedido como farmacia externa", "");
                FacesContext.getCurrentInstance().addMessage(null, msj2);
                this.nCveReq = -999;
            } else {
                this.nCveReq = this.oRequisicion.getCveRequisicion();
            }
        }
        if (this.nCveReq != 0) {
            this.agregados = insertarServicioPrestado();
        }
        String messageSP = (this.agregados) ? "Se ha realizado correctamente el cargo al paciente " + this.oPaciente.getNombreCompleto() : "No se ha realizado el cargo correctamente";
        FacesMessage msj2 = new FacesMessage((this.agregados) ? FacesMessage.SEVERITY_INFO : FacesMessage.SEVERITY_ERROR, messageSP, "");
        FacesContext.getCurrentInstance().addMessage(null, msj2);
    }

    public boolean insertarServicioPrestado() throws Exception {
        boolean validacion = true;
        this.folios = new ArrayList<String>();
        if (this.tipoPaciente.equals("Particular")) {
            this.nQuienPaga = 0;
        } else if (this.tipoPaciente.equals("Empresa")) {
            this.nQuienPaga = 1;
        }
        for (int i = 0; i < this.listaPedido.size(); i++) {
            ServicioPrestado oServicioPrestado = new ServicioPrestado();
            oServicioPrestado.setPaciente(this.oPaciente);
            oServicioPrestado.setFacturable(this.quererFactura);
            oServicioPrestado.setRegistro(new Date());
            oServicioPrestado.setCostoOriginal(this.listaPedido.get(i).getCostoUnitario());
            oServicioPrestado.setCostoCobrado(this.listaPedido.get(i).getCostoUnitario());
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
            LOG.trace("Material curacion encontrado: " + mc.buscaMaterialCuracion(this.listaPedido.get(i).getCveMedicamento()));
            LOG.trace("Medicamento encontrado: " + m.buscaMedicamentoVitrina(this.listaPedido.get(i).getCveMedicamento()));
            mc = mc.buscaMaterialCuracion(this.listaPedido.get(i).getCveMedicamento());
            m = m.buscaMedicamentoVitrina(this.listaPedido.get(i).getCveMedicamento());
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
            oServicioPrestado.setCantidad(this.listaPedido.get(i).getCantidadActualizada());
            oServicioPrestado.setUniMed(this.listaPedido.get(i).getUnidadMedida());
            cveMedMat = this.listaPedido.get(i).getCveMedicamento();//Aplica tanto para MaterialCuracion como para Medicamento

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
            String msj2 = oServicioPrestado.actualizarRequesicion(nCveReq);
            if (msj2.indexOf("ERROR") > 0) {
                FacesMessage msj3 = new FacesMessage(msj2, "");
                FacesContext.getCurrentInstance().addMessage(null, msj3);
                validacion = false;
                break;

            }
        }
        return validacion;
    }

    public void actualizaTotal() {
        float tmpTotal = 0;
        for (int i = 0; i < this.listaPedido.size(); i++) {
            tmpTotal += this.listaPedido.get(i).getCantidadActualizada() * this.listaPedido.get(i).getCostoUnitario();
        }
        this.nTotal = tmpTotal;
        LOG.trace(new StringBuilder().append("Total actualizado en: ").append(this.nTotal).toString());
    }

    public void pedidoFarmacia() {
        if (this.agregados && this.nCveReq != 0) {
            FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_INFO, this.oRequisicion.getMensaje(), "");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
            RequestContext.getCurrentInstance().execute("dlg1.hide();dlg2.show()");
        } else {
            FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, this.oRequisicion.getMensaje(), "");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
        }
    }

    public boolean buscarSeleccionadoEnPedido(String m) {
        for (InventarioVitrina iv : this.lista) {
            if (iv.getMatVitrina() != null) {
                if (iv.getMatVitrina().getMaterialCuracion().getCveMaterial().equals(m)) {
                    return true;
                }
            } else if (iv.getMedVitrina() != null) {
                if (iv.getMedVitrina().getMedicamento().getCveMedicamento().equals(m)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void salir() throws IOException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.getExternalContext().redirect("../login/menuOpe.xhtml");
    }

    public Paciente getPaciente() {
        return oPaciente;
    }

    public void setPaciente(Paciente paciente) {
        this.oPaciente = paciente;
    }

    public int getCveVitrina() {
        return nCveVitrina;
    }

    public void setCveVitrina(int nCveVitrina) throws Exception {
        LOG.trace("Dentro de setCveVtrina con valor=" + nCveVitrina);
        this.nCveVitrina = nCveVitrina;
        if (nCveVitrina != 0) {

            Vitrina vitrina = new Vitrina();
            vitrina.setCveVitrina(Short.valueOf(new StringBuilder().append(nCveVitrina).toString()));

            this.oMatVitrina = new InvMatVitrina();
            this.oMatVitrina.setVitrina(vitrina);
            this.materialesVitrina = this.oMatVitrina.buscaTodosPorVitrina();

            this.oMedVitrina = new InvMedVitrina();
            this.oMedVitrina.setVitrina(vitrina);
            this.medicamentosVitrina = this.oMedVitrina.buscaTodosPorVitrina();

            LOG.trace("Medic=" + this.medicamentosVitrina.length + " materiales=" + this.materialesVitrina.length);
        } else {
            this.materialesVitrina = null;
            this.medicamentosVitrina = null;
        }
    }

    public Vitrina[] getVitrinas() {
        return vitrinas;
    }

    public void setVitrinas(Vitrina[] vitrinas) {
        this.vitrinas = vitrinas;
    }

    public Date getFecha() {
        return dFecha;
    }

    public void setFecha(Date fecha) {
        this.dFecha = fecha;
    }

    public int getCantidad() {
        return nCantidad;
    }

    public void setCantidad(int cantidad) {
        this.nCantidad = cantidad;
    }

    public List<Medicamento> getListaPedido() {
        return listaPedido;
    }

    public void setListaPedido(List<Medicamento> listaPedido) {
        this.listaPedido = listaPedido;
    }

    public String getCveSeleccionMaterial() {
        return nCveSeleccionMaterial;
    }

    public void setCveSeleccionMaterial(String nCveSeleccionMaterial) {
        this.nCveSeleccionMaterial = nCveSeleccionMaterial;
    }

    public String getCveSeleccionMedicamento() {
        return nCveSeleccionMedicamento;
    }

    public void setCveSeleccionMedicamento(String nCveSeleccionMedicamento) {
        this.nCveSeleccionMedicamento = nCveSeleccionMedicamento;
    }

    public int getCveTipoSeleccion() {
        return nCveTipoSeleccion;
    }

    public void setCveTipoSeleccion(int cveTipoSeleccion) {
        this.nCveTipoSeleccion = cveTipoSeleccion;
    }

    public Farmacia getRequisicion() {
        return oRequisicion;
    }

    public void setRequisicion(Farmacia requisicion) {
        this.oRequisicion = requisicion;
    }

    public List<InventarioVitrina> getLista() {
        return lista;
    }

    public void setLista(List<InventarioVitrina> lista) {
        this.lista = lista;
    }

    public Vitrina getVitrina() {
        return oVitrina;
    }

    public void setVitrina(Vitrina oVitrina) {
        this.oVitrina = oVitrina;
    }

    public InvMatVitrina[] getMaterialesVitrina() {
        return materialesVitrina;
    }

    public void setMaterialesVitrina(InvMatVitrina[] materialesVitrina) {
        this.materialesVitrina = materialesVitrina;
    }

    public InvMedVitrina[] getMedicamentosVitrina() {
        return medicamentosVitrina;
    }

    public void setMedicamentosVitrina(InvMedVitrina[] medicamentosVitrina) {
        this.medicamentosVitrina = medicamentosVitrina;
    }

    public InvMatVitrina getMatVitrina() {
        return oMatVitrina;
    }

    public void setMatVitrina(InvMatVitrina oMatVitrina) {
        this.oMatVitrina = oMatVitrina;
    }

    public InvMedVitrina getMedVitrina() {
        return oMedVitrina;
    }

    public void setMedVitrina(InvMedVitrina oMedVitrina) {
        this.oMedVitrina = oMedVitrina;
    }

    public InventarioVitrina getInvVitrina() {
        return oInvVitrina;
    }

    public void setInvVitrina(InventarioVitrina oInvVitrina) {
        this.oInvVitrina = oInvVitrina;
    }

    public int getLimiteExistencia() {
        return limiteExistencia;
    }

    public void setLimiteExistencia(int limiteExistencia) {
        this.limiteExistencia = limiteExistencia;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public int getHabitacion() {
        return nHabitacion;
    }

    public void setHabitacion(int nHabitacion) {
        this.nHabitacion = nHabitacion;
    }

    public String getEmpresa() {
        return sEmpresa;
    }

    public void setEmpresa(String sEmpresa) {
        this.sEmpresa = sEmpresa;
    }

    public String getCveCliente() {
        return sCveCliente;
    }

    public void setCveCliente(String sCveCliente) {
        this.sCveCliente = sCveCliente;
    }

    public float getTotal() {
        return nTotal;
    }

    public void setTotal(float nTotal) {
        this.nTotal = nTotal;
    }

    public EpisodioMedico getEpisodioMedico() {
        return oEpisodioMedico;
    }

    public void setEpisodioMedico(EpisodioMedico oEpisodioMedico) {
        this.oEpisodioMedico = oEpisodioMedico;
    }

    public AreaFisica getAreaFisica() {
        return oAreaFisica;
    }

    public void setAreaFisica(AreaFisica oAreaFisica) {
        this.oAreaFisica = oAreaFisica;
    }

    public AreaFisica[] getAreasFisicas() {
        return areasFisicas;
    }

    public void setAreasFisicas(AreaFisica[] areasFisicas) {
        this.areasFisicas = areasFisicas;
    }

    /*
     * Para registrar en ServicioPrestado
     */
    public int getFarmaciaAPedir() {
        return farmaciaAPedir;
    }

    public void setFarmaciaAPedir(int farmaciaAPedir) {
        this.farmaciaAPedir = farmaciaAPedir;
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

    public boolean getDatosFaltantesObtenidos() {
        return datosFaltantesObtenidos;
    }

    public void setDatosFaltantesObtenidos(boolean datosFaltantesObtenidos) {
        this.datosFaltantesObtenidos = datosFaltantesObtenidos;
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

    @Override
    public String toString() {
        return "GeneraValereposicionJB{" + "oPaciente=" + oPaciente + ", nCveVitrina=" + nCveVitrina + ", oVitrina=" + oVitrina + ", vitrinas=" + vitrinas + ", dFecha=" + dFecha + ", nCantidad=" + nCantidad + ", listaPedido=" + listaPedido + ", lista=" + lista + ", oInvVitrina=" + oInvVitrina + ", oMatVitrina=" + oMatVitrina + ", materialesVitrina=" + materialesVitrina + ", oMedVitrina=" + oMedVitrina + ", medicamentosVitrina=" + medicamentosVitrina + ", nCveSeleccionMaterial=" + nCveSeleccionMaterial + ", nCveSeleccionMedicamento=" + nCveSeleccionMedicamento + ", nCveTipoSeleccion=" + nCveTipoSeleccion + ", oRequisicion=" + oRequisicion + ", oAreaFisica=" + oAreaFisica + ", areasFisicas=" + areasFisicas + ", fechaVencimiento=" + fechaVencimiento + ", limiteExistencia=" + limiteExistencia + ", nTotal=" + nTotal + ", nHabitacion=" + nHabitacion + ", sEmpresa=" + sEmpresa + ", sCveCliente=" + sCveCliente + ", oEpisodioMedico=" + oEpisodioMedico + ", farmaciaAPedir=" + farmaciaAPedir + ", tiposPacientes=" + tiposPacientes + ", quererFactura=" + quererFactura + ", tipoPaciente=" + tipoPaciente + ", listaUnidades=" + listaUnidades + ", nQuienPaga=" + nQuienPaga + ", bConEpisodio=" + bConEpisodio + ", oMedico=" + oMedico + ", oUnidadMedida=" + oUnidadMedida + ", nuevoCosto=" + nuevoCosto + ", folios=" + folios + ", nCveReq=" + nCveReq + ", agregados=" + agregados + '}';
    }
}
