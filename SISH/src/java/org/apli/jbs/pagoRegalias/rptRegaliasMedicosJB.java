package org.apli.jbs.pagoRegalias;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.apli.modelbeans.ConceptoIngreso;
import org.apli.modelbeans.DistribucionRegalias;
import org.apli.modelbeans.Medico;
import org.apli.modelbeans.PagoRegalia;
import org.apli.modelbeans.ServicioPrestado;

/**
 * AutRegaliasMedicosJB.java 
 * JSF Managed Bean archivo que realiza la autorizacion del pago de regalias para medicos
 *
 * @author Humberto Marin Vega Fecha: Julio 2014
 */
@ManagedBean(name = "rptRegMed")
@ViewScoped

public class rptRegaliasMedicosJB implements Serializable {
    private Date fechaIniP;
    private Date fechaFinP;

    private Medico oMed;
    private PagoRegalia selectedPagoRegalias;
    private Medico[] medicos;
    private ServicioPrestado oSP;
    private List<ServicioPrestado> serviciosMed;
    private List<PagoRegalia> regaliasPorMedico;
    private List<ServicioPrestado> selectedServiciosReg;
    private PagoRegalia regalia;

    private boolean disable;
    
    private float totalImporte;
    private float totalRegalias;
    private float totalTotales;
    private float totalHospital;
    private float totalExterno;
    
    private List<DistribucionRegalias> distribucion;
    private List<ConceptoIngreso> conceptos;
        

    public rptRegaliasMedicosJB() {
        this.disable = true;
        oMed = new Medico();
        oSP = new ServicioPrestado();
        regaliasPorMedico = new ArrayList<PagoRegalia>();
        distribucion = new ArrayList<DistribucionRegalias>();
        conceptos = new ArrayList<ConceptoIngreso>();
        regalia = new PagoRegalia();
    }  
    
    public void validaFecha(){
        String mess="";
        if (fechaIniP==null)
            mess="No ha especificado la fecha de inicio";
        else
            if (fechaFinP!=null)
                if (fechaIniP.compareTo(fechaFinP)>0)
                    mess="La fecha final del periodo debe ser posterior a la fecha de inicio";
        if (!mess.equals("")){
            FacesContext context= FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Pagos extras a enfermeras",mess));
        }
    }

    public void obtieneMedicos() {
        try {
            medicos = oMed.buscarTodosMedicosConRegalias(fechaIniP, fechaFinP);
            System.out.println("medicos " + medicos.length);
        } catch (Exception ex) {
            Logger.getLogger(rptRegaliasMedicosJB.class.getName()).log(Level.SEVERE, null, ex);
        }
        obtieneRegaliasMedicos();
    }

