package org.apli.modelbeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import org.apli.AD.AccesoDatos;

/**
 *
 * @author lleon
 */
public class OperacionCaja implements Serializable {
    private int nFolio;
    private String sDescripcion;
    private boolean bDeseaFactura;
    private boolean bFacturado;
    private Date dFechaOp;
    private FormaPago oFrmPago;
    private String sDescAlterna;
    private char cTipoOpe;
    private char cSituacion;
    private float nPctIvaOtro;
    private String sDatosPago="";
    private String nfolioRet;
    private AccesoDatos oAD;
    private String sQuienAutoriza;
    private String sMotivo;
    private String sCtaBcoRecep="";
    private String sFecIngRecep="";
    private String sNumDcto="";
    private String sNomTit="";
    private PersonalHospitalario oPersCapt;
    private PersonalHospitalario oPersAut;
    private double nMontoOtro; 
    private String sObs;
    
    /*Constantes para código legible*/
    public static final String TIPO_ANTICIPO_CTA = "c";
    public static final String TIPO_ANTICIPO_PAQ = "p";
    public static final String CON_DSCTO = "d";
    public static final String CON_PAGO_PARCIAL = "a";
    
    public static final String DESC_ANTICIPO = "PAGO DE ANTICIPO";
    public static final String DESC_ABO_CTA_ACT = "ABONO A CUENTA";
    public static final String DESC_RA_CTA_PART = "RA PARTICULAR";
    public static final String DESC_SERV = "PAGO SERVICIO";
    
    public OperacionCaja() {
        oPersCapt = new PersonalHospitalario();
        oPersAut = new PersonalHospitalario();
        oFrmPago = new FormaPago();
    }

    /****************************************************************/
    /*************** Inicia autorizaciones caja   *******************/
    public boolean verificaAutDsctoCaja() throws Exception{
    boolean bRet = false;
    String sQuery = "";
    Vector rst = null, linea=null;
    Valida oVal = new Valida();
    int i;
        if (this.oPersAut==null || this.oPersAut.getUsuario()==null ||
            oVal.stringIsNullOrEmpty(this.oPersAut.getUsuario().getUsuario()) ||
            oVal.stringIsNullOrEmpty(this.oPersAut.getUsuario().getContraseña()))
                throw new Exception ("OperacionCaja.verificaAutDsctoCaja: faltan datos");
        else{
            sQuery = "select * from validaUsuAutCaja('"+
                    this.oPersAut.getUsuario().getUsuario()+"','"+
                    this.oPersAut.getUsuario().getContraseña()+"')";
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
            if (rst != null && !rst.isEmpty()){
                linea = (Vector)rst.elementAt(0);
                i = ((Double)linea.elementAt(0)).intValue();
                if (i>0){ //Devuelve -1 si no es un usuario válido
                    this.oPersAut.setFolioPers(i);
                    bRet = true;
                }
            }         
        }
        return  bRet;
    }
    /*************** Fin de autorizaciones caja   *******************/
    /****************************************************************/
    
    
    /****************************************************************/
    /********** Inicia pago de anticipo (paq/cta) *******************/
    public boolean pagaAnticipo(String sTipoAnticipo, 
            ServicioPrestado oSP, String sConAutorizaciones) throws Exception{
    boolean bRet = false;
    Vector rst = null, vRowTemp= null;
    String sQuery = "";
    Valida oVal = new Valida();
        if (oVal.stringIsNullOrEmpty(sTipoAnticipo) || 
            this.getFrmPago()==null || 
            oVal.stringIsNullOrEmpty(this.getFrmPago().getCveFrmPago())||
            this.oPersCapt == null ||
            this.oPersCapt.getFolioPers()==0)
            throw new Exception("OperacionCaja.pagaAnticipo: Error, faltan datos");
        else{
            this.sDescripcion=DESC_ANTICIPO;
            sQuery = "SELECT * FROM insertaPagoAnticipo('"+sTipoAnticipo+"', '"+
                this.sDescripcion+"', '"+this.getFrmPago().getCveFrmPago()+"',"+
                oVal.cadenaParaBase(this.sDescAlterna)+", '"+
                ConceptoIngreso.TIPO_OPE_INGR+"', "+
                oVal.cadenaParaBase(this.sDatosPago)+ ", "+
                oVal.cadenaParaBase(this.sCtaBcoRecep)+", "+
                oVal.cadenaParaBase(this.sNumDcto)+", "+
                oVal.cadenaParaBase(this.sNomTit)+ ", "+
                oVal.cadenaParaBase(this.sFecIngRecep)+ ", "+
                this.oPersCapt.getFolioPers() +", "+
                (this.oPersAut==null || 
                 this.oPersAut.getFolioPers()==0?"null":this.oPersAut.getFolioPers())+ ", '"+
                oSP.getIdFolio()+"',"+ oSP.getCostoCobrado()+ ","+
                oVal.cadenaParaBase(sConAutorizaciones)+");";
            System.out.println(sQuery);
            if (this.oAD==null){
                this.oAD = new AccesoDatos();
                this.oAD.conectar();
                rst = getAD().ejecutarConsulta(sQuery);
                this.oAD.desconectar();
                this.oAD = null;
            }else{
                rst = getAD().ejecutarConsulta(sQuery);
            }
            if (rst!=null){
                vRowTemp = (Vector)rst.elementAt(0);
                this.nfolioRet = (String)vRowTemp.elementAt(0);
                try{
                    this.nFolio = Integer.parseInt(this.nfolioRet);
                    bRet = true;
                }catch(Exception e){
                    e.printStackTrace();
                    this.nfolioRet = "Error al generar el folio de caja";
                }
            }
        }
        return bRet;
    }
    /*************** Fin pago de anticipo (paq/cta) *****************/
    /****************************************************************/
    
