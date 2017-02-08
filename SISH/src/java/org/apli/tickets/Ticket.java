package org.apli.tickets;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Calendar;

/**
 *
 * @author mdamianhdz
 */
public class Ticket {

    private boolean reimpresion;
    private int title;
    private String fecha;
    private String hora;
    private String folioope;
    private Paciente paciente;
    private String empresa;
    private String doctor;
    private String fmapago;
    private String usuario;
    private List<Servicio> servicios = new ArrayList();
    private Servicio servicio;
    private String subtotal;
    private String iva;
    private String desc;
    private String total;
    private String mensaje;

    public String convertToJSON() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public String fechaActual() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DATE) + "-" + getNombreMes(calendar.get(Calendar.MONTH)) + "-" + calendar.get(Calendar.YEAR);
    }

    public String horaActual() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
    }

    public String obtieneSubtotal() {
        float total1 = 0;
        for (Servicio servicio1 : servicios) {
            total1 += Float.valueOf(servicio1.getPrecio());
        }
        total1 = formatDecimal(total1);
        return "" + total1;
    }

    public String obtieneIva() {
        float iva1 = 0;
        for (Servicio servicio1 : servicios) {
            iva1 += Float.valueOf(servicio1.getPrecio()) * (servicio1.getIva() / 100);
        }
        iva1 = formatDecimal(iva1);
        return "" + iva1;
    }

    public String obtieneDescuentos() {
        float descuento = 0;
        for (Servicio servicio1 : servicios) {
            descuento += (Float.valueOf(servicio1.getPrecioCobrado()) - Float.valueOf(servicio1.getPrecio()));
        }
        descuento = formatDecimal(descuento);
        return "" + descuento;
    }

    public String obtieneTotal() {
        float total1 = 0;
        for (Servicio servicio1 : servicios) {
            total1 += (Float.valueOf(servicio1.getPrecio()));
        }
        total1 -= Float.valueOf(obtieneDescuentos());
        total1 = formatDecimal(total1);
        return "" + total1;
    }

    public String getNombreMes(int mes) {
        String[] meses = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        return meses[mes];
    }

    public float formatDecimal(float importe) {
        DecimalFormatSymbols simbolo = new DecimalFormatSymbols();
        simbolo.setDecimalSeparator('.');
        DecimalFormat formateador = new DecimalFormat("########.##", simbolo);
        return Float.valueOf(formateador.format(importe));
    }

    public Ticket() {
    }

    public boolean isReimpresion() {
        return reimpresion;
    }

    public void setReimpresion(boolean reimpresion) {
        this.reimpresion = reimpresion;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
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

    public Servicio getServicio() {
        return servicio;
    }

    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
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

    @Override
    public String toString() {
        return "Ticket{" + "reimpresion=" + reimpresion + ", title=" + title + ", fecha=" + fecha + ", hora=" + hora + ", folioope=" + folioope + ", paciente=" + paciente + ", empresa=" + empresa + ", doctor=" + doctor + ", fmapago=" + fmapago + ", usuario=" + usuario + ", servicios=" + servicios + ", servicio=" + servicio + ", subtotal=" + subtotal + ", iva=" + iva + ", desc=" + desc + ", total=" + total + ", mensaje=" + mensaje + '}';
    }

}
