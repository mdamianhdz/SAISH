package org.apli.tickets.models;

import java.util.List;
import org.apli.tickets.Paciente;
import org.apli.tickets.Servicio;
import org.apli.tickets.BodyTicket;

/**
 *
 * @author mdamianhdz
 */
public class OrdenDeServicio extends BodyTicket {

    private int title;
    private String folioorden;
    private Paciente paciente;
    private List<Servicio> servicios;
    private String total;

    public OrdenDeServicio() {
        super.setFecha(fechaActual());
        super.setHora(horaActual());
        title = 12;
    }

    public OrdenDeServicio(int title, String folioorden, Paciente paciente, List<Servicio> servicios, String total) {
        this.title = title;
        this.folioorden = folioorden;
        this.paciente = paciente;
        this.servicios = servicios;
        this.total = total;
    }

    public void calcularCifras() {
        this.total = obtieneTotal(servicios);
    }

    public String obtieneTotal(List<Servicio> servicios) {
        float total = 0;
        for (Servicio servicio : servicios) {
            total += Float.valueOf(servicio.getPrecio());
        }
        total = super.formatDecimal(total);
        return "" + total;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public String getFolioorden() {
        return folioorden;
    }

    public void setFolioorden(String folioorden) {
        this.folioorden = folioorden;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public List<Servicio> getServicios() {
        return servicios;
    }

    public void setServicios(List<Servicio> servicios) {
        this.servicios = servicios;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "OrdenDeServicio{" + "title=" + title + ", folioorden=" + folioorden + ", paciente=" + paciente + ", servicios=" + servicios + ", total=" + total + '}';
    }

}
