package org.apli.tickets;

import com.google.gson.Gson;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author mdamianhdz
 */
public class BodyTicket {

    private boolean reimpresion;
    private String fecha;
    private String hora;
    private Object content;
    private static final Logger LOG = Logger.getLogger(BodyTicket.class.getName());

    public String convertToJSON(Object content) {
        Gson gson = new Gson();
        return gson.toJson(content);
    }

    public String labelTime(Date date) {
        DateFormat formatoHora = new SimpleDateFormat("HH:mm:ss");
        return formatoHora.format(date);
    }

    public String labelDate(Date date) {
        DateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        return formatoFecha.format(date);
    }

    public String fechaActual() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DATE) + "-" + getNombreMes(calendar.get(Calendar.MONTH)) + "-" + calendar.get(Calendar.YEAR);
    }

    public String horaActual() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
    }

    public String getNombreMes(int mes) {
        String[] meses = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        return meses[mes];
    }

    public float desglosarIVA(String importe, float iva) {
        float base = 1 + (Float.valueOf(iva) / 100);
        return Float.valueOf(importe) / base;
    }

    public float agregarIVA(String importe, float iva) {
        float valor = Float.parseFloat(importe);
        return valor + (valor * (iva / 100));
    }

    public List<Servicio> desglosarImportes(List<Servicio> servicios) {
        List<Servicio> temp = new ArrayList<Servicio>();
        for (Servicio servicio : servicios) {
            servicio.setPrecio(String.valueOf(formatDecimal(desglosarIVA(servicio.getPrecio(), servicio.getIva()))));
            servicio.setPrecioCobrado(String.valueOf(formatDecimal(desglosarIVA(servicio.getPrecioCobrado(), servicio.getIva()))));
            temp.add(servicio);
        }
        return temp;
    }

    public String obtieneSubtotal(List<Servicio> servicios) {
        float total = 0;
        for (Servicio servicio : servicios) {
            total += Float.valueOf(servicio.getPrecio());
        }
        total = formatDecimal(total);
        LOG.trace("Subtotal obtenido en ticket: [" + total + "]");
        return "" + total;
    }

    public String obtieneIva(List<Servicio> servicios) {
        float iva = 0;
        for (Servicio servicio : servicios) {
            iva += Float.valueOf(servicio.getPrecio()) * (servicio.getIva() / 100);
        }
        iva = formatDecimal(iva);
        LOG.trace("Iva obtenido en ticket: [" + iva + "]");
        return "" + iva;
    }

    public String obtieneDescuentos(List<Servicio> servicios) {
        float descuento = 0;
        for (Servicio servicio : servicios) {
            //Agregar IVA ya que los importes que estan almacenados actualmente son con IVA desglosado
            float precioConIva = Float.valueOf(agregarIVA(servicio.getPrecio(), servicio.getIva()));
            float precioCobradoConIva = Float.valueOf(agregarIVA(servicio.getPrecioCobrado(), servicio.getIva()));
            LOG.trace("Servicio: [" + servicio + "]");
            LOG.trace("PrecioConIVA: [" + precioConIva + "]");
            LOG.trace("PrecioCobradoConIVA: [" + precioCobradoConIva + "]");
            descuento += precioConIva - precioCobradoConIva;
        }
        descuento = formatDecimal(descuento);
        LOG.trace("Total de descuentos: [" + descuento + "]");
        return "" + descuento;
    }

    public float formatDecimal(float importe) {
        DecimalFormatSymbols simbolo = new DecimalFormatSymbols();
        simbolo.setDecimalSeparator('.');
        DecimalFormat formateador = new DecimalFormat("########.##", simbolo);
        return Float.valueOf(formateador.format(importe));
    }

    public BodyTicket() {
    }

    public boolean isReimpresion() {
        return reimpresion;
    }

    public void setReimpresion(boolean reimpresion) {
        this.reimpresion = reimpresion;
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

    @Override
    public String toString() {
        return "Ticket{" + "reimpresion=" + reimpresion + ", fecha=" + fecha + ", hora=" + hora + ", content=" + content + '}';
    }

}
