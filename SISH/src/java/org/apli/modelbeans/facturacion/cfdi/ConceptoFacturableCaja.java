package org.apli.modelbeans.facturacion.cfdi;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Vector;
import org.apli.AD.AccesoDatos;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase para definir los conceptos que serán considerados para facturar
 * Para particulares y rentas: cada concepto de caja corresponde a una factura
 * @author Isabel Espinoza Espinoza
 */
public class ConceptoFacturableCaja implements Serializable{
    protected int nClaveEpisodioMed;
    protected int nCantidad;
    protected String sUnidad;
    protected String sDescripcion;
    protected String sDescripcionAlterna;    
    protected BigDecimal oCostoUnitario;
    protected BigDecimal oDescuento;
    protected BigDecimal oImporte;
    protected BigDecimal oImpuesto;
    protected int nPctIVa;
    protected int nFolioPac; //Folio del paciente
    protected AccesoDatos oAD = null;
     //OCC operación concepto caja, OC operación caja
    protected int nFolioOCC;  //Folio de operación concepto caja
    protected int nSecOCC;   //Folio sec de operación concepto caja
    protected String sFolioOC;  //Folio operación caja      ----llave para el concepto facturable, 
                                                       //se pasa a cadena para poder identificar los conceptos
    
    protected String sFolioServicio=""; //Folio del servicio
    public List<ConceptoFacturableCaja> getConceptosFacturasCuentasEmpresa(int folioPaciente,int cveEpisodio)throws Exception{
        ConceptoFacturableCaja oP=null;
        Vector rst = null;
        Vector<ConceptoFacturableCaja> vObj = null;
        List<ConceptoFacturableCaja> lista=new ArrayList();
        String sQuery = "";
        int i=0;
        sQuery = "select * from buscaconceptosfacturasempresasPorCuenta("+folioPaciente+","+cveEpisodio+")";
        if (getAD() == null){
            setAD(new AccesoDatos());
            getAD().conectar();
        }
        rst = getAD().ejecutarConsulta(sQuery);
        if (rst != null) {
            vObj = new Vector<ConceptoFacturableCaja>();
            for (i = 0; i < rst.size(); i++) {
                oP = new ConceptoFacturableCaja();
                Vector vRowTemp = (Vector)rst.elementAt(i); 
                oP.setFolioOCC(((Double) vRowTemp.elementAt(0)).intValue());
                oP.setSecOCC(((Double) vRowTemp.elementAt(1)).intValue());
                oP.setFolioOC((""+((Double) vRowTemp.elementAt(2)).intValue()));
                oP.setFolioPac(((Double) vRowTemp.elementAt(3)).intValue());
                oP.setFolioServicio((String) vRowTemp.elementAt(4));
                oP.setCantidad(((Double) vRowTemp.elementAt(5)).intValue());
                oP.setUnidad((String) vRowTemp.elementAt(6));
                oP.setPctIVa(((Double) vRowTemp.elementAt(10)).intValue());
                oP.setCostoUnitario(new BigDecimal(((Double) vRowTemp.elementAt(9)).floatValue()));//costo cobrado
                oP.setDescripcion((String) vRowTemp.elementAt(7));
                oP.setDescripcionAlterna((String) vRowTemp.elementAt(8));
                lista.add(oP);
            }
        }
        getAD().desconectar();
        setAD(null);
        return lista;
    }
    //YA___ busca los conceptos facturables de acuerdo al número de paquete proporcionado
    
