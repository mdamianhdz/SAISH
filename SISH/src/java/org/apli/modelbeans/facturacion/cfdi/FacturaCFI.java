package org.apli.modelbeans.facturacion.cfdi;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import org.apli.AD.AccesoDatos;
import org.apli.modelbeans.EpisodioCiaCred;
import org.apli.modelbeans.EpisodioMedico;
import org.apli.modelbeans.OpeCajaConcepto;
import org.apli.modelbeans.Paciente;
import org.apli.modelbeans.facturacion.cfdi.v32.schema.Comprobante;
import org.apli.modelbeans.ventaCredito.CompaniaCred;

public class FacturaCFI implements Serializable{
    private int nPctIVA;
    private String sUuid;
    private String sTipoDetalle;
    private int nLineaDetalleCFDINotaCredito;
    List<DetalleCFDIOpeCajaConcepto> listaDetalles=new ArrayList<DetalleCFDIOpeCajaConcepto>();
    List<DetalleCFDIServicio> listaDetallesServ=new ArrayList<DetalleCFDIServicio>();
    private String sNombreSerie;
    private int nFolio;
    private String sVersion;
    private String sRfcEmisor ;
    private String sRfcReceptor;
    private char sTipoComprobante;
    private char sTipoDocumento ;
    private String sMetodoPago;
    private String sFormaPago;
    private String sNumCtaPago;
    private String sMoneda;
    private int nIdCoaseguro;
    private int nIdDeducible;
    private float nImporteCoaseguro;
    private float nImporteDeducible;
    private int nIdTraslado;
    private float nSubtotal;
    private float nImporteTotal;
    private float nDescuentos;
    private float nImpuestos;
    private Date dExpedicion;
    private String sSello;
    private String sNoCertificado;
    private String sCertificado;
    private String sLugarExpedicion;
    private String sSituacion;
    private String sObsIncobrable;
    private ArrayList<Integer> nFoliosConceptosCaja;
    private Paciente oPaciente;
    private AccesoDatos oAD;
    private Comprobante.Emisor oEmisor;
    private Comprobante.Receptor oReceptor;
    private CompaniaCred oCompCred;
    private EpisodioCiaCred oEpCiaCred;
    private OpeCajaConcepto oOpCC;
    String sQuery="";
    private final String sRutaXML="//BovedaFiscal//Propias//xml//";
	
