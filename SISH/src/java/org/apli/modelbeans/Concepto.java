package org.apli.modelbeans;

import java.io.Serializable;

/**
 * Concepto de ingreso o egreso de dinero al Sanatorio
 * @author BAOZ
 * @version 1.0
 * @created 07-abr-2014 10:11:09 a.m.
 */
public class Concepto implements Serializable {
    /**
     * Clave que identifica al concepto
     */
    protected int nCveConcep;
    /**
     * Descripción del concepto
     */
    protected String sDescripConcep;
    
    public Concepto(){}
    
    public int getCveConcep() {
        return nCveConcep;
    }

    public void setCveConcep(int nCveConcep) {
        this.nCveConcep = nCveConcep;
    }
    
    public String getDescripConcep() {
        return sDescripConcep;
    }

    public void setDescripConcep(String sDescripConcep) {
        this.sDescripConcep = sDescripConcep;
    }

    public String getDescripcion() {
        return sDescripConcep;
    }

    public void setDescripcion(String sDescripcion) {
        this.sDescripConcep = sDescripcion;
    }
}