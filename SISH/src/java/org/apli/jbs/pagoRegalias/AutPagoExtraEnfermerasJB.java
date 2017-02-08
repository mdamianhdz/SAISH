package org.apli.jbs.pagoRegalias;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.apli.modelbeans.ConceptoEgreso;
import org.apli.modelbeans.ConceptoIngreso;
import org.apli.modelbeans.DetalleRegalias;
import org.apli.modelbeans.DistribucionRegalias;
import org.apli.modelbeans.PagoRegalia;
import org.apli.modelbeans.PersonalHospitalario;
import org.apli.modelbeans.ServicioPrestado;
 /**
 * AutPagoRegaliasEnfermerasJB.java
 * JSF Managed Bean archivo que realiza la autorizacion del pago de regalias a enfermeras
 * @author Humberto Marin Vega /BAOZ
 * Fecha: Julio 2014 / Junio 2015
 */
@ManagedBean(name="oRegEnf")
@ViewScoped
public class AutPagoExtraEnfermerasJB  implements Serializable{
    private Date fechaIniP;
    private Date fechaFinP;
    private String enf;
    
    private PersonalHospitalario oEnf;
    private PersonalHospitalario [] enfermeras;
    
    private ServicioPrestado oSP;
    private List<ServicioPrestado> regalias;
    
    private List<DistribucionRegalias> distribucion;
    
    private float totalImporte;
    private float totalRegalias;
    
    private boolean disable;
    private List<String> nameColumnas;
    
