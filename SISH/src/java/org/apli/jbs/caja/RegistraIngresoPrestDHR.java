package org.apli.jbs.caja;

import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.apli.modelbeans.AreaDeServicio;
import org.apli.modelbeans.ConceptoIngreso;
import org.apli.modelbeans.LineaIngreso;
import org.apli.modelbeans.PersonalHospitalario;
import org.primefaces.context.RequestContext;

/**
 * JB para el registro de ingresos NO relacionados con servicios del sanatorio
 * (ajustes, ingreso por préstamos de DHR a SH, etc.)
 * @author BAOZ
 */
@ManagedBean(name = "oRegIngrPresDHR")
@ViewScoped
public class RegistraIngresoPrestDHR  extends IngresosCaja{
float nMonto;
private String sFolioRet="";
private ArrayList<LineaIngreso> lineasIngreso = null;
private ArrayList<ConceptoIngreso> arrServicios=null;
private LineaIngreso oLinSelec;
private ConceptoIngreso oServicioSeleccionado;
private boolean bMostrarPersonal=false;
private List<PersonalHospitalario> listPH;

    public RegistraIngresoPrestDHR() {
        super();
        oLinSelec =new LineaIngreso();
        try{
            lineasIngreso = (ArrayList)(new LineaIngreso()).todasLineasIng(
                    "0", ConceptoIngreso.TIPO_CAJA);
            listPH = (new PersonalHospitalario()).buscaTodosPersonalActivo();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void actualizaListaServicios() throws Exception {
        ConceptoIngreso oSM = new ConceptoIngreso();
        arrServicios = new ArrayList<ConceptoIngreso>();
        this.oServicioSeleccionado = new ConceptoIngreso();
        this.bMostrarPersonal = false;
        arrServicios = (ArrayList)oSM.buscaServicios(this.oLinSelec,
                  0, 0, ConceptoIngreso.BUSQ_TODOS,ConceptoIngreso.TIPO_CAJA, 
                  new AreaDeServicio());
        
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().
                  getSession(false);
        session.setAttribute("listaServicios", arrServicios);
    }
    
    public void seleccionaServicio(){
        if (this.oServicioSeleccionado != null && 
            (this.oServicioSeleccionado.getSoloEmpleado() || 
            this.oServicioSeleccionado.getSoloMedico())){
            this.bMostrarPersonal = true;
        }
        else
            this.bMostrarPersonal = false;
        
    }

        public List<ConceptoIngreso> obtenerServicios(String s) {
        List<ConceptoIngreso> servSelect = new ArrayList<ConceptoIngreso>();
        s = s.toUpperCase();
        try {

            if (s.trim().equals("")) {
                return new ArrayList<ConceptoIngreso>();
            }
            for (ConceptoIngreso srv : arrServicios) {
                if (srv.getDescripcion().contains(s)
                          || srv.getDescripcion().toUpperCase().contains(
                                    s.toLowerCase())) {
                    servSelect.add(srv);
                }
            }
            return servSelect;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
        
    public void validaRegistroPago() throws Exception {
        FacesMessage msg;
        if (this.oOpeCaja != null) {
            if (this.oOpeCaja.getObs().equals("") ||
                nMonto <=0) {
                msg = new FacesMessage("Aviso", "Faltan Datos.");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            } else {
                try{
                    this.oOpeCaja.setMontoOtro(nMonto);
                    this.oOpeCaja.getFrmPago().setCveFrmPago(sForma);
                    this.oOpeCaja.setDescripcion("Otro Ingreso");
                    this.oOpeCaja.registraIngresoOtrosServ(
                            this.oServicioSeleccionado.getCveConcep());
                    sFolioRet = this.oOpeCaja.getFolioRet();
                    RequestContext.getCurrentInstance().execute("diaE.show()");
                }catch(Exception e){
                    e.printStackTrace();
                    msg =new FacesMessage("Aviso","Error al guardar los datos");
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                }
            }
        } 
    }
    
    public void regresa() throws Exception {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.getExternalContext().redirect(
                "registrarIngrPrestDHR.xhtml?faces-redirect=true");
    }
    
    public float getMonto(){
        return this.nMonto;
    }
    
    public void setMonto(float valor){
        this.nMonto = valor;
    }
    
    public String getFolioRet() {
        return sFolioRet;
    }
    public void setFolioRet(String valor) {
        this.sFolioRet = valor;
    }
    
    public LineaIngreso getLineaSelec(){
        return this.oLinSelec;
    }
    public void setLineaSelec(LineaIngreso valor){
        this.oLinSelec = valor;
    }

    public List<LineaIngreso> getLineasIngreso() {
        return lineasIngreso;
    }
    
    public ConceptoIngreso getServicioSeleccionado() {
        return oServicioSeleccionado;
    }

    public void setServicioSeleccionado(ConceptoIngreso oServicioSeleccionado) {
        this.oServicioSeleccionado = oServicioSeleccionado;  
        this.seleccionaServicio();
    }
    
    public boolean getMostrarPersonal(){
        return this.bMostrarPersonal;
    }
    
    public List<PersonalHospitalario> getListPH() {
        return listPH;
    }
}
