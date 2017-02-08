package org.apli.jbs;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.apli.datamodels.ServicioPrestadoDataModel;
import org.apli.jbs.utilidades.DocsPDFJB;
import org.apli.modelbeans.ConceptoIngreso;
import org.apli.modelbeans.ExamenFisico;
import org.apli.modelbeans.Medico;
import org.apli.modelbeans.Paciente;
import org.apli.modelbeans.PersonalHospitalario;
import org.apli.modelbeans.ServicioPrestado;
import org.apli.modelbeans.Usuario;
import org.primefaces.context.RequestContext;

@ManagedBean(name="oEC")
@SessionScoped

public class ExpedienteClinicoJB implements Serializable{
    
    private List<ServicioPrestado> listaSP;
    private Paciente oPaciente = new Paciente();
    private ServicioPrestado selectedSP;
    private ServicioPrestadoDataModel oSPModel; 
    private Boolean bRenderAct=false;
    private Boolean bRenderCons=false;
    private ExamenFisico oEF;
    private String mess;
    private Boolean bConsulta;
    private Boolean bHosp;
    private int nEdad;
    private boolean bDisInter=true;
    private String sRutaPDF="";
    
    public ExpedienteClinicoJB() throws Exception{
        listaSP=new ArrayList(); 
        selectedSP=new ServicioPrestado();
        selectedSP.setExamenFisico(new ExamenFisico());
    }
    
    public void llena() throws Exception{
        oPaciente=new PacienteJB().getPacienteSesion();
        ServicioPrestado oSP=new ServicioPrestado();
        listaSP=oSP.serviciosPaciente(oPaciente.getFolioPac());
        oSPModel = new ServicioPrestadoDataModel(listaSP); 
        selectedSP=null;
        bRenderAct=true;
        bRenderCons=true;
    }

    public List<ServicioPrestado> getListaServicios(){
        return listaSP;
    }
    
    public ServicioPrestado getSelectedSP() {  
        return selectedSP;  
    }  
  
    public void setSelectedSP(ServicioPrestado selectedSP) {
        if (selectedSP.getRealizado()==null)
            bRenderAct=false;
        else
            bRenderAct=true;
        bRenderCons=false;
        this.selectedSP = selectedSP;  
    }
    public ServicioPrestadoDataModel getSPModel() {  
        return oSPModel;  
    }
    
    public Boolean getRenderAct(){
        return bRenderAct;
    }
    
    public void setRenderAct(Boolean bRenderAct){
        this.bRenderAct=bRenderAct;
    }

    public Boolean getRenderCons() {
        return bRenderCons;
    }

    public void setRenderCons(Boolean bRenderCons) {
        this.bRenderCons = bRenderCons;
    }
    
    public void deshabilita(){
        bRenderCons=true;
        bRenderAct=true;
    }
    
    public Paciente getPaciente(){
        return oPaciente;
    }
    
    public void setPaciente(Paciente oPaciente){
        this.oPaciente=oPaciente;
    }

    public Boolean getConsulta() {
        return bConsulta;
    }

    public void setConsulta(Boolean bConsulta) {
        this.bConsulta = bConsulta;
    }
    
    public Boolean getHosp() {
        return bHosp;
    }

    public void setHosp(Boolean bHosp) {
        this.bHosp = bHosp;
    }

    public boolean isDisInter() {
        return bDisInter;
    }

    public void setDisInter(boolean bDisInter) {
        this.bDisInter = bDisInter;
    }

    public String getRutaPDF() {
        return sRutaPDF;
    }

    public void setRutaPDF(String sRutaPDF) {
        this.sRutaPDF = sRutaPDF;
    }
    
    

        
    public int getEdad(){
        if (oPaciente==null || oPaciente.getFolioPac()<1)
            nEdad=0;
        else{
            Calendar cNac=Calendar.getInstance();
            Calendar cHoy=Calendar.getInstance();
            cNac.setTime(oPaciente.getNac());
            int anio=cHoy.get(Calendar.YEAR)-cNac.get(Calendar.YEAR);
            int mes=cHoy.get(Calendar.MONTH)-cNac.get(Calendar.MONTH);
            int dia=cHoy.get(Calendar.DATE)-cNac.get(Calendar.DATE);
            if(mes<0 || (mes==0 && dia<0))
                anio--;
            nEdad=anio;
        }
        return nEdad;
    }
    
    public void consultar(){
       this.setConsulta(true);
    }
    
    public void editar(){
       this.setConsulta(false);
    }
        
    public void regresa() throws Exception{
        FacesContext context= FacesContext.getCurrentInstance();
        context.getExternalContext().redirect("expedienteClinico.xhtml");
    }
    
    public void guarda() throws Exception{
        oEF=new ExamenFisico();
        mess=oEF.guardaExamenFis(selectedSP.getExamenFisico());
        if(!mess.contains("ERROR")){
            mess=selectedSP.guardaDatosCE(selectedSP);
        }
        FacesContext context= FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Guardar",mess));
        context.getExternalContext().getFlash().setKeepMessages(true);
        RequestContext.getCurrentInstance().execute("singleSPDialog.hide()");
        bRenderAct=true;
        selectedSP=null;
        llena();
        regresarCargo();
    }
    
    public List<Medico> getMedicos() throws Exception{
        Medico oMed=new Medico();
        List<Medico> listMed=oMed.buscaTodos();
        MedicoJB.medicos=listMed;
        return listMed;
    }
    
    public void validaMedConsulta() throws Exception{
        if (new Medico().isExterno(selectedSP.getMedRealiza().getFolioPers())==true)
            bDisInter=false;
        else
            bDisInter=true;
    }
    
    public void imprimeFolio() throws FileNotFoundException, Exception{
        List<ConceptoIngreso> list=new ArrayList();
        selectedSP.getConcepPrestado().getConServ().setFechaInterconsulta(new Date());
        selectedSP.getConcepPrestado().getConServ().setFolioServ(selectedSP.getIdFolio());
        selectedSP.getConcepPrestado().getConServ().setMedicoHonorarios(new Medico());
        selectedSP.getConcepPrestado().getConServ().getMedicoHonorarios().setFolioPers(selectedSP.getMedRealiza().getFolioPers());
        selectedSP.getConcepPrestado().setDescripcion(selectedSP.getConcepPrestado().getDescripConcep());
        selectedSP.getConcepPrestado().getConServ().setMedicoHonorarios(selectedSP.getEpisodioMedico().getMedTratante());
        list.add(selectedSP.getConcepPrestado());
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session =(HttpSession)context.getExternalContext().getSession(false);
        Usuario oUsu=(Usuario)session.getAttribute("usuarioSesion");
        String sCapturista=new PersonalHospitalario().buscaPersonalPorCveUsuario(oUsu.getUsuario()).getNombreCompleto();
        DocsPDFJB oDocPDF=new DocsPDFJB();
        sRutaPDF=oDocPDF.creaSolicitudInterconsulta(list, 
                selectedSP.getEpisodioMedico().getCveepisodio(), 
                oPaciente.getNombreCompleto(),0, 
                selectedSP.getMedRealiza().buscaLlavePersonal().getNombreCompleto(), sCapturista);
    }
    public void regresarCargo() throws IOException {
        if (sRutaPDF.length()!=0) {
            ExternalContext extCont = FacesContext.getCurrentInstance().getExternalContext();
            File folder = new File(extCont.getRealPath("//resources//"));
            File archivo = new File(folder, sRutaPDF);
            if (archivo.delete()) {
            }
        }
    }
    
}
