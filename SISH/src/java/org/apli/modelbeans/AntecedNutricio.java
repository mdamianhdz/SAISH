package org.apli.modelbeans;

import java.io.Serializable;
import java.util.Vector;
import org.apli.AD.AccesoDatos;

/**
 * Antecedentes nutricionales en el caso de pediatr�a
 * @author BAOZ
 * @version 1.0
 * @created 13-mar-2014 10:56:10 a.m.
 */
public class AntecedNutricio implements Serializable {

	/**
	 * Edad, en meses, en que inici� ablactaci�n (alimentaci�n distinta a la leche)
	 */
	private int nAblactacion;
	/**
	 * Duraci�n en meses de la lactancia materna
	 */
	private int nDuracionLact;
	/**
	 * Indica si, al momento de redactar la historia cl�nica, el ni�o a�n utilizaba
	 * biber�n o no
	 * S = S�
	 * N = No
	 * X = No se sabe
	 */
	private String sBiberonActual;
	/**
	 * Indica si, al momento de redactar la historia cl�nica, el ni�o consum�a f�rmula
	 * o no
	 * S = S�
	 * N = No
	 * X = No se sabe
	 */
	private String sFormActual;
        private String sNomFormula;
	/**
	 * Indica si hubo lactancia materna o no
	 * S = S�
	 * N = No
	 * X = No se sabe
	 */
	private String sLactanciaMaterna;
        
        private AccesoDatos oAD;

	public AntecedNutricio(){

	}

	public void finalize() throws Throwable {

	}

    public int getAblactacion() {
        return nAblactacion;
    }

    public void setAblactacion(int nAblactacion) {
        this.nAblactacion = nAblactacion;
    }

    public int getDuracionLact() {
        return nDuracionLact;
    }

    public void setDuracionLact(int nDuracionLact) {
        this.nDuracionLact = nDuracionLact;
    }

    public String getBiberonActual() {
        return sBiberonActual;
    }

    public void setBiberonActual(String sBiberonActual) {
        this.sBiberonActual = sBiberonActual;
    }

    public String getFormActual() {
        return sFormActual;
    }

    public void setFormActual(String sFormActual) {
        this.sFormActual = sFormActual;
    }
    public String getNomFormula() {
        return sNomFormula;
    }

    public void setNomFormula(String sNomFormula) {
        this.sNomFormula = sNomFormula;
    }

    public String getLactanciaMaterna() {
        return sLactanciaMaterna;
    }

    public void setLactanciaMaterna(String sLactanciaMaterna) {
        this.sLactanciaMaterna = sLactanciaMaterna;
    }

    public AccesoDatos getAD() {
        return oAD;
    }

    public void setAD(AccesoDatos oAD) {
        this.oAD = oAD;
    }
    
    public String guardaAntecedentesNutricio(int nFolio, AntecedNutricio oAN) throws Exception{
        Vector rst=null;
        String sQuery="";

        if (nFolio==0||oAN==null){
            throw new Exception("Historia Clínica. Guarda Antecedentes Perinatales: Error de programación. Faltan datos.");
        }else{
            sQuery="select * from guardaantecedentesNutricio("+nFolio+","+oAN.getAblactacion()+"::int2,"+
                    oAN.getDuracionLact()+"::int2,'"+oAN.getBiberonActual()+"','"+oAN.getFormActual()+
                    "','"+oAN.getLactanciaMaterna()+"',"+(oAN.getNomFormula()==null?"null":"'"+oAN.getNomFormula()+"'")+");";
            if (getAD()==null){
                setAD(new AccesoDatos());
                getAD().conectar();
                rst=getAD().ejecutarConsulta(sQuery);
                getAD().desconectar();
                setAD(null);
            }
        }
        return ""+rst.get(0);
    }
 }