/*
 * NOTA: CLASE NO PERSISTENTE
 */

package org.apli.modelbeans;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @author HumbertMarin
 */
public class DistribucionRegalias implements Serializable{
    
    private PersonalHospitalario oPersonal;
    private Date dFecha;
    private List<ConceptoIngreso> conceptos;
    private List<Float> numConceptos;  
    private float nImporte;
    private float nImporteRegalia;
    private String sSituacion;
    
    public DistribucionRegalias(){
        
    }

    public PersonalHospitalario getPersonal() {
        return oPersonal;
    }
    public void setPersonal(PersonalHospitalario personal) {
        this.oPersonal = personal;
    }
    
    public Date getFecha() {
        return dFecha;
    }
    public void setFecha(Date dFecha) {
        this.dFecha = dFecha;
    }

    public List<ConceptoIngreso> getConceptos() {
        return conceptos;
    }
    public void setConceptos(List<ConceptoIngreso> conceptos) {
        this.conceptos = conceptos;
    }

    public List<Float> getNumConceptos() {
        return numConceptos;
    }
    public void setNumConceptos(List<Float> numConceptos) {
        this.numConceptos = numConceptos;
    }

    public float getImporte() {
        return nImporte;
    }
    public void setImporte(float nImporte) {
        this.nImporte = nImporte;
    }

    public float getImporteRegalia() {
        return nImporteRegalia;
    }
    public void setImporteRegalia(float nImporteRegalia) {
        this.nImporteRegalia = nImporteRegalia;
    }

    public String getSituacion() {
        return sSituacion;
    }
    public void setSituacion(String sSituacion) {
        this.sSituacion = sSituacion;
    }

}