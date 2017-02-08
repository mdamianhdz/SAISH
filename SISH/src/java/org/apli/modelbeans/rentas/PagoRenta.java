/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apli.modelbeans.rentas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import org.apli.AD.AccesoDatos;
import org.apli.modelbeans.OpeCajaConcepto;

/**
 *
 * @author Lily_LnBnd
 */
public class PagoRenta implements Serializable{
    private int nMensualidad;
    private Date dProgramada;
    private OpeCajaConcepto oOperacionCaja;
    private AccesoDatos oAD;
    private int nTotal;
    
    public PagoRenta(){
        oOperacionCaja=new OpeCajaConcepto();
    }
    
    public List<PagoRenta> buscaPagosContrato(int nIdContrato)throws Exception{
        PagoRenta oPago=null;
        List<PagoRenta> listRet=null;
        Vector rst = null;
        String sQuery = "";
        
        sQuery = "select * from buscaPagosContrato("+nIdContrato+")";
        System.out.println(sQuery);
        if (getAD() == null){
            setAD(new AccesoDatos());
            getAD().conectar();
        }
        rst = getAD().ejecutarConsulta(sQuery);
        getAD().desconectar();
        setAD(null);
        if (rst != null) {
            listRet=new ArrayList();
            for (int i = 0; i < rst.size(); i++) {
                Vector vRowTemp = (Vector)rst.elementAt(i); 
                
                oPago=new PagoRenta();
                oPago.setMensualidad(((Double) vRowTemp.elementAt(0)).intValue());
                oPago.setProgramada((Date) vRowTemp.elementAt(1)); 
                oPago.getOperacionCaja().setNombreSerie((String) vRowTemp.elementAt(2)); 
                oPago.getOperacionCaja().setFolioCFDI(((Double) vRowTemp.elementAt(3)).intValue());
                oPago.getOperacionCaja().getOpeCaja().setFechaOp((Date) vRowTemp.elementAt(4)); 
               
                listRet.add(oPago);
            }
        }
        return listRet;
    }
    
    public void actualizaPagoRenta(int nIdContratoRenta, int nMensualidad, String nFolioCaja) throws Exception{
        Vector rst = null;
       
        String sQuery = "select * from actualizapagorenta("+nIdContratoRenta+","+nMensualidad+",'"+nFolioCaja+"')";

        if (getAD() == null) {
            setAD(new AccesoDatos());
            getAD().conectar();
            rst = getAD().ejecutarConsulta(sQuery);
            getAD().desconectar();
            setAD(null);
        }

    }
    
    public int getMensualidad() {
        return nMensualidad;
    }

    public void setMensualidad(int nMensualidad) {
        this.nMensualidad = nMensualidad;
    }

    public Date getProgramada() {
        return dProgramada;
    }

    public void setProgramada(Date dProgramada) {
        this.dProgramada = dProgramada;
    }

    public OpeCajaConcepto getOperacionCaja() {
        return oOperacionCaja;
    }

    public void setOperacionCaja(OpeCajaConcepto oOperacionCaja) {
        this.oOperacionCaja = oOperacionCaja;
    }

    public AccesoDatos getAD() {
        return oAD;
    }
    private void setAD(AccesoDatos o){
        this.oAD = o;
    }
    
    public void setTotalM(int nTot){
        this.nTotal = nTot;
    }
    
    public String getMensualidadFmt(){
    String sFmt="";
        if (this.nMensualidad==0)
            sFmt = "Depósito";
        else
            sFmt = this.nMensualidad+" de "+this.nTotal;
        return sFmt;
    }
}
