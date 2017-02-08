/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apli.modelbeans.facturacion.cfdi.v32.schema;
/**
 *
 * @author Isa
 */
public enum EmisorRegimenFiscal {
    regimen1 ("Régimen de Incorporacion Fiscal"),
    regimen2 ("Régimen General Actividad empresarial profesional"),
    regimen3 ("Arrendamiento"),
    regimen4 ("Salarios"),
    regimen5 ("Intereses y Dividendos");
    private String regimen;

    public String getRegimen() {
        return regimen;
    }
    public void setRegimen(String regimen) {
        this.regimen = regimen;
    }
    EmisorRegimenFiscal(String text) {
        this.regimen = text;
    }
    public static EmisorRegimenFiscal fromString(String text) {
        if (text != null) 
            for (EmisorRegimenFiscal b : EmisorRegimenFiscal.values()) 
                if (text.equalsIgnoreCase(b.regimen)) 
                    return b;
        return null;
    }
}
