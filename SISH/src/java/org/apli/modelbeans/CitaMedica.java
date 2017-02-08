package org.apli.modelbeans;

import java.io.Serializable;
import org.apli.AD.AccesoDatos;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;



/**
 * Atenci�n de un m�dico a un paciente en consultorio/cirug�a
 * @author BAOZ
 * @version 1.0
 * @created 19-Abr-2014 01:41:29 a.m.
 */
public class CitaMedica implements Serializable{

	/**
        * Paciente
        */
        private Paciente oPaciente;
        /**
        * Medico
        */
        private Medico oMedico;
        /**
	 * Indica si lo que se agenda es una cirug�a o no (cita para consultorio)
	 */
	private boolean bCirugia;
	/**
	 * Fecha-hora de la cita
	 */
	private Date dFecCita;
	/**
	 * Duraci�n de la cita, se mide en medias horas
	 */
	private int nDuracion;
	/**
	 * Indica la clasificaci�n principal de la cita del paciente respecto a su pago
	 * 0 = de contado
	 * 1 = de cr�dito
	 * 2 = de paquete
	 */
	private int nTipoPrincipal;
	/**
	 * Observaciones para la cita m�dica (por ejemplo "en ayunas", "pupilas dilatadas",
	 * "llegar 15 minutos antes para preparaci�n", etc.)
	 */
	private String sObs="";
         /**
          * Variable para el acceso a datos
          */
        protected AccesoDatos oAD = null;

	public CitaMedica(){
            this.dFecCita= new Date();
            this.oMedico=new Medico();
            this.oMedico.setEsp(new Especialidad());
            this.oPaciente = new Paciente();
	}
        public CitaMedica(Date fecha, Medico oMed){
            this.dFecCita=fecha;
            this.oMedico=oMed;
            this.oMedico.setEsp(new Especialidad());
            this.oPaciente = new Paciente();
        }
        public CitaMedica(boolean cirugia, int duracion, int tipoPrincipal, String obs){
            this.bCirugia=cirugia;
            this.nDuracion=duracion;
            this.nTipoPrincipal=tipoPrincipal;
            this.sObs=obs;
        }
        
