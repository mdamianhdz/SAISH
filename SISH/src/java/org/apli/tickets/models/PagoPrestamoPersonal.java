package org.apli.tickets.models;

import org.apli.tickets.BodyTicket;

/**
 *
 * @author mdamianhdz
 */
public class PagoPrestamoPersonal extends BodyTicket {

    private int title;
    private String prestamo;
    private String folioegreso;
    private String foliopago;//Folio de operacion en caja
    private String personal;
    private String abono;
    private String resta;

    public PagoPrestamoPersonal() {
        super.setFecha(fechaActual());
        super.setHora(horaActual());
        title = 10;
    }

    public PagoPrestamoPersonal(int title, String prestamo, String folioegreso, String foliopago, String personal, String abono, String resta) {
        this.title = title;
        this.prestamo = prestamo;
        this.folioegreso = folioegreso;
        this.foliopago = foliopago;
        this.personal = personal;
        this.abono = abono;
        this.resta = resta;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public String getPrestamo() {
        return prestamo;
    }

    public void setPrestamo(String prestamo) {
        this.prestamo = prestamo;
    }

    public String getFolioegreso() {
        return folioegreso;
    }

    public void setFolioegreso(String folioegreso) {
        this.folioegreso = folioegreso;
    }

    public String getFoliopago() {
        return foliopago;
    }

    public void setFoliopago(String foliopago) {
        this.foliopago = foliopago;
    }

    public String getPersonal() {
        return personal;
    }

    public void setPersonal(String personal) {
        this.personal = personal;
    }

    public String getAbono() {
        return abono;
    }

    public void setAbono(String abono) {
        this.abono = abono;
    }

    public String getResta() {
        return resta;
    }

    public void setResta(String resta) {
        this.resta = resta;
    }

    @Override
    public String toString() {
        return "PagoPrestamoPersonal{" + "title=" + title + ", prestamo=" + prestamo + ", folioegreso=" + folioegreso + ", foliopago=" + foliopago + ", personal=" + personal + ", abono=" + abono + ", resta=" + resta + '}';
    }

}
