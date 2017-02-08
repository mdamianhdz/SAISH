/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apli.jbs.contabilidadInterna;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import org.apli.modelbeans.FormaPago;
import org.apli.modelbeans.contabilidadInterna.ConceptoMovCtaBan;
import org.apli.modelbeans.contabilidadInterna.CuentaBanco;
import org.apli.modelbeans.contabilidadInterna.MovimientoCtaBan;
import org.apli.modelbeans.facturacion.cfdi.FacturaCFI;
import org.apli.modelbeans.ventaCredito.CompaniaCred;

/**
 *
 * @author Lily_LnBnd
 */
@ManagedBean(name="oMovBan")
@RequestScoped
public class MovimientosBancosJB implements Serializable{
    private static int selectedBanco;
    private static String selectedCuenta;
    private static List<CuentaBanco> listCuentas;
    private static Date dInicio;
    private static Date dFin;
    private static List<MovimientoCtaBan> listMovimientos;
    private static MovimientoCtaBan selectedMovimiento;
    private static MovimientoCtaBan currentMovimiento;
    private static List<ConceptoMovCtaBan> listConceptos;
    private static List<FormaPago> listFormasPago;
    private static boolean bRenderFact=false;
    private static List<FacturaCFI> listFacturas;
    private static List<FacturaCFI> selectedFacturas;
    private static boolean bDisDatos=false;
    private static boolean bRenderG=false;
    private static boolean bRenderM=false;
    private static boolean bRenderE=false;
    
    public MovimientosBancosJB(){
        
    }
    
    public void buscaCuentas()throws Exception{
        listCuentas=new CuentaBanco().buscaCuentas(selectedBanco);
    }
    
