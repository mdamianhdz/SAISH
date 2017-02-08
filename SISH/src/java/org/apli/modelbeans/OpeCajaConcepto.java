/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apli.modelbeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import org.apli.AD.AccesoDatos;

/**
 *
 * @author lleon
 */
public class OpeCajaConcepto implements Serializable{
    private OperacionCaja oOpeCaja;
    private int nSec;
    private ConceptoEgreso oConcepEgr;
    private ConceptoIngreso oConcepIngr;
    private ServicioPrestado oServPrest;
    private float nMonto;
    private String sNombreSerie;
    private int nFolioCFDI;
    private AccesoDatos oAD;
    
    public OpeCajaConcepto(){
        oOpeCaja=new OperacionCaja();
        oServPrest=new ServicioPrestado();
    }
    
    public List<OpeCajaConcepto> buscaExternos(int nFolioPaciente) throws Exception{
        List<OpeCajaConcepto> listRet=null;
        OpeCajaConcepto oOpeCC;
         OperacionCaja oOpeCaja;
         ServicioPrestado oSP;
         ConceptoIngreso oConcepI;
         Paciente oPac;
        
        Vector rst;
        String sQuery = "";
        
        sQuery = "select * from buscaServicioExterno("+nFolioPaciente+")";
        System.out.println(sQuery);
        if (getAD() == null){
            setAD(new AccesoDatos());
            getAD().conectar();
        }
        rst = getAD().ejecutarConsulta(sQuery);
        getAD().desconectar();
        setAD(null);
        
        if (rst != null) {
            listRet = new ArrayList();
            for (int i = 0; i < rst.size(); i++) {
                Vector vRowTemp = (Vector)rst.elementAt(i);
                oOpeCC=new OpeCajaConcepto();
                oOpeCaja=new OperacionCaja();
                oSP=new ServicioPrestado();
                oConcepI=new ConceptoIngreso();
                oPac=new Paciente();
                
                oSP.setIdFolio((String) vRowTemp.elementAt(0));
                oSP.setRealizado((Date) vRowTemp.elementAt(1));
                oConcepI.setDescripConcep((String) vRowTemp.elementAt(2));
                oOpeCC.setMonto(((Double) vRowTemp.elementAt(3)).floatValue());
                oOpeCaja.setFolio(((Double) vRowTemp.elementAt(4)).intValue());
                oPac.setNombre((String) vRowTemp.elementAt(5));
                oPac.setApellidoPaterno((String) vRowTemp.elementAt(6));
                oPac.setApellidoMaterno((String) vRowTemp.elementAt(7));
                
                oSP.setConcepPrestado(oConcepI);
                oSP.setPaciente(oPac);
                oOpeCC.setServPrest(oSP);
                oOpeCC.setOpeCaja(oOpeCaja);
                
                listRet.add(oOpeCC);
            }
        }
        return listRet;
    }
    
    public List<OpeCajaConcepto> buscaHospitalarios(int nFolioPaciente) throws Exception{
        List<OpeCajaConcepto> listRet=null;
        OpeCajaConcepto oOpeCC;
         OperacionCaja oOpeCaja;
         ServicioPrestado oSP;
         Paciente oPac;
         EpisodioMedico oEM;
        
        Vector rst;
        String sQuery = "";
        
        sQuery = "select * from buscaServicioHospitalario("+nFolioPaciente+")";
        System.out.println(sQuery);
        if (getAD() == null){
            setAD(new AccesoDatos());
            getAD().conectar();
        }
        rst = getAD().ejecutarConsulta(sQuery);
        getAD().desconectar();
        setAD(null);
        
        if (rst != null) {
            listRet = new ArrayList();
            for (int i = 0; i < rst.size(); i++) {
                Vector vRowTemp = (Vector)rst.elementAt(i);
                oOpeCC=new OpeCajaConcepto();
                oOpeCaja=new OperacionCaja();
                oSP=new ServicioPrestado();
                oPac=new Paciente();
                oEM=new EpisodioMedico();
                
                oSP.setIdFolio((String) vRowTemp.elementAt(0));
                oEM.setInicio((Date) vRowTemp.elementAt(1));
                oEM.setAlta((Date) vRowTemp.elementAt(2));
                oOpeCC.setMonto(((Double) vRowTemp.elementAt(3)).floatValue());
                oOpeCaja.setFolio(((Double) vRowTemp.elementAt(4)).intValue());
                oPac.setNombre((String) vRowTemp.elementAt(5));
                oPac.setApellidoPaterno((String) vRowTemp.elementAt(6));
                oPac.setApellidoMaterno((String) vRowTemp.elementAt(7));
                
                oSP.setPaciente(oPac);
                oSP.setEpisodioMedico(oEM);
                oOpeCC.setServPrest(oSP);
                oOpeCC.setOpeCaja(oOpeCaja);
                
                listRet.add(oOpeCC);
            }
        }
        return listRet;
    }
    
