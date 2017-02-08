package org.apli.modelbeans;

import java.io.Serializable;
import org.apli.AD.AccesoDatos;

/**
 *
 * @author JRuiz
 */
public class ServicioMedico implements Serializable {

    private float fMonto;
    private AccesoDatos oAD = null;
    private int nCveConcep;

    public ServicioMedico(AccesoDatos poAD)  {
        oAD = poAD;
    }

    public ServicioMedico() {
    }

    public float getMonto() {
        return fMonto;
    }

    public void setMonto(float fMonto) {
        this.fMonto = fMonto;
    }
    public int getCveConcep() {
        return nCveConcep;
    }

    public void setCveConcep(int nCveConcep) {
        this.nCveConcep = nCveConcep;
    }

}