    /****************************************************************/
    /********** Inicia venta de servicio *******************/
    public String pagaServicioMultiple(ServicioPrestado oSP, List<ServicioPrestado> lista,
            String sConAutorizaciones) 
            throws Exception{
    Vector rst = null, vRowTemp= null;
    String sQuery = "";
    Valida oVal = new Valida();
    ArrayList<String> listaAreas = new ArrayList();
        this.nfolioRet="";
        if (this.getFrmPago()==null || 
            oVal.stringIsNullOrEmpty(this.getFrmPago().getCveFrmPago())||
            this.oPersCapt == null ||
            this.oPersCapt.getFolioPers()==0)
            throw new Exception("OperacionCaja.pagaServicio: Error, faltan datos");
        else{
            for (ServicioPrestado sp:lista){
                if (!listaAreas.contains(sp.getConcepPrestado().getAreaServicio().getCve()))
                    listaAreas.add(sp.getConcepPrestado().getAreaServicio().getCve());
            }
            sQuery = "create temp table folios (folio varchar); ";
            this.sDescripcion=DESC_SERV;
            for (String area:listaAreas){
                sQuery = sQuery+" insert into folios SELECT * FROM insertaPagoServicioMultiple('"+
                    this.sDescripcion+"'::varchar, '"+
                    this.getFrmPago().getCveFrmPago()+"'::char(5),"+
                    oVal.cadenaParaBase(this.sDescAlterna)+"::varchar, '"+
                    ConceptoIngreso.TIPO_OPE_INGR+"'::char(1), "+
                    oVal.cadenaParaBase(this.sDatosPago)+ "::varchar, "+
                    oVal.cadenaParaBase(this.sCtaBcoRecep)+"::varchar, "+
                    oVal.cadenaParaBase(this.sNumDcto)+"::varchar, "+
                    oVal.cadenaParaBase(this.sNomTit)+ "::varchar, "+
                    oVal.cadenaParaBase(this.sFecIngRecep)+ "::varchar, "+
                    this.oPersCapt.getFolioPers() +"::integer, "+
                    (this.oPersAut==null || 
                     this.oPersAut.getFolioPers()==0?"null":this.oPersAut.getFolioPers())+ 
                    "::integer, ARRAY[";
                for (ServicioPrestado sp:lista){
                    if (area.equals(sp.getConcepPrestado().getAreaServicio().getCve())){
                        sQuery = sQuery + "'" + sp.getIdFolio() + "', ";
                    }
                }
                sQuery = sQuery.substring(0, sQuery.length()-2);//para quitar última coma
                sQuery = sQuery +
                    "]::varchar[], ARRAY[";

                for (ServicioPrestado sp:lista){
                    if (area.equals(sp.getConcepPrestado().getAreaServicio().getCve())){
                        sQuery = sQuery + sp.getCostoCobrado() + ", ";
                    }
                }
                sQuery = sQuery.substring(0, sQuery.length()-2);
                sQuery = sQuery + "]::numeric[],"+
                    oVal.cadenaParaBase(sConAutorizaciones)+"::char(1));";
            }
            //sQuery = sQuery + " select * from folios;";
            System.out.println(sQuery);
            if (this.oAD==null){
                this.oAD = new AccesoDatos();
                this.oAD.conectar();
                getAD().ejecutarComando(sQuery);
                rst = getAD().ejecutarConsulta("select * from folios;");
                System.out.println(rst.toString());
                this.oAD.desconectar();
                this.oAD = null;
            }else{
                rst = getAD().ejecutarConsulta(sQuery);
            }
            this.nfolioRet="";
            if (rst!=null){
                for (int i=0; i< rst.size(); i++){
                    vRowTemp = (Vector)rst.elementAt(i);
                    System.out.println((String)vRowTemp.elementAt(0));
                    this.nfolioRet = this.nfolioRet+ " " +(String)vRowTemp.elementAt(0);
                }
            }
        }
        return nfolioRet;
    }
    
