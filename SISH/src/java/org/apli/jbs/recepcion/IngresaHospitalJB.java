package org.apli.jbs.recepcion;

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
import javax.servlet.http.HttpSession;
import org.apli.jbs.DatosHistoriaClinicaJB;
import org.apli.jbs.ListaDiagnosticoJB;
import org.apli.jbs.utilidades.DocsPDFJB;
import org.apli.jbs.HabitacionJB;
import org.apli.jbs.MedicoJB;
import org.apli.jbs.PacienteJB;
import org.apli.modelbeans.DetallePaquete;
import org.apli.modelbeans.Diagnostico;
import org.apli.modelbeans.EpisodioCiaCred;
import org.apli.modelbeans.EpisodioMedico;
import org.apli.modelbeans.ventaCredito.FormatoCiaCred;
import org.apli.modelbeans.Habitacion;
import org.apli.modelbeans.HistoriaClinica;
import org.apli.modelbeans.Medico;
import org.apli.modelbeans.OtrosDsctosPaq;
import org.apli.modelbeans.Paciente;
import org.apli.modelbeans.PaqueteContratado;
import org.apli.modelbeans.ServicioPrestado;
import org.apli.modelbeans.TipoConvenio;
import org.apli.modelbeans.TipoEmpresa;
import org.apli.modelbeans.TipoPrincipalPaga;
import org.apli.modelbeans.ventaCredito.CompaniaCred;
import org.primefaces.context.RequestContext;

/**
 *
 * @author JRuiz
 */
@ManagedBean(name = "oIngrHosp")
@ViewScoped
public class IngresaHospitalJB implements Serializable, Converter {
    private boolean bPacienteExistente = false;
    private boolean bPacienteNuevo = false;
    private boolean bPacienteHospExis = false;
    private boolean bpanelActivo = false;
    private boolean bpac = false;
    private boolean bEmpresa = false;
    private boolean bAseg = false;
    private boolean bBanco = false;
    private boolean bMun = false;
    private boolean bOtro = false;
    private boolean bTieneMedTra = false;
    private boolean bTieneMedRecom = false;
    private Paciente oPaciente = null;
    private String sTipo;
    private List<CompaniaCred> compañias = new ArrayList<CompaniaCred>();
    private List<CompaniaCred> bancos = new ArrayList<CompaniaCred>();
    private List<CompaniaCred> todasCompañias = new ArrayList<CompaniaCred>();
    private List<CompaniaCred> empresas = new ArrayList<CompaniaCred>();
    private List<CompaniaCred> aseguradoras = new ArrayList<CompaniaCred>();
    private List<CompaniaCred> municipios = new ArrayList<CompaniaCred>();
    private List<CompaniaCred> otros = new ArrayList<CompaniaCred>();
    private List<EpisodioCiaCred> compañiasAutorizadas = new ArrayList<EpisodioCiaCred>();
    private EpisodioCiaCred compañiasAutorizada = new EpisodioCiaCred();
    private CompaniaCred oCompañia = null;
    private DatosHistoriaClinicaJB oDatosHC = null;
    private HistoriaClinica oIngresaHospital = new HistoriaClinica();
    private IngresaHospitalJB IngresaHospitalJB2;
    private ServicioPrestado oServicioPrestado;
    private List<EpisodioCiaCred> compañiasregistradasAutorizadas = new ArrayList<EpisodioCiaCred>();
    private EpisodioCiaCred oEmpresaSH = new EpisodioCiaCred();
    private EpisodioCiaCred oEmpresaUpdate = new EpisodioCiaCred();
    private int nCveEpiMed = 0;
    private String msjHeadDialog;
    private String rutaPDFContratoIngrPac;
    private String rutaPDFDocInfoIngr;
    private boolean identPac = false;
    private boolean identfam = false;
    private String sFolioServ;
    private boolean bPaquete = false;
    private PaqueteContratado oPaqueteContratado = null;
    private List<DetallePaquete> carritoServiciosPaq = new ArrayList<DetallePaquete>();
    private List<OtrosDsctosPaq> carritoDescuentos = new ArrayList<OtrosDsctosPaq>();
    private float totalCarrito2 = 0;
    private boolean habEncontradaPaq = false;
    private boolean facturaRegistrada = false;

