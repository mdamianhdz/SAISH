package org.apli.jbs.cobranza;

import java.io.Serializable;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apli.jbs.PacienteJB;
import org.apli.modelbeans.CiudadCP;
import org.apli.modelbeans.ManejoMsjsDB;
import org.apli.modelbeans.Paciente;
import org.apli.modelbeans.ServicioPrestado;
import org.apli.modelbeans.cobranza.CreditoParticular;
import org.apli.modelbeans.cobranza.PagareCredPart;
import org.primefaces.context.RequestContext;

/**
 * Bean administrado para la actualización del crédito (elección de pagaré a
 * cubrir para avisar a Caja lo que va a recibir)
 * @author BAOZ
 */
@ManagedBean(name = "oActCredPartJB")
@ViewScoped
public class ActualizaCredParticularJB implements Serializable{
private Paciente oPac;
private ManejoMsjsDB oMenDB=new ManejoMsjsDB();
private CreditoParticular oCredPartSeleccionado;
private PagareCredPart oPagaSeleccionado;
private List<CreditoParticular> oListaCred;
private boolean bBoton;
private boolean bHabBotonPaga;
private float nMontoPagar=0.0f;

    public ActualizaCredParticularJB() {
    }
    
    public void limpiaPagina(){
        oCredPartSeleccionado = new CreditoParticular();
        oCredPartSeleccionado.setCdCP(new CiudadCP());
        oPagaSeleccionado = new PagareCredPart();
    }
    
    public boolean validaPaciente() {
        boolean validacion = false;
        if (oPac != null) {
            validacion = true;
        }
        return validacion;
    }
    
    public void buscaDatos(){
    FacesMessage msg;
    CreditoParticular oC = new CreditoParticular();
        oPac = new PacienteJB().getPacienteSesion();
        oC.setServPres(new ServicioPrestado());
        try{
            if (oPac != null){
                oC.getServPres().setPaciente(oPac);
                oListaCred = oC.buscaTodosPorPaciente();
            }
        }catch(Exception e){
            msg = new FacesMessage("Aviso", "Error al buscar créditos particulares");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            e.printStackTrace();
        }
    }    
    
    public void seleccionaCred(){
        if (oCredPartSeleccionado == null||
            oCredPartSeleccionado.getIdCredPart()==0) {
            FacesMessage msg = new FacesMessage("Aviso", "Falta seleccionar elemento");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            oCredPartSeleccionado.getServPres().setPaciente(oPac);
            context.execute("PF('dlgCreditoClte').show()");
        }
    }
    
    public void seleccionaPagare(){
        if (oPagaSeleccionado == null||
            oPagaSeleccionado.getSecuencia()==0) {
            FacesMessage msg = new FacesMessage("Aviso", "Falta seleccionar elemento");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('dlgMontoPagar').show()");
        }
    }
    
    public void registraPagoCredito(){
    FacesMessage msg;
    String mess;
    FacesMessage.Severity nivel;
        try{
            if (nMontoPagar > 0.0f){
                oPagaSeleccionado.setMontoPagare(nMontoPagar);
                mess=oCredPartSeleccionado.actualizaCreditoParticular(
                        oPagaSeleccionado.getSecuencia());
                if (oMenDB.isValid(mess))
                    nivel = FacesMessage.SEVERITY_INFO;   
                else
                    nivel = FacesMessage.SEVERITY_ERROR;
                msg = new FacesMessage(nivel,"Guardar",oMenDB.manejoMensajes(mess));
                if (nivel.equals(FacesMessage.SEVERITY_INFO)){
                    RequestContext.getCurrentInstance().execute("PF('dlgMontoPagar').hide()");
                    RequestContext.getCurrentInstance().execute("PF('dlgCreditoClte').hide()");
                }
            }
            else{
                msg = new FacesMessage("Aviso", "El monto a pagar es incorrecto");
            }
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }catch(Exception e){
            msg = new FacesMessage("Aviso", "Error al actualizar el crédito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            e.printStackTrace();
        }
    }
    
    /**
     * Manda la pantalla a su estado inicial
     */ 
    public void regresar() throws Exception {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.getExternalContext().redirect("registraPagoCredPart.xhtml?faces-redirect=true");
    }
    /**
     * Habilita o deshabilita el botón para selección de un crédito
     */
    public void habilitaBoton(){
        if (this.oCredPartSeleccionado==null)
            bBoton=true;
        else
            bBoton=false;
    }
    
    public void habilitaBotonPaga(){
        if (this.oPagaSeleccionado==null)
            this.bHabBotonPaga=true;
        else
            this.bHabBotonPaga=false;
    }

    /**
     * @return the oPac
     */
    public Paciente getPaciente() {
        return oPac;
    }

    /**
     * @param oPac the oPac to set
     */
    public void setPaciente(Paciente oPac) {
        this.oPac = oPac;
    }

    /**
     * @return the oCredPartSeleccionado
     */
    public CreditoParticular getCredPartSeleccionado() {
        return oCredPartSeleccionado;
    }

    /**
     * @param oCredPartSeleccionado the oCredPartSeleccionado to set
     */
    public void setCredPartSeleccionado(CreditoParticular oCredPartSeleccionado) {
        this.oCredPartSeleccionado = oCredPartSeleccionado;
    }

    /**
     * @return the oPagaSeleccionado
     */
    public PagareCredPart getPagaSeleccionado() {
        return oPagaSeleccionado;
    }

    /**
     * @param oPagaSeleccionado the oPagaSeleccionado to set
     */
    public void setPagaSeleccionado(PagareCredPart oPagaSeleccionado) {
        this.oPagaSeleccionado = oPagaSeleccionado;
    }

    /**
     * @return the oListaCred
     */
    public List<CreditoParticular> getListaCred() {
        return oListaCred;
    }

    /**
     * @param oListaCred the oListaCred to set
     */
    public void setListaCred(List<CreditoParticular> oListaCred) {
        this.oListaCred = oListaCred;
    }

    /**
     * @return the bBoton
     */
    public boolean isBoton() {
        return bBoton;
    }

    /**
     * @param bBoton the bBoton to set
     */
    public void setBoton(boolean bBoton) {
        this.bBoton = bBoton;
    }

    /**
     * @return the nMontoPagar
     */
    public float getMontoPagar() {
        return nMontoPagar;
    }

    /**
     * @param nMontoPagar the nMontoPagar to set
     */
    public void setMontoPagar(float nMontoPagar) {
        this.nMontoPagar = nMontoPagar;
    }
    
    public boolean isHabBotonPaga() {
        return bHabBotonPaga;
    }

    /**
     * @param bBoton the bBoton to set
     */
    public void setHabBotonPaga(boolean bBoton) {
        this.bHabBotonPaga = bBoton;
    }
}
