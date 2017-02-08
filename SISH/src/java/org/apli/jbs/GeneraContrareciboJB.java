/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apli.jbs;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.apli.datamodels.FacturaDataModel;
import org.apli.datamodels.ContraReciboDataModel;
import org.apli.modelbeans.ContraRecibo;
import org.apli.modelbeans.facturacion.cfdi.FacturaCFI;

/**
 *
 * @author Lily_LnBnd
 */
@ManagedBean(name="oContraRec")
@SessionScoped

public class GeneraContrareciboJB implements Serializable{
    private List<FacturaCFI> listFacturas;
    
    private FacturaDataModel oFacModel; 
    private ContraReciboDataModel oCRModel;
    private FacturaCFI[] selectedFac;
    private ContraRecibo selectedCR;
    private int numCR;
    private Date fecVenc;
    private List<ContraRecibo> listCR;
    private int nTipoCia;
    private String sNomEmpresa;
    private String sNomAseguradora;
    private String sNomBanco;
    private boolean bDisBuscar=true;
    private boolean bDisGuardar=true;
    private boolean bDisEmp=true;
    private boolean bDisAseg=true;
    private boolean bDisBanco=true;
    
    public GeneraContrareciboJB(){
    }
    
    public void llenaLista() throws Exception{
        FacturaCFI oCFDI=new FacturaCFI();
        switch (getTipoCia()){
            case 1:
                listFacturas=oCFDI.buscaPendientesCobro(1,sNomEmpresa);
                break;
            case 2:
                listFacturas=oCFDI.buscaPendientesCobro(2,sNomAseguradora);
                break;
            case 3:
                listFacturas=oCFDI.buscaPendientesCobro(3,sNomBanco);
                break;
            default:
                listFacturas=oCFDI.buscaPendientesCobro();
                break;
        }
    }

    public List<FacturaCFI> getFacturas() {
        return listFacturas;
    }

    public void setFacturas(List<FacturaCFI> listFacturas) {
        this.listFacturas = listFacturas;
    }
    
    public FacturaCFI[] getSelectedFac() {  
        return selectedFac;  
    }  
  
    public void setSelectedFac(FacturaCFI[] selectedFac) {
        if (selectedFac.length>0)
            bDisGuardar=false;
        else
            bDisGuardar=true;
        this.selectedFac = selectedFac;  
    }
    public FacturaDataModel getFacModel() {  
        return oFacModel;  
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

    public int getTipoCia() {
        return nTipoCia;
    }

    public void setTipoCia(int nTipoCia) {
        this.nTipoCia = nTipoCia;
    }

    public String getNomEmpresa() {
        return sNomEmpresa;
    }

    public void setNomEmpresa(String sNomEmpresa) {
        this.sNomEmpresa = sNomEmpresa;
    }

    public String getNomAseguradora() {
        return sNomAseguradora;
    }

    public void setNomAseguradora(String sNomAseguradora) {
        this.sNomAseguradora = sNomAseguradora;
    }

    public String getNomBanco() {
        return sNomBanco;
    }

    public void setNomBanco(String sNomBanco) {
        this.sNomBanco = sNomBanco;
    }

    public boolean isDisBuscar() {
        return bDisBuscar;
    }

    public void setDisBuscar(boolean bDisBuscar) {
        this.bDisBuscar = bDisBuscar;
    }
    
    public boolean isDisGuardar() {
        return bDisGuardar;
    }

    public void setDisGuardar(boolean bDisGuardar) {
        this.bDisGuardar = bDisGuardar;
    }

    public boolean isDisEmp() {
        return bDisEmp;
    }

    public void setDisEmp(boolean bDisEmp) {
        this.bDisEmp = bDisEmp;
    }

    public boolean isDisAseg() {
        return bDisAseg;
    }

    public void setDisAseg(boolean bDisAseg) {
        this.bDisAseg = bDisAseg;
    }

    public boolean isDisBanco() {
        return bDisBanco;
    }

    public void setDisBanco(boolean bDisBanco) {
        this.bDisBanco = bDisBanco;
    }
    
    
  
    public void regresa() throws IOException{
        listFacturas=new ArrayList();
        nTipoCia=0;
        setNomEmpresa("");
        setNomAseguradora("");
        setNomBanco("");
        setDisBuscar(true);
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

    public void guarda() throws Exception{
        ContraRecibo oCR=new ContraRecibo();
        this.setSelectedCR(oCR.generaContraRecibo(selectedFac)); 
    }
    
    public void setNumCR (int numCR){
        this.numCR=numCR;
    }
    
    public int getNumCR (){
        return numCR;
    }
    public void setFecVenc (Date fecVenc){
        this.fecVenc=fecVenc;
    }
    public Date getFecVenc (){
        return fecVenc;
    }
    
    public List<ContraRecibo> getCR() {
        return listCR;
    }

    public void setCR(List<ContraRecibo> listCR) {
        this.listCR = listCR;
    }
    
    public void nuevaBusqueda(){
        listFacturas=new ArrayList();
        nTipoCia=0;
        setNomEmpresa("");
        setNomAseguradora("");
        setNomBanco("");
        bDisGuardar=true;
    }
    
    public void habilitaBuscar(){
        if (nTipoCia>0 && nTipoCia<5)
            this.setDisBuscar(false);
        switch (nTipoCia){
            case 1:
                setDisBanco(false);
                setDisAseg(true);
                setDisEmp(true);
                break;
            case 2:
                setDisEmp(true);
                setDisAseg(false);
                setDisBanco(true);
                break;
            case 3:
                setDisBanco(true);
                setDisAseg(true);
                setDisEmp(false);
                break;
            default:
                setDisEmp(true);
                setDisAseg(true);
                setDisBanco(true);
                break;
        }
    }
}
