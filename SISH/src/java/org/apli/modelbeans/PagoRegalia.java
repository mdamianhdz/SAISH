/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.apli.modelbeans;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import org.apli.AD.AccesoDatos;

/**
 *
 * @author HumbertMarin
 */
public class PagoRegalia implements Serializable{
    
    private int nIdPagoRegalias;
    private PersonalHospitalario oPersHosp;
    private Date dfin;
    private Date dinicio;
    private float nmontototal;
    private String ssituacion;
    private Date dpago;
    private Date dautoriz;
    public static final String SIT_AUTORIZ = "1";
    /**
      * Variable para el acceso a datos
      */
    protected AccesoDatos oAD = null;
    
    /**
         * Agrega una nueva cita medica a la base de datos
         * Regresa el número de registros afectados (insertados)
         **/
        public int insertar() throws Exception{
            Vector nRet = null;
            String sQuery = "";
            String msj="";
            if (this.oPersHosp.getFolioPers()==0  || 
                    this.dfin==null || 
                    this.dinicio==null ){
                throw new Exception("Pago Regalias.insertar: error de programación, faltan datos");
            }
            else {
                SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                sQuery = " select insertaPagoRegalia("+this.oPersHosp.getFolioPers()+",'"+
                        this.dfin+"','"+this.dinicio+"',"+this.nmontototal+",'"+
                        this.ssituacion+"','"+formatoDelTexto.format(this.dautoriz)+"')";
                System.out.println(sQuery);
                /*Si oAD es nulo, tiene que crearlo y conectarlo, de otro modo
                supone que ya viene conectado*/
                if (getAD() == null){
                    setAD(new AccesoDatos());
                    getAD().conectar();
                    nRet = getAD().ejecutarConsulta(sQuery);
                    getAD().desconectar();
                    setAD(null);
                }
                else{
                    nRet = getAD().ejecutarConsulta(sQuery);
                }
                msj=""+nRet.get(0);
                msj= msj.substring(1, msj.length()-3);
            
            }
            return Integer.parseInt(msj);
        }

    //=============== SET & GET ===============//
    public int getIdPagoRegalias() {
        return nIdPagoRegalias;
    }

    public void setIdPagoRegalias(int idPagoRegalias) {
        this.nIdPagoRegalias = idPagoRegalias;
    }
    
    public PersonalHospitalario getPersonalHospitalario() {
        return oPersHosp;
    }
    public void setPersonalHospitalario(PersonalHospitalario persHosp) {
        this.oPersHosp=persHosp;
    }

    public Date getFechaFin() {
        return dfin;
    }
    public void setFechaFin(Date dfin) {
        this.dfin = dfin;
    }

    public Date getFechaInicio() {
        return dinicio;
    }
    public void setFechaInicio(Date dinicio) {
        this.dinicio = dinicio;
    }

    public float getMontoTotal() {
        return nmontototal;
    }
    public void setMontoTotal(float nmontototal) {
        this.nmontototal = nmontototal;
    }

    public String getSituacion() {
        return ssituacion;
    }
    public void setSituacion(String ssituacion) {
        this.ssituacion = ssituacion;
    }

    public Date getFechaPago() {
        return dpago;
    }
    public void setFechaPago(Date dpago) {
        this.dpago = dpago;
    }

    public Date getFechaAutorizacion() {
        return dautoriz;
    }

    public void setFechaAutorizacion(Date dautoriz) {
        this.dautoriz = dautoriz;
    }
    
    public AccesoDatos getAD() {
        return oAD;
    }
    public void setAD(AccesoDatos oAD) {
        this.oAD = oAD;
    }
    
    
}
