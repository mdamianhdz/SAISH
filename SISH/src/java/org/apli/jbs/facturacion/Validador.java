/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apli.jbs.facturacion;
import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
/**
 *
 * @author Isa
 */
@ManagedBean(name="oValidador")
@RequestScoped
public class Validador implements Serializable{
    boolean error;
    FacesMessage msj  = new FacesMessage();
    Pattern p;
    Matcher m;
    String sResult;
    public void verificaNom(FacesContext context, UIComponent component, Object arg2)
			throws ValidatorException {              
            String nom = (String)arg2;
            msj  = new FacesMessage();
            p = Pattern.compile("[A-Za-z0-9]{1,60}");
            m = p.matcher(nom);
            error = m.matches();
            if (!error){
                sResult = "Error";
		((UIInput)component).setValid(false);
		msj.setDetail("Formato Incorrecto, nombre del receptor debe contener solo letras o números");
		msj.setSummary("Nombre incorrecto");
		context.addMessage(component.getClientId(context), msj);
            }  
    }   
    public void verificaApellidoPaterno(FacesContext context, UIComponent component, Object arg2)
			throws ValidatorException {              
            String apellido = (String)arg2;
            msj  = new FacesMessage();
            p = Pattern.compile("[A-Za-z0-9]{1,60}");
            m = p.matcher(apellido);
            error = m.matches();
            if (!error){
                sResult = "Error";
		((UIInput)component).setValid(false);
		msj.setDetail("Formato Incorrecto, apellido del anunciante debe contener solo letras o números");
		msj.setSummary("Apellido paterno incorrecto");
		context.addMessage(component.getClientId(context), msj);
            }  
    }  
    public void verificaApellidoMaterno(FacesContext context, UIComponent component, Object arg2)
			throws ValidatorException {              
            String apellido = (String)arg2;
            msj  = new FacesMessage();
            p = Pattern.compile("[A-Za-z0-9]{1,60}");
            m = p.matcher(apellido);
            error = m.matches();
            if (!error){
                sResult = "Error";
		((UIInput)component).setValid(false);
		msj.setDetail("Formato Incorrecto, apellido del anunciante debe contener solo letras o números");
		msj.setSummary("Apellido materno incorrecto");
		context.addMessage(component.getClientId(context), msj);
            }  
    }  
    public void verificaCalle(FacesContext context, UIComponent component, Object arg2)
			throws ValidatorException {              
            String calle1 = (String)arg2;
            msj  = new FacesMessage();
            p = Pattern.compile("[A-Za-z0-9]{1,60}");
            m = p.matcher(calle1);
            error = m.matches();
            if (!error){
                sResult = "Error";
		((UIInput)component).setValid(false);
		msj.setDetail("Formato Incorrecto, calle debe contener solo número o letras");
		msj.setSummary("Calle incorrecta");
		context.addMessage(component.getClientId(context), msj);
            }  
    }  
    public void verificaNumExt(FacesContext context, UIComponent component, Object arg2)
			throws ValidatorException { 
        try{
            Integer num = (Integer)arg2;
            msj  = new FacesMessage();
            p = Pattern.compile("[0-9]{1,5}");
            m = p.matcher(""+num);
            error = m.matches();
            if (!error){
                sResult = "Error";
		((UIInput)component).setValid(false);
		msj.setDetail("Formato Incorrecto, número debe ser un digito");
		msj.setSummary("Número exterior incorrecto");
		context.addMessage(component.getClientId(context), msj);
            }  	
        }
        catch(NumberFormatException ex){
                    ((UIInput)component).setValid(false);
                    msj.setDetail("El valor introducido no fue número");
                    msj.setSummary("Valor 'número' incorrecto");
                    context.addMessage(component.getClientId(context), msj);
        }
            
    }  
    public void verificaCol(FacesContext context, UIComponent component, Object arg2)
			throws ValidatorException {              
            String colonia = (String)arg2;
            msj  = new FacesMessage();
            p = Pattern.compile("[A-Za-z0-9]{1,60}");
            m = p.matcher(colonia);
            error = m.matches();
            if (!error){
                sResult = "Error";
		((UIInput)component).setValid(false);
		msj.setDetail("Formato Incorrecto, colonia debe ser conformada solo por letras o números");	
                msj.setSummary("Colonia incorrecto");
		context.addMessage(component.getClientId(context), msj);
            }  
    }  
    public void verificaCiudad(FacesContext context, UIComponent component, Object arg2)
			throws ValidatorException {              
            String cd = (String)arg2;
            msj  = new FacesMessage();
            p = Pattern.compile("[A-Za-z0-9]{1,60}");
            m = p.matcher(cd);
            error = m.matches();
            if (!error){
                sResult = "Error";
		((UIInput)component).setValid(false);
		msj.setDetail("Formato Incorrecto, ciudad debe ser formada solo por números o letras");
		msj.setSummary("Ciudad incorrecta");
		context.addMessage(component.getClientId(context), msj);
            }  
    }  
    public void verificaEstado(FacesContext context, UIComponent component, Object arg2)
			throws ValidatorException {              
            String edo = (String)arg2;
            msj  = new FacesMessage();
            p = Pattern.compile("[A-Za-z0-9]{1,60}");
            m = p.matcher(edo);
            error = m.matches();
            if (!error){
                sResult = "Error";
		((UIInput)component).setValid(false);
		msj.setDetail("Formato Incorrecto, estado debe ser compuesto solo por letras o números");
		msj.setSummary("Estado incorrecto");
		context.addMessage(component.getClientId(context), msj);
            }  
    }  
    public void verificaTel(FacesContext context, UIComponent component, Object arg2)
			throws ValidatorException {  
        try{
            Integer num = (Integer)arg2;
            msj  = new FacesMessage();
            p = Pattern.compile("[0-9]{1,15}");
            m = p.matcher(""+num);
            error = m.matches();
            if (!error){
                sResult = "Error";
		((UIInput)component).setValid(false);
		msj.setDetail("Formato Incorrecto, teléfono solo debe contener números");
		msj.setSummary("Teléfono incorrecto");
		context.addMessage(component.getClientId(context), msj);
            }  	
        }
        catch(NumberFormatException ex){
                    ((UIInput)component).setValid(false);
                    msj.setDetail("El valor introducido no fue número");
                    msj.setSummary("Valor 'número' incorrecto");
                    context.addMessage(component.getClientId(context), msj);
        }
            
    }
    public void verificaCel(FacesContext context, UIComponent component, Object arg2)
			throws ValidatorException {              
        try{
            Integer num = (Integer)arg2;
            msj  = new FacesMessage();
            p = Pattern.compile("[0-9]{1,15}");
            m = p.matcher(""+num);
            error = m.matches();
            if (!error){
                sResult = "Error";
		((UIInput)component).setValid(false);
		msj.setDetail("Formato Incorrecto, celular solo debe contener números");
		msj.setSummary("Celular incorrecto");
		context.addMessage(component.getClientId(context), msj);
            }  	
        }
        catch(NumberFormatException ex){
                    ((UIInput)component).setValid(false);
                    msj.setDetail("El valor introducido no fue número");
                    msj.setSummary("Valor 'número' incorrecto");
                    context.addMessage(component.getClientId(context), msj);
        }
    }
    public void verificaCorreo(FacesContext context, UIComponent component, Object arg2)
			throws ValidatorException {              
            String sCorre = (String)arg2;
            msj  = new FacesMessage();
            p = Pattern.compile("[A-Za-z0-9]{1,30}\\@[A-Za-z0-9]{1,30}");
            m = p.matcher(sCorre);
            error = m.matches();
            if (!error){
		((UIInput)component).setValid(false);
		msj.setDetail("El correo resulto no ser valido");
		msj.setSummary("Correo incorrecto");
		context.addMessage(component.getClientId(context), msj);
            }
    }
    public void validaRFC(FacesContext context, UIComponent component, Object arg2)
			throws ValidatorException { 
           String sCorre = (String)arg2;
            msj  = new FacesMessage();
            p = Pattern.compile("[a-zA-Z]{4}[0-9]{6}[a-zA-Z]{2}[0-9]{1}");
            m = p.matcher(sCorre);
            error = m.matches();
            if (!error){
		((UIInput)component).setValid(false);
		msj.setDetail("El RFC esta incorrecto");
		msj.setSummary("Correo incorrecto");
		context.addMessage(component.getClientId(context), msj);
            }
    }
    

}
