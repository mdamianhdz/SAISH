package org.apli.jbs.pagoRegalias;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import org.apli.modelbeans.DistribucionRegalias;
import org.apli.modelbeans.Medico;
import org.apli.modelbeans.PagoRegalia;
import org.apli.modelbeans.ServicioPrestado;

/**
 * AutRegaliasMedicosJB.java 
 * JSF Managed Bean archivo que realiza la autorizacion del pago de regalias para medicos de guardia
 *
 * @author Humberto Marin Vega Fecha: Julio 2014
 */
@ManagedBean(name = "rptRGuardia")
@ViewScoped

public class rptRegaliasMedGuardiaJB implements Serializable {

    private Date fechaIniP;
    private Date fechaFinP;

    private String mes;
    private int anio;

    private Medico oMed;
    private PagoRegalia selectedPagoRegalias;
    private Medico[] medicos;
    private ServicioPrestado oSP;
    private List<ServicioPrestado> serviciosMed;
    private List<PagoRegalia> regaliasPorMedico;
    private List<ServicioPrestado> selectedServiciosReg;
    private PagoRegalia regalia;

    private boolean disable;

    private int[] anios = {2014, 2015, 2016, 2017, 2018, 2019, 2020};
    private String[] meses = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
    private String periodo = "";
    
    private float totalImporte;
    private float totalRegalias;
    private float totalAut;
    
    private List<DistribucionRegalias> distribucion;
    
    public rptRegaliasMedGuardiaJB() {
        this.disable = true;
        oMed = new Medico();
        oSP = new ServicioPrestado();
        regaliasPorMedico = new ArrayList<PagoRegalia>();
        distribucion = new ArrayList<DistribucionRegalias>();
        regalia = new PagoRegalia();
    }