    public boolean pagaServicio(ServicioPrestado oSP, String sConAutorizaciones) 
            throws Exception{
    boolean bRet = false;
    Vector rst = null, vRowTemp= null;
    String sQuery = "";
    Valida oVal = new Valida();
        if (this.getFrmPago()==null || 
            oVal.stringIsNullOrEmpty(this.getFrmPago().getCveFrmPago())||
            this.oPersCapt == null ||
            this.oPersCapt.getFolioPers()==0)
            throw new Exception("OperacionCaja.pagaServicio: Error, faltan datos");
        else{
            this.sDescripcion=DESC_ANTICIPO;
            sQuery = "SELECT * FROM insertaPagoServicio('"+
                oSP.getConcepPrestado().getDescripcion()+"'::varchar, '"+
                this.getFrmPago().getCveFrmPago()+"'::char(5),"+
                oVal.cadenaParaBase(this.sDescAlterna)+"::varchar, '"+
                ConceptoIngreso.TIPO_OPE_INGR+"'::char(1), "+
                oVal.cadenaParaBase(this.sDatosPago)+ "::varchar, "+
                oVal.cadenaParaBase(this.sCtaBcoRecep)+"::varchar, "+
                oVal.cadenaParaBase(this.sNumDcto)+"::varchar, "+
                oVal.cadenaParaBase(this.sNomTit)+ "::varchar, "+
                oVal.cadenaParaBase(this.sFecIngRecep)+ "::varchar, "+
                this.oPersCapt.getFolioPers() +"::integer, "+
                (this.oPersAut==null || 
                 this.oPersAut.getFolioPers()==0?"null":this.oPersAut.getFolioPers())+ 
                "::integer, '"+oSP.getIdFolio()+"'::varchar,"+ 
                oSP.getCostoCobrado()+ "::numeric,"+
                oVal.cadenaParaBase(sConAutorizaciones)+"::char(1));";
            System.out.println(sQuery);
            if (this.oAD==null){
                this.oAD = new AccesoDatos();
                this.oAD.conectar();
                rst = getAD().ejecutarConsulta(sQuery);
                this.oAD.desconectar();
                this.oAD = null;
            }else{
                rst = getAD().ejecutarConsulta(sQuery);
            }
            if (rst!=null){
                vRowTemp = (Vector)rst.elementAt(0);
                this.nfolioRet = (String)vRowTemp.elementAt(0);
                try{
                    this.nFolio = Integer.parseInt(this.nfolioRet);
                    bRet = true;
                }catch(Exception e){
                    e.printStackTrace();
                    this.nfolioRet = "Error al generar el folio de caja";
                }
            }
        }
        return bRet;
    }
    /*************** Fin venta de servicio *****************/
    /****************************************************************/
    
    
    /****************************************************************/
    /********** Inicia pago de abono              *******************/
    public boolean pagaAbono(ServicioPrestado oSP) throws Exception{
    boolean bRet = false;
    Vector rst = null, vRowTemp= null;
    String sQuery = "";
    Valida oVal = new Valida();
        if (this.getFrmPago()==null || 
            oVal.stringIsNullOrEmpty(this.getFrmPago().getCveFrmPago())||
            this.oPersCapt == null ||
            this.oPersCapt.getFolioPers()==0)
            throw new Exception("OperacionCaja.pagaAbono: Error, faltan datos");
        else{
            this.sDescripcion=DESC_ABO_CTA_ACT;
            sQuery = "SELECT * FROM insertaPagoAbono('"+
                this.sDescripcion+"'::character varying, '"+
                this.getFrmPago().getCveFrmPago().trim()+"',"+
                oVal.cadenaParaBase(this.sDescAlterna)+"::character varying, '"+
                ConceptoIngreso.TIPO_OPE_INGR+"'::character, "+
                oVal.cadenaParaBase(this.sDatosPago)+ "::character varying, "+
                oVal.cadenaParaBase(this.sCtaBcoRecep)+"::character varying, "+
                oVal.cadenaParaBase(this.sNumDcto)+"::character varying, "+
                oVal.cadenaParaBase(this.sNomTit)+ "::character varying, "+
                oVal.cadenaParaBase(this.sFecIngRecep)+ "::character varying, "+
                this.oPersCapt.getFolioPers() +"::integer, "+
                (this.oPersAut==null || 
                 this.oPersAut.getFolioPers()==0?"null":this.oPersAut.getFolioPers())+ "::integer, "+
                oSP.getEpisodioMedico().getCveepisodio()+"::integer, "+ 
                oSP.getAnticipo()+ "::numeric,"+
                oSP.getQuienPaga() + "::smallint, " +
                oSP.getPaciente().getFolioPac() + "::integer, '" +
                (oSP.getFacturable()?"1":"0")+"'::character);";
            System.out.println(sQuery);
            if (this.oAD==null){
                this.oAD = new AccesoDatos();
                this.oAD.conectar();
                rst = getAD().ejecutarConsulta(sQuery);
                this.oAD.desconectar();
                this.oAD = null;
            }else{
                rst = getAD().ejecutarConsulta(sQuery);
            }
            if (rst!=null){
                vRowTemp = (Vector)rst.elementAt(0);
                this.nfolioRet = (String)vRowTemp.elementAt(0);
                try{
                    this.nFolio = Integer.parseInt(this.nfolioRet);
                    bRet = true;
                }catch(Exception e){
                    e.printStackTrace();
                    this.nfolioRet = "Error al generar el folio de caja";
                }
            }
        }
        return bRet;
    }
    /*************** Fin pago de abono *****************/
    /****************************************************************/
    
    
    /****************************************************************/
    /**********   Inicia pago de RA particular    *******************/
    public boolean pagaRAPart(ServicioPrestado oSP) throws Exception{
    boolean bRet = false;
    Vector rst = null, vRowTemp= null;
    String sQuery = "";
    Valida oVal = new Valida();
        if (this.getFrmPago()==null || 
            oVal.stringIsNullOrEmpty(this.getFrmPago().getCveFrmPago())||
            this.oPersCapt == null ||
            this.oPersCapt.getFolioPers()==0)
            throw new Exception("OperacionCaja.pagaAbono: Error, faltan datos");
        else{
            this.sDescripcion=DESC_RA_CTA_PART;
            sQuery = "SELECT * FROM insertaPagoRAParticular('"+
                this.sDescripcion+"'::character varying, '"+
                this.getFrmPago().getCveFrmPago().trim()+"'::character(5),"+
                oVal.cadenaParaBase(this.sDescAlterna)+"::character varying, '"+
                ConceptoIngreso.TIPO_OPE_INGR+"'::character, "+
                oVal.cadenaParaBase(this.sDatosPago)+ "::character varying, "+
                oVal.cadenaParaBase(this.sCtaBcoRecep)+"::character varying, "+
                oVal.cadenaParaBase(this.sNumDcto)+"::character varying, "+
                oVal.cadenaParaBase(this.sNomTit)+ "::character varying, "+
                oVal.cadenaParaBase(this.sFecIngRecep)+ "::character varying, "+
                this.oPersCapt.getFolioPers() +"::integer, "+
                (this.oPersAut==null || 
                 this.oPersAut.getFolioPers()==0?"null":this.oPersAut.getFolioPers())+ "::integer, "+
                oSP.getPaciente().getFolioPac()+"::integer, "+ 
                oSP.getEpisodioMedico().getCveepisodio()+"::integer, "+ 
                oSP.getCostoOriginal()+ "::numeric,'"+
                oSP.getIdFolio()+ "'::character varying);";
            System.out.println(sQuery);
            if (this.oAD==null){
                this.oAD = new AccesoDatos();
                this.oAD.conectar();
                rst = getAD().ejecutarConsulta(sQuery);
                this.oAD.desconectar();
                this.oAD = null;
            }else{
                rst = getAD().ejecutarConsulta(sQuery);
            }
            if (rst!=null){
                vRowTemp = (Vector)rst.elementAt(0);
                this.nfolioRet = (String)vRowTemp.elementAt(0);
                try{
                    this.nFolio = Integer.parseInt(this.nfolioRet);
                    bRet = true;
                }catch(Exception e){
                    e.printStackTrace();
                    this.nfolioRet = "Error al generar el folio de caja";
                }
            }
        }
        return bRet;
    }
    /************************ Fin pago de RA ************************/
    /****************************************************************/
    
    
    /****************************************************************/
    /************** Inicia pago por cierre de cuenta ****************/
    public boolean pagaCierreCuenta(ServicioPrestado oSP, 
            String sConAutorizaciones) throws Exception{
    boolean bRet = false;
    Vector rst = null, vRowTemp= null;
    String sQuery = "";
    Valida oVal = new Valida();
        if (this.getFrmPago()==null || 
            oVal.stringIsNullOrEmpty(this.getFrmPago().getCveFrmPago())||
            this.oPersCapt == null ||
            this.oPersCapt.getFolioPers()==0)
            throw new Exception("OperacionCaja.pagaCierreCuenta: Error, faltan datos");
        else{
            this.sDescripcion=DESC_ABO_CTA_ACT;
            sQuery = "SELECT * FROM insertaPagoCierreCuenta('"+
                this.sDescripcion+"', '"+this.getFrmPago().getCveFrmPago()+"',"+
                oVal.cadenaParaBase(this.sDescAlterna)+", '"+
                ConceptoIngreso.TIPO_OPE_INGR+"', "+
                oVal.cadenaParaBase(this.sDatosPago)+ ", "+
                oVal.cadenaParaBase(this.sCtaBcoRecep)+", "+
                oVal.cadenaParaBase(this.sNumDcto)+", "+
                oVal.cadenaParaBase(this.sNomTit)+ ", "+
                oVal.cadenaParaBase(this.sFecIngRecep)+ ", "+
                this.oPersCapt.getFolioPers() +", "+
                (this.oPersAut==null || 
                 this.oPersAut.getFolioPers()==0?"null":this.oPersAut.getFolioPers())+ ", "+
                oSP.getPaciente().getFolioPac() +","+ 
                oSP.getEpisodioMedico().getCveepisodio() +","+ 
                oSP.getAnticipo()+ ","+
                oVal.cadenaParaBase(sConAutorizaciones)+");";
            System.out.println(sQuery);
            if (this.oAD==null){
                this.oAD = new AccesoDatos();
                this.oAD.conectar();
                rst = getAD().ejecutarConsulta(sQuery);
                this.oAD.desconectar();
                this.oAD = null;
            }else{
                rst = getAD().ejecutarConsulta(sQuery);
            }
            if (rst!=null){
                vRowTemp = (Vector)rst.elementAt(0);
                this.nfolioRet = (String)vRowTemp.elementAt(0);
                try{
                    this.nFolio = Integer.parseInt(this.nfolioRet);
                    bRet = true;
                }catch(Exception e){
                    e.printStackTrace();
                    this.nfolioRet = "Error al generar el folio de caja";
                }
            }
        }
        return bRet;
    }
    /***************** Fin pago por cierre de cuenta ****************/
    /****************************************************************/
    
