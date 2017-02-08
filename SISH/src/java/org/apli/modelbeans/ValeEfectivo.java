/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apli.modelbeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import org.apli.AD.AccesoDatos;
import org.apache.log4j.Logger;

/**
 *
 * @author Manuel
 */
public class ValeEfectivo implements Serializable {

    public int nIdVale;
    public int nFolioPers;
    public int nFolio;
    public String sRazonCanc;
    public String sSituacion;
    public AccesoDatos oAD;
    public OperacionCaja oOperacionCaja;
    public OpeCajaConcepto oOpeCajaConcep;
    public PersonalHospitalario oPersonalHospitalario;
    private Valida oValida;
    private static final Logger LOG = Logger.getLogger(OperacionCaja.class.getName());

    public ValeEfectivo() {
    }

    public List<ValeEfectivo> todosIdValeEfectivo() throws Exception{
        List<ValeEfectivo> listVE= null;
        ValeEfectivo oVE;
        
        Vector rst= null;
        String sQuery="";
        
        sQuery="select * from buscatodosidvaleefectivo()";
        
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
            listVE = new ArrayList();
            for (int i = 0; i < rst.size(); i++) {
                oVE = new ValeEfectivo();
                Vector vRowTemp = (Vector) rst.elementAt(i);
                oVE.setIdVale(((Double)vRowTemp.elementAt(0)).intValue());       
                listVE.add(oVE);
            }
        }
        return listVE;
    }

    public ValeEfectivo buscaValeEfectivo(int pnidvale)throws Exception{
        ValeEfectivo oVE=null;
        Vector rst=null;
        OperacionCaja oOPeCaj=null;
        OpeCajaConcepto oOpeCajConcep=null;
        String sQuery = "";
        
        sQuery = "select * from buscavaleefectivo("+ pnidvale +")";
        LOG.trace(new StringBuilder().append("Query: [").append(sQuery).append("]").toString());

        if (getAD() == null) {
            setAD(new AccesoDatos());
            getAD().conectar();
        }
        rst = getAD().ejecutarConsulta(sQuery);
        getAD().desconectar();
        setAD(null);
        if (rst != null) {
            for (int i = 0; i < rst.size(); i++) {
                Vector vRowTemp = (Vector) rst.elementAt(i);

                oVE = new ValeEfectivo();
                oValida= new Valida();
                oVE.setIdVale(((Double) vRowTemp.elementAt(0)).intValue());
                oVE.setFolioPers(((Double) vRowTemp.elementAt(1)).intValue());
                oPersonalHospitalario = new PersonalHospitalario();
                oVE.setPersonalHospitalario(oPersonalHospitalario.buscaDatosPersonal(((Double)vRowTemp.elementAt(1)).intValue()));
                oVE.setFolio(((Double) vRowTemp.elementAt(2)).intValue());
                oVE.setRazonCanc((String) vRowTemp.elementAt(3));
                oVE.setSituacion(oValida.validaSituacion((String) vRowTemp.elementAt(4)));
                oOPeCaj = new OperacionCaja();
                oOPeCaj.setFechaOp((Date) vRowTemp.elementAt(5));
                oVE.setOperacionCaja(oOPeCaj);
                oOpeCajConcep = new OpeCajaConcepto();
                oOpeCajConcep.setMonto(((Double) vRowTemp.elementAt(6)).floatValue());
                oVE.setOpeCajaConcep(oOpeCajConcep);
            }
        }
        return oVE;
        
        
    }
    
    public List<ValeEfectivo> buscaValeEfectivo(int pnfoliopers, Date pdini, Date pdfin) throws Exception {
        ValeEfectivo oVE = null;
        List<ValeEfectivo> listRet = null;
        Vector rst = null;
        OperacionCaja oOPeCaj = null;
        OpeCajaConcepto oOpeCajConcep = null;
        String sQuery = "";

        //Puesto que la fecha fin se usa como rango, hay que agregar 1 día
        if (pdfin != null){
            Calendar c = Calendar.getInstance(); 
            c.setTime(pdfin); 
            c.add(Calendar.DATE, 1);
            pdfin = c.getTime();
        }
            
        sQuery = "select * from buscavaleefectivofiltro(" + pnfoliopers + "," +
                (pdini==null?"null":"'"+pdini+"'") + "," + 
                (pdfin==null?"null":"'"+pdfin+"'") + ")";
        LOG.trace(new StringBuilder().append("Query: [").append(sQuery).append("]").toString());

        if (getAD() == null) {
            setAD(new AccesoDatos());
            getAD().conectar();
        }
        rst = getAD().ejecutarConsulta(sQuery);
        getAD().desconectar();
        setAD(null);
        if (rst != null) {
            listRet = new ArrayList();
            for (int i = 0; i < rst.size(); i++) {
                Vector vRowTemp = (Vector) rst.elementAt(i);

                oVE = new ValeEfectivo();
                oValida= new Valida();
                oVE.setIdVale(((Double) vRowTemp.elementAt(0)).intValue());
                oVE.setFolioPers(((Double) vRowTemp.elementAt(1)).intValue());
                oPersonalHospitalario = new PersonalHospitalario();
            //    oPerHosp.setFolioPers(((Double) vRowTemp.elementAt(1)).intValue());
            //    oPerHosp.buscaDatosPersonal((((Double)vRowTemp.elementAt(1)).intValue()));
                oVE.setPersonalHospitalario(oPersonalHospitalario.buscaDatosPersonal(((Double)vRowTemp.elementAt(1)).intValue()));
                oVE.setFolio(((Double) vRowTemp.elementAt(2)).intValue());
                oVE.setRazonCanc((String) vRowTemp.elementAt(3));
                oVE.setSituacion(oValida.validaSituacion((String) vRowTemp.elementAt(4)));
                oOPeCaj = new OperacionCaja();
                oOPeCaj.setFechaOp((Date) vRowTemp.elementAt(5));
                oVE.setOperacionCaja(oOPeCaj);
                oOpeCajConcep = new OpeCajaConcepto();
                oOpeCajConcep.setMonto(((Double) vRowTemp.elementAt(6)).floatValue());
                oVE.setOpeCajaConcep(oOpeCajConcep);
                listRet.add(oVE);
            }
        }
        return listRet;
    }

    public String cancelaValeEfectivo(int nidvale, String srazoncanc) throws Exception {
        Vector rst;
        String msj = "";
        String sQuery = "";

        sQuery = "select * from cancelavaleefectivo(" + nidvale + ",'" + srazoncanc + "')";

        if (getAD() == null) {
            setAD(new AccesoDatos());
            getAD().conectar();
        }
        rst = getAD().ejecutarConsulta(sQuery);
        getAD().desconectar();
        setAD(null);

        if (rst != null) {
            msj = "" + rst.get(0);
        }
        System.out.print(msj);
        return msj.substring(1, msj.length() - 1);

    }

    public void insertarValeEfectivo(int nfolioper, String nfolio) throws Exception {
        Vector rst = null;
        String srazoncanc = "   ";
        String ssituacion = "E";

        String sQuery = "select * from insertavaleefectivo(" + nfolioper + ",'" + nfolio + "','" + srazoncanc + "','" + ssituacion + "')";

        if (getAD() == null) {
            setAD(new AccesoDatos());
            getAD().conectar();
            rst = getAD().ejecutarConsulta(sQuery);
            getAD().desconectar();
            setAD(null);
        }
    }


    /**
     * @return the oAD
     */
    public AccesoDatos getAD() {
        return oAD;
    }

    /**
     * @param oAD the oAD to set
     */
    public void setAD(AccesoDatos oAD) {
        this.oAD = oAD;
    }

    /**
     * @return the oOperacionCaja
     */
    public OperacionCaja getOperacionCaja() {
        return oOperacionCaja;
    }

    /**
     * @param oOperacionCaja the oOperacionCaja to set
     */
    public void setOperacionCaja(OperacionCaja oOperacionCaja) {
        this.oOperacionCaja = oOperacionCaja;
    }

    /**
     * @return the oOpeCajaConcep
     */
    public OpeCajaConcepto getOpeCajaConcep() {
        return oOpeCajaConcep;
    }

    /**
     * @param oOpeCajaConcep the oOpeCajaConcep to set
     */
    public void setOpeCajaConcep(OpeCajaConcepto oOpeCajaConcep) {
        this.oOpeCajaConcep = oOpeCajaConcep;
    }

    /**
     * @return the oPersonalHospitalario
     */
    public PersonalHospitalario getPersonalHospitalario() {
        return oPersonalHospitalario;
    }

    /**
     * @param oPersonalHospitalario the oPersonalHospitalario to set
     */
    public void setPersonalHospitalario(PersonalHospitalario oPersonalHospitalario) {
        this.oPersonalHospitalario = oPersonalHospitalario;
    }

    /**
     * @return the nIdVale
     */
    public int getIdVale() {
        return nIdVale;
    }

    /**
     * @param nIdVale the nIdVale to set
     */
    public void setIdVale(int nIdVale) {
        this.nIdVale = nIdVale;
    }

    /**
     * @return the nFolioPers
     */
    public int genFolioPers() {
        return nFolioPers;
    }

    /**
     * @param nFolioPers the nFolioPers to set
     */
    public void setFolioPers(int nFolioPers) {
        this.nFolioPers = nFolioPers;
    }

    /**
     * @return the nFolio
     */
    public int getFolio() {
        return nFolio;
    }

    /**
     * @param nFolio the nFolio to set
     */
    public void setFolio(int nFolio) {
        this.nFolio = nFolio;
    }

    /**
     * @return the sRazonCanc
     */
    public String getRazonCanc() {
        return sRazonCanc;
    }

    /**
     * @param sRazonCanc the sRazonCanc to set
     */
    public void setRazonCanc(String sRazonCanc) {
        this.sRazonCanc = sRazonCanc;
    }

    /**
     * @return the sSituacion
     */
    public String getSituacion() {
        return sSituacion;
    }

    /**
     * @param sSituacion the sSituacion to set
     */
    public void setSituacion(String sSituacion) {
        this.sSituacion = sSituacion;
    }
}
