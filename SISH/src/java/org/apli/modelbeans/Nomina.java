/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apli.modelbeans;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import org.apli.AD.AccesoDatos;

/**
 *
 * @author Lily_LnBnd
 */
public class Nomina implements Serializable{
    
    private Date dInicio;
    private Date dFin;
    private Date dPago;
    private AccesoDatos oAD;
    
    public Nomina(){
        
    }
    
    public String generaNomina(List<PersonalHospitalario> listPH, Date dIni, Date dFin, Date dPago)throws Exception{
        Vector rst = null;
        String sQuery = "", sRet="";      

        for (int i = 0; i < listPH.size(); i++) {
           sQuery=sQuery+"select * from insertanomina("+listPH.get(i).getFolioPers()+", '"+dIni+"', '"+dFin+"', '"+dPago+"');\n";
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
        if ("Multiple ResultSets were returned by the query".equals(rst.get(0).toString().substring(1, rst.get(0).toString().length()-1)))
            sRet="Se generó correctamente la nómina para los empleados seleccionados";
        else
            if ("[Se ha registrado la nómina satisfactoriamente]".equals(rst.get(0).toString()))
                sRet="Se generó correctamente la nómina para los empleados seleccionados";
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

    public Date getPago() {
        return dPago;
    }

    public void setPago(Date dPago) {
        this.dPago = dPago;
    }  

    public AccesoDatos getAD() {
        return oAD;
    }

    public void setAD(AccesoDatos oAD) {
        this.oAD = oAD;
    }
    
    
}