    public void detalleOperacionCajaPagoAnticipo(String pnfolio, String pnidfolio, double pnmontoegreotroingr) throws Exception {

        Vector rst = null;
        int pncveconcepotroingr = 1117;
        String sQuery = "select * from  insertadetallepago(" + pnfolio + "," + pncveconcepotroingr + ",'" + pnidfolio + "'," + pnmontoegreotroingr + ")";

        if (getAD() == null) {
            setAD(new AccesoDatos());
            getAD().conectar();
            rst = getAD().ejecutarConsulta(sQuery);
            getAD().desconectar();
            setAD(null);
        }
    }

    public void detalleOperacionCajaDevolucionDeposito(String nFolio, String nIdfolio, double nMonto) throws Exception {
        Vector rst = null;
        int concepEgre = 26;
        String sQuery = "select * from actualizaDetalleCajadevdep('" + nFolio + "'," + concepEgre + ",'" + nIdfolio + "'," + nMonto + ")";

        if (getAD() == null) {
            setAD(new AccesoDatos());
            getAD().conectar();
            rst = getAD().ejecutarConsulta(sQuery);
            getAD().desconectar();
            setAD(null);
        }
    }

    public void detalleOperacionCajaEgresoPrestamo(String nFolio, int concepEgre, 
            double nMonto) throws Exception {
        Vector rst = null;
        String sQuery = "select * from actualizaDetalleCajaEgresoPrestamo('" + nFolio + "'," + concepEgre + "," + nMonto + ")";

        if (getAD() == null) {
            setAD(new AccesoDatos());
            getAD().conectar();
            rst = getAD().ejecutarConsulta(sQuery);
            getAD().desconectar();
            setAD(null);
        }
    }

