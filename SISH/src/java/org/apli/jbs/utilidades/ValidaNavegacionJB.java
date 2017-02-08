/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apli.jbs.utilidades;

import java.io.IOException;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apli.jbs.UsuarioJB;
import org.apli.modelbeans.Usuario;
import org.primefaces.context.RequestContext;

/**
 *
 * @author JRuiz
 */
@ManagedBean(name = "oValNav")
@RequestScoped
public class ValidaNavegacionJB implements Serializable {

    /**
     * valida si la página en donde un usuario ingresó tiene autorización, si no
     * lo tiene lo redireccioa a iniciar sesion.
     */
   public String validaPermisoPagina() throws IOException {
        if (new UsuarioJB().getSesionUsuario() == null) {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ServletContext oServletContext = (ServletContext) facesContext.getExternalContext().getContext();
            String nombreProyecto = oServletContext.getContextPath() + "/";
            facesContext.getExternalContext().redirect(nombreProyecto.toUpperCase());
        } else {
            boolean valido = false;
            Usuario usuario = new UsuarioJB().getSesionUsuario();
            String sPagina = FacesContext.getCurrentInstance().getExternalContext().getRequestPathInfo();
            sPagina = sPagina.replaceAll(".xhtml", "");
            if (!sPagina.equals("/login/menuOpe")) {
                for (int i = 0; i < usuario.getListMenu().size(); i++) {
                    if (sPagina.equals(((Usuario) usuario.getListMenu().get(i)).getUrl())) {
                        valido = true;
                    }
                }
            } else {
                valido = true;
            }
            if (!valido) {
                FacesContext facesContext = FacesContext.getCurrentInstance();
                HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
                String paginaValida = (String) session.getAttribute("paginaValida");
                System.out.println("paginaValida"+paginaValida);
                System.out.println("sPagina"+sPagina);
                if (!sPagina.equals(paginaValida)) {
                    ServletContext oServletContext = (ServletContext) facesContext.getExternalContext().getContext();
                    String nombreProyecto = oServletContext.getContextPath() + "/";
                    facesContext.getExternalContext().redirect(nombreProyecto.toUpperCase());
                }
            }
        }
        return "";
    }

    public String validaSesion() throws IOException {
        if (new UsuarioJB().getSesionUsuario() == null) {

            FacesMessage msj2 = new FacesMessage("¡Aviso!", "Por seguridad la sesion finalizó, vuelva a iniciar sesion");
            RequestContext.getCurrentInstance().showMessageInDialog(msj2);
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ServletContext oServletContext = (ServletContext) facesContext.getExternalContext().getContext();
            String nombreProyecto = oServletContext.getContextPath() + "/";
            facesContext.getExternalContext().redirect(nombreProyecto.toUpperCase());
            System.out.println("¡Aviso! Por seguridad la sesion finalizó, vuelva a iniciar sesion");
        }
        return "";
    }

    public String obtenerPath() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ServletContext oServletContext = (ServletContext) facesContext.getExternalContext().getContext();
        String nombreProyecto = oServletContext.getContextPath() + "/";
        return nombreProyecto;
    }
}
