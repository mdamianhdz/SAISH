package org.apli.modelbeans;

import java.io.Serializable;
import java.util.Vector;
import org.apli.AD.AccesoDatos;

/**
 * Entidad de País
 * @author Usuario5/Gigi (abril 2015)
 */
public class Pais implements Serializable {
    /**
     * Clave (acrónimo) del país
     */
    private String sCve;
    /**
     * Descripción del país
     */
    private String sDescrip;

    private AccesoDatos oAD = null;

    public Pais(){

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
        
    public Pais[] buscarTodos() throws Exception {
        Pais arrRet[] = null, oP = null;
        Vector rst = null;
        Vector<Pais> vObj = null;
        String sQuery = "";
        int i = 0, nTam = 0;

        sQuery = "select * from buscatodospais();";
        if (oAD == null) {
            oAD =new AccesoDatos();
            oAD.conectar();
            rst = oAD.ejecutarConsulta(sQuery);
            oAD.desconectar();
            oAD =null;
        } else {
            rst = oAD.ejecutarConsulta(sQuery);
        }

        if (rst != null) {
            vObj = new Vector<Pais>();
            for (i = 0; i < rst.size(); i++) {
                oP = new Pais();
                Vector vRowTemp = (Vector) rst.elementAt(i);
                oP.setCve((String) vRowTemp.elementAt(0));
                oP.setDescrip((String) vRowTemp.elementAt(1));
                vObj.add(oP);
            }
            nTam = vObj.size();
            arrRet = new Pais[nTam];
            for (i = 0; i < nTam; i++) {
                arrRet[i] = vObj.elementAt(i);
            }
        }
        return arrRet;
    }
    
    public String insertar()throws Exception{
        Vector rst=null;
        String sQuery="";
        
        if(this.sCve.equals("")){
            throw new Exception("Pais.guarda: error de programacion, faltan datos");
        }else{
            sQuery="select * from insertarais('"+this.sCve+"','"+this.sDescrip+"')";
            System.out.println(sQuery);
            if(oAD==null){
                oAD = new AccesoDatos();
                oAD.conectar();
                rst=oAD.ejecutarConsulta(sQuery);
                oAD.desconectar();
                oAD=null;
            }else{
                rst=oAD.ejecutarConsulta(sQuery);
            }
        }
        return ""+((rst==null || rst.isEmpty())?"":rst.get(0).toString().substring(1,rst.get(0).toString().length()-1));
    }
    
    public String modificar()throws Exception{
        Vector rst=null;
        String sQuery="";
        
        if(this.sCve==null){
            throw new Exception("Pais.modifica: error de programacion, faltan datos");
        }else{
            sQuery="select * from modificapais('"+this.sCve+"','"+this.sDescrip+"')";
            System.out.println(sQuery);
            if(oAD==null){
                oAD = new AccesoDatos();
                oAD.conectar();
                rst=oAD.ejecutarConsulta(sQuery);
                oAD.desconectar();
                oAD=null;
            }else{
                rst=oAD.ejecutarConsulta(sQuery);
            }
        }
        return ""+((rst==null || rst.isEmpty())?"":rst.get(0).toString().substring(1,rst.get(0).toString().length()-1));
    }
    
    public String eliminar()throws Exception{
        Vector rst=null;
        String sQuery="";
        
        if(this.sCve==null){
            throw new Exception("Pais.elimina: error de programacion, faltan datos");
        }else{
            sQuery="select * from eliminar('"+this.sCve+"')";
            if(oAD==null){
                oAD = new AccesoDatos();
                oAD.conectar();
                rst=oAD.ejecutarConsulta(sQuery);
                oAD.desconectar();
                oAD=null;
            }else{
                rst=oAD.ejecutarConsulta(sQuery);
            }
        }
        return ""+((rst==null || rst.isEmpty())?"":rst.get(0).toString().substring(1,rst.get(0).toString().length()-1));
    }
}
