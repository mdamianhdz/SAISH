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
public class AreaFuncionamiento implements Serializable {

    private int nCveAreaFun;
    private String sDescripcion;
    private AccesoDatos oAD;

    public AreaFuncionamiento() {
    }

    public List<AreaFuncionamiento> todasAreasFuncionamiento() throws Exception {
        List<AreaFuncionamiento> listAF = null;
        AreaFuncionamiento oAF;

        Vector rst = null;
        String sQuery = "";

        sQuery = "select * from buscatodosareafuncionamiento();";

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
            listAF = new ArrayList();
            for (int i = 0; i < rst.size(); i++) {
               Vector vRowTemp = (Vector) rst.elementAt(i);

                oAF = new AreaFuncionamiento();
                oAF.setCveAreaFun(((Double)vRowTemp.elementAt(0)).intValue());
                oAF.setDescripcion((String)vRowTemp.elementAt(1));
               

                listAF.add(oAF);
            }
        }
        return listAF;
    }
	
    public String guardaAreaFuncionamiento(AreaFuncionamiento oAF) throws Exception{
        Vector rst = null;
        String sQuery = "";
        
        if (oAF==null){
            throw new Exception("Área Funcionamiento.guarda: error de programación, faltan datos");
	}else {
             sQuery="SELECT * from insertaareafuncionamiento('"+oAF.getDescripcion()+"'::varchar);";
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
        
        
     public String modificaAreaFuncionamiento(AreaFuncionamiento oAF) throws Exception{
        Vector rst = null;
        String sQuery = "";
        
        if (oAF==null){
            throw new Exception("Área Funcionamiento.modifica: error de programación, faltan datos");
	}else {
            sQuery="select * from modificaareafuncionamiento("+oAF.getCveAreaFun()+"::smallint,'"+oAF.getDescripcion()+"'::varchar);";
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
     
    public String eliminaAreaFuncionamiento(AreaFuncionamiento oAF) throws Exception{
        Vector rst = null;
        String sQuery = "";
        
        if (oAF==null){
            throw new Exception("Área Funcionamiento.elimina: error de programación, faltan datos");
	}else {
            sQuery="select * from eliminaareafuncionamiento("+oAF.getCveAreaFun()+"::smallint);";
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
    
    public int getCveAreaFun() {
        return nCveAreaFun;
    }

    public void setCveAreaFun(int nCveAreaFun) {
        this.nCveAreaFun = nCveAreaFun;
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
