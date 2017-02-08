package org.apli.modelbeans.cobranza;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import org.apli.AD.AccesoDatos;
import org.apli.modelbeans.CiudadCP;


/**
 * Representa la información de un aval para el caso de créditos a particulares
 * @author BAOZ
 */
public class AvalCredParticular implements Serializable{
    private CreditoParticular oCredPart;
    private short nSecuencia;
    private String sCalleYNum;
    private String sOtraCiudad;
    private String sColonia;
    private CiudadCP oCdCP;
    private String sTelCasa;
    private String sTelCelular;
    private String sCorreoElectronico;
    private String sNombre;
    private AccesoDatos oAD;
    
    public List<AvalCredParticular> buscaTodosPorCredPart() throws Exception{
    List<AvalCredParticular> lRet=null;
    Vector rst = null, vRowTemp=null;
    AvalCredParticular oAval=null;
    String sQuery = "";
        if (this.oCredPart == null || this.oCredPart.getIdCredPart()==0)
            throw new Exception("AvalCredParticular.buscaTodosPorCredPart: faltan datos");
        else{
            sQuery = "SELECT * FROM buscaTodosPorCredAvalCredPart("+
                      this.oCredPart.getIdCredPart()+");";
            if (oAD == null) {
                oAD = new AccesoDatos();
                oAD.conectar();
                rst = oAD.ejecutarConsulta(sQuery);
                oAD.desconectar();
                oAD=null;
            } else {
                rst = oAD.ejecutarConsulta(sQuery);
            }
            if (rst != null) {
                lRet = new ArrayList();
                for (int i = 0; i < rst.size(); i++) {
                    vRowTemp = (Vector) rst.elementAt(i);
                    oAval = new AvalCredParticular();
                    oAval.oCdCP = new CiudadCP();
                    //poutsCorreoElectronico
                    oAval.nSecuencia = ((Double)vRowTemp.elementAt(1)).shortValue();
                    oAval.sCalleYNum = (String)vRowTemp.elementAt(2);
                    oAval.sOtraCiudad = (String)vRowTemp.elementAt(3);
                    oAval.sColonia = (String)vRowTemp.elementAt(4);
                    oAval.oCdCP.setCveEdo((String)vRowTemp.elementAt(5));
                    oAval.oCdCP.setCveMun((String)vRowTemp.elementAt(6));
                    oAval.oCdCP.setCveCd((String)vRowTemp.elementAt(7));
                    oAval.oCdCP.setCP((String)vRowTemp.elementAt(8));
                    oAval.sTelCasa = (String)vRowTemp.elementAt(9);
                    oAval.sTelCelular = (String)vRowTemp.elementAt(10);
                    oAval.sCorreoElectronico = (String)vRowTemp.elementAt(11);
                    oAval.sNombre = (String)vRowTemp.elementAt(12);
                    oAval.getCdCP().buscaDatosPorCP();
                    lRet.add(oAval);
                }
            }
        }
        return lRet;
    }

    /**
     * @return the oCredPart
     */
    public CreditoParticular getCredPart() {
        return oCredPart;
    }

    /**
     * @param oCredPart the oCredPart to set
     */
    public void setCredPart(CreditoParticular oCredPart) {
        this.oCredPart = oCredPart;
    }

    /**
     * @return the nSecuencia
     */
    public short getSecuencia() {
        return nSecuencia;
    }

    /**
     * @param nSecuencia the nSecuencia to set
     */
    public void setSecuencia(short nSecuencia) {
        this.nSecuencia = nSecuencia;
    }

    /**
     * @return the sCalleYNum
     */
    public String getCalleYNum() {
        return sCalleYNum;
    }

    /**
     * @param sCalleYNum the sCalleYNum to set
     */
    public void setCalleYNum(String sCalleYNum) {
        this.sCalleYNum = sCalleYNum;
    }

    /**
     * @return the sOtraCiudad
     */
    public String getOtraCiudad() {
        return sOtraCiudad;
    }

    /**
     * @param sOtraCiudad the sOtraCiudad to set
     */
    public void setOtraCiudad(String sOtraCiudad) {
        this.sOtraCiudad = sOtraCiudad;
    }

    /**
     * @return the sColonia
     */
    public String getColonia() {
        return sColonia;
    }

    /**
     * @param sColonia the sColonia to set
     */
    public void setColonia(String sColonia) {
        this.sColonia = sColonia;
    }

    /**
     * @return the oCdCP
     */
    public CiudadCP getCdCP() {
        return oCdCP;
    }

    /**
     * @param oCdCP the oCdCP to set
     */
    public void setCdCP(CiudadCP oCdCP) {
        this.oCdCP = oCdCP;
    }

    /**
     * @return the sTelCasa
     */
    public String getTelCasa() {
        return sTelCasa;
    }

    /**
     * @param sTelCasa the sTelCasa to set
     */
    public void setTelCasa(String sTelCasa) {
        this.sTelCasa = sTelCasa;
    }

    /**
     * @return the sTelCelular
     */
    public String getTelCelular() {
        return sTelCelular;
    }

    /**
     * @param sTelCelular the sTelCelular to set
     */
    public void setTelCelular(String sTelCelular) {
        this.sTelCelular = sTelCelular;
    }

    /**
     * @return the sCorreoElectronico
     */
    public String getCorreoElectronico() {
        return sCorreoElectronico;
    }

    /**
     * @param sCorreoElectronico the sCorreoElectronico to set
     */
    public void setCorreoElectronico(String sCorreoElectronico) {
        this.sCorreoElectronico = sCorreoElectronico;
    }

    /**
     * @return the sNombre
     */
    public String getNombre() {
        return sNombre;
    }

    /**
     * @param sNombre the sNombre to set
     */
    public void setNombre(String sNombre) {
        this.sNombre = sNombre;
    }
}