    public void detalleOperacionCajaPagoHonorarios(String nFolio, double nMonto) throws Exception {
        Vector rst = null;
        int concepEgre = 30;
        String sQuery = "select * from actualizaDetalleCajapagohonorarios('" + nFolio + "'," + concepEgre + "," + nMonto + ")";

        if (getAD() == null) {
            setAD(new AccesoDatos());
            getAD().conectar();
            rst = getAD().ejecutarConsulta(sQuery);
            getAD().desconectar();
            setAD(null);
        }
    }

    public void detalleOperacionCajaValeEfectivo(String nFolio, double nMonto) throws Exception {
        Vector rst = null;
        int concepEgre = 237;
        String sQuery = "select * from actualizaDetalleCajaValeEfectivo('" + nFolio + "'," + concepEgre + "," + nMonto + ")";

        if (getAD() == null) {
            setAD(new AccesoDatos());
            getAD().conectar();
            rst = getAD().ejecutarConsulta(sQuery);
            getAD().desconectar();
            setAD(null);
        }
    }

    public void detalleOperacionCajaPagoCompra(String nFolio, int concepEgre, double nMonto) throws Exception {
        Vector rst = null;
        String sQuery = "select * from actualizaDetalleCajapagohonorarios('" + nFolio + "'," + concepEgre + "," + nMonto + ")";

        if (getAD() == null) {
            setAD(new AccesoDatos());
            getAD().conectar();
            rst = getAD().ejecutarConsulta(sQuery);
            getAD().desconectar();
            setAD(null);
        }
    }

    public void detalleOperacionCajaPagoExtra(String nFolio, double nMonto) throws Exception {
        Vector rst = null;
        int concepEgre = 92;
        String sQuery = "select * from actualizaDetalleCajapagohonorarios('" + nFolio + "'," + concepEgre + "," + nMonto + ")";

        if (getAD() == null) {
            setAD(new AccesoDatos());
            getAD().conectar();
            rst = getAD().ejecutarConsulta(sQuery);
            getAD().desconectar();
            setAD(null);
        }
    }

    public void detalleOperacionCajaPagoRentaAtrasado(String nFolio, int concepIngr) throws Exception {
        Vector rst = null;
        String sQuery = "select * from actualizadetallecajapagorentaatrasado('" + nFolio + "'," + concepIngr + ")";

        if (getAD() == null) {
            setAD(new AccesoDatos());
            getAD().conectar();
            rst = getAD().ejecutarConsulta(sQuery);
            getAD().desconectar();
            setAD(null);
        }
    }

    public void detalleOperacionCajaPagoRenta(String nFolio, int concepIngr, double nMonto) throws Exception {
        Vector rst = null;
        String sQuery = "select * from actualizadetallecajapagorenta('" + nFolio + "'," + concepIngr + "," + nMonto + ")";

        if (getAD() == null) {
            setAD(new AccesoDatos());
            getAD().conectar();
            rst = getAD().ejecutarConsulta(sQuery);
            getAD().desconectar();
            setAD(null);
        }
    }

    public void registraPagoDeRenta(String sDatosPago, String sDescAlterna, String sCveFrmPago, String sDeseaFactura) throws Exception {
        Vector rst = null;
        String sQuery = "";
        cSituacion = '1';
        cTipoOpe = 'I';
        String sDesc = "Pago de Renta";
        setFechaOp(new Date());
        sQuery = "select * from insertaoperacioncajapagorenta( '" + sDesc + "','" + sDeseaFactura + "','" + this.getFechaOp() + "'::timestamp without time zone,'" + sCveFrmPago + "','" + sDescAlterna + "','" + cTipoOpe + "','" + cSituacion + "','" + sDatosPago + "') ";

        if (getAD() == null) {
            setAD(new AccesoDatos());
            getAD().conectar();
            rst = getAD().ejecutarConsulta(sQuery);
            getAD().desconectar();
            setAD(null);
            setFolioRet("" + rst.get(0) + "");
        }
    }

