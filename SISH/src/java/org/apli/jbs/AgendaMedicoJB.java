package org.apli.jbs;
import org.apli.modelbeans.CitaMedica;
import org.apli.modelbeans.CitaMedicaDataModel;
import org.apli.modelbeans.Especialidad;
import org.apli.modelbeans.Medico;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
 /**
 * ConsultaJB.java
 * JSF Managed Bean archivo que controla la información referente al 
 * registrar una nueva cita medica.
 * @author Humberto Marin Vega
 * Fecha: Abril 2014
 */
@ManagedBean(name="oAgMedico")
@SessionScoped
public class AgendaMedicoJB  implements Serializable{
        
    private Medico oMed;
    private Medico arrMed[] ;
    private List<String> nomMed= new ArrayList();
    
    private CitaMedica oCitaMed;
    private List<CitaMedica> listaCitas= new ArrayList();
    
    private String doctor;
    private Date fecha;
   
    private CitaMedicaDataModel oCitaMedModel;
    private CitaMedica[] citas;     
    private CitaMedica selectedCitaMedica= new CitaMedica();   
    
   
    public AgendaMedicoJB(){
        oMed= new Medico();
        oCitaMed= new CitaMedica();
        
        //oCitaMedModel= new CitaMedicaDataModel(listaCitas);
        oMed.setEsp(new Especialidad());
        obtieneMedicos();
        //nuevaCita.setFecCita(new Date());
        listaCitas.clear();
        this.disable=false;
    }
    
    public void llena(){
        this.selectedCitaMedica.setPaciente(new PacienteJB().getPacienteSesion());
    }
    
    private void obtieneMedicos(){
        try{
            arrMed= oMed.buscarTodosMedicosAgenda();
        }catch(Exception ex){
            ex.printStackTrace();
        }
        nomMed.clear();
        for(int i=0; i<arrMed.length;i++){
            nomMed.add(arrMed[i].getNombreCompleto());
        }
    }
    
    public List<String> getMedicos(){
        return nomMed;
    }
    
    public void datosMedico(){
        for(int i=0; i<arrMed.length;i++){
            String nc=arrMed[i].getNombreCompleto();
            if(nc.equals(doctor)){
                oMed.setFolioPers(arrMed[i].getFolioPers());
                oMed.setNombre(arrMed[i].getNombre());
                oMed.setApellidoPaterno(arrMed[i].getApellidoPaterno());
                oMed.setApellidoMaterno(arrMed[i].getApellidoMaterno());
                oMed.setEsp(arrMed[i].getEsp());
                oMed.setCedProf(arrMed[i].getCedProf());
                oMed.setActivo(arrMed[i].isbActivo());
                oMed.setTurno(arrMed[i].getTurno());
            }
        }
      }
    
    
    public void calculaHoras(int diaCita){
        String horario= oMed.getTurno().getHorario();
        StringTokenizer tokens=new StringTokenizer(horario,",");
        ArrayList<String> lista= new ArrayList<String>();
	while(tokens.hasMoreTokens()){
            lista.add(tokens.nextToken());
        }
        String hrIni="", hrFin="";
        boolean bandera=false;
        for(int i = 0; i < lista.size(); i++){
            if((i%2)==0){
                if(lista.get(i).length()>3){
                    String[] resultDays = lista.get(i).split("-");
                    if(diaCita>=obtieneDia(resultDays[0]) && diaCita<=obtieneDia(resultDays[1])){
                        bandera=true;
                        String [] resultHours = lista.get(i+1).split("-");
                        hrIni=resultHours[0];
                        hrFin=resultHours[1];
                    }
                }else{
                    if(diaCita==obtieneDia(lista.get(i))){
                        bandera=true;
                        String [] resultHours = lista.get(i+1).split("-");
                        hrIni=resultHours[0];
                        hrFin=resultHours[1];
                    }
                }
            }else{
                if(!bandera){
                    //String [] resultHours = lista.get(i).split("-");
                    //hrIni=resultHours[0];
                    //hrFin=resultHours[1];
                //}else{
                    // System.out.println("No hay horario disponible para ese dia");
                }
            }
        }
        if(bandera){
            String [] resultHI = hrIni.split(":");
            String [] resultHF = hrFin.split(":");
            int hi= Integer.parseInt(resultHI[0]);
            int hf= Integer.parseInt(resultHF[0]);
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String	convertido = dateFormat.format(this.fecha);
            int mi = Integer.parseInt(resultHI[1]);
            int mf = Integer.parseInt(resultHF[1]);
            for(int i= hi; i<=hf-1; i++){
                Date fechaD1= convierteFecha(""+convertido+" "+i+":00");
                Date fechaD2= convierteFecha(""+convertido+" "+i+":30");
                listaCitas.add(new CitaMedica(fechaD1, oMed));
                listaCitas.add(new CitaMedica(fechaD2, oMed));
            }
            if(mf==30){
                Date fechaD1= convierteFecha(""+convertido+" "+hf+":00");
                listaCitas.add(new CitaMedica(fechaD1, oMed));
            }
            if(hi>hf){
                int xhoras= hi-hf;
                int j=0;
                for(int i=0; i<xhoras;i++){
                    if(hi<24){
                        Date fechaD1= convierteFecha(""+convertido+" "+hi+":00");
                        Date fechaD2= convierteFecha(""+convertido+" "+hi+":30");
                        listaCitas.add(new CitaMedica(fechaD1, oMed));
                        listaCitas.add(new CitaMedica(fechaD2, oMed));
                        hi++;
                    }else if(j<hf){                       
                        Date fechaD1= convierteFecha(""+convertido+" "+j+":00");
                        Date fechaD2= convierteFecha(""+convertido+" "+j+":30");
                        listaCitas.add(new CitaMedica(fechaD1, oMed));
                        listaCitas.add(new CitaMedica(fechaD2, oMed));
                        j++;
                    }
                }
            }
            
            oCitaMed.setMedico(oMed);
            oCitaMed.setFecCita(this.fecha);
            try{
                citas= oCitaMed.buscarTodasCitasMedico();
            }catch(Exception ex){
                ex.printStackTrace();
            }
            dateFormat = new SimpleDateFormat("hh:mm");
            for(int j=0; j<citas.length;j++){
                for(int i=0; i<listaCitas.size();i++){
                    if(dateFormat.format(listaCitas.get(i).getFecCita()).equals(dateFormat.format(citas[j].getFecCita()))){
                        listaCitas.remove(i);
                        listaCitas.add(i, citas[j]);
                        if(citas[j].getDuracion()>1){
                            for(int q=1; q<citas[j].getDuracion();q++){
                                listaCitas.remove(i+1);
                            }
                        }
                        break;
                    }
                }
            }   
        }
        else{
                   FacesMessage msg = new FacesMessage("Agenda médica", "No hay horario disponible para ese día.");
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                    // System.out.println("No hay horario disponible para ese dia");
                }
    }
    
