package org.apli.modelbeans.contabilidadInterna;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import org.apli.AD.AccesoDatos;

/**
 * Clase para sublíneas de conceptos de egreso
 * @author Lily_LnBnd
 */
public class SublineaEgreso implements Serializable{
    private int nCveSublineaEgre;
    private String sDescripcion;
    private LineaEgreso oLineaEgre;
    private AccesoDatos oAD;
    public static final int CVE_GTOS_PERSONAL = 33;
    
    public SublineaEgreso(){
        oLineaEgre=new LineaEgreso();
    }
    
    public List<SublineaEgreso> todasSublineasEgreso() throws Exception {
        List<SublineaEgreso> listSE = null;
        SublineaEgreso oSE;

        Vector rst = null;
        String sQuery = "";

        sQuery = "SELECT * FROM buscatodossublineaegreso();";

        if (getAD() == null) {
            setAD(new AccesoDatos());
            getAD().conectar();
            rst = getAD().ejecutarConsulta(sQuery);
            getAD().desconectar();
            setAD(null);
        } else {
            rst = getAD().ejecutarConsulta(sQuery);
        }

        if (rst != null) {
            listSE = new ArrayList();
            for (int i = 0; i < rst.size(); i++) {
               Vector vRowTemp = (Vector) rst.elementAt(i);

                oSE = new SublineaEgreso();
                oSE.setCveSublineaEgre(((Double)vRowTemp.elementAt(0)).intValue());
                oSE.setDescripcion((String)vRowTemp.elementAt(1));
                //oSE.getLineaEgre().setCveLineaEgr(((Double)vRowTemp.elementAt(2)).intValue());
                oSE.getLineaEgre().setDescripcion((String)vRowTemp.elementAt(2));
                listSE.add(oSE);
            }
        }
        return listSE;
    }
    
    public String guardaSublineaEgreso(SublineaEgreso oSE) throws Exception{
        Vector rst = null;
        String sQuery = "";
        
        if (oSE==null){
            throw new Exception("Sublínea Egreso.guarda: error de programación, faltan datos");
	}else {
            sQuery="SELECT * FROM insertasublineaegreso('"+oSE.getDescripcion()+"'::character varying,"+oSE.oLineaEgre.getCveLineaEgr()+"::smallint);";
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
    
     public String modificaSublineaEgreso(SublineaEgreso oSE) throws Exception{
        Vector rst = null;
        String sQuery = "";
        
        if (oSE==null){
            throw new Exception("Sublínea Egreso.modifica: error de programación, faltan datos");
	}else {
            sQuery="SELECT * FROM modificasublineaegreso("+oSE.getCveSublineaEgre()+"::smallint,'"+oSE.getDescripcion()+"'::character varying,"+oSE.oLineaEgre.getCveLineaEgr()+"::smallint);";
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
     
    public String eliminaSublineaEgreso(SublineaEgreso oSE) throws Exception{
        Vector rst = null;
        String sQuery = "";
        
        if (oSE==null){
            throw new Exception("Sublínea Egreso.elimina: error de programación, faltan datos");
	}else {
            sQuery="SELECT * FROM eliminasublineaegreso("+oSE.getCveSublineaEgre()+"::smallint);";
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
    
    public List<SublineaEgreso> buscaSublineas(int nCveLineaEgreso) throws Exception{
        List<SublineaEgreso> listRet=null;
        SublineaEgreso oSubLE;
        
        Vector rst = null;
        String sQuery = "";

        sQuery="select * from buscaSubnlineasEgreso("+nCveLineaEgreso+"::int2)";   
        System.out.println(sQuery);
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
                oSubLE=new SublineaEgreso();
                Vector vRowTemp = (Vector)rst.elementAt(i);
                oSubLE.setCveSublineaEgre(((Double)vRowTemp.elementAt(0)).intValue());
                oSubLE.setDescripcion((String)vRowTemp.elementAt(1));
                listRet.add(oSubLE);
            }
        }
        return listRet; 
    }

    public int getCveSublineaEgre() {
        return nCveSublineaEgre;
    }

    public void setCveSublineaEgre(int nCveSublineaEgre) {
        this.nCveSublineaEgre = nCveSublineaEgre;
    }

    public String getDescripcion() {
        return sDescripcion;
    }

    public void setDescripcion(String sDescripcion) {
        this.sDescripcion = sDescripcion;
    }

    public LineaEgreso getLineaEgre() {
        return oLineaEgre;
    }

    public void setLineaEgre(LineaEgreso oLineaEgre) {
        this.oLineaEgre = oLineaEgre;
    }

    public AccesoDatos getAD() {
        return oAD;
    }

    public void setAD(AccesoDatos oAD) {
        this.oAD = oAD;
    }
}
