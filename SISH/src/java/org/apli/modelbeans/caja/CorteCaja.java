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
import org.apli.jbs.caja.CorteCajaJB;

/**
 *
 * @author Lily_LnBnd
 */
public class CorteCaja implements Serializable{
    private int nIdCorte;
    private Date dRegistro;
    private float nSaldoFinal;
    private List<DatosCorteCaja> listOperaciones;
    private AccesoDatos oAD;
    
    public CorteCaja(){
        
    }
    
    public CorteCaja buscaUltimoCorte() throws Exception{
        CorteCaja oCorte=null;
        Vector rst = null;
        String sQuery = "";
        
        sQuery = "select * from buscaUltimoCorte()";
        System.out.println(sQuery);
        if (getAD() == null){
            setAD(new AccesoDatos());
            getAD().conectar();
        }
        rst = getAD().ejecutarConsulta(sQuery);
        getAD().desconectar();
        setAD(null);
        oCorte=new CorteCaja();
        oCorte.setSaldoFinal(0);
        if (rst != null && rst.size()>0) {
            Vector vRowTemp = (Vector)rst.elementAt(0); 
            oCorte.setIdCorte(((Double) vRowTemp.elementAt(0)).intValue());
            oCorte.setRegistro((Date) vRowTemp.elementAt(1)); 
            oCorte.setSaldoFinal(((Double) vRowTemp.elementAt(2)).floatValue());   
        }
        return oCorte;
    }
    
