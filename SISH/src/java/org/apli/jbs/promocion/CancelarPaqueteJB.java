/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apli.jbs.promocion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import org.apli.jbs.MedicoJB;
import org.apli.jbs.PacienteJB;
import org.apli.modelbeans.DetallePaquete;
import org.apli.modelbeans.EpisodioMedico;
import org.apli.modelbeans.Medico;
import org.apli.modelbeans.OtrosDsctosPaq;
import org.apli.modelbeans.Paciente;
import org.apli.modelbeans.Paquete;
import org.apli.modelbeans.PaqueteContratado;
import org.apli.modelbeans.PersonalHospitalario;
import org.apli.modelbeans.ServicioPrestado;
import org.apli.modelbeans.TipoPaquete;
import org.apli.modelbeans.Valida;
import org.primefaces.context.RequestContext;

/**
 *
 * @author ERJI
 */
@ManagedBean(name = "CancelarPaqueteJB")
@SessionScoped

public class CancelarPaqueteJB implements Serializable, Converter {

    private Paciente oPaciente;
    private boolean panelActivo;
    private Paquete oPaquete;
    private List<DetallePaquete> carritoServicios = new ArrayList<DetallePaquete>();
    private List<OtrosDsctosPaq> carritoDescuentos = new ArrayList<OtrosDsctosPaq>();
    private List<PersonalHospitalario> lPersonalAutorizacion;
    private List<ServicioPrestado> listaServCons = new ArrayList<ServicioPrestado>();
    private PersonalHospitalario oPersonalAutoriza;
    private float totalCarrito = 0;
    private float montoPagado = 0;
    private PaqueteContratado oPaqueteContratado;
    private EpisodioMedico oEpisodioMedico;
    private static CancelarPaqueteJB oCancelarPaqueteJB;

    public void pacienteExistente() throws Exception {
        limpia();
        oPaciente = new PacienteJB().getPacienteSesion();
        if (oPaciente != null) {
            if (oPaciente.getFallecido() != true) {
                oPaqueteContratado = oPaciente.buscaPacienteConPaqueteContratado();
                if (oPaqueteContratado != null) {
                    montoPagado = oPaqueteContratado.montoPagadoPorPaquete(oPaqueteContratado.getEpisodioMedico().getCveepisodio());
                    DetallePaquete oDP = new DetallePaquete();
                    oDP.setPaquete(oPaqueteContratado.getPaquete());
                    this.carritoServicios = oDP.buscaTodosDetallesPaquete();
                    OtrosDsctosPaq oODP = new OtrosDsctosPaq();
                    oODP.setPaquete(oPaqueteContratado.getPaquete());
                    carritoDescuentos = oODP.buscaTodosDsctosPaq();
                    listaServCons = new ServicioPrestado().buscaTodosServiciosConsumidosPaquete(oPaqueteContratado.getEpisodioMedico().getCveepisodio());
                    this.actualizaTotal();
                    lPersonalAutorizacion = new PersonalHospitalario().buscaTodosPersonalAutorizacion();
                    panelActivo = true;

                } else {
                    FacesMessage msj2 = new FacesMessage("¡Aviso!", "¡El paciente : " + oPaciente.getNombreCompleto() + " con folio: " + oPaciente.getFolioPac() + " no tiene un paquete contratado!");
                    RequestContext.getCurrentInstance().showMessageInDialog(msj2);
                }
            } else {
                FacesMessage msj2 = new FacesMessage("¡Aviso!", "¡El paciente : " + oPaciente.getNombreCompleto() + " con folio: " + oPaciente.getFolioPac() + " es fallecido!");
                RequestContext.getCurrentInstance().showMessageInDialog(msj2);
            }
        } else {
            FacesMessage msj2 = new FacesMessage("¡Aviso!", "¡No has seleccionado un paciente!");
            RequestContext.getCurrentInstance().showMessageInDialog(msj2);
        }
    }

