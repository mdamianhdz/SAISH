package org.apli.modelbeans.facturacion.cfdi.v32.schema;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import org.apli.AD.AccesoDatos;
/**
 * Clase para ABC de series fiscales
 * Autor: Isabel Espinoza Espinoza
 * Fecha: Abril 2014
 */
public class SerieFiscal implements Serializable{
    
    //Atributos
    private int nfolio=0;
    private String sNombre;
    private TipoDocumento oTipo;
    protected AccesoDatos oAD = null;
    
    //Contructores
    public SerieFiscal(){
    }
    public SerieFiscal(AccesoDatos oAD){
        this.oAD=oAD;
    }
    
    //Métodos
    public TipoDocumento getTipo() {
        return oTipo;
    }

      public void setTipo(TipoDocumento oTipo) {
        this.oTipo = oTipo;
    }
    public SerieFiscal buscarSerie() throws Exception{
        SerieFiscal oP=null;
        Vector rst = null;
        Vector<SerieFiscal> vObj = null;
        String sQuery = "";
        int i=0, nTam = 0;
        sQuery = " select * from buscallaveseriefiscal('"+this.sNombre+"'::character varying)";
        if (getAD() == null){
            setAD(new AccesoDatos());
            getAD().conectar();
        }
        rst = getAD().ejecutarConsulta(sQuery);
        getAD().desconectar();
        setAD(null);
        if (rst != null) {
            vObj = new Vector<SerieFiscal>();   
            oP = new SerieFiscal();
            Vector vRowTemp = (Vector)rst.elementAt(i);
            oP.setNombre((String) vRowTemp.elementAt(0));
            oP.setFolio(((Double) vRowTemp.elementAt(1)).intValue());
            oP.setTipo(TipoDocumento.fromString((String)vRowTemp.elementAt(2)));   
        }
        return this;
    }
    /**
    * Obtiene todas las series fiscales 
    */
    public List<SerieFiscal> getSeriesFiscalesPorDocumento(String tipo)throws Exception{
        SerieFiscal oP=null;
        Vector rst = null;
        Vector<SerieFiscal> vObj = null;
        List<SerieFiscal> listSeries=new ArrayList();
        String sQuery = "";
        int i=0;
        sQuery = "select * from seriefiscal where stipo='"+tipo+"'";
        if (getAD() == null){
            setAD(new AccesoDatos());
            getAD().conectar();
        }
        rst = getAD().ejecutarConsulta(sQuery);
        getAD().desconectar();
        setAD(null);
        if (rst != null) {
            vObj = new Vector<SerieFiscal>();
            for (i = 0; i < rst.size(); i++) {
                oP = new SerieFiscal();
                Vector vRowTemp = (Vector)rst.elementAt(i); 
                oP.setNombre((String) vRowTemp.elementAt(0));
                oP.setFolio(((Double) vRowTemp.elementAt(1)).intValue());
                oP.setTipo(TipoDocumento.fromString((String)vRowTemp.elementAt(2))); 
                listSeries.add(oP);
            }
        }
        return listSeries;
    }
    public List<SerieFiscal> getSeriesFiscales()throws Exception{
        SerieFiscal oP=null;
        Vector rst = null;
        Vector<SerieFiscal> vObj = null;
        List<SerieFiscal> listSeries=new ArrayList();
        String sQuery = "";
        int i=0;
        sQuery = "select * from buscaseriesfiscales()";
        if (getAD() == null){
            setAD(new AccesoDatos());
            getAD().conectar();
        }
        rst = getAD().ejecutarConsulta(sQuery);
        getAD().desconectar();
        setAD(null);
        if (rst != null) {
            vObj = new Vector<SerieFiscal>();
            for (i = 0; i < rst.size(); i++) {
                oP = new SerieFiscal();
                Vector vRowTemp = (Vector)rst.elementAt(i); 
                oP.setNombre((String) vRowTemp.elementAt(0));
                oP.setFolio(((Double) vRowTemp.elementAt(1)).intValue());
                oP.setTipo(TipoDocumento.fromString((String)vRowTemp.elementAt(2))); 
                listSeries.add(oP);
            }
        }
        return listSeries;
    }
    /*
     * Inserta una serie fiscal en la base de datos
     */
    public String insertarSerieFiscal() throws Exception{
        Vector rst = null;
        String sQuery = "";
        if (this.oTipo==null||
            this.sNombre.equals("")||this.sNombre==null ) {
            throw new Exception("Serie fiscal.insertar: error de programación, faltan datos");
	}else {
            sQuery = "select * from insertaseriefiscal('"+this.sNombre+"'::character varying,"+this.nfolio + "::int2, '" +"" + this.oTipo.getTipo()+ "'::character varying)";
            //Si oAD es nulo, tiene que crearlo y conectarlo, de otro modo
            //supone que ya viene conectado
            if (getAD() == null){
                setAD(new AccesoDatos());
		getAD().conectar();
            }
            rst = getAD().ejecutarConsulta(sQuery);
            getAD().desconectar();
            setAD(null);
	}
        return " "+rst.get(0);
    }
     /*
     * Modifica una serie fiscal en la base de datos
     */
    public String verificaPosibilidadEliminarActualizar() throws Exception{
        System.out.println("Entra a verifica "+sNombre);
        Vector rst = null;
        String sQuery = "";
        if (getAD() == null){
                setAD(new AccesoDatos());
		getAD().conectar();
        }
        sQuery = "select * from verificaserieeliminarcancelarse('"+this.sNombre+"'::character varying)";
        rst = getAD().ejecutarConsulta(sQuery);
        getAD().desconectar();
        setAD(null);
        return rst.toString();
    }
    public String modificarSerieFiscal() throws Exception{
        Vector rst = null;
        String sQuery = "";
        if (this.oTipo.getTipo().equals("")||
            this.sNombre.equals("") ) {
            throw new Exception("Serie fiscal.modificar: error de programación, faltan datos");
	}else {
            sQuery = "select * from modificaseriefiscal('"+this.sNombre+"'::character varying,"+this.nfolio+"::int2,'"+this.oTipo.getTipo()+"'::character varying)";
            if (getAD() == null){
                setAD(new AccesoDatos());
		getAD().conectar();
            }
            rst = getAD().ejecutarConsulta(sQuery);
            getAD().desconectar();
            setAD(null);
	}
        return " "+rst.get(0);
    }
     /*
     * Elimina una serie fiscal en la base de datos
     */
    public String eliminarSerieFiscal()throws Exception{
        Vector rst = null;
        String sQuery = "";
        if (this.sNombre.equals("")||this.sNombre==null){
            throw new Exception("Serie fiscal.eliminar: error de programación, faltan datos");
	}else {
            if (getAD() == null){
                setAD(new AccesoDatos());
		getAD().conectar();
            }
            sQuery = "select * from eliminaseriefiscal('"+this.sNombre+"'::character varying)";
            rst = getAD().ejecutarConsulta(sQuery);
            getAD().desconectar();
            setAD(null);
        }
        return rst.toString();
    }
    public AccesoDatos getAD() {
        return oAD;
    }
    public void setAD(AccesoDatos oAD) {
        this.oAD = oAD;
    }
    public int getFolio() {
        return nfolio;
    }
    public void setFolio(int folio) {
        this.nfolio = folio;
    }
    public String getNombre() {
        return sNombre;
    }
    public void setNombre(String nombre) {
        this.sNombre = nombre;
    }
}