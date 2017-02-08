package org.apli.jbs.personal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import org.apli.modelbeans.ConceptoEgreso;
import org.apli.modelbeans.PersonalHospitalario;
import org.apli.modelbeans.personal.Prestamo;

/**
 *
 * @author Lily_LnBnd
 */
@ManagedBean(name="oRegPres")
@ViewScoped
public class RegistrarPrestamoJB implements Serializable{
private List<PersonalHospitalario> listPersonal;
private List<PersonalHospitalario> arrPersFiltrado;
private PersonalHospitalario selectedPersonal;
private Prestamo oPrestamo;
private List<SelectItem> arrTipoPres;

    
    public RegistrarPrestamoJB(){
        listPersonal=new ArrayList();
        arrTipoPres = new ArrayList<SelectItem>();
        llenaListas();
    }
    
    private void llenaListas(){
    List<ConceptoEgreso> arrConcep;
        try{
            listPersonal=new PersonalHospitalario().buscaPersonalPrestamo();
            arrConcep = (new ConceptoEgreso()).buscaConceptosPrestamos();
            if (arrConcep != null){
                for (int i = 0; i<arrConcep.size(); i++){
                    arrTipoPres.add(new 
                            SelectItem(arrConcep.get(i).getCveConcep()+"",
                            arrConcep.get(i).getDescripcion()
                            ));
                }
            }            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void confPres(){
        oPrestamo=new Prestamo();
        oPrestamo.setFechaRegistro(new Date());
        oPrestamo.setSituacion('S');
    }
    
    public void registraPrestamo() throws Exception{
        String mess="";
        if (oPrestamo.getSituacion()=='S' || oPrestamo.getMonto()==0.0 ||
            oPrestamo.getConcep().getCveConcepEgr()==-1)
            mess="Error de Validación. Faltan datos";
        else{
            oPrestamo.setPersonal(selectedPersonal);
            mess=new Prestamo().registrarPrestamo(oPrestamo);
        }
        FacesContext context= FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Registrar Prestamo",mess));
        context.getExternalContext().getFlash().setKeepMessages(true);
    }

    public List<PersonalHospitalario> getListPersonal() {
        return listPersonal;
    }

    public void setListPersonal(List<PersonalHospitalario> listPersonal) {
        this.listPersonal = listPersonal;
    }

    public PersonalHospitalario getSelectedPersonal() {
        return selectedPersonal;
    }

    public void setSelectedPersonal(PersonalHospitalario selectedPersonal) {
        this.selectedPersonal = selectedPersonal;
    }

    public Prestamo getPrestamo() {
        return oPrestamo;
    }

    public void setPrestamo(Prestamo oPrestamo) {
        this.oPrestamo = oPrestamo;
    }
    
    public String getEsDeudorExterno(){
    String sVisibleDeuExt = "hidden";
        if (this.selectedPersonal != null &&
            this.selectedPersonal.getNombreCompleto().contains("DEUDOR"))
            sVisibleDeuExt = "visible";
        return sVisibleDeuExt;
    }
            
    public List<PersonalHospitalario> getPersFiltrado() {
        return arrPersFiltrado;
    }
    public void setPersFiltrado(List<PersonalHospitalario> valor) {
        this.arrPersFiltrado = valor;
    }
    
    public List<SelectItem> getTipos(){
        return this.arrTipoPres;
    }
}
