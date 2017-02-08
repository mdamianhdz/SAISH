package org.apli.modelbeans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import org.apli.AD.AccesoDatos;

/**
 * Relaci�n de informes de paquetes que han realizado los promotores
 *
 * @author BAOZ
 * @version 1.0
 * @created 22-jul.-2014 17:15:31
 */
public class BitacoraInfoPaq {

    /**
     * Fecha y hora en que se brind� informaci�n a un prospecto de paciente
     */
    private Date dInfo;
    /**
     * Edad del prospecto
     */
    private short nEdad;
    /**
     * Identificador del registro de bit�cora, es autogenerado
     */
    private long nIdBitInfoPaq;
    /**
     * Tipo de paquete del que se dieron informes
     */
    private TipoPaquete oTipoPac;
    /**
     * Apellido materno del prospecto
     */
    private String sApellidoMaterno;
    /**
     * Apellido paterno del prospecto
     */
    private String sApellidoPaterno;
    /**
     * Descripci�n general de la cirug�a ofertada en el caso de paquetes
     * quir�rgicos personalizados
     */
    private String sCirugia;
    /**
     * G�nero (F=femenino, M=Masculino) del prospecto
     */
    private char sGenero;
    /**
     * Nombre de pila del prospecto
     */
    private String sNombre;
    private String sCorreo;
    /**
     * Observaciones respecto a la informaci�n/prospecto
     */
    private String sObs;
    private PersonalHospitalario oPersHosp;
    private String sTels;
    private String sTelCasa;
    private String sDireccion;
    private String sDondeViene;
    private String sComoSeEntero;
    protected AccesoDatos oAD = null;

    public BitacoraInfoPaq() {
        oTipoPac=new TipoPaquete();
        oPersHosp=new PersonalHospitalario();
    }
    
    public BitacoraInfoPaq(AccesoDatos poAD) {
        oAD = poAD;
    }

    public List<PersonalHospitalario> buscaTodosPromotoresPaquete() throws Exception {
        List<PersonalHospitalario> listaRegConPromotores = new ArrayList<PersonalHospitalario>();
        BitacoraInfoPaq oBiInfPaq = null;
        Vector rst = null;
        Vector<BitacoraInfoPaq> vObj = null;
        String sQuery = "";
        int i = 0;
        if (this.oTipoPac.getCve().equals("")) {
            throw new Exception("Paquete. insertaPaquete(): Error de programación. Faltan datos.");
        } else {
            sQuery = "SELECT * FROM buscaLlavebitinfopaqtipopaquete('" + this.oTipoPac.getCve() + "'::CHARACTER(1));";
            if (getAD() == null) {
                setAD(new AccesoDatos());
                getAD().conectar();
                rst = getAD().ejecutarConsulta(sQuery);
                getAD().desconectar();
                setAD(null);
            } else {
                rst = getAD().ejecutarConsulta(sQuery);
            }
            if (rst != null) {
                vObj = new Vector<BitacoraInfoPaq>();
                for (i = 0; i < rst.size(); i++) {
                    oBiInfPaq = new BitacoraInfoPaq();
                    Vector vRowTemp = (Vector) rst.elementAt(i);
                    oBiInfPaq.setIdBitInfoPaq(((Double) vRowTemp.elementAt(0)).intValue());
                    oPersHosp=new PersonalHospitalario();
                    oPersHosp.setFolioPers(((Double) vRowTemp.elementAt(1)).intValue());
                    oPersHosp=oPersHosp.buscaLlavePersonal();
                    if(oPersHosp!=null){
                        listaRegConPromotores.add(oPersHosp);
                    }
                    
                }
            }
        }
        return listaRegConPromotores;
    }
    
