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
 * Para actualización de contrarrecibo
 * @author Lily_LnBnd
 */
@ManagedBean(name="oA")
@SessionScoped

public class A implements Serializable{
    private String sRecibioLlamada;
    private String sRealizoLlamada;
    private String sRecibioFactura;
    private String sResultadoLlamada;
    private String sObservaciones;
    private String sGuiaEnvio;
    private char cTipo;
    private Date dFechaLlamada;
    private Date dFechaRecepcion;
    private Date dFechaProbPago;
    private Llamada oLlam;
    private List<Llamada> listLlamada;
    private List<ContraRecibo> listCR;
    private ContraReciboDataModel oCRModel;
    private ContraRecibo selectedCR;
    private int nTipoCia;
    private boolean bDisBuscar=true;
    private boolean bDisActualizar=true;
    private int nNumCR;
    private String sNomEmpresa;
    private String sNomAseguradora;
    private String sNomBanco;
    private boolean bDisCR=true;
    private boolean bDisEmp=true;
    private boolean bDisAseg=true;
    private boolean bDisBanco=true;

    
    public A(){
    }
    
    public void llenaLista() throws Exception{
        ContraRecibo oCR=new ContraRecibo();
        switch (nTipoCia){
            case 1:
                listCR=oCR.buscaEnProceso(nNumCR);
                break;
            case 2:
                listCR=oCR.buscaEnProceso(1,sNomBanco);
                break;
            case 3:
                listCR=oCR.buscaEnProceso(2,sNomAseguradora);
                break;
            case 4:
               listCR=oCR.buscaEnProceso(3,sNomEmpresa);
                break;
            default:
                listCR=oCR.buscaEnProceso();
                break;
        }
    }
    public void setRecibioLlamada(String sRecibioLlamada){this.sRecibioLlamada=sRecibioLlamada;}
    public String getRecibioLlamada(){return sRecibioLlamada;}
    public void setRealizoLlamada(String sRealizoLlamada){this.sRealizoLlamada=sRealizoLlamada;}
    public String getRealizoLlamada(){return sRealizoLlamada;}
    public void setRecibioFactura(String sRecibioFactura){this.sRecibioFactura=sRecibioFactura;}
    public String getRecibioFactura(){return sRecibioFactura;}
    public void setResultadoLlamada(String sResultadoLlamada){this.sResultadoLlamada=sResultadoLlamada;}
    public String getResultadoLlamada(){return sResultadoLlamada;}
    public void setObservaciones(String sObservaciones){this.sObservaciones=sObservaciones;}
    public String getObservaciones(){return sObservaciones;}
    public void setGuiaEnvio(String sGuiaEnvio){this.sGuiaEnvio=sGuiaEnvio;}
    public String getGuiaEnvio(){return sGuiaEnvio;}
    public void setTipo(char cTipo){this.cTipo=cTipo;}
    public char getTipo(){return cTipo;}
    public void setFechaLlamada(Date dFechaLlamada){this.dFechaLlamada=dFechaLlamada;}
    public Date getFechaLlamada(){return dFechaLlamada;}
    public void setFechaRecepcion(Date dFechaRecepcion){this.dFechaRecepcion=dFechaRecepcion;}
    public Date getFechaRecepcion(){return dFechaRecepcion;}
    public void setFechaProbPago(Date dFechaProbPago){this.dFechaProbPago=dFechaProbPago;}
    public Date getFechaProbPago(){return dFechaProbPago;}
    public void setLlamada(Llamada oLlam){this.oLlam=oLlam;}
    public Llamada getLlamada(){return oLlam;}
    public void setListaLlamadas(List<Llamada> listLlamada){this.listLlamada=listLlamada;}
    public List<Llamada> getListaLlamadas(){return listLlamada;}
    public List<ContraRecibo> getCR() {return listCR;}
    public void setCR(List<ContraRecibo> listCR) {this.listCR = listCR;}
    
    public ContraRecibo getSelectedCR() {
        return selectedCR; 
    }  
    
    public void setSelectedCR(ContraRecibo selectedCR) {
        if (selectedCR == null)
            bDisActualizar=true;
        else
            bDisActualizar=false;
        this.selectedCR = selectedCR;  
    }
    public ContraReciboDataModel getCRModel() { return oCRModel;}

    public int getTipoCia() {
        return nTipoCia;
    }

    public void setTipoCia(int nTipoCia) {
        this.nTipoCia = nTipoCia;
    }

    public boolean isDisBuscar() {
        return bDisBuscar;
    }

    public void setDisBuscar(boolean bDisBuscar) {
        this.bDisBuscar = bDisBuscar;
    }
    
    public boolean getDisActualizar() {
        return bDisActualizar;
    }

