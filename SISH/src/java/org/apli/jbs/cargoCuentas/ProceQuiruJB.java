package org.apli.jbs.cargoCuentas;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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
import org.apli.jbs.PacienteJB;
import org.apli.jbs.utilidades.DocsPDFJB;
import org.apli.modelbeans.AreaDeServicio;
import org.apli.modelbeans.ConceptoIngreso;
import org.apli.modelbeans.DetallePaquete;
import org.apli.modelbeans.EpisodioMedico;
import org.apli.modelbeans.LineaIngreso;
import org.apli.modelbeans.Medico;
import org.apli.modelbeans.OtrosDsctosPaq;
import org.apli.modelbeans.Paciente;
import org.apli.modelbeans.PaqueteContratado;
import org.apli.modelbeans.PersonalHospitalario;
import org.apli.modelbeans.ProcedimientoRealizado;
import org.apli.modelbeans.ServProcQx;
import org.apli.modelbeans.ServicioPrestado;
import org.apli.modelbeans.TipoPrincipalPaga;
import org.apli.modelbeans.TipoProcQx;
import org.apli.modelbeans.UnidadMedida;
import org.apli.modelbeans.Valida;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author ERJI
 */
@ManagedBean(name = "oProceQuiruJB")
@ViewScoped
public class ProceQuiruJB implements Serializable, Converter {

    private List<LineaIngreso> olineasIngreso = null;
    private List<ConceptoIngreso> servicios = new ArrayList<ConceptoIngreso>();
    private List<ServicioPrestado> carritoServicios = new ArrayList<ServicioPrestado>();
    private List<DetallePaquete> carritoServiciosPaq = new ArrayList<DetallePaquete>();
    private List<OtrosDsctosPaq> carritoDescuentos = new ArrayList<OtrosDsctosPaq>();
    private List<UnidadMedida> listaUnidades;
    private List<PersonalHospitalario> listEnf = new ArrayList<PersonalHospitalario>();
    private List<PersonalHospitalario> listaMed = new ArrayList<PersonalHospitalario>();
    private List<PersonalHospitalario> listaMedEnf = new ArrayList<PersonalHospitalario>();
    private List<TipoProcQx> listaCirugias = new ArrayList<TipoProcQx>();
    private UnidadMedida oUnidadMedida = new UnidadMedida();
    private Paciente oPaciente = null;
    private EpisodioMedico oEpisodioMedico = null;
    private PaqueteContratado oPaqueteContratado = null;
    private AreaDeServicio oAreaServicio;
    private LineaIngreso lineaIngreso;
    private PersonalHospitalario oMedEnf;
    private ProceQuiruJB oProceQuiruJB2;
    private ConceptoIngreso oServicioSeleccionado;
    private Date dFechaInicio = null;
    private Date dFechaFin = null;
    private ProcedimientoRealizado oProcRealizado = new ProcedimientoRealizado();
    private boolean panelActivo = false;
    private boolean esPaquete = false;
    private boolean bfacturable = false;
    private int tipoPaciente = -1;
    private String tipoProgramacion = "";
    private String tipoCirugia = "";
    private String sObs = "";
    private String sRol = "";
    private List<TipoPrincipalPaga> tiposPacientes;
    private float totalCarrito2 = 0;
    private float totalCarrito = 0;
    private float nMonto;
    private String rutaPDF;
    private String sTipoMedEnf = "Médicos/Enfermeras";
    private String sTipoQx = "", sSistema = "", sLocalizacion = "",
            sAbordaje = "", sDispositivo = "";
    private List<TipoProcQx> listTipoProc = null;
    private List<ServicioPrestado> listServsConsms = new ArrayList<ServicioPrestado>();
    private int nCantidad = 1;
    private boolean facturaRegistrada = false;
    private List<ConceptoIngreso> carritoServiciosAConsumir = new ArrayList<ConceptoIngreso>();
    private List<ConceptoIngreso> serviciosAConsumir = new ArrayList<ConceptoIngreso>();

