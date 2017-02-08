package org.apli.jbs;

import org.apli.modelbeans.Paciente;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.apli.modelbeans.ventaCredito.CompaniaCred;
import org.apli.modelbeans.EpisodioMedico;
import org.apli.modelbeans.ServicioPrestado;
import org.apli.modelbeans.Utilidad;
import org.primefaces.event.RowEditEvent;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import org.apli.jbs.utilidades.DocsPDFJB;
import org.apli.modelbeans.Diagnostico;

/**
 * ConsultaJB.java JSF Managed Bean archivo que realiza el cierre de una cuenta
 *
 * @author Humberto Marin Vega Fecha: Junio 2014
 */
@ManagedBean(name = "oPreCCta")
@ViewScoped
public class PrecerrarCuentaJB implements Serializable {

    private Date fecha;
    private Paciente oPac;
    private EpisodioMedico oEpMed;
    private Utilidad oUtilidad;

    private Paciente[] pacientes;
    private List<String> nomPacientes;
    private List<ServicioPrestado> serviciosNoPagados;
    private List<ServicioPrestado> serviciosPagosExterno;
    private List<ServicioPrestado> serviciosAnticipo;
    private CompaniaCred[] companiasCred;
    private List<String> nomCompanias;

    private String paciente;
    private CompaniaCred oComCred;
    private float subtotalCom = 0;
    private float ivaCom = 0;
    private float totalCom = 0;
    private float subtotalPac = 0;
    private float ivaPac = 0;
    private float totalPac = 0;
    private float totalCuenta = 0;
    private float totalPagos = 0;
    private float totaPendientePago = 0;
    private String numLetra = "";

    // PAGARE
    private String rutaPDFpagare;
    private Date fechaPagare;
    private float interesPagare;
    private String autorizacion = "";
    private List<String> polizas;
    private boolean encontrado = false;
    private boolean disableCredito;
    private boolean disable;

    public PrecerrarCuentaJB() {
        this.fecha = new Date();
        oPac = new Paciente();
        oEpMed = new EpisodioMedico();
        oUtilidad = new Utilidad();
        nomPacientes = new ArrayList<String>();
        this.serviciosNoPagados = new ArrayList<ServicioPrestado>();
        this.serviciosPagosExterno = new ArrayList<ServicioPrestado>();
        this.serviciosAnticipo = new ArrayList<ServicioPrestado>();
        nomCompanias = new ArrayList<String>();
        oComCred = new CompaniaCred();
        obtienePacientesCtaAbierta();

        this.disable = false;
        this.disableCredito = false;
    }

