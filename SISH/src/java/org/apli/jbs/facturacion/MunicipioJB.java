/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apli.jbs.facturacion;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.apli.modelbeans.Estado;
import org.apli.modelbeans.ManejoMsjsDB;
import org.apli.modelbeans.Municipio;
import org.primefaces.context.RequestContext;
/**
 * Autor: Isabel Espinoza Espinoza
 * Daniel López Rosas Abril/2015
 * Fecha: Abril 2014
 */
@ManagedBean(name="oMunicipio")
@ViewScoped
public class MunicipioJB {
    public Municipio oMunicipio=new Municipio();
    public ManejoMsjsDB oMenDB=new ManejoMsjsDB();
    private List<Municipio> listaMunicipios;
    private Municipio selectedMunicipio;
    private Municipio currentMunicipio;
    private String sCveEdo;
    private String sNomButton;
    private int nOpe;
    private boolean bDisDatos;
    private boolean bDisDatosCveMun;
    public static List<org.apli.modelbeans.Municipio> listMunicipiosDF;
    public static List<org.apli.modelbeans.Municipio> listMunicipiosLE;
    
    public MunicipioJB() {
    }
    
    public void mostrarMunicipios() throws Exception{
        selectedMunicipio = null;
        currentMunicipio = null;
        listaMunicipios=new ArrayList();
        llenaLista();
    }
    
    private void llenaLista(){
        try {
            listaMunicipios.addAll(oMunicipio.getMunicipiosEdo(sCveEdo));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void confModificar(){
        if (getSelectedMunicipio()==null){
            FacesContext context= FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Error","Debe seleccionar un registro"));
            context.getExternalContext().getFlash().setKeepMessages(true);
        }else{
            currentMunicipio=selectedMunicipio;
            sNomButton="Guardar";
            bDisDatos=false;
            bDisDatosCveMun=true;
            nOpe=2;
            RequestContext.getCurrentInstance().execute("dlgMun.show()");
        }
    }
    
    public void confEliminar(){
        if (getSelectedMunicipio()==null){
            FacesContext context= FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Error","Debe seleccionar un registro"));
            context.getExternalContext().getFlash().setKeepMessages(true);
        }else{
            currentMunicipio=selectedMunicipio;
            sNomButton="Eliminar";
            bDisDatos=true;
            bDisDatosCveMun=true;
            nOpe=3;
            RequestContext.getCurrentInstance().execute("dlgMun.show()");
        }
    }
    
    public void confInsertar() throws Exception{
        selectedMunicipio=null;
        currentMunicipio=new Municipio();
        currentMunicipio.setEdo(new Estado());
        currentMunicipio.getEdo().setCve(sCveEdo);
        sNomButton="Guardar";
        bDisDatos=false;
        bDisDatosCveMun=false;
        nOpe=1;
    }
    public void preGuarda(ActionEvent ae)throws Exception{
        guarda();
    }
    
    public void guarda()throws Exception{
        String mess="";
        Severity nivel;
        if(nOpe==1)
            mess=currentMunicipio.guardaMunicipio(currentMunicipio);
        else{
            if(nOpe==3)
                mess=currentMunicipio.eliminaMunicipio(currentMunicipio);
            else
                mess=currentMunicipio.modificaMunicipio(currentMunicipio);
        }
        if (oMenDB.isValid(mess))
            nivel = FacesMessage.SEVERITY_INFO;   
        else
            nivel = FacesMessage.SEVERITY_ERROR;
        
        FacesContext context= FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(nivel,"Guardar.",oMenDB.manejoMensajes(mess)));
        context.getExternalContext().getFlash().setKeepMessages(true);
            listaMunicipios.clear();
            llenaLista();
            nOpe = 0;
            selectedMunicipio = null;
            currentMunicipio = selectedMunicipio;
            RequestContext.getCurrentInstance().execute("dlgMun.hide()");
        
    }
    
    public List<Municipio> getListaMunicipios(){
        return listaMunicipios;
    }
    
    public Municipio getMunicipio() {
        return oMunicipio;
    }

    public void setMunicipio(Municipio oMun) {
        this.oMunicipio = oMun;
    }

    public String getCveEdo() {
        return sCveEdo;
    }
    
    public void setCveEdo(String cve) {
        sCveEdo = cve;
    }
    
    
    public Municipio getSelectedMunicipio() {
        return selectedMunicipio;
    }

    public void setSelectedMunicipio(Municipio selectedMun) {
        this.selectedMunicipio = selectedMun;
    }
    
    
    public Municipio getCurrentMunicipio () {
        return currentMunicipio;
    }

    public void setCurrentMunicipio (Municipio currentMunicipio) {
        this.currentMunicipio = currentMunicipio;
    }
    
    public String getNomButton() {
        return sNomButton;
    }
    
    public boolean isDisDatos() {
        return bDisDatos;
    }
    
    public boolean isDisDatosCveMun() {
        return bDisDatosCveMun;
    }
}
