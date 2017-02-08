package org.apli.modelbeans;

import java.io.Serializable;
import java.util.Vector;
import org.apli.AD.AccesoDatos;

/**
 * Representa informaci�n de antecedentes m�dicos familiares
 * @author BAOZ
 * @version 1.0
 * @updated 14-mar-2014 11:40:50 a.m.
 */
public class AntecedentesHeredoFamiliares implements Serializable {

	/**
	 * Describe el o los familiares del paciente que han padecido alcoholismo; nulo si
	 * no los tiene o si no se sabe
	 */
	private String sAlcoholismo;
	/**
	 * Describe el o los familiares del paciente que han padecido alergias; nulo si no
	 * los tiene o si no se sabe
	 */
	private String sAlergias;
	/**
	 * Describe el o los familiares del paciente que han padecido alteraciones
	 * mentales; nulo si no los tiene o si no se sabe
	 */
	private String sAltMentales;
	/**
	 * Describe el o los familiares del paciente que han padecido asma; nulo si no los
	 * tiene o si no se sabe
	 */
	private String sAsma;
	/**
	 * Describe el o los familiares del paciente que han padecido c�ncer; nulo si no
	 * los tiene o si no se sabe
	 */
	private String sCancer;
	/**
	 * Describe el o los familiares del paciente que han padecido enfermedades
	 * cong�nitas; nulo si no los tiene o si no se sabe
	 */
	private String sCongenitos;
	/**
	 * Describe el o los familiares del paciente que han padecido convulsiones; nulo
	 * si no los tiene o si no se sabe
	 */
	private String sConvulsiones;
	/**
	 * Describe el o los familiares del paciente que han padecido diabetes; nulo si no
	 * los tiene o si no se sabe
	 */
	private String sDiabetes;
	/**
	 * Describe el o los familiares del paciente que han padecido drogadicci�n; nulo
	 * si no los tiene o si no se sabe
	 */
	private String sDrogadiccion;
	/**
	 * Describe el o los familiares del paciente que han padecido enfisema; nulo si no
	 * los tiene o si no se sabe
	 */
	private String sEnfisema;
	/**
	 * Describe el o los familiares del paciente que han padecido epilepsia; nulo si
	 * no los tiene o si no se sabe
	 */
	private String sEpilepsia;
	/**
	 * Describe el o los familiares del paciente que han padecido cardiopat�as; nulo
	 * si no los tiene o si no se sabe
	 */
	private String sFamCardiopatias;
	/**
	 * Describe el o los familiares del paciente que han padecido HAS; nulo si no los
	 * tiene o si no se sabe
	 */
	private String sHAS;
	/**
	 * Describe el o los familiares del paciente que han padecido IAM (infarto agudo
	 * al miocardio); nulo si no los tiene o si no se sabe
	 */
	private String sIAM;
	/**
	 * Describe el o los familiares del paciente que han padecido litiasis; nulo si no
	 * los tiene o si no se sabe
	 */
	private String sLitiasis;
	/**
	 * Describe el o los familiares del paciente que han padecido tabaquismo; nulo si
	 * no los tiene o si no se sabe
	 */
	private String sTabaquismo;
	/**
	 * Describe el o los familiares del paciente que han padecido tubercolisis; nulo
	 * si no los tiene o si no se sabe
	 */
	private String sTuberculosis;
        private AccesoDatos oAD;

	public AntecedentesHeredoFamiliares(){

	}

	public void finalize() throws Throwable {

	}

        public String getAlcoholismo() {
            return sAlcoholismo;
        }

        public void setAlcoholismo(String sAlcoholismo) {
            this.sAlcoholismo = sAlcoholismo;
        }

        public String getAlergias() {
            return sAlergias;
        }

        public void setAlergias(String sAlergias) {
            this.sAlergias = sAlergias;
        }

        public String getAltMentales() {
            return sAltMentales;
        }

        public void setAltMentales(String sAltMentales) {
            this.sAltMentales = sAltMentales;
        }

        public String getAsma() {
            return sAsma;
        }