    public void registraPagoDeRegalias() throws Exception {
        Vector rst = null;
        String sQuery = "";
        String sFacturado = "0";
        cSituacion = '1';
        cTipoOpe = 'E';
        String sCveFrmPago = "EFE  ";
        int nDeseaFactura = 0;
         sDatosPago = "";
         sDescripcion = "Entrega en efectivo por pago de regalías";
        setFechaOp(new Date());
        float nPctivaOtro = 16;
        sQuery = "select * from insertaoperacioncaja('" + sDescripcion + "','" + 
                nDeseaFactura + "','" + sFacturado + "','" + this.getFechaOp() + 
                "'::timestamp without time zone,'" + sCveFrmPago + "','" + 
                cTipoOpe + "','" + cSituacion + "'," + nPctivaOtro + ",'" + 
                sDatosPago + "');";

        if (getAD() == null) {
            System.out.print("    Ejecutando insercion en caja     ");
            setAD(new AccesoDatos());
            getAD().conectar();
            rst = getAD().ejecutarConsulta(sQuery);
            getAD().desconectar();
            setAD(null);
            setFolioRet("" + rst.get(0) + "");
        }

    }

    public void registraValeEfectivo() throws Exception {
        Vector rst = null;
        String sQuery = "";
        cSituacion = '1';
        cTipoOpe = 'E';
        String sCveFrmPago = "EFE  ";
        sDescripcion = "Vale de Efectivo";
        setFechaOp(new Date());

        sQuery = "select * from insertaoperacioncajavaleefectivo('" + sDescripcion + "','" + this.getFechaOp() + "'::timestamp without time zone,'" + sCveFrmPago + "','" + cTipoOpe + "','" + cSituacion + "');";

        if (getAD() == null) {
            setAD(new AccesoDatos());
            getAD().conectar();
            rst = getAD().ejecutarConsulta(sQuery);
            getAD().desconectar();
            setAD(null);
            setFolioRet("" + rst.get(0) + "");
        }
        System.out.print(getFolioRet());
    }

    public void registraDevolucionDeposito(String sObservaciones, String sDatos) throws Exception {
        Vector rst = null;
        String sQuery = "";
        cSituacion = '1';
        cTipoOpe = 'E';
        String sCveFrmPago = "EFE  ";
        sDatosPago = sDatos;
        sDescripcion = "Entrega en efectivo por devolución de depósito";
        setFechaOp(new Date());
        sQuery = "select * from insertaoperacioncajadevoluciondeposito('" + 
                sDescripcion + "','" + 
                this.getFechaOp() + "'::timestamp without time zone,'" + 
                sCveFrmPago + "','" + cTipoOpe + "','" + cSituacion + "','" + 
                sDatosPago + "','" + sObservaciones + "');";

        if (getAD() == null) {
            System.out.print("    Ejecutando insercion en caja para devolucion de deposito    ");
            setAD(new AccesoDatos());
            getAD().conectar();
            rst = getAD().ejecutarConsulta(sQuery);
            getAD().desconectar();
            setAD(null);
            setFolioRet("" + rst.get(0) + "");
        }
        System.out.print(getFolioRet());
    }

    public void registraEgresoPrestamo(int nIdPres) throws Exception {
        Vector rst = null;
        String sQuery = "";
        cSituacion = '1';
        cTipoOpe = 'E';
        String sCveFrmPago = "EFE  ";

        String sDesc = "Efectivo,entrega de préstamo personal folio "+nIdPres;
        setFechaOp(new Date());
        sQuery = "select * from insertaoperacioncajaegresoprestamo('" + sDesc + "','" + this.getFechaOp() + "'::timestamp without time zone,'" + sCveFrmPago + "','" + cTipoOpe + "','" + cSituacion + "','" + sDatosPago + "');";

        if (getAD() == null) {
            System.out.print("    Ejecutando insercion en caja para Egreso Prestamo    ");
            setAD(new AccesoDatos());
            getAD().conectar();
            rst = getAD().ejecutarConsulta(sQuery);
            getAD().desconectar();
            setAD(null);
            setFolioRet("" + rst.get(0) + "");
        }

    }

    public void registraPagoHonorarios() throws Exception {
        Vector rst = null;
        String sQuery = "";
        cSituacion = '1';
        cTipoOpe = 'E';
        String sCveFrmPago = "EFE  ";
        char bdeseafactura = '0';
        sDescripcion = "Entrega en efectivo por pago de honorarios";
        setFechaOp(new Date());

        sQuery = "select * from insertaoperacioncajapagohonorarios('" + sDescripcion + "','" + bdeseafactura + "','" + this.getFechaOp() + "'::timestamp without time zone,'" + sCveFrmPago + "','" + cTipoOpe + "','" + cSituacion + "');";

        if (getAD() == null) {
            setAD(new AccesoDatos());
            getAD().conectar();
            rst = getAD().ejecutarConsulta(sQuery);
            getAD().desconectar();
            setAD(null);
            setFolioRet("" + rst.get(0) + "");
        }
        System.out.print(getFolioRet());
    }

