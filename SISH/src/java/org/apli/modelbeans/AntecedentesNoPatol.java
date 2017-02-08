package org.apli.modelbeans;

import java.io.Serializable;
import java.util.Vector;
import org.apli.AD.AccesoDatos;

/**
 * Representa los antecedentes no patol�gicos del paciente
 * @author BAOZ
 * @version 1.0
 * @created 13-mar-2014 10:56:02 a.m.
 */
public class AntecedentesNoPatol implements Serializable {

	/**
	 * Indica si el paciente cuenta con agua potable o no en su vivienda
	 */
	private boolean bAguaPot;
	/**
	 * Indica si el paciente presenta alcoholismo o no
	 */
	private boolean bAlcoholismo;
	/**
	 * Indica si el paciente cuenta con drenaje o no en su vivienda
	 */
	private boolean bDrenaje;
	/**
	 * Indica si el paciente cuenta con electricidad o no en su vivenda
	 */
	private boolean bElectricidad;
	/**
	 * Indica si el paciente cuenta o no con servicios sanitarios en su vivienda
	 */
	private boolean bServSanit;
	/**
	 * Indica si el paciente presenta tabaquismo o no
	 */
	private boolean bTabaquismo;
	/**
	 * Lista de mascotas/animales con que cuenta el paciente, nulo si no tiene
	 */
	private String sAnimales;
	/**
	 * Cuadro de vacunaci�n
	 * C = completo
	 * I = incompleto
	 * N = nulo
	 */
	private String sCuadroVac;
	/**
	 * B = buena
	 * R = regular
	 * M = mala
	 */
	private String sDieta;
	/**
	 * U = urbana
	 * R = rural
	 */
	private String sTipoCasaHab;
        
        private AccesoDatos oAD;

	public AntecedentesNoPatol(){

	}

	public void finalize() throws Throwable {

	}

    public boolean isAguaPot() {
        return bAguaPot;
    }

    public void setAguaPot(boolean bAguaPot) {
        this.bAguaPot = bAguaPot;
    }

    public boolean isAlcoholismo() {
        return bAlcoholismo;
    }

    public void setAlcoholismo(boolean bAlcoholismo) {
        this.bAlcoholismo = bAlcoholismo;
    }

    public boolean isDrenaje() {
        return bDrenaje;
    }

    public void setDrenaje(boolean bDrenaje) {
        this.bDrenaje = bDrenaje;
    }

    public boolean isElectricidad() {
        return bElectricidad;
    }

    public void setElectricidad(boolean bElectricidad) {
        this.bElectricidad = bElectricidad;
    }

    public boolean isServSanit() {
        return bServSanit;
    }

    public void setServSanit(boolean bServSanit) {
        this.bServSanit = bServSanit;
    }

    public boolean isTabaquismo() {
        return bTabaquismo;
    }

    public void setTabaquismo(boolean bTabaquismo) {
        this.bTabaquismo = bTabaquismo;
    }

    public String getAnimales() {
        return sAnimales;
    }

    public void setAnimales(String sAnimales) {
        this.sAnimales = sAnimales;
    }

    public String getCuadroVac() {
        return sCuadroVac;
    }

    public void setCuadroVac(String sCuadroVac) {
        this.sCuadroVac = sCuadroVac;
    }

    public String getDieta() {
        return sDieta;
    }

    public void setDieta(String sDieta) {
        this.sDieta = sDieta;
    }

    public String getTipoCasaHab() {
        return sTipoCasaHab;
    }

    public void setTipoCasaHab(String sTipoCasaHab) {
        this.sTipoCasaHab = sTipoCasaHab;
    }
    
    public AccesoDatos getAD() {
        return oAD;
    }

    public void setAD(AccesoDatos oAD) {
        this.oAD = oAD;
    }
    
    public String guardaAntecedentesNoPatol(int nFolio, AntecedentesNoPatol oANP) throws Exception{
        Vector rst=null;
        String sQuery="";

        if (nFolio==0||oANP==null){
            throw new Exception("Historia Clínica. Guarda Antecedentes No Patológicos: Error de programación. Faltan datos.");
        }else{
            sQuery="select * from guardaantecedentesnopatol("+nFolio+",'"+cambiaBool(String.valueOf(oANP.isAguaPot()))+"','"+
                    cambiaBool(String.valueOf(oANP.isAlcoholismo()))+"','"+cambiaBool(String.valueOf(oANP.isDrenaje()))+"','"+
                    cambiaBool(String.valueOf(oANP.isElectricidad()))+"','"+cambiaBool(String.valueOf(oANP.isServSanit()))+
                    "', '"+cambiaBool(String.valueOf(oANP.isTabaquismo()))+"', '"+oANP.getAnimales()+"','"+
                    oANP.getCuadroVac()+"','"+oANP.getDieta()+"','"+oANP.getTipoCasaHab()+"');";
            System.out.println(sQuery);
            if (getAD()==null){
                setAD(new AccesoDatos());
                getAD().conectar();
                rst=getAD().ejecutarConsulta(sQuery);
                getAD().desconectar();
                setAD(null);
            }
        }
        return " "+rst.get(0);
    }
    
    public char cambiaBool(String var){
        if (var.equals("true"))
            return '1';
        else
            return '0';
    }
}