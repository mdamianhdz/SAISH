package org.apli.modelbeans.cobranza;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import org.apli.AD.AccesoDatos;
import org.apli.modelbeans.CiudadCP;
import org.apli.modelbeans.ConceptoIngreso;
import org.apli.modelbeans.EpisodioMedico;
import org.apli.modelbeans.Paciente;
import org.apli.modelbeans.ServicioPrestado;
import org.apli.modelbeans.Valida;

/**
 * Representa el crédito otorgado a particulares (pagó parcialmente su cuenta
 * y debe completarla posteriormente)
 * @author BAOZ
 */
public class CreditoParticular implements Serializable{
    private int nIdCredPart;
    private EpisodioMedico oEpiMed;
    private float nMontoCred;
    private String sResponsableGastos;
    private String sCalleYNum;
    private String sOtraCiudad;
    private String sColonia;
    private CiudadCP oCdCP;
    private String sTelCasa;
    private String sTelCelular;
    private String sCorreoElectronico;
    private String sObservaciones;
    private float nPctInteres;
    private ServicioPrestado oServPres;
    private List<AvalCredParticular> arrAvalCredPart;
    private List<PagareCredPart> arrPagareCredPart;
    private AccesoDatos oAD;
    
    //transient
    private boolean bPagado;
    private float nMontoPagado;
    private float nMontoCuentaOri;
    