    public void buscaCuentasDlg()throws Exception{
        listCuentas=new CuentaBanco().buscaCuentas(currentMovimiento.getCuentaBanco().getBanco().getIdEmp());
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
    
    public List<CompaniaCred> getListBancos()throws Exception{
        return new CompaniaCred().buscaBancos();
    }
    
    public void buscar()throws Exception{
        String sCondicion="";
        if (selectedBanco>0 && selectedCuenta.length()>0)
            sCondicion=" pnidemp="+selectedBanco+" and psnumctaban='"+selectedCuenta+"'";
        if (dInicio!=null && dFin!=null)
            if (sCondicion.length()==0)
                sCondicion="pdregistro between '"+dInicio+"' and '"+dFin+"'";
            else
                sCondicion=sCondicion+" and pdfecmov between '"+dInicio+"' and '"+dFin+"'";
        listMovimientos=new MovimientoCtaBan().buscaMovimientos(sCondicion);
    }
    
    public void confNuevo() throws Exception{
        currentMovimiento=new MovimientoCtaBan();
        currentMovimiento.setIdMovCtaBan(0);
        listCuentas=new ArrayList();
        listConceptos=new ArrayList();
        listFormasPago=new FormaPago().buscaFormasPago();
        bDisDatos=false;
        renderBotones("G");
    }
    
    public void confSel(String sOp) throws Exception{
        currentMovimiento=selectedMovimiento;
        listCuentas=new CuentaBanco().buscaCuentas(currentMovimiento.getCuentaBanco().getBanco().getIdEmp());
        listConceptos=new ConceptoMovCtaBan().buscaConceptos(currentMovimiento.getConceptoMov().getTipoMovCta());
        listFormasPago=new FormaPago().buscaFormasPago();
        if (sOp.equals("C")||sOp.equals("E"))
            bDisDatos=true;
        else
            bDisDatos=false;
        renderBotones(sOp);
    }
    
    public void renderBotones(String sVar){
        if ("G".equals(sVar)){
            bRenderG=true;
            bRenderM=false;
            bRenderE=false;
        }
        if ("M".equals(sVar)){
            bRenderM=true;
            bRenderG=false;
            bRenderE=false;
        }
        if ("E".equals(sVar)){
            bRenderE=true;
            bRenderM=false;
            bRenderG=false;
        }
        if ("C".equals(sVar)){
            bRenderE=false;
            bRenderM=false;
            bRenderG=false;
        }
    }
    
    public void validaConcepto()throws Exception{
        if(currentMovimiento.getConceptoMov().getTipoMovCta().equals("E")&& currentMovimiento.getConceptoMov().getConcepMovCta()==3){
           bRenderFact=true;
           listFacturas=new FacturaCFI().buscaPagadas();
        }else
           bRenderFact=false;
    }
    
    public void guardar()throws Exception{
        String mess="";
        if (currentMovimiento.getCuentaBanco().getBanco().getIdEmp()==0 || currentMovimiento.getCuentaBanco().getNumCtaBan().length()==0 ||
                currentMovimiento.getFechaMov()==null || currentMovimiento.getMonto()==0 || currentMovimiento.getConceptoMov().getTipoMovCta().length()==0 ||
                currentMovimiento.getConceptoMov().getConcepMovCta()==0|| currentMovimiento.getFormaPago().getCveFrmPago().length()==0||
                currentMovimiento.getNumDocto().length()==0||currentMovimiento.getReferencia().length()==0)
            mess="Error de validación. Faltan Datos";
        else{
            mess=new MovimientoCtaBan().guardarMovimiento(currentMovimiento, selectedFacturas);
            if (mess.equals("OK"))
                mess="Se ha registrado el movimiento satisfactoriamente";
            else
                mess="Se produjo un error al registrar el movimiento. Vuelva a intentarlo.";
        }
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Guardar", mess));
    }
    
    public String cambiaTipoMov(String sVar){
        if ("E".equals(sVar))
            return "ENTRADA";
        if ("S".equals(sVar))
            return "SALIDA";
        else
            return "";
    }
    
    public void modificar()throws Exception{
        String mess="";
        if (currentMovimiento.getCuentaBanco().getBanco().getIdEmp()==0 || currentMovimiento.getCuentaBanco().getNumCtaBan().length()==0 ||
                currentMovimiento.getFechaMov()==null || currentMovimiento.getMonto()==0 || currentMovimiento.getConceptoMov().getTipoMovCta().length()==0 ||
                currentMovimiento.getConceptoMov().getConcepMovCta()==0|| currentMovimiento.getFormaPago().getCveFrmPago().length()==0||
                currentMovimiento.getNumDocto().length()==0||currentMovimiento.getReferencia().length()==0)
            mess="Error de validación. Faltan Datos";
        else{
            mess=new MovimientoCtaBan().guardarMovimiento(currentMovimiento, selectedFacturas);
            if (mess.equals("OK"))
                mess="Se ha modificado el movimiento satisfactoriamente";
            else
                mess="Se produjo un error al modificar el movimiento. Vuelva a intentarlo.";
        }
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Guardar", mess));
    }
    
    public void eliminar()throws Exception{
        String mess=new MovimientoCtaBan().eliminarMovimiento(currentMovimiento.getIdMovCtaBan());
        buscar();
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Guardar", mess));
    }
    
    public void buscaConceptos()throws Exception{
        listConceptos=new ConceptoMovCtaBan().buscaConceptos(currentMovimiento.getConceptoMov().getTipoMovCta());
    }

    public int getSelectedBanco() {
        return selectedBanco;
    }

    public void setSelectedBanco(int selectedBanco) {
        MovimientosBancosJB.selectedBanco = selectedBanco;
    }
   
    public List<CuentaBanco> getListCuentas()throws Exception{
        return listCuentas;
    }
    
    public String getSelectedCuenta() {
        return selectedCuenta;
    }

    public void setSelectedCuenta(String selectedCuenta) {
        MovimientosBancosJB.selectedCuenta = selectedCuenta;
    }
    
    public Date getInicio() {
        return dInicio;
    }

    public void setInicio(Date dInicio) {
        MovimientosBancosJB.dInicio = dInicio;
    }

    public Date getFin() {
        return dFin;
    }

    public void setFin(Date dFin) {
        MovimientosBancosJB.dFin = dFin;
    }

    public List<MovimientoCtaBan> getListMovimientos() {
        return listMovimientos;
    }

    public void setListMovimientos(List<MovimientoCtaBan> listMovimientos) {
        MovimientosBancosJB.listMovimientos = listMovimientos;
    }  

    public MovimientoCtaBan getSelectedMovimiento() {
        return selectedMovimiento;
    }

    public void setSelectedMovimiento(MovimientoCtaBan selectedMovimiento) {
        MovimientosBancosJB.selectedMovimiento = selectedMovimiento;
    }

    public MovimientoCtaBan getCurrentMovimiento() {
        return currentMovimiento;
    }

    public void setCurrentMovimiento(MovimientoCtaBan currentMovimiento) {
        MovimientosBancosJB.currentMovimiento = currentMovimiento;
    }
    
    public List<ConceptoMovCtaBan> getListConceptos() {
        return listConceptos;
    }
    
    public void setListConceptos(List<ConceptoMovCtaBan> listConceptos) {
        MovimientosBancosJB.listConceptos = listConceptos;
    } 
     
    public List<FormaPago> getListFormasPago() throws Exception{
        return listFormasPago;
    }

    public boolean isRenderFact() {
        return bRenderFact;
    }

    public void setRenderFact(boolean bRenderFact) {
        MovimientosBancosJB.bRenderFact = bRenderFact;
    }

    public List<FacturaCFI> getListFacturas() {
        return listFacturas;
    }

    public void setListFacturas(List<FacturaCFI> listFacturas) {
        MovimientosBancosJB.listFacturas = listFacturas;
    }
    
    public List<FacturaCFI> getSelectedFacturas() {
        return selectedFacturas;
    }

    public void setSelectedFacturas(List<FacturaCFI> selectedFacturas) {
        MovimientosBancosJB.selectedFacturas = selectedFacturas;
    }

    public boolean isDisDatos() {
        return bDisDatos;
    }

    public void setDisDatos(boolean bDisDatos) {
        MovimientosBancosJB.bDisDatos = bDisDatos;
    }

    public boolean isRenderG() {
        return bRenderG;
    }

    public void setRenderG(boolean bRenderG) {
        MovimientosBancosJB.bRenderG = bRenderG;
    }

    public boolean isRenderM() {
        return bRenderM;
    }

    public void setRenderM(boolean bRenderM) {
        MovimientosBancosJB.bRenderM = bRenderM;
    }

    public boolean isRenderE() {
        return bRenderE;
    }

    public void setRenderE(boolean bRenderE) {
        MovimientosBancosJB.bRenderE = bRenderE;
    }
   
}