        /**
         * Busca la lista de todas las citas de un medico por un rango de fechas,
         * Regresa un arreglo de Citas medicas
         **/
        public CitaMedica[] buscarTodasCitasMedico()throws Exception{
            CitaMedica[] arrRet=null;
            CitaMedica oCitaMed=null;
            Vector rst = null;
            Vector<CitaMedica> vObj = null;
            String sQuery = "";
            int i=0, nTam = 0;
            sQuery =" select * from buscarTodasCitasPorCveMedico("+this.oMedico.getFolioPers()+",'"+this.dFecCita+"')";
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
                vObj = new Vector<CitaMedica>();
                Paciente tempPac;
              
                for (i = 0; i < rst.size(); i++) {
                    oCitaMed = new CitaMedica();
                    tempPac= new Paciente();
                    Vector vRowTemp = (Vector)rst.elementAt(i);
                    tempPac.setFolioPac(((Double) vRowTemp.elementAt(0)).intValue());
                    oCitaMed.setFecCita((Date) vRowTemp.elementAt(1));
                    if( Integer.parseInt((String)vRowTemp.elementAt(2))==0){
                        oCitaMed.setCirugia(false);
                    }else{
                        oCitaMed.setCirugia(true);
                    }
                    oCitaMed.setObs((String) vRowTemp.elementAt(3));
                    oCitaMed.setTipoPrincipal(((Double) vRowTemp.elementAt(4)).intValue());
                    oCitaMed.setDuracion(((Double) vRowTemp.elementAt(5)).intValue());
                    tempPac.setNombre((String) vRowTemp.elementAt(6));
                    tempPac.setApellidoPaterno((String) vRowTemp.elementAt(7));
                    tempPac.setApellidoMaterno((String) vRowTemp.elementAt(8));
                    oCitaMed.setPaciente(tempPac);     
                    oCitaMed.setMedico(oMedico);
                    vObj.add(oCitaMed);
                }
                nTam = vObj.size();
                arrRet = new CitaMedica[nTam];

                for (i=0; i<nTam; i++){
                    arrRet[i] = vObj.elementAt(i);
                }
            }
            return arrRet;
        }
        
        /**
         * Busca la lista de todas las citas de un medico por un rango de fechas,
         * Regresa un arreglo de Citas medicas
         **/
        public CitaMedica[] buscarTodasCitasMedicoPorFechas(Date fechaIni, Date fechaFin)throws Exception{
            CitaMedica[] arrRet=null;
            CitaMedica oCitaMed=null;
            Vector rst = null;
            Vector<CitaMedica> vObj = null;
            String sQuery = "";
            int i=0, nTam = 0;
            sQuery ="select * from buscarTodasCitasMedicoPorFechas("+this.oMedico.getFolioPers()+
                    ","+(fechaIni==null?"null":"'"+fechaIni+"'")+","+
                    (fechaFin==null?"null":"'"+fechaFin+"'")+")";
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
                vObj = new Vector<CitaMedica>();
                Paciente tempPac;
              
                for (i = 0; i < rst.size(); i++) {
                    oCitaMed = new CitaMedica();
                    tempPac= new Paciente();
                    //pa.nfoliopaciente(0), c.dfechoracita(1), c.bcirugia(2), c.sobservaciones(3), 
                    //c.ntipoprincipal(4), c.nduracion(5), pa.snombre(6), pa.sappaterno(7), pa.sapmaterno(8),
                    //pa.stelcasa(9), pa.stelcelular(10)
		    Vector vRowTemp = (Vector)rst.elementAt(i);
                    tempPac.setFolioPac(0);
                    oCitaMed.setFecCita((Date) vRowTemp.elementAt(1));
                    if( Integer.parseInt((String)vRowTemp.elementAt(2))==0){
                        oCitaMed.setCirugia(false);
                    }else{
                        oCitaMed.setCirugia(true);
                    }
                    oCitaMed.setObs((String) vRowTemp.elementAt(3));
                    oCitaMed.setTipoPrincipal(((Double) vRowTemp.elementAt(4)).intValue());
                    oCitaMed.setDuracion(((Double) vRowTemp.elementAt(5)).intValue());
                    tempPac.setNombre((String) vRowTemp.elementAt(6));
                    tempPac.setApellidoPaterno((String) vRowTemp.elementAt(7));
                    tempPac.setApellidoMaterno((String) vRowTemp.elementAt(8));
                    tempPac.setTelCasa((String) vRowTemp.elementAt(9));
                    tempPac.setTelCelular((String) vRowTemp.elementAt(10));
                    oCitaMed.setPaciente(tempPac);                   
                    vObj.add(oCitaMed);
                }
                nTam = vObj.size();
                arrRet = new CitaMedica[nTam];

                for (i=0; i<nTam; i++){
                    arrRet[i] = vObj.elementAt(i);
                }
            }
            return arrRet;
        }
        
        /**
         * Busca la lista de todas las citas,
         * Regresa un arreglo de Citas medicas
         **/
        public CitaMedica[] buscarTodasCitasFecha()throws Exception{
            CitaMedica[] arrRet=null;
            CitaMedica oCitaMed=null;
            Vector rst = null;
            Vector<CitaMedica> vObj = null;
            String sQuery = "";
            int i=0, nTam = 0;
            sQuery ="select * from buscarTodasCitasPorFecha('"+this.dFecCita+"')";
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
                vObj = new Vector<CitaMedica>();
                Paciente tempPac;
              
                for (i = 0; i < rst.size(); i++) {
                    oCitaMed = new CitaMedica();
                    tempPac= new Paciente();
                    Vector vRowTemp = (Vector)rst.elementAt(i);
                    tempPac.setFolioPac(((Double) vRowTemp.elementAt(0)).intValue());
                    oCitaMed.setFecCita((Date) vRowTemp.elementAt(1));
                    if( Integer.parseInt((String)vRowTemp.elementAt(2))==0){
                        oCitaMed.setCirugia(false);
                    }else{
                        oCitaMed.setCirugia(true);
                    }
                    oCitaMed.setObs((String) vRowTemp.elementAt(3));
                    oCitaMed.setTipoPrincipal(((Double) vRowTemp.elementAt(4)).intValue());
                    oCitaMed.setDuracion(((Double) vRowTemp.elementAt(5)).intValue());
                    tempPac.setNombre((String) vRowTemp.elementAt(6));
                    tempPac.setApellidoPaterno((String) vRowTemp.elementAt(7));
                    tempPac.setApellidoMaterno((String) vRowTemp.elementAt(8));
                    oCitaMed.setPaciente(tempPac);     
                    oCitaMed.setMedico(oMedico);
                    vObj.add(oCitaMed);
                }
                nTam = vObj.size();
                arrRet = new CitaMedica[nTam];

                for (i=0; i<nTam; i++){
                    arrRet[i] = vObj.elementAt(i);
                }
            }
            return arrRet;
        }
        
        
        /**
         * Busca la lista de todas las citas por especialidad,
         * Regresa un arreglo de Citas medicas
         **/
        public CitaMedica[] buscarTodasCitasEspecialidadFecha()throws Exception{
            CitaMedica[] arrRet=null;
            CitaMedica oCitaMed=null;
            Vector rst = null;
            Vector<CitaMedica> vObj = null;
            String sQuery = "";
            int i=0, nTam = 0;
            sQuery ="select * from buscarTodasCitasPorEspecialidadFecha('"+this.dFecCita+"','"+this.oMedico.getEsp().getDescrip()+"')";
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
                vObj = new Vector<CitaMedica>();
                Paciente tempPac;
              
                for (i = 0; i < rst.size(); i++) {
                    oCitaMed = new CitaMedica();
                    tempPac= new Paciente();
                    Vector vRowTemp = (Vector)rst.elementAt(i);
                    tempPac.setFolioPac(((Double) vRowTemp.elementAt(0)).intValue());
                    oCitaMed.setFecCita((Date) vRowTemp.elementAt(1));
                    if( Integer.parseInt((String)vRowTemp.elementAt(2))==0){
                        oCitaMed.setCirugia(false);
                    }else{
                        oCitaMed.setCirugia(true);
                    }
                    oCitaMed.setObs((String) vRowTemp.elementAt(3));
                    oCitaMed.setTipoPrincipal(((Double) vRowTemp.elementAt(4)).intValue());
                    oCitaMed.setDuracion(((Double) vRowTemp.elementAt(5)).intValue());
                    tempPac.setNombre((String) vRowTemp.elementAt(6));
                    tempPac.setApellidoPaterno((String) vRowTemp.elementAt(7));
                    tempPac.setApellidoMaterno((String) vRowTemp.elementAt(8));
                    oCitaMed.setPaciente(tempPac);     
                    oCitaMed.setMedico(oMedico);
                    vObj.add(oCitaMed);
                }
                nTam = vObj.size();
                arrRet = new CitaMedica[nTam];

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
            if (this.oPaciente.getFolioPac()==0 || this.oMedico.getFolioPers()==0 ||
                     this.nDuracion==0 ){
                throw new Exception("Cita Medica.insertar: error de programación, faltan datos");
            }
            else {
                 SimpleDateFormat formatoDelTexto = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                 int ncirugia=0;
                 if(this.bCirugia){
                     ncirugia=1;
                 }
            
                // select insertaCitaMedica(persona.folio, medico.folio, fecha, cirugia, duracion, tipo, Obs)	
                sQuery = " select insertaCitaMedica("+this.oPaciente.getFolioPac()+","+this.oMedico.getFolioPers()+","
                        +"'"+formatoDelTexto.format(this.dFecCita)+"',"+ncirugia+","+this.nDuracion+","+this.nTipoPrincipal+",'"+this.sObs+"')";
                /*Si oAD es nulo, tiene que crearlo y conectarlo, de otro modo
                supone que ya viene conectado*/
                System.out.print(sQuery);
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
            if (this.oPaciente.getFolioPac()==0 || this.oMedico.getFolioPers()==0 ||
                     this.dFecCita==null){
                throw new Exception("Cita Medica.insertar: error de programación, faltan datos");
            }
            else {
                // select * from eliminarCitaServicio( integer, integer, date) 	
                SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            
                sQuery = " select eliminarCitaMedica ("+this.oPaciente.getFolioPac()+","+this.oMedico.getFolioPers()+","
                        +"'"+formatoDelTexto.format(this.dFecCita)+"')";
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
        public Paciente getPaciente() {
            return oPaciente;
        }
        public void setPaciente(Paciente oPaciente) {
            this.oPaciente = oPaciente;
        }     
        public Medico getMedico() {
            return oMedico;
        }
        public void setMedico(Medico oMedico) {
            this.oMedico = oMedico;
        }      
        public boolean getCirugia() {
            return bCirugia;
        }
        public void setCirugia(boolean bCirugia) {
            this.bCirugia = bCirugia;
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
               
}