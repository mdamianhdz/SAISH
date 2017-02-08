/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apli.modelbeans.caja;

import java.io.Serializable;
import org.apli.AD.AccesoDatos;
import org.apli.modelbeans.OpeCajaConcepto;

/**
 *
 * @author Lily_LnBnd
 */
public class DetalleCorte implements Serializable{
    private CorteCaja oCorte;
    private OpeCajaConcepto oOpeCC;
    private AccesoDatos oAD;
    
    public DetalleCorte(){
        
    }

    public CorteCaja getCorte() {
        return oCorte;
    }

    public void setCorte(CorteCaja oCorte) {
        this.oCorte = oCorte;
    }

    public OpeCajaConcepto getOpeCC() {
        return oOpeCC;
    }

    public void setOpeCC(OpeCajaConcepto oOpeCC) {
        this.oOpeCC = oOpeCC;
    }

    public AccesoDatos getAD() {
        return oAD;
    }

    public void setAD(AccesoDatos oAD) {
        this.oAD = oAD;
    }
    
    
}
