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
import org.apli.modelbeans.PagoHonorarios;
import org.apli.modelbeans.Valida;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Manuel
 */
@ManagedBean(name = "oRegEgrePagHon")
@ViewScoped
public class registrarEgresoPagoDeHonorariosJB implements Serializable {

    public String sForma = "";
    public boolean bTransferencia = false;
    public boolean bCheque = false;
    private Valida oValida = new Valida();
    private OperacionCaja oOpeCaja = new OperacionCaja();
    public String sDatosPago = "";
    public List<PagoHonorarios> listPH;
    public PagoHonorarios selectedPH;
    private PagoHonorarios oPH;
    public String sFolioRet;

    public registrarEgresoPagoDeHonorariosJB() throws Exception {
        cargaPagosPendientes();
    }

    public void limpiaPagina() throws Exception{
        cargaPagosPendientes();
        sForma = "";
        bTransferencia = false;
        bCheque = false;

    }

    public void validaForma() {

        int result = sForma.compareToIgnoreCase("CHQ  ");
        int result2 = sForma.compareToIgnoreCase("TRE  ");

        if (result == 0) {

            bCheque = true;
            bTransferencia = false;
        } else if (result2 == 0) {
            bTransferencia = true;
            bCheque = false;
        } else {
            bCheque = false;
            bTransferencia = false;
        }
    }

    public void validaPago() throws Exception {
        
        if (selectedPH != null) {
            if (sForma.equalsIgnoreCase("EFE  ")) {
                registraPagoHonorario();
            } else {
                FacesMessage msg = new FacesMessage("Aviso", "La Forma de Pago debe ser en 'Efectivo'");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }
        } else {
            FacesMessage msg = new FacesMessage("Aviso", "No hay servicios seleccionados.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    @Override
    public String toString() {
        return "registrarEgresoPagoDeHonorariosJB{" + "sForma=" + sForma + ", bTransferencia=" + bTransferencia + ", bCheque=" + bCheque + ", oValida=" + oValida + ", oOpeCaja=" + oOpeCaja + ", sDatosPago=" + sDatosPago + ", listPH=" + listPH + ", selectedPH=" + selectedPH + ", oPH=" + oPH + ", sFolioRet=" + sFolioRet + '}';
    }
    private static final Logger LOG = Logger.getLogger(registrarEgresoPagoDeHonorariosJB.class.getName());

    public void registraPagoHonorario() throws Exception {

        OperacionCaja oOpeCaj = new OperacionCaja();
        oOpeCaj.registraPagoHonorarios();
        oValida = new Valida();
        this.sFolioRet = oValida.eliminaMSJCorchetes(oOpeCaj.getFolioRet());
        System.out.print(this.sFolioRet);
        oOpeCaj.detalleOperacionCajaPagoHonorarios(this.sFolioRet, Double.parseDouble(this.selectedPH.getMonto()));
        PagoHonorarios oPagHon = new PagoHonorarios();
        oPagHon.actualizaSituacionPagoHonorarios(selectedPH.getIdPagoHonorarios(), Integer.parseInt(this.sFolioRet));
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('diaE').show()");
    }

    public void regresar() throws IOException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.getExternalContext().redirect("registrarEgresoPagoDeHonorarios.xhtml?faces-redirect=true");
    }

    private List<PagoHonorarios> cargaPagosPendientes() throws Exception {
        listPH = new ArrayList();
        oPH = new PagoHonorarios();
        listPH = oPH.todosPagosAutorizados();
        return listPH;
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
     * @return the sDatosPago
     */
    public String getDatosPago() {
        return sDatosPago;
    }

    /**
     * @param sDatosPago the sDatosPago to set
     */
    public void setDatosPago(String sDatosPago) {
        this.sDatosPago = sDatosPago;
    }

    /**
     * @return the listPH
     */
    public List<PagoHonorarios> getListPH() {
        return listPH;
    }

    /**
     * @param listPH the listPH to set
     */
    public void setListPH(List<PagoHonorarios> listPH) {
        this.listPH = listPH;
    }

    /**
     * @return the selectedPH
     */
    public PagoHonorarios getSelectedPH() {
        return selectedPH;
    }

    /**
     * @param selectedPH the selectedPH to set
     */
    public void setSelectedPH(PagoHonorarios selectedPH) {
        this.selectedPH = selectedPH;
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
