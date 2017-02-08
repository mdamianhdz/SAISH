package org.apli.modelbeans.facturacion.cfdi;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.Date;
import org.apli.AD.AccesoDatos;
import java.io.Serializable;
/**
 *  Julio de 2014
 * @author Isabel Espinoza Espinoza
 */
public class CuentaBusquedaPorNombrePaciente implements Serializable{
    private Date oDiaInicioCuenta;
    private int nCveEpisodio;
    private int nNumPaquete;
    private int nFolioPaciente;
    private Date oDiaNacimiento;
    private String sNombre;
    private String sApPaterno;
    private String sApMaterno;
    private String sCurp;
    protected AccesoDatos oAD = null;
    public List<CuentaBusquedaPorNombrePaciente> buscarFoliosParaFacturarDePacientesPorNombre(String sNombre, String sApPaterno,String sApMaterno) throws Exception{
        CuentaBusquedaPorNombrePaciente oP=null;
        Vector rst = null;
        Vector<CuentaBusquedaPorNombrePaciente> vObj = null;
        List<CuentaBusquedaPorNombrePaciente> lista=new ArrayList();
        String sQuery = "";
        int i=0;
        if(sNombre==null)sNombre="";
        if(sApPaterno==null)sApPaterno="";
        if(sApMaterno==null)sApMaterno="";
        sNombre="%"+sNombre+"%";
        sApPaterno="%"+sApPaterno+"%";
        sApMaterno="%"+sApMaterno+"%";
        sQuery = "select * from buscarfoliospacientesparticularesparafacturar('"+sNombre+"'::character varying,'"+sApPaterno+"'::character varying,'"+sApMaterno+"'::character varying)";
        if (getAD() == null){
            setAD(new AccesoDatos());
            getAD().conectar();
        }
        rst = getAD().ejecutarConsulta(sQuery);
        getAD().desconectar();
        setAD(null);
        if (rst != null) {
            vObj = new Vector<CuentaBusquedaPorNombrePaciente>();
            for (i = 0; i < rst.size(); i++) {
                oP = new CuentaBusquedaPorNombrePaciente();
                Vector vRowTemp = (Vector)rst.elementAt(i); 
                oP.setFolioPaciente(((Double) vRowTemp.elementAt(0)).intValue());
                oP.setDiaNacimiento((Date) vRowTemp.elementAt(1));
                oP.setNombre((String) vRowTemp.elementAt(2));
                oP.setApPaterno((String) vRowTemp.elementAt(3));
                oP.setApMaterno((String) vRowTemp.elementAt(4));
                oP.setCurp((String) vRowTemp.elementAt(5)); 
                lista.add(oP);
            }
        }
        return lista;
    }
    
