package org.apli.jbs.contabilidadInterna;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apli.modelbeans.contabilidadInterna.Gasto;
import org.apli.modelbeans.contabilidadInterna.Proveedor;
import org.apli.modelbeans.contabilidadInterna.CuentaBanco;
import org.apli.modelbeans.contabilidadInterna.MovimientoCtaBan;
import org.apli.modelbeans.ventaCredito.CompaniaCred;

/**
 *
 * @author Lily_LnBnd
 */
@ManagedBean(name="oPagos")
@ViewScoped
public class PagosARealizarJB implements Serializable{
    private List<Gasto> listGastos;
    private List<CuentaBanco> listCuentas;    
    private Gasto selectedGasto;
    private MovimientoCtaBan oMovCtaBan;
    private boolean bRenderCheque=true;
    private int selectedProv;
    private Date dInicio;
    private Date dFin;
    private boolean bDisBoton=true;
    
    public PagosARealizarJB(){
        this.selectedGasto = new Gasto();
    }
    
    public void validaFecha(){
        String mess="";
        if (dInicio==null)
            mess="No ha especificado la fecha de inicio";
        else
            if (dInicio.compareTo(dFin)>0)
                mess="La fecha final del periodo debe ser posterior a la fecha de inicio";
        if (!"".equals(mess)){
            FacesContext context= FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Capturar Pago",mess));
            context.getExternalContext().getFlash().setKeepMessages(true);
        }
    }
    
    public void buscar()throws Exception{
        selectedGasto=null;
        String sCondicion="pssituacion<>'2'";
        if (selectedProv!=0)
            sCondicion=sCondicion+" and pnidprov="+selectedProv;
        if (dInicio!=null && dFin!=null)
            sCondicion=sCondicion+" and pdregistro between '"+dInicio+"' and '"+dFin+"'";
        listGastos=new Gasto().buscaGastos(sCondicion);
    }
    
    public void buscaCuentas()throws Exception{
        listCuentas=new CuentaBanco().buscaCuentas(selectedGasto.getMovCtaBan().getCuentaBanco().getBanco().getIdEmp());
    }
    
    public void validaFormaPago(){
        if (selectedGasto.getMovCtaBan().getFormaPago().getCveFrmPago().equals("CHQ"))
            bRenderCheque=true;
        else
            bRenderCheque=false;
    }
    
    public void habilitaBoton(){
        if (selectedGasto==null)
            bDisBoton=true;
        else
            bDisBoton=false;
    }
    
    public void guardar() throws Exception{
        String mess;
        if (selectedGasto.getMovCtaBan().getFormaPago().getCveFrmPago()==null ||
            selectedGasto.getMovCtaBan().getFormaPago().getCveFrmPago().equals(""))
            mess="Error de Validación. Faltan datos";
        else
            mess= new MovimientoCtaBan().guardar(selectedGasto);
        FacesMessage message = new FacesMessage("Guardar", mess);
        FacesContext.getCurrentInstance().addMessage(null, message);
        buscar();
    }

    public List<Gasto> getListGastos() throws Exception{
        return listGastos;
    }

    public void setListGastos(List<Gasto> value) {
        listGastos = value;
    }

    public Gasto getSelectedGasto() {
        System.out.println(selectedGasto);
        return selectedGasto;
    }

    public void setSelectedGasto(Gasto value) {
        selectedGasto = value;
    }

    public MovimientoCtaBan getMovCtaBan() {
        return oMovCtaBan;
    }

    public void setMovCtaBan(MovimientoCtaBan oMovCtaBan) {
        this.oMovCtaBan = oMovCtaBan;
    }  
    
    public List<CompaniaCred> getListBancos()throws Exception{
        return new CompaniaCred().buscaBancos();
    }
    
    public List<CuentaBanco> getListCuentas()throws Exception{
        return listCuentas;
    }

    public boolean isRenderCheque() {
        return bRenderCheque;
    }

    public void setRenderCheque(boolean bRenderCheque) {
        this.bRenderCheque = bRenderCheque;
    }

    public int getSelectedProv() {
        return selectedProv;
    }

    public void setSelectedProv(int value) {
        selectedProv = value;
    }

    public Date getInicio() {
        return dInicio;
    }

    public void setInicio(Date value) {
        dInicio = value;
    }

    public Date getFin() {
        return dFin;
    }

    public void setFin(Date value) {
        dFin = value;
    }
    
    public List<Proveedor> getListProveedores()throws Exception{
        return new Proveedor().buscaProveedores();
    }

    public boolean isDisBoton() {
        return bDisBoton;
    }

    public void setDisBoton(boolean bDisBoton) {
        this.bDisBoton = bDisBoton;
    }
}
