package org.apli.modelbeans;

import java.io.Serializable;

/**
 * Cat�logo de ciudades de acuerdo a SEPOMEX
 * @author BAOZ
 * @version 1.0
 * @created 13-mar-2014 10:56:14 a.m.
 */
public class Ciudad implements Serializable {

	/**
	 * Estado al que corresponde la ciudad
	 */
	private Estado oEdo;
        /**
	 * Municipio al que corresponde la ciudad
	 */
	private Municipio oMpio;
	/**
	 * Clave de la ciudad
	 */
	private String sCve;
	/**
	 * Descripci�n de la ciudad
	 */
	private String sDescrip;

	public Ciudad(){

	}

	public void finalize() throws Throwable {

	}

    public Estado getEdo() {
        return oEdo;
    }

    public void setEdo(Estado oEdo) {
        this.oEdo = oEdo;
    }

    public Municipio getMpio() {
        return oMpio;
    }

    public void setMpio(Municipio oMpio) {
        this.oMpio = oMpio;
    }

    public String getCve() {
        return sCve;
    }

    public void setCve(String sCve) {
        this.sCve = sCve;
    }

    public String getDescrip() {
        return sDescrip;
    }

    public void setDescrip(String sDescrip) {
        this.sDescrip = sDescrip;
    }
}