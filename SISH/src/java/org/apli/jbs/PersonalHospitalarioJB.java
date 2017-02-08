package org.apli.jbs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.apli.modelbeans.AreaDeServicio;
import org.apli.modelbeans.Especialidad;
import org.apli.modelbeans.ManejoMsjsDB;
import org.apli.modelbeans.Medico;
import org.apli.modelbeans.PersonalHospitalario;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DualListModel;

/**
 *
 * @author JRuiz/Daniel López
 */
@ManagedBean(name="oPersonalHosp")
@ViewScoped
public class PersonalHospitalarioJB implements Serializable {
    public static List<PersonalHospitalario> listaPersonalHospitalario;  

    public Medico oPersHosp=new Medico();
public ManejoMsjsDB oMenDB=new ManejoMsjsDB();
private List<Medico> listaPersHosp;
private Medico selectedPersHosp;
private Medico currentPersHosp;
private boolean bDisDatosMed;
private int nOpe;
private int nTipPer;
private boolean bRenDatPershosp;
private boolean bRenDatMedExt;
private boolean bDisBoton=true;
private String sNomCatalogo;
private List<AreaDeServicio> source=new ArrayList();
private List<AreaDeServicio> target=new ArrayList();
private DualListModel<AreaDeServicio> dualList;
private List<PersonalHospitalario> arrPersFiltrado;
    
    public PersonalHospitalarioJB() {
    }
    
    public void mostrarPersonal() throws Exception{
        selectedPersHosp=null;
        currentPersHosp=null;
        listaPersHosp=new ArrayList(); 
        llenaLista();
    }
    
    private void llenaLista(){
        try {
            if(nTipPer==1){
                listaPersHosp.addAll(oPersHosp.buscaTodoPersonal());
                bRenDatPershosp=true;
                bRenDatMedExt=!(bRenDatPershosp);
                selectedPersHosp=null;
                currentPersHosp=null;
                sNomCatalogo="personal hospitalario";
            }
            if(nTipPer==2){
                listaPersHosp.addAll(oPersHosp.buscaTodoMedicosExternos());
                bRenDatMedExt=true;
                bRenDatPershosp=!(bRenDatMedExt);
                selectedPersHosp=null;
                currentPersHosp=null;
                sNomCatalogo="médico externo";
            }
            bDisBoton=false;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void confInsertar() throws Exception{
        selectedPersHosp=null;
        currentPersHosp=new Medico();
        currentPersHosp.setEsp(new Especialidad());
        nOpe=1;
        bDisDatosMed=true;
        if(sNomCatalogo.startsWith("m"))
              currentPersHosp.getPuesto().setCve("EXT");
    }
    
    public void confModificar() throws Exception{
        if (getSelectedPersonalHosp()==null){
            FacesContext context= FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Error","Debe seleccionar un registro"));
            context.getExternalContext().getFlash().setKeepMessages(true);
        }else{
            currentPersHosp=selectedPersHosp;
            if(sNomCatalogo.startsWith("m"))
                currentPersHosp.getPuesto().setCve("EXT");
            if(currentPersHosp.getPuesto().getCve().startsWith("Med")){
                bDisDatosMed=false;
            }
            else{
                bDisDatosMed=true;
            }
            nOpe=2;
            RequestContext.getCurrentInstance().execute("dlgPH.show()");
        }
    }
    public void preGuarda(ActionEvent ae)throws Exception{
        guarda();
    }
    
    public void guarda()throws Exception{
        String mess="";
        FacesMessage.Severity nivel;
        if(nOpe==1)
            mess=currentPersHosp.guardaPersonalHospitalario(currentPersHosp,dualList.getTarget());
        if(nOpe==2)
            mess=currentPersHosp.modificaPersonalHospitalario(currentPersHosp,dualList.getTarget());
        
        if (oMenDB.isValid(mess))
            nivel = FacesMessage.SEVERITY_INFO;   
        else
            nivel = FacesMessage.SEVERITY_ERROR;
        
        if(((currentPersHosp.getPuesto().getCve().trim()).equals("AuxImg") &&dualList.getTarget().isEmpty()) ||
            ((currentPersHosp.getPuesto().getCve().trim()).equals("AuxLab")&&dualList.getTarget().isEmpty())){
            FacesContext context= FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Validación.","Debe seleccionar por lo menos un servicio que atiende."));
            context.getExternalContext().getFlash().setKeepMessages(true);
        }
        else{
            FacesContext context= FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(nivel,"Guardar.",oMenDB.manejoMensajes(mess)));
            context.getExternalContext().getFlash().setKeepMessages(true);
            listaPersHosp.clear();
            llenaLista();
            nOpe = 0;
            selectedPersHosp = null;
            currentPersHosp = selectedPersHosp;
            RequestContext.getCurrentInstance().execute("dlgPH.hide()");
        }
    }
    
    public void campos(){
        if(currentPersHosp.getPuesto().getCve().startsWith("Med"))
            bDisDatosMed=false;
        else
            bDisDatosMed=true;
    }
    
    public List<Medico> getListaPersonal(){
        return listaPersHosp;
    }
    
    public Medico getSelectedPersonalHosp() {
        return selectedPersHosp;
    }

    public void setSelectedPersonalHosp(Medico selectedPH) {
        this.selectedPersHosp = selectedPH;
    }
    
    public Medico getCurrentPersonalHosp() {
        return currentPersHosp;
    }

    public void setCurrentPersonalHosp(Medico currentPH) {
        this.currentPersHosp = currentPH;
    }
    
    public boolean isDisDatosMed() {
        return bDisDatosMed;
    }
    
    public boolean isDisBoton() {
        return bDisBoton;
    }
    
    public boolean isRenDatPershosp() {
        return bRenDatPershosp;
    }
    
    public boolean isRenDatMedExt() {
        return bRenDatMedExt;
    }
    
    public int getTipPer() {
        return nTipPer;
    }

    public void setTipPer(int anTipPer) {
        nTipPer = anTipPer;
    }
    
    public String getNomCatalogo() {
        return sNomCatalogo;
    }
    
    public void llenaPick(int folio) throws Exception{
        dualList=new DualListModel<AreaDeServicio>(source, target);
        source=new ArrayList();
        target=new ArrayList();
        AreaDeServicio oAS=new AreaDeServicio();
        source.addAll((oAS.buscaSource(folio)));
        target.addAll((oAS.buscaTarget(folio)));
        setAreas(new DualListModel<AreaDeServicio>(source, target));
        dualList=new DualListModel<AreaDeServicio>(source, target);
    }
    
    public void setAreas(DualListModel<AreaDeServicio> dlist){
        this.dualList=dlist;      
    }
    
    public DualListModel<AreaDeServicio> getAreas(){
        return dualList;
    }
            
    public List<PersonalHospitalario> getPersFiltrado() {
        return arrPersFiltrado;
    }
    public void setPersFiltrado(List<PersonalHospitalario> valor) {
        this.arrPersFiltrado = valor;
    }
}
