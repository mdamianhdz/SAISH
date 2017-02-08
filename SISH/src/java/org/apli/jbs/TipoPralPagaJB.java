package org.apli.jbs;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.apli.modelbeans.TipoPrincipalPaga;
import java.io.Serializable;
import java.util.List;

/**
 * Encapsula listado de Tipo Principal / Quien Paga 
 * @author BAOZ
 */
@ManagedBean(name = "oTipoPralPaga")
@SessionScoped
public class TipoPralPagaJB  implements Serializable {
private static List<TipoPrincipalPaga> oListaTipoPral;
    public TipoPralPagaJB() {
        oListaTipoPral = (new TipoPrincipalPaga()).buscaTiposPrincipalPaga();
    }
    
    public List<TipoPrincipalPaga> getListaTipoPral(){
        return oListaTipoPral;
    }
}
