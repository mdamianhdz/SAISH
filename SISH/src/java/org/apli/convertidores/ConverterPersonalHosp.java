package org.apli.convertidores;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import org.apli.modelbeans.PersonalHospitalario;

/**
 *
 * @author JRuiz
 */
@FacesConverter("ConverterPersonalHosp")
public class ConverterPersonalHosp implements Converter{
    
    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {  
        if (submittedValue.trim().equals("")) {  
            return null;  
        } else {  
            try {  
                PersonalHospitalario obj = new PersonalHospitalario();
                int number = Integer.parseInt(submittedValue);  
                obj.setFolioPers(number);
                obj = obj.buscaLlavePersonal();
                if (obj.getApellidoPaterno() != null &&
                    !obj.getApellidoPaterno().equals(""))
                    return obj;
            } catch(Exception e) { 
                e.printStackTrace();
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Al buscar personal"));  
            }  
        }  
  
        return null;  
    }  
  
    @Override
    public String getAsString(FacesContext facesContext, UIComponent component, Object value) {  
        if (value == null || value.equals("")) {  
            return "";  
        } else {  
            return String.valueOf(((PersonalHospitalario) value).getFolioPers());  
        }  
    }
    
    
    
}
