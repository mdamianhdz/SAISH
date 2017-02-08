package org.apli.tickets.models;

import java.util.List;
import org.apli.tickets.Servicio;
import org.apli.tickets.BodyTicket;

/**
 *
 * @author mdamianhdz
 */
public class PagoDeRenta extends BodyTicket {

    private int title;
    private String folioope;
    private String doctor;
    private String recfiscal;
    private String mes;
    private String anio;
    private String usuario;
    private List<Servicio> servicios;

    public PagoDeRenta() {
        super.setFecha(fechaActual());
        super.setHora(horaActual());
        title = 5;
    }

    public PagoDeRenta(int title, String folioope, String doctor, String recfiscal, String mes, String anio, String usuario, List<Servicio> servicios) {
        this.title = title;
        this.folioope = folioope;
        this.doctor = doctor;
        this.recfiscal = recfiscal;
        this.mes = mes;
        this.anio = anio;
        this.usuario = usuario;
        this.servicios = servicios;
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

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getRecfiscal() {
        return recfiscal;
    }

    public void setRecfiscal(String recfiscal) {
        this.recfiscal = recfiscal;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public String getAnio() {
        return anio;
    }

    public void setAnio(String anio) {
        this.anio = anio;
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

    @Override
    public String toString() {
        return "PagoDeRenta{" + "title=" + title + ", folioope=" + folioope + ", doctor=" + doctor + ", recfiscal=" + recfiscal + ", mes=" + mes + ", anio=" + anio + ", usuario=" + usuario + ", servicios=" + servicios + '}';
    }

}
