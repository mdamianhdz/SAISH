package org.apli.modelbeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import org.apli.AD.AccesoDatos;

/**
 * Catálogo de métodos anticonceptivos
 * @author BAOZ
 * @version 1.0
 * @updated 14-mar-2014 11:40:50 a.m.
 */
public class MetodoAnticonceptivo implements Serializable{

    /**
     * Clave del m�todo anticonceptivo
     */
    private String sCve;
    /**
     * Descripci�n del tipo de m�todo anticonceptivo
     */
    private String sDescrip;
    private AccesoDatos oAD;

    public MetodoAnticonceptivo(){

    }

    public void finalize() throws Throwable {

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
    
    public String getDescip() {
        return sDescrip;
    }

    public void setDescrip(String sDescrip) {
        this.sDescrip = sDescrip;
    }
       
    public List<MetodoAnticonceptivo> buscaTodos() throws Exception{
        List<MetodoAnticonceptivo> listRet=null;
        MetodoAnticonceptivo oMA;
        
        Vector rst = null;
        String sQuery = "";

        sQuery="select * from buscaTodosMetodoAnticoncep()";     
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
                oMA = new MetodoAnticonceptivo();
                Vector vRowTemp = (Vector)rst.elementAt(i);
                oMA.setCve(vRowTemp.elementAt(0).toString());
                oMA.setDescrip((String)vRowTemp.elementAt(1));
                listRet.add(oMA);
            }
        }
        return listRet;
    }
    
    public String eliminaMAPaciente(int pnFolioPac)throws Exception{
        Vector rst=null;
        String sQuery="";
        if (pnFolioPac==0){
            throw new Exception("MetodoAnticonceptivo.eliminaMAPaciente: error de programación, faltan datos");
        }else{
            sQuery="select * from eliminaMAPac("+pnFolioPac+")";
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
    
    public String actualizaMAPaciente(String[] listaMA, int nFolioPac) throws Exception{
        Vector rst = null;
        String sQuery="";
        if (listaMA == null || nFolioPac==0) {
            throw new Exception("MetodoAnticonceptivo.eliminaMAPaciente: error de programación, faltan datos");
	}else {
            if (getAD() == null){
                setAD(new AccesoDatos());
		getAD().conectar();
                for (int i=0; i<listaMA.length;i++){
                    sQuery="select * from insertaMetAnticoncepUsados("+nFolioPac+","+listaMA[i]+"::int2);";
                    rst=getAD().ejecutarConsulta(sQuery);
                }
            }else{
                for (int i=0; i<listaMA.length;i++){
                    sQuery="select * from insertaMetAnticoncepUsados("+nFolioPac+","+listaMA[i]+"::int2);";
                    rst=getAD().ejecutarConsulta(sQuery);
                }
            }
            getAD().desconectar();
            setAD(null);
	}
        return ""+rst.get(0);
    }
    
    public String[] buscaMAPac(int nFolio) throws Exception{
        Vector rst = null;
        String[] arrRet=null;
        String sQuery="";
        if (nFolio==0) {
            throw new Exception("MetodoAnticonceptivo.buscaMAPac: error de programación, faltan datos");
	}else {
            sQuery = "select * from buscaMetAnticoncepPaciente("+nFolio+");";
            if (getAD() == null){
                setAD(new AccesoDatos());
		getAD().conectar();
                rst = this.getAD().ejecutarConsulta(sQuery);
                getAD().desconectar();
                setAD(null);
            }else{
                rst = this.getAD().ejecutarConsulta(sQuery);
            }
            if (rst != null) {
                Vector<String> vObj = new Vector<String>();
                arrRet=new String[rst.size()];
                for (int i = 0; i < rst.size(); i++) {
                   Vector vRowTemp = (Vector)rst.elementAt(i);
                   arrRet[i]=((String) vRowTemp.elementAt(1).toString());
                }
            }
            return arrRet;
        }
    }

    public String insertaMetodoAnticonceptivo(MetodoAnticonceptivo oMA) throws Exception {
        Vector rst = null;
        String sQuery = "";

        if (oMA == null) {
            throw new Exception("");
        } else {
            sQuery = "select * from insertametodoanticonceptivo(" + oMA.getCve() + "::smallint,'" + oMA.getDescip() + "'::character varying);";
            System.out.println(sQuery);
            if (getAD() == null) {
                setAD(new AccesoDatos());
                getAD().conectar();
                rst = getAD().ejecutarConsulta(sQuery);
                getAD().desconectar();
                setAD(null);
            }
        }
        return "" + rst.get(0).toString().substring(1, rst.get(0).toString().length() - 1);
    }

    public String modificaMetodoAnticonceptivo(MetodoAnticonceptivo oMA) throws Exception {
        Vector rst = null;
        String sQuery = "";

        if (oMA == null) {
            throw new Exception("");
        } else {
            sQuery = "select * from modificametodoanticonceptivo(" + oMA.getCve()+ "::smallint,'" + oMA.getDescip() + "'::character varying);";
            System.out.println(sQuery);
            if (getAD() == null) {
                setAD(new AccesoDatos());
                getAD().conectar();
                rst = getAD().ejecutarConsulta(sQuery);
                getAD().desconectar();
                setAD(null);
            }
        }
        return "" + rst.get(0).toString().substring(1, rst.get(0).toString().length() - 1);
    }

    public String eliminaMetodoAnticonceptivo(MetodoAnticonceptivo oMA) throws Exception {
        Vector rst = null;
        String sQuery = "";

        if (oMA == null) {
            throw new Exception("");
        } else {
            sQuery = "select * from eliminametodoanticonceptivo(" + oMA.getCve() + "::smallint);";
            System.out.println(sQuery);
            if (getAD() == null) {
                setAD(new AccesoDatos());
                getAD().conectar();
                rst = getAD().ejecutarConsulta(sQuery);
                getAD().desconectar();
                setAD(null);
            }
        }
        return "" + rst.get(0).toString().substring(1, rst.get(0).toString().length() - 1);
    }
}