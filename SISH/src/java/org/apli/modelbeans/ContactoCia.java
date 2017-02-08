/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apli.modelbeans;

import java.io.Serializable;
import org.apli.AD.AccesoDatos;
import org.apli.modelbeans.ventaCredito.CompaniaCred;

/**
 *
 * @author Lily_LnBnd
 */
public class ContactoCia implements Serializable{
    private CompaniaCred oCompania;//*** No estoy segura de que lleve el campo
    private int nNumContacto;
    private String sNombre;
    private String sApPaterno;
    private String sApMaterno;
    private String sCorreo;
    private String sTelefono;
    private String sTipo; //M=Coordinador Médico, C=Contacto
    private AccesoDatos oAD;
    
    public ContactoCia(){
        
    }

    public CompaniaCred getCompania() {
        return oCompania;
    }

    public void setCompania(CompaniaCred oCompania) {
        this.oCompania = oCompania;
    }

    public int getNumContacto() {
        return nNumContacto;
    }

    public void setNumContacto(int nNumContacto) {
        this.nNumContacto = nNumContacto;
    }

    public String getNombre() {
        return sNombre;
    }
    
    public String getNombreCompleto() {
        return sNombre+" "+sApPaterno;
    }

    public void setNombre(String sNombre) {
        this.sNombre = sNombre;
    }

    public String getApPaterno() {
        return sApPaterno;
    }

    public void setApPaterno(String sApPaterno) {
        this.sApPaterno = sApPaterno;
    }

    public String getApMaterno() {
        return sApMaterno;
    }

    public void setApMaterno(String sApMaterno) {
        this.sApMaterno = sApMaterno;
    }

    public String getCorreo() {
        return sCorreo;
    }

    public void setCorreo(String sCorreo) {
        this.sCorreo = sCorreo;
    }

    public String getTelefono() {
        return sTelefono;
    }

    public void setTelefono(String sTelefono) {
        this.sTelefono = sTelefono;
    }

    public String getTipo() {
        return sTipo;
    }

    public void setTipo(String sTipo) {
        this.sTipo = sTipo;
    }

    public AccesoDatos getAD() {
        return oAD;
    }

    public void setAD(AccesoDatos oAD) {
        this.oAD = oAD;
    }
    
    
}
