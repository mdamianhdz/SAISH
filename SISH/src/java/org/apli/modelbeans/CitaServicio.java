package org.apli.modelbeans;

import java.io.Serializable;
import org.apli.AD.AccesoDatos;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

/**
 * Control de citas de los servicios (Rayos X, Laboratorio, etc.)
 * @author BAOZ
 * @version 1.0
 * @created 20-Abr-2014 10:36:14 p.m.
 */
public class CitaServicio implements Serializable{

	/**
         * Objeto paciente
         */
        private Paciente oPaciente; 
        /**
         * Objeto Area de Servicio
         */
        private AreaDeServicio oAreaServicio;
        /**
	 * Fecha-hora de la cita
	 */
	private Date dFecCita;
	/**
	 * Duraci�n en medias horas
	 */
	private int nDuracion;
	/**
	 * Quien paga
	 */
	private int nTipoPrincipal;
	/**
	 * Observaciones para la cita
	 */
	private String sObs;
        /**
         * Variable para el acceso a datos
         */
        protected AccesoDatos oAD = null;
        

	public CitaServicio(){
            this.dFecCita= new Date();
        }
        
        public CitaServicio(Date dFecha, AreaDeServicio oAreaServicio){
            this.dFecCita=dFecha;
            this.oAreaServicio=oAreaServicio;
            this.oPaciente = new Paciente();
        }
        
        public CitaServicio(Paciente oPaciente, AreaDeServicio oAreaServicio, Date dFecCita,
            int nDuracion, int nTipoPrincipal, String sObs){
            this.oPaciente=oPaciente;
            this.oAreaServicio= oAreaServicio;
            this.dFecCita=dFecCita;
            this.nDuracion=nDuracion;
            this.nTipoPrincipal=nTipoPrincipal;
            this.sObs=sObs;
        }
        
        /**
         * Busca la lista de todas las citas del servicio registradas,
         * Regresa un arreglo de citas de servicio
         **/
        public CitaServicio[] buscarTodasCitasDelServicio()throws Exception{
            CitaServicio[] arrRet = null;
            CitaServicio oCitaS=null;
            Vector rst = null;
            Vector<CitaServicio> vObj = null;
            String sQuery = "";
            int i=0, nTam = 0;

            //sQuery =" SELECT buscarTodasCitasPorCveServicio("+this.oAreaServicio.getCve()+",'"+this.dFecCita+"')";
            sQuery =" SELECT * FROM buscarTodasCitasPorCveServicio("+this.oAreaServicio.getCve()+",'"+this.dFecCita+"')";
            System.out.println(sQuery);
            /*Si oAD es nulo, tiene que crearlo y conectarlo, de otro modo
            supone que ya viene conectado*/
            if (getAD() == null){
                setAD(new AccesoDatos());
                getAD().conectar();
                rst = getAD().ejecutarConsulta(sQuery);
                getAD().desconectar();
                setAD(null);
            }
            else{
                rst = getAD().ejecutarConsulta(sQuery);
            }
            if (rst != null) {
                vObj = new Vector<CitaServicio>();
                Paciente tempPac;
                
                for (i = 0; i < rst.size(); i++) {
                    oCitaS = new CitaServicio();
                    tempPac= new Paciente();
                    Vector vRowTemp = (Vector)rst.elementAt(i);
                    //c.nfoliopaciente, c.dfechoracita, c.sobservaciones, c.ntipoprincipal, c.nduracion,
                    //p.snombre, p.sappaterno, p.sapmaterno 
                    tempPac.setFolioPac(((Double)vRowTemp.elementAt(0)).intValue());
                    oCitaS.setFecCita((Date) vRowTemp.elementAt(1));
                    oCitaS.setObs((String) vRowTemp.elementAt(2));
                    oCitaS.setTipoPrincipal(((Double) vRowTemp.elementAt(3)).intValue());
                    oCitaS.setDuracion(((Double) vRowTemp.elementAt(4)).intValue());
                    tempPac.setNombre((String) vRowTemp.elementAt(5));
                    tempPac.setApellidoPaterno((String) vRowTemp.elementAt(6));
                    tempPac.setApellidoMaterno((String) vRowTemp.elementAt(7));
                    oCitaS.setPaciente(tempPac);
                    vObj.add(oCitaS);
                }
                System.out.println(vObj.size());
                nTam = vObj.size();
                arrRet = new CitaServicio[nTam];

                for (i=0; i<nTam; i++){
                    arrRet[i] = vObj.elementAt(i);
                }
            }
            return arrRet;
        }
        
