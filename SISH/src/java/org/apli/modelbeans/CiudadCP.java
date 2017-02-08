/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apli.modelbeans;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import org.apli.AD.AccesoDatos;
/**
 *
 * @author Isa
 */
public class CiudadCP implements Serializable{
    private String sCveEdo;
    private String sCveMun;
    private String sCveCd;
    private String sCP;
    private String sDescEdo;
    private String sCvePais;
    private String sDescPais;
    private String sDescMun;
    private String sDescCd;
    CiudadCP oP=null;
    protected AccesoDatos oAD = null;
    
    public CiudadCP(){
        //oP=new CiudadCP();
    }
    public CiudadCP(AccesoDatos oAD){
        this.oAD=oAD;
    }
    public List<CiudadCP> getCiudades()throws Exception{
        CiudadCP oP1=null;
        Vector rst = null;
        Vector<CiudadCP> vObj = null;
        List<CiudadCP> lista=new ArrayList();
        String sQuery = "";
        int i=0;
        sQuery = "select * from buscaciudades()";
        if (getAD() == null){
            setAD(new AccesoDatos());
            getAD().conectar();
        }
        rst = getAD().ejecutarConsulta(sQuery);
        getAD().desconectar();
        setAD(null);
        if (rst != null) {
            vObj = new Vector<CiudadCP>();
            for (i = 0; i < rst.size(); i++) {
                Vector vRowTemp = (Vector)rst.elementAt(i); 
                oP1 = new CiudadCP();
                oP1.setCveEdo((String) vRowTemp.elementAt(0));
                oP1.setCveMun((String) vRowTemp.elementAt(1));
                oP1.setCveCd((String) vRowTemp.elementAt(2)); 
                oP1.setDescCd((String) vRowTemp.elementAt(3));
                lista.add(oP1);
            }
        }
        return lista;
    }
        public List<CiudadCP> getMunicipios()throws Exception{
        CiudadCP oP1=null;
        Vector rst = null;
        Vector<CiudadCP> vObj = null;
        List<CiudadCP> lista=new ArrayList();
        String sQuery = "";
        int i=0;
        sQuery = "select * from buscamunicipios()";
        if (getAD() == null){
            setAD(new AccesoDatos());
            getAD().conectar();
        }
        rst = getAD().ejecutarConsulta(sQuery);
        getAD().desconectar();
        setAD(null);
        if (rst != null) {
            vObj = new Vector<CiudadCP>();
            for (i = 0; i < rst.size(); i++) {
                oP1 = new CiudadCP();
                Vector vRowTemp = (Vector)rst.elementAt(i); 
                oP1.setCveEdo((String) vRowTemp.elementAt(0));
                oP1.setCveMun((String) vRowTemp.elementAt(1));
                oP1.setDescMun((String) vRowTemp.elementAt(2));
                lista.add(oP1);
            }
        }
        return lista;
    }
    public List<CiudadCP> getEstados()throws Exception{
        CiudadCP oP1=null;
        Vector rst = null;
        Vector<CiudadCP> vObj = null;
        List<CiudadCP> lista=new ArrayList();
        String sQuery = "";
        int i=0;
        sQuery = "select * from buscaestados()";
        if (getAD() == null){
            setAD(new AccesoDatos());
            getAD().conectar();
        }
        rst = getAD().ejecutarConsulta(sQuery);
        getAD().desconectar();
        setAD(null);
        if (rst != null) {
            vObj = new Vector<CiudadCP>();
            for (i = 0; i < rst.size(); i++) {
                oP1 = new CiudadCP();
                Vector vRowTemp = (Vector)rst.elementAt(i); 
                oP1.setCveEdo((String) vRowTemp.elementAt(0));
                oP1.setDescEdo((String) vRowTemp.elementAt(1));
                oP1.setCvePais((String) vRowTemp.elementAt(2));
                lista.add(oP1);
            }
        }
        return lista;
    }
    public List<CiudadCP> getCodPostales()throws Exception{
        CiudadCP oP1=null;
        Vector rst = null;
        Vector<CiudadCP> vObj = null;
        List<CiudadCP> lista=new ArrayList();
        String sQuery = "";
        int i=0;
        sQuery = "select * from buscaciudadescp()";
        if (getAD() == null){
            setAD(new AccesoDatos());
            getAD().conectar();
        }
        rst = getAD().ejecutarConsulta(sQuery);
        getAD().desconectar();
        setAD(null);
        if (rst != null) {
            vObj = new Vector<CiudadCP>();
            for (i = 0; i < rst.size(); i++) {
                oP1 = new CiudadCP();
                Vector vRowTemp = (Vector)rst.elementAt(i); 
                oP1.setCveEdo((String) vRowTemp.elementAt(0));
                oP1.setCveMun((String) vRowTemp.elementAt(1));
                oP1.setCveCd((String) vRowTemp.elementAt(2)); 
                oP1.setCP((String) vRowTemp.elementAt(3));
                lista.add(oP1);
            }
        }
        return lista;
    }
    public void actualizaPais() throws Exception{
        
        Vector rst = null;
        Vector<CiudadCP> vObj = null;
        List<CiudadCP> lista=new ArrayList();
        String sQuery = "";
        int i=0;
        sQuery = "select * from buscapais('"+this.sCvePais+"'::character)";
        if (getAD() == null){
            setAD(new AccesoDatos());
            getAD().conectar();
        }
        rst = getAD().ejecutarConsulta(sQuery);
        getAD().desconectar();
        setAD(null);
        if (rst != null) {
            vObj = new Vector<CiudadCP>();
            oP = new CiudadCP();
            Vector vRowTemp = (Vector)rst.elementAt(i); 
            oP.setDescPais((String) vRowTemp.elementAt(0));
        }
    }
    