    public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {
        if (submittedValue.trim().equals("")) {
            return null;
        } else {
            try {
                if (component.getId().equals("personalAutoriza")) {
                    int num = Integer.parseInt(submittedValue);
                    for (PersonalHospitalario oPH : this.lPersonalAutorizacion) {
                        if (oPH.getFolioPers() == num) {
                            return oPH;
                        }
                    }
                }
            } catch (NumberFormatException exception) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "No es un paquete válido"));
            }
        }
        return null;
    }

    public String getAsString(FacesContext facesContext, UIComponent component, Object value) {
        if (value == null || value.equals("")) {
            return "";
        } else {
            String valor = "";
            if (component.getId().equals("personalAutoriza")) {
                return valor = String.valueOf(((PersonalHospitalario) value).getFolioPers());
            }
            return valor;
        }
    }

    public void actualizaTotal() {
        totalCarrito = 0;
        for (int i = 0; i < carritoServicios.size(); i++) {
            totalCarrito += (carritoServicios.get(i).getConceptoIngreso().getMontoNuevo() * carritoServicios.get(i).getConceptoIngreso().getCantidad());
        }
    }

    public void limpia() {
        oPaciente = new Paciente();
        panelActivo = false;
        oPaquete = new Paquete();
        carritoServicios = new ArrayList<DetallePaquete>();
        carritoDescuentos = new ArrayList<OtrosDsctosPaq>();
        totalCarrito = 0;
        lPersonalAutorizacion = new ArrayList<PersonalHospitalario>();
        oPersonalAutoriza = new PersonalHospitalario();
        oPaqueteContratado = new PaqueteContratado();

    }

    public void regresaCancelar() throws Exception {
        this.limpia();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.getExternalContext().redirect("cancelarPaquete.xhtml");
    }

    public void cancelarPaquete() throws Exception {
        String msj = oPaqueteContratado.cancelarPaqueteContratado();
        if (!msj.contains("ERROR") && !msj.contains("ADVERTENCIA")) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, oPaciente.getNombreCompleto()+ ": " + new Valida().eliminaMSJCorchetes(msj), "");
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, msg);
            context.getExternalContext().getFlash().setKeepMessages(true);
            FacesContext facesContext = FacesContext.getCurrentInstance();
            facesContext.getExternalContext().redirect("cancelarPaquete.xhtml");
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, msj, "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public Paciente getPaciente() {
        return oPaciente;
    }

    public void setPaciente(Paciente oPaciente) {
        this.oPaciente = oPaciente;
    }

    public boolean getPanelActivo() {
        return panelActivo;
    }

    public void setPanelActivo(boolean panelActivo) {
        this.panelActivo = panelActivo;
    }

    public Paquete getPaquete() {
        return oPaquete;
    }

    public void setPaquete(Paquete oPaquete) {
        this.oPaquete = oPaquete;
    }

    public List<DetallePaquete> getCarritoServicios() {
        return carritoServicios;
    }

    public void setCarritoServicios(List<DetallePaquete> carritoServicios) {
        this.carritoServicios = carritoServicios;
    }

    public List<OtrosDsctosPaq> getCarritoDescuentos() {
        return carritoDescuentos;
    }

    public void setCarritoDescuentos(List<OtrosDsctosPaq> carritoDescuentos) {
        this.carritoDescuentos = carritoDescuentos;
    }

    public float getTotalCarrito() {
        return totalCarrito;
    }

    public void setTotalCarrito(float totalCarrito) {
        totalCarrito = totalCarrito;
    }

    public List<PersonalHospitalario> getPersonalAutorizacion() {
        return lPersonalAutorizacion;
    }

    public void setPersonalAutorizacion(List<PersonalHospitalario> lPersonalAutorizacion) {
        this.lPersonalAutorizacion = lPersonalAutorizacion;
    }

    public PersonalHospitalario getPersonaAutoriza() {
        return oPersonalAutoriza;
    }

    public void setPersonaAutoriza(PersonalHospitalario oPersonalAutoriza) {
        this.oPersonalAutoriza = oPersonalAutoriza;
    }

    public PaqueteContratado getPaqueteContratado() {
        return oPaqueteContratado;
    }

    public void setPaqueteContratado(PaqueteContratado oPaqueteContratado) {
        this.oPaqueteContratado = oPaqueteContratado;
    }

    public CancelarPaqueteJB getCancelarPaqueteJB() {
        return CancelarPaqueteJB.oCancelarPaqueteJB;
    }

    public EpisodioMedico getEpisodioMedico() {
        return oEpisodioMedico;
    }

    public void setEpisodioMedico(EpisodioMedico oEpisodioMedico) {
        this.oEpisodioMedico = oEpisodioMedico;
    }

    public List<Medico> getMedicos() {
        return MedicoJB.medicos;
    }

    public void setMedicos(List medicos) {
        MedicoJB.medicos = medicos;
    }

    public float getMontoPagado() {
        return montoPagado;
    }

    public void setMontoPagado(float montoPagado) {
        this.montoPagado = montoPagado;
    }

    public List<ServicioPrestado> getListaServCons() {
        return listaServCons;
    }

    public void setListaServCons(List<ServicioPrestado> listaServCons) {
        this.listaServCons = listaServCons;
    }

}
