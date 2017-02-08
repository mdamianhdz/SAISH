/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apli.jbs.caja;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.apli.modelbeans.AreaDeServicio;
import org.apli.modelbeans.AreaFisica;
import org.apli.modelbeans.ConceptoEgreso;
import org.apli.modelbeans.ValeEfectivo;
import org.apli.modelbeans.contabilidadInterna.LineaEgreso;
import org.apli.modelbeans.contabilidadInterna.Proveedor;
import org.apli.modelbeans.contabilidadInterna.SublineaEgreso;
import org.primefaces.context.RequestContext;
import org.apache.log4j.Logger;
import org.apli.modelbeans.OperacionCaja;
import org.apli.modelbeans.Valida;
import org.apli.modelbeans.contabilidadInterna.Gasto;

/**
 *
 * @author Manuel
 */
@ManagedBean(name = "oRegEgreCom")
@ViewScoped
public class RegistraEgresoCompraJB implements Serializable {

    private List<ValeEfectivo> listVE;
    public List<Proveedor> listPV;
    public List<LineaEgreso> listLE;
    public List<SublineaEgreso> listSE;
    public List<ConceptoEgreso> listCE;
    public List<AreaFisica> listAF;
    public List<AreaDeServicio> listAS;
    private ValeEfectivo oVE;
    private Proveedor oPV;
    private LineaEgreso oLE;
    private SublineaEgreso oSE;
    private ConceptoEgreso oCE;
    private AreaFisica oAF;
    private AreaDeServicio oAS;
    public int nIdvale;
    public int nIdProv;
    public int nCveLineaEgre;
    public int nCveSublineaEgre;
    public int nCveConcepEgr;
    public int nCveAreaServ;
    public int nCveAreaFis;
    public float nMonto;
    public ValeEfectivo oValeEfectivo;
    public String sFolioRet;
    public OperacionCaja oOpeCaja;
    public Valida oValida;
    public Gasto oGasto;
    private static final Logger LOG = Logger.getLogger(OperacionCaja.class.getName());

    public RegistraEgresoCompraJB() throws Exception {
        listaNumeroVales();
        buscaTodosProveedores();
        buscaTodasLineasEgreso();
    }

    public void limpiaPagina() throws Exception {
        listaNumeroVales();
        nCveAreaFis = 0;
        nCveAreaServ = 0;
        nCveConcepEgr = 0;
        nCveLineaEgre = 0;
        nCveLineaEgre = 0;
        nCveSublineaEgre = 0;
        nIdProv = 0;
        nMonto = 0;
        nIdvale = 0;
        oValeEfectivo = null;
    }

    private List<ValeEfectivo> listaNumeroVales() throws Exception {
        this.listVE = new ArrayList();
        this.oVE = new ValeEfectivo();
        this.listVE = oVE.todosIdValeEfectivo();
        return this.listVE;
    }

    public ValeEfectivo buscaValeEfectivo() throws Exception {
        this.oValeEfectivo = new ValeEfectivo();
        this.oVE = new ValeEfectivo();
        this.oValeEfectivo = oVE.buscaValeEfectivo(getIdvale());
        return this.oValeEfectivo;

    }

    private List<Proveedor> buscaTodosProveedores() throws Exception {
        this.listPV = new ArrayList();
        this.oPV = new Proveedor();
        this.listPV = oPV.buscaProveedores();
        return this.listPV;
    }

    private List<LineaEgreso> buscaTodasLineasEgreso() throws Exception {
        this.listLE = new ArrayList();
        this.oLE = new LineaEgreso();
        this.listLE = oLE.buscaTodosLineaEgreso();
        return this.listLE;
    }

    public List<SublineaEgreso> buscaSublineasEgreso() throws Exception {
        this.listSE = new ArrayList();
        this.oSE = new SublineaEgreso();
        this.listSE = oSE.buscaSublineas(nCveLineaEgre);
        return this.listSE;
    }

    public List<ConceptoEgreso> buscaConceptoEgreso() throws Exception {
        this.listCE = new ArrayList();
        this.oCE = new ConceptoEgreso();
        this.listCE = oCE.buscaConceptosEgreso(nCveSublineaEgre);
        return this.listCE;
    }

