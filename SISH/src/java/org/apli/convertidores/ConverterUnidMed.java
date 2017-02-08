package org.apli.convertidores;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import org.apli.jbs.UnidadMedidaJB;
import org.apli.modelbeans.UnidadMedida;

/**
 *
 * @author JRuiz
 */
@FacesConverter("oConverterUnidMed")
public class ConverterUnidMed implements Converter{
      @Override
      public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {  
        if (submittedValue.trim().equals("")) {  
            return null;  
        } else {  
            try {  
                String number = submittedValue;  
                for (UnidadMedida u : UnidadMedidaJB.unidadesMedidas) {  
                    if (u.getCve().equals(number)) {  
                        return u;  
                    }  
                }  
  
            } catch(NumberFormatException exception) {  
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid player"));  
            }  
        }  
  
        return null;  
    }  
  
      @Override
    public String getAsString(FacesContext facesContext, UIComponent component, Object value) {  
        if (value == null || value.equals("")) {  
            return "";  
        } else {  
            return String.valueOf(((UnidadMedida) value).getCve());  
        }  
    }
    
}