    public List<ConceptoFacturableCaja> getConceptosContratosPaquetes(int nNumPaquete,int pctIVA)throws Exception{
         ConceptoFacturableCaja oP=null;
        Vector rst = null;
        Vector<ConceptoFacturableCaja> vObj = null;
        List<ConceptoFacturableCaja> lista=new ArrayList();
        String sQuery = "";
        int i=0;
        sQuery = "select * from buscaconceptosContratosPaquetes("+nNumPaquete+")";
        System.out.println(sQuery);
        if (getAD() == null){
            setAD(new AccesoDatos());
            getAD().conectar();
        }
        rst = getAD().ejecutarConsulta(sQuery);
        getAD().desconectar();
        setAD(null);
        if (rst != null) {
            vObj = new Vector<ConceptoFacturableCaja>();
            for (i = 0; i < rst.size(); i++) {
                 oP = new ConceptoFacturableCaja();
                Vector vRowTemp = (Vector)rst.elementAt(i); 
                oP.setFolioOCC(((Double) vRowTemp.elementAt(0)).intValue());    //nidpaquetecont
                oP.setSecOCC(((Double) vRowTemp.elementAt(1)).intValue());      //nidpaquete
                oP.setFolioOC((""+((Double) vRowTemp.elementAt(2)).intValue()));//nsec
                oP.setCantidad(((Double) vRowTemp.elementAt(3)).intValue());
                oP.setPctIVa(pctIVA);
                oP.setCostoUnitario(new BigDecimal(((Double) vRowTemp.elementAt(4)).floatValue()));//costo cobrado
                oP.setDescripcion((String) vRowTemp.elementAt(5));
                oP.setUnidad((String) vRowTemp.elementAt(6));
                lista.add(oP);
            }
        }
        return lista;
    }
    public List<ConceptoFacturableCaja> getConceptosFacturasPaquetes(int nNumPaquete)throws Exception{
         ConceptoFacturableCaja oP=null;
        Vector rst = null;
        Vector<ConceptoFacturableCaja> vObj = null;
        List<ConceptoFacturableCaja> lista=new ArrayList();
        String sQuery = "";
        int i=0;
        sQuery = "select * from buscaconceptosfacturasPaquetes("+nNumPaquete+")";
        System.out.println(sQuery);
        if (getAD() == null){
            setAD(new AccesoDatos());
            getAD().conectar();
        }
        rst = getAD().ejecutarConsulta(sQuery);
        getAD().desconectar();
        setAD(null);
        if (rst != null) {
            vObj = new Vector<ConceptoFacturableCaja>();
            for (i = 0; i < rst.size(); i++) {
                 oP = new ConceptoFacturableCaja();
                Vector vRowTemp = (Vector)rst.elementAt(i); 
                oP.setFolioOCC(((Double) vRowTemp.elementAt(0)).intValue());
                oP.setSecOCC(((Double) vRowTemp.elementAt(1)).intValue());
                oP.setFolioOC((""+((Double) vRowTemp.elementAt(2)).intValue()));
                oP.setFolioPac(((Double) vRowTemp.elementAt(3)).intValue());
                oP.setFolioServicio((String) vRowTemp.elementAt(4));
                oP.setCantidad(((Double) vRowTemp.elementAt(5)).intValue());
                oP.setUnidad((String) vRowTemp.elementAt(6));
                oP.setPctIVa(((Double) vRowTemp.elementAt(10)).intValue());
                oP.setCostoUnitario(new BigDecimal(((Double) vRowTemp.elementAt(9)).floatValue()));//costo cobrado
                oP.setDescripcion((String) vRowTemp.elementAt(7));
                oP.setDescripcionAlterna((String) vRowTemp.elementAt(8));
                lista.add(oP);
            }
        }
        return lista;
    }
    //YA_______Busca conceptos que corresponden a facturas  de cuentas de particulares, previamente se obtuvo el folio del paciente y del episodio
    //los cuales corresponden a un paciente que tiene una cuenta de hospital que el pagará
    //incluye deducibles y coaseguros que ya fueron facturados para la empresa
    public List<ConceptoFacturableCaja> getConceptosFacturasCuentasParticulares(int folioPaciente,int cveEpisodio)throws Exception{
        ConceptoFacturableCaja oP=null;
        Vector rst = null;
        Vector<ConceptoFacturableCaja> vObj = null;
        List<ConceptoFacturableCaja> lista=new ArrayList();
        String sQuery = "";
        int i=0;
        sQuery = "select * from buscaconceptosfacturasparticulares("+folioPaciente+","+cveEpisodio+")";
        if (getAD() == null){
            setAD(new AccesoDatos());
            getAD().conectar();
        }
        rst = getAD().ejecutarConsulta(sQuery);
        if (rst != null) {
            vObj = new Vector<ConceptoFacturableCaja>();
            for (i = 0; i < rst.size(); i++) {
                oP = new ConceptoFacturableCaja();
                Vector vRowTemp = (Vector)rst.elementAt(i); 
                oP.setFolioOCC(((Double) vRowTemp.elementAt(0)).intValue());
                oP.setSecOCC(((Double) vRowTemp.elementAt(1)).intValue());
                oP.setFolioOC((""+((Double) vRowTemp.elementAt(2)).intValue()));
                oP.setFolioPac(((Double) vRowTemp.elementAt(3)).intValue());
                oP.setFolioServicio((String) vRowTemp.elementAt(4));
                oP.setCantidad(((Double) vRowTemp.elementAt(5)).intValue());
                oP.setUnidad((String) vRowTemp.elementAt(6));
                oP.setPctIVa(((Double) vRowTemp.elementAt(10)).intValue());
                oP.setCostoUnitario(new BigDecimal(((Double) vRowTemp.elementAt(9)).floatValue()));//costo cobrado
                oP.setDescripcion((String) vRowTemp.elementAt(7));
                oP.setDescripcionAlterna((String) vRowTemp.elementAt(8));
                lista.add(oP);
            }
        }
        getAD().desconectar();
        setAD(null);
        return lista;
    }
    public List<ConceptoFacturableCaja> getConceptosConsumosCuentasParticulares(int folioPaciente,int cveEpisodio)throws Exception{
        ConceptoFacturableCaja oP=null;
        Vector rst = null;
        Vector<ConceptoFacturableCaja> vObj = null;
        List<ConceptoFacturableCaja> lista=new ArrayList();
        String sQuery = "";
        int i=0;
        sQuery = "select * from buscaConsumosParaFacturarCuentasHospital("+folioPaciente+","+cveEpisodio+")";
        if (getAD() == null){
            setAD(new AccesoDatos());
            getAD().conectar();
        }
        rst = getAD().ejecutarConsulta(sQuery);
        if (rst != null) {
            vObj = new Vector<ConceptoFacturableCaja>();
            for (i = 0; i < rst.size(); i++) {
                oP = new ConceptoFacturableCaja();
                Vector vRowTemp = (Vector)rst.elementAt(i); 
                oP.setFolioPac(((Double) vRowTemp.elementAt(0)).intValue());
                oP.setFolioServicio((String) vRowTemp.elementAt(1));
                oP.setCantidad(((Double) vRowTemp.elementAt(2)).intValue());
                oP.setUnidad((String) vRowTemp.elementAt(3));
                oP.setPctIVa(((Double) vRowTemp.elementAt(6)).intValue());
                oP.setCostoUnitario(new BigDecimal(((Double) vRowTemp.elementAt(5)).floatValue()));//costo cobrado
                oP.setDescripcion((String) vRowTemp.elementAt(4));
                lista.add(oP);
            }
        }
        getAD().desconectar();
        setAD(null);
        return lista;
    }
    public List<ConceptoFacturableCaja> getConceptosDeduciblesCoasegurosFacturasCuentasParticulares(int folioPaciente,int cveEpisodio)throws Exception{
        ConceptoFacturableCaja oP=null;
        Vector rst = null;
        Vector<ConceptoFacturableCaja> vObj = null;
        List<ConceptoFacturableCaja> lista=new ArrayList();
        int i=0;
        String sQuery = "select * from buscaconceptosfacturascoasegurosparticulares("+folioPaciente+","+cveEpisodio+")";     //vienen de servicio pero fueron marcadas para ser pagados por empresas (empresa necesita)
        if (getAD() == null){
            setAD(new AccesoDatos());
            getAD().conectar();
        }
        rst = getAD().ejecutarConsulta(sQuery);
        if (rst != null) {
            vObj = new Vector<ConceptoFacturableCaja>();
            for (i = 0; i < rst.size(); i++) {
                oP = new ConceptoFacturableCaja();
                Vector vRowTemp = (Vector)rst.elementAt(i); 
                oP.setFolioOCC(((Double) vRowTemp.elementAt(0)).intValue());
                oP.setSecOCC(((Double) vRowTemp.elementAt(1)).intValue());
                oP.setFolioOC(("COA-"+((Double) vRowTemp.elementAt(2)).intValue()));
                oP.setFolioPac(((Double) vRowTemp.elementAt(3)).intValue());
                oP.setPctIVa(((Double) vRowTemp.elementAt(6)).intValue());
                oP.setCostoUnitario(new BigDecimal(((Double) vRowTemp.elementAt(4)).floatValue()));//costo cobrado
                oP.setDescripcion("PAGO DE COASEGURO");
                oP.setCantidad(1);
                oP.setUnidad("No aplica");
                lista.add(oP);
            }
        }
        
         //se agregarán los deducibes que ya podrá pagar el cliente, es decir ya fueron registrados y 
        //corresponden a una factura de empresa que requirio un deducible
        i=0;
        sQuery = "select * from buscaconceptosfacturasdeduciblesparticulares("+folioPaciente+","+cveEpisodio+")";     //vienen de servicio pero fueron marcadas para ser pagados por empresas (empresa necesita)
        rst = getAD().ejecutarConsulta(sQuery);
        if (rst != null) {
            vObj = new Vector<ConceptoFacturableCaja>();
            for (i = 0; i < rst.size(); i++) {
                oP = new ConceptoFacturableCaja();
                Vector vRowTemp = (Vector)rst.elementAt(i); 
                oP.setFolioOCC(((Double) vRowTemp.elementAt(0)).intValue());
                oP.setSecOCC(((Double) vRowTemp.elementAt(1)).intValue());
                oP.setFolioOC(("DED-"+((Double) vRowTemp.elementAt(2)).intValue()));
                oP.setFolioPac(((Double) vRowTemp.elementAt(3)).intValue());
                oP.setPctIVa(((Double) vRowTemp.elementAt(6)).intValue());
                oP.setCostoUnitario(new BigDecimal(((Double) vRowTemp.elementAt(4)).floatValue()));//costo cobrado
                oP.setDescripcion("PAGO DE DEDUCIBLE");
                oP.setCantidad(1);
                oP.setUnidad("No aplica");
                lista.add(oP);
            }
        }
        getAD().desconectar();
        setAD(null);
        return lista;
    }
    