    public String guardaCorteCaja(List<DatosCorteCaja> listOperaciones, Date dFecha)throws Exception{
        Vector rst = null;
        String sQuery = "", sRet="";
        
        if (listOperaciones==null){
             throw new Exception("Corte de Caja: error de programación, faltan datos");
        }else{
            sQuery="select * from guardaCorteCaja('"+dFecha+"',"+
                    new CorteCajaJB().calculaSaldo(listOperaciones.get(listOperaciones.size()-1).getOpeCC().getOpeCaja().getFolio(), 
                    listOperaciones.get(listOperaciones.size()-1).getMonto())+");";
            for (int i = 1; i < listOperaciones.size()-1; i++) {
                if (i==1)
                    sQuery=sQuery+"\n select * from guardaDetalleCorteCaja("+listOperaciones.get(i).getOpeCC().getOpeCaja().getFolio() +");";
                else
                    if(listOperaciones.get(i).getOpeCC().getOpeCaja().getFolio()!=listOperaciones.get(i-1).getOpeCC().getOpeCaja().getFolio())
                        sQuery=sQuery+"\n select * from guardaDetalleCorteCaja("+listOperaciones.get(i).getOpeCC().getOpeCaja().getFolio() +");";
            }
            
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
        if ("Se ha registrado el corte de caja exitosamente".equals(rst.get(0).toString().substring(1, rst.get(0).toString().length()-1)))
            sRet="Se ha registrado el corte de caja exitosamente";
        else
            if ("Multiple ResultSets were returned by the query".equals(rst.get(0).toString().substring(1, rst.get(0).toString().length()-1)))
                sRet="Se ha registrado el corte de caja exitosamente";
            else
                sRet="Se produjo un error mientras se almacenaban los datos. Vuelva a intentarlo";
        return sRet;
    }
    
    public List<CorteCaja> buscaCortesCaja(String sCondicion) throws Exception{
        CorteCaja oCC;
        DatosCorteCaja oDatosCC;
        List<CorteCaja> listRet=null;
        Vector rst = null;
        String sQuery = "";
        
        sQuery = "select * from buscadetallescortecaja()";
        if (!sCondicion.equals(""))
            sQuery=sQuery+" where "+sCondicion;
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
                if (i==0){
                    oCC=new CorteCaja();
                    oCC.setIdCorte(((Double) vRowTemp.elementAt(0)).intValue());
                    oCC.setRegistro(((Date) vRowTemp.elementAt(1)));
                    oCC.setSaldoFinal(((Double) vRowTemp.elementAt(2)).floatValue());
                    oCC.setListOperaciones(new ArrayList());
                    oDatosCC=new DatosCorteCaja();
                    oDatosCC.getOpeCC().getOpeCaja().setFolio(0);
                    oDatosCC.setConcepto("SALDO INICIAL");
                    oDatosCC.setMonto(((Double) vRowTemp.elementAt(10)).floatValue());
                    oDatosCC.setTipoIngreso("");
                    oDatosCC.setTipoOperacion("");
                    oCC.getListOperaciones().add(oDatosCC);
                    oDatosCC=new DatosCorteCaja();
                    oDatosCC.getOpeCC().getOpeCaja().setFolio(((Double) vRowTemp.elementAt(3)).intValue());
                    oDatosCC.getOpeCC().getOpeCaja().setFechaOp(((Date) vRowTemp.elementAt(4)));
                    oDatosCC.setConcepto((String) vRowTemp.elementAt(5)); 
                    oDatosCC.getFormaPago().setDescripcion((String) vRowTemp.elementAt(6)); 
                    oDatosCC.setTipoOperacion((String) vRowTemp.elementAt(7)); 
                    oDatosCC.setMonto(((Double) vRowTemp.elementAt(8)).floatValue());
                    oDatosCC.setTipoIngreso((String) vRowTemp.elementAt(9)); 
                    oCC.getListOperaciones().add(oDatosCC);
                    listRet.add(oCC);
                }else{
                    if ((listRet.get(listRet.size()-1)).getIdCorte()!=((Double)vRowTemp.elementAt(0)).intValue()){
                        oDatosCC=new DatosCorteCaja();
                        oDatosCC.getOpeCC().getOpeCaja().setFolio(-1);
                        oDatosCC.setConcepto("TOTAL");
                        oDatosCC.setMonto(0);
                        oDatosCC.setTipoIngreso("");
                        oDatosCC.setTipoOperacion("");
                        listRet.get(listRet.size()-1).getListOperaciones().add(oDatosCC);
                        oCC=new CorteCaja();
                        oCC.setIdCorte(((Double) vRowTemp.elementAt(0)).intValue());
                        oCC.setRegistro(((Date) vRowTemp.elementAt(1)));
                        oCC.setSaldoFinal(((Double) vRowTemp.elementAt(2)).floatValue());
                        oCC.setListOperaciones(new ArrayList());
                        oDatosCC=new DatosCorteCaja();
                        oDatosCC.getOpeCC().getOpeCaja().setFolio(0);
                        oDatosCC.setConcepto("SALDO INICIAL");
                        oDatosCC.setMonto(((Double) vRowTemp.elementAt(10)).floatValue());
                        oDatosCC.setTipoIngreso("");
                        oDatosCC.setTipoOperacion("");
                        oCC.getListOperaciones().add(oDatosCC);
                        oDatosCC=new DatosCorteCaja();
                        oDatosCC.getOpeCC().getOpeCaja().setFolio(((Double) vRowTemp.elementAt(3)).intValue());
                        oDatosCC.getOpeCC().getOpeCaja().setFechaOp(((Date) vRowTemp.elementAt(4)));
                        oDatosCC.setConcepto((String) vRowTemp.elementAt(5)); 
                        oDatosCC.getFormaPago().setDescripcion((String) vRowTemp.elementAt(6)); 
                        oDatosCC.setTipoOperacion((String) vRowTemp.elementAt(7)); 
                        oDatosCC.setMonto(((Double) vRowTemp.elementAt(8)).floatValue());
                        oDatosCC.setTipoIngreso((String) vRowTemp.elementAt(9)); 
                        oCC.getListOperaciones().add(oDatosCC);
                        listRet.add(oCC);
                        if (i==rst.size()-1){
                            oDatosCC=new DatosCorteCaja();
                            oDatosCC.getOpeCC().getOpeCaja().setFolio(-1);
                            oDatosCC.setConcepto("TOTAL");
                            oDatosCC.setMonto(0);
                            oDatosCC.setTipoIngreso("");
                            oDatosCC.setTipoOperacion("");
                            listRet.get(listRet.size()-1).getListOperaciones().add(oDatosCC);
                        }
                    }else{
                        oDatosCC=new DatosCorteCaja();
                        oDatosCC.getOpeCC().getOpeCaja().setFolio(((Double) vRowTemp.elementAt(3)).intValue());
                        oDatosCC.getOpeCC().getOpeCaja().setFechaOp(((Date) vRowTemp.elementAt(4)));
                        oDatosCC.setConcepto((String) vRowTemp.elementAt(5)); 
                        oDatosCC.getFormaPago().setDescripcion((String) vRowTemp.elementAt(6)); 
                        oDatosCC.setTipoOperacion((String) vRowTemp.elementAt(7)); 
                        oDatosCC.setMonto(((Double) vRowTemp.elementAt(8)).floatValue());
                        oDatosCC.setTipoIngreso((String) vRowTemp.elementAt(9)); 
                        listRet.get(listRet.size()-1).getListOperaciones().add(oDatosCC);
                        if (i==rst.size()-1){
                            oDatosCC=new DatosCorteCaja();
                            oDatosCC.getOpeCC().getOpeCaja().setFolio(-1);
                            oDatosCC.setConcepto("TOTAL");
                            oDatosCC.setMonto(0);
                            oDatosCC.setTipoIngreso("");
                            oDatosCC.setTipoOperacion("");
                            listRet.get(listRet.size()-1).getListOperaciones().add(oDatosCC);
                        }
                    }
                }  
            }
            
        }
        return listRet;
    }

    public int getIdCorte() {
        return nIdCorte;
    }

    public void setIdCorte(int nIdCorte) {
        this.nIdCorte = nIdCorte;
    }

    public Date getRegistro() {
        return dRegistro;
    }

    public void setRegistro(Date dRegistro) {
        this.dRegistro = dRegistro;
    }

    public float getSaldoFinal() {
        return nSaldoFinal;
    }

    public void setSaldoFinal(float nSaldoFinal) {
        this.nSaldoFinal = nSaldoFinal;
    }

    public AccesoDatos getAD() {
        return oAD;
    }

    public void setAD(AccesoDatos oAD) {
        this.oAD = oAD;
    } 

    public List<DatosCorteCaja> getListOperaciones() {
        return listOperaciones;
    }

    public void setListOperaciones(List<DatosCorteCaja> listOperaciones) {
        this.listOperaciones = listOperaciones;
    }
}