    public void obtieneRegaliasMedicos() {
    float cuenta = 0;
    String sSit = "";
        this.regaliasPorMedico.clear();
        for (int i = 0; i < medicos.length; i++) {
            try {
                oSP.setMedico(medicos[i]);
                this.serviciosMed = oSP.buscaServiciosPrestadosRPTRegaliasMedico(fechaIniP, fechaFinP);

                for (int j = 0; j < serviciosMed.size(); j++) {
                    cuenta += serviciosMed.get(j).getCostoCobrado();
                    sSit = serviciosMed.get(j).getSituacion();
                }
                
                oSP.setCostoOriginal(cuenta);
                PagoRegalia prTmp = new PagoRegalia();
                prTmp.setPersonalHospitalario(medicos[i]);
                prTmp.setMontoTotal(cuenta);
                prTmp.setFechaInicio(fechaIniP);
                prTmp.setFechaFin(fechaFinP);
                prTmp.setSituacion(sSit);
                this.regaliasPorMedico.add(prTmp);
                cuenta=0;
                oSP= new ServicioPrestado();
            } catch (Exception ex) {
                Logger.getLogger(rptRegaliasMedicosJB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        this.serviciosMed.clear();
    }

    public void regaliasAlMedico() {
        try {
            totalImporte=0;
            totalRegalias=0;
            oSP.setMedico((Medico) this.selectedPagoRegalias.getPersonalHospitalario());
            serviciosMed = oSP.buscaServiciosPrestadosRPTRegaliasMedico(fechaIniP, fechaFinP);
            for (int i = 0; i < this.serviciosMed.size(); i++) {
                //En costo original quedó lo cobrado y en costo cobrado la regalía
                totalImporte+=serviciosMed.get(i).getCostoOriginal()*
                        serviciosMed.get(i).getCantidad();
                totalRegalias+=serviciosMed.get(i).getCostoCobrado();
            }
        } catch (Exception ex) {
            Logger.getLogger(rptRegaliasMedicosJB.class.getName()).log(Level.SEVERE, null, ex);
        }    
        this.obtieneConceptos();
    }
    
    public void obtieneConceptos(){
        conceptos.clear();
        List<Integer> cveCon= new ArrayList<Integer>();
        for(int i=0; i<this.serviciosMed.size(); i++){
            cveCon.add(serviciosMed.get(i).getConcepPrestado().getCveConcep());
        }
        HashSet hs = new HashSet();
        hs.addAll(cveCon);
        cveCon.clear();
        cveCon.addAll(hs);
        for(int i=0; i<cveCon.size(); i++){
            for(int j=0; j<serviciosMed.size(); j++){
                if(cveCon.get(i)==serviciosMed.get(j).getConcepPrestado().getCveConcep()){
                    conceptos.add(serviciosMed.get(j).getConcepPrestado());
                    break;
                }
            }
        }
        this.obtieneTotales();
    }
    
    public void obtieneTotales(){
        this.distribucion.clear();
        float ttlRegalias=0;
        int c=0;
        float totalServHosp=0;
        float totalServExt=0;
        String tipo;
        for(int i=0; i<conceptos.size(); i++){
            for(int j=0; j<serviciosMed.size(); j++){
                if(conceptos.get(i).getCveConcep()==serviciosMed.get(j).getConcepPrestado().getCveConcep()){
                    c++;
                    if(serviciosMed.get(j).getIdFolio().startsWith("H"))
                        totalServHosp+=serviciosMed.get(j).getCostoCobrado();
                    else
                        totalServExt+=serviciosMed.get(j).getCostoCobrado();
                    ttlRegalias+=serviciosMed.get(j).getCostoCobrado();
                }
            }
            DistribucionRegalias tmp= new DistribucionRegalias();
            List<ConceptoIngreso> tmp_ci= new ArrayList<ConceptoIngreso>();
            tmp_ci.add(conceptos.get(i));
            tmp.setConceptos(tmp_ci);
            List<Float> numeros= new ArrayList<Float>();
            numeros.add(totalServHosp);
            numeros.add(totalServExt);
            tmp.setNumConceptos(numeros);
            tmp.setImporteRegalia(ttlRegalias);
            distribucion.add(tmp);
            c=0;
            ttlRegalias=0;
            totalServHosp=0;
            totalServExt=0;
        }
        System.out.println("DISTRIBUCION");
        totalTotales=0;
        totalHospital=0;
        totalExterno=0;
        for(int i=0; i<distribucion.size(); i++){
            totalHospital+=distribucion.get(i).getNumConceptos().get(0);
            totalExterno+=distribucion.get(i).getNumConceptos().get(1);
            totalTotales+=distribucion.get(i).getImporteRegalia();
        }
    }
    
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
        this.selectedPagoRegalias = new rptRegaliasMedicosJB().getPagoRegaliasSesion();
        if(this.fechaIniP!=null && this.fechaFinP!=null){
            this.regaliasAlMedico();
        }
    }
    
    public void limpiar() {
        this.disable = true;
        this.fechaIniP = null;
        this.fechaFinP = null;
        this.oMed = new Medico();
        this.selectedPagoRegalias = new PagoRegalia();
        this.medicos = new Medico[0];
        this.regaliasPorMedico.clear();
        this.distribucion.clear();
        
        this.totalImporte=0;
        this.totalRegalias=0;
        this.totalExterno=0;
        this.totalHospital=0;
        this.totalTotales=0;
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

    public float getTotalTotales() {
        return totalTotales;
    }
    public void setTotalTotales(float totalTotales) {
        this.totalTotales = totalTotales;
    }

    public float getTotalHospital() {
        return totalHospital;
    }
    public void setTotalHospital(float totalHospital) {
        this.totalHospital = totalHospital;
    }

    public float getTotalExterno() {
        return totalExterno;
    }
    public void setTotalExterno(float totalExterno) {
        this.totalExterno = totalExterno;
    }
    
}