    public AutPagoExtraEnfermerasJB(){
        this.enf="";
        this.disable = true;
        this.distribucion = new ArrayList<DistribucionRegalias>();
        this.regalias = new ArrayList<ServicioPrestado>();
        obtieneEnfermeras();
        this.nameColumnas= new ArrayList<String>();
        this.nameColumnas.add("Fecha");
        this.nameColumnas.add("Total");
        this.nameColumnas.add("Regalías");
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
    
    private void obtieneEnfermeras(){
        oEnf = new PersonalHospitalario();
        try {
            enfermeras = oEnf.buscaTodasJefasEnfermeria();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public String[] getJefasEnfermeria(){
        String[] enf1= new String[enfermeras.length];
        for(int i=0; i<enfermeras.length; i++){
            enf1[i]=enfermeras[i].getNombreCompleto();
        }
        return enf1;
    }
    
    public void obtieneRegalias(ActionEvent ae){
        if(enf.equals("")){
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_WARN, 
                    "Regalías a enfermeras", "Seleccione una Jefa de Enfermería."));    
        }else{     
        try {
            oSP= new ServicioPrestado();
            regalias= oSP.buscaServiciosPrestadosRegaliasEnfermeras(fechaIniP, fechaFinP);
            if(regalias.size()<=0){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Regalías a enfermeras", "Registros no disponibles."));    
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Regalías a enfermeras", "Error: Faltan datos."));    
        }
        agrupa();
        }
    }
    
    public void agrupa(){
        List<Date> fechas = new ArrayList<Date>();
        
        for(int i=0; i<regalias.size(); i++){
            fechas.add(regalias.get(i).getRealizado());
        }
        HashSet hs = new HashSet();
        hs.addAll(fechas);
        fechas.clear();
        fechas.addAll(hs);
        Collections.sort(fechas);
        for(int i=0; i<fechas.size(); i++){
            System.out.println("Fecha:"+fechas.get(i));
        }
        
        this.distribucion= new ArrayList<DistribucionRegalias>();
        
        ConceptoIngreso conin= new ConceptoIngreso();
        List<ConceptoIngreso> consEnf= new ArrayList<ConceptoIngreso>();
        
        try{
            consEnf= conin.buscarConceptosEnfermeria();
        }catch(Exception ex){
            ex.printStackTrace();
        }
        this.nameColumnas.clear();
        this.nameColumnas.add("FECHA");
        for(int i=0; i<consEnf.size(); i++){
            this.nameColumnas.add(consEnf.get(i).getDescripConcep());
        }
        this.nameColumnas.add("TOTAL");
        this.nameColumnas.add("REGALÍAS");
    
        float importeDia=0;
        
        DistribucionRegalias tmp= new DistribucionRegalias();
        List<Float> nums;
             
        ConceptoIngreso cing= new ConceptoIngreso();
        int c=0;
        
        for(int j=0; j<fechas.size(); j++ ){
            tmp= new DistribucionRegalias();
            tmp.setFecha(fechas.get(j));
            tmp.setConceptos(consEnf);
            nums=cuentaConceptos(fechas.get(j), consEnf);
            tmp.setNumConceptos(nums);
            importeDia= importeDia(fechas.get(j), consEnf);
            tmp.setImporte(importeDia);
            tmp.setImporteRegalia(importeDiaRegalia(fechas.get(j),consEnf));
            this.distribucion.add(tmp);
            importeDia=0;
         }
    }
    
    public List<Float> cuentaConceptos(Date fecha, List<ConceptoIngreso> consEnf){
        List<Float> numeros= new ArrayList<Float>();
        ConceptoIngreso cin=new ConceptoIngreso();
        for(int h=0; h<consEnf.size(); h++){
            try {
                String x2=cin.buscaNoRegaliasPorConceptoDia(consEnf.get(h).getCveConcep(), fecha , fechaIniP, fechaFinP);
                x2= x2.substring(2, x2.length()-1);
                numeros.add((float)Double.valueOf(x2).intValue());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return numeros;
    }
    
    public float importeDia(Date fecha, List<ConceptoIngreso> consEnf){
        float importeDia=0;
        ConceptoIngreso cin=new ConceptoIngreso();
        for(int h=0; h<consEnf.size(); h++){
            try {
                for(int i=0; i<regalias.size(); i++){
                    if(regalias.get(i).getRealizado().equals(fecha) && regalias.get(i).getConcepPrestado().getCveConcep()==consEnf.get(h).getCveConcep()){
                        importeDia+=regalias.get(i).getCostoCobrado();
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return importeDia;
    }
    
    public float importeDiaRegalia(Date fecha, List<ConceptoIngreso> consEnf){
        float importeDia=0;
        ConceptoIngreso cin=new ConceptoIngreso();
        for(int h=0; h<consEnf.size(); h++){
            try {
                for(int i=0; i<regalias.size(); i++){
                    if(regalias.get(i).getRealizado().equals(fecha) && regalias.get(i).getConcepPrestado().getCveConcep()==consEnf.get(h).getCveConcep()){
                        importeDia+=regalias.get(i).getCostoCobrado()*(regalias.get(i).getConcepPrestado().getPctRegalEnf()/100);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return importeDia;
    }
    
    public void autorizacionPago(){
        this.nameColumnas.clear();
        if(regalias.size()>0){
            PersonalHospitalario enfJT= new PersonalHospitalario();
            for(int i=0; i<enfermeras.length; i++){
                if(enfermeras[i].getNombreCompleto().equals(enf)){
                    enfJT=enfermeras[i];
                }
            }
            PagoRegalia pagoReg = new PagoRegalia();
            pagoReg.setPersonalHospitalario(enfJT);
            pagoReg.setFechaInicio(fechaIniP);
            pagoReg.setFechaFin(fechaFinP);
            pagoReg.setMontoTotal(totalRegalias);
            pagoReg.setFechaAutorizacion(new Date());
            pagoReg.setSituacion(PagoRegalia.SIT_AUTORIZ);
            int insertar=0;
            try {
                insertar = pagoReg.insertar();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Autorización de regalías a enfermeras", "Regalías autorizadas."));    
            } catch (Exception ex) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Autorización de regalías a enfermeras", "Error: No se pueden autorizar las regalías."));           
                ex.printStackTrace();
            }
            pagoReg.setIdPagoRegalias(insertar);
            List<DetalleRegalias> detRegalias= new ArrayList<DetalleRegalias>();
            for(int i=0; i<regalias.size();i++){
                DetalleRegalias tmp= new DetalleRegalias();
                tmp.setPagoRegalias(pagoReg);
                ConceptoEgreso ceTmp= new ConceptoEgreso();
                ceTmp.setCveConcepEgr(86); // 86 - Aplicaciones enfermería
                tmp.setConceptoEgreso(ceTmp);
                tmp.setServicioPrestado(regalias.get(i));
                tmp.setMontoCalculado(regalias.get(i).getCostoCobrado()*regalias.get(i).getConcepPrestado().getPctRegalEnf()/100);
                detRegalias.add(tmp);
            }
            
            for(int i=0; i<detRegalias.size(); i++){
                try {
                    String numRows=detRegalias.get(i).insertar();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Logger.getLogger(AutPagoExtraEnfermerasJB.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }else{
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Autorización de regalías a enfermeras", "Error: No se pueden autorizar las regalías."));           
        }
    }
    
    public void limpiar(){
        this.disable=true;
        this.fechaIniP=null;
        this.fechaFinP=null;
        this.distribucion.clear();
        this.regalias.clear();
        this.enf="";
    }
    
    public List<String> getNameColumnas(){
        return nameColumnas;
    }
    
    public int getNumColumnas(){
        return this.nameColumnas.size()-2;
    }
    
    public DistribucionRegalias getDist(){
        return new DistribucionRegalias();
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
    
    public boolean isDisable() {
        return disable;
    }
    public void setDisable(boolean disable) {
        this.disable = disable;
    }

    public String getJefaEnf() {
        return enf;
    }
    public void setJefaEnf(String enf) {
        this.enf = enf;
    }

    public List<ServicioPrestado> getRegalias() {
        return regalias;
    }
    public void setRegalias(List<ServicioPrestado> regalias) {
        this.regalias = regalias;
    }

    public List<DistribucionRegalias> getDistribucion() {
        return distribucion;
    }
    public void setDistribucion(List<DistribucionRegalias> distribucion) {
        this.distribucion = distribucion;
    }
    
    public float getTotalImporte(){
        totalImporte=0;
        for(int i=0; i<distribucion.size(); i++){
            totalImporte+=distribucion.get(i).getImporte();
        }
        return totalImporte;
    }
    
    public float getTotalRegalias(){
        totalRegalias=0;
        for(int i=0; i<distribucion.size(); i++){
            this.totalRegalias+=distribucion.get(i).getImporteRegalia();
        }
        return totalRegalias;

    }
}    