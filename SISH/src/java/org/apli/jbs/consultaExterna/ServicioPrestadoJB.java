package org.apli.jbs.consultaExterna;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;
import org.apli.jbs.AreaServicioJB;
import org.apli.jbs.LineaIngresoJB;
import org.apli.jbs.PacienteJB;
import org.apli.jbs.UnidadMedidaJB;
import org.apli.jbs.UsuarioJB;
import org.apli.jbs.utilidades.DocsPDFJB;
import org.apli.modelbeans.AreaDeServicio;
import org.apli.modelbeans.ConceptoIngreso;
import org.apli.modelbeans.DetallePaquete;
import org.apli.modelbeans.EpisodioMedico;
import org.apli.modelbeans.Farmacia;
import org.apli.modelbeans.LineaIngreso;
import org.apli.modelbeans.Medicamento;
import org.apli.modelbeans.Medico;
import org.apli.modelbeans.OtrosDsctosPaq;
import org.apli.modelbeans.Paciente;
import org.apli.modelbeans.PaqueteContratado;
import org.apli.modelbeans.PersonalHospitalario;
import org.apli.modelbeans.ServicioPrestado;
import org.apli.modelbeans.TipoPrincipalPaga;
import org.apli.modelbeans.UnidadMedida;
import org.apli.tickets.models.Ticket;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CellEditEvent;

/**
 *
 * @author JRuiz
 */
@ManagedBean(name = "oServcPrest2")
@ViewScoped
public class ServicioPrestadoJB implements Serializable, Converter {

    private int nCantidad = 1;
    private Paciente paciente;
    private List<TipoPrincipalPaga> tiposPacientes;
    private List<ConceptoIngreso> servicios = new ArrayList<ConceptoIngreso>();
    private List<ConceptoIngreso> carritoServicios = new 
            ArrayList<ConceptoIngreso>();
    private AreaDeServicio areaDeServicio;
    private LineaIngreso lineaIngreso=new LineaIngreso();
    private float totalCarrito = 0;
    private float totalCarrito2 = 0;
    private String servicio;
    private String observaciones = "NA";
    private ConceptoIngreso oServicioSeleccionado;
    private boolean registroExitoso = false;
    private boolean quererFactura;
    private boolean bConEpisodio = false;
    private boolean bValListMed = true;
    private ServicioPrestadoJB oServicioPrestadoJB2;
    private Medico oMedico;
    private String tipoPaciente = "";
    private int nQuienPaga;
    private String quienRealizaOrden;
    private List<AreaDeServicio> areasDeServicio;
    private String sArea;
    private List<String> folios;
    private List<ServicioPrestado> arrServicios;
    private EpisodioMedico oEpisodioMedico;
    private List<UnidadMedida> listaUnidades;
    private UnidadMedida oUnidadMedida;
    private boolean bPaquete;
    private PaqueteContratado oPaqueteContratado = null;
    private List<DetallePaquete> carritoServiciosPaq = new 
            ArrayList<DetallePaquete>();
    private List<ConceptoIngreso> carritoServiciosAConsumir = new 
            ArrayList<ConceptoIngreso>();
    private List<ConceptoIngreso> serviciosAConsumir = new 
            ArrayList<ConceptoIngreso>();
    private List<OtrosDsctosPaq> carritoDescuentos = new 
            ArrayList<OtrosDsctosPaq>();
    private List<Medico> medicosInterconsulta = new ArrayList<Medico>();
    private List<Medico> medicosTodos = new ArrayList<Medico>();
    private Medico oMedicoHonorarios;
    private Date fechaInterconsulta;
    private String rutaPDF;
    private boolean interconsulta = false;
    private boolean campoInterconsulta = false;
    private boolean siena = false;
    private List<ServicioPrestado> listServsConsms = new 
            ArrayList<ServicioPrestado>();
    private boolean facturaRegistrada = false;
    private boolean alertaPagoPaqueteMat = false;
    private float nMontoPagadoPaq;
    private Medico oMedSolicita;
    private boolean bPaginado = false;
    private int nRenglones = 0;
    private int nNumOrden = 0;
    private boolean bTieneEmpresa = false;
    private String sEmpresa = "";
    private ArrayList<LineaIngreso> lineasIngreso = null;
    private int nCveLin=0;
    private boolean bCambiaTipoPac=true;

