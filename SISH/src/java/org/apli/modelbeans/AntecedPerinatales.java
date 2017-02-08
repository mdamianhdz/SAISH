package org.apli.modelbeans;

import java.io.Serializable;
import java.util.Vector;
import org.apli.AD.AccesoDatos;

/**
 * Antecedentes perinatales en el caso de pediatr?a
 *
 * @author BAOZ
 * @version 1.0
 * @created 13-mar-2014 10:56:08 a.m.
 */
public class AntecedPerinatales implements Serializable {

    /**
     * Peso al nacer, en gramos
     */
    private int nPeso;
    /**
     * Talla al nacer, en cent?metros
     */
    private int nTalla;
    /**
     * Indica si el embarazo fue controlado o no S = S? N = No X = No se sabe
     */
    private String sEmbControlado;
    /**
     * Indica si el embarazo fue normal o no S = S? N = No X = No se sabe
     */
    private String sEmbNormal;
    /**
     * Indica si el reci?n nacido fue sano o no S = S? N = No X = No se sabe
     */
    private String sPartoNormal;
    /**
     * Indica si el parto fue normal o no S = S? N = No X = No se sabe
     */
    private String sNeonatoSano;
    /**
     * Observaciones respecto a los antecedentes perinatales
     */
    private String sObs;
    private String stipoparto;
    private Diagnostico oDiag;
    private String sapgar;
    private int nperimcefal;
    private boolean bSano = true;
    private AccesoDatos oAD;

    public AntecedPerinatales() {
        oDiag = new Diagnostico();
    }

    public void finalize() throws Throwable {
    }

    public int getPeso() {
        return nPeso;
    }

    public void setPeso(int nPeso) {
        this.nPeso = nPeso;
    }

    public int getTalla() {
        return nTalla;
    }

    public void setTalla(int nTalla) {
        this.nTalla = nTalla;
    }

    public String getEmbControlado() {
        return sEmbControlado;
    }

    public void setEmbControlado(String sEmbControlado) {
        this.sEmbControlado = sEmbControlado;
    }

    public String getEmbNormal() {
        return sEmbNormal;
    }

    public void setEmbNormal(String sEmbNormal) {
        this.sEmbNormal = sEmbNormal;
    }

    public String getPartoNormal() {
        return sPartoNormal;
    }

    public void setPartoNormal(String sPartoNormal) {
        this.sPartoNormal = sPartoNormal;
    }

    public String getNeonatoSano() {
        return sNeonatoSano;
    }

    public void setNeonatoSano(String sNeonatoSano) {
        this.sNeonatoSano = sNeonatoSano;
    }

    public String getObs() {
        return sObs;
    }

    public void setObs(String sObs) {
        this.sObs = sObs;
    }

    public AccesoDatos getAD() {
        return oAD;
    }

    public void setAD(AccesoDatos oAD) {
        this.oAD = oAD;
    }

    public String guardaAntecedentesPerinat(int nFolio, AntecedPerinatales oAP) throws Exception {
        Vector rst = null;
        String sQuery = "";

        if (nFolio == 0 || oAP == null) {
            throw new Exception("Historia Clínica. Guarda Antecedentes Perinatales: Error de programación. Faltan datos.");
        } else {
            sQuery = "select * from guardaantecedentesperinat(" + nFolio + "," + oAP.getPeso()
                    + "::int2," + oAP.getTalla() + "::int2,'" + oAP.getEmbControlado() + "'::char(1),'" + oAP.getEmbNormal() + "'::char(1),'"
                    + oAP.getNeonatoSano() + "'::char(1),'" + oAP.getObs() + "','" + oAP.getPartoNormal() + "'::char(1),'" + oAP.getTipoparto() + "'::character(1),'" + oAP.getDiag().getCve() + "'::character(6),'" + oAP.getApgar() + "'::character(5)," + oAP.getPerimcefal() + "::int2);";
            if (getAD() == null) {
                setAD(new AccesoDatos());
                getAD().conectar();
                rst = getAD().ejecutarConsulta(sQuery);
                getAD().desconectar();
                setAD(null);
            }
        }
        return "" + rst.get(0);
    }

    public String getQueryGuardaAntecedentesPerinat(int nFolio, AntecedPerinatales oAP) throws Exception {
        Vector rst = null;
        String sQuery = "";

        if (nFolio == 0 || oAP == null) {
            throw new Exception("Historia Clínica. Guarda Antecedentes Perinatales: Error de programación. Faltan datos.");
        } else {
            String sDiagn = "";
            if (oAP.getDiag() == null) {
                sDiagn = "null";
            } else {
                sDiagn = "'" + oAP.getDiag().getCve() + "'::character(6)";
            }
            sQuery = "select * from guardaantecedentesperinat(" + nFolio + "," + oAP.getPeso()
                    + "::int2," + oAP.getTalla() + "::int2,'" + oAP.getEmbControlado() + "'::char(1),'" + oAP.getEmbNormal() + "'::char(1),'"
                    + oAP.getNeonatoSano() + "'::char(1),'" + oAP.getObs() + "'::character varying,'" + oAP.getPartoNormal() + "'::char(1),'" + oAP.getTipoparto() + "'::character(1)," + sDiagn + ",'" + oAP.getApgar() + "'::character(5)," + oAP.getPerimcefal() + "::int2);";
        }
        return sQuery;
    }

    public String getTipoparto() {
        return stipoparto;
    }

    public void setTipoparto(String stipoparto) {
        this.stipoparto = stipoparto;
    }

    public Diagnostico getDiag() {
        return oDiag;
    }

    public void setDiag(Diagnostico scvediag) {
        this.oDiag = scvediag;
    }

    public String getApgar() {
        return sapgar;
    }

    public void setApgar(String sapgar) {
        this.sapgar = sapgar;
    }

    public int getPerimcefal() {
        return nperimcefal;
    }

    public void setPerimcefal(int nperimcefal) {
        this.nperimcefal = nperimcefal;
    }

    public boolean getSano() {
        return bSano;
    }

    public void setSano(boolean bSano) {
        this.bSano = bSano;
    }
}