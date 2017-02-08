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
 * Bean administrado para el registro de pago de anticipos (paquete o cuenta)
 * @author Manuel /BAOZ
 */
@ManagedBean(name = "oRegPagAnt")
@ViewScoped
public class RegistrarPagoAnticipoJB extends IngresosCaja implements Serializable {
    private Paciente paciente;
    private List<ServicioPrestado> listAP;
    private ServicioPrestado oSP;
    
    public RegistrarPagoAnticipoJB() {
    }

    public List<ServicioPrestado> buscaAnticiposPorPagar(){
    paciente = new PacienteJB().getPacienteSesion();
    listAP = new ArrayList();
    oSP = new ServicioPrestado();
    FacesMessage msg;
        try{
            if (paciente != null){
                oSP.setPaciente(paciente);
                listAP = oSP.buscaAnticiposPorPagar(paciente.getFolioPac());
            }
        }catch(Exception e){
            msg = new FacesMessage("Aviso", "Error al buscar anticipos por pagar");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            e.printStackTrace();
        }
        return listAP;
    }

    public void limpiaPagina() throws Exception{
        buscaAnticiposPorPagar();
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
    String sConAutorizaciones="";
    FacesMessage msg;
        try{
            if (this.bAutDscto && this.bAutorizado)
                sConAutorizaciones = OperacionCaja.CON_DSCTO;
            else if (this.bAutPagoMenor && this.bAutorizado)
                sConAutorizaciones = OperacionCaja.CON_PAGO_PARCIAL;
            if (selectedServ.getPaqueteContratado().getIdpaqcont() == 0) {
                oOpeCaja.getFrmPago().setCveFrmPago(sForma);
                bResult = oOpeCaja.pagaAnticipo(
                        OperacionCaja.TIPO_ANTICIPO_CTA,
                        this.selectedServ,sConAutorizaciones);
            } else {
                oOpeCaja.getFrmPago().setCveFrmPago(sForma);
                bResult = oOpeCaja.pagaAnticipo(
                        OperacionCaja.TIPO_ANTICIPO_PAQ,
                        this.selectedServ,sConAutorizaciones);
            }
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
        facesContext.getExternalContext().redirect("registrarPagoDeAnticipo.xhtml?faces-redirect=true");
    }

    /**
     * @return the listAP
     */
    public List<ServicioPrestado> getListAP() {
        return listAP;
    }

    /**
     * @param listAP the listAP to set
     */
    public void setListAP(List<ServicioPrestado> listAP) {
        this.listAP = listAP;
    }

    /**
     * @return the paciente
     */
    public Paciente getPaciente() {
        return paciente;
    }

    /**
     * @param paciente the paciente to set
     */
    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }
}
