package org.apli.convertidores;

import java.util.ArrayList;
import java.util.Arrays;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import org.apli.modelbeans.LineaIngreso;

/**
 *
 * @author JRuiz
 */
@FacesConverter("oConverterLineaIngreso")
public class ConverterLineaIngreso  implements Converter  {
private static ArrayList<LineaIngreso> lineas;
    
    public ConverterLineaIngreso(){
        if (lineas == null)
            try{
                lineas = new ArrayList<LineaIngreso>(Arrays.asList(
                        (new LineaIngreso()).buscaTodas()));
            }catch(Exception e){
                e.printStackTrace();
            }
    }
    
    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, 
                                String submittedValue) { 
    LineaIngreso oRet=null;
        System.out.println("En convertidor "+submittedValue);
        if (submittedValue.trim().equals("")) {  
            return null;  
        } else {  
            try {  
                int number = Integer.parseInt(submittedValue);  
  
                for (LineaIngreso l : lineas) {  
                    if (l.getCveLin() == number) {  
                        oRet = l;  
                    }  
                }  
            } catch(NumberFormatException exception) {  
                throw new ConverterException(new FacesMessage(
                        FacesMessage.SEVERITY_ERROR, "Conversion Error", 
                        "Problemas con líneas de ingreso"));  
            }  
        }  
        System.out.println(oRet);
        return oRet;  
    }  
  
    @Override
    public String getAsString(FacesContext facesContext, UIComponent component, Object value) {  
        if (value == null || value.equals("")) {  
            return "";  
        } else {
            return String.valueOf(((LineaIngreso) value).getCveLin());  
        }  
    }
}