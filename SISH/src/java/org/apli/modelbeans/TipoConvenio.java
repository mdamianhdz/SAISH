package org.apli.modelbeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author BAOZ
 */
public class TipoConvenio implements Serializable {
    private short nTipo;
    private String sDesc="";
    private List<TipoConvenio> tipos;
    public static short TIPO_CRED = 0;
    public static short TIPO_DESC = 1;
    
    public TipoConvenio(){
        tipos = new ArrayList();
        tipos.add(new TipoConvenio(TIPO_CRED, "A crédito"));
        tipos.add(new TipoConvenio(TIPO_DESC, "Con descuento"));
    }
    
    public TipoConvenio(short cve, String nom){
        this.nTipo = cve;
        this.sDesc = nom;
    }
    
    
    public boolean buscar() throws Exception{
    boolean bRet = false;
        if (this.nTipo<0)
            throw new Exception("TipoConvenio.buscar: faltan datos");
        else{
            if (nTipo>=0 && nTipo <=1){
                this.sDesc = tipos.get(nTipo).getDescripcion();
                bRet = true;
            }
        }
        return bRet;
    }
    
    public short getTipo(){
        return this.nTipo;
    }
    
    public void setTipo(short value){
        this.nTipo = value;
    }
    
    public String getDescripcion(){
        return this.sDesc;
    }
    
    public List<TipoConvenio> getTodas(){
        return this.tipos;
    }
}
