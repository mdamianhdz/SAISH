package org.apli.jbs.utilidades;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apli.modelbeans.PersonalHospitalario;
import org.apli.modelbeans.Usuario;
import org.primefaces.context.RequestContext;
/**
 * Clase para el control de edición de usuarios.
 * Autor: Jesús Vázquez Ruiz
 * Fecha: Febrero 2014
 */
@ManagedBean(name="oUsuABC")
@ViewScoped
public class UsuABCJB implements Serializable{
    private Usuario currentUsuario;
    private List<PersonalHospitalario> listaPersonal;
    private List<Usuario> listaUsu;
    private Usuario oUsuario=new Usuario();
    private Usuario selectedUsuario;
    private boolean bDisDatos;
    private int nOpe = 0;
    
    public UsuABCJB(){
        listaUsu=new ArrayList();
        llenaLista(listaUsu);
    }
    private void llenaLista(List<Usuario> lista){
        try {
            lista.addAll(Arrays.asList(oUsuario.buscaTodos()));
            listaPersonal=new PersonalHospitalario().buscaPersonalInterno();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void confUsuario(){
        if (selectedUsuario==null){
            FacesContext context= FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Error","Debe seleccionar un registro"));
            context.getExternalContext().getFlash().setKeepMessages(true);
        }else{
            currentUsuario=selectedUsuario;
            bDisDatos=true;
            RequestContext.getCurrentInstance().execute("dlgUsu.show()");
            nOpe = 2;
        }
    }
    
    public void confNuevo(){
        selectedUsuario=null;
        currentUsuario=new Usuario();
        currentUsuario.setPH(new PersonalHospitalario());
        bDisDatos=false;
        RequestContext.getCurrentInstance().execute("dlgUsu.show()");
        nOpe = 1;
    }
    
    public void buscaPersonal()throws Exception {
        currentUsuario.setPH(new PersonalHospitalario().buscaPersonalInterno(currentUsuario.getPH().getFolioPers()));
        if (currentUsuario.getPH()==null){
            currentUsuario.setPH(new PersonalHospitalario());
            FacesContext context= FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Error.","No se encuentra registrado el personal con el folio especificado"));
            context.getExternalContext().getFlash().setKeepMessages(true);
        }
    }
    
    public List<PersonalHospitalario> completePersonal(String val)throws Exception{
        List<PersonalHospitalario> personalFiltrado=new ArrayList();
        for (int i=0; i<listaPersonal.size(); i++){
            if (listaPersonal.get(i).getNombreCompleto().toUpperCase().contains(val.toUpperCase()))
                personalFiltrado.add(listaPersonal.get(i));
        }
        return personalFiltrado;
    }
    
    public void guarda(String op)throws Exception{
        String mess="";
        if (nOpe ==1)
            mess=currentUsuario.guardaUsuario(currentUsuario);
        else
            mess=currentUsuario.modificaUsuario(currentUsuario);
        FacesContext context= FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Guardar.",mess));
        if ("Se ha registrado el usuario exitosamente".equals(mess)||"Se ha modificado el usuario exitosamente".equals(mess)){
            listaUsu=new ArrayList();
            llenaLista(listaUsu);
            RequestContext.getCurrentInstance().execute("dlgUsu.hide()");
        }
    }
    
    public List<Usuario> getListaUsuarios(){
        return listaUsu;
    }
    
    public Usuario getSelectedUsuario() {
        return selectedUsuario;
    }

    public void setSelectedUsuario(Usuario value) {
        selectedUsuario = value;
    }
    
    public boolean isDisDatos() {
        return bDisDatos;
    }

    public void setDisDatos(boolean value) {
        bDisDatos = value;
    }

    public List<PersonalHospitalario> getListaPersonal() {
        return listaPersonal;
    }

    public void setListaPersonal(List<PersonalHospitalario> value) {
        listaPersonal = value;
    }

    public Usuario getCurrentUsuario() {
        return currentUsuario;
    }

    public void setCurrentUsuario(Usuario value) {
        currentUsuario = value;
    }
}