    public void setDisActualizar(boolean bDisActualizar) {
        this.bDisActualizar = bDisActualizar;
    }

    public int getNumCR() {
        return nNumCR;
    }

    public void setNumCR(int nNumCR) {
        this.nNumCR = nNumCR;
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

    public boolean isDisCR() {
        return bDisCR;
    }

    public void setDisCR(boolean bDisCR) {
        this.bDisCR = bDisCR;
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
    
    public void agregaLlamada(){
        if (dFechaLlamada==null || sRecibioLlamada.equals("") || sResultadoLlamada.equals("")||sRealizoLlamada.equals("")||cTipo==' '){
            FacesContext context= FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Error","Faltan datos!"));
            context.getExternalContext().getFlash().setKeepMessages(true);
        }else{
            oLlam=new Llamada();
            oLlam.setFecLlamada(dFechaLlamada);
            oLlam.setRecibioLlamada(sRecibioLlamada);
            oLlam.setResultado(sResultadoLlamada);
            oLlam.setRealizoLlamada(sRealizoLlamada);
            oLlam.setTipo(cTipo);
            selectedCR.getLlamadas().add(oLlam);
            this.dFechaLlamada=null;
            this.sRecibioLlamada=null;
            this.sRealizoLlamada=null;
            this.sResultadoLlamada=null;
            this.cTipo=' ';
        }
    }
    
    public void eliminaLlamada(Llamada llam){
        for(int i=0;i<selectedCR.getLlamadas().size();i++){
            if (selectedCR.getLlamadas().get(i).getFecLlamada()==llam.getFecLlamada() &&
                    selectedCR.getLlamadas().get(i).getRecibioLlamada().equals(llam.getRecibioLlamada()) &&
                    selectedCR.getLlamadas().get(i).getRealizoLlamada().equals(llam.getRealizoLlamada()) &&
                    selectedCR.getLlamadas().get(i).getTipo()==llam.getTipo() &&
                    selectedCR.getLlamadas().get(i).getResultado().equals(llam.getResultado())) {
                selectedCR.getLlamadas().remove(i);
                break;
            }
        }
    }
    
    public void guarda() throws IOException, Exception{
        if (!"".equals(sRecibioFactura))
            selectedCR.setRecibioFactura(sRecibioFactura);
        if(!"".equals(sObservaciones))
            selectedCR.setObservaciones(sObservaciones);
        if (dFechaRecepcion!=null)
            selectedCR.setFecRecepcion(dFechaRecepcion);
        if (dFechaProbPago!=null)
            selectedCR.setFecProbPago(dFechaProbPago);
        if (!"".equalsIgnoreCase(sGuiaEnvio))
            selectedCR.setGuiaEnvio(sGuiaEnvio);
        String mess=selectedCR.guardaContraRecibo(selectedCR);
        
        this.dFechaRecepcion=null;
        this.dFechaProbPago=null;
        this.sRecibioFactura="";
        this.sObservaciones="";
        this.sGuiaEnvio="";
        FacesContext context= FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Guarda",mess));
        context.getExternalContext().getFlash().setKeepMessages(true);
        context.getExternalContext().redirect("actualizaContrarecibo.xhtml");
    }

    public boolean  renderInput(String dato){
        if (dato.equals("")||dato.equals("null"))
            return true;
        else
            return false;
    }
    public boolean  renderOutput(String dato){
        if (dato.equals("")||dato.equals("null"))

            return false;
        else
            return true;
    }
    
    
    public void nuevaBusqueda(){
        listCR=new ArrayList();
    }
    
    public void buscaLlamadas() throws Exception{
        Llamada oLl=new Llamada();
        selectedCR.setLlamadas(oLl.buscaLlamadas(selectedCR.getNumContraRecibo()));
    }
    
    public void habilitaBuscar(){
        if (nTipoCia>0 && nTipoCia<6)
            setDisBuscar(false);
        switch (nTipoCia){
            case 1:
                setDisCR(false);
                setDisEmp(true);
                setDisAseg(true);
                setDisBanco(true);
                break;
            case 2:
                setDisCR(true);
                setDisEmp(false);
                setDisAseg(true);
                setDisBanco(true);
                break;
            case 3:
                setDisCR(true);
                setDisEmp(true);
                setDisAseg(false);
                setDisBanco(true);
                break;
            case 4:
                setDisCR(true);
                setDisEmp(true);
                setDisAseg(true);
                setDisBanco(false);
                break;
            default:
                setDisCR(true);
                setDisEmp(true);
                setDisAseg(true);
                setDisBanco(true);
                break;
        }
        setNomEmpresa("");
        setNomAseguradora("");
        setNomBanco("");
        setNumCR(0);
    }
    
    public String cambiaTipo(char var){
        if (var=='L')
            return "Llamada";
        else
            return "Visita";
    }
}