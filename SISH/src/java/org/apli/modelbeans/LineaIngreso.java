package org.apli.modelbeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import org.apli.AD.AccesoDatos;

/**
 * Corresponde a una clasificación de ingresos
 *
 * @author BAOZ
 * @version 1.0
 * @created 09-may-2014 11:49:41 a.m.
 */
public class LineaIngreso implements Serializable {
    /**
     * Clave de la l�nea, es autogenerada
     */
    private int nCveLin;
    /**
     * Descripci�n de la l�nea
     */
    private String sDescrip;
    private String sDescripFact;
    
    private AccesoDatos oAD = null;
    
    public static int CVE_LIN_DEVOL = 85;
    public static int CVE_LIN_IMSS_INFONAVIT = 100;

    public LineaIngreso() {

    }

    public LineaIngreso(AccesoDatos poAD) {
        oAD = poAD;
    }
    
    public ArrayList<LineaIngreso> todasLineasIngrServCaja() throws Exception {
    LineaIngreso oL = null;
    Vector rst = null;
    ArrayList<LineaIngreso> listLineas = new ArrayList();
    String sQuery = "";
    int i = 0;
        sQuery = "select * from buscaTodasLineasServsCaja()";

        if (this.oAD == null) {
            this.oAD = new AccesoDatos();
            this.oAD.conectar();
            rst = this.oAD.ejecutarConsulta(sQuery);
            this.oAD.desconectar();
            this.oAD = null;
        } else {
            rst = this.oAD.ejecutarConsulta(sQuery);
        }
        if (rst != null) {
            for (i = 0; i < rst.size(); i++) {
                oL = new LineaIngreso();
                Vector vRowTemp = (Vector) rst.elementAt(i);

                oL.setCveLin(((Double) vRowTemp.elementAt(0)).intValue());
                oL.setDescrip((String) vRowTemp.elementAt(1));
                listLineas.add(oL);
            }
        }
        return listLineas;
    }
    
    public List<LineaIngreso> todasLineasIng(String sArea, String sTipo) 
            throws Exception {
    int clave = Integer.parseInt(sArea);
    LineaIngreso oL = null;
    Vector rst = null;
    Vector<LineaIngreso> vObj = null;
    List<LineaIngreso> listLineas = new ArrayList();
    String sQuery = "";
    int i = 0;

        sQuery = "select * from buscatodaslineasingreso(" + clave + 
                "::int2, '"+sTipo+"')";

        if (this.oAD == null) {
            this.oAD= new AccesoDatos();
            this.oAD.conectar();
            rst = this.oAD.ejecutarConsulta(sQuery);
            this.oAD.desconectar();
            this.oAD = null;
        } else {
            rst = this.oAD.ejecutarConsulta(sQuery);

        }
        if (rst != null) {
            vObj = new Vector<LineaIngreso>();
            for (i = 0; i < rst.size(); i++) {
                oL = new LineaIngreso();
                Vector vRowTemp = (Vector) rst.elementAt(i);

                oL.setCveLin(((Double) vRowTemp.elementAt(0)).intValue());
                oL.setDescrip((String) vRowTemp.elementAt(1));
                listLineas.add(oL);
                System.out.println(oL.getDescrip());
            }
        }

        return listLineas;
    }
    
    public LineaIngreso buscaLineaIngreso() throws Exception {
        LineaIngreso oLI = new LineaIngreso();
        Vector rst = null;
        String sQuery = "";

        if (this.nCveLin==0) {
            throw new Exception("LineaIngreso.buscaLineaIngreso: error de programación, faltan datos");
        } else {
            sQuery = "select * from buscaLlavelineaingreso(" + this.nCveLin + "::smallint);";
           // System.out.println("SqL:"+sQuery);
            if (this.oAD == null) {
                this.oAD=new AccesoDatos();
                this.oAD.conectar();
                rst = this.oAD.ejecutarConsulta(sQuery);
                this.oAD.desconectar();
                this.oAD = null;
            } else {
                rst = this.oAD.ejecutarConsulta(sQuery);
            }

            if (rst != null && rst.size() == 1) {
                Vector vRowTemp = (Vector) rst.elementAt(0);
                oLI.setCveLin(((Double) vRowTemp.elementAt(0)).intValue());
                oLI.setDescrip((String) vRowTemp.elementAt(1));
            }
        }
        return oLI;
    }
    
