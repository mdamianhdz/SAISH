package org.apli.modelbeans.reportes;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import org.apli.modelbeans.LineaIngreso;
import org.apli.AD.AccesoDatos;
/**
 * Clase que representa la información del reporte de Desglose de Servicios
 * Prestados
 * @author BAOZ
 */
public class RptDesgloseServiciosPrestados implements Serializable{
private Date dIni;
private Date dFin;
private LineaIngreso oLin;
private long nCantContHosp;
private long nCantContExt;
private long nCantCredHosp;
private long nCantCredExt;
private float nMontoContHosp;
private float nMontoContExt;
private float nMontoCredHosp;
private float nMontoCredExt;
private float nTotalContado;
private float nTotalCredito;
private float nTotal;
private AccesoDatos oAD;
    public List<RptDesgloseServiciosPrestados> buscarRpt() throws Exception{
    Vector rst = null;
    String sQuery = "";
    RptDesgloseServiciosPrestados oDesglose;
    List<RptDesgloseServiciosPrestados> arrRet = null;
    SimpleDateFormat fmtTxt = new SimpleDateFormat("yyyy-MM-dd");
        if (this.dIni==null || this.dFin == null)
            throw new Exception("RptDesgloseServiciosPrestados.buscarRpt: faltan datos");
        else{
            sQuery="select * from buscaRptDesgloseServiciosOtorgadosRango('"+
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
                arrRet = new ArrayList();
            for (int i = 0; i < rst.size(); i++) {
                oDesglose=new RptDesgloseServiciosPrestados();
                Vector vRowTemp = (Vector)rst.elementAt(i);
                oDesglose.oLin = new LineaIngreso();
                oDesglose.oLin.setCveLin(((Double)vRowTemp.elementAt(0)).intValue());
                oDesglose.oLin.setDescrip((String)vRowTemp.elementAt(1));
                oDesglose.nCantContHosp = ((Double)vRowTemp.elementAt(2)).longValue();
                oDesglose.nMontoContHosp = ((Double)vRowTemp.elementAt(3)).floatValue();
                oDesglose.nCantContExt = ((Double)vRowTemp.elementAt(4)).longValue();
                oDesglose.nMontoContExt = ((Double)vRowTemp.elementAt(5)).floatValue();
                oDesglose.nTotalContado = ((Double)vRowTemp.elementAt(6)).floatValue();
                oDesglose.nCantCredHosp = ((Double)vRowTemp.elementAt(7)).longValue();
                oDesglose.nMontoCredHosp = ((Double)vRowTemp.elementAt(8)).floatValue();
                oDesglose.nCantCredExt = ((Double)vRowTemp.elementAt(9)).longValue();
                oDesglose.nMontoCredExt = ((Double)vRowTemp.elementAt(10)).floatValue();
                oDesglose.nTotalCredito = ((Double)vRowTemp.elementAt(11)).floatValue();
                oDesglose.nTotal = ((Double)vRowTemp.elementAt(12)).floatValue();
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
     * @return the oLin
     */
    public LineaIngreso getLin() {
        return oLin;
    }

    /**
     * @param oLin the oLin to set
     */
    public void setLin(LineaIngreso oLin) {
        this.oLin = oLin;
    }

    /**
     * @return the nCantContHosp
     */
    public long getCantContHosp() {
        return nCantContHosp;
    }

    /**
     * @param nCantContHosp the nCantContHosp to set
     */
    public void setCantContHosp(long nCantContHosp) {
        this.nCantContHosp = nCantContHosp;
    }

    /**
     * @return the nCantContExt
     */
    public long getCantContExt() {
        return nCantContExt;
    }

    /**
     * @param nCantContExt the nCantContExt to set
     */
    public void setCantContExt(long nCantContExt) {
        this.nCantContExt = nCantContExt;
    }

    /**
     * @return the nCantCredHosp
     */
    public long getCantCredHosp() {
        return nCantCredHosp;
    }

    /**
     * @param nCantCredHosp the nCantCredHosp to set
     */
    public void setCantCredHosp(long nCantCredHosp) {
        this.nCantCredHosp = nCantCredHosp;
    }

    /**
     * @return the nCantCredExt
     */
    public long getCantCredExt() {
        return nCantCredExt;
    }

    /**
     * @param nCantCredExt the nCantCredExt to set
     */
    public void setCantCredExt(long nCantCredExt) {
        this.nCantCredExt = nCantCredExt;
    }

    /**
     * @return the nMontoContHosp
     */
    public float getMontoContHosp() {
        return nMontoContHosp;
    }

    /**
     * @param nMontoContHosp the nMontoContHosp to set
     */
    public void setMontoContHosp(float nMontoContHosp) {
        this.nMontoContHosp = nMontoContHosp;
    }

    /**
     * @return the nMontoContExt
     */
    public float getMontoContExt() {
        return nMontoContExt;
    }

    /**
     * @param nMontoContExt the nMontoContExt to set
     */
    public void setMontoContExt(float nMontoContExt) {
        this.nMontoContExt = nMontoContExt;
    }

    /**
     * @return the nMontoCredHosp
     */
    public float getMontoCredHosp() {
        return nMontoCredHosp;
    }

    /**
     * @param nMontoCredHosp the nMontoCredHosp to set
     */
    public void setMontoCredHosp(float nMontoCredHosp) {
        this.nMontoCredHosp = nMontoCredHosp;
    }

    /**
     * @return the nMontoCredExt
     */
    public float getMontoCredExt() {
        return nMontoCredExt;
    }

    /**
     * @param nMontoCredExt the nMontoCredExt to set
     */
    public void setMontoCredExt(float nMontoCredExt) {
        this.nMontoCredExt = nMontoCredExt;
    }

    /**
     * @return the nTotalContado
     */
    public float getTotalContado() {
        return nTotalContado;
    }

    /**
     * @param nTotalContado the nTotalContado to set
     */
    public void setTotalContado(float nTotalContado) {
        this.nTotalContado = nTotalContado;
    }

    /**
     * @return the nTotalCredito
     */
    public float getTotalCredito() {
        return nTotalCredito;
    }

    /**
     * @param nTotalCredito the nTotalCredito to set
     */
    public void setTotalCredito(float nTotalCredito) {
        this.nTotalCredito = nTotalCredito;
    }

    /**
     * @return the nTotal
     */
    public float getTotal() {
        return nTotal;
    }

    /**
     * @param nTotal the nTotal to set
     */
    public void setTotal(float nTotal) {
        this.nTotal = nTotal;
    }
}