    public void obtieneMedicos() {
        if (disable) {
            try {
                String s1, s2;
                int dias = diasDelMes(mes, anio);
                s1 = anio + "-" + numMes(mes) + "-" + 01;
                s2 = anio + "-" + numMes(mes) + "-" + dias;
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                this.fechaIniP = simpleDateFormat.parse(s1);
                this.fechaFinP = simpleDateFormat.parse(s2);
            } catch (ParseException ex) {
                Logger.getLogger(rptRegaliasMedGuardiaJB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println("Del: " + fechaIniP + " al " + fechaFinP);
        try {
            medicos = oMed.buscarTodosMedicosGuardiaConRegalias(fechaIniP, fechaFinP);
            if(medicos.length==0){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Autorización de regalías a médicos", "No se encuentran médicos con regalías."));    
            }
            System.out.println("medicos " + medicos.length);
        } catch (Exception ex) {
            Logger.getLogger(rptRegaliasMedGuardiaJB.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
        obtieneRegaliasMedicos();
    }

    public void obtieneRegaliasMedicos() {
        this.regaliasPorMedico.clear();
        float cuenta = 0;
        for (int i = 0; i < medicos.length; i++) {
            try {
                oSP.setMedico(medicos[i]);
                this.serviciosMed = oSP.buscaServiciosPrestadosRPTRegaliasMedicoGuardia(fechaIniP, fechaFinP);

                for (int j = 0; j < serviciosMed.size(); j++) {
                    cuenta += serviciosMed.get(j).getCostoCobrado() * (serviciosMed.get(j).getConcepPrestado().getPctRegalGuard() / 100);
                   // System.out.println("CEgr: "+serviciosMed.get(j).getConcepPrestado().buscaCveConcepEgrMedExt());
                }
                
                System.out.println("Med: " + oSP.getMedico().getFolioPers() + " - " + cuenta);
                PagoRegalia prTmp = new PagoRegalia();
                prTmp.setPersonalHospitalario(medicos[i]);
                prTmp.setMontoTotal(cuenta);
                prTmp.setFechaInicio(fechaIniP);
                prTmp.setFechaFin(fechaFinP);
                this.regaliasPorMedico.add(prTmp);
                cuenta=0;
            } catch (Exception ex) {
                Logger.getLogger(AutPagoRegMedicosExternoJB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void regaliasAlMedico() {
        try {
            totalImporte=0;
            totalRegalias=0;
            oSP.setMedico((Medico) this.selectedPagoRegalias.getPersonalHospitalario());
            System.out.println("NomCompSel: "+oSP.getMedico().getNombreCompleto());
            serviciosMed = oSP.buscaServiciosPrestadosRPTRegaliasMedicoGuardia(fechaIniP, fechaFinP);
            System.out.println("serviciosMed.size()+"+serviciosMed.size());
            for (int i = 0; i < this.serviciosMed.size(); i++) {
                System.out.println("Nombre"+serviciosMed.get(i).getIdFolio()+"- "+serviciosMed.get(i).getPaciente().getNombreCompleto());
                totalImporte+=serviciosMed.get(i).getCostoCobrado();
                totalRegalias+=serviciosMed.get(i).getCostoCobrado()*(serviciosMed.get(i).getConcepPrestado().getPctRegalGuard()/100);
            }
        } catch (Exception ex) {
            Logger.getLogger(AutPagoRegMedicosExternoJB.class.getName()).log(Level.SEVERE, null, ex);
        }   
        this.cambiaSituacion();
    }
    
    
    public void calculaTotalAutorizado(){
        totalAut=0;
        for(int i=0; i<this.selectedServiciosReg.size(); i++){
            totalAut+=this.selectedServiciosReg.get(i).getCostoCobrado()*(this.selectedServiciosReg.get(i).getConcepPrestado().getPctRegalGuard()/100);
        }
        System.out.println("Total autorizado: "+totalAut);
    }
    
    public void cambiaSituacion(){
        for(int i=0; i<this.serviciosMed.size(); i++){
            if(serviciosMed.get(i).getSituacion().equals("P")){
                serviciosMed.get(i).setSituacion("Pagado");
            }else if(serviciosMed.get(i).getSituacion().equals("N")){
                serviciosMed.get(i).setSituacion("Nuevo");
            }else if(serviciosMed.get(i).getSituacion().equals("A")){
                serviciosMed.get(i).setSituacion("Autorizado a crédito");
            }else if(serviciosMed.get(i).getSituacion().equals("O")){
                serviciosMed.get(i).setSituacion("Autorizado a paquete");
            }
        }
    }
            

    //------------------------------------------------------------------------------------------->>
    //------------------------------------------------------------------------------------------->>
    public void setSelectedPagoRegalias(PagoRegalia selectPagoRegalias) {
        this.selectedPagoRegalias = selectPagoRegalias;
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().
                getSession(false);
        session.setAttribute("oPagoRegaliasSeleccionado", this.selectedPagoRegalias);
    }

    public PagoRegalia getPagoRegaliasSesion() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
        return (PagoRegalia) session.getAttribute("oPagoRegaliasSeleccionado");
    }

    public void llena() {
        this.selectedPagoRegalias = new rptRegaliasMedGuardiaJB().getPagoRegaliasSesion();
        if(this.fechaIniP!=null && this.fechaFinP!=null){
            this.regaliasAlMedico();
        }
    }

    public int numMes(String mess) {
        int mes1 = 0;
        for (int i = 0; i < meses.length; i++) {
            if (mess.equals(meses[i])) {
                mes1 = i + 1;
            }
        }
        return mes1;
    }

    public int diasDelMes(String mess, int año) {
        int nmes = this.numMes(mess);
        System.out.println(mess + "-" + nmes);
        switch (nmes - 1) {
            case 0:  // Enero
            case 2:  // Marzo
            case 4:  // Mayo
            case 6:  // Julio
            case 7:  // Agosto
            case 9:  // Octubre
            case 11: // Diciembre
                return 31;
            case 3:  // Abril
            case 5:  // Junio
            case 8:  // Septiembre
            case 10: // Noviembre
                return 30;
            case 1:  // Febrero
                if (((año % 100 == 0) && (año % 400 == 0))
                        || ((año % 100 != 0) && (año % 4 == 0))) {
                    return 29;  // Año Bisiesto
                } else {
                    return 28;
                }
            default:
                throw new java.lang.IllegalArgumentException(
                        "El mes debe estar entre 0 y 11");
        }
    }

    public void limpiar() {
        this.disable = true;
        this.fechaIniP = null;
        this.fechaFinP = null;
        this.mes = "";
        this.anio = 0;
        this.oMed = new Medico();
        this.selectedPagoRegalias = new PagoRegalia();
        this.medicos = new Medico[0];
        this.regaliasPorMedico.clear();
        this.distribucion.clear();
        this.totalImporte=0;
        this.totalRegalias=0;
        this.totalAut=0;
    }

    //=============== SET & GET ===============//
    public Date getFechaInicio() {
        return fechaIniP;
    }
    public void setFechaInicio(Date fechaIniP) {
        this.fechaIniP = fechaIniP;
    }

    public Date getFechaFin() {
        return fechaFinP;
    }
    public void setFechaFin(Date fechaFinP) {
        this.fechaFinP = fechaFinP;
    }

    public String getMes() {
        return mes;
    }
    public void setMes(String mes) {
        this.mes = mes;
    }

    public int getAnio() {
        return anio;
    }
    public void setAnio(int anio) {
        this.anio = anio;
    }

    public Medico getMedico() {
        return oMed;
    }
    public void setMedico(Medico medico) {
        this.oMed = medico;
    }

    public Medico[] getMedicos() {
        return medicos;
    }
    public void setMedicos(Medico[] medicos) {
        this.medicos = medicos;
    }

    public boolean isDisable() {
        return disable;
    }
    public void setDisable(boolean disable) {
        this.disable = disable;
    }

    public int[] getAnios() {
        return anios;
    }
    public void setAnios(int[] anios) {
        this.anios = anios;
    }

    public String[] getMeses() {
        return meses;
    }
    public void setMeses(String[] meses) {
        this.meses = meses;
    }

    public String getPeriodo() {
        return periodo;
    }
    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public List<PagoRegalia> getRegaliasPorMedico() {
        return regaliasPorMedico;
    }
    public void setCuentaRegalias(List<PagoRegalia> regaliasxMedico) {
        this.regaliasPorMedico = regaliasxMedico;
    }
    
    public PagoRegalia getSelectedPagoRegalias() {
        return selectedPagoRegalias;
    }
    
    public List<ServicioPrestado> getServiciosDelMedico() {
        return serviciosMed;
    }
    public void setServiciosDelMedico(List<ServicioPrestado> serviciosMed) {
        this.serviciosMed = serviciosMed;
    }

    public List<ServicioPrestado> getSelectedServiciosReg() {
        return selectedServiciosReg;
    }
    public void setSelectedServiciosReg(List<ServicioPrestado> selectedServiciosReg) {
        System.out.println("entro"+selectedServiciosReg.size());
        this.selectedServiciosReg = selectedServiciosReg;
    }

    public PagoRegalia getRegalia() {
        return regalia;
    }
    public void setRegalia(PagoRegalia regalia) {
        this.regalia = regalia;
    }

    public float getTotalImporte() {
        return totalImporte;
    }
    public void setTotalImporte(float totalImporte) {
        this.totalImporte = totalImporte;
    }

    public float getTotalRegalias() {
        return totalRegalias;
    }
    public void setTotalRegalias(float totalRegalias) {
        this.totalRegalias = totalRegalias;
    }

    public List<DistribucionRegalias> getDistribucion() {
        return distribucion;
    }
    public void setDistribucion(List<DistribucionRegalias> distribucion) {
        this.distribucion = distribucion;
    }

    public float getTotalAut() {
        return totalAut;
    }
    public void setTotalAut(float totalAut) {
        this.totalAut = totalAut;
    }
    
}