/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apli.modelbeans.facturacion.cfdi;
import java.io.Serializable;
import java.util.Vector;
import org.apli.AD.AccesoDatos;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Isabel Espinoza
 */
public class DetalleCFDIServicio  implements Serializable{
    private String sNombreSerie;
    private int nFolio;
    private int nLinCfdi;
    private String sIdFolioServPres;
    protected AccesoDatos oAD = null;
    String sQuery;
    public String agregarQueryInsertarDetalleCFDIServicio(){
        return "select * from insertadetallecfdiserv('"+this.sNombreSerie+"'::character varying,"+this.nFolio+"::"
                + "int2,"+this.nLinCfdi+"::int2,'"+this.sIdFolioServPres+"'::character varying);";
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

    public void setLinCfdi(int nLinCfdi) {
        this.nLinCfdi = nLinCfdi;
    }
    public String getNombreSerie() {
        return sNombreSerie;
    }

    public void setNombreSerie(String sNombreSerie) {
        this.sNombreSerie = sNombreSerie;
    }
    public String getIdFolioServPres() {
        return sIdFolioServPres;
    }

    public void setIdFolioServPres(String sIdFolioServPres) {
        this.sIdFolioServPres = sIdFolioServPres;
    }
}
