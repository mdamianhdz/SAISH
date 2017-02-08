package org.apli.modelbeans.contabilidadInterna;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import org.apli.AD.AccesoDatos;

/**
 *
 * @author Lily_LnBnd
 */
public class ConceptoMovCtaBan implements Serializable{
    private int nConcepMovCta;
    private String sDescripcion;
    private String sTipoMovCta;//E=Entrada; S=Salida
    private AccesoDatos oAD;
    
    public ConceptoMovCtaBan(){
        
    }
    
    public List<ConceptoMovCtaBan> buscaConceptos(String sTipoMov) throws Exception{
        ConceptoMovCtaBan oCon=null;
        List<ConceptoMovCtaBan> listRet=null;
        Vector rst = null;
        String sQuery = "";
        
        sQuery = "select * from buscaConceptos('"+sTipoMov+"')";

        System.out.println(sQuery);
        if (getAD() == null){
            setAD(new AccesoDatos());
            getAD().conectar();
        }
        rst = getAD().ejecutarConsulta(sQuery);
        getAD().desconectar();
        setAD(null);
        if (rst != null) {
            listRet=new ArrayList();
            for (int i = 0; i < rst.size(); i++) {
                Vector vRowTemp = (Vector)rst.elementAt(i); 
               
                oCon=new ConceptoMovCtaBan();
                oCon.setConcepMovCta(((Double) vRowTemp.elementAt(0)).intValue());
                oCon.setDescripcion((String) vRowTemp.elementAt(1));
                listRet.add(oCon);
            }
        }
        return listRet;
    }
    
    public String guardaConceptoMovCtaBan(ConceptoMovCtaBan oCMCB) throws Exception{
        Vector rst = null;
        String sQuery = "";
        
        if (oCMCB==null){
            throw new Exception("Concepto movimiento cuenta bancaria.guarda: error de programación, faltan datos");
	}else {
             sQuery="SELECT * from insertaconceptomovcta('"+oCMCB.getDescripcion()+"'::character varying,'"+oCMCB.getTipoMovCta()+"'::character);";
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
    
    public String modificaConceptoMovCtaBan(ConceptoMovCtaBan oCMCB) throws Exception{
        Vector rst = null;
        String sQuery = "";
        
        if (oCMCB==null){
            throw new Exception("Concepto movimiento cuenta bancaria.modifica: error de programación, faltan datos");
	}else {
            sQuery="select * from modificaconceptomovcta("+oCMCB.getConcepMovCta()+"::smallint,'"+oCMCB.getDescripcion()+"'::varchar,'"+oCMCB.getTipoMovCta()+"'::character);";
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
    
    public String eliminaConceptoMovCtaBan(ConceptoMovCtaBan oCMCB) throws Exception{
        Vector rst = null;
        String sQuery = "";
        
        if (oCMCB==null){
            throw new Exception("Concepto movimiento cuenta bancaria.elimina: error de programación, faltan datos");
	}else {
            sQuery="select * from eliminaconceptomovcta("+oCMCB.getConcepMovCta()+"::smallint);";
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
    

    public int getConcepMovCta() {
        return nConcepMovCta;
    }

    public void setConcepMovCta(int nConcepMovCta) {
        this.nConcepMovCta = nConcepMovCta;
    }

    public String getDescripcion() {
        return sDescripcion;
    }

    public void setDescripcion(String sDescripcion) {
        this.sDescripcion = sDescripcion;
    }

    public String getTipoMovCta() {
        return sTipoMovCta;
    }

    public void setTipoMovCta(String sTipoMovCta) {
        this.sTipoMovCta = sTipoMovCta;
    }

    public AccesoDatos getAD() {
        return oAD;
    }

    public void setAD(AccesoDatos oAD) {
        this.oAD = oAD;
    } 
}
