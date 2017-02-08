package org.apli.modelbeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import org.apli.AD.AccesoDatos;

/**
 * Unidad de medida (medicamentos, material de curación, inventarios)
 * @author BAOZ
 */
public class UnidadMedida implements Serializable {
    /** 
     * Clave de la unidad de medida
     */
     private String sCve;
    
     /*
      * Descripción de la unidad de medida
      */
     private String sDescrip;
     private AccesoDatos oAD;
     
     public UnidadMedida(){}
     
     public UnidadMedida(AccesoDatos poAD){
        oAD = poAD;
     }
     
     public List<UnidadMedida> buscatodasunidades() throws Exception{            
            
            List<UnidadMedida> listaUnidades = new ArrayList<UnidadMedida>();
            
           
            UnidadMedida arrS[] = null, oUM=null;
            Vector rst = null;
            Vector<UnidadMedida> vObj = null;

            String sQuery = "";
            int i=0, nTam = 0;

                sQuery = "select * from buscatodasunidades();";

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
                    vObj = new Vector<UnidadMedida>();
                    for (i = 0; i < rst.size(); i++) {
                        oUM = new UnidadMedida();
                        Vector vRowTemp = (Vector)rst.elementAt(i);

                        oUM.setCve((String) vRowTemp.elementAt(0)); 
                        oUM.setDescrip((String) vRowTemp.elementAt(1));                         
                        listaUnidades.add(oUM);
                    }
                    nTam = vObj.size();
                    arrS = new UnidadMedida[nTam];

                    for (i=0; i<nTam; i++){
                        arrS[i] = vObj.elementAt(i);
                    }
                }
                
                return listaUnidades;
     }
     
     public UnidadMedida buscaUnidadMedidad(String cveUnidad) throws Exception {
        UnidadMedida oUM = new UnidadMedida();
        Vector rst = null;
        String sQuery = "";

        if (cveUnidad.equals("")) {
            throw new Exception("UnidadMedida.buscaUnidadMedidad: error de programación, faltan datos");
        } else {
            sQuery = "select * from buscaLlaveunidadmedida('" + cveUnidad + "')";
            if (this.getAD() == null) {
                this.setAD(new AccesoDatos());
                this.getAD().conectar();
                rst = this.getAD().ejecutarConsulta(sQuery);
                this.getAD().desconectar();
                setAD(null);
            } else {
                rst = this.getAD().ejecutarConsulta(sQuery);
            }

            if (rst != null && rst.size() == 1) {
                Vector vRowTemp = (Vector) rst.elementAt(0);
                oUM.setCve((String) vRowTemp.elementAt(0));
                oUM.setDescrip((String) vRowTemp.elementAt(1));
            }
        }
        return oUM;
    }
     public String opUnidad(String op)throws Exception{
        Vector rst=null;
        String sQuery="";
        
        if(this.sDescrip.equals("")||this.sCve.equals("")){
            throw new Exception("UnidadMedida.opUnidad: error de programacion, faltan datos");
        }else{
            sQuery="select * from opUnidad('"+this.sDescrip+"','"+this.sCve+"')";
            if(getAD()==null){
                setAD(new AccesoDatos());
                getAD().conectar();
                rst=getAD().ejecutarConsulta(sQuery);
                getAD().desconectar();
                setAD(null);
            }else{
                rst=getAD().ejecutarConsulta(sQuery);
            }
        }
        return " "+((rst==null || rst.isEmpty())?"":rst.get(0));
    }
    
    public String insertar()throws Exception{
        Vector rst=null;
        String sQuery="";
        
        if(this.sCve.equals("")){
            throw new Exception("UnidadMedida.guarda: error de programacion, faltan datos");
        }else{
            sQuery="select * from insertaunidad('"+this.sCve+"','"+this.sDescrip+"')";
            if(oAD==null){
                oAD=new AccesoDatos();
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
        
        if(this.sCve.equals("")){
            throw new Exception("UnidadMedida.modifica: error de programacion, faltan datos");
        }else{
            sQuery="select * from modificaunidad('"+this.sCve+"','"+this.sDescrip+"')";
            System.out.println(sQuery);
            if(getAD()==null){
                setAD(new AccesoDatos());
                getAD().conectar();
                rst=getAD().ejecutarConsulta(sQuery);
                getAD().desconectar();
                setAD(null);
            }
        }
        return ""+((rst==null || rst.isEmpty())?"":rst.get(0).toString().substring(1,rst.get(0).toString().length()-1));
    }
    
    public String eliminar()throws Exception{
        Vector rst=null;
        String sQuery="";
        
        if(this.sCve.equals("")){
            throw new Exception("UnidadMedida.elimina: error de prograamcion, faltan datos");
        }else{
            sQuery="select * from eliminaunidad('"+this.sCve+"')";
            if(getAD()==null){
                setAD(new AccesoDatos());
                getAD().conectar();
                rst=getAD().ejecutarConsulta(sQuery);
                getAD().desconectar();
                setAD(null);
            }
        }
        return ""+((rst==null || rst.isEmpty())?"":rst.get(0).toString().substring(1,rst.get(0).toString().length()-1));
    }
    
    public AccesoDatos getAD() {
        return oAD;
    }

    public void setAD(AccesoDatos oAD) {
        this.oAD = oAD;
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
    
    
}
