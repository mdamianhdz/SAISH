package org.apli.jbs.pagoHonorarios;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apli.modelbeans.DetalleHonorarios;
import org.apli.modelbeans.DistribucionHonorarios;
import org.apli.modelbeans.Paciente;
import org.apli.modelbeans.PersonalHospitalario;
import org.apli.modelbeans.ServicioPrestado;

/**
 * RPTPagoHonorariosJB.java JSF Managed Bean archivo para el
 * reporte de pago de honorarios
 *
 * @author Humberto Marin Vega Fecha: Agosto 2014
 */
@ManagedBean(name = "rptCuentaPgHon")
@ViewScoped
public class RPTCuentaPagoHonorariosJB implements Serializable {
    
    private DetalleHonorarios oDetHon;
    private Paciente[] pacientes;
    private List<PersonalHospitalario> personal;
    private Paciente oPaciente;
    private PersonalHospitalario oPersHosp;
    private List<String> nombrePaciente;
    private String opcion="0";
    private String nomPaciente;
    private List<DetalleHonorarios> honorarios;
    private boolean pendientes=false;
    
    private List<DistribucionHonorarios> distribucion;

    public RPTCuentaPagoHonorariosJB() {
        oDetHon = new DetalleHonorarios();
        oPaciente = new Paciente();
        oPersHosp = new PersonalHospitalario();
        personal = new ArrayList<PersonalHospitalario>();
        nombrePaciente = new ArrayList<String>();
        honorarios = new ArrayList<DetalleHonorarios>();
        distribucion =  new ArrayList<DistribucionHonorarios>();
        this.obtienePacientes();
    }
    
    public void buscarDatos(){
        try{
            System.out.println("opcion:"+opcion);
            if(opcion.equals("0")){
                honorarios = oDetHon.buscaTodosRPTPagoHonorarios();
            }else if(opcion.equals("1")){
               honorarios = oDetHon.buscaTodosRPTPagoHonorariosPorPaciente(obtieneFolioPaciente());
            }else if(opcion.equals("2")){
                honorarios = oDetHon.buscaTodosRPTPagoHonorarios();
                this.honorariosPorHospital();
            }else if(opcion.equals("3")){
                honorarios = oDetHon.buscaTodosRPTPagoHonorarios();
                this.honorariosPorConsultaExterna();
            }
       }catch(Exception ex){
           ex.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Reporte de cuenta para pago de honorarios", "No se encuentran registros de pago de honorarios."));
        }
       verificaPagados();
       this.creaDistribucionHonorarios();
       if(distribucion.size()>0)
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Reporte de cuenta para pago de honorarios", "Pago de honorarios registrados."));
       else
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Reporte de cuenta para pago de honorarios", "No se encuentran registros de pago de honorarios."));
        
    }
    
    public void creaDistribucionHonorarios(){
        List<Integer> idPagoHon= new ArrayList<Integer>();
        distribucion.clear();
        for(int i=0; i<honorarios.size(); i++){
            idPagoHon.add(honorarios.get(i).getPagoHonorarios().getIdPagoHonorarios());
        }
        HashSet hs = new HashSet();
        hs.addAll(idPagoHon);
        idPagoHon.clear();
        idPagoHon.addAll(hs);
        int c=0;
        for(int i=0; i<idPagoHon.size(); i++){
            System.out.println(idPagoHon.get(i));
           for(int j=0; j<honorarios.size(); j++){
               if(idPagoHon.get(i)==honorarios.get(j).getPagoHonorarios().getIdPagoHonorarios() && c==0){
                   DistribucionHonorarios dis= new DistribucionHonorarios();
                   dis.setPagoHonorarios(honorarios.get(j).getPagoHonorarios());
                   distribucion.add(dis);
                   c++;
               }
           }
           c=0;
        }
        for(int i=0; i<distribucion.size(); i++){
            System.out.println("Personal"+distribucion.get(i).getPagoHonorarios().getPersonalHospitalario().getNombreCompleto());
        }
        
        for(int i=0; i<distribucion.size(); i++){
            distribucion.get(i).setDetalles(this.obtieneHonorariosDis(distribucion.get(i).getPagoHonorarios().getIdPagoHonorarios()));
            distribucion.get(i).setMontoTotalAPagar(this.obtieneMontoTotalAPagarDis(distribucion.get(i).getPagoHonorarios().getIdPagoHonorarios()));
        }
        
        for(int i=0; i<distribucion.size(); i++){
            System.out.println(distribucion.get(i).getPagoHonorarios().getIdPagoHonorarios()+" "+distribucion.get(i).getPagoHonorarios().getPersonalHospitalario().getNombreCompleto());
            List<DetalleHonorarios> dtHon= distribucion.get(i).getDetalles();
            for(int j=0; j<dtHon.size(); j++){
                System.out.println("DH"+dtHon.get(j).getServicioPrestado().getIdFolio());
            }
        }
        cambiaSituacion();
    }
    
     public void verificaPagados(){
        System.out.println(pendientes);
        List<DetalleHonorarios> disHon= new ArrayList<DetalleHonorarios>();
        for(int i=0; i<honorarios.size(); i++){
            if(honorarios.get(i).getPagoHonorarios().getSituacion().equals("2")){
                    disHon.add(honorarios.get(i));
            }
        }
        honorarios.clear();
        honorarios.addAll(disHon);
    }
    
