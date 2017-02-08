/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apli.jbs.utilidades;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.apli.modelbeans.Perfil;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author Lily_LnBnd
 */
@ManagedBean(name="oPerfil")
@SessionScoped

public class PerfilJB  implements Serializable{
    
    private String sPerfil;
    private String sDescripcion;
    private List<Perfil> listaP;
    
    public String getPerfil(){return sPerfil;}
    public void setPerfil(String value){sPerfil = value;}
    public String getDescripcion(){return sDescripcion;}
    public void setDescripcion(String value){sDescripcion = value;}
    
    public PerfilJB(){
        listaP=new ArrayList();
        llenaLista(listaP);
    }
    
    private void llenaLista(List<Perfil> lista){
        Perfil oP=new Perfil();
        try {
            lista.addAll(Arrays.asList(oP.buscaTodos()));
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public List<Perfil> getPerfiles() { 
        return listaP;
    }
    
    public void limpia(){
        System.out.println("limpia()");
        listaP=new ArrayList();
        setPerfil("");
        setDescripcion("");
    }
    
    public void inserta() throws Exception{
        Perfil oP=new Perfil();
        oP.setPerfil(sPerfil);
        oP.setDescripcion(sDescripcion);
        String rst=oP.opPerfil("insert");
        FacesContext context= FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(rst,rst));
        context.getExternalContext().getFlash().setKeepMessages(true);
        context.getExternalContext().redirect("perfilesMenu.xhtml");
        listaP=new ArrayList();
        llenaLista(listaP);
        
        
    }
    
    public void onEdit(RowEditEvent event)throws Exception{
        String rst=((Perfil) event.getObject()).opPerfil("update");
        FacesContext context= FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(rst,rst));
        context.getExternalContext().getFlash().setKeepMessages(true);
        context.getExternalContext().redirect("perfilesMenu.xhtml");
        listaP=new ArrayList();
        llenaLista(listaP);
    }
    
}
