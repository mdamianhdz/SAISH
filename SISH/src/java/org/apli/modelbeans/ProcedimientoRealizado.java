package org.apli.modelbeans;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import org.apli.AD.AccesoDatos;

/**
 * Procedimiento quir�rgico realizado (operaci�n)
 *
 * @author BAOZ
 * @version 1.0
 * @created 04-ago.-2014 15:55:15
 */
public class ProcedimientoRealizado implements Serializable {

    /**
     * Fecha-hora de fin del procedimiento
     */
    private Date dFin;
    /**
     * Fecha-hora de inicio del procedimiento
     */
    private Date dIni;
    /**
     * Identificador del procedimiento realizado, es autogenerado
     */
    private int nIdProcQxRealizado;
    /**
     * Tipo principal de procedimiento (cirug�a) realizado
     */
    private TipoProcQx oTipoProcQx=null;
    /**
     * Descripci�n general del procedimiento
     */
    private String sNotaMedica;
    /**
     * Tipo de programaci�n que tuvo la cirug�a U = Urgencia P = Paquete R =
     * Programada
     */
    private String sTipoProg;
    private String sDuracion;
    private AccesoDatos oAD;

    public ProcedimientoRealizado(AccesoDatos poAD) {
        oAD = poAD;
    }

    public ProcedimientoRealizado() {

    }

    public void finalize() throws Throwable {

    }

    public String insertarProceRealizado() throws Exception {
        Vector rst = null;
        String sQuery = "";

        if (dIni==null || dFin== null || sNotaMedica.equals("") || oTipoProcQx==null || sTipoProg.equals("") ) {
            throw new Exception("ProcedimientoRealizado.insertarProceRealizado(): error de programación, faltan datos");
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd h:mm:ss");
            sQuery = "SELECT * from insertaprocqxrealizado('" + sdf.format(dIni) + "'::timestamp without time zone,'" + sdf.format(dFin) + "'::timestamp without time zone, '" + sNotaMedica + "'::character varying, '" + this.sTipoProg + "'::character(1), '" + this.oTipoProcQx.getCveTipoProcQx() + "'::character varying);";
            if (getAD() == null) {
                setAD(new AccesoDatos());
                getAD().conectar();
                rst = getAD().ejecutarConsulta(sQuery);
                getAD().desconectar();
                setAD(null);
            }
        }
        return "" + rst.get(0);
    }

    public Date getFin() {
        return dFin;
    }

    public void setFin(Date dFin) {
        this.dFin = dFin;
    }

    public Date getIni() {
        return dIni;
    }

    public void setIni(Date dIni) {
        this.dIni = dIni;
    }

    public int getIdProcQxRealizado() {
        return nIdProcQxRealizado;
    }

    public void setIdProcQxRealizado(int nIdProcQxRealizado) {
        this.nIdProcQxRealizado = nIdProcQxRealizado;
    }

    public TipoProcQx getTipoProcQx() {
        return oTipoProcQx;
    }

    public void setTipoProcQx(TipoProcQx oTipoProcQx) {
        this.oTipoProcQx = oTipoProcQx;
    }

    public String getNotaMedica() {
        return sNotaMedica;
    }

    public void setNotaMedica(String sNotaMedica) {
        this.sNotaMedica = sNotaMedica;
    }

    public String getTipoProg() {
        return sTipoProg;
    }

    public void setTipoProg(String sTipoProg) {
        this.sTipoProg = sTipoProg;
    }
    
    public String getDuracion() {
        return sDuracion;
    }
    public void setDuracion(String sDuracion) {
        this.sDuracion = sDuracion;
    }
        
    public AccesoDatos getAD() {
        return oAD;
    }

    public void setAD(AccesoDatos oAD) {
        this.oAD = oAD;
    }

}
