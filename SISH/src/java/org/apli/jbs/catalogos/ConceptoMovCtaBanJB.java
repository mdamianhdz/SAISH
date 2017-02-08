package org.apli.jbs.catalogos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.apli.modelbeans.ManejoMsjsDB;
import org.apli.modelbeans.contabilidadInterna.ConceptoMovCtaBan;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Daniel
 */
@ManagedBean(name="ConceptMCB")
@ViewScoped
public class ConceptoMovCtaBanJB implements Serializable {

    public ConceptoMovCtaBan oCoceptMCB=new ConceptoMovCtaBan();
    public ManejoMsjsDB oMenDB=new ManejoMsjsDB();
    private List<ConceptoMovCtaBan> listaConceptMCB;
    private ConceptoMovCtaBan selectedConceptMCB;
    private ConceptoMovCtaBan currentConceptMCB;
    private boolean bDisDatos;
    private boolean bRenDatos;
    private String sNomButton;
    private int nOpe;
    private String sTipoMovCtaBan;

    /**
     * Creates a new instance of ConceptoMovCtaBanJB
     */
    public ConceptoMovCtaBanJB() {
        
    }
    
     public void mostrarConceptos() throws Exception{
        currentConceptMCB=null;
        selectedConceptMCB=null;
        listaConceptMCB=new ArrayList();
        llenaLista();
    }

    private void llenaLista(){
        try {
            listaConceptMCB.addAll(oCoceptMCB.buscaConceptos(sTipoMovCtaBan));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void confModificar(){
        if (getSelectedConceptoMovCtaBan()==null){
            FacesContext context= FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Error","Debe seleccionar un registro"));
            context.getExternalContext().getFlash().setKeepMessages(true);
        }else{
            currentConceptMCB=selectedConceptMCB;
            bDisDatos=false;
            bRenDatos=false;
            sNomButton="Guardar";
            nOpe=2;
            RequestContext.getCurrentInstance().execute("dlgCMCB.show()");
        }
    }
    
    public void confEliminar(){
        if (getSelectedConceptoMovCtaBan()==null){
            FacesContext context= FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Error","Debe seleccionar un registro"));
            context.getExternalContext().getFlash().setKeepMessages(true);
        }else{
            bDisDatos=true;
            bRenDatos=true;
            currentConceptMCB=selectedConceptMCB;
            sNomButton="Eliminar";
            nOpe=3;
            RequestContext.getCurrentInstance().execute("dlgCMCB.show()");
        }
    }
    
    public void confInsertar(){
        selectedConceptMCB=null;
        currentConceptMCB=new ConceptoMovCtaBan();
        bDisDatos=false;
        bRenDatos=false;
        sNomButton="Guardar";
        nOpe=1;
    }
    
    public void preGuarda(ActionEvent ae)throws Exception{
        guarda();
    }
     public void guarda()throws Exception{
        String mess="";
        Severity nivel;
        if(nOpe==1)
            mess=currentConceptMCB.guardaConceptoMovCtaBan(currentConceptMCB);
        else{
            if(nOpe==3)
                mess=currentConceptMCB.eliminaConceptoMovCtaBan(currentConceptMCB);
            else
                mess=currentConceptMCB.modificaConceptoMovCtaBan(currentConceptMCB);
        }
        if (oMenDB.isValid(mess))
            nivel = FacesMessage.SEVERITY_INFO;
        
        else
            nivel = FacesMessage.SEVERITY_ERROR;
        FacesContext context= FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(nivel,"Guardar.",oMenDB.manejoMensajes(mess)));
        context.getExternalContext().getFlash().setKeepMessages(true);
        listaConceptMCB.clear();
        llenaLista();
        nOpe = 0;
        selectedConceptMCB = null;
        currentConceptMCB = selectedConceptMCB;
            RequestContext.getCurrentInstance().execute("dlgCMCB.hide()");
        }
        
     public List<ConceptoMovCtaBan> getListaConceptMCB(){
        return listaConceptMCB;
    }
    
    public void setSelectedConceptoMovCtaBan(ConceptoMovCtaBan selectedCMCB) {
        this.selectedConceptMCB = selectedCMCB;
    }
     
    public ConceptoMovCtaBan getSelectedConceptoMovCtaBan() {
        return selectedConceptMCB;
    }
    
    public ConceptoMovCtaBan getCurrentConceptoMovCtaBan() {
        return currentConceptMCB;
    }

    public void setCurrentConceptoMovCtaBan(ConceptoMovCtaBan currentCMCB) {
        this.currentConceptMCB = currentCMCB;
    }
    
    public String getNomButton() {
        return sNomButton;
    }
    
    public void setTipoMovCtaBan(String TipoMovCtaBan){
        sTipoMovCtaBan=TipoMovCtaBan;
    }
    
    public String getTipoMovCtaBan() {
        return sTipoMovCtaBan;
    }
    
    public boolean isDisDatos() {
        return bDisDatos;
}
    
    public boolean isRenDatos() {
        return bRenDatos;
    }
    
    public String getDescripTipo(String tipo){
        String sTipo="";
        if(tipo.equals("E")){sTipo="Entrada";}
        else sTipo="Salida";
        return sTipo;
    }
}
