package org.apli.convertidores;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import org.apli.jbs.facturacion.ComprobanteJB;
import org.apli.modelbeans.facturacion.cfdi.v32.schema.Comprobante;
/**
 * @author Isabel Espinoza
 */
@ManagedBean(name="oConverterReceptor")
@RequestScoped
public class ConverterReceptor implements Converter {
    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {  
        if (submittedValue.trim().equals(""))
            return null;
        else{
            try{
                for (Comprobante.Receptor p : ComprobanteJB.receptores) 
                    if (p.getRfc().equals(submittedValue)) 
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
            return String.valueOf(((Comprobante.Receptor) value).getRfc());
    }
}