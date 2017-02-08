package org.apli.modelbeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import org.apli.AD.AccesoDatos;

/**
 *
 * @author lleon
 */
public class FormaPago implements Serializable {
    /**
     * Clave de la forma de pago
     */
    private String sCveFrmPago;
    /**
     * Descripción de la forma de pago
     */
    private String sDescripcion;

    private AccesoDatos oAD;
    
    public static String CVE_TDC = "TDC";
    public static String CVE_TDD = "TDD";
    public static String CVE_CHQ = "CHQ";
    public static String CVE_TRE = "TRE";
    public static String CVE_DEP = "DEP";
    public static String CVE_EFE = "EFE";



    
    public FormaPago() {
    }

    public List<FormaPago> buscaFormasPago() throws Exception {
    FormaPago oFM = null;
    List<FormaPago> listRet = null;
    Vector rst = null;
    String sQuery = "";

        sQuery = "select * from buscatodosformadepago()";
        if(oAD==null){
            oAD=new AccesoDatos();
            oAD.conectar();
            rst=oAD.ejecutarConsulta(sQuery);
            oAD.desconectar();
            oAD=null;
        }else{
            rst=oAD.ejecutarConsulta(sQuery);
        }
        if (rst != null) {
            listRet = new ArrayList();
            for (int i = 0; i < rst.size(); i++) {
                Vector vRowTemp = (Vector) rst.elementAt(i);
                oFM = new FormaPago();
                oFM.setCveFrmPago((String) vRowTemp.elementAt(0));
                oFM.setDescripcion((String) vRowTemp.elementAt(1));
                listRet.add(oFM);
            }
        }
        return listRet;
    }

    public boolean buscar()throws Exception{
    Vector rst = null;
    String sQuery = "";
    boolean bRet = false;

        if (this.sCveFrmPago.equals("")) {
            throw new Exception("formaPago.buscar: error de programación, faltan datos");
        }else{
            sQuery = "select * from buscaLlaveFormaDePago('"+this.sCveFrmPago+"')";
            if(oAD==null){
                oAD=new AccesoDatos();
                oAD.conectar();
                rst=oAD.ejecutarConsulta(sQuery);
                oAD.desconectar();
                oAD=null;
            }else{
                rst=oAD.ejecutarConsulta(sQuery);
            }

            if (rst != null && rst.size() == 1) {
                Vector vRowTemp = (Vector)rst.elementAt(0);
                this.sDescripcion = (String) vRowTemp.elementAt(1);
                bRet = true;
            }
        }
        return bRet;
    }

    public String insertar() throws Exception{
    Vector rst = null;
    String sQuery = "";
        
        if (this.sCveFrmPago.equals("")||
            this.sDescripcion.equals("")){
            throw new Exception("FormaPago.insertar: error de programación, faltan datos");
	}else {
            sQuery="SELECT * from insertaFormaDePago('"+
                    this.sCveFrmPago+"'::char(5),'"+
                    this.sDescripcion+"'::varchar)";
            System.out.println(sQuery);
            if(oAD==null){
                oAD=new AccesoDatos();
                oAD.conectar();
                rst=oAD.ejecutarConsulta(sQuery);
                oAD.desconectar();
                oAD=null;
            }else{
                rst=oAD.ejecutarConsulta(sQuery); //regresa el código
            }
	}
        return ""+rst.get(0).toString().substring(1, rst.get(0).toString().length() - 1);
    }        
        
    public String modificar() throws Exception{
    Vector rst = null;
    String sQuery = "";
        
        if (this.sCveFrmPago.equals("")||
            this.sDescripcion.equals("")){
            throw new Exception("FormaPago.modificar: error de programación, faltan datos");
	}else {
            sQuery="select * from modificaFormaDePago('"+
                    this.sCveFrmPago+"'::char(5),'"+
                    this.sDescripcion+"'::varchar)";
            System.out.println(sQuery);
            if(oAD==null){
                oAD=new AccesoDatos();
                oAD.conectar();
                rst=oAD.ejecutarConsulta(sQuery);
                oAD.desconectar();
                oAD=null;
            }else{
                rst=oAD.ejecutarConsulta(sQuery); //regresa el código
            } 
	}
        return ""+rst.get(0).toString().substring(1, rst.get(0).toString().length() - 1);
    }
     
    public String eliminar() throws Exception{
    Vector rst = null;
    String sQuery = "";
        
        if (this.sCveFrmPago.equals("")){
            throw new Exception("FormaPago.eliminar: error de programación, faltan datos");
	}else {
            sQuery="select * from eliminaFormaDePago('"+
                    this.sCveFrmPago+"'::char(5))";
            System.out.println(sQuery);
            if(oAD==null){
                oAD=new AccesoDatos();
                oAD.conectar();
                rst=oAD.ejecutarConsulta(sQuery);
                oAD.desconectar();
                oAD=null;
            }else{
                rst=oAD.ejecutarConsulta(sQuery); //regresa el código
            } 
	}
        return ""+rst.get(0).toString().substring(1, rst.get(0).toString().length() - 1);
    }
    
    public String getCveFrmPago() {
        return sCveFrmPago;
    }

    public void setCveFrmPago(String sCveFrmPago) {
        this.sCveFrmPago = sCveFrmPago;
    }

    public String getDescripcion() {
        return sDescripcion;
    }

    public void setDescripcion(String sDescripcion) {
        this.sDescripcion = sDescripcion;
    }

    public AccesoDatos getAD() {
        return oAD;
    }

    public void setAD(AccesoDatos oAD) {
        this.oAD = oAD;
    }
}