    public void registraOperacionCajaEgreComp() throws Exception {

        Vector rst = null;
        String sQuery = "";
        String sFacturado = "0";
        cSituacion = '1';
        cTipoOpe = 'E';
        sDescripcion = "Entrega de Efectivo por Compra";
        int nDeseaFactura = 1;
        String sCveFrmPago = "EFE  ";
        float nPctivaOtro = 16;
        sDatosPago = "";

        setFechaOp(new Date());

        sQuery = "select * from insertaoperacioncaja('" + sDescripcion + "','" + 
                nDeseaFactura + "','" + sFacturado + "','" + this.getFechaOp() + 
                "'::timestamp without time zone,'" + sCveFrmPago + "','" + 
                cTipoOpe + "','" + cSituacion + "'," + nPctivaOtro + ",'" + 
                sDatosPago + "');";
        if (getAD() == null) {

            setAD(new AccesoDatos());
            getAD().conectar();
            rst = getAD().ejecutarConsulta(sQuery);
            getAD().desconectar();
            setAD(null);

            this.setFolioRet("" + rst.get(0));
        }


    }

    public void insertaOperacionCajaVnt(String sDescripcion, int bDeseaFactura, 
            String sCveFrmPago, float nPctivaOtro, String sDatosPago) throws Exception {

        Vector rst = null;
        String sQuery = "";
        String sFacturado = "0";
        cSituacion = '1';
        cTipoOpe = 'I';

        setFechaOp(new Date());

        sQuery = "select * from insertaoperacioncaja('" + sDescripcion + "','" + 
                bDeseaFactura + "','" + sFacturado + "','" + this.getFechaOp() + 
                "'::timestamp without time zone,'" + sCveFrmPago + "','" + 
                cTipoOpe + "','" + cSituacion + "'," + nPctivaOtro + ",'" + 
                sDatosPago + "');";
        if (getAD() == null) {

            setAD(new AccesoDatos());
            getAD().conectar();
            rst = getAD().ejecutarConsulta(sQuery);
            getAD().desconectar();
            setAD(null);

            this.setFolioRet("" + rst.get(0));
        }


    }

    public void registraIngresoOtrosServ(int nCveConcep)throws Exception {
    Vector rst = null;
    String sQuery = "";
        sQuery = "select * from insertaIngresoOtrosServ( " +
            (this.sObs.equals("")?"null":"'"+this.sObs+"'")+
                "::character varying, '"+
            (this.bDeseaFactura?1:0) + "'::character, '" +
            this.oFrmPago.getCveFrmPago() + "'::character varying, " +
            this.nMontoOtro + "::numeric, " +
            (this.sDatosPago.equals("")?"null":"'"+this.sDatosPago+"'") + "::character varying," +
            (this.sCtaBcoRecep.equals("")?"null":"'"+this.sCtaBcoRecep+"'")+"::character varying,"+
            (this.sNumDcto.equals("")?"null":"'"+this.sNumDcto+"'") + "::character varying," +
            (this.sNomTit.equals("")?"null":"'"+this.sNomTit+"'") + "::character varying," +
            this.oPersCapt.getFolioPers() + "::int, " +  nCveConcep + ", "+
            (this.sFecIngRecep.equals("")?"null":"'"+this.sFecIngRecep+"'") +
            "::character varying)";
        System.out.println(sQuery);
        if (oAD == null) {
            oAD = new AccesoDatos();
            oAD.conectar();
            rst = oAD.ejecutarConsulta(sQuery);
            oAD.desconectar();
            oAD = null;
            if (rst!=null)
                this.setFolioRet("" + rst.get(0));
        }
    }
    
    public void registraPagoPrestamo(int nFolioPres)throws Exception {
    Vector rst = null;
    String sQuery = "";
        sQuery = "select * from insertapagoprestamo( " +
            nFolioPres+
                "::int, '"+
            this.oFrmPago.getCveFrmPago() + "'::character varying, " +
            (this.sDatosPago.equals("")?"null":"'"+this.sDatosPago+"'") + "::character varying," +
            this.nMontoOtro + "::numeric, " +
            (this.sCtaBcoRecep.equals("")?"null":"'"+this.sCtaBcoRecep+"'")+"::character varying,"+
            (this.sNumDcto.equals("")?"null":"'"+this.sNumDcto+"'") + "::character varying," +
            (this.sNomTit.equals("")?"null":"'"+this.sNomTit+"'") + "::character varying," +
            this.oPersCapt.getFolioPers() + ", " + 
            (this.sFecIngRecep.equals("")?"null":"'"+this.sFecIngRecep+"'")+
            "::character varying)";
        System.out.println(sQuery);
        if (oAD == null) {
            oAD = new AccesoDatos();
            oAD.conectar();
            rst = oAD.ejecutarConsulta(sQuery);
            oAD.desconectar();
            oAD = null;
            if (rst!=null)
                this.setFolioRet("" + rst.get(0));
        }
    }
    
    
    public void registraOperacionCajaPagoAnticipo(String sCveFrmPago, String sDatosPago) throws Exception {
        Vector rst = null;
        String sQuery = "";
        String sFacturado = "0";
        int nDeseaFactura = 0;
        cSituacion = '1';
        cTipoOpe = 'I';
        sDescripcion = "Pago de Anticipo";
        float nPctivaOtro = 16;

        setFechaOp(new Date());

        sQuery = "select * from insertaoperacioncaja('" + sDescripcion + "','" + 
                nDeseaFactura + "','" + sFacturado + "','" + this.getFechaOp() + 
                "'::timestamp without time zone,'" + sCveFrmPago + "','" + 
                cTipoOpe + "','" + cSituacion + "'," + nPctivaOtro + ",'" + 
                sDatosPago + "');";
        if (getAD() == null) {
            setAD(new AccesoDatos());
            getAD().conectar();
            rst = getAD().ejecutarConsulta(sQuery);
            getAD().desconectar();
            setAD(null);

            this.setFolioRet("" + rst.get(0));
        }
    }

