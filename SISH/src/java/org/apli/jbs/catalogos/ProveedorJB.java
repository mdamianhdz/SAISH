package org.apli.jbs.catalogos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apli.modelbeans.contabilidadInterna.Proveedor;
import org.primefaces.context.RequestContext;

/**
 * Bean administrado para el control ABC de proveedores
 * @author AliberGA
 */
@ManagedBean(name="oProveedor")
@ViewScoped
public final class ProveedorJB implements Serializable{
private Proveedor oProveedor=new Proveedor();
private List<Proveedor> listaProv;
private Proveedor selectedProveedor;
private Proveedor currentProveedor;
private boolean bDisDatos;
private String C;
private boolean bDisDatosDescrip;

    public ProveedorJB() {
        listaProv=new ArrayList();
        llenaLista(listaProv);
    }
    
    public void llenaLista(List<Proveedor> lista){
        try {
            lista.addAll(oProveedor.buscaProveedores());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void confProveedor(int D){
        if (getSelectedProveedor()==null){
            FacesContext context= FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Error","Debe seleccionar un registro"));
            context.getExternalContext().getFlash().setKeepMessages(true);
        }else{
            if(D==2){
                C="A";
                bDisDatos=true;
                bDisDatosDescrip=false;
            }
            else if(D==1){
                C="E";
                bDisDatos=true;
                bDisDatosDescrip=true;
            }
            setCurrentProveedor(getSelectedProveedor());            
            RequestContext.getCurrentInstance().execute("dlgProv.show()");        
        }    
    }
    
    public void confNuevo(){
        setSelectedProveedor(null);
        setCurrentProveedor(new Proveedor());
        bDisDatos=false;
        bDisDatosDescrip=false;
        C="I";
    }    
    
    public void guarda()throws Exception{
    String mess="";
        FacesContext context= FacesContext.getCurrentInstance();
       
        if(C.equals("E")){
            mess=getCurrentProveedor().eliminaProveedor();
        }
        else if(C.equals("I")){
             mess=getCurrentProveedor().insertaProveedor();
        }
        else if(C.equals("A")){
            mess=getCurrentProveedor().modificaProveedor();
        }
        if ("El proveedor ha sido eliminado exitosamente".equals(mess)||
            "El proveedor ha sido registrado exitosamente".equals(mess)||
            "El proveedor ha sido modificado exitosamente".equals(mess)){
            listaProv=new ArrayList();
            llenaLista(listaProv);
            RequestContext.getCurrentInstance().execute("dlgProv.hide()");
        }   
        context.addMessage(null, new FacesMessage("Guardar",mess));
    }  
    
    public List<Proveedor> getListaProveedor(){
        return listaProv;
    }
    
    public boolean isDisDatos() {
        return bDisDatos;
    }

    public void setDisDatos(boolean value) {
        bDisDatos = value;
    }
    
     public Proveedor getProveedor() {
        return oProveedor;
    }

    public void setProveedor(Proveedor oProveedor) {
        this.oProveedor = oProveedor;
    }
     public  Proveedor getSelectedProveedor() {
        return selectedProveedor;
    }

    public  void setSelectedProveedor(Proveedor aSelectedProveedor) {
        selectedProveedor = aSelectedProveedor;
    }

    public Proveedor getCurrentProveedor() {
        return currentProveedor;
    }

    public  void setCurrentProveedor(Proveedor aCurrentProveedor) {
        currentProveedor = aCurrentProveedor;
    }    

    public boolean isDisDatosDescrip() {
        return bDisDatosDescrip;
    }
}
