package org.apli.jbs.caja;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apli.jbs.PacienteJB;
import org.apli.modelbeans.Paciente;
import org.apli.modelbeans.ServicioPrestado;
import org.primefaces.context.RequestContext;

/**
 *
 * @author BAOZ
 */
@ManagedBean(name = "oRegRAPart")
@ViewScoped
public class RegistrarPagoRAParticularJB  extends IngresosCaja implements Serializable {
private Paciente paciente;
private List<ServicioPrestado> listaServ;
private ServicioPrestado oSP;

    /**
     * Creates a new instance of RegistrarPagoRAParticularJB
     */
    public RegistrarPagoRAParticularJB() {
    }
    
    public List<ServicioPrestado> buscaCuentasParaAbono(){
    paciente = new PacienteJB().getPacienteSesion();
    listaServ = new ArrayList();
    oSP = new ServicioPrestado();
    FacesMessage msg;
        try{
            if (paciente != null){
                oSP.setPaciente(paciente);
                listaServ = oSP.buscRAPorPagarParticular(oSP.getPaciente().getFolioPac());
                
            }
        }catch(Exception e){
            msg = new FacesMessage("Aviso", "Error al buscar cuentas por pagar");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            e.printStackTrace();
        }
        return listaServ;
    }

    public void limpiaPagina() throws Exception{
        buscaCuentasParaAbono();
        sForma="";
        paciente = null;
    }
    
    public boolean validaUsuario() {
        boolean validacion = false;
        if (paciente != null) {
            validacion = true;
        }
        return validacion;
    }
    
    public void registraPago(){
    boolean bResult;
    FacesMessage msg;
        try{
            oOpeCaja.getFrmPago().setCveFrmPago(sForma);
            bResult = oOpeCaja.pagaRAPart(this.selectedServ);
            
            if (bResult){
                RequestContext.getCurrentInstance().execute("PF('diaE').show()");
            }
            else{
                msg = new FacesMessage("Aviso", oOpeCaja.getFolioRet());
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }
        }catch(Exception e){
            e.printStackTrace();
            msg = new FacesMessage("Aviso", "Error al guardar el pago");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
    
    public void regresar() throws Exception {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.getExternalContext().redirect("registrarPagoDeRAParticular.xhtml?faces-redirect=true");
    }
    
    public List<ServicioPrestado> getListaServ() {
        return this.listaServ;
    }
    
    public void setListaServ(List<ServicioPrestado> value) {
        this.listaServ = value;
    }
    
    public Paciente getPaciente() {
        return paciente;
    }
    
    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }
}
