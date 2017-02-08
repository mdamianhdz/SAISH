/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apli.modelbeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import org.apli.AD.AccesoDatos;

/**
 *
 * @author Usuario5
 */
public class Municipio implements Serializable {

	/**
	 * Clave (acr�nimo) del estado
	 */
	private String sCve;
	/**
	 * Descripci�n del estado
	 */
	private String sDescrip;
        private Estado oEdo;
        protected AccesoDatos oAD = null;
	public Municipio(){

	}



    public Estado getEdo() {
        return oEdo;
    }

    public void setEdo(Estado oEdo) {
        this.oEdo = oEdo;
    }
        public AccesoDatos getAD() {
            return oAD;
        }
        public void setAD(AccesoDatos oAD) {
            this.oAD = oAD;
        }
    public List<Municipio> getMunicipios() throws Exception{
            Municipio oP=null;
            Vector rst = null;
            Vector<Municipio> vObj = null;
            List<Municipio> lista=new ArrayList();
            String sQuery = "";
            int i=0;
            sQuery = "select * from buscamunicipios()";
            if (getAD() == null){
                setAD(new AccesoDatos());
                getAD().conectar();
                rst = getAD().ejecutarConsulta(sQuery);
                getAD().desconectar();
                setAD(null);
            }
            else{
                rst = getAD().ejecutarConsulta(sQuery);
                getAD().desconectar();
                setAD(null);
            }
            if (rst != null |rst.size()>0) {
                int x=0;
                vObj = new Vector<Municipio>();
                Estado estado;
                for (i = 0; i < rst.size(); i++) {
                    oP = new Municipio();
                    estado=new Estado();
                    Vector vRowTemp = (Vector)rst.elementAt(i); 
                    oP.setCve((String) vRowTemp.elementAt(1));
                    oP.setDescrip((String) vRowTemp.elementAt(2));
                    oP.setEdo(estado.buscaEstado((String) vRowTemp.elementAt(0)));
                    lista.add(oP);
                }
            }
            return lista;
    }
    
    public String guardaMunicipio(Municipio oMun) throws Exception{
        Vector rst = null;
        String sQuery = "";
        
        if (oMun==null){
            throw new Exception("Municipio.guarda: error de programación, faltan datos");
	}else {
             sQuery="SELECT * from insertamunicipio('"+oMun.getEdo().getCve()+"'::character varying,'"+oMun.getCve()+"'::character varying,'"+oMun.getDescrip()+"'::character varying);";
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
        
        
     public String modificaMunicipio(Municipio oMun) throws Exception{
        Vector rst = null;
        String sQuery = "";
        
        if (oMun==null){
            throw new Exception("Municipio.modifica: error de programación, faltan datos");
	}else {
            sQuery="select * from modificamunicipio('"+oMun.getEdo().getCve()+"'::character varying,'"+oMun.getCve()+"'::character varying,'"+oMun.getDescrip()+"'::character varying);";
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
     
    public String eliminaMunicipio(Municipio oMun) throws Exception{
        Vector rst = null;
        String sQuery = "";
        
        if (oMun==null){
            throw new Exception("Municipio.elimina: error de programación, faltan datos");
	}else {
            sQuery="select * from eliminamunicipio('"+oMun.getEdo().getCve()+"'::character varying,'"+oMun.getCve()+"'::character varying);";
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
    
    public List<Municipio> getMunicipiosEdo(String scveedo) throws Exception{
        Municipio oP=null;
        Vector rst = null;
        Vector<Municipio> vObj = null;
        List<Municipio> lista=new ArrayList();
        String sQuery = "";
        int i=0;
        sQuery = "select * from buscamunicipiosedo('"+scveedo+"'::character varying)";
            if (getAD() == null){
                setAD(new AccesoDatos());
                getAD().conectar();
                rst = getAD().ejecutarConsulta(sQuery);
                getAD().desconectar();
                setAD(null);
            }
            else{
                rst = getAD().ejecutarConsulta(sQuery);
                getAD().desconectar();
                setAD(null);
            }
            if (rst != null |rst.size()>0) {
                int x=0;
                vObj = new Vector<Municipio>();
                Estado estado;
                for (i = 0; i < rst.size(); i++) {
                    oP = new Municipio();
                    estado=new Estado();
                    Vector vRowTemp = (Vector)rst.elementAt(i);
                    oP.setEdo(estado);
                    oP.getEdo().setCve((String)vRowTemp.elementAt(0));
                    oP.setCve((String) vRowTemp.elementAt(1));
                    oP.setDescrip((String) vRowTemp.elementAt(2));
                    lista.add(oP);
                }
            }
        return lista; 
    }

    /**
     * @return the sCve
     */
    public String getCve() {
        return sCve;
    }

    /**
     * @param sCve the sCve to set
     */
    public void setCve(String sCve) {
        this.sCve = sCve;
    }

    /**
     * @return the sDescrip
     */
    public String getDescrip() {
        return sDescrip;
    }

    /**
     * @param sDescrip the sDescrip to set
     */
    public void setDescrip(String sDescrip) {
        this.sDescrip = sDescrip;
    }
    
}
