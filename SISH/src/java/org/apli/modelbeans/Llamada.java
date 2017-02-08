/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apli.modelbeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import org.apli.AD.AccesoDatos;

/**
 *
 * @author Lily_LnBnd
 */
public class Llamada implements Serializable {
    private String sRecibioLlamada;
    private String sRealizoLlamada;
    private char cTipo;
    private Date dFecLlamada;
    private String sResultado;
    private AccesoDatos oAD;

    public List<Llamada> buscaLlamadas(int numContraRecibo) throws Exception{
        List<Llamada> listRet=null;
        Llamada oLlam;
        
        Vector rst = null;
        String sQuery = "";

        sQuery="select * from buscaLlamadas("+numContraRecibo+")";     
        if (getAD() == null){
            setAD(new AccesoDatos());
            getAD().conectar();
            rst = getAD().ejecutarConsulta(sQuery);
            getAD().desconectar();
            setAD(null);
        }else{
            rst = getAD().ejecutarConsulta(sQuery);
        }
               
        if (rst != null) {
            listRet = new ArrayList();
            for (int i = 0; i < rst.size(); i++) {
                oLlam = new Llamada();
                Vector vRowTemp = (Vector)rst.elementAt(i);
                oLlam.setFecLlamada((Date) vRowTemp.elementAt(1));
                oLlam.setRecibioLlamada((String) vRowTemp.elementAt(2));
                oLlam.setResultado((String) vRowTemp.elementAt(3));
                oLlam.setRealizoLlamada((String) vRowTemp.elementAt(4));
                oLlam.setTipo(((String) vRowTemp.elementAt(5)).charAt(0));
                listRet.add(oLlam);
            }
        }
        return listRet;
    } 
    
    public String getRecibioLlamada() {
        return sRecibioLlamada;
    }

    public void setRecibioLlamada(String sRecibioLlamada) {
        this.sRecibioLlamada = sRecibioLlamada;
    }

    public Date getFecLlamada() {
        return dFecLlamada;
    }

    public void setFecLlamada(Date dFecLlamada) {
        this.dFecLlamada = dFecLlamada;
    }

    public String getResultado() {
        return sResultado;
    }

    public void setResultado(String sResultado) {
        this.sResultado = sResultado;
    }

    public AccesoDatos getAD() {
        return oAD;
    }

    public void setAD(AccesoDatos oAD) {
        this.oAD = oAD;
    }

    public String getRealizoLlamada() {
        return sRealizoLlamada;
    }

    public void setRealizoLlamada(String sRealizoLlamada) {
        this.sRealizoLlamada = sRealizoLlamada;
    }

    public char getTipo() {
        return cTipo;
    }

    public void setTipo(char cTipo) {
        this.cTipo = cTipo;
    }
    
    
}


