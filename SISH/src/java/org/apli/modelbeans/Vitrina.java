package org.apli.modelbeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import org.apli.AD.AccesoDatos;

/**
 *
 * @author MiguelAngel
 */
public class Vitrina implements Serializable {

    private short nCveVitrina;
    private String sDescripcion;
    private AreaFisica areaFisica;
    private boolean bActiva;
    protected AccesoDatos oAD = null;

    public Vitrina() {
        areaFisica=new AreaFisica();
    }

    public Vitrina(AccesoDatos poAD) {
        oAD = poAD;
    }

    public Vitrina[] buscaTodos() throws Exception {
        Vitrina arrRet[] = null, oVit = null;
        Vector rst = null;
        Vector<Vitrina> vObj = null;
        String sQuery = "";
        int i = 0, nTam = 0;

        sQuery = "SELECT * FROM buscaTodosVitrina()";
        if (getAD() == null) {
            setAD(new AccesoDatos());
            getAD().conectar();
            rst = getAD().ejecutarConsulta(sQuery);
            getAD().desconectar();
            setAD(null);
        } else {
            rst = getAD().ejecutarConsulta(sQuery);
        }

        if (rst != null) {
            vObj = new Vector<Vitrina>();
            for (i = 0; i < rst.size(); i++) {
                oVit = new Vitrina();
                Vector vRowTemp = (Vector) rst.elementAt(i);
                oVit.setCveVitrina(((Double) vRowTemp.elementAt(0)).shortValue());
                oVit.setDescripcion((String) vRowTemp.elementAt(1));
                vObj.add(oVit);
            }
            nTam = vObj.size();
            arrRet = new Vitrina[nTam];

            for (i = 0; i < nTam; i++) {
                arrRet[i] = vObj.elementAt(i);
            }
        }
        return arrRet;
    }

    public Vitrina buscaVitrina(Short cveVitrina) throws Exception {
        Vitrina v = new Vitrina();
        Vector rst = null;
        String sQuery = "";

        if (cveVitrina == 0) {
            throw new Exception("Vitrina.buscaVitrina: error de programación, faltan datos");
        } else {
            sQuery = "select * from buscaLlaveVitrina(" + cveVitrina + "::smallint)";
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
                v.setCveVitrina(((Double) vRowTemp.elementAt(0)).shortValue());
                v.setDescripcion((String) vRowTemp.elementAt(1));
            }
        }
        return v;
    }
    
    public List<Vitrina> busca(String sDescripcion, int nAreaFisica) throws Exception{
        List<Vitrina> listRet=null;
        Vitrina oVitrina;
        AreaFisica oAreaF;
        
        Vector rst = null;
        String sQuery = "";

        sQuery="select * from buscaVitrinas('"+sDescripcion+"',"+nAreaFisica+"::int2);";
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
               
        if (rst != null) {
            listRet = new ArrayList();
            for (int i = 0; i < rst.size(); i++) {
                oVitrina=new Vitrina();
                oAreaF=new AreaFisica();
                Vector vRowTemp = (Vector)rst.elementAt(i);
                oVitrina.setCveVitrina(((Double)vRowTemp.elementAt(0)).shortValue());
                oVitrina.setDescripcion((String)vRowTemp.elementAt(1));
                oAreaF.setCve(((Double)vRowTemp.elementAt(2)).intValue());
                oAreaF.setDescrip((String)vRowTemp.elementAt(3));
                oVitrina.setActiva(cambia(((String)vRowTemp.elementAt(4)).charAt(0)));
                oVitrina.setAreaFisica(oAreaF);
                listRet.add(oVitrina);
            }
        }
        return listRet;
    }
    
    public String insertaVitrina(Vitrina oVit) throws Exception{
        Vector rst = null;
        String sQuery = "";

        sQuery="select * from insertaVitrina('"+oVit.getDescripcion()+"',"+oVit.getAreaFisica().getCve()+"::int2);";   
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
        return ""+rst.get(0).toString().substring(1, rst.get(0).toString().length()-1);
    }
    
    public String modifica(Vitrina oVit)throws Exception{
        Vector rst = null;
        String sQuery = "";

        sQuery="select * from modificavitrina("+oVit.getCveVitrina()+"::int2,"+oVit.getAreaFisica().getCve()+"::int2);"; 
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
        return ""+rst.get(0).toString().substring(1,rst.get(0).toString().length()-1);
    }
    
    public String modificaEstado(Vitrina oVit)throws Exception{
        Vector rst = null;
        String sQuery = "";

        sQuery="select * from cambiaestadovitrina("+oVit.getCveVitrina()+"::int2,'"+cambia(oVit.isActiva())+"');"; 
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
        return ""+rst.get(0).toString().substring(1,rst.get(0).toString().length()-1);
    }
    
    public boolean cambia(char val){
        if (val=='1')
            return true;
        else 
            return false;
    }
    
    public char cambia(boolean val){
        if (val==true)
            return '0';
        else
            return '1';
    }

    public short getCveVitrina() {
        return nCveVitrina;
    }

    public void setCveVitrina(short nCveVitrina) {
        this.nCveVitrina = nCveVitrina;
    }

    public String getDescripcion() {
        return sDescripcion;
    }

    public void setDescripcion(String sDescripcion) {
        this.sDescripcion = sDescripcion;
    }

    public AreaFisica getAreaFisica() {
        return areaFisica;
    }

    public void setAreaFisica(AreaFisica areaFisica) {
        this.areaFisica = areaFisica;
    }
    
    public boolean isActiva(){
        return bActiva;
    }
    
    public void setActiva(boolean bActiva){
        this.bActiva=bActiva;
    }

    public AccesoDatos getAD() {
        return oAD;
    }

    public void setAD(AccesoDatos oAD) {
        this.oAD = oAD;
    }
}
