package org.apli.modelbeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import org.apli.AD.AccesoDatos;

public class Funcion implements Serializable{
    
    private String sFuncion;
    private String sDescripcion;
    private String sUrl;
    private Menu oPadre;

    private List<Funcion> listFunc=new ArrayList<Funcion>();
    protected AccesoDatos oAD = null;
    
    public Funcion(){}
    
    public Funcion(AccesoDatos poAD){
        oAD = poAD;
    }
    
    public Funcion[] buscaTodos() throws Exception {
        Funcion arrRet[] = null, oFnc=null;
        Vector rst = null;
        Vector<Funcion> vObj = null;
        String sQuery = "";
        int i=0, nTam = 0;
        Menu oMenu;

        sQuery = "select * from funcion order by funcion";      
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
            vObj = new Vector<Funcion>();
            for (i = 0; i < rst.size(); i++) {
                oFnc = new Funcion();
                Vector vRowTemp = (Vector)rst.elementAt(i);
                oFnc.setFuncion((String)vRowTemp.elementAt(0));
                oFnc.setDescripcion((String) vRowTemp.elementAt(1));
                oFnc.setUrl((String) vRowTemp.elementAt(2));
                oMenu=new Menu();
                oMenu=oMenu.buscaMenu((String) vRowTemp.elementAt(3));
                oFnc.setPadre(oMenu);
                vObj.add(oFnc);
            }
            nTam = vObj.size();
            arrRet = new Funcion[nTam];

            for (i=0; i<nTam; i++){
                arrRet[i] = vObj.elementAt(i);
            }
        }
        return arrRet;
    }
    
    public String opFuncion(String op) throws Exception{
        Vector rst = null;
        String sQuery = "";
        
        if (this.sFuncion.equals("") || this.sDescripcion.equals("") || this.sUrl.equals("") || this.oPadre.equals(null)) {
            throw new Exception("Funcion.modifica: error de programación, faltan datos");
	}else {
            sQuery="select * from opFuncion('"+this.sFuncion+"','"+this.sDescripcion+"','"+this.sUrl+"','"+this.oPadre.getMenu()+"','"+op+"')";
            System.out.println(sQuery);
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
    
    public Funcion buscaFuncion(String funcion)throws Exception{
        Funcion oF=new Funcion();
        Vector rst = null;
        String sQuery = "";
        Menu oMenu;

        if (funcion.equals("")) {
            throw new Exception("Perfil.buscaPerfil: error de programación, faltan datos");
        }else{
            sQuery = "select * from funcion where funcion='"+funcion+"'";
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
                oF.setFuncion((String) vRowTemp.elementAt(0));
                oF.setDescripcion((String) vRowTemp.elementAt(1));
                oF.setUrl((String) vRowTemp.elementAt(2));
                oMenu=new Menu();
                oMenu=oMenu.buscaMenu((String) vRowTemp.elementAt(3));
                oF.setPadre(oMenu);
            }
        }
        return oF;
    }
    
    public Funcion[] buscaTarget(String perfil) throws Exception {
        Funcion arrRet[] = null, oFnc=null;
        Vector rst = null;
        Vector<Funcion> vObj = null;
        String sQuery = "";
        int i=0, nTam = 0;
        Menu oMenu;
        
        if (perfil.equals("")) {
            throw new Exception("Funcion.buscaTarget: error de programación, faltan datos");
        }else{
            sQuery = "select t2.* from perfil_funcion t1, funcion t2 where t1.funcion=t2.funcion and perfil='"+perfil+"' order by funcion";      
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
                vObj = new Vector<Funcion>();
                for (i = 0; i < rst.size(); i++) {
                    oFnc = new Funcion();
                    Vector vRowTemp = (Vector)rst.elementAt(i);
                    oFnc.setFuncion((String)vRowTemp.elementAt(0));
                    oFnc.setDescripcion((String) vRowTemp.elementAt(1));
                    oFnc.setUrl((String) vRowTemp.elementAt(2));
                    oMenu=new Menu();
                    oMenu=oMenu.buscaMenu((String) vRowTemp.elementAt(3));
                    oFnc.setPadre(oMenu);
                    vObj.add(oFnc);
                }
                nTam = vObj.size();
                arrRet = new Funcion[nTam];

                for (i=0; i<nTam; i++){
                    arrRet[i] = vObj.elementAt(i);
                }
            }
        }
        return arrRet;
    }
    
    public Funcion[] buscaSource(String perfil) throws Exception {
        Funcion arrRet[] = null, oFnc=null;
        Vector rst = null;
        Vector<Funcion> vObj = null;
        String sQuery = "";
        int i=0, nTam = 0;
        Menu oMenu;
        
        if (perfil.equals("")) {
            throw new Exception("Funcion.buscaSource: error de programación, faltan datos");
        }else{
            sQuery = "select * from funcion where funcion not in(select funcion from perfil_funcion where perfil='"+perfil+"') order by funcion";      
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
                vObj = new Vector<Funcion>();
                for (i = 0; i < rst.size(); i++) {
                    oFnc = new Funcion();
                    Vector vRowTemp = (Vector)rst.elementAt(i);
                    oFnc.setFuncion((String)vRowTemp.elementAt(0));
                    oFnc.setDescripcion((String) vRowTemp.elementAt(1));
                    oFnc.setUrl((String) vRowTemp.elementAt(2));
                    oMenu=new Menu();
                    oMenu=oMenu.buscaMenu((String) vRowTemp.elementAt(3));
                    oFnc.setPadre(oMenu);
                    vObj.add(oFnc);
                }
                nTam = vObj.size();
                arrRet = new Funcion[nTam];

                for (i=0; i<nTam; i++){
                    arrRet[i] = vObj.elementAt(i);
                }
            }
        }
        return arrRet;
    }
    
    public AccesoDatos getAD() {
        return oAD;
    }

    public void setAD(AccesoDatos oAD) {
        this.oAD = oAD;
    }

    public String getFuncion() {
        return sFuncion;
    }

    public void setFuncion(String sFuncion) {
        this.sFuncion = sFuncion;
    }
    
    public String getDescripcion() {
        return sDescripcion;
    }

    public void setDescripcion(String sDescripcion) {
        this.sDescripcion = sDescripcion;
    }

    public String getUrl() {
        return sUrl;
    }

    public void setUrl(String sUrl) {
        this.sUrl = sUrl;
    }
    
    public Menu getPadre() {
        return oPadre;
    }

    public void setPadre(Menu oPadre) {
        this.oPadre = oPadre;
    }
}