package org.apli.modelbeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Listado de elementos sobre el responsable de pago: paciente (particular), 
 * empresa, paquete o paciente pero de empresa con descuento
 * @author BAOZ
 */
public class TipoPrincipalPaga  implements Serializable {
    private int nTipo;
    private String sDesc="";
    private List<TipoPrincipalPaga> tipos;
    public static int TIPO_PART = 0;
    public static int TIPO_EMP = 1;
    public static int TIPO_PAQ = 2;
    public static int TIPO_DSCTO = 3;
    
    public TipoPrincipalPaga(){
        tipos = new ArrayList();
        tipos.add(new TipoPrincipalPaga(TIPO_PART, "Particular"));
        tipos.add(new TipoPrincipalPaga(TIPO_EMP, "Crédito Empresa"));
        tipos.add(new TipoPrincipalPaga(TIPO_PAQ, "Paquete"));
        tipos.add(new TipoPrincipalPaga(TIPO_DSCTO, "Particular Empresa c/Descuento"));
    }
    
    public TipoPrincipalPaga(int nCve, String sNom){
        this.nTipo = nCve;
        this.sDesc = sNom;
    }
    
    public List<TipoPrincipalPaga> buscaTiposPrincipalPaga() {
        return tipos;
    }
    public boolean buscar() throws Exception{
    boolean bRet = false;
        if (this.nTipo<0)
            throw new Exception("TipoPrincipalPaga.buscar: faltan datos");
        else{
            if (nTipo>=0 && nTipo <=3){
                this.sDesc = tipos.get(nTipo).getDescripcion();
                bRet = true;
            }
        }
        return bRet;
    }
    
    public int getTipo(){
        return this.nTipo;
    }
    
    public void setTipo(int value){
        this.nTipo = value;
    }
    
    public String getDescripcion(){
        return this.sDesc;
    }
}
