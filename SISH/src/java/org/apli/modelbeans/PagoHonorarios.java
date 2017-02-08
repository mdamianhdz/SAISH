/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apli.modelbeans;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import org.apli.AD.AccesoDatos;

/**
 *
 * @author HumbertMarin
 */
public class PagoHonorarios implements Serializable{

    private int nidpagohonorarios;
    private PersonalHospitalario oPersonalHospitalario;
    private int nFolioPers;
    private Date dRegistro;
    private Date dAutoriz;
    private Date dImpresion;
    private Date dPago;
    private String sSituacion;
    private int nfolio;
    /**
     * Variable para el acceso a datos
     */
    protected AccesoDatos oAD = null;
    public String sMonto;
    public Valida oValida;

    public PagoHonorarios() {
    }

    /**
     * Agrega un pago de honorarios a la base de datos Regresa el número de
     * registros afectados (insertados)
     *
     */
    public int insertar() throws Exception {
        Vector nRet = null;
        String sQuery = "";
        String msj = "";
        if (this.oPersonalHospitalario.getFolioPers() == 0 || this.dRegistro == null) {
            throw new Exception("Pago Honorarios.insertar: error de programación, faltan datos");
        } else {
            SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sQuery = " SELECT insertapagohonorarios(" + this.oPersonalHospitalario.getFolioPers() + ",'" + this.dRegistro + "','" + this.sSituacion + "')";
            System.out.println(sQuery);
            /*  Si oAD es nulo, tiene que crearlo y conectarlo, de otro modo
             supone que ya viene conectado*/
            if (getAD() == null) {
                setAD(new AccesoDatos());
                getAD().conectar();
                nRet = getAD().ejecutarConsulta(sQuery);
                getAD().desconectar();
                setAD(null);
            } else {
                nRet = getAD().ejecutarConsulta(sQuery);
            }
            msj = "" + nRet.get(0);
            msj = msj.substring(1, msj.length() - 3);
        }
        return Integer.parseInt(msj);
    }

    public String actualizaSituacionPagoHonorarios(int idPagoHonorarios, int folio) throws Exception{
        Vector rst= null;
        String sQuery="";
        
        sSituacion="2";
        
        
        sQuery="select * from actualizasituacionpagohonorarios("+idPagoHonorarios+",'"+sSituacion+"',"+folio+");";
        if(getAD()==null){
            setAD(new AccesoDatos());
            getAD().conectar();
            rst=getAD().ejecutarConsulta(sQuery);
            getAD().desconectar();
            setAD(null);
        }
        return ""+(rst==null?"":rst.get(0));
    }
    
    public String autorizarPagoHonorarios() throws Exception {
        Vector rst = null;
        String sQuery = "";

        if (this.nidpagohonorarios == 0 && this.oPersonalHospitalario.getFolioPers() == 0 && this.dAutoriz == null) {
            throw new Exception("PagoHonorarios.autorizarPagoHonorarios: error de programación, faltan datos");
        } else {
            SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sQuery = "SELECT * FROM autorizarPagoHonorarios(" + this.nidpagohonorarios + "," + this.oPersonalHospitalario.getFolioPers() + ",'" + this.dAutoriz + "')";
            System.out.println(sQuery);
            if (getAD() == null) {
                setAD(new AccesoDatos());
                getAD().conectar();
                rst = getAD().ejecutarConsulta(sQuery);
                getAD().desconectar();
                setAD(null);
            }
        }
        return " " + (rst==null?"":rst.get(0));
    }

    public String calculaMontoPagos(int nid) throws Exception {
        Vector rst = null;
        String sQuery = "";

        sQuery = "select * from calculamontohonorarios(" + nid + ")";
        if (getAD() == null) {
            setAD(new AccesoDatos());
            getAD().conectar();
            rst = getAD().ejecutarConsulta(sQuery);
            getAD().desconectar();
            setAD(null);
        }
        return "" + (rst==null?"":rst.get(0));
    }

    public List<PagoHonorarios> todosPagosAutorizados() throws Exception {

        List<PagoHonorarios> listRet = new ArrayList<PagoHonorarios>();
        PagoHonorarios oPH;
        Vector rst = null;
        String sQuery = "";

        sQuery = "select * from buscapagohonorariospendientes();";

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
            listRet = new ArrayList();
            for (int i = 0; i < rst.size(); i++) {
                oPH = new PagoHonorarios();
                Vector vRowTemp = (Vector) rst.elementAt(i);
                oPH.setIdPagoHonorarios(((Double) vRowTemp.elementAt(0)).intValue());
                String monto=calculaMontoPagos(oPH.getIdPagoHonorarios());
                String montoRet=monto.substring(1, monto.length() - 1);
                oPH.setMonto(montoRet);
                oPersonalHospitalario = new PersonalHospitalario();
                oPH.setPersonalHospitalario(oPersonalHospitalario.buscaDatosPersonal(((Double) vRowTemp.elementAt(1)).intValue()));


                listRet.add(oPH);
            }
        }
        return listRet;
    }

    //=============== SET & GET ===============//
    public int getIdPagoHonorarios() {
        return nidpagohonorarios;
    }

    public void setIdPagoHonorarios(int nidpagohonorarios) {
        this.nidpagohonorarios = nidpagohonorarios;
    }

    public PersonalHospitalario getPersonalHospitalario() {
        return oPersonalHospitalario;
    }

    public void setPersonalHospitalario(PersonalHospitalario oPersonalHospitalario) {
        this.oPersonalHospitalario = oPersonalHospitalario;
    }

    public int getFolioPers() {
        return nFolioPers;
    }

    public void setFolioPers(int nFolioPers) {
        this.nFolioPers = nFolioPers;
    }

    public Date getRegistro() {
        return dRegistro;
    }

    public void setRegistro(Date dRegistro) {
        this.dRegistro = dRegistro;
    }

    public Date getAutoriz() {
        return dAutoriz;
    }

    public void setAutoriz(Date dAutoriz) {
        this.dAutoriz = dAutoriz;
    }

    public Date getImpresion() {
        return dImpresion;
    }

    public void setImpresion(Date dImpresion) {
        this.dImpresion = dImpresion;
    }

    public Date getPago() {
        return dPago;
    }

    public void setPago(Date dPago) {
        this.dPago = dPago;
    }

    public String getSituacion() {
        return sSituacion;
    }

    public void setSituacion(String sSituacion) {
        this.sSituacion = sSituacion;
    }

    public int getNfolio() {
        return nfolio;
    }

    public void setNfolio(int nfolio) {
        this.nfolio = nfolio;
    }

    public AccesoDatos getAD() {
        return oAD;
    }

    public void setAD(AccesoDatos oAD) {
        this.oAD = oAD;
    }

    /**
     * @return the sMonto
     */
    public String getMonto() {
        return sMonto;
    }

    /**
     * @param sMonto the sMonto to set
     */
    public void setMonto(String sMonto) {
        this.sMonto = sMonto;
    }
}