    private void obtienePacientesCtaAbierta() {
        try {
            pacientes = oPac.buscarPacientesCuentaAbierta();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        for (int i = 0; i < pacientes.length; i++) {
            nomPacientes.add(pacientes[i].getNombreCompleto());
        }
    }

    public void datosEpisodio() {
        this.nomCompanias.clear();
        nomCompanias.add("Empresa o aseguradora");
        for (int i = 0; i < pacientes.length; i++) {
            if ((pacientes[i].getNombreCompleto()).equals(paciente)) {
                try {
                    oPac = pacientes[i];
                    //EpisodioMedico oEM= new EpisodioMedico();
                    oEpMed = oEpMed.datosCierreCuenta(oPac.getFolioPac());
                    oPac = oPac.buscaPaciente();
                    companiasCred = oEpMed.buscaCompaniasCredEpisodioMed(oPac.getFolioPac());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        for (int i = 0; i < companiasCred.length; i++) {
            nomCompanias.add(companiasCred[i].getNombreCorto());
        }
        this.subtotalCom = 0;
        this.subtotalPac = 0;
        this.ivaCom = 0;
        this.ivaPac = 0;
        this.totalCom = 0;
        this.totalCuenta=0;
        this.totalPac = 0;
        this.totalPagos=0;
        this.totaPendientePago=0;
        this.disable = false;
        obtieneServicios();

    }

    public void obtieneServicios() {
        ServicioPrestado oSP = new ServicioPrestado();
        oSP.setEpisodioMedico(oEpMed);
        oSP.setPaciente(oPac);
        try {
            this.serviciosNoPagados = oSP.buscaServiciosPrestadosNoPagadosPorEpisodioMed();
            this.serviciosPagosExterno = oSP.buscaServiciosPrestadosPorEpisodioMedPagosExterno();
            this.serviciosAnticipo = oSP.buscaServiciosPrestadosPorEpisodioMedAnticipos();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        for (int i = 0; i < this.serviciosNoPagados.size(); i++) {
            for (int j = 0; j < companiasCred.length; j++) {
                if (companiasCred[j].getIdEmp() == this.serviciosNoPagados.get(i).getCompaniaCred().getIdEmp()) {
                    this.serviciosNoPagados.get(i).setCompaniaCred(companiasCred[j]);
                }
            }
        }
        calculaCuenta();
    }

    public void calculaCuenta() {
        float preciosiniva = 0;
        for (int i = 0; i < this.serviciosNoPagados.size(); i++) {
            if (this.serviciosNoPagados.get(i).getCompaniaCred().getIdEmp() == 0) {
                preciosiniva = (this.serviciosNoPagados.get(i).getCostoCobrado()*
                        this.serviciosNoPagados.get(i).getCantidad()* 100) / (100 + this.serviciosNoPagados.get(i).getPctIVA());
                subtotalPac += preciosiniva;
                ivaPac += preciosiniva * this.serviciosNoPagados.get(i).getPctIVA() / 100;
                totalPac += this.serviciosNoPagados.get(i).getCostoCobrado()*
                        this.serviciosNoPagados.get(i).getCantidad();
                System.out.println(this.serviciosNoPagados.get(i).getCantidad());
            } else {
                preciosiniva = (this.serviciosNoPagados.get(i).getCostoCobrado()*
                        this.serviciosNoPagados.get(i).getCantidad() * 100) / (100 + this.serviciosNoPagados.get(i).getPctIVA());
                subtotalCom += preciosiniva;
                ivaCom += preciosiniva * this.serviciosNoPagados.get(i).getPctIVA() / 100;
                totalCom += this.serviciosNoPagados.get(i).getCostoCobrado()*
                        this.serviciosNoPagados.get(i).getCantidad();
            }
        }
        for (int i = 0; i < this.serviciosPagosExterno.size(); i++) {
            preciosiniva = (this.serviciosPagosExterno.get(i).getCostoCobrado()*
                        this.serviciosPagosExterno.get(i).getCantidad() * 100) / (100 + this.serviciosPagosExterno.get(i).getPctIVA());
            subtotalPac += preciosiniva;
            ivaPac += preciosiniva * this.serviciosPagosExterno.get(i).getPctIVA() / 100;
            totalPac += this.serviciosPagosExterno.get(i).getCostoCobrado();
            totalPagos += this.serviciosPagosExterno.get(i).getCostoCobrado();
        }
        for (int i = 0; i < this.serviciosAnticipo.size(); i++) {
            totalPagos += this.serviciosAnticipo.get(i).getCostoCobrado()*
                        this.serviciosAnticipo.get(i).getCantidad();
        }
        totalPac = subtotalPac + ivaPac;
        totalCom = subtotalCom + ivaCom;
        totalCuenta = totalPac + totalCom;
        this.totaPendientePago = totalCuenta - totalPagos;
    }

    public void limpiar() {
        this.paciente = "";
        this.oEpMed = new EpisodioMedico();
        this.oPac = new Paciente();
        this.serviciosNoPagados.clear();
        this.nomPacientes.clear();
        obtienePacientesCtaAbierta();
        this.subtotalCom = 0;
        this.subtotalPac = 0;
        this.totalPagos = 0;
        this.ivaCom = 0;
        this.ivaPac = 0;
        this.totalCom = 0;
        this.totalPac = 0;
        this.totalCuenta = 0;
        this.totalPagos = 0;
        this.totaPendientePago = 0;
        this.nomCompanias.clear();
        this.disable = false;
        this.disableCredito = false;
        this.encontrado = false;
        this.interesPagare = 0;

    }

    public void onRowEdit(RowEditEvent event) {
        ServicioPrestado oServP = (ServicioPrestado) event.getObject();
        FacesMessage msg;

        try {
            companiasCred = oEpMed.buscaCompaniasCredEpisodioMed(oPac.getFolioPac());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        for (int i = 0; i < companiasCred.length; i++) {
            if (oServP.getCompaniaCred().getNombreCorto().equals("Empresa o aseguradora")) {
                CompaniaCred cc = new CompaniaCred();
                cc.setNombreCorto(" : ");
                oServP.setCompaniaCred(cc);
                oServP.setNumPoliza(" ");
            }
            if (companiasCred[i].getNombreCorto().equals(oServP.getCompaniaCred().getNombreCorto())) {
                oServP.setCompaniaCred(companiasCred[i]);
            }
        }
        try {
            String msj = oServP.modificarCompaniaCred();
            msj = msj.substring(2, msj.length() - 1);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Compañia actualizada", msj + " " + oServP.getCompaniaCred().getNombreCorto()));
            if (oServP.getNumPoliza().length() > 0) {
                msj = oServP.actualizaNumPoliza();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Número de poliza actualizada", msj));
            }
        } catch (Exception ex) {
            msg = new FacesMessage("Error", "No se pudo actualizar la compañia de credito.");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error", "No se pueden actualizar los datos."));

        }
        this.obtieneServicios();
    }

    public void precerrarCuenta() {
        String msj = "";
        if (verificaPedidosFarmacia()) {
            /*if (this.totaPendientePago>0 && !this.disableCredito)
             FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Hay pagos por realizar" ));
             else{*/
            if (this.oEpMed.getDxEgreso() == null
                      || this.oEpMed.getDxEgreso().getCve() == null
                      || this.oEpMed.getDxEgreso().getCve().startsWith("9999")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Falta diagnóstico de egreso"));
            } else if (this.oEpMed.getTipoAlta() == null
                      || this.oEpMed.getTipoAlta().equals("")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Falta tipo de alta"));
            } else {
                try {
                    System.out.println("Diag Egre " + this.oEpMed.getDxEgreso().getCve());
                    this.oEpMed.getDxEgreso().setCve("A000");
                    msj = this.oEpMed.precerrarCuenta(this.oPac.getFolioPac());
                    msj = msj.substring(2, msj.length() - 1);
                    if (msj.contains("ERROR")) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Error en base de datos, no se puede precerrar cuenta"));
                        System.out.println(msj);
                    } else {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Cuenta Precerrada", msj + ". No se permiten más cambios."));
                        this.disable = true;
                    }
                } catch (Exception ex) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se puede precerrar cuenta"));
                    ex.printStackTrace();
                }
            }
            //}
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cerrar cuenta", "No se puede cerrar cuenta, se encuentran pedidos enviados a farmacia."));
        }
    }

    public boolean verificaPedidosFarmacia() {
        boolean cerrar = true;
        for (int i = 0; i < this.serviciosNoPagados.size(); i++) {
            if (serviciosNoPagados.get(i).getSitFarmacia().equals("E") || serviciosNoPagados.get(i).getSitFarmacia().equals("e")) {
                cerrar = false;
            }
        }
        for (int i = 0; i < this.serviciosPagosExterno.size(); i++) {
            if (serviciosPagosExterno.get(i).getSitFarmacia().equals("E") || serviciosPagosExterno.get(i).getSitFarmacia().equals("e")) {
                cerrar = false;
            }
        }
        return cerrar;
    }

    public void setPacienteSesion() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().
                  getSession(false);
        session.setAttribute("oPacienteSeleccionado", oPac);
        session.setAttribute("nTotalCuenta", totaPendientePago);
        session.setAttribute("oEpisodioMed", oEpMed);
    }

    public Paciente getPacienteSesion() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
        return (Paciente) session.getAttribute("oPacienteSeleccionado");
    }

    public float getTotalCuentaSesion() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
        float x = ((Double) session.getAttribute("nTotalCuenta")).floatValue();
        if (x == 0) {
            return x;
        } else {
            return 0;
        }
    }

    public EpisodioMedico getEpisodioMedicoSesion() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
        return (EpisodioMedico) session.getAttribute("oEpisodioMed");
    }

