package org.apli.jbs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.apli.modelbeans.Diagnostico;

/**
 *
 * @author JRuiz
 */
@ManagedBean(name="oListaDiag")
@ViewScoped
public class ListaDiagnosticoJB implements Serializable {
    public static List<Diagnostico> diagnosticos=new ArrayList<Diagnostico>();
    
    
    public List<Diagnostico> obtenerDiagnostico(String s) {
        List<Diagnostico> diagSelect = new ArrayList<Diagnostico>();
        s = s.toUpperCase();
        try {
            if (ListaDiagnosticoJB.diagnosticos.isEmpty())
                ListaDiagnosticoJB.diagnosticos = new Diagnostico().buscaTodos();
            if (s.trim().equals("")) {
                return new ArrayList<Diagnostico>();
            }
            for (Diagnostico d : ListaDiagnosticoJB.diagnosticos) {
                if (d.getDescrip().contains(s)) {
                    diagSelect.add(d);
                }
            }
            return diagSelect;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
