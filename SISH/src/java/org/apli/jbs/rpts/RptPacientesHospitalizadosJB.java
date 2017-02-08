package org.apli.jbs.rpts;

import java.util.Date;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import org.apli.modelbeans.reportes.RptHospitalizacion;

/**
 *
 * @author juan
 */
@ManagedBean(name="oPacHosp")
@RequestScoped
public class RptPacientesHospitalizadosJB {
    
    private Date fechaInicio;
    private Date fechaFin;
    private RptHospitalizacion oRHospi;
    private RptHospitalizacion datosHospitalizacion[];
    /**
     * Creates a new instance of PacientesHospitalizadosJB
     */
    public RptPacientesHospitalizadosJB() {
        fechaInicio = new Date();
        oRHospi= new RptHospitalizacion();
    }
    
    public void limpiarPacientesHosp(){
        fechaInicio= new Date();
        fechaFin=new Date();
        setRHospi(new RptHospitalizacion());
        
    }
    
    public void datosPacientesHospitalizados(){
        
        String mess="";
        if (fechaInicio==null)
            mess="No ha especificado la fecha de inicio";
        else
            if(fechaInicio.before(fechaFin) || fechaInicio.equals(fechaFin)) {
                try {
                    datosHospitalizacion=getRHospi().getPacientesHospitalizados(fechaInicio,fechaFin);
                    
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else
                mess="La fecha final del periodo debe ser posterior a la fecha de inicio";
        
        if (!mess.equals("")){
            FacesContext context= FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Pacientes hospitalizados",mess));
        }
    }
    
    public void validaFecha(){
        String mess="";
        if (fechaInicio==null)
            mess="No ha especificado la fecha de inicio";
        else
            if (fechaFin!=null)
                if (fechaInicio.compareTo(fechaFin)>0)
                    mess="La fecha final del periodo debe ser posterior a la fecha de inicio";
        if (!mess.equals("")){
            FacesContext context= FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Pacientes hospitalizados",mess));
        }
    }

    /**
     * @return the fechaInicio
     */
    public Date getFechaInicio() {
        return fechaInicio;
    }

    /**
     * @param fechaInicio the fechaInicio to set
     */
    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    /**
     * @return the fechaFin
     */
    public Date getFechaFin() {
        return fechaFin;
    }

    /**
     * @param fechaFin the fechaFin to set
     */
    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    /**
     * @return the oHospi
     */
    public RptHospitalizacion getRHospi() {
        return oRHospi;
    }

    /**
     * @param oHospi the oHospi to set
     */
    public void setRHospi(RptHospitalizacion oHospi) {
        this.oRHospi = oHospi;
    }

    /**
     * @return the datosHospitalizacion
     */
    public RptHospitalizacion[] getDatosHospitalizacion() {
        return datosHospitalizacion;
    }

    /**
     * @param datosHospitalizacion the datosHospitalizacion to set
     */
    public void setDatosHospitalizacion(RptHospitalizacion[] datosHospitalizacion) {
        this.datosHospitalizacion = datosHospitalizacion;
    }
    
    
}
