package org.apli.modelbeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import org.apli.AD.AccesoDatos;



/**
 * Corresponde al cat�logo internacional de diagn�sticos m�dicos (CIE-10)
 * @author BAOZ
 * @version 1.0
 * @created 29-abr.-2014 15:50:45
 */
public class Diagnostico implements Serializable{

	/**
	 * Clave del diagn�stico
	 */
	private String sCve;
	/**
	 * Descripci�n del diagn�stico
	 */
	private String sDescrip;
        private String sDescrip2;
        
        private AccesoDatos oAD;

	public Diagnostico(){

	}
        
        public Diagnostico(String scve, String sdescrip){
            this.sCve=scve;
            this.sDescrip=sdescrip;
        }
    
    public List<Diagnostico> buscaTodos() throws Exception{
        List<Diagnostico> listRet=null;
        Diagnostico oD;
        
        Vector rst = null;
        String sQuery = "";

        sQuery="select * from buscaTodosDiagnostico()";     
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
                oD = new Diagnostico();
                Vector vRowTemp = (Vector)rst.elementAt(i);
                oD.setCve((String)vRowTemp.elementAt(0));
                oD.setDescrip((String)vRowTemp.elementAt(1));
                String d=(String)vRowTemp.elementAt(1);
                String d2="";
                if(d.length()>71){
                    for(int j=0;j<70;j++){
                        d2+=d.charAt(j);
                    }
                    d2+="...";
                }else{
                    d2=d;
                }
                oD.setDescrip2(d2);


                listRet.add(oD);
            }
        }
        return listRet;
    }
    
    public List<Diagnostico> buscaFiltro(String scvediag) throws Exception{
        List<Diagnostico> listRet=null;
        Diagnostico oD;
        
        Vector rst = null;
        String sQuery = "";

        sQuery="SELECT * FROM buscafiltrodiagnostico('"+scvediag+"'::character varying);";     
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
                oD = new Diagnostico();
                Vector vRowTemp = (Vector)rst.elementAt(i);
                oD.setCve((String)vRowTemp.elementAt(0));
                oD.setDescrip((String)vRowTemp.elementAt(1));
                listRet.add(oD);
            }
        }
        return listRet;
    }
    
    public Diagnostico buscaDiagnostico(String sClave) throws Exception{
        
        Diagnostico oD= new Diagnostico();
        if (sClave.equals("")) {
            throw new Exception("Funcion.buscaDiagnostico: error de programación, faltan datos");
        }else {
            Vector rst = null;
            String sQuery = "";

            sQuery="SELECT * FROM buscadiagnostico('"+sClave+"');";     
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


                    oD = new Diagnostico();
                    Vector vRowTemp = (Vector)rst.elementAt(0);
                    oD.setCve((String)vRowTemp.elementAt(0));
                    oD.setDescrip((String)vRowTemp.elementAt(1));

            }
        }
        return oD;
    }
    
    public String guardaDiagnostico(Diagnostico oD) throws Exception{
        Vector rst = null;
        String sQuery = "";
        
        if (oD==null){
            throw new Exception("Diagnostico.guarda: error de programación, faltan datos");
	}else {
             sQuery="SELECT * from insertadiagnostico('"+oD.getCve()+"'::character varying,"+oD.getDescrip()+"'::character varying);";
            System.out.println(sQuery);
            if (getAD() == null){
                setAD(new AccesoDatos());
		getAD().conectar();
		rst = getAD().ejecutarConsulta(sQuery);
		getAD().desconectar();
                setAD(null);
            }  
	}
        return ""+rst.get(0).toString().substring(1, rst.get(0).toString().length() - 1);
    }
        
     public String modificaDiagnostico(Diagnostico oD) throws Exception{
        Vector rst = null;
        String sQuery = "";
        
        if (oD==null){
            throw new Exception("Diagnostico.modifica: error de programación, faltan datos");
	}else {
            sQuery="SELECT * from modificadiagnostico('"+oD.getCve()+"'::character varying,"+oD.getDescrip()+"'::character varying);";
            System.out.println(sQuery);
            if (getAD() == null){
                setAD(new AccesoDatos());
		getAD().conectar();
		rst = getAD().ejecutarConsulta(sQuery);
		getAD().desconectar();
                setAD(null);
            }  
	}
        return ""+rst.get(0).toString().substring(1, rst.get(0).toString().length() - 1);
    }
     
    public String eliminaDiagnostico(Diagnostico oD) throws Exception{
        Vector rst = null;
        String sQuery = "";
        
        if (oD==null){
            throw new Exception("Diagnostico.elimina: error de programación, faltan datos");
	}else {
            sQuery="select * from eliminadiagnostico("+oD.getCve()+"::character varying);";
            System.out.println(sQuery);
            if (getAD() == null){
                setAD(new AccesoDatos());
		getAD().conectar();
                rst = getAD().ejecutarConsulta(sQuery);
		getAD().desconectar();
                setAD(null);
            }  
	}
        return ""+rst.get(0).toString().substring(1, rst.get(0).toString().length() - 1);
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
    
    public AccesoDatos getAD(){
        return oAD;
    }
    
    public void setAD(AccesoDatos oAD) {
        this.oAD = oAD;
    }
    
    public String getDescrip2() {
        return sDescrip2;
    }

    public void setDescrip2(String sDescrip) {
        this.sDescrip2 = sDescrip;
    }

 
    
}