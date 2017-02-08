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
 * @author Isabel Espinoza
 */
public class DetalleCFDIConsumosContratos implements Serializable{
    private List<DetalleCFDIServicio> oServiciosConsumidos=new ArrayList<DetalleCFDIServicio>();
    private List<DetalleCFDIPaquete> oServiciosContratados=new ArrayList<DetalleCFDIPaquete>();
    
    public List<DetalleCFDIServicio> getServiciosConsumidos() {
        return oServiciosConsumidos;
    }

    public void setServiciosConsumidos(List<DetalleCFDIServicio> oServiciosConsumidos) {
        this.oServiciosConsumidos = oServiciosConsumidos;
    }
    public void agregarServicioConsumido(DetalleCFDIServicio detalle){
        oServiciosConsumidos.add(detalle);
    }
    public void agregarServicioContratado(DetalleCFDIPaquete detalle){
        oServiciosContratados.add(detalle);
    }
    public List<DetalleCFDIPaquete> getServiciosContratados() {
        return oServiciosContratados;
    }

    public void setServiciosContratados(List<DetalleCFDIPaquete> oServiciosContratados) {
        this.oServiciosContratados = oServiciosContratados;
    }
}
