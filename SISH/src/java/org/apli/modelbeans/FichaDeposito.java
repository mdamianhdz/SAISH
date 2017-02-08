
package org.apli.modelbeans;

import java.io.Serializable;
import java.util.Date;
import java.util.Vector;
import org.apli.AD.AccesoDatos;
/**
 *Muestra los datos por forma de pago para Fichas de Deposito que hereda de FormatoFormaPago
 * incluye los datos part/empresa y banco receptor
 * @author AIMEE R
 */
public class FichaDeposito extends FormatoFormaPago implements Serializable {
    
    private String sEmpresa;
    private String sBancoReceptor;
    private AccesoDatos oAD;
    
    public FichaDeposito(){
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
    
    public FichaDeposito[] buscaFichasDeposito(Date fechacorte) throws Exception{
      FichaDeposito[] arrRet=null;
      FichaDeposito oFD=null;
      Vector rst=null;
      Vector<FichaDeposito> vObj=null;
      String sQuery="";
      int i=0,nTam=0;
      sQuery="SELECT * FROM buscaDetallexFormaPagoDEP('"+fechacorte+"'::date);";
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
          vObj=new Vector<FichaDeposito>();
          for(i=0;i<rst.size();i++){
              oFD=new FichaDeposito();
              Vector vRowTemp=(Vector)rst.elementAt(i);
              oFD.setFecha((Date)vRowTemp.elementAt(0));
              oFD.setNombre((String)vRowTemp.elementAt(1));
              oFD.setMonto(((Double)vRowTemp.elementAt(2)).floatValue());
              oFD.setConcepto((String)vRowTemp.elementAt(3));
              oFD.setEmpresa((String)vRowTemp.elementAt(4));
              oFD.setFactura((String)vRowTemp.elementAt(5));
              oFD.setBancoReceptor((String)vRowTemp.elementAt(6));
              oFD.setFolio(((Double)vRowTemp.elementAt(7)).intValue());
              vObj.add(oFD);
          }
          nTam=vObj.size();
          arrRet=new FichaDeposito[nTam];
          for(i=0;i<nTam;i++){
              arrRet[i]=vObj.elementAt(i);
          }
      }
      return arrRet;
    }
    
    public FichaDeposito calculaMontoTotalFichaDeposito(Date fechacorte) throws Exception {
        FichaDeposito oFD= new FichaDeposito();
        Vector rst = null;
        String sQuery = "";
        Boolean bBol = true;

        sQuery = "SELECT sum(monto) FROM buscaDetallexFormaPagoDEP('"+fechacorte+"'::date);";
        
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
            oFD.setTotal(((Double)vRowTemp.elementAt(0)).floatValue());
        }
        return oFD;
    }
}
