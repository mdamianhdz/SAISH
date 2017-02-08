package org.apli.modelbeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import org.apli.AD.AccesoDatos;
import org.apli.modelbeans.contabilidadInterna.SublineaEgreso;

/**
 *
 * @author lleon
 */
public class ConceptoEgreso extends Concepto implements Serializable{
private SublineaEgreso oSublineaEgr;
private AccesoDatos oAD;
public static final int CVE_PREST=139;
public static final int CVE_ADELANTO = 239;
    
    public ConceptoEgreso(){
        oSublineaEgr=new SublineaEgreso();
    }
    
    public List<ConceptoEgreso> buscaConceptosEgreso(int nCveSublinea) throws Exception{
        List<ConceptoEgreso> listRet=null;
        ConceptoEgreso oCE;
        
        Vector rst = null;
        String sQuery = "";

        sQuery="select * from buscaconceptosegreso("+nCveSublinea+"::int2)";   
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
                oCE=new ConceptoEgreso();
                Vector vRowTemp = (Vector)rst.elementAt(i);
                oCE.setCveConcepEgr(((Double)vRowTemp.elementAt(0)).intValue());
                oCE.setDescripcion((String)vRowTemp.elementAt(1));
                listRet.add(oCE);
            }
        }
        return listRet; 
    }

    public List<ConceptoEgreso> buscaConceptosPrestamos() throws Exception{
        List<ConceptoEgreso> listRet=null;
        ConceptoEgreso oCE;
        
        Vector rst = null;
        String sQuery = "";

        sQuery="select * from buscaconceptosegreso("+
                SublineaEgreso.CVE_GTOS_PERSONAL+"::int2) "+
                "where pncveconcepegr IN ("+ CVE_PREST + ","+ CVE_ADELANTO+")";   
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
                oCE=new ConceptoEgreso();
                Vector vRowTemp = (Vector)rst.elementAt(i);
                oCE.setCveConcepEgr(((Double)vRowTemp.elementAt(0)).intValue());
                oCE.setDescripcion((String)vRowTemp.elementAt(1));
                listRet.add(oCE);
            }
        }
        return listRet; 
    }

    
    public int getCveConcepEgr() {
        return nCveConcep;
    }

    public void setCveConcepEgr(int nCveConcepEgr) {
        this.nCveConcep = nCveConcepEgr;
    }

    public SublineaEgreso getSublineaEgreso() {
        return oSublineaEgr;
    }

    public void setSublineaEgreso(SublineaEgreso oSublineaEgr) {
        this.oSublineaEgr = oSublineaEgr;
    }
    
    public AccesoDatos getAD() {
        return oAD;
    }

    public void setAD(AccesoDatos oAD) {
        this.oAD = oAD;
    }
}
