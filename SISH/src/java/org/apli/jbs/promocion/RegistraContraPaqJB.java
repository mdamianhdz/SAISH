package org.apli.jbs.promocion;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import org.apli.jbs.utilidades.DocsPDFJB;
import org.apli.jbs.MedicoJB;
import org.apli.jbs.PacienteJB;
import org.apli.jbs.UsuarioJB;
import org.apli.modelbeans.BitacoraInfoPaq;
import org.apli.modelbeans.CarnetPagos;
import org.apli.modelbeans.DetallePaquete;
import org.apli.modelbeans.EpisodioMedico;
import org.apli.modelbeans.Medico;
import org.apli.modelbeans.OtrosDsctosPaq;
import org.apli.modelbeans.Paciente;
import org.apli.modelbeans.Paquete;
import org.apli.modelbeans.PaqueteContratado;
import org.apli.modelbeans.PersonalHospitalario;
import org.apli.modelbeans.TipoPaquete;
import org.primefaces.context.RequestContext;

/**
 *
 * @author ERJI
 */
@ManagedBean(name = "RegistraContraPaqJB")
@SessionScoped
public class RegistraContraPaqJB implements Serializable, Converter {

    private Paciente oPaciente;
    private boolean panelActivo;
    private boolean panelParcialidades;
    private boolean panelTotal;
    private boolean autorizarAbono;
    private List<Paquete> lPaquetes;
    private Paquete oPaquete;
    private Paquete oPaqueteSeleccionado;
    private List<PersonalHospitalario> lPromotores;
    private PersonalHospitalario oPromotor = null;
    private List<DetallePaquete> carritoServicios = new ArrayList<DetallePaquete>();
    private List<OtrosDsctosPaq> carritoDescuentos = new ArrayList<OtrosDsctosPaq>();
    private List<CarnetPagos> lCarnetsPagos = new ArrayList<CarnetPagos>();
    private List<PersonalHospitalario> lPersonalAutorizacion;
    private PersonalHospitalario oPersonalAutoriza;
    private float totalCarrito = 0;
    private String sTipo = "";
    private int montoPrimerPago = 0;
    private PaqueteContratado oPaqueteContratado;
    private EpisodioMedico oEpisodioMedico;
    private static RegistraContraPaqJB oRegistraContraPaqJB2;
    private String rutaContrPaquete;

