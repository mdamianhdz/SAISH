package org.apli.jbs.catalogos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import org.apli.modelbeans.UnidadMedida;
import org.primefaces.context.RequestContext;

/**
 * Bean Administrado para el control de ABC de unidades de medida
 * @author Gigi
 */
@ManagedBean(name="oUnidad")
@RequestScoped
public class UnidadMedidaJB implements Serializable {

    private UnidadMedida oUnidad=new UnidadMedida();
    private static List<UnidadMedida> listaUnidad;
    private static UnidadMedida selectedUnidad;
    private static UnidadMedida currentUnidad;
    private static boolean bDisDatos;
    private String clave;
    private String descrip;
    private static int estado;

    /**
     * Creates a new instance of UnidadMedidaJB
     */
    public UnidadMedidaJB() {
        listaUnidad=new ArrayList();
        llenaLista(listaUnidad);
    }
    
    private void llenaLista(List<UnidadMedida> lista){
        try{
            lista.addAll(oUnidad.buscatodasunidades());
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    public void confUnidad(){
        if(selectedUnidad==null){
            FacesContext context=FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Error","Debe seleccionar un registro"));
            context.getExternalContext().getFlash().setKeepMessages(true);
        }else{
            currentUnidad=selectedUnidad;
            bDisDatos=false;
            RequestContext.getCurrentInstance().execute("dlgUsu.show()");
            estado=2;
        }
    }
    
    public void confUnidadDelete() throws Exception{
        FacesContext context=FacesContext.getCurrentInstance();
        if(selectedUnidad==null){
            context.addMessage(null, new FacesMessage("Error","Debe seleccionar un registro"));
            context.getExternalContext().getFlash().setKeepMessages(true);
        }else{
            setListaUnidad((List<UnidadMedida>)new ArrayList());
            llenaLista(getListaUnidad());
            RequestContext.getCurrentInstance().execute("dlgUsu.show()");
            currentUnidad=selectedUnidad;
            bDisDatos=true;
            estado=3;
        }
    }
    
    public void confNueva(){
        selectedUnidad=null;
        currentUnidad=new UnidadMedida();
        currentUnidad.setCve(clave);
        currentUnidad.setDescrip(descrip);
        bDisDatos=false;
        estado=1;
    }
    
    public void guardar() throws Exception{
        String mess="";
        FacesContext context=FacesContext.getCurrentInstance();
        if(estado==1){
            mess=currentUnidad.insertar();
            context.addMessage(null, new FacesMessage("Registrar.",mess));
            context.getExternalContext().getFlash().setKeepMessages(true);
        }else if(estado==2){
            mess=currentUnidad.modificar();
            context.addMessage(null, new FacesMessage("Modificar.",mess));
            context.getExternalContext().getFlash().setKeepMessages(true);
        }else{
            mess=currentUnidad.eliminar();
            context.addMessage(null, new FacesMessage("Eliminar.",mess));
            context.getExternalContext().getFlash().setKeepMessages(true);
        }
        
        context.getExternalContext().getFlash().setKeepMessages(true);
        if("La unidad de medida ha sido registrada exitosamente".equals(mess)||
           "La unidad de medida ha sido modificada exitosamente".equals(mess)||
           "La unidad de medida ha sido eliminada exitosamente".equals(mess)){
            listaUnidad=new ArrayList();
            llenaLista(listaUnidad);
            RequestContext.getCurrentInstance().execute("dlgUsu.show");
        }
    }
    
    /**
     * @return the listaUnidad
     */
    public List<UnidadMedida> getListaUnidad() {
        return listaUnidad;
    }

    /**
     * @param aListaUnidad the listaUnidad to set
     */
    public void setListaUnidad(List<UnidadMedida> aListaUnidad) {
        listaUnidad = aListaUnidad;
    }

    /**
     * @return the oUnidad
     */
    public UnidadMedida getUnidad() {
        return oUnidad;
    }

    /**
     * @param oUnidad the oUnidad to set
     */
    public void setUnidad(UnidadMedida oUnidad) {
        this.oUnidad = oUnidad;
    }
    
    /**
     * @return the selectedUnidad
     */
    public UnidadMedida getSelectedUnidad() {
        return selectedUnidad;
    }

    /**
     * @param aSelectedUnidad the selectedUnidad to set
     */
    public void setSelectedUnidad(UnidadMedida aSelectedUnidad) {
        selectedUnidad = aSelectedUnidad;
    }

    /**
     * @return the currentMedida
     */
    public UnidadMedida getCurrentUnidad() {
        return currentUnidad;
    }

    /**
     * @param aCurrentMedida the currentMedida to set
     */
    public void setCurrentUnidad(UnidadMedida aCurrentUnidad) {
        currentUnidad = aCurrentUnidad;
    }
    
    /**
     * @return the bDisDatos
     */
    public boolean isDisDatos() {
        return bDisDatos;
    }

    /**
     * @param abDisDatos the bDisDatos to set
     */
    public void setDisDatos(boolean abDisDatos) {
        bDisDatos = abDisDatos;
    }
}
