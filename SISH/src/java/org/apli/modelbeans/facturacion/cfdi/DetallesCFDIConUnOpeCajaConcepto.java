/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apli.modelbeans.facturacion.cfdi;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;
import java.math.BigDecimal;
/**
 *
 * @author Isa
 */
public class DetallesCFDIConUnOpeCajaConcepto  extends DetalleCFDIConsumosContratos implements Serializable{
    private List <DetalleCFDI> oDetallesCFDI=new ArrayList<DetalleCFDI>();
    DetalleCFDIOpeCajaConcepto oDetalleCFDIServicio=new DetalleCFDIOpeCajaConcepto();
    
    public void eliminarDetalleCFDI(int id){
        for(DetalleCFDI d:oDetallesCFDI){
            if(d.getId()==id){
                oDetallesCFDI.remove(d); 
                break;
            }
        }
    }
    public void agregarDetalleCFDI(int cantidad,String descripcion, BigDecimal monto,String unidad, int id){
        DetalleCFDI d=new DetalleCFDI();
        d.setId(id);
        d.setDescrip(descripcion);
        d.setCant(cantidad);
        d.setMonto(monto);
        
        d.setUnidad(unidad);
        oDetallesCFDI.add(d);
    }
    public DetalleCFDIOpeCajaConcepto getDetalleCFDIOpeCajaConcepto() {
        return oDetalleCFDIServicio;
    }

    public void setDetalleCFDIOpeCajaConcepto(DetalleCFDIOpeCajaConcepto conceptosFacturables) {
        this.oDetalleCFDIServicio = conceptosFacturables;
    }
    public List<DetalleCFDI> getDetallesCFDI() {
        return oDetallesCFDI;
    }

    public void setDetallesCFDI(List<DetalleCFDI> oDetalleCFDI) {
        this.oDetallesCFDI = oDetalleCFDI;
    }
}
