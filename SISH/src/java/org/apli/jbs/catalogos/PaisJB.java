package org.apli.jbs.catalogos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import org.apli.modelbeans.Pais;
import org.primefaces.context.RequestContext;

/**
 * Bean administrado para el control ABC de países
 * @author Gigi
 */
@ManagedBean (name="oPais")
@RequestScoped
public class PaisJB implements Serializable {

    private Pais oPais=new Pais();
    private static List<Pais> listaPais;
    private static Pais selectedPais;
    private static Pais currentPais;
    private static String cveP;
    private static String sdescrip;
    private static boolean bDisDatos;
    private static int estado;
    
    public PaisJB() {
        listaPais=new ArrayList();
        llenaLista(listaPais);
    }
    
    private void llenaLista(List<Pais> lista){
        try{
            lista.addAll(Arrays.asList(oPais.buscarTodos()));
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    public void confModificaPais(){
        if(selectedPais==null){
            FacesContext context=FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Error","Debe seleccionar un registro"));
            context.getExternalContext().getFlash().setKeepMessages(true);
        }else{
            currentPais=selectedPais;
            bDisDatos=false;
            estado=2;
            RequestContext.getCurrentInstance().execute("dlgUsu.show()");
        }
    }
    
    public void confEliminarPais()throws Exception{
        if(selectedPais==null){
            FacesContext context=FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Error","Debe seleccionar un registro"));
            context.getExternalContext().getFlash().setKeepMessages(true);
        }else{
            setListaPais((List<Pais>)new ArrayList());
            llenaLista(getListaPais());
            RequestContext.getCurrentInstance().execute("dlgUsu.show()");
            currentPais=selectedPais;
            bDisDatos=true;
            estado=3;
        }
    }
    
    public void confNuevo(){
        selectedPais=null;
        currentPais=new Pais();
        currentPais.setCve(cveP);
        currentPais.setDescrip(sdescrip);
        bDisDatos=false;
        estado=1;
    }
    
    public void guardar()throws Exception{
        String mess="";
        FacesContext context=FacesContext.getCurrentInstance();
        if(estado==1){
            mess=currentPais.insertar();
            context.addMessage(null, new FacesMessage("Registrar.",mess));
            context.getExternalContext().getFlash().setKeepMessages(true);
        }else if(estado==2){
            mess=currentPais.modificar();
            context.addMessage(null, new FacesMessage("Modificar.",mess));
            context.getExternalContext().getFlash().setKeepMessages(true);
        }else{
            mess=currentPais.eliminar();
            context.addMessage(null, new FacesMessage("Eliminar.",mess));
            context.getExternalContext().getFlash().setKeepMessages(true);
        }
        
        context.getExternalContext().getFlash().setKeepMessages(true);
        if("El país ha sido registrado exitosamente".equals(mess)||
           "El país ha sido modificado exitosamente".equals(mess)||
           "El país ha sido eliminado exitosamente".equals(mess)){
            listaPais=new ArrayList();
            llenaLista(listaPais);
            RequestContext.getCurrentInstance().execute("dlgUsu.show()");
        }
    }

    /**
     * @return the oPais
     */
    public Pais getPais() {
        return oPais;
    }

    /**
     * @param oPais the oPais to set
     */
    public void setPais(Pais oPais) {
        this.oPais = oPais;
    }

    /**
     * @return the cveP
     */
    public String getCveP() {
        return cveP;
    }

    /**
     * @param cveP the cveP to set
     */
    public void setCveP(String cveP) {
        this.cveP = cveP;
    }

    /**
     * @return the sdescrip
     */
    public String getdescrip() {
        return sdescrip;
    }

    /**
     * @param sdescrip the sdescrip to set
     */
    public void setdescrip(String sdescrip) {
        this.sdescrip = sdescrip;
    }
    
    /**
     * @return the listaPais
     */
    public List<Pais> getListaPais() {
        return listaPais;
    }

    /**
     * @param aListaPais the listaPais to set
     */
    public void setListaPais(List<Pais> aListaPais) {
        listaPais = aListaPais;
    }

    /**
     * @return the selectedPais
     */
    public Pais getSelectedPais() {
        return selectedPais;
    }

    /**
     * @param aSelectedPais the selectedPais to set
     */
    public void setSelectedPais(Pais aSelectedPais) {
        selectedPais = aSelectedPais;
    }

    /**
     * @return the currentPais
     */
    public Pais getCurrentPais() {
        return currentPais;
    }

    /**
     * @param aCurrentPais the currentPais to set
     */
    public void setCurrentPais(Pais aCurrentPais) {
        currentPais = aCurrentPais;
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

    /**
     * @return the estado
     */
    public int getEstado() {
        return estado;
    }

    /**
     * @param aEstado the estado to set
     */
    public void setEstado(int aEstado) {
        estado = aEstado;
    }
}
