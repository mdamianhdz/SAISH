/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apli.modelbeans.facturacion.cfdi;
import java.io.Serializable;
/**
 *Tiene la estructura de DetalleCFDIPaquete
 * @author Isa
 */
public class DetalleCFDIPaquete  implements Serializable{
    private String sNombreSerie;
    private int nFolio;
    private int nLinCfdi;
    private int nSec;
    private int nIdPaquete;
    private int nIdPaqueteContratado;
    public String agregarQueryInsertarDetalleCFDIPaquete(){
        return "select * from insertadetalleCFDIpaq('"+this.sNombreSerie+"'::character varying,"+this.nFolio+"::int2,"+
                this.nLinCfdi+"::int2,"+this.nSec+"::int2,"+this.nIdPaquete+");";
    }
    public int getIdPaqueteContratado() {
        return nIdPaqueteContratado;
    }

    public void setIdPaqueteContratado(int nIdPaqueteContratado) {
        this.nIdPaqueteContratado = nIdPaqueteContratado;
    }
    public int getFolio() {
        return nFolio;
    }

    public void setFolio(int nFolio) {
        System.out.println("Se esta cambiando el folio");
        this.nFolio = nFolio;
    }

    public int getLinCfdi() {
        return nLinCfdi;
    }

    public void setLinCfdi(int nLinCfdi) {
        this.nLinCfdi = nLinCfdi;
    }
    public String getNombreSerie() {
        return sNombreSerie;
    }

    public void setNombreSerie(String sNombreSerie) {
        this.sNombreSerie = sNombreSerie;
    }
    public int getIdPaquete() {
        return nIdPaquete;
    }

    public void setIdPaquete(int nIdPaquete) {
        this.nIdPaquete = nIdPaquete;
    }

    public int getSec() {
        return nSec;
    }

    public void setSec(int nSec) {
        this.nSec = nSec;
    }
}
