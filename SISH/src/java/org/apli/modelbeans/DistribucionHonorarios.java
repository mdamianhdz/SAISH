package org.apli.modelbeans;

import java.io.Serializable;
import java.util.List;

public class DistribucionHonorarios implements Serializable{
    
    private PagoHonorarios oPagoHonorarios;
    private List<DetalleHonorarios> detalles;
    private float montoTotalAPagar;
    
    public DistribucionHonorarios(){
        
    }

    public PagoHonorarios getPagoHonorarios() {
        return oPagoHonorarios;
    }
    public void setPagoHonorarios(PagoHonorarios oPagoHonorarios) {
        this.oPagoHonorarios = oPagoHonorarios;
    }

    public List<DetalleHonorarios> getDetalles() {
        return detalles;
    }
    public void setDetalles(List<DetalleHonorarios> detalles) {
        this.detalles = detalles;
    }

    public float getMontoTotalAPagar() {
        return montoTotalAPagar;
    }
    public void setMontoTotalAPagar(float montoTotalAPagar) {
        this.montoTotalAPagar = montoTotalAPagar;
    }
    
    
    
}