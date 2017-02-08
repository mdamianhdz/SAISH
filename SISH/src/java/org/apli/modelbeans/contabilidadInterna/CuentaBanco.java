package org.apli.modelbeans.contabilidadInterna;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import org.apli.AD.AccesoDatos;
import org.apli.modelbeans.ventaCredito.CompaniaCred;

/**
 *
 * @author Lily_LnBnd
 */
public class CuentaBanco implements Serializable{
    private CompaniaCred oBanco;
    private String sNumCtaBan;
    private String sDescripcion;
    private String sMoneda;
    private float nSaldoInicial;
    private AccesoDatos oAD;
    
    public CuentaBanco(){
        oBanco=new CompaniaCred();
    }
    
    public List<CuentaBanco> buscaCuentas(int nIdEmp)throws Exception{
        List<CuentaBanco> listRet=new ArrayList();
        CuentaBanco  oCB;
        
        Vector rst = null;
        String sQuery = "";
        
        sQuery = "select * from buscaCuentas("+nIdEmp+"::int2)";
        if (getAD() == null){
            setAD(new AccesoDatos());
            getAD().conectar();
            rst = getAD().ejecutarConsulta(sQuery);
            getAD().desconectar();
            setAD(null);
        }else{
            rst = getAD().ejecutarConsulta(sQuery);
        }
               
        if (rst != null) {
            listRet = new ArrayList();
            for (int i = 0; i < rst.size(); i++) {
                Vector vRowTemp = (Vector)rst.elementAt(i);
                oCB=new CuentaBanco();
                oCB.setNumCtaBan((String) vRowTemp.elementAt(0)); 
                oCB.setDescripcion((String) vRowTemp.elementAt(1)); 
                listRet.add(oCB);
            }
        }
        return listRet;
    }

    public CompaniaCred getBanco() {
        return oBanco;
    }

    public void setBanco(CompaniaCred oBanco) {
        this.oBanco = oBanco;
    }

    public String getNumCtaBan() {
        return sNumCtaBan;
    }

    public void setNumCtaBan(String sNumCtaBan) {
        this.sNumCtaBan = sNumCtaBan;
    }

    public String getDescripcion() {
        return sDescripcion;
    }

    public void setDescripcion(String sDescripcion) {
        this.sDescripcion = sDescripcion;
    }

    public String getMoneda() {
        return sMoneda;
    }

    public void setMoneda(String sMoneda) {
        this.sMoneda = sMoneda;
    }

    public float getSaldoInicial() {
        return nSaldoInicial;
    }

    public void setSaldoInicial(float nSaldoInicial) {
        this.nSaldoInicial = nSaldoInicial;
    }

    public AccesoDatos getAD() {
        return oAD;
    }

    public void setAD(AccesoDatos oAD) {
        this.oAD = oAD;
    }
}
