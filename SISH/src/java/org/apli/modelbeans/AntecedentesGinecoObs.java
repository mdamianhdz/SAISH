package org.apli.modelbeans;
import java.io.Serializable;
import java.util.Date;
import java.util.Vector;
import org.apli.AD.AccesoDatos;

/**
 * Representa los antecedentes gineco-obst�tricos de una paciente
 * @author BAOZ
 * @version 1.0
 * @created 13-mar-2014 10:56:05 a.m.
 */
public class AntecedentesGinecoObs implements Serializable {

	/**
	 * M�todo(s) anticonceptivo(s) utilizados por la paciente 
	 */
	//private MetodoAnticonceptivo arrMetAnticoncep;
	/**
	 * Fecha del parto o ces�rea m�s reciente
	 */
	private Date dFecUltNac;
	/**
	 * Fecha del periodo menstrual m�s reciente
	 */
	private Date dFUR;
	/**
	 * Fecha del examen Papanicolau m�s reciente
	 */
	private Date dUltPapanicolau;
	/**
	 * Edad en a�os IVSA, entero mayor o igual a cero
	 */
	private int nIVSA;
	/**
	 * Edad en a�os en que se present� la menarquia, n�mero entero mayor a 5
	 */
	private int nMenarquia;
	/**
	 * Peso en kg del producto m�s reciente
	 */
	private float nPesoProd;
	/**
	 * N�mero de d�as que dura el periodo menstural
	 */
	private int nRitmoDias;
	/**
	 * N�mero de d�as entre periodos menstruales
	 * (Se lee "nRitmoDias x nRitmoFrec" se lee "4 d�as por cada 25 d�as"
	 */
	private int nRitmoFrec;
	/**
	 * Resultado del examen Papanicolau m�s reciente
	 */
	private String sResultadoPap;
        private AccesoDatos oAD;

	public AntecedentesGinecoObs(){

	}

	public void finalize() throws Throwable {

	}

    public Date getFecUltNac() {
        return dFecUltNac;
    }

    public void setFecUltNac(Date dFecUltNac) {
        this.dFecUltNac = dFecUltNac;
    }

    public Date getFUR() {
        return dFUR;
    }

    public void setFUR(Date dFUR) {
        this.dFUR = dFUR;
    }

    public Date getUltPapanicolau() {
        return dUltPapanicolau;
    }

    public void setUltPapanicolau(Date dUltPapanicolau) {
        this.dUltPapanicolau = dUltPapanicolau;
    }

    public int getIVSA() {
        return nIVSA;
    }

    public void setIVSA(int nIVSA) {
        this.nIVSA = nIVSA;
    }

    public int getMenarquia() {
        return nMenarquia;
    }

    public void setMenarquia(int nMenarquia) {
        this.nMenarquia = nMenarquia;
    }

    public float getPesoProd() {
        return nPesoProd;
    }

    public void setPesoProd(float nPesoProd) {
        this.nPesoProd = nPesoProd;
    }

    public int getRitmoDias() {
        return nRitmoDias;
    }

    public void setRitmoDias(int nRitmoDias) {
        this.nRitmoDias = nRitmoDias;
    }

    public int getRitmoFrec() {
        return nRitmoFrec;
    }

    public void setRitmoFrec(int nRitmoFrec) {
        this.nRitmoFrec = nRitmoFrec;
    }

    public String getResultadoPap() {
        return sResultadoPap;
    }

    public void setResultadoPap(String sResultadoPap) {
        this.sResultadoPap = sResultadoPap;
    }

    public AccesoDatos getAD() {
        return oAD;
    }

    public void setAD(AccesoDatos oAD) {
        this.oAD = oAD;
    }
        
    public String guardaAntecedentesGinecoObs(int nFolio, AntecedentesGinecoObs oAGO, String[] listaMA) throws Exception{
        Vector rst=null;
        String sQuery="";
        MetodoAnticonceptivo oMA;

        if (nFolio==0||oAGO==null){
            throw new Exception("Historia Clínica. Guarda Antecedentes Gineco-Obstétricos: Error de programación. Faltan datos.");
        }else{
            sQuery="select * from guardaantecedentesAntecedentesGinecoObs("+nFolio+", "+(oAGO.getFecUltNac()==null?"null":"'"+oAGO.getFecUltNac()+"'")+
                    ", "+(oAGO.getFUR()==null?"null":"'"+oAGO.getFUR()+"'")+", "+(oAGO.getUltPapanicolau()==null?"null":"'"+oAGO.getUltPapanicolau()+"'")+", "+oAGO.getIVSA()+
                    "::int2, "+oAGO.getMenarquia()+"::int2, "+oAGO.getPesoProd()+", "+oAGO.getRitmoDias()+
                    "::int2, "+oAGO.getRitmoFrec()+"::int2, "+(oAGO.getResultadoPap()==null?"null":"'"+oAGO.getResultadoPap()+"'")+");";
            System.out.println(sQuery);
            if (getAD()==null){
                setAD(new AccesoDatos());
                getAD().conectar();
                rst=getAD().ejecutarConsulta(sQuery);
                getAD().desconectar();
                setAD(null);
            }else{
                rst = this.getAD().ejecutarConsulta(sQuery);
            }
            oMA=new MetodoAnticonceptivo();
            oMA.eliminaMAPaciente(nFolio);
            if (listaMA.length>0)
                oMA.actualizaMAPaciente(listaMA, nFolio);
        }
        return " "+rst.get(0);
    }    

}