    public int getFolio() {
        return nFolio;
    }

    public void setFolio(int nFolio) {
        this.nFolio = nFolio;
    }

    public String getDescripcion() {
        return sDescripcion;
    }

    public void setDescripcion(String sDescripcion) {
        this.sDescripcion = sDescripcion;
    }

    public boolean isDeseaFactura() {
        return bDeseaFactura;
    }

    public void setDeseaFactura(boolean bDeseaFactura) {
        this.bDeseaFactura = bDeseaFactura;
    }

    public boolean isFacturado() {
        return bFacturado;
    }

    public void setFacturado(boolean bFacturado) {
        this.bFacturado = bFacturado;
    }

    public Date getFechaOp() {
        return dFechaOp;
    }

    public void setFechaOp(Date dFechaOp) {
        this.dFechaOp = dFechaOp;
    }

    public FormaPago getFrmPago() {
        return oFrmPago;
    }

    public void setFrmPago(FormaPago oFrmPago) {
        this.oFrmPago = oFrmPago;
    }

    public String getDescAlterna() {
        return sDescAlterna;
    }

    public void setDescAlterna(String sDescAlterna) {
        this.sDescAlterna = sDescAlterna;
    }

    public char getTipoOpe() {
        return cTipoOpe;
    }

    public void setTipoOpe(char cTipoOpe) {
        this.cTipoOpe = cTipoOpe;
    }

    public char getSituacion() {
        return cSituacion;
    }

    public void setSituacion(char cSituacion) {
        this.cSituacion = cSituacion;
    }

    public float getPctIvaOtro() {
        return nPctIvaOtro;
    }

    public void setPctIvaOtro(float nPctIvaOtro) {
        this.nPctIvaOtro = nPctIvaOtro;
    }

    public String getDatosPago() {
        return sDatosPago;
    }

    public void setDatosPago(String sDatosPago) {
        this.sDatosPago = sDatosPago;
    }

    public AccesoDatos getAD() {
        return oAD;
    }

    public void setAD(AccesoDatos oAD) {
        this.oAD = oAD;
    }

    /**
     * @return the nfolioRet
     */
    public String getFolioRet() {
        return nfolioRet;
    }

    /**
     * @param nfolioRet the nfolioRet to set
     */
    public void setFolioRet(String nfolioRet) {
        this.nfolioRet = nfolioRet;
    }

    /**
     * @return the sQuienAutoriza
     */
    public String getQuienAutoriza() {
        return sQuienAutoriza;
    }

    /**
     * @param sQuienAutoriza the sQuienAutoriza to set
     */
    public void setQuienAutoriza(String sQuienAutoriza) {
        this.sQuienAutoriza = sQuienAutoriza;
    }

    /**
     * @return the sMotivo
     */
    public String getMotivo() {
        return sMotivo;
    }

    /**
     * @param sMotivo the sMotivo to set
     */
    public void setMotivo(String sMotivo) {
        this.sMotivo = sMotivo;
    }

    /**
     * @return the sCtaBcoRecep
     */
    public String getCtaBcoRecep() {
        return sCtaBcoRecep;
    }

    /**
     * @param sCtaBcoRecep the sCtaBcoRecep to set
     */
    public void setCtaBcoRecep(String sCtaBcoRecep) {
        this.sCtaBcoRecep = sCtaBcoRecep;
    }

    /**
     * @return the sFecIngRecep
     */
    public String getFecIngRecep() {
        return sFecIngRecep;
    }

    /**
     * @param sFecIngRecep the sFecIngRecep to set
     */
    public void setFecIngRecep(String sFecIngRecep) {
        this.sFecIngRecep = sFecIngRecep;
    }

    /**
     * @return the sNumDcto
     */
    public String getNumDcto() {
        return sNumDcto;
    }

    /**
     * @param sNumDcto the sNumDcto to set
     */
    public void setNumDcto(String sNumDcto) {
        this.sNumDcto = sNumDcto;
    }

    /**
     * @return the sNomTit
     */
    public String getNomTit() {
        return sNomTit;
    }

    /**
     * @param sNomTit the sNomTit to set
     */
    public void setNomTit(String sNomTit) {
        this.sNomTit = sNomTit;
    }

    /**
     * @return the oPersCapt
     */
    public PersonalHospitalario getPersCapt() {
        return oPersCapt;
    }

    /**
     * @param oPersCapt the oPersCapt to set
     */
    public void setPersCapt(PersonalHospitalario oPersCapt) {
        this.oPersCapt = oPersCapt;
    }

    /**
     * @return the oPersAut
     */
    public PersonalHospitalario getPersAut() {
        return oPersAut;
    }

    /**
     * @param oPersAut the oPersAut to set
     */
    public void setPersAut(PersonalHospitalario oPersAut) {
        this.oPersAut = oPersAut;
    }

    /**
     * @return the nMontoOtro
     */
    public double getMontoOtro() {
        return nMontoOtro;
    }

    /**
     * @param nMontoOtro the nMontoOtro to set
     */
    public void setMontoOtro(double nMontoOtro) {
        this.nMontoOtro = nMontoOtro;
    }
    
    public String getObs(){
        return this.sObs;
    }
    public void setObs(String valor){
        this.sObs = valor;
    }
}