    public void cambiaSituacion(){
        for(int i=0; i<distribucion.size(); i++){
            for(int j=0; j<distribucion.get(i).getDetalles().size(); j++){
            if(distribucion.get(i).getDetalles().get(j).getPagoHonorarios().getSituacion().equals("0")){
                distribucion.get(i).getDetalles().get(j).getPagoHonorarios().setSituacion("Por autorizar");
            }else if(distribucion.get(i).getDetalles().get(j).getPagoHonorarios().getSituacion().equals("1")){
                distribucion.get(i).getDetalles().get(j).getPagoHonorarios().setSituacion("Autorizado");
            }else if(distribucion.get(i).getDetalles().get(j).getPagoHonorarios().getSituacion().equals("2")){
                distribucion.get(i).getDetalles().get(j).getPagoHonorarios().setSituacion("Pagado");
            }
            }
        }
        cambiaSituacionSP();
    }
    
    public void cambiaSituacionSP(){
        for(int i=0; i<distribucion.size(); i++){
            for(int j=0; j<distribucion.get(i).getDetalles().size(); j++){
            if(distribucion.get(i).getDetalles().get(j).getServicioPrestado().getSituacion().equals("N")){
                distribucion.get(i).getDetalles().get(j).getServicioPrestado().setSituacion("Nuevo");
            }else if(distribucion.get(i).getDetalles().get(j).getServicioPrestado().getSituacion().equals("P")){
                distribucion.get(i).getDetalles().get(j).getServicioPrestado().setSituacion("Pagado");
            }else if(distribucion.get(i).getDetalles().get(j).getServicioPrestado().getSituacion().equals("A")){
                distribucion.get(i).getDetalles().get(j).getServicioPrestado().setSituacion("Autorizado a crédito");
            }else if(distribucion.get(i).getDetalles().get(j).getServicioPrestado().getSituacion().equals("O")){
                distribucion.get(i).getDetalles().get(j).getServicioPrestado().setSituacion("Autorizado a paquete");
            }
            }
        }
    }
    
    
    public List<DetalleHonorarios> obtieneHonorariosDis(int idPagoHon){
        List<DetalleHonorarios> detallesH= new ArrayList<DetalleHonorarios>();
        for(int i=0; i<honorarios.size(); i++){
            if(honorarios.get(i).getPagoHonorarios().getIdPagoHonorarios()== idPagoHon){
                    detallesH.add(honorarios.get(i));
              }
        }
        return detallesH;
    }
    
    public float obtieneMontoTotalAPagarDis(int idPagoHon){
        float total=0;
        for(int i=0; i<honorarios.size(); i++){
            if(honorarios.get(i).getPagoHonorarios().getIdPagoHonorarios()== idPagoHon){
                    total += honorarios.get(i).getAutorizado();
              }
        }
        return total;
    }
    
    
    private void obtienePacientes(){
        try{
            pacientes = oPaciente.buscarPacientesRPTCuentaPagoHonorarios();
        }catch(Exception ex){
            ex.printStackTrace();
        }
        for(int i=0; i<pacientes.length; i++){
            nombrePaciente.add(""+pacientes[i].getNombreCompleto());
        }
    }
    
    public int obtieneFolioPaciente(){
        int folioPac=0;
        for(int i=0; i<pacientes.length; i++){
            if(pacientes[i].getNombreCompleto().equals(nomPaciente)){
                folioPac=pacientes[i].getFolioPac();
            }
        }
        return folioPac;
    }
    
    public void honorariosPorHospital(){
        List<DetalleHonorarios> dHon = new ArrayList<DetalleHonorarios>();
        for(int i=0; i<honorarios.size(); i++){
            if(honorarios.get(i).getServicioPrestado().getEpisodioMedico().getHab().getHab()!=0){
                dHon.add(honorarios.get(i));
            }
        }
        honorarios.clear();
        honorarios.addAll(dHon);
    }
    
    public void honorariosPorConsultaExterna(){
        List<DetalleHonorarios> dHon = new ArrayList<DetalleHonorarios>();
        for(int i=0; i<honorarios.size(); i++){
            if(honorarios.get(i).getServicioPrestado().getEpisodioMedico().getHab().getHab()==0){
                dHon.add(honorarios.get(i));
            }
        }
        honorarios.clear();
        honorarios.addAll(dHon);
    }
    
    public void limpiar(){
        this.nomPaciente="";
        this.distribucion.clear();
        this.pendientes=false;
    }

 //=============== SET & GET ===============//
       
    public String getOpcion() {
        return opcion;
    }
    public void setOpcion(String opcion) {
        this.opcion = opcion;
    }

    public List<String> getNombrePaciente() {
        return nombrePaciente;
    }
    public void setNombrePaciente(List<String> nombrePaciente) {
        this.nombrePaciente = nombrePaciente;
    }

    public String getNomPaciente() {
        return nomPaciente;
    }
    public void setNomPaciente(String nomPaciente) {
        this.nomPaciente = nomPaciente;
    }

    public List<DetalleHonorarios> getHonorarios() {
        return honorarios;
    }
    public void setHonorarios(List<DetalleHonorarios> honorarios) {
        this.honorarios = honorarios;
    }

    public List<DistribucionHonorarios> getDistribucion() {
        return distribucion;
    }
    public void setDistribucion(List<DistribucionHonorarios> distribucion) {
        this.distribucion = distribucion;
    }

    public boolean isPendientes() {
        return pendientes;
    }
    public void setPendientes(boolean pedientes) {
        this.pendientes = pedientes;
    }
    
    public ServicioPrestado getSP(){
        return new ServicioPrestado();
    }
    
}