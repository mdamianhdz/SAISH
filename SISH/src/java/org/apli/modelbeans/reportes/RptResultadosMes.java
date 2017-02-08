package org.apli.modelbeans.reportes;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import org.apli.AD.AccesoDatos;

/**
 * Clase que representa la información del reporte de resultados del mes
 * @author BAOZ
 */
public class RptResultadosMes implements Serializable{
private Date dIni;
private Date dFin;
private float nVtasContExt;
private float nVtasContHosp;
private float nVtasCredExt;
private float nVtasCredHosp;
private float nPagosServExt;
private float nAnticipos;
private float nAbonos;
private float nFiniquitos;
private float nMontoPaqMaternidad;
private float nMontoPaqPediatricos;
private float nMontoOtrosPaq;
private float nMontoRA;
private float nMontoPrestamos;
private float nMontoRentas;
private float nMontoOtrosIngr;
private float nMontoEgreCaja;
private float nMontoEgreChq;
private float nMontoPendFact;
private float nMontoPendNoFact;
private float nMontoFactRealizada;
private float nMontoFactEsperada;
private float nMontoFactEmpresas;
private float nMontoFactParticulares;
private float nMontoFactDia;
private float nMontoFactCobMes;
private float nMontoFactCobAnteriores;
private long nCantPaqMaternidad;
private long nCantPaqPediatricos;
private long nCantPaqOtros;
private long nCantHabParticulares;
private long nCantHabEmpresa;
private long nCantAmbulatoriosParticulares;
private long nCantAmbulatoriosEmpresa;
private long nCantUrgenciasParticulares;
private long nCantUrgenciasEmpresas;
private AccesoDatos oAD;

    public List<RptResultadosMes> buscarRpt() throws Exception{
    Vector rst = null;
    String sQuery = "";
    RptResultadosMes oDesglose;
    List<RptResultadosMes> arrRet = null;
    SimpleDateFormat fmtTxt = new SimpleDateFormat("yyyy-MM-dd");
    if (this.dIni==null || this.dFin == null)
            throw new Exception("RptDesgloseServiciosPrestados.buscarRpt: faltan datos");
        else{
            sQuery="select * from buscaRptResultadosRango('"+
                    fmtTxt.format(dIni)+"', '"+fmtTxt.format(dFin)+"')";     
            if (oAD == null){
                oAD = new AccesoDatos();
                oAD.conectar();
                rst = oAD.ejecutarConsulta(sQuery);
                oAD.desconectar();
                oAD=null;
            }else{
                rst = oAD.ejecutarConsulta(sQuery);
            }
            if (rst != null){
                System.out.println("Encontrados "+rst.size());
                arrRet = new ArrayList<RptResultadosMes>();
                for (int i = 0; i < rst.size(); i++) {
                    oDesglose=new RptResultadosMes();
                    Vector vRowTemp = (Vector)rst.elementAt(i);
                    oDesglose.nVtasContExt = ((Double)vRowTemp.elementAt(0)).floatValue();
                    oDesglose.nVtasContHosp= ((Double)vRowTemp.elementAt(1)).floatValue();
                    oDesglose.nVtasCredExt= ((Double)vRowTemp.elementAt(2)).floatValue();
                    oDesglose.nVtasCredHosp= ((Double)vRowTemp.elementAt(3)).floatValue();
                    oDesglose.nPagosServExt= ((Double)vRowTemp.elementAt(4)).floatValue();
                    oDesglose.nAnticipos= ((Double)vRowTemp.elementAt(5)).floatValue();
                    oDesglose.nAbonos= ((Double)vRowTemp.elementAt(6)).floatValue();
                    oDesglose.nFiniquitos= ((Double)vRowTemp.elementAt(7)).floatValue();
                    oDesglose.nMontoPaqMaternidad= ((Double)vRowTemp.elementAt(8)).floatValue();
                    oDesglose.nMontoPaqPediatricos= ((Double)vRowTemp.elementAt(9)).floatValue();
                    oDesglose.nMontoOtrosPaq= ((Double)vRowTemp.elementAt(10)).floatValue();
                    oDesglose.nMontoRA= ((Double)vRowTemp.elementAt(11)).floatValue();
                    oDesglose.nMontoPrestamos= ((Double)vRowTemp.elementAt(12)).floatValue();
                    oDesglose.nMontoRentas= ((Double)vRowTemp.elementAt(13)).floatValue();
                    oDesglose.nMontoOtrosIngr= ((Double)vRowTemp.elementAt(14)).floatValue();
                    oDesglose.nMontoEgreCaja= ((Double)vRowTemp.elementAt(15)).floatValue();
                    oDesglose.nMontoEgreChq= ((Double)vRowTemp.elementAt(16)).floatValue();
                    oDesglose.nMontoPendFact= ((Double)vRowTemp.elementAt(17)).floatValue();
                    oDesglose.nMontoPendNoFact= ((Double)vRowTemp.elementAt(18)).floatValue();
                    oDesglose.nMontoFactRealizada= ((Double)vRowTemp.elementAt(19)).floatValue();
                    oDesglose.nMontoFactEsperada= ((Double)vRowTemp.elementAt(20)).floatValue();
                    oDesglose.nMontoFactEmpresas= ((Double)vRowTemp.elementAt(21)).floatValue();
                    oDesglose.nMontoFactParticulares= ((Double)vRowTemp.elementAt(22)).floatValue();
                    oDesglose.nMontoFactDia= ((Double)vRowTemp.elementAt(23)).floatValue();
                    oDesglose.nMontoFactCobMes= ((Double)vRowTemp.elementAt(24)).floatValue();
                    oDesglose.nMontoFactCobAnteriores= ((Double)vRowTemp.elementAt(25)).floatValue();
                    oDesglose.nCantPaqMaternidad= ((Double)vRowTemp.elementAt(26)).longValue();
                    oDesglose.nCantPaqPediatricos= ((Double)vRowTemp.elementAt(27)).longValue();
                    oDesglose.nCantPaqOtros= ((Double)vRowTemp.elementAt(28)).longValue();
                    oDesglose.nCantHabParticulares= ((Double)vRowTemp.elementAt(29)).longValue();
                    oDesglose.nCantHabEmpresa= ((Double)vRowTemp.elementAt(30)).longValue();
                    oDesglose.nCantAmbulatoriosParticulares= ((Double)vRowTemp.elementAt(31)).longValue();
                    oDesglose.nCantAmbulatoriosEmpresa= ((Double)vRowTemp.elementAt(32)).longValue();
                    oDesglose.nCantUrgenciasParticulares= ((Double)vRowTemp.elementAt(33)).longValue();
                    oDesglose.nCantUrgenciasEmpresas= ((Double)vRowTemp.elementAt(34)).longValue();
                    arrRet.add(oDesglose);
                }
            }
        }
        return arrRet;
    }

