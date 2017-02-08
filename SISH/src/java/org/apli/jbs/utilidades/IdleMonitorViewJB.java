/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apli.jbs.utilidades;

import java.io.IOException;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.apli.jbs.UsuarioJB;

/**
 *
 * @author JRuiz
 */
@ManagedBean(name="IdleMonitorView")
@RequestScoped
public class IdleMonitorViewJB implements Serializable {
    
    public void onIdle() throws IOException {
       
        
        FacesContext facesContext = FacesContext.getCurrentInstance();
                    HttpSession session =
                    (HttpSession)facesContext.getExternalContext().
                    getSession(false);
                    if (session != null){
                            session.invalidate();
                    }
        ServletContext oServletContext=(ServletContext)facesContext.getExternalContext().getContext();
        String nombreProyecto=oServletContext.getContextPath()+"/";
        facesContext.getExternalContext().redirect(nombreProyecto.toUpperCase());
           
        
    }
 
    
    
}
