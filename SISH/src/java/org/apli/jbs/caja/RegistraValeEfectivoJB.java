/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apli.jbs.caja;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.apli.modelbeans.PersonalHospitalario;
import org.apli.modelbeans.ValeEfectivo;
import org.apli.modelbeans.contabilidadInterna.AreaFuncionamiento;
import java.util.Date;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.apli.modelbeans.OperacionCaja;
import org.apli.modelbeans.Valida;
import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Manuel
 */
@ManagedBean(name = "oRegValEfec")
@SessionScoped
public class RegistraValeEfectivoJB implements Serializable {
    
    public RegistraValeEfectivoJB() throws Exception {
        getAreasFuncionamiento();
        actualizapersonalPorAreaFuncionamiento();
    }
    public List<AreaFuncionamiento> listAF;
    public List<ValeEfectivo> listVE;
    public List<PersonalHospitalario> listPH;
    public List<PersonalHospitalario> listPHN;
    public PersonalHospitalario oPH;
    public ValeEfectivo oVE;
    public AreaFuncionamiento oAF;
    public int nArea;
    public int nFolioPers;
    public Date dini;
    public Date dfin;
    public ValeEfectivo selectedVE;
    public String srazon;
    public Valida oValida = new Valida();
    public String sFolioRet = "";
    public double nMonto;
    public int nAreaN;
    public int nFolioPersN;
    public boolean focusDialog;
    public int valor;
    private static final Logger LOG = Logger.getLogger(OperacionCaja.class.getName());
    
    public void limpiaPagina() throws Exception {
        selectedVE = null;
        nArea = 0;
        getAreasFuncionamiento();
        actualizapersonalPorAreaFuncionamiento();
        listVE = null;
        srazon = "";
        nMonto = 0;
        this.focusDialog = false;
        
    }
    
    public List<AreaFuncionamiento> getAreasFuncionamiento() throws Exception {
        this.listAF = new ArrayList();
        this.oAF = new AreaFuncionamiento();
        this.listAF = oAF.todasAreasFuncionamiento();
        return this.listAF;
    }
    
    public List<PersonalHospitalario> actualizaPersonalNuevo() throws Exception {
        
        listPHN = new ArrayList();
        oPH = new PersonalHospitalario();
        LOG.trace(new StringBuilder().append("Area Nueva: [").append(this.nAreaN).append("]").toString());
        listPHN = oPH.buscaPersonalAreaFuncionamiento(nAreaN);
        LOG.trace(new StringBuilder().append("Folio Personal: [").append(this.listPHN).append("]").toString());
        return this.listPHN;
    }
    
    public List<PersonalHospitalario> actualizapersonalPorAreaFuncionamiento() throws Exception {
        LOG.trace(new StringBuilder().append("Area Nueva: [").append(this.nArea).append("]").toString());
        LOG.trace(new StringBuilder().append("Area Nueva Dialog: [").append(this.nAreaN).append("]").toString());
        LOG.trace(new StringBuilder().append("focusDialog: [").append(this.focusDialog).append("]").toString());
        listPH = new ArrayList();
        oPH = new PersonalHospitalario();
        listPH = (this.focusDialog) ? oPH.buscaPersonalAreaFuncionamiento(this.nAreaN) : oPH.buscaPersonalAreaFuncionamiento(nArea);
        return this.listPH;
    }
    
    public List<ValeEfectivo> valeEfectivoFiltro() throws Exception {
        
        this.listVE = new ArrayList();
        this.oVE = new ValeEfectivo();
        LOG.trace(new StringBuilder().append("Folio Personal: [").append(this.nFolioPers).append("]").toString());
        LOG.trace(new StringBuilder().append("Fecha de inicio: [").append(this.dini).append("]").toString());
        LOG.trace(new StringBuilder().append("Fecha de fin: [").append(this.dfin).append("]").toString());
        this.listVE = oVE.buscaValeEfectivo(this.nFolioPers, this.dini, this.dfin);
        return listVE;
    }
    
    public void registraValeEfectivo() throws Exception {
        OperacionCaja oOpeCaja = new OperacionCaja();
        oOpeCaja.registraValeEfectivo();
        oValida = new Valida();
        this.sFolioRet = oValida.eliminaMSJCorchetes(oOpeCaja.getFolioRet());
        oOpeCaja.detalleOperacionCajaValeEfectivo(this.sFolioRet, this.nMonto);
        int nfolio = Integer.parseInt(this.sFolioRet);
        oVE = new ValeEfectivo();
        oVE.insertarValeEfectivo(this.nFolioPers, this.sFolioRet);
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.getExternalContext().redirect("registrarValeEfectivo.xhtml?faces-redirect=true");
    }
    
