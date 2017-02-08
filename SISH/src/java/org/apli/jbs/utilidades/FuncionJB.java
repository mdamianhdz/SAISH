package org.apli.jbs.utilidades;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apli.jbs.UsuarioJB;
import org.apli.modelbeans.Funcion;
import org.apli.modelbeans.Menu;
import org.apli.modelbeans.Usuario;
import org.primefaces.event.RowEditEvent;

@ManagedBean(name="oFnc")
@ViewScoped
public class FuncionJB implements Serializable{
    
    private String sFuncion;
    private String sDescripcion;
    private String sUrl;
    private String sPadre;
    private Menu oPadre;
    private List<Funcion> listaFnc;
    private List<Menu> listaMenu;
    private List<Funcion> listaFiltrada;
    
    public String getFuncion(){return sFuncion;}
    public void setFuncion(String value){sFuncion = value;}
    public String getDescripcion(){return sDescripcion;}
    public void setDescripcion(String value){sDescripcion = value;}
    public String getUrl(){return sUrl;}
    public void setUrl(String value){sUrl = value;}
    public Menu getPadre(){return oPadre;}
    public void setPadre(Menu value){oPadre = value;}
    public String getNomPadre(){return sPadre;}
    public void setNomPadre(String value) throws Exception{
        this.sPadre = value;
        this.setPadre(new Menu().buscaMenu(value));
    }
    

    public FuncionJB(){
        listaFnc=new ArrayList();
        llenaLista(listaFnc);
    }
    
    private void llenaLista(List<Funcion> lista){
        Funcion oF=new Funcion();
        try {
            lista.addAll(Arrays.asList(oF.buscaTodos()));
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public List<Funcion> getFunciones() { 
        return listaFnc;
    }
    
    public List<Menu> getMenus() throws Exception { 
        listaMenu=new ArrayList<Menu>();
        Menu oM=new Menu();
        try {
            listaMenu=oM.buscaTodos();
        }catch (Exception ex) {
            ex.printStackTrace();
        }
        return listaMenu;
    }
    
    public void limpia(){
        System.out.println("limpia()");
        listaMenu=new ArrayList();
        setFuncion("");
        setDescripcion("");
        setUrl("");
        setPadre(null);
    }
    public void inserta() throws Exception{
        Funcion oF=new Funcion();
        oF.setFuncion(sFuncion);
        oF.setDescripcion(sDescripcion);
        oF.setUrl(sUrl);
        oF.setPadre(oPadre);
        String rst=oF.opFuncion("insert");
        FacesContext context= FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(rst,rst));
        context.getExternalContext().getFlash().setKeepMessages(true);
        context.getExternalContext().redirect("../utilidades/funcionesMenu.xhtml");
        listaFnc=new ArrayList();
        llenaLista(listaFnc);
        
    }
    
    public void onEdit(RowEditEvent event)throws Exception{
        String rst=((Funcion) event.getObject()).opFuncion("update");
        FacesContext context= FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(rst,rst));
        context.getExternalContext().getFlash().setKeepMessages(true);
        context.getExternalContext().redirect("../utilidades/funcionesMenu.xhtml");
        listaFnc=new ArrayList();
        llenaLista(listaFnc);        
    }
    /**
    * valida si la página en donde un usuario ingresó tiene autorización, si no lo tiene lo redireccioa a 
    * iniciar sesion.
    */  
    public String validaPermisoPagina(String funcion) throws IOException{
       if(new UsuarioJB().getSesionUsuario()==null){
            FacesContext context= FacesContext.getCurrentInstance();
            context.getExternalContext().getFlash().setKeepMessages(true);
            context.getExternalContext().redirect("index.xhtml");
        }else {
            
            boolean valido=false;
            Usuario usuario=new UsuarioJB().getSesionUsuario();
            for(int i=0;i<usuario.getListMenu().size();i++){
                if(funcion.equals(((Usuario)usuario.getListMenu().get(i)).getUrl())){
                    valido=true;
                }
            }
            if(!valido){
                FacesContext context= FacesContext.getCurrentInstance();
                context.getExternalContext().getFlash().setKeepMessages(true);
                context.getExternalContext().redirect("index.xhtml");
            }
            
        }
        return "";
    }   
    
    public List<Funcion> getFiltradas(){
        return this.listaFiltrada;
    }
    public void setFiltradas(List<Funcion> val){
        this.listaFiltrada = val;
    }
}