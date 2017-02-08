/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apli.modelbeans.rentas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import org.apli.AD.AccesoDatos;
import org.apli.modelbeans.facturacion.cfdi.v32.schema.Comprobante.Receptor;

/**
 *
 * @author Lily_LnBnd
 */
public class ContratoRenta implements Serializable {

    private int nIdContratoRenta;
    private Receptor oReceptor;
    private float nRentaMensual;
    private float nDeposito;
    private Date dInicio;
    private Date dFin;
    private boolean bCancelado;
    private String sRazonCancelado;
    private List<PagoRenta> listPagos;
    private AccesoDatos oAD;
    public PagoRenta oPagoRenta;
    public EspacioRentable oEspacioRent;

    public ContratoRenta() {
        oReceptor = new Receptor();
        listPagos = new ArrayList();
    }

    public ContratoRenta calculaPagosContrato(ContratoRenta oCR) {
        oCR.setListPagos(new ArrayList());
        PagoRenta oPR = new PagoRenta();
        int nNumPago = 0;
        Date dFechaPago = oCR.getInicio();

        oPR.setMensualidad(nNumPago);
        oPR.setProgramada(dFechaPago);
        oCR.getListPagos().add(oPR);
        while (dFechaPago.compareTo(oCR.getFin()) < 0) {
            nNumPago = nNumPago + 1;
            oPR = new PagoRenta();
            oPR.setMensualidad(nNumPago);
            oPR.setProgramada(dFechaPago);
            oCR.getListPagos().add(oPR);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dFechaPago);
            calendar.add(Calendar.MONTH, 1);
            dFechaPago = calendar.getTime();

        }
        return oCR;
    }

    public String buscaAlertas(int nIdContrato) throws Exception {
        Vector rst = null;
        String sQuery = "";

        if (nIdContrato != 0) {
            sQuery = "select * from buscaAlertas(" + nIdContrato + ");";

            System.out.println(sQuery);
            if (getAD() == null) {
                setAD(new AccesoDatos());
                getAD().conectar();
                rst = getAD().ejecutarConsulta(sQuery);
                getAD().desconectar();
                setAD(null);
            } else {
                rst = getAD().ejecutarConsulta(sQuery);
            }
        }
        if (rst != null) {
            return "" + rst.get(0).toString().substring(1, rst.get(0).toString().length() - 1);
        } else {
            return "";
        }
    }

    public List<ContratoRenta> buscaPagosRenta(String prfcarrendatario) throws Exception {
        ContratoRenta oContRent = null;
        PagoRenta oPago = null;
        Receptor oRecep = null;
        EspacioRentable oEspRent=null;
        List<ContratoRenta> listRet = null;
        Vector rst = null;
        String sQuery = "";

        sQuery = "select * from buscapagosrenta2('" + prfcarrendatario + "');";

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
                oContRent= new ContratoRenta();
                oContRent.setIdContratoRenta(((Double) vRowTemp.elementAt(0)).intValue());
                oRecep = new Receptor();
                oRecep.setRfc((String) vRowTemp.elementAt(1));
                // oContRent.oReceptor.setRfc((String) vRowTemp.elementAt(1));
                //Lo que trae como renta mensual es lo que debe pagar esa mensualidad
                oContRent.setRentaMensual(((Double) vRowTemp.elementAt(2)).floatValue());
                oContRent.setInicio((Date) vRowTemp.elementAt(3));
                oContRent.setFin((Date) vRowTemp.elementAt(4));
                oRecep.setNombre((String) vRowTemp.elementAt(5));
                // oContRent.oReceptor.setNombre((String) vRowTemp.elementAt(5));
                oContRent.setReceptor(oRecep);
                oPago = new PagoRenta();
                oPago.setMensualidad(((Double) vRowTemp.elementAt(6)).intValue());
                oPago.setProgramada((Date) vRowTemp.elementAt(7));
                oPago.setTotalM(((Double) vRowTemp.elementAt(11)).intValue()-1);
                oContRent.setPagoRenta(oPago);
          //    oContRent.oPagoRenta.setMensualidad(((Double) vRowTemp.elementAt(6)).intValue());
         //     oContRent.oPagoRenta.setProgramada((Date) vRowTemp.elementAt(7));
                oEspRent= new EspacioRentable();
                oEspRent.setDescripcion((String)vRowTemp.elementAt(9));
                oContRent.setEspacioRent(oEspRent);
                oContRent.setDeposito(((Double)vRowTemp.elementAt(10)).floatValue());
                listRet.add(oContRent);
            }
        }
        return listRet;
    }

    public int getIdContratoRenta() {
        return nIdContratoRenta;
    }

    public void setIdContratoRenta(int nIdContratoRenta) {
        this.nIdContratoRenta = nIdContratoRenta;
    }

    public Receptor getReceptor() {
        return oReceptor;
    }

    public void setReceptor(Receptor oReceptor) {
        this.oReceptor = oReceptor;
    }

    public float getRentaMensual() {
        return nRentaMensual;
    }

    public void setRentaMensual(float nRentaMensual) {
        this.nRentaMensual = nRentaMensual;
    }

    public float getDeposito() {
        return nDeposito;
    }

    public void setDeposito(float nDeposito) {
        this.nDeposito = nDeposito;
    }

    public Date getInicio() {
        return dInicio;
    }

    public void setInicio(Date dInicio) {
        this.dInicio = dInicio;
    }

    public Date getFin() {
        return dFin;
    }

    public void setFin(Date dFin) {
        this.dFin = dFin;
    }

    public boolean isCancelado() {
        return bCancelado;
    }

    public void setCancelado(boolean bCancelado) {
        this.bCancelado = bCancelado;
    }

    public String getRazonCancelado() {
        return sRazonCancelado;
    }

    public void setRazonCancelado(String sRazonCancelado) {
        this.sRazonCancelado = sRazonCancelado;
    }

    public List<PagoRenta> getListPagos() {
        return listPagos;
    }

    public void setListPagos(List<PagoRenta> listPagos) {
        this.listPagos = listPagos;
    }

    public AccesoDatos getAD() {
        return oAD;
    }

    public void setAD(AccesoDatos oAD) {
        this.oAD = oAD;
    }

    /**
     * @return the oPagoRenta
     */
    public PagoRenta getPagoRenta() {
        return oPagoRenta;
    }

    /**
     * @param oPagoRenta the oPagoRenta to set
     */
    public void setPagoRenta(PagoRenta oPagoRenta) {
        this.oPagoRenta = oPagoRenta;
    }

    /**
     * @return the oEspacioRent
     */
    public EspacioRentable getEspacioRent() {
        return oEspacioRent;
    }

    /**
     * @param oEspacioRent the oEspacioRent to set
     */
    public void setEspacioRent(EspacioRentable oEspacioRent) {
        this.oEspacioRent = oEspacioRent;
    }
}