    public LineaIngreso[] buscaTodas() throws Exception{
        LineaIngreso arrRet[]=null, oLI=null;
        Vector rst=null;
        Vector<LineaIngreso> vObj=null;
        String sQuery="";
        int i=0, nTam=0;
        
        sQuery="select * from buscatodaslineas();";
        if(oAD==null){
            oAD=new AccesoDatos();
            oAD.conectar();
            rst=oAD.ejecutarConsulta(sQuery);
            oAD.desconectar();
            oAD=null;
        }else{
            rst=oAD.ejecutarConsulta(sQuery);
        }
        
        if(rst!=null){
            vObj=new Vector<LineaIngreso>();
            for(i=0;i<rst.size();i++){
                oLI=new LineaIngreso();
                Vector vRowTemp=(Vector)rst.elementAt(i);
                oLI.setCveLin(((Double)vRowTemp.elementAt(0)).intValue());
                oLI.setDescrip((String)vRowTemp.elementAt(1));
                vObj.add(oLI);
            }
            nTam=vObj.size();
            arrRet=new LineaIngreso[nTam];
            
            for(i=0;i<nTam;i++){
                arrRet[i]=vObj.elementAt(i);
            }
        }
        return arrRet;
    }
     
    public String opLineaIngreso(String op) throws Exception{
        Vector rst=null;
        String sQuery="";
        
        if(this.sDescrip.equals("") || this.nCveLin==0){
            throw new Exception("LineaIngreso.opLineaIngreso: error de programación, faltan datos");
        }else{
            sQuery="select * from oplinin('"+this.sDescrip+"','"+this.nCveLin+"')";
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
        return " "+((rst==null || rst.isEmpty())?"":rst.get(0));
    }
    
    public String insertar() throws Exception{
        Vector rst=null;
        String sQuery="";
        
        if(this.nCveLin==0){
            throw new Exception("LineaIngreso.insertar: error de programacion, faltan datos");
        }else{
            sQuery="select * from insertalinea("+this.nCveLin+",'"+this.sDescrip+"')";
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
    
    public String modificar() throws Exception{
        Vector rst=null;
        String sQuery="";
        
        if(this.nCveLin==0){
            throw new Exception("LineaIngreso.modificar: error de programacion, faltan datos");
        }else{
            sQuery="select * from modificalinea("+this.nCveLin+",'"+this.sDescrip+"')";
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
    
    public String eliminar()throws Exception{
        Vector rst=null;
        String sQuery="";
        
        if(this.nCveLin==0){
            throw new Exception("LineaIngreso.elimina: error de programacion, faltan datos");
        }else{
            sQuery="select * from eliminalinea("+this.nCveLin+")";
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

    public int getCveLin() {
        return nCveLin;
    }

    public void setCveLin(int nCveLin) {
        this.nCveLin = nCveLin;
    }

    public String getDescrip() {
        return sDescrip;
    }
    public void setDescrip(String sDescrip) {
        this.sDescrip = sDescrip;
    }
    
    public String getDescripFact() {
        return sDescripFact;
    }
    public void setDescripFact(String sDescrip) {
        this.sDescripFact = sDescrip;
    }

    
    
    @Override
    public boolean equals(Object other){
    boolean bRet = false;
        if (other == null) bRet = false;
        if (other == this) bRet = true;
        if (!(other instanceof LineaIngreso))bRet = false;
        LineaIngreso otro = (LineaIngreso)other;
        if (otro != null && otro.nCveLin == this.nCveLin)
            bRet = true;
        return bRet;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + this.nCveLin;
        return hash;
    }
}
