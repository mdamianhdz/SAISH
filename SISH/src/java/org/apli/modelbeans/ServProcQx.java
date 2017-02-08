package org.apli.modelbeans;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Vector;
import org.apli.AD.AccesoDatos;

/**
 * Participantes en el procedimiento quir�rgico (hay un servicio prestado por
 * honorarios por cada m�dico/enfermera participante)
 *
 * @author BAOZ
 * @version 1.0
 * @created 04-ago.-2014 15:55:18
 */
public class ServProcQx implements Serializable {

    /**
     * Rol en el que participa un m�dico/enfermera representado mediante un
     * cargo por servicio dentro de un procedimiento quir�rgico C = Cirujano A =
     * Anestesi�logo P = Primer ayudante S = Segundo ayudante I = Enfermera
     * Instrumentista
     */
    private String sRol;
    private String sFolioServicioPrest;
    private String sDescripRol;
    private PersonalHospitalario oPersonal;
    private ProcedimientoRealizado oProcedimientoRealizado;

    public ServProcQx() {
    }

    private AccesoDatos oAD;

    public ServProcQx(AccesoDatos poAD) {
        oAD = poAD;
    }

    public void finalize() throws Throwable {

    }

    public String sQueryInsertarServProcQx() throws Exception {
        String sQuery = "";
        if (oProcedimientoRealizado.getIdProcQxRealizado() == 0 || sFolioServicioPrest.equals("") || sRol.equals("")) {
            throw new Exception("ProcedimientoRealizado.sQueryInsertarServProcQx(): error de programación, faltan datos");
        } else {
            sQuery = "SELECT * from insertaservprocqx(" + this.oProcedimientoRealizado.getIdProcQxRealizado() + ", '" + this.sFolioServicioPrest + "'::character varying, '" + sRol + "'::character(1));";
        }
        System.out.println("sQueryins:"+sQuery);    
        return sQuery;
    }

    public String insertarServProcQx(String sQuery) throws Exception {
        Vector rst = null;
        if (sQuery.equals("")) {
            throw new Exception("ProcedimientoRealizado.insertarProceRealizado(): error de programación, faltan datos");
        } else {
           if (getAD() == null) {
                setAD(new AccesoDatos());
                getAD().conectar();
                rst = getAD().ejecutarConsulta(sQuery);
                getAD().desconectar();
                setAD(null);
            }
        }
        return " " + rst.get(0);
    }

    public String getRol() {
        return sRol;
    }

    public void setRol(String sRol) {
        this.sRol = sRol;
    }

    public PersonalHospitalario getPersonal() {
        return oPersonal;
    }

    public void setPersonal(PersonalHospitalario oPersonal) {
        this.oPersonal = oPersonal;
    }

    public String getDescripRol() {
        return sDescripRol;
    }

    public void setDescripRol(String sDescripRol) {
        this.sDescripRol = sDescripRol;
    }

    public ProcedimientoRealizado getProcedimientoRealizado() {
        return oProcedimientoRealizado;
    }

    public void setProcedimientoRealizado(ProcedimientoRealizado oProcedimientoRealizado) {
        this.oProcedimientoRealizado = oProcedimientoRealizado;
    }

    public String getFolioServicioPrest() {
        return sFolioServicioPrest;
    }

    public void setFolioServicioPrest(String sFolioServicioPrest) {
        this.sFolioServicioPrest = sFolioServicioPrest;
    }

    public AccesoDatos getAD() {
        return oAD;
    }

    public void setAD(AccesoDatos oAD) {
        this.oAD = oAD;
    }

}