	//método de Isabel
    public void limpiarDatosBusqueda(){
        this.setTipoDetalle(null);
        this.setRfcEmisor(null);
        this.setRfcReceptor(null);
        this.setIdCoaseguro(0);
        this.setIdDeducible(0);
        this.setImporteCoaseguro(0.0f);
        this.setImporteDeducible(0.0f);
        this.setIdTraslado(0);
        this.setSubtotal(0.0f);
        this.setImpuestos(0.0f);
        this.setImporteTotal(0.0f);
        this.setExpedicion(null);
        this.setUuid(null);
        this.listaDetalles.clear();
        this.listaDetallesServ.clear();
    }
	//método de Isabel
    public String ejecutarQuery() throws Exception{
        Vector rst = null;
        if (getAD() == null){
                setAD(new AccesoDatos());
		getAD().conectar();
        }
        rst = getAD().ejecutarConsulta(sQuery);
        getAD().desconectar();
        setAD(null);
        return " "+rst.get(0);
    }
    //método de Isabel
    public void generarQueryParaCancelarCFDI() throws Exception{
        if(this.getTipoDocumento()=='0'){
            //Elimina primero la parte mas baja de las jeraquias: paquetes y servicios
            //Paquetes: se eliminan los registros de conceptos de paquete asociados a la factura a cancelar (se paga un abono de paquete, la factura considera conceptos del paquete)
            if(sTipoDetalle.equals("2")){   
                sQuery+="select * from eliminarDetalleCFDIPaq('"+this.sNombreSerie+"'::character varying,"+this.nFolio+"::int2);";
            }
            //Cuentas: Elimina los registros de servicios consumidos asociados a la factura a cancelar (se pago un abono y se 
            //facturaron por ese abono los distintos servicios consumidos, no pagados uno a uno, pagados por abono a cuenta)  Empresas y cuentas de hospital
            else if(sTipoDetalle.equals("3")){
                sQuery+="select * from eliminarDetalleCFDIServ('"+this.sNombreSerie+"'::character varying,"+this.nFolio+"::int2);";
                for(DetalleCFDIServicio detalle:listaDetallesServ){
                    sQuery+="select * from modificarServicioPrestadoPorcancelacioncfdi('"+detalle.getIdFolioServPres()+"'::character varying);";
                }
            }
            //parte común para todas: Cambiar a cancelada la factura, elimina traslados, detalleCFDI y detalleCFDIOpeCajConcep
            sQuery += "select * from cancelarFactura('"+this.sNombreSerie+"'::character varying,"+this.nFolio+"::int2,"+this.nIdTraslado+");";
            //para cada concepto que fue marcado como facturado lo va a modificar a no facturado
            for(DetalleCFDIOpeCajaConcepto detalle:this.listaDetalles){
                //va a cambiar si estan facturados o no los coaseguros o deducibles: publico en general o para cuentas
                if (detalle.getTipo().contains("PAGO DE COASEGURO") &&(sTipoDetalle.equals("1")|sTipoDetalle.equals("3"))&&this.nIdCoaseguro!=0){
                    sQuery+="select * from modificarCoaseguroPorCancelacionCfdi("+this.nIdCoaseguro+");";
                }else if (detalle.getTipo().contains("PAGO DE DEDUCIBLE") &&(sTipoDetalle.equals("1")|sTipoDetalle.equals("3"))&&this.nIdDeducible!=0){
                    sQuery+="select * from modificarDeduciblePorCancelacionCfdi("+this.nIdDeducible+");";
                }else{
                    sQuery += "select * from modificarOperacionCajaPorCancelacionCfdi("+detalle.getFolioCaja()+");";
                }
            } 
        }else if(this.getTipoDocumento()=='1'){
            sQuery += "select * from cancelarFactura('"+this.sNombreSerie+"'::character varying,"+this.nFolio+"::int2,"+this.nIdTraslado+");";
        }
    }
	//método de Isabel
    public String consultarFacturaGenerada()throws Exception{
            this.limpiarDatosBusqueda();
            Vector rst = null,vRowTemp;
            Vector<FacturaCFI> vObj = null;
            String sQuery;
            if (getAD() == null){
                setAD(new AccesoDatos());
                getAD().conectar();
            }        
            sQuery = " select * from buscadatosdefacturaResumen('"+this.sNombreSerie+"'::character varying,"+this.nFolio+"::int2)";
            System.out.println(sQuery);
            this.listaDetalles=new ArrayList<DetalleCFDIOpeCajaConcepto>();
            rst = getAD().ejecutarConsulta(sQuery);
            if (rst != null && !rst.isEmpty() ) {
                vObj = new Vector<FacturaCFI>();
                for (int i = 0; i < rst.size(); i++) {
                    vRowTemp = (Vector)rst.elementAt(i); 
                    if(i==0){
                        this.setTipoDetalle((String) vRowTemp.elementAt(0));
                        this.setRfcEmisor((String) vRowTemp.elementAt(1));
                        this.setRfcReceptor((String) vRowTemp.elementAt(2));
                        if(vRowTemp.elementAt(3)!=null){
                            this.setIdCoaseguro(((Double) vRowTemp.elementAt(3)).intValue());
                            if(nIdCoaseguro!=0)
                                buscarImporteCoaseguro(nIdCoaseguro);
                        }
                        if(vRowTemp.elementAt(4)!=null){
                            this.setIdDeducible(((Double) vRowTemp.elementAt(4)).intValue());
                            if(nIdDeducible!=0)
                                buscarImporteDeducible(nIdDeducible);
                        }
                        this.setIdTraslado(((Double) vRowTemp.elementAt(5)).intValue());
                        this.setSubtotal(((Double) vRowTemp.elementAt(6)).floatValue());
                        this.setImpuestos(((Double) vRowTemp.elementAt(7)).floatValue());
                        this.setImporteTotal(((Double) vRowTemp.elementAt(8)).floatValue());
                        this.setExpedicion((Date) vRowTemp.elementAt(9));
                        this.setUuid((String) vRowTemp.elementAt(10));
                    }
                }
            }else{
                getAD().desconectar();
                setAD(null); 
                return rst.toString();
            }
            if (getAD() != null){
                getAD().desconectar();
                setAD(null); 
            }    
            return "Se encontro la factura";
    }
	//médoto de Isabel
    public String buscarFacturaParaNotaCredito()throws Exception{
       this.limpiarDatosBusqueda();
        Vector rst = null,vRowTemp;
        Vector<FacturaCFI> vObj = null;
        String sQuery = "select * from verificafacturaParaNotaCredito('"+this.sNombreSerie+"'::character varying,"+this.nFolio+"::int2)";
        if (getAD() == null){
            setAD(new AccesoDatos());
            getAD().conectar();
        }
        rst = getAD().ejecutarConsulta(sQuery);
        if(!rst.toString().contains("ERROR")){
            sQuery = "select * from buscadatosdefacturaparaNotaCredito('"+this.sNombreSerie+"'::character varying,"+this.nFolio+"::int2);";
            rst = getAD().ejecutarConsulta(sQuery);
            if (rst != null && !rst.isEmpty() ) {
                        vObj = new Vector<FacturaCFI>();
                        vRowTemp = (Vector)rst.elementAt(0); 
                        this.setRfcEmisor((String) vRowTemp.elementAt(0));
                        this.setRfcReceptor((String) vRowTemp.elementAt(1));
                        if(vRowTemp.elementAt(2)!=null){
                            this.setIdCoaseguro(((Double) vRowTemp.elementAt(2)).intValue());
                            if(nIdCoaseguro!=0)
                                buscarImporteCoaseguro(nIdCoaseguro);
                        }
                        if(vRowTemp.elementAt(3)!=null){
                            this.setIdDeducible(((Double) vRowTemp.elementAt(3)).intValue());
                            if(nIdDeducible!=0)
                                buscarImporteDeducible(nIdDeducible);
                        }
                        this.setSubtotal(((Double) vRowTemp.elementAt(4)).floatValue());
                        this.setImpuestos(((Double) vRowTemp.elementAt(5)).floatValue());
                        this.setExpedicion((Date) vRowTemp.elementAt(6));
                        this.setLugarExpedicion((String) vRowTemp.elementAt(7));
                        this.setImporteTotal(((Double) vRowTemp.elementAt(8)).floatValue());
                        this.setTipoDetalle((String) vRowTemp.elementAt(9));
                        this.oReceptor=new Comprobante.Receptor();
                        oReceptor.setRfc((String) vRowTemp.elementAt(10));
                        oReceptor.setNombre((String) vRowTemp.elementAt(11));
                        oReceptor.setCorreoElectronico((String) vRowTemp.elementAt(12));
                        oReceptor.setDomicilio(oReceptor.buscarDomicilio(((Double) vRowTemp.elementAt(13)).intValue()));
                        this.oEmisor=new Comprobante.Emisor();
                        oEmisor.setRfc((String) vRowTemp.elementAt(14));
                        oEmisor.setNombre((String) vRowTemp.elementAt(15));
                        oEmisor.setDomicilioFiscal(oEmisor.buscarDomicilioFiscal(((Double) vRowTemp.elementAt(16)).intValue()));
                        oEmisor.setExpedidoEn(oEmisor.buscarDomicilio(((Double) vRowTemp.elementAt(17)).intValue()));
                        oEmisor.setDom_fiscal_dom_exp(((String) vRowTemp.elementAt(18)).charAt(0));
                        this.setPctIVA(((Double) vRowTemp.elementAt(19)).intValue());
            }   
        }else{
            if (getAD() != null){
                getAD().desconectar();
                setAD(null);
            }
            return rst.toString();
        }
        if (getAD() != null){
            getAD().desconectar();
            setAD(null); 
        }
        return "Se encontro la factura para cancelar"; 
    }
    //médoto de Isabel
    public String buscarFacturaParaCancelar() throws Exception{
        this.limpiarDatosBusqueda();
        Vector rst = null,vRowTemp;
        Vector<FacturaCFI> vObj = null;
        String sQuery = "select * from verificaSiFacturaPuedeCancelarse('"+this.sNombreSerie+"'::character varying,"+this.nFolio+"::int2)";
        if (getAD() == null){
            setAD(new AccesoDatos());
            getAD().conectar();
        }
        rst = getAD().ejecutarConsulta(sQuery);
        if(!rst.toString().contains("ERROR")){
            if(rst.toString().contains("CFDI para cancelar")){
                this.setTipoDocumento('0');
                sQuery = " select * from buscaDatosDefacturaParaCancelar('"+this.sNombreSerie+"'::character varying,"+this.nFolio+"::int2)";
                this.listaDetalles=new ArrayList<DetalleCFDIOpeCajaConcepto>();
                rst = getAD().ejecutarConsulta(sQuery);
                if (rst != null && !rst.isEmpty() ) {
                    vObj = new Vector<FacturaCFI>();
                    for (int i = 0; i < rst.size(); i++) {
                        vRowTemp = (Vector)rst.elementAt(i); 
                        if(i==0){
                            this.setTipoDetalle((String) vRowTemp.elementAt(0));
                            this.setRfcEmisor((String) vRowTemp.elementAt(1));
                            this.setRfcReceptor((String) vRowTemp.elementAt(2));
                            if(vRowTemp.elementAt(3)!=null){
                                this.setIdCoaseguro(((Double) vRowTemp.elementAt(3)).intValue());
                                if(nIdCoaseguro!=0)
                                    buscarImporteCoaseguro(nIdCoaseguro);
                            }
                            if(vRowTemp.elementAt(4)!=null){
                                this.setIdDeducible(((Double) vRowTemp.elementAt(4)).intValue());
                                if(nIdDeducible!=0)
                                    buscarImporteDeducible(nIdDeducible);
                            }
                            this.setIdTraslado(((Double) vRowTemp.elementAt(5)).intValue());
                            this.setSubtotal(((Double) vRowTemp.elementAt(6)).floatValue());
                            this.setImpuestos(((Double) vRowTemp.elementAt(7)).floatValue());
                            this.setImporteTotal(((Double) vRowTemp.elementAt(8)).floatValue());
                            this.setExpedicion((Date) vRowTemp.elementAt(13));
                            this.setUuid((String) vRowTemp.elementAt(14));
                        }
                        DetalleCFDIOpeCajaConcepto detalle=new DetalleCFDIOpeCajaConcepto();
                        detalle.setLinCfdi(((Double) vRowTemp.elementAt(9)).intValue());
                        detalle.setTipo((String) vRowTemp.elementAt(10));  //aqui se va a guardar la descripcion del detalleCFDI
                        detalle.setFolioCaja(((Double) vRowTemp.elementAt(11)).intValue());
                        detalle.setSec(((Double) vRowTemp.elementAt(12)).intValue());
                        this.listaDetalles.add(detalle);
                    }
                }   
                sQuery = " select * from buscaDetalleCFDIServParaCancelarCFDI('"+this.sNombreSerie+"'::character varying,"+this.nFolio+"::int2)";
                setAD(new AccesoDatos());
                getAD().conectar();
                rst = getAD().ejecutarConsulta(sQuery);
                if (rst != null && !rst.isEmpty() ) {
                        DetalleCFDIServicio detalleServ;
                        vObj = new Vector<FacturaCFI>();
                        for (int i = 0; i < rst.size(); i++) {
                            vRowTemp = (Vector)rst.elementAt(i); 
                            detalleServ=new DetalleCFDIServicio();
                            detalleServ.setIdFolioServPres((String) vRowTemp.elementAt(0));
                            listaDetallesServ.add(detalleServ);
                        }
                }  
            }
            else{
                this.setTipoDocumento('1');
                sQuery = " select * from buscadatosdeNotaCreditoparacancelar('"+this.sNombreSerie+"'::character varying,"+this.nFolio+"::int2)";
                this.listaDetalles=new ArrayList<DetalleCFDIOpeCajaConcepto>();
                rst = getAD().ejecutarConsulta(sQuery);
                if (rst != null && !rst.isEmpty() ) {
                    vObj = new Vector<FacturaCFI>();
                    vRowTemp = (Vector)rst.elementAt(0); 
                    this.setRfcEmisor((String) vRowTemp.elementAt(0));
                    this.setRfcReceptor((String) vRowTemp.elementAt(1));
                    this.setIdTraslado(((Double) vRowTemp.elementAt(2)).intValue());
                    this.setSubtotal(((Double) vRowTemp.elementAt(3)).floatValue());
                    this.setImpuestos(((Double) vRowTemp.elementAt(4)).floatValue());
                    this.setImporteTotal(((Double) vRowTemp.elementAt(5)).floatValue());
                    this.setLineaDetalleCFDINotaCredito(((Double) vRowTemp.elementAt(6)).intValue());
                    this.setExpedicion((Date) vRowTemp.elementAt(7));
                    this.setUuid((String) vRowTemp.elementAt(8));
                } 
            }
        }else{
            getAD().desconectar();
            setAD(null);
            return rst.toString();
        }
        getAD().desconectar();
        setAD(null); 
        return "Se encontro la factura para cancelar";
    }
    //médoto de Isabel
	public void buscarImporteCoaseguro(int id) throws Exception{
        Vector rst = null,vRowTemp;
        String sQuery = "select * from buscaImporteCoaseguroParaCFDI("+id+")";
        if (getAD() == null){
            setAD(new AccesoDatos());
            getAD().conectar();
        }
        rst = getAD().ejecutarConsulta(sQuery);
        getAD().desconectar();
        setAD(null); 
        vRowTemp = (Vector)rst.elementAt(0);  
        String resultado=""+((Double) vRowTemp.elementAt(0)).floatValue();
        if(!resultado.contains("ERROR"))
            setImporteCoaseguro(Float.valueOf(resultado));
    }
    //médoto de Isabel
	public void buscarImporteDeducible(int id) throws Exception{
        Vector rst = null,vRowTemp;
        String sQuery = "select * from buscaImporteDeducibleParaCFDI("+id+")";
        if (getAD() == null){
            setAD(new AccesoDatos());
            getAD().conectar();
        }
        rst = getAD().ejecutarConsulta(sQuery);
        getAD().desconectar();
        setAD(null); 
        vRowTemp = (Vector)rst.elementAt(0);  
        String resultado=""+((Double) vRowTemp.elementAt(0)).floatValue();
        if(!resultado.contains("ERROR"))
            setImporteDeducible(Float.valueOf(resultado));
    }
     //médoto de Liliana
	 public List<FacturaCFI> buscaPendientesCobro() throws Exception{
        List<FacturaCFI> listRet=null;
        FacturaCFI oCFDI;
        
        Vector rst = null;
        String sQuery = "";

        sQuery="select * from buscaFacturasPendientesCobro()";     
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
            Comprobante.Emisor oEm;
            Comprobante.Receptor oRec;
            CompaniaCred oCC;
            Paciente oPac;
            EpisodioCiaCred oECC;
            EpisodioMedico oEM;
            for (int i = 0; i < rst.size(); i++) {
                oCFDI = new FacturaCFI();
                Vector vRowTemp = (Vector)rst.elementAt(i);
                oCFDI.setNombreSerie((String)vRowTemp.elementAt(0));
                oCFDI.setFolio(((Double)vRowTemp.elementAt(1)).intValue());
                oEm=new Comprobante.Emisor();
                oEm.setRfc((String)vRowTemp.elementAt(2));
                oEm.setNombre((String)vRowTemp.elementAt(3));
                oCFDI.setEmisor(oEm);
                oCFDI.setExpedicion((Date)vRowTemp.elementAt(4));
                oRec=new Comprobante.Receptor();
                oRec.setRfc((String)vRowTemp.elementAt(5));
                oRec.setNombre((String)vRowTemp.elementAt(6));
                oCFDI.setReceptor(oRec);
                oCC=new CompaniaCred();
                oCC.setRFC((String)vRowTemp.elementAt(5));
                oCC.setIdEmp(((Double)vRowTemp.elementAt(7)).intValue());
                oCC.setNombreCorto((String)vRowTemp.elementAt(8));
                oCFDI.setCompCred(oCC);
                oPac=new Paciente();
                oPac.setFolioPac(((Double)vRowTemp.elementAt(9)).intValue());
                oPac.setNombre((String)vRowTemp.elementAt(10));
                oPac.setApellidoPaterno((String)vRowTemp.elementAt(11));
                oPac.setApellidoMaterno((String)vRowTemp.elementAt(12));
                oCFDI.setPaciente(oPac);
                oEM=new EpisodioMedico();
                oEM.setCveepisodio(((Double)vRowTemp.elementAt(13)).intValue());
                oECC=new EpisodioCiaCred();
                oECC.setEpisodioM(oEM);
                oECC.setPaciente(oPac);
                oECC.setCompCred(oCC);
                oECC.setNumPoliza((String)vRowTemp.elementAt(14));
                oECC.setNumSiniestro((String)vRowTemp.elementAt(15));
                oCFDI.setEpCiaCred(oECC);
                oCFDI.setImporteTotal(((Double)vRowTemp.elementAt(16)).floatValue());
                listRet.add(oCFDI);
            }
        }
        return listRet;
    }
    
    public List<FacturaCFI> buscaPendientesCobro(int nTipo, String sCompania) throws Exception{
        List<FacturaCFI> listRet=null;
        FacturaCFI oCFDI;
        
        Vector rst = null;
        String sQuery = "";

        sQuery="select * from buscaFacturasPendientesCobro("+nTipo+"::int2, '"+sCompania+"')";     
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
            Comprobante.Emisor oEm;
            Comprobante.Receptor oRec;
            CompaniaCred oCC;
            Paciente oPac;
            EpisodioCiaCred oECC;
            EpisodioMedico oEM;
            for (int i = 0; i < rst.size(); i++) {
                oCFDI = new FacturaCFI();
                Vector vRowTemp = (Vector)rst.elementAt(i);
                oCFDI.setNombreSerie((String)vRowTemp.elementAt(0));
                oCFDI.setFolio(((Double)vRowTemp.elementAt(1)).intValue());
                oEm=new Comprobante.Emisor();
                oEm.setRfc((String)vRowTemp.elementAt(2));
                oEm.setNombre((String)vRowTemp.elementAt(3));
                oCFDI.setEmisor(oEm);
                oCFDI.setExpedicion((Date)vRowTemp.elementAt(4));
                oRec=new Comprobante.Receptor();
                oRec.setRfc((String)vRowTemp.elementAt(5));
                oRec.setNombre((String)vRowTemp.elementAt(6));
                oCFDI.setReceptor(oRec);
                oCC=new CompaniaCred();
                oCC.setRFC((String)vRowTemp.elementAt(5));
                oCC.setIdEmp(((Double)vRowTemp.elementAt(7)).intValue());
                oCC.setNombreCorto((String)vRowTemp.elementAt(8));
                oCFDI.setCompCred(oCC);
                oPac=new Paciente();
                oPac.setFolioPac(((Double)vRowTemp.elementAt(9)).intValue());
                oPac.setNombre((String)vRowTemp.elementAt(10));
                oPac.setApellidoPaterno((String)vRowTemp.elementAt(11));
                oPac.setApellidoMaterno((String)vRowTemp.elementAt(12));
                oCFDI.setPaciente(oPac);
                oEM=new EpisodioMedico();
                oEM.setCveepisodio(((Double)vRowTemp.elementAt(13)).intValue());
                oECC=new EpisodioCiaCred();
                oECC.setEpisodioM(oEM);
                oECC.setPaciente(oPac);
                oECC.setCompCred(oCC);
                oECC.setNumPoliza((String)vRowTemp.elementAt(14));
                oECC.setNumSiniestro((String)vRowTemp.elementAt(15));
                oCFDI.setEpCiaCred(oECC);
                oCFDI.setImporteTotal(((Double)vRowTemp.elementAt(16)).floatValue());
                listRet.add(oCFDI);
            }
        }
        return listRet;
    }
    public List<FacturaCFI> buscaBovedaInterna(String sCondicion) throws Exception{
        List<FacturaCFI> listRet=null;
        FacturaCFI oCFDI;
        
        Vector rst = null;
        String sQuery = "";

        sQuery="select * from buscaBovedaInterna()";  
        if (!sCondicion.equals(""))
            sQuery=sQuery+" where "+sCondicion;
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
            Comprobante.Receptor oRec;
            for (int i = 0; i < rst.size(); i++) {
                oCFDI = new FacturaCFI();
                Vector vRowTemp = (Vector)rst.elementAt(i);
                oCFDI.setNombreSerie((String)vRowTemp.elementAt(0));
                oCFDI.setFolio(((Double)vRowTemp.elementAt(1)).intValue());
                oRec=new Comprobante.Receptor();
                oRec.setRfc((String)vRowTemp.elementAt(2));
                oCFDI.setReceptor(oRec);
                oCFDI.setExpedicion((Date)vRowTemp.elementAt(3));
                oCFDI.setImporteTotal(((Double)vRowTemp.elementAt(4)).floatValue());
                oCFDI.setUuid((String)vRowTemp.elementAt(5));
                listRet.add(oCFDI);
            }
        }
        return listRet;
    }
	//método de Liliana
    public List<FacturaCFI> buscaPagadas() throws Exception{
        List<FacturaCFI> listRet=null;
        FacturaCFI oCFDI;
        
        Vector rst = null;
        String sQuery = "";

        sQuery="select * from buscaFacturasPagadas()";     
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
            Comprobante.Receptor oRec;
            for (int i = 0; i < rst.size(); i++) {
                oCFDI = new FacturaCFI();
                Vector vRowTemp = (Vector)rst.elementAt(i);
                oCFDI.setNombreSerie((String)vRowTemp.elementAt(0));
                oCFDI.setFolio(((Double)vRowTemp.elementAt(1)).intValue());
                oRec=new Comprobante.Receptor();
                oRec.setRfc((String)vRowTemp.elementAt(2));
                oRec.setNombre((String)vRowTemp.elementAt(3));
                oCFDI.setReceptor(oRec);
                listRet.add(oCFDI);
            }
        }
        return listRet;
    }
	public String getNombreXML(){
        String sNomXML=""+(dExpedicion.getYear()+1900);
        if (dExpedicion.getMonth()<9)
            sNomXML=sNomXML+"0"+(dExpedicion.getMonth()+1);
        else
            sNomXML=sNomXML+(dExpedicion.getMonth()+1);
        if (dExpedicion.getDate()<10)
            sNomXML=sNomXML+"0"+(dExpedicion.getDate());
        else
            sNomXML=sNomXML+(dExpedicion.getDate());
        sNomXML=sNomXML+"_"+this.getReceptor().getRfc()+".xml";
        return sNomXML;
    }
    
    public String getRutaXML(){
        return sRutaXML;
    }
    public OpeCajaConcepto getOpCC() {
        return oOpCC;
    }

    public void setOpCC(OpeCajaConcepto oOpCC) {
        this.oOpCC = oOpCC;
    }
     public Paciente getPaciente() {
        return oPaciente;
    }

    public void setPaciente(Paciente oPaciente) {
        this.oPaciente = oPaciente;
    }
    public float getImporteTotal() {
        return nImporteTotal;
    }

    public void setImporteTotal(float nImporteTotal) {
        this.nImporteTotal = nImporteTotal;
    }
    public int getIdTraslado() {
        return nIdTraslado;
    }

    public void setIdTraslado(int nIdTraslado) {
        this.nIdTraslado = nIdTraslado;
    }
    public String getUuid() {
        return sUuid;
    }

    public void setUuid(String sUuid) {
        this.sUuid = sUuid;
    }
    public Date getExpedicion() {
        return dExpedicion;
    }

    public void setExpedicion(Date dExpedicion) {
        this.dExpedicion = dExpedicion;
    }

    public float getDescuentos() {
        return nDescuentos;
    }

    public void setDescuentos(float nDescuentos) {
        this.nDescuentos = nDescuentos;
    }

    public int getFolio() {
        return nFolio;
    }

    public void setFolio(int nFolio) {
        this.nFolio = nFolio;
    }

    public int getIdCoaseguro() {
        return nIdCoaseguro;
    }

    public void setIdCoaseguro(int nIdCoaseguro) {
        this.nIdCoaseguro = nIdCoaseguro;
    }

    public int getIdDeducible() {
        return nIdDeducible;
    }

    public void setIdDeducible(int nIdDeducible) {
        this.nIdDeducible = nIdDeducible;
    }

    public float getImpuestos() {
        return nImpuestos;
    }

    public void setImpuestos(float nImpuestos) {
        this.nImpuestos = nImpuestos;
    }

    public float getSubtotal() {
        return nSubtotal;
    }

    public void setSubtotal(float nSubtotal) {
        this.nSubtotal = nSubtotal;
    }

    public String getCertificado() {
        return sCertificado;
    }

    public void setCertificado(String sCertificado) {
        this.sCertificado = sCertificado;
    }

    public String getFormaPago() {
        return sFormaPago;
    }

    public void setFormaPago(String sFormaPago) {
        this.sFormaPago = sFormaPago;
    }

    public String getLugarExpedicion() {
        return sLugarExpedicion;
    }

    public void setLugarExpedicion(String sLugarExpedicion) {
        this.sLugarExpedicion = sLugarExpedicion;
    }

    public String getMetodoPago() {
        return sMetodoPago;
    }

    public void setMetodoPago(String sMetodoPago) {
        this.sMetodoPago = sMetodoPago;
    }

    public String getMoneda() {
        return sMoneda;
    }

    public void setMoneda(String sMoneda) {
        this.sMoneda = sMoneda;
    }

    public String getNoCertificado() {
        return sNoCertificado;
    }

    public void setNoCertificado(String sNoCertificado) {
        this.sNoCertificado = sNoCertificado;
    }

    public String getNombreSerie() {
        return sNombreSerie;
    }

    public void setNombreSerie(String sNombreSerie) {
        this.sNombreSerie = sNombreSerie;
    }

    public String getNumCtaPago() {
        return sNumCtaPago;
    }

    public void setNumCtaPago(String sNumCtaPago) {
        this.sNumCtaPago = sNumCtaPago;
    }

    public String getObsIncobrable() {
        return sObsIncobrable;
    }

    public void setObsIncobrable(String sObsIncobrable) {
        this.sObsIncobrable = sObsIncobrable;
    }

    public String getRfcEmisor() {
        return sRfcEmisor;
    }

    public void setRfcEmisor(String sRfcEmisor) {
        this.sRfcEmisor = sRfcEmisor;
    }

    public String getRfcReceptor() {
        return sRfcReceptor;
    }

    public void setRfcReceptor(String sRfcReceptor) {
        this.sRfcReceptor = sRfcReceptor;
    }

    public String getSello() {
        return sSello;
    }

    public void setSello(String sSello) {
        this.sSello = sSello;
    }

    public String getSituacion() {
        return sSituacion;
    }

    public void setSituacion(String sSituacion) {
        this.sSituacion = sSituacion;
    }

    public char getTipoComprobante() {
        return sTipoComprobante;
    }

    public void setTipoComprobante(char sTipoComprobante) {
        this.sTipoComprobante = sTipoComprobante;
    }

    public char getTipoDocumento() {
        return sTipoDocumento;
    }

    public void setTipoDocumento(char sTipoDocumento) {
        this.sTipoDocumento = sTipoDocumento;
    }

    public String getVersion() {
        return sVersion;
    }

    public void setVersion(String sVersion) {
        this.sVersion = sVersion;
    }
        public float getImporteCoaseguro() {
        return nImporteCoaseguro;
    }

    public void setImporteCoaseguro(float nImporteCoaseguro) {
        this.nImporteCoaseguro = nImporteCoaseguro;
    }

    public float getImporteDeducible() {
        return nImporteDeducible;
    }

    public void setImporteDeducible(float nImporteDeducible) {
        this.nImporteDeducible = nImporteDeducible;
    }
    public ArrayList<Integer> getFoliosConceptosCaja() {
        return nFoliosConceptosCaja;
    }

    public void setFoliosConceptosCaja(ArrayList<Integer> nFoliosConceptosCaja) {
        this.nFoliosConceptosCaja = nFoliosConceptosCaja;
    }
    public AccesoDatos getAD() {
        return oAD;
    }
    public void setAD(AccesoDatos oAD) {
        this.oAD = oAD;
    }
    public Comprobante.Emisor getEmisor() {
        return oEmisor;
    }

    public void setEmisor(Comprobante.Emisor oEmisor) {
        this.oEmisor = oEmisor;
    }

    public Comprobante.Receptor getReceptor() {
        return oReceptor;
    }

    public void setReceptor(Comprobante.Receptor oReceptor) {
        this.oReceptor = oReceptor;
    }

    public CompaniaCred getCompCred() {
        return oCompCred;
    }

    public void setCompCred(CompaniaCred oCompCred) {
        this.oCompCred = oCompCred;
    }

    public EpisodioCiaCred getEpCiaCred() {
        return oEpCiaCred;
    }

    public void setEpCiaCred(EpisodioCiaCred oEpCiaCred) {
        this.oEpCiaCred = oEpCiaCred;
    }
        public String getTipoDetalle() {
        return sTipoDetalle;
    }

    public void setTipoDetalle(String sTipoDetalle) {
        this.sTipoDetalle = sTipoDetalle;
    }
    
    public int getPctIVA() {
        return nPctIVA;
    }

    public void setPctIVA(int nPctIVA) {
        this.nPctIVA = nPctIVA;
    }
    public int getLineaDetalleCFDINotaCredito() {
        return nLineaDetalleCFDINotaCredito;
    }

    public void setLineaDetalleCFDINotaCredito(int nLineaDetalleCFDINotaCredito) {
        this.nLineaDetalleCFDINotaCredito = nLineaDetalleCFDINotaCredito;
    }
}