    public void llena() {
        System.out.println("Tipo alta" + this.oEpMed.getTipoAlta());
    }

    public void autorizaSalidaCredito() {
        String msj = "";

        try {
            oUtilidad.setVariable("CveAutSalCred");
            oUtilidad.setValor(autorizacion);
            encontrado = oUtilidad.buscaCveAutSalidadCredito();
            if (encontrado) {
                System.out.println("Total: " + totaPendientePago);
                NumberToLetterConverter conNum = new NumberToLetterConverter();
                DecimalFormat df = new DecimalFormat("#.##");
                String na = df.format(totaPendientePago);
                System.out.println("Numero:" + na);
                System.out.println("L: " + na + " - " + conNum.convertNumberToLetter(na));

                this.numLetra = conNum.convertNumberToLetter(na);

                DocsPDFJB oDPDF = new DocsPDFJB();
                SimpleDateFormat formatoDelTexto = new SimpleDateFormat("dd-MM-yyyy");
                this.rutaPDFpagare = oDPDF.creaPagare(this.oEpMed.getResponsableCuenta(), Float.parseFloat(na), this.numLetra, this.interesPagare, formatoDelTexto.format(this.fechaPagare), this.oEpMed.getCveepisodio());
                this.oEpMed.setAutorizaSalidaCred(autorizacion);
                msj = this.oEpMed.autorizarSalidaCredito(this.oPac.getFolioPac());
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Crédito autorizado", "Autorización registrada " + " "));
            } else {
                this.disableCredito = false;
                this.fechaPagare = null;
                this.interesPagare = 0f;
                this.autorizacion = "";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Salida con crédito", "No se puede autorizar."));
            }
        } catch (Exception ex) {
            this.disableCredito = false;
            this.fechaPagare = null;
            this.interesPagare = 0f;
            this.autorizacion = "";
            ex.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Salida con crédito", "No se puede actualizar"));
        }
    }

