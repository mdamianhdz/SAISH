/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apli.convertidores;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import org.apli.jbs.HabitacionJB;
import org.apli.modelbeans.Habitacion;

/**
 *
 * @author JRuiz
 */
@ManagedBean(name="oConverterHabitacion")
@RequestScoped
public class ConverterHabitacion implements Converter {

    public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {  
        if (submittedValue.trim().equals("")) {  
            return null;  
        } else {  
            try {  
                int number = Integer.parseInt(submittedValue);  
                for (Habitacion h : HabitacionJB.habitaciones) {  
                    if (h.getHab() == number) {  
                        return h;  
                    }  
                }  
  
            } catch(NumberFormatException exception) {  
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid player"));  
            }  
        }  
  
        return null;  
    }  
  
    public String getAsString(FacesContext facesContext, UIComponent component, Object value) {  
        if (value == null || value.equals("")) {  
            return "";  
        } else {  
            return String.valueOf(((Habitacion) value).getHab());  
        }  
    }
    
}
