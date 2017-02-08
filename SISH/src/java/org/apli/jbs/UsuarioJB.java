package org.apli.jbs;

import java.io.IOException;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.apli.modelbeans.Usuario;
/**
 * Clase que guarda en una variable de sesion un objeto de tipo Usuario donde 
 * tendrá toda la información de un usuario como de sus funciones.
 * Autor: Jesús Vázquez Ruiz
 * Fecha: Febrero 2014
 */
@ManagedBean(name="oUsuario")
@SessionScoped
public class UsuarioJB implements Serializable {
    
    private Usuario oUsuario=new Usuario();
    public String nombreCompletoUsuario;
    
    
    public UsuarioJB(){
    }
    
    /**
    * Busca un usuario determinado , si es encontrado lo guarda en una variable de sesion y manda un mensaje de 
    * bienvenida, y lo redirecciona a su página de administración, si no, manda un mensaje a la página de 
    * error.
    */
    public void buscaUsuario(){
        try{
            if(oUsuario.buscaUsuario()){
                
                String msj="Bienvenido "+oUsuario.getUsuario(); 
                FacesContext facesContext = FacesContext.getCurrentInstance();
                
                HttpSession session =(HttpSession)facesContext.getExternalContext().
                getSession(false);
                session.setAttribute("usuarioSesion",oUsuario);
                boolean cargaMenu=true;
                session.setAttribute("cargaMenu",cargaMenu);
                
                facesContext.addMessage(null, new FacesMessage(msj,""));
                facesContext.getExternalContext().getFlash().setKeepMessages(true);
                facesContext.getExternalContext().redirect("faces/login/menuOpe.xhtml");
                
            }else{
                String msj=oUsuario.getMsjQuery();
                FacesContext facesContext = FacesContext.getCurrentInstance();
                facesContext.addMessage(null, new FacesMessage(msj,msj));
                facesContext.getExternalContext().getFlash().setKeepMessages(true);
            }
        }catch(Exception e){
            e.printStackTrace();
            String msj="No tiene permisos de ingreso";
            FacesContext facesContext = FacesContext.getCurrentInstance();
            facesContext.addMessage(null, new FacesMessage(msj,msj));
            facesContext.getExternalContext().getFlash().setKeepMessages(true);
        }
        
    }
    
    /**
    * Regresa el objeto Usuario almacenado en una variable de Sesion
    */
    public Usuario getSesionUsuario() throws IOException{
        
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session =(HttpSession)facesContext.getExternalContext().getSession(true);
        
        if (session == null){
            ServletContext oServletContext=(ServletContext)facesContext.getExternalContext().getContext();
                String nombreProyecto=oServletContext.getContextPath()+"/";
                facesContext.addMessage(null, new FacesMessage("La sesión ha caducado",""));
                facesContext.getExternalContext().getFlash().setKeepMessages(true);
                facesContext.getExternalContext().redirect(nombreProyecto.toUpperCase());
        }
        return (Usuario)session.getAttribute("usuarioSesion");
    }

    public Usuario getUsuario() {
        return oUsuario;
    }

    public void setUsuario(Usuario oUsuario) {
        this.oUsuario = oUsuario;
    }
    
    public void salir() throws IOException{
        
                String msj="";
        
                
                    msj="¡Que tenga Buen día!";
                    FacesContext facesContext = FacesContext.getCurrentInstance();
                    HttpSession session =
                    (HttpSession)facesContext.getExternalContext().
                    getSession(false);
                    if (session != null){
                            session.invalidate();
                    }
                    
                ServletContext oServletContext=(ServletContext)facesContext.getExternalContext().getContext();
                String nombreProyecto=oServletContext.getContextPath()+"/";
                
                facesContext.addMessage(null, new FacesMessage(msj,msj));
                facesContext.getExternalContext().getFlash().setKeepMessages(true);
		facesContext.getExternalContext().redirect(nombreProyecto.toUpperCase());
                 
    }

    public boolean getCargaMenu() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session =(HttpSession)facesContext.getExternalContext().getSession(true);
        boolean cargaMenu=false;
        try{
            cargaMenu=(Boolean)session.getAttribute("cargaMenu");
        }catch(Exception e){
            
        }
        return cargaMenu ;
    }

    public void setCargaMenu(boolean cargaMenu) {
        
    }


}