    public List<Estado> buscaPorCP(String scp)throws Exception{
        List<Estado> listRet=null;
        Estado oEdo;
        
        Vector rst;
        String sQuery = "";
        
        sQuery = "select * from buscaPorCP('"+scp+"')";
        if (getAD() == null){
            setAD(new AccesoDatos());
            getAD().conectar();
        }
        rst = getAD().ejecutarConsulta(sQuery);
        getAD().desconectar();
        setAD(null);
        
        if (rst != null) {
            listRet = new ArrayList();
            for (int i = 0; i < rst.size(); i++) {
                Vector vRowTemp = (Vector)rst.elementAt(i); 
                if (i==0){
                    oEdo=new Estado();
                    oEdo.setCve((String) vRowTemp.elementAt(0));
                    oEdo.setDescrip((String) vRowTemp.elementAt(1));
                    listRet.add(oEdo);
                }else{
                    if (!((Estado)listRet.get(listRet.size()-1)).getCve().equals((String)vRowTemp.elementAt(0))){
                        oEdo=new Estado();
                        oEdo.setCve((String) vRowTemp.elementAt(0));
                        oEdo.setDescrip((String) vRowTemp.elementAt(1));
                        listRet.add(oEdo);
                    }
                }
            }
        }
        return listRet;
    }
    
    public Pais buscaPais(String scp)throws Exception{
        Pais oPais=new Pais();
        Vector rst;
        String sQuery = "";
        sQuery = "select * from buscaPais('"+scp+"')";
        if (getAD() == null){
            setAD(new AccesoDatos());
            getAD().conectar();
        }
        rst = getAD().ejecutarConsulta(sQuery);
        getAD().desconectar();
        setAD(null);
        
        if (rst != null && rst.size()>0) {
            Vector vRowTemp = (Vector)rst.elementAt(0); 
            oPais.setCve((String) vRowTemp.elementAt(0));
            oPais.setDescrip((String) vRowTemp.elementAt(1));
        }
        return oPais;
    }
    
