/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apli.modelbeans.rentas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import org.apli.AD.AccesoDatos;

/**
 *
 * @author Lily_LnBnd
 */
public class CondRenta implements Serializable{

    private int nSec;
    private String sDescripcion;
    private AccesoDatos oAD;
    
    public CondRenta(){
    }
    
    public List<CondRenta> buscaCondicionesRenta(int nIdEspacio)throws Exception{
        List<CondRenta> listRet=null;
        CondRenta oCR;
        Vector rst;
        
         if (nIdEspacio==0){
             throw new Exception("CondRenta.buscaCondicionesRenta: error de programación, faltan datos");
        }else{
        
            String sQuery="select * from buscaCondicionesRenta("+nIdEspacio+"::int2)";
            System.out.println(sQuery);
            if (getAD()==null){
                setAD(new AccesoDatos());
                getAD().conectar();
            }
            rst=getAD().ejecutarConsulta(sQuery);
            getAD().desconectar();
            setAD(null);

            if(rst!=null){
                listRet=new ArrayList();
                for (int i = 0; i < rst.size(); i++) {
                    Vector vRowTemp=(Vector) rst.get(i);
                    oCR=new CondRenta();
                    oCR.setSec(((Double)vRowTemp.get(0)).intValue());
                    oCR.setDescripcion((String)vRowTemp.get(1));

                    listRet.add(oCR);
                }
            }
         }
        return listRet;
    }

    public int getSec() {
        return nSec;
    }

    public void setSec(int nSec) {
        this.nSec = nSec;
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
