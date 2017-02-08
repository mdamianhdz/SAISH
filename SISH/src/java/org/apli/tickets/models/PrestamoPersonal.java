package org.apli.tickets.models;

import org.apli.tickets.BodyTicket;

/**
 *
 * @author mdamianhdz
 */
public class PrestamoPersonal extends BodyTicket {

    private int title;
    private String folioope;
    private String personal;
    private String usuario;
    private String concepto;
    private String importe;

    public PrestamoPersonal() {
        super.setFecha(fechaActual());
        super.setHora(horaActual());
        title = 11;
    }

    public PrestamoPersonal(int title, String folioope, String personal, String usuario, String concepto, String importe) {
        this.title = title;
        this.folioope = folioope;
        this.personal = personal;
        this.usuario = usuario;
        this.concepto = concepto;
        this.importe = importe;
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

    public String getPersonal() {
        return personal;
    }

    public void setPersonal(String personal) {
        this.personal = personal;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public String getImporte() {
        return importe;
    }

    public void setImporte(String importe) {
        this.importe = importe;
    }

    @Override
    public String toString() {
        return "PrestamoPersonal{" + "title=" + title + ", folioope=" + folioope + ", personal=" + personal + ", usuario=" + usuario + ", concepto=" + concepto + ", importe=" + importe + '}';
    }

}
