/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apli.modelbeans;

import java.io.Serializable;
import java.util.Vector;
import org.apli.AD.AccesoDatos;

/**
 *
 * @author Lily_LnBnd
 */
public class Perfil implements Serializable {
    private String sPerfil;
    private String sDescripcion;
    private AccesoDatos oAD;
    
    public Perfil(){}
    
    public Perfil(AccesoDatos poAD){
        oAD = poAD;
    }

    public Perfil[] buscaTodos() throws Exception {
        Perfil arrRet[] = null, oP=null;
        Vector rst = null;
        Vector<Perfil> vObj = null;
        String sQuery = "";
        int i=0, nTam = 0;

        sQuery = "select * from perfil order by perfil";      
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
            vObj = new Vector<Perfil>();
            for (i = 0; i < rst.size(); i++) {
                oP = new Perfil();
                Vector vRowTemp = (Vector)rst.elementAt(i);
                oP.setPerfil((String)vRowTemp.elementAt(0));
                oP.setDescripcion((String) vRowTemp.elementAt(1));
                vObj.add(oP);
            }
            nTam = vObj.size();
            arrRet = new Perfil[nTam];

            for (i=0; i<nTam; i++){
                arrRet[i] = vObj.elementAt(i);
            }
        }
        return arrRet;
    }
    
    public String opPerfil(String op) throws Exception{
        Vector rst = null;
        String sQuery = "";
        
        if (this.sPerfil.equals("") || this.sDescripcion.equals("")) {
            throw new Exception("Perfil.opPerfil: error de programación, faltan datos");
	}else {
            sQuery="select * from opPerfil('"+this.sPerfil+"','"+this.sDescripcion+"','"+op+"')";
            if (getAD() == null){
                setAD(new AccesoDatos());
		getAD().conectar();
		rst = getAD().ejecutarConsulta(sQuery);
		getAD().desconectar();
                setAD(null);
            }  
	}
        return " "+rst.get(0);
    }
    
    public Perfil buscaPerfil(String perfil)throws Exception{
        Perfil oP=new Perfil();
        Vector rst = null;
        String sQuery = "";

        if (perfil.equals("")) {
            throw new Exception("Perfil.buscaPerfil: error de programación, faltan datos");
        }else{
            sQuery = "select * from perfil where perfil='"+perfil+"'";
            if (this.getAD() == null){
                this.setAD(new AccesoDatos());
                this.getAD().conectar();
                rst = this.getAD().ejecutarConsulta(sQuery);
                this.getAD().desconectar();
                setAD(null);
            }else{
                rst = this.getAD().ejecutarConsulta(sQuery);
            }

            if (rst != null && rst.size() == 1) {
                Vector vRowTemp = (Vector)rst.elementAt(0);
                oP.setPerfil((String) vRowTemp.elementAt(0));
                oP.setDescripcion((String) vRowTemp.elementAt(1));
            }
        }
        return oP;
    }
    
    public Perfil[] buscaTarget(String usuario) throws Exception {
        Perfil arrRet[] = null, oPer=null;
        Vector rst = null;
        Vector<Perfil> vObj = null;
        String sQuery = "";
        int i=0, nTam = 0;
        
        if (usuario.equals("")) {
            throw new Exception("Perfil.buscaTarget: error de programación, faltan datos");
        }else{
            sQuery="select t2.* from usuario_perfil t1, perfil t2 where t1.perfil=t2.perfil and usuario='"+usuario+"' order by perfil";     
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
                vObj = new Vector<Perfil>();
                for (i = 0; i < rst.size(); i++) {
                    oPer = new Perfil();
                    Vector vRowTemp = (Vector)rst.elementAt(i);
                    oPer.setPerfil((String) vRowTemp.elementAt(0));
                    oPer.setDescripcion((String) vRowTemp.elementAt(1));
                    vObj.add(oPer);
                }
                nTam = vObj.size();
                arrRet = new Perfil[nTam];

                for (i=0; i<nTam; i++){
                    arrRet[i] = vObj.elementAt(i);
                }
            }
        }
        return arrRet;
    }
    
    public Perfil[] buscaSource(String usuario) throws Exception {
        Perfil arrRet[] = null, oPer=null;
        Vector rst = null;
        Vector<Perfil> vObj = null;
        String sQuery = "";
        int i=0, nTam = 0;
        
        if (usuario.equals("")) {
            throw new Exception("Perfil.buscaSource: error de programación, faltan datos");
        }else{
            sQuery = "select * from perfil where perfil not in(select perfil from usuario_perfil where usuario='"+usuario+"')";      
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
                vObj = new Vector<Perfil>();
                for (i = 0; i < rst.size(); i++) {
                    oPer = new Perfil();
                    Vector vRowTemp = (Vector)rst.elementAt(i);
                    oPer.setPerfil((String)vRowTemp.elementAt(0));
                    oPer.setDescripcion((String) vRowTemp.elementAt(1));
                    vObj.add(oPer);
                }
                nTam = vObj.size();
                arrRet = new Perfil[nTam];

                for (i=0; i<nTam; i++){
                    arrRet[i] = vObj.elementAt(i);
                }
            }
        }
        return arrRet;
    }
    
    public String getPerfil() {
        return sPerfil;
    }

    public void setPerfil(String sPerfil) {
        this.sPerfil = sPerfil;
    }

    public String getDescripcion() {
        return sDescripcion;
    }

    public void setDescripcion(String sDescripcion) {
        this.sDescripcion = sDescripcion;
    }
    
    public AccesoDatos getAD() {
        return oAD;
    }

    public void setAD(AccesoDatos oAD) {
        this.oAD = oAD;
    }
    
}
