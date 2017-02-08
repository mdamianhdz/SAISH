package org.apli.jbs.catalogos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.apli.modelbeans.EstadoCivil;
import org.apli.modelbeans.ManejoMsjsDB;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Daniel
 */
@ManagedBean(name="oEdoCivil")
@ViewScoped
public class EdoCivilJB implements Serializable{
    public EstadoCivil oEdoCivil=new EstadoCivil();
    public ManejoMsjsDB oMenDB=new ManejoMsjsDB();
    private List<EstadoCivil> listaEdoCivil;
    private EstadoCivil selectedEdoCivil;
    private EstadoCivil currentEdoCivil;
    private int nOpe;
    private String sNomButton;
    private boolean bDisDatos;
    private boolean bDisDescrip;


    /**
     * Creates a new instance of EdoCivilJB
     */
    public EdoCivilJB() {
        listaEdoCivil=new ArrayList();
        llenaLista();
    }
    
    public final void llenaLista(){
        try {
            listaEdoCivil.addAll(oEdoCivil.buscaTodos());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void confModificar(){
        if (getSelectedEstadoCivil()==null){
            FacesContext context= FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Error","Debe seleccionar un registro"));
            context.getExternalContext().getFlash().setKeepMessages(true);
        }else{
            currentEdoCivil=selectedEdoCivil;
            sNomButton="Guardar";
            nOpe=2;
            bDisDatos=true;
            bDisDescrip=false;
            RequestContext.getCurrentInstance().execute("dlgEdoCiv.show()");
        }
    }
    
    public void confEliminar(){
        if (getSelectedEstadoCivil()==null){
            FacesContext context= FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Error","Debe seleccionar un registro"));
            context.getExternalContext().getFlash().setKeepMessages(true);
        }else{
            currentEdoCivil=selectedEdoCivil;
            sNomButton="Eliminar";
            nOpe=3;
            bDisDatos=true;
            bDisDescrip=true;
            RequestContext.getCurrentInstance().execute("dlgEdoCiv.show()");
        }
    }
    
    public void confInsertar(){
        selectedEdoCivil=null;
        currentEdoCivil=new EstadoCivil();
        sNomButton="Guardar";
        nOpe=1;
        bDisDatos=false;
        bDisDescrip=false;
    }
    public void preGuarda(ActionEvent ae)throws Exception{
        guarda();
    }
     public void guarda()throws Exception{
        String mess="";
        Severity nivel;
        if(nOpe==1)
            mess=currentEdoCivil.guardaEstadoCivil(currentEdoCivil);
        else{
            if(nOpe==3)
                mess=currentEdoCivil.eliminaEstadoCivil(currentEdoCivil);
            else
                mess=currentEdoCivil.modificaEstadoCivil(currentEdoCivil);
        }
        if (oMenDB.isValid(mess))
            nivel = FacesMessage.SEVERITY_INFO;

        else
            nivel = FacesMessage.SEVERITY_ERROR;
        
        FacesContext context= FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(nivel,"Guardar.",oMenDB.manejoMensajes(mess)));
        context.getExternalContext().getFlash().setKeepMessages(true);
            listaEdoCivil.clear();
            llenaLista();
            nOpe = 0;
            selectedEdoCivil = null;
            currentEdoCivil = selectedEdoCivil;
            RequestContext.getCurrentInstance().execute("dlgEdoCiv.hide()");
        
        }
     
   
     public List<EstadoCivil> getListaEdoCivil(){
        return listaEdoCivil;
    }
     
    public EstadoCivil getSelectedEstadoCivil() {
        return selectedEdoCivil;
    }

    public void setSelectedEstadoCivil(EstadoCivil selectedEstadoCivil) {
        this.selectedEdoCivil = selectedEstadoCivil;
    }
    
       public EstadoCivil getCurrentAreaFuncionamiento() {
        return currentEdoCivil;
    }

    public void setCurrentAreaFuncionamiento(EstadoCivil currentEC) {
        this.currentEdoCivil = currentEC;
    }
    
    public boolean isDisDatos() {
        return bDisDatos;
    }
    
    public boolean isDisDescrip() {
        return bDisDescrip;
    }
    
    public String getNomButton() {
        return sNomButton;
    }
    
}