    public List<Municipio> buscaPorEdo(String scp, String scveedo)throws Exception{
        List<Municipio> listRet=null;
        Municipio oMun;
        
        Vector rst;
        String sQuery = "";
        
        sQuery = "select * from buscaPorEdo('"+scp+"','"+scveedo+"')";
        if (getAD() == null){
            setAD(new AccesoDatos());
            getAD().conectar();
        }
        rst = getAD().ejecutarConsulta(sQuery);
        getAD().desconectar();
        setAD(null);
        
        if (rst != null) {
            listRet = new ArrayList();
            for (int i = 0; i < rst.size(); i++) {
                Vector vRowTemp = (Vector)rst.elementAt(i); 
                if (i==0){
                    oMun=new Municipio();
                    oMun.setCve((String) vRowTemp.elementAt(0));
                    oMun.setDescrip((String) vRowTemp.elementAt(1));
                    listRet.add(oMun);
                }else{
                    if (!((Municipio)listRet.get(listRet.size()-1)).getCve().equals((String)vRowTemp.elementAt(0))){
                        oMun=new Municipio();
                        oMun.setCve((String) vRowTemp.elementAt(0));
                        oMun.setDescrip((String) vRowTemp.elementAt(1));
                        listRet.add(oMun);
                    }
                }
            }
        }
        return listRet;
    }
    public List<Municipio> buscaPorEdo(String scveedo)throws Exception{
        List<Municipio> listRet=null;
        Municipio oMun;
        
        Vector rst;
        String sQuery = "";
        
        sQuery = "select * from buscaPorEdo('"+scveedo+"')";
        if (getAD() == null){
            setAD(new AccesoDatos());
            getAD().conectar();
        }
        rst = getAD().ejecutarConsulta(sQuery);
        getAD().desconectar();
        setAD(null);
        
        if (rst != null) {
            listRet = new ArrayList();
            for (int i = 0; i < rst.size(); i++) {
                Vector vRowTemp = (Vector)rst.elementAt(i); 
                if (i==0){
                    oMun=new Municipio();
                    oMun.setCve((String) vRowTemp.elementAt(0));
                    oMun.setDescrip((String) vRowTemp.elementAt(1));
                    listRet.add(oMun);
                }else{
                    if (!((Municipio)listRet.get(listRet.size()-1)).getCve().equals((String)vRowTemp.elementAt(0))){
                        oMun=new Municipio();
                        oMun.setCve((String) vRowTemp.elementAt(0));
                        oMun.setDescrip((String) vRowTemp.elementAt(1));
                        listRet.add(oMun);
                    }
                }
            }
        }
        return listRet;
    }
    
    public List<Ciudad> buscaPorMun(String scp, String scveedo, String scvemun)throws Exception{
        List<Ciudad> listRet=null;
        Ciudad oCd;
        Vector rst;
        String sQuery = "";
        sQuery = "select * from buscaPorMun('"+scp+"','"+scveedo+"','"+scvemun+"')";
        if (getAD() == null){
            setAD(new AccesoDatos());
            getAD().conectar();
        }
        rst = getAD().ejecutarConsulta(sQuery);
        getAD().desconectar();
        setAD(null);
        
        if (rst != null) {
            listRet = new ArrayList();
            for (int i = 0; i < rst.size(); i++) {
                Vector vRowTemp = (Vector)rst.elementAt(i); 
                if (i==0){
                    oCd=new Ciudad();
                    oCd.setCve((String) vRowTemp.elementAt(0));
                    oCd.setDescrip((String) vRowTemp.elementAt(1));
                    listRet.add(oCd);
                }else{
                    if (!((Ciudad)listRet.get(listRet.size()-1)).getCve().equals((String)vRowTemp.elementAt(0))){
                        oCd=new Ciudad();
                        oCd.setCve((String) vRowTemp.elementAt(0));
                        oCd.setDescrip((String) vRowTemp.elementAt(1));
                        listRet.add(oCd);
                    }
                }
            }
        }
        return listRet;
    }
    public List<Ciudad> buscaPorMun(String scveedo, String scvemun)throws Exception{
        List<Ciudad> listRet=null;
        Ciudad oCd;
        Vector rst;
        String sQuery = "";
        sQuery = "select * from buscaPorMun('"+scveedo+"','"+scvemun+"')";
        if (getAD() == null){
            setAD(new AccesoDatos());
            getAD().conectar();
        }
        rst = getAD().ejecutarConsulta(sQuery);
        getAD().desconectar();
        setAD(null);
        
        if (rst != null) {
            listRet = new ArrayList();
            for (int i = 0; i < rst.size(); i++) {
                Vector vRowTemp = (Vector)rst.elementAt(i); 
                if (i==0){
                    oCd=new Ciudad();
                    oCd.setCve((String) vRowTemp.elementAt(0));
                    oCd.setDescrip((String) vRowTemp.elementAt(1));
                    listRet.add(oCd);
                }else{
                    if (!((Ciudad)listRet.get(listRet.size()-1)).getCve().equals((String)vRowTemp.elementAt(0))){
                        oCd=new Ciudad();
                        oCd.setCve((String) vRowTemp.elementAt(0));
                        oCd.setDescrip((String) vRowTemp.elementAt(1));
                        listRet.add(oCd);
                    }
                }
            }
        }
        return listRet;
    }

