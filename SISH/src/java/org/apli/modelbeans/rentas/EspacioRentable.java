package org.apli.modelbeans.rentas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import org.apli.AD.AccesoDatos;
import org.apli.modelbeans.facturacion.cfdi.v32.schema.Comprobante.Emisor;

/**
 *
 * @author Lily_LnBnd
 */
public class EspacioRentable implements Serializable{
    private int nIdEspacio;
    private TipoEspacio oTipoEspacio;
    private String sDescripcion;
    private String sUbicacion;
    private float nRentaMensual;
    private float nRentaAnual;
    private float nDeposito;
    private Emisor oArrendador;
    private boolean bRentado;//0=No=false; 1=Si=true
    private boolean bActivo;//0=Inactivo=false; 1=Activo=true
    private ContratoRenta oContratoRenta;
    private List<CondRenta> listCondiciones;
    private AccesoDatos oAD;
    
    public EspacioRentable(){
        oArrendador=new Emisor();
        oTipoEspacio=new TipoEspacio();
        oContratoRenta=new ContratoRenta();
    }
    
    public List<EspacioRentable> buscaInfoEspacios(String sCondicion) throws Exception{
        List<EspacioRentable> listRet=null;
        EspacioRentable oEsp;
        Vector rst;
        
        String sQuery="select * from buscaInfoEspacios()";
        if (sCondicion.length()>0)
            sQuery=sQuery+"where "+sCondicion;
        System.out.println(sQuery);
        if (getAD()==null){
            setAD(new AccesoDatos());
            getAD().conectar();
        }
        rst=getAD().ejecutarConsulta(sQuery);
        getAD().desconectar();
        setAD(null);
        
        if(rst!=null){
            listRet=new ArrayList();
            for (int i = 0; i < rst.size(); i++) {
                Vector vRowTemp=(Vector) rst.get(i);
                if (i==0) {
                    oEsp=new EspacioRentable();
                    oEsp.setIdEspacio(((Double)vRowTemp.get(0)).intValue());
                    oEsp.getTipoEspacio().setIdTipoEsp(((Double)vRowTemp.get(1)).intValue());
                    oEsp.getTipoEspacio().setDescripcion((String)vRowTemp.get(2));
                    oEsp.setDescripcion((String)vRowTemp.get(3));
                    oEsp.setUbicacion((String)vRowTemp.get(4));
                    oEsp.setRentaAnual(((Double)vRowTemp.get(5)).floatValue());
                    oEsp.setRentaMensual(((Double)vRowTemp.get(6)).floatValue());
                    oEsp.setDeposito(((Double)vRowTemp.get(7)).floatValue());
                    oEsp.getArrendador().setRfc((String)vRowTemp.get(8));
                    oEsp.setRentado(cambiaBoolean(((String)vRowTemp.get(9)).charAt(0)));
                    oEsp.setActivo(cambiaBoolean(((String)vRowTemp.get(10)).charAt(0)));
                    oEsp.getContratoRenta().setIdContratoRenta(((Double)vRowTemp.get(11)).intValue());
                    oEsp.getContratoRenta().getReceptor().setRfc((String)vRowTemp.get(12));
                    oEsp.getContratoRenta().getReceptor().setNombre((String)vRowTemp.get(13));
                    oEsp.getContratoRenta().setRentaMensual(((Double)vRowTemp.get(14)).floatValue());
                    oEsp.getContratoRenta().setDeposito(((Double)vRowTemp.get(15)).floatValue());
                    oEsp.getContratoRenta().setInicio((Date)vRowTemp.get(16));
                    oEsp.getContratoRenta().setFin((Date)vRowTemp.get(17));
                    if (((String)vRowTemp.get(18)).length()>0)
                        oEsp.getContratoRenta().setCancelado(cambiaBoolean(((String)vRowTemp.get(18)).charAt(0)));
                    oEsp.getContratoRenta().setRazonCancelado((String)vRowTemp.get(19));

                    listRet.add(oEsp);
                }else{
                    if ((listRet.get(listRet.size()-1)).getIdEspacio()!=((Double)vRowTemp.elementAt(0)).intValue()){
                        oEsp=new EspacioRentable();
                        oEsp.setIdEspacio(((Double)vRowTemp.get(0)).intValue());
                        oEsp.getTipoEspacio().setIdTipoEsp(((Double)vRowTemp.get(1)).intValue());
                        oEsp.getTipoEspacio().setDescripcion((String)vRowTemp.get(2));
                        oEsp.setDescripcion((String)vRowTemp.get(3));
                        oEsp.setUbicacion((String)vRowTemp.get(4));
                        oEsp.setRentaAnual(((Double)vRowTemp.get(5)).floatValue());
                        oEsp.setRentaMensual(((Double)vRowTemp.get(6)).floatValue());
                        oEsp.setDeposito(((Double)vRowTemp.get(7)).floatValue());
                        oEsp.getArrendador().setRfc((String)vRowTemp.get(8));
                        oEsp.setRentado(cambiaBoolean(((String)vRowTemp.get(9)).charAt(0)));
                        oEsp.setActivo(cambiaBoolean(((String)vRowTemp.get(10)).charAt(0)));
                        oEsp.getContratoRenta().setIdContratoRenta(((Double)vRowTemp.get(11)).intValue());
                        oEsp.getContratoRenta().getReceptor().setRfc((String)vRowTemp.get(12));
                        oEsp.getContratoRenta().getReceptor().setNombre((String)vRowTemp.get(13));
                        oEsp.getContratoRenta().setRentaMensual(((Double)vRowTemp.get(14)).floatValue());
                        oEsp.getContratoRenta().setDeposito(((Double)vRowTemp.get(15)).floatValue());
                        oEsp.getContratoRenta().setInicio((Date)vRowTemp.get(16));
                        oEsp.getContratoRenta().setFin((Date)vRowTemp.get(17));
                        if (((String)vRowTemp.get(18)).length()>0)
                            oEsp.getContratoRenta().setCancelado(cambiaBoolean(((String)vRowTemp.get(18)).charAt(0)));
                        oEsp.getContratoRenta().setRazonCancelado((String)vRowTemp.get(19));

                        listRet.add(oEsp);
                    }
                }
            }
        }
        return listRet;
    }
    
