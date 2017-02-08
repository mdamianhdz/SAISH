/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import org.apli.modelbeans.contabilidadInterna.SublineaEgreso;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Daniel
 */
@ManagedBean(name="oSubLineaEgr")
@ViewScoped
public class SubLineaEgrJB implements Serializable{
        public SublineaEgreso oSublineaEgr=new SublineaEgreso();
        public ManejoMsjsDB oMenDB=new ManejoMsjsDB();
        private List<SublineaEgreso> listaSublineaEgr;
        private SublineaEgreso selectedSublineaEgr;
        private SublineaEgreso currentSublineaEgr;
        private String sNomButton;
        private int nOpe;
        private boolean bDisDatos;
    
    /**
     * Creates a new instance of SubLineaEgrJB
     */
    public SubLineaEgrJB() {
        listaSublineaEgr=new ArrayList();
        llenaLista();
    }
    
    private void llenaLista(){
        try {
            listaSublineaEgr.addAll(oSublineaEgr.todasSublineasEgreso());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void confModificar(){
        if (getSelectedSublineaEgr()==null){
            FacesContext context= FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Error","Debe seleccionar un registro"));
            context.getExternalContext().getFlash().setKeepMessages(true);
        }else{
            currentSublineaEgr=selectedSublineaEgr;
            sNomButton="Guardar";
            bDisDatos=false;
           System.out.println("+"+currentSublineaEgr.getLineaEgre().getCveLineaEgr()+"+");
            nOpe=2;
            RequestContext.getCurrentInstance().execute("dlgSE.show()");
        }
    }
    
    public void confEliminar(){
        if (getSelectedSublineaEgr()==null){
            FacesContext context= FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Error","Debe seleccionar un registro"));
            context.getExternalContext().getFlash().setKeepMessages(true);
        }else{
            currentSublineaEgr=selectedSublineaEgr;
            sNomButton="Eliminar";
            bDisDatos=true;
            
            nOpe=3;
            RequestContext.getCurrentInstance().execute("dlgSE.show()");
        }
    }
    
    public void confInsertar(){
        selectedSublineaEgr=null;
        currentSublineaEgr=new SublineaEgreso();
        sNomButton="Guardar";
        nOpe=1;
        bDisDatos=false;
       
    }
    
    public void preGuarda(ActionEvent ae)throws Exception{
        guarda();
    }
    
    public void guarda()throws Exception{
        String mess="";
        Severity nivel;
        if(nOpe==1)
            mess=currentSublineaEgr.guardaSublineaEgreso(currentSublineaEgr);
        else{
            if(nOpe==3)
                mess=currentSublineaEgr.eliminaSublineaEgreso(currentSublineaEgr);
            else
                mess=currentSublineaEgr.modificaSublineaEgreso(currentSublineaEgr);
        }
        if (oMenDB.isValid(mess))
            nivel = FacesMessage.SEVERITY_INFO;   
        else
            nivel = FacesMessage.SEVERITY_ERROR;
        
        FacesContext context= FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(nivel,"Guardar.",oMenDB.manejoMensajes(mess)));
        context.getExternalContext().getFlash().setKeepMessages(true);
            listaSublineaEgr.clear();
            llenaLista();
            nOpe = 0;
            selectedSublineaEgr = null;
            currentSublineaEgr = selectedSublineaEgr;
            RequestContext.getCurrentInstance().execute("dlgSE.hide()");
        }
    
    public List<SublineaEgreso> getListaSublineaEgr(){
        return listaSublineaEgr;
    }
    
    public SublineaEgreso getSublineaEgreso() {
        return oSublineaEgr;
    }

    public void setSublineaEgreso(SublineaEgreso oSublineaEgr) {
        this.oSublineaEgr = oSublineaEgr;
    }
    public SublineaEgreso getSelectedSublineaEgr() {
        return selectedSublineaEgr;
    }

    public void setSelectedSublineaEgr(SublineaEgreso selectedSE) {
        this.selectedSublineaEgr = selectedSE;
    }
    
    
    public SublineaEgreso getCurrentSublineaEgr() {
        return currentSublineaEgr;
    }

    public void setCurrentSublineaEgr(SublineaEgreso currentSE) {
        this.currentSublineaEgr = currentSE;
    }
    
    public String getNomButton() {
        return sNomButton;
    }
    
    public boolean isDisDatos() {
        return bDisDatos;
    }
}
