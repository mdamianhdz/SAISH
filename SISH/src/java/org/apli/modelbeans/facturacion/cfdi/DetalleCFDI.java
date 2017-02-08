/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apli.modelbeans.facturacion.cfdi;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Vector;
import org.apli.AD.AccesoDatos;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Isa
 */
public class DetalleCFDI  implements Serializable{
    private int nId;
    private String sNombreSerie;
    private int nFolio;
    private int nLinCfdi;
    private String sDescrip;
    private String sUnidad;
    private int nCant;
    private BigDecimal nMonto;
    protected AccesoDatos oAD = null;
    String sQuery="";
    public String buscaProxFolioDetalleCFDI() throws Exception{
        Vector rst = null;
        if (getAD() == null){
            setAD(new AccesoDatos());
            getAD().conectar();
        }
        String consulta="select * from buscaproxfoliodetallecfdi();";
        rst = getAD().ejecutarConsulta(consulta);
        getAD().desconectar();
        setAD(null);
        Vector vRowTemp = (Vector)rst.elementAt(0);  
        consulta=""+((Double) vRowTemp.elementAt(0)).intValue();
        return consulta;
    }
    public String agregarQueryInsertarDetalleCFDI(){
        sQuery+="Select * from insertaDetalleCFDI('"+getNombreSerie()+"'::character varying,"+getFolio()+"::int2,"+this.getLinCfdi()+"::int8,'"+getDescrip()+"'"
                + "::character varying,"+getCant()+"::int2,"+getMonto()+",'"+this.getUnidad()+"'::character varying);";
        return sQuery;
    }
    public AccesoDatos getAD() {
        return oAD;
    }
    public void setAD(AccesoDatos oAD) {
        this.oAD = oAD;
    }
    public int getCant() {
        return nCant;
    }

    public void setCant(int nCant) {
        this.nCant = nCant;
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

    public BigDecimal getMonto() {
        return nMonto;
    }

    public void setMonto(BigDecimal nMonto) {
        this.nMonto = nMonto;
    }

    public String getDescrip() {
        return sDescrip;
    }

    public void setDescrip(String sDescrip) {
        this.sDescrip = sDescrip;
    }

    public String getNombreSerie() {
        return sNombreSerie;
    }

    public void setNombreSerie(String sNombreSerie) {
        this.sNombreSerie = sNombreSerie;
    }
    
    public int getId() {
        return nId;
    }

    public void setId(int id) {
        this.nId = id;
    }
     public String getUnidad() {
        return sUnidad;
    }

    public void setUnidad(String sUnidad) {
        this.sUnidad = sUnidad;
    }
}