    /**
     * @return the dIni
     */
    public Date getIni() {
        return dIni;
    }

    /**
     * @param dIni the dIni to set
     */
    public void setIni(Date dIni) {
        this.dIni = dIni;
    }

    /**
     * @return the dFin
     */
    public Date getFin() {
        return dFin;
    }

    /**
     * @param dFin the dFin to set
     */
    public void setFin(Date dFin) {
        this.dFin = dFin;
    }

    /**
     * @return the nVtasContExt
     */
    public float getVtasContExt() {
        return nVtasContExt;
    }

    /**
     * @param nVtasContExt the nVtasContExt to set
     */
    public void setVtasContExt(float nVtasContExt) {
        this.nVtasContExt = nVtasContExt;
    }

    /**
     * @return the nVtasContHosp
     */
    public float getVtasContHosp() {
        return nVtasContHosp;
    }

    /**
     * @param nVtasContHosp the nVtasContHosp to set
     */
    public void setVtasContHosp(float nVtasContHosp) {
        this.nVtasContHosp = nVtasContHosp;
    }

    /**
     * @return the nVtasCredExt
     */
    public float getVtasCredExt() {
        return nVtasCredExt;
    }

    /**
     * @param nVtasCredExt the nVtasCredExt to set
     */
    public void setVtasCredExt(float nVtasCredExt) {
        this.nVtasCredExt = nVtasCredExt;
    }

    /**
     * @return the nVtasCredHosp
     */
    public float getVtasCredHosp() {
        return nVtasCredHosp;
    }

    /**
     * @param nVtasCredHosp the nVtasCredHosp to set
     */
    public void setVtasCredHosp(float nVtasCredHosp) {
        this.nVtasCredHosp = nVtasCredHosp;
    }

    /**
     * @return the nPagosServExt
     */
    public float getPagosServExt() {
        return nPagosServExt;
    }

    /**
     * @param nPagosServExt the nPagosServExt to set
     */
    public void setPagosServExt(float nPagosServExt) {
        this.nPagosServExt = nPagosServExt;
    }

    /**
     * @return the nAnticipos
     */
    public float getAnticipos() {
        return nAnticipos;
    }

    /**
     * @param nAnticipos the nAnticipos to set
     */
    public void setAnticipos(float nAnticipos) {
        this.nAnticipos = nAnticipos;
    }

    /**
     * @return the nAbonos
     */
    public float getAbonos() {
        return nAbonos;
    }

    /**
     * @param nAbonos the nAbonos to set
     */
    public void setAbonos(float nAbonos) {
        this.nAbonos = nAbonos;
    }

