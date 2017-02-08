package org.apli.modelbeans;

import java.io.Serializable;
import java.util.Vector;
import org.apli.AD.AccesoDatos;

/**
 * Informaci�n sobre el desarrollo del infante en el caso de pediatr�a
 * @author BAOZ
 * @version 1.0
 * @created 13-mar-2014 10:56:12 a.m.
 */
public class Desarrollo implements Serializable {

	/**
	 * Indica el tiempo, en meses, del control cef�lico (controla y sostiene la cabeza)
	 */
	private int nControlCefalico;
	/**
	 * Edad, en meses, en que inici� el control de esf�nteres
	 */
	private int nCtrlEsfinteres;
	/**
	 * Edad, en meses, en que empez� a pronunciar disilabos
	 */
	private int nDisilabos;
	/**
	 * Edad, en meses, cuando inici� el gateo
	 */
	private int nGateo;
	/**
	 * Edad, en meses, en que inici� la marcha sin apoyo
	 */
	private int nMarcha;
	/**
	 * Edad en meses de la aparici�n de la primera dentici�n
	 */
	private int nPrimerDiente;
	/**
	 * Edad en meses en que empez� a sentarse solo
	 */
	private int nSentarse;
	/**
	 * Descripci�n del comportamiento
	 */
	private String sComportamiento;
	/**
	 * Descripci�n de escolaridad y rendimiento
	 */
	private String sEscolaridadRend;
        private AccesoDatos oAD;

	public Desarrollo(){

	}

	public void finalize() throws Throwable {

	}

    public int getControlCefalico() {
        return nControlCefalico;
    }

    public void setControlCefalico(int nControlCefalico) {
        this.nControlCefalico = nControlCefalico;
    }

    public int getCtrlEsfinteres() {
        return nCtrlEsfinteres;
    }

    public void setCtrlEsfinteres(int nCtrlEsfinteres) {
        this.nCtrlEsfinteres = nCtrlEsfinteres;
    }

    public int getDisilabos() {
        return nDisilabos;
    }

    public void setDisilabos(int nDisilabos) {
        this.nDisilabos = nDisilabos;
    }

    public int getGateo() {
        return nGateo;
    }

    public void setGateo(int nGateo) {
        this.nGateo = nGateo;
    }

    public int getMarcha() {
        return nMarcha;
    }

    public void setMarcha(int nMarcha) {
        this.nMarcha = nMarcha;
    }

    public int getPrimerDiente() {
        return nPrimerDiente;
    }

    public void setPrimerDiente(int nPrimerDiente) {
        this.nPrimerDiente = nPrimerDiente;
    }

    public int getSentarse() {
        return nSentarse;
    }

    public void setSentarse(int nSentarse) {
        this.nSentarse = nSentarse;
    }

    public String getComportamiento() {
        return sComportamiento;
    }

    public void setComportamiento(String sComportamiento) {
        this.sComportamiento = sComportamiento;
    }

    public String getEscolaridadRend() {
        return sEscolaridadRend;
    }

    public void setEscolaridadRend(String sEscolaridadRend) {
        this.sEscolaridadRend = sEscolaridadRend;
    }

    public AccesoDatos getAD() {
        return oAD;
    }

    public void setAD(AccesoDatos oAD) {
        this.oAD = oAD;
    }    
    
    public String guardaDesarrollo(int nFolio, Desarrollo oD) throws Exception{
        Vector rst=null;
        String sQuery="";

        if (nFolio==0||oD==null){
            throw new Exception("Historia Clínica. Guarda Desarrollo: Error de programación. Faltan datos.");
        }else{
            sQuery="select * from guardaDesarrollo("+nFolio+","+oD.getControlCefalico()+
                    "::int2,"+oD.getCtrlEsfinteres()+"::int2,"+oD.getDisilabos()+"::int2,"+oD.getGateo()+
                    "::int2,"+oD.getMarcha()+"::int2,"+oD.getPrimerDiente()+"::int2,"+oD.getSentarse()+
                    "::int2,'"+oD.getComportamiento()+"','"+oD.getEscolaridadRend()+"');";
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