package org.apli.jbs.caja;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.apli.modelbeans.facturacion.cfdi.v32.schema.Comprobante.Receptor;
import org.apli.modelbeans.rentas.ContratoRenta;
import org.apache.log4j.Logger;
import org.apli.modelbeans.OperacionCaja;
import org.apli.modelbeans.Valida;
import org.apli.modelbeans.rentas.PagoRenta;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author Manuel
 */
@ManagedBean(name = "oRegPagRent")
@SessionScoped
public class RegistrarPagoDeRentaJB implements Serializable {

    public List<ContratoRenta> listCR;
    public String sRfc;
    public ContratoRenta oContRent;
    public ContratoRenta selectedCR;
    private static final Logger LOG = Logger.getLogger(ContratoRenta.class.getName());
    public boolean bPagoPena = false;
    public boolean bUltimoPago = false;
    public boolean bTransferencia = false;
    public boolean bCheque = false;
    public String sForma;
    public String sFolioRet;
    public String sDatosPago;
    public boolean bFactura;
    private OperacionCaja oOpeCaja;
    private Valida oValida;
    private PagoRenta oPagRent;

    public RegistrarPagoDeRentaJB() throws Exception {
    }

    public List<Receptor> getListReceptores() throws Exception {
        return new Receptor().getTodosReceptores();
    }

    public Date sumarDiasFecha(Date fecha, int dias) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        calendar.add(Calendar.DAY_OF_YEAR, dias);
        return calendar.getTime();
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

    public void onRowSelect(SelectEvent event) {
        Date sfecha = new Date();
        Date sfechaAct = new Date();
        this.bPagoPena = false;
        bUltimoPago = false;
        sfecha.getTime();
        sfechaAct.getTime();
        sfecha = sumarDiasFecha(selectedCR.oPagoRenta.getProgramada(), 7);
        
        if (this.selectedCR.oPagoRenta.getMensualidad() == 0) {
            FacesMessage msg = new FacesMessage("Aviso", ("El Monto del pago corresponde a Depósito Inicial de $" + selectedCR.getDeposito() + " ."));
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }

        if (sfechaAct.after(sfecha)) {
            this.bPagoPena = true;
            FacesMessage msg = new FacesMessage("Aviso", "Se hará un cobro extra de $1260.0 por concepto de: PAGO DE PENA CONVENCIONAL POR RENTA");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }

        if (selectedCR.oPagoRenta.getProgramada().equals(selectedCR.getFin())) {
            bUltimoPago = true;
            FacesMessage msg = new FacesMessage("Aviso", "ULTIMO PAGO");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        if (sfechaAct.after(selectedCR.getFin())) {
            bUltimoPago = true;
            FacesMessage msg = new FacesMessage("Aviso", "ULTIMO PAGO");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public void validaPagoRenta() throws Exception {
        
        if (this.selectedCR != null) {
            if (this.sForma == null) {
                FacesMessage msg = new FacesMessage("Aviso", "Seleccione una forma de pago.");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            } else {
                registraPagoRenta();
            }
        } else {
            FacesMessage msg = new FacesMessage("Aviso", "No se ha seleccionado un registro.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public void limpiaPagina() {
        bPagoPena = false;
        bUltimoPago = false;
        listCR = null;
        sRfc = "";
        selectedCR = null;
    }

    public void registraPagoRenta() throws Exception {

        String sDescripcionAlt = "Pago del Mes " + selectedCR.oPagoRenta.getMensualidad() + " del " + selectedCR.oEspacioRent.getDescripcion() + " de " + selectedCR.getReceptor().getNombre() + ".";
        String sDeseaFactura = "";
        if (bFactura == true) {
            sDeseaFactura = "1";
        } else {
            sDeseaFactura = "0";
        }
        oOpeCaja = new OperacionCaja();
        oOpeCaja.registraPagoDeRenta(this.sDatosPago, sDescripcionAlt, this.sForma, sDeseaFactura);
        oValida = new Valida();
        this.sFolioRet = oValida.eliminaMSJCorchetes(oOpeCaja.getFolioRet());
        int nConcepOtroIngr;
        if (this.selectedCR.oPagoRenta.getMensualidad() == 0) {
            nConcepOtroIngr = 258;
            oOpeCaja.detalleOperacionCajaPagoRenta(sFolioRet, nConcepOtroIngr, this.selectedCR.getRentaMensual());
        } else {
            nConcepOtroIngr = 259;
            oOpeCaja.detalleOperacionCajaPagoRenta(sFolioRet, nConcepOtroIngr, this.selectedCR.getRentaMensual());
        }
        if (this.bPagoPena == true) {
            nConcepOtroIngr = 260;
            oOpeCaja.detalleOperacionCajaPagoRentaAtrasado(sFolioRet, nConcepOtroIngr);
        }
        oPagRent = new PagoRenta();
        oPagRent.actualizaPagoRenta(this.selectedCR.getIdContratoRenta(), this.selectedCR.oPagoRenta.getMensualidad(), sFolioRet);

        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('diaE').show()");

    }

    public void regresar() throws IOException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.getExternalContext().redirect("registrarPagoDeRenta.xhtml?faces-redirect=true");
    }

    public List<ContratoRenta> cargaPagosRenta() throws Exception {
        selectedCR = null;
        listCR = new ArrayList();
        oContRent = new ContratoRenta();
        listCR = oContRent.buscaPagosRenta(sRfc);
        return listCR;
    }

    /**
     * @return the sRfc
     */
    public String getRfc() {
        return sRfc;
    }

    /**
     * @param sRfc the sRfc to set
     */
    public void setRfc(String sRfc) {
        this.sRfc = sRfc;
    }

    /**
     * @return the oContRent
     */
    public ContratoRenta getContRent() {
        return oContRent;
    }

    /**
     * @param oContRent the oContRent to set
     */
    public void setContRent(ContratoRenta oContRent) {
        this.oContRent = oContRent;
    }

    /**
     * @return the listCR
     */
    public List<ContratoRenta> getListCR() {
        return listCR;
    }

    /**
     * @param listCR the listCR to set
     */
    public void setListCR(List<ContratoRenta> listCR) {
        this.listCR = listCR;
    }

    /**
     * @return the selectedCR
     */
    public ContratoRenta getSelectedCR() {
        return selectedCR;
    }

    /**
     * @param selectedCR the selectedCR to set
     */
    public void setSelectedCR(ContratoRenta selectedCR) {
        this.selectedCR = selectedCR;
    }

    /**
     * @return the bPagoPena
     */
    public boolean isPagoPena() {
        return bPagoPena;
    }

    /**
     * @param bPagoPena the bPagoPena to set
     */
    public void setPagoPena(boolean bPagoPena) {
        this.bPagoPena = bPagoPena;
    }

    /**
     * @return the bUltimoPago
     */
    public boolean isUltimoPago() {
        return bUltimoPago;
    }

    /**
     * @param bUltimoPago the bUltimoPago to set
     */
    public void setUltimoPago(boolean bUltimoPago) {
        this.bUltimoPago = bUltimoPago;
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
     * @return the bFactura
     */
    public boolean isFactura() {
        return bFactura;
    }

    /**
     * @param bFactura the bFactura to set
     */
    public void setFactura(boolean bFactura) {
        this.bFactura = bFactura;
    }
}
