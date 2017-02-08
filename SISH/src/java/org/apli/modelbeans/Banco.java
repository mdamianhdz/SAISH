package org.apli.modelbeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Listado de bancos
 * @author BAOZ
 */
public class Banco  implements Serializable {
    
    public List<String> getBancos() {
        List<String> listB = new ArrayList();
        listB.add("Banamex");
        listB.add("Bancomer");
        listB.add("Banorte");
        listB.add("Santander");

        return listB;
    }
}
