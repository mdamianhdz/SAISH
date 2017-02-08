package org.apli.jbs.caja;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import org.apache.log4j.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apli.modelbeans.OperacionCaja;
import org.apli.modelbeans.Regalia;
import org.apli.modelbeans.Valida;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Manuel
 */
@ManagedBean(name = "oRegPagExt")
@ViewScoped
public class RegistrarPagosExtrasJB implements Serializable {
    List<Regalia> listR;
    Regalia oReg;
    public Regalia selectedReg;
    private Regalia registroSeleccionado;
    private boolean bTransferencia = false;
    private boolean bCheque = false;
    private String sForma;
    public String sFolioRet;
    private static final Logger LOG = Logger.getLogger(RegistrarPagosExtrasJB.class.getName());

    public RegistrarPagosExtrasJB() throws Exception {
        getPagosPendientes();
    }

    public void regresaMenu() throws Exception {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.getExternalContext().redirect("registrarEgreso.xhtml?faces-redirect=true");
    }

    public void limpiaPagina() {
        bCheque = false;
        bTransferencia = false;
        sForma = "";
        selectedReg = null;
    }

    public void validaRegistro() throws Exception {
        if (this.registroSeleccionado != null) {
            if (sForma.equalsIgnoreCase("EFE  ")) {
                registraPagoDeRegalias();
            } else {
                FacesMessage msg = new FacesMessage("Aviso", "La Forma de Pago debe ser en 'Efectivo'");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }
        } else {
            FacesMessage msg = new FacesMessage("Aviso", "No hay servicios seleccionados.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public void registraPagoDeRegalias() throws Exception {
        OperacionCaja oOpeCaj = new OperacionCaja();
        oOpeCaj.registraPagoDeRegalias();
        Valida oValida = new Valida();
        this.sFolioRet = oValida.eliminaMSJCorchetes(oOpeCaj.getFolioRet());
        oOpeCaj.detalleOperacionCajaPagoExtra(this.sFolioRet, registroSeleccionado.getMonto());
        oReg = new Regalia();
        oReg.actualizaSituacionRegalias(registroSeleccionado.nIdPagoRegalias,this.sFolioRet);
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('diaE').show()");
    }

    public List<Regalia> getPagosPendientes() throws Exception {
        listR = new ArrayList();
        oReg = new Regalia();
        listR = oReg.pagosPendientes();
        return listR;
    }

    public void validaForma() {

        int result = getForma().compareToIgnoreCase("CHQ  ");
        int result2 = getForma().compareToIgnoreCase("TRE  ");

        if (result == 0) {
            setCheque(true);
            setTransferencia(false);
        } else if (result2 == 0) {
            setTransferencia(true);
            setCheque(false);
        } else {
            setCheque(false);
            setTransferencia(false);
        }
    }

    public void regresar() throws IOException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.getExternalContext().redirect("registrarPagosExtras.xhtml?faces-redirect=true");
    }

    /**
     * @return the selectedReg
     */
    public Regalia getSelectedReg() {
        return selectedReg;
    }

    /**
     * @param selectedReg the selectedReg to set
     */
    public void setSelectedReg(Regalia selectedReg) {
        this.selectedReg = selectedReg;
    }

    /**
     * @return the bTransferencia
     */
    public boolean isTransferencia() {
        return bTransferencia;
    }

    /**
     * @param bTransferencia the bTransferencia to set
     */
    public void setTransferencia(boolean bTransferencia) {
        this.bTransferencia = bTransferencia;
    }

    /**
     * @return the bCheque
     */
    public boolean isCheque() {
        return bCheque;
    }

    /**
     * @param bCheque the bCheque to set
     */
    public void setCheque(boolean bCheque) {
        this.bCheque = bCheque;
    }

    /**
     * @return the sForma
     */
    public String getForma() {
        return sForma;
    }

    /**
     * @param sForma the sForma to set
     */
    public void setForma(String sForma) {
        this.sForma = sForma;
    }

    /**
     * @return the registroSeleccionado
     */
    public Regalia getRegistroSeleccionado() {
        return registroSeleccionado;
    }

    /**
     * @param registroSeleccionado the registroSeleccionado to set
     */
    public void setRegistroSeleccionado(Regalia registroSeleccionado) {
        this.registroSeleccionado = registroSeleccionado;
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
}
