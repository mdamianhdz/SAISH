package org.apli.jbs.facturacion;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import org.apli.modelbeans.facturacion.cfdi.v32.schema.Comprobante;
/**
 * Autor: Isabel Espinoza Espinoza
 * Fecha: Abril 2014
 */
@ManagedBean(name="oEmisor")
@RequestScoped
public class EmisorJB  implements Serializable{
    private Comprobante.Emisor oEmisorSeleccionado;
    private List<Comprobante.Emisor> emisores;  
    /*
     * Modifica el emisor
     */
    public String modificar() throws Exception{
        if (oEmisorSeleccionado.modificarEmisor()<1)
            return "Error";
        else return "admReceptor";     
    }
    /*
     * Elimina el emisor
     */
    public String eliminar() throws Exception{
        if (oEmisorSeleccionado.eliminarEmisor()<1)
            return "Error";
        else return "admReceptor";
    }
     /*
     * Crea un nuevo emisor
     */
    public String insertar() throws Exception{
        if (oEmisorSeleccionado.insertarEmisor()<1)
            return "Error";
        else 
            return "admReceptor";            
    }
    public List<Comprobante.Emisor> getEmisores() {
        return emisores;
    }
    public void setEmisores(List<Comprobante.Emisor> value) {
        this.emisores = value;
    }
    public EmisorJB() {
        try {
            emisores = new Comprobante.Emisor().getTodosEmisores();
        } catch (Exception ex) {
            Logger.getLogger(EmisorJB.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 
    public Comprobante.Emisor getEmisorSeleccionado() {
        return oEmisorSeleccionado;
    }
    public void setEmisorSeleccionado(Comprobante.Emisor emisor) {
        this.oEmisorSeleccionado = emisor;
    }
}

