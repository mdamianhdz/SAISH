package org.apli.jbs.caja;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apli.jbs.PacienteJB;
import org.apli.modelbeans.OperacionCaja;
import org.apli.modelbeans.Paciente;
import org.apli.modelbeans.ServicioPrestado;
import org.primefaces.context.RequestContext;

/**
 * Registra el cierre de una cuenta de hospital mediante el pago (o la 
 * devolución) del saldo de la cuenta
 * @author BAOZ
 */
@ManagedBean(name = "oRegCieCtaJB")
@ViewScoped
public class RegistrarPagoCierreCuentaJB  extends IngresosCaja implements Serializable {
private Paciente paciente;
private List<ServicioPrestado> listaServ;
private ServicioPrestado oSP;

    public RegistrarPagoCierreCuentaJB() {
    }
    
    public List<ServicioPrestado> buscaCuentasParaCerrar(){
    paciente = new PacienteJB().getPacienteSesion();
    listaServ = new ArrayList();
    oSP = new ServicioPrestado();
    FacesMessage msg;
        try{
            System.out.println(paciente);
            if (paciente != null){
                oSP.setPaciente(paciente);
                listaServ = oSP.buscaCuentaParaCerrar();
                System.out.println(listaServ.size());
            }
        }catch(Exception e){
            msg = new FacesMessage("Aviso", "Error al buscar cuentas por cerrar");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            e.printStackTrace();
        }
        return listaServ;
    }

    public void limpiaPagina() throws Exception{
        buscaCuentasParaCerrar();
        sForma="";
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
    String sConAutorizaciones="";
    FacesMessage msg;
        try{
            if (this.bAutDscto && this.bAutorizado)
                sConAutorizaciones = OperacionCaja.CON_DSCTO;
            else if (this.bAutPagoMenor && this.bAutorizado)
                sConAutorizaciones = OperacionCaja.CON_PAGO_PARCIAL;
            
            oOpeCaja.getFrmPago().setCveFrmPago(sForma);
            bResult = oOpeCaja.pagaCierreCuenta(this.selectedServ,sConAutorizaciones);
            
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
        facesContext.getExternalContext().redirect("registrarPagoCierreCuenta.xhtml?faces-redirect=true");
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