    public List<EspacioRentable> buscaEspacios(String sCondicion) throws Exception{
        List<EspacioRentable> listRet=null;
        EspacioRentable oEsp;
        Vector rst;
        
        String sQuery="select * from buscaEspacios()";
        if (sCondicion.length()>0)
            sQuery=sQuery+"where "+sCondicion;
        System.out.println(sQuery);
        if (getAD()==null){
            setAD(new AccesoDatos());
            getAD().conectar();
        }
        rst=getAD().ejecutarConsulta(sQuery);
        getAD().desconectar();
        setAD(null);
        
        if(rst!=null){
            listRet=new ArrayList();
            for (int i = 0; i < rst.size(); i++) {
                Vector vRowTemp=(Vector) rst.get(i);
                oEsp=new EspacioRentable();
                oEsp.setIdEspacio(((Double)vRowTemp.get(0)).intValue());
                oEsp.getTipoEspacio().setIdTipoEsp(((Double)vRowTemp.get(1)).intValue());
                oEsp.getTipoEspacio().setDescripcion((String)vRowTemp.get(2));
                oEsp.setDescripcion((String)vRowTemp.get(3));
                oEsp.setUbicacion((String)vRowTemp.get(4));
                oEsp.setRentaAnual(((Double)vRowTemp.get(5)).floatValue());
                oEsp.setRentaMensual(((Double)vRowTemp.get(6)).floatValue());
                oEsp.setDeposito(((Double)vRowTemp.get(7)).floatValue());
                oEsp.getArrendador().setRfc((String)vRowTemp.get(8));
                oEsp.setRentado(cambiaBoolean(((String)vRowTemp.get(9)).charAt(0)));
                oEsp.setActivo(cambiaBoolean(((String)vRowTemp.get(10)).charAt(0)));
                oEsp.setListCondiciones(new CondRenta().buscaCondicionesRenta(oEsp.getIdEspacio()));

                listRet.add(oEsp);
            }
        }
        return listRet;
    }
    
