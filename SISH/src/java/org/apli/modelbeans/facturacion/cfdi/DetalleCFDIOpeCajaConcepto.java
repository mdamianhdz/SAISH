/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apli.modelbeans.facturacion.cfdi;
import java.io.Serializable;

/**
 *
 * @author Isa
 */
public class DetalleCFDIOpeCajaConcepto implements Serializable{
    private String sNombreSerie;
    private int nFolio;
    private int nLinCfdi;
    private int nFolioCaja;
    private int nSec;
    private String sTipo;       //Coaseguro o deducible
    private int nIdTipo;        //Id de la tabla coaseguro o deducible

    public int getIdTipo() {
        return nIdTipo;
    }

    public void setIdTipo(int nIdTipo) {
        this.nIdTipo = nIdTipo;
    }

    public String getTipo() {
        return sTipo;
    }

    public void setTipo(String sTipo) {
        this.sTipo = sTipo;
    }
    public String agregarQueryModificarOpeCaja(){
        return "Select * from modificaroperacioncajaporcfdi("+this.getFolioCaja()+");";
    }
    public String agregarQueryInsertarDetalleCFDIOpeCajaConc(){
        return "Select * from insertaDetalleCFDIOpeCajaConcepto('"+getNombreSerie()+"'::character varying,"+getFolio()
                + "::int2,"+getLinCfdi()+"::int8,"+getFolioCaja()+"::int8,"+getSec()+"::int8);";
    }
    public int getFolio() {
        return nFolio;
    }

    public void setFolio(int nFolio) {
        this.nFolio = nFolio;
    }

    public int getFolioCaja() {
        return nFolioCaja;
    }

    public void setFolioCaja(int nFolioCaja) {
        this.nFolioCaja = nFolioCaja;
    }

    public int getLinCfdi() {
        return nLinCfdi;
    }

    public void setLinCfdi(int nLinCfdi) {
        this.nLinCfdi = nLinCfdi;
    }

    public int getSec() {
        return nSec;
    }

    public void setSec(int nSec) {
        this.nSec = nSec;
    }

    public String getNombreSerie() {
        return sNombreSerie;
    }

    public void setNombreSerie(String sNombreSerie) {
        this.sNombreSerie = sNombreSerie;
    }
}
