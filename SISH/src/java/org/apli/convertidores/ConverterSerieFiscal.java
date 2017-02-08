package org.apli.convertidores;

import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import org.apli.jbs.facturacion.ComprobanteJB;

import org.apli.modelbeans.facturacion.cfdi.v32.schema.SerieFiscal;
/**
 * @author Isabel Espinoza
 */
@ManagedBean(name="oConverterSerieFiscal")
@RequestScoped
public class ConverterSerieFiscal implements Converter {
    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {  
        if (submittedValue.trim().equals(""))
            return null;
        else{
            try{
                List<SerieFiscal> series=new ComprobanteJB().getSeries();
                for (SerieFiscal p : series) 
                    if (p.getNombre().equals(submittedValue)) 
                        return p;
            }catch(Exception exception){
            }
        }
        return null;
    }  
    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null || value.equals(""))
            return "";
        else 
            return String.valueOf(((SerieFiscal) value).getNombre());
    }
}