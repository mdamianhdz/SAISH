package org.apli.jbs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.apli.modelbeans.Paciente;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

/**
 * PacienteJB.java
 * JSF Managed Bean archivo que realiza la busqueda del paciente
 * @author HMarín
 * Fecha: 19 Febrero 2014
 */
@ManagedBean(name="oPacienteJB")
@ViewScoped
public class PacienteJB implements Serializable {
    
    private Paciente oPaciente;
    private List<Paciente> listaPac= new ArrayList();
    private Paciente[] arrPac = null;
    private Paciente selectedPaciente;
    
    public PacienteJB(){
        oPaciente= new Paciente();
        selectedPaciente=new Paciente();
        listaPac=new ArrayList<Paciente>();
    }    
    
    public void datos(){
        try{
            listaPac=new ArrayList<Paciente>();
            arrPac = (Paciente[])oPaciente.buscarTodosPacientesPorFiltro();
            for(int i=0; i<arrPac.length; i++){
                Paciente temp= arrPac[i];
                listaPac.add(temp);
            }
        }    catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void limpia(){
      listaPac=new ArrayList<Paciente>();
      selectedPaciente=new Paciente();
      oPaciente=new Paciente();
      FacesContext facesContext = FacesContext.getCurrentInstance();
      HttpSession session =(HttpSession)facesContext.getExternalContext().
      getSession(false);
      session.setAttribute("oPacienteSeleccionado",selectedPaciente);
      RequestContext.getCurrentInstance().execute("dlgCompBusqVar.show()");
    }
    public Paciente getPacienteSesion(){
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session =(HttpSession)facesContext.getExternalContext().getSession(true);
        return (Paciente)session.getAttribute("oPacienteSeleccionado");
    } 
    
     public void limpiaSelectedPaciente() {
      listaPac=new ArrayList<Paciente>();
      selectedPaciente=new Paciente();
      oPaciente=new Paciente();
      FacesContext facesContext = FacesContext.getCurrentInstance();
      HttpSession session =(HttpSession)facesContext.getExternalContext().
      getSession(false);
      session.setAttribute("oPacienteSeleccionado",selectedPaciente);
    }
    
    public Paciente getPaciente(){
        return oPaciente;
    }
    
    public void setPaciente(Paciente oPac){
        this.oPaciente=oPac;
    }
    
    public List<Paciente> getListaPaciente(){
        return listaPac;
    }
    
    public Paciente getSelectedPaciente() {  
        return selectedPaciente;  
    }  
    
    public void setSelectedPaciente(Paciente selectecPaciente) {
        this.selectedPaciente=selectecPaciente;
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session =(HttpSession)facesContext.getExternalContext().
        getSession(false);
        session.setAttribute("oPacienteSeleccionado",selectedPaciente);
    }
    
    public void seleccionado(){
        System.out.println("Folio: "+selectedPaciente.getFolioPac());
        System.out.println("Nombre: "+selectedPaciente.getNombre());
    }
    public void onRowSelect(SelectEvent event) {
        System.out.println("Paciente seleccionado "+this.selectedPaciente.getNombre()+" - "+this.selectedPaciente.getApellidoPaterno());
    }    
}
