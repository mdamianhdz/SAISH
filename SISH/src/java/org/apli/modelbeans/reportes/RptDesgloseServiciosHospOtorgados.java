package org.apli.modelbeans.reportes;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import org.apli.AD.AccesoDatos;
/**
 * Clase que representa la información del reporte de Desglose de Servicios
 * Hospitalizados Otorgados
 * @author JDanny Hdz
 */
public class RptDesgloseServiciosHospOtorgados implements Serializable{
private String dOpeFecha;
private int nFolio;
private int nFolioPaciente;
private String sNombre;
private String sApPaterno;
private String sApMaterno;
private String nEmpresaPart;

private String sDescripcion;
private float fPrecio;
private float fIva;
private float fTotal;
private String nCredOcont;
private AccesoDatos oAD;

private String sDescripLinea;
private float fTotalLinea;

    public List<RptDesgloseServiciosHospOtorgados> buscaRpt() throws Exception{
        Vector rst = null;
        String sQuery="";
        RptDesgloseServiciosHospOtorgados oServicios;
        List<RptDesgloseServiciosHospOtorgados> arrRet = null;
        
        SimpleDateFormat fmtTxt = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                sQuery="select * from buscaRptDesgloseServiciosHospitalizadosOtorgados()";
                if(oAD==null){
                    oAD= new AccesoDatos();
                    oAD.conectar();
                    rst=oAD.ejecutarConsulta(sQuery);
                    oAD.desconectar();
                    oAD=null;
                }else{
                    rst=oAD.ejecutarConsulta(sQuery);
                    }
                if(rst!=null){
                    arrRet=new ArrayList();
                    for(int i=0; i<rst.size();i++){
                        oServicios=new RptDesgloseServiciosHospOtorgados();
                        Vector vRowTemp = (Vector)rst.elementAt(i);
                        oServicios.dOpeFecha=fmtTxt.format((Date)vRowTemp.elementAt(0));
                        oServicios.nFolio=((Double)vRowTemp.elementAt(1)).intValue();
                        oServicios.nFolioPaciente=((Double)vRowTemp.elementAt(2)).intValue();
                        oServicios.sNombre=((String)vRowTemp.elementAt(3));
                        oServicios.sApPaterno=((String)vRowTemp.elementAt(4));
                        oServicios .sApMaterno=((String)vRowTemp.elementAt(5));
                        //oServicios.nEmpresaPart=((Double)vRowTemp.elementAt(6)).intValue();
                        if(((Double)vRowTemp.elementAt(6)).intValue()==0 || ((Double)vRowTemp.elementAt(6)).intValue()==2)
                            oServicios.nEmpresaPart="Particular";
                        else
                            oServicios.nEmpresaPart="Empresa";
                        oServicios.sDescripcion=((String)vRowTemp.elementAt(7));
                        oServicios.fPrecio=((Double)vRowTemp.elementAt(8)).floatValue();
                        oServicios.fIva=((Double)vRowTemp.elementAt(9)).floatValue();
                        oServicios.fTotal=((Double)vRowTemp.elementAt(10)).floatValue();
                        //oServicios.nCredOcont=((Double)vRowTemp.elementAt(11)).intValue();
                        if(((Double)vRowTemp.elementAt(11)).intValue()==1)
                            oServicios.nCredOcont="Credito";
                        else
                            oServicios.nCredOcont="Contado";
                        arrRet.add(oServicios);
                    }
                }
        return arrRet;
    }
    
    public List<RptDesgloseServiciosHospOtorgados> buscaRptLinea(String cad) throws Exception{
        Vector rst = null;
        String sQuery="";
        RptDesgloseServiciosHospOtorgados oServicios;
        List<RptDesgloseServiciosHospOtorgados> arrRet = null;
                sQuery="select * from buscarptdesgloseservicioshospitalizadosotorgadosPorLinea('"+cad+"')";
                if(oAD==null){
                    oAD= new AccesoDatos();
                    oAD.conectar();
                    rst=oAD.ejecutarConsulta(sQuery);
                    oAD.desconectar();
                    oAD=null;
                }else{
                    rst=oAD.ejecutarConsulta(sQuery);
                    }
                if(rst!=null){
                    arrRet=new ArrayList();
                    for(int i=0; i<rst.size();i++){
                        oServicios=new RptDesgloseServiciosHospOtorgados();
                        Vector vRowTemp = (Vector)rst.elementAt(i);
                        oServicios.sDescripLinea=(String)vRowTemp.elementAt(0);
                        oServicios.fTotalLinea=((Double)vRowTemp.elementAt(1)).floatValue();
                        arrRet.add(oServicios);
                    }
                }
        return arrRet;
    }
    /**
     * @return the dOpeFecha
     */
    public String getOpeFecha() {
        return dOpeFecha;
    }

