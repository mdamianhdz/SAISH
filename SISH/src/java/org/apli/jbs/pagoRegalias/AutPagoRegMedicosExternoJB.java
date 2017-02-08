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
import org.apli.modelbeans.ConceptoEgreso;
import org.apli.modelbeans.ConceptoIngreso;
import org.apli.modelbeans.DetalleRegalias;
import org.apli.modelbeans.DistribucionRegalias;
import org.apli.modelbeans.Medico;
import org.apli.modelbeans.Paciente;
import org.apli.modelbeans.PagoRegalia;
import org.apli.modelbeans.ServicioPrestado;

/**
 * AutRegaliasMedicosJB.java JSF Managed Bean archivo que realiza la autorizacion del pago de regalias para medicos
 *
 * @author Humberto Marin Vega Fecha: Julio 2014
 */
@ManagedBean(name = "oRegMed")
@ViewScoped
public class AutPagoRegMedicosExternoJB implements Serializable {
    private Date fechaIniP;
    private Date fechaFinP;

    private Medico oMed;
    private PagoRegalia selectedPagoRegalias;
    private Medico[] medicos;
    private ServicioPrestado oSP;
    private List<ServicioPrestado> serviciosMed;
    private List<ServicioPrestado> serviciosLab;
    private List<PagoRegalia> regaliasPorMedico;
    private List<ServicioPrestado> selectedServiciosReg;
    private PagoRegalia regalia;
    
    private float totalImporte;
    private float totalRegalias;
    private float totalTotales;
    private float totalHospital;
    private float totalExterno;
    
    private List<DistribucionRegalias> distribucion;
    private List<ConceptoIngreso> conceptos;
        

