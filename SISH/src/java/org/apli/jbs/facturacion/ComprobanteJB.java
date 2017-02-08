package org.apli.jbs.facturacion;
import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.apli.configuracion.Configuracion;
import org.apli.generacionCodQr.QrCode;
import org.apli.generacionFormatoImp.GeneradorFormatoImpreso;
import org.apli.modelbeans.facturacion.cfdi.*;
import org.apli.modelbeans.facturacion.cfdi.v32.schema.RegistroDeComprobante;
import org.apli.modelbeans.facturacion.cfdi.v32.schema.Comprobante;
import org.apli.modelbeans.facturacion.cfdi.v32.schema.EmisorRegimenFiscal;
import org.apli.modelbeans.facturacion.cfdi.v32.schema.SerieFiscal;
import org.apli.modelbeans.facturacion.cfdi.v32.schema.TipoDeComprobante;
import org.apli.modelbeans.facturacion.cfdi.v32.schema.TipoDocumento;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.apli.timbradoCFDI.Timbrado;
import org.apli.jbs.utilidades.StandardCharsets;
/**
 * JB para los Comprobantes Fiscales digitales
 * @author Isabel Espinoza Espinoza
 */
@ManagedBean(name="oComprobante")
@SessionScoped
public class ComprobanteJB  implements Serializable{
    Timbrado timbrado;
    private ConvertidorNumeroLetra oConvertidorNumeroLetra=new ConvertidorNumeroLetra();
    private String sCantidadLetra;
    private String sObservaciones="";
    private String sObservacionesFinales = "";
    private boolean bComprobanteValido;
    private FacturaCFI oFactura=new FacturaCFI();
    private FacturaCFI oFacturaResultante=new FacturaCFI();
    private FacesMessage msj  = new FacesMessage();
    private int contador;        //verifica si ya puede comparar el tipo de factura que se hace contra la que se estaba tratando de hacer en el intento previo, sin recargar la página
    private int nAccionConceptos;
    private String sTipoBusquedaParticulares="";
    private int idDetalleCFDI=0;
    private int idDetallePaqueteAgregado=0;
    private String descripcionDetalleCFDI="";
    private String unidadDetalleCFDI="NA";
    private BigDecimal montoDetalleCFDI=BigDecimal.ZERO;
    private int cantidadDetalleCFDI=1; 
    private String sTipoFactura="";
    private String sTipoFacturaAuxiliar="";
    private int nTipoFact;                  //Tipo de factura númerico para los renders
    private String sTipoDocumento="CFDI";   //ingreso o egreso
    //Búsquedas personalizadas
    private int nNumPaquete;                //para la búsqueda de elementos a facturar de un paquete
    //para la búsqueda de cuentas de un paciente
    private CuentaBusquedaPorNombrePaciente oCuentaPacienteSeleccionado=new CuentaBusquedaPorNombrePaciente();
    private List<CuentaBusquedaPorNombrePaciente> oCuentasPacientesPorNombre;
    private String sNombrePaciente="";     //Para la obtención de un episodio médico a partir de nombre
    private String sApPaternoPaciente="";  //Para la búsqueda de un episodio médico a partir de apellido paterno
    private String sApMaternoPaciente="";  //Para la búsqueda de un episodio médico a partir de apellido materno   
    private int nFolioPaciente;             //para la obtención del folio del paciente del que se este facturando 
    private int nClaveEpisodioMed;
    private Comprobante oComprobanteCreado=new Comprobante();
    private Comprobante oComprobanteResultante=new Comprobante();
    //Emisor
    private Comprobante.Emisor oEmisorSeleccionado;
    public static List<Comprobante.Emisor> emisores;  
    private String sRegimen;
    //Receptor
    public static List<Comprobante.Receptor> receptores;
    private Comprobante.Receptor oReceptorSeleccionado;
    //Series fiscales
    private List<SerieFiscal> oSeriesPorDocumento; 
    private List<SerieFiscal> oSeries;
    //Para conceptos a facturar
    private List<ConceptoFacturableCaja> conceptosTodosConsultados;         
    private List<ConceptoFacturableCaja> conceptosPaquetesCuentas;          
    private ConceptoFacturableCaja oConcepto=new ConceptoFacturableCaja();
    private ConceptoFacturableCaja oConceptoSeleccionado=new ConceptoFacturableCaja();
    private List<ConceptoFacturableCaja> conceptosSeleccionados;            
    private List<ConceptoFacturableCaja> consumosSeleccionados;             
    private List<ConceptoFacturableCaja> contratosSeleccionados;            
    private List<ConceptoFacturableCaja> consumosSeleccionadosAcumulados;   
    private List<ConceptoFacturableCaja> conceptosSeleccionadosAcumulados;  
    private List<ConceptoFacturableCaja> conceptosParaFacturar;             
    private List<ConceptoFacturableCaja> conceptosFinalesEnFactura;  
    BigDecimal oSubtotalTasaCeroIVA=BigDecimal.ZERO;
    BigDecimal oSubtotalOtraTasaIVA=BigDecimal.ZERO;
    private BigDecimal oSubtotal=BigDecimal.ZERO;
    private BigDecimal oImpuestos=BigDecimal.ZERO;
    private int nTasaImp=0;
    private BigDecimal oTotal=BigDecimal.ZERO;
    private BigDecimal oSubtotalNoGravadoFinal=BigDecimal.ZERO;
    private BigDecimal oSubtotalGravadoFinal=BigDecimal.ZERO;
    private BigDecimal oSubtotalFinal=BigDecimal.ZERO;
    private BigDecimal oImpuestosFinal=BigDecimal.ZERO;
    private BigDecimal oTotalFinal=BigDecimal.ZERO;
    //Variables temporales para la carga de conceptos que se van a: agrupar, pasar con otro nombre, dividir
    //Lista para contener varios: detalleCFDI con un concepto de servicioPrestado 
    List<DetalleCFDIConUnOpeCajaConcepto> lista_detalleCFDIConUnOpeCajaConcepto=new ArrayList<DetalleCFDIConUnOpeCajaConcepto>();
    //objeto para tener una detalleCFDI con un concepto de ServicioPrestado
    DetalleCFDIConUnOpeCajaConcepto detalleCFDIConUnOpeCajaConcepto=new DetalleCFDIConUnOpeCajaConcepto();
    //Lista para contener varios: detalleCFDI con varios concepto de servicioPrestado 
    List<DetalleCFDIConVariosOpeCajaConcepto> lista_detalleCFDIConVariosOpeCajaConcepto=new ArrayList<DetalleCFDIConVariosOpeCajaConcepto>();
    //objeto para tener una detalleCFDI con varios concepto de ServicioPrestado
    DetalleCFDIConVariosOpeCajaConcepto detalleCFDIConVariosOpeCajaConcepto=new DetalleCFDIConVariosOpeCajaConcepto();
    //Lista para contener varios: varios detalleCFDI con un concepto de servicioPrestado 
    List<DetallesCFDIConUnOpeCajaConcepto> lista_detallesCFDIConUnOpeCajaConcepto=new ArrayList<DetallesCFDIConUnOpeCajaConcepto>();
    //Objeto para contener varios: varios detalleCFDI con un concepto de servicioPrestado 
    DetallesCFDIConUnOpeCajaConcepto detallesCFDIConUnOpeCajaConcepto=new DetallesCFDIConUnOpeCajaConcepto();
    DetalleCFDIServicio detalleCFDIServicio=new DetalleCFDIServicio();
    DetalleCFDIPaquete detalleCFDIPaquete=new DetalleCFDIPaquete();
    List<ConceptoFacturableCaja> listaConceptosFacturables=new ArrayList<ConceptoFacturableCaja>();  
    
