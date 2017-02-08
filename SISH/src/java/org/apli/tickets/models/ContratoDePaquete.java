package org.apli.tickets.models;

import org.apli.tickets.Paciente;
import org.apli.tickets.BodyTicket;

/**
 *
 * @author mdamianhdz
 */
public class ContratoDePaquete extends BodyTicket {

    private int title;
    private String folioope;
    private Paciente paciente;
    private String usuario;
    private String paquete;
    private String importe;
    private String debe;

    public ContratoDePaquete() {
        super.setFecha(fechaActual());
        super.setHora(horaActual());
        title = 6;
    }

    public ContratoDePaquete(int title, String folioope, Paciente paciente, String usuario, String paquete, String importe, String debe) {
        this.title = title;
        this.folioope = folioope;
        this.paciente = paciente;
        this.usuario = usuario;
        this.paquete = paquete;
        this.importe = importe;
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

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPaquete() {
        return paquete;
    }

    public void setPaquete(String paquete) {
        this.paquete = paquete;
    }

    public String getImporte() {
        return importe;
    }

    public void setImporte(String importe) {
        this.importe = importe;
    }

    public String getDebe() {
        return debe;
    }

    public void setDebe(String debe) {
        this.debe = debe;
    }

    @Override
    public String toString() {
        return "ContratoDePaquete{" + "title=" + title + ", folioope=" + folioope + ", paciente=" + paciente + ", usuario=" + usuario + ", paquete=" + paquete + ", importe=" + importe + ", debe=" + debe + '}';
    }

}
