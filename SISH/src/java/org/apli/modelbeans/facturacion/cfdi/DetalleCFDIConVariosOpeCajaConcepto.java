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
public class DetalleCFDIConVariosOpeCajaConcepto  extends DetalleCFDIConsumosContratos implements Serializable{
    private DetalleCFDI oDetalleCFDI;
    List<DetalleCFDIOpeCajaConcepto> oConceptosFacturables=new ArrayList<DetalleCFDIOpeCajaConcepto>();
    
    public List<DetalleCFDIOpeCajaConcepto> getDetallesCFDIOpeCajaConcepto() {
        return oConceptosFacturables;
    }
    public void agregarDetalleCFDIOpeCajaConcepto(DetalleCFDIOpeCajaConcepto detalle){
        oConceptosFacturables.add(detalle);
    }
    public void setDetallesCFDIOpeCajaConcepto(List<DetalleCFDIOpeCajaConcepto> conceptosFacturables) {
        this.oConceptosFacturables = conceptosFacturables;
    }
    public DetalleCFDI getDetalleCFDI() {
        return oDetalleCFDI;
    }

    public void setDetalleCFDI(DetalleCFDI oDetalleCFDI) {
        this.oDetalleCFDI = oDetalleCFDI;
    }
}