    /**
     * @return the nFiniquitos
     */
    public float getFiniquitos() {
        return nFiniquitos;
    }

    /**
     * @param nFiniquitos the nFiniquitos to set
     */
    public void setFiniquitos(float nFiniquitos) {
        this.nFiniquitos = nFiniquitos;
    }

    /**
     * @return the nMontoPaqMaternidad
     */
    public float getMontoPaqMaternidad() {
        return nMontoPaqMaternidad;
    }

    /**
     * @param nMontoPaqMaternidad the nMontoPaqMaternidad to set
     */
    public void setMontoPaqMaternidad(float nMontoPaqMaternidad) {
        this.nMontoPaqMaternidad = nMontoPaqMaternidad;
    }

    /**
     * @return the nMontoPaqPediatricos
     */
    public float getMontoPaqPediatricos() {
        return nMontoPaqPediatricos;
    }

    /**
     * @param nMontoPaqPediatricos the nMontoPaqPediatricos to set
     */
    public void setMontoPaqPediatricos(float nMontoPaqPediatricos) {
        this.nMontoPaqPediatricos = nMontoPaqPediatricos;
    }

    /**
     * @return the nMontoOtrosPaq
     */
    public float getMontoOtrosPaq() {
        return nMontoOtrosPaq;
    }

    /**
     * @param nMontoOtrosPaq the nMontoOtrosPaq to set
     */
    public void setMontoOtrosPaq(float nMontoOtrosPaq) {
        this.nMontoOtrosPaq = nMontoOtrosPaq;
    }

    /**
     * @return the nMontoRA
     */
    public float getMontoRA() {
        return nMontoRA;
    }

    /**
     * @param nMontoRA the nMontoRA to set
     */
    public void setMontoRA(float nMontoRA) {
        this.nMontoRA = nMontoRA;
    }

    /**
     * @return the nMontoPrestamos
     */
    public float getMontoPrestamos() {
        return nMontoPrestamos;
    }

    /**
     * @param nMontoPrestamos the nMontoPrestamos to set
     */
    public void setMontoPrestamos(float nMontoPrestamos) {
        this.nMontoPrestamos = nMontoPrestamos;
    }

    /**
     * @return the nMontoRentas
     */
    public float getMontoRentas() {
        return nMontoRentas;
    }

    /**
     * @param nMontoRentas the nMontoRentas to set
     */
    public void setMontoRentas(float nMontoRentas) {
        this.nMontoRentas = nMontoRentas;
    }

    /**
     * @return the nMontoOtrosIngr
     */
    public float getMontoOtrosIngr() {
        return nMontoOtrosIngr;
    }

    /**
     * @param nMontoOtrosIngr the nMontoOtrosIngr to set
     */
    public void setMontoOtrosIngr(float nMontoOtrosIngr) {
        this.nMontoOtrosIngr = nMontoOtrosIngr;
    }

    /**
     * @return the nMontoEgreCaja
     */
    public float getMontoEgreCaja() {
        return nMontoEgreCaja;
    }

    /**
     * @param nMontoEgreCaja the nMontoEgreCaja to set
     */
    public void setMontoEgreCaja(float nMontoEgreCaja) {
        this.nMontoEgreCaja = nMontoEgreCaja;
    }

    /**
     * @return the nMontoEgreChq
     */
    public float getMontoEgreChq() {
        return nMontoEgreChq;
    }

    /**
     * @param nMontoEgreChq the nMontoEgreChq to set
     */
    public void setMontoEgreChq(float nMontoEgreChq) {
        this.nMontoEgreChq = nMontoEgreChq;
    }

    /**
     * @return the nMontoPendFact
     */
    public float getMontoPendFact() {
        return nMontoPendFact;
    }

    /**
     * @param nMontoPendFact the nMontoPendFact to set
     */
    public void setMontoPendFact(float nMontoPendFact) {
        this.nMontoPendFact = nMontoPendFact;
    }

    /**
     * @return the nMontoPendNoFact
     */
    public float getMontoPendNoFact() {
        return nMontoPendNoFact;
    }

    /**
     * @param nMontoPendNoFact the nMontoPendNoFact to set
     */
    public void setMontoPendNoFact(float nMontoPendNoFact) {
        this.nMontoPendNoFact = nMontoPendNoFact;
    }

    /**
     * @return the nMontoFactRealizada
     */
    public float getMontoFactRealizada() {
        return nMontoFactRealizada;
    }

    /**
     * @param value the nMontoFactRealizada to set
     */
    public void setMontoFactRealizada(float value) {
        this.nMontoFactRealizada = value;
    }

    /**
     * @return the nMontoFactEsperada
     */
    public float getMontoFactEsperada() {
        return nMontoFactEsperada;
    }

