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
import org.apli.modelbeans.PerfilUsuario;
import org.apli.modelbeans.Perfil;
import org.apli.modelbeans.Usuario;
import org.primefaces.model.DualListModel;

/**
 *
 * @author Lily_LnBnd
 */
@ManagedBean(name="oPU")
@SessionScoped
public class PerfilUsuarioJB implements Serializable{
    private Usuario oUsu;
    private List<Perfil> source=new ArrayList();
    private List<Perfil> target=new ArrayList();
    private List<PerfilUsuario> listaPU;
    private DualListModel<Perfil> dualList;

    public Usuario getUsuario(){return oUsu ;}
    public void setUsuario(Usuario value){oUsu = value;}
    public List<Perfil> getPerfil(){return source;}
    public void setPerfil(List<Perfil> value){source = value;}
    
    public PerfilUsuarioJB(){
        inicia();
    }
    private void inicia(){
        listaPU=new ArrayList();
        dualList=new DualListModel<Perfil>(source, target);
        llenaLista(listaPU);
    }
    
    private void llenaLista(List<PerfilUsuario> lista){
        PerfilUsuario oPU=new PerfilUsuario();
        try {
            lista.addAll(Arrays.asList(oPU.buscaTodos()));
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public List<PerfilUsuario> getRelaciones() { 
        return listaPU;
    }
    public DualListModel<Perfil> getPerfiles(){
        return dualList;
    }
    
    public void setPerfiles(DualListModel<Perfil> dlist){
        this.dualList=dlist;
    }
    public void llenaPick(String usuario) throws Exception{
        source=new ArrayList();
        target=new ArrayList();
        Perfil oP=new Perfil();
        source.addAll(Arrays.asList(oP.buscaSource(usuario)));
        target.addAll(Arrays.asList(oP.buscaTarget(usuario)));
        setPerfiles(new DualListModel<Perfil>(source, target));
        dualList=new DualListModel<Perfil>(source, target);
        oUsu=new Usuario();
        oUsu.setUsuario(usuario);
    }
    
    public boolean  renderCommand(int id){
        if (id==0)
            return true;
        else
            return false;
    }
    
    public boolean  renderCol(String funcion){
        if (funcion.equals("vacio"))
            return false;
        else
            return true;
    }
    
    public void actualizaRel() throws Exception{
        String[][] lista=new String[2][getPerfiles().getTarget().size()];
        String rst="";
        PerfilUsuario oPU=new PerfilUsuario();
        if (getPerfiles().getTarget().isEmpty()){
            rst=oPU.eliminaRelaciones(getUsuario().getUsuario());    
        }else{
            for (int i=0; i<getPerfiles().getTarget().size();i++){
                lista[0][i]=getUsuario().getUsuario();
                lista[1][i]=""+getPerfiles().getTarget().get(i);
            }
            rst=oPU.actualizaRelaciones(lista);
        }
        FacesContext context= FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(rst,rst));
        context.getExternalContext().getFlash().setKeepMessages(true);
        inicia();
        context.getExternalContext().redirect("perfilUsu.xhtml");
    }
}