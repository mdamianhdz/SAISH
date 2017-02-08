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
public class Utilidades implements Serializable {
    
    private String sUrlLogo;
    private String sPie;
    private int nIdMenu;
    private String sMenu;
    private String sDescripcionMenu;
    private String nPadreMenu="";
    protected AccesoDatos oAD = null;
    
    public Utilidades(){}
    
    public Utilidades(AccesoDatos AD){
        oAD = AD;
    }
    
    public List<Utilidades> todasOpcionesMenu()throws Exception{
    Utilidades arrU[] = null, oU=null;
    Vector rst = null;
    Vector<Utilidades> vObj = null;
    List<Utilidades> listPac=new ArrayList();
    
    String sQuery = "";
    int i=0, nTam = 0;

        sQuery = "select * from menu";

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
            vObj = new Vector<Utilidades>();
            for (i = 0; i < rst.size(); i++) {
                oU = new Utilidades();
                Vector vRowTemp = (Vector)rst.elementAt(i);
                
                oU.setMenu((String) vRowTemp.elementAt(0));
                oU.setDescripcionMenu((String) vRowTemp.elementAt(1));
                oU.setPadreMenu((String)  vRowTemp.elementAt(2));
                listPac.add(oU);
            }
            nTam = vObj.size();
            arrU = new Utilidades[nTam];

            for (i=0; i<nTam; i++){
                arrU[i] = vObj.elementAt(i);
            }
        }
        return listPac;
    }
    
    public String insertaElementoMenu() throws Exception{
    Vector rst = null;
    String sQuery = "";

        if (this.sMenu.equals("") || this.sDescripcionMenu.equals("") || this.nPadreMenu.equals("")) {
            throw new Exception("Utilidades.modificarElementoMenu: error de programación, faltan datos");
	}
	else {
            
            //sQuery="select * from funcionMenu ('"nombre, desc, padre, funcion"')";
            sQuery="select * from opmenu('"+this.sMenu+"','"+this.sDescripcionMenu+"','"+this.nPadreMenu+"','insert')";

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
    
    
        
    public String modificarElementoMenu() throws Exception{
        
    System.out.println("modificarElementoMenu()");
    Vector rst = null;
    String sQuery = "";

        if (this.sMenu.equals("") || this.sDescripcionMenu.equals("") || this.nPadreMenu.equals("")) {
            throw new Exception("Utilidades.modificarElementoMenu: error de programación, faltan datos");
	}else {
            
            if(nPadreMenu.equals("Menu principal")){
                nPadreMenu="0";
            }
            //sQuery="select * from funcionMenu ('"nombre, desc, padre, funcion"')";
            sQuery="select * from opMenu ('"+this.sMenu+"','"+this.sDescripcionMenu+"','"+this.nPadreMenu+"','update')";

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
    
    
        public String eliminarElementoMenu() throws Exception{
        
        Vector rst = null;
        String sQuery = "";

        if (this.sMenu.equals("")) {
            throw new Exception("Utilidades.eliminarElementoMenu: error de programación, faltan datos");
	}
	else {
            
            //sQuery="select * from funcionMenu ('"nombre, desc, padre, funcion"')";
            sQuery="select * from eliminamenu ('"+this.sMenu+"')";

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

    public String getUrlLogo() {
        return sUrlLogo;
    }

    public void setUrlLogo(String sUrlLogo) {
        this.sUrlLogo = sUrlLogo;
    }

    public String getPie() {
        return sPie;
    }

    public void setPie(String sPie) {
        this.sPie = sPie;
    }

    public int getIdMenu() {
        return nIdMenu;
    }

    public void setIdMenu(int nIdMenu) {
        this.nIdMenu = nIdMenu;
    }

    public String getMenu() {
        return sMenu;
    }

    public void setMenu(String sMenu) {
        this.sMenu = sMenu;
    }

    public String getDescripcionMenu() {
        return sDescripcionMenu;
    }

    public void setDescripcionMenu(String sDescripcion) {
        this.sDescripcionMenu = sDescripcion;
    }

    public String getPadreMenu() {
        String padre=nPadreMenu;
        if(nPadreMenu.equals("0")){
            padre="Menu";
        }
        return padre;
    }
    
    public String getPadreMenuSelect() {
        String padre=nPadreMenu;
        if(nPadreMenu.equals("0")){
            padre="Menu principal";
        }else{
            padre=nPadreMenu;
        }
        return padre;
    }
    
    public void setPadreMenuSelect(String select ) {
        nPadreMenu=select;
    }

    public void setPadreMenu(String nPadre) {
        this.nPadreMenu = nPadre;
    }
    
    
    
    
}
