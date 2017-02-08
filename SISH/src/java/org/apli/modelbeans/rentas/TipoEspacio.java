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
public class TipoEspacio implements Serializable{
    private int nIdTipoEsp;
    private String sDescripcion;
    private String sFmtContrato;
    private AccesoDatos oAD;
    
    public TipoEspacio(){
        
    }
    
    public List<TipoEspacio> buscaTipos()throws Exception{
        List<TipoEspacio> listRet=null;
        TipoEspacio oTE;
        Vector rst;
        
        String sQuery="select * from buscaTiposEspacio();";
        
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
                oTE=new TipoEspacio();
                oTE.setIdTipoEsp(((Double)vRowTemp.get(0)).intValue());
                oTE.setDescripcion((String)vRowTemp.get(1));
                oTE.setFmtContrato((String)vRowTemp.get(2));
                listRet.add(oTE);
            }
        }
        return listRet;
    }

    public int getIdTipoEsp() {
        return nIdTipoEsp;
    }

    public void setIdTipoEsp(int nIdTipoEsp) {
        this.nIdTipoEsp = nIdTipoEsp;
    }

    public String getDescripcion() {
        return sDescripcion;
    }

    public void setDescripcion(String sDescripcion) {
        this.sDescripcion = sDescripcion;
    }

    public String getFmtContrato() {
        return sFmtContrato;
    }

    public void setFmtContrato(String sFmtContrato) {
        this.sFmtContrato = sFmtContrato;
    }

    public AccesoDatos getAD() {
        return oAD;
    }

    public void setAD(AccesoDatos oAD) {
        this.oAD = oAD;
    }

}
