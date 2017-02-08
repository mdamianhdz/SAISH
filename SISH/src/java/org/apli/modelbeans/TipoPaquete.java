package org.apli.modelbeans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author ERJI
 */
public class TipoPaquete implements Serializable{
    
    private String sCve;
    private String sDescrip;
    public static final int PEDIATRICO=0;
    public static final int MATERNIDAD=1;
    public static final int QUIRURGICO=2;
    public static final int PERSONALIZADO=3;
    
    public TipoPaquete(){}
    
    public TipoPaquete(String cve, String desc){
        this.sCve = cve;
        this.sDescrip = desc;
    }

    public String getCve() {
        return sCve;
    }

    public void setCve(String sCve) {
        this.sCve = sCve;
    }

    public String getDescrip() {
        return sDescrip;
    }

    public void setDescrip(String sDescrip) {
        this.sDescrip = sDescrip;
    }
    
    public ArrayList<TipoPaquete> buscaTodosSinPersonalizado(){
    ArrayList<TipoPaquete> arrRet = new ArrayList();
    TipoPaquete oTip;
        oTip = new TipoPaquete(PEDIATRICO+"", "Pediátrico");
        arrRet.add(oTip);
        oTip = new TipoPaquete(MATERNIDAD+"", "Maternidad");
        arrRet.add(oTip);
        oTip = new TipoPaquete(QUIRURGICO+"", "Quirúrgico");
        arrRet.add(oTip);
        return arrRet;
    }
    
    public ArrayList<TipoPaquete> buscaTodosConPersonalizado(){
    ArrayList<TipoPaquete> arrRet = buscaTodosSinPersonalizado();
    TipoPaquete oTip;
        oTip = new TipoPaquete(PERSONALIZADO+"", "Personalizado");
        arrRet.add(oTip);
        return arrRet;
    }
}
