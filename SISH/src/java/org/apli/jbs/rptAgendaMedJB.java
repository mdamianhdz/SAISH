package org.apli.jbs;
import org.apli.modelbeans.CitaMedica;
import org.apli.modelbeans.Especialidad;
import org.apli.modelbeans.Medico;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.apli.modelbeans.TipoPrincipalPaga;
 /**
 * ConsultaJB.java
 * JSF Managed Bean archivo que controla la información referente al expediente
 * @author Humberto Marin Vega
 * Fecha: 3 Marzo 2014
 */
@ManagedBean(name="rptAgMed")
@SessionScoped

public class rptAgendaMedJB  implements Serializable{
    
    private Especialidad oEsp;
    private Medico oMed;
    private Medico arrMed[] = null;
    
    private Especialidad arrEsp[] = null;
    private CitaMedica oCitaMed=null;
    private CitaMedica[] citas=null;   
    private Date fechaIni;
    private Date fechaFin;
    private String doctor;
    private List<String> nomMed= new ArrayList();
        
    public rptAgendaMedJB(){
        oEsp= new Especialidad();
        oMed=new Medico();
        oMed.setEsp(new Especialidad(0,""));
        oCitaMed= new CitaMedica();
        cargaEspecialidades();
        cargaMedicosEsp();
    }
    
    private void cargaEspecialidades(){
        try {
            arrEsp=oEsp.buscarTodasEspecialidades();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void cargaMedicosEsp(){
        try{
            arrMed=oMed.buscarTodosMedicosPorEspecialidad();
        } catch(Exception ex){
            ex.printStackTrace();
        }
        nomMed.clear();
    }
    
    public List<String> getMedicos(){
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
        for(int i=0; i<arrMed.length; i++){
            if(doctor.equals(arrMed[i].getNombreCompleto())){
                oCitaMed.setMedico(arrMed[i]);
            }
        }
        try {
            citas=oCitaMed.buscarTodasCitasMedicoPorFechas(fechaIni, fechaFin);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void limpiarRptAgendaMed(){
        //this.arrMed= null;
        //this.oMed=new Medico();
        citas=null;
        this.oMed= new Medico();
        this.oMed.setEsp(new Especialidad(0,""));
        this.fechaIni=null;
        this.fechaFin=null;
        this.doctor="";
        this.nomMed.clear();
    }
    
    public Medico getMedico(){
        return oMed;
    }
    public void setMedico(Medico med){
        this.oMed=med;
    }
    public Date getFechaInicio(){
        return fechaIni;
    }
    public void setFechaInicio(Date fechaI){
        this.fechaIni=fechaI;
    }
    public Date getFechaFin(){
        return fechaFin;
    }
    public void setFechaFin(Date fechaF){
        this.fechaFin=fechaF;
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
    
    public boolean isContado(int n){
        return (n == TipoPrincipalPaga.TIPO_PART ||
                n == TipoPrincipalPaga.TIPO_DSCTO);
    }
    public boolean isCredito(int n){
        return n == TipoPrincipalPaga.TIPO_EMP;
    }
    public boolean isPaquete(int n){
        return n == TipoPrincipalPaga.TIPO_PAQ;
    }
}