package org.apli.jbs;
import org.apli.modelbeans.AreaDeServicio;
import org.apli.modelbeans.CitaServicio;
import org.apli.modelbeans.CitaServicioDataModel;
import org.apli.modelbeans.Paciente;
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
 * JSF Managed Bean archivo que controla la información referente al expediente
 * @author Humberto Marin Vega
 * Fecha: Abril 2014
 */
@ManagedBean(name="oAgServ")
@SessionScoped

public class AgendaServicioJB  implements Serializable{
        
    private AreaDeServicio oServ;
    private AreaDeServicio arrServ[];   
    private List<String> nomServ= new ArrayList();
  
    private CitaServicio oCitaServ;
    private List<CitaServicio> listaCitas= new ArrayList();
    
    private Date fecha;
    private String servicio;
    
    private CitaServicioDataModel oCitaSerModel;
    private CitaServicio[] citas;
    private CitaServicio selectedCitaServicio = new CitaServicio();
    
    public AgendaServicioJB(){
        oServ = new AreaDeServicio();
        oCitaServ = new CitaServicio();
        oCitaServ.setPaciente(new Paciente());
        selectedCitaServicio.setPaciente(new Paciente());
        
        //this.oCitaSerModel = new CitaServicioDataModel(listaCitas);
        this.obtieneServicios();
        
        listaCitas.clear();
        this.disable=false;
    }
    
    public void llena(){
        this.selectedCitaServicio.setPaciente(new PacienteJB().getPacienteSesion());
    }
    
    private void obtieneServicios(){
        try{
            arrServ= oServ.buscarTodasAreasDeServicioAgenda();
        }catch(Exception ex){
            ex.printStackTrace();
        }
        nomServ.clear();
        for(int i=0; i<arrServ.length;i++){
            nomServ.add(arrServ[i].getDescrip());
        }
    }
    
    public List<String> getServicios(){
        return nomServ;
    }
    
    public void datosServicio(){
        for(int i=0; i<arrServ.length;i++){
            if(arrServ[i].getDescrip().equals(servicio)){
                oServ.setCve(arrServ[i].getCve());
                oServ.setDescrip(arrServ[i].getDescrip());
                oServ.setHorario(arrServ[i].getHorario());
                oServ.setAgenda(arrServ[i].getAgenda());
            }
       }
        oCitaServ.setAreaServicio(oServ);
    }
    
    public void datosAgenda(){
        try{
            this.datosServicio();
            this.listaCitas.clear();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(fecha);
            int diaSem = calendar.get(Calendar.DAY_OF_WEEK);
            System.out.println("DAY_OF_WEEK: "+diaSem);
            this.calculaHoras(diaSem);
        }catch(Exception ex){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Faltan datos", "Verifique sus datos"));    
        }
    }
    