    public String guardaCaptacion(BitacoraInfoPaq oBit)throws Exception{
        Vector rst = null;
        String sQuery;
        
        sQuery = "select * from insertabitacorapaq('"+oBit.getPersHosp().getNombre()+"', '"+oBit.getTipoPac().getCve()+"', '"+oBit.getCirugia()+
                "', '"+oBit.getNombre()+"', '"+oBit.getApellidoPaterno()+"', '"+oBit.getApellidoMaterno()+"', '"+oBit.getGenero()+"', "+oBit.getEdad()+
                "::int2, '"+oBit.getTels()+"', '"+oBit.getDireccion()+"', '"+oBit.getObs()+"','"+oBit.getTelCasa()+"','"+oBit.getCorreo()+"','"+oBit.getDondeViene()+"','"+oBit.getComoSeEntero()+"');";     

        System.out.println(sQuery);
        if (getAD() == null){
            setAD(new AccesoDatos());
            getAD().conectar();
            rst = getAD().ejecutarConsulta(sQuery);
            getAD().desconectar();
            setAD(null);
        }else{
            rst = getAD().ejecutarConsulta(sQuery);
        }
        
        return rst.get(0).toString().substring(1, rst.get(0).toString().length()-1);
    }

    public Date getInfo() {
        return dInfo;
    }

    public void setInfo(Date dInfo) {
        this.dInfo = dInfo;
    }

    public short getEdad() {
        return nEdad;
    }

    public void setEdad(short nEdad) {
        this.nEdad = nEdad;
    }

    public long getIdBitInfoPaq() {
        return nIdBitInfoPaq;
    }

    public void setIdBitInfoPaq(long nIdBitInfoPaq) {
        this.nIdBitInfoPaq = nIdBitInfoPaq;
    }

    public TipoPaquete getTipoPac() {
        return oTipoPac;
    }

    public void setTipoPac(TipoPaquete oTipoPac) {
        this.oTipoPac = oTipoPac;
    }

    public String getApellidoMaterno() {
        return sApellidoMaterno;
    }

    public void setApellidoMaterno(String sApellidoMaterno) {
        this.sApellidoMaterno = sApellidoMaterno;
    }

    public String getApellidoPaterno() {
        return sApellidoPaterno;
    }

    public void setApellidoPaterno(String sApellidoPaterno) {
        this.sApellidoPaterno = sApellidoPaterno;
    }

    public String getCirugia() {
        return sCirugia;
    }

    public void setCirugia(String sCirugia) {
        this.sCirugia = sCirugia;
    }

    public char getGenero() {
        return sGenero;
    }

    public void setGenero(char sGenero) {
        this.sGenero = sGenero;
    }

    public String getNombre() {
        return sNombre;
    }

    public void setNombre(String sNombre) {
        this.sNombre = sNombre;
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

    public PersonalHospitalario getPersHosp() {
        return oPersHosp;
    }

    public void setPersHosp(PersonalHospitalario oPersHosp) {
        this.oPersHosp = oPersHosp;
    }
    
    public String getTels() {
        return sTels;
    }

    public void setTels(String sTels) {
        this.sTels = sTels;
    }
    
    public String getTelCasa() {
        return sTelCasa;
    }

    public void setTelCasa(String sTels) {
        this.sTelCasa = sTels;
    }

    public String getDireccion() {
        return sDireccion;
    }

    public void setDireccion(String sDireccion) {
        this.sDireccion = sDireccion;
    }

    public String getCorreo() {
        return this.sCorreo;
    }
    public void setCorreo(String scorreo){
        this.sCorreo=scorreo;
    }

    /**
     * @return the sDondeViene
     */
    public String getDondeViene() {
        return sDondeViene;
    }

    /**
     * @param sDondeViene the sDondeViene to set
     */
    public void setDondeViene(String sDondeViene) {
        this.sDondeViene = sDondeViene;
    }

    /**
     * @return the sComoSeEntero
     */
    public String getComoSeEntero() {
        return sComoSeEntero;
    }

    /**
     * @param sComoSeEntero the sComoSeEntero to set
     */
    public void setComoSeEntero(String sComoSeEntero) {
        this.sComoSeEntero = sComoSeEntero;
    }
}
