package org.apli.modelbeans;

import java.io.Serializable;
import java.util.Vector;
import org.apli.AD.AccesoDatos;

/**
 * Representa los antecedentes personales patol�gicos de un paciente
 * @author BAOZ
 * @version 1.0
 * @created 13-mar-2014 10:56:04 a.m.
 */
public class AntecedPersPatol implements Serializable {

	/**
	 * Indica si el paciente ha presentado cuadros al�rgicos previos; nulo si no los
	 * tiene o si no se sabe
	 */
	private String sAlergicos;
	/**
	 * Indica si el paciente ha presentado cardiopat�as previas; nulo si no los tiene
	 * o si no se sabe
	 */
	private String sCardiopatias;
	/**
	 * Indica si el paciente ha presentado diabetes previa; nulo si no los tiene o si
	 * no se sabe
	 */
	private String sDiabetes;
	/**
	 * Indica si el paciente ha tenido hospitalizaciones previas; nulo si no los tiene
	 * o si no se sabe
	 */
	private String sHosp;
	/**
	 * Indica si el paciente ha presentado cuadros de hipertensi�n previos; nulo si no
	 * los tiene o si no se sabe
	 */
	private String sHTA;
	/**
	 * Indica si el paciente ha tenido procedimientos quir�rgicos previos; nulo si no
	 * los tiene o si no se sabe
	 */
	private String sQx;
	/**
	 * Indica si el paciente ha tenido transfusiones previas; nulo si no los tiene o
	 * si no se sabe
	 */
	private String sTransf;
	/**
	 * Indica si el paciente ha presentado cuadros traum�ticos previos; nulo si no los
	 * tiene o si no se sabe
	 */
	private String sTraumat;
        private AccesoDatos oAD;

	public AntecedPersPatol(){

	}

	public void finalize() throws Throwable {

	}

    public String getAlergicos() {
        return sAlergicos;
    }

    public void setAlergicos(String sAlergicos) {
        this.sAlergicos = sAlergicos;
    }

    public String getCardiopatias() {
        return sCardiopatias;
    }

    public void setCardiopatias(String sCardiopatias) {
        this.sCardiopatias = sCardiopatias;
    }

    public String getDiabetes() {
        return sDiabetes;
    }

    public void setDiabetes(String sDiabetes) {
        this.sDiabetes = sDiabetes;
    }

    public String getHosp() {
        return sHosp;
    }

    public void setHosp(String sHosp) {
        this.sHosp = sHosp;
    }

    public String getHTA() {
        return sHTA;
    }

    public void setHTA(String sHTA) {
        this.sHTA = sHTA;
    }

    public String getQx() {
        return sQx;
    }

    public void setQx(String sQx) {
        this.sQx = sQx;
    }

    public String getTransf() {
        return sTransf;
    }

    public void setTransf(String sTransf) {
        this.sTransf = sTransf;
    }

    public String getTraumat() {
        return sTraumat;
    }

    public void setTraumat(String sTraumat) {
        this.sTraumat = sTraumat;
    }
    
    public AccesoDatos getAD() {
        return oAD;
    }

    public void setAD(AccesoDatos oAD) {
        this.oAD = oAD;
    }
    
    public String guardaAntecedentesPatol(int nFolio, AntecedPersPatol oAP) throws Exception{
        Vector rst=null;
        String sQuery="";

        if (nFolio==0||oAP==null){
            throw new Exception("Historia Clínica. Guarda Antecedentes Patológicos: Error de programación. Faltan datos.");
        }else{
            sQuery="select * from guardaantecedentesperspatol("+nFolio+",'"+cambiaStr(oAP.getAlergicos())+
                    "', '"+cambiaStr(oAP.getCardiopatias())+"','"+cambiaStr(oAP.getDiabetes())+"','"+
                    cambiaStr(oAP.getHosp())+"','"+cambiaStr(oAP.getHTA())+"','"+cambiaStr(oAP.getQx())+
                    "','"+cambiaStr(oAP.getTransf())+"','"+cambiaStr(oAP.getTraumat())+"')";
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
    
    public String cambiaStr(String var){
        if (var == null || var.equals("null")||var.equals(""))
            return "";
        else
            return var;
    }
}