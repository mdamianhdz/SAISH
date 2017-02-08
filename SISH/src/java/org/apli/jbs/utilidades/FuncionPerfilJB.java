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
import org.apli.jbs.UsuarioJB;
import org.apli.modelbeans.Funcion;
import org.apli.modelbeans.FuncionPerfil;
import org.apli.modelbeans.Perfil;
import org.apli.modelbeans.Usuario;
import org.primefaces.model.DualListModel;

/**
 *
 * @author Lily_LnBnd
 */
@ManagedBean(name="oFP")
@SessionScoped
public class FuncionPerfilJB implements Serializable{
    private Perfil oPerfil;
    private List<Funcion> source=new ArrayList();
    private List<Funcion> target=new ArrayList();
    private List<FuncionPerfil> listaFP;
    private DualListModel<Funcion> dualList;

    public Perfil getPerfil(){return oPerfil ;}
    public void setPerfil(Perfil value){oPerfil = value;}
    public List<Funcion> getFuncion(){return source;}
    public void setFuncion(List<Funcion> value){source = value;}
    
    public FuncionPerfilJB(){
        inicia();
    }
    public void inicia(){
        listaFP=new ArrayList();
        dualList=new DualListModel<Funcion>(source, target);
        llenaLista(listaFP);
    }
    
    private void llenaLista(List<FuncionPerfil> lista){
        FuncionPerfil oFP=new FuncionPerfil();
        try {
            lista.addAll(Arrays.asList(oFP.buscaTodos()));
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public List<FuncionPerfil> getRelaciones() { 
        return listaFP;
    }
    public DualListModel<Funcion> getFunciones(){
        return dualList;
    }
    
    public void setFunciones(DualListModel<Funcion> dlist){
        this.dualList=dlist;
    }
    public void llenaPick(String perfil) throws Exception{
        source=new ArrayList();
        target=new ArrayList();
        Funcion oF=new Funcion();
        source.addAll(Arrays.asList(oF.buscaSource(perfil)));
        target.addAll(Arrays.asList(oF.buscaTarget(perfil)));
        setFunciones(new DualListModel<Funcion>(source, target));
        dualList=new DualListModel<Funcion>(source, target);
        oPerfil=new Perfil();
        oPerfil.setPerfil(perfil);
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
        String[][] lista=new String[2][getFunciones().getTarget().size()];
        String rst="";
        FuncionPerfil oFP=new FuncionPerfil();
        if (getFunciones().getTarget().isEmpty()){
            rst=oFP.eliminaRelaciones(getPerfil().getPerfil());    
        }else{
            for (int i=0; i<getFunciones().getTarget().size();i++){
                lista[0][i]=getPerfil().getPerfil();
                lista[1][i]=""+getFunciones().getTarget().get(i);
            }
            rst=oFP.actualizaRelaciones(lista);
        }
        FacesContext context= FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(rst,rst));
        context.getExternalContext().getFlash().setKeepMessages(true);
        inicia();
        Usuario usuario=new UsuarioJB().getSesionUsuario();
        usuario.buscaUsuario();
        new MenuJB().actualizaModelMenu();
        context.getExternalContext().redirect("../utilidades/fncPerfil.xhtml?faces-redirect=true");
    }
}