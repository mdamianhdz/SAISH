package org.apli.jbs.caja;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;
import org.apli.jbs.PacienteJB;
import org.apli.modelbeans.AreaDeServicio;
import org.apli.modelbeans.ConceptoIngreso;
import org.apli.modelbeans.LineaIngreso;
import org.apli.modelbeans.Medico;
import org.apli.modelbeans.Paciente;
import org.apli.modelbeans.ServicioPrestado;
import org.apli.modelbeans.TipoPrincipalPaga;
import org.apli.modelbeans.Valida;
import org.primefaces.context.RequestContext;

/**
 *
 * @author BAOZ
 */
@ManagedBean(name = "oRegServCaja")
@ViewScoped
public class RegistrarServDesdeCaja   extends IngresosCaja implements Serializable{
    private Paciente paciente;
    private ServicioPrestado oSP;
    private ArrayList<LineaIngreso> lineasIngreso = null;
    private ArrayList<ConceptoIngreso> servicios = null;
    private ArrayList<Medico> medicos=null;
    private LineaIngreso lineaIngreso=new LineaIngreso();
    private ConceptoIngreso oServicioSeleccionado = new ConceptoIngreso();
    private int nIdMed;

    public RegistrarServDesdeCaja() {
        try{
            lineasIngreso = (new LineaIngreso()).todasLineasIngrServCaja();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void reiniciar(ActionEvent ae){
        limpiaPagina();
    }
    
    public void limpiaPagina(){
        sForma="";
        servicios = null;
        medicos = null;
        lineaIngreso = new LineaIngreso();
    }
    
    public void seleccionPaciente(ActionEvent ae){
    paciente = new PacienteJB().getPacienteSesion();
    oSP = new ServicioPrestado();
    FacesMessage msg;
        try{
            if (paciente != null){
                oSP.setPaciente(paciente);
            }
        }catch(Exception e){
            msg = new FacesMessage("Aviso", "Error al buscar el paciente");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            e.printStackTrace();
        }
    }
    
    public void actualizaListaServiciosOrdServ(){
        servicios = new ArrayList<ConceptoIngreso>();
        ConceptoIngreso oSM = new ConceptoIngreso();
        try{
            servicios = (ArrayList)oSM.buscaServicios(lineaIngreso, 
                paciente.getFolioPac(), 0, ConceptoIngreso.BUSQ_TODOS, 
                ConceptoIngreso.TIPO_CAJA,
                new AreaDeServicio());
            medicos = (new Medico()).buscaTodosParaCaja(
                    paciente.getFolioPac(), lineaIngreso.getCveLin());
            FacesContext facesContext = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) facesContext.getExternalContext().
                      getSession(false);
            session.setAttribute("listaServicios", servicios);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public List<ConceptoIngreso> obtenerServicios(String s) {
        List<ConceptoIngreso> servSelect = new ArrayList<ConceptoIngreso>();
        s = s.toUpperCase();
        servSelect = this.oServicioSeleccionado.obtenerServicios(s, servicios);
        return servSelect;
    }
    
    public void registraPago(){
    boolean bResult;
    FacesMessage msg;
        try{
            System.out.println();
            oOpeCaja.getFrmPago().setCveFrmPago(sForma);
            
            
            
            
            
            
            bResult = oOpeCaja.pagaAbono(this.selectedServ);
            
            
            
            
            
            if (bResult){
                RequestContext.getCurrentInstance().execute("PF('diaE').show()");
            }
            else{
                msg = new FacesMessage("Aviso", oOpeCaja.getFolioRet());
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }
        }catch(Exception e){
            e.printStackTrace();
            msg = new FacesMessage("Aviso", "Error al guardar el pago");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
    
    public void regresar() throws Exception {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.getExternalContext().redirect("registrarServDesdeCaja.xhtml?faces-redirect=true");
    }
    
    public boolean validaPaciente() {
        boolean validacion = false;
        if (paciente != null) {
            validacion = true;
        }
        return validacion;
    }
    
    @Override
    public void seleccion(){
        selectedServ.setPaciente(paciente);
        selectedServ.setCantidad(1);
        selectedServ.setCostoCobrado(this.oServicioSeleccionado.getMonto());
        selectedServ.setCostoOriginal(selectedServ.getCostoCobrado());
        selectedServ.setConcepPrestado(oServicioSeleccionado);
        selectedServ.setQuienPaga(TipoPrincipalPaga.TIPO_PART);
        selectedServ.setConvQuienPaga((new Valida()).QuienPaga(TipoPrincipalPaga.TIPO_PART));
        if (selectedServ == null) {
            FacesMessage msg = new FacesMessage("Aviso", "Falta seleccionar pago");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('selDialog').show()");
        }
    }
    
    public void pasaMonto(){
        this.selectedServ.setCostoCobrado(this.oServicioSeleccionado.getMonto());
    }
    
    public Paciente getPaciente() {
        return paciente;
    }
    
    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }
    
    public ArrayList<LineaIngreso> getLineasIngreso() {
        return lineasIngreso;
    }
    
    public LineaIngreso getLineaIngreso() {
        return lineaIngreso;
    }
    public void setLineaIngreso(LineaIngreso valor) {
        System.out.println(valor);
        this.lineaIngreso = valor;
    }

    public ConceptoIngreso getServicioSeleccionado() {
        return oServicioSeleccionado;
    }
    public void setServicioSeleccionado(ConceptoIngreso valor) {
        this.oServicioSeleccionado = valor;
        
    }
    
    public ArrayList<Medico> getMedicos(){
        return this.medicos;
    }
    
    public int getIdMed(){
        return this.nIdMed;
    }
    public void setIdMed(int valor){
        this.nIdMed = valor;
    }
}