    public List<EspacioRentable> buscaTodosEspacios() throws Exception{
        List<EspacioRentable> listRet=null;
        EspacioRentable oEsp;
        Vector rst;
        
        String sQuery="select * from buscaEspacios()";
        if (getAD()==null){
            setAD(new AccesoDatos());
            getAD().conectar();
        }
        rst=getAD().ejecutarConsulta(sQuery);
        getAD().desconectar();
        setAD(null);
        
        if(rst!=null){
            listRet=new ArrayList();
            for (int i = 0; i < rst.size(); i++) {
                Vector vRowTemp=(Vector) rst.get(i);
                oEsp=new EspacioRentable();
                oEsp.setIdEspacio(((Double)vRowTemp.get(0)).intValue());
                oEsp.getTipoEspacio().setIdTipoEsp(((Double)vRowTemp.get(1)).intValue());
                oEsp.getTipoEspacio().setDescripcion((String)vRowTemp.get(2));
                oEsp.setDescripcion((String)vRowTemp.get(3));
                oEsp.setUbicacion((String)vRowTemp.get(4));
                oEsp.setRentaAnual(((Double)vRowTemp.get(5)).floatValue());
                oEsp.setRentaMensual(((Double)vRowTemp.get(6)).floatValue());
                oEsp.setDeposito(((Double)vRowTemp.get(7)).floatValue());
                oEsp.getArrendador().setRfc((String)vRowTemp.get(8));
                oEsp.setRentado(cambiaBoolean(((String)vRowTemp.get(9)).charAt(0)));
                oEsp.setActivo(cambiaBoolean(((String)vRowTemp.get(10)).charAt(0)));
                oEsp.setListCondiciones(new CondRenta().buscaCondicionesRenta(oEsp.getIdEspacio()));

                listRet.add(oEsp);
            }
        }
        return listRet;
    }
    
    public String rentar(EspacioRentable oEspacio)throws Exception{
        Vector rst = null;
        String sQuery = "", sRet="";
        
        if (oEspacio==null){
             throw new Exception("EspacioRentable.rentar: error de programación, faltan datos");
        }else{
            sQuery="select * from rentarEspacio("+oEspacio.getIdEspacio()+"::int2, '"+oEspacio.getContratoRenta().getReceptor().getRfc()+"', '"+
                    oEspacio.getContratoRenta().getReceptor().getNombre()+"', "+oEspacio.getContratoRenta().getRentaMensual()+", '"+
                    oEspacio.getContratoRenta().getInicio()+"', '"+oEspacio.getContratoRenta().getFin()+
                    "', '', '', "+oEspacio.getContratoRenta().getDeposito()+");";
            for (int i = 0; i < oEspacio.getContratoRenta().getListPagos().size(); i++) {
                sQuery=sQuery+"\n select * from insertaPagoRenta("+oEspacio.getContratoRenta().getListPagos().get(i).getMensualidad()+
                        "::int2,'"+oEspacio.getContratoRenta().getListPagos().get(i).getProgramada() +"');";
            }
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
        }
        if ("Multiple ResultSets were returned by the query".equals(rst.get(0).toString().substring(1, rst.get(0).toString().length() - 1)))
            sRet="Se ha registrado la renta del espacio exitosamente";
        else
            sRet="Se produjo un error mientras se almacenaban los datos. Vuelva a intentarlo.";
        return sRet;
    }
    
    public String cancelar(EspacioRentable oEspacio)throws Exception{
        Vector rst = null;
        String sQuery = "";
        
        if (oEspacio==null){
             throw new Exception("Gasto.guarda: error de programación, faltan datos");
        }else{
            sQuery="select * from cancelarContrato("+oEspacio.getIdEspacio()+"::int2, "+oEspacio.getContratoRenta().getIdContratoRenta()+
                    ",'"+oEspacio.getContratoRenta().getRazonCancelado()+"');";
            
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
        }
        return ""+rst.get(0).toString().substring(1, rst.get(0).toString().length() - 1);
    }
    
    public String cambiarEstado(int nIdEspacio)throws Exception{
        Vector rst = null;
        String sQuery = "";
        
        if (nIdEspacio==0){
             throw new Exception("EspacioRentable.cambiarEstado: error de programación, faltan datos");
        }else{
            sQuery="select * from cambiarEstadoEspacio("+nIdEspacio+"::int2);";
            
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
        }
        return ""+rst.get(0).toString().substring(1, rst.get(0).toString().length() - 1);
    }
    
    public Date buscaFechaFin(int nIdEspacio)throws Exception{
        Vector rst = null;
        String sQuery = "";
        Date dRet=null;
        
        if (nIdEspacio==0){
             throw new Exception("EspacioRentable.buscaFechaFin: error de programación, faltan datos");
        }else{
            sQuery="select * from buscaFechaFinContrato("+nIdEspacio+"::int2);";
            
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
            
            if (rst!=null){
                Vector vRowTemp=(Vector) rst.get(0);
                dRet=((Date)vRowTemp.get(0));
            }   
        }
        return dRet;
    }
    