    public void pacienteExistente() throws Exception {
        limpia();
        oPaciente = new PacienteJB().getPacienteSesion();
        if (oPaciente != null) {
            oIngresaHospital = new HistoriaClinica();
            if (oPaciente.getFallecido() != true) {
                EpisodioMedico oEpisodioMedico = new EpisodioMedico();
                if (!oEpisodioMedico.buscaPacienteHospitalizado(oPaciente.getFolioPac())) {
                    llena();
                    if (oEpisodioMedico.getCveepisodio() != 0) {
                        facturaRegistrada = true;
                        oIngresaHospital.getEpisodioMedico().getServicioPrestado().setFacturable(oEpisodioMedico.esFacturableEpisodio());
                    }
                    if (oEpisodioMedico.tieneEpisodioMedicoPaciente(oPaciente.getFolioPac())) {
                        if (oEpisodioMedico.getMedTratante() != null) {
                            oIngresaHospital.getEpisodioMedico().setMedTratante(oEpisodioMedico.getMedTratante());
                            bTieneMedTra = true;
                        }
                        if (oEpisodioMedico.getMedRecibe() != null) {
                            oIngresaHospital.getEpisodioMedico().setMedRecibe(oEpisodioMedico.getMedRecibe());
                        }
                    } else {
                        bTieneMedTra = false;
                    }

                    if (oPaciente.getMedRecomienda() != null) {
                        Medico oMedico = new Medico().buscaMedicoNombre(oPaciente.getMedRecomienda().getFolioPers());
                        oIngresaHospital.getEpisodioMedico().setMedRecom(oMedico);
                        bTieneMedRecom = true;
                    }
                    bPacienteExistente = true;
                    bPacienteNuevo = false;
                    bPacienteHospExis = false;
                    oDatosHC = null;
                    carritoServiciosPaq = new ArrayList<DetallePaquete>();
                    carritoDescuentos = new ArrayList<OtrosDsctosPaq>();
                    oPaqueteContratado = oPaciente.buscaPacienteConPaqueteContratado();
                    if (oPaqueteContratado != null) {
                        DetallePaquete oDP = new DetallePaquete();
                        oDP.setPaquete(oPaqueteContratado.getPaquete());
                        this.carritoServiciosPaq = oDP.buscaTodosDetallesPaquete();
                        OtrosDsctosPaq oODP = new OtrosDsctosPaq();
                        oODP.setPaquete(oPaqueteContratado.getPaquete());
                        carritoDescuentos = oODP.buscaTodosDsctosPaq();
                        actualizaTotal2();
                        bPaquete = true;
                        sTipo = "6";

                        for (int i = 0; i < this.carritoServiciosPaq.size(); i++) {
                            if (carritoServiciosPaq.get(i).getHabitacion() != null) {
                                for (int j = 0; j < HabitacionJB.habitaciones.size(); j++) {
                                    if (HabitacionJB.habitaciones.get(j).getHab() == carritoServiciosPaq.get(i).getHabitacion().getHab()) {
                                        this.oIngresaHospital.getEpisodioMedico().setHab(HabitacionJB.habitaciones.get(j));
                                        habEncontradaPaq = true;
                                        break;
                                    }
                                }

                            }
                        }
                        if (habEncontradaPaq == false) {
                            for (int i = 0; i < this.carritoServiciosPaq.size(); i++) {
                                if (carritoServiciosPaq.get(i).getHabitacion() != null) {
                                    for (int j = 0; j < HabitacionJB.habitaciones.size(); j++) {
                                        if (HabitacionJB.habitaciones.get(j).getTipoHab().getDescrip().equals(carritoServiciosPaq.get(i).getHabitacion().getTipoHab().getDescrip())) {
                                            this.oIngresaHospital.getEpisodioMedico().setHab(HabitacionJB.habitaciones.get(j));
                                            habEncontradaPaq = true;
                                            break;
                                        }
                                    }

                                }
                            }
                        }
                    }
                } else {
                    FacesMessage msj2 = new FacesMessage("¡Aviso!", "¡El paciente : " + oPaciente.getNombreCompleto() + " con folio: " + oPaciente.getFolioPac() + " ya ha ingresado al hospital!");
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

    public void pacienteNuevo() throws Exception {
        limpia();
        new PacienteJB().limpiaSelectedPaciente();
        oIngresaHospital = new HistoriaClinica();
        bPacienteNuevo = true;
        bPacienteExistente = false;
        bPacienteHospExis = false;
        oDatosHC = new DatosHistoriaClinicaJB();
        llena();
    }

    public void selectPacienteHospExi() throws Exception {
        limpia();
        oPaciente = new PacienteJB().getPacienteSesion();
        if (oPaciente != null) {
            if (oPaciente.getFallecido() != true) {
                oIngresaHospital = new HistoriaClinica();
                oDatosHC = null;
                EpisodioMedico oEpisodioMedico = new EpisodioMedico();
                if (oEpisodioMedico.tieneEpisodioMedicoPacienteHospitalizado(oPaciente.getFolioPac())) {
                    llena();
                    oServicioPrestado = new ServicioPrestado().buscaServicioPrestadoDeHospitalizado(oEpisodioMedico.getCveepisodio());
                    oIngresaHospital.getEpisodioMedico().setServicioPrestado(oServicioPrestado);
                    if (oEpisodioMedico.getMedTratante() != null) {
                        oIngresaHospital.getEpisodioMedico().setMedTratante(oEpisodioMedico.getMedTratante());
                        bTieneMedTra = true;
                    }
                    if (oPaciente.getMedRecomienda() != null) {
                        Medico oMedico = new Medico().buscaMedicoNombre(oPaciente.getMedRecomienda().getFolioPers());
                        oIngresaHospital.getEpisodioMedico().setMedRecom(oMedico);
                        bTieneMedRecom = true;
                    }
                    if (oEpisodioMedico.getMedRecibe() != null) {
                        oIngresaHospital.getEpisodioMedico().setMedRecibe(oEpisodioMedico.getMedRecibe());
                    }
                    compañiasregistradasAutorizadas = new EpisodioCiaCred().todosEpisodiosCiaCred(oPaciente.getFolioPac(), oEpisodioMedico.getCveepisodio());
                    this.guardaCompañiasautorizadas(compañiasregistradasAutorizadas);
                    if (compañiasregistradasAutorizadas.size() > 0) {
                        if (compañiasregistradasAutorizadas.get(0).getCompCred().getIdEmp() == 1) {
                            oEmpresaSH = compañiasregistradasAutorizadas.get(0);
                        }
                    }
                    bPacienteHospExis = true;
                    bPacienteNuevo = false;
                    bPacienteExistente = false;
                    oIngresaHospital.getEpisodioMedico().setHab(new Habitacion().buscaHabitacion(oEpisodioMedico.getHab().getHab()));
                    oIngresaHospital.getEpisodioMedico().setHabReg(new Habitacion().buscaHabitacion(oEpisodioMedico.getHab().getHab()));
                    nCveEpiMed = oEpisodioMedico.getCveepisodio();
                    List<Habitacion> listaHab = new ArrayList<Habitacion>();
                    listaHab.add(oIngresaHospital.getEpisodioMedico().getHab());
                    listaHab.addAll(HabitacionJB.habitaciones);
                    HabitacionJB.habitaciones = listaHab;
                    oPaqueteContratado = oPaciente.buscaPacienteConPaqueteContratado();
                    if (oPaqueteContratado != null) {
                        DetallePaquete oDP = new DetallePaquete();
                        oDP.setPaquete(oPaqueteContratado.getPaquete());
                        this.carritoServiciosPaq = oDP.buscaTodosDetallesPaquete();
                        OtrosDsctosPaq oODP = new OtrosDsctosPaq();
                        oODP.setPaquete(oPaqueteContratado.getPaquete());
                        carritoDescuentos = oODP.buscaTodosDsctosPaq();
                        actualizaTotal2();
                        bPaquete = true;
                        sTipo = "6";
                    }
                } else {
                    FacesMessage msj2 = new FacesMessage("¡Aviso!", "¡El paciente : " + oPaciente.getNombreCompleto() + " con folio: " + oPaciente.getFolioPac() + " no ha ingresado al hospital!");
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

    public void llena() throws Exception {
        oIngresaHospital = new HistoriaClinica();
        EpisodioMedico oEP = new EpisodioMedico();
        ServicioPrestado oSP = new ServicioPrestado();
        oEP.setServicioPrestado(oSP);
        oIngresaHospital.setEpisodioMedico(oEP);
        CompaniaCred oC = new CompaniaCred();
        oC.setIdEmp(1);
        List<FormatoCiaCred> formatos = new ArrayList<FormatoCiaCred>();
        FormatoCiaCred oFmtC = new FormatoCiaCred();
        oFmtC.setIdFmt(1);
        oFmtC.setNomFmt("Copia de identificación del paciente");
        FormatoCiaCred oFmtC2 = new FormatoCiaCred();
        oFmtC2.setIdFmt(2);
        oFmtC2.setNomFmt("Copia de identificación del familiar responsable");
        formatos.add(oFmtC);
        formatos.add(oFmtC2);
        oEmpresaSH.setCompCred(oC);
        oEmpresaSH.setFormatos(formatos);
        bpanelActivo = true;
        MedicoJB.medicos = new ArrayList<Medico>();
        MedicoJB.medicos = new Medico().buscaTodos();
        HabitacionJB.habitaciones = new ArrayList<Habitacion>();
        HabitacionJB.habitaciones = new Habitacion().buscaTodasHabsDisp();
        bancos = new ArrayList<CompaniaCred>();
        empresas = new ArrayList<CompaniaCred>();
        aseguradoras = new ArrayList<CompaniaCred>();
        municipios = new ArrayList<CompaniaCred>();
        otros = new ArrayList<CompaniaCred>();
        todasCompañias = new CompaniaCred().getCompaniasCredActivas();
        bPaquete = false;
        oPaqueteContratado = null;
        carritoServiciosPaq = new ArrayList<DetallePaquete>();
        carritoDescuentos = new ArrayList<OtrosDsctosPaq>();

        for (int i = 0;
                i < todasCompañias.size();
                i++) {
            int nTipo = todasCompañias.get(i).getTipoCia();
            if (nTipo == 1) {
                bancos.add(todasCompañias.get(i));
            }
            if (nTipo == 2) {
                aseguradoras.add(todasCompañias.get(i));
            }
            if (nTipo == 3) {
                empresas.add(todasCompañias.get(i));
            }
            if (nTipo == 4) {
                municipios.add(todasCompañias.get(i));
            }
            if (nTipo == 5) {
                otros.add(todasCompañias.get(i));
            }
        }
    }

    public void validaTipo() {

        if (sTipo.equals("0")) {
            bpac = true;
            bEmpresa = false;
            bAseg = false;
            bBanco = false;
            bMun = false;
            bOtro = false;
        }
        if (sTipo.equals("1")) {
            bBanco = true;
            bpac = false;
            bEmpresa = false;
            bAseg = false;
            bMun = false;
            bOtro = false;
            compañias = bancos;
        }
        if (sTipo.equals("2")) {
            bpac = false;
            bEmpresa = false;
            bAseg = true;
            bBanco = false;
            bMun = false;
            bOtro = false;
            compañias = aseguradoras;
        }
        if (sTipo.equals("3")) {
            bpac = false;
            bAseg = false;
            bEmpresa = true;
            bBanco = false;
            bMun = false;
            bOtro = false;
            compañias = empresas;
        }
        if (sTipo.equals("4")) {
            bpac = false;
            bAseg = false;
            bEmpresa = false;
            bBanco = false;
            bMun = true;
            bOtro = false;
            compañias = municipios;
        }
        if (sTipo.equals("5")) {
            bpac = false;
            bAseg = false;
            bEmpresa = false;
            bBanco = false;
            bMun = false;
            bOtro = true;
            compañias = otros;
        }
    }

    public void eligeBancoAutorizar() {

        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().
                getSession(false);
        session.setAttribute("oCompañia", oCompañia);
    }

    public void autorizarBanco() {

        boolean validacion = true;
        compañiasAutorizadas = getCompañiasautorizadas();
        if (oCompañia != null) {
            for (int i = 0; i < compañiasAutorizadas.size(); i++) {
                if (oCompañia.getIdEmp() == compañiasAutorizadas.get(i).getCompCred().getIdEmp()) {
                    validacion = false;
                    break;
                }
            }
            if (validacion == true) {
                RequestContext.getCurrentInstance().execute("dlg1.show()");
            } else {
                String rst = "¡Aviso: " + oCompañia.getNombreCorto() + " ya fue autorizada/o.!";
                FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
                FacesContext.getCurrentInstance().addMessage(null, msj2);
            }
        } else {
            String rst = "¡Aviso: necesita seleccionar primero un Banco/Aseguradora/Empresa!";
            FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
        }

    }

    public void ingresarHospital() throws Exception {
        compañiasAutorizadas = getCompañiasautorizadas();
        if (bPacienteNuevo == true && oDatosHC == null) {
            String rst = "Paciente nuevo: Error de validación: Llene los datos para el nuevo paciente.";
            FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
        } else if (bPacienteNuevo == true && oDatosHC.getHistoriaClinica().getPaciente().getGenero() == ' ') {
            String rst = "Género: Error de validación: Se necesitan datos.";
            FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
        } else if (bPacienteExistente == true && oPaciente == null) {
            String rst = "Paciente: Error de validación: No se puede agregar un ingreso a hospital sin un paciente registrado.";
            FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
        } else if (!sTipo.equals("0") && !sTipo.equals("6") && 
                compañiasAutorizadas.isEmpty()) {
            String rst = "Banco/Aseguradora/Empresa/Municipio/Otra : Error de validación: Se debe autorizar por lo menos un/a Banco/Aseguradora/Empresa .";
            FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
        } else {
            String tipoPaciente = "";
            if (bPacienteNuevo == true) {
                tipoPaciente = "1";
                Paciente oPacNuevo = oDatosHC.getHistoriaClinica().getPaciente();
                oIngresaHospital.setPaciente(oPacNuevo);
            }
            if (bPacienteExistente == true) {
                tipoPaciente = "0";
                oIngresaHospital.setPaciente(oPaciente);
            }
            compañiasAutorizadas.add(oEmpresaSH);
            if (compañiasAutorizadas.size() > 1) {
                if (this.oCompañia.getTipoConvenio() == TipoConvenio.TIPO_CRED)
                    oIngresaHospital.getEpisodioMedico().getServicioPrestado().
                        setQuienPaga(TipoPrincipalPaga.TIPO_EMP);
                else
                    oIngresaHospital.getEpisodioMedico().getServicioPrestado().
                        setQuienPaga(TipoPrincipalPaga.TIPO_DSCTO);
            }
            if (sTipo.equals("6")) {
                oIngresaHospital.getEpisodioMedico().getServicioPrestado().
                        setQuienPaga(TipoPrincipalPaga.TIPO_PAQ);
            }
            boolean validacionServicio = true;

            String sFolioServicio = oIngresaHospital.ingresaPacienteHospital(
                    tipoPaciente, compañiasAutorizadas);
            sFolioServ = sFolioServicio;
            if (sFolioServicio.indexOf("ERROR") > 0 ||
                sFolioServicio.indexOf("-")<1) {
                validacionServicio = false;
            }
            if (validacionServicio == false) {
                FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_FATAL, sFolioServicio, "");
                FacesContext.getCurrentInstance().addMessage(null, msj2);
            } else {
                msjHeadDialog = "¡Paciente ingresado al hospital exitosamente!";
                DocsPDFJB oDPDF = new DocsPDFJB();
                EpisodioMedico oEpisodioMedico = new EpisodioMedico();
                if (oIngresaHospital.getPaciente().getFolioPac() == 0) {
                    oIngresaHospital.getPaciente().setFolioPac(
                            oIngresaHospital.getPaciente().buscaPacientePorServiPrest(
                            sFolioServ));
                }
                if (oEpisodioMedico.tieneEpisodioMedicoPacienteHospitalizado(
                        oIngresaHospital.getPaciente().getFolioPac())) {
                    nCveEpiMed = oEpisodioMedico.getCveepisodio();
                }
                oIngresaHospital.getEpisodioMedico().setCveepisodio(nCveEpiMed);
                oIngresaHospital.getEpisodioMedico().setInicio(new Date());
                oIngresaHospital.getEpisodioMedico().setDxIngreso( (new Diagnostico()).buscaDiagnostico(
                        oIngresaHospital.getEpisodioMedico().getDxIngreso().getCve()));
                rutaPDFContratoIngrPac = oDPDF.creaContratoIngresHosp(oIngresaHospital.getPaciente().getNombreCompleto(), oIngresaHospital.getEpisodioMedico().getResponsableCuenta(), nCveEpiMed);
                rutaPDFDocInfoIngr = oDPDF.creaDocInfoIngreso(oIngresaHospital.getPaciente().getNombreCompleto(), oIngresaHospital.getEpisodioMedico().getHab().getHab(), nCveEpiMed);
                IngresaHospitalJB2 = this;
                RequestContext.getCurrentInstance().execute("dlg3.show()");
            }
        }
    }

    public void actualizaIngresarHospital() throws Exception {
        compañiasAutorizadas = getCompañiasautorizadas();
        if (bPacienteHospExis == true && oPaciente == null) {
            String rst = "Paciente: Error de validación: No se puede actualizarr un ingreso a hospital sin un paciente registrado.";
            FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
        } else {
            String tipoPaciente = "";
            if (bPacienteHospExis == true) {
                tipoPaciente = "0";
            }
            oIngresaHospital.setPaciente(oPaciente);
            int noEmpresas = 0;
            for (int i = 0; i < compañiasAutorizadas.size(); i++) {
                if (compañiasAutorizadas.get(i).getCompCred().getIdEmp() != 1 && 
                    compañiasAutorizadas.get(i).getEliminaEmpresa() == false) {
                    noEmpresas++;
                }
            }
            if (noEmpresas >= 1) {
                if (this.oCompañia.getTipoConvenio() == TipoConvenio.TIPO_CRED)
                    oIngresaHospital.getEpisodioMedico().getServicioPrestado().
                        setQuienPaga(TipoPrincipalPaga.TIPO_EMP);
                else
                    oIngresaHospital.getEpisodioMedico().getServicioPrestado().
                        setQuienPaga(TipoPrincipalPaga.TIPO_DSCTO);
            }

            if (compañiasAutorizadas != null && !compañiasAutorizadas.isEmpty() &&
                    oEmpresaSH.getCompCred().getIdEmp() == compañiasAutorizadas.get(0).getCompCred().getIdEmp()) {
                compañiasAutorizadas.set(0, oEmpresaSH);
            } else {
                compañiasAutorizadas.add(oEmpresaSH);
            }
            boolean validacionServicio = true;
            String sFolioServicio = oIngresaHospital.actualzaIngresaPacienteHospital(oServicioPrestado.getIdFolio(), nCveEpiMed, compañiasAutorizadas);
            sFolioServ = oServicioPrestado.getIdFolio();
            if (sFolioServicio.indexOf("ERROR") > 0) {
                validacionServicio = false;
            }
            if (validacionServicio == false) {
                FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_FATAL, sFolioServicio, "");
                FacesContext.getCurrentInstance().addMessage(null, msj2);
            } else {
                msjHeadDialog = "¡Datos actualizados exitosamente para el paciente ingresado al hospital!";
                DocsPDFJB oDPDF = new DocsPDFJB();
                EpisodioMedico oEpisodioMedico = new EpisodioMedico();
                if (oEpisodioMedico.tieneEpisodioMedicoPacienteHospitalizado(oPaciente.getFolioPac())) {
                    rutaPDFContratoIngrPac = oDPDF.creaContratoIngresHosp(oIngresaHospital.getPaciente().getNombreCompleto(), oEpisodioMedico.getResponsableCuenta(), nCveEpiMed);
                    rutaPDFDocInfoIngr = oDPDF.creaDocInfoIngreso(oIngresaHospital.getPaciente().getNombreCompleto(), oIngresaHospital.getEpisodioMedico().getHab().getHab(), nCveEpiMed);
                }
                oIngresaHospital.setEpisodioMedico(oEpisodioMedico);
                System.out.println(oIngresaHospital.getEpisodioMedico().getDxIngreso().getCve()+ " " +
                        oIngresaHospital.getEpisodioMedico().getDxIngreso().getDescrip());
                IngresaHospitalJB2 = this;
                RequestContext.getCurrentInstance().execute("dlg3.show()");
            }
        }
    }

    public void regresarIngresar() throws IOException {
        IngresaHospitalJB IHJB3 = getIngresaHospitalJB2();
        String archivoAElimina = IHJB3.getRutaPDFContratoIngrPac();
        ExternalContext extCont = FacesContext.getCurrentInstance().getExternalContext();
        File folder = new File(extCont.getRealPath("//resources//"));
        if (!archivoAElimina.equals("")) {

            File archivo = new File(folder, archivoAElimina);
            if (archivo.delete()) {
                archivoAElimina = IHJB3.getRutaPDFDocInfoIngr();
            }
        }
        if (!archivoAElimina.equals("")) {

            File archivo = new File(folder, archivoAElimina);
            if (archivo.delete()) {
            }
        }
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.getExternalContext().redirect("ingresarHospital.xhtml");
    }

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {
        if (submittedValue.trim().equals("")) {
            return null;
        } else {
            try {

                int number = Integer.parseInt(submittedValue);

                for (CompaniaCred c : compañias) {
                    if (c.getIdEmp() == number) {
                        return c;
                    }
                }

            } catch (NumberFormatException exception) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid player"));
            }
        }

        return null;
    }

    public void actualizaTotal2() {
        totalCarrito2 = 0;
        for (int i = 0; i < carritoServiciosPaq.size(); i++) {
            totalCarrito2 += (carritoServiciosPaq.get(i).getConceptoIngreso().getMontoNuevo() * carritoServiciosPaq.get(i).getConceptoIngreso().getCantidad());
        }
    }

    public CompaniaCred getBancoAAutorizar() {
        compañiasAutorizada = new EpisodioCiaCred();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
        oCompañia = (CompaniaCred) session.getAttribute("oCompañia");
        return oCompañia;

    }

    public void seleccionaEmprUpdat(EpisodioCiaCred EmpresaUpdate) {
        oEmpresaUpdate = EmpresaUpdate;
        RequestContext.getCurrentInstance().execute("dlgEmpUpdate.show()");
    }

    public void guardaCompañiaautorizadaActualizada() {

        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
        compañiasAutorizadas = (List<EpisodioCiaCred>) session.getAttribute("listCompañiasautorizadas");

        for (int i = 0; i < compañiasAutorizadas.size(); i++) {
            if (oEmpresaUpdate.getCompCred().getIdEmp() == compañiasAutorizadas.get(i).getCompCred().getIdEmp()) {
                compañiasAutorizadas.set(i, oEmpresaUpdate);
            }
        }
        session = (HttpSession) facesContext.getExternalContext().
                getSession(false);
        session.setAttribute("listCompañiasautorizadas", compañiasAutorizadas);

        String rst = "¡Aviso: Los datos de autorización de la compañia " + oEmpresaUpdate.getCompCred().getNombreCorto() + " se actualizaron exitosamente!";
        FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_INFO, rst, "");
        FacesContext.getCurrentInstance().addMessage(null, msj2);

        RequestContext.getCurrentInstance().execute("dlgEmpUpdate.hide()");
    }

    public void guardaCompañiaautorizada() throws Exception {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
        compañiasAutorizadas = (List<EpisodioCiaCred>) session.getAttribute("listCompañiasautorizadas");
        compañiasAutorizada.setCompCred(oCompañia);
        compañiasAutorizada.setCompañiaNueva(true);
        if (compañiasAutorizadas == null) {
            compañiasAutorizadas = new ArrayList<EpisodioCiaCred>();
        }
        compañiasAutorizada.setCompañiaNueva(true);
        List<FormatoCiaCred> formatos = new ArrayList<FormatoCiaCred>();
        formatos = compañiasAutorizada.getFormatoCiaCred().todosFormatosPorEmpresa(compañiasAutorizada.getCompCred().getIdEmp());
        compañiasAutorizada.setFormatos(formatos);
        compañiasAutorizadas.add(compañiasAutorizada);
        session = (HttpSession) facesContext.getExternalContext().
                getSession(false);
        session.setAttribute("listCompañiasautorizadas", compañiasAutorizadas);
        String rst = "¡Aviso: " + oCompañia.getNombreCorto() + " se autorizó exitosamente!";
        FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_INFO, rst, "");
        FacesContext.getCurrentInstance().addMessage(null, msj2);
        RequestContext.getCurrentInstance().execute("dlg1.hide()");

    }

    public void guardaCompañiasautorizadas(List<EpisodioCiaCred> listCompañiasRegistradas) {

        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session2 = (HttpSession) facesContext.getExternalContext().getSession(true);
        session2 = (HttpSession) facesContext.getExternalContext().
                getSession(false);
        session2.setAttribute("listCompañiasautorizadas", listCompañiasRegistradas);

    }

    public void guardaDocsCompAut() {
        String rst = "";
        if (oPaciente == null) {
            rst = "¡Aviso: se guardó exitosamente la documentación para el nuevo usuario!";
        } else {
            rst = "¡Aviso: se guardó exitosamente la documentación para: " + oPaciente.getNombreCompleto() + "!";
        }
        FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_INFO, rst, "");
        FacesContext.getCurrentInstance().addMessage(null, msj2);
        RequestContext.getCurrentInstance().execute("dlg2.hide()");
    }

    public List<EpisodioCiaCred> getCompañiasautorizadas() {

        compañiasAutorizadas = new ArrayList<EpisodioCiaCred>();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
        compañiasAutorizadas = (List<EpisodioCiaCred>) session.getAttribute("listCompañiasautorizadas");
        return compañiasAutorizadas;

    }

    public void eliminaCompañiaAutorizada(int nCompAut) {
        compañiasAutorizadas = getCompañiasautorizadas();

        for (int i = 0; i < compañiasAutorizadas.size(); i++) {
            if (compañiasAutorizadas.get(i).getCompCred().getIdEmp() == nCompAut) {
                compañiasAutorizadas.remove(i);
            }
        }

    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent component, Object value) {
        if (value == null || value.equals("")) {
            return "";
        } else {
            return String.valueOf(((CompaniaCred) value).getIdEmp());
        }
    }

    public void limpia() {
        oEmpresaSH = new EpisodioCiaCred();
        bTieneMedTra = false;
        bPacienteHospExis = false;
        bPacienteNuevo = false;
        bPacienteExistente = false;
        bpanelActivo = false;
        bpac = false;
        bEmpresa = false;
        bAseg = false;
        bBanco = false;
        bMun = false;
        oPaciente = null;
        sTipo = "";
        compañias = new ArrayList<CompaniaCred>();
        bancos = new ArrayList<CompaniaCred>();
        todasCompañias = new ArrayList<CompaniaCred>();
        empresas = new ArrayList<CompaniaCred>();
        aseguradoras = new ArrayList<CompaniaCred>();
        compañiasAutorizadas = new ArrayList<EpisodioCiaCred>();
        oCompañia = null;
        oIngresaHospital = new HistoriaClinica();
        compañiasAutorizadas = new ArrayList<EpisodioCiaCred>();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
        session = (HttpSession) facesContext.getExternalContext().
                getSession(false);
        session.setAttribute("listCompañiasautorizadas", compañiasAutorizadas);
        identfam = false;
        identPac = false;
        bTieneMedTra = false;
        oServicioPrestado = new ServicioPrestado();
        habEncontradaPaq = false;
    }

    public EpisodioCiaCred getEmpresaUpdate() {
        return oEmpresaUpdate;
    }

    public void setEmpresaUpdate(EpisodioCiaCred oEmpresaUpdate) {
        this.oEmpresaUpdate = oEmpresaUpdate;
    }

    public List<Medico> getMedicos() {
        return MedicoJB.medicos;
    }

    public void setMedicos(List medicos) {
        MedicoJB.medicos = medicos;
    }

    public Paciente getPaciente() {
        return oPaciente;
    }

    public void setPaciente(Paciente oPaciente) {
        this.oPaciente = oPaciente;
    }

    public List<Diagnostico> getDiagnosticos() {
        return ListaDiagnosticoJB.diagnosticos;
    }

    public void setDiagnosticos(List diagnosticos) {
        ListaDiagnosticoJB.diagnosticos = diagnosticos;
    }

    public List<Habitacion> getHabitaciones() {
        return HabitacionJB.habitaciones;
    }

    public void setHabitaciones(List habitaciones) {
        HabitacionJB.habitaciones = habitaciones;
    }

    public String getTipo() {
        return sTipo;
    }

    public void setTipo(String sTipo) {
        this.sTipo = sTipo;
    }

    public boolean getIspac() {
        return bpac;
    }

    public void setpac(boolean bpac) {
        this.bpac = bpac;
    }

    public boolean getIsEmpresa() {
        return bEmpresa;
    }

    public void setEmpresa(boolean bEmpresa) {
        this.bEmpresa = bEmpresa;
    }

    public boolean getIsAseg() {
        return bAseg;
    }

    public void setAseg(boolean bAseg) {
        this.bAseg = bAseg;
    }

    public CompaniaCred getCompañia() {
        return oCompañia;
    }

    public void setCompañia(CompaniaCred oCompañia) {
        this.oCompañia = oCompañia;
    }

    public List<CompaniaCred> getCompañias() {
        return compañias;
    }

    public void setCompañias(List<CompaniaCred> compañias) {
        this.compañias = compañias;
    }

    public List<CompaniaCred> getBancos() {
        return bancos;
    }

    public void setBancos(List<CompaniaCred> bancos) {
        this.bancos = bancos;
    }

    public boolean getIsBanco() {
        return bBanco;
    }

    public void setBanco(boolean bBanco) {
        this.bBanco = bBanco;
    }

    public boolean getIsMun() {
        return bMun;
    }

    public void setMun(boolean value) {
        this.bMun = value;
    }
    
    public boolean getIsOtro(){
        return this.bOtro;
    }
    public void setOtro(boolean value){
        this.bOtro = value;
    }

    public boolean getPacienteExistente() {
        return bPacienteExistente;
    }

    public void setPacienteExistente(boolean bPacienteExistente) {
        this.bPacienteExistente = bPacienteExistente;
    }

    public EpisodioCiaCred getCompañiasAutorizada() {
        return compañiasAutorizada;
    }

    public void setCompañiasAutorizada(EpisodioCiaCred compañiasAutorizada) {
        this.compañiasAutorizada = compañiasAutorizada;
    }

    public boolean getIsPacienteNuevo() {
        return bPacienteNuevo;
    }

    public void setPacienteNuevo(boolean bPacienteNuevo) {
        this.bPacienteNuevo = bPacienteNuevo;
    }

    public DatosHistoriaClinicaJB getODatosHC() {
        return oDatosHC;
    }

    public void setODatosHC(DatosHistoriaClinicaJB oDatosHC) {
        this.oDatosHC = oDatosHC;
    }

    public boolean getPanelActivo() {
        return bpanelActivo;
    }

    public void setPanelActivo(boolean bpanelActivo) {
        this.bpanelActivo = bpanelActivo;
    }

    public HistoriaClinica getIngresaHospital() {
        return oIngresaHospital;
    }

    public void setIngresaHospital(HistoriaClinica oIngresaHospital) {
        this.oIngresaHospital = oIngresaHospital;
    }

    public boolean getIdentPac() {
        return identPac;
    }

    public void setIdentPac(boolean identPac) {
        this.identPac = identPac;
    }

    public boolean getIdentfam() {
        return identfam;
    }

    public void setIdentfam(boolean identfam) {
        this.identfam = identfam;
    }

    public IngresaHospitalJB getIngresaHospitalJB2() {
        return IngresaHospitalJB2;
    }

    public void setIngresaHospitalJB2(IngresaHospitalJB value) {
        IngresaHospitalJB2 = value;
    }

    public String getFolioServicio() {
        return sFolioServ;
    }

    public void setFolioServicio(String sFolioServicio) {
        this.sFolioServ = sFolioServicio;
    }

    public boolean getPacienteHospExis() {
        return bPacienteHospExis;
    }

    public void setPacienteHospExis(boolean bPacienteHopExis) {
        this.bPacienteHospExis = bPacienteHopExis;
    }

    public List<EpisodioCiaCred> getCompañiasregistradasAutorizadas() {
        return compañiasregistradasAutorizadas;
    }

    public void setCompañiasregistradasAutorizadas(List<EpisodioCiaCred> compañiasregistradasAutorizadas) {
        this.compañiasregistradasAutorizadas = compañiasregistradasAutorizadas;
    }

    public EpisodioCiaCred getEmpresaSH() {
        return oEmpresaSH;
    }

    public void setEmpresaSH(EpisodioCiaCred oEmpresaSH) {
        this.oEmpresaSH = oEmpresaSH;
    }

    public String getMsjHeadDialog() {
        return msjHeadDialog;
    }

    public void setMsjHeadDialog(String msjHeadDialog) {
        this.msjHeadDialog = msjHeadDialog;
    }

    public boolean getTieneMedTra() {
        return bTieneMedTra;
    }

    public void setTieneMedTra(boolean bTieneMedTra) {
        this.bTieneMedTra = bTieneMedTra;
    }

    public String getRutaPDFContratoIngrPac() {
        return rutaPDFContratoIngrPac;
    }

    public void setRutaPDFContratoIngrPac(String rutaPDFContratoIngrPac) {
        this.rutaPDFContratoIngrPac = rutaPDFContratoIngrPac;
    }

    public String getRutaPDFDocInfoIngr() {
        return rutaPDFDocInfoIngr;
    }

    public void setRutaPDFDocInfoIngr(String rutaPDFDocInfoIngr) {
        this.rutaPDFDocInfoIngr = rutaPDFDocInfoIngr;
    }

    public boolean getTieneMedRecom() {
        return bTieneMedRecom;
    }

    public void setTieneMedRecom(boolean bTieneMedRecom) {
        this.bTieneMedRecom = bTieneMedRecom;
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

    public boolean getFacturaRegistrada() {
        return facturaRegistrada;
    }

    public void setFacturaRegistrada(boolean facturaRegistrada) {
        this.facturaRegistrada = facturaRegistrada;
    }
    
    public TipoEmpresa[] getListaTiposEmpresa(){
        return (new TipoEmpresa()).buscarTodas();
    }
    public String getEmpresaPart(){
        String a="";
        if(IngresaHospitalJB2.getTipo().compareTo("0")==0){
            a="Particular";
        }else{
            a="Empresa";
        }
        return a;
        
    }
}
