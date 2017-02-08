package org.apli.modelbeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import org.apli.AD.AccesoDatos;

/**
 * Representa un �rea f�sica del hospital
 *
 * @author BAOZ
 * @version 1.0
 * @created 07-abr-2014 10:14:58 a.m.
 */
public class AreaFisica implements Serializable {

    /**
     * Clave del �rea f�sica
     */
    private int nCve;
    /**
     * Descripci�n del �rea f�sica
     */
    private String sDescrip;
    /*
     * Parametro que se envia a farmacia
     */
    private String sIdParaFarm;
    protected AccesoDatos oAD = null;

    public AreaFisica() {
    }

    public AreaFisica(AccesoDatos poAD) {
        oAD = poAD;
    }

    public AreaFisica[] buscaTodos() throws Exception {
        AreaFisica arrRet[] = null, oAreaF = null;
        Vector rst = null;
        Vector<AreaFisica> vObj = null;
        String sQuery = "";
        int i = 0, nTam = 0;

        sQuery = "SELECT * FROM buscaTodosAreaFisicaConParaFarm();";
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
            vObj = new Vector<AreaFisica>();
            for (i = 0; i < rst.size(); i++) {
                Vector vRowTemp = (Vector) rst.elementAt(i);
                oAreaF = new AreaFisica();
                oAreaF.setCve(((Double) vRowTemp.elementAt(0)).intValue());
                oAreaF.setDescrip(vRowTemp.elementAt(1).toString());
                oAreaF.setIdParaFarm(vRowTemp.elementAt(2).toString());
                vObj.add(oAreaF);
            }
            nTam = vObj.size();
            arrRet = new AreaFisica[nTam];

            for (i = 0; i < nTam; i++) {
                arrRet[i] = vObj.elementAt(i);
            }
        }
        return arrRet;
    }
    
    public List<AreaFisica> buscaTodosAF() throws Exception{
        List<AreaFisica> listRet=null;
        AreaFisica oAF;
        
        Vector rst = null;
        String sQuery = "";

        sQuery="select * from buscaTodosAreaFisica()";     
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
                oAF=new AreaFisica();
                Vector vRowTemp = (Vector)rst.elementAt(i);
                oAF.setCve(((Double)vRowTemp.elementAt(0)).intValue());
                oAF.setDescrip((String)vRowTemp.elementAt(1));
                listRet.add(oAF);
            }
        }
        return listRet; 
    }
    
    public List<AreaFisica> buscaAreasFConceptoEgreso(int nCveConceptoEgreso) throws Exception{
        List<AreaFisica> listRet=null;
        AreaFisica oAF;
        
        Vector rst = null;
        String sQuery = "";

        sQuery="select * from buscaAreasFConceptoEgreso("+nCveConceptoEgreso+")";     
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
                oAF=new AreaFisica();
                Vector vRowTemp = (Vector)rst.elementAt(i);
                oAF.setCve(((Double)vRowTemp.elementAt(0)).intValue());
                oAF.setDescrip((String)vRowTemp.elementAt(1));
                listRet.add(oAF);
            }
        }
        return listRet; 
    }

    public void finalize() throws Throwable {
    }

    public int getCve() {
        return nCve;
    }

    public void setCve(int nCve) {
        this.nCve = nCve;
    }

    public String getDescrip() {
        return sDescrip;
    }

    public void setDescrip(String sDescrip) {
        this.sDescrip = sDescrip;
    }

    public String getIdParaFarm() {
        return sIdParaFarm;
    }

    public void setIdParaFarm(String sIdParaFarm) {
        this.sIdParaFarm = sIdParaFarm;
    }

    public AccesoDatos getAD() {
        return oAD;
    }

    public void setAD(AccesoDatos oAD) {
        this.oAD = oAD;
    }
}