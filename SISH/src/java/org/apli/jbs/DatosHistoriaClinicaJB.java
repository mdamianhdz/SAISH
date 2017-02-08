package org.apli.jbs;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apli.jbs.ventaCredito.FormatoCiaCredJB;
import org.apli.modelbeans.Ciudad;
import org.apli.modelbeans.DatosLaborales;
import org.apli.modelbeans.Diagnostico;
import org.apli.modelbeans.EpisodioMedico;
import org.apli.modelbeans.Estado;
import org.apli.modelbeans.EstadoCivil;
import org.apli.modelbeans.HistoriaClinica;
import org.apli.modelbeans.Medico;
import org.apli.modelbeans.MetodoAnticonceptivo;
import org.apli.modelbeans.Municipio;
import org.apli.modelbeans.Paciente;
import org.apli.modelbeans.ventaCredito.CompaniaCred;
import org.apli.modelbeans.ventaCredito.FormatoCiaCred;

/**
 *
 * @author Usuario5
 */

@ManagedBean(name="oDatosHC")
@ViewScoped
public class DatosHistoriaClinicaJB implements Serializable{
    private HistoriaClinica oHC;
    private List<Diagnostico> listDiag;
    private List<EstadoCivil> listEC;
    private List<Medico> listMed;
    private List<MetodoAnticonceptivo> listMetA;
    private List<CompaniaCred> listCompanias;
    private String[] selectedMA;
    private Diagnostico oDiag;
    private EstadoCivil oEC;
    private Medico oMed;
    private MetodoAnticonceptivo oMA;
    private EpisodioMedico oEM;
    private boolean bAFCard=false;
    private boolean bAFCancer=false;
    private boolean bAFIAM=false;
    private boolean bAFEnf=false;
    private boolean bAFLiti=false;
    private boolean bAFAsma=false;
    private boolean bAFDiab=false;
    private boolean bAFHAS=false;
    private boolean bAFConv=false;
    private boolean bAFAltM=false;
    private boolean bAPQuir=false;
    private boolean bAPHosp=false;
    private boolean bAPTransf=false;
    private boolean bAPAler=false;
    private boolean bAPTraum=false;
    private boolean bAPHTA=false;
    private boolean bAPDiab=false;
    private boolean bAPCard=false;
    private boolean bANPAnimales=false;
    private boolean bDLBenef=false;
    private boolean bDLEmpesa=false;
    private boolean bRenderAF=false;
    private boolean bRenderAP=false;
    private boolean bRenderANP=false;
    private boolean bRenderID=false;
    private boolean bRenderGineco=false;
    private boolean bRenderPerinat=false;
    private boolean bRenderNutricio=false;
    private boolean bRenderDesarrollo=false;
    private boolean bRenderLaborales=false;
    private boolean bDisID=false;
    private boolean bDisMenorEdad=false;
    private String mess;
    private int nEdad;
    private String sFormaPago;
    private boolean bDisCompania=true;
    private List<FormatoCiaCred> listFormatos;
    private FormatoCiaCred selSelectedFmt;
    private FormatoCiaCred dtSelectedFmt;
   
    public DatosHistoriaClinicaJB() throws Exception{
        oHC=new HistoriaClinica();
        oHC=oHC.buscaHC(new Paciente());   
    }
    
