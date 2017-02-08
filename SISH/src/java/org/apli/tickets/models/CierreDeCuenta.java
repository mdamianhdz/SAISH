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
public class CierreDeCuenta extends BodyTicket {

    private int title;
    private String folioope;
    private String foliohosp;
    private String fingreso;
    private Paciente paciente;
    private String empresa;
    private String fmapago;
    private String usuario;
    private List<Servicio> servicios;
    private String subtotal;
    private String desc;
    private String total;
    private String debe;

    public CierreDeCuenta() {
        super.setFecha(fechaActual());
        super.setHora(horaActual());
        title = 4;
    }

    public CierreDeCuenta(int title, String folioope, String foliohosp, String fingreso, Paciente paciente, String empresa, String fmapago, String usuario, List<Servicio> servicios, String subtotal, String desc, String total, String debe) {
        this.title = title;
        this.folioope = folioope;
        this.foliohosp = foliohosp;
        this.fingreso = fingreso;
        this.paciente = paciente;
        this.empresa = empresa;
        this.fmapago = fmapago;
        this.usuario = usuario;
        this.servicios = servicios;
        this.subtotal = subtotal;
        this.desc = desc;
        this.total = total;
        this.debe = debe;
    }

    public String obtieneSubtotal(List<Servicio> servicios) {
        float total = 0;
        for (Servicio servicio : servicios) {
            total += Float.valueOf(servicio.getPrecio());
        }
        total = formatDecimal(total);
        return "" + total;
    }

    public String obtieneDescuentos(List<Servicio> servicios) {
        float descuento = 0;
        for (Servicio servicio : servicios) {
            descuento += (Float.valueOf(servicio.getPrecioCobrado()) - Float.valueOf(servicio.getPrecio()));
        }
        descuento = formatDecimal(descuento);
        return "" + descuento;
    }

    public String obtieneTotal(List<Servicio> servicios) {
        float total = 0;
        for (Servicio servicio : servicios) {
            total += (Float.valueOf(servicio.getPrecio()));
        }
        total -= Float.valueOf(obtieneDescuentos(servicios));
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
        setDesc(obtieneDescuentos(servicios));
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

    public String getFingreso() {
        return fingreso;
    }

    public void setFingreso(String fingreso) {
        this.fingreso = fingreso;
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

    public String getDebe() {
        return debe;
    }

    public void setDebe(String debe) {
        this.debe = debe;
    }

    @Override
    public String toString() {
        return "CierreDeCuenta{" + "title=" + title + ", folioope=" + folioope + ", foliohosp=" + foliohosp + ", fingreso=" + fingreso + ", paciente=" + paciente + ", empresa=" + empresa + ", fmapago=" + fmapago + ", usuario=" + usuario + ", servicios=" + servicios + ", subtotal=" + subtotal + ", desc=" + desc + ", total=" + total + ", debe=" + debe + '}';
    }

}
