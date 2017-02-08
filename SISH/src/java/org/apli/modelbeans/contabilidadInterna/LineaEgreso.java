package org.apli.modelbeans.contabilidadInterna;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import org.apli.AD.AccesoDatos;

/**
 *
 * @author Lily_LnBnd
 */
public class LineaEgreso implements Serializable{
    private int nCveLineaEgr;
    private String sDescripcion;
    private AccesoDatos oAD;
    
    public LineaEgreso(){
        
    }
    
    public List<LineaEgreso> buscaTodosLineaEgreso() throws Exception{
        List<LineaEgreso> listRet=null;
        LineaEgreso oLE;
        
        Vector rst = null;
        String sQuery = "";

        sQuery="select * from buscaTodosLineaEgreso()";     
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
                oLE=new LineaEgreso();
                Vector vRowTemp = (Vector)rst.elementAt(i);
                oLE.setCveLineaEgr(((Double)vRowTemp.elementAt(0)).intValue());
                oLE.setDescripcion((String)vRowTemp.elementAt(1));
                listRet.add(oLE);
            }
        }
        return listRet; 
    }

    public String guardaEgreso(LineaEgreso oLineaEgreso) throws Exception {
        Vector rst=null;
        String sQuery="";
        
        if(oLineaEgreso==null){
            throw new Exception("LineaEgreso.guarda: error de programacion, faltan datos");
        }else{
            sQuery="select * from insertarlineaegreso('"+oLineaEgreso.getCveLineaEgr()+"','"+oLineaEgreso.getDescripcion()+"')";
            System.out.println(sQuery);
            if(getAD()==null){
                setAD(new AccesoDatos());
                getAD().conectar();
                rst=getAD().ejecutarConsulta(sQuery);
                getAD().desconectar();
                setAD(null);
            }
        }
        return ""+rst.get(0).toString().substring(1, rst.get(0).toString().length()-1);
    }
    
    public String modificaEgreso(LineaEgreso oLineaEgreso) throws Exception {
        Vector rst=null;
        String sQuery="";
        
        if(oLineaEgreso==null){
            throw new Exception("LineaEgreso.guarda: error de programacion, faltan datos");
        }else{
            sQuery="select * from modificarlineaegreso('"+oLineaEgreso.getCveLineaEgr()+"','"+oLineaEgreso.getDescripcion()+"')";
            System.out.println(sQuery);
            if(getAD()==null){
                setAD(new AccesoDatos());
                getAD().conectar();
                rst=getAD().ejecutarConsulta(sQuery);
                getAD().desconectar();
                setAD(null);
            }
        }
        return ""+rst.get(0).toString().substring(1, rst.get(0).toString().length()-1);
    }
    
    public String eliminaEgreso(LineaEgreso oLineaEgreso) throws Exception{
        Vector rst=null;
        String sQuery="";
        
        if(oLineaEgreso==null){
            throw new Exception("LineaEgreso.elimina: error de programacion, faltan datos");
        }else{
            sQuery="select * from eliminarlineaegreso('"+oLineaEgreso.getCveLineaEgr()+"','"+oLineaEgreso.getDescripcion()+"')";
            System.out.println(sQuery);
            if(getAD()==null){
                setAD(new AccesoDatos());
                getAD().conectar();
                rst=getAD().ejecutarConsulta(sQuery);
                getAD().desconectar();
                setAD(null);
            }
        }
        return ""+rst.get(0).toString().substring(1, rst.get(0).toString().length()-1);
    }
    
    public int getCveLineaEgr() {
        return nCveLineaEgr;
    }

    public void setCveLineaEgr(int nCveLineaEgr) {
        this.nCveLineaEgr = nCveLineaEgr;
    }

    public String getDescripcion() {
        return sDescripcion;
    }

    public void setDescripcion(String sDescripcion) {
        this.sDescripcion = sDescripcion;
    }

    public AccesoDatos getAD() {
        return oAD;
    }

    public void setAD(AccesoDatos oAD) {
        this.oAD = oAD;
    }
    
}
