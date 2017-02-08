package org.apli.tickets.models;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import org.apli.tickets.Paciente;
import org.apli.tickets.Servicio;
import org.apli.tickets.BodyTicket;

/**
 *
 * @author mdamianhdz
 */
public class CargoCuenta extends BodyTicket {

    private int title;
    private String folioope;
    private String foliohosp;
    private Paciente paciente;
    private String empresa;
    private String usuario;
    private List<Servicio> servicios;
    private String subtotal;
    private String iva;
    private String total;

    public CargoCuenta() {
        super.setFecha(fechaActual());
        super.setHora(horaActual());
        title = 2;
    }

    public CargoCuenta(int title, String folioope, String foliohosp, Paciente paciente, String empresa, String usuario, List<Servicio> servicios, String subtotal, String iva, String total) {
        this.title = title;
        this.folioope = folioope;
        this.foliohosp = foliohosp;
        this.paciente = paciente;
        this.empresa = empresa;
        this.usuario = usuario;
        this.servicios = servicios;
        this.subtotal = subtotal;
        this.iva = iva;
        this.total = total;
    }

    public String obtieneSubtotal(List<Servicio> servicios) {
        float total = 0;
        for (Servicio servicio : servicios) {
            total += Float.valueOf(servicio.getPrecio());
        }
        total = formatDecimal(total);
        return "" + total;
    }

    public String obtieneIva(List<Servicio> servicios) {
        float iva = 0;
        for (Servicio servicio : servicios) {
            iva += Float.valueOf(servicio.getPrecio()) * (servicio.getIva() / 100);
        }
        iva = formatDecimal(iva);
        return "" + iva;
    }

    public String obtieneTotal(List<Servicio> servicios) {
        float total = 0;
        for (Servicio servicio : servicios) {
            total += (Float.valueOf(servicio.getPrecio()));
        }
        total = formatDecimal(total);
        return "" + total;
    }

    public float formatDecimal(float importe) {
        DecimalFormatSymbols simbolo = new DecimalFormatSymbols();
        simbolo.setDecimalSeparator('.');
        DecimalFormat formateador = new DecimalFormat("########.##", simbolo);
        return Float.valueOf(formateador.format(importe));
    }

    public void calcularCifras() {
        setSubtotal(obtieneSubtotal(servicios));
        setIva(obtieneIva(servicios));
        setTotal(obtieneTotal(servicios));
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

    public String getFoliohosp() {
        return foliohosp;
    }

    public void setFoliohosp(String foliohosp) {
        this.foliohosp = foliohosp;
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
        return "CargoCuenta{" + "title=" + title + ", folioope=" + folioope + ", foliohosp=" + foliohosp + ", paciente=" + paciente + ", empresa=" + empresa + ", usuario=" + usuario + ", servicios=" + servicios + ", subtotal=" + subtotal + ", iva=" + iva + ", total=" + total + '}';
    }

}