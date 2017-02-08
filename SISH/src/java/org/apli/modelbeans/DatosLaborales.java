package org.apli.modelbeans;

import java.io.Serializable;
import java.util.Vector;
import org.apli.AD.AccesoDatos;
import org.apli.modelbeans.ventaCredito.CompaniaCred;

/**
 *
 * @author Lily_LnBnd
 */
public class DatosLaborales implements Serializable{
    private CompaniaCred oEmpresa;
    private Paciente oPaciente;
    private String sOtraEmpresa;
    private String sSucursal;
    private String sNumEmpleado;
    private String sOcupacion;
    private String sJefeInmediato;
    private String sNomEmpleado;
    private AccesoDatos oAD;
    
    public DatosLaborales(){
        oEmpresa=new CompaniaCred();
    }

    public CompaniaCred getEmpresa() {
        return oEmpresa;
    }
    
    public String guardaDatosLaborales(Paciente oPaciente)throws Exception{
        Vector rst = null;
        String sQuery = "";
        
        if (oPaciente==null){
             throw new Exception("DatosLaborales.guarda: error de programación, faltan datos");
        }else{
            sQuery="select * from guardaDatosLaborales("+oPaciente.getFolioPac()+", "+oPaciente.getDatosLaborales().getEmpresa().getIdEmp()+"::int2, '"+
                    (oPaciente.getDatosLaborales().getOtraEmpresa()==null?"":oPaciente.getDatosLaborales().getOtraEmpresa())+"', '"+
                    (oPaciente.getDatosLaborales().getSucursal()==null?"":oPaciente.getDatosLaborales().getSucursal())+"', '"+
                    (oPaciente.getDatosLaborales().getNumEmpleado()==null?"":oPaciente.getDatosLaborales().getNumEmpleado())+"', '"+
                    (oPaciente.getDatosLaborales().getOcupacion()==null?"":oPaciente.getDatosLaborales().getOcupacion())+"', '"+
                    (oPaciente.getDatosLaborales().getJefeInmediato()==null?"":oPaciente.getDatosLaborales().getJefeInmediato())+"', '"+
                    (oPaciente.getDatosLaborales().getNomEmpleado()==null?"":oPaciente.getDatosLaborales().getNomEmpleado())+"')";
            System.out.println(sQuery);
            if (getAD() == null){
                setAD(new AccesoDatos());
                getAD().conectar();
                rst = getAD().ejecutarConsulta(sQuery);
                getAD().desconectar();
                setAD(null);
            }else{
                rst = getAD().ejecutarConsulta(sQuery);
            }
        }
        return ""+rst.get(0).toString().substring(1, rst.get(0).toString().length()-1);
    }

    public void setEmpresa(CompaniaCred oEmpresa) {
        this.oEmpresa = oEmpresa;
    }

    public Paciente getPaciente() {
        return oPaciente;
    }

    public void setPaciente(Paciente oPaciente) {
        this.oPaciente = oPaciente;
    }

    public String getOtraEmpresa() {
        return sOtraEmpresa;
    }

    public void setOtraEmpresa(String sOtraEmpresa) {
        this.sOtraEmpresa = sOtraEmpresa;
    }

    public String getSucursal() {
        return sSucursal;
    }

    public void setSucursal(String sSucursal) {
        this.sSucursal = sSucursal;
    }

    public String getNumEmpleado() {
        return sNumEmpleado;
    }

    public void setNumEmpleado(String sNumEmpleado) {
        this.sNumEmpleado = sNumEmpleado;
    }

    public String getOcupacion() {
        return sOcupacion;
    }

    public void setOcupacion(String sOcupacion) {
        this.sOcupacion = sOcupacion;
    }

    public String getJefeInmediato() {
        return sJefeInmediato;
    }

    public void setJefeInmediato(String sJefeInmediato) {
        this.sJefeInmediato = sJefeInmediato;
    }

    public String getNomEmpleado() {
        return sNomEmpleado;
    }

    public void setNomEmpleado(String sNomEmpleado) {
        this.sNomEmpleado = sNomEmpleado;
    }

    public AccesoDatos getAD() {
        return oAD;
    }

    public void setAD(AccesoDatos oAD) {
        this.oAD = oAD;
    }
}
