package org.apli.jbs.rpts;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apli.modelbeans.cobranza.CreditoParticular;
import org.primefaces.context.RequestContext;

/**
 *
 * @author BAOZ
 */
@ManagedBean(name = "oRptCredPartJB")
@ViewScoped
public class RptCredParticularJB  implements Serializable{
private Date dIni;
private Date dFin;
private List<CreditoParticular> oLista;
private CreditoParticular oCredSeleccionado;
private boolean bHabBoton=false;
    /**
     * Creates a new instance of RptCredParticularJB
     */
    public RptCredParticularJB() {
        oCredSeleccionado = new CreditoParticular();
    }
    
    public void validaFecha(){
        String mess="";
        if (dIni !=null && dFin!=null)
            if (dIni.compareTo(dFin)>0)
                mess="La fecha final del periodo debe ser posterior a la fecha de inicio";
        if (!mess.equals("")){
            FacesContext context= FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Aviso",mess));
        }
    }
    
    public void seleccionaCred(){
            System.out.println(oCredSeleccionado.getIdCredPart());
        if (oCredSeleccionado == null||
            oCredSeleccionado.getIdCredPart()==0) {
            FacesMessage msg = new FacesMessage("Aviso", "Falta seleccionar elemento");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('dlgCreditoClte').show()");
        }
    }
    public void buscar(){
    CreditoParticular oCred=new CreditoParticular();
    String mess="";
        try{
            oLista = oCred.buscaTodosPorFiltro(dIni, dFin);
        }catch(Exception e){
            e.printStackTrace();
            FacesContext context= FacesContext.getCurrentInstance();
            mess = "Error al buscar el reporte";
            context.addMessage(null, new FacesMessage("Aviso",mess));
        }
    }
    
    public void habilitaBoton(){
        System.out.println("paso");
        if (oCredSeleccionado == null)
            this.bHabBoton=false;
        else
            this.bHabBoton=true;
    }
    public void regresar() throws Exception {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.getExternalContext().redirect("rptCredPart.xhtml?faces-redirect=true");
    }
    
    
    public String getRangoFechas(){
    String sRet = "Consulta General de Créditos a Particulares <br/>";
    SimpleDateFormat fmtTxt = new SimpleDateFormat("dd/MM/yyyy");
        if (dIni != null)
            sRet = sRet + " del "+ fmtTxt.format(dIni);
        if (dFin != null)
            sRet = sRet + " al " + fmtTxt.format(dFin);
        return sRet;
    }

    /**
     * @return the dIni
     */
    public Date getIni() {
        return dIni;
    }

    /**
     * @param dIni the dIni to set
     */
    public void setIni(Date dIni) {
        this.dIni = dIni;
    }

    /**
     * @return the dFin
     */
    public Date getFin() {
        return dFin;
    }

    /**
     * @param dFin the dFin to set
     */
    public void setFin(Date dFin) {
        this.dFin = dFin;
    }

    /**
     * @return the oLista
     */
    public List<CreditoParticular> getLista() {
        return oLista;
    }

    /**
     * @param oLista the oLista to set
     */
    public void setLista(List<CreditoParticular> oLista) {
        this.oLista = oLista;
    }

    /**
     * @return the oCredSeleccionado
     */
    public CreditoParticular getCredSeleccionado() {
        System.out.println("Datos "+oCredSeleccionado.getCalleYNum());
        return oCredSeleccionado;
    }

    /**
     * @param oCredSeleccionado the oCredSeleccionado to set
     */
    public void setCredSeleccionado(CreditoParticular oCredSeleccionado) {
        this.oCredSeleccionado = oCredSeleccionado;
    }
    
    public boolean getHabBoton(){
        return this.bHabBoton;
    }
    public void setHabBoton(boolean value){
        this.bHabBoton = value;
    }
}
