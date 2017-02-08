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
 * @author Lily_LnBnd
 */
public class Menu implements Serializable {
    private String sMenu;
    private String sDescripcion;
    private String sPadre;
    private AccesoDatos oAD;
    
    public Menu(){}
    
    public Menu(AccesoDatos poAD){
        oAD = poAD;
    }

    public List<Menu> buscaTodos() throws Exception {
        Menu arrRet[] = null, oMenu=null;
        Vector rst = null;
        Vector<Menu> vObj = null;
        String sQuery = "";
        int i=0, nTam = 0;
        List<Menu> lista=new ArrayList();

        sQuery = "select * from menu order by menu";      
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
            vObj = new Vector<Menu>();
            for (i = 0; i < rst.size(); i++) {
                oMenu = new Menu();
                Vector vRowTemp = (Vector)rst.elementAt(i);
                oMenu.setMenu((String)vRowTemp.elementAt(0));
                oMenu.setDescripcion((String) vRowTemp.elementAt(1));
                oMenu.setPadre((String) vRowTemp.elementAt(2));
                vObj.add(oMenu);
                lista.add(oMenu);
            }
            nTam = vObj.size();
            arrRet = new Menu[nTam];

            for (i=0; i<nTam; i++){
                arrRet[i] = vObj.elementAt(i);
            }
        }
        return lista;
    }
    
    public Menu buscaMenu(String menu)throws Exception{
        Menu oMenu=new Menu();
        Vector rst = null;
        String sQuery = "";

        if (menu.equals("")) {
            throw new Exception("Menu.buscaMenu: error de programación, faltan datos");
        }else{
            sQuery = "select * from menu where menu='"+menu+"'";
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
                oMenu.setMenu((String) vRowTemp.elementAt(0));
                oMenu.setDescripcion((String) vRowTemp.elementAt(1));
                oMenu.setPadre((String) vRowTemp.elementAt(2));
            }
        }
        return oMenu;
    }

    public String getMenu() {
        return sMenu;
    }

    public void setMenu(String sMenu) {
        this.sMenu = sMenu;
    }

    public String getDescripcion() {
        return sDescripcion;
    }

    public void setDescripcion(String sDescripcion) {
        this.sDescripcion = sDescripcion;
    }

    public String getPadre() {
        return sPadre;
    }

    public void setPadre(String sPadre) {
        this.sPadre = sPadre;
    }
    
    public AccesoDatos getAD() {
        return oAD;
    }

    public void setAD(AccesoDatos oAD) {
        this.oAD = oAD;
    }
}
