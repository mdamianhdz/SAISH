package org.apli.jbs.catalogos;

import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import org.apli.modelbeans.Puesto;

/**
 *
 * @author Daniel
 */
@ManagedBean(name="oPuesto")
@RequestScoped
public class PuestoJB {
    private List<Puesto> listaPuestos;
    public Puesto oPu=new Puesto();
    /**
     * Creates a new instance of PuestoJB
     */
    public PuestoJB() {
        listaPuestos=new ArrayList();
        llenaLista();
    }

    private void llenaLista(){
        try {
            listaPuestos.addAll(oPu.buscaTodosPuestos());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
     public List<Puesto> getListaPuestos(){
        return listaPuestos;
    }
}
