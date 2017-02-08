package org.apli.jbs.promocion;

import java.io.Serializable;
import java.util.Date;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.apli.jbs.UsuarioJB;
import org.apli.modelbeans.Usuario;
import org.apli.modelbeans.BitacoraInfoPaq;

/**
 *
 * @author Lily_LnBnd
 */
@ManagedBean(name="oCaptQx")
@RequestScoped
public class CaptacionQxJB implements Serializable{
    private BitacoraInfoPaq oBit;
    
    public CaptacionQxJB(){
        oBit=new BitacoraInfoPaq();
    }

    public BitacoraInfoPaq getBit() {
        return oBit;
    }

    public void setBit(BitacoraInfoPaq oBit) {
        this.oBit = oBit;
    }  
    
    public void guardaCaptacion()throws Exception{
        String mess="";
        FacesContext context = FacesContext.getCurrentInstance();
        if (oBit==null||oBit.getNombre().equals("")||oBit.getApellidoMaterno().equals("")||oBit.getApellidoMaterno().equals("")||oBit.getGenero()=='S'||
                oBit.getEdad()==0||oBit.getCirugia().equals("") || oBit.getCorreo().equals(""))
            mess="Error de Validación. Faltan datos";
        else if(oBit.getTelCasa().equals("") && oBit.getTels().equals("")){
            mess="Al menos debe registrar un telefono";
        }else{
            
            HttpSession session =(HttpSession)context.getExternalContext().getSession(false);
            Usuario oUsu=(Usuario)session.getAttribute("usuarioSesion");
            oBit.getTipoPac().setCve("2");
            oBit.getPersHosp().setNombre(oUsu.getUsuario());
            mess=oBit.guardaCaptacion(oBit); 
            oBit=new BitacoraInfoPaq();
        }
        context.addMessage(null, new FacesMessage("Guardar Captación",mess));
        context.getExternalContext().getFlash().setKeepMessages(true);
    }
    
    public void guardaPromocion()throws Exception{
        String mess="";
        FacesContext context = FacesContext.getCurrentInstance();
        if (oBit==null||oBit.getNombre().equals("")||oBit.getApellidoPaterno().equals("")||oBit.getApellidoMaterno().equals("")||oBit.getGenero()=='S'||
                oBit.getEdad()==0||oBit.getTipoPac().getCve().length()==0 || oBit.getCorreo().equals(""))
            mess="Error de Validación. Faltan datos";
        else if(oBit.getTelCasa().equals("") && oBit.getTels().equals("")){
            mess="Al menos debe registrar un telefono";
        }else{
            
            HttpSession session =(HttpSession)context.getExternalContext().getSession(false);
            Usuario oUsu=(Usuario)session.getAttribute("usuarioSesion");
            oBit.setCirugia("");
            oBit.getPersHosp().setNombre(oUsu.getUsuario());
            mess=oBit.guardaCaptacion(oBit); 
            oBit=new BitacoraInfoPaq();
        }
        context.addMessage(null, new FacesMessage("Guardar Captación",mess));
        context.getExternalContext().getFlash().setKeepMessages(true);
    }
}
