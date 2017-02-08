/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apli.modelbeans.facturacion.cfdi;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;
/**
 *
 * @author Isa
 */
public class DetalleCFDIConUnOpeCajaConcepto extends DetalleCFDIConsumosContratos implements Serializable{
    private DetalleCFDI oDetalleCFDI;
    DetalleCFDIOpeCajaConcepto oDetalleServicio=new DetalleCFDIOpeCajaConcepto();
    
    public DetalleCFDIOpeCajaConcepto getDetalleCFDIOpeCajaConcepto() {
        return oDetalleServicio;
    }

    public void setDetalleCFDIOpeCajaConcepto(DetalleCFDIOpeCajaConcepto conceptosFacturables) {
        this.oDetalleServicio = conceptosFacturables;
    }
    public DetalleCFDI getDetalleCFDI() {
        return oDetalleCFDI;
    }

    public void setDetalleCFDI(DetalleCFDI oDetalleCFDI) {
        this.oDetalleCFDI = oDetalleCFDI;
    }
}
