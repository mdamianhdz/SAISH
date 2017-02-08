package org.apli.modelbeans;

import java.io.Serializable;
import java.util.Vector;
import org.apli.AD.AccesoDatos;

/**
 * Resultado del examen f�sico durante la revisi�n del paciente
 * @author BAOZ
 * @version 1.0
 * @created 13-mar-2014 10:56:07 a.m.
 */
public class ExamenFisico implements Serializable {
    
        private int nCveExFis;
	/**
	 * N�mero de latidos por minuto
	 */
	private int nFrecCard;
	/**
	 * N�mero de respiraciones por minuto
	 */
	private int nFrecResp;
	/**
	 * Peso en kg del paciente
	 */
	private float nPeso;
	/**
	 * Valor alto de la tensi�n arterial en mmHg (mil�metros de mercurio)
	 */
	private int nTAA;
	/**
	 * Valor bajo de la tensi�n arterial en mmHg
	 */
	private int nTAB;
	/**
	 * Talla (estatura) en metros del paciente (se muestra en cent�metros en el caso
	 * de pediatr�a
	 */
	private float nTalla;
	/**
	 * Temperatura corporal en grados celcius
	 */
	private float nTemp;
        
        private AccesoDatos oAD;

	public ExamenFisico(){

	}

    public int getCveExFis() {
        return nCveExFis;
    }

    public void setCveExFis(int nCveExFis) {
        this.nCveExFis = nCveExFis;
    }
    
    public int getFrecCard() {
        return nFrecCard;
    }

    public void setFrecCard(int nFrecCard) {
        this.nFrecCard = nFrecCard;
    }

    public int getFrecResp() {
        return nFrecResp;
    }

    public void setFrecResp(int nFrecResp) {
        this.nFrecResp = nFrecResp;
    }

    public float getPeso() {
        return nPeso;
    }

    public void setPeso(float nPeso) {
        this.nPeso = nPeso;
    }

    public int getTAA() {
        return nTAA;
    }

    public void setTAA(int nTAA) {
        this.nTAA = nTAA;
    }

    public int getTAB() {
        return nTAB;
    }

    public void setTAB(int nTAB) {
        this.nTAB = nTAB;
    }

    public float getTalla() {
        return nTalla;
    }

    public void setTalla(float nTalla) {
        this.nTalla = nTalla;
    }

    public float getTemp() {
        return nTemp;
    }

    public void setTemp(float nTemp) {
        this.nTemp = nTemp;
    }

    public AccesoDatos getAD() {
        return oAD;
    }

    public void setAD(AccesoDatos oAD) {
        this.oAD = oAD;
    }

    public String guardaExamenFis(ExamenFisico oEF) throws Exception{
        Vector rst = null;
        String sQuery = "";
        
        if (oEF==null || oEF.getCveExFis()==0) {
            throw new Exception("ExamenFisico.guardaEF: error de programación, faltan datos");
	}else {
            sQuery="select * from modificaexamenfisico("+oEF.getCveExFis()+", "+oEF.getFrecCard()+"::int2, "+
                    oEF.getFrecResp()+"::int2, "+oEF.getPeso()+", "+oEF.getTAA()+"::int2, "+
                    oEF.getTAB()+"::int2, "+oEF.getTalla()+", "+oEF.getTemp()+");";
            if (getAD() == null){
                setAD(new AccesoDatos());
		getAD().conectar();
		rst = getAD().ejecutarConsulta(sQuery);
		getAD().desconectar();
                setAD(null);
            }  
	}
        return " "+rst.get(0);
    }
        
        
}