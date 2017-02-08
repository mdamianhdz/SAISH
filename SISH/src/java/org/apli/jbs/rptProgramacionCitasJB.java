package org.apli.jbs;
import org.apli.AD.AccesoDatos;
import org.apli.modelbeans.CitaMedica;
import org.apli.modelbeans.Especialidad;
import org.apli.modelbeans.Medico;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Vector;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
 /**
 * ConsultaJB.java
 * JSF Managed Bean archivo que controla la información referente al expediente
 * @author Humberto Marin Vega
 * Fecha: 3 Marzo 2014
 */
@ManagedBean(name="rptProgCitas")
@SessionScoped
        
public class rptProgramacionCitasJB  implements Serializable{

    private Especialidad oEsp;
    private Medico oMed;
    private Medico arrMed[] = null;
    
    private Especialidad arrEsp[] = null;
    private CitaMedica oCitaMed=null;
    private CitaMedica[] citas=null;   
    private Date fecha;
    private String doctor;
    
    public rptProgramacionCitasJB(){
        oEsp= new Especialidad();
        oMed=new Medico();
        oMed.setEsp(new Especialidad(0,""));
        oCitaMed= new CitaMedica();
        obtieneFecha();
        cargaEspecialidades();
        cargaMedicosEsp();
    }
    
    private void obtieneFecha(){
        Calendar fechaAct = new GregorianCalendar();
        int dias= fechaAct.get(Calendar.DAY_OF_WEEK);
        try{
            if(dias==6){
                fechaDiaHabil(2,fechaAct.getTime());
            }else{
                fechaDiaHabil(1, fechaAct.getTime());
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }   
    
    private void cargaEspecialidades(){
        try {
            arrEsp=oEsp.buscarTodasEspecialidades();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void cargaMedicosEsp(){
        arrMed= null;
        if(!oMed.getEsp().getDescrip().equals("*")){
            try{
                arrMed=oMed.buscarTodosMedicosPorEspecialidad();
            } catch(Exception ex){
                ex.printStackTrace();
            }
        }else{
            try{
                arrMed=oMed.buscarTodosMedicosAgenda();
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }
    
    public List<String> getMedicos(){
        List<String> nomMed= new ArrayList();
        for(int i=0; i<arrMed.length; i++){
            nomMed.add(arrMed[i].getNombreCompleto());
        }
        return nomMed;
    }
   
    public List<String> getEspecialidades(){
        List<String> nomEsp= new ArrayList();
        for(int i=0; i<arrEsp.length; i++){
            nomEsp.add(arrEsp[i].getDescrip());
        }
        return nomEsp;
    }
 
    public void datosAgenda(){
        FacesMessage msg= new FacesMessage();
        oCitaMed.setFecCita(fecha);
        if(!doctor.equals("*")){
            for(int i=0; i<arrMed.length; i++){
                if(doctor.equals(arrMed[i].getNombreCompleto())){
                    oCitaMed.setMedico(arrMed[i]);
                }
            }
            try{
                citas=oCitaMed.buscarTodasCitasMedico();
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, 
                        "Reporte de programación de citas", "Reporte generado.");
            } catch (Exception ex) {
                ex.printStackTrace();
                msg = new FacesMessage(FacesMessage.SEVERITY_WARN, 
                        "Reporte de programación de citas", "Error, faltan datos.");
            }
        }else{
            if(!this.oMed.getEsp().getDescrip().equals("*")){
                try {
                    for(int i=0; i<arrMed.length; i++){
                        if(doctor.equals(arrMed[i].getNombreCompleto())){
                            oCitaMed.setMedico(arrMed[i]);
                            oCitaMed.getMedico().setEsp(oMed.getEsp());
                        }
                    }
                    oCitaMed.setMedico(oMed);
                    citas=oCitaMed.buscarTodasCitasEspecialidadFecha();
                    msg = new FacesMessage(FacesMessage.SEVERITY_INFO, 
                            "Reporte de programación de citas", "Reporte generado.");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    msg = new FacesMessage(FacesMessage.SEVERITY_WARN, 
                            "Reporte de programación de citas", "Error,faltan datos.");
                }
            }else{
                try {
                    citas=oCitaMed.buscarTodasCitasFecha();
                    msg = new FacesMessage(FacesMessage.SEVERITY_INFO, 
                            "Reporte de programación de citas", "Reporte generado.");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    msg = new FacesMessage(FacesMessage.SEVERITY_WARN, 
                            "Reporte de programación de citas", "Error, faltan datos.");
                }
            }
        }
        FacesContext.getCurrentInstance().addMessage(null, msg);      
    }
    
    public void limpiar(){
        oCitaMed= new CitaMedica();
        citas=null;
        this.doctor="";
    }
    
    public Medico getMedico(){
        return oMed;
    }
    public void setMedico(Medico med){
        this.oMed=med;
    }
    public Date getFecha(){
        return fecha;
    }
    public void setFecha(Date fecha){
        this.fecha=fecha;
    }
    public CitaMedica[] getCitas(){
        return citas;
    }
    public void setCitas(CitaMedica[] citasM){
        this.citas=citasM;
    }
    public Especialidad getEspecialidad(){
        return oEsp;
    }
    public void setEspecialidad(Especialidad esp){
        esp=oEsp;
    }
    public String getDoctor(){
        return doctor;
    }
    public void setDoctor(String doc){
        this.doctor=doc;
    }
    
    //*****************************************************************************************************
    /**
     * Consulta fecha dia habil
     */
    public boolean fechaDiaHabil(int noD, Date fechaD)throws Exception{
        boolean bRet = false;
        Vector rst = null;
        String sQuery = "select * from calculodiashabiles("+noD+",'"+fechaD+"')";

        /*Si oAD es nulo, tiene que crearlo y conectarlo, de otro modo
        supone que ya viene conectado*/
       if (this.getAD() == null){
            this.setAD(new AccesoDatos());
            this.getAD().conectar();
            rst = this.getAD().ejecutarConsulta(sQuery);
		this.getAD().desconectar();
                setAD(null);
            }
            else{
		rst = this.getAD().ejecutarConsulta(sQuery);
            }
            if (rst != null && rst.size() == 1) {
                Vector vRowTemp = (Vector)rst.elementAt(0);
                this.setFecha((Date) vRowTemp.elementAt(0));
                bRet = true;
            }
	return bRet;     
    }

    protected AccesoDatos oAD = null;

    public AccesoDatos getAD() {
            return oAD;
    }
    public void setAD(AccesoDatos oAD) {
            this.oAD = oAD;
    }
}