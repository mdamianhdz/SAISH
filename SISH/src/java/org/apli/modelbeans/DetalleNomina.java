/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apli.modelbeans;

import java.io.Serializable;
import java.util.Date;
import java.util.Vector;
import org.apli.AD.AccesoDatos;

/**
 *
 * @author Lily_LnBnd
 */
public class DetalleNomina implements Serializable{
    private Date dInicio;
    private Date dFin;
    private ConceptoNomina oConcNom;
    private float nMonto;
    private AccesoDatos oAD;
    
    public DetalleNomina(){
        
    }
    
   public String guardaNomina(PersonalHospitalario oPH)throws Exception{
        Vector rst = null;
        String sQuery = "", sRet="";      
        

        sQuery="delete from detallenomina where nfoliopers="+oPH.getFolioPers()+" and dini='"+oPH.getNomina().getInicio()+
                "' and dfin='"+oPH.getNomina().getFin()+"';";
        for (int i = 0; i < oPH.getListConceptos().size(); i++) {
           sQuery=sQuery+"\nselect * from insertaDetalleNomina("+oPH.getFolioPers()+", '"+oPH.getNomina().getInicio()+"','"+oPH.getNomina().getFin()+
                   "', "+oPH.getListConceptos().get(i).getConcNom().getCveConcepNom() +"::int2, "+oPH.getListConceptos().get(i).getMonto()+");\n";
       }
        System.out.println(sQuery);
        if (getAD() == null){
            setAD(new AccesoDatos());
            getAD().conectar();
            rst = getAD().ejecutarConsulta(sQuery);
            getAD().desconectar();
            setAD(null);
        }else{
            rst = getAD().ejecutarConsulta(sQuery);
        }
        System.out.println("'"+rst.get(0).toString()+"'");
        if ("Multiple ResultSets were returned by the query".equals(rst.get(0).toString().substring(1, rst.get(0).toString().length()-1)))
            sRet="Se guardó correctamente la nómina del empleado: "+oPH.getNombreCompleto();
        else
            if (" La consulta no retornó ningún resultado.".equals(rst.get(0).toString()))
                sRet="Se guardó correctamente la nómina del empleado: "+oPH.getNombreCompleto();
            else
                sRet="Se produjo un error mientras se almacenaban los datos. Vuelva a intentarlo";
        return sRet;
   }

    public Date getInicio() {
        return dInicio;
    }

    public void setInicio(Date dInicio) {
        this.dInicio = dInicio;
    }

    public Date getFin() {
        return dFin;
    }

    public void setFin(Date dFin) {
        this.dFin = dFin;
    }

    public ConceptoNomina getConcNom() {
        return oConcNom;
    }

    public void setConcNom(ConceptoNomina oConcNom) {
        this.oConcNom = oConcNom;
    }

    public float getMonto() {
        return nMonto;
    }

    public void setMonto(float nMonto) {
        this.nMonto = nMonto;
    }

    public AccesoDatos getAD() {
        return oAD;
    }

    public void setAD(AccesoDatos oAD) {
        this.oAD = oAD;
    }
}
