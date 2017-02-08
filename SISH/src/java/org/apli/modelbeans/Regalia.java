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
 * Representa los pagos extra (regalias) cubiertas a médicos y enfermeras
 *
 * @author BAOZ
 * @version 1.0
 * @created 30-jul.-2014 01:05:54 p. m.
 */
public class Regalia implements Serializable {

    /**
     * Conjunto de servicios prestados que dieron origen al pago de regalías
     */
    private ServicioPrestado arrServiciosConsiderados;
    /**
     * Fecha de autorización
     */
    private Date dAutoriz;
    /**
     * Fecha final del rango al que corresponde el pago
     */
    private Date dFin;
    /**
     * Fecha inicial del rango al que corresponde el pago
     */
    private Date dIni;
    /**
     * Fecha en que se realiza el pago
     */
    private Date dPago;
    /**
     * Fecha en que se programa el pago
     */
    private Date dProgPago;
    /**
     * Monto del pago extra (regalía)
     */
    public float nMonto;
    /**
     * Personal (médico, enfermera) al que se le hace el pago
     */
    public PersonalHospitalario oPersonal;
    /**
     * Situación del pago 0 = autorizado 1= pago programado 2= pagado
     */
    public String sSituacion;
    public int nIdPagoRegalias;
    public AccesoDatos oAD = null;

    public Regalia() {
    }

    public String actualizaSituacionRegalias(int nFolioRegalia, String sFolioCaja) throws Exception {
        Vector rst;
        String msj = "";
        String sQuery = "";
        String sSituacion2 = "2";
        sQuery = "select * from actualizaSituacionPagoRegalia(" + nFolioRegalia + ",'" 
                + sSituacion2 + "',"+sFolioCaja+");";

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

    public List<Regalia> pagosPendientes() throws Exception {

        List<Regalia> listR = null;
        Regalia oReg;
        PersonalHospitalario oPH;

        Vector rst = null;
        String sQuery = "";

        sQuery = "select * from buscapagosextraspendientes();";

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
            listR = new ArrayList();
            for (int i = 0; i < rst.size(); i++) {
                oPH = new PersonalHospitalario();
                oReg = new Regalia();
                Vector vRowTemp = (Vector) rst.elementAt(i);
                oReg.setIdPagoRegalias(((Double) vRowTemp.elementAt(0)).intValue());
                oPH = oPH.buscaDatosPersonal(((Double) vRowTemp.elementAt(1)).intValue());
                oReg.setPersonal(oPH);
                oReg.setMonto(((Double) vRowTemp.elementAt(2)).floatValue());
                oReg.setSituacion((String) vRowTemp.elementAt(3));

                listR.add(oReg);
            }
        }
        return listR;
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
     * @return the nMonto
     */
    public float getMonto() {
        return nMonto;
    }

    /**
     * @param nMonto the nMonto to set
     */
    public void setMonto(float nMonto) {
        this.nMonto = nMonto;
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

    /**
     * @return the oPersonal
     */
    public PersonalHospitalario getPersonal() {
        return oPersonal;
    }

    /**
     * @param oPersonal the oPersonal to set
     */
    public void setPersonal(PersonalHospitalario oPersonal) {
        this.oPersonal = oPersonal;
    }

    /**
     * @return the nIdPagoRegalias
     */
    public int getIdPagoRegalias() {
        return nIdPagoRegalias;
    }

    /**
     * @param nIdPagoRegalias the nIdPagoRegalias to set
     */
    public void setIdPagoRegalias(int nIdPagoRegalias) {
        this.nIdPagoRegalias = nIdPagoRegalias;
    }
}