        /**
         * Agrega una nueva cita medica a la base de datos
         * Regresa el número de registros afectados (insertados)
         **/
        public int insertar() throws Exception{
            Vector nRet = null;
            String sQuery = "";
            if (this.oPaciente.getFolioPac()==0 || this.oAreaServicio.getCve().equals("") ||
                     this.nDuracion==0 ){
                throw new Exception("Cita Servicio.insertar: error de programación, faltan datos");
            }
            else {
                // select insertaCitaServicio(paciente.folio,areaservicio.cve,fecha,obs, tipo, duracion);
                SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                sQuery = " select insertaCitaServicio("+this.oPaciente.getFolioPac()+",'"+this.oAreaServicio.getCve()+"',"
                        +"'"+formatoDelTexto.format(this.dFecCita)+"','"+this.sObs+"',"+this.nTipoPrincipal+","+this.nDuracion+")";
                System.out.println(sQuery);
                /*Si oAD es nulo, tiene que crearlo y conectarlo, de otro modo
                supone que ya viene conectado*/
                if (getAD() == null){
                    setAD(new AccesoDatos());
                    getAD().conectar();
                    nRet = getAD().ejecutarConsulta(sQuery);
                    getAD().desconectar();
                    setAD(null);
                }
                else{
                    nRet = getAD().ejecutarConsulta(sQuery);
                }
            }
            return nRet.size();
        }
        
        /**
         * Eliminar una nueva cita e servicio en la base de datos
         * Regresa el número de registros afectados (Eliminados)
         **/
        public int eliminar() throws Exception{
            Vector nRet = null;
            String sQuery = "";
            if (this.oPaciente.getFolioPac()==0 || this.oAreaServicio.getCve().equals("") ||
                     this.dFecCita==null){
                throw new Exception("Cita Servicio.insertar: error de programación, faltan datos");
            }
            else {
                
                // select * from eliminarCitaServicio( integer, integer, date) 	
                SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                System.out.println("eliminar: "+formatoDelTexto.format(this.dFecCita));
            
                sQuery = " select eliminarCitaServicio ("+this.oPaciente.getFolioPac()+","+this.oAreaServicio.getCve()+","
                        +"'"+formatoDelTexto.format(this.dFecCita)+"')";
                System.out.println(sQuery);
                /*Si oAD es nulo, tiene que crearlo y conectarlo, de otro modo
                supone que ya viene conectado*/
                if (getAD() == null){
                    setAD(new AccesoDatos());
                    getAD().conectar();
                    nRet = getAD().ejecutarConsulta(sQuery);
                    getAD().desconectar();
                    setAD(null);
                }
                else{
                    nRet = getAD().ejecutarConsulta(sQuery);
                }
            }
            return nRet.size();
        }
    
        //=============== SET & GET ===============//
        public Paciente getPaciente(){
            return oPaciente;
        }
        public void setPaciente(Paciente opaciente){
            this.oPaciente=opaciente;
        }
        
        public AreaDeServicio getAreaServicio(){
            return oAreaServicio;
        }
        public void setAreaServicio(AreaDeServicio oareaServicio){
            this.oAreaServicio=oareaServicio;
        }
        
        public Date getFecCita() {
            return dFecCita;
        }
        public void setFecCita(Date dFecCita) {
            this.dFecCita = dFecCita;
        } 
        
        public int getDuracion() {
            return nDuracion;
        }
        public void setDuracion(int nDuracion) {
            this.nDuracion = nDuracion;
        }
        
        public int getTipoPrincipal() {
            return nTipoPrincipal;
        }
        public void setTipoPrincipal(int nTipoPrincipal) {
            this.nTipoPrincipal = nTipoPrincipal;
        }
        
        public String getObs() {
            return sObs;
        }
        public void setObs(String sObs) {
            this.sObs = sObs;
        }             
        
        public AccesoDatos getAD() {
            return oAD;
        }
        public void setAD(AccesoDatos oAD) {
            this.oAD = oAD;
        }
       
        public String getFecha(){
            SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return formatoDelTexto.format(dFecCita);
        }
         
        public void finalize() throws Throwable {

	}
}