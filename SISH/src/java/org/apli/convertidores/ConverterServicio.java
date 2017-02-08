package org.apli.convertidores;

import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import javax.servlet.http.HttpSession;
import org.apli.modelbeans.ConceptoIngreso;

/**
 *
 * @author JRuiz
 */
@FacesConverter("ConverterServicio")
public class ConverterServicio implements Converter {

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, 
                            String submittedValue) {
    List<ConceptoIngreso> servicios = null;//serv.getServicios();
    HttpSession session =(HttpSession)facesContext.getExternalContext().getSession(true);
        servicios = (List<ConceptoIngreso>)session.getAttribute("listaServicios");
        System.out.println("En convertidor "+submittedValue);
        if (submittedValue.trim().equals("")) {
            return null;
        } else {
            try {
                for (ConceptoIngreso ci : servicios) {
                    System.out.println("Gen "+ci.getIdGenerico());
                    if (ci.getIdGenerico().equals(submittedValue)) {
                        System.out.println("Obtuvo concepto "+ci.getDescripcion());
                        return ci;
                    }
                }

            } catch (NumberFormatException exception) {
                exception.printStackTrace();
                throw new ConverterException(new FacesMessage(
                        FacesMessage.SEVERITY_ERROR, "Error de conversión", 
                        "No es un servicio válido"));
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent component, Object value) {
        if (value == null || value.equals("")) {
            return "";
        } else {
            return String.valueOf(((ConceptoIngreso) value).getIdGenerico());
        }
    }
}
