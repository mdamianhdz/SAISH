/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apli.modelbeans.cobranza;

import java.io.Serializable;
import java.util.Date;
import java.util.Vector;
import org.apli.AD.AccesoDatos;
import org.apli.modelbeans.facturacion.cfdi.FacturaCFI;

/**
 *
 * @author Lily_LnBnd
 */
public class PagoCFDI implements Serializable{
    private FacturaCFI oFactura;
    private Date dFechaPago;
    private String sReferencia;
    private AccesoDatos oAD;
    
    public PagoCFDI(){
        
    }
    
    public String gurdaPagoCFDI(FacturaCFI oFact, Date dFecha, String sFormaPago, String sRef)throws Exception{
        Vector rst = null;
        String sQuery = "", sRet="";
        
        if (oFact==null||dFecha==null||sRef.length()==0){
             throw new Exception("PagoCFDI: error de programación, faltan datos");
        }else{
            sQuery="select * from identificarPagoCFDI('"+oFact.getNombreSerie()+"', "+oFact.getFolio()+"::int2, '"+dFecha+
                    "', '"+sFormaPago+"', '"+sRef+"');";
            
            System.out.println(sQuery);
            if (getAD() == null){
                setAD(new AccesoDatos());
                getAD().conectar();
                rst = getAD().ejecutarConsulta(sQuery);
                getAD().desconectar();
                setAD(null);
            }else{
                rst = getAD().ejecutarConsulta(sQuery);
            }
        }
        return ""+rst.get(0).toString().substring(1, rst.get(0).toString().length() - 1);
    }

    public FacturaCFI getFactura() {
        return oFactura;
    }

    public void setFactura(FacturaCFI oFactura) {
        this.oFactura = oFactura;
    }

    public Date getFechaPago() {
        return dFechaPago;
    }

    public void setFechaPago(Date dFechaPago) {
        this.dFechaPago = dFechaPago;
    }

    public String getReferencia() {
        return sReferencia;
    }

    public void setReferencia(String sReferencia) {
        this.sReferencia = sReferencia;
    }

    public AccesoDatos getAD() {
        return oAD;
    }

    public void setAD(AccesoDatos oAD) {
        this.oAD = oAD;
    }
}
