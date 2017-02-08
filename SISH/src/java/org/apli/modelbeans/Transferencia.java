
package org.apli.modelbeans;

import java.io.Serializable;
import java.util.Date;
import java.util.Vector;
import org.apli.AD.AccesoDatos;

/**
 *Representa el desglose de forma de pago por Transferencia, que hereda de FormatoFormaPago,
 * tiene los datos part/empresa,bancoreceptor
 * @author AIMEE R
 */
public class Transferencia extends FormatoFormaPago implements Serializable{
    
    private String sEmpresa;
    private String sBancoReceptor;
    private AccesoDatos oAD;
    
    public Transferencia(){
        sEmpresa="";
        sBancoReceptor="";
    }

    public String getBancoReceptor(){
        return sBancoReceptor;
    }
    public void setBancoReceptor(String sBancoReceptor){
        this.sBancoReceptor=sBancoReceptor;
    }
    
    public String getEmpresa(){
        return sEmpresa;
    }
    public void setEmpresa(String sEmpresa){
        this.sEmpresa=sEmpresa;
    }
    
    public void setAD(AccesoDatos oAD) {
        this.oAD = oAD;
    }
    public AccesoDatos getAD() {
        return oAD;
    }
    
    public Transferencia[] buscaTransferencias(Date fechacorte) throws Exception{
      Transferencia[] arrRet=null;
      Transferencia oTransferencia=null;
      Vector rst=null;
      Vector<Transferencia> vObj=null;
      String sQuery="";
      int i=0,nTam=0;
      sQuery="SELECT * FROM buscaDetallexFormaPagoTRE('"+fechacorte+"'::date);";
      System.out.println(sQuery);
      
      //Si objeto para acceso a datos es nulo, entonces vuelve a conectar y desconectar, de lo contrario, sigue conectado y solo hace consulta
      if(getAD()==null){
          setAD(new AccesoDatos());
          getAD().conectar();
          rst=getAD().ejecutarConsulta(sQuery);
          getAD().desconectar();
          setAD(null);
      }
      else{
          rst=getAD().ejecutarConsulta(sQuery);
      }
      
      if(rst!=null){
          vObj=new Vector<Transferencia>();
          for(i=0;i<rst.size();i++){
              oTransferencia=new Transferencia();
              Vector vRowTemp=(Vector)rst.elementAt(i);
              oTransferencia.setFecha((Date)vRowTemp.elementAt(0));
              oTransferencia.setNombre((String)vRowTemp.elementAt(1));
              oTransferencia.setMonto(((Double)vRowTemp.elementAt(2)).floatValue());
              oTransferencia.setConcepto((String)vRowTemp.elementAt(3));
              oTransferencia.setEmpresa((String)vRowTemp.elementAt(4));
              oTransferencia.setFactura((String)vRowTemp.elementAt(5));
              oTransferencia.setBancoReceptor((String)vRowTemp.elementAt(6));
              oTransferencia.setFolio(((Double)vRowTemp.elementAt(7)).intValue());
              vObj.add(oTransferencia);
          }
          nTam=vObj.size();
          arrRet=new Transferencia[nTam];
          for(i=0;i<nTam;i++){
              arrRet[i]=vObj.elementAt(i);
          }
      }
      return arrRet;
    }
    
    public Transferencia calculaMontoTotalTransferencia(Date fechacorte) throws Exception {
        Transferencia oTransferencia= new Transferencia();
        Vector rst = null;
        String sQuery = "";
        Boolean bBol = true;

        sQuery = "SELECT sum(monto) FROM buscaDetallexFormaPagoTRE('"+fechacorte+"'::date);";
        
        if (this.getAD() == null) {
            this.setAD(new AccesoDatos());
            this.getAD().conectar();
            rst = this.getAD().ejecutarConsulta(sQuery);
            this.getAD().desconectar();
            setAD(null);
        } else {
            rst = this.getAD().ejecutarConsulta(sQuery);
        }

        if (rst != null && rst.size() == 1) {
            Vector vRowTemp = (Vector) rst.elementAt(0);
            oTransferencia.setTotal(((Double)vRowTemp.elementAt(0)).floatValue());
        }
        return oTransferencia;
    }
}
