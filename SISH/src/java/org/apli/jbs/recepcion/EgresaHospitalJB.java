package org.apli.jbs.recepcion;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.apli.jbs.utilidades.DocsPDFJB;
import org.apli.jbs.PacienteJB;
import org.apli.modelbeans.EpisodioMedico;
import org.apli.modelbeans.HistoriaClinica;
import org.apli.modelbeans.Paciente;
import org.apli.modelbeans.ServicioPrestado;
import org.primefaces.context.RequestContext;

/**
 *
 * @author ERJI
 */
@ManagedBean(name = "oEgrHosp")
@SessionScoped
public class EgresaHospitalJB implements Serializable{
    private Paciente oPaciente = null;
    private boolean bpanelActivo = false;
    private int nNumServAuxi = 0;
    private String sEstudiosResultados = "";
    private EgresaHospitalJB oEgresaHospitalJB2;
    private boolean bEncuesta = false;
    private boolean bTipoAlta = false;
    private String sTipoAlta;
    private HistoriaClinica oHC;
    private String sRutaPaseSalida;
    private String sRutaEncuestaHospitalYCheckList;
    private Date dFechaSalida;
    private ServicioPrestado oServicioPrestado;

    private final int particular=0;
    private final int empresa=1;

    public void selectPacienteHospExi() throws Exception {
        limpia();
        oPaciente = new PacienteJB().getPacienteSesion();
        if (oPaciente != null) {
            oHC = new HistoriaClinica();
            EpisodioMedico oEpiMed = new EpisodioMedico();
            ServicioPrestado oServPrest = new ServicioPrestado();
            oEpiMed.setServicioPrestado(oServPrest);
            oHC.setEpisodioMedico(oEpiMed);
            if (oHC.getEpisodioMedico().buscaPacienteHospitalizadoParaEgresar(oPaciente.getFolioPac())) {
                 oHC.getEpisodioMedico().getServicioPrestado().setEpisodioMedico(oHC.getEpisodioMedico());
                 oHC.getEpisodioMedico().getServicioPrestado().setPaciente(oPaciente);
                if (oHC.getEpisodioMedico().getServicioPrestado().buscaInformacionExtraPaseSalida()) {
                    
                    bpanelActivo = true;
                    if (oHC.getEpisodioMedico().getTipoAlta().equals("0")) {
                        bTipoAlta = false;
                        sTipoAlta = "NO";
                    }
                    if (oHC.getEpisodioMedico().getTipoAlta().equals("1")) {
                        sTipoAlta = "Alta voluntaria";
                    }
                    if (oHC.getEpisodioMedico().getTipoAlta().equals("2")) {
                        sTipoAlta = "Alta por traslado";
                    }
                    if (oHC.getEpisodioMedico().getTipoAlta().equals("3")) {
                        sTipoAlta = "Defunción";
                    }
                    nNumServAuxi = oHC.getEpisodioMedico().getServicioPrestado().buscaServiciosAuxiliaresDeDiag(oPaciente.getFolioPac());
                    
                    System.out.println("servicios realizados a "+oPaciente.getFolioPac()+" "+nNumServAuxi);
                    if (nNumServAuxi == 0) {
                        sEstudiosResultados = "No requiere.";
                    }
                    if (nNumServAuxi > 0 && oHC.getEpisodioMedico().getServicioPrestado().getQuienPaga() == particular) {
                        sEstudiosResultados = "Entregar a paciente.";
                    }
                    if (nNumServAuxi > 0 && oHC.getEpisodioMedico().getServicioPrestado().getQuienPaga() == empresa) {
                        sEstudiosResultados = "Entregar a Aseguradora/Banco/Empresa.";
                    }
                    if(nNumServAuxi > 0){
                        sEstudiosResultados = "Requiere.";
                    }
                   
                } else {
                    FacesMessage msj2 = new FacesMessage("¡Aviso!", "¡El paciente : " + oPaciente.getNombreCompleto() + " con folio: " + oPaciente.getFolioPac() + " aun tiene servicios que no han sido pagados/autorizados!");
                    RequestContext.getCurrentInstance().showMessageInDialog(msj2);
                }
            } else {
                FacesMessage msj2 = new FacesMessage("¡Aviso!", "¡El paciente : " + oPaciente.getNombreCompleto() + " con folio: " + oPaciente.getFolioPac() + " no está listo para ser dado de alta o ya fue dado de alta!");
                RequestContext.getCurrentInstance().showMessageInDialog(msj2);
            }
        } else {
            FacesMessage msj2 = new FacesMessage("¡Aviso!", "¡No has seleccionado un paciente!");
            RequestContext.getCurrentInstance().showMessageInDialog(msj2);
        }

    }

