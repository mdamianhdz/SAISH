/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apli.modelbeans;

import java.io.Serializable;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import org.apli.AD.AccesoDatos;
import org.primefaces.model.DualListModel;

/**
 *
 * @author Lily_LnBnd
 */
public class PerfilUsuario implements Serializable{
    
    private List<Perfil> listPerfil = new ArrayList<Perfil>();
    private Usuario oUsu;
    protected AccesoDatos oAD = null;
    
    public PerfilUsuario(){}
    
    public PerfilUsuario(AccesoDatos poAD){
        oAD = poAD;
    }
    
    public PerfilUsuario[] buscaTodos() throws Exception {
        PerfilUsuario arrRet[] = null, oPU=null;
        Vector rst = null;
        Vector<PerfilUsuario> vObj = null;
        String sQuery = "";
        int i=0, nTam = 0;
        Usuario oUsu;
        Perfil arrPer[], oPer;
        List<Perfil> lista;

        sQuery = "select * from usuario order by cveusuario";      
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
            vObj = new Vector<PerfilUsuario>();
            for (i = 0; i < rst.size(); i++) {
                oPU = new PerfilUsuario();
                Vector vRowTemp = (Vector)rst.elementAt(i);
                
                oUsu=new Usuario();
                oUsu.setUsuario((String) vRowTemp.elementAt(0));         
                oUsu.setContraseña((String) vRowTemp.elementAt(1));
                oPU.setUsuario(oUsu);
                oPer=new Perfil();
                arrPer=oPer.buscaTarget((String) vRowTemp.elementAt(0));
                lista=new ArrayList();
                lista.addAll(Arrays.asList(arrPer));
                if (lista.isEmpty()){
                    oPer.setPerfil("vacio");
                    lista.add(oPer);
                }
                    
                oPU.setListaPerfiles(lista);
                vObj.add(oPU);
            }
            nTam = vObj.size();
            arrRet = new PerfilUsuario[nTam];

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
                rst=getAD().createArray(lista, "pu");
		getAD().desconectar();
                setAD(null);
            }  
	}
        return rst;
    }
    
    public String eliminaRelaciones(String usuario)throws Exception{
        Vector rst=null;
        String sQuery="";
        if (usuario.equals("")){
            throw new Exception("FuncionPerfil.actualizaRelaciones: error de programación, faltan datos");
        }else{
            sQuery="select * from eliminaPU('"+usuario+"')";
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

    public Usuario getUsuario() {
        return oUsu;
    }

    public void setUsuario(Usuario oUsu) {
        this.oUsu = oUsu;
    }
    
    public List<Perfil> getListaPerfiles() {
        return listPerfil;
    }

    public void setListaPerfiles(List<Perfil> lista) {
        this.listPerfil = lista;
    }
    
}