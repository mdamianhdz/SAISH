/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apli.jbs.caja;

import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import org.apli.modelbeans.Valida;
import org.apli.modelbeans.personal.Prestamo;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Manuel
 */
@ManagedBean(name = "oRegPagPrest")
@ViewScoped
public class RegistrarPagoDePrestamoJB extends IngresosCaja {

    private Prestamo oPrest;
    private List<Prestamo> listPP;
    private String sFolioRet;
    private int nFolio;
    private int nMonto;
    private Prestamo selectedPrestamo;
    private String sDatosPago;

    public RegistrarPagoDePrestamoJB() {
    }

    public List<SelectItem> getPersonal(){
    List<SelectItem> lista=null;
    Prestamo oPres = new Prestamo();
    List<Prestamo> listaPres;
        try{
            listaPres = oPres.buscaPersonalConPrestamosPorPagar();
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
    
    public void registrarPago() {
        if (selectedPrestamo != null) {
            if (nMonto == 0 || sForma.equalsIgnoreCase("")) {
                FacesMessage msg = new FacesMessage("Aviso", "Faltan Datos.");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            } else {
                if (nMonto <= (selectedPrestamo.getMonto() - selectedPrestamo.getMontoPagado())) {
                    try{
                        oPrest = new Prestamo();
                        this.oOpeCaja.setMontoOtro(nMonto);
                        this.oOpeCaja.getFrmPago().setCveFrmPago(sForma);
                        this.oOpeCaja.setDescripcion("Pago de Préstamo");
                        this.oOpeCaja.registraPagoPrestamo(this.selectedPrestamo.getFolioPrestamo());
                        sFolioRet = this.oOpeCaja.getFolioRet();
                        RequestContext.getCurrentInstance().execute("diaE.show()");
                    }catch(Exception e){
                        FacesMessage msg = new FacesMessage("Aviso", ("Error al registrar el pago"));
                        FacesContext.getCurrentInstance().addMessage(null, msg);
                        e.printStackTrace();
                    }
                } else {
                    FacesMessage msg = new FacesMessage("Aviso", ("El monto a pagar no debe ser mayor a $" + (selectedPrestamo.getMonto() - selectedPrestamo.getMontoPagado()) + " ."));
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                }
            }

        } else {
            FacesMessage msg = new FacesMessage("Aviso", "No hay pagos seleccionados.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public List<Prestamo> buscaPrestamosPersonal() throws Exception {
        bCheque = false;
        bTransferencia = false;
        listPP = new ArrayList();
        oPrest = new Prestamo();
        listPP = oPrest.buscaPrestamoPersonalEntregado(nFolio);
        return listPP;
    }

    public void regresa() throws Exception {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.getExternalContext().redirect("registrarPagoDePrestamos.xhtml?faces-redirect=true");
    }
/*
    public void registraPagoDePrestamo() {        
        try{
            oPrest = new Prestamo();
            this.oOpeCaja.registraPagoPrestamo(this.selectedPrestamo.getFolioPrestamo());
            sFolioRet = this.oOpeCaja.getFolioRet();
            System.out.println(sFolioRet);
            RequestContext.getCurrentInstance().execute("diaE.show()");
        }catch(Exception e){
            FacesMessage msg = new FacesMessage("Aviso", ("Error al registrar el pago"));
            FacesContext.getCurrentInstance().addMessage(null, msg);
            e.printStackTrace();
        }
    }
*/
    public double getMontoPendiente() {
        if (this.selectedPrestamo != null && this.nMonto != 0) {
            return new Valida().redondeaCifra(this.selectedPrestamo.getMonto() - (this.selectedPrestamo.getMontoPagado() + this.nMonto));
        } else {
            return 0;
        }
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
}
