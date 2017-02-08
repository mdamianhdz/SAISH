/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.apli.modelbeans;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import org.apli.AD.AccesoDatos;

/**
 *
 * @author HumbertMarin
 */
public class DetalleRegalias {
    
    private PagoRegalia oPagoRegalias;
    private ServicioPrestado oSP;
    private ConceptoEgreso oConcepEgr;
    private float nMontoCalculado;
    /**
      * Variable para el acceso a datos
      */
    protected AccesoDatos oAD = null;
        
    
    /**
         * Agrega una nueva cita medica a la base de datos
         * Regresa el número de registros afectados (insertados)
         **/
        public String insertar() throws Exception{
            Vector nRet = null;
            String sQuery = "";
            if ( this.oPagoRegalias.getIdPagoRegalias()==0 || this.oSP.getIdFolio().equals("") || this.oConcepEgr.getCveConcepEgr()==0  ){
                throw new Exception("Detalle Regalias.insertar: error de programación, faltan datos");
            }
            else {
                // select insertaCitaServicio(paciente.folio,areaservicio.cve,fecha,obs, tipo, duracion);
                SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                //INSERT INTO detalleregalias( nidpagoregalias, nidfolio, ncveconcepegr, nmontocalculado) VALUES (?, ?, ?, ?);

                sQuery = " select insertaDetalleRegalias("+this.oPagoRegalias.getIdPagoRegalias()+",'"+this.oSP.getIdFolio()+"',"+this.oConcepEgr.getCveConcepEgr()+","+this.nMontoCalculado+")";
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
            }
            return ""+nRet.get(0);
        }

    //=============== SET & GET ===============//
    
    public PagoRegalia getPagoRegalias() {
        return oPagoRegalias;
    }
    public void setPagoRegalias(PagoRegalia oPagoRegalias) {
        this.oPagoRegalias = oPagoRegalias;
    }
    
    public ServicioPrestado getServicioPrestado() {
        return oSP;
    }
    public void setServicioPrestado(ServicioPrestado oSP) {
        this.oSP = oSP;
    }

    public ConceptoEgreso getConceptoEgreso() {
        return oConcepEgr;
    }
    public void setConceptoEgreso(ConceptoEgreso oConcepEgr) {
        this.oConcepEgr = oConcepEgr;
    }

    public float getMontoCalculado() {
        return nMontoCalculado;
    }
    public void setMontoCalculado(float nMontoCalculado) {
        this.nMontoCalculado = nMontoCalculado;
    }
    
    public AccesoDatos getAD() {
        return oAD;
    }
    public void setAD(AccesoDatos oAD) {
        this.oAD = oAD;
    }
     
}