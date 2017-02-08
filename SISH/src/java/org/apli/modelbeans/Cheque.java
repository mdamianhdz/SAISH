
package org.apli.modelbeans;


import java.io.Serializable;
import java.util.Date;
import java.util.Vector;
import org.apli.AD.AccesoDatos;

/**
 *Representa el desglose de forma de pago por Cheque, que hereda de FormatoFormaPago
 * incluye los datos  cuentahabiente del cheque, num. cheque, banco 
 * @author AIMEE R
 */
public class Cheque extends FormatoFormaPago implements Serializable{
    
    private String sCuentaHabiente;
    private String sNumCheque;
    private String sBanco;
    //private FormaPago oFP;
    //private List<String> listBanco=new ArrayList();
    private AccesoDatos oAD;
    
    public Cheque(){
        sCuentaHabiente="";
        sNumCheque="";
    }
    
    public String getBanco(){
        return sBanco;
    }
    public void setBanco(String sBanco){
        this.sBanco=sBanco;
    }
        
    public String getCuentaHabiente(){
        return sCuentaHabiente;
    }
    public void setCuentaHabiente(String sCuentaHabiente){
        this.sCuentaHabiente=sCuentaHabiente;
    }
    
    public String getNumCheque(){
        return sNumCheque;
    }
    public void setNumCheque(String nNumCheque){
        this.sNumCheque=nNumCheque;
    }
    
    public void setAD(AccesoDatos oAD) {
        this.oAD = oAD;
    }
    public AccesoDatos getAD() {
        return oAD;
    }
    
    public Cheque[] buscaCheques(Date fechacorte) throws Exception{
      Cheque[] arrRet=null;
      Cheque oCheque=null;
      Vector rst=null;
      Vector<Cheque> vObj=null;
      String sQuery="";
      int i=0,nTam=0;
      sQuery="SELECT * FROM buscaDetallexFormaPagoCHQ('"+fechacorte+"'::date);";
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
          vObj=new Vector<Cheque>();
          for(i=0;i<rst.size();i++){
              oCheque=new Cheque();
              Vector vRowTemp=(Vector)rst.elementAt(i);
              oCheque.setFecha((Date)vRowTemp.elementAt(0));
              oCheque.setNombre((String)vRowTemp.elementAt(1));
              oCheque.setMonto(((Double)vRowTemp.elementAt(2)).floatValue());
              oCheque.setConcepto((String)vRowTemp.elementAt(3));
              oCheque.setNumCheque((String)vRowTemp.elementAt(4));
              oCheque.setFactura((String)vRowTemp.elementAt(5));
              oCheque.setBanco((String)vRowTemp.elementAt(6));
              oCheque.setFolio(((Double)vRowTemp.elementAt(7)).intValue());
              vObj.add(oCheque);
          }
          nTam=vObj.size();
          arrRet=new Cheque[nTam];
          for(i=0;i<nTam;i++){
              arrRet[i]=vObj.elementAt(i);
          }
      }
      return arrRet;
    }
    
    public Cheque calculaMontoTotalCheques(Date fechacorte) throws Exception {
        Cheque oCheque= new Cheque();
        Vector rst = null;
        String sQuery = "";
        Boolean bBol = true;

        sQuery = "SELECT sum(monto) FROM buscaDetallexFormaPagoCHQ('"+fechacorte+"'::date);";
        
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
            oCheque.setTotal(((Double)vRowTemp.elementAt(0)).floatValue());
        }
        return oCheque;
    }
}
