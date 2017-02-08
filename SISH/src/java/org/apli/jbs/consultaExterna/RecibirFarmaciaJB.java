package org.apli.jbs.consultaExterna;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import org.apache.log4j.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.apli.jbs.PacienteJB;
import org.apli.modelbeans.EpisodioMedico;
import org.apli.modelbeans.Paciente;
import org.apli.modelbeans.ServicioPrestado;
import org.primefaces.context.RequestContext;

/**
 *
 * @author MiguelAngel
 */
@ManagedBean(name = "oRecibirFarmacia")
@SessionScoped
public class RecibirFarmaciaJB implements Serializable {

    private Paciente oPaciente;
    private EpisodioMedico oEpisodioMedico;
    private String sArea;
    private boolean bPacHospitalizado;
    private List<ServicioPrestado> listSP;
    private ServicioPrestado oServicioPrestado;
    private boolean pedidosPendientes;
    private int habitacion;
    private static final Logger LOG = Logger.getLogger(RecibirFarmaciaJB.class.getName());

    public RecibirFarmaciaJB() {
    }

    public void obtenerDatos() throws Exception {
        limpiarDatos();
        String sPaginaLlamada = FacesContext.getCurrentInstance().getViewRoot().getViewId();
        LOG.trace(new StringBuilder().append("Pagina llamada: ").append(sPaginaLlamada));
        if (sPaginaLlamada.equals("/cargoCuentas/recibirPedidoFarmacia.xhtml")) {
            this.sArea = "AdmonPiso";
        } else if (sPaginaLlamada.equals("/consultaExterna/registrarValeSurtido.xhtml")) {
            this.sArea = "ConsultaExt";
        }
        this.oPaciente = new PacienteJB().getPacienteSesion();
        if (this.sArea.equals("AdmonPiso")) { //Ingresando desde cargo a cuentas para recibir un pedido de farmacia para paciente hospitalizado
            LOG.trace(new StringBuilder().append("Folio de paciente a enviar: ").append(this.oPaciente.getFolioPac()));
            this.oEpisodioMedico = new EpisodioMedico();
            if (!this.oEpisodioMedico.buscaPacienteHospitalizado(this.oPaciente.getFolioPac())) {
                this.bPacHospitalizado = false;
                FacesMessage msj2 = new FacesMessage("¡Aviso!", "¡El paciente : " + this.oPaciente.getNombreCompleto() + " con folio: " + this.oPaciente.getFolioPac() + " no ha sido hospitalizado!");
                RequestContext.getCurrentInstance().showMessageInDialog(msj2);
            } else {
                bPacHospitalizado = true;
                this.habitacion = this.oEpisodioMedico.getHab().getHab();
                this.oServicioPrestado = new ServicioPrestado();
                this.listSP = this.oServicioPrestado.buscarServiciosPendientesFarmacia(this.oPaciente.getFolioPac());
                LOG.trace(listSP);
                if (this.listSP.isEmpty()) {
                    FacesMessage msj2 = new FacesMessage("¡Aviso!", "¡El paciente : " + this.oPaciente.getNombreCompleto() + " con folio: " + this.oPaciente.getFolioPac() + " no tiene requisiciones pendientes!");
                    RequestContext.getCurrentInstance().showMessageInDialog(msj2);
                } else {
                    this.pedidosPendientes = true;
                }
            }

        }
        if (this.sArea.equals("ConsultaExt")) {//Ingresando desde CE para registrar un pedido que fue surtido a un paciente NO hospitalizado
            this.oServicioPrestado = new ServicioPrestado();
            this.habitacion = 200;
            this.listSP = this.oServicioPrestado.buscarServiciosPendientesFarmacia(this.oPaciente.getFolioPac());
            LOG.trace(listSP);
            if (this.listSP.isEmpty()) {
                FacesMessage msj2 = new FacesMessage("¡Aviso!", "¡El paciente : " + this.oPaciente.getNombreCompleto() + " con folio: " + this.oPaciente.getFolioPac() + " no tiene requisiciones pendientes!");
                RequestContext.getCurrentInstance().showMessageInDialog(msj2);
            } else {
                this.pedidosPendientes = true;
            }
        }
    }

