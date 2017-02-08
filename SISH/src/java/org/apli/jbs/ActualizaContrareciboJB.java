/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apli.jbs;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.apli.datamodels.ContraReciboDataModel;
import org.apli.modelbeans.ContraRecibo;
import org.apli.modelbeans.Llamada;

/**
 *
 * @author Lily_LnBnd
 */
@ManagedBean(name="oActCR")
@SessionScoped

public class ActualizaContrareciboJB implements Serializable{
    private List<ContraRecibo> listCR;
    private List<Llamada> listLlam;
    private ContraReciboDataModel oCRModel;
    private ContraRecibo selectedCR;
    private Llamada oLlam;
    private String sRecibioLlam;
    private String sResultadoLlam;
    private Date dFechaLlam;
    
    public ActualizaContrareciboJB() throws Exception{
        listCR=new ArrayList();
    }
    
    public void llenaLista() throws Exception{
        ContraRecibo oCR=new ContraRecibo();
        listCR=oCR.buscaEnProceso();
    }

    public ContraRecibo getSelectedCR() {  
        return selectedCR;  
    }  
  
    public void setSelectedCR(ContraRecibo selectedCR) {  
        this.selectedCR = selectedCR;  
    }
    public ContraReciboDataModel getCRModel() {  
        return oCRModel;  
    }
    
    public void setLlamada(Llamada llamada) {  
        this.oLlam = llamada;  
    }
    public Llamada getLlamada() {  
        return oLlam;  
    }
    
    public String getRecibioLlamada(){
        return sRecibioLlam;
    }
    
    public void setRecibioLlamda(String sRecibioLlam){
        this.sRecibioLlam=sRecibioLlam;
    }
    
    public String getResultadoLlamada(){
        return sResultadoLlam;
    }
    
    public void setResultadoLlamda(String sResultadoLlam){
        this.sResultadoLlam=sResultadoLlam;
    }
    public Date getFechaLlamada(){
        return dFechaLlam;
    }
    
    public void setFechaLlamda(Date dFechaLlam){
        this.dFechaLlam=dFechaLlam;
    }
    
    public void setListaLlamadas(List<Llamada> listLlam) {  
        this.listLlam = listLlam;  
    }
    public List<Llamada> getListaLlamadas() {  
        return listLlam;  
    }
    
    public void regresa() throws IOException{
        FacesContext context= FacesContext.getCurrentInstance();
       //context.addMessage(null, new FacesMessage("Guarda","Datos guardados"));
        //context.getExternalContext().getFlash().setKeepMessages(true);
        context.getExternalContext().redirect("generaContrarecibo.xhtml");
    }
    
    public void imprime() throws IOException{
        FacesContext context= FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Imprimir","Imprime formato de contrarecibo"));
        context.getExternalContext().getFlash().setKeepMessages(true);
        context.getExternalContext().redirect("generaContrarecibo.xhtml");
    }
    
    public void actualizaCR() throws IOException{
        FacesContext context= FacesContext.getCurrentInstance();
       //context.addMessage(null, new FacesMessage("Guarda","Datos guardados"));
        //context.getExternalContext().getFlash().setKeepMessages(true);
        context.getExternalContext().redirect("actualizaContrarecibo.xhtml");
    }

    
    public List<ContraRecibo> getCR() {
        return listCR;
    }

    public void setCR(List<ContraRecibo> listCR) {
        this.listCR = listCR;
    }
    
    public void agregaLlamada(){
        System.out.println("agregaLlamada");
        if(sRecibioLlam.equals("") ||sResultadoLlam.equals("")||dFechaLlam==null){
            System.out.println("Error de validacion");
        }else{
            oLlam=new Llamada();
            oLlam.setFecLlamada(dFechaLlam);
            oLlam.setRecibioLlamada(sRecibioLlam);
            oLlam.setResultado(sResultadoLlam);
            selectedCR.getLlamadas().add(oLlam);
        }
    }
}