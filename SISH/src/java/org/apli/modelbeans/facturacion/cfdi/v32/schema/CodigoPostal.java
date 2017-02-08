/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apli.modelbeans.facturacion.cfdi.v32.schema;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import org.apli.AD.AccesoDatos;
/**
 *
 * @author Isa
 */
public class CodigoPostal {
    private String sCve;
    private String sCveEdo;
    private String sCveMun;
    private String sCveLoc;
         protected AccesoDatos oAD = null;
         
    public AccesoDatos getAD() {
        return oAD;
    }
    public void setAD(AccesoDatos oAD) {
        this.oAD = oAD;
    }
    public CodigoPostal buscarCodigoPostal() throws Exception{
        CodigoPostal oP=null;
        Vector rst = null;
        Vector<CodigoPostal> vObj = null;
        String sQuery = "";
        int i=0;
        sQuery = "SELECT * cod_postal where cve='"+this.sCve+"'";
        if (getAD() == null){
            setAD(new AccesoDatos());
            getAD().conectar();
            rst = getAD().ejecutarConsulta(sQuery);
            getAD().desconectar();
            setAD(null);
        }
        else{
            rst = getAD().ejecutarConsulta(sQuery);
        }
        if (rst != null) {
            vObj = new Vector<CodigoPostal>();   
                oP = new CodigoPostal();
                Vector vRowTemp = (Vector)rst.elementAt(i);
                oP.setCve((String) vRowTemp.elementAt(0));
                oP.setCveEdo((String) vRowTemp.elementAt(1));
                oP.setCveMun((String) vRowTemp.elementAt(2));
                oP.setCveLoc((String) vRowTemp.elementAt(3));
        }
        return this;
    }
  
    public List<String> getClavesCodigosPostales() throws Exception{
        String arr[] = null, oP=null;
        Vector rst = null;
        Vector<String> vObj = null;
        List<String> list=new ArrayList();
        String sQuery = "";
        int i=0, nTam = 0;
        sQuery = "select * from cod_postal";
        if (getAD() == null){
            setAD(new AccesoDatos());
            getAD().conectar();
            rst = getAD().ejecutarConsulta(sQuery);
            getAD().desconectar();
            setAD(null);
        }
        else{
            rst = getAD().ejecutarConsulta(sQuery);
        }
        if (rst != null) {
            vObj = new Vector<String>();
            for (i = 0; i < rst.size(); i++) {
                oP = new String();
                Vector vRowTemp = (Vector)rst.elementAt(i); 
                list.add((String) vRowTemp.elementAt(0));
            }
            nTam = vObj.size();
            arr = new String[nTam];
            for (i=0; i<nTam; i++){
                arr[i] = vObj.elementAt(i);
            }
        }
        return list;
    }

    public List<CodigoPostal> getInformacionCodigosPostales()throws Exception{
        CodigoPostal arrCodPost[] = null, oP=null;
        Vector rst = null;
        Vector<CodigoPostal> vObj = null;
        List<CodigoPostal> listCodPost=new ArrayList();
        String sQuery = "";
        int i=0, nTam = 0;
        sQuery = "select * from cod_postal";
        if (getAD() == null){
            setAD(new AccesoDatos());
            getAD().conectar();
            rst = getAD().ejecutarConsulta(sQuery);
            getAD().desconectar();
            setAD(null);
        }
        else{
            rst = getAD().ejecutarConsulta(sQuery);
        }
        if (rst != null) {
            vObj = new Vector<CodigoPostal>();
            for (i = 0; i < rst.size(); i++) {
                oP = new CodigoPostal();
                Vector vRowTemp = (Vector)rst.elementAt(i); 
                oP.setCve((String) vRowTemp.elementAt(0));
                oP.setCveEdo((String) vRowTemp.elementAt(1));
                oP.setCveMun((String) vRowTemp.elementAt(2));
                oP.setCveLoc((String) vRowTemp.elementAt(3));
                listCodPost.add(oP);
            }
            nTam = vObj.size();
            arrCodPost = new CodigoPostal[nTam];
            for (i=0; i<nTam; i++){
                arrCodPost[i] = vObj.elementAt(i);
            }
        }
        return listCodPost;
    }
    public String getCve() {
        return sCve;
    }

    public void setCve(String nCve) {
        this.sCve = nCve;
    }

    public String getCveEdo() {
        return sCveEdo;
    }

    public void setCveEdo(String nCveEdo) {
        this.sCveEdo = nCveEdo;
    }

    public String getCveLoc() {
        return sCveLoc;
    }

    public void setCveLoc(String nCveLoc) {
        this.sCveLoc = nCveLoc;
    }

    public String getCveMun() {
        return sCveMun;
    }

    public void setCveMun(String nCveMun) {
        this.sCveMun = nCveMun;
    }

}