    public void buscaDatosPorCP() throws Exception {
        Vector rst = null;
        String sQuery = "";

        if (this.sCP.equals("")) {
            throw new Exception("CiudadCP.buscaCiudad: error de programación, faltan datos");
        } else {
            sQuery = "select * from buscacdporcp('" + this.sCP + "')";
            if (this.getAD() == null) {
                this.setAD(new AccesoDatos());
                this.getAD().conectar();
                rst = this.getAD().ejecutarConsulta(sQuery);
                this.getAD().desconectar();
                setAD(null);
            } else {
                rst = this.getAD().ejecutarConsulta(sQuery);
            }

            if (rst != null && rst.size() >= 1) {
                Vector vRowTemp = (Vector) rst.elementAt(0);
                this.sCveEdo = ((String) vRowTemp.elementAt(0));
                this.sDescEdo = ((String) vRowTemp.elementAt(1));
                this.sCveMun = ((String) vRowTemp.elementAt(2));
                this.sDescMun = ((String) vRowTemp.elementAt(3));
                this.sCveCd = ((String) vRowTemp.elementAt(4));
                this.sDescCd = ((String) vRowTemp.elementAt(5));
            }
        }
    }
    
    
    public String getCvePais() {
        return sCvePais;
    }

    public void setCvePais(String sCvePais) {
        this.sCvePais = sCvePais;
    }

    public String getDescCd() {
        return sDescCd;
    }

    public void setDescCd(String sDescCd) {
        this.sDescCd = sDescCd;
    }

    public String getDescEdo() {
        return sDescEdo;
    }

    public void setDescEdo(String sDescEdo) {
        this.sDescEdo = sDescEdo;
    }

    public String getDescMun() {
        return sDescMun;
    }

    public void setDescMun(String sDescMun) {
        this.sDescMun = sDescMun;
    }

    public String getDescPais() {
        return sDescPais;
    }

    public void setDescPais(String sDescPais) {
        this.sDescPais = sDescPais;
    }
    
    public String getCP() {
        return sCP;
    }

    public void setCP(String sCP) {
        this.sCP = sCP;
    }

    public String getCveCd() {
        return sCveCd;
    }

    public void setCveCd(String sCveCd) {
        this.sCveCd = sCveCd;
    }

    public String getCveEdo() {
        return sCveEdo;
    }

    public void setCveEdo(String sCveEdo) {
        this.sCveEdo = sCveEdo;
    }

    public String getCveMun() {
        return sCveMun;
    }

    public void setCveMun(String sCveMun) {
        this.sCveMun = sCveMun;
    }
    public AccesoDatos getAD() {
        return oAD;
    }
    public void setAD(AccesoDatos oAD) {
        this.oAD = oAD;
    }
}
