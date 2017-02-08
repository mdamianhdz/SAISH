package org.apli.modelbeans;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author ERJI
 */
public class ConceptoIngreServicio implements Serializable {

    private Medico oMedicoHonorarios;
    private boolean pagoHono = false;
    private Date fechaInterconsulta;
    private String sFolioServ;
    
    public Medico getMedicoHonorarios() {
        return oMedicoHonorarios;
    }

    public void setMedicoHonorarios(Medico oMedicoHonorarios) {
        this.oMedicoHonorarios = oMedicoHonorarios;
    }

    public boolean getPagoHono() {
        return pagoHono;
    }

    public void setPagoHono(boolean pagoHono) {
        this.pagoHono = pagoHono;
    }

    public Date getFechaInterconsulta() {
        return fechaInterconsulta;
    }

    public void setFechaInterconsulta(Date fechaInterconsulta) {
        this.fechaInterconsulta = fechaInterconsulta;
    }

    public String getFolioServ() {
        return sFolioServ;
    }

    public void setFolioServ(String sFolioServ) {
        this.sFolioServ = sFolioServ;
    }
}