    public void cargaLista(ActionEvent ae) {
        try {
            listTipoProc = new TipoProcQx().buscarTodosPorFiltro(
                    sTipoQx.toUpperCase(), sSistema, sLocalizacion, 
                    sAbordaje, sDispositivo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void llena() throws Exception {

        oPaciente = new PacienteJB().getPacienteSesion();
        if (oPaciente != null) {
            if (oPaciente.getFallecido() != true) {
                oEpisodioMedico = new EpisodioMedico();
                oEpisodioMedico = new EpisodioMedico();
                esPaquete = false;
                if (oEpisodioMedico.tieneEpisodioMedicoPacienteHospitalizado(oPaciente.getFolioPac())) {
                    if (oEpisodioMedico.getCveepisodio() != 0) {
                        listServsConsms = new ServicioPrestado().buscaTodosServiciosConsumidosPaquete(oEpisodioMedico.getCveepisodio());
                    }
                    if (oEpisodioMedico.getCveepisodio() != 0) {
                        facturaRegistrada = true;
                        bfacturable = oEpisodioMedico.esFacturableEpisodio();
                    }
                    oPaqueteContratado = oPaciente.buscaPacienteConPaqueteContratado();
                    if (oPaqueteContratado != null) {
                        listTipoProc = null;
                        DetallePaquete oDP = new DetallePaquete();
                        oDP.setPaquete(oPaqueteContratado.getPaquete());
                        this.carritoServiciosPaq = oDP.buscaTodosDetallesPaquete();
                        OtrosDsctosPaq oODP = new OtrosDsctosPaq();
                        oODP.setPaquete(oPaqueteContratado.getPaquete());
                        carritoDescuentos = oODP.buscaTodosDsctosPaq();
                        actualizaTotal2();
                        esPaquete = true;
                        tipoPaciente = TipoPrincipalPaga.TIPO_PAQ;

                        carritoServiciosAConsumir = new ArrayList<ConceptoIngreso>();
                        for (int i = 0; i < carritoServiciosPaq.size(); i++) {
                            if (carritoServiciosPaq.get(i).getConceptoIngreso().getAreaServicio().getCve().equals("5")) {
                                boolean encontrado = false;
                                int cantidadDisp = carritoServiciosPaq.get(i).getCant();
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
                                        carritoServiciosPaq.get(i).getConceptoIngreso().setCantidad(0);
                                        carritoServiciosAConsumir.add(carritoServiciosPaq.get(i).getConceptoIngreso());
                                    }
                                }
                            }
                        }
                    }
                    tiposPacientes = (new TipoPrincipalPaga()).buscaTiposPrincipalPaga();
                    //El tipo Paquete no es elegible
                    tiposPacientes.remove(TipoPrincipalPaga.TIPO_PAQ);
                    oAreaServicio = new AreaDeServicio();
                    oAreaServicio.setCve("5");
                    List<LineaIngreso> oTodaslineasIngreso = new ArrayList<LineaIngreso>();
                    List<LineaIngreso> lineasIngresoHonorarios = new ArrayList<LineaIngreso>();
                    olineasIngreso = new ArrayList<LineaIngreso>();
                    oTodaslineasIngreso = new LineaIngreso().todasLineasIng(
                            oAreaServicio.getCve(), ConceptoIngreso.TIPO_QX);
                    for (int i = 0; i < oTodaslineasIngreso.size(); i++) {
                        if (oTodaslineasIngreso.get(i).getDescrip().contains("HONORARIOS") ||
                            oTodaslineasIngreso.get(i).getDescrip().contains("INSTRU")) {
                            lineasIngresoHonorarios.add(oTodaslineasIngreso.get(i));
                        }
                    }
                    for (int i = 0; i < lineasIngresoHonorarios.size(); i++) {
                        if (lineasIngresoHonorarios.get(i).getDescrip().contains(oPaciente.getTipo().trim())) {
                            olineasIngreso.add(lineasIngresoHonorarios.get(i));
                        }
                    }
                    oProcRealizado = new ProcedimientoRealizado();
                    oServicioSeleccionado = new ConceptoIngreso();
                    oServicioSeleccionado.setMontoNuevo(0);
                    oServicioSeleccionado.setMonto(0);
                    nMonto = 0;
                    panelActivo = true;
                } else {
                    FacesMessage msj2 = new FacesMessage("¡Aviso!", "¡El paciente : " + oPaciente.getNombreCompleto() + " con folio: " + oPaciente.getFolioPac() + " no ha sido hospitalizado!");
                    RequestContext.getCurrentInstance().showMessageInDialog(msj2);
                }

            } else {
                FacesMessage msj2 = new FacesMessage("¡Aviso!", "¡El paciente : " + oPaciente.getNombreCompleto() + " con folio: " + oPaciente.getFolioPac() + " es fallecido!");
                RequestContext.getCurrentInstance().showMessageInDialog(msj2);
            }
        } else {
            FacesMessage msj2 = new FacesMessage("¡Aviso!", "¡No has seleccionado un paciente!");
            RequestContext.getCurrentInstance().showMessageInDialog(msj2);
        }
    }

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {
        if (submittedValue.trim().equals("")) {
            return null;
        } else {
            try {
                if (component.getId().equals("lineas")) {
                    int num = Integer.parseInt(submittedValue);
                    for (LineaIngreso l : this.olineasIngreso) {
                        if (l.getCveLin() == num) {
                            return l;
                        }
                    }
                }
                if (component.getId().equals("servicios")) {
                    for (ConceptoIngreso oCI : servicios) {
                        if (oCI.getIdGenerico().equals(submittedValue)) {
                            return oCI;
                        }
                    }
                }
                if (component.getId().equals("MedEnf")) {
                    int number = Integer.parseInt(submittedValue);
                    for (PersonalHospitalario ph : listaMedEnf) {
                        if (ph.getFolioPers() == number) {
                            return ph;
                        }
                    }
                }
                if (component.getId().equals("Cirugia")) {
                    for (TipoProcQx oTPQ : listaCirugias) {
                        if (oTPQ.getCveTipoProcQx().equals(submittedValue)) {
                            return oTPQ;
                        }
                    }
                }

            } catch (NumberFormatException exception) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", ""));
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent component, Object value) {
        if (value == null || value.equals("")) {
            return "";
        } else {
            String valor = "";
            if (component.getId().equals("lineas")) {
                return valor = String.valueOf(((LineaIngreso) value).getCveLin());
            }
            if (component.getId().equals("servicios")) {
                return valor = String.valueOf(((ConceptoIngreso) value).getIdGenerico());
            }
            if (component.getId().equals("MedEnf")) {
                return valor = String.valueOf(((PersonalHospitalario) value).getFolioPers());
            }
            if (component.getId().equals("Cirugia")) {
                return valor = String.valueOf(((TipoProcQx) value).getCveTipoProcQx());
            }
            return valor;
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
                if (srv.getDescripcion().contains(s) ) {
                    servSelect.add(srv);
                }
            }
            return servSelect;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void actualizaListaServicios() throws Exception {
        servicios = new ArrayList<ConceptoIngreso>();
        ConceptoIngreso oSM = new ConceptoIngreso();
        
        servicios = oSM.buscaServicios(lineaIngreso, 
                oPaciente.getFolioPac(), this.oEpisodioMedico.getCveepisodio(), 
                ConceptoIngreso.BUSQ_PAC, ConceptoIngreso.TIPO_QX, 
                oAreaServicio);
        if (!servicios.isEmpty()) {
            for (int i = 0; i < servicios.size(); i++) {
                if (servicios.get(i).getDescripcion().contains("HABITACION")) {
                    servicios.get(i).setDisponibilidadMax(1);
                }
                if (this.tipoPaciente==TipoPrincipalPaga.TIPO_DSCTO)
                    servicios.get(i).setMonto(servicios.get(i).getMontoNuevo());
            }
        }
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session =(HttpSession)facesContext.getExternalContext().
        getSession(false);
        session.setAttribute("listaServicios",servicios);
        listaMedEnf = new ArrayList<PersonalHospitalario>();
        List<Medico> listaMedicos = new Medico().buscaTodos();
        listaMed = new ArrayList<PersonalHospitalario>();
        for (int i = 0; i < listaMedicos.size(); i++) {
            PersonalHospitalario oPH;
            if ((lineaIngreso.getDescrip().contains("MEDSH") && 
                    listaMedicos.get(i).getTipo().contains("SH")) || 
                    (lineaIngreso.getDescrip().contains("MEDICOS SH") && 
                    listaMedicos.get(i).getTipo().contains("SH")) ||
                    (lineaIngreso.getDescrip().contains("MEDICOS EXT") &&
                    listaMedicos.get(i).getTipo().contains("NSH"))) {
                System.out.println(listaMedicos.get(i).getTipo());
                oPH = listaMedicos.get(i);
                listaMed.add(oPH);
            }
            if ((lineaIngreso.getDescrip().contains("MEDNSH") && 
                    listaMedicos.get(i).getTipo().contains("NSH")) || 
                   (lineaIngreso.getDescrip().contains("MEDICOS NSH") && 
                    listaMedicos.get(i).getTipo().contains("NSH"))) {
                oPH = listaMedicos.get(i);
                listaMed.add(oPH);
            }
        }
        listEnf = new ArrayList<PersonalHospitalario>();
        listEnf = new PersonalHospitalario().buscaTodosPersonalEnfermeras();
        oMedEnf = null;
        sTipoMedEnf = "";
    }

    
    public void validaMedicoEnfermera() {
        if (sTipoMedEnf.equals("Enfermeras")) {
            listaMedEnf = listEnf;
        }
        if (sTipoMedEnf.equals("Médicos")) {
            listaMedEnf = listaMed;
        }

    }

    public void agregaServicio(ActionEvent ae) {
        if (oAreaServicio == null) {
            String rst = "Área : Error de validación: se necesita un valor.";
            FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
        } else if (lineaIngreso == null) {
            String rst = "Linea ingreso : Error de validación: se necesita un valor.";
            FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
        } else if (oServicioSeleccionado == null) {
            String rst = "Servicio  : Error de validación: se necesita un valor.";
            FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
        } else if (oMedEnf == null) {
            String rst = "Médico / Enfermera  : Error de validación: se necesita un valor.";
            FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
        } else if (sRol == null || sRol.equals("")) {
            String rst = "Rol  : Error de validación: se necesita un valor.";
            FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
        } else {
            boolean encontrado = false;
            oUnidadMedida = new UnidadMedida();
            oUnidadMedida.setCve("NA");
            for (int i = 0; i < carritoServicios.size(); i++) {
                if (carritoServicios.get(i).getConcepPrestado().getIdGenerico().equals(oServicioSeleccionado.getIdGenerico()) && carritoServicios.get(i).getMedico().getFolioPers() == oMedEnf.getFolioPers()) {
                    encontrado = true;
                }
            }
            if (encontrado == false) {
                oServicioSeleccionado.setAreaServicio(oAreaServicio);
                oServicioSeleccionado.setUnidadMedida(oUnidadMedida);
                esServicioPaquete();
                aplicaDescuento();
                ServicioPrestado oSP = new ServicioPrestado();
                oSP.setEpisodioMedico(oEpisodioMedico);
                oSP.setPaciente(oPaciente);
                oSP.setCostoOriginal(oServicioSeleccionado.getMonto());
                oSP.setCostoCobrado(oServicioSeleccionado.getMonto());
                Medico oM = new Medico();
                oM.setFolioPers(oMedEnf.getFolioPers());
                oM.setNombre(oMedEnf.getNombre());
                oM.setApellidoPaterno(oMedEnf.getApellidoPaterno());
                oM.setApellidoMaterno(oMedEnf.getApellidoMaterno());
                oSP.setMedico(oM);
                oSP.setUniMed(oUnidadMedida);
                oSP.setConcepPrestado(oServicioSeleccionado);
                oSP.setCantidad(1);
                ServProcQx oServProcQx = new ServProcQx();
                if (sRol.equals("A")) {
                    oServProcQx.setDescripRol("Anestesiólogo");
                }
                if (sRol.equals("C")) {
                    oServProcQx.setDescripRol("Cirujano");
                }
                if (sRol.equals("P")) {
                    oServProcQx.setDescripRol("Primer ayudante");
                }
                if (sRol.equals("S")) {
                    oServProcQx.setDescripRol("Segundo ayudante");
                }
                if (sRol.equals("I")) {
                    oServProcQx.setDescripRol("Enfermera Instrumentista");
                }
                oServProcQx.setRol(sRol);
                oServProcQx.setPersonal(oMedEnf);
                oSP.setServProcQx(oServProcQx);
                carritoServicios.add(oSP);
                lineaIngreso = null;
                oServicioSeleccionado = null;
                oMedEnf = null;
                sRol = "";
            }
            actualizaTotal();
        }

    }

    private void esServicioPaquete() {
        for (int i = 0; i < carritoServiciosPaq.size(); i++) {
            if (carritoServiciosPaq.get(i).getConceptoIngreso().getIdGenerico().equals(oServicioSeleccionado.getIdGenerico())) {
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
            if (listServsConsms.get(j).getConcepPrestado().getIdGenerico().equals(oServicioSeleccionado.getIdGenerico())) {
                nConsumidos += listServsConsms.get(j).getCantidad();
            }
        }
        if (listServsConsms.isEmpty()) {
            if (nPermitidos >= oServicioSeleccionado.getCantidad()) {
                oServicioSeleccionado.setMontoNuevo(0);
                oServicioSeleccionado.setCantidad(nCantidad);
                oServicioSeleccionado.setCantidadACobrar(nCantidad);
            } else {
                nPorConsumir = nPermitidos;
                nPorcobrar = (oServicioSeleccionado.getCantidad() - nPorConsumir);
                oServicioSeleccionado.setMontoNuevo(nPorcobrar * oServicioSeleccionado.getMonto());
                oServicioSeleccionado.setCantidad(nCantidad);
                oServicioSeleccionado.setCantidadACobrar(nPorcobrar);
            }
        } else {
            if (nPermitidos >= (nConsumidos + oServicioSeleccionado.getCantidad())) {
                oServicioSeleccionado.setMontoNuevo(0);
                oServicioSeleccionado.setCantidad(nCantidad);
                oServicioSeleccionado.setCantidadACobrar(nCantidad);
            } else {
                if (nConsumidos < nPermitidos) {
                    nPorConsumir = nPermitidos - nConsumidos;
                }
                nPorcobrar = (oServicioSeleccionado.getCantidad() - nPorConsumir);
                oServicioSeleccionado.setMontoNuevo(nPorcobrar * oServicioSeleccionado.getMonto());
                oServicioSeleccionado.setCantidad(nCantidad);
                oServicioSeleccionado.setCantidadACobrar(nPorcobrar);

            }
        }

    }

    private void aplicaDescuento() {
        for (int i = 0; i < carritoDescuentos.size(); i++) {
            if (carritoDescuentos.get(i).getLineaIngreso().getCveLin() == oServicioSeleccionado.getLineaIngreso().getCveLin()) {
                float nPorcentaje = (oServicioSeleccionado.getMonto() * carritoDescuentos.get(i).getPctDscto()) / 100;
                float nPrecioConDesc = oServicioSeleccionado.getMonto() - ((float) nPorcentaje);
                oServicioSeleccionado.setMontoNuevo(nPrecioConDesc);
            }
        }
    }

    public void insertaProcedomientoQuiru() throws Exception {
        if (this.tipoPaciente==-1) {
            String rst = "Tipo de Paciente : Error de validación: se necesita un valor.";
            FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
        } else if (this.oProcRealizado.getTipoProg().equals("")) {
            String rst = "Tipo de Programación: Error de validación: se necesita un valor.";
            FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
        } else if (this.oProcRealizado.getTipoProcQx() == null) {
            String rst = "Tipo de Cirugía: Error de validación: se necesita un valor.";
            FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
        } else if (this.oProcRealizado.getIni() == null) {
            String rst = "Fecha de Inicio: Error de validación: se necesita un valor.";
            FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
        } else if (this.oProcRealizado.getFin() == null) {
            String rst = "Fecha de Fin: Error de validación: se necesita un valor.";
            FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
        } else if (this.oProcRealizado.getFin().getTime() < this.oProcRealizado.getIni().getTime()) {
            String rst = "Fechas: Error de validación: la Fecha Fin no puede ser anterior a la de Inicio.";
            FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
        } else if (this.oProcRealizado.getFin().getTime() > new Date().getTime() || this.oProcRealizado.getIni().getTime() > new Date().getTime()) {
            String rst = "Fechas: Error de validación: las Fechas no pueden ser mayores a la fecha actual.";
            FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
        } else if (this.oProcRealizado.getNotaMedica().equals("")) {
            String rst = "Descripción: Error de validación: la Fecha Fin no puede ser anterior a la de Inicio.";
            FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
        } else if (carritoServicios.isEmpty()) {
            String rst = "Servicios: Error de validación: Debe ingresar un servicio por lo menos.";
            FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
        } else {
            boolean validacion = true;
            for (int i = 0; i < carritoServicios.size(); i++) {
                carritoServicios.get(i).setFacturable(bfacturable);
                carritoServicios.get(i).setPaciente(oPaciente);
                carritoServicios.get(i).setRegistro(new Date());
                carritoServicios.get(i).setQuienPaga(this.tipoPaciente);
                carritoServicios.get(i).setObs(
                        oProcRealizado.getNotaMedica().toUpperCase());
                carritoServicios.get(i).setRealizado(oProcRealizado.getIni());
                String msj = carritoServicios.get(i).insertarServicioPrestado(carritoServicios.get(i).getConcepPrestado().getLineaIngreso().getCveLin(), "", oAreaServicio.getCve(), carritoServicios.get(i).getConcepPrestado().getTipoConcIngr());
                if (msj.indexOf("ERROR") > 0) {
                    FacesMessage msj2 = new FacesMessage(msj, "");
                    FacesContext.getCurrentInstance().addMessage(null, msj2);
                    validacion = false;
                    break;

                } else {
                    String sFolioServicio = msj;
                    carritoServicios.get(i).setIdFolio(sFolioServicio);
                }
            }
            if (validacion == true) {
                String msj = this.oProcRealizado.insertarProceRealizado();
                if (msj.indexOf("ERROR") > 0) {
                    FacesMessage msj2 = new FacesMessage(msj, "");
                    FacesContext.getCurrentInstance().addMessage(null, msj2);
                } else {
                    try {
                        String idCadena = new Valida().eliminaMSJCorchetes(msj);
                        int idProQx = Integer.parseInt(idCadena);
                        oProcRealizado.setIdProcQxRealizado(idProQx);
                        String sQuery = "";
                        for (int i = 0; i < carritoServicios.size(); i++) {
                            carritoServicios.get(i).getServProcQx().setProcedimientoRealizado(oProcRealizado);
                            carritoServicios.get(i).getServProcQx().setFolioServicioPrest(carritoServicios.get(i).getIdFolio());
                            sQuery += carritoServicios.get(i).getServProcQx().sQueryInsertarServProcQx();
                        }
                        String msj2 = new ServProcQx().insertarServProcQx(sQuery);
                        if (msj2.indexOf("ERROR") > 0) {
                            FacesMessage msj3 = new FacesMessage(msj2, "");
                            FacesContext.getCurrentInstance().addMessage(null, msj3);
                        } else {
                            DocsPDFJB oD = new DocsPDFJB();
                            rutaPDF = oD.creaComprobanteProceQuirurgico(carritoServicios, oEpisodioMedico.getCveepisodio(), oPaciente.getNombreCompleto(), oEpisodioMedico.getHab().getHab(), oEpisodioMedico.getMedTratante().getNombreCompleto());
                            oProceQuiruJB2 = this;
                            RequestContext.getCurrentInstance().execute("dlg1.show()");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        FacesMessage msj2 = new FacesMessage(msj, "");
                        FacesContext.getCurrentInstance().addMessage(null, msj2);
                    }
                }
            }
        }
    }

    public void buscaPrecio() {
        nMonto = oServicioSeleccionado.getMonto();
    }

    public void actualizaTotal2() {
        totalCarrito2 = 0;
        for (int i = 0; i < carritoServiciosPaq.size(); i++) {
            totalCarrito2 += (carritoServiciosPaq.get(i).getConceptoIngreso().getMontoNuevo() * carritoServiciosPaq.get(i).getConceptoIngreso().getCantidad());
        }
    }

    public void actualizaTotal() {
        totalCarrito = 0;
        for (int i = 0; i < carritoServicios.size(); i++) {
            totalCarrito += (carritoServicios.get(i).getCostoCobrado());
        }
    }

    public void eliminaServicioInt(String id) {
        for (int i = 0; i < carritoServicios.size(); i++) {
            if (carritoServicios.get(i).getConcepPrestado().getIdGenerico().equals(id)) {
                if (carritoServicios.get(i).getConcepPrestado().getTipoPaquete()) {
                    this.carritoServiciosAConsumir.add(carritoServicios.get(i).getConcepPrestado());
                }
                carritoServicios.remove(i);
                break;
            }
        }
        actualizaTotal();
    }

    public void limpia() {
        oPaciente = null;
        oEpisodioMedico = null;
        olineasIngreso = new ArrayList<LineaIngreso>();
        panelActivo = false;
        oPaqueteContratado = null;
        carritoServiciosPaq = new ArrayList<DetallePaquete>();
        carritoDescuentos = new ArrayList<OtrosDsctosPaq>();
        bfacturable = false;
        esPaquete = false;
        tipoProgramacion = "";
        tipoCirugia = "";
        dFechaInicio = null;
        dFechaFin = null;
        sObs = "";
        listaUnidades = null;
        oUnidadMedida = null;
        nMonto = 0;
        carritoServicios = new ArrayList<ServicioPrestado>();
        tipoPaciente = -1;
        oMedEnf = null;
        sRol = "";
        sTipoQx = "";
        sSistema = "";
        sLocalizacion = "";
        sAbordaje = "";
        sDispositivo = "";
        listTipoProc = null;
        carritoServiciosAConsumir = new ArrayList<ConceptoIngreso>();
        serviciosAConsumir = new ArrayList<ConceptoIngreso>();
    }

    public void regresar() throws IOException {

        String archivoAElimina = this.rutaPDF;
        ExternalContext extCont = FacesContext.getCurrentInstance().getExternalContext();
        File folder = new File(extCont.getRealPath("//resources//"));
        if (!archivoAElimina.equals("")) {
            File archivo = new File(folder, archivoAElimina);
            if (archivo.delete()) {
            }
        }
        limpia();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.getExternalContext().redirect("procedimientoQuirurgico.xhtml");
    }

    public void validaServiciosACons() {

    }

    public void agregaServicio2(ActionEvent ae) {
        System.out.println("agregaServicio2()");
        if (oServicioSeleccionado == null) {
            String rst = "Servicio  : Error de validación: se necesita un valor.";
            FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
        } else if (oMedEnf == null) {
            String rst = "Médico / Enfermera  : Error de validación: se necesita un valor.";
            FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
        } else if (sRol == null || sRol.equals("")) {
            String rst = "Rol  : Error de validación: se necesita un valor.";
            FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
        } else {
            boolean encontrado = false;
            for (int i = 0; i < carritoServicios.size(); i++) {
                if (carritoServicios.get(i).getConcepPrestado().getIdGenerico().equals(oServicioSeleccionado.getIdGenerico()) && carritoServicios.get(i).getMedico().getFolioPers() == oMedEnf.getFolioPers()) {
                    encontrado = true;
                }
            }
            if (encontrado == false) {
                ServicioPrestado oSP = new ServicioPrestado();
                for (int j = 0; j < this.carritoServiciosAConsumir.size(); j++) {
                    if (carritoServiciosAConsumir.get(j).getIdGenerico().equals(oServicioSeleccionado.getIdGenerico())) {
                        carritoServiciosAConsumir.get(j).setTipoPaquete(true);
                        oSP.setConcepPrestado(carritoServiciosAConsumir.get(j));
                        carritoServiciosAConsumir.remove(j);
                    }
                }
                oSP.setConcepPrestado(oServicioSeleccionado);
                oSP.setEpisodioMedico(oEpisodioMedico);
                oSP.setPaciente(oPaciente);
                oSP.setCostoOriginal(oServicioSeleccionado.getMonto());
                oSP.setCostoCobrado(oServicioSeleccionado.getMonto());
                Medico oM = new Medico();
                oM.setFolioPers(oMedEnf.getFolioPers());
                oM.setNombre(oMedEnf.getNombre());
                oM.setApellidoPaterno(oMedEnf.getApellidoPaterno());
                oM.setApellidoMaterno(oMedEnf.getApellidoMaterno());
                oSP.setMedico(oM);
                oSP.setUniMed(oUnidadMedida);
                oSP.setConcepPrestado(oServicioSeleccionado);
                oSP.setCantidad(1);
                ServProcQx oServProcQx = new ServProcQx();
                if (sRol.equals("A")) {
                    oServProcQx.setDescripRol("Anestesiólogo");
                }
                if (sRol.equals("C")) {
                    oServProcQx.setDescripRol("Cirujano");
                }
                if (sRol.equals("P")) {
                    oServProcQx.setDescripRol("Primer ayudante");
                }
                if (sRol.equals("S")) {
                    oServProcQx.setDescripRol("Segundo ayudante");
                }
                if (sRol.equals("I")) {
                    oServProcQx.setDescripRol("Enfermera Instrumentista");
                }
                oServProcQx.setRol(sRol);
                oServProcQx.setPersonal(oMedEnf);
                oSP.setServProcQx(oServProcQx);
                carritoServicios.add(oSP);
                lineaIngreso = null;
                oServicioSeleccionado = null;
                oMedEnf = null;
                sRol = "";
            }
            actualizaTotal();
            RequestContext.getCurrentInstance().execute("dlgServPaquete.hide()");
        }
    }
    
    public void onRowSelect(SelectEvent event) throws Exception {
        listaMedEnf = new ArrayList<PersonalHospitalario>();
        List<Medico> listaMedicos = new Medico().buscaTodos();
        listaMed = new ArrayList<PersonalHospitalario>();
        for (int i = 0; i < listaMedicos.size(); i++) {
            PersonalHospitalario oPH;
            if (((ConceptoIngreso) event.getObject()).getLineaIngreso().getDescrip().contains("MEDSH") && listaMedicos.get(i).getTipo().contains("SH") || ((ConceptoIngreso) event.getObject()).getLineaIngreso().getDescrip().contains("MEDICOS SH") && listaMedicos.get(i).getTipo().contains("SH")) {
                oPH = listaMedicos.get(i);
                listaMed.add(oPH);
            }
            if (!((ConceptoIngreso) event.getObject()).getLineaIngreso().getDescrip().contains("MEDNSH") && listaMedicos.get(i).getTipo().contains("NSH") || !((ConceptoIngreso) event.getObject()).getLineaIngreso().getDescrip().contains("MEDICOS SH") && listaMedicos.get(i).getTipo().contains("NSH")) {
                oPH = listaMedicos.get(i);
                listaMed.add(oPH);
            }
        }
        listEnf = new ArrayList<PersonalHospitalario>();
        listEnf = new PersonalHospitalario().buscaTodosPersonalEnfermeras();
        oMedEnf = null;
        sTipoMedEnf = "";
    }

    public Paciente getPaciente() {
        return oPaciente;
    }

    public void setPaciente(Paciente oPaciente) {
        this.oPaciente = oPaciente;
    }

    public EpisodioMedico getEpisodioMedico() {
        return oEpisodioMedico;
    }

    public void setEpisodioMedico(EpisodioMedico oEpisodioMedico) {
        this.oEpisodioMedico = oEpisodioMedico;
    }

    public List<LineaIngreso> getLineasIngreso() {
        return olineasIngreso;
    }

    public void setLineasIngreso(List<LineaIngreso> olineasIngreso) {
        this.olineasIngreso = olineasIngreso;
    }

    public boolean getPanelActivo() {
        return panelActivo;
    }

    public void setPanelActivo(boolean panelActivo) {
        this.panelActivo = panelActivo;
    }

    public PaqueteContratado getPaqueteContratado() {
        return oPaqueteContratado;
    }

    public void setPaqueteContratado(PaqueteContratado oPaqueteContratado) {
        this.oPaqueteContratado = oPaqueteContratado;
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

    public boolean getEsPaquete() {
        return esPaquete;
    }

    public void setEsPaquete(boolean esPaquete) {
        this.esPaquete = esPaquete;
    }

    public boolean getFacturable() {
        return bfacturable;
    }

    public void setFacturable(boolean bfacturable) {
        this.bfacturable = bfacturable;
    }

    public int getTipoPaciente() {
        return tipoPaciente;
    }

    public void setTipoPaciente(int tipoPaciente) {
        this.tipoPaciente = tipoPaciente;
    }

    public List<TipoPrincipalPaga> getTiposPacientes() {
        return tiposPacientes;
    }

    public void setTiposPacientes(List<TipoPrincipalPaga> tiposPacientes) {
        this.tiposPacientes = tiposPacientes;
    }

    public float getTotalCarrito2() {
        return totalCarrito2;
    }

    public void setTotalCarrito2(float totalCarrito2) {
        this.totalCarrito2 = totalCarrito2;
    }

    public LineaIngreso getLineaIngreso() {
        return lineaIngreso;
    }

    public void setLineaIngreso(LineaIngreso lineaIngreso) {
        this.lineaIngreso = lineaIngreso;
    }

    public List<ConceptoIngreso> getServicios() {
        return servicios;
    }

    public void setServicios(List<ConceptoIngreso> servicios) {
        this.servicios = servicios;
    }

    public String getTipoProgramacion() {
        return tipoProgramacion;
    }

    public void setTipoProgramacion(String tipoProgramacion) {
        this.tipoProgramacion = tipoProgramacion;
    }

    public String getTipoCirugia() {
        return tipoCirugia;
    }

    public void setTipoCirugia(String tipoCirugia) {
        this.tipoCirugia = tipoCirugia;
    }

    public Date getFechaInicio() {
        return dFechaInicio;
    }

    public void setFechaInicio(Date dFechaInicio) {
        this.dFechaInicio = dFechaInicio;
    }

    public Date getFechaFin() {
        return dFechaFin;
    }

    public void setFechaFin(Date dFechaFin) {
        this.dFechaFin = dFechaFin;
    }

    public String getObs() {
        return sObs;
    }

    public void setObs(String sObs) {
        this.sObs = sObs;
    }

    public ConceptoIngreso getServicioSeleccionado() {
        return oServicioSeleccionado;
    }

    public void setServicioSeleccionado(ConceptoIngreso value) {
        oServicioSeleccionado = value;
    }

    public List<UnidadMedida> getListaUnidades() {
        return listaUnidades;
    }

    public void setListaUnidades(List<UnidadMedida> listaUnidades) {
        this.listaUnidades = listaUnidades;
    }

    public UnidadMedida getUnidadMedida() {
        return oUnidadMedida;
    }

    public void setUnidadMedida(UnidadMedida oUnidadMedida) {
        this.oUnidadMedida = oUnidadMedida;
    }

    public PersonalHospitalario getMedEnf() {
        return oMedEnf;
    }

    public void setMedEnf(PersonalHospitalario oMedEnf) {
        this.oMedEnf = oMedEnf;
    }

    public List<PersonalHospitalario> getListaMedEnf() {
        return listaMedEnf;
    }

    public void setListaMedEnf(List<PersonalHospitalario> listaMedEnf) {
        this.listaMedEnf = listaMedEnf;
    }

    public ProcedimientoRealizado getProcRealizado() {
        return oProcRealizado;
    }

    public void setProcRealizado(ProcedimientoRealizado oProcRealizado) {
        this.oProcRealizado = oProcRealizado;
    }

    public float getMonto() {
        return nMonto;
    }

    public void setMonto(float nMonto) {
        this.nMonto = nMonto;
    }

    public String getRol() {
        return sRol;
    }

    public void setRol(String sRol) {
        this.sRol = sRol;
    }

    public List<ServicioPrestado> getCarritoServicios() {
        return carritoServicios;
    }

    public void setCarritoServicios(List<ServicioPrestado> carritoServicios) {
        this.carritoServicios = carritoServicios;
    }

    public float getTotalCarrito() {
        return totalCarrito;
    }

    public void setTotalCarrito(float totalCarrito) {
        this.totalCarrito = totalCarrito;
    }

    public List<TipoProcQx> getListaCirugias() {
        return listaCirugias;
    }

    public void setListaCirugias(List<TipoProcQx> listaCirugias) {
        this.listaCirugias = listaCirugias;
    }

    public String getRutaPDF() {
        return rutaPDF;
    }

    public void setRutaPDF(String rutaPDF) {
        this.rutaPDF = rutaPDF;
    }

    public ProceQuiruJB getProceQuiruJB() {
        return oProceQuiruJB2;
    }

    public List<TipoProcQx> getListaQx() {
        return listTipoProc;
    }

    public String getTipoMedEnf() {
        return sTipoMedEnf;
    }

    public void setTipoMedEnf(String sTipoMedEnf) {
        this.sTipoMedEnf = sTipoMedEnf;
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

    public boolean getFacturaRegistrada() {
        return facturaRegistrada;
    }

    public void setFacturaRegistrada(boolean facturaRegistrada) {
        this.facturaRegistrada = facturaRegistrada;
    }

    /**
     * @return the sTipo
     */
    public String getTipoQx() {
        return sTipoQx;
    }

    /**
     * @param sTipo the sTipo to set
     */
    public void setTipoQx(String sTipoQx) {
        this.sTipoQx = sTipoQx;
    }

    /**
     * @return the sSistema
     */
    public String getSistema() {
        return sSistema;
    }

    /**
     * @param sSistema the sSistema to set
     */
    public void setSistema(String sSistema) {
        this.sSistema = sSistema;
    }

    /**
     * @return the sLocalizacion
     */
    public String getLocalizacion() {
        return sLocalizacion;
    }

    /**
     * @param sLocalizacion the sLocalizacion to set
     */
    public void setLocalizacion(String sLocalizacion) {
        this.sLocalizacion = sLocalizacion;
    }

    /**
     * @return the sAbordaje
     */
    public String getAbordaje() {
        return sAbordaje;
    }

    /**
     * @param sAbordaje the sAbordaje to set
     */
    public void setAbordaje(String sAbordaje) {
        this.sAbordaje = sAbordaje;
    }

    /**
     * @return the sDispositivo
     */
    public String getDispositivo() {
        return sDispositivo;
    }

    /**
     * @param sDispositivo the sDispositivo to set
     */
    public void setDispositivo(String sDispositivo) {
        this.sDispositivo = sDispositivo;
    }
}
