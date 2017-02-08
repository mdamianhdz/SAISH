package org.apli.modelbeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import org.apli.AD.AccesoDatos;



/**
 * Catálogo de estados civiles válidos
 * @author BAOZ
 * @version 1.0
 * @created 07-abr-2014 01:03:27 p.m.
 */
public class EstadoCivil implements Serializable{

	/**
	 * Clave del estado civil
	 */
	private String sCve;
	/**
	 * Descripción del estado civil
	 */
	private String sDescrip;
        private List<EstadoCivil> listEdoCivil;
        
        private AccesoDatos oAD;

	public EstadoCivil(){

	}

	public void finalize() throws Throwable {

	}

    public String getCve() {
        return sCve;
    }

    public void setCve(String sCve) {
        this.sCve = sCve;
    }

    public String getDescrip() {
        return sDescrip;
    }

    public void setDescrip(String sDescrip) {
        this.sDescrip = sDescrip;
    }
    
    public AccesoDatos getAD(){
        return oAD;
    }
    
    public void setAD(AccesoDatos oAD) {
        this.oAD = oAD;
    }
    
    public List<EstadoCivil> buscaTodos() throws Exception{
        List<EstadoCivil> listRet=null;
        EstadoCivil oEC;
        
        Vector rst = null;
        String sQuery = "";

        sQuery="select * from buscaTodosEstadoCivil()";     
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
                oEC = new EstadoCivil();
                Vector vRowTemp = (Vector)rst.elementAt(i);
                oEC.setCve((String)vRowTemp.elementAt(0));
                oEC.setDescrip((String)vRowTemp.elementAt(1));
                listRet.add(oEC);
            }
        }
        return listRet;
    }
	
    
    public String guardaEstadoCivil(EstadoCivil oEC) throws Exception{
        Vector rst = null;
        String sQuery = "";
        
        if (oEC==null){
            throw new Exception("Estado Civil.guarda: error de programación, faltan datos");
	}else {
             sQuery="SELECT * from insertaestadocivil('"+oEC.getCve()+"'::varchar,'"+oEC.getDescrip()+"'::varchar);";
            System.out.println(sQuery);
            if (getAD() == null){
                setAD(new AccesoDatos());
		getAD().conectar();
		rst = getAD().ejecutarConsulta(sQuery);
		getAD().desconectar();
                setAD(null);
            }  
	}
        return ""+rst.get(0).toString().substring(1, rst.get(0).toString().length() - 1);
    }
    
    public String modificaEstadoCivil(EstadoCivil oEC) throws Exception{
        Vector rst = null;
        String sQuery = "";
        
        if (oEC==null){
            throw new Exception("Estado Civil.modifica: error de programación, faltan datos");
	}else {
            sQuery="SELECT * from modificaestadocivil('"+oEC.getCve()+"'::varchar,'"+oEC.getDescrip()+"'::varchar);";
            System.out.println(sQuery);
            if (getAD() == null){
                setAD(new AccesoDatos());
		getAD().conectar();
		rst = getAD().ejecutarConsulta(sQuery);
		getAD().desconectar();
                setAD(null);
            }  
	}
        return ""+rst.get(0).toString().substring(1, rst.get(0).toString().length() - 1);
    }
    
    public String eliminaEstadoCivil(EstadoCivil oEC) throws Exception{
        Vector rst = null;
        String sQuery = "";
        
        if (oEC==null){
            throw new Exception("Estado Civil.elimina: error de programación, faltan datos");
	}else {
            sQuery="select * from eliminaestadocivil('"+oEC.getCve()+"'::varchar)";
            System.out.println(sQuery);
            if (getAD() == null){
                setAD(new AccesoDatos());
		getAD().conectar();
                rst = getAD().ejecutarConsulta(sQuery);
		getAD().desconectar();
                setAD(null);
            }  
	}
        return ""+rst.get(0).toString().substring(1, rst.get(0).toString().length() - 1);
    }
}