    public List<CuentaBusquedaPorNombrePaciente> buscarCuentasParaFacturarDePacientesPorNombre(String sNombre, String sApPaterno,String sApMaterno) throws Exception{
        CuentaBusquedaPorNombrePaciente oP=null;
        Vector rst = null;
        Vector<CuentaBusquedaPorNombrePaciente> vObj = null;
        List<CuentaBusquedaPorNombrePaciente> lista=new ArrayList();
        String sQuery = "";
        int i=0;
        if(sNombre==null)sNombre="";
        if(sApPaterno==null)sApPaterno="";
        if(sApMaterno==null)sApMaterno="";
        sNombre="%"+sNombre+"%";
        sApPaterno="%"+sApPaterno+"%";
        sApMaterno="%"+sApMaterno+"%";
        sQuery = "select * from buscarcuentasparafacturar('"+sNombre+"'::character varying,'"+sApPaterno+"'::character varying,'"+sApMaterno+"'::character varying)";
        if (getAD() == null){
            setAD(new AccesoDatos());
            getAD().conectar();
        }
        rst = getAD().ejecutarConsulta(sQuery);
        getAD().desconectar();
        setAD(null);
        if (rst != null) {
            vObj = new Vector<CuentaBusquedaPorNombrePaciente>();
            for (i = 0; i < rst.size(); i++) {
                oP = new CuentaBusquedaPorNombrePaciente();
                Vector vRowTemp = (Vector)rst.elementAt(i); 
                oP.setDiaInicioCuenta((Date) vRowTemp.elementAt(0));
                oP.setCveEpisodio(((Double) vRowTemp.elementAt(1)).intValue());
                oP.setFolioPaciente(((Double) vRowTemp.elementAt(2)).intValue());
                oP.setDiaNacimiento((Date) vRowTemp.elementAt(3));
                oP.setNombre((String) vRowTemp.elementAt(4));
                oP.setApPaterno((String) vRowTemp.elementAt(5));
                oP.setApMaterno((String) vRowTemp.elementAt(6));
                oP.setCurp((String) vRowTemp.elementAt(7)); 
                lista.add(oP);
            }
        }
        return lista;
    }
    public List<CuentaBusquedaPorNombrePaciente> buscarCuentasParaFacturarDePaquetesPorNombre(String sNombre, String sApPaterno,String sApMaterno) throws Exception{
        CuentaBusquedaPorNombrePaciente oP=null;
        Vector rst = null;
        Vector<CuentaBusquedaPorNombrePaciente> vObj = null;
        List<CuentaBusquedaPorNombrePaciente> lista=new ArrayList();
        String sQuery = "";
        int i=0;
        if(sNombre==null)sNombre="";
        if(sApPaterno==null)sApPaterno="";
        if(sApMaterno==null)sApMaterno="";
        sNombre="%"+sNombre+"%";
        sApPaterno="%"+sApPaterno+"%";
        sApMaterno="%"+sApMaterno+"%";
        sQuery = "select * from buscarcuentapaquetesparafacturar('"+sNombre+"'::character varying,'"+sApPaterno+"'::character varying,'"+sApMaterno+"'::character varying)";
        if (getAD() == null){
            setAD(new AccesoDatos());
            getAD().conectar();
        }
        rst = getAD().ejecutarConsulta(sQuery);
        getAD().desconectar();
        setAD(null);
        if (rst != null) {
            vObj = new Vector<CuentaBusquedaPorNombrePaciente>();
            for (i = 0; i < rst.size(); i++) {
                oP = new CuentaBusquedaPorNombrePaciente();
                Vector vRowTemp = (Vector)rst.elementAt(i); 
                oP.setNumPaquete(((Double) vRowTemp.elementAt(0)).intValue());
                oP.setDiaInicioCuenta((Date) vRowTemp.elementAt(1));
                oP.setCveEpisodio(((Double) vRowTemp.elementAt(2)).intValue());
                oP.setFolioPaciente(((Double) vRowTemp.elementAt(3)).intValue());
                oP.setDiaNacimiento((Date) vRowTemp.elementAt(4));
                oP.setNombre((String) vRowTemp.elementAt(5));
                oP.setApPaterno((String) vRowTemp.elementAt(6));
                oP.setApMaterno((String) vRowTemp.elementAt(7));
                oP.setCurp((String) vRowTemp.elementAt(8)); 
                lista.add(oP);
            }
        }
        return lista;
    }
    public List<CuentaBusquedaPorNombrePaciente> buscarCuentasParaFacturarDePacientesEmpresaPorNombre(String sNombre, String sApPaterno,String sApMaterno,String empresa) throws Exception{
        CuentaBusquedaPorNombrePaciente oP=null;
        Vector rst = null;
        Vector<CuentaBusquedaPorNombrePaciente> vObj = null;
        List<CuentaBusquedaPorNombrePaciente> lista=new ArrayList();
        String sQuery = "";
        int i=0;
        if(sNombre==null)sNombre="";
        if(sApPaterno==null)sApPaterno="";
        if(sApMaterno==null)sApMaterno="";
        sNombre="%"+sNombre+"%";
        sApPaterno="%"+sApPaterno+"%";
        sApMaterno="%"+sApMaterno+"%";
        sQuery = "select * from buscarcuentasEmpresaParafacturar('"+sNombre+"'::character varying,'"+sApPaterno+"'::character varying,'"+sApMaterno+"'::character varying,'"+empresa+"'::character varying)";
        if (getAD() == null){
            setAD(new AccesoDatos());
            getAD().conectar();
        }
        rst = getAD().ejecutarConsulta(sQuery);
        getAD().desconectar();
        setAD(null);
        if (rst != null) {
            vObj = new Vector<CuentaBusquedaPorNombrePaciente>();
            for (i = 0; i < rst.size(); i++) {
                oP = new CuentaBusquedaPorNombrePaciente();
                Vector vRowTemp = (Vector)rst.elementAt(i); 
                oP.setDiaInicioCuenta((Date) vRowTemp.elementAt(0));
                oP.setCveEpisodio(((Double) vRowTemp.elementAt(1)).intValue());
                oP.setFolioPaciente(((Double) vRowTemp.elementAt(2)).intValue());
                oP.setDiaNacimiento((Date) vRowTemp.elementAt(3));
                oP.setNombre((String) vRowTemp.elementAt(4));
                oP.setApPaterno((String) vRowTemp.elementAt(5));
                oP.setApMaterno((String) vRowTemp.elementAt(6));
                oP.setCurp((String) vRowTemp.elementAt(7)); 
                lista.add(oP);
            }
        }
        return lista;
    }
    public String getNombreCompleto(){
        return this.sNombre+" "+this.sApPaterno+" "+this.sApMaterno;
    }
    public int getFolioPaciente() {
        return nFolioPaciente;
    }

    public void setFolioPaciente(int nFolioPaciente) {
        this.nFolioPaciente = nFolioPaciente;
    }

    public Date getDiaNacimiento() {
        return oDiaNacimiento;
    }

    public void setDiaNacimiento(Date oDiaNacimiento) {
        this.oDiaNacimiento = oDiaNacimiento;
    }

    public String getApMaterno() {
        return sApMaterno;
    }

    public void setApMaterno(String sApMaterno) {
        this.sApMaterno = sApMaterno;
    }

    public String getApPaterno() {
        return sApPaterno;
    }

    public void setApPaterno(String sApPaterno) {
        this.sApPaterno = sApPaterno;
    }

    public String getCurp() {
        return sCurp;
    }

    public void setCurp(String sCurp) {
        this.sCurp = sCurp;
    }

    public String getNombre() {
        return sNombre;
    }

    public void setNombre(String sNombre) {
        this.sNombre = sNombre;
    }
    public AccesoDatos getAD() {
        return oAD;
    }
    public void setAD(AccesoDatos oAD) {
        this.oAD = oAD;
    }
    public int getCveEpisodio() {
        return nCveEpisodio;
    }

    public void setCveEpisodio(int nCveEpisodio) {
        this.nCveEpisodio = nCveEpisodio;
    }

    public Date getDiaInicioCuenta() {
        return oDiaInicioCuenta;
    }

    public void setDiaInicioCuenta(Date pDiaInicioCuenta) {
        this.oDiaInicioCuenta = pDiaInicioCuenta;
    }
    public int getNumPaquete() {
        return nNumPaquete;
    }

    public void setNumPaquete(int nNumPaquete) {
        this.nNumPaquete = nNumPaquete;
    }
}
