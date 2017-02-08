package org.apli.modelbeans.cobranza;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import org.apli.AD.AccesoDatos;

/**
 * Representa un pagaré asociado a un crédito otorgado a un particular
 * @author BAOZ
 */
public class PagareCredPart implements Serializable{
    private CreditoParticular oCredPart;
    private short nSecuencia;
    private float nMontoPagare;
    private Date dEsperadaPago;
    private String sSituacion;
    private PagareCredPart oPagareSustituido;
    private String sRazonCancela;
    private AccesoDatos oAD;
    public static String PAGARE_SIT_AUT="A";
    
    public boolean busca() throws Exception{
    boolean bRet= false;
    Vector rst = null, vRowTemp=null;
    String sQuery = "";
        if (this.oCredPart == null || this.oCredPart.getIdCredPart()==0 ||
            this.nSecuencia == 0)
            throw new Exception("PagareCredPart.busca: faltan datos");
        else{
            sQuery = "SELECT * FROM buscaLlavePagareCredPart("+
                    this.oCredPart.getIdCredPart()+", "+
                    this.nSecuencia + ");";
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
                vRowTemp = (Vector)rst.elementAt(0);
                this.nMontoPagare = ((Double)vRowTemp.elementAt(2)).floatValue();
                this.dEsperadaPago = (Date)vRowTemp.elementAt(3);
                this.sSituacion = (String)vRowTemp.elementAt(4);
                if (vRowTemp.elementAt(5)!= null){
                    this.oPagareSustituido = new PagareCredPart();
                    this.oPagareSustituido.setCredPart(oCredPart);
                    this.oPagareSustituido.setSecuencia(((Double)vRowTemp.elementAt(6)).shortValue());
                    this.sRazonCancela = (String)vRowTemp.elementAt(7);
                }
                bRet = true;
            }
        }
        return bRet;
    }
    
    public List<PagareCredPart> buscaTodosPorCredPart() throws Exception{
    List<PagareCredPart> lRet=null;
    Vector rst = null, vRowTemp=null;
    PagareCredPart oPaga=null;
    String sQuery = "";
        if (this.oCredPart == null || this.oCredPart.getIdCredPart()==0)
            throw new Exception("PagareCredPart.buscaTodosPorCredPart: faltan datos");
        else{
            sQuery = "SELECT * FROM buscaTodosPorCredPagareCredPart("+
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
                    oPaga = new PagareCredPart();
                    oPaga.nSecuencia = ((Double)vRowTemp.elementAt(1)).shortValue();
                    oPaga.nMontoPagare = ((Double)vRowTemp.elementAt(2)).floatValue();
                    oPaga.dEsperadaPago = (Date)vRowTemp.elementAt(3);
                    oPaga.sSituacion = (String)vRowTemp.elementAt(4);
                    if (vRowTemp.elementAt(5) != null){
                        oPaga.oPagareSustituido = new PagareCredPart();
                        oPaga.oPagareSustituido.setCredPart(oCredPart);
                        oPaga.oPagareSustituido.setSecuencia(
                                ((Double)vRowTemp.elementAt(6)).shortValue());
                        oPaga.sRazonCancela = (String)vRowTemp.elementAt(7);
                    }
                    lRet.add(oPaga);
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
     * @return the nMontoPagare
     */
    public float getMontoPagare() {
        return nMontoPagare;
    }

    /**
     * @param nMontoPagare the nMontoPagare to set
     */
    public void setMontoPagare(float nMontoPagare) {
        this.nMontoPagare = nMontoPagare;
    }

    /**
     * @return the dEsperadaPago
     */
    public Date getEsperadaPago() {
        return dEsperadaPago;
    }

    /**
     * @param dEsperadaPago the dEsperadaPago to set
     */
    public void setEsperadaPago(Date dEsperadaPago) {
        this.dEsperadaPago = dEsperadaPago;
    }

    /**
     * @return the sSituacion
     */
    public String getSituacion() {
        return sSituacion;
    }

    /**
     * @param sSituacion the sSituacion to set
     */
    public void setSituacion(String sSituacion) {
        this.sSituacion = sSituacion;
    }

    /**
     * @return the oPagareSustituido
     */
    public PagareCredPart getPagareSustituido() {
        return oPagareSustituido;
    }

    /**
     * @param oPagareSustituido the oPagareSustituido to set
     */
    public void setPagareSustituido(PagareCredPart oPagareSustituido) {
        this.oPagareSustituido = oPagareSustituido;
    }

    /**
     * @return the sRazonCancela
     */
    public String getRazonCancela() {
        return sRazonCancela;
    }

    /**
     * @param sRazonCancela the sRazonCancela to set
     */
    public void setRazonCancela(String sRazonCancela) {
        this.sRazonCancela = sRazonCancela;
    }
    
    
    public boolean getPorPagar(){        
        return (this.sSituacion.equals(PAGARE_SIT_AUT));
    }
}