    public List<OpeCajaConcepto> buscaRentas() throws Exception{
        List<OpeCajaConcepto> listRet=null;
        OpeCajaConcepto oOpeCC;
        OperacionCaja oOpeCaja;
        ServicioPrestado oSP;
        Paciente oPac;
        
        Vector rst;
        String sQuery = "";
        
        sQuery = "select * from buscaoprenta();";
        System.out.println(sQuery);
        if (getAD() == null){
            setAD(new AccesoDatos());
            getAD().conectar();
        }
        rst = getAD().ejecutarConsulta(sQuery);
        getAD().desconectar();
        setAD(null);
        
        if (rst != null) {
            listRet = new ArrayList();
            for (int i = 0; i < rst.size(); i++) {
                Vector vRowTemp = (Vector)rst.elementAt(i);
                oOpeCC=new OpeCajaConcepto();
                oOpeCaja=new OperacionCaja();
                oSP=new ServicioPrestado();
                oPac=new Paciente();
                
                oOpeCaja.setFolio(((Double) vRowTemp.elementAt(0)).intValue());
                oOpeCaja.setFechaOp((Date) vRowTemp.elementAt(1));
                oOpeCC.setMonto(((Double) vRowTemp.elementAt(2)).floatValue());
                //Pendiente manejo de Clientes, por el momento se guarda el nombre en el objeto paciente
                oPac.setNombre((String) vRowTemp.elementAt(3));
                
                oSP.setPaciente(oPac);
                oOpeCC.setServPrest(oSP);
                oOpeCC.setOpeCaja(oOpeCaja);
                
                listRet.add(oOpeCC);
            }
        }
        return listRet;
    }
    
    public String marcarNoFact(OperacionCaja oOpeCaja)throws Exception{
        Vector rst;
        String mess="", sQuery = "";
        
        sQuery = "select * from marcarNoFact("+oOpeCaja.getFolio()+", '"+oOpeCaja.getQuienAutoriza()+"', '"+oOpeCaja.getMotivo()+"');";
        
        if (getAD() == null){
            setAD(new AccesoDatos());
            getAD().conectar();
        }
        rst = getAD().ejecutarConsulta(sQuery);
        getAD().desconectar();
        setAD(null);
        
        if (rst != null) {
            mess=""+rst.get(0);
        }
        
        return mess.substring(1, mess.length()-1);
    }

    public OperacionCaja getOpeCaja() {
        return oOpeCaja;
    }

    public void setOpeCaja(OperacionCaja oOpeCaja) {
        this.oOpeCaja = oOpeCaja;
    }

    public int getSec() {
        return nSec;
    }

    public void setSec(int nSec) {
        this.nSec = nSec;
    }

    public ConceptoEgreso getConcepEgr() {
        return oConcepEgr;
    }

    public void setConcepEgr(ConceptoEgreso oConcepEgr) {
        this.oConcepEgr = oConcepEgr;
    }

    public ConceptoIngreso getConcepIngr() {
        return oConcepIngr;
    }

    public void setConcepIngr(ConceptoIngreso oConcepIngr) {
        this.oConcepIngr = oConcepIngr;
    }

    public ServicioPrestado getServPrest() {
        return oServPrest;
    }

    public void setServPrest(ServicioPrestado oServPrest) {
        this.oServPrest = oServPrest;
    }

    public float getMonto() {
        return nMonto;
    }

    public void setMonto(float nMonto) {
        this.nMonto = nMonto;
    }

    public String getNombreSerie() {
        return sNombreSerie;
    }

    public void setNombreSerie(String sNombreSerie) {
        this.sNombreSerie = sNombreSerie;
    }

    public int getFolioCFDI() {
        return nFolioCFDI;
    }

    public void setFolioCFDI(int nFolioCFDI) {
        this.nFolioCFDI = nFolioCFDI;
    }
    
    public AccesoDatos getAD() {
        return oAD;
    }
    public void setAD(AccesoDatos oAD) {
        this.oAD = oAD;
    }
}
