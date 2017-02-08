/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apli.modelbeans.reportes;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import org.apli.AD.AccesoDatos;

/**
 *Clase que representa la información de los pagos de préstamos en una fecha determinada
 * @author Danny Hdz
 */
public class RptPagosDePrestamos implements Serializable {
    private Date dFecha;
    private String dOpeCaja;
    private int nFolio;
    private int nFolioPers;
    private String sNombre;
    private String sApPaterno;
    private String sApMaterno;
    private String sDepartamento;
    private float fMonto;
    private Date dRegPrestamo;
    private int nFolioPres;
    private float fPagosRealizados;
    private char cSituacion;
    private float fMontoEgreOtroIngr;
    private float fSaldoPorPagar;
    private String cCveFrmPago;
    
    private AccesoDatos oAD;
   
    public List<RptPagosDePrestamos> buscaRpt() throws Exception{
        Vector rst = null;
        String sQuery="";
        RptPagosDePrestamos oPagos;
        List<RptPagosDePrestamos> arrRet = null;
        SimpleDateFormat fmtTxt = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat fmtTxt2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            if(this.dFecha==null)
                throw new Exception("RptPagosDePrestamos.buscaRpt: Faltan datos");
            else{
                sQuery="select * from buscarptpagosdeprestamosporfecha('"+fmtTxt.format(dFecha)+"')";
                if(oAD==null){
                    oAD= new AccesoDatos();
                    oAD.conectar();
                    rst=oAD.ejecutarConsulta(sQuery);
                    oAD.desconectar();
                    oAD=null;
                }else{
                    rst=oAD.ejecutarConsulta(sQuery);
                    }
                if(rst!=null){
                    arrRet=new ArrayList();
                    for(int i=0; i<rst.size();i++){
                        oPagos=new RptPagosDePrestamos();
                        Vector vRowTemp = (Vector)rst.elementAt(i);
                        oPagos.dOpeCaja=fmtTxt2.format((Date)vRowTemp.elementAt(0));
                        oPagos.nFolio=((Double)vRowTemp.elementAt(1)).intValue();
                        oPagos.nFolioPers=((Double)vRowTemp.elementAt(2)).intValue();
                        oPagos.sNombre=((String)vRowTemp.elementAt(3));
                        oPagos.sApPaterno=((String)vRowTemp.elementAt(4));
                        oPagos .sApMaterno=((String)vRowTemp.elementAt(5));
                        oPagos .sDepartamento=((String)vRowTemp.elementAt(6));
                        oPagos.fMonto=((Double)vRowTemp.elementAt(7)).floatValue();
                        oPagos.dRegPrestamo=((Date)vRowTemp.elementAt(8));
                        oPagos.nFolioPres=((Double)vRowTemp.elementAt(9)).intValue();
                        oPagos.fPagosRealizados=((Double)vRowTemp.elementAt(10)).floatValue();
                        oPagos.cSituacion=((String)vRowTemp.elementAt(11)).charAt(0);
                        oPagos.fMontoEgreOtroIngr=((Double)vRowTemp.elementAt(12)).floatValue();
                        oPagos.fSaldoPorPagar=((Double)vRowTemp.elementAt(13)).floatValue();
                        oPagos.cCveFrmPago=((String)vRowTemp.elementAt(14));
                        arrRet.add(oPagos);
                    }
                }
        }
        return arrRet;
    }
    

    /**
     * @return the dFecha
     */
    public Date getFecha() {
        return dFecha;
    }

    /**
     * @param dFecha the dFecha to set
     */
    public void setFecha(Date dFecha) {
        this.dFecha = dFecha;
    }

    /**
     * @return the dOpeCaja
     */
    public String getOpeCaja() {
        return dOpeCaja;
    }

    /**
     * @param dOpeCaja the dOpeCaja to set
     */
    public void setOpeCaja(String dOpeCaja) {
        this.dOpeCaja = dOpeCaja;
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
     * @return the sNombre
     */
    public String getNombre() {
        return sNombre;
    }

    /**
     * @param sNombre the sNombre to set
     */
    public void setNombre(String sNombre) {
        this.sNombre = sNombre;
    }

    /**
     * @return the sApPaterno
     */
    public String getApPaterno() {
        return sApPaterno;
    }

    /**
     * @param sApPaterno the sApPaterno to set
     */
    public void setApPaterno(String sApPaterno) {
        this.sApPaterno = sApPaterno;
    }

    /**
     * @return the sApMaterno
     */
    public String getApMaterno() {
        return sApMaterno;
    }

    /**
     * @param sApMaterno the sApMaterno to set
     */
    public void setApMaterno(String sApMaterno) {
        this.sApMaterno = sApMaterno;
    }

    /**
     * @return the fMonto
     */
    public float getMonto() {
        return fMonto;
    }

    /**
     * @param fMonto the fMonto to set
     */
    public void setMonto(float fMonto) {
        this.fMonto = fMonto;
    }

    /**
     * @return the dRegPrestamo
     */
    public Date getRegPrestamo() {
        return dRegPrestamo;
    }

    /**
     * @param dRegPrestamo the dRegPrestamo to set
     */
    public void setRegPrestamo(Date dRegPrestamo) {
        this.dRegPrestamo = dRegPrestamo;
    }

    /**
     * @return the nFolioPres
     */
    public int getFolioPres() {
        return nFolioPres;
    }

    /**
     * @param nFolioPres the nFolioPres to set
     */
    public void setFolioPres(int nFolioPres) {
        this.nFolioPres = nFolioPres;
    }

    /**
     * @return the fPagosRealizados
     */
    public float getPagosRealizados() {
        return fPagosRealizados;
    }

    /**
     * @param fPagosRealizados the nPagosRealizados to set
     */
    public void setPagosRealizados(float fPagosRealizados) {
        this.fPagosRealizados = fPagosRealizados;
    }

    /**
     * @return the cSituacion
     */
    public char getSituacion() {
        return cSituacion;
    }

    /**
     * @param cSituacion the cSituacion to set
     */
    public void setSituacion(char cSituacion) {
        this.cSituacion = cSituacion;
    }

    /**
     * @return the fMontoEgreOtroIngr
     */
    public float getMontoEgreOtroIngr() {
        return fMontoEgreOtroIngr;
    }

    /**
     * @param fMontoEgreOtroIngr the fMontoEgreOtroIngr to set
     */
    public void setMontoEgreOtroIngr(float fMontoEgreOtroIngr) {
        this.fMontoEgreOtroIngr = fMontoEgreOtroIngr;
    }

    /**
     * @return the fSaldoPorPagar
     */
    public float getSaldoPorPagar() {
        return fSaldoPorPagar;
    }

    /**
     * @param fSaldoPorPagar the fSaldoPorPagar to set
     */
    public void setSaldoPorPagar(float fSaldoPorPagar) {
        this.fSaldoPorPagar = fSaldoPorPagar;
    }

    /**
     * @return the cCveFrmPago
     */
    public String getCveFrmPago() {
        return cCveFrmPago;
    }

    /**
     * @param cCveFrmPago the cCveFrmPago to set
     */
    public void setCveFrmPago(String cCveFrmPago) {
        this.cCveFrmPago = cCveFrmPago;
    }

    public String getNombreCompleto(){
        return this.sNombre+" " + this.sApPaterno + " " + this.sApMaterno;
    }

    /**
     * @return the sDepartamento
     */
    public String getDepartamento() {
        return sDepartamento;
    }

    /**
     * @param sDepartamento the sDepartamento to set
     */
    public void setDepartamento(String sDepartamento) {
        this.sDepartamento = sDepartamento;
    }
}