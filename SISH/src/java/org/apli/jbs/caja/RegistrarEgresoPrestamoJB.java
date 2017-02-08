package org.apli.jbs.caja;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import org.apli.modelbeans.FormaPago;
import org.apli.modelbeans.OperacionCaja;
import org.apli.modelbeans.PersonalHospitalario;
import org.apli.modelbeans.Valida;
import org.apli.modelbeans.personal.Prestamo;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Manuel
 */
@ManagedBean(name = "oRegEgrePrest")
@ViewScoped
public class RegistrarEgresoPrestamoJB implements Serializable {

    public List<PersonalHospitalario> listPH;
    public List<Prestamo> listPP;
    PersonalHospitalario oPH;
    public int nFolio;
    public int nMonto;
    /**
     *
     */
    public String sForma;
    public FormaPago oFP;
    public Prestamo oPrest;
    public boolean bTransferencia = false;
    public boolean bCheque = false;
    private Prestamo selectedPrestamo;
    private OperacionCaja oOpeCaja;
    public String sFolioRet;
    private Valida oValida = new Valida();

    /**
     *
     */
    public RegistrarEgresoPrestamoJB() {
    }

    
    public List<SelectItem> getPersonal(){
    List<SelectItem> lista=null;
    Prestamo oPres = new Prestamo();
    List<Prestamo> listaPres;
        try{
            listaPres = oPres.buscaPersonalConPrestamosPorEntregar();
            if (listaPres != null){
                lista = new ArrayList<SelectItem>();
                for (int i=0; i< listaPres.size(); i++){
                    lista.add(new SelectItem(listaPres.get(i).getPersonal().getFolioPers()+"",
                            listaPres.get(i).getPersonal().getNombreCompleto()));
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return lista;
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

    public List<FormaPago> getFormasDePago() throws Exception {
        FormaPago oFrmPago = new FormaPago();
        List<FormaPago> lista = new ArrayList();
        oFrmPago.setCveFrmPago(FormaPago.CVE_EFE);
        oFrmPago.setDescripcion("Efectivo");
        lista.add(oFrmPago);
        return lista;
    }

    /**
     *
     */
    public boolean ValidaBusquedaPersonal() {
        boolean validacion = false;
        return validacion;
    }

    public List<PersonalHospitalario> buscaPersonalHospitalario() throws Exception {

        listPH = new ArrayList();
        oPH = new PersonalHospitalario();
        listPH = oPH.buscaPersonal(nFolio);
        return listPH;

    }

    public List<Prestamo> buscaPrestamosPersonal() throws Exception {
        bCheque = false;
        bTransferencia = false;
        listPP = new ArrayList();
        oPrest = new Prestamo();
        try{
            listPP = oPrest.buscaPrestamoPersonal(nFolio);
            if (listPP==null || listPP.isEmpty()){
                FacesMessage msg = new FacesMessage("Aviso","No hay préstamos del personal indicado");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listPP;
    }

    public void regresa() throws Exception {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.getExternalContext().redirect("registrarEgresoPrestamo.xhtml?faces-redirect=true");
    }

    public void limpiaPagina() {
        oPH = null;
        nFolio = 0;
        nMonto = 0;
        sForma = "";
        bTransferencia = false;
        bCheque = false;
        listPP = null;
    }

    public void validaEgreso() throws Exception {
        if (selectedPrestamo != null) {
            if (sForma.contains("EFE")) {
                registrarEgresoPrest();
            } else {
                FacesMessage msg = new FacesMessage("Aviso","La Forma de Pago debe ser en 'Efectivo'");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }

        } else {
            FacesMessage msg = new FacesMessage("Aviso", "No hay servicios seleccionados.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public void registrarEgresoPrest() throws Exception {
        String sSituacion = "E";
        System.out.println("Entra a Registra Egreso");
        oOpeCaja = new OperacionCaja();
        oOpeCaja.setDatosPago(this.selectedPrestamo.getFolioPrestamo()+"");
        oOpeCaja.registraEgresoPrestamo(this.selectedPrestamo.getFolioPrestamo());
        oValida = new Valida();
        this.sFolioRet = oValida.eliminaMSJCorchetes(oOpeCaja.getFolioRet());
        oOpeCaja.detalleOperacionCajaEgresoPrestamo(this.sFolioRet, 
                this.selectedPrestamo.getConcep().getCveConcepEgr(),
                this.selectedPrestamo.getMonto());
        oPrest = new Prestamo();
        oPrest.actualizaSituacionPrestamo(this.selectedPrestamo.getFolioPrestamo(), sSituacion);
        RequestContext.getCurrentInstance().execute("diaE.show()");
    }

    /**
     * @return the nFolio
     */
    public int getFolio() {
        return nFolio;
    }

    /**
     * @param nFolio the nFolio to set
     */
    public void setFolio(int nFolio) {
        this.nFolio = nFolio;
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
     * @return the nMonto
     */
    public int getMonto() {
        return nMonto;
    }

    /**
     * @param nMonto the nMonto to set
     */
    public void setMonto(int nMonto) {
        this.nMonto = nMonto;
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
     * @return the oFP
     */
    public FormaPago getFP() {
        return oFP;
    }

    /**
     * @param oFP the oFP to set
     */
    public void setFP(FormaPago oFP) {
        this.oFP = oFP;
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
     * @return the listPP
     */
    public List<Prestamo> getListPP() {
        return listPP;
    }

    /**
     * @param listPP the listPP to set
     */
    public void setListPP(List<Prestamo> listPP) {
        this.listPP = listPP;
    }

    /**
     * @return the oPrest
     */
    public Prestamo getPrest() {
        return oPrest;
    }

    /**
     * @param oPrest the oPrest to set
     */
    public void setPrest(Prestamo oPrest) {
        this.oPrest = oPrest;
    }

    /**
     * @return the selectedPrestamo
     */
    public Prestamo getSelectedPrestamo() {
        return selectedPrestamo;
    }

    /**
     * @param selectedPrestamo the selectedPrestamo to set
     */
    public void setSelectedPrestamo(Prestamo selectedPrestamo) {
        this.selectedPrestamo = selectedPrestamo;
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
