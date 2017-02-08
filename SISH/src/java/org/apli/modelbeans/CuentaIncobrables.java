/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apli.modelbeans;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author ERJI
 */
public class CuentaIncobrables implements Serializable {
    
     private String sEmpresa;
     private Date dFechaFactura;
     private String sID;
     private String sFolio;
     private String sContraRecibo;
     private Date dFechaContrarecibo;
     
     private String sFolioServExt;
     private String sFolioHosp;
     private Date dFechaAtencion;
     
     private String sRazonSocial;
     private String sPaciente;
     private float fImporte;
     private Date dFechaEsperada;
     private String sObservaciones;

    public String getEmpresa() {
        return sEmpresa;
    }

    public void setEmpresa(String sEmpresa) {
        this.sEmpresa = sEmpresa;
    }

    public Date getFechaFactura() {
        return dFechaFactura;
    }

    public void setFechaFactura(Date dFechaFactura) {
        this.dFechaFactura = dFechaFactura;
    }

    public String getFolio() {
        return sFolio;
    }

    public void setFolio(String sFolio) {
        this.sFolio = sFolio;
    }

    public String getRazonSocial() {
        return sRazonSocial;
    }

    public void setRazonSocial(String sRazonSocial) {
        this.sRazonSocial = sRazonSocial;
    }

    public String getPaciente() {
        return sPaciente;
    }

    public void setPaciente(String sPaciente) {
        this.sPaciente = sPaciente;
    }

    public float getImporte() {
        return fImporte;
    }

    public void setImporte(float fImporte) {
        this.fImporte = fImporte;
    }

    public String getContraRecibo() {
        return sContraRecibo;
    }

    public void setContraRecibo(String sContraRecibo) {
        this.sContraRecibo = sContraRecibo;
    }

    public Date getFechaContrarecibo() {
        return dFechaContrarecibo;
    }

    public void setFechaContrarecibo(Date dFechaContrarecibo) {
        this.dFechaContrarecibo = dFechaContrarecibo;
    }

    public Date getFechaEsperada() {
        return dFechaEsperada;
    }

    public void setFechaEsperada(Date dFechaEsperada) {
        this.dFechaEsperada = dFechaEsperada;
    }

    public String getObservaciones() {
        return sObservaciones;
    }

    public void setObservaciones(String sObservaciones) {
        this.sObservaciones = sObservaciones;
    }

    public String getFolioServExt() {
        return sFolioServExt;
    }

    public void setFolioServExt(String sFolioServExt) {
        this.sFolioServExt = sFolioServExt;
    }

    public String getFolioHosp() {
        return sFolioHosp;
    }

    public void setFolioHosp(String sFolioHosp) {
        this.sFolioHosp = sFolioHosp;
    }

    public Date getFechaAtencion() {
        return dFechaAtencion;
    }

    public void setFechaAtencion(Date dFechaAtencion) {
        this.dFechaAtencion = dFechaAtencion;
    }

    public String getID() {
        return sID;
    }

    public void setID(String sID) {
        this.sID = sID;
    }
    
    
     
     
    
}
