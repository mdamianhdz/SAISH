package org.apli.modelbeans;

import java.io.Serializable;
import org.apli.AD.AccesoDatos;
import java.util.Vector;

public class Especialidad implements Serializable{

private int sCve;
private String sDescrip="";
/* Variable para el acceso a datos*/
protected AccesoDatos oAD = null;

    public Especialidad(){
    }

    public Especialidad(AccesoDatos poAD){
        oAD = poAD;
    }

    public Especialidad(int scve){
         this.sCve=scve;
    }

    public Especialidad(int scve, String sdescrip){
            this.sCve=scve;
            this.sDescrip=sdescrip;
        }

    /**
     * Busca la lista de todas las especialidades registrados,
     * Regresa un arreglo de especialidades
     **/
    public Especialidad[] buscarTodasEspecialidades()throws Exception{
        Especialidad[] arrRet=null;
        Especialidad oEsp=null;
        Vector rst = null;
        Vector<Especialidad> vObj = null;
        String sQuery = "";
        int i=0, nTam = 0;
        sQuery ="select * from buscatodosespecialidad()";
        /*Si oAD es nulo, tiene que crearlo y conectarlo, de otro modo
        supone que ya viene conectado*/
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
            vObj = new Vector<Especialidad>();

            for (i = 0; i < rst.size(); i++) {
                oEsp = new Especialidad();
                Vector vRowTemp = (Vector)rst.elementAt(i);
                oEsp.setCve(((Double) vRowTemp.elementAt(0)).intValue());
                oEsp.setDescrip((String) vRowTemp.elementAt(1));
                vObj.add(oEsp);
            }
            nTam = vObj.size();
            arrRet = new Especialidad[nTam];

            for (i=0; i<nTam; i++){
                arrRet[i] = vObj.elementAt(i);
            }
        }
        return arrRet;
    }

    

    public String insertaEspecialidad(Especialidad oEsp) throws Exception {
    Vector rst = null;
    String sQuery = "";

    if (oEsp == null) {
        throw new Exception("");
    } else {
        sQuery = "select * from insertaespecialidad(" + oEsp.getCve() + "::smallint,'" + oEsp.getDescrip() + "'::varchar);";
        System.out.println(sQuery);
        if (getAD() == null) {
            setAD(new AccesoDatos());
            getAD().conectar();
            rst = getAD().ejecutarConsulta(sQuery);
            getAD().desconectar();
            setAD(null);
        }
    }
    return "" + rst.get(0).toString().substring(1, rst.get(0).toString().length() - 1);
}

    public String modificaEspecialidad(Especialidad oEsp) throws Exception {
        Vector rst = null;
        String sQuery = "";

        if (oEsp == null) {
            throw new Exception("");
        } else {
            sQuery = "select * from modificaespecialidad(" + oEsp.getCve()+ "::smallint,'" + oEsp.getDescrip() + "'::varchar);";
            System.out.println(sQuery);
            if (getAD() == null) {
                setAD(new AccesoDatos());
                getAD().conectar();
                rst = getAD().ejecutarConsulta(sQuery);
                getAD().desconectar();
                setAD(null);
            }
        }
        return "" + rst.get(0).toString().substring(1, rst.get(0).toString().length() - 1);
    }

    public String eliminaEspecialidad(Especialidad oEsp) throws Exception {
        Vector rst = null;
        String sQuery = "";

        if (oEsp == null) {
            throw new Exception("");
        } else {
            sQuery = "select * from eliminaespecialidad(" + oEsp.getCve() + "::smallint);";
            System.out.println(sQuery);
            if (getAD() == null) {
                setAD(new AccesoDatos());
                getAD().conectar();
                rst = getAD().ejecutarConsulta(sQuery);
                getAD().desconectar();
                setAD(null);
            }
        }
        return "" + rst.get(0).toString().substring(1, rst.get(0).toString().length() - 1);
    }

    //=============== SET & GET ===============//
    public int getCve() {
        return sCve;
    }
    public void setCve(int sCve) {
        this.sCve = sCve;
    }
    public String getDescrip() {
        return sDescrip;
    }
    public void setDescrip(String sDescrip) {
        this.sDescrip = sDescrip;
    }
    public AccesoDatos getAD() {
        return oAD;
    }
    public void setAD(AccesoDatos oAD) {
        this.oAD = oAD;
    }

}