    public List<AreaFisica> buscaAreaFisicas() throws Exception {
        this.listAF = new ArrayList();
        this.oAF = new AreaFisica();
        this.listAF = oAF.buscaTodosAF();
        return this.listAF;
    }

    public List<AreaFisica> buscaAreaFisicaConcepEgre() throws Exception {
        this.listAF = new ArrayList();
        this.oAF = new AreaFisica();
        this.listAF = oAF.buscaAreasFConceptoEgreso(nCveConcepEgr);
        return this.listAF;
    }

    public List<AreaDeServicio> buscaTodasAreasDeServicio() throws Exception {
        this.listAS = new ArrayList();
        this.oAS = new AreaDeServicio();
        this.listAS = oAS.buscaAreasServicio();
        return this.listAS;
    }

    public List<AreaDeServicio> buscaAreasDeServicioConcepEgreso() throws Exception {
        this.listAS = new ArrayList();
        this.oAS = new AreaDeServicio();
        this.listAS = oAS.buscaAreasSConceptoEgreso(nCveConcepEgr);
        return this.listAS;
    }

    public void areasConcepEgre() throws Exception {
        buscaAreaFisicaConcepEgre();
        buscaAreasDeServicioConcepEgreso();
    }
    
    public void buttonAction(ActionEvent actionEvent) {
        System.out.println("vale "+oValeEfectivo);
        FacesMessage msg = new FacesMessage("Aviso", "Otro aviso");
                FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    public void validaEgresoCompra() throws Exception {
        System.out.println("vale "+oValeEfectivo);
        if (oValeEfectivo != null) {
            LOG.trace(new StringBuilder().append("Monto Ingresado: [").append(this.nMonto).append("]").toString());
            LOG.trace(new StringBuilder().append("Monto Vale: [").append(this.oValeEfectivo.oOpeCajaConcep.getMonto()).append("]").toString());
            if (this.nCveAreaServ == 0 || this.nCveAreaFis == 0
                    || this.nCveAreaServ == 0 || this.nCveConcepEgr == 0 || this.nCveLineaEgre == 0 || this.nCveSublineaEgre == 0
                    || this.nIdProv == 0) {
                FacesMessage msg = new FacesMessage("Aviso", "Faltan Datos.");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            } else {
                if (this.nMonto == 0) {
                    FacesMessage msg = new FacesMessage("Aviso", "El Monto de Compra debe ser mayor a 0.");
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                } else if (this.nMonto > this.oValeEfectivo.oOpeCajaConcep.getMonto()) {
                    FacesMessage msg = new FacesMessage("Aviso", "El Monto de Compra debe ser menor o igual al Monto del Vale.");
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                } else {
                    registraEgresoCompra();
                }
            }
        } else {
            FacesMessage msg = new FacesMessage("Aviso", "No se ha seleccionado un Vale.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public void regresar() throws IOException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.getExternalContext().redirect("registrarEgresoCompra.xhtml?faces-redirect=true");
    }

    public void registraEgresoCompra() throws Exception {
        oOpeCaja = new OperacionCaja();
        oOpeCaja.registraOperacionCajaEgreComp();
        oValida = new Valida();
        sFolioRet = oValida.eliminaMSJCorchetes(oOpeCaja.getFolioRet());
        oOpeCaja.detalleOperacionCajaPagoCompra(sFolioRet, nCveConcepEgr, nMonto);
        String sDescrip = "Gasto Vale de Efectivo con Id " + oValeEfectivo.nIdVale + "";
        oGasto = new Gasto();
        oGasto.insertaGastoCompra(sDescrip, nMonto, nIdProv, nCveConcepEgr, nCveAreaServ, nCveAreaFis, sFolioRet);
        oVE = new ValeEfectivo();
        String sRazon = "Cambio a Compra";
        oVE.cancelaValeEfectivo(nIdvale, sRazon);

        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('diaE').show()");
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

    public List<ValeEfectivo> getListVE() {
        return listVE;
    }

    public void setListVE(List<ValeEfectivo> listVE) {
        this.listVE = listVE;
    }

    /**
     * @return the oValeEfectivo
     */
    public ValeEfectivo getValeEfectivo() {
        return oValeEfectivo;
    }

    /**
     * @param oValeEfectivo the oValeEfectivo to set
     */
    public void setValeEfectivo(ValeEfectivo oValeEfectivo) {
        this.oValeEfectivo = oValeEfectivo;
    }

    /**
     * @return the listPV
     */
    public List<Proveedor> getListPV() {
        return listPV;
    }

    /**
     * @param listPV the listPV to set
     */
    public void setListPV(List<Proveedor> listPV) {
        this.listPV = listPV;
    }

    /**
     * @return the nIdProv
     */
    public int getIdProv() {
        return nIdProv;
    }

    /**
     * @param nIdProv the nIdProv to set
     */
    public void setIdProv(int nIdProv) {
        this.nIdProv = nIdProv;
    }

    /**
     * @return the nIdvale
     */
    public int getIdvale() {
        return nIdvale;
    }

    /**
     * @param nIdvale the nIdvale to set
     */
    public void setIdvale(int nIdvale) {
        this.nIdvale = nIdvale;
    }

    /**
     * @return the listLE
     */
    public List<LineaEgreso> getListLE() {
        return listLE;
    }

    /**
     * @param listLE the listLE to set
     */
    public void setListLE(List<LineaEgreso> listLE) {
        this.listLE = listLE;
    }

    /**
     * @return the nCveLineaEgre
     */
    public int getCveLineaEgre() {
        return nCveLineaEgre;
    }

    /**
     * @param nCveLineaEgre the nCveLineaEgre to set
     */
    public void setCveLineaEgre(int nCveLineaEgre) {
        this.nCveLineaEgre = nCveLineaEgre;
    }

    /**
     * @return the listSE
     */
    public List<SublineaEgreso> getListSE() {
        return listSE;
    }

    /**
     * @param listSE the listSE to set
     */
    public void setListSE(List<SublineaEgreso> listSE) {
        this.listSE = listSE;
    }

    /**
     * @return the nCveSublineaEgre
     */
    public int getCveSublineaEgre() {
        return nCveSublineaEgre;
    }

    /**
     * @param nCveSublineaEgre the nCveSublineaEgre to set
     */
    public void setCveSublineaEgre(int nCveSublineaEgre) {
        this.nCveSublineaEgre = nCveSublineaEgre;
    }

    /**
     * @return the listCE
     */
    public List<ConceptoEgreso> getListCE() {
        return listCE;
    }

    /**
     * @param listCE the listCE to set
     */
    public void setListCE(List<ConceptoEgreso> listCE) {
        this.listCE = listCE;
    }

    /**
     * @return the nCveConcepEgr
     */
    public int getCveConcepEgr() {
        return nCveConcepEgr;
    }

    /**
     * @param nCveConcepEgr the nCveConcepEgr to set
     */
    public void setCveConcepEgr(int nCveConcepEgr) {
        this.nCveConcepEgr = nCveConcepEgr;
    }

    /**
     * @return the listAF
     */
    public List<AreaFisica> getListAF() {
        return listAF;
    }

    /**
     * @param listAF the listAF to set
     */
    public void setListAF(List<AreaFisica> listAF) {
        this.listAF = listAF;
    }

    /**
     * @return the listAS
     */
    public List<AreaDeServicio> getListAS() {
        return listAS;
    }

    /**
     * @param listAS the listAS to set
     */
    public void setListAS(List<AreaDeServicio> listAS) {
        this.listAS = listAS;
    }

    /**
     * @return the nCveAreaServ
     */
    public int getCveAreaServ() {
        return nCveAreaServ;
    }

    /**
     * @param nCveAreaServ the nCveAreaServ to set
     */
    public void setCveAreaServ(int nCveAreaServ) {
        this.nCveAreaServ = nCveAreaServ;
    }

    /**
     * @return the nCveAreaFis
     */
    public int getCveAreaFis() {
        return nCveAreaFis;
    }

    /**
     * @param nCveAreaFis the nCveAreaFis to set
     */
    public void setCveAreaFis(int nCveAreaFis) {
        this.nCveAreaFis = nCveAreaFis;
    }

    /**
     * @return the nMonto
     */
    public float getMonto() {
        return nMonto;
    }

    /**
     * @param nMonto the nMonto to set
     */
    public void setMonto(float value) {
        this.nMonto = value;
    }
}