    String sDirCodigoRQ;
    
    
    /**********************************************************************************************************************
    /                                            GENERACIÓN DE NOTA DE CRÉDITO                                                  /
    ***********************************************************************************************************************/
    public void limpiarNotaCredito() throws Exception{
        this.bComprobanteValido=false;
        msj  = new FacesMessage();
        this.sTipoDocumento="Nota de crédito";
        oComprobanteCreado=new Comprobante();
        oComprobanteCreado.setTipoDeComprobante("egreso");
        oComprobanteCreado.setFormaDePago("No aplica");
        oComprobanteCreado.setMetodoDePago("No aplica");
        this.limpiarDatosDetalleCFDI();
        oSeries = new SerieFiscal().getSeriesFiscales();
        oFactura=new FacturaCFI();
    }
    public void generarNotaCredito() throws Exception{
        oComprobanteResultante = new Comprobante();
        conceptosFinalesEnFactura = new ArrayList<ConceptoFacturableCaja>();
        oComprobanteResultante = generarNotaCreditoConDatosDeFormulario();
        this.sObservacionesFinales = this.sObservaciones;
        if (oComprobanteResultante != null) {
            oComprobanteResultante.actualizarFolio();      //al Comprobante se le pone el folio que debe tomar el CFDI cuando se hagan los registros
            QrCode codigoQr = new QrCode();
            timbrado = new Timbrado(oFactura.getEmisor().getRfc(), oFactura.getReceptor().getRfc(), Configuracion.usuarioTimbrado, Configuracion.passwordTimbrado,oComprobanteResultante.getSerie(),oComprobanteResultante.getFolio());
            oFactura.getEmisor().setRegimen(sRegimen);
            oComprobanteResultante.setEmisor(oFactura.getEmisor());
            oComprobanteResultante.setReceptor(oFactura.getReceptor());
            CFDv32 cfd = new CFDv32(oComprobanteResultante, "org.apli.modelbeans.facturacion.cfdi");
            CFDi cfdi = new CFDi(cfd, codigoQr, timbrado);
            ExternalContext extCont = FacesContext.getCurrentInstance().getExternalContext();
            String rutaBase = (extCont.getRealPath("//BovedaFiscal//Certificados//")) + "\\" + oComprobanteResultante.getEmisor().getRfc();
            String rutaBaseCadOriginal = (extCont.getRealPath("//BovedaFiscal//Auxiliares//")) + "cadenaOriginal.txt";
            if (cfdi.facturar(oComprobanteResultante.getSerie(),oComprobanteResultante.getFolio(),oComprobanteResultante.getEmisor().getContrasenia(), rutaBase + ".key", rutaBase + ".cer", oComprobanteResultante.getReceptor().getRfc())) {
                setDirCodigoQR(cfdi.getArchivoPNG());
                //DESCOMENTAR
                //cfdi.timbrar();
                timbrado.getDatosTimbrado(oComprobanteResultante);
                GeneradorFormatoImpreso formatoImpreso = new GeneradorFormatoImpreso(oComprobanteResultante, "org.apli.modelbeans.facturacion.cfdi");
                cfdi.generarCadenaOriginal(formatoImpreso, rutaBaseCadOriginal);
                oComprobanteResultante.setCadenaOriginal(cfdi.getCadenaOriginal());
                boolean bandera = guardarNotaCreditoEnBD(oComprobanteResultante);
                if (bandera) {
                    this.limpiarNotaCredito();
                } else {
                    msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, "No fue posible generar el CFDI", "");
                    FacesContext.getCurrentInstance().addMessage(null, msj);
                    this.limpiarNotaCredito();
                }
            } else {
                oComprobanteResultante = new Comprobante();
                msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, "La contraseña es incorrecta", "");
                FacesContext.getCurrentInstance().addMessage(null, msj);
            }
        }
    }
    public void obtenerFacturaParaNotaCredito()throws Exception{
        boolean bValida=true;
        String msjError="";
        if(oFactura.getNombreSerie()==null|oFactura.getNombreSerie().equals("")){
            msj= new FacesMessage(FacesMessage.SEVERITY_ERROR,"ERROR: No ha indicado la serie","");
            FacesContext.getCurrentInstance().addMessage(null, msj);
            bValida=false;       
        }
        if(oFactura.getFolio()==0){
            msj= new FacesMessage(FacesMessage.SEVERITY_ERROR,"ERROR: No ha indicado el folio","");
            FacesContext.getCurrentInstance().addMessage(null, msj);
            bValida=false;
        }
        if(bValida){
            msjError=oFactura.buscarFacturaParaNotaCredito();
        }
        if (msjError.contains("ERROR")){
            limpiarNotaCredito();
            msj= new FacesMessage(FacesMessage.SEVERITY_ERROR,msjError,"");
            FacesContext.getCurrentInstance().addMessage(null, msj);
        }   
    }
    /**********************************************************************************************************************
    /                                            GENERACIÓN DE LA FACTURA                                                  /
    ***********************************************************************************************************************/ 
    
    /*Verifica combinaciones
     * Régimen Arrendamiento -> tipoFactura = Rentas
     * Otro
     * Si RFC receptor = XAXX010101000 (público en general) 
     *              -> tipoFactura = PublicoGral o PublicoGralConTDC
     * Si RFC receptor longitud = 12 (persona moral)
     *              -> tipoFactura = Credito-Empresas
     * Si RFC receptor longitud = 13 (persona física)
     *              -> tipoFactura = Paquetes o Cuentas o Particulares
     */
    public void validaTipoFactura() {
        if (this.sRegimen.equals("Arrendamiento")){
            if (!this.sTipoFactura.equals("Rentas")){
                msj= new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Para régimen de arrendamiento sólo se permite emitir factura por rentas","");
                FacesContext.getCurrentInstance().addMessage(null, msj);
                this.sTipoFactura="";
            }
        }else{
            if (this.sTipoFactura.contains("PublicoGral")){
                if (this.oReceptorSeleccionado != null &&
                    !this.oReceptorSeleccionado.getRfc().equals("XAXX010101000")){
                    msj= new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Receptor equivocado para venta al público","");
                    FacesContext.getCurrentInstance().addMessage(null, msj);
                    this.sTipoFactura="";
                }
            }
        }
    }
    
    

    public void generarVistaPrevia() {
        revisarCFDI();
    }

    public void prepararVistaPrevia() throws Exception {
        oComprobanteResultante = this.oComprobanteCreado;
        conceptosFinalesEnFactura = new ArrayList<ConceptoFacturableCaja>();
        this.sObservacionesFinales = this.sObservaciones;
        generarCFDIParaVistaPrevia();
        if (oComprobanteResultante != null ) {
            if (oEmisorSeleccionado != null & oReceptorSeleccionado != null &oComprobanteResultante.getSerie()!=null &oComprobanteResultante.getFolio()!=null) {
                timbrado = new Timbrado(oEmisorSeleccionado.getRfc(), oReceptorSeleccionado.getRfc(), Configuracion.usuarioTimbrado, Configuracion.passwordTimbrado,oComprobanteResultante.getSerie(),oComprobanteResultante.getFolio());
                oEmisorSeleccionado.setRegimen(this.sRegimen);
                oComprobanteResultante.setEmisor(this.oEmisorSeleccionado);
                oComprobanteResultante.setReceptor(this.oReceptorSeleccionado);
            }
        }
    }
    
    public void generarFactura()throws Exception{
        oComprobanteResultante = new Comprobante();
        conceptosFinalesEnFactura = new ArrayList<ConceptoFacturableCaja>();
        oComprobanteResultante = generarCFDIConDatosDeFormulario();
        this.sObservacionesFinales = this.sObservaciones;
        if (oComprobanteResultante != null) {
            oComprobanteResultante.actualizarFolio();
            QrCode codigoQr = new QrCode();
            timbrado = new Timbrado(oEmisorSeleccionado.getRfc(), oReceptorSeleccionado.getRfc(), Configuracion.usuarioTimbrado, Configuracion.passwordTimbrado,oComprobanteResultante.getSerie(),oComprobanteResultante.getFolio());
            oEmisorSeleccionado.setRegimen(this.sRegimen);
            oComprobanteResultante.setEmisor(this.oEmisorSeleccionado);
            oComprobanteResultante.setReceptor(this.oReceptorSeleccionado);
            CFDv32 cfd = new CFDv32(oComprobanteResultante, "org.apli.modelbeans.facturacion.cfdi");
            CFDi cfdi = new CFDi(cfd, codigoQr, timbrado);
            ExternalContext extCont = FacesContext.getCurrentInstance().getExternalContext();
            String rutaBase = (extCont.getRealPath("//BovedaFiscal//Certificados//")) + "\\" + oComprobanteResultante.getEmisor().getRfc();
            String rutaBaseCadOriginal = (extCont.getRealPath("//BovedaFiscal//Auxiliares//")) + "cadenaOriginal.txt";
            if (cfdi.facturar(oComprobanteResultante.getSerie(),oComprobanteResultante.getFolio(),oComprobanteResultante.getEmisor().getContrasenia(), rutaBase + ".key", rutaBase + ".cer", oComprobanteResultante.getReceptor().getRfc())) {
                setDirCodigoQR(cfdi.getArchivoPNG());
                //Descomentar la siguiente línea de código para el timbrado del CFDI
                //cfdi.timbrar();  
                timbrado.getDatosTimbrado(oComprobanteResultante);
                GeneradorFormatoImpreso formatoImpreso = new GeneradorFormatoImpreso(oComprobanteResultante, "org.apli.modelbeans.facturacion.cfdi");
                cfdi.generarCadenaOriginal(formatoImpreso, rutaBaseCadOriginal);
                oComprobanteResultante.setCadenaOriginal(cfdi.getCadenaOriginal());
                if (guardarFacturaEnBD(oComprobanteResultante)) {
                    limpiar();
                } else {
                    msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, "No fue posible generar el CFDI", "");
                    FacesContext.getCurrentInstance().addMessage(null, msj);
                    limpiar();
                }
            } else {
                oComprobanteResultante = new Comprobante();
                msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, "La contraseña es incorrecta", "");
                FacesContext.getCurrentInstance().addMessage(null, msj);
            }
        }
    }
    
    /**********************************************************************************************************************
    /              ADMINISTRACIÓN DE CONCEPTOS FACTURABLES Y LOS DATOS DE LA CONFIGURACIÓN                                          /
    **********************************************************************************************************************/
    
   /*------------------------------------------------------------------------------------------------------------------------------*/
    /*                                                  AGREGAR ELEMENTOS                                                           */
    /*------------------------------------------------------------------------------------------------------------------------------*/
    //para el caso de seleccionar solo un concepto facturable y asociarlo a varias lineas de CFDI se tiene el método agregarLinea, en caso de que ya no quiera la línea agregada
    public void agregarDetalleCFDI(){
        if(verificaLlenadoDetalleCFDI()){     
            idDetalleCFDI++;
            detallesCFDIConUnOpeCajaConcepto.agregarDetalleCFDI(cantidadDetalleCFDI, descripcionDetalleCFDI, montoDetalleCFDI,unidadDetalleCFDI,idDetalleCFDI);
            limpiarDatosDetalleCFDI();
        }
    } 
    public void llenarDetalleCFDI(DetalleCFDI detalleCFDI){
       detalleCFDI.setCant(this.cantidadDetalleCFDI);
       detalleCFDI.setDescrip(this.descripcionDetalleCFDI);
       detalleCFDI.setUnidad(this.unidadDetalleCFDI);
       detalleCFDI.setMonto(this.montoDetalleCFDI);
       detalleCFDI.setId(this.idDetalleCFDI); 
    }
    public void llenarConceptoFacturableCajaConDetalleCFDI(ConceptoFacturableCaja concepto){
        concepto.setCantidad(this.cantidadDetalleCFDI);
        concepto.setUnidad(this.unidadDetalleCFDI);
        concepto.setDescripcion(this.descripcionDetalleCFDI);
        concepto.setCostoUnitarioSinIva(this.montoDetalleCFDI);
        concepto.setFolioOC(""+this.idDetalleCFDI);
    }
    public void llenarDetalleCFDIConConceptoFacturableCaja(DetalleCFDI detalleCFDI,ConceptoFacturableCaja concepto){
        detalleCFDI.setCant(concepto.getCantidad());
        detalleCFDI.setDescrip(concepto.getDescripcion());
        detalleCFDI.setMonto(concepto.getImporteTotalSinIva());
        detalleCFDI.setUnidad(concepto.getUnidad());
        detalleCFDI.setId(this.idDetalleCFDI); 
    }
    //La restricción es que los deducibles y coaseguros no los podrá editar, solo agregar
    public void agregarCoaseguroDeducible(){
        boolean verifica=true;
        ConceptoFacturableCaja concepto=getConceptoSeleccionado();
        if((getConceptoSeleccionado()==null)){
            msj= new FacesMessage(FacesMessage.SEVERITY_ERROR,"Debe seleccionar solo un concepto","");
            FacesContext.getCurrentInstance().addMessage(null, msj);
            verifica=false;
        }
        for(ConceptoFacturableCaja conAcumulado:conceptosSeleccionadosAcumulados){
            if(conAcumulado.getDescripcion().contains("PAGO DE DEDUCIBLE") &&concepto.getDescripcion().contains("PAGO DE DEDUCIBLE")){
                conceptosSeleccionados.remove(concepto); //No puede agregar el coaseguro o deducible       
                verifica=false;
                msj= new FacesMessage(FacesMessage.SEVERITY_ERROR,"No es posible agregar el deducible pues ya hay uno indicado en la factura","");
                FacesContext.getCurrentInstance().addMessage(null, msj);
                break;
            }else if(conAcumulado.getDescripcion().contains("PAGO DE COASEGURO") &&concepto.getDescripcion().contains("PAGO DE COASEGURO")){
                conceptosSeleccionados.remove(concepto); //No puede agregar el coaseguro o deducible       
                verifica=false;
                msj= new FacesMessage(FacesMessage.SEVERITY_ERROR,"No es posible agregar el coaseguro pues ya hay uno indicado en la factura","");
                FacesContext.getCurrentInstance().addMessage(null, msj);
                break;
            } 
        }
        if(verifica){
            if(this.sTipoFactura.equals("Credito-Empresas")){
                //Verifica si es posible que para esa factura se descuente el deducible, coaseguro e impuestos que correspondan
                if((oSubtotal.compareTo(concepto.getImporteTotalSinIva())==0|oSubtotal.compareTo(concepto.getImporteTotalSinIva())==1)&&
                    (oImpuestos.compareTo(concepto.getImpuestoTotal())==0|oImpuestos.compareTo(concepto.getImpuestoTotal())==1)){                                                                            //Es coaseguro
                    oSubtotal=oSubtotal.subtract(concepto.getImporteTotalSinIva());
                    oImpuestos=oImpuestos.subtract(concepto.getImpuestoTotal());
                }else{
                    conceptosSeleccionados.remove(concepto); //No puede agregar el coaseguro o deducible       
                    verifica=false;
                    msj= new FacesMessage(FacesMessage.SEVERITY_ERROR,"No es posible agregar el concepto porque su monto debe ser menor al total de la factura","");
                    FacesContext.getCurrentInstance().addMessage(null, msj);
                }
            }else{ //Es paciente de cuenta de hospital
                oSubtotal=oSubtotal.add(concepto.getImporteTotalSinIva());
                oImpuestos=oImpuestos.add(concepto.getImpuestoTotal());
            }
            if(verifica){
                this.nFolioPaciente=concepto.getFolioPac();
                this.nClaveEpisodioMed=concepto.getClaveEpisodioMed();
                oTotal=oSubtotal.add(oImpuestos);
                conceptosTodosConsultados.remove(concepto);                 //se remueve de conceptos pero se mantiene en conceptosseleccionados                                                
                this.conceptosSeleccionadosAcumulados.add(concepto);        //se agrega el concepto seleccionado para acumularlo a los demás
                this.conceptosParaFacturar.add(concepto);
                DetalleCFDI detalleCFDI=new DetalleCFDI();
                this.idDetalleCFDI++;
                llenarDetalleCFDIConConceptoFacturableCaja(detalleCFDI,concepto);
                detalleCFDIConUnOpeCajaConcepto=new DetalleCFDIConUnOpeCajaConcepto();
                //se agrega un detalle de CFDI
                detalleCFDIConUnOpeCajaConcepto.setDetalleCFDI(detalleCFDI);     
                DetalleCFDIOpeCajaConcepto detalleCFDIOpeCajaConcepto=new DetalleCFDIOpeCajaConcepto();
                detalleCFDIOpeCajaConcepto.setFolioCaja(concepto.getFolioOCC());
                detalleCFDIOpeCajaConcepto.setSec(concepto.getSecOCC());
                if((sTipoFactura.equals("PublicoGral")|sTipoFactura.equals("PublicoGralConTDC")|sTipoFactura.equals("Cuentas"))&&concepto.getDescripcion().contains("PAGO DE COASEGURO"))
                    detalleCFDIOpeCajaConcepto.setTipo("COASEGURO");
                if((sTipoFactura.equals("PublicoGral")|sTipoFactura.equals("PublicoGralConTDC")|sTipoFactura.equals("Cuentas"))&&concepto.getDescripcion().contains("PAGO DE DEDUCIBLE")){
                    detalleCFDIOpeCajaConcepto.setTipo("DEDUCIBLE");
                }
                if(sTipoFactura.equals("PublicoGral")|sTipoFactura.equals("PublicoGralConTDC")|sTipoFactura.equals("Cuentas")){
                    String folioCompleto=concepto.getFolioOC().substring(4);
                    if(folioCompleto.length()!=0){   
                        detalleCFDIOpeCajaConcepto.setIdTipo(Integer.parseInt(folioCompleto));       //Folio de la tabla coaseguro o deducible
                    }
                }
                //se agrega un servicio prestado (el elegido) para el detalle de CFDI agregado
                detalleCFDIConUnOpeCajaConcepto.setDetalleCFDIOpeCajaConcepto(detalleCFDIOpeCajaConcepto);
                //se agrega a la lista tanto el servicio como el detalle del CFDI
                lista_detalleCFDIConUnOpeCajaConcepto.add(detalleCFDIConUnOpeCajaConcepto);
            }
            limpiarDatosDetalleCFDI();
        }
    }
    public boolean verificaLlenadoDetalleCFDI(){
        if((this.getDescripcionDetalleCFDI()!=null && this.getDescripcionDetalleCFDI().equals("")) |
           (this.getUnidadDetalleCFDI()!=null && this.getUnidadDetalleCFDI().equals(""))|
            (this.getMontoDetalleCFDI().floatValue()==0 && this.sTipoFactura.equals("Credito-Empresas")) |  this.getCantidadDetalleCFDI()==0){
            msj= new FacesMessage(FacesMessage.SEVERITY_ERROR,"Debe proporcionar cantidad, descripción y monto","");
            FacesContext.getCurrentInstance().addMessage(null, msj);
            return false;
        }
        if(((this.getDescripcionDetalleCFDI()!=null && this.getDescripcionDetalleCFDI().equals("")) |
           (this.getUnidadDetalleCFDI()!=null && this.getUnidadDetalleCFDI().equals(""))|this.getCantidadDetalleCFDI()==0)
                && !this.sTipoFactura.equals("Credito-Empresas")){
            msj= new FacesMessage(FacesMessage.SEVERITY_ERROR,"Debe proporcionar cantidad y descripción","");
            FacesContext.getCurrentInstance().addMessage(null, msj);
            return false;
        }
        return true;
    }
    public void agregarConceptosSeleccionados(){
        if(conceptosSeleccionados.isEmpty())
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error de validación: No hay conceptos seleccionados",""));
        else{
            if (this.sTipoFactura.equals("Particulares")||this.sTipoFactura.equals("Rentas"))
                agregarConceptosParticularesRentas();
            else if(this.sTipoFactura.equals("PublicoGral")||this.sTipoFactura.equals("PublicoGralConTDC"))
                agregarConceptosPublicoGeneral();
        }
    } 
    //Método para agregar los conceptos seleccionados a la factura (EN LA VISTA)
    public void agregarConceptosParticularesRentas(){
        for(ConceptoFacturableCaja conceptoSeleccionado:conceptosSeleccionados){  
            oSubtotal=oSubtotal.add(conceptoSeleccionado.getImporteTotalSinIva());
            oImpuestos=oImpuestos.add(conceptoSeleccionado.getImpuestoTotal());
            conceptosTodosConsultados.remove(conceptoSeleccionado);  
            conceptosSeleccionadosAcumulados.add(conceptoSeleccionado);
            conceptosParaFacturar.add(conceptoSeleccionado);
            DetalleCFDI detalle=new DetalleCFDI();
            this.idDetalleCFDI++;
            this.llenarDetalleCFDIConConceptoFacturableCaja(detalle, conceptoSeleccionado);
            DetalleCFDIOpeCajaConcepto detalleCaja=new DetalleCFDIOpeCajaConcepto();
            detalleCaja.setFolioCaja(conceptoSeleccionado.getFolioOCC());
            detalleCaja.setSec(conceptoSeleccionado.getSecOCC());
            detalleCFDIConUnOpeCajaConcepto=new DetalleCFDIConUnOpeCajaConcepto();
            detalleCFDIConUnOpeCajaConcepto.setDetalleCFDI(detalle);
            detalleCFDIConUnOpeCajaConcepto.setDetalleCFDIOpeCajaConcepto(detalleCaja);
            lista_detalleCFDIConUnOpeCajaConcepto.add(detalleCFDIConUnOpeCajaConcepto);
        }
        //Se saca el total sumando impuestos más subtotal
        oTotal=oTotal.add(oImpuestos).add(oSubtotal);
        conceptosSeleccionados.clear();
    }
    //Método para agregar los conceptos seleccionados a la factura (EN LA VISTA), por todos los seleccionados los separará 
    //conceptosSeleccionados (todos los elegidos) y conceptosParaFacturar (todos los que aparecerán en la factura)
    public void agregarConceptosPublicoGeneral(){
            int folio=0;
            lista_detalleCFDIConVariosOpeCajaConcepto=new ArrayList<DetalleCFDIConVariosOpeCajaConcepto>();
            limpiarDatosTotalesConceptosCFDI();
            DetalleCFDIOpeCajaConcepto detalleCaja=new DetalleCFDIOpeCajaConcepto();
            DetalleCFDIConVariosOpeCajaConcepto detalleCFDIVariosOpeCajaTasaCero=new DetalleCFDIConVariosOpeCajaConcepto();
            DetalleCFDIConVariosOpeCajaConcepto detalleCFDIVariosOpeCajaOtraTasa=new DetalleCFDIConVariosOpeCajaConcepto();
            ConceptoFacturableCaja conceptoTasaCero=new ConceptoFacturableCaja();
            ConceptoFacturableCaja conceptoOtraTasa=new ConceptoFacturableCaja();
            //Se agregan los nuevos conceptos a conceptosSeleccionadosAcumulados
            for(ConceptoFacturableCaja conSeleccionado:conceptosSeleccionados)
                for(ConceptoFacturableCaja concepto:conceptosTodosConsultados)
                    if(conSeleccionado.getFolioOC().equals(concepto.getFolioOC())){
                        conceptosTodosConsultados.remove(concepto);
                        this.conceptosSeleccionadosAcumulados.add(concepto);
                        break;
                    }
            for(ConceptoFacturableCaja conAcumulado:conceptosSeleccionadosAcumulados){
                detalleCaja=new DetalleCFDIOpeCajaConcepto();
                detalleCaja.setFolioCaja(conAcumulado.getFolioOCC());
                detalleCaja.setSec(conAcumulado.getSecOCC());
                if(conAcumulado.getDescripcion().equals("PAGO DE COASEGURO")){
                    String folioCompleto=conAcumulado.getFolioOC().substring(4);
                    detalleCaja.setTipo("COASEGURO");
                    if(folioCompleto.length()!=0){
                        folio=Integer.parseInt(folioCompleto);    
                        detalleCaja.setIdTipo(folio);
                    }
                }
                if(conAcumulado.getDescripcion().equals("PAGO DE DEDUCIBLE")){
                    String folioCompleto=conAcumulado.getFolioOC().substring(4);
                    detalleCaja.setTipo("DEDUCIBLE");
                    if(folioCompleto.length()!=0){
                        folio=Integer.parseInt(folioCompleto);    
                        detalleCaja.setIdTipo(folio);
                    }
                }
                if(conAcumulado.getPctIVa()==0){ //El IVA es tasa 0
                    oSubtotalTasaCeroIVA=oSubtotalTasaCeroIVA.add(conAcumulado.getImporteTotalSinIva());
                    detalleCFDIVariosOpeCajaTasaCero.agregarDetalleCFDIOpeCajaConcepto(detalleCaja);
                }else{
                    oSubtotalOtraTasaIVA=oSubtotalOtraTasaIVA.add(conAcumulado.getImporteTotalSinIva());
                    oImpuestos=oImpuestos.add(conAcumulado.getImpuestoTotal());
                    detalleCFDIVariosOpeCajaOtraTasa.agregarDetalleCFDIOpeCajaConcepto(detalleCaja);
                }
            }
            conceptosParaFacturar.clear();
            if(oSubtotalTasaCeroIVA.compareTo(BigDecimal.ZERO)==1){
                conceptoTasaCero.setFolioOC("1");
                conceptoTasaCero.setCantidad(1);
                conceptoTasaCero.setPctIVa(0);
                conceptoTasaCero.setCostoUnitario(oSubtotalTasaCeroIVA);
                conceptoTasaCero.setImpuestoTotal(BigDecimal.ZERO);
                conceptoTasaCero.setDescripcion("VENTA AL PUBLICO EN GENERAL");
                conceptoTasaCero.setUnidad("NO APLICA");
                conceptosParaFacturar.add(conceptoTasaCero);
                DetalleCFDI detalleCFDI=new DetalleCFDI();
                this.llenarDetalleCFDIConConceptoFacturableCaja(detalleCFDI, conceptoTasaCero);
                detalleCFDI.setId(1);
                detalleCFDIVariosOpeCajaTasaCero.setDetalleCFDI(detalleCFDI);
                lista_detalleCFDIConVariosOpeCajaConcepto.add(detalleCFDIVariosOpeCajaTasaCero);
            }
            if(oSubtotalOtraTasaIVA.compareTo(BigDecimal.ZERO)==1){
                conceptoOtraTasa.setFolioOC("2");
                conceptoOtraTasa.setCantidad(1);
                conceptoOtraTasa.setPctIVa(16);
                conceptoOtraTasa.setCostoUnitario(oSubtotalOtraTasaIVA.add(oImpuestos));
                conceptoOtraTasa.setImpuestoTotal(oImpuestos);
                conceptoOtraTasa.setDescripcion("VENTA AL PUBLICO EN GENERAL");
                conceptoOtraTasa.setUnidad("NO APLICA");
                conceptosParaFacturar.add(conceptoOtraTasa);
                DetalleCFDI detalleCFDI=new DetalleCFDI();
                this.llenarDetalleCFDIConConceptoFacturableCaja(detalleCFDI, conceptoOtraTasa);
                detalleCFDI.setId(2);
                detalleCFDIVariosOpeCajaOtraTasa.setDetalleCFDI(detalleCFDI);
                lista_detalleCFDIConVariosOpeCajaConcepto.add(detalleCFDIVariosOpeCajaOtraTasa);
            }
            //Obtención de totales
            oSubtotal=oSubtotal.add(oSubtotalTasaCeroIVA).add(oSubtotalOtraTasaIVA);
            oTotal=oTotal.add(oImpuestos).add(oSubtotal); 
            conceptosSeleccionados.clear();
    }
    public void agregarUnConceptoSeleccionadoConUnDetalleCFDI(){//Cuentas, paquetes y empresa
        boolean verificaSeleccion=true;
        if(!(this.conceptosSeleccionados.size()==1)){
            msj= new FacesMessage(FacesMessage.SEVERITY_ERROR,"Debe seleccionar solo un concepto","");
            FacesContext.getCurrentInstance().addMessage(null, msj);
            verificaSeleccion=false;
        }
        if(verificaLlenadoDetalleCFDI()&&verificarSeleccionConsumosContratos()&&verificaSeleccion){
            ConceptoFacturableCaja conceptoSeleccionado=conceptosSeleccionados.get(0);
            conceptosSeleccionados.clear();
            ConceptoFacturableCaja concepto=new ConceptoFacturableCaja();
            concepto.setPctIVa(conceptoSeleccionado.getPctIVa());
            if(!this.sTipoFactura.equals("Credito-Empresas")){
                this.montoDetalleCFDI=conceptoSeleccionado.getCostoUnitario();
            }
            this.idDetalleCFDI++;
            llenarConceptoFacturableCajaConDetalleCFDI(concepto);     
            this.oSubtotal=oSubtotal.add(concepto.getImporteTotalSinIva());
            this.oImpuestos=oImpuestos.add(concepto.getImpuestoTotal());
            this.oTotal=oSubtotal.add(oImpuestos);
            this.conceptosParaFacturar.add(concepto);
            this.conceptosSeleccionadosAcumulados.add(conceptoSeleccionado);
            this.conceptosTodosConsultados.remove(conceptoSeleccionado);
            DetalleCFDI detalleCFDI=new DetalleCFDI();
            
            llenarDetalleCFDI(detalleCFDI);
            detalleCFDIConUnOpeCajaConcepto=new DetalleCFDIConUnOpeCajaConcepto();
            //se agrega un detalle de CFDI
            detalleCFDIConUnOpeCajaConcepto.setDetalleCFDI(detalleCFDI);     
            DetalleCFDIOpeCajaConcepto detalleCFDIOpeCajaConcepto=new DetalleCFDIOpeCajaConcepto();
            detalleCFDIOpeCajaConcepto.setFolioCaja(conceptoSeleccionado.getFolioOCC());
            detalleCFDIOpeCajaConcepto.setSec(conceptoSeleccionado.getSecOCC());
            //se agrega un servicio prestado (el elegido) para el detalle de CFDI agregado
            detalleCFDIConUnOpeCajaConcepto.setDetalleCFDIOpeCajaConcepto(detalleCFDIOpeCajaConcepto);
            //se agrega a la lista tanto el servicio como el detalle del CFDI
            if(this.sTipoFactura.equals("Cuentas")|this.sTipoFactura.equals("Paquetes"))
                llenarConsumosContratos("detalleCFDIConUnOpeCajaConcepto");
            lista_detalleCFDIConUnOpeCajaConcepto.add(detalleCFDIConUnOpeCajaConcepto);
        }     
        limpiarDatosDetalleCFDI();
    }  
    
    //Permite agregar un concepto seleccionado para un detalle de CFDI
    public void agruparVariosConceptosSeleccionadosEnUnDetalleCFDI(){
        boolean verificaSeleccion=true;
        boolean verificaTasaIVA=true;
        int pctIVA=0;
        if(this.conceptosSeleccionados.isEmpty()|this.conceptosSeleccionados.size()==1){
            msj= new FacesMessage(FacesMessage.SEVERITY_ERROR,"Debe seleccionar varios conceptos para poder agruparlos","");
            FacesContext.getCurrentInstance().addMessage(null, msj);
            verificaSeleccion=false;
        }else
            pctIVA=this.conceptosSeleccionados.get(0).getPctIVa();
        for(ConceptoFacturableCaja con:conceptosSeleccionados)
                if(con.getPctIVa()!=pctIVA){
                    msj= new FacesMessage(FacesMessage.SEVERITY_ERROR,"El conjunto de conceptos a agrupar debe tener la misma tasa de IVA","");
                    FacesContext.getCurrentInstance().addMessage(null, msj);
                    verificaTasaIVA=false;
                }
        if(verificaLlenadoDetalleCFDI()&&verificarSeleccionConsumosContratos()&&verificaSeleccion&&verificaTasaIVA){
            detalleCFDIConVariosOpeCajaConcepto=new DetalleCFDIConVariosOpeCajaConcepto();
            DetalleCFDIOpeCajaConcepto detalleCFDIOpeCajaConcepto;
            //agrega todos los conceptos de servicio (son varios porque los va a agrupar)
            BigDecimal monto=BigDecimal.ZERO;
            for(ConceptoFacturableCaja conSeleccionado:conceptosSeleccionados){
                        detalleCFDIOpeCajaConcepto =new DetalleCFDIOpeCajaConcepto();
                        detalleCFDIOpeCajaConcepto.setFolioCaja(conSeleccionado.getFolioOCC());
                        detalleCFDIOpeCajaConcepto.setSec(conSeleccionado.getSecOCC());
                        detalleCFDIConVariosOpeCajaConcepto.agregarDetalleCFDIOpeCajaConcepto(detalleCFDIOpeCajaConcepto);
                        this.conceptosTodosConsultados.remove(conSeleccionado);
                        this.conceptosSeleccionadosAcumulados.add(conSeleccionado);
                        monto=monto.add(conSeleccionado.getCostoUnitario());
            }
            //agrega el detalle de CFDI
            DetalleCFDI detalleCFDI=new DetalleCFDI();
            this.idDetalleCFDI++;
            if(!this.sTipoFactura.equals("Credito-Empresas"))
                this.montoDetalleCFDI=monto;
            llenarDetalleCFDI(detalleCFDI);
            detalleCFDIConVariosOpeCajaConcepto.setDetalleCFDI(detalleCFDI);
            ConceptoFacturableCaja concepto=new ConceptoFacturableCaja();
            concepto.setPctIVa(pctIVA);
            llenarConceptoFacturableCajaConDetalleCFDI(concepto);
            this.conceptosParaFacturar.add(concepto);
            this.oSubtotal=oSubtotal.add(concepto.getImporteTotalSinIva());
            this.oImpuestos=oImpuestos.add(concepto.getImpuestoTotal());
            this.oTotal=oSubtotal.add(oImpuestos);
            if(this.sTipoFactura.equals("Cuentas")|this.sTipoFactura.equals("Paquetes"))
                llenarConsumosContratos("detalleCFDIConVariosOpeCajaConcepto");
            this.lista_detalleCFDIConVariosOpeCajaConcepto.add(detalleCFDIConVariosOpeCajaConcepto);
        }
        limpiarDatosDetalleCFDI();     
        
    }
    
    public void agregarUnConceptoConVariosDetallesCFDI(){
        boolean verificaDetallesCFDI=true;
        boolean verificaSeleccion=true;
        boolean verificaMontoDetallesCFDI=true;
        if(!(this.conceptosSeleccionados.size()==1)){
            msj= new FacesMessage(FacesMessage.SEVERITY_ERROR,"Debe selecci0nar solo un concepto","");
            FacesContext.getCurrentInstance().addMessage(null, msj);
            verificaSeleccion=false;
        }
        if(detallesCFDIConUnOpeCajaConcepto.getDetallesCFDI().isEmpty()){
            msj= new FacesMessage(FacesMessage.SEVERITY_ERROR,"Aun no se agregan detalles para el CFDI","");
            FacesContext.getCurrentInstance().addMessage(null, msj);
            verificaDetallesCFDI=false;
        }   
        if(verificaSeleccion&&verificaDetallesCFDI){
            BigDecimal montoDetallesCFDI=BigDecimal.ZERO;
            for(DetalleCFDI detalle: detallesCFDIConUnOpeCajaConcepto.getDetallesCFDI())
                montoDetallesCFDI=montoDetallesCFDI.add(detalle.getMonto());
            BigDecimal montoDiferencia=conceptosSeleccionados.get(0).getImporteTotalSinIva().subtract(montoDetallesCFDI);
            if(montoDiferencia.floatValue()!=0 && !this.sTipoFactura.equals("Credito-Empresas")){
                msj= new FacesMessage(FacesMessage.SEVERITY_ERROR,"Verifique los montos agregados, la suma de estos debe coincidir con el pago con el que desea relacionarlo, la diferencia es de "+montoDiferencia,"");
                FacesContext.getCurrentInstance().addMessage(null, msj);
                verificaMontoDetallesCFDI=false;
            }
        }
        if(verificarSeleccionConsumosContratos()&&verificaSeleccion&&verificaDetallesCFDI&&verificaMontoDetallesCFDI){
            ConceptoFacturableCaja conceptoSeleccionado=conceptosSeleccionados.get(0);
            for(DetalleCFDI detalle: detallesCFDIConUnOpeCajaConcepto.getDetallesCFDI()){
                    ConceptoFacturableCaja concepto=new ConceptoFacturableCaja();
                    concepto.setCantidad(detalle.getCant());
                    concepto.setUnidad(detalle.getUnidad());
                    concepto.setPctIVa(conceptoSeleccionado.getPctIVa());
                    concepto.setCostoUnitarioSinIva(detalle.getMonto());
                    concepto.setFolioOC(""+detalle.getId());
                    concepto.setDescripcion(detalle.getDescrip());
                    this.conceptosParaFacturar.add(concepto);
                    this.oSubtotal=oSubtotal.add(concepto.getImporteTotalSinIva());
                    this.oImpuestos=oImpuestos.add(concepto.getImpuestoTotal());
                    this.oTotal=oSubtotal.add(oImpuestos);
            }
            DetalleCFDIOpeCajaConcepto detalle=new DetalleCFDIOpeCajaConcepto();
            detalle.setFolioCaja(conceptoSeleccionado.getFolioOCC());
            detalle.setSec(conceptoSeleccionado.getSecOCC());
            this.detallesCFDIConUnOpeCajaConcepto.setDetalleCFDIOpeCajaConcepto(detalle);
            this.conceptosTodosConsultados.remove(conceptoSeleccionado);
            this.conceptosSeleccionadosAcumulados.add(conceptoSeleccionado);
            if(this.sTipoFactura.equals("Cuentas")|this.sTipoFactura.equals("Paquetes"))
                llenarConsumosContratos("detallesCFDIConUnOpeCajaConcepto");
            lista_detallesCFDIConUnOpeCajaConcepto.add(detallesCFDIConUnOpeCajaConcepto);
            detallesCFDIConUnOpeCajaConcepto=new DetallesCFDIConUnOpeCajaConcepto();
            limpiarDatosDetalleCFDI();
        }
    } 
    public void llenarConsumosContratos(String tipoAgregado){
        if(this.sTipoFactura.equals("Cuentas")){
                for(ConceptoFacturableCaja conSel:consumosSeleccionados){
                    detalleCFDIServicio=new DetalleCFDIServicio();
                    detalleCFDIServicio.setIdFolioServPres(conSel.getFolioServicio());
                    conceptosPaquetesCuentas.remove(conSel);
                    consumosSeleccionadosAcumulados.add(conSel);
                    if(tipoAgregado.equals("detalleCFDIConUnOpeCajaConcepto"))
                        detalleCFDIConUnOpeCajaConcepto.agregarServicioConsumido(detalleCFDIServicio);    
                    else if(tipoAgregado.equals("detalleCFDIConVariosOpeCajaConcepto"))
                        detalleCFDIConVariosOpeCajaConcepto.agregarServicioConsumido(detalleCFDIServicio);    
                    else if(tipoAgregado.equals("detallesCFDIConUnOpeCajaConcepto"))
                        detallesCFDIConUnOpeCajaConcepto.agregarServicioConsumido(detalleCFDIServicio);    
                }
                consumosSeleccionados.clear();
        }
        if(this.sTipoFactura.equals("Paquetes")){
                for(ConceptoFacturableCaja conSel:contratosSeleccionados){
                    this.idDetallePaqueteAgregado++;
                    detalleCFDIPaquete=new DetalleCFDIPaquete();
                    detalleCFDIPaquete.setIdPaquete(conSel.getSecOCC());
                    detalleCFDIPaquete.setIdPaqueteContratado(conSel.getFolioOCC());
                    detalleCFDIPaquete.setSec(Integer.parseInt(conSel.getFolioOC()));
                    detalleCFDIPaquete.setFolio(idDetallePaqueteAgregado);
                    conSel.setFolioServicio(""+idDetallePaqueteAgregado);
                    conceptosPaquetesCuentas.remove(conSel);
                    consumosSeleccionadosAcumulados.add(conSel);
                    if(tipoAgregado.equals("detalleCFDIConUnOpeCajaConcepto"))
                        detalleCFDIConUnOpeCajaConcepto.agregarServicioContratado(detalleCFDIPaquete);  
                    else if(tipoAgregado.equals("detalleCFDIConVariosOpeCajaConcepto"))
                        detalleCFDIConVariosOpeCajaConcepto.agregarServicioContratado(detalleCFDIPaquete);
                    else if(tipoAgregado.equals("detallesCFDIConUnOpeCajaConcepto"))
                        detallesCFDIConUnOpeCajaConcepto.agregarServicioContratado(detalleCFDIPaquete);
                }
                contratosSeleccionados.clear();
        } 
    }
    public boolean verificarSeleccionConsumosContratos(){
        if((this.sTipoFactura.equals("Cuentas")&&this.consumosSeleccionados.isEmpty())|(this.sTipoFactura.equals("Paquetes")&&this.contratosSeleccionados.isEmpty())){
            msj= new FacesMessage(FacesMessage.SEVERITY_ERROR,"Debe seleccionar algún concepto que se relacione","");
            FacesContext.getCurrentInstance().addMessage(null, msj);
            return false;     
        }
        return true;
    }
    
    //Se agregan los elementos que en la vista se quedaron como: conceptosSeleccionados
    public RegistroDeComprobante agregarAComprobanteConceptosSeleccionados(RegistroDeComprobante reg){
        for(ConceptoFacturableCaja concepto:conceptosSeleccionados){
            reg.registrarConcepto(concepto.getUnidad(),concepto.getImporteTotalSinIva(),new BigDecimal(concepto.getCantidad()),concepto.getDescripcion(),concepto.getCostoUnitario());
        }
        return reg;
    }
    //Agrega configuración (versión, serie, moneda, método de pago, lugar de expedición, tipo de comprovante, forma de pago, número de cuenta
    //totales, conceptos e impuestos
    public Comprobante generarCFDIConDatosDeFormulario() throws Exception{
    byte temp1[] = oComprobanteCreado.getMetodoDePago().getBytes(StandardCharsets.ISO_8859_1),
         temp2[] = oComprobanteCreado.getLugarExpedicion().getBytes(StandardCharsets.ISO_8859_1);  
    String sMetodoPago =  new String(temp1, StandardCharsets.UTF_8),
           sLugarExpedicion = new String(temp2, StandardCharsets.UTF_8);
        RegistroDeComprobante reg=new RegistroDeComprobante();
        reg.configurarComponente(Configuracion.version,
                oComprobanteCreado.getSerie(),oComprobanteCreado.getMoneda(),
                sMetodoPago,sLugarExpedicion,
                oComprobanteCreado.getTipoDeComprobante(),
                oComprobanteCreado.getFormaDePago(),
                oComprobanteCreado.getNumCtaPago());
        reg.registrarTotales(this.oSubtotal, this.oTotal);
        oSubtotalGravadoFinal=BigDecimal.ZERO;
        oSubtotalNoGravadoFinal=BigDecimal.ZERO;
        for(ConceptoFacturableCaja concepto:conceptosParaFacturar){                            //Ya solo quedaron agrupados por tasa los diferentes conceptos
            reg.registrarConcepto(concepto.getUnidad(),concepto.getImporteTotalSinIva(),new BigDecimal(concepto.getCantidad()),concepto.getDescripcion(),concepto.getCostoUnitario());        
            if(concepto.getPctIVa()!=0){
                this.oSubtotalGravadoFinal=oSubtotalGravadoFinal.add(concepto.getImporteTotalSinIva());
                this.nTasaImp=concepto.getPctIVa();
            }else
                oSubtotalNoGravadoFinal=oSubtotalNoGravadoFinal.add(concepto.getImporteTotalSinIva());
        }
        this.oSubtotalFinal=oSubtotalGravadoFinal.add(oSubtotalNoGravadoFinal);
        oTotalFinal=oTotal;
        this.setCantidadLetra(oConvertidorNumeroLetra.convertirNumeroALetras(oTotalFinal.toString(),oComprobanteCreado.getMoneda()));        
        conceptosFinalesEnFactura=conceptosParaFacturar;
        if(getImpuestos()!=null){
            reg.registrarImpuestosTrasladadosIVA(getImpuestos(),"IVA",new BigDecimal(this.nTasaImp));
            this.oImpuestosFinal=getImpuestos();
        }
        return reg.getComprobante();  
    }
    

    public void generarCFDIParaVistaPrevia() throws Exception {
        oSubtotalGravadoFinal = BigDecimal.ZERO;
        oSubtotalNoGravadoFinal = BigDecimal.ZERO;
        for (ConceptoFacturableCaja concepto : conceptosParaFacturar) {
            if (concepto.getPctIVa() != 0) {
                this.oSubtotalGravadoFinal = oSubtotalGravadoFinal.add(concepto.getImporteTotalSinIva());
                this.nTasaImp = concepto.getPctIVa();
            } else {
                oSubtotalNoGravadoFinal = oSubtotalNoGravadoFinal.add(concepto.getImporteTotalSinIva());
            }
        }
        this.oSubtotalFinal = oSubtotalGravadoFinal.add(oSubtotalNoGravadoFinal);
        oTotalFinal = oTotal;
        this.setCantidadLetra(oConvertidorNumeroLetra.convertirNumeroALetras(oTotalFinal.toString(), oComprobanteCreado.getMoneda()));
        conceptosFinalesEnFactura = conceptosParaFacturar;
        if (getImpuestos() != null) {
            this.oImpuestosFinal = getImpuestos();
        }
    }
    
    public Comprobante generarNotaCreditoConDatosDeFormulario() throws Exception{
    byte temp2[] = oComprobanteCreado.getLugarExpedicion().getBytes(StandardCharsets.ISO_8859_1);  
    String sLugarExpedicion = new String(temp2, StandardCharsets.UTF_8);
        RegistroDeComprobante reg=new RegistroDeComprobante();
        reg.configurarComponente(Configuracion.version,
                oComprobanteCreado.getSerie(),"","No aplica",
                sLugarExpedicion,oComprobanteCreado.getTipoDeComprobante(),
                "No aplica","");
        ConceptoFacturableCaja concepto=new ConceptoFacturableCaja();
        concepto.setPctIVa(oFactura.getPctIVA());
        this.llenarConceptoFacturableCajaConDetalleCFDI(concepto);
        this.nTasaImp=oFactura.getPctIVA();
        this.oImpuestos=concepto.getImpuestoTotal();
        this.oSubtotal=concepto.getImporteTotalSinIva();
        this.oTotal=concepto.getImporteTotalSinIva().add(concepto.getImpuestoTotal());
        reg.registrarTotales(this.oSubtotal,this.oTotal);
        reg.registrarConcepto(concepto.getUnidad(),concepto.getImporteTotalSinIva(),new BigDecimal(concepto.getCantidad()),concepto.getDescripcion(),concepto.getCostoUnitario());        
        if(concepto.getImpuestoTotal()!=null)
            reg.registrarImpuestosTrasladadosIVA(concepto.getImpuestoTotal(),"IVA",new BigDecimal(oFactura.getPctIVA()));
        return reg.getComprobante();  
    }
     /*------------------------------------------------------------------------------------------------------------------------------*/
    /*                                                  ELIMINAR ELEMENTOS                                                           */
    /*------------------------------------------------------------------------------------------------------------------------------*/
    public void eliminarConceptoParaFacturar(String folio){
        boolean isElementoEliminado=false;
        if(sTipoFactura.equals("Credito-Empresas")){
            isElementoEliminado=eliminarConceptoParaFacturarEmpresa(isElementoEliminado,folio);
        }else if(sTipoFactura.equals("Rentas")|sTipoFactura.equals("Particulares")){
            isElementoEliminado=eliminarConceptoParaFacturarParticularesRentas(isElementoEliminado,folio);
        }else if(sTipoFactura.equals("PublicoGral")|sTipoFactura.equals("PublicoGralConTDC")){
            isElementoEliminado=eliminarConceptoParaFacturarPubGral(isElementoEliminado,folio);
        }else if(sTipoFactura.equals("Cuentas")|sTipoFactura.equals("Paquetes")){
            isElementoEliminado=eliminarConceptoParaFacturarCuentasPaquetes(isElementoEliminado,folio);
        }
        if(isElementoEliminado)
            if(conceptosParaFacturar.isEmpty())
                this.limpiarDatosTotalesConceptosCFDI();
        if(!isElementoEliminado){
           msj= new FacesMessage("No fue posible eliminar el elemento seleccionado");
           FacesContext.getCurrentInstance().addMessage(null, msj); 
        }
    }
    public boolean eliminarConceptoParaFacturarParticularesRentas(boolean isElementoEliminado,String folio){
        if(!isElementoEliminado){
            //Busca el concepto en lista_detalleCFDIConUnOpeCajaConcepto: un detalleCFDI para un concepto de caja
            for(DetalleCFDIConUnOpeCajaConcepto linea:lista_detalleCFDIConUnOpeCajaConcepto)
                if(linea.getDetalleCFDIOpeCajaConcepto().getFolioCaja()==Integer.parseInt(folio)){
                    for(ConceptoFacturableCaja c:conceptosParaFacturar)
                        if(c.getFolioOC().equals(folio)){
                            oSubtotal=oSubtotal.subtract(c.getImporteTotalSinIva());
                            oImpuestos=oImpuestos.subtract(c.getImpuestoTotal());
                            oTotal=oSubtotal.add(oImpuestos);
                            conceptosParaFacturar.remove(c);
                            conceptosSeleccionadosAcumulados.remove(c);
                            break;
                        }
                    lista_detalleCFDIConUnOpeCajaConcepto.remove(linea);
                    isElementoEliminado=true;
                    break;
                }
        }
        return isElementoEliminado;
    }
    public boolean eliminarConceptoParaFacturarPubGral(boolean isElementoEliminado,String folio){
        if(!isElementoEliminado){
            for(DetalleCFDIConVariosOpeCajaConcepto linea:lista_detalleCFDIConVariosOpeCajaConcepto)
                if(linea.getDetalleCFDI().getId()==Integer.parseInt(folio)){
                    for(DetalleCFDIOpeCajaConcepto detalle:linea.getDetallesCFDIOpeCajaConcepto())
                        for(ConceptoFacturableCaja con:conceptosSeleccionadosAcumulados)
                            if(detalle.getFolioCaja()==Integer.parseInt(con.getFolioOC())){
                                conceptosSeleccionadosAcumulados.remove(con);
                                break;
                            }
                    for(ConceptoFacturableCaja c:conceptosParaFacturar)
                        if(c.getFolioOC().equals(folio)){
                            oSubtotal=oSubtotal.subtract(c.getImporteTotalSinIva());
                            oImpuestos=oImpuestos.subtract(c.getImpuestoTotal());
                            oTotal=oSubtotal.add(oImpuestos);
                            conceptosParaFacturar.remove(c);
                            break;
                        }
                    lista_detalleCFDIConVariosOpeCajaConcepto.remove(linea);
                    conceptosSeleccionados.clear();
                    isElementoEliminado=true;
                    break;
                } 
        }
        return isElementoEliminado;
    }
    public boolean eliminarConceptoParaFacturarEmpresa(boolean isElementoEliminado,String folio){
        BigDecimal nMontoDeduciblesCoaseguros=BigDecimal.ZERO;
        BigDecimal nImpuestosDeduciblesCoaseguros=BigDecimal.ZERO;
        BigDecimal oTotalSinConceptoAEliminar=BigDecimal.ZERO;
        BigDecimal oImpuestosSinImpuestoAEliminar=BigDecimal.ZERO;
        boolean posibleEliminar=false;
        for(ConceptoFacturableCaja con:conceptosParaFacturar)
            if(con.getDescripcion().contains("PAGO DE DEDUCIBLE")|con.getDescripcion().contains("PAGO DE COASEGURO")){
                nMontoDeduciblesCoaseguros=con.getImporteTotalSinIva();
                nImpuestosDeduciblesCoaseguros=con.getImpuestoTotal();
            }
        for(ConceptoFacturableCaja conceptoFac:conceptosParaFacturar)          //coaseguro deducible
            if(conceptoFac.getFolioOC().equals(folio))
                if(conceptoFac.getDescripcion().contains("PAGO DE COASEGURO")|conceptoFac.getDescripcion().contains("PAGO DE DEDUCIBLE")){
                    conceptosParaFacturar.remove(conceptoFac);
                    oSubtotal=oSubtotal.add(conceptoFac.getImporteTotalSinIva());
                    oImpuestos=oImpuestos.add(conceptoFac.getImpuestoTotal());
                    oTotal=oSubtotal.add(oImpuestos);
                    for(ConceptoFacturableCaja c:conceptosSeleccionadosAcumulados)
                        if(c.getFolioOC().equals(folio)){
                            conceptosSeleccionadosAcumulados.remove(conceptoFac);
                            break;
                        }
                    isElementoEliminado=true;
                    break;
                }
        if(!isElementoEliminado)
            for(DetalleCFDIConUnOpeCajaConcepto linea:lista_detalleCFDIConUnOpeCajaConcepto)//Busca el concepto en lista_detalleCFDIConUnOpeCajaConcepto: un detalleCFDI para un concepto de caja
                if(linea.getDetalleCFDI().getId()==Integer.parseInt(folio)){
                    for(ConceptoFacturableCaja c:conceptosParaFacturar)
                        if(c.getFolioOC().equals(folio)){
                            oTotalSinConceptoAEliminar=oTotal.subtract(c.getImporteTotalSinIva());
                            oImpuestosSinImpuestoAEliminar=oImpuestos.subtract(c.getImpuestoTotal());
                            if((nMontoDeduciblesCoaseguros.compareTo(oTotalSinConceptoAEliminar)==0|nMontoDeduciblesCoaseguros.compareTo(oTotalSinConceptoAEliminar)==-1)&&
                                (nImpuestosDeduciblesCoaseguros.compareTo(oImpuestosSinImpuestoAEliminar)==0|nImpuestosDeduciblesCoaseguros.compareTo(oImpuestosSinImpuestoAEliminar)==-1)){                                                                            //Es coaseguro
                                oSubtotal=oSubtotal.subtract(c.getImporteTotalSinIva());
                                oImpuestos=oImpuestos.subtract(c.getImpuestoTotal());
                                oTotal=oSubtotal.add(oImpuestos);
                                conceptosParaFacturar.remove(c);
                                posibleEliminar=true;
                                break;
                            }
                        }
                    if(posibleEliminar){
                        for(ConceptoFacturableCaja con:conceptosSeleccionadosAcumulados)
                            if(linea.getDetalleCFDIOpeCajaConcepto().getFolioCaja()==Integer.parseInt(con.getFolioOC())){
                                conceptosSeleccionadosAcumulados.remove(con);
                                break;
                            }
                        lista_detalleCFDIConUnOpeCajaConcepto.remove(linea);
                        conceptosSeleccionados.clear();
                        isElementoEliminado=true;
                    }else{
                        msj= new FacesMessage(FacesMessage.SEVERITY_ERROR,"No es posible eliminar el concepto indicado, el monto total no puede quedar en valores negativos (Se tiene un coaseguro)","");
                        FacesContext.getCurrentInstance().addMessage(null, msj);
                        isElementoEliminado=true;
                    }
                    break;
                }
        if(!isElementoEliminado)
            for(DetalleCFDIConVariosOpeCajaConcepto linea:lista_detalleCFDIConVariosOpeCajaConcepto)//Busca el concepto en lista_detalleCFDIConVariosOpeCajaConcepto: un detalleCFDI para varios conceptos de caja
                if(linea.getDetalleCFDI().getId()==Integer.parseInt(folio)){
                    for(ConceptoFacturableCaja c:conceptosParaFacturar){
                        if(c.getFolioOC().equals(folio)){
                            oTotalSinConceptoAEliminar=oTotal.subtract(c.getImporteTotalSinIva());
                            oImpuestosSinImpuestoAEliminar=oImpuestos.subtract(c.getImpuestoTotal());
                            if((nMontoDeduciblesCoaseguros.compareTo(oTotalSinConceptoAEliminar)==0|nMontoDeduciblesCoaseguros.compareTo(oTotalSinConceptoAEliminar)==-1)&&
                                (nImpuestosDeduciblesCoaseguros.compareTo(oImpuestosSinImpuestoAEliminar)==0|nImpuestosDeduciblesCoaseguros.compareTo(oImpuestosSinImpuestoAEliminar)==-1)){
                                oSubtotal=oSubtotal.subtract(c.getImporteTotalSinIva());
                                oImpuestos=oImpuestos.subtract(c.getImpuestoTotal());
                                oTotal=oSubtotal.add(oImpuestos);
                                conceptosParaFacturar.remove(c);
                                posibleEliminar=true;
                                break;
                            }
                        }
                    }
                    if(posibleEliminar){
                       for(DetalleCFDIOpeCajaConcepto detalle:linea.getDetallesCFDIOpeCajaConcepto())
                        for(ConceptoFacturableCaja con:conceptosSeleccionadosAcumulados)
                            if(detalle.getFolioCaja()==Integer.parseInt(con.getFolioOC())){
                                conceptosSeleccionadosAcumulados.remove(con);
                                break;
                            }
                        lista_detalleCFDIConVariosOpeCajaConcepto.remove(linea);
                        conceptosSeleccionados.clear();
                        isElementoEliminado=true; 
                    }else{
                        msj= new FacesMessage(FacesMessage.SEVERITY_ERROR,"No es posible eliminar el concepto indicado, el monto total no puede quedar en valores negativos (Se tiene un coaseguro)","");
                        FacesContext.getCurrentInstance().addMessage(null, msj);
                        isElementoEliminado=true;
                    }
                    break;
                } 
        if(!isElementoEliminado){//Busca el concepto en lista_detallesCFDIConUnOpeCajaConcepto: varios detalleCFDI para un concepto de caja
            boolean conceptoEncontrado=false;
            for(DetallesCFDIConUnOpeCajaConcepto linea:lista_detallesCFDIConUnOpeCajaConcepto){
                for(DetalleCFDI detalle:linea.getDetallesCFDI())
                    if(detalle.getId()==Integer.parseInt(folio)){
                        conceptoEncontrado=true;
                        break;
                    }
                if(conceptoEncontrado){
                    oTotalSinConceptoAEliminar=oTotal;
                    oImpuestosSinImpuestoAEliminar=oImpuestos;
                    for(DetalleCFDI detalleCFDI:linea.getDetallesCFDI())
                        for(ConceptoFacturableCaja con:conceptosParaFacturar)
                            if((""+detalleCFDI.getId()).equals(con.getFolioOC())){
                                oTotalSinConceptoAEliminar=oTotalSinConceptoAEliminar.subtract(con.getImporteTotalSinIva());
                                oImpuestosSinImpuestoAEliminar=oImpuestosSinImpuestoAEliminar.subtract(con.getImpuestoTotal());
                            }
                    if((nMontoDeduciblesCoaseguros.compareTo(oTotalSinConceptoAEliminar)==0|nMontoDeduciblesCoaseguros.compareTo(oTotalSinConceptoAEliminar)==-1)&&
                        (nImpuestosDeduciblesCoaseguros.compareTo(oImpuestosSinImpuestoAEliminar)==0|nImpuestosDeduciblesCoaseguros.compareTo(oImpuestosSinImpuestoAEliminar)==-1)){
                        posibleEliminar=true;
                    }
                    if(posibleEliminar){
                        for(DetalleCFDI detalleCFDI:linea.getDetallesCFDI())
                            for(ConceptoFacturableCaja con:conceptosParaFacturar)
                                if((""+detalleCFDI.getId()).equals(con.getFolioOC())){
                                    conceptosParaFacturar.remove(con);
                                    oSubtotal=oSubtotal.subtract(con.getImporteTotalSinIva());
                                    oImpuestos=oImpuestos.subtract(con.getImpuestoTotal());
                                    oTotal=oSubtotal.add(oImpuestos);
                                    break;
                                }
                        for(ConceptoFacturableCaja concepto:conceptosSeleccionadosAcumulados)
                            if(linea.getDetalleCFDIOpeCajaConcepto().getFolioCaja()==Integer.parseInt(concepto.getFolioOC())){
                                conceptosSeleccionadosAcumulados.remove(concepto);
                                break;
                            }
                        lista_detallesCFDIConUnOpeCajaConcepto.remove(linea);
                        conceptosSeleccionados.clear();
                        isElementoEliminado=true;
                    }
                }else{
                        msj= new FacesMessage(FacesMessage.SEVERITY_ERROR,"No es posible eliminar el concepto indicado, el monto total no puede quedar en valores negativos (Se tiene un Coaseguro/Deducible)","");
                        FacesContext.getCurrentInstance().addMessage(null, msj);
                        isElementoEliminado=true;
                }  
                break;
            }
        }
        return isElementoEliminado;
    }
    public void eliminarConsumosCuentasPaquetes(DetalleCFDIConsumosContratos linea){
        if(this.sTipoFactura.equals("Cuentas")){
            for(DetalleCFDIServicio c:linea.getServiciosConsumidos())
                for(ConceptoFacturableCaja cf:consumosSeleccionadosAcumulados)
                    if(c.getIdFolioServPres().equals(cf.getFolioServicio())){
                        consumosSeleccionadosAcumulados.remove(cf);
                        break;
                    }
        }else{
            for(DetalleCFDIPaquete c:linea.getServiciosContratados())
                for(ConceptoFacturableCaja cf:consumosSeleccionadosAcumulados){
                    if(c.getFolio()==Integer.parseInt(cf.getFolioServicio())){
                        consumosSeleccionadosAcumulados.remove(cf);
                        break;
                    }
                }
        }
    }
    public boolean eliminarConceptoParaFacturarCuentasPaquetes(boolean isElementoEliminado,String folio){
        if(!isElementoEliminado)
            for(DetalleCFDIConUnOpeCajaConcepto linea:lista_detalleCFDIConUnOpeCajaConcepto){//Busca el concepto en lista_detalleCFDIConUnOpeCajaConcepto: un detalleCFDI para un concepto de caja
                if(linea.getDetalleCFDI().getId()==Integer.parseInt(folio)){
                    for(ConceptoFacturableCaja c:conceptosParaFacturar){
                        
                        if(c.getFolioOC().equals(folio)){
                        
                            oSubtotal=oSubtotal.subtract(c.getImporteTotalSinIva());
                                oImpuestos=oImpuestos.subtract(c.getImpuestoTotal());
                                oTotal=oSubtotal.add(oImpuestos);
                                conceptosParaFacturar.remove(c);
                                break;
                        }
                    }
                    for(ConceptoFacturableCaja con:conceptosSeleccionadosAcumulados){
                            if(linea.getDetalleCFDIOpeCajaConcepto().getFolioCaja()==Integer.parseInt(con.getFolioOC())){
                                conceptosSeleccionadosAcumulados.remove(con);
                                break;
                            }
                    }
                    eliminarConsumosCuentasPaquetes(linea);
                    lista_detalleCFDIConUnOpeCajaConcepto.remove(linea);
                    conceptosSeleccionados.clear();
                    isElementoEliminado=true;
                    break;
                }
            }
        if(!isElementoEliminado)
            for(DetalleCFDIConVariosOpeCajaConcepto linea:lista_detalleCFDIConVariosOpeCajaConcepto)//Busca el concepto en lista_detalleCFDIConVariosOpeCajaConcepto: un detalleCFDI para varios conceptos de caja
                if(linea.getDetalleCFDI().getId()==Integer.parseInt(folio)){
                    for(ConceptoFacturableCaja c:conceptosParaFacturar)
                        if(c.getFolioOC().equals(folio)){
                                oSubtotal=oSubtotal.subtract(c.getImporteTotalSinIva());
                                oImpuestos=oImpuestos.subtract(c.getImpuestoTotal());
                                oTotal=oSubtotal.add(oImpuestos);
                                conceptosParaFacturar.remove(c);
                                break;
                        }
                    for(DetalleCFDIOpeCajaConcepto detalle:linea.getDetallesCFDIOpeCajaConcepto())
                        for(ConceptoFacturableCaja con:conceptosSeleccionadosAcumulados)
                            if(detalle.getFolioCaja()==Integer.parseInt(con.getFolioOC())){
                                conceptosSeleccionadosAcumulados.remove(con);
                                break;
                            }
                    eliminarConsumosCuentasPaquetes(linea);
                    lista_detalleCFDIConVariosOpeCajaConcepto.remove(linea);
                    conceptosSeleccionados.clear();
                    isElementoEliminado=true; 
                    break;
                } 
        if(!isElementoEliminado){//Busca el concepto en lista_detallesCFDIConUnOpeCajaConcepto: varios detalleCFDI para un concepto de caja
            boolean conceptoEncontrado=false;
            for(DetallesCFDIConUnOpeCajaConcepto linea:lista_detallesCFDIConUnOpeCajaConcepto){
                for(DetalleCFDI detalle:linea.getDetallesCFDI())
                    if(detalle.getId()==Integer.parseInt(folio)){
                        conceptoEncontrado=true;
                        break;
                    }
                if(conceptoEncontrado){    
                        for(DetalleCFDI detalleCFDI:linea.getDetallesCFDI())
                            for(ConceptoFacturableCaja con:conceptosParaFacturar)
                                if((""+detalleCFDI.getId()).equals(con.getFolioOC())){
                                    conceptosParaFacturar.remove(con);
                                    oSubtotal=oSubtotal.subtract(con.getImporteTotalSinIva());
                                    oImpuestos=oImpuestos.subtract(con.getImpuestoTotal());
                                    oTotal=oSubtotal.add(oImpuestos);
                                    break;
                                }
                        for(ConceptoFacturableCaja concepto:conceptosSeleccionadosAcumulados)
                            if(linea.getDetalleCFDIOpeCajaConcepto().getFolioCaja()==Integer.parseInt(concepto.getFolioOC())){
                                conceptosSeleccionadosAcumulados.remove(concepto);
                                break;
                            }
                        eliminarConsumosCuentasPaquetes(linea);
                        lista_detallesCFDIConUnOpeCajaConcepto.remove(linea);
                        conceptosSeleccionados.clear();
                        isElementoEliminado=true;
                } 
                break;
            }
        }
        return isElementoEliminado;
    }
    //para el caso de seleccionar solo un concepto facturable y asociarlo a varias lineas de CFDI se tiene el método eliminarDetalleCFDI, en caso de que ya no quiera la línea agregada
    public void eliminarDetalleCFDI(int id){
       detallesCFDIConUnOpeCajaConcepto.eliminarDetalleCFDI(id);
   }
    /**********************************************************************************************************************
    /              GENERACIÓN DE QUERYS Y GUARDADO EN LA BASE DE DATOS: INCINIA                                           /
    **********************************************************************************************************************/
   //Considera conceptosSeleccionados (uno a uno aparecerán en la factura), considera la parte común, las lineas y la asociación de estas con los conceptos
    //de caja, se agregan deducibles o coaseguros en caso de requerirlo o se modifican cuando estos ya habían sido creados
    public void agregarQueryDeConceptos(Comprobante comprobante) throws Exception{
        DetalleCFDI detalleCFDI=new DetalleCFDI();
        int nLinDetalleCFDI=0;
        String consulta="";
        consulta=detalleCFDI.buscaProxFolioDetalleCFDI();
        if(!consulta.contains("ERROR")){
            nLinDetalleCFDI=Integer.parseInt(consulta);
        }
        nLinDetalleCFDI--;
        //Paso 3: por cada elemento de las listas: lista_detalleCFDIConUnOpeCajaConcepto,  lista_detalleCFDIConVariosOpeCajaConcepto, lista_detallesCFDIConUnOpeCajaConcepto se hará lo siguiente
        for(DetalleCFDIConUnOpeCajaConcepto elemento:lista_detalleCFDIConUnOpeCajaConcepto){
            nLinDetalleCFDI++;
            //Para el detalleCFDI
            elemento.getDetalleCFDI().setLinCfdi(nLinDetalleCFDI);
            elemento.getDetalleCFDI().setNombreSerie(comprobante.getSerie());
            elemento.getDetalleCFDI().setFolio(Integer.parseInt(comprobante.getFolio()));
            comprobante.agregarQuery(elemento.getDetalleCFDI().agregarQueryInsertarDetalleCFDI());        //Un DetalleCFDI
            //Para el DetalleCFDIOpeCajaConcepto
            elemento.getDetalleCFDIOpeCajaConcepto().setNombreSerie(comprobante.getSerie());
            elemento.getDetalleCFDIOpeCajaConcepto().setFolio(Integer.parseInt(comprobante.getFolio()));
            elemento.getDetalleCFDIOpeCajaConcepto().setLinCfdi(nLinDetalleCFDI);
            comprobante.agregarQuery(elemento.getDetalleCFDIOpeCajaConcepto().agregarQueryInsertarDetalleCFDIOpeCajaConc());   //un DetalleCFDIOpeCajaConcepto
            comprobante.agregarQuery(elemento.getDetalleCFDIOpeCajaConcepto().agregarQueryModificarOpeCaja());
            if(elemento.getDetalleCFDIOpeCajaConcepto().getTipo()!=null){
                if(elemento.getDetalleCFDIOpeCajaConcepto().getTipo().equals("DEDUCIBLE")&&!sTipoFactura.equals("Credito-Empresas")){
                    comprobante.agregarQueryParaActualizarDeducible(elemento.getDetalleCFDIOpeCajaConcepto().getIdTipo()); 
                }if(elemento.getDetalleCFDIOpeCajaConcepto().getTipo().equals("COASEGURO")&&!sTipoFactura.equals("Credito-Empresas")){
                    comprobante.agregarQueryParaActualizarCoaseguro(elemento.getDetalleCFDIOpeCajaConcepto().getIdTipo());
                }
            }
            if(elemento.getDetalleCFDI().getDescrip().contains("PAGO DE COASEGURO, A REALIZARSE POR EL CLIENTE")&&sTipoFactura.equals("Credito-Empresas")){
                comprobante.agregarQueryParaInsertarCoaseguroEmpresa(elemento.getDetalleCFDI().getMonto().multiply(new BigDecimal("1."+this.nTasaImp)),this.nClaveEpisodioMed,this.nFolioPaciente); 
            }
            if(elemento.getDetalleCFDI().getDescrip().contains("PAGO DE DEDUCIBLE, A REALIZARSE POR EL CLIENTE")&&sTipoFactura.equals("Credito-Empresas")){
                comprobante.agregarQueryParaInsertarDeducibleEmpresa(elemento.getDetalleCFDI().getMonto().multiply(new BigDecimal("1."+this.nTasaImp)),this.nClaveEpisodioMed,this.nFolioPaciente); 
            }
            if(sTipoFactura.equals("Cuentas")){
                for(DetalleCFDIServicio detalleServ: elemento.getServiciosConsumidos()){
                    detalleServ.setNombreSerie(comprobante.getSerie());
                    detalleServ.setFolio(Integer.parseInt(comprobante.getFolio()));
                    detalleServ.setLinCfdi(nLinDetalleCFDI);
                    comprobante.agregarQuery(detalleServ.agregarQueryInsertarDetalleCFDIServicio());
                }
            }
            if(sTipoFactura.equals("Paquetes")){
                for(DetalleCFDIPaquete detallePaq: elemento.getServiciosContratados()){
                    detallePaq.setNombreSerie(comprobante.getSerie());
                    detallePaq.setFolio(Integer.parseInt(comprobante.getFolio()));
                    detallePaq.setLinCfdi(nLinDetalleCFDI);
                    comprobante.agregarQuery(detallePaq.agregarQueryInsertarDetalleCFDIPaquete());
                }
            }
        } 
        for(DetalleCFDIConVariosOpeCajaConcepto elemento:lista_detalleCFDIConVariosOpeCajaConcepto){
            nLinDetalleCFDI++;
            //Para el detalleCFDI
            elemento.getDetalleCFDI().setLinCfdi(nLinDetalleCFDI);
            elemento.getDetalleCFDI().setNombreSerie(comprobante.getSerie());
            elemento.getDetalleCFDI().setFolio(Integer.parseInt(comprobante.getFolio()));
            comprobante.agregarQuery(elemento.getDetalleCFDI().agregarQueryInsertarDetalleCFDI());        //un DetalleCFDI
            //Para el DetalleCFDIOpeCajaConcepto
            for(DetalleCFDIOpeCajaConcepto detalle:elemento.getDetallesCFDIOpeCajaConcepto()){
                detalle.setNombreSerie(comprobante.getSerie());
                detalle.setFolio(Integer.parseInt(comprobante.getFolio()));
                detalle.setLinCfdi(nLinDetalleCFDI);
                comprobante.agregarQuery(detalle.agregarQueryInsertarDetalleCFDIOpeCajaConc());           //Varios DetalleCFDIOpeCajaConcepto
                comprobante.agregarQuery(detalle.agregarQueryModificarOpeCaja());
                if(detalle.getTipo()!=null){
                    if(detalle.getTipo().equals("COASEGURO")&&!sTipoFactura.equals("Credito-Empresas"))
                        comprobante.agregarQueryParaActualizarDeducible(detalle.getIdTipo()); 
                    if(detalle.getTipo().equals("DEDUCIBLE")&&!sTipoFactura.equals("Credito-Empresas"))
                        comprobante.agregarQueryParaActualizarCoaseguro(detalle.getIdTipo());
                }
            }
            if(sTipoFactura.equals("Cuentas")){
                for(DetalleCFDIServicio detalleServ: elemento.getServiciosConsumidos()){
                    detalleServ.setNombreSerie(comprobante.getSerie());
                    detalleServ.setFolio(Integer.parseInt(comprobante.getFolio()));
                    detalleServ.setLinCfdi(nLinDetalleCFDI);
                    comprobante.agregarQuery(detalleServ.agregarQueryInsertarDetalleCFDIServicio());
                }
            }
            if(sTipoFactura.equals("Paquetes")){
                for(DetalleCFDIPaquete detallePaq: elemento.getServiciosContratados()){
                    detallePaq.setNombreSerie(comprobante.getSerie());
                    detallePaq.setFolio(Integer.parseInt(comprobante.getFolio()));
                    detallePaq.setLinCfdi(nLinDetalleCFDI);
                    comprobante.agregarQuery(detallePaq.agregarQueryInsertarDetalleCFDIPaquete());
                }
            }
        }
        for(DetallesCFDIConUnOpeCajaConcepto elemento:lista_detallesCFDIConUnOpeCajaConcepto){
            elemento.getDetalleCFDIOpeCajaConcepto().setNombreSerie(comprobante.getSerie());
            elemento.getDetalleCFDIOpeCajaConcepto().setFolio(Integer.parseInt(comprobante.getFolio()));
            comprobante.agregarQuery(elemento.getDetalleCFDIOpeCajaConcepto().agregarQueryModificarOpeCaja());
            for(DetalleCFDI detalle:elemento.getDetallesCFDI()){
                nLinDetalleCFDI++;
                detalle.setLinCfdi(nLinDetalleCFDI);
                detalle.setNombreSerie(comprobante.getSerie());
                detalle.setFolio(Integer.parseInt(comprobante.getFolio()));
                comprobante.agregarQuery(detalle.agregarQueryInsertarDetalleCFDI());          //Varios DetalleCFDI
                elemento.getDetalleCFDIOpeCajaConcepto().setLinCfdi(nLinDetalleCFDI);
                comprobante.agregarQuery(elemento.getDetalleCFDIOpeCajaConcepto().agregarQueryInsertarDetalleCFDIOpeCajaConc());  //varios DetalleCFDIOpeCajaConcepto
                if(sTipoFactura.equals("Cuentas")){
                    for(DetalleCFDIServicio detalleServ: elemento.getServiciosConsumidos()){
                        detalleServ.setNombreSerie(comprobante.getSerie());
                        detalleServ.setFolio(Integer.parseInt(comprobante.getFolio()));
                        detalleServ.setLinCfdi(nLinDetalleCFDI);
                        comprobante.agregarQuery(detalleServ.agregarQueryInsertarDetalleCFDIServicio());
                    }
                }
                if(sTipoFactura.equals("Paquetes")){
                    for(DetalleCFDIPaquete detallePaq: elemento.getServiciosContratados()){
                        detallePaq.setNombreSerie(comprobante.getSerie());
                        detallePaq.setFolio(Integer.parseInt(comprobante.getFolio()));
                        detallePaq.setLinCfdi(nLinDetalleCFDI);
                        comprobante.agregarQuery(detallePaq.agregarQueryInsertarDetalleCFDIPaquete());
                    }
                }
            } 
        }
    }
    public void agregarQueryDeConceptosNotaCredito(Comprobante comprobante) throws Exception{
        int nLinDetalleCFDI=0;
        DetalleCFDI detalleCFDI=new DetalleCFDI();
        String consulta="";
        consulta=detalleCFDI.buscaProxFolioDetalleCFDI();
        if(!consulta.contains("ERROR")){
            nLinDetalleCFDI=Integer.parseInt(consulta);
        }
        detalleCFDI.setCant(this.cantidadDetalleCFDI);
        detalleCFDI.setDescrip(this.descripcionDetalleCFDI);
        detalleCFDI.setLinCfdi(nLinDetalleCFDI);
        detalleCFDI.setMonto(this.montoDetalleCFDI);
        detalleCFDI.setNombreSerie(comprobante.getSerie());
        detalleCFDI.setUnidad(this.unidadDetalleCFDI);
        detalleCFDI.setFolio(Integer.parseInt(comprobante.getFolio()));
        comprobante.agregarQuery(detalleCFDI.agregarQueryInsertarDetalleCFDI());        //Un DetalleCFDI
    }
    public boolean guardarFacturaEnBD(Comprobante comprobante) throws Exception{
        //------------------------------Parte en comun para todos----------------------------------
        String respuesta="";
        comprobante.actualizarFolio();      //al Comprobante se le pone el folio que debe tomar el CFDI cuando se hagan los registros
        //Paso 1: Insertar en la tabla "cfdi"  <-La factura
        comprobante.agregarQueryParaInsertarCFDI(this.sTipoDocumento,this.sTipoFactura);  //ya
        //Paso 2: Insertar en la tabla "traslado"   <-Los impuestos
        comprobante.agregarQueryParaInsertarImpuestosTrasladados(this.oImpuestos,this.nTasaImp); //ya
        //Paso 3: Para cada una de los diferentes tipos de factura se hara lo siguiente:
        agregarQueryDeConceptos(comprobante);     
        //Paso 4: Se ejecuta el query que ha concatenado todo lo necesario para agregar a la base de datos los registros y actualizaciones
        
        respuesta =comprobante.ejecutarQuery();
        if(respuesta.contains("ERROR"))
            return false;
        else 
            return true;
    }
    public boolean guardarNotaCreditoEnBD(Comprobante comprobante) throws Exception{
        //------------------------------Parte en comun para todos----------------------------------
        String respuesta="";
        comprobante.actualizarFolio();      //al Comprobante se le pone el folio que debe tomar el CFDI cuando se hagan los registros
        //Paso 1: Insertar en la tabla "cfdi"  <-La factura
        comprobante.agregarQueryParaInsertarCFDI(this.sTipoDocumento,this.sTipoFactura);  //ya
        //Paso 2: Insertar en la tabla "traslado"   <-Los impuestos
        comprobante.agregarQueryParaInsertarImpuestosTrasladados(this.oImpuestos,this.nTasaImp); //ya
        //Paso 3: Para cada una de los diferentes tipos de factura se hara lo siguiente:
        this.agregarQueryDeConceptosNotaCredito(comprobante); 
        comprobante.agregarQueryReferenciaCFDINotaCredito(this.oFactura.getNombreSerie(),this.oFactura.getFolio(),comprobante.getSerie(),Integer.parseInt(comprobante.getFolio()));
        //Paso 4: Se ejecuta el query que ha concatenado todo lo necesario para agregar a la base de datos los registros y actualizaciones
        respuesta =comprobante.ejecutarQuery();
        if(respuesta.contains("ERROR"))
            return false;
        else 
            return true;
    } 
    /*********************************************************************************************************************
    /              GENERACIÓN DE QUERYS Y GUARDADO EN LA BASE DE DATOS: TERMINA
    *********************************************************************************************************************/
   
    /*********************************************************************************************************************
    /        LLENADO DE LA TABLA DE LOS CONCEPTOS QUE VERÁ EL USUARIO Y QUE PODRÁ SELECCIONAR PARA FACTURAR: INICIA        /
    *********************************************************************************************************************/
    //llena un listado de pacientes con cuentas de hospital que tiene conceptos para facturar
    public void buscarCuentasPacienteParticularPorNombre() throws Exception{
        oCuentasPacientesPorNombre=oCuentaPacienteSeleccionado.buscarCuentasParaFacturarDePacientesPorNombre(this.sNombrePaciente,this.sApPaternoPaciente,this.sApMaternoPaciente);    
        this.conceptosTodosConsultados=new ArrayList<ConceptoFacturableCaja>();
    }
    //llena un listado cuentas de pacientes de empresa que tienen pencientes de facturar
    public void buscarCuentasPacientesEmpresaPorNombre() throws Exception{
        if(this.oReceptorSeleccionado==null){
            msj= new FacesMessage(FacesMessage.SEVERITY_ERROR,"Debe seleccionar un receptor (empresa) a la que correspondan los pacientes que desea visualizar","");
            FacesContext.getCurrentInstance().addMessage(null, msj);
        }else{
            oCuentasPacientesPorNombre=oCuentaPacienteSeleccionado.buscarCuentasParaFacturarDePacientesEmpresaPorNombre(this.sNombrePaciente,this.sApPaternoPaciente,this.sApMaternoPaciente,this.oReceptorSeleccionado.getRfc());    
            this.conceptosTodosConsultados=new ArrayList<ConceptoFacturableCaja>();
        }
    }       
    //llena un listado cuentas de pacientes con paquetes que tienen pendientes de facturar
    public void buscarPaquetesPacientesPorNombre() throws Exception{
        oCuentasPacientesPorNombre=oCuentaPacienteSeleccionado.buscarCuentasParaFacturarDePaquetesPorNombre(this.sNombrePaciente,this.sApPaternoPaciente,this.sApMaternoPaciente);    
        this.conceptosTodosConsultados=new ArrayList<ConceptoFacturableCaja>();
    } 
    //llena un listado de pacientes particulares que tienen conceptos por facturar
    public void buscarFoliosPacienteParticularPorNombre() throws Exception{
        this.sTipoBusquedaParticulares="";
        oCuentasPacientesPorNombre=oCuentaPacienteSeleccionado.buscarFoliosParaFacturarDePacientesPorNombre(this.sNombrePaciente,this.sApPaternoPaciente,this.sApMaternoPaciente);    
    }
    public void buscarConceptosPaquetes() throws Exception{
        if(this.oCuentaPacienteSeleccionado==null) {
            msj= new FacesMessage(FacesMessage.SEVERITY_ERROR,"Debe seleccionar algún paquete","");
            FacesContext.getCurrentInstance().addMessage(null, msj);
        }
        if (this.oCuentaPacienteSeleccionado!=null){
           int pctIVA=0;
            listaConceptosFacturables=llenarConceptosPaquetes();
            for(ConceptoFacturableCaja conceptoSeleccionado: conceptosSeleccionadosAcumulados)
                for (ConceptoFacturableCaja conceptoConsultado:listaConceptosFacturables)
                    if(conceptoConsultado.getFolioOC().equals(conceptoSeleccionado.getFolioOC())){
                        listaConceptosFacturables.remove(conceptoConsultado);
                        if(conceptoSeleccionado.getPctIVa()!=0){
                            pctIVA=conceptoSeleccionado.getPctIVa();
                        }
                        break;
                    }
            setConceptosTodosConsultados(listaConceptosFacturables);
            this.setConceptosPaquetesCuentas(llenarContratosPaquetes(pctIVA));
        }
    }
    //Llena la tabla dependiendo del tipo de factura a realizar
    public void buscarConceptos() throws Exception{
        boolean verifica=comprobarSiEsPosibleBusquedaConceptos(); 
        if(verifica){
            if(sTipoFactura.equals("Credito-Empresas")){   
                if(this.oReceptorSeleccionado!=null)
                    listaConceptosFacturables=llenarConceptosCreditoEmpresas();
            }else if(sTipoFactura.equals("PublicoGral")){   
                listaConceptosFacturables=llenarConceptosPublicoGral();
            }else if(sTipoFactura.equals("PublicoGralConTDC")){
                listaConceptosFacturables=llenarConceptosPublicoGralTarjetaCredito();
            }else if(sTipoFactura.equals("Rentas")) 
                listaConceptosFacturables=llenarConceptosContadoRentas();
            for(ConceptoFacturableCaja conceptoSeleccionado: conceptosSeleccionadosAcumulados)
                for (ConceptoFacturableCaja conceptoConsultado:listaConceptosFacturables)
                    if(conceptoConsultado.getFolioOC().equals(conceptoSeleccionado.getFolioOC())){
                        listaConceptosFacturables.remove(conceptoConsultado);
                        break;
                    }
            conceptosSeleccionados.clear();
            setConceptosTodosConsultados(listaConceptosFacturables);
        }
    }
    public void buscarCoasegurosDeduciblesEmpresa()throws Exception{
        boolean verifica=comprobarSiEsPosibleBusquedaConceptos(); 
        if(verifica){
            listaConceptosFacturables =oConcepto.getConceptosCoasegurosDeduciblesFacturasEmpresas(this.oReceptorSeleccionado.getRfc());
            for(ConceptoFacturableCaja conceptoSeleccionado: conceptosSeleccionadosAcumulados)
                for (ConceptoFacturableCaja conceptoConsultado:listaConceptosFacturables)
                    if(conceptoConsultado.getFolioOC().equals(conceptoSeleccionado.getFolioOC())){
                        listaConceptosFacturables.remove(conceptoConsultado);
                        break;
                    }
            setConceptosTodosConsultados(listaConceptosFacturables);  
        }
        
    }
    public void buscarConceptosPacientesParticulares() throws Exception{
        if(this.oCuentaPacienteSeleccionado==null) {
            msj= new FacesMessage(FacesMessage.SEVERITY_ERROR,"Debe seleccionar un paciente","");
            FacesContext.getCurrentInstance().addMessage(null, msj);
        }   
        if(this.sTipoBusquedaParticulares.equals("")) {
            msj= new FacesMessage(FacesMessage.SEVERITY_ERROR,"Debe indicar el tipo de busqueda: general o por servicio","");
            FacesContext.getCurrentInstance().addMessage(null, msj);
        } 
        if (this.oCuentaPacienteSeleccionado!=null){
            if(this.oCuentaPacienteSeleccionado.getFolioPaciente()!=0 && !this.sTipoBusquedaParticulares.equals("")){
            listaConceptosFacturables=llenarConceptosContadoPart();
            for(ConceptoFacturableCaja conceptoSeleccionado: conceptosSeleccionadosAcumulados)
                for (ConceptoFacturableCaja conceptoConsultado:listaConceptosFacturables)
                    if(conceptoConsultado.getFolioOC().equals(conceptoSeleccionado.getFolioOC())){
                        listaConceptosFacturables.remove(conceptoConsultado);
                        break;
                    }
            conceptosSeleccionados.clear();
            setConceptosTodosConsultados(listaConceptosFacturables);
            }
        }
    }
    
    //Busca conceptos para facturar de las cuentas de hospital de particulares
    public void buscarConceptosCuentasParticulares() throws Exception{
        if (this.oCuentaPacienteSeleccionado!=null && this.oCuentaPacienteSeleccionado.getFolioPaciente()!=0){
            listaConceptosFacturables=llenarConceptosCuentas();
            for(ConceptoFacturableCaja conceptoSeleccionado: conceptosSeleccionadosAcumulados)
                for (ConceptoFacturableCaja conceptoConsultado:listaConceptosFacturables)
                    if(conceptoConsultado.getFolioOC().equals(conceptoSeleccionado.getFolioOC())){
                        listaConceptosFacturables.remove(conceptoConsultado);
                        break;
                    }
            conceptosSeleccionados.clear();
            setConceptosTodosConsultados(listaConceptosFacturables);
            listaConceptosFacturables=this.llenarConsumosCuentas();
            for(ConceptoFacturableCaja conceptoSeleccionado: consumosSeleccionadosAcumulados)
                for (ConceptoFacturableCaja conceptoConsultado:listaConceptosFacturables)
                    if(conceptoConsultado.getFolioServicio().equals(conceptoSeleccionado.getFolioServicio())){
                        listaConceptosFacturables.remove(conceptoConsultado);
                        break;
                    }
            consumosSeleccionados.clear();
            this.setConceptosPaquetesCuentas(listaConceptosFacturables);
        }else{
             msj= new FacesMessage(FacesMessage.SEVERITY_ERROR,"Debe seleccionar una cuenta de paciente","");
             FacesContext.getCurrentInstance().addMessage(null, msj);
        }
    }
    //Busca conceptos para facturar de las cuentas de pacientes de empresas
    public void buscarConceptosCuentasEmpresa() throws Exception{
        if (this.oCuentaPacienteSeleccionado!=null && this.oCuentaPacienteSeleccionado.getFolioPaciente()!=0){
            listaConceptosFacturables=llenarConceptosCuentasEmpresas();
            for(ConceptoFacturableCaja conceptoSeleccionado: conceptosSeleccionadosAcumulados)
                for (ConceptoFacturableCaja conceptoConsultado:listaConceptosFacturables)
                    if(conceptoConsultado.getFolioOC().equals(conceptoSeleccionado.getFolioOC())){
                        listaConceptosFacturables.remove(conceptoConsultado);
                        break;
                    }
            conceptosSeleccionados.clear();
            setConceptosTodosConsultados(listaConceptosFacturables);
            listaConceptosFacturables=this.llenarConsumosCuentas();
            for(ConceptoFacturableCaja conceptoSeleccionado: consumosSeleccionadosAcumulados)
                for (ConceptoFacturableCaja conceptoConsultado:listaConceptosFacturables)
                    if(conceptoConsultado.getFolioServicio().equals(conceptoSeleccionado.getFolioServicio())){
                        listaConceptosFacturables.remove(conceptoConsultado);
                        break;
                    }
            consumosSeleccionados.clear();
            this.setConceptosPaquetesCuentas(listaConceptosFacturables);
        }else{
             msj= new FacesMessage(FacesMessage.SEVERITY_ERROR,"Debe seleccionar una cuenta de empresa","");
             FacesContext.getCurrentInstance().addMessage(null, msj);
        }
    }
    //Busca deducibles o coaseguros para facturar de las cuentas de hospital de particulares
    public void buscarCoasegurosDeduciblesCuentasEmpresas() throws Exception{
        if (this.oCuentaPacienteSeleccionado!=null && this.oCuentaPacienteSeleccionado.getFolioPaciente()!=0){
            listaConceptosFacturables=llenarConceptosDeduciblesCoasegurosCuentasEmpresas();
            for(ConceptoFacturableCaja conceptoSeleccionado: conceptosSeleccionadosAcumulados)
                for (ConceptoFacturableCaja conceptoConsultado:listaConceptosFacturables)
                    if(conceptoConsultado.getFolioOC().equals(conceptoSeleccionado.getFolioOC())){
                        listaConceptosFacturables.remove(conceptoConsultado);
                        break;
                    }
            conceptosSeleccionados.clear();
            setConceptosTodosConsultados(listaConceptosFacturables);   
        }else{
             msj= new FacesMessage(FacesMessage.SEVERITY_ERROR,"Debe seleccionar una cuenta de paciente","");
             FacesContext.getCurrentInstance().addMessage(null, msj);
        }
    }
    //Busca deducibles o coaseguros para facturar de las cuentas de hospital de particulares
    public void buscarCoasegurosDeduciblesCuentasParticulares() throws Exception{
        if (this.oCuentaPacienteSeleccionado!=null && this.oCuentaPacienteSeleccionado.getFolioPaciente()!=0){
            listaConceptosFacturables=llenarConceptosDeduciblesCoasegurosCuentas();
            for(ConceptoFacturableCaja conceptoSeleccionado: conceptosSeleccionadosAcumulados)
                for (ConceptoFacturableCaja conceptoConsultado:listaConceptosFacturables)
                    if(conceptoConsultado.getFolioOC().equals(conceptoSeleccionado.getFolioOC())){
                        listaConceptosFacturables.remove(conceptoConsultado);
                        break;
                    }
            conceptosSeleccionados.clear();
            setConceptosTodosConsultados(listaConceptosFacturables);   
        }else{
             msj= new FacesMessage(FacesMessage.SEVERITY_ERROR,"Debe seleccionar una cuenta de paciente","");
             FacesContext.getCurrentInstance().addMessage(null, msj);
        }
    }
    
    private List<ConceptoFacturableCaja> llenarConceptosPaquetes() throws Exception {
        listaConceptosFacturables =oConcepto.getConceptosFacturasPaquetes(oCuentaPacienteSeleccionado.getNumPaquete());
        return listaConceptosFacturables;
    }
     private List<ConceptoFacturableCaja> llenarConceptosCuentasEmpresas() throws Exception {
        listaConceptosFacturables =oConcepto.getConceptosFacturasCuentasEmpresa(oCuentaPacienteSeleccionado.getFolioPaciente(),oCuentaPacienteSeleccionado.getCveEpisodio());
        return listaConceptosFacturables;
    }
    private List<ConceptoFacturableCaja> llenarConceptosCuentas() throws Exception {
        listaConceptosFacturables =oConcepto.getConceptosFacturasCuentasParticulares(oCuentaPacienteSeleccionado.getFolioPaciente(),oCuentaPacienteSeleccionado.getCveEpisodio());
        return listaConceptosFacturables;
    }
    private List<ConceptoFacturableCaja> llenarContratosPaquetes(int pctIVA) throws Exception {
        listaConceptosFacturables =oConcepto.getConceptosContratosPaquetes(oCuentaPacienteSeleccionado.getNumPaquete(),pctIVA);
        return listaConceptosFacturables;
    }
    private List<ConceptoFacturableCaja> llenarConsumosCuentas() throws Exception {
        listaConceptosFacturables =oConcepto.getConceptosConsumosCuentasParticulares(oCuentaPacienteSeleccionado.getFolioPaciente(),oCuentaPacienteSeleccionado.getCveEpisodio());
        return listaConceptosFacturables;
    }
    private List<ConceptoFacturableCaja> llenarConceptosDeduciblesCoasegurosCuentas() throws Exception {
        listaConceptosFacturables =oConcepto.getConceptosDeduciblesCoasegurosFacturasCuentasParticulares(oCuentaPacienteSeleccionado.getFolioPaciente(),oCuentaPacienteSeleccionado.getCveEpisodio());
        return listaConceptosFacturables;
    }
    private List<ConceptoFacturableCaja> llenarConceptosDeduciblesCoasegurosCuentasEmpresas() throws Exception {
        listaConceptosFacturables =oConcepto.getConceptosCoasegurosDeduciblesFacturasCuentasEmpresas(oCuentaPacienteSeleccionado.getFolioPaciente(),oCuentaPacienteSeleccionado.getCveEpisodio());
        return listaConceptosFacturables;
    }
    private List<ConceptoFacturableCaja> llenarConceptosCreditoEmpresas() throws Exception {
        listaConceptosFacturables =oConcepto.getConceptosFacturasEmpresas(this.oReceptorSeleccionado.getRfc());
        return listaConceptosFacturables;
    }
    private List<ConceptoFacturableCaja> llenarConceptosContadoPart() throws Exception {
        if(this.sTipoBusquedaParticulares.equals("servicio"))
            listaConceptosFacturables=oConcepto.getConceptoFacturasParticulares(oCuentaPacienteSeleccionado.getFolioPaciente());
        else
            listaConceptosFacturables=oConcepto.getConceptoFacturasParticularesPorNombreGeneral(oCuentaPacienteSeleccionado.getFolioPaciente());
        return listaConceptosFacturables;
    }
    private List<ConceptoFacturableCaja> llenarConceptosPublicoGral() throws Exception {
        listaConceptosFacturables=oConcepto.getConceptoFacturasPublicoEnGeneral();
        return listaConceptosFacturables;
    }
    private List<ConceptoFacturableCaja> llenarConceptosPublicoGralTarjetaCredito() throws Exception {
        listaConceptosFacturables=oConcepto.getConceptoFacturasPublicoEnGeneralTarjetaCredito();
        return listaConceptosFacturables;
    }
    private List<ConceptoFacturableCaja> llenarConceptosContadoRentas() throws Exception {
        listaConceptosFacturables=oConcepto.getConceptoFacturasRentas();
        return listaConceptosFacturables;
    } 
    //*********************************************************************************************************************
    //       LLENADO DE LA TABLA DE LOS CONCEPTOS QUE VERÁ EL USUARIO Y QUE PODRÁ SELECCIONAR PARA FACTURAR: TERMINA
    //********************************************************************************************************************* 
    public boolean revisarNotaCredito(){
        boolean bValida=true;     
        ExternalContext extCont= FacesContext.getCurrentInstance().getExternalContext();
        String rutaBase=(extCont.getRealPath("//BovedaFiscal//Certificados//"))+"\\"+oFactura.getEmisor().getRfc();
        File file;
        file= new File(rutaBase+".key");
        if(!file.exists()){
            msj= new FacesMessage(FacesMessage.SEVERITY_ERROR,"La llave del certificado de sello digital no se encuentra, contactarse con soporte técnico","");
            FacesContext.getCurrentInstance().addMessage(null, msj);
            bValida=false; 
        }
        file= new File(rutaBase+".cer");
        if(!file.exists()){
            msj= new FacesMessage(FacesMessage.SEVERITY_ERROR,"El certificado de sello digital no se encuentra, contactarse con soporte técnico","");
            FacesContext.getCurrentInstance().addMessage(null, msj);
            bValida=false; 
        }
        if(oFactura.getEmisor()==null){
            msj= new FacesMessage(FacesMessage.SEVERITY_ERROR,"El emisor no ha sido indicado","");
            FacesContext.getCurrentInstance().addMessage(null, msj);
            bValida=false;
        }
        if(this.sRegimen==null|this.sRegimen.equals("")){
            msj= new FacesMessage(FacesMessage.SEVERITY_ERROR,"El regimen fiscal no ha sido indicado","");
            FacesContext.getCurrentInstance().addMessage(null, msj);
            bValida=false;
        }
        if(oComprobanteCreado.getSerie()==null |(oComprobanteCreado.getSerie()!=null&&oComprobanteCreado.getSerie().equals(""))){
            msj= new FacesMessage(FacesMessage.SEVERITY_ERROR,"La serie de la Nota de Crédito no ha sido indicada","");
            FacesContext.getCurrentInstance().addMessage(null, msj);
            bValida=false;
        }
        if(oComprobanteCreado.getLugarExpedicion()==null|(oComprobanteCreado.getLugarExpedicion()!=null&&oComprobanteCreado.getLugarExpedicion().equals(""))){
            msj= new FacesMessage(FacesMessage.SEVERITY_ERROR,"El lugar de expedición no ha sido indicado","");
            FacesContext.getCurrentInstance().addMessage(null, msj);
            bValida=false;
        }
        if((this.getDescripcionDetalleCFDI()!=null && this.getDescripcionDetalleCFDI().equals("")) |
            (this.getUnidadDetalleCFDI()!=null && this.getUnidadDetalleCFDI().equals(""))|
            this.getMontoDetalleCFDI().floatValue()==0 |  this.getCantidadDetalleCFDI()==0){
            msj= new FacesMessage(FacesMessage.SEVERITY_ERROR,"Debe proporcionar cantidad, unidad, descripción y monto","");
            FacesContext.getCurrentInstance().addMessage(null, msj);
            bValida=false;
        }else if(this.getMontoDetalleCFDI().floatValue()>oFactura.getImporteTotal() ){
            msj= new FacesMessage(FacesMessage.SEVERITY_ERROR,"El monto de la nota de crédito no debe ser mayor al monto de la factura consultada","");
            FacesContext.getCurrentInstance().addMessage(null, msj);
            bValida=false;
        }
        this.bComprobanteValido=bValida;
        return bValida;
    }
    public boolean revisarCFDI(){
        boolean bValida=true;    
        ExternalContext extCont= FacesContext.getCurrentInstance().getExternalContext();
        String rutaBase="";
        File file;
        if(oEmisorSeleccionado!=null){
            rutaBase=(extCont.getRealPath("//BovedaFiscal//Certificados//"))+"\\"+oEmisorSeleccionado.getRfc();
        }
        file= new File(rutaBase+".key");
        if(!file.exists()){
            msj= new FacesMessage(FacesMessage.SEVERITY_ERROR,"La llave del certificado de sello digital no se encuentra, contactarse con soporte técnico","");
            FacesContext.getCurrentInstance().addMessage(null, msj);
            bValida=false; 
        }
        file= new File(rutaBase+".cer");
        if(!file.exists()){
            msj= new FacesMessage(FacesMessage.SEVERITY_ERROR,"El certificado de sello digital no se encuentra, contactarse con soporte técnico","");
            FacesContext.getCurrentInstance().addMessage(null, msj);
            bValida=false; 
        }
        //Revisión de montos
        BigDecimal montoVerificado=BigDecimal.ZERO;
        for(ConceptoFacturableCaja c:this.conceptosParaFacturar){
            if(this.sTipoFactura.equals("Credito-Empresas")&&(c.getDescripcion().equals("PAGO DE COASEGURO, A REALIZARSE POR EL CLIENTE")|c.getDescripcion().equals("PAGO DE DEDUCIBLE, A REALIZARSE POR EL CLIENTE")))
                montoVerificado=montoVerificado.subtract(c.getImporteTotalSinIva());    
            else
                montoVerificado=montoVerificado.add(c.getImporteTotalSinIva());  
        }
        BigDecimal diferenciaSubtotales=oSubtotal.subtract(montoVerificado);
        if(diferenciaSubtotales.floatValue()<-.1 |diferenciaSubtotales.floatValue()>.1){
            
            msj= new FacesMessage(FacesMessage.SEVERITY_ERROR,"El subtotal no coincide con la suma de los importes de conceptos","");
            FacesContext.getCurrentInstance().addMessage(null, msj);
            bValida=false; 
        }else if(!this.oSubtotal.add(this.oImpuestos).equals(this.oTotal)){
            msj= new FacesMessage(FacesMessage.SEVERITY_ERROR,"Verifique los montos finales, las sumas no coinciden con el total","");
            FacesContext.getCurrentInstance().addMessage(null, msj);
            bValida=false; 
        }
        if(oEmisorSeleccionado==null){
            msj= new FacesMessage(FacesMessage.SEVERITY_ERROR,"El emisor no ha sido indicado","");
            FacesContext.getCurrentInstance().addMessage(null, msj);
            bValida=false;
        }
        if(this.sRegimen==null|this.sRegimen.equals("")){
            msj= new FacesMessage(FacesMessage.SEVERITY_ERROR,"El regimen fiscal no ha sido indicado","");
            FacesContext.getCurrentInstance().addMessage(null, msj);
            bValida=false;
        }
        if(oReceptorSeleccionado==null){
            msj= new FacesMessage(FacesMessage.SEVERITY_ERROR,"El receptor no ha sido indicado","");
            FacesContext.getCurrentInstance().addMessage(null, msj);
            bValida=false;
        }
        if(oComprobanteCreado.getSerie()==null |oComprobanteCreado.getSerie().equals("")){
            msj= new FacesMessage(FacesMessage.SEVERITY_ERROR,"La serie no ha sido indicada","");
            FacesContext.getCurrentInstance().addMessage(null, msj);
            bValida=false;
        }
        if(oComprobanteCreado.getFormaDePago()==null |oComprobanteCreado.getFormaDePago().equals("")){
            msj= new FacesMessage(FacesMessage.SEVERITY_ERROR,"La forma de pago no ha sido indicada","");
            FacesContext.getCurrentInstance().addMessage(null, msj);
            bValida=false;
        }
        if(oComprobanteCreado.getMetodoDePago()==null|oComprobanteCreado.getMetodoDePago().equals("")){
            msj= new FacesMessage(FacesMessage.SEVERITY_ERROR,"El método de pago no ha sido indicado","");
            FacesContext.getCurrentInstance().addMessage(null, msj);
            bValida=false;
        }
        if(oComprobanteCreado.getLugarExpedicion()==null|oComprobanteCreado.getLugarExpedicion().equals("")){
            msj= new FacesMessage(FacesMessage.SEVERITY_ERROR,"El lugar de expedición no ha sido indicado","");
            FacesContext.getCurrentInstance().addMessage(null, msj);
            bValida=false;
        }
        if(oComprobanteCreado.getTipoDeComprobante()==null|oComprobanteCreado.getTipoDeComprobante().equals("")){
            msj= new FacesMessage(FacesMessage.SEVERITY_ERROR,"El tipo de comprobante no ha sido indicado","");
            FacesContext.getCurrentInstance().addMessage(null, msj);
            bValida=false;
        } 
        if(this.conceptosParaFacturar.isEmpty()){
            msj= new FacesMessage(FacesMessage.SEVERITY_ERROR,"No se han indicado conceptos a facturar","");
            FacesContext.getCurrentInstance().addMessage(null, msj);
            bValida=false; 
        }
        this.bComprobanteValido=bValida;
        System.out.println("El comprobante es valido? "+bComprobanteValido);
        if (!bValida)
            this.oEmisorSeleccionado = new Comprobante.Emisor();
        return bValida;
    }
    public boolean comprobarSiEsPosibleBusquedaConceptos(){
        boolean bValida=true;  
        if(this.nTipoFact==1 && (this.oReceptorSeleccionado==null)){  
            msj= new FacesMessage(FacesMessage.SEVERITY_ERROR,"Debe elegir un receptor 'Compañia de crédito","");
            FacesContext.getCurrentInstance().addMessage(null, msj);
            bValida=false; 
        }
        return bValida;
    }
    public boolean comprobarSiEsPosibleBusquedaConceptosCuentasParticulares(){
        boolean bValida=true;        
        if(this.oCuentaPacienteSeleccionado.getFolioPaciente()==0){  
            msj= new FacesMessage(FacesMessage.SEVERITY_ERROR,"Debe seleccionar una cuenta de paciente","");
            FacesContext.getCurrentInstance().addMessage(null, msj);
            bValida=false; 
        }
        return bValida;
    }
    public void setTipoFactura(String tipo) {
        limpiarDatosTotalesConceptosCFDI();
        if(contador>1){
            if(!tipo.equals(sTipoFacturaAuxiliar)){
                this.conceptosParaFacturar=new ArrayList<ConceptoFacturableCaja>();
                this.conceptosSeleccionadosAcumulados=new ArrayList<ConceptoFacturableCaja>();
                this.consumosSeleccionadosAcumulados=new ArrayList<ConceptoFacturableCaja>();
                this.lista_detallesCFDIConUnOpeCajaConcepto=new ArrayList<DetallesCFDIConUnOpeCajaConcepto>();
                this.lista_detalleCFDIConVariosOpeCajaConcepto=new ArrayList<DetalleCFDIConVariosOpeCajaConcepto>();
                this.lista_detalleCFDIConUnOpeCajaConcepto=new ArrayList<DetalleCFDIConUnOpeCajaConcepto>();
                limpiarDatosTotalesConceptosCFDI();
                this.idDetalleCFDI=0;
                this.idDetallePaqueteAgregado=0;
            }
        }
        this.conceptosSeleccionados=new ArrayList<ConceptoFacturableCaja>();
        this.consumosSeleccionados=new ArrayList<ConceptoFacturableCaja>();
        this.contratosSeleccionados=new ArrayList<ConceptoFacturableCaja>();
        this.conceptosTodosConsultados=new ArrayList<ConceptoFacturableCaja>();
        this.conceptosPaquetesCuentas=new ArrayList<ConceptoFacturableCaja>();
        this.oCuentasPacientesPorNombre=new ArrayList<CuentaBusquedaPorNombrePaciente>();
        sTipoFacturaAuxiliar=tipo;    
        contador++;
        if(tipo.equals("Credito-Empresas")){
            this.nTipoFact=1;
        }else if(tipo.equals("Paquetes")){
            this.nTipoFact=2;
            this.nNumPaquete=0;
        }else if(tipo.equals("Cuentas"))
            this.nTipoFact=3;
        else if(tipo.equals("Particulares")){
            sTipoBusquedaParticulares="";
            this.nTipoFact=4;
        }else if(tipo.equals("Rentas"))
            this.nTipoFact=5;
        else
            this.nTipoFact=6;   //Público en general
        if(nTipoFact<5){
            this.oCuentaPacienteSeleccionado=new CuentaBusquedaPorNombrePaciente();
            this.sNombrePaciente="";
            this.sApPaternoPaciente="";
            this.sApMaternoPaciente="";
        }
        this.sTipoFactura = tipo;
    }
    //Devuelve una lista de SerieFiscal pero su búsqueda esta basada en el tipo de documento proporcionado por el usuario
    public List<SerieFiscal> getSeriesPorDocumentoCFDI() throws Exception {
        oSeriesPorDocumento = new SerieFiscal().getSeriesFiscalesPorDocumento("CFDI");
        return oSeriesPorDocumento;
    }
    //Devuelve una lista de SerieFiscal pero su búsqueda esta basada en el tipo de documento proporcionado por el usuario
    public List<SerieFiscal> getSeriesPorDocumentoNotaCredito() throws Exception {
        oSeriesPorDocumento = new SerieFiscal().getSeriesFiscalesPorDocumento("Nota de crédito");
        return oSeriesPorDocumento;
    }
    /*********************************************************************************************************************
    /              CONSTRUCCIÓN Y LIMPIADO DE LA PÁGINA AL RECARGAR O CANCELAR: INICIA                                           /
    *********************************************************************************************************************/ 
    public ComprobanteJB() throws Exception{
        contador=0;
        //crea objetos para los conceptos a facturar
        conceptosTodosConsultados=new ArrayList<ConceptoFacturableCaja>();
        conceptosSeleccionados=new ArrayList<ConceptoFacturableCaja>();
        //llena listas de elementos
        oSeries = new SerieFiscal().getSeriesFiscales();
        emisores = new Comprobante.Emisor().getTodosEmisores();
        receptores = new Comprobante.Receptor().getTodosReceptores();
        oComprobanteCreado.setFormaDePago("PAGO EN UNA SOLA EXHIBICION");
        oComprobanteCreado.setTipoDeComprobante("ingreso");
    }
    public void limpiar() throws Exception{
        this.bComprobanteValido=false;
        this.idDetalleCFDI=0;
        this.idDetallePaqueteAgregado=0;
        sNombrePaciente="";    
        sApPaternoPaciente="";  
        sApMaternoPaciente="";
        sRegimen="";
        nTipoFact=0;
        sTipoBusquedaParticulares="";
        sTipoFactura="";
        sTipoDocumento="CFDI";
        oConcepto=new ConceptoFacturableCaja();
        conceptosTodosConsultados=new ArrayList<ConceptoFacturableCaja>();
        conceptosSeleccionados=new ArrayList<ConceptoFacturableCaja>();
        conceptosParaFacturar=new ArrayList<ConceptoFacturableCaja>();
        conceptosSeleccionadosAcumulados=new ArrayList<ConceptoFacturableCaja>();
        contratosSeleccionados=new ArrayList<ConceptoFacturableCaja>();
        consumosSeleccionados=new ArrayList<ConceptoFacturableCaja>();
        consumosSeleccionadosAcumulados=new ArrayList<ConceptoFacturableCaja>();
        limpiarDatosTotalesConceptosCFDI();
        oComprobanteCreado=new Comprobante();
        oComprobanteCreado.setFormaDePago("PAGO EN UNA SOLA EXHIBICION");
        oComprobanteCreado.setTipoDeComprobante("ingreso");
        oEmisorSeleccionado=new Comprobante.Emisor();
        oReceptorSeleccionado=new Comprobante.Receptor();
        emisores = new Comprobante.Emisor().getTodosEmisores();
        receptores = new Comprobante.Receptor().getTodosReceptores();
        this.oCuentaPacienteSeleccionado=new CuentaBusquedaPorNombrePaciente();
        this.lista_detallesCFDIConUnOpeCajaConcepto=new ArrayList<DetallesCFDIConUnOpeCajaConcepto>();
        this.lista_detalleCFDIConVariosOpeCajaConcepto=new ArrayList<DetalleCFDIConVariosOpeCajaConcepto>();
        this.lista_detalleCFDIConUnOpeCajaConcepto=new ArrayList<DetalleCFDIConUnOpeCajaConcepto>();
        oFactura=new FacturaCFI();
    }
    
    public void limpiarFactura() throws Exception{
       oFacturaResultante=new FacturaCFI();
    }
    public void cancelar() throws Exception{
        this.limpiar();
        this.limpiarFactura();
    }
    public void limpiarDatosDetalleCFDI(){
        this.cantidadDetalleCFDI=1;
        this.descripcionDetalleCFDI="";
        this.montoDetalleCFDI=BigDecimal.ZERO;
        this.unidadDetalleCFDI="NA";
    }
    public void limpiarDatosTotalesConceptosCFDI(){
        oImpuestos=BigDecimal.ZERO;
        oSubtotal=BigDecimal.ZERO;
        oTotal=BigDecimal.ZERO;
        oSubtotalTasaCeroIVA=BigDecimal.ZERO;
        oSubtotalOtraTasaIVA=BigDecimal.ZERO;
        nTasaImp=0;
    }
    /**********************************************************************************************************************
    /              CONSTRUCCIÓN Y LIMPIADO DE LA PÁGINA AL RECARGAR O CANCELAR: TERMINA                                    /
    **********************************************************************************************************************/
    
    
    /*********************************************************************************************************************
    /                                       GET y SET DE LOS ATRIBUTOS                                                   /
    *********************************************************************************************************************/
    public void setCuentaPacienteSeleccionado(CuentaBusquedaPorNombrePaciente oDatosCuentaPacienteSeleccionado) {
        this.oCuentaPacienteSeleccionado = oDatosCuentaPacienteSeleccionado;
        if(oCuentaPacienteSeleccionado!=null){
            if(oCuentaPacienteSeleccionado.getNombre()!=null)
                this.sNombrePaciente=oCuentaPacienteSeleccionado.getNombre();
            if(oCuentaPacienteSeleccionado.getApPaterno()!=null)
                this.sApPaternoPaciente=oCuentaPacienteSeleccionado.getApPaterno();
            if(oCuentaPacienteSeleccionado.getApMaterno()!=null)
                this.sApMaternoPaciente=oCuentaPacienteSeleccionado.getApMaterno();
        }
    }
    public CuentaBusquedaPorNombrePaciente getCuentaPacienteSeleccionado() {
        return oCuentaPacienteSeleccionado;
    }
    public String getRegimen() {
        return sRegimen;
    }
    public void setRegimen(String sRegimen) {
        this.sRegimen = sRegimen;
    }
    //Obtiene y cambia los impuestos correspondiente a facturar
    public BigDecimal getImpuestos() {
        return oImpuestos;
    }
    public void setImpuestos(BigDecimal nImpuestos) {
        this.oImpuestos = nImpuestos;
    }
    //Obtiene y cambia el subtotal correspondiente a facturar
    public BigDecimal getSubtotal() {
        return oSubtotal;
    }
    public void setSubtotal(BigDecimal nSubtotal) {
        this.oSubtotal = nSubtotal;
    }
    //Obtiene y cambia el total correspondiente a facturar
    public BigDecimal getTotal() {
        return oTotal;
    }
    public void setTotal(BigDecimal nTotal) {
        this.oTotal = nTotal;
    }
    //Obtiene y cambia el tipo de factura, para solicitar las consultas de los conceptos dependiendo de si son para empresas, venta del día, clientes de contado, etc.
    public String getTipoFactura() {
        return sTipoFactura;
    }
    //obtiene y cambia los conceptos seleccionados por el usuario
    public List<ConceptoFacturableCaja> getConceptosSeleccionados() {
        return conceptosSeleccionados;
    }
    public void setConceptosSeleccionados(List<ConceptoFacturableCaja> conceptosSeleccionados) {
        this.conceptosSeleccionados = conceptosSeleccionados;
    }
    //obtiene y cambia todos los conceptos de los cuales elegirá algunos el usuario
    public List<ConceptoFacturableCaja> getConceptosTodosConsultados() {
        return conceptosTodosConsultados;
    }
    public final void setConceptosTodosConsultados(List<ConceptoFacturableCaja> conceptos) {
        this.conceptosTodosConsultados = conceptos;
    }
    public Comprobante getComprobanteCreado() {
        return oComprobanteCreado;
    }
    public void setComprobanteCreado(Comprobante comp) {
        this.oComprobanteCreado = comp;
    }
    public List<Comprobante.Receptor> getReceptores() throws Exception {
        return receptores;
    }
    public void setReceptores(List<Comprobante.Receptor> receptores) {
        ComprobanteJB.receptores = receptores;
    }
    public Comprobante.Receptor getReceptorSeleccionado() {
        return oReceptorSeleccionado;
    }
    public void setReceptorSeleccionado(Comprobante.Receptor emisor) {
        this.oReceptorSeleccionado = emisor;
    }
    public Comprobante.Emisor getEmisorSeleccionado() {
        return oEmisorSeleccionado;
    }
    public void setEmisorSeleccionado(Comprobante.Emisor emisor) {
        this.oEmisorSeleccionado = emisor;
    }
    public void setEmisores(List<Comprobante.Emisor> emisores) {
        ComprobanteJB.emisores = emisores;
    }
    public List<Comprobante.Emisor> getEmisores() {
        return emisores;
    }
    public TipoDeComprobante[] getTiposDeComprobantes(){
        return TipoDeComprobante.values();
    }
    public TipoDocumento[] getTiposDeDocumentos(){
        return TipoDocumento.values();
    }   
    public void actualizarReceptor() throws Exception{
        oComprobanteCreado.actualizarDatosReceptor();
    }
    public EmisorRegimenFiscal[] getRegimenesFiscales(){
        return EmisorRegimenFiscal.values();
    }
    public String getTipoDocumento() {
        return sTipoDocumento;
    }
    public void setTipoDocumento(String tipoDocumento) {
        this.sTipoDocumento = tipoDocumento;
    }
    public void setSeriesPorDocumento(List<SerieFiscal> oSeriesPorDocumento) {
        this.oSeriesPorDocumento = oSeriesPorDocumento;
    }
    public List<ConceptoFacturableCaja> getConceptosParaFacturar() {
        return conceptosParaFacturar;
    }
    public void setConceptosParaFacturar(List<ConceptoFacturableCaja> conceptosParaFacturar) {
        this.conceptosParaFacturar = conceptosParaFacturar;
    }
    public int getNumPaquete() {
        return nNumPaquete;
    }
    public void setNumPaquete(int nNumPaquete) {
        this.nNumPaquete = nNumPaquete;
    }
    public String getNombrePaciente() {
        return sNombrePaciente;
    }
    public void setNombrePaciente(String sNombrePaciente) {
        this.sNombrePaciente = sNombrePaciente;
    }
    public int getTipoFact() {
        return nTipoFact;
    }
    public void setTipoFact(int nTipoFact) {
        this.nTipoFact = nTipoFact;
    }
    public List<SerieFiscal> getSeries() {
        return oSeries;
    }
    public void setSeries(List<SerieFiscal> oSeries) {
        this.oSeries = oSeries;
    }
    public String getTipoFacturaAuxiliar() {
        return sTipoFacturaAuxiliar;
    }
    public void setTipoFacturaAuxiliar(String sTipoFacturaAuxiliar) {
        this.sTipoFacturaAuxiliar = sTipoFacturaAuxiliar;
    }
    public List<CuentaBusquedaPorNombrePaciente> getCuentasPacientesPorNombre() {
        return oCuentasPacientesPorNombre;
    }
    public void setCuentasPacientesPorNombre(List<CuentaBusquedaPorNombrePaciente> oDatosCuentasPacientes) {
        this.oCuentasPacientesPorNombre = oDatosCuentasPacientes;
    }
    public int getFolioPaciente() {
        return nFolioPaciente;
    }
    public void setFolioPaciente(int nFolioPaciente) {
        this.nFolioPaciente = nFolioPaciente;
    }
    public String getApMaternoPaciente() {
        return sApMaternoPaciente;
    }
    public void setApMaternoPaciente(String sApMaternoPaciente) {
        this.sApMaternoPaciente = sApMaternoPaciente;
    }
    public String getApPaternoPaciente() {
        return sApPaternoPaciente;
    }
    public void setApPaternoPaciente(String sApPaternoPaciente) {
        this.sApPaternoPaciente = sApPaternoPaciente;
    }
    public int getAccionConceptos() {
        return nAccionConceptos;
    }
    public void setAccionConceptos(int nAcccionConceptos) {
        this.nAccionConceptos = nAcccionConceptos;
    }
     public List<DetalleCFDIConUnOpeCajaConcepto> getLista_unaLineaUnConceptoDeServicio() {
        return lista_detalleCFDIConUnOpeCajaConcepto;
    }
    public void setLista_unaLineaUnConceptoDeServicio(List<DetalleCFDIConUnOpeCajaConcepto> lista_unaLineaUnConceptoDeServicio) {
        this.lista_detalleCFDIConUnOpeCajaConcepto = lista_unaLineaUnConceptoDeServicio;
    }
    public List<DetalleCFDIConVariosOpeCajaConcepto> getLista_unaLineasVariosConceptoDeServicio() {
        return lista_detalleCFDIConVariosOpeCajaConcepto;
    }
    public void setLista_unaLineasVariosConceptoDeServicio(List<DetalleCFDIConVariosOpeCajaConcepto> lista_unaLineasVariosConceptoDeServicio) {
        this.lista_detalleCFDIConVariosOpeCajaConcepto = lista_unaLineasVariosConceptoDeServicio;
    }
    public List<DetallesCFDIConUnOpeCajaConcepto> getLista_variasLineasUnConceptoDeServicio() {
        return lista_detallesCFDIConUnOpeCajaConcepto;
    }
    public void setLista_variasLineasUnConceptoDeServicio(List<DetallesCFDIConUnOpeCajaConcepto> lista_variasLineasUnConceptoDeServicio) {
        this.lista_detallesCFDIConUnOpeCajaConcepto = lista_variasLineasUnConceptoDeServicio;
    }
    public DetalleCFDIConUnOpeCajaConcepto getUnaLineaUnConceptoDeServicio() {
        return detalleCFDIConUnOpeCajaConcepto;
    }
    public void setUnaLineaUnConceptoDeServicio(DetalleCFDIConUnOpeCajaConcepto unaLineaUnConceptoDeServicio) {
        this.detalleCFDIConUnOpeCajaConcepto = unaLineaUnConceptoDeServicio;
    }
    public DetalleCFDIConVariosOpeCajaConcepto getUnaLinesVariosConceptoDeServicio() {
        return detalleCFDIConVariosOpeCajaConcepto;
    }
    public void setUnaLinesVariosConceptoDeServicio(DetalleCFDIConVariosOpeCajaConcepto unaLinesVariosConceptoDeServicio) {
        this.detalleCFDIConVariosOpeCajaConcepto = unaLinesVariosConceptoDeServicio;
    }
    public DetallesCFDIConUnOpeCajaConcepto getVariasLineasUnConceptoDeServicio() {
        return detallesCFDIConUnOpeCajaConcepto;
    }
    public void setVariasLineasUnConceptoDeServicio(DetallesCFDIConUnOpeCajaConcepto variasLineasUnConceptoDeServicio) {
        this.detallesCFDIConUnOpeCajaConcepto = variasLineasUnConceptoDeServicio;
    }
     public int getCantidadDetalleCFDI() {
        return cantidadDetalleCFDI;
    }
    public void setCantidadDetalleCFDI(int cantidadDetalleCFDI) {
        this.cantidadDetalleCFDI = cantidadDetalleCFDI;
    }
    public String getDescripcionDetalleCFDI() {
        return descripcionDetalleCFDI;
    }
    public void setDescripcionDetalleCFDI(String descripcionDetalleCFDI) {
        this.descripcionDetalleCFDI = descripcionDetalleCFDI.toUpperCase();
    }
    public BigDecimal getMontoDetalleCFDI() {
        return montoDetalleCFDI;
    }
    public void setMontoDetalleCFDI(BigDecimal montoDetalleCFDI) {
        this.montoDetalleCFDI = montoDetalleCFDI;
    }   
    public String getUnidadDetalleCFDI() {
        return unidadDetalleCFDI;
    }
    public void setUnidadDetalleCFDI(String unidadDetalleCFDI) {
        this.unidadDetalleCFDI = unidadDetalleCFDI;
    }
    public List<ConceptoFacturableCaja> getConceptosSeleccionadosAcumulados() {
        return conceptosSeleccionadosAcumulados;
    }
    public void setConceptosSeleccionadosAcumulados(List<ConceptoFacturableCaja> conceptosSeleccionadosAcumulados) {
        this.conceptosSeleccionadosAcumulados = conceptosSeleccionadosAcumulados;
    }
    public ConceptoFacturableCaja getConceptoSeleccionado() {
        return oConceptoSeleccionado;
    }
    public void setConceptoSeleccionado(ConceptoFacturableCaja oConceptoSeleccionado) {
        this.oConceptoSeleccionado = oConceptoSeleccionado;
    }
     public List<ConceptoFacturableCaja> getConceptosPaquetesCuentas() {
        return conceptosPaquetesCuentas;
    }
    public void setConceptosPaquetesCuentas(List<ConceptoFacturableCaja> conceptosPaquetesCuentas) {
        this.conceptosPaquetesCuentas = conceptosPaquetesCuentas;
    }
    public List<ConceptoFacturableCaja> getConsumosSeleccionadosAcumulados() {
        return consumosSeleccionadosAcumulados;
    }
    public void setConsumosSeleccionadosAcumulados(List<ConceptoFacturableCaja> consumosSeleccionadosAcumulados) {
        this.consumosSeleccionadosAcumulados = consumosSeleccionadosAcumulados;
    }
    public List<ConceptoFacturableCaja> getConsumosSeleccionados() {
        return consumosSeleccionados;
    }
    public void setConsumosSeleccionados(List<ConceptoFacturableCaja> consumosSeleccionados) {
        this.consumosSeleccionados = consumosSeleccionados;
    }
    public List<ConceptoFacturableCaja> getContratosSeleccionados() {
        return contratosSeleccionados;
    }
    public void setContratosSeleccionados(List<ConceptoFacturableCaja> contratosSeleccionados) {
        this.contratosSeleccionados = contratosSeleccionados;
    }
    public int getnClaveEpisodioMed() {
        return nClaveEpisodioMed;
    }

    public void setnClaveEpisodioMed(int nClaveEpisodioMed) {
        this.nClaveEpisodioMed = nClaveEpisodioMed;
    }
    public boolean isComprobanteValido() {
        return bComprobanteValido;
    }

    public void setComprobanteValido(boolean sComprobanteValido) {
        this.bComprobanteValido = sComprobanteValido;
    }

    public String getsRegimen() {
        return sRegimen;
    }

    public void setsRegimen(String sRegimen) {
        this.sRegimen = sRegimen;
    }
    public FacturaCFI getFacturaResultante() {
        return oFacturaResultante;
    }

    public void setFacturaResultante(FacturaCFI oFacturaResultante) {
        this.oFacturaResultante = oFacturaResultante;
    }
    
    public FacturaCFI getFactura() {
        return oFactura;
    }
    public void setFactura(FacturaCFI oFactura) {
        this.oFactura = oFactura;
    }
    public String getTipoBusquedaParticulares() {
        return sTipoBusquedaParticulares;
    }
    public void setTipoBusquedaParticulares(String sTipoBusquedaParticulares) {
        if(this.sTipoBusquedaParticulares.equals(sTipoBusquedaParticulares)){
                    this.conceptosParaFacturar=new ArrayList<ConceptoFacturableCaja>();
                    this.conceptosSeleccionadosAcumulados=new ArrayList<ConceptoFacturableCaja>();
                    this.lista_detallesCFDIConUnOpeCajaConcepto=new ArrayList<DetallesCFDIConUnOpeCajaConcepto>();
                    this.lista_detalleCFDIConVariosOpeCajaConcepto=new ArrayList<DetalleCFDIConVariosOpeCajaConcepto>();
                    this.lista_detalleCFDIConUnOpeCajaConcepto=new ArrayList<DetalleCFDIConUnOpeCajaConcepto>();
                    limpiarDatosTotalesConceptosCFDI();
                    this.idDetalleCFDI=0;
                    this.idDetallePaqueteAgregado=0; 
            }
        this.sTipoBusquedaParticulares = sTipoBusquedaParticulares;
    }
    public void calcularGravados(){
        BigDecimal tasaCero=BigDecimal.ZERO;
        BigDecimal tasaDiferente=BigDecimal.ZERO;
        System.out.println("Tamaño "+conceptosParaFacturar.size());
        for(ConceptoFacturableCaja c:conceptosParaFacturar){
            if(c.getPctIVa()!=0)
                tasaDiferente=tasaDiferente.add(c.getImporteTotalSinIva());
            else{
                tasaCero=tasaCero.add(c.getImporteTotalSinIva());
                this.nTasaImp=c.getPctIVa();
            }
        }
        setObservaciones("IMPORTE TASA 0%: "+tasaCero.floatValue()+", IMPORTE TASA "+this.nTasaImp+"%: "+tasaDiferente.floatValue());
        System.out.println("Observaciones: "+getObservaciones());
    }
    public String getObservaciones() {
        return sObservaciones;
    }
    public void setObservaciones(String sObservaciones) {
        this.sObservaciones = sObservaciones;
    }
    public Timbrado getTimbrado(){
        return timbrado;
    }
    public void setTimbrado(Timbrado timbrado){
        this.timbrado=timbrado;
    }
    public void setCantidadLetra(String cantidad){
        this.sCantidadLetra=cantidad;
    }
    public String getCantidadLetra(){
        return sCantidadLetra;
    }
    public Comprobante getComprobanteResultante() {
        return oComprobanteResultante;
    }
    public void setComprobanteResultante(Comprobante oComprobanteResultado) {
        this.oComprobanteResultante = oComprobanteResultado;
    }
    public List<ConceptoFacturableCaja> getConceptosFinalesEnFactura() {
        return conceptosFinalesEnFactura;
    }
    public void setConceptosFinalesEnFactura(List<ConceptoFacturableCaja> conceptosFinalesEnFactura) {
        this.conceptosFinalesEnFactura = conceptosFinalesEnFactura;
    }
    public BigDecimal getSubtotalNoGravadoFinal() {
        return oSubtotalNoGravadoFinal;
    }
    public void setSubtotalNoGravadoFinal(BigDecimal oSubtotalNoGravadoFinal) {
        this.oSubtotalNoGravadoFinal = oSubtotalNoGravadoFinal;
    }
    public BigDecimal getSubtotalGravadoFinal() {
        return oSubtotalGravadoFinal;
    }
    public void setSubtotalGravadoFinal(BigDecimal oSubtotalGravadoFinal) {
        this.oSubtotalGravadoFinal = oSubtotalGravadoFinal;
    }
    public BigDecimal getImpuestosFinal() {
        return oImpuestosFinal;
    }
    public void setImpuestosFinal(BigDecimal oImpuestosFinal) {
        this.oImpuestosFinal = oImpuestosFinal;
    }
    public BigDecimal getTotalFinal() {
        return oTotalFinal;
    }
    public void setTotalFinal(BigDecimal oTotalFinal) {
        this.oTotalFinal = oTotalFinal;
    }
    public BigDecimal getSubtotalFinal() {
        return oSubtotalFinal;
    }
    public void setSubtotalFinal(BigDecimal oTotalFinal) {
        this.oSubtotalFinal = oTotalFinal;
    }
    public void setDirCodigoQR(String codigo){
        sDirCodigoRQ=codigo;
    }
    public String getDirCodigoQR(){
        return sDirCodigoRQ;
    }
    
}