    //YA_______Se buscan los conceptos que corresponden a una empresa, con cierto RFC de la empresa y el número de póliza que estan atendiendo
    public List<ConceptoFacturableCaja> getConceptosFacturasEmpresas(String rfcEmpresa)throws Exception{
        ConceptoFacturableCaja oP=null;
        Vector rst = null;
        Vector<ConceptoFacturableCaja> vObj = null;
        List<ConceptoFacturableCaja> lista=new ArrayList();
        String sQuery = "";
        int i=0;
        sQuery = "select * from buscaconceptosfacturasempresas('"+rfcEmpresa+"'::character varying)";
        if (getAD() == null){
            setAD(new AccesoDatos());
            getAD().conectar();
        }
        rst = getAD().ejecutarConsulta(sQuery);
        getAD().desconectar();
        setAD(null);
        if (rst != null) {
            vObj = new Vector<ConceptoFacturableCaja>();
            for (i = 0; i < rst.size(); i++) {
                oP = new ConceptoFacturableCaja();
                Vector vRowTemp = (Vector)rst.elementAt(i); 
                oP.setFolioOCC(((Double) vRowTemp.elementAt(0)).intValue());
                oP.setSecOCC(((Double) vRowTemp.elementAt(1)).intValue());
                oP.setFolioOC((""+((Double) vRowTemp.elementAt(2)).intValue()));
                oP.setFolioPac(((Double) vRowTemp.elementAt(3)).intValue());
                oP.setFolioServicio((String) vRowTemp.elementAt(4));
                oP.setCantidad(((Double) vRowTemp.elementAt(5)).intValue());
                oP.setUnidad((String) vRowTemp.elementAt(6));
                oP.setPctIVa(((Double) vRowTemp.elementAt(7)).intValue());
                oP.setCostoUnitario(new BigDecimal(((Double) vRowTemp.elementAt(8)).floatValue()));//costo cobrado
                oP.setDescripcion((String) vRowTemp.elementAt(9));
                lista.add(oP);
            }
        }
        return lista;
    }
     public List<ConceptoFacturableCaja> getConceptosCoasegurosDeduciblesFacturasEmpresas(String rfcEmpresa)throws Exception{
        ConceptoFacturableCaja oP=null;
        Vector rst = null;
        Vector<ConceptoFacturableCaja> vObj = null;
        List<ConceptoFacturableCaja> lista=new ArrayList();
        String sQuery = "";
        int i=0;
        sQuery = "select * from buscaconceptosfacturasCoasegurosDeduciblesempresas('"+rfcEmpresa+"'::character varying)";
        if (getAD() == null){
            setAD(new AccesoDatos());
            getAD().conectar();
        }
        rst = getAD().ejecutarConsulta(sQuery);
        getAD().desconectar();
        setAD(null);
        String auxiliarDescripcion, auxiliarDescripcionAlterna="";
        if (rst != null) {
            vObj = new Vector<ConceptoFacturableCaja>();
            for (i = 0; i < rst.size(); i++) {
                oP = new ConceptoFacturableCaja();
                Vector vRowTemp = (Vector)rst.elementAt(i); 
                oP.setFolioOCC(((Double) vRowTemp.elementAt(0)).intValue());
                oP.setSecOCC(((Double) vRowTemp.elementAt(1)).intValue());
                oP.setFolioOC((""+((Double) vRowTemp.elementAt(2)).intValue()));
                oP.setFolioPac(((Double) vRowTemp.elementAt(3)).intValue());
                oP.setFolioServicio((String) vRowTemp.elementAt(4));
                oP.setCantidad(((Double) vRowTemp.elementAt(5)).intValue());
                oP.setUnidad((String) vRowTemp.elementAt(6));
                oP.setPctIVa(((Double) vRowTemp.elementAt(10)).intValue());
                oP.setCostoUnitario(new BigDecimal(((Double) vRowTemp.elementAt(9)).floatValue()));//costo cobrado
                auxiliarDescripcion=(String) vRowTemp.elementAt(7);
                auxiliarDescripcionAlterna=(String) vRowTemp.elementAt(8);
                if(auxiliarDescripcion.equals("PAGO DE DEDUCIBLE/COASEGURO")&&auxiliarDescripcionAlterna.equals("coaseguro"))
                    oP.setDescripcion("PAGO DE COASEGURO, A REALIZARSE POR EL CLIENTE");
                else if(auxiliarDescripcion.equals("PAGO DE DEDUCIBLE/COASEGURO")&&auxiliarDescripcionAlterna.equals("deducible"))
                    oP.setDescripcion("PAGO DE DEDUCIBLE, A REALIZARSE POR EL CLIENTE");
                oP.setClaveEpisodioMed(((Double) vRowTemp.elementAt(11)).intValue());
                lista.add(oP);
            }
        }
        return lista;
    }
     public List<ConceptoFacturableCaja> getConceptosCoasegurosDeduciblesFacturasCuentasEmpresas(int paciente,int episodio)throws Exception{
        ConceptoFacturableCaja oP=null;
        Vector rst = null;
        Vector<ConceptoFacturableCaja> vObj = null;
        List<ConceptoFacturableCaja> lista=new ArrayList();
        String sQuery = "";
        int i=0;
        sQuery = "select * from buscaconceptosfacturascoasegurosdeduciblesCuentasempresas("+paciente+","+episodio+")";
        if (getAD() == null){
            setAD(new AccesoDatos());
            getAD().conectar();
        }
        rst = getAD().ejecutarConsulta(sQuery);
        getAD().desconectar();
        setAD(null);
        String auxiliarDescripcion, auxiliarDescripcionAlterna="";
        if (rst != null) {
            vObj = new Vector<ConceptoFacturableCaja>();
            for (i = 0; i < rst.size(); i++) {
                oP = new ConceptoFacturableCaja();
                Vector vRowTemp = (Vector)rst.elementAt(i); 
                oP.setFolioOCC(((Double) vRowTemp.elementAt(0)).intValue());
                oP.setSecOCC(((Double) vRowTemp.elementAt(1)).intValue());
                oP.setFolioOC((""+((Double) vRowTemp.elementAt(2)).intValue()));
                oP.setFolioPac(((Double) vRowTemp.elementAt(3)).intValue());
                oP.setFolioServicio((String) vRowTemp.elementAt(4));
                oP.setCantidad(((Double) vRowTemp.elementAt(5)).intValue());
                oP.setUnidad((String) vRowTemp.elementAt(6));
                oP.setPctIVa(((Double) vRowTemp.elementAt(10)).intValue());
                oP.setCostoUnitario(new BigDecimal(((Double) vRowTemp.elementAt(9)).floatValue()));//costo cobrado
                auxiliarDescripcion=(String) vRowTemp.elementAt(7);
                auxiliarDescripcionAlterna=(String) vRowTemp.elementAt(8);
                if(auxiliarDescripcion.equals("PAGO DE DEDUCIBLE/COASEGURO")&&auxiliarDescripcionAlterna.equals("coaseguro"))
                    oP.setDescripcion("PAGO DE COASEGURO, A REALIZARSE POR EL CLIENTE");
                else if(auxiliarDescripcion.equals("PAGO DE DEDUCIBLE/COASEGURO")&&auxiliarDescripcionAlterna.equals("deducible"))
                    oP.setDescripcion("PAGO DE DEDUCIBLE, A REALIZARSE POR EL CLIENTE");
                oP.setClaveEpisodioMed(((Double) vRowTemp.elementAt(11)).intValue());
                lista.add(oP);
            }
        }
        return lista;
    }
   //YA______ Considera los conceptos que no son de cuentas de hospital
    public List<ConceptoFacturableCaja> getConceptoFacturasParticulares()throws Exception{
        ConceptoFacturableCaja oP=null;
        Vector rst = null;
        Vector<ConceptoFacturableCaja> vObj = null;
        List<ConceptoFacturableCaja> lista=new ArrayList();
        String sQuery = "";
        int i=0;
        sQuery = "select * from buscaconceptosfacturasparticulares()";      //vienen de servicio prestado, son conceptos normales
        if (getAD() == null){
            setAD(new AccesoDatos());
            getAD().conectar();
        }
        rst = getAD().ejecutarConsulta(sQuery);
        if (rst != null) {
            vObj = new Vector<ConceptoFacturableCaja>();
            for (i = 0; i < rst.size(); i++) {
                oP = new ConceptoFacturableCaja();
                Vector vRowTemp = (Vector)rst.elementAt(i); 
                oP.setFolioOCC(((Double) vRowTemp.elementAt(0)).intValue());
                oP.setSecOCC(((Double) vRowTemp.elementAt(1)).intValue());
                oP.setFolioOC((""+((Double) vRowTemp.elementAt(2)).intValue()));
                oP.setFolioPac(((Double) vRowTemp.elementAt(3)).intValue());
                oP.setFolioServicio((String) vRowTemp.elementAt(4));
                oP.setCantidad(((Double) vRowTemp.elementAt(5)).intValue());
                oP.setUnidad((String) vRowTemp.elementAt(6));
                oP.setPctIVa(((Double) vRowTemp.elementAt(10)).intValue());
                oP.setCostoUnitario(new BigDecimal(((Double) vRowTemp.elementAt(9)).floatValue()));//costo cobrado
                oP.setDescripcion((String) vRowTemp.elementAt(7));
                oP.setDescripcionAlterna((String) vRowTemp.elementAt(8));
                lista.add(oP);
            }
        } 
        getAD().desconectar();
        setAD(null);
        return lista;
    }
    //YA______Busqua por número de folio del paciente
    public List<ConceptoFacturableCaja> getConceptoFacturasParticulares(int folioPaciente)throws Exception{
        ConceptoFacturableCaja oP=null;
        Vector rst = null;
        Vector<ConceptoFacturableCaja> vObj = null;
        List<ConceptoFacturableCaja> lista=new ArrayList();
        String sQuery = "select * from buscaconceptosfacturasparticulares("+folioPaciente+")";
        int i=0;
        if (getAD() == null){
            setAD(new AccesoDatos());
            getAD().conectar();
        }
        rst = getAD().ejecutarConsulta(sQuery);
        if (rst != null) {
            vObj = new Vector<ConceptoFacturableCaja>();
            for (i = 0; i < rst.size(); i++) {
                oP = new ConceptoFacturableCaja();
                Vector vRowTemp = (Vector)rst.elementAt(i); 
                oP.setFolioOCC(((Double) vRowTemp.elementAt(0)).intValue());
                oP.setSecOCC(((Double) vRowTemp.elementAt(1)).intValue());
                oP.setFolioOC((""+((Double) vRowTemp.elementAt(2)).intValue()));
                oP.setFolioPac(((Double) vRowTemp.elementAt(3)).intValue());
                oP.setFolioServicio((String) vRowTemp.elementAt(4));
                oP.setCantidad(((Double) vRowTemp.elementAt(5)).intValue());
                oP.setUnidad((String) vRowTemp.elementAt(6));
                oP.setPctIVa(((Double) vRowTemp.elementAt(10)).intValue());
                oP.setCostoUnitario(new BigDecimal(((Double) vRowTemp.elementAt(9)).floatValue()));//costo cobrado
                oP.setDescripcion((String) vRowTemp.elementAt(7));
                oP.setDescripcionAlterna((String) vRowTemp.elementAt(8));
                lista.add(oP);
            }
        }
        getAD().desconectar();
        setAD(null);
        return lista;
    }
    public List<ConceptoFacturableCaja> getConceptoFacturasParticularesPorNombreGeneral(int folioPaciente)throws Exception{
        ConceptoFacturableCaja oP=null;
        Vector rst = null;
        Vector<ConceptoFacturableCaja> vObj = null;
        List<ConceptoFacturableCaja> lista=new ArrayList();
        String sQuery = "select * from buscaconceptosfacturasparticularesPorNombreGeneral("+folioPaciente+")";
        int i=0;
        if (getAD() == null){
            setAD(new AccesoDatos());
            getAD().conectar();
        }
        rst = getAD().ejecutarConsulta(sQuery);
        if (rst != null) {
            vObj = new Vector<ConceptoFacturableCaja>();
            for (i = 0; i < rst.size(); i++) {
                oP = new ConceptoFacturableCaja();
                Vector vRowTemp = (Vector)rst.elementAt(i); 
                oP.setFolioOCC(((Double) vRowTemp.elementAt(0)).intValue());
                oP.setSecOCC(((Double) vRowTemp.elementAt(1)).intValue());
                oP.setFolioOC((""+((Double) vRowTemp.elementAt(2)).intValue()));
                oP.setFolioPac(((Double) vRowTemp.elementAt(3)).intValue());
                oP.setFolioServicio((String) vRowTemp.elementAt(4));
                oP.setCantidad(((Double) vRowTemp.elementAt(5)).intValue());
                oP.setUnidad((String) vRowTemp.elementAt(6));
                oP.setPctIVa(((Double) vRowTemp.elementAt(10)).intValue());
                oP.setCostoUnitario(new BigDecimal(((Double) vRowTemp.elementAt(9)).floatValue()));//costo cobrado
                oP.setDescripcion((String) vRowTemp.elementAt(7));
                oP.setDescripcionAlterna((String) vRowTemp.elementAt(8));
                lista.add(oP);
            }
        }
        getAD().desconectar();
        setAD(null);
        return lista;
    }
    //YA____Se agregaron los cambios para separar tarjeta de crédito de la demás venta
    public List<ConceptoFacturableCaja> getConceptoFacturasPublicoEnGeneralTarjetaCredito()throws Exception{ 
        ConceptoFacturableCaja oP=null;
        Vector rst = null;
        Vector<ConceptoFacturableCaja> vObj = null;
        List<ConceptoFacturableCaja> lista=new ArrayList();
        String sQuery = "select * from buscaconceptosfacturaspublicogralTarjetaCredito()";
        int i=0;
        if (getAD() == null){
            setAD(new AccesoDatos());
            getAD().conectar();
        }
        rst = getAD().ejecutarConsulta(sQuery);
        if (rst != null) {
            vObj = new Vector<ConceptoFacturableCaja>();
            for (i = 0; i < rst.size(); i++) {
                oP = new ConceptoFacturableCaja();
                Vector vRowTemp = (Vector)rst.elementAt(i); 
                oP.setFolioOCC(((Double) vRowTemp.elementAt(0)).intValue());
                oP.setSecOCC(((Double) vRowTemp.elementAt(1)).intValue());
                oP.setFolioOC((""+((Double) vRowTemp.elementAt(2)).intValue()));
                oP.setFolioPac(((Double) vRowTemp.elementAt(3)).intValue());
                oP.setFolioServicio((String) vRowTemp.elementAt(4));
                oP.setCantidad(((Double) vRowTemp.elementAt(5)).intValue());
                oP.setUnidad((String) vRowTemp.elementAt(6));
                oP.setPctIVa(((Double) vRowTemp.elementAt(10)).intValue());
                oP.setCostoUnitario(new BigDecimal(((Double) vRowTemp.elementAt(9)).floatValue()));//costo cobrado
                oP.setDescripcion((String) vRowTemp.elementAt(7));
                oP.setDescripcionAlterna((String) vRowTemp.elementAt(8));
                lista.add(oP);
            }
        }
        //se agregarán los coaseguros que ya pago el cliente pero de los que no requirio factura, es decir ya fueron registrados y 
        //corresponden a una factura de empresa que requirio un coaseguro
        i=0;
        sQuery = "select * from buscaconceptosfacturascoasegurospublicogralTarjetaCredito()";
        rst = getAD().ejecutarConsulta(sQuery);
        if (rst != null) {
            vObj = new Vector<ConceptoFacturableCaja>();
            for (i = 0; i < rst.size(); i++) {
                oP = new ConceptoFacturableCaja();
                Vector vRowTemp = (Vector)rst.elementAt(i); 
                oP.setFolioOCC(((Double) vRowTemp.elementAt(0)).intValue());
                oP.setSecOCC(((Double) vRowTemp.elementAt(1)).intValue());
                oP.setFolioOC(("COA-"+((Double) vRowTemp.elementAt(2)).intValue()));
                oP.setFolioPac(((Double) vRowTemp.elementAt(3)).intValue());
                oP.setPctIVa(((Double) vRowTemp.elementAt(6)).intValue());
                oP.setCostoUnitario(new BigDecimal(((Double) vRowTemp.elementAt(4)).floatValue()));//costo cobrado
                oP.setDescripcion("PAGO DE COASEGURO");
                oP.setCantidad(1);
                oP.setUnidad("No aplica");
                
                lista.add(oP);
            }
        }
         //se agregarán los deducibes que ya pago el cliente, pero que no requirio factura, es decir ya fueron registrados y 
        //corresponden a una factura de empresa que requirio un deducible
        i=0;
        sQuery = "select * from buscaconceptosfacturasdeduciblespublicogralTarjetaCredito()";
        rst = getAD().ejecutarConsulta(sQuery);
        if (rst != null) {
            vObj = new Vector<ConceptoFacturableCaja>();
            for (i = 0; i < rst.size(); i++) {
                oP = new ConceptoFacturableCaja();
                Vector vRowTemp = (Vector)rst.elementAt(i); 
                oP.setFolioOCC(((Double) vRowTemp.elementAt(0)).intValue());
                oP.setSecOCC(((Double) vRowTemp.elementAt(1)).intValue());
                oP.setFolioOC(("DED-"+((Double) vRowTemp.elementAt(2)).intValue()));
                oP.setFolioPac(((Double) vRowTemp.elementAt(3)).intValue());
                oP.setPctIVa(((Double) vRowTemp.elementAt(6)).intValue());
                oP.setCostoUnitario(new BigDecimal(((Double) vRowTemp.elementAt(4)).floatValue()));//costo cobrado
                oP.setDescripcion("PAGO DE DEDUCIBLE");
                oP.setCantidad(1);
                oP.setUnidad("No aplica");
                
                lista.add(oP);
            }
        }
        //se consideran aquellos conceptos de rentas para aquellas personas hicieron el pago pero no requierieron factura
        i=0;
        sQuery = "select * from buscaconceptosfacturasrentaspublicogralTarjetaCredito()";
        rst = getAD().ejecutarConsulta(sQuery);
        if (rst != null) {
            vObj = new Vector<ConceptoFacturableCaja>();
            for (i = 0; i < rst.size(); i++) {
                oP = new ConceptoFacturableCaja();
                Vector vRowTemp = (Vector)rst.elementAt(i); 
                oP.setFolioOCC(((Double) vRowTemp.elementAt(0)).intValue());
                oP.setSecOCC(((Double) vRowTemp.elementAt(1)).intValue());
                oP.setFolioOC((""+((Double) vRowTemp.elementAt(2)).intValue()));
                oP.setPctIVa(((Double) vRowTemp.elementAt(6)).intValue());
                oP.setCostoUnitario(new BigDecimal(((Double) vRowTemp.elementAt(3)).intValue()));//costo cobrado
                oP.setDescripcion((String) vRowTemp.elementAt(5));              //Se guarda por si fuera necesario después
                oP.setDescripcionAlterna((String) vRowTemp.elementAt(4));       //Por petición de usuario, se solicito que esta fuera la principal descripción
                oP.setCantidad(1);
                oP.setUnidad("No aplica");
                lista.add(oP);
            }
        }
        getAD().desconectar();
        setAD(null);
        return lista;
    }
    //YA____las funciones consideran cualquier pago cuyo método no sea tarjeta de crédito
    public List<ConceptoFacturableCaja> getConceptoFacturasPublicoEnGeneral()throws Exception{
        ConceptoFacturableCaja oP=null;
        Vector rst = null;
        Vector<ConceptoFacturableCaja> vObj = null;
        List<ConceptoFacturableCaja> lista=new ArrayList();
        String sQuery = "select * from buscaconceptosfacturaspublicogral()";
        int i=0;
        if (getAD() == null){
            setAD(new AccesoDatos());
            getAD().conectar();
        }
        rst = getAD().ejecutarConsulta(sQuery);
        if (rst != null) {
            vObj = new Vector<ConceptoFacturableCaja>();
            for (i = 0; i < rst.size(); i++) {
                oP = new ConceptoFacturableCaja();
                Vector vRowTemp = (Vector)rst.elementAt(i); 
                oP.setFolioOCC(((Double) vRowTemp.elementAt(0)).intValue());
                oP.setSecOCC(((Double) vRowTemp.elementAt(1)).intValue());
                oP.setFolioOC((""+((Double) vRowTemp.elementAt(2)).intValue()));
                oP.setFolioPac(((Double) vRowTemp.elementAt(3)).intValue());
                oP.setFolioServicio((String) vRowTemp.elementAt(4));
                oP.setCantidad(((Double) vRowTemp.elementAt(5)).intValue());
                oP.setUnidad((String) vRowTemp.elementAt(6));
                oP.setPctIVa(((Double) vRowTemp.elementAt(10)).intValue());
                oP.setCostoUnitario(new BigDecimal(((Double) vRowTemp.elementAt(9)).floatValue()));//costo cobrado
                oP.setDescripcion((String) vRowTemp.elementAt(7));
                oP.setDescripcionAlterna((String) vRowTemp.elementAt(8));
                lista.add(oP);
            }
        }
        //se agregarán los coaseguros que ya pago el cliente pero de los que no requirio factura, es decir ya fueron registrados y 
        //corresponden a una factura de empresa que requirio un coaseguro
        i=0;
        sQuery = "select * from buscaconceptosfacturascoasegurospublicogral()";
        rst = getAD().ejecutarConsulta(sQuery);
        if (rst != null) {
            vObj = new Vector<ConceptoFacturableCaja>();
            for (i = 0; i < rst.size(); i++) {
                oP = new ConceptoFacturableCaja();
                Vector vRowTemp = (Vector)rst.elementAt(i); 
                oP.setFolioOCC(((Double) vRowTemp.elementAt(0)).intValue());
                oP.setSecOCC(((Double) vRowTemp.elementAt(1)).intValue());
                oP.setFolioOC(("COA-"+((Double) vRowTemp.elementAt(2)).intValue()));
                oP.setFolioPac(((Double) vRowTemp.elementAt(3)).intValue());
                oP.setPctIVa(((Double) vRowTemp.elementAt(6)).intValue());
                oP.setCostoUnitario(new BigDecimal(((Double) vRowTemp.elementAt(4)).floatValue()));//costo cobrado
                oP.setDescripcion("PAGO DE COASEGURO");
                oP.setCantidad(1);
                oP.setUnidad("No aplica");
                
                lista.add(oP);
            }
        }
         //se agregarán los deducibes que ya pago el cliente, pero que no requirio factura, es decir ya fueron registrados y 
        //corresponden a una factura de empresa que requirio un deducible
        i=0;
        sQuery = "select * from buscaconceptosfacturasdeduciblespublicogral()";
        rst = getAD().ejecutarConsulta(sQuery);
        if (rst != null) {
            vObj = new Vector<ConceptoFacturableCaja>();
            for (i = 0; i < rst.size(); i++) {
                oP = new ConceptoFacturableCaja();
                Vector vRowTemp = (Vector)rst.elementAt(i); 
                oP.setFolioOCC(((Double) vRowTemp.elementAt(0)).intValue());
                oP.setSecOCC(((Double) vRowTemp.elementAt(1)).intValue());
                oP.setFolioOC(("DED-"+((Double) vRowTemp.elementAt(2)).intValue()));
                oP.setFolioPac(((Double) vRowTemp.elementAt(3)).intValue());
                oP.setPctIVa(((Double) vRowTemp.elementAt(6)).intValue());
                oP.setCostoUnitario(new BigDecimal(((Double) vRowTemp.elementAt(4)).floatValue()));//costo cobrado
                oP.setDescripcion("PAGO DE DEDUCIBLE");
                oP.setCantidad(1);
                oP.setUnidad("No aplica");
                
                lista.add(oP);
            }
        }
        //se consideran aquellos conceptos de rentas para aquellas personas hicieron el pago pero no requierieron factura
        i=0;
        sQuery = "select * from buscaconceptosfacturasrentaspublicogral()";
        rst = getAD().ejecutarConsulta(sQuery);
        if (rst != null) {
            vObj = new Vector<ConceptoFacturableCaja>();
            for (i = 0; i < rst.size(); i++) {
                oP = new ConceptoFacturableCaja();
                Vector vRowTemp = (Vector)rst.elementAt(i); 
                oP.setFolioOCC(((Double) vRowTemp.elementAt(0)).intValue());
                oP.setSecOCC(((Double) vRowTemp.elementAt(1)).intValue());
                oP.setFolioOC((""+((Double) vRowTemp.elementAt(2)).intValue()));
                oP.setPctIVa(((Double) vRowTemp.elementAt(6)).intValue());
                oP.setCostoUnitario(new BigDecimal(((Double) vRowTemp.elementAt(3)).intValue()));//costo cobrado
                oP.setDescripcion((String) vRowTemp.elementAt(5));               //Se guarda por si fuera necesario después
                oP.setDescripcionAlterna((String) vRowTemp.elementAt(4));       //Descripción solicitada para el caso de rentas, esa se vera en la factura
                oP.setCantidad(1);
                oP.setUnidad("No aplica");
                lista.add(oP);
            }
        }
        getAD().desconectar();
        setAD(null);
        return lista;
    }
    //YA____Condiera los conceptos por renta que son registrados en caja
    public List<ConceptoFacturableCaja> getConceptoFacturasRentas()throws Exception{
        ConceptoFacturableCaja oP=null;
        Vector rst = null;
        Vector<ConceptoFacturableCaja> vObj = null;
        List<ConceptoFacturableCaja> lista=new ArrayList();
        String sQuery = "select * from buscaConceptosFacturasRentas()";
        int i=0;
        if (getAD() == null){
            setAD(new AccesoDatos());
            getAD().conectar();
        }
        rst = getAD().ejecutarConsulta(sQuery);
        getAD().desconectar();
        setAD(null);
        if (rst != null) {
            vObj = new Vector<ConceptoFacturableCaja>();
            for (i = 0; i < rst.size(); i++) {
                oP = new ConceptoFacturableCaja();
                Vector vRowTemp = (Vector)rst.elementAt(i); 
                oP.setFolioOCC(((Double) vRowTemp.elementAt(0)).intValue());
                oP.setSecOCC(((Double) vRowTemp.elementAt(1)).intValue());
                oP.setFolioOC((""+((Double) vRowTemp.elementAt(2)).intValue()));
                oP.setPctIVa(((Double) vRowTemp.elementAt(6)).intValue());
                oP.setCostoUnitario(new BigDecimal(((Double) vRowTemp.elementAt(3)).intValue()));//costo cobrado
                oP.setDescripcion((String) vRowTemp.elementAt(5));          //Guarda el nombre indicado para rentas, es el que solicitaron aparezca en la factura
                oP.setDescripcionAlterna((String) vRowTemp.elementAt(4));   //Por si acaso fuera necesario guarda la descripción original
                oP.setCantidad(1);
                oP.setUnidad("No aplica");
                lista.add(oP);
            }
        }
        return lista;
    }
    public BigDecimal getCostoUnitario() {
        return this.oCostoUnitario;
    }
    public void setCostoUnitario(BigDecimal costo) {
        //Recibe el costo del producto con IVA, va a determinar cual fue el costo sin IVA y cual será el importe de los impuestos
        float iva=(this.nPctIVa/100.0f)+1.0f;
        this.oCostoUnitario=costo.divide(new BigDecimal(iva), 2, RoundingMode.CEILING); 
        setImpuestoUnitario(costo.subtract(oCostoUnitario));
    }
    public BigDecimal getImpuestoUnitario() {
        return oImpuesto;
    }
    public void setImpuestoUnitario(BigDecimal impuesto) {
        this.oImpuesto = impuesto;
    }
    public BigDecimal getImpuestoTotal() {
        this.oImpuesto=getImpuestoUnitario().multiply(new BigDecimal(nCantidad));
        return oImpuesto;
    }
    public void setImpuestoTotal(BigDecimal impuesto) {
        this.oImpuesto = impuesto;
    }
    public BigDecimal getImporteTotalSinIva() {
        this.oImporte=new BigDecimal(nCantidad).multiply(getCostoUnitario(),MathContext.DECIMAL32);
        this.oImporte=new BigDecimal(""+Math.rint(oImporte.floatValue()*100)/100);
        return this.oImporte;
    }
    public void setImporteTotalSinIva(BigDecimal importe) {
        this.oImporte = importe;
    }
    public void setCostoUnitarioSinIva(BigDecimal costoUnitario){
        this.oCostoUnitario=costoUnitario;
        BigDecimal impuestoUnitario=oCostoUnitario.multiply(new BigDecimal(this.nPctIVa*.01));
        impuestoUnitario=new BigDecimal(""+Math.rint(impuestoUnitario.floatValue()*100)/100);
        setImpuestoUnitario(impuestoUnitario);
    }
    