    public void llena() throws Exception {
        limpiaServicios();
        String sPaginaLlamada = FacesContext.getCurrentInstance(
                ).getViewRoot().getViewId();
        if (sPaginaLlamada.equals("/consultaExterna/generarOrdenServicio.xhtml")) {
            sArea = "Orden servicio";
        }
        if (sPaginaLlamada.equals("/cargoCuentas/registrarCargoCuentas.xhtml")) {
            sArea = "Cargo Cuentas";
        }
        paciente = new PacienteJB().getPacienteSesion();
        if (paciente != null) {
            if (paciente.getFallecido() != true) {
                tiposPacientes = new ArrayList();
                oEpisodioMedico = new EpisodioMedico();
                oEpisodioMedico.buscaDiagnosticoActual(paciente.getFolioPac());
                oEpisodioMedico.tieneEpisodioMedicoPaciente(paciente.getFolioPac());
                carritoServiciosPaq = new ArrayList<DetallePaquete>();
                carritoDescuentos = new ArrayList<OtrosDsctosPaq>();
                if (oEpisodioMedico.getCveepisodio() != 0) {
                    facturaRegistrada = true;
                    quererFactura = oEpisodioMedico.esFacturableEpisodio();
                    listServsConsms = new ServicioPrestado().buscaTodosServiciosConsumidosPaquete(oEpisodioMedico.getCveepisodio());
                    this.nQuienPaga = oEpisodioMedico.getTipoPrincipal();
                    this.bTieneEmpresa = false;
                    if (this.nQuienPaga == TipoPrincipalPaga.TIPO_PART) {
                        TipoPrincipalPaga x = new TipoPrincipalPaga();
                        x.setTipo(nQuienPaga);
                        x.buscar();
                        this.tipoPaciente = nQuienPaga + "";//x.getDescripcion();
                    } else if (this.nQuienPaga == TipoPrincipalPaga.TIPO_EMP) {
                        bTieneEmpresa = true;
                        oEpisodioMedico.setArrCiaCred(
                                  oEpisodioMedico.buscaCompaniasCredEpisodioMed(
                                            paciente.getFolioPac()));
                        if (oEpisodioMedico.getArrCiaCred() != null
                                  && oEpisodioMedico.getArrCiaCred().length > 0) {
                            this.sEmpresa = oEpisodioMedico.getArrCiaCred()[0].getNombreCorto();
                        }
                    }

                }
                if (paciente.getNombre().contains("RN")) {
                    oPaqueteContratado = paciente.buscaPacienteConPaqueteContratadoHeredado();
                } else {
                    oPaqueteContratado = paciente.buscaPacienteConPaqueteContratado();
                }
                if (oPaqueteContratado != null) {
                    DetallePaquete oDP = new DetallePaquete();
                    oDP.setPaquete(oPaqueteContratado.getPaquete());
                    this.carritoServiciosPaq = oDP.buscaTodosDetallesPaquete();
                    OtrosDsctosPaq oODP = new OtrosDsctosPaq();
                    oODP.setPaquete(oPaqueteContratado.getPaquete());
                    carritoDescuentos = oODP.buscaTodosDsctosPaq();
                    actualizaTotal2();
                    bPaquete = true;
                    this.nQuienPaga = TipoPrincipalPaga.TIPO_PAQ;
                    TipoPrincipalPaga x = new TipoPrincipalPaga();
                    x.setTipo(nQuienPaga);
                    x.buscar();
                    this.tipoPaciente = x.getDescripcion();
                    carritoServiciosAConsumir = new ArrayList<ConceptoIngreso>();
                    for (int i = 0; i < carritoServiciosPaq.size(); i++) {
                        boolean encontrado = false;
                        int cantidadDisp = carritoServiciosPaq.get(i).getConceptoIngreso().getCantidad();
                        for (int j = 0; j < listServsConsms.size(); j++) {
                            if (carritoServiciosPaq.get(i).getConceptoIngreso().getIdGenerico().equals(listServsConsms.get(j).getConcepPrestado().getIdGenerico())) {
                                encontrado = true;
                                cantidadDisp -= listServsConsms.get(j).getConcepPrestado().getCantidad();
                            }
                        }
                        if (encontrado == false) {
                            carritoServiciosAConsumir.add(carritoServiciosPaq.get(i).getConceptoIngreso());
                        } else {
                            if (cantidadDisp > 0) {
                                carritoServiciosPaq.get(i).getConceptoIngreso().setCantidad(cantidadDisp);
                                carritoServiciosPaq.get(i).getConceptoIngreso().setCantidadACobrar(0);
                                carritoServiciosAConsumir.add(carritoServiciosPaq.get(i).getConceptoIngreso());
                            }
                        }
                    }
                    if (oPaqueteContratado.getPaquete().getTipoPaquete().getDescrip().equals("Maternidad") && oPaqueteContratado.getProbableparto() != null) {
                        Date dFechaActual = new Date();
                        long diferenciaFechas = oPaqueteContratado.getProbableparto().getTime() - oPaqueteContratado.getRegistro().getTime() / 2;
                        if (dFechaActual.getTime() > diferenciaFechas) {
                            nMontoPagadoPaq = oPaqueteContratado.montoPagadoPorPaquete(oPaqueteContratado.getEpisodioMedico().getCveepisodio());
                            float nPorcpagado = (oPaqueteContratado.getPaquete().getCosto() * (float) 0.17);
                            if (nMontoPagadoPaq < nPorcpagado) {
                                this.alertaPagoPaqueteMat = true;
                            }
                        }
                    }
                }
                if (oEpisodioMedico.getMedTratante() != null) {
                    bConEpisodio = true;
                }
                if (bConEpisodio == true) {
                    bValListMed = false;
                }
                AreaServicioJB.areasServicios = new ArrayList<AreaDeServicio>();
                AreaDeServicio oAreaServicio = new AreaDeServicio();
                if (sArea.equals("Orden servicio")) {
                    AreaServicioJB.areasServicios = oAreaServicio.todasAreasServicios(AreaDeServicio.AreaServicioPrestado);
                    areasDeServicio = AreaServicioJB.areasServicios;
                    llenaListas();
                    if (!carritoServiciosAConsumir.isEmpty()) {
                        Iterator<ConceptoIngreso> iter = carritoServiciosAConsumir.iterator();
                        while (iter.hasNext()) {
                            String clave = iter.next().getAreaServicio().getCve();
                            if (clave.equals("4") || clave.equals("5")
                                      || clave.equals("13") || clave.equals("14")
                                      || clave.equals("15") || clave.equals("16")) {
                                iter.remove();
                            }
                        }
                    }
                }
                if (sArea.equals("Cargo Cuentas")) {
                    bConEpisodio = false;
                    oEpisodioMedico = new EpisodioMedico();
                    oEpisodioMedico.tieneEpisodioMedicoPacienteHospitalizado(paciente.getFolioPac());
                    if (oEpisodioMedico.getMedTratante().getFolioPers() != 0) {
                        bConEpisodio = true;
                    }
                    AreaServicioJB.areasServicios = oAreaServicio.todasAreasServicios(AreaDeServicio.AreaServicioCargoCuenta);
                    areasDeServicio = AreaServicioJB.areasServicios;
                    if (bConEpisodio == true) {
                        llenaListas();
                        if (!carritoServiciosAConsumir.isEmpty()) {
                            Iterator<ConceptoIngreso> iter = carritoServiciosAConsumir.iterator();
                            while (iter.hasNext()) {
                                String clave = iter.next().getAreaServicio().getCve();
                            }
                        }
                    } else {
                        FacesMessage msj2 = new FacesMessage("¡Aviso!", "¡El paciente : " + paciente.getNombreCompleto() + " con folio: " + paciente.getFolioPac() + " no ha sido hospitalizado!");
                        RequestContext.getCurrentInstance().showMessageInDialog(msj2);
                    }
                }
            } else {
                FacesMessage msj2 = new FacesMessage("¡Aviso!", "¡El paciente : " + paciente.getNombreCompleto() + " con folio: " + paciente.getFolioPac() + " es fallecido!");
                RequestContext.getCurrentInstance().showMessageInDialog(msj2);
            }
        } else {
            FacesMessage msj2 = new FacesMessage("¡Aviso!", "¡No ha seleccionado un paciente!");
            RequestContext.getCurrentInstance().showMessageInDialog(msj2);
        }
    }

    public void llenaListas() throws Exception {

        tiposPacientes = (new TipoPrincipalPaga()).buscaTiposPrincipalPaga();
        //El tipo Paquete no es elegible
        tiposPacientes.remove(TipoPrincipalPaga.TIPO_PAQ);

        Medico oM = new Medico();
        this.medicosTodos = oM.buscaTodos();

        listaUnidades = new ArrayList<UnidadMedida>();
        listaUnidades = new UnidadMedida().buscatodasunidades();
        UnidadMedidaJB.unidadesMedidas = listaUnidades;

    }