    public void calculaHoras(int diaCita){
        String horario= this.oServ.getHorario();
        StringTokenizer tokens=new StringTokenizer(horario,",");
        ArrayList<String> lista= new ArrayList<String>();
	while(tokens.hasMoreTokens()){
            lista.add(tokens.nextToken());
        }
        String hrIni="", hrFin="";
        boolean bandera=false;
        for(int i = 0; i < lista.size(); i++){
            if((i%2)==0){
                if(lista.get(i).length()>2){
                    String[] resultDays = lista.get(i).split("-");
                    if(diaCita>=obtieneDia(resultDays[0]) && diaCita<=obtieneDia(resultDays[1])){
                        bandera=true;
                        String [] resultHours = lista.get(i+1).split("-");
                        hrIni=resultHours[0];
                        hrFin=resultHours[1];
                    }else{
                        int df=0;
                        if(resultDays[1].equals("Do")) df=8;
                        System.out.println(diaCita+" >="+obtieneDia(resultDays[0])+" && "+diaCita+" <="+df+" || "+diaCita+" == "+obtieneDia(resultDays[1]) );
                        if(diaCita>=obtieneDia(resultDays[0]) && diaCita<=df || diaCita==obtieneDia(resultDays[1])){
                            bandera=true;
                            String [] resultHours = lista.get(i+1).split("-");
                            hrIni=resultHours[0];
                            hrFin=resultHours[1];
                        }
                    }
                }else{
                    if(diaCita==obtieneDia(lista.get(i))){
                        bandera=true;
                        String [] resultHours = lista.get(i+1).split("-");
                        hrIni=resultHours[0];
                        hrFin=resultHours[1];
                    }
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
                listaCitas.add(new CitaServicio(fechaD1, oServ));
                listaCitas.add(new CitaServicio(fechaD2, oServ));
            }
            if(mf==30){
                Date fechaD1= convierteFecha(""+convertido+" "+hf+":00");
                listaCitas.add(new CitaServicio(fechaD1, oServ));
            }
            if(hi>hf){
                int xhoras= hi-hf;
                int j=0;
                for(int i=0; i<xhoras;i++){
                    if(hi<24){
                        Date fechaD1= convierteFecha(""+convertido+" "+hi+":00");
                        Date fechaD2= convierteFecha(""+convertido+" "+hi+":30");
                        listaCitas.add(new CitaServicio(fechaD1, oServ));
                        listaCitas.add(new CitaServicio(fechaD2, oServ));
                        hi++;
                    }else if(j<hf){                       
                        Date fechaD1= convierteFecha(""+convertido+" "+j+":00");
                        Date fechaD2= convierteFecha(""+convertido+" "+j+":30");
                        listaCitas.add(new CitaServicio(fechaD1, oServ));
                        listaCitas.add(new CitaServicio(fechaD2, oServ));
                        j++;
                    }
                }
            }
            this.oCitaServ.setAreaServicio(oServ);
            this.oCitaServ.setFecCita(fecha);
            try{
                citas= this.oCitaServ.buscarTodasCitasDelServicio();
            }catch(Exception ex){
                ex.printStackTrace();
            }
            dateFormat = new SimpleDateFormat("hh:mm");
            for(int j=0; j<citas.length;j++){
                for(int i=0; i<listaCitas.size();i++){
                    if(dateFormat.format(listaCitas.get(i).getFecCita()).equals(dateFormat.format(citas[j].getFecCita()))){
                        listaCitas.remove(i);
                        citas[j].setAreaServicio(oServ);
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
        } else{
                   FacesMessage msg = new FacesMessage("Agenda de servicios", "No hay horario disponible para ese día.");
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
    
     public void limpiarAgenda(){
         this.oServ= new AreaDeServicio();
         this.listaCitas.clear();
         this.fecha=null;
         this.servicio="";
         this.selectedCitaServicio= new CitaServicio();
         this.selectedCitaServicio.setPaciente(new Paciente());
    }
     
     public void dirPaginaValida(){
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session =(HttpSession)facesContext.getExternalContext().
        getSession(false);
        session.setAttribute("paginaValida","/consultaExterna/nuevaCitaServicio");
     }
             
    
     public void eliminarCita(){
         this.selectedCitaServicio.setAreaServicio(oServ);
         
        if(selectedCitaServicio.getFecCita()!=null){
            try {
                int i=selectedCitaServicio.eliminar();
                if(i==1){
                   FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO , "Agenda de servicios", "Cita eliminada."));    
                }
            } catch (Exception ex) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Agenda de servicios", "Error: No se puede eliminar la cita."));    
            }
        }
    }
     
     public void insertarCita(){
        if(selectedCitaServicio.getPaciente()==null||
           selectedCitaServicio.getPaciente().getFolioPac()<1){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Seleccione un paciente", "Error: No se puede eliminar la cita."));    
        }else{
            try {
                if(selectedCitaServicio.getDuracion()==0){
                    selectedCitaServicio.setDuracion(1);
                }
                int i=selectedCitaServicio.insertar();
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
     
    public CitaServicio  getSelectedCitaServicio(){
        return selectedCitaServicio;
    }
    public void setSelectedCitaServicio(CitaServicio selCitaServ){
        this.selectedCitaServicio=selCitaServ;
        this.dirPaginaValida();
    }
    
    public Date getFecha(){
        return fecha;
    }
    public void setFecha(Date fecha){
        this.fecha=fecha;
    }
    public List<CitaServicio> getListaCitas(){
        return listaCitas;
    }
    public void setListaCitas(List<CitaServicio> citas){
       this.listaCitas=citas;
    }
    public AreaDeServicio getAreaDeServicio(){
        return oServ;
    }
    public void setAreaDeServicio(AreaDeServicio arserv){
        this.oServ=arserv;
    }
    public String getNombreServicio(){
        return servicio;
    }
    public void setNombreServicio(String servicio){
        this.servicio=servicio;
    }
  
}