    public int getCantidad() {
        return nCantidad;
    }
    public void setCantidad(int nCantidad) {
        this.nCantidad = nCantidad;
    }
    public String getDescripcion() {
        return sDescripcion;
    }
    public void setDescripcion(String sDescripcion) {
        this.sDescripcion = sDescripcion+". TASA DE IVA APLICABLE "+this.nPctIVa+"%";
    }
    public String getUnidad() {
        return sUnidad;
    }
    public void setUnidad(String sUnidad) {
        this.sUnidad = sUnidad;
    }    
    public int getPctIVa() {
        return nPctIVa;
    }

    public void setPctIVa(int nPctIVa) {
        this.nPctIVa = nPctIVa;
    }
    public String getFolioOC() {
        return sFolioOC;
    }

    public void setFolioOC(String sFolioOC) {
        this.sFolioOC = sFolioOC;
    }

    public int getFolioOCC() {
        return nFolioOCC;
    }

    public void setFolioOCC(int nFolioOCC) {
        this.nFolioOCC = nFolioOCC;
    }

    public int getFolioPac() {
        return nFolioPac;
    }

    public void setFolioPac(int nFolioPac) {
        this.nFolioPac = nFolioPac;
    }

    public int getSecOCC() {
        return nSecOCC;
    }

    public void setSecOCC(int nSecOCC) {
        this.nSecOCC = nSecOCC;
    }

    public String getFolioServicio() {
        return sFolioServicio;
    }

    public void setFolioServicio(String sFolioServicio) {
        this.sFolioServicio = sFolioServicio;
    }    
    public AccesoDatos getAD() {
        return oAD;
    }
    public void setAD(AccesoDatos oAD) {
        this.oAD = oAD;
    }
    
    public ConceptoFacturableCaja(){
        
    }
    public String getDescripcionAlterna() {
        return sDescripcionAlterna;
    }

    public void setDescripcionAlterna(String sDescripcionAlterna) {
        this.sDescripcionAlterna = sDescripcionAlterna;
    }
    public BigDecimal getDescuento() {
        return oDescuento;
    }

    public void setDescuento(BigDecimal oDescuento) {
        this.oDescuento = oDescuento;
    }
    
    public int getClaveEpisodioMed() {
        return nClaveEpisodioMed;
    }

    public void setClaveEpisodioMed(int nClaveEpisodioMed) {
        this.nClaveEpisodioMed = nClaveEpisodioMed;
    }
}