    public void llena(){
    Paciente oPaciente;
        oPaciente=new PacienteJB().getPacienteSesion();
        if (oPaciente != null)
            oHC.setPaciente(oPaciente);
        try{
            if (oHC.getPaciente().getFolioPac()==0 && 
                    new PacienteJB().getPacienteSesion()==null)
                oHC=oHC.buscaHC(new Paciente().buscaUltimoPacienteReg());
            else
                oHC=oHC.buscaHC(new PacienteJB().getPacienteSesion());
            if (oHC.getEpisodioCC().getCompCred().getIdEmp()>0){
                sFormaPago="CRED";
                listFormatos=new FormatoCiaCred().buscaFormatos(oHC.getEpisodioCC().getCompCred().getIdEmp());
                FormatoCiaCredJB.formatos=listFormatos;
                for (int i = 0; i < oHC.getEpisodioCC().getFormatos().size(); i++) {
                    for (int j = listFormatos.size()-1; j >=0 ; j--) {
                        if (oHC.getEpisodioCC().getFormatos().get(i).getIdFmt()==listFormatos.get(j).getIdFmt())
                            listFormatos.remove(j);
                    }
                }
            }else
                sFormaPago="CONT";
            this.validaFormaPago();
            habilitaDatos();
            getDiagnosticos();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void nuevoPac() throws Exception{
        oHC=oHC.buscaHC(new Paciente());
        this.bRenderPerinat=false;
        this.bRenderNutricio=false;
        this.bRenderDesarrollo=false;
        this.bRenderGineco=false;
        this.bRenderAF=false;
        this.bRenderAP=false;
        this.bRenderANP=false;
        this.bRenderID=false;
    }
    
    public HistoriaClinica getHistoriaClinica(){
        return oHC;
    }
    
    public void setHistoriaClinica(HistoriaClinica oHC){
        this.oHC=oHC;
    }
    
    public void setSelectedMA(String[] selectedMA){
        this.selectedMA=selectedMA;
    }
    
    public String[] getSelectedMA(){
        return selectedMA;
    }
    
    public List<EstadoCivil> getEdoCivil() throws Exception{
        listEC=new ArrayList();
        oEC=new EstadoCivil();
        listEC=oEC.buscaTodos();
        return listEC;
    }
    
    public List<Medico> getMedicos() throws Exception{
        listMed=new ArrayList();
        oMed=new Medico();
        listMed=oMed.buscaTodos();
        return listMed;
    }
    
    public List<MetodoAnticonceptivo> getMetodos() throws Exception{
        listMetA=new ArrayList();
        oMA=new MetodoAnticonceptivo();
        listMetA=oMA.buscaTodos();
        return listMetA;
    }
    
    public List<Diagnostico> getDiagnosticos() throws Exception{
        listDiag=new ArrayList();
        oDiag=new Diagnostico();
        listDiag=oDiag.buscaTodos();
        return listDiag;
    }

    public List<CompaniaCred> getListEmpresas() throws Exception{
        return new CompaniaCred().buscaEmpresas();
    }
    
    public List<CompaniaCred> getListCompanias(){
        return listCompanias;
    }
    
    public void setListCompanias(List<CompaniaCred> listCompanias){
        this.listCompanias=listCompanias;
    }
    
    public List<FormatoCiaCred> getListFormatos(){
        return listFormatos;
    }
    
    public void setListFormatos(List<FormatoCiaCred> listFormatos){
        this.listFormatos=listFormatos;
    }
    
    public boolean isAFCard() {
        return bAFCard;
    }

    public void setAFCard(boolean bAFCard) {
        this.bAFCard = bAFCard;
    }

    public boolean isAFCancer() {
        return bAFCancer;
    }

    public void setAFCancer(boolean bAFCancer) {
        this.bAFCancer = bAFCancer;
    }

    public boolean isAFIAM() {
        return bAFIAM;
    }

    public void setAFIAM(boolean bAFIAM) {
        this.bAFIAM = bAFIAM;
    }

    public boolean isAFEnf() {
        return bAFEnf;
    }

    public void setAFEnf(boolean bAFEnf) {
        this.bAFEnf = bAFEnf;
    }

    public boolean isAFLiti() {
        return bAFLiti;
    }

    public void setAFLiti(boolean bAFLiti) {
        this.bAFLiti = bAFLiti;
    }

    public boolean isAFAsma() {
        return bAFAsma;
    }

    public void setAFAsma(boolean bAFAsma) {
        this.bAFAsma = bAFAsma;
    }

    public boolean isAFDiab() {
        return bAFDiab;
    }

    public void setAFDiab(boolean bAFDiab) {
        this.bAFDiab = bAFDiab;
    }

    public boolean isAFHAS() {
        return bAFHAS;
    }

    public void setAFHAS(boolean bAFHAS) {
        this.bAFHAS = bAFHAS;
    }

    public boolean isAFConv() {
        return bAFConv;
    }

    public void setAFConv(boolean bAFConv) {
        this.bAFConv = bAFConv;
    }

    public boolean isAFAltM() {
        return bAFAltM;
    }

    public void setAFAltM(boolean bAFAltM) {
        this.bAFAltM = bAFAltM;
    }

    public boolean isAPQuir() {
        return bAPQuir;
    }

    public void setAPQuir(boolean bAPQuir) {
        this.bAPQuir = bAPQuir;
    }

    public boolean isAPHosp() {
        return bAPHosp;
    }

    public void setAPHosp(boolean bAPHosp) {
        this.bAPHosp = bAPHosp;
    }

    public boolean isAPTransf() {
        return bAPTransf;
    }

    public void setAPTransf(boolean bAPTransf) {
        this.bAPTransf = bAPTransf;
    }

    public boolean isAPAler() {
        return bAPAler;
    }

    public void setAPAler(boolean bAPAler) {
        this.bAPAler = bAPAler;
    }

    public boolean isAPTraum() {
        return bAPTraum;
    }

    public void setAPTraum(boolean bAPTraum) {
        this.bAPTraum = bAPTraum;
    }

    public boolean isAPHTA() {
        return bAPHTA;
    }

    public void setAPHTA(boolean bAPHTA) {
        this.bAPHTA = bAPHTA;
    }

    public boolean isAPDiab() {
        return bAPDiab;
    }

    public void setAPDiab(boolean bAPDiab) {
        this.bAPDiab = bAPDiab;
    }

    public boolean isAPCard() {
        return bAPCard;
    }

    public void setAPCard(boolean bAPCard) {
        this.bAPCard = bAPCard;
    }
    
    public boolean isANPAnimales() {
        return bANPAnimales;
    }

    public void setANPAnimales(boolean bANPAnimales) {
        this.bANPAnimales = bANPAnimales;
    }
    
    public boolean isDLBenef() {
        return bDLBenef;
    }

    public void setDLBenef(boolean bDLBenef) {
        if (bDLBenef==false)
            oHC.getPaciente().getDatosLaborales().setNomEmpleado("");
        this.bDLBenef = bDLBenef;
    }
    
    public boolean isDLEmpesa() {
        return bDLEmpesa;
    }

    public void setDLEmpesa(boolean bDLEmpesa) {
        this.bDLEmpesa = bDLEmpesa;
    }
    
    public boolean isRenderGineco() {
        return bRenderGineco;
    }

    public void setRenderGineco(boolean bRenderGineco) {
        this.bRenderGineco = bRenderGineco;
    }

    public boolean isRenderPerinat() {
        return bRenderPerinat;
    }

    public void setRenderPerinat(boolean bRenderPerinat) {
        this.bRenderPerinat = bRenderPerinat;
    }

    public boolean isRenderNutricio() {
        return bRenderNutricio;
    }

    public void setRenderNutricio(boolean bRenderNutricio) {
        this.bRenderNutricio = bRenderNutricio;
    }

    public boolean isRenderDesarrollo() {
        return bRenderDesarrollo;
    }

    public void setRenderDesarrollo(boolean bRenderDesarrollo) {
        this.bRenderDesarrollo = bRenderDesarrollo;
    }
    
    public boolean isRenderLaborales() {
        return bRenderLaborales;
    }

    public void setRenderLaborales(boolean bRenderLaborales) {
        this.bRenderLaborales = bRenderLaborales;
    }

    public boolean isRenderAF() {
        return bRenderAF;
    }

    public void setRenderAF(boolean bRenderAF) {
        this.bRenderAF = bRenderAF;
    }

    public boolean isRenderAP() {
        return bRenderAP;
    }

    public void setRenderAP(boolean bRenderAP) {
        this.bRenderAP = bRenderAP;
    }

    public boolean isRenderANP() {
        return bRenderANP;
    }

    public void setRenderANP(boolean bRenderANP) {
        this.bRenderANP = bRenderANP;
    }

    public boolean isRenderID() {
        return bRenderID;
    }

    public void setRenderID(boolean bRenderID) {
        this.bRenderID = bRenderID;
    }

    public boolean isDisID() {
        return bDisID;
    }

    public void setDisID(boolean bDisID) {
        this.bDisID = bDisID;
    }

    public boolean isDisMenorEdad() {
        return bDisMenorEdad;
    }

    public void setDisMenorEdad(boolean bDisMenorEdad) {
        this.bDisMenorEdad = bDisMenorEdad;
    }

    public String getFormaPago() {
        return sFormaPago;
    }

    public void setFormaPago(String sFormaPago) {
        this.sFormaPago = sFormaPago;
    }
    
    public boolean isDisCompania() {
        return bDisCompania;
    }

    public void setDisCompania(boolean bDisCompania) {
        this.bDisCompania = bDisCompania;
    }

    public void setSelSelectedFmt(FormatoCiaCred value){
        selSelectedFmt=value;
    }
    
    public FormatoCiaCred getSelSelectedFmt(){
        return this.selSelectedFmt;
    }
    
    public void setDtSelectedFmt(FormatoCiaCred value){
        dtSelectedFmt=value;
    }
    
    public FormatoCiaCred getDtSelectedFmt(){
        return this.dtSelectedFmt;
    }
    
    public boolean selFormula(String val){
        if (this.getEdad()<18){
            if (val.equals("S"))
                return false;
            else
                return true;
        }else
            return true;
    }
    
    public boolean selDiag(String val){
        if (this.getEdad()<18){
            if (val.equals("N"))
                return false;
            else
                return true;
        }else
            return true;
    }
    
    public int getEdad(){
        calculaEdad();
        return this.nEdad;
    }
    
    public void setEdad(int nEdad){
        this.nEdad=nEdad;
    }
    
    public void buscaCD() throws Exception{
        String[] arrDatos=new String[6];
        if(oHC.getPaciente().getCP().equals("")){
            oHC.getPaciente().setEdo(new Estado());
            oHC.getPaciente().setMpio(new Municipio());
            oHC.getPaciente().setCiudad(new Ciudad());
        }else{
            arrDatos=oHC.getPaciente().buscaCiudad(oHC.getPaciente().getCP());
            oHC.getPaciente().getEdo().setCve(arrDatos[0]);
            oHC.getPaciente().getEdo().setDescrip(arrDatos[1]);
            oHC.getPaciente().getMpio().setCve(arrDatos[2]);
            oHC.getPaciente().getMpio().setDescrip(arrDatos[3]);
            oHC.getPaciente().getCiudad().setCve(arrDatos[4]);
            oHC.getPaciente().getCiudad().setDescrip(arrDatos[5]);
        }
    }

    public void guardaDatosGrales(){
        if (oHC.getPaciente().getApellidoPaterno().equals("")||
                oHC.getPaciente().getNombre().equals("")||
                oHC.getPaciente().getNac() == null || 
                oHC.getPaciente().getGenero()=='X'|| 
                oHC.getPaciente().getGenero()==' ' ||
                oHC.getEpisodioMedico().getMedRecibe() == null ||
                oHC.getEpisodioMedico().getMedRecibe().getFolioPers()==0){
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage("Error de validación",
                    "Verifique que se proporcionaron todos los datos obligatorios"));
        }else{
            try{
                mess=oHC.guardaDatosGenerales(oHC);
                this.llena();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Guardar",mess));
            }catch(Exception e){
                e.printStackTrace();
                FacesContext.getCurrentInstance().addMessage(null, 
                        new FacesMessage("Guardar","Error al guardar datos del paciente"));
            }
        }
    }
    
    public void guardaDatosLaborales()throws IOException, Exception{
        mess=new DatosLaborales().guardaDatosLaborales(oHC.getPaciente());
        this.llena();
        FacesContext context= FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Guardar",mess));
        context.getExternalContext().getFlash().setKeepMessages(true);
    }
    
    public void guardaAntFam()throws IOException, Exception{
        mess=oHC.m_AntecedentesHeredoFamiliares.guardaAntecedentesFam(oHC.getPaciente().getFolioPac(),oHC.m_AntecedentesHeredoFamiliares);
        this.llena();
        FacesContext context= FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Guardar",mess));
        context.getExternalContext().getFlash().setKeepMessages(true);
    }
    
    public void guardaAntPatol()throws IOException, Exception{
        mess=oHC.m_AntecedPersPatol.guardaAntecedentesPatol(oHC.getPaciente().getFolioPac(),oHC.m_AntecedPersPatol);
        this.llena();
        FacesContext context= FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Guardar",mess));
        context.getExternalContext().getFlash().setKeepMessages(true);
    }
    
    public void guardaAntNoPatol()throws IOException, Exception{
        mess=oHC.m_AntecedentesNoPatol.guardaAntecedentesNoPatol(oHC.getPaciente().getFolioPac(),oHC.m_AntecedentesNoPatol);
        this.llena();
        FacesContext context= FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Guardar",mess));
        context.getExternalContext().getFlash().setKeepMessages(true);
    }
    
    public void guardaAntGineco()throws IOException, Exception{
        mess=oHC.m_AntecedentesGinecoObs.guardaAntecedentesGinecoObs(oHC.getPaciente().getFolioPac(),oHC.m_AntecedentesGinecoObs, selectedMA);
        this.llena();
        FacesContext context= FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Guardar",mess));
        context.getExternalContext().getFlash().setKeepMessages(true);
    }   
    
    public void guardaAntPerinat()throws IOException, Exception{
        mess=oHC.m_AntecedPerinatales.guardaAntecedentesPerinat(oHC.getPaciente().getFolioPac(),oHC.m_AntecedPerinatales);
        this.llena();
        FacesContext context= FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Guardar",mess));
        context.getExternalContext().getFlash().setKeepMessages(true);
    }
    
    public void guardaAntNutri()throws IOException, Exception{
        mess=oHC.m_AntecedNutricio.guardaAntecedentesNutricio(oHC.getPaciente().getFolioPac(),oHC.m_AntecedNutricio);
        this.llena();
        FacesContext context= FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Guardar",mess));
        context.getExternalContext().getFlash().setKeepMessages(true);
    }
    
    public void guardaDesarrollo()throws IOException, Exception{
        mess=oHC.m_Desarrollo.guardaDesarrollo(oHC.getPaciente().getFolioPac(),oHC.m_Desarrollo);
        this.llena();
        FacesContext context= FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Guardar",mess));
        context.getExternalContext().getFlash().setKeepMessages(true);
    }
    
    public void guardaImpDiag()throws IOException, Exception{
        oEM=new EpisodioMedico();
        mess=oEM.guardaImpresionDiag(oHC.getPaciente().getFolioPac(), oHC.getEpisodioMedico());
        this.llena();
        FacesContext context= FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Guardar",mess));
        context.getExternalContext().getFlash().setKeepMessages(true);
    }
    
    public void regresa() throws IOException{
        FacesContext context= FacesContext.getCurrentInstance();
        context.getExternalContext().redirect("datosPaciente.xhtml");
    }
    
    public void cancela() throws IOException{
        FacesContext context= FacesContext.getCurrentInstance();
        context.getExternalContext().redirect("../login/menuOpe.xhtml");
    }
    
    public void habilitaDatos() throws Exception{
        //Antecedentes Heredo-Familiares
        if(oHC.getAntecedentesHeredoFamiliares().getFamCardiopatias() == null || "".equals(oHC.getAntecedentesHeredoFamiliares().getFamCardiopatias()))
           this.bAFCard=false;
        else 
            this.bAFCard=true;
        if(oHC.getAntecedentesHeredoFamiliares().getCancer()== null ||"".equals(oHC.getAntecedentesHeredoFamiliares().getCancer()))
           this.bAFCancer=false;
        else 
            this.bAFCancer=true; 
        if(oHC.getAntecedentesHeredoFamiliares().getIAM()== null ||"".equals(oHC.getAntecedentesHeredoFamiliares().getIAM()))
           this.bAFIAM=false;
        else 
            this.bAFIAM=true;  
        if(oHC.getAntecedentesHeredoFamiliares().getEnfisema()== null ||"".equals(oHC.getAntecedentesHeredoFamiliares().getEnfisema()))
           this.bAFEnf=false;
        else 
            this.bAFEnf=true; 
        if(oHC.getAntecedentesHeredoFamiliares().getLitiasis()== null ||"".equals(oHC.getAntecedentesHeredoFamiliares().getLitiasis()))
           this.bAFLiti=false;
        else 
            this.bAFLiti=true; 
        if(oHC.getAntecedentesHeredoFamiliares().getAsma()== null ||"".equals(oHC.getAntecedentesHeredoFamiliares().getAsma()))
           this.bAFAsma=false;
        else 
            this.bAFAsma=true; 
        if(oHC.getAntecedentesHeredoFamiliares().getDiabetes()== null ||"".equals(oHC.getAntecedentesHeredoFamiliares().getDiabetes()))
           this.bAFDiab=false;
        else 
            this.bAFDiab=true; 
        if(oHC.getAntecedentesHeredoFamiliares().getHAS()== null ||"".equals(oHC.getAntecedentesHeredoFamiliares().getHAS()))
           this.bAFHAS=false;
        else 
            this.bAFHAS=true; 
        if(oHC.getAntecedentesHeredoFamiliares().getConvulsiones()== null ||"".equals(oHC.getAntecedentesHeredoFamiliares().getConvulsiones()))
           this.bAFConv=false; 
        else 
            this.bAFConv=true;
        if(oHC.getAntecedentesHeredoFamiliares().getAltMentales()== null ||"".equals(oHC.getAntecedentesHeredoFamiliares().getAltMentales()))
           this.bAFAltM=false; 
        else 
            this.bAFAltM=true;
        //Antecedentes Patológicos
        if(oHC.getAntecedPersPatol().getQx()== null ||"".equals(oHC.getAntecedPersPatol().getQx()))
           this.bAPQuir=false; 
        else 
            this.bAPQuir=true;
        if(oHC.getAntecedPersPatol().getHosp()== null ||"".equals(oHC.getAntecedPersPatol().getHosp()))
           this.bAPHosp=false; 
        else 
            this.bAPHosp=true;
        if(oHC.getAntecedPersPatol().getTransf()== null ||"".equals(oHC.getAntecedPersPatol().getTransf()))
           this.bAPTransf=false; 
        else 
            this.bAPTransf=true;
        if(oHC.getAntecedPersPatol().getAlergicos()== null ||"".equals(oHC.getAntecedPersPatol().getAlergicos()))
           this.bAPAler=false; 
        else 
            this.bAPAler=true;
        if(oHC.getAntecedPersPatol().getTraumat()== null ||"".equals(oHC.getAntecedPersPatol().getTraumat()))
           this.bAPTraum=false;
        else 
            this.bAPTraum=true; 
        if(oHC.getAntecedPersPatol().getHTA()== null ||"".equals(oHC.getAntecedPersPatol().getHTA()))
           this.bAPHTA=false; 
        else 
            this.bAPHTA=true;
        if(oHC.getAntecedPersPatol().getDiabetes()== null ||"".equals(oHC.getAntecedPersPatol().getDiabetes()))
           this.bAPDiab=false; 
        else 
            this.bAPDiab=true;
        if(oHC.getAntecedPersPatol().getCardiopatias()== null ||"".equals(oHC.getAntecedPersPatol().getCardiopatias()))
           this.bAPCard=false; 
        else 
            this.bAPCard=true;
        //Antecedentes No Patológicos
        if(oHC.getAntecedentesNoPatol().getAnimales()== null ||"".equals(oHC.getAntecedentesNoPatol().getAnimales()))
           this.bANPAnimales=false; 
        else 
            this.bANPAnimales=true;
        ///Antecedentes Nutricionales 
        if(!"".equals(oHC.getAntecedNutricio().getNomFormula()) && oHC.getAntecedNutricio().getNomFormula()!=null)
           oHC.getAntecedNutricio().setFormActual("S"); 
        //Datos Laborales
        if(oHC.getPaciente().getDatosLaborales().getNomEmpleado()== null ||"".equals(oHC.getPaciente().getDatosLaborales().getNomEmpleado()) ||"null".equals(oHC.getPaciente().getDatosLaborales().getNomEmpleado())){
           this.bDLBenef=false;
           oHC.getPaciente().getDatosLaborales().setNomEmpleado("");
        }else 
            this.bDLBenef=true;
        if(oHC.getPaciente().getDatosLaborales().getOtraEmpresa()== null ||"".equals(oHC.getPaciente().getDatosLaborales().getOtraEmpresa()) ||"null".equals(oHC.getPaciente().getDatosLaborales().getOtraEmpresa())){
           this.bDLEmpesa=false;
           oHC.getPaciente().getDatosLaborales().setOtraEmpresa("");
        }else 
            this.bDLEmpesa=true;
        //Habilita Pestañas correspondientes
        if(getEdad()<18){
            this.bRenderDesarrollo=true;
            this.bRenderPerinat=true;
            this.bRenderNutricio=true;
            this.bDisMenorEdad=false;
        }else{
            this.bDisMenorEdad=true;
            if (oHC.isPerin()==true)
                this.bRenderPerinat=true;
            else
               this.bRenderPerinat=false; 
            if (oHC.isNutri()==true)
                this.bRenderNutricio=true;
            else
                this.bRenderNutricio=false;
            if (oHC.isDes()==true)
                this.bRenderDesarrollo=true;
            else
                this.bRenderDesarrollo=false;
            
        }
        if(oHC.getPaciente().getGenero()=='F'){
            this.bRenderGineco=true;
            oMA=new MetodoAnticonceptivo();
            this.setSelectedMA(oMA.buscaMAPac(oHC.getPaciente().getFolioPac()));
        }else
            this.bRenderGineco=false;
        this.bRenderAF=true;
        this.bRenderAP=true;
        this.bRenderANP=true;
        this.bRenderID=true;
        this.bRenderLaborales=true;
        //Impresion Diagnóstica
        this.bDisID=true;
        if (oHC.getEpisodioMedico().getAlta()==null){
            oHC.getEpisodioMedico().setReqCons(true);
            this.bDisID=false;
        }
    }
    
    public void calculaEdad(){
        if (oHC.getPaciente().getNac()==null)
            nEdad=0;
        else{
            Calendar cNac=Calendar.getInstance();
            Calendar cHoy=Calendar.getInstance();
            cNac.setTime(oHC.getPaciente().getNac());
            int anio=cHoy.get(Calendar.YEAR)-cNac.get(Calendar.YEAR);
            int mes=cHoy.get(Calendar.MONTH)-cNac.get(Calendar.MONTH);
            int dia=cHoy.get(Calendar.DATE)-cNac.get(Calendar.DATE);
            if(mes<0 || (mes==0 && dia<0))
                anio--;
            nEdad=anio;
        }
    }
    
    public List<Diagnostico> completeDiagnostico(String val){
        ListaDiagnosticoJB.diagnosticos=listDiag;
        List<Diagnostico> diagnosticosFiltrados=new ArrayList();
        for (int i=0; i<listDiag.size(); i++){
            Diagnostico temp=listDiag.get(i);
            if (temp.getDescrip().toUpperCase().startsWith(val))
                diagnosticosFiltrados.add(temp);
        }
        return diagnosticosFiltrados;
    }
    
    public void cambiaEmpresa(){
        if (oHC.getPaciente().getDatosLaborales().getOtraEmpresa().equals("")){
            this.bDLEmpesa=false;
        }else{
            this.bDLEmpesa=true;
            oHC.getPaciente().getDatosLaborales().getEmpresa().setIdEmp(0);
        }
    }
    
    public void validaFormaPago() throws Exception{
        if (sFormaPago.equals("CRED")){
            listCompanias=new CompaniaCred().buscaTodasCompanias();
            bDisCompania=false;
        }else{
            bDisCompania=true;
            listCompanias=new ArrayList();
            listFormatos=new ArrayList();
            oHC.getEpisodioCC().setFormatos(new ArrayList());
        }
    }
    
    public void validaCompania() throws Exception{
        listFormatos=new FormatoCiaCred().buscaFormatos(oHC.getEpisodioCC().getCompCred().getIdEmp());
        FormatoCiaCredJB.formatos=listFormatos;
    }
    
    public void agregarDocto() throws Exception{
        if (selSelectedFmt==null)
            mess="Seleccione el documento que desea agregar";
        else{
            oHC.getEpisodioCC().getFormatos().add(selSelectedFmt);
            listFormatos.remove(selSelectedFmt);
        }
        if (!mess.equals("")){
            FacesContext context= FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Agregar Documento",mess));
            context.getExternalContext().getFlash().setKeepMessages(true);
        }
    }
    
    public void eliminarDocto() throws Exception{
        if (dtSelectedFmt==null)
            mess="Seleccione el documento que desea eliminar";
        else{
            oHC.getEpisodioCC().getFormatos().remove(dtSelectedFmt);
            listFormatos.add(dtSelectedFmt);
        }
        if (!"".equals(mess)){
            FacesContext context= FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Eliminar Documento",mess));
            context.getExternalContext().getFlash().setKeepMessages(true);
        } 
    }
    
    public void validaAntPato(){
        if (!bAPQuir)
            oHC.getAntecedPersPatol().setQx("");
        if (!bAPHosp)
            oHC.getAntecedPersPatol().setHosp("");
        if (!bAPTransf)
            oHC.getAntecedPersPatol().setTransf("");
        if (!bAPAler)
            oHC.getAntecedPersPatol().setAlergicos("");
        if (!bAPTraum)
            oHC.getAntecedPersPatol().setTraumat("");
        if (!bAPHTA)
            oHC.getAntecedPersPatol().setHTA("");
        if (!bAPDiab)
            oHC.getAntecedPersPatol().setDiabetes("");
        if (!bAPCard)
            oHC.getAntecedPersPatol().setCardiopatias("");
    }
    
    public void validaAntFam(){
        if (!bAFCard)
            oHC.getAntecedentesHeredoFamiliares().setFamCardiopatias("");
        if (!bAFCancer)
            oHC.getAntecedentesHeredoFamiliares().setCancer("");
        if (!bAFIAM)
            oHC.getAntecedentesHeredoFamiliares().setIAM("");
        if (!bAFEnf)
            oHC.getAntecedentesHeredoFamiliares().setEnfisema("");
        if (!bAFLiti)
            oHC.getAntecedentesHeredoFamiliares().setLitiasis("");
        if (!bAFAsma)
            oHC.getAntecedentesHeredoFamiliares().setAsma("");
        if (!bAFDiab)
            oHC.getAntecedentesHeredoFamiliares().setDiabetes("");
        if (!bAFHAS)
            oHC.getAntecedentesHeredoFamiliares().setHAS("");
        if (!bAFConv)
            oHC.getAntecedentesHeredoFamiliares().setConvulsiones("");
        if (!bAFAltM)
            oHC.getAntecedentesHeredoFamiliares().setAltMentales("");
    }
    
    public void validaFormula(){
        if (!oHC.getAntecedNutricio().getFormActual().equals("S"))
            oHC.getAntecedNutricio().setNomFormula("");
    }
}

