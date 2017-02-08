package org.apli.modelbeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import org.apli.AD.AccesoDatos;

/**
 * Información de los turnos de trabajo que se cubren en el sanatorio
 * @author BAOZ
 * @version 1.0
 * @created 07-abr-2014 10:21:48 a.m.
 */
public class Turno implements Serializable {

	/**
	 * Clave del turno
	 */
	private String sCve;
	/**
	 * Descripci�n del turno
	 */
	private String sDesc;
	/**
	 * Descripci�n del horario (por ejemplo "L-V,07-15")
	 */
	private String sHorario;
        
        public AccesoDatos oAD;

	public Turno(){

	}
        
        public Turno(String cve, String horario){
            this.sCve=cve;
            this.sHorario=horario;
        }

        public String getCve() {
            return sCve;
        }
        public void setCve(String sCve) {
            this.sCve = sCve;
        }

        public String getDesc() {
            return sDesc;
        }
        public void setDesc(String sDesc) {
            this.sDesc = sDesc;
        }

        public String getHorario() {
            return sHorario;
        }
        public void setHorario(String sHorario) {
            this.sHorario = sHorario;
        }
        
        public AccesoDatos getAD() {
        return oAD;
    }

    /**
     * @param oAD the oAD to set
     */
    public void setAD(AccesoDatos oAD) {
        this.oAD = oAD;
    }

	
        public List<Turno> buscaTodosTurnos()throws Exception{
        List<Turno> listRet=null;
        Turno oT;
        Vector rst;
        
        String sQuery="select * from buscatodosturnos();";
        
        if (getAD()==null){
            setAD(new AccesoDatos());
            getAD().conectar();
        }
        rst=getAD().ejecutarConsulta(sQuery);
        getAD().desconectar();
        setAD(null);
        
        if(rst!=null){
            listRet=new ArrayList();
            for (int i = 0; i < rst.size(); i++) {
                Vector vRowTemp=(Vector) rst.get(i);
                oT=new Turno();
                oT.setCve((String)vRowTemp.get(0));
                oT.setDesc((String)vRowTemp.get(1));
                oT.setHorario((String)vRowTemp.get(2));
                listRet.add(oT);
            }
        }
        return listRet;
    }
}