    public Date convierteFecha(String strFecha){
        SimpleDateFormat formatoDelTexto = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date datFecha = null;
        try {
            datFecha = formatoDelTexto.parse(strFecha);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return datFecha;
    }
    
    public int obtieneDia(String dia){
        int rdia=0;
        if (dia.equals("Do"))
            rdia=1;
        if(dia.equals("Lu"))
            rdia=2;
        if(dia.equals("Ma"))
            rdia=3;
        if(dia.equals("Mi"))
            rdia=4;
        if(dia.equals("Ju"))
            rdia=5;
        if(dia.equals("Vi"))
            rdia=6;
        if(dia.equals("Sa"))
            rdia=7;
        return rdia;
    }
    
    public void datosAgenda(){
        try{
            this.datosMedico();
            this.listaCitas.clear();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(fecha);
            int diaSem = calendar.get(Calendar.DAY_OF_WEEK);
            this.calculaHoras(diaSem);
        }catch(Exception ex){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Faltan datos", "Verifique sus datos"));    
        }
    }
    
    public void limpiarAgenda(){
       this.oMed=new Medico();
       this.oMed.setEsp(new Especialidad());
       this.listaCitas.clear();
       this.fecha=null;
       this.doctor="";
       this.selectedCitaMedica=new CitaMedica();
    }
    
    public void eliminarCita(){
        if(this.selectedCitaMedica.getFecCita()!=null){
            try{
                int i=selectedCitaMedica.eliminar();
                if(i==1){
                   FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO , "Agenda medica", "Cita eliminada."));    
                }
            } catch (Exception ex) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Agenda medica", "Error: No se puede eliminar la cita."));    
            }
        }
        this.selectedCitaMedica=new CitaMedica();
    }
    
    public void insertarCita(){
        if(selectedCitaMedica.getPaciente()==null){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Seleccione un paciente", "Error: No se puede eliminar la cita."));    
        }else{
            try {
                if(selectedCitaMedica.getDuracion()==0){
                    selectedCitaMedica.setDuracion(1);
                }
                int i=selectedCitaMedica.insertar();
                if(i==1){
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Cita Registrada", "Nueva cita medica registrada."));
                    this.disable=true;
                }
            } catch (Exception ex) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "No se puede registrar la cita", "Error: No se puede eliminar la cita."));    
                ex.printStackTrace();
            }        
        }
    }
    
    public void dirPaginaValida(){
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session =(HttpSession)facesContext.getExternalContext().
        getSession(false);
        session.setAttribute("paginaValida","/consultaExterna/nuevaCitaMedica");
     }
    
    public void activaBoton(){
        this.disable=false;
    }
    
 //=============================== SET & GET ===============================//
    private boolean disable=false;
    public boolean getDisable(){
        return disable;
    }
    public void setDisable(boolean disable){
        this.disable=disable;
    }
            
    public CitaMedica  getSelectedCitaMedica(){
        return selectedCitaMedica;
    }
    public void setSelectedCitaMedica(CitaMedica selCitaMed){
        this.selectedCitaMedica=selCitaMed;
        this.dirPaginaValida();
    }
    public Medico getMedico(){
        return oMed;
    }
    public void setMedico(Medico med){
        this.oMed=med;
    }
    public String getDoctor(){
        return doctor;
    }
    public void setDoctor(String dr){
        this.doctor=dr;
    }
    public Date getFecha(){
        return fecha;
    }
    public void setFecha(Date fecha){
        this.fecha=fecha;
    }
    public List<CitaMedica> getListaCitas(){
        return listaCitas;
    }
    public void setListaCitas(List<CitaMedica> citas){
       this.listaCitas=citas;
    }
           
}