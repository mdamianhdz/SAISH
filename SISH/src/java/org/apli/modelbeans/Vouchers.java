
package org.apli.modelbeans;

import java.io.Serializable;
import java.util.Date;
import java.util.Vector;
import org.apli.AD.AccesoDatos;

/**
 *Representa el desglose de la forma de Pago por Vouchers que 
 * hereda del FomatoFormaPago
 * titular tarjeta,hora,datos tarjeta,tipotarjeta
 * @author AIMEE R
 */
public class Vouchers extends FormatoFormaPago implements Serializable{
    
    private String sTitularTarjeta;
    private String sHora;
    private String sDatosTarjeta;
    private String sTipoTarjeta;
    private AccesoDatos oAD;
    
    
    public Vouchers(){
        sTitularTarjeta="";
        sHora="";
        sDatosTarjeta="";
        sTipoTarjeta="";
    }
    
    public String getTitularTarjeta(){
        return sTitularTarjeta;
    }
    public void setTitularTarjeta(String sTitularTarjeta){
        this.sTitularTarjeta=sTitularTarjeta;
    }
    
    public String getHora(){
        return sHora;
    }
    public void setHora(String sHora){
        this.sHora=sHora;
    }
    
    public String getDatosTarjeta(){
        return sDatosTarjeta;
    }
    public void setDatosTarjeta(String sDatosTarjeta){
        this.sDatosTarjeta=sDatosTarjeta;
    }
    
    public String getTipoTarjeta(){
        return sTipoTarjeta;
    }
    public void setTipoTarjeta(String sTipoTarjeta){
        this.sTipoTarjeta=sTipoTarjeta;
    }
    
    public void setAD(AccesoDatos oAD) {
        this.oAD = oAD;
    }

    public AccesoDatos getAD() {
        return oAD;
    }
    
    public Vouchers[] buscaVouchers(Date fechacorte) throws Exception{
      Vouchers[] arrRet=null;
      Vouchers oVouchers=null;
      Vector rst=null;
      Vector<Vouchers> vObj=null;
      String sQuery="";
      int i=0,nTam=0;
      
      if (fechacorte == null) {
            throw new Exception("Vouchers.buscaVouchers: error de programación, faltan datos");
        }
      else{
        sQuery="SELECT * FROM buscaDetallexFormaPagoTDC('"+fechacorte+"'::date);";
      
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
          vObj=new Vector<Vouchers>();
            for(i=0;i<rst.size();i++){
              oVouchers=new Vouchers();
              Vector vRowTemp=(Vector)rst.elementAt(i);
              oVouchers.setFecha((Date)vRowTemp.elementAt(0));
              oVouchers.setNombre((String)vRowTemp.elementAt(1));
              oVouchers.setMonto(((Double)vRowTemp.elementAt(2)).floatValue());
              oVouchers.setConcepto((String)vRowTemp.elementAt(3));
              oVouchers.setTitularTarjeta((String)vRowTemp.elementAt(4));
              oVouchers.setHora((String)vRowTemp.elementAt(5));
              oVouchers.setFactura((String)vRowTemp.elementAt(6));
              oVouchers.setDatosTarjeta((String)vRowTemp.elementAt(7));
              oVouchers.setTipoTarjeta((String)vRowTemp.elementAt(8));
              oVouchers.setFolio(((Double)vRowTemp.elementAt(9)).intValue());
              vObj.add(oVouchers);
          }
          nTam=vObj.size();
          arrRet=new Vouchers[nTam];
            for(i=0;i<nTam;i++){
              arrRet[i]=vObj.elementAt(i);
            }
        }
            return arrRet;
      }
    }

    public Vouchers calculaMontoTotalVouchers(Date fechacorte) throws Exception {
        Vouchers oVouchers= new Vouchers();
        Vector rst = null;
        String sQuery = "";
        Boolean bBol = true;

        sQuery = "SELECT sum(monto) FROM buscaDetallexFormaPagoTDC('"+fechacorte+"'::date);";
        
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
            oVouchers.setTotal(((Double)vRowTemp.elementAt(0)).floatValue());
        }
        return oVouchers;
    }
    
}
