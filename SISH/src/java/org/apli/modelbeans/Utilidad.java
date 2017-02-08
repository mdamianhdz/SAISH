/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.apli.modelbeans;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Vector;
import org.apli.AD.AccesoDatos;

/**
 *
 * @author HumbertMarin
 */
public class Utilidad implements Serializable{
    
    private String sVariable;
    private String sDescripcion;
    private String sValor;
    private String sTipo;
    
    private String sMsjQuery;   
    
    protected AccesoDatos oAD = null;
    
    public Utilidad(){
        
    }
    
    /** Autoriza la salida con credito en el precierre
    */
    public boolean buscaCveAutSalidadCredito()throws Exception{
    
    boolean encontrado=false;
    Utilidad arrP[] = null, oP=null;
    Vector rst = null;
    Vector<Utilidad> vObj = null;
    String sQuery = "";
    int i=0, nTam = 0;
        sQuery = "select * from validaAutSalidaConCredito('"+this.sVariable+"',md5('"+this.sValor+"'))";
        System.out.println(sQuery);
        if (getAD() == null){
            setAD(new AccesoDatos());
            getAD().conectar();
            rst = getAD().ejecutarConsulta(sQuery);
            getAD().desconectar();
            setAD(null);
            
        }
        else{
            rst = getAD().ejecutarConsulta(sQuery);
        }
        
        sMsjQuery=""+rst.get(0);
        if(sMsjQuery.indexOf("ERROR")>0){
            encontrado=false;
        }else{
            encontrado=true;
            if (rst != null) {
                vObj = new Vector<Utilidad>();
                for (i = 0; i < rst.size(); i++) {
                    oP = new Utilidad();
                    Vector vRowTemp = (Vector)rst.elementAt(i);
                    /* variable, descripcion, valor, tipo */
                    oP.setVariable((String) vRowTemp.elementAt(0));
                    oP.setDescripcion((String) vRowTemp.elementAt(1));
                    oP.setValor((String) vRowTemp.elementAt(2));
                    oP.setTipo((String) vRowTemp.elementAt(3));
                }
                nTam = vObj.size();
                arrP = new Utilidad[nTam];
                for (i=0; i<nTam; i++){
                    arrP[i] = vObj.elementAt(i);
                }
            }
        }
        return encontrado;
    }
    
    public String getMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String hashtext = number.toString(16);
            // Now we need to zero pad it if you actually want the full 32 chars.
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }
        catch (Exception e) {
            return "Exception MD5";
        }
    }
    
        public String buscaMensajeTicket() throws Exception {
        Vector rst = null;
        String sQuery = "";
        this.setVariable("MsjTicketFin");
        sQuery = "SELECT * FROM buscarMensajeTicket('" + this.sVariable + "')";
        System.out.println(sQuery);
        if (getAD() == null) {
            setAD(new AccesoDatos());
            getAD().conectar();
            rst = getAD().ejecutarConsulta(sQuery);
            getAD().desconectar();
            setAD(null);
        }
        if (rst != null && rst.size() == 1) {
            try {
                Vector vRowTemp = (Vector) rst.elementAt(0);
                this.setDescripcion((String) vRowTemp.elementAt(1));
                this.setValor((String) vRowTemp.elementAt(2));
                this.setTipo("mensaje");
            } catch (Exception e) {
                this.setValor("              --- SISH ---              ");
            }
        }
        return this.getValor();
    }

    public String getVariable() {
        return sVariable;
    }
    public void setVariable(String sVariable) {
        this.sVariable = sVariable;
    }

    public String getDescripcion() {
        return sDescripcion;
    }
    public void setDescripcion(String sDescripcion) {
        this.sDescripcion = sDescripcion;
    }

    public String getValor() {
        return sValor;
    }
    public void setValor(String sValor) {
        this.sValor = sValor;
    }

    public String getTipo() {
        return sTipo;
    }
    public void setTipo(String sTipo) {
        this.sTipo = sTipo;
    }

    public String getMsjQuery() {
        return sMsjQuery;
    }

    public void setMsjQuery(String sMsjQuery) {
        this.sMsjQuery = sMsjQuery;
    }
   
    public AccesoDatos getAD() {
        return oAD;
    }
    public void setAD(AccesoDatos oAD) {
        this.oAD = oAD;
    }
        
}