    /**
     * @param value the nMontoFactEsperada to set
     */
    public void setMontoFactEsperada(float value) {
        this.nMontoFactEsperada = value;
    }

    /**
     * @return the nMontoFactEmpresas
     */
    public float getMontoFactEmpresas() {
        return nMontoFactEmpresas;
    }

    /**
     * @param value the nMontoFactEmpresas to set
     */
    public void setMontoFactEmpresas(float value) {
        this.nMontoFactEmpresas = value;
    }

    /**
     * @return the nMontoFactParticulares
     */
    public float getMontoFactParticulares() {
        return nMontoFactParticulares;
    }

    /**
     * @param value the nMontoFactParticulares to set
     */
    public void setMontoFactParticulares(float value) {
        this.nMontoFactParticulares = value;
    }

    /**
     * @return the nMontoFactDia
     */
    public float getMontoFactDia() {
        return nMontoFactDia;
    }

    /**
     * @param value the nMontoFactDia to set
     */
    public void setMontoFactDia(float value) {
        this.nMontoFactDia = value;
    }

    /**
     * @return the nMontoFactCobMes
     */
    public float getMontoFactCobMes() {
        return nMontoFactCobMes;
    }

    /**
     * @param value the nMontoFactCobMes to set
     */
    public void setMontoFactCobMes(float value) {
        this.nMontoFactCobMes = value;
    }

    /**
     * @return the nMontoFactCobAnteriores
     */
    public float getMontoFactCobAnteriores() {
        return nMontoFactCobAnteriores;
    }

    /**
     * @param value the nMontoFactCobAnteriores to set
     */
    public void setMontoFactCobAnteriores(float value) {
        this.nMontoFactCobAnteriores = value;
    }

    /**
     * @return the nCantPaqMaternidad
     */
    public long getCantPaqMaternidad() {
        return nCantPaqMaternidad;
    }

    /**
     * @param value the nCantPaqMaternidad to set
     */
    public void setCantPaqMaternidad(long value) {
        this.nCantPaqMaternidad = value;
    }

    /**
     * @return the nCantPaqPediatricos
     */
    public long getCantPaqPediatricos() {
        return nCantPaqPediatricos;
    }

    /**
     * @param value the nCantPaqPediatricos to set
     */
    public void setCantPaqPediatricos(long value) {
        this.nCantPaqPediatricos = value;
    }

    /**
     * @return the nCantPaqOtros
     */
    public long getCantPaqOtros() {
        return nCantPaqOtros;
    }

    /**
     * @param value the nCantPaqOtros to set
     */
    public void setCantPaqOtros(long value) {
        this.nCantPaqOtros = value;
    }

    /**
     * @return the nCantHabParticulares
     */
    public long getCantHabParticulares() {
        return nCantHabParticulares;
    }

    /**
     * @param value the nCantHabParticulares to set
     */
    public void setCantHabParticulares(long value) {
        this.nCantHabParticulares = value;
    }

    /**
     * @return the nCantHabEmpresa
     */
    public long getCantHabEmpresa() {
        return nCantHabEmpresa;
    }

    /**
     * @param value the nCantHabEmpresa to set
     */
    public void setCantHabEmpresa(long value) {
        this.nCantHabEmpresa = value;
    }

    /**
     * @return the nCantAmbulatoriosParticulares
     */
    public long getCantAmbulatoriosParticulares() {
        return nCantAmbulatoriosParticulares;
    }

    /**
     * @param value the nCantAmbulatoriosParticulares to set
     */
    public void setCantAmbulatoriosParticulares(long value) {
        this.nCantAmbulatoriosParticulares = value;
    }

    /**
     * @return the nCantAmbulatoriosEmpresa
     */
    public long getCantAmbulatoriosEmpresa() {
        return nCantAmbulatoriosEmpresa;
    }

    /**
     * @param value the nCantAmbulatoriosEmpresa to set
     */
    public void setCantAmbulatoriosEmpresa(long value) {
        this.nCantAmbulatoriosEmpresa = value;
    }

    /**
     * @return the nCantUrgenciasParticulares
     */
    public long getCantUrgenciasParticulares() {
        return nCantUrgenciasParticulares;
    }

    /**
     * @param value the nCantUrgenciasParticulares to set
     */
    public void setCantUrgenciasParticulares(long value) {
        this.nCantUrgenciasParticulares = value;
    }

    /**
     * @return the nCantUrgenciasEmpresas
     */
    public long getCantUrgenciasEmpresas() {
        return nCantUrgenciasEmpresas;
    }

    /**
     * @param value the nCantUrgenciasEmpresas to set
     */
    public void setCantUrgenciasEmpresas(long value) {
        this.nCantUrgenciasEmpresas = value;
    }
}