    public void pacienteExistente() throws Exception {
        limpia();
        oPaciente = new PacienteJB().getPacienteSesion();
        if (oPaciente != null) {
            if (oPaciente.getFallecido() != true) {
                if (oPaciente.buscaPacienteConPaqueteContratado() == null) {
                    oEpisodioMedico = new EpisodioMedico();
                    oPaquete.setTipoPaquete(new TipoPaquete());
                    panelActivo = true;
                    MedicoJB.medicos = new ArrayList<Medico>();
                    MedicoJB.medicos = new Medico().buscaTodos();
                } else {
                    FacesMessage msj2 = new FacesMessage("¡Aviso!", "¡El paciente : " + oPaciente.getNombreCompleto() + " con folio: " + oPaciente.getFolioPac() + " ya tiene un paquete contratado!");
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

    public void buscaPaquetes() throws Exception {
        lPaquetes = new ArrayList<Paquete>();
        lPaquetes = oPaquete.buscaTodosPaquetesTipo();
        lPromotores = new ArrayList<PersonalHospitalario>();
        BitacoraInfoPaq oBitacoraInfoPaq = new BitacoraInfoPaq();
        oBitacoraInfoPaq.setTipoPac(oPaquete.getTipoPaquete());
        lPromotores = oBitacoraInfoPaq.buscaTodosPromotoresPaquete();
        lPersonalAutorizacion = new PersonalHospitalario().buscaTodosPersonalAutorizacion();
        carritoServicios = new ArrayList<DetallePaquete>();
        carritoDescuentos = new ArrayList<OtrosDsctosPaq>();
        oPaqueteSeleccionado.setCosto(0);
        this.totalCarrito = 0;
        this.panelParcialidades = false;
        this.panelTotal = false;
        autorizarAbono = false;
        montoPrimerPago = 0;
        lCarnetsPagos = new ArrayList<CarnetPagos>();
        oPaqueteContratado = new PaqueteContratado();
        oPaqueteContratado.setPaquete(oPaqueteSeleccionado);
    }

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {
        if (submittedValue.trim().equals("")) {
            return null;
        } else {
            try {
                if (component.getId().equals("paquete")) {
                    int num = Integer.parseInt(submittedValue);
                    for (Paquete oPaq : this.lPaquetes) {
                        if (oPaq.getIdPaquete() == num) {
                            return oPaq;
                        }
                    }
                }
                if (component.getId().equals("promotor")) {
                    int num = Integer.parseInt(submittedValue);
                    for (PersonalHospitalario oPH : this.lPromotores) {
                        if (oPH.getFolioPers() == num) {
                            return oPH;
                        }
                    }
                }
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

    @Override
    public String getAsString(FacesContext facesContext, UIComponent component, Object value) {
        if (value == null || value.equals("")) {
            return "";
        } else {
            String valor = "";
            if (component.getId().equals("paquete")) {
                return valor = String.valueOf(((Paquete) value).getIdPaquete());
            }
            if (component.getId().equals("promotor")) {
                return valor = String.valueOf(((PersonalHospitalario) value).getFolioPers());
            }
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
        lPaquetes = new ArrayList<Paquete>();
        oPaquete = new Paquete();
        oPaqueteSeleccionado = new Paquete();
        carritoServicios = new ArrayList<DetallePaquete>();
        carritoDescuentos = new ArrayList<OtrosDsctosPaq>();
        totalCarrito = 0;
        panelParcialidades = false;
        this.panelTotal = false;
        autorizarAbono = false;
        sTipo = "";
        lPersonalAutorizacion = new ArrayList<PersonalHospitalario>();
        oPersonalAutoriza = new PersonalHospitalario();
        lPromotores = new ArrayList<PersonalHospitalario>();
        oPromotor = new PersonalHospitalario();
        montoPrimerPago = 0;
        lCarnetsPagos = new ArrayList<CarnetPagos>();
        oPaqueteContratado = new PaqueteContratado();

    }

    public void buscaInfoPaquete() throws Exception {
        DetallePaquete oDP = new DetallePaquete();
        oDP.setPaquete(oPaqueteSeleccionado);
        this.carritoServicios = oDP.buscaTodosDetallesPaquete();
        OtrosDsctosPaq oODP = new OtrosDsctosPaq();
        oODP.setPaquete(oPaqueteSeleccionado);
        carritoDescuentos = oODP.buscaTodosDsctosPaq();
        this.actualizaTotal();
        if (this.oPaqueteSeleccionado.getTipoPago().equals("T")) {
            this.sTipo = "Total";
            this.panelTotal = true;
        }
        if (this.oPaqueteSeleccionado.getTipoPago().equals("P")) {
            this.sTipo = "Parcialidades";
            this.panelParcialidades = true;
            lCarnetsPagos.clear();
            float monto = (oPaqueteSeleccionado.getCosto() - oPaqueteSeleccionado.getAnticipo()) / oPaqueteSeleccionado.getCantParcial();
            for (int i = 0; i < oPaqueteSeleccionado.getCantParcial(); i++) {
                CarnetPagos oCarnetPagos = new CarnetPagos();
                oCarnetPagos.setMonto(monto);
                lCarnetsPagos.add(oCarnetPagos);
            }
        }
    }

    public void regresaRegistraPaq() throws Exception {
        String archivoAElimina = oRegistraContraPaqJB2.getRutaContrPaquete();
        ExternalContext extCont = FacesContext.getCurrentInstance().getExternalContext();
        File folder = new File(extCont.getRealPath("//resources//"));
        if (!archivoAElimina.equals("")) {
            File archivo = new File(folder, archivoAElimina);
            if (archivo.delete()) {

            }
        }
        this.limpia();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.getExternalContext().redirect("registraContratacionPaquete.xhtml");
    }

    public void registrarContratacionPaquete() throws Exception {
        PersonalHospitalario oVende = new PersonalHospitalario();
        oVende = oVende.buscaPersonalPorCveUsuario(new UsuarioJB().getSesionUsuario().getUsuario());
        oPaqueteContratado.setTipo(sTipo);
        oPaqueteContratado.setAnticipo(oPaqueteSeleccionado.getCosto());
        boolean validacionPrimerPago = false;
        boolean validacionPagarAl = false;
        if (this.autorizarAbono == true) {
            CarnetPagos oCP = new CarnetPagos();
            lCarnetsPagos = new ArrayList<CarnetPagos>();
            oCP.setPagarAl("Antes de ejercer el paquete.");
            oCP.setMonto((oPaqueteSeleccionado.getCosto() - this.montoPrimerPago));
            lCarnetsPagos.add(oCP);
            oPaqueteContratado.setTipo("TotalAParcial");
            oPaqueteContratado.setAnticipo(montoPrimerPago);
        }
        for (int i = 0; i < this.lCarnetsPagos.size(); i++) {
            if (lCarnetsPagos.get(i).getMonto() == 0.0) {
                validacionPrimerPago = true;
            }
        }
        for (int i = 0; i < this.lCarnetsPagos.size(); i++) {
            if (lCarnetsPagos.get(i).getPagarAl().equals("")) {
                validacionPagarAl = true;
            }
        }
        if (validacionPrimerPago == true) {
            String rst = "Primer pago : Error de validación: Se necesita un valor.";
            FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
            FacesContext.getCurrentInstance().addMessage(null, msj2);

        } else if (validacionPagarAl == true) {
            String rst = "Pagar Al : Error de validación: Se necesita un valor.";
            FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
        }  else if (oPaquete.getTipoPaquete().getCve().equals("1") && oPaqueteContratado.getCesareaUrgencia()==0.0) {
            String rst = "Importe de urgencia por Cesárea : Error de validación: Se necesita un valor.";
            FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
        } else if (oPaquete.getTipoPaquete().getCve().equals("1") && oPaqueteContratado.getBloqueo()==0.0) {
            String rst = "Costo del bloqueo en Parto Normal : Error de validación: Se necesita un valor.";
            FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
        }else if (oVende == null) {
            String rst = "Usuario  : Error de validación: El usuario que está registrando este paquete no tiene un folio en Personal Hospitalario.";
            FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
        } else {
            oPaqueteContratado.setPersonalVende(oVende);
            oPaqueteContratado.setPaciente(oPaciente);
            oPaqueteContratado.setRegistro(new Date());
            oPaqueteContratado.setEpisodioMedico(oEpisodioMedico);
            oPaqueteContratado.setPaquete(oPaqueteSeleccionado);
            if (this.panelParcialidades) {
                oPaqueteContratado.setAnticipo(oPaqueteSeleccionado.getAnticipo());
            }

            String msj = oPaqueteContratado.insertaPaqueteContratado(lCarnetsPagos);
            if (!msj.contains("ERROR") && !msj.contains("ADVERTENCIA")) {
                System.out.println("MSJ:"+msj);
                String sTipoHab = "";
                for (int i = 0; i < this.carritoServicios.size(); i++) {
                    if (carritoServicios.get(i).getHabitacion() != null) {
                        sTipoHab = carritoServicios.get(i).getHabitacion().getTipoHab().getDescrip();
                        break;
                    } else {
                        sTipoHab = "Sin habitación registrada.";
                    }
                }
                
                int nTipoPaquete =Integer.parseInt(oPaquete.getTipoPaquete().getCve());
                
                DocsPDFJB oDocs = new DocsPDFJB();
                
                
                this.rutaContrPaquete = oDocs.creaContratoPaquete(
                        oPaciente.getNombreCompleto(),
                        oPaqueteContratado.getPadreoTutor(),
                        oPaqueteContratado.getIdpaqcont(),
                        oPaqueteContratado.getAnticipo(),
                        oPaqueteContratado.getCesareaUrgencia(),
                        oPaqueteContratado.getBloqueo(),
                        oPaqueteSeleccionado.getNombre(),
                        sTipoHab,
                        nTipoPaquete,
                        carritoServicios
                );
                oRegistraContraPaqJB2 = this;
                RequestContext.getCurrentInstance().execute("dlg.show()");
            } else {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, msj, "");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }
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

    public Paquete getPaqueteSeleccionado() {
        return oPaqueteSeleccionado;
    }

    public void setPaqueteSeleccionado(Paquete oPaqueteSeleccionado) {
        this.oPaqueteSeleccionado = oPaqueteSeleccionado;
    }

    public List<Paquete> getPaquetes() {
        return lPaquetes;
    }

    public void setPaquetes(List<Paquete> lPaquetes) {
        this.lPaquetes = lPaquetes;
    }

    public List<PersonalHospitalario> getPromotores() {
        return lPromotores;
    }

    public void setPromotores(List<PersonalHospitalario> lPromotores) {
        this.lPromotores = lPromotores;
    }

    public PersonalHospitalario getPromotor() {
        return oPromotor;
    }

    public void setPromotor(PersonalHospitalario oPromotor) {
        this.oPromotor = oPromotor;
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

    public boolean getPanelParcialidades() {
        return panelParcialidades;
    }

    public void setPanelParcialidades(boolean panelParcialidades) {
        this.panelParcialidades = panelParcialidades;
    }

    public boolean getPanelTotal() {
        return panelTotal;
    }

    public void setPanelTotal(boolean panelTotal) {
        this.panelTotal = panelTotal;
    }

    public String getTipo() {
        return sTipo;
    }

    public void setTipo(String sTipo) {
        this.sTipo = sTipo;
    }

    public float getTotalCarrito() {
        return totalCarrito;
    }

    public void setTotalCarrito(float value) {
        totalCarrito = value;
    }

    public boolean getAutorizarAbono() {
        return autorizarAbono;
    }

    public void setAutorizarAbono(boolean value) {
        this.autorizarAbono = value;
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

    public int getMontoPrimerPago() {
        return montoPrimerPago;
    }

    public void setMontoPrimerPago(int montoPrimerPago) {
        this.montoPrimerPago = montoPrimerPago;
    }

    public List<CarnetPagos> getCarnetsPagos() {
        return lCarnetsPagos;
    }

    public void setCarnetsPagos(List<CarnetPagos> lCarnetsPagos) {
        this.lCarnetsPagos = lCarnetsPagos;
    }

    public PaqueteContratado getPaqueteContratado() {
        return oPaqueteContratado;
    }

    public void setPaqueteContratado(PaqueteContratado oPaqueteContratado) {
        this.oPaqueteContratado = oPaqueteContratado;
    }

    public RegistraContraPaqJB getRegistraContraPaqJB2() {
        return RegistraContraPaqJB.oRegistraContraPaqJB2;
    }

    public void setoRegistraContraPaqJB2(RegistraContraPaqJB oRegistraContraPaqJB2) {
        RegistraContraPaqJB.oRegistraContraPaqJB2 = oRegistraContraPaqJB2;
    }

    public List<Paquete> getlPaquetes() {
        return lPaquetes;
    }

    public void setlPaquetes(List<Paquete> lPaquetes) {
        this.lPaquetes = lPaquetes;
    }

    public String getRutaContrPaquete() {
        return rutaContrPaquete;
    }

    public void setRutaContrPaquete(String rutaContrPaquete) {
        this.rutaContrPaquete = rutaContrPaquete;
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

}