    public void egresarHospital() throws Exception {

        boolean validacionServicio = true;
        dFechaSalida = new Date();
        String msj = oHC.egresaPacienteHospital(oHC.getEpisodioMedico().getCveepisodio(), nNumServAuxi,dFechaSalida);

        if (msj.indexOf("ERROR") > 0) {
            validacionServicio = false;
        }
        if (validacionServicio == false) {
            FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_FATAL, msj, "");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
        } else {
            this.oEgresaHospitalJB2 = this;
            RequestContext.getCurrentInstance().execute("dlg.show()");
        }
    }

    public void creaPaseSalida() throws Exception {
        DocsPDFJB oDPDF = new DocsPDFJB();
        String tipoPac="";
        ServicioPrestado []oServiciosPrestados = null;
        oServicioPrestado= new ServicioPrestado();
                
        if (oHC.getEpisodioMedico().getServicioPrestado().getQuienPaga()==0) {
            tipoPac="Particular";
        }
        if (oHC.getEpisodioMedico().getServicioPrestado().getQuienPaga()==1) {
            tipoPac="Empresa";
        }
        if (oHC.getEpisodioMedico().getServicioPrestado().getQuienPaga()==2) {
            tipoPac="PAQUETE";
        }
        
        if( sEstudiosResultados.equals("Requiere.")){
            int i;
            List<ServicioPrestado>serviciosPrestados;
            oServicioPrestado.setEpisodioMedico(oHC.getEpisodioMedico());
            int tamServEsc=0;
            
            //obtiene la descripcion de los servicios prestados y su numero de linea de ingreso
            serviciosPrestados=oServicioPrestado.buscaServiciosPrestadosYLineaIngresoPorEpisodioMed();
            int nTam = serviciosPrestados.size();
            
            ServicioPrestado[] oServPrestTemp = new ServicioPrestado[nTam];
            
            for (i=0; i<nTam; i++){
                oServPrestTemp[i] = serviciosPrestados.get(i);    
                //lineas de ingreso 5 a 8, 22, 23, 29, 33, 35, 39 o 41
                int lineaIngr=oServPrestTemp[i].getLineaIngreso();
                if(lineaIngr==5 || 
                   lineaIngr==6 || 
                   lineaIngr==7 || 
                   lineaIngr==8 || 
                   lineaIngr==22 || 
                   lineaIngr==23 || 
                   lineaIngr==29 || 
                   lineaIngr==33 || 
                   lineaIngr==35 || 
                   lineaIngr==39 || 
                   lineaIngr==41)
                {
                    tamServEsc++;
    }
            }

            for (i=0; i<nTam; i++){
                System.out.println("serviciod "+oServPrestTemp[i].getLineaIngreso()+
                        ", "+oServPrestTemp[i].getConcepPrestado().getDescripConcep());                
            }
            
            oServiciosPrestados = new ServicioPrestado[tamServEsc];
            
            int cur=0;
            for (i = 0; i < tamServEsc; i++) {
                int lineaIngr=oServPrestTemp[i].getLineaIngreso();
                if(lineaIngr==5 || 
                   lineaIngr==6 || 
                   lineaIngr==7 || 
                   lineaIngr==8 || 
                   lineaIngr==22 || 
                   lineaIngr==23 || 
                   lineaIngr==29 || 
                   lineaIngr==33 || 
                   lineaIngr==35 || 
                   lineaIngr==39 || 
                   lineaIngr==41)
                {
                    oServiciosPrestados[cur++]=oServPrestTemp[i];
                }
            }
        }
        
        sRutaPaseSalida = oDPDF.creaPaseSalida(oPaciente.getNombreCompleto(),
                tipoPac,
                (oHC.getEpisodioMedico().getServicioPrestado().getControlTv()) ? "Si." : "No aplica.", 
                (oHC.getEpisodioMedico().getServicioPrestado().getControlClima()) ? "Si." : "No aplica.", 
                sEstudiosResultados, oHC.getEpisodioMedico().getServicioPrestado().getIdFolio(), 
                oHC.getEpisodioMedico().getHab().getHab(),
                oHC.getEpisodioMedico().getCveepisodio(), 
                dFechaSalida,oServiciosPrestados);
    }

    public void creaEncuestaHospitalYCheckList() throws Exception {
        DocsPDFJB oDPDF = new DocsPDFJB();
        this.sRutaEncuestaHospitalYCheckList = oDPDF.creaEncuestaHospitalYCheckList(oHC.getEpisodioMedico().getCveepisodio(), oPaciente.getNombreCompleto(), bEncuesta, bTipoAlta, oHC.getEpisodioMedico().getHab().getHab(),oHC.getEpisodioMedico().getInicio(),dFechaSalida);
    }

    public void regresarEgresar() throws IOException {
        EgresaHospitalJB EHJB3 = getEgresaHospitalJB2();
        String archivoAElimina = EHJB3.getRutaPaseSalida();
        ExternalContext extCont = FacesContext.getCurrentInstance().getExternalContext();
        File folder = new File(extCont.getRealPath("//resources//"));
        if (archivoAElimina!=null && !archivoAElimina.equals("")) {
            File archivo = new File(folder, archivoAElimina);
            if (archivo.delete()) {
                archivoAElimina = EHJB3.getRutaEncuestaHospital();
            }
        }
        if (archivoAElimina!= null && !archivoAElimina.equals("")) {
            File archivo = new File(folder, archivoAElimina);
            if (archivo.delete()) {
            }
        }
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.getExternalContext().redirect("egresarHospital.xhtml");
    }

    public void limpia() {
        oPaciente = null;
        bpanelActivo = false;
        nNumServAuxi = 0;
        sEstudiosResultados = "";
        oEgresaHospitalJB2 = null;
    }

    public boolean validaBotonCheck() {
        boolean valida = false;
        if (this.bEncuesta || this.bTipoAlta) {
            valida = true;
        }
        return valida;
    }

    public EgresaHospitalJB getEgresaHospitalJB2() {
        return oEgresaHospitalJB2;
    }

    public void setEgresaHospitalJB2(EgresaHospitalJB oEgresaHospitalJB2) {
        this.oEgresaHospitalJB2 = oEgresaHospitalJB2;
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

    public String getEstudiosResultados() {
        return sEstudiosResultados;
    }

    public void setEstudiosResultados(String sEstudiosResultados) {
        this.sEstudiosResultados = sEstudiosResultados;
    }

    public boolean getBTipoAlta() {
        return bTipoAlta;
    }

    public void setBTipoAlta(boolean bTipoAlta) {
        this.bTipoAlta = bTipoAlta;
    }

    public boolean getEncuesta() {
        return bEncuesta;
    }

    public void setEncuesta(boolean bEncuesta) {
        this.bEncuesta = bEncuesta;
    }

    public String getSTipoAlta() {
        return sTipoAlta;
    }

    public void setSTipoAlta(String sTipoAlta) {
        this.sTipoAlta = sTipoAlta;
    }

    public HistoriaClinica getHC() {
        return oHC;
    }

    public void setHC(HistoriaClinica oHC) {
        this.oHC = oHC;
    }

    public String getRutaPaseSalida() {
        return sRutaPaseSalida;
    }

    public void setRutaPaseSalida(String sRutaPaseSalida) {
        this.sRutaPaseSalida = sRutaPaseSalida;
    }

    public String getRutaEncuestaHospital() {
        return sRutaEncuestaHospitalYCheckList;
    }

    public void setRutaEncuestaHospital(String sRutaEncuestaHospital) {
        this.sRutaEncuestaHospitalYCheckList = sRutaEncuestaHospital;
    }

}
