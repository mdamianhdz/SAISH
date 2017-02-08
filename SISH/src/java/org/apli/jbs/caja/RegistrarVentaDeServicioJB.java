package org.apli.jbs.caja;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apli.modelbeans.OperacionCaja;
import org.apli.modelbeans.ServicioPrestado;
import org.primefaces.context.RequestContext;
import org.apli.jbs.PacienteJB;
import org.apli.modelbeans.Paciente;
import org.apli.tickets.models.ServicioExternoAux;
import org.apli.tickets.models.Ticket;

/**
 *
 * @author Manuel
 */
@ManagedBean(name = "oRegVntSr")
@ViewScoped
public class RegistrarVentaDeServicioJB extends IngresosCaja
                                        implements Serializable {

private List<ServicioPrestado> listSP;
private List<ServicioPrestado> listaServSelec;
private List<ServicioPrestado> listaFiltrada;
private ServicioPrestado oSP;
private Paciente oPacSeleccionado;
private String foliosPago = "";
private String[] tickets;
private List<ServicioExternoAux> infoTickets;
private static final Logger LOG = Logger.getLogger(RegistrarVentaDeServicioJB.class.getName());
    
    public RegistrarVentaDeServicioJB() {
    }

    public void limpiaPagina() {
        sForma = "";
        if (this.oPacSeleccionado != null && this.oPacSeleccionado.getFolioPac() > 0) {
            cargaServiciosPrestados();
        }
    }
    
    public boolean validaPaciente() {
        boolean validacion = false;
        if (oPacSeleccionado != null) {
            validacion = true;
        }
        return validacion;
    }

    public void regresarSeleccion() {
        sForma = "";
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('selDialog').hide()");
    }

    public void regresarVenta() throws Exception {
        selectedServ = new ServicioPrestado();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.getExternalContext().redirect("registrarVentaDeServicio.xhtml?faces-redirect=true");
    }

    public void cargaServiciosPrestados() {
    this.oPacSeleccionado = new PacienteJB().getPacienteSesion();
    listSP = new ArrayList();
    oSP = new ServicioPrestado();
    FacesMessage msg;
        try {
            oSP.setPaciente(oPacSeleccionado);
            listSP = oSP.todosServsPrstsNvs();
        } catch (Exception e) {
            e.printStackTrace();
            msg = new FacesMessage("Aviso", "Error al buscar servicios por pagar");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public void validaPagoDeServicio() throws Exception {
        if (this.sForma.equalsIgnoreCase("")) {
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('diaFD').show()");
        } else if (selectedServ.getQuienPaga() == 1) {
            if (this.sForma.equalsIgnoreCase("CRE  ")) {
                pagarServicioPrestado();
            } else {
                RequestContext context = RequestContext.getCurrentInstance();
                context.execute("PF('diaEE').show()");
            }
        } else if (this.sForma.equalsIgnoreCase("CHQ  ")) {
            if (this.oOpeCaja.getDatosPago().equalsIgnoreCase("")) {
                RequestContext context = RequestContext.getCurrentInstance();
                context.execute("PF('diaFDP').show()");
            } else {
                pagarServicioPrestado();
            }
        } else if (this.sForma.equalsIgnoreCase("TRE  ")) {
            if (this.oOpeCaja.getDatosPago().equalsIgnoreCase("")) {
                RequestContext context = RequestContext.getCurrentInstance();
                context.execute("PF('diaFDP').show()");
            } else {
                pagarServicioPrestado();
            }
        } else {
            pagarServicioPrestado();
        }
    }

    public void pagarServicioPrestado() throws Exception {
    boolean bResult;
        String sConAutorizaciones = "", sFolios = "";
    FacesMessage msg;
        try {
            if (this.bAutDscto && this.bAutorizado) {
                sConAutorizaciones = OperacionCaja.CON_DSCTO;
            } else if (this.bAutPagoMenor && this.bAutorizado) {
                sConAutorizaciones = OperacionCaja.CON_PAGO_PARCIAL;
            }
            oOpeCaja.getFrmPago().setCveFrmPago(sForma);
            sFolios = oOpeCaja.pagaServicioMultiple(this.selectedServ, 
                    this.listaServSelec, sConAutorizaciones);
            System.out.println(sFolios);
            this.foliosPago = sFolios;
            if (sFolios.equals("")) {
                bResult = false;
            } else {
                bResult = true;
            }
            if (bResult) {
                createFoliosOpeList();
                RequestContext.getCurrentInstance().execute("PF('dlg1').show()");
            } else {
                msg = new FacesMessage("Aviso", oOpeCaja.getFolioRet());
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
            msg = new FacesMessage("Aviso", "Error al guardar el pago");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    //--------------------------------------------------------------------------
    public void createFoliosOpeList() throws Exception {
        String[] tmp;
        ServicioExternoAux aux;
        if (!this.foliosPago.equals("")) {
            this.foliosPago = this.foliosPago.trim();
            tmp = foliosPago.split(" ");
            //Creacion de lista auxiliar con etiquetas y contenido de ticket para visualizacion en xhtml
            this.infoTickets = new ArrayList<ServicioExternoAux>();
            for (String folio : tmp) {
                aux = new ServicioExternoAux();
                aux.setTicketFolio(folio);
                aux.setTicketLabel(new Ticket().buscarAreaDeServicioParaTicket(folio));//Buscar etiqueta para tabla en dialog de folios de operacion
                this.infoTickets.add(aux);
            }
            LOG.trace("Total de folios de operacion encontrados: " + tmp.length);
        }
    }
            
    public String createTicket(String folio) throws Exception {
        String ticket = "";
        if (!folio.equals("")) {
            ticket = new Ticket().buscarTicketExterno(folio, false);
            LOG.trace("Ticket generado: " + ticket);
        }
        return ticket;
    }
 
    //--------------------------------------------------------------------------
    public org.apli.modelbeans.Paciente getPaciente() {
        return oPacSeleccionado;
    }

    public void setPaciente(org.apli.modelbeans.Paciente value) {
        this.oPacSeleccionado = value;
    }

    public List<ServicioPrestado> getListSP() {
        return listSP;
    }

    public void setListSP(List<ServicioPrestado> value) {
        this.listSP = value;
    }

    public List<ServicioPrestado> getListServSelec() {
        return this.listaServSelec;
    }

    public void setListServSelec(List<ServicioPrestado> value) {
        this.listaServSelec = value;
        if (!listaServSelec.isEmpty()) {
            this.selectedServ = listaServSelec.get(0);
        }
    }
    
    public float getTotalPagar() {
    float nRet = 0.0f;
        if (listaServSelec != null && !listaServSelec.isEmpty()) {
            for (ServicioPrestado s : listaServSelec) {
                nRet = nRet + (s.getCantidad() * s.getCostoOriginal());
            }
        }
        return nRet;
    }
    
    @Override
    public void seleccion() {
        boolean bDiferentesPaga = false;
    FacesMessage msg;
        if (selectedServ == null
                  || selectedServ.getIdFolio() == null || selectedServ.getIdFolio().equals("")) {
            msg = new FacesMessage("Aviso", "Falta seleccionar pago");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            //Verifica si todos los servicios marcados tienen el mismo tipo
            //que el primero (selectedServ se queda con el primero de la lista)
            for (ServicioPrestado s : listaServSelec) {
                if (s.getQuienPaga() != selectedServ.getQuienPaga()) {
                    bDiferentesPaga = true;
                    break;
                }
            }
            if (bDiferentesPaga) {
                msg = new FacesMessage("Aviso", 
                        "Todos los servicios seleccionados deben pagarse igual ");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            } else {
                RequestContext.getCurrentInstance().execute("PF('selDialog').show()");
            }
        }
    }

    public String getFoliosPago() {
        return foliosPago;
}

    public void setFoliosPago(String foliosPago) {
        this.foliosPago = foliosPago;
    }

    public String[] getTickets() {
        return tickets;
    }

    public void setTickets(String[] tickets) {
        this.tickets = tickets;
    }

    public List<ServicioExternoAux> getInfoTickets() {
        return infoTickets;
    }

    public void setInfoTickets(List<ServicioExternoAux> infoTickets) {
        this.infoTickets = infoTickets;
    }
    
    public List<ServicioPrestado> getListaFiltrada() {
        return listaFiltrada;
    }
    public void setListaFiltrada(List<ServicioPrestado> valor) {
        this.listaFiltrada = valor;
    }

}