    public String guardarEspacioRentable(EspacioRentable oEspacio)throws Exception{
        Vector rst = null;
        String sQuery = "", sRet="";
        
        if (oEspacio==null){
             throw new Exception("EspacioRentable.guardarEspacioRentable: error de programación, faltan datos");
        }else{
            sQuery="select * from almacenaEspacioRentable("+
                    oEspacio.getIdEspacio()+"::int2, "+
                    oEspacio.getTipoEspacio().getIdTipoEsp()+"::int2, '"+
                    oEspacio.getDescripcion()+"', '"+
                    oEspacio.getUbicacion()+"', "+
                    oEspacio.getRentaAnual()+", "+
                    oEspacio.getRentaMensual()+", "+
                    oEspacio.getDeposito()+", "+
                    (oEspacio.getArrendador()==null||
                    oEspacio.getArrendador().getRfc().equals("")?"null":
                    "'"+oEspacio.getArrendador().getRfc()+"'")+", '"+
                    cambiaBoolean(oEspacio.isRentado())+ "', '"+
                    cambiaBoolean(oEspacio.isActivo())+"', ARRAY[";
            if (oEspacio.getListCondiciones()!= null &&
                oEspacio.getListCondiciones().size()>0){
                for (int i = 0; i < oEspacio.getListCondiciones().size(); i++) {
                    sQuery=sQuery+"'"+oEspacio.getListCondiciones().get(i).getDescripcion() +"',";
                }
                sQuery = sQuery.substring(0, sQuery.length()-1);
            }
            sQuery = sQuery + "]::varchar[]);";
            
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
            if (rst==null)
                sRet = "Error al guardar el espacio rentable";
            else
                sRet = (String)((Vector)rst.get(0)).get(0);
        }
        return sRet;
    }
    
    public boolean cambiaBoolean(char cVar){
        if (cVar=='0')
            return false;
        else 
            return true;
    }
    
    public char cambiaBoolean(boolean bVar){
        if (bVar==true)
            return '1';
        else 
            return '0';
    }

    public int getIdEspacio() {
        return nIdEspacio;
    }

    public void setIdEspacio(int nIdEspacio) {
        this.nIdEspacio = nIdEspacio;
    }

    public TipoEspacio getTipoEspacio() {
        return oTipoEspacio;
    }

    public void setTipoEspacio(TipoEspacio oTipoEspacio) {
        this.oTipoEspacio = oTipoEspacio;
    }

    public String getDescripcion() {
        return sDescripcion;
    }

    public void setDescripcion(String sDescripcion) {
        this.sDescripcion = sDescripcion;
    }

    public String getUbicacion() {
        return sUbicacion;
    }

    public void setUbicacion(String sUbicacion) {
        this.sUbicacion = sUbicacion;
    }

    public float getRentaMensual() {
        return nRentaMensual;
    }

    public void setRentaMensual(float nRentaMensual) {
        this.nRentaMensual = nRentaMensual;
    }

    public float getRentaAnual() {
        return nRentaAnual;
    }

    public void setRentaAnual(float nRentaAnual) {
        this.nRentaAnual = nRentaAnual;
    }
    
    public float getDeposito() {
        return nDeposito;
    }

    public void setDeposito(float nDeposito) {
        this.nDeposito = nDeposito;
    }

    public Emisor getArrendador() {
        return oArrendador;
    }

    public void setArrendador(Emisor oArrendador) {
        this.oArrendador = oArrendador;
    }

    public boolean isRentado() {
        return bRentado;
    }

    public void setRentado(boolean bRentado) {
        this.bRentado = bRentado;
    }

    public boolean isActivo() {
        return bActivo;
    }

    public void setActivo(boolean bActivo) {
        this.bActivo = bActivo;
    }

    public ContratoRenta getContratoRenta() {
        return oContratoRenta;
    }

    public void setContratoRenta(ContratoRenta oContratoRenta) {
        this.oContratoRenta = oContratoRenta;
    }
    
    public AccesoDatos getAD() {
        return oAD;
    }

    public void setAD(AccesoDatos oAD) {
        this.oAD = oAD;
    }

    public List<CondRenta> getListCondiciones() {
        return listCondiciones;
    }

    public void setListCondiciones(List<CondRenta> listCondiciones) {
        this.listCondiciones = listCondiciones;
    }
    
    public String getCompositeKey(){
        return nIdEspacio+"."+oContratoRenta.getIdContratoRenta();
    }
    
}
