/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apli.jbs.cargoCuentas;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.apli.jbs.ListaDiagnosticoJB;
import org.apli.jbs.HabitacionJB;
import org.apli.jbs.PacienteJB;
import org.apli.modelbeans.AntecedPerinatales;
import org.apli.modelbeans.Diagnostico;
import org.apli.modelbeans.EpisodioMedico;
import org.apli.modelbeans.Habitacion;
import org.apli.modelbeans.HistoriaClinica;
import org.apli.modelbeans.Medico;
import org.apli.modelbeans.Paciente;
import org.apli.modelbeans.ServicioPrestado;
import org.primefaces.context.RequestContext;

/**
 *
 * @author ERJI
 */
@ManagedBean(name = "oRegRecNac")
@SessionScoped
public class RegistrarRecienNacidoJB implements Serializable {

    private Paciente oPaciente = null;
    private boolean bpanelActivo = false;
    private RegistrarRecienNacidoJB oRegistrarRecienNacidoJB2;
    private String sCantidadNacidos;
    private List<HistoriaClinica> nacidos = new ArrayList<HistoriaClinica>();
    private String sApellidoPat;
    private HistoriaClinica oIngresaHospital;

    public void selectPacienteHospExi() throws Exception {
    String sCveDx="";
        limpia();
        oPaciente = new PacienteJB().getPacienteSesion();
        if (oPaciente != null) {
            if (oPaciente.getGenero() == 'F') {
                EpisodioMedico oEM = new EpisodioMedico();
                Diagnostico oD = new Diagnostico();
                oEM.setDxIngreso(oD);
                ServicioPrestado oSP = new ServicioPrestado();
                oEM.setServicioPrestado(oSP);
                oIngresaHospital = new HistoriaClinica();
                oIngresaHospital.setEpisodioMedico(oEM);
                if (oIngresaHospital.getEpisodioMedico().buscaPacienteHospitalizado(oPaciente.getFolioPac())) {
                    if (oIngresaHospital.getEpisodioMedico().getDxIngreso().getCve() != null) {
                        sCveDx = oIngresaHospital.getEpisodioMedico().getDxIngreso().getCve();
                        if (sCveDx.contains("O") || sCveDx.contains("Z32") ||
                            sCveDx.contains("Z33") || sCveDx.contains("Z34") ||
                            sCveDx.contains("Z35")) {
                            bpanelActivo = true;
                            if (sCveDx.contains("O01") || sCveDx.contains("O02")
                                    || sCveDx.contains("O03") || sCveDx.contains("O04")
                                    || sCveDx.contains("O05") || sCveDx.contains("O06")
                                    || sCveDx.contains("O08")) {
                                FacesMessage msj2 = new FacesMessage("¡Aviso!", "¡El paciente : " + oPaciente.getNombreCompleto() + " con folio: " + oPaciente.getFolioPac() + " y diagnóstico:" + oIngresaHospital.getEpisodioMedico().getDxIngreso().getDescrip() + " tiene relación a embarazo pero no es permitido para registrar un recién nacido!");
                                RequestContext.getCurrentInstance().showMessageInDialog(msj2);
                                bpanelActivo = false;
                            } else {
                                bpanelActivo = true;
                                ListaDiagnosticoJB.diagnosticos = new ArrayList<Diagnostico>();
                                ListaDiagnosticoJB.diagnosticos = new Diagnostico().buscaTodos();
                                HabitacionJB.habitaciones = new ArrayList<Habitacion>();
                                HabitacionJB.habitaciones = new Habitacion().buscaTodosCunerosDisp();
                                ServicioPrestado oSP2 = new ServicioPrestado();
                                oIngresaHospital.getEpisodioMedico().setServicioPrestado(oSP2.buscaServicioPrestadoDeHospitalizado(oIngresaHospital.getEpisodioMedico().getCveepisodio()));
                                if (oIngresaHospital.getEpisodioMedico().getMedTratante() != null) {
                                    oIngresaHospital.getEpisodioMedico().setMedTratante(oIngresaHospital.getEpisodioMedico().getMedTratante());
                                }
                                if (oPaciente.getMedRecomienda() != null) {
                                    Medico oMedico = new Medico().buscaMedicoNombre(oPaciente.getMedRecomienda().getFolioPers());
                                    oIngresaHospital.getEpisodioMedico().setMedRecom(oMedico);
                                }
                                if (oIngresaHospital.getEpisodioMedico().getMedRecibe() != null) {
                                    oIngresaHospital.getEpisodioMedico().setMedRecibe(oIngresaHospital.getEpisodioMedico().getMedRecibe());
                                }
                            }
                        } else {
                            FacesMessage msj2 = new FacesMessage("¡Aviso!", "¡El paciente : " + oPaciente.getNombreCompleto() + " con folio: " + oPaciente.getFolioPac() + " y diagnóstico:" + oIngresaHospital.getEpisodioMedico().getDxIngreso().getDescrip() + " no tiene relación a embarazo!");
                            RequestContext.getCurrentInstance().showMessageInDialog(msj2);
                        }
                    } else {
                        FacesMessage msj2 = new FacesMessage("¡Aviso!", "¡El paciente : " + oPaciente.getNombreCompleto() + " con folio: " + oPaciente.getFolioPac() + " no tiene un diagnóstico de ingreso registrado.!");
                        RequestContext.getCurrentInstance().showMessageInDialog(msj2);
                    }
                } else {
                    FacesMessage msj2 = new FacesMessage("¡Aviso!", "¡El paciente : " + oPaciente.getNombreCompleto() + " con folio: " + oPaciente.getFolioPac() + " no está hospitalizado!");
                    RequestContext.getCurrentInstance().showMessageInDialog(msj2);
                }

            } else {
                FacesMessage msj2 = new FacesMessage("¡Aviso!", "¡El paciente seleccionado no es una mujer!");
                RequestContext.getCurrentInstance().showMessageInDialog(msj2);
            }

        } else {
            FacesMessage msj2 = new FacesMessage("¡Aviso!", "¡No has seleccionado un paciente!");
            RequestContext.getCurrentInstance().showMessageInDialog(msj2);
        }

    }

