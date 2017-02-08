/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apli.jbs.utilidades;

/**
 *
 * @author ERJI
 */
import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import javax.faces.context.FacesContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
 
@ManagedBean(name="oGuest")
@SessionScoped
public class GuestPreferences implements Serializable {

        private String theme = ""; 

        public GuestPreferences(){
            
        }
        
        public String getTheme() {               
            Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
            if(params.containsKey("theme")) {
                theme = params.get("theme");
            }
            return theme;
        }

        public void setTheme(String t) {
                this.theme = t;
        }
        
        public void guarda() {
        
            System.out.println("guarda():"+theme);
        
        }
        
        public void cancelar() throws IOException {
            theme = "";
            FacesContext context= FacesContext.getCurrentInstance();
            context.getExternalContext().getFlash().setKeepMessages(true);
            context.getExternalContext().redirect("admPlantilla.xhtml");        
        }        
}
