package org.apli.tickets.models;

import java.util.List;
import org.apli.tickets.Paciente;
import org.apli.tickets.Servicio;
import org.apli.tickets.BodyTicket;

/**
 *
 * @author mdamianhdz
 */
public class OtorgarServicioDePaquete extends BodyTicket {

    private int title;
    private String folioope;
    private String foliocontrato;
    private Paciente paciente;
    private String paquete;
    private String usuario;
    private List<Servicio> servicios;
    private String subtotal;
    private String iva;
    private String total;

    public OtorgarServicioDePaquete() {
        super.setFecha(fechaActual());
        super.setHora(horaActual());
        title = 7;
    }

    public OtorgarServicioDePaquete(int title, String folioope, String foliocontrato, Paciente paciente, String paquete, String usuario, List<Servicio> servicios, String subtotal, String iva, String total) {
        this.title = title;
        this.folioope = folioope;
        this.foliocontrato = foliocontrato;
        this.paciente = paciente;
        this.paquete = paquete;
        this.usuario = usuario;
        this.servicios = servicios;
        this.subtotal = subtotal;
        this.iva = iva;
        this.total = total;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public String getFolioope() {
        return folioope;
    }

    public void setFolioope(String folioope) {
        this.folioope = folioope;
    }

    public String getFoliocontrato() {
        return foliocontrato;
    }

    public void setFoliocontrato(String foliocontrato) {
        this.foliocontrato = foliocontrato;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public String getPaquete() {
        return paquete;
    }

    public void setPaquete(String paquete) {
        this.paquete = paquete;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public List<Servicio> getServicios() {
        return servicios;
    }

    public void setServicios(List<Servicio> servicios) {
        this.servicios = servicios;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public String getIva() {
        return iva;
    }

    public void setIva(String iva) {
        this.iva = iva;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "OtorgarServicioDePaquete{" + "title=" + title + ", folioope=" + folioope + ", foliocontrato=" + foliocontrato + ", paciente=" + paciente + ", paquete=" + paquete + ", usuario=" + usuario + ", servicios=" + servicios + ", subtotal=" + subtotal + ", iva=" + iva + ", total=" + total + '}';
    }

}
