/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apli.modelbeans.caja;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import org.apli.AD.AccesoDatos;
import org.apli.modelbeans.FormaPago;
import org.apli.modelbeans.OpeCajaConcepto;

/**
 *
 * @author Lily_LnBnd
 */
public class DatosCorteCaja implements Serializable{
    private OpeCajaConcepto oOpeCC;
    private String sConcepto;
    private FormaPago oFormaPago;
    private String sTipoOperacion;
    private float nMonto;
    private String sTipoIngreso;
    private AccesoDatos oAD;
    
    public DatosCorteCaja(){
        oOpeCC=new OpeCajaConcepto();
        oFormaPago=new FormaPago();
    }
    
    public List<DatosCorteCaja> buscaDatosCorteCaja() throws Exception{
        DatosCorteCaja oDatos=null;
        List<DatosCorteCaja> listRet=null;
        Vector rst = null;
        String sQuery = "";
        
            sQuery = "select * from buscaDatosCorteCaja()";
        if (getAD() == null){
            setAD(new AccesoDatos());
            getAD().conectar();
        }
        rst = getAD().ejecutarConsulta(sQuery);
        getAD().desconectar();
        setAD(null);
        
        listRet=new ArrayList();
        oDatos=new DatosCorteCaja();
        oDatos.setConcepto("SALDO INICIAL");
        oDatos.setMonto(new CorteCaja().buscaUltimoCorte().getSaldoFinal());
        listRet.add(oDatos);
        if (rst != null) {
            for (int i = 0; i < rst.size(); i++) {
                Vector vRowTemp = (Vector)rst.elementAt(i); 
               
                oDatos=new DatosCorteCaja();
                oDatos.getOpeCC().getOpeCaja().setFolio(((Double) vRowTemp.elementAt(0)).intValue());
                oDatos.getOpeCC().getOpeCaja().setFechaOp((Date) vRowTemp.elementAt(1));
                oDatos.setConcepto((String) vRowTemp.elementAt(2));
                oDatos.getFormaPago().setDescripcion((String) vRowTemp.elementAt(3));
                oDatos.setTipoOperacion((String) vRowTemp.elementAt(4));
                oDatos.setMonto(((Double) vRowTemp.elementAt(5)).intValue());
                oDatos.setTipoIngreso((String) vRowTemp.elementAt(6));

                listRet.add(oDatos);
            }
        }
        oDatos=new DatosCorteCaja();
        oDatos.setConcepto("TOTAL");
        oDatos.getOpeCC().getOpeCaja().setFolio(-1);
        oDatos.setTipoOperacion("");
        listRet.add(oDatos);
        return listRet;
    }

    public OpeCajaConcepto getOpeCC() {
        return oOpeCC;
    }

    public void setOpeCC(OpeCajaConcepto oOpeCC) {
        this.oOpeCC = oOpeCC;
    }

    public String getConcepto() {
        return sConcepto;
    }

    public void setConcepto(String sConcepto) {
        this.sConcepto = sConcepto;
    }

    public FormaPago getFormaPago() {
        return oFormaPago;
    }

    public void setFormaPago(FormaPago oFormaPago) {
        this.oFormaPago = oFormaPago;
    }

    public String getTipoOperacion() {
        return sTipoOperacion;
    }

    public void setTipoOperacion(String sTipoOperacion) {
        this.sTipoOperacion = sTipoOperacion;
    }

    public float getMonto() {
        return nMonto;
    }

    public void setMonto(float nMonto) {
        this.nMonto = nMonto;
    }

    public String getTipoIngreso() {
        return sTipoIngreso;
    }

    public void setTipoIngreso(String sTipoIngreso) {
        this.sTipoIngreso = sTipoIngreso;
    }

    public AccesoDatos getAD() {
        return oAD;
    }

    public void setAD(AccesoDatos oAD) {
        this.oAD = oAD;
    }
    
    
}
