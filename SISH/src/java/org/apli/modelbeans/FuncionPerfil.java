/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apli.modelbeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import org.apli.AD.AccesoDatos;

/**
 *
 * @author Lily_LnBnd
 */
public class FuncionPerfil implements Serializable{
    
    private List<Funcion> listFnc = new ArrayList<Funcion>();
    private Perfil oPerfil;
    protected AccesoDatos oAD = null;
    
    public FuncionPerfil(){}
    
    public FuncionPerfil(AccesoDatos poAD){
        oAD = poAD;
    }
    
    public FuncionPerfil[] buscaTodos() throws Exception {
        FuncionPerfil arrRet[] = null, oFP=null;
        Vector rst = null;
        Vector<FuncionPerfil> vObj = null;
        String sQuery = "";
        int i=0, nTam = 0;
        Perfil oPer;
        Funcion arrFun[], oFun;
        List<Funcion> lista;

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
            vObj = new Vector<FuncionPerfil>();
            for (i = 0; i < rst.size(); i++) {
                oFP = new FuncionPerfil();
                Vector vRowTemp = (Vector)rst.elementAt(i);
                oPer=new Perfil();
                oPer.setPerfil((String) vRowTemp.elementAt(0));
                oPer.setDescripcion((String) vRowTemp.elementAt(1));
                oFP.setPerfil(oPer);
                oFun=new Funcion();
                arrFun=oFun.buscaTarget((String) vRowTemp.elementAt(0));
                lista=new ArrayList();
                lista.addAll(Arrays.asList(arrFun));
                if (lista.isEmpty()){
                    oFun.setFuncion("vacio");
                    lista.add(oFun);
                }
                    
                oFP.setListaFunciones(lista);
                vObj.add(oFP);
            }
            nTam = vObj.size();
            arrRet = new FuncionPerfil[nTam];

            for (i=0; i<nTam; i++){
                arrRet[i] = vObj.elementAt(i);
            }
        }
        return arrRet;
    }
    
    public String actualizaRelaciones(String[][] lista) throws Exception{
        String rst = "";
        
        if (lista == null) {
            throw new Exception("FuncionPerfil.actualizaRelaciones: error de programación, faltan datos");
	}else {
            if (getAD() == null){
                setAD(new AccesoDatos());
		getAD().conectar();
                rst=getAD().createArray(lista, "pf");
		getAD().desconectar();
                setAD(null);
            }  
	}
        return rst;
    }
    
    public String eliminaRelaciones(String perfil)throws Exception{
        Vector rst=null;
        String sQuery="";
        if (perfil.equals("")){
            throw new Exception("FuncionPerfil.actualizaRelaciones: error de programación, faltan datos");
        }else{
            sQuery="select * from eliminaPF('"+perfil+"')";
            System.out.println(sQuery);
            if (getAD() == null){
                setAD(new AccesoDatos());
		getAD().conectar();
		rst = getAD().ejecutarConsulta(sQuery);
		getAD().desconectar();
                setAD(null);
            }  
	}
        return ""+rst.get(0);
    }
    
    public AccesoDatos getAD() {
        return oAD;
    }

    public void setAD(AccesoDatos oAD) {
        this.oAD = oAD;
    }

    public Perfil getPerfil() {
        return oPerfil;
    }

    public void setPerfil(Perfil oPerfil) {
        this.oPerfil = oPerfil;
    }
    
    public List<Funcion> getListaFunciones() {
        return listFnc;
    }

    public void setListaFunciones(List<Funcion> lista) {
        this.listFnc = lista;
    }
    
}