    public AutPagoRegMedicosExternoJB() {
        oMed = new Medico();
        oSP = new ServicioPrestado();
        regaliasPorMedico = new ArrayList<PagoRegalia>();
        distribucion = new ArrayList<DistribucionRegalias>();
        conceptos = new ArrayList<ConceptoIngreso>();
        regalia = new PagoRegalia();
        this.serviciosMed = new ArrayList<ServicioPrestado>();
        this.serviciosLab =  new ArrayList<ServicioPrestado>();
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
            if(medicos.length==0){
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Autorización de regalías a médicos", "No se encuentran médicos con regalías."));    
            }
        } catch (Exception ex) {
            Logger.getLogger(AutPagoRegMedicosExternoJB.class.getName()).log(Level.SEVERE, null, ex);
        }
        obtieneRegaliasMedicos();
    }

    public void obtieneRegaliasMedicos() {
        this.regaliasPorMedico.clear();
        float cuenta = 0;
        for (int i = 0; i < medicos.length; i++) {
            try {
                oSP.setMedico(medicos[i]);
                this.serviciosMed = oSP.buscaServiciosPrestadosRegaliasMedico(fechaIniP, fechaFinP);
                this.serviciosLab = oSP.buscaServiciosPrestadosRegaliasLaboratorioMedicoExt(fechaIniP, fechaFinP);

                for(int im=0; im < serviciosMed.size(); im++){
                    if(serviciosMed.get(im).getConcepPrestado().getPctRegalMed()==0)
                        serviciosMed.get(im).getConcepPrestado().setPctRegalMed(10);
                }
                for(int il=0; il < serviciosLab.size(); il++){
                    if(serviciosLab.get(il).getConcepPrestado().getPctRegalMed()==0)
                        serviciosLab.get(il).getConcepPrestado().setPctRegalMed(15);
                }
            
                for (int j = 0; j < serviciosMed.size(); j++) {
                    cuenta += serviciosMed.get(j).getCostoCobrado() * 
                            serviciosMed.get(j).getCantidad() * 
                            (serviciosMed.get(j).getConcepPrestado().getPctRegalMed() / 100);
                    System.out.println("CEgr: "+serviciosMed.get(j).getConcepPrestado().buscaCveConcepEgrMedExt());
                }
                for (int k=0; k < serviciosLab.size(); k++){
                    cuenta += serviciosLab.get(k).getCostoCobrado() * 
                            serviciosMed.get(k).getCantidad() * 
                            (serviciosLab.get(k).getConcepPrestado().getPctRegalMed() / 100);
                }
                
                oSP.setCostoOriginal(cuenta);
                PagoRegalia prTmp = new PagoRegalia();
                prTmp.setPersonalHospitalario(medicos[i]);
                prTmp.setMontoTotal(cuenta);
                prTmp.setFechaInicio(fechaIniP);
                prTmp.setFechaFin(fechaFinP);
                this.regaliasPorMedico.add(prTmp);
                cuenta=0;
                oSP= new ServicioPrestado();
            } catch (Exception ex) {
                Logger.getLogger(AutPagoRegMedicosExternoJB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        for (int j = 0; j < this.regaliasPorMedico.size() ; j++) {
           System.out.println("Regalias :" + regaliasPorMedico.get(j).getPersonalHospitalario().getNombreCompleto() + " - " + regaliasPorMedico.get(j).getMontoTotal());
        }
        this.serviciosMed.clear();
    }

    public void regaliasAlMedico() {
        try {
            totalImporte=0;
            totalRegalias=0;
            oSP.setMedico((Medico) this.selectedPagoRegalias.getPersonalHospitalario());
            serviciosMed = oSP.buscaServiciosPrestadosRegaliasMedico(fechaIniP, fechaFinP);
            serviciosLab = oSP.buscaServiciosPrestadosRegaliasLaboratorioMedicoExt(fechaIniP, fechaFinP);
            
            for(int i=0; i < serviciosMed.size(); i++){
                if(serviciosMed.get(i).getConcepPrestado().getPctRegalMed()==0)
                    serviciosMed.get(i).getConcepPrestado().setPctRegalMed(10);
            }
            for(int i=0; i < serviciosLab.size(); i++){
                if(serviciosLab.get(i).getConcepPrestado().getPctRegalMed()==0)
                    serviciosLab.get(i).getConcepPrestado().setPctRegalMed(15);
            }
            
            for(int i=0; i < serviciosLab.size(); i++){
                serviciosMed.add(serviciosLab.get(i));
            }
            
            for (int i = 0; i < this.serviciosMed.size(); i++) {
                totalImporte+=serviciosMed.get(i).getCostoCobrado()*
                        serviciosMed.get(i).getCantidad();
                totalRegalias+=serviciosMed.get(i).getCostoCobrado()*
                        serviciosMed.get(i).getCantidad()*
                        (serviciosMed.get(i).getConcepPrestado().getPctRegalMed()/100);
            }
        } catch (Exception ex) {
            Logger.getLogger(AutPagoRegMedicosExternoJB.class.getName()).log(Level.SEVERE, null, ex);
        }    
    }
    
    public void obtieneConceptos(){
        conceptos.clear();
        List<Integer> cveCon= new ArrayList<Integer>();
        for(int i=0; i<this.selectedServiciosReg.size(); i++){
            cveCon.add(selectedServiciosReg.get(i).getConcepPrestado().getCveConcep());
        }
        HashSet hs = new HashSet();
        hs.addAll(cveCon);
        cveCon.clear();
        cveCon.addAll(hs);
        for(int i=0; i<cveCon.size(); i++){
            System.out.println(cveCon.get(i));
            for(int j=0; j<selectedServiciosReg.size(); j++){
                if(cveCon.get(i)==selectedServiciosReg.get(j).getConcepPrestado().getCveConcep()){
                    conceptos.add(selectedServiciosReg.get(j).getConcepPrestado());
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
        float totalPSH=0;
        float totalPNSH=0;
        String tipo;
        for(int i=0; i<conceptos.size(); i++){
            for(int j=0; j<selectedServiciosReg.size(); j++){
                if(conceptos.get(i).getCveConcep()==selectedServiciosReg.get(j).getConcepPrestado().getCveConcep()){
                    c++;
                    tipo=this.buscarPacientePSH(selectedServiciosReg.get(j).getPaciente().getFolioPac());
                    if(tipo.equals("PSH "))
                        totalPSH+=selectedServiciosReg.get(j).getCostoCobrado()*
                                selectedServiciosReg.get(j).getCantidad()*
                                (selectedServiciosReg.get(j).getConcepPrestado().getPctRegalMed()/100);
                    else if (tipo.equals("PNSH"))
                        totalPNSH+=selectedServiciosReg.get(j).getCostoCobrado()*
                                selectedServiciosReg.get(j).getCantidad()*
                                (selectedServiciosReg.get(j).getConcepPrestado().getPctRegalMed()/100);
                    ttlRegalias+=selectedServiciosReg.get(j).getCostoCobrado()*
                                selectedServiciosReg.get(j).getCantidad()*
                            (selectedServiciosReg.get(j).getConcepPrestado().getPctRegalMed()/100);                    
                }
            }
            DistribucionRegalias tmp= new DistribucionRegalias();
            List<ConceptoIngreso> tmp_ci= new ArrayList<ConceptoIngreso>();
            tmp_ci.add(conceptos.get(i));
            tmp.setConceptos(tmp_ci);
            List<Float> numeros= new ArrayList<Float>();
            numeros.add(totalPSH);
            numeros.add(totalPNSH);
            tmp.setNumConceptos(numeros);
            tmp.setImporteRegalia(ttlRegalias);
            
            distribucion.add(tmp);
            c=0;
            ttlRegalias=0;
            totalPSH=0;
            totalPNSH=0;
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
    
    public String buscarPacientePSH(int folio){
        Paciente oPac= new Paciente();
        oPac.setFolioPac(folio);
        try {
            oPac=oPac.buscaPaciente();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return oPac.getTipo();
    }
    
    public void autorizacionPago(){
        if( this.selectedServiciosReg.size()>0){
            PagoRegalia pagoReg = new PagoRegalia();
            pagoReg.setPersonalHospitalario(this.selectedPagoRegalias.getPersonalHospitalario());
            pagoReg.setFechaInicio(fechaIniP);
            pagoReg.setFechaFin(fechaFinP);
            pagoReg.setMontoTotal(totalTotales);
            pagoReg.setSituacion(PagoRegalia.SIT_AUTORIZ);
            pagoReg.setFechaAutorizacion(new Date());
            
            int insertar=0;
            try {
                insertar = pagoReg.insertar();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Autorización de regalías a médicos", "Regalias autorizadas."));    
            } catch (Exception ex) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Autorización de regalías a médicos", "Error: No se pueden autorizar las regalías."));           
                ex.printStackTrace();
            }
            
            List<DetalleRegalias> detRegalias= new ArrayList<DetalleRegalias>();
            pagoReg.setIdPagoRegalias(insertar);
            try{
                for(int i=0; i<selectedServiciosReg.size();i++){
                    DetalleRegalias tmp= new DetalleRegalias();
                    tmp.setPagoRegalias(pagoReg);
                    tmp.setServicioPrestado(selectedServiciosReg.get(i));
                    ConceptoEgreso cEgrTmp = new ConceptoEgreso();
                    cEgrTmp.setCveConcepEgr(selectedServiciosReg.get(i).getConcepPrestado().buscaCveConcepEgrMedExt());
                    tmp.setConceptoEgreso( cEgrTmp );
                    tmp.setMontoCalculado(selectedServiciosReg.get(i).getCostoCobrado()*
                            selectedServiciosReg.get(i).getCantidad()*
                            selectedServiciosReg.get(i).getConcepPrestado().getPctRegalMed()/100);
                    detRegalias.add(tmp);
                }
            }catch(Exception ex){
                ex.printStackTrace();
            }
           
            for(int i=0; i<detRegalias.size(); i++){
                try {
                    String numRows=detRegalias.get(i).insertar();
                } catch (Exception ex) {
                    Logger.getLogger(AutPagoExtraEnfermerasJB.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }else{
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Autorización de regalías a enfermeras", "Error: No se pueden autorizar las regalías."));           
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
        this.selectedPagoRegalias = new AutPagoRegMedicosExternoJB().getPagoRegaliasSesion();
        if(this.fechaIniP!=null && this.fechaFinP!=null){
            this.regaliasAlMedico();
        }
    }

    public void limpiar() {
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