    public void buscaPolizas(String EmpCC) {
        System.out.println("EpMed: " + this.oEpMed.getCveepisodio() + " Pac: " + this.oPac.getFolioPac() + " Cia.Cred:" + EmpCC);
        if (!EmpCC.equals("Empresa o aseguradora")) {
            try {
                int idCiaCred = 0;
                for (int i = 0; i < this.companiasCred.length; i++) {
                    if (companiasCred[i].getNombreCorto().equals(EmpCC)) {
                        idCiaCred = companiasCred[i].getIdEmp();
                    }
                }
                polizas = this.oEpMed.buscaNumPolizasPorCiaCred(this.oPac.getFolioPac(), idCiaCred);
                for (int i = 0; i < polizas.size(); i++) {
                    System.out.println("pol:" + polizas.get(i));
                }
            } catch (Exception ex) {
                Logger.getLogger(PrecerrarCuentaJB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    //=============== SET & GET ===============//
    public boolean isDisableCredito() {
        return disableCredito;
    }

    public void setDisableCredito(boolean disableCredit) {
        this.disableCredito = disableCredit;
    }

    public boolean isDisable() {
        return disable;
    }

    public void setDisable(boolean disable) {
        this.disable = disable;
    }

    public List<String> getPacientes() {
        return nomPacientes;
    }

    public Date getFecha() {
        return this.fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Paciente getPaciente() {
        return oPac;
    }

    public void setPaciente(Paciente oPac) {
        this.oPac = oPac;
    }

    public EpisodioMedico getEpisodioMedico() {
        return oEpMed;
    }

    public void setEpisodioMedico(EpisodioMedico oEpMed) {
        this.oEpMed = oEpMed;
    }

    public List<String> getNomPacientes() {
        return nomPacientes;
    }

    public void setNomPacientes(ArrayList<String> nomPacientes) {
        this.nomPacientes = nomPacientes;
    }

    public List<ServicioPrestado> getServiciosNoPagados() {
        return serviciosNoPagados;
    }

    public void setServiciosNoPagados(List<ServicioPrestado> serviciosNoPagados) {
        this.serviciosNoPagados = serviciosNoPagados;
    }

    public List<ServicioPrestado> getServiciosPagosExterno() {
        return serviciosPagosExterno;
    }

    public void setServiciosPagosExterno(List<ServicioPrestado> serviciosPagosExterno) {
        this.serviciosPagosExterno = serviciosPagosExterno;
    }

    public List<ServicioPrestado> getServiciosAnticipo() {
        return serviciosAnticipo;
    }

    public void setServiciosAnticipo(List<ServicioPrestado> serviciosAnticipo) {
        this.serviciosAnticipo = serviciosAnticipo;
    }

    public String getNombrePaciente() {
        return paciente;
    }

    public void setNombrePaciente(String nompaciente) {
        this.paciente = nompaciente;
    }

    public List<String> getNomCompaniasCred() {
        return nomCompanias;
    }

    public void setNomCompaniasCred(ArrayList<String> nomCompanias) {
        this.nomPacientes = nomCompanias;
    }

    public CompaniaCred getCompaniaCredito() {
        return oComCred;
    }

    public void setCompaniaCredito(CompaniaCred oComCred) {
        this.oComCred = oComCred;
    }

    //-_-_-_-_-_-_-_-_ cuenta compania _-_-_-_-_-_-_-_-//
    public float getSubtotalCompania() {
        return this.subtotalCom;
    }

    public void setSubtotalCompania(float subtotal) {
        this.subtotalCom = subtotal;
    }

    public float getIvaCompania() {
        return this.ivaCom;
    }

    public void setIvaCompania(float iva) {
        this.ivaCom = iva;
    }

    public float getTotalCompania() {
        return this.totalCom;
    }

    public void setTotalCompania(float total) {
        this.totalCom = total;
    }

    //-_-_-_-_-_-_-_-_ cuenta paciente _-_-_-_-_-_-_-_-//
    public float getSubtotalPaciente() {
        return this.subtotalPac;
    }

    public void setSubtotalPaciente(float subtotal) {
        this.subtotalPac = subtotal;
    }

    public float getIvaPaciente() {
        return this.ivaPac;
    }

    public void setIvaPaciente(float iva) {
        this.ivaPac = iva;
    }

    public float getTotalPaciente() {
        return this.totalPac;
    }

    public void setTotalPaciente(float total) {
        this.totalPac = total;
    }

    public float getTotalCuenta() {
        return totalCuenta;
    }

    public void setTotalCuenta(float totalCuenta) {
        this.totalCuenta = totalCuenta;
    }

    public float getTotalPagos() {
        return totalPagos;
    }

    public void setTotalPagos(float totalPagos) {
        this.totalPagos = totalPagos;
    }

    public float getTotaPendientePago() {
        return totaPendientePago;
    }

    public void setTotaPendientePago(float totaPendientePago) {
        this.totaPendientePago = totaPendientePago;
    }

    public Utilidad getUtilidad() {
        return oUtilidad;
    }

    public void setUtilidad(Utilidad oUtilidad) {
        this.oUtilidad = oUtilidad;
    }

    public boolean isEncontrado() {
        return encontrado;
    }

    public void setEncontrado(boolean encontrado) {
        this.encontrado = encontrado;
    }

    public String getNumLetra() {
        return numLetra;
    }

    public void setNumLetra(String numLetra) {
        this.numLetra = numLetra;
    }

    public String getRutaPDFpagare() {
        return rutaPDFpagare;
    }

    public void setRutaPDFpagare(String rutaPDFpagare) {
        this.rutaPDFpagare = rutaPDFpagare;
    }

    public Date getFechaPagare() {
        return fechaPagare;
    }

    public void setFechaPagare(Date fechaPagare) {
        this.fechaPagare = fechaPagare;
    }

    public float getInteresPagare() {
        return interesPagare;
    }

    public void setInteresPagare(float interesPagare) {
        this.interesPagare = interesPagare;
    }

    public List<String> getPolizas() {
        return polizas;
    }

    public void setPolizas(List<String> polizas) {
        this.polizas = polizas;
    }

    /*Indica si se autorizó o no el crédito*/
    public String getAutorizado() {
        return this.fechaPagare != null ? "--AUTORIZADO PARA SALIDA A CRÉDITO--" : "";
    }

    /**
     * @return the autorizacion
     */
    public String getAutorizacion() {
        return autorizacion;
    }

    /**
     * @param autorizacion the autorizacion to set
     */
    public void setAutorizacion(String autorizacion) {
        this.autorizacion = autorizacion;
    }

    public List<Diagnostico> getDiagnosticos() {
        return ListaDiagnosticoJB.diagnosticos;
    }

    public void setDiagnosticos(List diagnosticos) {
        ListaDiagnosticoJB.diagnosticos = diagnosticos;
    }
}