        public void setAsma(String sAsma) {
            this.sAsma = sAsma;
        }

        public String getCancer() {
            return sCancer;
        }

        public void setCancer(String sCancer) {
            this.sCancer = sCancer;
        }

        public String getCongenitos() {
            return sCongenitos;
        }

        public void setCongenitos(String sCongenitos) {
            this.sCongenitos = sCongenitos;
        }

        public String getConvulsiones() {
            return sConvulsiones;
        }

        public void setConvulsiones(String sConvulsiones) {
            this.sConvulsiones = sConvulsiones;
        }

        public String getDiabetes() {
            return sDiabetes;
        }

        public void setDiabetes(String sDiabetes) {
            this.sDiabetes = sDiabetes;
        }

        public String getDrogadiccion() {
            return sDrogadiccion;
        }

        public void setDrogadiccion(String sDrogadiccion) {
            this.sDrogadiccion = sDrogadiccion;
        }

        public String getEnfisema() {
            return sEnfisema;
        }

        public void setEnfisema(String sEnfisema) {
            this.sEnfisema = sEnfisema;
        }

        public String getEpilepsia() {
            return sEpilepsia;
        }

        public void setEpilepsia(String sEpilepsia) {
            this.sEpilepsia = sEpilepsia;
        }

        public String getFamCardiopatias() {
            return sFamCardiopatias;
        }

        public void setFamCardiopatias(String sFamCardiopatias) {
            this.sFamCardiopatias = sFamCardiopatias;
        }

        public String getHAS() {
            return sHAS;
        }

        public void setHAS(String sHAS) {
            this.sHAS = sHAS;
        }

        public String getIAM() {
            return sIAM;
        }

        public void setIAM(String sIAM) {
            this.sIAM = sIAM;
        }

        public String getLitiasis() {
            return sLitiasis;
        }

        public void setLitiasis(String sLitiasis) {
            this.sLitiasis = sLitiasis;
        }

        public String getTabaquismo() {
            return sTabaquismo;
        }

        public void setTabaquismo(String sTabaquismo) {
            this.sTabaquismo = sTabaquismo;
        }

        public String getTuberculosis() {
            return sTuberculosis;
        }

        public void setTuberculosis(String sTuberculosis) {
            this.sTuberculosis = sTuberculosis;
        }
        
        public AccesoDatos getAD(){
            return oAD;
        }
        
        public void setAD(AccesoDatos oAD){
            this.oAD=oAD;
        }
        
        public String guardaAntecedentesFam(int nFolio, AntecedentesHeredoFamiliares oAHF) throws Exception{
            Vector rst=null;
            String sQuery="";

            if (nFolio==0||oAHF==null){
                throw new Exception("Historia Clínica. Guarda Antecedentes Familiares: Error de programación. Faltan datos.");
            }else{
                sQuery="select * from guardaAntecedentesFamiliares("+nFolio+",'"+
                        cambiaStr(oAHF.getAlcoholismo())+"','"+cambiaStr(oAHF.getAlergias())+"','"+
                        cambiaStr(oAHF.getAltMentales())+"','"+cambiaStr(oAHF.getAsma())+"','"+
                        cambiaStr(oAHF.getCancer())+"','"+cambiaStr(oAHF.getCongenitos())+"','"+
                        cambiaStr(oAHF.getConvulsiones())+"','"+cambiaStr(oAHF.getDiabetes())+"','"+
                        cambiaStr(oAHF.getDrogadiccion())+"','"+cambiaStr(oAHF.getEnfisema())+"','"+
                        cambiaStr(oAHF.getEpilepsia())+"','"+cambiaStr(oAHF.getFamCardiopatias())+"','"+
                        cambiaStr(oAHF.getHAS())+"','"+cambiaStr(oAHF.getIAM())+"','"+
                        cambiaStr(oAHF.getLitiasis())+"','"+cambiaStr(oAHF.getTabaquismo())+"','"+
                        cambiaStr(oAHF.getTuberculosis())+"')";
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
            if (var==null ||var.equals("null") ||var.equals(""))
                return "";
            else
                return var;
        }
}