package org.apli.jbs.catalogos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import org.apli.modelbeans.MetodoAnticonceptivo;
import org.primefaces.context.RequestContext;

/**
 * Bean administrado para el control de ABC de métodos anticonceptivos
 * @author AliberGA
 */
@ManagedBean(name = "oMetodoAnticonceptivo")
@RequestScoped
public class MetodoAnticonceptivoJB {
private MetodoAnticonceptivo oMetodoAnticonceptivo=new MetodoAnticonceptivo();
private static List<MetodoAnticonceptivo> listaMetodoAnticonceptivo;
private static MetodoAnticonceptivo selectedMetodoAnticonceptivo;
private static MetodoAnticonceptivo currentMetodoAnticonceptivo;
private static boolean bDisDatos;
private static String C;

    public MetodoAnticonceptivoJB() {
        listaMetodoAnticonceptivo=new ArrayList();
        llenaLista(listaMetodoAnticonceptivo);
    }
    
    public void llenaLista(List<MetodoAnticonceptivo> lista){
        try {
            lista.addAll(oMetodoAnticonceptivo.buscaTodos());
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void confMetodoAnticonceptivo(int D){
        if (getSelectedMetodoAnticonceptivo()==null){
            FacesContext context= FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Error","Debe seleccionar un registro"));
            context.getExternalContext().getFlash().setKeepMessages(true);
        }else{
            if(D==2){
                C="A";
                bDisDatos=true;
            }
            else if(D==1){
                C="E";
                 bDisDatos=true;
            }
            setCurrentMetodoAnticonceptivo(getSelectedMetodoAnticonceptivo());
            
            RequestContext.getCurrentInstance().execute("dlgMA.show()");
        }
    }
    
    public void confNuevo(){
        setSelectedMetodoAnticonceptivo(null);
        setCurrentMetodoAnticonceptivo(new MetodoAnticonceptivo());
        bDisDatos=false;
        C="I";
    }
        
    public void guarda()throws Exception{
    String mess="";
    FacesContext context= FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Guardar.",mess));
        context.getExternalContext().getFlash().setKeepMessages(true);
       
        if(C.equals("E")){
            mess=getCurrentMetodoAnticonceptivo().eliminaMetodoAnticonceptivo(getCurrentMetodoAnticonceptivo());
            System.out.println("Elimina");
        }
        else if(C.equals("I")){
             mess=getCurrentMetodoAnticonceptivo().insertaMetodoAnticonceptivo(getCurrentMetodoAnticonceptivo());
             System.out.println("Inserta");
        }
        else if(C.equals("A")){
            mess=getCurrentMetodoAnticonceptivo().modificaMetodoAnticonceptivo(getCurrentMetodoAnticonceptivo());
            System.out.println("Modifica");                  
        }
        if ("El Método Anticonceptivo ha sido eliminado exitosamente".equals(mess)||
            "El Método Anticonceptivo ha sido registrado exitosamente".equals(mess)||
            "El Método Anticonceptivo ha sido modificado exitosamente".equals(mess)){
            listaMetodoAnticonceptivo=new ArrayList();
            llenaLista(listaMetodoAnticonceptivo);
            RequestContext.getCurrentInstance().execute("dlgMA.hide()");
        }   
        context.addMessage(null, new FacesMessage(mess));
     }    
    
    public List<MetodoAnticonceptivo> getListaMetodoAnticonceptivo(){
        return listaMetodoAnticonceptivo;
    }
       

    public boolean isDisDatos() {
        return bDisDatos;
    }

    public void setDisDatos(boolean bDisDatos) {
        MetodoAnticonceptivoJB.bDisDatos = bDisDatos;
    }
     
    
     public MetodoAnticonceptivo getMetodoAnticonceptivo() {
        return oMetodoAnticonceptivo;
    }

    public void setMetodoAnticonceptivo(MetodoAnticonceptivo oMetodoAnticonceptivo) {
        this.oMetodoAnticonceptivo = oMetodoAnticonceptivo;
    }
     public  MetodoAnticonceptivo getSelectedMetodoAnticonceptivo() {
        return selectedMetodoAnticonceptivo;
    }

    public  void setSelectedMetodoAnticonceptivo(MetodoAnticonceptivo aSelectedMetodoAnticonceptivo) {
        selectedMetodoAnticonceptivo = aSelectedMetodoAnticonceptivo;
    }

    public MetodoAnticonceptivo getCurrentMetodoAnticonceptivo() {
        return currentMetodoAnticonceptivo;
    }

    public  void setCurrentMetodoAnticonceptivo(MetodoAnticonceptivo aCurrentMetodoAnticonceptivo) {
        currentMetodoAnticonceptivo = aCurrentMetodoAnticonceptivo;
    }
}
