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
import org.apli.modelbeans.ManejoMsjsDB;
import org.apli.modelbeans.contabilidadInterna.AreaFuncionamiento;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Daniel
 */
@ManagedBean(name="oAreaFunc")
@ViewScoped
public class AreaFuncJB implements Serializable {

    public AreaFuncionamiento oAreaFunc=new AreaFuncionamiento();
    public ManejoMsjsDB oMenDB=new ManejoMsjsDB();
    private List<AreaFuncionamiento> listaAreaFunc;
    private AreaFuncionamiento selectedAreaFuncionamiento;
    private AreaFuncionamiento currentAreaFuncionamiento;
    private boolean bDisDatos;
    private boolean bDisDatosDescrip;
    private String sNomButton;
    private int nOpe;

    /**
     * Creates a new instance of AreaFuncJB
     */
    public AreaFuncJB() {
        listaAreaFunc=new ArrayList();
        llenaLista();
    }
    
    private void llenaLista(){
        try {
            listaAreaFunc.addAll(oAreaFunc.todasAreasFuncionamiento());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    
    
    public void confModificar(){
        System.out.println(selectedAreaFuncionamiento.getDescripcion());
        if (getSelectedAreaFuncionamiento()==null){
            FacesContext context= FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Error","Debe seleccionar un registro"));
            context.getExternalContext().getFlash().setKeepMessages(true);
        }else{
            currentAreaFuncionamiento=selectedAreaFuncionamiento;
            bDisDatos=true;
            bDisDatosDescrip=false;
            sNomButton="Guardar";
            nOpe=2;
            RequestContext.getCurrentInstance().execute("dlgAF.show()");
        }
    }
    
    public void confEliminar(){
        if (getSelectedAreaFuncionamiento()==null){
            FacesContext context= FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Error","Debe seleccionar un registro"));           
            context.getExternalContext().getFlash().setKeepMessages(true);
        }else{
            currentAreaFuncionamiento=selectedAreaFuncionamiento;
            bDisDatos=true;
            bDisDatosDescrip=true;
            sNomButton="Eliminar";
            nOpe=3;
            RequestContext.getCurrentInstance().execute("dlgAF.show()");
        }
    }
    
    public void confInsertar(){
        selectedAreaFuncionamiento=null;
        currentAreaFuncionamiento=new AreaFuncionamiento();
        bDisDatos=false;
        bDisDatosDescrip=false;
        sNomButton="Guardar";
        nOpe=1;
    }
    
    public void preGuarda(ActionEvent ae)throws Exception{
        guarda();
    }
    
    public void guarda()throws Exception{
        String mess="";
        Severity nivel;
        if(nOpe==1)
            mess=currentAreaFuncionamiento.guardaAreaFuncionamiento(currentAreaFuncionamiento);
        else{
            if(nOpe==3)
                mess=currentAreaFuncionamiento.eliminaAreaFuncionamiento(currentAreaFuncionamiento);
            else
                mess=currentAreaFuncionamiento.modificaAreaFuncionamiento(currentAreaFuncionamiento);
        }

        if (oMenDB.isValid(mess))
            nivel = FacesMessage.SEVERITY_INFO;   
        else
            nivel = FacesMessage.SEVERITY_ERROR;
        
        FacesContext context= FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(nivel,"Guardar.",oMenDB.manejoMensajes(mess)));
        context.getExternalContext().getFlash().setKeepMessages(true);
        listaAreaFunc.clear();
        llenaLista();
        nOpe = 0;
        selectedAreaFuncionamiento = null;
        currentAreaFuncionamiento = selectedAreaFuncionamiento;
            RequestContext.getCurrentInstance().execute("dlgAF.hide()");
        }
    
    public List<AreaFuncionamiento> getListaAreasFunc(){
        return listaAreaFunc;
    }
    
    public AreaFuncionamiento getSelectedAreaFuncionamiento() {
        return selectedAreaFuncionamiento;
        
    }

    public void setSelectedAreaFuncionamiento(AreaFuncionamiento selectedAreaFunc) {
        this.selectedAreaFuncionamiento = selectedAreaFunc;
        System.out.println("Se invoco!!! SET:  "+selectedAreaFunc.getDescripcion());
    }
    
    
    public AreaFuncionamiento getCurrentAreaFuncionamiento() {
        return currentAreaFuncionamiento;
    }

    public void setCurrentAreaFuncionamiento(AreaFuncionamiento currentAreaFunc) {
        this.currentAreaFuncionamiento = currentAreaFunc;
    }
    
    public boolean isDisDatos() {
        return bDisDatos;
    }


    public boolean isDisDatosDescrip() {
        return bDisDatosDescrip;
    }
    
    public String getNomButton() {
        return sNomButton;
    }

}
