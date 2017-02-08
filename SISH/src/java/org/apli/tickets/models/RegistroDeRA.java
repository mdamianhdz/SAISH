package org.apli.tickets.models;

import java.util.List;
import org.apli.tickets.Paciente;
import org.apli.tickets.Servicio;
import org.apli.tickets.BodyTicket;

/**
 *
 * @author mdamianhdz
 */
public class RegistroDeRA extends BodyTicket {

    private int title;
    private String folioope;
    private Paciente paciente;
    private String fmapago;
    private String factura;
    private String usuario;
    private List<Servicio> servicios;
    private String cuenta;
    private String abonos;
    private String pago;
    private String desc;
    private String debe;

    public RegistroDeRA() {
        super.setFecha(fechaActual());
        super.setHora(horaActual());
        title = 8;
    }

    public RegistroDeRA(int title, String folioope, Paciente paciente, String fmapago, String factura, String usuario, List<Servicio> servicios, String cuenta, String abonos, String pago, String desc, String debe) {
        this.title = title;
        this.folioope = folioope;
        this.paciente = paciente;
        this.fmapago = fmapago;
        this.factura = factura;
        this.usuario = usuario;
        this.servicios = servicios;
        this.cuenta = cuenta;
        this.abonos = abonos;
        this.pago = pago;
        this.desc = desc;
        this.debe = debe;
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

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public String getFmapago() {
        return fmapago;
    }

    public void setFmapago(String fmapago) {
        this.fmapago = fmapago;
    }

    public String getFactura() {
        return factura;
    }

    public void setFactura(String factura) {
        this.factura = factura;
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

    public String getCuenta() {
        return cuenta;
    }

    public void setCuenta(String cuenta) {
        this.cuenta = cuenta;
    }

    public String getAbonos() {
        return abonos;
    }

    public void setAbonos(String abonos) {
        this.abonos = abonos;
    }

    public String getPago() {
        return pago;
    }

    public void setPago(String pago) {
        this.pago = pago;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDebe() {
        return debe;
    }

    public void setDebe(String debe) {
        this.debe = debe;
    }

    @Override
    public String toString() {
        return "RegistroDeRA{" + "title=" + title + ", folioope=" + folioope + ", paciente=" + paciente + ", fmapago=" + fmapago + ", factura=" + factura + ", usuario=" + usuario + ", servicios=" + servicios + ", cuenta=" + cuenta + ", abonos=" + abonos + ", pago=" + pago + ", desc=" + desc + ", debe=" + debe + '}';
    }

}
