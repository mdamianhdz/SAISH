package org.apli.modelbeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import org.apli.AD.AccesoDatos;

/**
 * Estado de la Rep�blica Mexicana
 * @author BAOZ
 * @version 1.0
 * @created 13-mar-2014 10:56:13 a.m.
 */
public class Estado implements Serializable {

	/**
	 * Clave (acr�nimo) del estado
	 */
	private String sCve;
	/**
	 * Descripci�n del estado
	 */
	private String sDescrip;
        private Pais oPais;
        protected AccesoDatos oAD = null;

	public Estado(){

	}

	public void finalize() throws Throwable {

	}

    public String getCve() {
        return sCve;
    }

    public void setCve(String sCve) {
        this.sCve = sCve;
    }

    public String getDescrip() {
        return sDescrip;
    }

    public void setDescrip(String sDescrip) {
        this.sDescrip = sDescrip;
    }

    public Pais getPais() {
        return oPais;
    }

    public void setPais(Pais oPais) {
        this.oPais = oPais;
    }
    public AccesoDatos getAD() {
            return oAD;
        }
        public void setAD(AccesoDatos oAD) {
            this.oAD = oAD;
        }
    public Estado buscaEstado(String clave) throws Exception{
        Estado oP=null;
            Vector rst = null;
            Vector<Estado> vObj = null;
            String sQuery = "";
            sQuery = "select * from buscaestadoPais('"+clave+"'::character varying)";
            if (getAD() == null){
                setAD(new AccesoDatos());
                getAD().conectar();
            }
            rst = getAD().ejecutarConsulta(sQuery);
            getAD().desconectar();
            setAD(null);
            if (rst != null |rst.size()>0) {
                int x=0;
                vObj = new Vector<Estado>();
                Pais pais;
                    oP = new Estado();
                    pais=new Pais();
                    
                    Vector vRowTemp = (Vector) rst.elementAt(0); 
                    oP.setCve((String) vRowTemp.elementAt(0));
                    oP.setDescrip((String) vRowTemp.elementAt(1));
                    pais.setCve((String) vRowTemp.elementAt(2));
                    pais.setDescrip((String) vRowTemp.elementAt(3));
                    oP.setPais(pais);
                
            }
            return oP;
    }
    public List<Estado> getEstados() throws Exception{
            Estado oP=null;
            Vector rst = null;
            Vector<Estado> vObj = null;
            List<Estado> lista=new ArrayList();
            String sQuery = "";
            int i=0;
            sQuery = "select * from buscaEstados()";
            if (getAD() == null){
                setAD(new AccesoDatos());
                getAD().conectar();
                rst = getAD().ejecutarConsulta(sQuery);
                getAD().desconectar();
                setAD(null);
            }
            else{
                rst = getAD().ejecutarConsulta(sQuery);
                getAD().desconectar();
                setAD(null);
            }
            if (rst != null |rst.size()>0) {
                int x=0;
                vObj = new Vector<Estado>();
                Pais pais;
                for (i = 0; i < rst.size(); i++) {
                    oP = new Estado();
                    pais=new Pais();
                    Vector vRowTemp = (Vector)rst.elementAt(i); 
                    oP.setCve((String) vRowTemp.elementAt(0));
                    oP.setDescrip((String) vRowTemp.elementAt(1));
                    pais.setCve((String) vRowTemp.elementAt(2));
                    pais.setDescrip((String) vRowTemp.elementAt(3));
                    oP.setPais(pais);
                    lista.add(oP);
                }
            }
            return lista;
    }
}