    public void validaCancelaValeEfectivo() throws Exception {
        if (this.selectedVE != null) {
            LOG.trace(new StringBuilder().append("Situacion: [").append(this.selectedVE.getSituacion()).append("]").toString());
            if (this.selectedVE.getSituacion().equalsIgnoreCase("Entregado")) {
                RequestContext context = RequestContext.getCurrentInstance();
                context.execute("PF('diaC').show()");
            } else if (this.selectedVE.getSituacion().equalsIgnoreCase("Cancelado")) {
                RequestContext context = RequestContext.getCurrentInstance();
                context.execute("PF('diaSC').show()");
            }
        } else {
            FacesMessage msg = new FacesMessage("Aviso", "No hay vale seleccionado.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
    
    public void cancelaValeEfectivo() throws Exception {
        LOG.trace(new StringBuilder().append("Folio idVale2: [").append(this.selectedVE.getIdVale()).append("]").toString());
        oVE = new ValeEfectivo();
        oVE.cancelaValeEfectivo(this.selectedVE.getIdVale(), srazon);
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('diaEC').show()");
        
    }
    
    public void regresaPrincipal() throws IOException {
        this.selectedVE = null;
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.getExternalContext().redirect("registrarValeEfectivo.xhtml?faces-redirect=true");
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
     * @return the nMonto
     */
    public double getMonto() {
        return nMonto;
    }

    /**
     * @param nMonto the nMonto to set
     */
    public void setMonto(double nMonto) {
        this.nMonto = nMonto;
    }

    /**
     * @return the dini
     */
    public Date getFechaInicial() {
        return dini;
    }

    /**
     * @param dini the dini to set
     */
    public void setFechaInicial(Date dini) {
        this.dini = dini;
    }

    /**
     * @return the dfin
     */
    public Date getFechaFinal() {
        return dfin;
    }

    /**
     * @param dfin the dfin to set
     */
    public void setFechaFinal(Date dfin) {
        this.dfin = dfin;
    }

    /**
     * @return the selectedVE
     */
    public ValeEfectivo getSelectedVE() {
        return selectedVE;
    }

    /**
     * @param selectedVE the selectedVE to set
     */
    public void setSelectedVE(ValeEfectivo selectedVE) {
        this.selectedVE = selectedVE;
    }

    /**
     * @return the listAF
     */
    public List<AreaFuncionamiento> getListAF() {
        return listAF;
    }

    /**
     * @param listAF the listAF to set
     */
    public void setListAF(List<AreaFuncionamiento> listAF) {
        this.listAF = listAF;
    }

    /**
     * @return the listVE
     */
    public List<ValeEfectivo> getListVE() {
        return listVE;
    }

    /**
     * @param listVE the listVE to set
     */
    public void setListVE(List<ValeEfectivo> listVE) {
        this.listVE = listVE;
    }

    /**
     * @return the listPH
     */
    public List<PersonalHospitalario> getListPH() {
        return listPH;
    }

    /**
     * @param listPH the listPH to set
     */
    public void setListPH(List<PersonalHospitalario> listPH) {
        this.listPH = listPH;
    }

    /**
     * @return the oPH
     */
    public PersonalHospitalario getPH() {
        return oPH;
    }

    /**
     * @param oPH the oPH to set
     */
    public void setPH(PersonalHospitalario oPH) {
        this.oPH = oPH;
    }

    /**
     * @return the oVE
     */
    public ValeEfectivo getVE() {
        return oVE;
    }

    /**
     * @param oVE the oVE to set
     */
    public void setVE(ValeEfectivo oVE) {
        this.oVE = oVE;
    }

    /**
     * @return the oAF
     */
    public AreaFuncionamiento getAF() {
        return oAF;
    }

    /**
     * @param oAF the oAF to set
     */
    public void setAF(AreaFuncionamiento oAF) {
        this.oAF = oAF;
    }

    /**
     * @return the srazon
     */
    public String getRazon() {
        return srazon;
    }

    /**
     * @param srazon the srazon to set
     */
    public void setRazon(String srazon) {
        this.srazon = srazon;
    }

    /**
     * @return the oValida
     */
    public Valida getValida() {
        return oValida;
    }

    /**
     * @param oValida the oValida to set
     */
    public void setValida(Valida oValida) {
        this.oValida = oValida;
    }

    /**
     * @return the nArea
     */
    public int getArea() {
        return nArea;
    }

    /**
     * @param nArea the nArea to set
     */
    public void setArea(int nArea) {
        this.focusDialog = false;
        LOG.info("Area recibida: [" + nArea + "]");
        this.nArea = nArea;
    }

    /**
     * @return the nFolioPers
     */
    public int getFolioPers() {
        return nFolioPers;
    }

    /**
     * @param nFolioPers the nFolioPers to set
     */
    public void setFolioPers(int nFolioPers) {
        this.nFolioPers = nFolioPers;
    }

    /**
     * @return the nAreaN
     */
    public int getAreaN() {
        return nAreaN;
    }

    /**
     * @param nAreaN the nAreaN to set
     */
    public void setAreaN(int valor) {
        this.focusDialog = true;
        LOG.info("Area recibida Dialog: [" + valor + "]");
        this.nAreaN = valor;
    }

    /**
     * @return the nFolioPersN
     */
    public int getFolioPersN() {
        return nFolioPersN;
    }

    /**
     * @param nFolioPersN the nFolioPersN to set
     */
    public void setFolioPersN(int nFolioPersN) {
        this.nFolioPersN = nFolioPersN;
    }

    /**
     * @return the listPHN
     */
    public List<PersonalHospitalario> getListPHN() {
        return listPHN;
    }

    /**
     * @param listPHN the listPHN to set
     */
    public void setListPHN(List<PersonalHospitalario> listPHN) {
        this.listPHN = listPHN;
    }

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.focusDialog = true;
        LOG.info("Area recibida Dialog: [" + valor + "] en nueva variable 'valor' ");
        this.valor = valor;
    }
    
}
