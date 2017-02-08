package org.apli.modelbeans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * Representa a cualquier participante del proceso (paciente o personal del
 * hospital)
 * @author BAOZ
 * @version 1.0
 * @created 07-abr-2014 01:02:32 p.m.
 */
public abstract class Persona implements Serializable {
/**
 * Fecha de nacimiento, incluye hora si se conoce (para efectos de reci�n nacidos)
 */
protected Date dNac;
/**
 * Edad en a�os, es calculada en funci�n de la fecha actual y la fecha de
 * nacimiento
 */
protected volatile int nEdad;
/**
 * Estado civil de la persona
 */
protected EstadoCivil oEdoCiv;
protected String sApellidoMaterno="";
protected String sApellidoPaterno="";
protected String sCURP;
/**
 * F = Femenino
 * M = Masculino
 */
protected char sGenero;

protected String sNombre="";
protected String sRFC;
private DatosLaborales oDatosLaborales;

    public Persona(){
        oDatosLaborales=new DatosLaborales();
    }

    public Date getNac() {
        return dNac;
    }

    public void setNac(Date dNac) {
        this.dNac = dNac;
    }

    public int getEdad() {
        if (nEdad==0 && dNac!=null)
            nEdad = calculaEdad(dNac);
        return nEdad;
    }

    public void setEdad(int nEdad) {
        this.nEdad = nEdad;
    }

    public EstadoCivil getEdoCiv() {
        return oEdoCiv;
    }

    public void setEdoCiv(EstadoCivil oEdoCiv) {
        this.oEdoCiv = oEdoCiv;
    }

    public String getApellidoMaterno() {
        return sApellidoMaterno;
    }

    public void setApellidoMaterno(String sApellidoMaterno) {
        this.sApellidoMaterno = sApellidoMaterno;
    }

    public String getApellidoPaterno() {
        return sApellidoPaterno;
    }

    public void setApellidoPaterno(String sApellidoPaterno) {
        this.sApellidoPaterno = sApellidoPaterno;
    }

    public String getCURP() {
        return sCURP;
    }

    public void setCURP(String sCURP) {
        this.sCURP = sCURP;
    }

    public char getGenero() {
        return sGenero;
    }

    public void setGenero(char sGenero) {
        this.sGenero = sGenero;
    }

    public String getNombre() {
        return sNombre;
    }
	
    public String getNombreCompleto() {
        return 
        (sApellidoPaterno==null || sApellidoPaterno.equals("")?"": " "+sApellidoPaterno)+
        (sApellidoMaterno==null || sApellidoMaterno.equals("")?"": " "+sApellidoMaterno)+
        (sNombre==null || sNombre.equals("")?"":" "+sNombre);
    }

    public void setNombre(String sNombre) {
        this.sNombre = sNombre;
    }

    public String getRFC() {
        return sRFC;
    }

    public void setRFC(String sRFC) {
        this.sRFC = sRFC;
    }

    public DatosLaborales getDatosLaborales() {
        return oDatosLaborales;
    }

    public void setDatosLaborales(DatosLaborales oDatosLaborales) {
        this.oDatosLaborales = oDatosLaborales;
    }
    
    public int calculaEdad(Date fechaNaci) {
        Calendar fechaNac = Calendar.getInstance();
        fechaNac.setTime(fechaNaci);

        Calendar fechaActual = Calendar.getInstance();

        int anios = fechaActual.get(Calendar.YEAR) - fechaNac.get(Calendar.YEAR);
        int meses = fechaActual.get(Calendar.MONTH) - fechaNac.get(Calendar.MONTH);
        int dias = fechaActual.get(Calendar.DAY_OF_MONTH) - fechaNac.get(Calendar.DAY_OF_MONTH);

        if (meses < 0 || (meses == 0 && dias < 0)) {
            anios--;
        }
        return anios;
    }
}