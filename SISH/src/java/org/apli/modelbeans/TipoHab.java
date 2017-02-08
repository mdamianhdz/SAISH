package org.apli.modelbeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import org.apli.AD.AccesoDatos;



/**
 * Tipo de habitaci�n y su costo
 * @author BAOZ
 * @version 1.0
 * @created 12-may-2014 04:44:33 p.m.
 */
public class TipoHab implements Serializable {

	/**
	 * Costo del tipo de habitaci�n, incluye IVA
	 */
	private float nCosto;
	/**
	 * Clave del tipo de habitaci�n
	 */
	private int nTipo;
	/**
	 * Descripci�n del tipo de habitaci�n
	 */
	private String sDescrip;
        private AccesoDatos oAD;

	public TipoHab(){

	}

	public void finalize() throws Throwable {

	}
        
          public AccesoDatos getAD(){
            return oAD;
        }
    
        public void setAD(AccesoDatos oAD) {
            this.oAD = oAD;
        }
    
        public List<TipoHab> buscaTodosTiposHab() throws Exception{
            List<TipoHab> listRet=null;
            TipoHab oTH;

            Vector rst = null;
            String sQuery = "";

            sQuery="select * from buscaTodosTipoHab()";     
            if (getAD() == null){
                setAD(new AccesoDatos());
                getAD().conectar();
                rst = getAD().ejecutarConsulta(sQuery);
                getAD().desconectar();
                setAD(null);
            }else{
                rst = getAD().ejecutarConsulta(sQuery);
            }

            if (rst != null) {
                listRet = new ArrayList();
                for (int i = 0; i < rst.size(); i++) {
                    oTH = new TipoHab();
                    Vector vRowTemp = (Vector)rst.elementAt(i);
                    oTH.setTipo(((Double)vRowTemp.elementAt(0)).intValue());
                    oTH.setDescrip((String)vRowTemp.elementAt(1));
                    listRet.add(oTH);
                }
            }
            return listRet;
        }
        
        public List<TipoHab> buscaTodosTiposHabDisp() throws Exception{
            List<TipoHab> listRet=null;
            TipoHab oTH;

            Vector rst = null;
            String sQuery = "";

            sQuery="select * from buscaTodosTipoHabdisponibles()";     
            if (getAD() == null){
                setAD(new AccesoDatos());
                getAD().conectar();
                rst = getAD().ejecutarConsulta(sQuery);
                getAD().desconectar();
                setAD(null);
            }else{
                rst = getAD().ejecutarConsulta(sQuery);
            }

            if (rst != null) {
                listRet = new ArrayList();
                for (int i = 0; i < rst.size(); i++) {
                    oTH = new TipoHab();
                    Vector vRowTemp = (Vector)rst.elementAt(i);
                    oTH.setTipo(((Double)vRowTemp.elementAt(0)).intValue());
                    oTH.setDescrip((String)vRowTemp.elementAt(1));
                    listRet.add(oTH);
                }
            }
            return listRet;
        }

        public int getTipo() {
            return nTipo;
        }

        public void setTipo(int nTipo) {
            this.nTipo = nTipo;
        }

        public String getDescrip() {
            return sDescrip;
        }

        public void setDescrip(String sDescrip) {
            this.sDescrip = sDescrip;
        }
        
        
        

}