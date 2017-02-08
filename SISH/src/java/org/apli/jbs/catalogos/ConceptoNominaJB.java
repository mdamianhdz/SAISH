package org.apli.jbs.catalogos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import org.apli.modelbeans.ConceptoNomina;

/**
 *
 * @author eduardo
 */
@ManagedBean (name="oConceptoNomina")
@RequestScoped
public class ConceptoNominaJB implements Serializable {
    public ConceptoNomina oConceptoNomina=new ConceptoNomina();
    private static List<ConceptoNomina> conceptoNomina;
    private static ConceptoNomina selectedNomina;
    private static ConceptoNomina currentNomina;
    private static boolean bDisDatos;
    public static char cTipoConNom;
    public static String sDescripcion;
    public static int nCveConNom;
    private static int estado=0;
    

    /**
     * Creates a new instance of ConceptoNominaJB
     */
    public ConceptoNominaJB() {
        conceptoNomina=new ArrayList();
        llenarLista(conceptoNomina);
    }
    
    public void llenarLista(List<ConceptoNomina> lista){
        try{
            lista.addAll(oConceptoNomina.buscaPercepciones());
            lista.addAll(oConceptoNomina.buscaDeducciones());
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    public void agregarConceptoNomina(){
        selectedNomina=null;
        currentNomina=new ConceptoNomina();
        
        currentNomina.setTipoConNom(cTipoConNom);
        currentNomina.setDescripcion(sDescripcion);
        bDisDatos=false;
        estado=1;
    }
    
    public void modificarConceptoNomina(){
        if(selectedNomina==null){
            FacesContext context= FacesContext.getCurrentInstance();
            context.addMessage(null,new FacesMessage("Error","Debe seleccionar un registro"));
            context.getExternalContext().getFlash().setKeepMessages(true);
        }
        else{
            currentNomina=selectedNomina;
            bDisDatos=false;
            estado=2;
            RequestContext.getCurrentInstance().execute("dlgUsu.show()");
        }
    }
    
    public void eliminarConceptoNomina(){
        if(selectedNomina==null){
            FacesContext context=FacesContext.getCurrentInstance();
            context.addMessage(null,new FacesMessage("Error","Debe seleccionar un registro"));
            context.getExternalContext().getFlash().setKeepMessages(true);
        }
        else{
            setConceptoNomina((List<ConceptoNomina>) new ArrayList());
            llenarLista(getConceptoNomina());
            RequestContext.getCurrentInstance().execute("dlgUsu.show()");
            currentNomina=selectedNomina;
            bDisDatos=true;
            estado=3;
        }
    }
    
    public void guardar()throws Exception{
        String mess="";
        FacesContext context=FacesContext.getCurrentInstance();
        
        if(estado==1){
            mess=currentNomina.agregarConceptoNomina(currentNomina);
            context.addMessage(null,new FacesMessage("Registrar.",mess));
            context.getExternalContext().getFlash().setKeepMessages(true);
        }
        else{
            if(estado==2){
                mess=currentNomina.modificarConceptoNomina(currentNomina);
                context.addMessage(null,new FacesMessage("Modificar.",mess));
                context.getExternalContext().getFlash().setKeepMessages(true);
            }
            else{
                mess=currentNomina.eliminarConceptoNomina(currentNomina);
                context.addMessage(null,new FacesMessage("Eliminar.",mess));
                context.getExternalContext().getFlash().setKeepMessages(true);
            }
        }
        
        context.getExternalContext().getFlash().setKeepMessages(true);
        if("El Concepto de Nómina ha sido registrado exitosamente".equals(mess)||
           "El Concepto de Nómina ha sido modificado exitosamente".equals(mess)||
           "El Concepto de Nómina ha sido eliminado exitosamente".equals(mess)){
            conceptoNomina=new ArrayList();
            llenarLista(conceptoNomina);
            RequestContext.getCurrentInstance().execute("dlgUsu.show()");
        }
    }
    
    public ConceptoNomina getoConceptoNomina(){
        return oConceptoNomina;
    }
    
    public void setoConceptoNomina(ConceptoNomina oConceptoNomina){
        this.oConceptoNomina=oConceptoNomina;
    }
    
    public List<ConceptoNomina> getConceptoNomina(){
        return conceptoNomina;
    }
    
    public void setConceptoNomina(List<ConceptoNomina> conceptoNomina){
        this.conceptoNomina=conceptoNomina;
    }
    
    public ConceptoNomina getSelectedNomina(){
        return selectedNomina;
    }
    
    public void setSelectedNomina(ConceptoNomina selectedNomina){
        this.selectedNomina=selectedNomina;
    }
    
    public ConceptoNomina getCurrentNomina(){
        return currentNomina;
    }
    
    public void setCurrentNomina(ConceptoNomina currentNomina){
        this.currentNomina=currentNomina;
    }
    
    public boolean getDisDatos() {
        return bDisDatos;
    }

    public void setDisDatos(boolean bDisDatos) {
        this.bDisDatos = bDisDatos;
    }
    
    public char getcTipoConNom(){
        return cTipoConNom;
    }
    
    public void setcTipoConNom(char cTipoConNom){
        this.cTipoConNom=cTipoConNom;
    }
    
    public int getnCveConNom(){
        return nCveConNom;
    }
    
    public void setnCveConNom(int nCveConNom){
        this.nCveConNom=nCveConNom;
    }
    
    public String getsDescripcion(){
        return sDescripcion;
    }
    
    public void setsDescripcion(String sDescripcion){
        this.sDescripcion=sDescripcion;
    }
    
}
