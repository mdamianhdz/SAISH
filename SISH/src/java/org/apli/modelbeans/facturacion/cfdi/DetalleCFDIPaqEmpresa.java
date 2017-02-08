/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apli.modelbeans.facturacion.cfdi;

/**
 *
 * @author Isa
 */
public class DetalleCFDIPaqEmpresa {

    String sNombreSerie;
    int nFolio;
    int nLinCfdi;
    int nSec;
    int nCveConcepIngrPaq;
    int nCveConcepIngr;
    public String agregarQueryInsertarDetalleCFDIPaqEmpresa(){
        return "select * from insertadetalleCFDIpaq('"+this.sNombreSerie+"'::character varying,"+this.nFolio+"::int2,"+
                this.nLinCfdi+"::int2,"+this.nSec+"::int2,"+this.nCveConcepIngrPaq+"::int2,"+nCveConcepIngr+"::int2);";
    }
    public String getNombreSerie() {
        return sNombreSerie;
    }

    public void setNombreSerie(String sNombreSerie) {
        this.sNombreSerie = sNombreSerie;
    }

    public int getFolio() {
        return nFolio;
    }

    public void setFolio(int nFolio) {
        this.nFolio = nFolio;
    }

    public int getLinCfdi() {
        return nLinCfdi;
    }

    public void setLinCfdi(int nCinCfdi) {
        this.nLinCfdi = nCinCfdi;
    }

    public int getSec() {
        return nSec;
    }

    public void setSec(int nSec) {
        this.nSec = nSec;
    }

    public int getCveConcepIngrPaq() {
        return nCveConcepIngrPaq;
    }

    public void setCveConcepIngrPaq(int nCveConcepIngrPaq) {
        this.nCveConcepIngrPaq = nCveConcepIngrPaq;
    }

    public int getCveConcepIngr() {
        return nCveConcepIngr;
    }

    public void setCveConcepIngr(int nCveConcepIngr) {
        this.nCveConcepIngr = nCveConcepIngr;
    }
}
