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
import org.apli.modelbeans.Diagnostico;
import org.apli.modelbeans.ManejoMsjsDB;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Daniel
 */
@ManagedBean(name="oDiag")
@ViewScoped
public class DiagnosticoJB implements Serializable{
    public Diagnostico oDiag=new Diagnostico();
    public ManejoMsjsDB oMenDB=new ManejoMsjsDB();
    private List<Diagnostico> listaDiag;
    private Diagnostico selectedDiagnostico;
    private Diagnostico currentDiagnostico;
    private boolean bDisDatos;
    private boolean bDisDatosDescrip;
    private String sNomButton;
    private String sCveDiag;
    private int nOpe;
    public static List<Diagnostico> diagnosticos;
    /**
     * Creates a new instance of DiagnosticoJB
     */
    public DiagnosticoJB()  {
    }
        
    public void filtrar(ActionEvent ae)throws Exception{
        currentDiagnostico=null;
        selectedDiagnostico=null;
        muestraDiagnosticos();
    }
    
    public void muestraDiagnosticos(){
        listaDiag=new ArrayList();
        llenaLista();
    }
    
    private void llenaLista(){
        try {
            listaDiag.addAll(oDiag.buscaFiltro(sCveDiag));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void confModificar(){
        if (getSelectedDiagnostico()==null){
            FacesContext context= FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Error","Debe seleccionar un registro"));
            context.getExternalContext().getFlash().setKeepMessages(true);
        }else{
            currentDiagnostico=selectedDiagnostico;
            bDisDatos=true;
            bDisDatosDescrip=false;
            sNomButton="Guardar";
            nOpe=2;
            RequestContext.getCurrentInstance().execute("dlgDiag.show()");
        }
    }
    
    public void confEliminar(){
        if (getSelectedDiagnostico()==null){
            FacesContext context= FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Error","Debe seleccionar un registro"));
            context.getExternalContext().getFlash().setKeepMessages(true);
        }else{
            currentDiagnostico=selectedDiagnostico;
            bDisDatos=true;
            bDisDatosDescrip=true;
            sNomButton="Eliminar";
            nOpe=3;
            RequestContext.getCurrentInstance().execute("dlgDiag.show()");
        }
    }
    
    public void confInsertar(){
        selectedDiagnostico=null;
        currentDiagnostico=new Diagnostico();
        bDisDatos=false;
        bDisDatosDescrip=false;
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
            mess=currentDiagnostico.guardaDiagnostico(currentDiagnostico);
        else{
            if(nOpe==3)
                mess=currentDiagnostico.eliminaDiagnostico(currentDiagnostico);
            else
                mess=currentDiagnostico.modificaDiagnostico(currentDiagnostico);
        }
        if (oMenDB.isValid(mess))
            nivel = FacesMessage.SEVERITY_INFO;
            
        else
            nivel = FacesMessage.SEVERITY_ERROR;
        
        FacesContext context= FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(nivel,"Guardar.",oMenDB.manejoMensajes(mess)));
        context.getExternalContext().getFlash().setKeepMessages(true);
            listaDiag.clear();
            llenaLista();
            nOpe = 0;
            selectedDiagnostico = null;
            currentDiagnostico = selectedDiagnostico;
            RequestContext.getCurrentInstance().execute("dlgDiag.hide()");
        }
    
    public List<Diagnostico> getListaDiagnosticos(){
        return listaDiag;
    }
    
     public void setSelectedDiagnostico(Diagnostico selectedDiag) {
        this.selectedDiagnostico = selectedDiag;
    }
     
    public Diagnostico getSelectedDiagnostico() {
        return selectedDiagnostico;
    }
    
    public Diagnostico getCurrentDiagnostico() {
        return currentDiagnostico;
    }

    public void setCurrentDiagnostico(Diagnostico currentDiag) {
        this.currentDiagnostico = currentDiag;
    }
    
    public String getNomButton() {
        return sNomButton;
    }
    
    public String getCveDiag() {
        return sCveDiag;
    }
    
    public void setCveDiag(String CveDiag){
        sCveDiag=CveDiag; 
    }

    public boolean isDisDatos() {
        return bDisDatos;
    }
    
    public boolean isDisDatosDescrip() {
        return bDisDatosDescrip;
    }
    
}

