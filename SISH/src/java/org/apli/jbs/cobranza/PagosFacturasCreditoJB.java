/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apli.jbs.cobranza;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import org.apli.modelbeans.ContraRecibo;
import org.apli.modelbeans.FormaPago;
import org.apli.modelbeans.cobranza.PagoCFDI;
import org.apli.modelbeans.ventaCredito.CompaniaCred;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Lily_LnBnd
 */
@ManagedBean(name="oFacturasCred")
@RequestScoped
public class PagosFacturasCreditoJB implements Serializable{
    private static int selectedCompania;
    private static List<ContraRecibo> listFacturas;
    private static ContraRecibo selectedFactura;
    private static Date dFechaPago;
    private static String sReferencia;
    private static String sFormaPago;
    
    public PagosFacturasCreditoJB(){
        
    }
    
    public void buscaFacturas()throws Exception{
        String sCondicion="";
        if (selectedCompania>0)
            sCondicion="pnidemp="+selectedCompania;
        listFacturas=new ContraRecibo().buscaFacturasCredito(sCondicion);
    }
    
    public void identificar(){
        System.out.println("identificar"+selectedFactura);
        String mess;
        if (selectedFactura==null){
            mess="Error! Debe seleccionar un registro.";
            FacesContext context= FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Pagar facturas a crédito",mess));
            context.getExternalContext().getFlash().setKeepMessages(true);
        }else{
            RequestContext.getCurrentInstance().execute("dlgPago.show()");
        }
    }
    
    public void guardar()throws Exception{
        String mess=new PagoCFDI().gurdaPagoCFDI(selectedFactura.getFactura(), dFechaPago, sFormaPago, sReferencia);
        buscaFacturas();
        FacesContext context= FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Pagar facturas a crédito",mess));
        context.getExternalContext().getFlash().setKeepMessages(true);
    }

    public int getSelectedCompania() {
        return selectedCompania;
    }

    public void setSelectedCompania(int selectedCompania) {
        PagosFacturasCreditoJB.selectedCompania = selectedCompania;
    }
   
    public List<CompaniaCred> getListCompanias()throws Exception{
        return new CompaniaCred().buscaCompanias();
    } 

    public List<ContraRecibo> getListFacturas() {
        return listFacturas;
    }

    public void setListFacturas(List<ContraRecibo> listFacturas) {
        PagosFacturasCreditoJB.listFacturas = listFacturas;
    }

    public ContraRecibo getSelectedFactura() {
        return selectedFactura;
    }

    public void setSelectedFactura(ContraRecibo selectedFactura) {
        PagosFacturasCreditoJB.selectedFactura = selectedFactura;
    }

    public Date getFechaPago() {
        return dFechaPago;
    }

    public void setFechaPago(Date dFechaPago) {
        PagosFacturasCreditoJB.dFechaPago = dFechaPago;
    }

    public String getReferencia() {
        return sReferencia;
    }

    public void setReferencia(String sReferencia) {
        PagosFacturasCreditoJB.sReferencia = sReferencia;
    }
    
    public List<FormaPago> getListFormasPago()throws Exception{
        return new FormaPago().buscaFormasPago();
    } 

    public  String getFormaPago() {
        return sFormaPago;
    }

    public  void setFormaPago(String sFormaPago) {
        PagosFacturasCreditoJB.sFormaPago = sFormaPago;
    }
 
    
}