    /**
     * @param dOpeFecha the dOpeFecha to set
     */
    public void setOpeFecha(String dOpeFecha) {
        this.dOpeFecha = dOpeFecha;
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
     * @return the nFolioPaciente
     */
    public int getFolioPaciente() {
        return nFolioPaciente;
    }

    /**
     * @param nFolioPaciente the nFolioPaciente to set
     */
    public void setFolioPaciente(int nFolioPaciente) {
        this.nFolioPaciente = nFolioPaciente;
    }

    /**
     * @return the sNombre
     */
    public String getNombre() {
        return sNombre;
    }

    /**
     * @param sNombre the sNombre to set
     */
    public void setNombre(String sNombre) {
        this.sNombre = sNombre;
    }

    /**
     * @return the sApPaterno
     */
    public String getApPaterno() {
        return sApPaterno;
    }

    /**
     * @param sApPaterno the sApPaterno to set
     */
    public void setApPaterno(String sApPaterno) {
        this.sApPaterno = sApPaterno;
    }

    /**
     * @return the sApMaterno
     */
    public String getApMaterno() {
        return sApMaterno;
    }

    /**
     * @param sApMaterno the sApMaterno to set
     */
    public void setApMaterno(String sApMaterno) {
        this.sApMaterno = sApMaterno;
    }

    /**
     * @return the nEmpresaPart
     */
    public String getEmpresaPart() {
        return nEmpresaPart;
    }

    /**
     * @param nEmpresaPart the nEmpresaPart to set
     */
    public void setEmpresaPart(String nEmpresaPart) {
        this.nEmpresaPart = nEmpresaPart;
    }

    /**
     * @return the sDescripcion
     */
    public String getDescripcion() {
        return sDescripcion;
    }

    /**
     * @param sDescripcion the sDescripcion to set
     */
    public void setDescripcion(String sDescripcion) {
        this.sDescripcion = sDescripcion;
    }

    /**
     * @return the fPrecio
     */
    public float getPrecio() {
        return fPrecio;
    }

    /**
     * @param fPrecio the fPrecio to set
     */
    public void setPrecio(float fPrecio) {
        this.fPrecio = fPrecio;
    }

    /**
     * @return the fIva
     */
    public float getIva() {
        return fIva;
    }

    /**
     * @param fIva the fIva to set
     */
    public void setIva(float fIva) {
        this.fIva = fIva;
    }

    /**
     * @return the fTotal
     */
    public float getTotal() {
        return fTotal;
    }

    /**
     * @param fTotal the fTotal to set
     */
    public void setTotal(float fTotal) {
        this.fTotal = fTotal;
    }

    /**
     * @return the nCredOcont
     */
    public String getCredOcont() {
        return nCredOcont;
    }

    /**
     * @param nCredOcont the nCredOcont to set
     */
    public void setCredOcont(String nCredOcont) {
        this.nCredOcont = nCredOcont;
    }

    public String getNombreCompleto(){
        return this.sNombre+" " + this.sApPaterno + " " + this.sApMaterno;
    }

    /**
     * @return the sDescripLinea
     */
    public String getDescripLinea() {
        return sDescripLinea;
    }

    /**
     * @param sDescripLinea the sDescripLinea to set
     */
    public void setDescripLinea(String sDescripLinea) {
        this.sDescripLinea = sDescripLinea;
    }

    /**
     * @return the fTotalLinea
     */
    public float getTotalLinea() {
        return fTotalLinea;
    }

    /**
     * @param fTotalLinea the fTotalLinea to set
     */
    public void setTotalLinea(float fTotalLinea) {
        this.fTotalLinea = fTotalLinea;
    }
}
