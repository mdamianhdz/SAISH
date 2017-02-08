package org.apli.jbs.cargoCuentas;
import org.apli.modelbeans.Paciente;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apli.modelbeans.EpisodioMedico;
import org.apli.modelbeans.ServicioPrestado;
 /**
 * ConsultaJB.java
 * JSF Managed Bean archivo que realiza el cierre de una cuenta
 * @author Humberto Marin Vega
 * Fecha: Junio 2014
 */
@ManagedBean(name="oCnlCargo")
@ViewScoped

public class CancelarCargoJB  implements Serializable{
        
    private Date fecha;
    private Paciente oPac;
    private EpisodioMedico oEpMed;
    
    private Paciente[] pacientes;
    private List<String> nomPacientes;
    private List<ServicioPrestado> servicios;
    
    private ServicioPrestado selectedServicioPrestado = new ServicioPrestado();
    
    private String paciente;
    private float subtotal=0;
    private float iva=0;
    private float total=0;
    
    public CancelarCargoJB(){
        oPac= new Paciente();
        oEpMed= new EpisodioMedico();
        nomPacientes= new ArrayList<String>();
        servicios = new ArrayList <ServicioPrestado>();
        fecha= new Date();
        obtienePacientesCtaAbierta();
        
        this.disable=false;
    }
    
    private void obtienePacientesCtaAbierta(){
        try{
            pacientes= oPac.buscarPacientesCuentaAbierta();
        }catch(Exception ex){
            ex.printStackTrace();
        }
        for(int i=0; i<pacientes.length;i++){
            nomPacientes.add(pacientes[i].getNombre()+" "+pacientes[i].getApellidoPaterno()+" "+pacientes[i].getApellidoMaterno());
        }
    }
    
    public void datosEpisodio(){
        for(int i=0; i<pacientes.length;i++){
            if((pacientes[i].getNombre()+" "+pacientes[i].getApellidoPaterno()+" "+pacientes[i].getApellidoMaterno()).equals(paciente)){
                try {
                    oPac=pacientes[i];
                   //EpisodioMedico oEM= new EpisodioMedico();
                   oEpMed=oEpMed.datosCierreCuenta(oPac.getFolioPac());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
       }
        iva=0;
        total=0;
        subtotal=0;
        obtieneServicios();
        this.disable=false;
    }
    
    public void obtieneServicios(){
        ServicioPrestado oSP= new ServicioPrestado();
        oSP.setEpisodioMedico(oEpMed);
        try {
            servicios=oSP.buscaServiciosPrestadosPorEpisodioMed();
            
            if (servicios!=null && servicios.size()>0)
                selectedServicioPrestado=servicios.get(0);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        calculaCuenta();
    }
    
    public void calculaCuenta(){
        float preciosiniva=0;
        this.subtotal=0;
        this.iva=0;
        this.total=0;
        for(int i=0; i<servicios.size();i++){
            preciosiniva=(servicios.get(i).getCostoCobrado()*100)/(100+servicios.get(i).getPctIVA());
            subtotal+=preciosiniva;
            iva+=preciosiniva*servicios.get(i).getPctIVA()/100;
            total+=servicios.get(i).getCostoCobrado();
        }
    }
    
    public void cerrarCuenta(){
        String msj;
        FacesMessage msg;
        try {
            msj=this.oEpMed.cerrarCuenta(this.oPac.getFolioPac());
            msj= msj.substring(2, msj.length()-1);
            msg = new FacesMessage("Cuenta cerrada", msj);
        } catch (Exception ex) {
            msg = new FacesMessage("Error", "No se pudo cerrar la cuenta");
        }
        FacesContext.getCurrentInstance().addMessage(null, msg);
        this.disable=true;
    }
    
    public void limpiar(){
         this.paciente="";
         this.oEpMed= new EpisodioMedico();
         this.oPac= new Paciente();
         this.servicios.clear();
         this.nomPacientes.clear();
         obtienePacientesCtaAbierta();
         this.total=0;
         this.subtotal=0;
         this.iva=0;
         this.selectedServicioPrestado=new ServicioPrestado();
    }
    
     public void cancelarCargo(){
        String msj;
        FacesMessage msg;
        this.selectedServicioPrestado.setEpisodioMedico(oEpMed);
        this.selectedServicioPrestado.setPaciente(oPac);
        try {
            msj=this.selectedServicioPrestado.cancelarCargo();
            msj= msj.substring(2, msj.length()-1);
            msg = new FacesMessage(msj, "Eliminado " + selectedServicioPrestado.getIdFolio());
        } catch (Exception ex) {
            msg = new FacesMessage("Error", "No se canceló el cargo.");
            ex.printStackTrace();
        }
        FacesContext.getCurrentInstance().addMessage(null, msg);
        this.obtieneServicios();
    }
    

    private boolean disable;

    public boolean isDisable() {
       return disable;
    }
    public void setDisable(boolean disable) {
       this.disable = disable;
    }
    
    //=============== SET & GET ===============//
    
    public ServicioPrestado getSelectedServicioPrestado() {
        return selectedServicioPrestado;
    }
    public void setSelectedServicioPrestado(ServicioPrestado value) {
        this.selectedServicioPrestado = value;
        System.out.println("click "+this.selectedServicioPrestado.getIdFolio());
    }
    
    public List<String> getPacientes(){
        return nomPacientes;
    }
    
    public Date getFecha(){
        return fecha;
    }
    public void setFecha(Date fecha){
        this.fecha=fecha;
    }

    public Paciente getPaciente() {
        return oPac;
    }
    public void setPaciente(Paciente oPac) {
        this.oPac = oPac;
    }

    public EpisodioMedico getEpisodioMedico() {
        return oEpMed;
    }
    public void setEpisodioMedico(EpisodioMedico oEpMed) {
        this.oEpMed = oEpMed;
    }

    public List<String> getNomPacientes() {
        return nomPacientes;
    }
    public void setNomPacientes(ArrayList<String> nomPacientes) {
        this.nomPacientes = nomPacientes;
    }

    public List<ServicioPrestado> getServicios() {
        return servicios;
    }
    public void setServicios(ArrayList<ServicioPrestado> servicios) {
        this.servicios = servicios;
    }
    
    public String getNombrePaciente(){
        return paciente;
    }
    public void setNombrePaciente(String nompaciente){
        this.paciente=nompaciente;
    }

    //--- cuenta
    public float getSubtotal() {
        return subtotal;
    }
    public void setSubtotal(float subtotal) {
        this.subtotal = subtotal;
    }

    public float getIva() {
        return iva;
    }
    public void setIva(float iva) {
        this.iva = iva;
    }

    public float getTotal() {
        return total;
    }
    public void setTotal(float total) {
        this.total = total;
    }
    public void onRowSelect(){
        System.out.println("click 2"+this.selectedServicioPrestado.getIdFolio());
    }
}