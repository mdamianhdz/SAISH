package org.apli.modelbeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import org.apli.AD.AccesoDatos;



/**
 * Representa la informaci�n de una habitaci�n del hospital
 * @author BAOZ
 * @version 1.0
 * @created 12-may-2014 04:44:31 p.m.
 */
public class Habitacion implements Serializable {

	/**
	 * Indica la disponibilidad de la habitaci�n
	 * 0 = disponible
	 * 1 = ocupada con paciente
	 * 2 = no disponible por sanitizaci�n o mantenimiento
	 */
	private int nDisponibilidad;
	/**
	 * N�mero de la habitaci�n
	 */
	private int nHab=0;
	/**
	 * Tipo de habitaci�n 
	 */
	private TipoHab oTipoHab;
        private AccesoDatos oAD;
        private String habnumDesp;
        
        public AccesoDatos getAD(){
            return oAD;
        }
    
        public void setAD(AccesoDatos oAD) {
            this.oAD = oAD;
        }

	public Habitacion(){

	}
        
        public Habitacion(int nhab){
            this.nHab=nhab;
        }
        
        public List<Habitacion> buscaTodasHabsDisp() throws Exception{
            List<Habitacion> listRet=null;
            Habitacion oH;
            Vector rst = null;
            String sQuery = "";

            sQuery="SELECT * FROM buscatodashabitacionesdisponibles('Habitacion');";     
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
                    oH = new Habitacion();
                    Vector vRowTemp = (Vector)rst.elementAt(i);
                    oH.setHab(((Double)vRowTemp.elementAt(0)).intValue());
                    oTipoHab=new TipoHab();
                    oTipoHab.setDescrip((String)vRowTemp.elementAt(1));
                    oH.setTipoHab(oTipoHab);
                    oH.setHabnumDesp("Habitacion:"+(String)vRowTemp.elementAt(1)+" ,no:"+((Double)vRowTemp.elementAt(0)).intValue());
                    listRet.add(oH);
                }
            }
            return listRet;
        }
        
        public List<Habitacion> buscaTodosCunerosDisp() throws Exception{
            List<Habitacion> listRet=null;
            Habitacion oH;
            Vector rst = null;
            String sQuery = "";

            sQuery="SELECT * FROM buscatodashabitacionesdisponibles('Cunero');";     
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
                    oH = new Habitacion();
                    Vector vRowTemp = (Vector)rst.elementAt(i);
                    oH.setHab(((Double)vRowTemp.elementAt(0)).intValue());
                    oTipoHab=new TipoHab();
                    oTipoHab.setDescrip((String)vRowTemp.elementAt(1));
                    oH.setTipoHab(oTipoHab);
                    oH.setHabnumDesp("Habitacion:"+(String)vRowTemp.elementAt(1)+" ,no:"+((Double)vRowTemp.elementAt(0)).intValue());
                    listRet.add(oH);
                }
            }
            return listRet;
        }
        
         public Habitacion buscaHabitacion(int nHab) throws Exception{
            
            
            Habitacion oH=new Habitacion();
            
            if (nHab==0) {
                throw new Exception("Funcion.buscaHabitacion: error de programación, faltan datos");
            }else {

                Vector rst = null;
                String sQuery = "";

                sQuery="select * from buscaHabitacion("+nHab+"::int2)";     
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

                    for (int i = 0; i < rst.size(); i++) {
                        oH = new Habitacion();
                        Vector vRowTemp = (Vector)rst.elementAt(i);
                        oH.setHab(((Double)vRowTemp.elementAt(0)).intValue());
                        oTipoHab=new TipoHab();
                        oTipoHab.setDescrip((String)vRowTemp.elementAt(1));
                        oH.setTipoHab(oTipoHab);
                        oH.setHabnumDesp("Habitacion:"+(String)vRowTemp.elementAt(1)+" ,no:"+((Double)vRowTemp.elementAt(0)).intValue());
                    }
                }
            }
            return oH;
        }

	public void finalize() throws Throwable {

	}

    public int getDisponibilidad() {
        return nDisponibilidad;
    }

    public void setDisponibilidad(int nDisponibilidad) {
        this.nDisponibilidad = nDisponibilidad;
    }

    public int getHab() {
        return nHab;
    }

    public void setHab(int nHab) {
        this.nHab = nHab;
    }

    public TipoHab getTipoHab() {
        return oTipoHab;
    }

    public void setTipoHab(TipoHab oTipoHab) {
        this.oTipoHab = oTipoHab;
    }

    public String getHabnumDesp() {
        return habnumDesp;
    }

    public void setHabnumDesp(String habnumDesp) {
        this.habnumDesp = habnumDesp;
    }

}