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
 * Clase que consulta un usuario registrado y las funciones que puede acceder para su menú operacional
 * Autor: Jesús Vázquez Ruiz
 * Fecha: Febrero 2014
 */
public class Usuario implements Serializable {
    
    private String sUsuario;
    private String sContraseña;
    private String sMsjQuery;   
    private String sMenu;
    private String sSubMenu;
    private String sFuncion;
    private String sUrl;
    private PersonalHospitalario oPH;
    
    private List<Usuario> listMenu;
    protected AccesoDatos oAD = null;
    
    public Usuario(){}
    
    public Usuario(AccesoDatos poAD){
        oAD = poAD;
    }
    
    /**
    * Busca un usuario específico, si este es escontrado regresa true, como también llena un objeto de tipo 
    * usuario con las funciones que tendrá visibles en el menú operacional, si no es encontrado, solo 
    * regresará false
    */
    public boolean buscaUsuario()throws Exception{
    
    boolean encontrado=false;
    Usuario arrP[] = null, oP=null;
    Vector rst = null;
    Vector<Usuario> vObj = null;
    listMenu=new ArrayList<Usuario>();
    
    String sQuery = "";
    int i=0, nTam = 0;

        sQuery = "select * from validausuario('"+this.sUsuario+"','"+this.sContraseña+"')";

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
        if (rst == null || rst.isEmpty())
            sMsjQuery = "No tiene permisos de ingreso";
        else{
            sMsjQuery=""+rst.get(0);
            if(sMsjQuery.indexOf("ERROR")>0){
                encontrado=false;
            }else{
                encontrado=true;
                vObj = new Vector<Usuario>();
                for (i = 0; i < rst.size(); i++) {
                    oP = new Usuario();
                    Vector vRowTemp = (Vector)rst.elementAt(i);
                    oP.setMenu((String) vRowTemp.elementAt(0));
                    oP.setSubMenu((String) vRowTemp.elementAt(1));
                    oP.setFuncion((String) vRowTemp.elementAt(2));               
                    oP.setUrl((String) vRowTemp.elementAt(3));
                    listMenu.add(oP);
                }
                nTam = vObj.size();
                arrP = new Usuario[nTam];

                for (i=0; i<nTam; i++){
                    arrP[i] = vObj.elementAt(i);
                }
            }
        }
        return encontrado;
    }
    
    public Usuario[] buscaTodos() throws Exception{
        Usuario arrRet[] = null, oUsu=null;
        Vector rst = null;
        Vector<Usuario> vObj = null;
        String sQuery = "";
        int i=0, nTam = 0;
        
        sQuery = "select * from buscaTodosUsuarios();";      
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
            vObj = new Vector<Usuario>();
            for (i = 0; i < rst.size(); i++) {
                oUsu = new Usuario();
                Vector vRowTemp = (Vector)rst.elementAt(i);
                oUsu.setUsuario((String)vRowTemp.elementAt(0));
                oUsu.setContraseña((String)vRowTemp.elementAt(1));
                oUsu.setPH(new PersonalHospitalario());
                oUsu.getPH().setFolioPers(((Double)vRowTemp.elementAt(2)).intValue());
                oUsu.getPH().setNombre((String)vRowTemp.elementAt(3));
                oUsu.getPH().setApellidoPaterno((String)vRowTemp.elementAt(4));
                oUsu.getPH().setApellidoMaterno((String)vRowTemp.elementAt(5));
                oUsu.getPH().setPuesto(new Puesto());
                oUsu.getPH().getPuesto().setDescrip((String)vRowTemp.elementAt(6));
                vObj.add(oUsu);
            }
            nTam = vObj.size();
            arrRet = new Usuario[nTam];

            for (i=0; i<nTam; i++){
                arrRet[i] = vObj.elementAt(i);
            }
        }
        return arrRet;
    }
    
    public String opUsuario(String op) throws Exception{
        Vector rst = null;
        String sQuery = "", sRet="";
        
        if (this.sUsuario.equals("") ||  this.sContraseña.equals("")){
            throw new Exception("Funcion.modifica: error de programación, faltan datos");
	}else {
            sQuery="select * from opUsuario('"+this.sUsuario+"','"+this.sContraseña+"')";
            System.out.println(sQuery);
            if (getAD() == null){
                setAD(new AccesoDatos());
		getAD().conectar();
		rst = getAD().ejecutarConsulta(sQuery);
		getAD().desconectar();
                setAD(null);
            }
            if (rst != null){
                sRet = (String)rst.get(0);
            }
	}
        return sRet;
    }
    
    public String guardaUsuario(Usuario oUsu) throws Exception{
        Vector rst = null;
        String sQuery = "", sRet="";
        
        if (oUsu==null){
            throw new Exception("Usuario.guarda: error de programación, faltan datos");
	}else {
            sQuery="select * from guardaUsuario('"+oUsu.getUsuario()+"','"+oUsu.getContraseña()+"',"+oUsu.getPH().getFolioPers()+")";
            System.out.println(sQuery);
            if (getAD() == null){
                setAD(new AccesoDatos());
		getAD().conectar();
		rst = getAD().ejecutarConsulta(sQuery);
		getAD().desconectar();
                setAD(null);
            }
            if (rst != null){
                sRet = rst.get(0).toString().substring(1, rst.get(0).toString().length() - 1);
            }
	}
        return sRet;
    }
    
    public String modificaUsuario(Usuario oUsu) throws Exception{
        Vector rst = null;
        String sQuery = "", sRet ="";
        
        if (oUsu==null){
            throw new Exception("Usuario.guarda: error de programación, faltan datos");
	}else {
            sQuery="select * from modificaUsuario('"+oUsu.getUsuario()+"','"+oUsu.getContraseña()+"')";
            System.out.println(sQuery);
            if (getAD() == null){
                setAD(new AccesoDatos());
		getAD().conectar();
		rst = getAD().ejecutarConsulta(sQuery);
		getAD().desconectar();
                setAD(null);
            }
            if (rst!=null){
                sRet = rst.get(0).toString().substring(1, rst.get(0).toString().length() - 1);
            }
	}
        return sRet;
    }

    public String getUsuario() {
        return sUsuario;
    }

    public void setUsuario(String usuario) {
        this.sUsuario = usuario;
    }

    public String getContraseña() {
        return sContraseña;
    }

    public void setContraseña(String contraseña) {
        this.sContraseña = contraseña;
    }
    
    public AccesoDatos getAD() {
        return oAD;
    }

    public void setAD(AccesoDatos oAD) {
        this.oAD = oAD;
    }

    public List getListMenu() {
        return listMenu;
    }

    public void setListMenu(List listOpciones) {
        this.listMenu = listOpciones;
    }

    public String getMsjQuery() {
        return sMsjQuery;
    }

    public void setMsjQuery(String sMsjQuery) {
        this.sMsjQuery = sMsjQuery;
    }
   
    public String getMenu() {
        return sMenu;
    }

    public void setMenu(String sMenu) {
        this.sMenu = sMenu;
    }

    public String getsSubMenu() {
        return sSubMenu;
    }

    public void setSubMenu(String sSubMenu) {
        this.sSubMenu = sSubMenu;
    }

    public String getFuncion() {
        return sFuncion;
    }

    public void setFuncion(String sFuncion) {
        this.sFuncion = sFuncion;
    }

    public String getUrl() {
        return sUrl;
    }

    public void setUrl(String sUrl) {
        this.sUrl = sUrl;
    }

    public PersonalHospitalario getPH() {
        return oPH;
    }

    public void setPH(PersonalHospitalario oPH) {
        this.oPH = oPH;
    }
 
}