    public void regresarServicio() throws IOException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.getExternalContext().redirect("generarOrdenServicio.xhtml");
    }

    public void regresarCargo() throws IOException {
        if (this.interconsulta) {
            String archivoAElimina = this.rutaPDF;
            ExternalContext extCont = FacesContext.getCurrentInstance().getExternalContext();
            File folder = new File(extCont.getRealPath("//resources//"));
            if (!archivoAElimina.equals("")) {
                File archivo = new File(folder, archivoAElimina);
                if (archivo.delete()) {
                }
            }
        }
        limpiaServicios();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.getExternalContext().redirect("registrarCargoCuentas.xhtml");
    }

    public boolean validaUsuario() {
        boolean validacion = false;
        if (paciente != null) {
            validacion = true;
        }
        return validacion;

    }

    public void actualizaListaServiciosOrdServ() throws Exception {
        servicios = new ArrayList<ConceptoIngreso>();
        ConceptoIngreso oSM = new ConceptoIngreso();
        servicios = oSM.buscaServicios(lineaIngreso,
                  paciente.getFolioPac(), this.oEpisodioMedico.getCveepisodio(), 
                  ConceptoIngreso.BUSQ_PAC,ConceptoIngreso.TIPO_CE, 
                  areaDeServicio);
        if (!servicios.isEmpty()) {
            for (int i = 0; i < servicios.size(); i++) {
                if (this.nQuienPaga == TipoPrincipalPaga.TIPO_PART){
                    if (servicios.get(i).getDescripcion().contains("EMPRESA"))
                        servicios.remove(i); //si es Particular no ve conceptos para empresas
                }
                else if (this.nQuienPaga == TipoPrincipalPaga.TIPO_DSCTO) {
                    servicios.get(i).setMonto(servicios.get(i).getMontoNuevo());
                } else {
                    servicios.get(i).setMontoNuevo(servicios.get(i).getMonto());
                }
            }
        }
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().
                  getSession(false);
        session.setAttribute("listaServicios", servicios);
    }

    public void actualizaListaLineasOrdServ() {
    LineaIngreso oLI = new LineaIngreso();
    String sTipoDiferente = "";
    ArrayList<LineaIngreso> arrLocal=new ArrayList<LineaIngreso>();
        try {
            lineasIngreso = new ArrayList<LineaIngreso>();
            arrLocal = (ArrayList)oLI.todasLineasIng(
                    areaDeServicio.getCve(), ConceptoIngreso.TIPO_CE);
            if (this.paciente.getTipo().contains(Paciente.TIPO_PNSH)) {
                sTipoDiferente = " PSH";
            } else {
                sTipoDiferente = " PNSH";
            }
            for (int i = 0; i < arrLocal.size(); i++) {
                if (!(
                        //Para PACIENTES VARIOS sólo considerar línea VARIOS
                        (this.paciente.getNombreCompleto().contains(
                        Paciente.DESC_PARCIAL_VARIOS)&&
                        !arrLocal.get(i).getDescrip().contains(
                        Paciente.DESC_PARCIAL_VARIOS)
                        ) 
                        || //si es PSH no incluir PNSH y viceversa
                        (arrLocal.get(i).getDescrip().contains(sTipoDiferente))
                    )){
                    System.out.println(i + " "+ arrLocal.get(i).getDescrip());
                    lineasIngreso.add(arrLocal.get(i));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actualizaListaServiciosCargCnt() throws Exception {
        campoInterconsulta = false;
        servicios = new ArrayList<ConceptoIngreso>();
        ConceptoIngreso oSM = new ConceptoIngreso();
        if (areaDeServicio!=null && lineaIngreso!=null){
            servicios = oSM.buscaServicios(lineaIngreso,
                  paciente.getFolioPac(), this.oEpisodioMedico.getCveepisodio(), 
                  ConceptoIngreso.BUSQ_PAC,ConceptoIngreso.TIPO_CTA_HOSP,
                  areaDeServicio);
            System.out.println(servicios);
            if (!servicios.isEmpty()) {
                for (int i = 0; i < servicios.size(); i++) {
                    if (servicios.get(i).getDescripcion().contains("HABITACION")) {
                        servicios.get(i).setDisponibilidadMax(1);
                    }
                    if (this.nQuienPaga == TipoPrincipalPaga.TIPO_DSCTO) {
                        servicios.get(i).setMonto(servicios.get(i).getMontoNuevo());
                    } else {
                        servicios.get(i).setMontoNuevo(servicios.get(i).getMonto());
                    }
                }
            }
        if (lineaIngreso.getCveLin() >= 16 && lineaIngreso.getCveLin() <= 21) {
            campoInterconsulta = true;
            medicosInterconsulta = new ArrayList<Medico>();
            for (int i = 0; i < this.medicosTodos.size(); i++) {
                if (lineaIngreso.getDescrip().contains("MEDSH")
                          && this.medicosTodos.get(i).getTipo().contains("SH")
                          || lineaIngreso.getDescrip().contains("MEDICOS SH")
                          && this.medicosTodos.get(i).getTipo().contains("SH")) {
                    this.medicosInterconsulta.add(this.medicosTodos.get(i));
                }
                if (!lineaIngreso.getDescrip().contains("MEDNSH")
                          && this.medicosTodos.get(i).getTipo().contains("NSH")
                          || !lineaIngreso.getDescrip().contains("MEDICOS SH")
                          && this.medicosTodos.get(i).getTipo().contains("NSH")) {
                    this.medicosInterconsulta.add(this.medicosTodos.get(i));
                }
            }
        }
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().
                  getSession(false);
        session.setAttribute("listaServicios", servicios);
        }
    }

    public void actualizaListaLineasCargCnt() {
        lineasIngreso = new ArrayList<LineaIngreso>();
        LineaIngreso oLI = new LineaIngreso();

        try {
            lineasIngreso = (ArrayList)oLI.todasLineasIng(
                    areaDeServicio.getCve(), ConceptoIngreso.TIPO_CTA_HOSP);
            String sTipoDiferente = "";
            if (this.paciente.getTipo().equals(Paciente.TIPO_PNSH)) {
                sTipoDiferente = " PSH";
            } else {
                sTipoDiferente = " PNSH";
            }
            for (int i = 0; i < lineasIngreso.size(); i++) {
                if (lineasIngreso.get(i).getDescrip().contains(sTipoDiferente)) {
                    lineasIngreso.remove(i);
                }
            }
            /*for (int i = 0; i < LineaIngresoJB.lineasIngreso.size(); i++) {
                if (LineaIngresoJB.lineasIngreso.get(i).getDescrip().contains("HONORARIOS")) {
                    LineaIngresoJB.lineasIngreso.remove(i);
                }
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void limpiaServicios() {
        carritoServicios = new ArrayList<ConceptoIngreso>();
        totalCarrito = 0;
        areaDeServicio = null;
        lineaIngreso = new LineaIngreso();
        oServicioSeleccionado = null;
        servicios = new ArrayList<ConceptoIngreso>();
        lineasIngreso = new ArrayList<LineaIngreso>();
        observaciones = "";
        tipoPaciente = "";
        oMedico = new Medico();
        paciente = null;
        nQuienPaga = 0;
        quererFactura = false;
        listaUnidades = new ArrayList<UnidadMedida>();
        bConEpisodio = false;
        bValListMed = true;
        bPaquete = false;
        oPaqueteContratado = null;
        carritoServiciosPaq = new ArrayList<DetallePaquete>();
        carritoDescuentos = new ArrayList<OtrosDsctosPaq>();
        oMedicoHonorarios = null;
        fechaInterconsulta = null;
        rutaPDF = "";
        interconsulta = false;
        siena = false;
        nCantidad = 1;
        campoInterconsulta = false;
        carritoServiciosAConsumir = new ArrayList<ConceptoIngreso>();
        serviciosAConsumir = new ArrayList<ConceptoIngreso>();
        alertaPagoPaqueteMat = false;
        oMedSolicita = new Medico();
    }

    public void agregaServicio() {
        if (areaDeServicio == null) {
            String rst = "Área : Error de validación: se necesita un valor.";
            FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
        } else if (lineaIngreso == null || lineaIngreso.getCveLin()<1) {
            String rst = "Linea ingreso : Error de validación: se necesita un valor.";
            FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
        } else if (this.oServicioSeleccionado == null) {
            String rst = "Servicio  : Error de validación: se necesita un valor.";
            FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
        } else if (oUnidadMedida == null) {
            String rst = "Unidad de Medida  : Error de validación: se necesita un valor.";
            FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
        } else if (this.oServicioSeleccionado.getLineaIngreso().getCveLin() >= 16
                  && this.oServicioSeleccionado.getLineaIngreso().getCveLin() <= 21
                  && fechaInterconsulta == null) {
            String rst = "Fecha de Interconsulta : Error de validación: para la linea seleccionada se necesita se necesita un valor.";
            FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
        } else if (this.oServicioSeleccionado.getLineaIngreso().getCveLin() >= 16
                  && this.oServicioSeleccionado.getLineaIngreso().getCveLin() <= 21
                  && oMedicoHonorarios == null) {
            String rst = "Médico de Interconsulta : Error de validación: para la linea seleccionada se necesita un valor.";
            FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
        } else if (tipoPaciente.equals("-1")) {
            String rst = "Tipo Paciente : Error de validación: se necesita un valor.";
            FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
        } else {
            boolean encontrado = false;
            for (int i = 0; i < carritoServicios.size(); i++) {
                if (carritoServicios.get(i).getIdGenerico().equals(
                          this.oServicioSeleccionado.getIdGenerico())) {
                    encontrado = true;
                }
            }
            if (encontrado == false) {
                this.oServicioSeleccionado.setTipoPrincipalPaga(
                          Integer.parseInt(this.tipoPaciente));
                if (this.oServicioSeleccionado.getTipoConcIngr().equals(
                          ConceptoIngreso.COMPORT_MED)
                          || this.oServicioSeleccionado.getTipoConcIngr().equals(
                             ConceptoIngreso.COMPORT_MAT)) {
                    if (this.siena) {
                        this.oServicioSeleccionado.setSiena(true);
                        this.oServicioSeleccionado.setAreaServicio(areaDeServicio);
                        this.oServicioSeleccionado.setUnidadMedida(oUnidadMedida);
                        this.oServicioSeleccionado.setCantidad(nCantidad);
                        this.oServicioSeleccionado.setCantidadACobrar(nCantidad);
                        esServicioPaquete();
                        aplicaDescuento();

                        if (this.oServicioSeleccionado.validaExistenciaMax()) {
                            if (this.oServicioSeleccionado.getDisponibilidadMax() <= nCantidad) {
                                String rst = oServicioSeleccionado.getDescripcion() + " : Solo cuenta con " + oServicioSeleccionado.getDisponibilidadMax() + " disponibles.";
                                FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
                                FacesContext.getCurrentInstance().addMessage(null, msj2);
                            }
                        } else {
                            String rst = oServicioSeleccionado.getDescripcion() + " : Sin existencia.";
                            FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
                            FacesContext.getCurrentInstance().addMessage(null, msj2);
                        }
                        this.bCambiaTipoPac = false;
                        carritoServicios.add(this.oServicioSeleccionado);
                    } else {
                        this.oServicioSeleccionado.setSiena(false);
                        this.oServicioSeleccionado.setAreaServicio(areaDeServicio);
                        this.oServicioSeleccionado.setUnidadMedida(oUnidadMedida);
                        this.oServicioSeleccionado.setCantidad(nCantidad);
                        this.oServicioSeleccionado.setCantidadACobrar(nCantidad);
                        esServicioPaquete();
                        aplicaDescuento();
                        carritoServicios.add(this.oServicioSeleccionado);
                    }
                } else {
                    this.oServicioSeleccionado.setAreaServicio(areaDeServicio);
                    if (this.oServicioSeleccionado.getLineaIngreso().getCveLin() >= 16
                              && this.oServicioSeleccionado.getLineaIngreso().getCveLin() <= 21) {
                        this.oServicioSeleccionado.getConServ().setPagoHono(true);
                        this.oServicioSeleccionado.getConServ().setMedicoHonorarios(oMedicoHonorarios);
                        this.oServicioSeleccionado.getConServ().setFechaInterconsulta(fechaInterconsulta);
                    }
                    this.oServicioSeleccionado.setUnidadMedida(oUnidadMedida);
                    this.oServicioSeleccionado.setCantidad(nCantidad);
                    this.oServicioSeleccionado.setCantidadACobrar(nCantidad);
                    esServicioPaquete();
                    aplicaDescuento();
                    //Se ve el checkbox "Indicado por médico externo"
                    //si se trata de un paciente PNSH y la línea de ingreso
                    //contiene EXT
                    if (this.paciente.getTipo().equals(Paciente.TIPO_PNSH) &&
                        this.oServicioSeleccionado.getLineaIngreso().getDescrip().contains("EXT")){
                        this.oServicioSeleccionado.setVerIndicadoPorMed("visible");
                    }else{
                        this.oServicioSeleccionado.setVerIndicadoPorMed("hidden");
                    }
                    carritoServicios.add(this.oServicioSeleccionado);
                    this.oMedicoHonorarios = null;
                }
                areaDeServicio = null;
                lineaIngreso = new LineaIngreso();
                this.oServicioSeleccionado = null;
                oUnidadMedida = null;
                nCantidad = 1;
            }
            actualizaTotal();
        }

    }

    private void esServicioPaquete() {
        boolean esPaquete = false;
        for (int i = 0; i < carritoServiciosPaq.size(); i++) {
            if (carritoServiciosPaq.get(i).getConceptoIngreso().getIdGenerico().
                      equals(this.oServicioSeleccionado.getIdGenerico())) {
                validaServicioConsumido(carritoServiciosPaq.get(i).getConceptoIngreso().getCantidad());
            }
        }
    }

    private void validaServicioConsumido(int cantidadServPaquete) {
        int nConsumidos = 0;
        int nPorcobrar = 0;
        int nPorConsumir = 0;
        int nPermitidos = cantidadServPaquete;
        for (int j = 0; j < listServsConsms.size(); j++) {
            if (listServsConsms.get(j).getConcepPrestado().getIdGenerico().
                      equals(this.oServicioSeleccionado.getIdGenerico())) {
                nConsumidos += listServsConsms.get(j).getCantidad();
            }
        }
        if (listServsConsms.isEmpty()) {
            if (nPermitidos >= this.oServicioSeleccionado.getCantidad()) {
                this.oServicioSeleccionado.setMontoNuevo(0);
                this.oServicioSeleccionado.setCantidad(nCantidad);
                this.oServicioSeleccionado.setCantidadACobrar(nCantidad);
            } else {
                nPorConsumir = nPermitidos;
                nPorcobrar = (this.oServicioSeleccionado.getCantidad() - nPorConsumir);
                this.oServicioSeleccionado.setMontoNuevo(nPorcobrar * this.oServicioSeleccionado.getMonto());
                this.oServicioSeleccionado.setCantidad(nCantidad);
                this.oServicioSeleccionado.setCantidadACobrar(nPorcobrar);
            }
        } else {
            if (nPermitidos >= (nConsumidos + this.oServicioSeleccionado.getCantidad())) {
                this.oServicioSeleccionado.setMontoNuevo(0);
                this.oServicioSeleccionado.setCantidad(nCantidad);
                this.oServicioSeleccionado.setCantidadACobrar(nCantidad);
            } else {
                if (nConsumidos < nPermitidos) {
                    nPorConsumir = nPermitidos - nConsumidos;
                }
                nPorcobrar = (this.oServicioSeleccionado.getCantidad() - nPorConsumir);
                this.oServicioSeleccionado.setMontoNuevo(nPorcobrar * this.oServicioSeleccionado.getMonto());
                this.oServicioSeleccionado.setCantidad(nCantidad);
                this.oServicioSeleccionado.setCantidadACobrar(nPorcobrar);

            }
        }

    }

    private void aplicaDescuento() {
        for (int i = 0; i < carritoDescuentos.size(); i++) {
            if (carritoDescuentos.get(i).getLineaIngreso().getCveLin()
                      == this.oServicioSeleccionado.getLineaIngreso().getCveLin()) {
                float nPorcentaje = (this.oServicioSeleccionado.getMonto() * carritoDescuentos.get(i).getPctDscto()) / 100;
                float nPrecioConDesc = this.oServicioSeleccionado.getMonto() - ((float) nPorcentaje);
                this.oServicioSeleccionado.setMontoNuevo(nPrecioConDesc);
            }
        }
    }

    public void eliminaServicioInt(int id) {
        for (int i = 0; i < carritoServicios.size(); i++) {
            if (carritoServicios.get(i).getTipoConcIngr().equals("Servicio medico") || carritoServicios.get(i).getTipoConcIngr().equals("otro ingreso")) {
                if (carritoServicios.get(i).getCveConcep() == id) {
                    if (carritoServicios.get(i).getTipoPaquete()) {
                        this.carritoServiciosAConsumir.add(carritoServicios.get(i));
                    }
                    carritoServicios.remove(i);
                    break;
                }
            }
        }

        actualizaTotal();
    }

    public void eliminaServicioString(String id) {
        for (int i = 0; i < carritoServicios.size(); i++) {

            if (carritoServicios.get(i).getTipoConcIngr().equals("medicamento")) {
                if (carritoServicios.get(i).getMedicamento().getCveMedicamento().equals(id)) {
                    if (carritoServicios.get(i).getTipoPaquete()) {
                        this.carritoServiciosAConsumir.add(carritoServicios.get(i));
                    }
                    carritoServicios.remove(i);
                    break;
                }
            }
            if (carritoServicios.get(i).getTipoConcIngr().equals("material")) {
                if (carritoServicios.get(i).getMaterialCuracion().getCveMaterial().equals(id)) {
                    if (carritoServicios.get(i).getTipoPaquete()) {
                        this.carritoServiciosAConsumir.add(carritoServicios.get(i));
                    }
                    carritoServicios.remove(i);
                    break;
                }
            }
        }
        actualizaTotal();
    }

    public void actualizaTotal() {
        this.totalCarrito = 0;
        for (int i = 0; i < carritoServicios.size(); i++) {
            this.totalCarrito += (carritoServicios.get(i).getMontoNuevo()
                      * carritoServicios.get(i).getCantidadACobrar());
        }
        System.out.println(this.totalCarrito);
    }

    public void actualizaTotal2() {
        totalCarrito2 = 0;
        for (int i = 0; i < carritoServiciosPaq.size(); i++) {
            totalCarrito2 += (carritoServiciosPaq.get(i).getConceptoIngreso().getMontoNuevo() * carritoServiciosPaq.get(i).getConceptoIngreso().getCantidad());
        }
    }

    public void insertaServicio(ActionEvent ee) {
        ServicioPrestado oServ;
        boolean validacion = true;
        boolean validacionUnidadmd = false;
        folios = new ArrayList<String>();
        arrServicios = new ArrayList<ServicioPrestado>();
        nNumOrden = 0;
        for (int i = 0; i < carritoServicios.size(); i++) {
            if (carritoServicios.get(i).getUnidadMedida() == null) {
                validacionUnidadmd = true;
            }
        }
        if (validacionUnidadmd == true) {
            String rst = "Unidad de medida : Error de validación: Todo servicio agregado deberá tener una unidad de medida.";
            FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
        } else if (tipoPaciente.equals("-1")) {
            String rst = "Tipo Paciente : Error de validación: se necesita un valor.";
            FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
        } else if (oMedico == null && oMedico.getFolioPers() < 1 && bConEpisodio == false) {
            String rst = "Médico tratante: Error de validación: se necesita un valor.";
            FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
        } else if (carritoServicios.isEmpty()) {
            String rst = "No ha agregado ningún servicio";
            FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Servicios: " + rst, "");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
        } else {
            for (int i = 0; i < carritoServicios.size(); i++) {
                ServicioPrestado oServicioPrestado = new ServicioPrestado();
                oServicioPrestado.setNumOrden(nNumOrden);
                oServicioPrestado.setPaciente(paciente);
                oServicioPrestado.setFacturable(quererFactura);
                oServicioPrestado.setRegistro(new Date());
                oServicioPrestado.setMedicoSolicita(oMedSolicita);

                if (carritoServicios.get(i).getTipoConcIngr().equals(
                          "Servicio medico")
                          || carritoServicios.get(i).getTipoConcIngr().equals(
                                    "otro ingreso")) {
                    oServicioPrestado.setCostoOriginal(carritoServicios.get(i).getMonto());
                }
                if (carritoServicios.get(i).getTipoConcIngr().equals("medicamento")) {
                    oServicioPrestado.setCostoOriginal(carritoServicios.get(i).getMedicamento().getCostoUnitario());
                }
                if (carritoServicios.get(i).getTipoConcIngr().equals("material")) {
                    oServicioPrestado.setCostoOriginal(carritoServicios.get(i).getMaterialCuracion().getFMonto());
                }

                oServicioPrestado.setCostoCobrado(carritoServicios.get(i).getMontoNuevo());
                oServicioPrestado.setQuienPaga(carritoServicios.get(i).getTipoPrincipalPaga());
                if (bConEpisodio == true) {
                    if (this.oMedico.getFolioPers() == 0) {
                        oMedico = oEpisodioMedico.getMedTratante();
                    }
                    //oMedico.setFolioPers(0);
                    oServicioPrestado.setMedico(oMedico);
                } else {
                    oServicioPrestado.setMedico(oMedico);
                }
                if (carritoServicios.get(i).getLineaIngreso().getCveLin() >= 16 && carritoServicios.get(i).getLineaIngreso().getCveLin() <= 21) {
                    oServicioPrestado.setMedico(carritoServicios.get(i).getConServ().getMedicoHonorarios());
                    oServicioPrestado.setRealizado(carritoServicios.get(i).getConServ().getFechaInterconsulta());
                    this.interconsulta = true;
                }else{
                    if (carritoServicios.get(i).isHonorarios())
                        oServicioPrestado.setRealizado(new Date());
                }
                oServicioPrestado.setObs(observaciones);
                oServicioPrestado.setSituacion("N");
                ConceptoIngreso oCI = new ConceptoIngreso();
                oCI.setCveConcep(carritoServicios.get(i).getCveConcep());
                oServicioPrestado.setConcepPrestado(oCI);
                oServicioPrestado.setIndicadoPorMedico(carritoServicios.get(i).getIndicadoPorMedico());
                oServicioPrestado.setEpisodioMedico(oEpisodioMedico);
                oServicioPrestado.setCantidad(carritoServicios.get(i).getCantidad());
                oServicioPrestado.setUniMed(carritoServicios.get(i).getUnidadMedida());
                oServicioPrestado.setRegistro(new Date());
                
                String cveMedMat = "0";
                if (carritoServicios.get(i).getTipoConcIngr().equals(
                        ConceptoIngreso.COMPORT_MED)) {
                    cveMedMat = carritoServicios.get(i).getMedicamento().getCveMedicamento();
                }
                if (carritoServicios.get(i).getTipoConcIngr().equals(
                        ConceptoIngreso.COMPORT_MAT)) {
                    cveMedMat = carritoServicios.get(i).getMaterialCuracion().getCveMaterial();
                }
                try {
                    String msj = oServicioPrestado.insertarServicioPrestado(
                              carritoServicios.get(i).getLineaIngreso().getCveLin(),
                              cveMedMat,
                              carritoServicios.get(i).getAreaServicio().getCve(),
                              carritoServicios.get(i).getTipoConcIngr());

                    if (msj.indexOf("ERROR") > 0) {
                        FacesMessage msj2 = new FacesMessage(msj, "");
                        FacesContext.getCurrentInstance().addMessage(null, msj2);
                        validacion = false;
                        break;
                    }
                    if (msj.indexOf("historiaclinica_episodiomedico_fk") > 0) {
                        FacesMessage msj2 = new FacesMessage("El paciente : " + paciente.getNombreCompleto() + " con folio: " + paciente.getFolioPac() + " no tiene historia clínica", "");
                        FacesContext.getCurrentInstance().addMessage(null, msj2);
                        validacion = false;
                        break;
                    } else {
                        carritoServicios.get(i).getConServ().setFolioServ(msj);
                        oServicioPrestado.setIdFolio(msj);
                        oServicioPrestado.buscaNumOrden();
                        nNumOrden = oServicioPrestado.getNumOrden();

                        folios.add(msj);
                        oServ = new ServicioPrestado();
                        oServ.setIdFolio(msj);
                        oServ.setNumOrden(nNumOrden);
                        oServ.setConcepPrestado(carritoServicios.get(i));
                        arrServicios.add(oServ);
                        if (sArea.equals("Cargo Cuentas")
                                  && (carritoServicios.get(i).getTipoConcIngr().equals(
                                            ConceptoIngreso.COMPORT_MED)
                                  || carritoServicios.get(i).getTipoConcIngr().equals(
                                            ConceptoIngreso.COMPORT_MAT))) {
                            int nRequisicion = -999;
                            if (carritoServicios.get(i).getSiena()) {
                                List<Medicamento> requisicion = new ArrayList<Medicamento>();
                                Medicamento oMed = new Medicamento();
                                if (carritoServicios.get(i).getTipoConcIngr(
                                        ).equals(ConceptoIngreso.COMPORT_MED)) {
                                    oMed = carritoServicios.get(i).getMedicamento();
                                    oMed.setCantidad(carritoServicios.get(i).getCantidad());
                                    requisicion.add(oMed);
                                }
                                if (carritoServicios.get(i).getTipoConcIngr(
                                        ).equals(ConceptoIngreso.COMPORT_MAT)) {
                                    oMed.setCveMedicamento(carritoServicios.get(i).getMaterialCuracion().getCveMaterial());
                                    oMed.setCostoUnitario(carritoServicios.get(i).getMaterialCuracion().getFMonto());
                                    oMed.setCantidad(carritoServicios.get(i).getCantidad());
                                    requisicion.add(oMed);
                                }
                                Farmacia oFarm = new Farmacia();
                                if (!requisicion.isEmpty()) {
                                    oFarm.setPaciente(paciente);
                                    oFarm.setPedido(requisicion);
                                    oFarm.setBranch("1");
                                    oFarm.setEmpresa("Sanatorio Huerta S.A de S.V");
                                    oFarm.setHabitacion(oEpisodioMedico.getHab().getHab());
                                    oFarm.setCveAreaFisica(carritoServicios.get(i).getAreaServicio().getCve());
                                    oFarm.registrarPedido();
                                    if (oFarm.getCveRequisicion() == -1) {
                                        nRequisicion = -999;
                                    } else {
                                        nRequisicion = oFarm.getCveRequisicion();
                                    }
                                }
                            } else {
                                nRequisicion = -999;
                            }
                            String msj2 = oServicioPrestado.actualizarRequesicion(nRequisicion);
                            if (msj2.indexOf("ERROR") > 0) {
                                FacesMessage msj3 = new FacesMessage(msj2, "");
                                FacesContext.getCurrentInstance().addMessage(null, msj3);
                                validacion = false;
                                break;
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    FacesMessage msj4 = new FacesMessage("Error al registrar");
                    FacesContext.getCurrentInstance().addMessage(null, msj4);
                    validacion = false;
                }
            }
            if (validacion == true) {
                if (interconsulta == true) {
                    List<ConceptoIngreso> serviciosInter = new ArrayList<ConceptoIngreso>();
                    for (int i = 0; i < carritoServicios.size(); i++) {
                        if (carritoServicios.get(i).getLineaIngreso().getCveLin() >= 16
                                  && carritoServicios.get(i).getLineaIngreso().getCveLin() <= 21) {
                            serviciosInter.add(carritoServicios.get(i));
                        }
                    }
                    PersonalHospitalario capturista = new PersonalHospitalario();
                    try {
                        capturista = capturista.buscaPersonalPorCveUsuario(new UsuarioJB().getSesionUsuario().getUsuario());
                        DocsPDFJB oD = new DocsPDFJB();
                        this.rutaPDF = oD.creaSolicitudInterconsulta(
                                  serviciosInter, oEpisodioMedico.getCveepisodio(),
                                  this.paciente.getNombreCompleto(),
                                  oEpisodioMedico.getHab().getHab(),
                                  oEpisodioMedico.getMedTratante().getNombreCompleto(),
                                  capturista.getNombreCompleto());
                    } catch (Exception e) {
                        e.printStackTrace();
                        FacesMessage msj3 = new FacesMessage("Error al generar folio interconsulta");
                        FacesContext.getCurrentInstance().addMessage(null, msj3);
                        validacion = false;
                    }
                }
                oServicioPrestadoJB2 = this;
                RequestContext.getCurrentInstance().execute("dlg1.show()");
            }
        }
    }

    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();

        if (newValue != null && !newValue.equals(oldValue)) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Se ha indicado la unidad de medida :" + ((UnidadMedida) newValue).getDescrip() + " ", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public List<ConceptoIngreso> obtenerServicios(String s) {
        List<ConceptoIngreso> servSelect = new ArrayList<ConceptoIngreso>();
        s = s.toUpperCase();
        try {

            if (s.trim().equals("")) {
                return new ArrayList<ConceptoIngreso>();
            }
            for (ConceptoIngreso srv : servicios) {
                if (srv.getDescripcion().contains(s)
                          || srv.getDescripcion().toUpperCase().contains(
                                    s.toLowerCase())) {
                    servSelect.add(srv);
                }
            }
            return servSelect;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {
        if (submittedValue.trim().equals("")) {
            return null;
        } else {
            try {

                int number = Integer.parseInt(submittedValue);

                for (Medico m : this.medicosInterconsulta) {
                    if (m.getFolioPers() == number) {
                        return m;
                    }
                }
            } catch (NumberFormatException exception) {
                exception.printStackTrace();
                throw new ConverterException(new FacesMessage(
                          FacesMessage.SEVERITY_ERROR,
                          "Error de conversión", "No es un médico válido"));
            }
        }

        return null;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent component, Object value) {
        if (value == null || value.equals("")) {
            return "";
        } else {
            return String.valueOf(((Medico) value).getFolioPers());
        }
    }

    public void validaServiciosACons() {
        boolean encontrado = false;
        for (int i = 0; i < serviciosAConsumir.size(); i++) {
            for (int j = 0; j < carritoServicios.size(); j++) {
                if (serviciosAConsumir.get(i).getIdGenerico().equals(carritoServicios.get(j).getIdGenerico())) {
                    encontrado = true;
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "El servicio  :" + carritoServicios.get(j).getDescripcion() + " ya fue agregado. ", "");
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                    break;
                }
            }
        }
        if (encontrado == false) {
            for (int i = 0; i < serviciosAConsumir.size(); i++) {
                for (int j = 0; j < this.carritoServiciosAConsumir.size(); j++) {
                    if (serviciosAConsumir.get(i).getIdGenerico().equals(carritoServiciosAConsumir.get(j).getIdGenerico())) {
                        serviciosAConsumir.get(i).setTipoPaquete(true);
                        carritoServiciosAConsumir.remove(j);
                    }
                }
            }
            this.carritoServicios.addAll(serviciosAConsumir);
            serviciosAConsumir.clear();
        }
    }

    public String generarTicket() throws IOException, Exception {
        String ticketJSON = "";
        if (this.nNumOrden != 0) {
            ticketJSON = new Ticket().buscarTicketOrdenDeServicio(this.nNumOrden, false);
        }
        return ticketJSON;
    }

    public void cambiaTablaParaImp(ActionEvent ae) {
        this.bPaginado = false;
        this.nRenglones = 0;
    }

    public void regresaTablaOriginal() {
        this.bPaginado = true;
        this.nRenglones = 6;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente oPaciente) {
        this.paciente = oPaciente;
    }

    public List<TipoPrincipalPaga> getTiposPacientes() {
        return tiposPacientes;
    }

    public void setTiposPacientes(List<TipoPrincipalPaga> tiposPacientes) {
        this.tiposPacientes = tiposPacientes;
    }

    public List<ConceptoIngreso> getServicios() {
        return servicios;
    }

    public List<ConceptoIngreso> getCarritoServicios() {
        return this.carritoServicios;
    }

    public void setCarritoServicios(List<ConceptoIngreso> carritoServicios) {
        this.carritoServicios = carritoServicios;
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    public ConceptoIngreso getServicioSeleccionado() {
        return oServicioSeleccionado;
    }

    public void setServicioSeleccionado(ConceptoIngreso oServicioSeleccionado) {
        this.oServicioSeleccionado = oServicioSeleccionado;
    }

    public float getTotalCarrito() {
        return this.totalCarrito;
    }
    
    public String getTotalCarritoFmt(){
        return this.totalCarrito+"";
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public boolean isRegistroExitoso() {
        return registroExitoso;
    }

    public void setRegistroExitoso(boolean registroExitoso) {
        this.registroExitoso = registroExitoso;
    }

    public ServicioPrestadoJB getServicioPrestadoJB() {
        return this.oServicioPrestadoJB2;
    }

    public void setServicioPrestadoJB(ServicioPrestadoJB servicioPrestado) {
    }

    public boolean getQuererFactura() {
        return quererFactura;
    }

    public void setQuererFactura(boolean quererFactura) {
        this.quererFactura = quererFactura;
    }

    public Medico getMedico() {
        return oMedico;
    }

    public void setMedico(Medico oMedico) {
        this.oMedico = oMedico;
    }

    public String getTipoPaciente() {
        return tipoPaciente;
    }

    public void setTipoPaciente(String tipoPaciente) {
        this.tipoPaciente = tipoPaciente;
        this.nQuienPaga = Integer.parseInt(this.tipoPaciente);
    }

    public String getDescTipoPaciente() {
        TipoPrincipalPaga oTip = new TipoPrincipalPaga();
        String sDesc = "";
        oTip.setTipo(this.nQuienPaga);
        try {
            oTip.buscar();
            sDesc = oTip.getDescripcion();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sDesc;
    }

    public String getQuienRealizaOrden() {
        return quienRealizaOrden;
    }

    public void setQuienRealizaOrden(String quienRealizaOrden) {
        this.quienRealizaOrden = quienRealizaOrden;
    }

    public LineaIngreso getLineaIngreso() {
        return lineaIngreso;
    }

    public void setLineaIngreso(LineaIngreso lineaIngreso) {
        System.out.println("Set "+lineaIngreso);
        this.lineaIngreso = lineaIngreso;
    }

    public List<LineaIngreso> getLineasIngreso() {
        return lineasIngreso;
    }

    public List<AreaDeServicio> getAreasDeServicio() {
        return areasDeServicio;
    }

    public void setAreasDeServicio(List<AreaDeServicio> areasDeServicio) {
        this.areasDeServicio = areasDeServicio;
    }

    public AreaDeServicio getAreaDeServicio() {
        return areaDeServicio;
    }

    public void setAreaDeServicio(AreaDeServicio areaDeServicio) {
        this.areaDeServicio = areaDeServicio;
    }

    public List<String> getFolios() {
        return folios;
    }

    public void setFolios(List<String> folios) {
        this.folios = folios;
    }

    public List<ServicioPrestado> getListaServAlm() {
        return this.arrServicios;
    }

    public EpisodioMedico getEpisodioMedico() {
        return oEpisodioMedico;
    }

    public void setEpisodioMedico(EpisodioMedico oEpisodioMedico) {
        this.oEpisodioMedico = oEpisodioMedico;
    }

    public List<UnidadMedida> getListaUnidades() {
        return listaUnidades;
    }

    public void setListaUnidades(List<UnidadMedida> listaUnidades) {
        this.listaUnidades = listaUnidades;
    }

    public boolean getConEpisodio() {
        return bConEpisodio;
    }

    public void setConEpisodio(boolean bConEpisodio) {
        this.bConEpisodio = bConEpisodio;
    }

    public boolean getValListMed() {
        return bValListMed;
    }

    public void setValListMed(boolean bValListMed) {
        this.bValListMed = bValListMed;
    }

    public boolean getIsPaquete() {
        return bPaquete;
    }

    public void setIsPaquete(boolean bPaquete) {
        this.bPaquete = bPaquete;
    }

    public List<DetallePaquete> getCarritoServiciosPaq() {
        return carritoServiciosPaq;
    }

    public void setCarritoServiciosPaq(List<DetallePaquete> carritoServiciosPaq) {
        this.carritoServiciosPaq = carritoServiciosPaq;
    }

    public List<OtrosDsctosPaq> getCarritoDescuentos() {
        return carritoDescuentos;
    }

    public void setCarritoDescuentos(List<OtrosDsctosPaq> carritoDescuentos) {
        this.carritoDescuentos = carritoDescuentos;
    }

    public PaqueteContratado getPaqueteContratado() {
        return oPaqueteContratado;
    }

    public void setPaqueteContratado(PaqueteContratado oPaqueteContratado) {
        this.oPaqueteContratado = oPaqueteContratado;
    }

    public float getTotalCarrito2() {
        return totalCarrito2;
    }

    public void setTotalCarrito2(float totalCarrito2) {
        this.totalCarrito2 = totalCarrito2;
    }

    public List<Medico> getMedicosInterconsulta() {
        return medicosInterconsulta;
    }

    public void setMedicosInterconsulta(List<Medico> medicosInterconsulta) {
        this.medicosInterconsulta = medicosInterconsulta;
    }

    public Medico getMedicoHonorarios() {
        return oMedicoHonorarios;
    }

    public void setMedicoHonorarios(Medico oMedicoHonorarios) {
        this.oMedicoHonorarios = oMedicoHonorarios;
    }

    public Date getFechaInterconsulta() {
        return fechaInterconsulta;
    }

    public void setFechaInterconsulta(Date fechaInterconsulta) {
        this.fechaInterconsulta = fechaInterconsulta;
    }

    public String getRutaPDF() {
        return rutaPDF;
    }

    public void setRutaPDF(String rutaPDF) {
        this.rutaPDF = rutaPDF;
    }

    public boolean getInterconsulta() {
        return interconsulta;
    }

    public void setInterconsulta(boolean interconsulta) {
        this.interconsulta = interconsulta;
    }

    public UnidadMedida getUnidadMedida() {
        return oUnidadMedida;
    }

    public void setUnidadMedida(UnidadMedida oUnidadMedida) {
        this.oUnidadMedida = oUnidadMedida;
    }

    public boolean getSiena() {
        return siena;
    }

    public void setSiena(boolean siena) {
        this.siena = siena;
    }

    public int getCantidad() {
        return nCantidad;
    }

    public void setCantidad(int nCantidad) {
        this.nCantidad = nCantidad;
    }

    public boolean getCampoInterconsulta() {
        return campoInterconsulta;
    }

    public void setCampoInterconsulta(boolean campoInterconsulta) {
        this.campoInterconsulta = campoInterconsulta;
    }

    public boolean getFacturaRegistrada() {
        return facturaRegistrada;
    }

    public void setFacturaRegistrada(boolean facturaRegistrada) {
        this.facturaRegistrada = facturaRegistrada;
    }

    public List<ConceptoIngreso> getCarritoServiciosAConsumir() {
        return carritoServiciosAConsumir;
    }

    public void setCarritoServiciosAConsumir(List<ConceptoIngreso> carritoServiciosAConsumir) {
        this.carritoServiciosAConsumir = carritoServiciosAConsumir;
    }

    public List<ConceptoIngreso> getServiciosAConsumir() {
        return serviciosAConsumir;
    }

    public void setServiciosAConsumir(List<ConceptoIngreso> serviciosAConsumir) {
        this.serviciosAConsumir = serviciosAConsumir;
    }

    public boolean getAlertaPagoPaqueteMat() {
        return alertaPagoPaqueteMat;
    }

    public void setAlertaPagoPaqueteMat(boolean alertaPagoPaqueteMat) {
        this.alertaPagoPaqueteMat = alertaPagoPaqueteMat;
    }

    public float getMontoPagadoPaq() {
        return nMontoPagadoPaq;
    }

    public void setMontoPagadoPaq(float nMontoPagadoPaq) {
        this.nMontoPagadoPaq = nMontoPagadoPaq;
    }

    public List<Medico> getMedicosTodos() {
        return this.medicosTodos;
    }

    public void setMedicosTodos(List<Medico> value) {
        this.medicosTodos = value;
    }

    public void actualizaServicios() {
        System.out.println("Tipo de paciente: "+this.tipoPaciente);
    }

    /**
     * @return the oMedSolicita
     */
    public Medico getMedSolicita() {
        return oMedSolicita;
    }

    /**
     * @param oMedSolicita the oMedSolicita to set
     */
    public void setMedSolicita(Medico valor) {
        this.oMedSolicita = valor;
    }

    public boolean getPaginado() {
        return this.bPaginado;
    }

    public int getRenglones() {
        return this.nRenglones;
    }

    public int getNumOrden() {
        return this.nNumOrden;
    }

    public boolean getTieneEmpresa() {
        return this.bTieneEmpresa;
    }

    public String getNomEmpresa() {
        return this.sEmpresa;
    }

    @Override
    public String toString() {
        return "ServicioPrestadoJB{" + "nCantidad=" + nCantidad + ", paciente=" + paciente + ", tiposPacientes=" + tiposPacientes + ", servicios=" + servicios + ", carritoServicios=" + carritoServicios + ", areaDeServicio=" + areaDeServicio + ", lineaIngreso=" + lineaIngreso + ", totalCarrito=" + totalCarrito + ", totalCarrito2=" + totalCarrito2 + ", servicio=" + servicio + ", observaciones=" + observaciones + ", oServicioSeleccionado=" + oServicioSeleccionado + ", registroExitoso=" + registroExitoso + ", quererFactura=" + quererFactura + ", bConEpisodio=" + bConEpisodio + ", bValListMed=" + bValListMed + ", oServicioPrestadoJB2=" + oServicioPrestadoJB2 + ", oMedico=" + oMedico + ", tipoPaciente=" + tipoPaciente + ", nQuienPaga=" + nQuienPaga + ", quienRealizaOrden=" + quienRealizaOrden + ", areasDeServicio=" + areasDeServicio + ", sArea=" + sArea + ", folios=" + folios + ", arrServicios=" + arrServicios + ", oEpisodioMedico=" + oEpisodioMedico + ", listaUnidades=" + listaUnidades + ", oUnidadMedida=" + oUnidadMedida + ", bPaquete=" + bPaquete + ", oPaqueteContratado=" + oPaqueteContratado + ", carritoServiciosPaq=" + carritoServiciosPaq + ", carritoServiciosAConsumir=" + carritoServiciosAConsumir + ", serviciosAConsumir=" + serviciosAConsumir + ", carritoDescuentos=" + carritoDescuentos + ", medicosInterconsulta=" + medicosInterconsulta + ", medicosTodos=" + medicosTodos + ", oMedicoHonorarios=" + oMedicoHonorarios + ", fechaInterconsulta=" + fechaInterconsulta + ", rutaPDF=" + rutaPDF + ", interconsulta=" + interconsulta + ", campoInterconsulta=" + campoInterconsulta + ", siena=" + siena + ", listServsConsms=" + listServsConsms + ", facturaRegistrada=" + facturaRegistrada + ", alertaPagoPaqueteMat=" + alertaPagoPaqueteMat + ", nMontoPagadoPaq=" + nMontoPagadoPaq + ", oMedSolicita=" + oMedSolicita + ", bPaginado=" + bPaginado + ", nRenglones=" + nRenglones + ", nNumOrden=" + nNumOrden + ", bTieneEmpresa=" + bTieneEmpresa + ", sEmpresa=" + sEmpresa + '}';
    }
    
    public int getCveLin(){
        this.nCveLin = lineaIngreso.getCveLin();
        return this.nCveLin;
    }
    public void setCveLin(int nCve){
        this.nCveLin = nCve;
        this.lineaIngreso = LineaIngresoJB.getUnaLinea(nCve);
    }
    
    public boolean getCambiaTipoPac(){
        return this.bCambiaTipoPac;
    }
}
