package org.apli.modelbeans.facturacion.cfdi.v32.schema;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import org.apli.modelbeans.facturacion.cfdi.v32.schema.Comprobante.Conceptos;
import org.apli.modelbeans.facturacion.cfdi.v32.schema.Comprobante.Conceptos.Concepto;
import org.apli.modelbeans.facturacion.cfdi.v32.schema.Comprobante.Impuestos;
import org.apli.modelbeans.facturacion.cfdi.v32.schema.Comprobante.Impuestos.Retenciones;
import org.apli.modelbeans.facturacion.cfdi.v32.schema.Comprobante.Impuestos.Retenciones.Retencion;
import org.apli.modelbeans.facturacion.cfdi.v32.schema.Comprobante.Impuestos.Traslados;
import org.apli.modelbeans.facturacion.cfdi.v32.schema.Comprobante.Impuestos.Traslados.Traslado;

public class RegistroDeComprobante implements Serializable{
	Comprobante comp;
	ObjectFactory of;
	Conceptos cps ;
	List<Concepto> list ; 
	Impuestos imps ;
	Traslados trs;
	List<Traslado> listTraslados; 	
        Retenciones rts;
	List<Retencion> listRetenciones;
        
	public RegistroDeComprobante(){
            of = new ObjectFactory();
	    comp = of.createComprobante();
	    cps = of.createComprobanteConceptos();
	    list = cps.getConcepto(); 
	    imps = of.createComprobanteImpuestos();
	    trs = of.createComprobanteImpuestosTraslados();
	    listTraslados = trs.getTraslado();  
            rts = of.createComprobanteImpuestosRetenciones();
	    listRetenciones = rts.getRetencion();    
	}
        
	public Comprobante getComprobante(){
            comp.setConceptos(cps);
            comp.setImpuestos(imps);
            return comp;
	}
	 
	public void configurarComponente(String version, String serie,String moneda, String metodoPago, 
                                         String lugarExpedicion, String tipo,String formaPago,String numCtaPago) throws Exception {
            if(!(serie==null|serie.isEmpty()|serie.equals("")))
                comp.setSerie(serie); 				
            if(!(moneda==null|moneda.isEmpty()|moneda.equals("")))
                comp.setMoneda(moneda);
            if(!(numCtaPago==null|numCtaPago.isEmpty()|numCtaPago.equals("")))
                comp.setNumCtaPago(numCtaPago);
            comp.setVersion(version); 	
            comp.setFormaDePago(formaPago);
            comp.setMetodoDePago(metodoPago);
            comp.setLugarExpedicion(lugarExpedicion);
            comp.setTipoDeComprobante(tipo);
	}
	
	public void registrarTotales(BigDecimal subtotal,BigDecimal total ) throws Exception {
            Date date = new GregorianCalendar().getTime();
            comp.setFecha(date);            
            comp.setSubTotal(subtotal);	
            comp.setTotal(total);						
	}
	
	public void registrarConcepto(String unidad,BigDecimal importe,BigDecimal cantidad,String descripcion,BigDecimal valorUnit) {
            Concepto concepto;
            concepto = of.createComprobanteConceptosConcepto();
            concepto.setUnidad(unidad);
            concepto.setImporte(importe);
            concepto.setCantidad(cantidad);
            concepto.setDescripcion(descripcion);
            concepto.setValorUnitario(valorUnit);
            list.add(concepto);
	}
	public void registrarImpuestosRetenidos(BigDecimal importe,String impuesto) {
            if(listRetenciones.isEmpty()){
                imps.setTotalImpuestosRetenidos(BigDecimal.ZERO);
            }
            Retencion retencion;
            retencion = of.createComprobanteImpuestosRetencionesRetencion();
            retencion.setImporte(importe);
            retencion.setImpuesto(impuesto);
            listRetenciones.add(retencion);
            imps.setRetenciones(rts);
            imps.setTotalImpuestosRetenidos(imps.getTotalImpuestosRetenidos().add(importe));
	}
	public void registrarImpuestosTrasladadosIVA(BigDecimal importe,String impuesto,BigDecimal tasa) {
            if(listTraslados.isEmpty()){
                imps.setTotalImpuestosTrasladados(BigDecimal.ZERO);
            }
            Traslado traslado;
            traslado = of.createComprobanteImpuestosTrasladosTraslado();
            traslado.setImporte(importe);
            traslado.setImpuesto(impuesto);
            traslado.setTasa(tasa);
            listTraslados.add(traslado);
            imps.setTraslados(trs);
            imps.setTotalImpuestosTrasladados((imps.getTotalImpuestosTrasladados()).add(importe));
	}
}