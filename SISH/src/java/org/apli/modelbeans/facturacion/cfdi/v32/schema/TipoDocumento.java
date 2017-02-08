/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apli.modelbeans.facturacion.cfdi.v32.schema;
public enum TipoDocumento {
    tipo1 ("CFDI"),
    tipo2 ("Nota de crédito"); 
    private String tipo; 
    TipoDocumento(String text) {
        this.tipo = text;
    }
    public String getTipo(){
        return tipo;
    }
    public void setTipo(String tipo){
        this.tipo=tipo;
    }
    public static TipoDocumento fromString(String text) {
        if (text != null)
            for (TipoDocumento b : TipoDocumento.values()) 
                if (text.equalsIgnoreCase(b.tipo)) 
                    return b;
        return null;
    }
}