    public void actualizaListaNacidos() {
        nacidos = new ArrayList<HistoriaClinica>();
        int nCantidadNacidos = Integer.parseInt(sCantidadNacidos);
        if (nCantidadNacidos > 0 && nCantidadNacidos <= 8) {
            for (int i = 0; i < nCantidadNacidos; i++) {
                HistoriaClinica oNacido = new HistoriaClinica();
                Paciente oPac = new Paciente();
                oPac.setTipo(oPaciente.getTipo());
                oPac.setNombre("RN" + (i + 1));
                AntecedPerinatales oAP = new AntecedPerinatales();
                EpisodioMedico oEM = new EpisodioMedico();
                ServicioPrestado oSP = new ServicioPrestado();
                oEM.setServicioPrestado(oSP);
                oNacido.setEpisodioMedico(oEM);
                oNacido.setPaciente(oPac);
                oNacido.setAntecedPerinatales(oAP);
                nacidos.add(oNacido);
            }
        } else {
            nacidos.clear();
            FacesMessage msj2 = new FacesMessage("¡Aviso!", "¡Tienes que indicar mínimo 1 nacido o máximo 8!");
            RequestContext.getCurrentInstance().showMessageInDialog(msj2);
        }
    }

    public void registrarRecienNacido(ActionEvent ae) throws Exception {
        String msj = "";
        if (nacidos.size() >= 1) {
            boolean bHabRep = false;
            if (bHabRep == false) {
                msj = oIngresaHospital.ingresaRecienNacidoaHospital(nacidos, 
                        sApellidoPat.toUpperCase(), 
                        oPaciente.getApellidoPaterno().toUpperCase(), 
                        oPaciente.getFolioPac());
                if (msj.indexOf("ERROR") > 0) {
                    FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_FATAL, msj, "");
                    FacesContext.getCurrentInstance().addMessage(null, msj2);
                } else {
                    oRegistrarRecienNacidoJB2 = this;
                    RequestContext.getCurrentInstance().execute("dlg.show()");
                }
            } else {
                msj = "Aviso:¡Los recién nacidos no pueden estar en mismo cunero, verificar repetidos.!";
                FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_FATAL, msj, "");
                FacesContext.getCurrentInstance().addMessage(null, msj2);

            }
        } else {
            msj = "!Aviso:debe registrar al menos 1 niño¡";
            FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_FATAL, msj, "");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
        }
    }

    public void regresarRegRecnNac() throws IOException {
//        IngresaHospitalJB IHJB3=getIngresaHospitalJB2();
//        String archivoAElimina=IHJB3.getRutaPDFContratoIngrPac();
//        ExternalContext extCont= FacesContext.getCurrentInstance().getExternalContext();
//        File folder= new File(extCont.getRealPath("//resources//"));
//        if(!archivoAElimina.equals("")){
//
//                        File archivo= new File(folder, archivoAElimina);
//                        if(archivo.delete()){
//                            archivoAElimina=IHJB3.getRutaPDFDocInfoIngr();
//                        }
//        }
//        if(!archivoAElimina.equals("")){
//
//                        File archivo= new File(folder, archivoAElimina);
//                        if(archivo.delete()){
//                        }
//        }
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.getExternalContext().redirect("registrarRecienNacido.xhtml");
    }

    public void limpia() {
        oPaciente = null;
        bpanelActivo = false;
        oRegistrarRecienNacidoJB2 = null;
        nacidos = new ArrayList<HistoriaClinica>();
        sCantidadNacidos = "";
        sApellidoPat = "";
        oIngresaHospital = null;
    }

    public Paciente getPaciente() {
        return oPaciente;
    }

    public void setPaciente(Paciente oPaciente) {
        this.oPaciente = oPaciente;
    }

    public boolean getPanelActivo() {
        return bpanelActivo;
    }

    public void setPanelActivo(boolean bpanelActivo) {
        this.bpanelActivo = bpanelActivo;
    }

    public RegistrarRecienNacidoJB getRegistrarRecienNacidoJB2() {
        return oRegistrarRecienNacidoJB2;
    }

    public void setRegistrarRecienNacidoJB2(RegistrarRecienNacidoJB value) {
        oRegistrarRecienNacidoJB2 = value;
    }

    public String getCantidadNacidos() {
        return sCantidadNacidos;
    }

    public void setCantidadNacidos(String CantidadNacidos) {
        this.sCantidadNacidos = CantidadNacidos;
    }

    public List<HistoriaClinica> getNacidos() {
        return nacidos;
    }

    public void setNacidos(List<HistoriaClinica> nacidos) {
        this.nacidos = nacidos;
    }

    public String getApellidoPat() {
        return sApellidoPat;
    }

    public void setApellidoPat(String apellidoPat) {
        this.sApellidoPat = apellidoPat;
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

    public HistoriaClinica getIngresaHospital() {
        return oIngresaHospital;
    }

    public void setIngresaHospital(HistoriaClinica oIngresaHospital) {
        this.oIngresaHospital = oIngresaHospital;
    }
}
