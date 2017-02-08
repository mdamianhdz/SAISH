/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apli.jbs.caja;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.apli.AD.AccesoDatos;
import org.apli.jbs.PacienteJB;
import org.apli.modelbeans.OperacionCaja;
import org.apli.modelbeans.Paciente;
import org.apli.modelbeans.ServicioPrestado;
import org.apli.modelbeans.Valida;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Manuel
 */
@ManagedBean(name = "oRegDevDep")
@SessionScoped
public class RegistrarDevolucionDepositoJB implements Serializable {

    private static final Logger LOG = Logger.getLogger(RegistrarDevolucionDepositoJB.class.getName());
    public Paciente paciente;
    public AccesoDatos oAD;
    public List<ServicioPrestado> selectedDeps;
    ServicioPrestado oSP;
    public List<ServicioPrestado> listDP;
    public double monto;
    public double montoDev;
    public String sRazon="";
    public boolean valida = false;
    public String sFolioRet = "";
    private Valida oValida = new Valida();
    public ServicioPrestado selectedServ;

    public RegistrarDevolucionDepositoJB() {
    }

    public void regresaMenu() throws Exception {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        // facesContext.getExternalContext().redirect("registrarEgreso.xhtml?faces-redirect=true");
        facesContext.getExternalContext().redirect(".../caja/registrarDevolucionDeposito.xhtml?faces-redirect=true");


    }

    public void guardaDevolucion() {
        monto = 123.00;
        montoDev = 0;
    }

    public boolean validaUsuario() {

        boolean validacion = false;
        if (paciente != null) {
            validacion = true;
        }
        return validacion;

    }

    public void calculaMonto() {
        if (selectedServ == null) {
            montoDev = 0;
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('diaNS').show()");
        } else {
            this.montoDev = selectedServ.getCostoOriginal();
        }
    }

    public void validaMontos() {
        if (selectedServ != null) {
            System.out.print(this.montoDev);
            if (this.monto < this.montoDev) {
                RequestContext context = RequestContext.getCurrentInstance();
                context.execute("PF('diaD').show()");
            }
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('diaNS').show()");
        }
    }

    public void validaDevolucion() throws Exception {
        if (selectedServ != null) {
            LOG.debug(this);
            LOG.debug("Result: " + (this.monto < 0.0) + " monto=[" + this.monto + "]");
            if (this.monto > 0) {
                if (this.monto < this.montoDev) {
                    RequestContext context = RequestContext.getCurrentInstance();
                    context.execute("PF('diaD').show()");
                } else {
                    registraDevolucion();
                }
            } else {
                RequestContext context = RequestContext.getCurrentInstance();
                context.execute("PF('diaN').show()");
            }
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('diaNS').show()");
        }
    }

    public void limpiaPagina() {
        valida = false;
        paciente = null;
        monto = 0;
        selectedServ = null;
        montoDev = 0;
    }

    public void regresar() throws Exception {
        selectedServ = new ServicioPrestado();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.getExternalContext().redirect("registrarDevolucionDeposito.xhtml?faces-redirect=true");
    }

    public void registraDevolucion() throws Exception {
        OperacionCaja oOpeCaj = new OperacionCaja();
        oOpeCaj.registraDevolucionDeposito(sRazon, getSelectedServ().getIdFolio());
        oValida = new Valida();
        this.sFolioRet = oValida.eliminaMSJCorchetes(oOpeCaj.getFolioRet());
        System.out.print(this.sFolioRet);
        oOpeCaj.detalleOperacionCajaDevolucionDeposito(this.sFolioRet, this.selectedServ.getIdFolio(), this.selectedServ.getCostoOriginal());
        ServicioPrestado oServPrest = new ServicioPrestado();
        oServPrest.actualizaSituacionServicio(this.selectedServ.getIdFolio(), "C");

        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('diaE').show()");

    }

    public List<ServicioPrestado> buscaDepositosPacientes() throws Exception {
        paciente = new PacienteJB().getPacienteSesion();
        listDP = new ArrayList();
        oSP = new ServicioPrestado();
        listDP = oSP.buscadepositosPaciente(paciente.getFolioPac());
        return listDP;
    }

    public List<ServicioPrestado> buscaDepositosPaciente() throws Exception {

        this.paciente = new PacienteJB().getPacienteSesion();

        this.listDP = new ArrayList();
        this.oSP = new ServicioPrestado();
        this.listDP = oSP.buscadepositosPaciente(paciente.getFolioPac());
        // return this.listDP;
        return this.listDP;
    }

    public void seleccion() throws Exception {
        if (selectedServ == null) {
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('diaNS').show()");
        } else {
            registraDevolucion();
        }

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

    /**
     * @return the oAD
     */
    public AccesoDatos getAD() {
        return oAD;
    }

    /**
     * @param oAD the oAD to set
     */
    public void setAD(AccesoDatos oAD) {
        this.oAD = oAD;
    }

    /**
     * @return the selectedDeps
     */
    public List<ServicioPrestado> getSelectedDeps() {
        return selectedDeps;
    }

    /**
     * @param selectedDeps the selectedDeps to set
     */
    public void setSelectedDeps(List<ServicioPrestado> selectedDeps) {
        this.selectedDeps = selectedDeps;
    }

    /**
     * @return the listDP
     */
    public List<ServicioPrestado> getListDP() {
        return listDP;
    }

    /**
     * @param listDP the listDP to set
     */
    public void setListDP(List<ServicioPrestado> listDP) {
        this.listDP = listDP;
    }

    /**
     * @return the monto
     */
    public double getMonto() {
        return monto;
    }

    /**
     * @param monto the monto to set
     */
    public void setMonto(double monto) {
        this.monto = monto;
    }

    /**
     * @return the montodev
     */
    public double getMontoDev() {
        return montoDev;
    }

    /**
     * @param montodev the montodev to set
     */
    public void setMontoDev(double montoDev) {
        this.montoDev = montoDev;
    }

    /**
     * @return the sRazon
     */
    public String getRazon() {
        return sRazon;
    }

    /**
     * @param sRazon the sRazon to set
     */
    public void setRazon(String sRazon) {
        this.sRazon = sRazon;
    }

    /**
     * @return the valida
     */
    public boolean isValida() {
        return valida;
    }

    /**
     * @param valida the valida to set
     */
    public void setValida(boolean valida) {
        this.valida = valida;
    }

    /**
     * @return the sFolioRet
     */
    public String getFolioRet() {
        return sFolioRet;
    }

    /**
     * @param sFolioRet the sFolioRet to set
     */
    public void setFolioRet(String sFolioRet) {
        this.sFolioRet = sFolioRet;
    }

    /**
     * @return the selectedServ
     */
    public ServicioPrestado getSelectedServ() {
        return selectedServ;
    }

    /**
     * @param selectedServ the selectedServ to set
     */
    public void setSelectedServ(ServicioPrestado selectedServ) {
        this.selectedServ = selectedServ;
    }

    @Override
    public String toString() {
        return "RegistrarDevolucionDepositoJB{" + "paciente=" + paciente + ", oAD=" + oAD + ", selectedDeps=" + selectedDeps + ", oSP=" + oSP + ", listDP=" + listDP + ", monto=" + monto + ", montoDev=" + montoDev + ", sRazon=" + sRazon + ", valida=" + valida + ", sFolioRet=" + sFolioRet + ", oValida=" + oValida + ", selectedServ=" + selectedServ + '}';
    }
}
