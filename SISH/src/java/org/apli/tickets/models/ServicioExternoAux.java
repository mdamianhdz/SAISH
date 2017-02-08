package org.apli.tickets.models;

import java.io.Serializable;

/**
 *
 * @author Angel
 */
public class ServicioExternoAux implements Serializable{

    private String ticketFolio;
    private String ticketLabel;

    public ServicioExternoAux() {
    }

    public String getTicketFolio() {
        return ticketFolio;
    }

    public void setTicketFolio(String ticketFolio) {
        this.ticketFolio = ticketFolio;
    }

    public String getTicketLabel() {
        return ticketLabel;
    }

    public void setTicketLabel(String ticketLabel) {
        this.ticketLabel = ticketLabel;
    }

    @Override
    public String toString() {
        return "ServicioExternoAux{" + "ticketContent=" + ticketFolio + ", labelTicket=" + ticketLabel + '}';
    }

}
