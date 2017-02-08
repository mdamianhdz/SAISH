package org.apli.modelbeans.contabilidadInterna;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import org.apli.AD.AccesoDatos;

/**
 * Clase para la entidad Proveedor (recibe pagos del Sanatorio)
 * @author Lily_LnBnd
 */
public class Proveedor implements Serializable{
    private int nIdProv;
    private String sNombreRazSoc;
    private AccesoDatos oAD;
    
    public Proveedor(){
        
    }
    
    public List<Proveedor> buscaProveedores()throws Exception{
        List<Proveedor> listRet=null;
        Proveedor oProv;
        
        Vector rst = null;
        String sQuery = "";

        sQuery = "select * from buscaTodosProveedores();";
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
            listRet=new ArrayList();
            for (int i = 0; i < rst.size(); i++) {
                oProv = new Proveedor();
                Vector vRowTemp = (Vector)rst.elementAt(i);
                oProv.setIdProv(((Double) vRowTemp.elementAt(0)).intValue());
                oProv.setNombreRazSoc((String) vRowTemp.elementAt(1));               
                listRet.add(oProv);
            }
        }
        return listRet;
    }

    public String insertaProveedor() throws Exception {
        Vector rst = null;
        String sQuery = "", sRet="";

        if (this.sNombreRazSoc == null||
            this.sNombreRazSoc.equals("")) {
            throw new Exception("Proveedor.insertar: faltan datos");
        } else {
            sQuery = "select * from insertaproveedor('" + this.getNombreRazSoc() + "'::varchar);";
            System.out.println(sQuery);
            if (getAD() == null) {
                setAD(new AccesoDatos());
                getAD().conectar();
                rst = getAD().ejecutarConsulta(sQuery);
                getAD().desconectar();
                setAD(null);
            }
            if (rst != null){
                sRet = rst.get(0).toString().substring(1, rst.get(0).toString().length() - 1);
            }
        }
        return sRet; 
    }

    public String modificaProveedor() throws Exception {
        Vector rst = null;
        String sQuery = "", sRet="";

        if (this.nIdProv==0 ||
            this.sNombreRazSoc == null ||
            this.sNombreRazSoc.equals("")) {
            throw new Exception("Proveedor.modifica: faltan datos");
        } else {
            sQuery = "select * from modificaproveedor(" + this.getIdProv() + 
                    "::smallint,'" + this.getNombreRazSoc() + "'::varchar);";
            if (getAD() == null) {
                setAD(new AccesoDatos());
                getAD().conectar();
                rst = getAD().ejecutarConsulta(sQuery);
                getAD().desconectar();
                setAD(null);
            }
            if (rst != null)
                sRet = rst.get(0).toString().substring(1, rst.get(0).toString().length() - 1);
        }
        return sRet; 
    }

    public String eliminaProveedor() throws Exception {
        Vector rst = null;
        String sQuery = "", sRet = "";

        if (this.nIdProv==0) {
            throw new Exception("Proveedor.elimina: faltan datos");
        } else {
            sQuery = "select * from eliminaproveedor(" + this.getIdProv() + "::smallint);";
            System.out.println(sQuery);
            if (getAD() == null) {
                setAD(new AccesoDatos());
                getAD().conectar();
                rst = getAD().ejecutarConsulta(sQuery);
                getAD().desconectar();
                setAD(null);
            }
            if (rst != null)
                sRet = rst.get(0).toString().substring(1, rst.get(0).toString().length() - 1);
        }
        return sRet; 
    }

    public int getIdProv() {
        return nIdProv;
    }

    public void setIdProv(int nIdProv) {
        this.nIdProv = nIdProv;
    }

    public String getNombreRazSoc() {
        return sNombreRazSoc;
    }

    public void setNombreRazSoc(String sNombreRazSoc) {
        this.sNombreRazSoc = sNombreRazSoc;
    }

    public AccesoDatos getAD() {
        return oAD;
    }

    public void setAD(AccesoDatos oAD) {
        this.oAD = oAD;
    } 
}
