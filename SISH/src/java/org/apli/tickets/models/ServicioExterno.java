package org.apli.tickets.models;

import java.util.Date;
import java.util.List;
import org.apli.tickets.Paciente;
import org.apli.tickets.Servicio;
import org.apli.tickets.BodyTicket;

/**
 *
 * @author mdamianhdz
 */
public class ServicioExterno extends BodyTicket {

    private int title;
    private String folioope;
    private Paciente paciente;
    private String empresa;
    private String doctor;
    private String fmapago;
    private String usuario;
    private List<Servicio> servicios;
    private String subtotal;
    private String iva;
    private String desc;
    private String total;
    private String mensaje;

    public ServicioExterno() {
        title = 1;
    }

    public ServicioExterno(String folioope, Paciente paciente, String empresa, String doctor, String fmapago, String usuario, List<Servicio> servicios, String subtotal, String iva, String desc, String total, String mensaje) {
        this.folioope = folioope;
        this.paciente = paciente;
        this.empresa = empresa;
        this.doctor = doctor;
        this.fmapago = fmapago;
        this.usuario = usuario;
        this.servicios = servicios;
        this.subtotal = subtotal;
        this.iva = iva;
        this.desc = desc;
        this.total = total;
        this.mensaje = mensaje;
    }

    public void calcularCifras() {
        this.servicios = super.desglosarImportes(servicios);
        this.subtotal = super.obtieneSubtotal(servicios);
        this.iva = super.obtieneIva(servicios);
        this.desc = super.obtieneDescuentos(servicios);
        this.total = String.valueOf(super.formatDecimal((Float.valueOf(this.subtotal) + Float.valueOf(this.iva)) - Float.valueOf(this.desc)));
    }

    public String getFolioope() {
        return folioope;
    }

    public void setFolioope(String folioope) {
        this.folioope = folioope;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getFmapago() {
        return fmapago;
    }

    public void setFmapago(String fmapago) {
        this.fmapago = fmapago;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "ServicioExterno{" + "title=" + title + ", folioope=" + folioope + ", paciente=" + paciente + ", empresa=" + empresa + ", doctor=" + doctor + ", fmapago=" + fmapago + ", usuario=" + usuario + ", servicios=" + servicios + ", subtotal=" + subtotal + ", iva=" + iva + ", desc=" + desc + ", total=" + total + ", mensaje=" + mensaje + '}';
    }

}
