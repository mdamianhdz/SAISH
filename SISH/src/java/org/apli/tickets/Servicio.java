package org.apli.tickets;

/**
 *
 * @author mdamianhdz
 */
public class Servicio {

    private int cantidad;
    private String descripcion;
    private String precio;
    private String precioCobrado;
    private float iva;

    public Servicio(int cantidad, String descripcion, String precio, float iva) {
        this.cantidad = cantidad;
        this.descripcion = descripcion;
        this.precio = precio;
        this.iva = iva;
    }

    public Servicio(int cantidad, String descripcion, String precio, String precioOriginal, float iva) {
        this.cantidad = cantidad;
        this.descripcion = descripcion;
        this.precio = precioOriginal;
        this.precioCobrado = precio;
        this.iva = iva;
    }

    public Servicio() {
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public float getIva() {
        return iva;
    }

    public void setIva(float iva) {
        this.iva = iva;
    }

    public String getPrecioCobrado() {
        return precioCobrado;
    }

    public void setPrecioCobrado(String precioCobrado) {
        this.precioCobrado = precioCobrado;
    }

    @Override
    public String toString() {
        return "Servicio{" + "cantidad=" + cantidad + ", descripcion=" + descripcion + ", precio=" + precio + ", precioOriginal=" + precioCobrado + ", iva=" + iva + '}';
    }

}
