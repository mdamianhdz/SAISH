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
import org.apli.jbs.facturacion.MunicipioJB;
import org.apli.modelbeans.Municipio;

/**
 *
 * @author Lily_LnBnd
 */
@ManagedBean(name="oConverterMunicipioDF")
@RequestScoped
public class ConverterMunicipioDF implements Converter{
    
      public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {  
        if (submittedValue.trim().equals("")) {  
            return null;  
        } else {  
            try {  
                String number = submittedValue;  
                for (Municipio e : MunicipioJB.listMunicipiosDF) {  
                    if (e.getCve().equals(number)) {  
                        return e;  
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
            return String.valueOf(((Municipio) value).getCve());  
        }  
    }
    
}


