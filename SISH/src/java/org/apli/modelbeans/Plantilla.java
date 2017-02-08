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
 * @author JRuiz
 */
public class Plantilla implements Serializable {
    
    private String sDescripcion;
    private String sVariable;
    private String sValor;
    private String sTipo;
    protected AccesoDatos oAD = null;
    
    public List<Plantilla> todosElementosPlantilla()throws Exception{
    Plantilla arrP[] = null, oP=null;
    Vector rst = null;
    List<Plantilla> listPlantll=new ArrayList();
    
    String sQuery = "";
    int i=0;

        sQuery = "select * from utilidad where tipo='Plantilla'";

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
        if (rst != null) {
            for (i = 0; i < rst.size(); i++) {
                oP = new Plantilla();
                Vector vRowTemp=(Vector) rst.elementAt(i);
                
                oP.setVariable((String) vRowTemp.elementAt(0));
                oP.setDescripcion((String) vRowTemp.elementAt(1));               
                oP.setValor((String) vRowTemp.elementAt(2));
                oP.setTipo((String) vRowTemp.elementAt(3));
                listPlantll.add(oP);
            }
        }
        return listPlantll;
    }
    
    public String actualizaPlantilla() throws Exception{
        
    Vector rst = null;
    String sQuery = "";

        if (this.sDescripcion.equals("") || this.sVariable.equals("") || this.sValor.equals("")) {
            throw new Exception("Plantilla.actualizaPlantilla: error de programación, faltan datos");
	}else {
            
           sQuery="update utilidad set descripcion='"+this.sDescripcion+"', valor='"+this.sValor+"' where variable='"+this.sVariable+"'";

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
    
    public String actualizaLogo() throws Exception{
        
    Vector rst = null;
    String sQuery = "";

        if (this.sValor.equals("")) {
            throw new Exception("Plantilla.actualizaPlantilla: error de programación, faltan datos");
	}else {
            
           sQuery="update utilidad set valor='"+this.sValor+"' where variable='Logo'";

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
    
    public String actualizaTema() throws Exception{
        
    Vector rst = null;
    String sQuery = "";

        if (this.sValor.equals("")) {
            throw new Exception("Plantilla.actualizaTema: error de programación, faltan datos");
	}else {
            
           sQuery="update utilidad set valor='"+this.sValor+"' where variable='Tema'";

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
    
    public AccesoDatos getAD() {
        return oAD;
    }

    public void setAD(AccesoDatos oAD) {
        this.oAD = oAD;
    }

    public String getDescripcion() {
        return sDescripcion;
    }

    public void setDescripcion(String sDescripcion) {
        this.sDescripcion = sDescripcion;
    }

    public String getVariable() {
        return sVariable;
    }

    public void setVariable(String sVariable) {
        this.sVariable = sVariable;
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

    
    
    
    
    
}