    public void limpiarDatos() {
        this.oPaciente = null;
        this.bPacHospitalizado = false;
        this.listSP = null;
        this.sArea = "";
        this.oEpisodioMedico = null;
        this.oServicioPrestado = null;
        this.pedidosPendientes = false;
    }

    public boolean validaUsuario() {
        boolean validacion = false;
        if (this.oPaciente != null) {
            validacion = true;
        }
        return validacion;
    }

    public void actualizaSitFarmacia() throws Exception {
        List<ServicioPrestado> tmpListSP = new ArrayList<ServicioPrestado>();
        boolean updateFailed = false;
        String messageFailed = new StringBuilder().toString();
        String message = new StringBuilder().toString();
        for (ServicioPrestado servicioPrestado : this.listSP) {
            if (servicioPrestado.isRecibido()) {
                message = servicioPrestado.actualizaSituacionFarmacia();
                if (message.indexOf("ERROR") > -1) {
                    updateFailed = true;
                    messageFailed = message;
                }
                LOG.trace(message);
            } else {
                tmpListSP.add(servicioPrestado);
            }
        }
        if (updateFailed) {
            String rst = new StringBuilder().append("Recibir pedido: Error de actualizacion: ").append(messageFailed).toString();
            FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
        } else {
            if (this.listSP.size() == tmpListSP.size()) {
                String rst = new StringBuilder().append("Recibir pedido: No se ha seleccionado material y/o medicamento para recibir").toString();
                FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_INFO, rst, "");
                FacesContext.getCurrentInstance().addMessage(null, msj2);
            } else {
                FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_INFO, message, "");
                FacesContext.getCurrentInstance().addMessage(null, msj2);
            }
        }
        this.listSP = tmpListSP;
        LOG.trace(this.listSP);
    }

    public Paciente getPaciente() {
        return oPaciente;
    }

    public void setPaciente(Paciente oPaciente) {
        this.oPaciente = oPaciente;
    }

    public EpisodioMedico getEpisodioMedico() {
        return oEpisodioMedico;
    }

    public void setEpisodioMedico(EpisodioMedico oEpisodioMedico) {
        this.oEpisodioMedico = oEpisodioMedico;
    }

    public String getArea() {
        return sArea;
    }

    public void setArea(String sArea) {
        this.sArea = sArea;
    }

    public boolean getPacHospitalizado() {
        return bPacHospitalizado;
    }

    public void setPacHospitalizado(boolean bPacHospitalizado) {
        this.bPacHospitalizado = bPacHospitalizado;
    }

    public List<ServicioPrestado> getListSP() {
        return listSP;
    }

    public void setListSP(List<ServicioPrestado> listSP) {
        this.listSP = listSP;
    }

    public ServicioPrestado getServicioPrestado() {
        return oServicioPrestado;
    }

    public void setServicioPrestado(ServicioPrestado oServicioPrestado) {
        this.oServicioPrestado = oServicioPrestado;
    }

    public boolean getPedidosPendientes() {
        return pedidosPendientes;
    }

    public void setPedidosPendientes(boolean pedidosPendientes) {
        this.pedidosPendientes = pedidosPendientes;
    }

    public int getHabitacion() {
        return habitacion;
    }

    public void setHabitacion(int habitacion) {
        this.habitacion = habitacion;
    }
    

    @Override
    public String toString() {
        return "RecibirFarmaciaJB{" + "oPaciente=" + oPaciente + ", oEpisodioMedico=" + oEpisodioMedico + ", sArea=" + sArea + ", bPacHospitalizado=" + bPacHospitalizado + ", listSP=" + listSP + ", oServicioPrestado=" + oServicioPrestado + ", pedidosPendientes=" + pedidosPendientes + ", habitacion=" + habitacion + '}';
    }
}