    public List<CreditoParticular> buscaTodosPorFiltro(Date dIni, Date dFin) throws Exception{
    List<CreditoParticular> lRet = null;
    
    Vector rst = null, vRowTemp=null;
    String sQuery = "";
    Valida oVal = new Valida(); 
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sQuery = "SELECT * FROM buscaRptCredPart(null::integer, "+
                    (dIni==null?"null":"'"+sdf.format(dIni)+"'") + "::timestamp without time zone, " +
                    (dFin==null?"null":"'"+sdf.format(dFin)+"'") + "::timestamp without time zone " +
                    "); ";
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
                    CreditoParticular oCred = new CreditoParticular();
                    AvalCredParticular oAval = new AvalCredParticular();
                    PagareCredPart oPag = new PagareCredPart();
                    oCred.setEpiMed(new EpisodioMedico());
                    oCred.setCdCP(new CiudadCP());
                    oCred.setServPres(new ServicioPrestado());
                    oCred.setIdCredPart(((Double)vRowTemp.elementAt(0)).intValue());
                    oCred.getEpiMed().setCveepisodio(((Double)vRowTemp.
                            elementAt(1)).intValue());
                    oCred.setMontoCred(((Double)vRowTemp.elementAt(2)).floatValue());
                    oCred.setResponsableGastos((String)vRowTemp.elementAt(3));
                    oCred.setCalleYNum((String)vRowTemp.elementAt(4));
                    oCred.setOtraCiudad((String)vRowTemp.elementAt(5));
                    oCred.setColonia((String)vRowTemp.elementAt(6));
                    oCred.getCdCP().setCveCd((String)vRowTemp.elementAt(7));
                    oCred.getCdCP().setCveMun((String)vRowTemp.elementAt(8));
                    oCred.getCdCP().setCveCd((String)vRowTemp.elementAt(9));
                    oCred.getCdCP().setCP((String)vRowTemp.elementAt(10));
                    oCred.setTelCasa((String)vRowTemp.elementAt(11));
                    oCred.setTelCelular((String)vRowTemp.elementAt(12));
                    oCred.setCorreoElectronico((String)vRowTemp.elementAt(13));
                    oCred.setObservaciones((String)vRowTemp.elementAt(14));
                    oCred.setPctInteres(((Double)vRowTemp.elementAt(15)).floatValue());
                    oCred.getServPres().setIdFolio((String)vRowTemp.elementAt(16));
                    //17 es la fecha de registro
                    oCred.setPagado(oVal.BinarioStringToBoolean(
                            ((String)vRowTemp.elementAt(18))));
                    oCred.setMontoPagado(((Double)vRowTemp.elementAt(19)).floatValue());
                    //20 es paciente
                    oCred.getServPres().setPaciente(new Paciente());
                    oCred.getServPres().getPaciente().setFolioPac(((Double)vRowTemp.elementAt(20)).intValue());
                    oCred.setMontoCuentaOri(((Double)vRowTemp.elementAt(21)).floatValue());
                    oCred.getServPres().setEpisodioMedico(oCred.getEpiMed());
                    oCred.getServPres().setConcepPrestado(new ConceptoIngreso());
                    if (oCred.getServPres().getIdFolio().equals(""))
                        oCred.getServPres().getConcepPrestado().setDescripConcep("CUENTA DE HOSPITAL");
                    else
                        oCred.getServPres().getConcepPrestado().setDescripConcep("SERVICIOS EXTERNOS");
                    oAval.setCredPart(oCred);
                    oPag.setCredPart(oCred);
                    oCred.setAvalesCredPart(oAval.buscaTodosPorCredPart());
                    oCred.setPagaresCredPart(oPag.buscaTodosPorCredPart());
                    oCred.getCdCP().buscaDatosPorCP();     
                    Paciente o=oCred.getServPres().getPaciente().buscaPaciente();
                    oCred.getServPres().setPaciente(o);
                    lRet.add(oCred);
                }
            }
        
        return lRet;
    }
    
    public List<CreditoParticular> buscaTodosPorPaciente() throws Exception{
    List<CreditoParticular> lRet = null;
    Vector rst = null, vRowTemp=null;
    String sQuery = "";
    Valida oVal = new Valida(); 
        if (this.getServPres()==null ||
            this.getServPres().getPaciente()==null ||
            this.getServPres().getPaciente().getFolioPac()==0)
            throw new Exception("CreditoParticular.buscaTodosPorPaciente: faltan datos");
        else{
            sQuery = "SELECT * FROM buscaTodosPorPacCredPart("+
                    this.getServPres().getPaciente().getFolioPac() + "); ";
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
                    CreditoParticular oCred = new CreditoParticular();
                    AvalCredParticular oAval = new AvalCredParticular();
                    PagareCredPart oPag = new PagareCredPart();
                    oCred.setEpiMed(new EpisodioMedico());
                    oCred.setCdCP(new CiudadCP());
                    oCred.setServPres(new ServicioPrestado());
                    oCred.setIdCredPart(((Double)vRowTemp.elementAt(0)).intValue());
                    oCred.getEpiMed().setCveepisodio(((Double)vRowTemp.
                            elementAt(1)).intValue());
                    oCred.setMontoCred(((Double)vRowTemp.elementAt(2)).floatValue());
                    oCred.setResponsableGastos((String)vRowTemp.elementAt(3));
                    oCred.setCalleYNum((String)vRowTemp.elementAt(4));
                    oCred.setOtraCiudad((String)vRowTemp.elementAt(5));
                    oCred.setColonia((String)vRowTemp.elementAt(6));
                    oCred.getCdCP().setCveCd((String)vRowTemp.elementAt(7));
                    oCred.getCdCP().setCveMun((String)vRowTemp.elementAt(8));
                    oCred.getCdCP().setCveCd((String)vRowTemp.elementAt(9));
                    oCred.getCdCP().setCP((String)vRowTemp.elementAt(10));
                    oCred.setTelCasa((String)vRowTemp.elementAt(11));
                    oCred.setTelCelular((String)vRowTemp.elementAt(12));
                    oCred.setCorreoElectronico((String)vRowTemp.elementAt(13));
                    oCred.setObservaciones((String)vRowTemp.elementAt(14));
                    oCred.setPctInteres(((Double)vRowTemp.elementAt(15)).floatValue());
                    oCred.getServPres().setIdFolio((String)vRowTemp.elementAt(16));
                    //17 es la fecha de registro
                    oCred.setPagado(oVal.BinarioStringToBoolean(
                            ((String)vRowTemp.elementAt(18))));
                    oCred.setMontoPagado(((Double)vRowTemp.elementAt(19)).floatValue());
                    oCred.getServPres().setEpisodioMedico(oCred.getEpiMed());
                    oAval.setCredPart(oCred);
                    oPag.setCredPart(oCred);
                    oCred.setAvalesCredPart(oAval.buscaTodosPorCredPart());
                    oCred.setPagaresCredPart(oPag.buscaTodosPorCredPart());
                    oCred.getCdCP().buscaDatosPorCP();                    
                    lRet.add(oCred);
                }
            }
        }
        return lRet;
    }
    
    public String actualizaCreditoParticular(int nSecPagare) throws Exception{
    Vector rst = null;
    String sQuery = "", sRet = "";
        if (this.nIdCredPart==0 || nSecPagare==0 ||
            this.arrPagareCredPart.isEmpty() ||
            this.arrPagareCredPart.get(nSecPagare-1).getMontoPagare()==0)
            throw new Exception("CreditoParticular.actualizaCreditoParticular: faltan datos");
        else{
            sQuery = "SELECT * FROM actualizaCredParticular("+
                    this.nIdCredPart + ", " + nSecPagare + "::smallint, "+
                    this.arrPagareCredPart.get(nSecPagare-1).getMontoPagare() + ");";
            System.out.println(sQuery);
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
                sRet = (String)((Vector)rst.elementAt(0)).elementAt(0);
            }
        }
        return sRet;
    }
    
    public String guardaCreditoParticular(float nMontoPrimero) throws Exception{
    Vector rst = null;
    String sQuery = "", sRet = "", sCalleYNumeroAval="ARRAY[", 
            sNombreOtraCiudadAval="ARRAY[", sColoniaAval="ARRAY[",
            sCveEdoAval="ARRAY[", sCveMunAval="ARRAY[", sNumCdAval="ARRAY[",
            sCPAval="ARRAY[", sTelCasaAval="ARRAY[", sTelCelularAval="ARRAY[",
            sCorreoElectronicoAval="ARRAY[", sNombreAval="ARRAY[", 
            sMontoPagare="ARRAY[", sFecEsperaPagoPagare="ARRAY[";
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Valida oVal = new Valida(); 
    int i = 0;
        if (this.oEpiMed == null || 
            this.getServPres()==null ||
            this.getServPres().getPaciente()==null ||
            this.getServPres().getPaciente().getFolioPac()==0 ||
            this.arrAvalCredPart==null || this.arrAvalCredPart.isEmpty() ||
            this.arrPagareCredPart==null || this.arrPagareCredPart.isEmpty() ||
            nMontoPrimero <= 0.0f)
            throw new Exception("CreditoParticular.guardaCreditoParticular: faltan datos");
        else{
            sQuery = "SELECT * FROM guardaCredParticular("+
                    this.getServPres().getPaciente().getFolioPac() + ", "+
                    this.oEpiMed.getCveepisodio() + ", "+
                    this.nMontoCred + ", " + 
                    oVal.cadenaParaBase(this.sResponsableGastos)+ ", " + 
                    oVal.cadenaParaBase(this.sCalleYNum) + ", " +
                    oVal.cadenaParaBase(this.sOtraCiudad) + ", " +
                    oVal.cadenaParaBase(this.sColonia) + ", " +
                    oVal.cadenaParaBase(this.oCdCP.getCveEdo()) + "::char(5), " +
                    oVal.cadenaParaBase(this.oCdCP.getCveMun()) + "::char(5), " +
                    oVal.cadenaParaBase(this.oCdCP.getCveCd()) + "::char(5), " +
                    oVal.cadenaParaBase(this.oCdCP.getCP()) + "::char(5), " +
                    oVal.cadenaParaBase(this.sTelCasa) + ", " +
                    oVal.cadenaParaBase(this.sTelCelular) + ", " +
                    oVal.cadenaParaBase(this.sCorreoElectronico) + ", " +
                    oVal.cadenaParaBase(this.sObservaciones) + ", " +
                    this.nPctInteres + ", " +
                    oVal.cadenaParaBase(this.oServPres.getIdFolio()) + ", " +
                    nMontoPrimero + ", " ;
            for (i=0; i< this.arrAvalCredPart.size(); i++){
                sCalleYNumeroAval = sCalleYNumeroAval + oVal.cadenaParaBase(this.arrAvalCredPart.get(i).getCalleYNum())+",";
                sNombreOtraCiudadAval = sNombreOtraCiudadAval+
                        oVal.cadenaParaBase(this.arrAvalCredPart.get(i).getOtraCiudad())+",";
                sColoniaAval = sColoniaAval + oVal.cadenaParaBase(this.arrAvalCredPart.get(i).getColonia())+",";
                sCveEdoAval = sCveEdoAval + oVal.cadenaParaBase(this.arrAvalCredPart.get(i).getCdCP().getCveEdo())+",";
                sCveMunAval = sCveMunAval + oVal.cadenaParaBase(this.arrAvalCredPart.get(i).getCdCP().getCveMun())+",";
                sNumCdAval = sNumCdAval+ oVal.cadenaParaBase(this.arrAvalCredPart.get(i).getCdCP().getCveCd())+",";
                sCPAval = sCPAval + oVal.cadenaParaBase(this.arrAvalCredPart.get(i).getCdCP().getCP())+",";
                sTelCasaAval = sTelCasaAval + 
                        oVal.cadenaParaBase(this.arrAvalCredPart.get(i).getTelCasa())+",";
                sTelCelularAval = sTelCelularAval +
                        oVal.cadenaParaBase(this.arrAvalCredPart.get(i).getTelCelular())+",";
                sCorreoElectronicoAval = sCorreoElectronicoAval +
                        oVal.cadenaParaBase(this.arrAvalCredPart.get(i).getCorreoElectronico())+",";
                sNombreAval = sNombreAval +
                        oVal.cadenaParaBase(this.arrAvalCredPart.get(i).getNombre())+",";
            }
            sQuery = sQuery + 
                   sCalleYNumeroAval.substring(0, sCalleYNumeroAval.length()-1) + "]::VARCHAR(35)[], "+
                   sNombreOtraCiudadAval.substring(0, sNombreOtraCiudadAval.length()-1) + "]::VARCHAR(50)[], "+
                   sColoniaAval.substring(0, sColoniaAval.length()-1)+ "]::VARCHAR(100)[],"+
                   sCveEdoAval.substring(0, sCveEdoAval.length()-1)+ "]::CHAR(5)[], "+
                   sCveMunAval.substring(0, sCveMunAval.length()-1)+ "]::CHAR(5)[], "+
                   sNumCdAval.substring(0, sNumCdAval.length()-1) +"]::CHAR(5)[], " +
                   sCPAval.substring(0, sCPAval.length()-1)+"]::CHAR(5)[], " +
                   sTelCasaAval.substring(0, sTelCasaAval.length()-1) + "]::VARCHAR(13)[], "+
                   sTelCelularAval.substring(0, sTelCelularAval.length()-1) + "]::VARCHAR(13)[], "+
                   sCorreoElectronicoAval.substring(0, sCorreoElectronicoAval.length()-1) + "]::VARCHAR(60)[], "+
                   sNombreAval.substring(0, sNombreAval.length()-1) + "]::VARCHAR(100)[], ";
            for (i=0; i< this.arrPagareCredPart.size(); i++){
                sMontoPagare = sMontoPagare + this.arrPagareCredPart.get(i).getMontoPagare()+",";
                sFecEsperaPagoPagare = sFecEsperaPagoPagare +"'"+
                        sdf.format(this.arrPagareCredPart.get(i).getEsperadaPago()) + "',";
            }
            sQuery = sQuery +
                    sMontoPagare.substring(0, sMontoPagare.length()-1) + "]::NUMERIC(10,2)[], "+
                    sFecEsperaPagoPagare.substring(0, sFecEsperaPagoPagare.length()-1)+"]::DATE[]);";
            System.out.println(sQuery);
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
                sRet = (String)((Vector)rst.elementAt(0)).elementAt(0);
            }
        }
        return sRet;
    }
    
    /**
     * @return the nIdCredPart
     */
    public int getIdCredPart() {
        return nIdCredPart;
    }

    /**
     * @param nIdCredPart the nIdCredPart to set
     */
    public void setIdCredPart(int nIdCredPart) {
        this.nIdCredPart = nIdCredPart;
    }

    /**
     * @return the oEpi
     */
    public EpisodioMedico getEpiMed() {
        return oEpiMed;
    }

    /**
     * @param oEpi the oEpi to set
     */
    public void setEpiMed(EpisodioMedico oEpi) {
        this.oEpiMed = oEpi;
    }

    /**
     * @return the nMontoCred
     */
    public float getMontoCred() {
        return nMontoCred;
    }

    /**
     * @param nMontoCred the nMontoCred to set
     */
    public void setMontoCred(float nMontoCred) {
        this.nMontoCred = nMontoCred;
    }

    /**
     * @return the sResponsableGastos
     */
    public String getResponsableGastos() {
        return sResponsableGastos;
    }

    /**
     * @param sResponsableGastos the sResponsableGastos to set
     */
    public void setResponsableGastos(String sResponsableGastos) {
        this.sResponsableGastos = sResponsableGastos;
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
     * @return the sObservaciones
     */
    public String getObservaciones() {
        return sObservaciones;
    }

    /**
     * @param sObservaciones the sObservaciones to set
     */
    public void setObservaciones(String sObservaciones) {
        this.sObservaciones = sObservaciones;
    }

    /**
     * @return the nPctInteres
     */
    public float getPctInteres() {
        return nPctInteres;
    }

    /**
     * @param nPctInteres the nPctInteres to set
     */
    public void setPctInteres(float nPctInteres) {
        this.nPctInteres = nPctInteres;
    }

    /**
     * @return the oServPres
     */
    public ServicioPrestado getServPres() {
        return oServPres;
    }

    /**
     * @param oServPres the oServPres to set
     */
    public void setServPres(ServicioPrestado oServPres) {
        this.oServPres = oServPres;
    }

    /**
     * @return the arrAvalCredPart
     */
    public List<AvalCredParticular> getAvalesCredPart() {
        return arrAvalCredPart;
    }

    /**
     * @param arrAvalCredPart the arrAvalCredPart to set
     */
    public void setAvalesCredPart(List<AvalCredParticular> arrAvalCredPart) {
        this.arrAvalCredPart = arrAvalCredPart;
    }

    /**
     * @return the arrPagareCredPart
     */
    public List<PagareCredPart> getPagaresCredPart() {
        return arrPagareCredPart;
    }

    /**
     * @param arrPagareCredPart the arrPagareCredPart to set
     */
    public void setPagaresCredPart(List<PagareCredPart> arrPagareCredPart) {
        this.arrPagareCredPart = arrPagareCredPart;
    }
    
    public boolean getPagado(){
        return bPagado;
    }
    public void setPagado(boolean value){
        this.bPagado = value;
    }
    
    public float getMontoPagado(){
        return this.nMontoPagado;
    }
    public void setMontoPagado(float value){
        this.nMontoPagado = value;
    }
    
    public float getMontoCuentaOri(){
        return this.nMontoCuentaOri;
    }
    public void setMontoCuentaOri(float value){
        this.nMontoCuentaOri=value;
    }
}
