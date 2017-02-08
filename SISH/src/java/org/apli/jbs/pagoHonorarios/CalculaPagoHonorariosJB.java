package org.apli.jbs.pagoHonorarios;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apli.modelbeans.ConceptoIngreso;
import org.apli.modelbeans.DetalleHonorarios;
import org.apli.modelbeans.DistribucionRegalias;
import org.apli.modelbeans.EpisodioMedico;
import org.apli.modelbeans.PagoHonorarios;
import org.apli.modelbeans.PersonalHospitalario;
import org.apli.modelbeans.ProcedimientoRealizado;
import org.apli.modelbeans.ServicioPrestado;
import org.primefaces.event.RowEditEvent;

/**
 * CalculaPagoHonorariosJB.java 
 * JSF Managed Bean archivo que realiza el cálculo del pago de honorarios
 *
 * @author Humberto Marin Vega/BAOZ
 * Fecha: Agosto 2014/Junio 2015
 */
@ManagedBean(name = "oCalPHon")
@ViewScoped

public class CalculaPagoHonorariosJB implements Serializable {
    private Date fechaIniP;
    private Date fechaFinP;
    private ServicioPrestado oSP;
    private List<DetalleHonorarios> servicios;
    private List<DetalleHonorarios> serviciosPaq;
    private List<ServicioPrestado> spMedicos;
    private List<ServicioPrestado> spEnfermeras;
    private List<ServicioPrestado> spTecnicos;
    private List<ServicioPrestado> spMedicosPaq;
    private List<ServicioPrestado> spEnfermerasPaq;
    private List<ServicioPrestado> spTecnicosPaq;
    private List<ConceptoIngreso> conceptos;
    private List<DistribucionRegalias> distribucion;
    private List<String> nameColumnas;
    private List<Integer> honorarios;
    private List<DetalleHonorarios> serviciosPers;
    private List<DetalleHonorarios> serviciosHon; 
    private float totalHonorarios;
    private DistribucionRegalias selectedDistribucionRegalias;

    public CalculaPagoHonorariosJB() {
        oSP = new ServicioPrestado();
        selectedDistribucionRegalias = new DistribucionRegalias();
        serviciosPers = new ArrayList<DetalleHonorarios>();
        serviciosHon  = new ArrayList<DetalleHonorarios>();
        this.totalHonorarios=0;
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

    public void buscarPersonal() {
        servicios = new ArrayList<DetalleHonorarios>();
        serviciosPaq = new ArrayList<DetalleHonorarios>();
        spMedicos = new ArrayList<ServicioPrestado>();
        spEnfermeras = new ArrayList<ServicioPrestado>();
        spTecnicos = new ArrayList<ServicioPrestado>();
        spMedicosPaq = new ArrayList<ServicioPrestado>();
        spEnfermerasPaq = new ArrayList<ServicioPrestado>();
        spTecnicosPaq = new ArrayList<ServicioPrestado>();
        distribucion = new ArrayList<DistribucionRegalias>();
        try {
            spMedicos = oSP.buscaServiciosPrestadosHonorariosMedico(fechaIniP, fechaFinP);
            spEnfermeras = oSP.buscaServiciosPrestadosHonorariosEnfermeras(fechaIniP, fechaFinP);
            spTecnicos = oSP.buscaServiciosPrestadosHonorariosTecnicos(fechaIniP, fechaFinP);
            spMedicosPaq = oSP.buscaServiciosPrestadosPaqHonorariosMedico(fechaIniP, fechaFinP);
            spEnfermerasPaq = oSP.buscaServiciosPrestadosPaqHonorariosEnfermeras(fechaIniP, fechaFinP);
            spTecnicosPaq = oSP.buscaServiciosPrestadosPaqHonorariosTecnicos(fechaIniP, fechaFinP);
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Calcula pago de honorarios", "Error: No se encuentran registros."));    
        }
        for (int i = 0; i < spMedicos.size(); i++) {
            DetalleHonorarios honMed= new DetalleHonorarios();
            honMed.setServicioPrestado(spMedicos.get(i));
            servicios.add(honMed);
        }
        for (int i = 0; i < spEnfermeras.size(); i++) {
            DetalleHonorarios honEnf = new DetalleHonorarios();
            honEnf.setServicioPrestado(spEnfermeras.get(i));
            servicios.add(honEnf);
        }
        for (int i = 0; i < spTecnicos.size(); i++) {
            DetalleHonorarios honTec = new DetalleHonorarios();
            honTec.setServicioPrestado(spTecnicos.get(i));
            servicios.add(honTec);
        }
        for (int i=0; i<spMedicosPaq.size(); i++){
            DetalleHonorarios honMedPaq = new DetalleHonorarios();
            honMedPaq.setServicioPrestado(spMedicosPaq.get(i));
            serviciosPaq.add(honMedPaq);
        }
        for (int i = 0; i < spEnfermerasPaq.size(); i++) {
            DetalleHonorarios honEnfPaq = new DetalleHonorarios();
            honEnfPaq.setServicioPrestado(spEnfermerasPaq.get(i));
            serviciosPaq.add(honEnfPaq);
        }
        for (int i = 0; i < spTecnicosPaq.size(); i++) {
            DetalleHonorarios honTecPaq = new DetalleHonorarios();
            honTecPaq.setServicioPrestado(spTecnicosPaq.get(i));
            serviciosPaq.add(honTecPaq);
        }
        for(int i=0; i< serviciosPaq.size(); i++){
            servicios.add(serviciosPaq.get(i));
        }
        this.obtieneServiciosPaquete();
        this.obtieneConceptos();
        this.obtieneMedicos();
        this.obtieneEnfermeras();
        this.obtieneTecnicos();
    }
    
    public void obtieneServiciosPaquete(){
       try{
            EpisodioMedico epMedPaq = new EpisodioMedico();
            List<EpisodioMedico> episodios = new ArrayList<EpisodioMedico>();
            episodios = epMedPaq.buscaTodosEpisodioMedicoPaq(fechaIniP, fechaFinP);
            float totalConsumido=0;
            int noProc=0;
            float costoPaq=0;
            float honPaq=0;
            for(int i=0; i<episodios.size(); i++){
                totalConsumido =this.sumaServiciosPorEpisodio(episodios.get(i).getCveepisodio());
                costoPaq = episodios.get(i).buscaCostoPaqueteParaHonorarios();
                noProc = episodios.get(i).buscaNoProcedimientosPorEpisodioMedico(fechaIniP, fechaFinP);
                System.out.println("EpMed"+episodios.get(i).getCveepisodio()+" - Total consumido: "+totalConsumido+
                        " - No de procedimientos: "+noProc+" - Costo Paq.:"+costoPaq);
                honPaq=(costoPaq-totalConsumido)/noProc;
                this.agregaExcedente(episodios.get(i).getCveepisodio(),honPaq);
            }
       }catch(Exception ex){
           ex.printStackTrace();
       }
    }
    
    public float sumaServiciosPorEpisodio(int cveEpisodio){
        List <String> folios= new ArrayList<String>();
        for(int i=0; i<serviciosPaq.size();i++){
            folios.add(serviciosPaq.get(i).getServicioPrestado().getIdFolio());
        }
        HashSet hs = new HashSet();
        hs.addAll(folios);
        folios.clear();
        folios.addAll(hs);
        int c=0;
        List<DetalleHonorarios> serviciosPaquete= new ArrayList<DetalleHonorarios>();
        for(int i=0; i<folios.size();i++){
            for(int j=0; j<serviciosPaq.size(); j++){
                if(folios.get(i).equals(serviciosPaq.get(j).getServicioPrestado().getIdFolio()) && c==0 ){
                    serviciosPaquete.add(serviciosPaq.get(j));
                    c++;
                }
            }
            c=0;
        }
        float total=0;
        for(int i=0; i<serviciosPaquete.size();i++){
            if(serviciosPaquete.get(i).getServicioPrestado().getEpisodioMedico().getCveepisodio()==cveEpisodio){
                total+=serviciosPaquete.get(i).getServicioPrestado().getCostoOriginal();
            }
        }
        return total;
    }
    
    public void agregaExcedente(int cveepisodio, float excedente){
        for(int i=0; i<this.servicios.size(); i++){
            if(servicios.get(i).getServicioPrestado().getEpisodioMedico().
                    getCveepisodio()==cveepisodio){
               servicios.get(i).getServicioPrestado().setCostoOriginal(
                       servicios.get(i).getServicioPrestado().getCostoOriginal()
                       +excedente);
            }
        }
    }
    
    public void obtieneConceptos() {
        conceptos = new ArrayList<ConceptoIngreso>();
        honorarios = new ArrayList<Integer>();

        List<Integer> nombreConceptos = new ArrayList<Integer>();
        for (int i = 0; i < servicios.size(); i++) {
            nombreConceptos.add(servicios.get(i).getServicioPrestado().getConcepPrestado().getCveConcep());
        }
        HashSet hs = new HashSet();
        hs.addAll(nombreConceptos);
        nombreConceptos.clear();
        nombreConceptos.addAll(hs);
        int c = 0;
        //======================================================================>>
         for(int i=0; i<nombreConceptos.size(); i++){
            if ((ConceptoIngreso.isCveHonorarios(nombreConceptos.get(i)))){
                honorarios.add(nombreConceptos.get(i));
                //nombreConceptos.remove(i);
            }
        } 
        for(int i=0; i<honorarios.size(); i++){
            for(int j=0; j<nombreConceptos.size(); j++){
                 if(honorarios.get(i)==nombreConceptos.get(j)){
                     nombreConceptos.remove(j);
                 }
            }
        }
        for (int i = 0; i < nombreConceptos.size(); i++) {
            for (int j = 0; j < servicios.size(); j++) {
                if (nombreConceptos.get(i) == servicios.get(j).
                        getServicioPrestado().getConcepPrestado().getCveConcep() 
                        && c == 0) {
                    conceptos.add(servicios.get(j).getServicioPrestado().
                            getConcepPrestado());
                    c++;
                }
            }
            c = 0;
        }
        ConceptoIngreso conIng = new ConceptoIngreso();
        conIng.setCveConcep(0);
        conIng.setDescripConcep("HONORARIOS");
        conceptos.add(conIng);

        this.nameColumnas = new ArrayList<String>();
        nameColumnas.add("PERSONAL");
        for (int i = 0; i < conceptos.size(); i++) {
            nameColumnas.add(conceptos.get(i).getDescripConcep().toUpperCase());
        }
        nameColumnas.add("CALCULAR");
    }

    public void obtenerDatosPersona() {
    int nCveConcep;
        if(this.selectedDistribucionRegalias.getPersonal()!=null){
            serviciosPers.clear();
            serviciosHon.clear();
            for(int i=0; i<servicios.size(); i++){
                nCveConcep = servicios.get(i).getServicioPrestado().getConcepPrestado().getCveConcep();
                if(selectedDistribucionRegalias.getPersonal().getFolioPers()== 
                        servicios.get(i).getServicioPrestado().getMedico().getFolioPers()){
                    if((ConceptoIngreso.isCveHonorarios(nCveConcep))){
                        servicios.get(i).setAutorizado(servicios.get(i).getServicioPrestado().getCostoCobrado());
                        serviciosHon.add(servicios.get(i));
                    }else{
                        servicios.get(i).setAutorizado(servicios.get(i).getServicioPrestado().getCostoCobrado());
                        serviciosPers.add(servicios.get(i));
                    }
                }
            }  
            cambiaVistaSituacion(serviciosPers);
            cambiaVistaSituacion(serviciosHon);
             //ORDENA LISTAS
            Collections.sort(serviciosPers, new Comparator<DetalleHonorarios>(){
			@Override
			public int compare(DetalleHonorarios sp1, DetalleHonorarios sp2) {
				return sp1.getServicioPrestado().getRealizado().compareTo(sp2.getServicioPrestado().getRealizado());
			}	
		});
            Collections.sort(serviciosHon, new Comparator<DetalleHonorarios>(){
			@Override
			public int compare(DetalleHonorarios sp1, DetalleHonorarios sp2) {
				return sp1.getServicioPrestado().getRealizado().compareTo(sp2.getServicioPrestado().getRealizado());
			}	
		});
            try {
                this.obtieneProcedimientoQx();
            } catch (Exception ex) {
                Logger.getLogger(CalculaPagoHonorariosJB.class.getName()).log(Level.SEVERE, null, ex);
            }
            this.obtieneEquipo();
            this.calculaCuenta();
        }
    }
    
    public void obtieneProcedimientoQx() throws Exception{
        List<ProcedimientoRealizado> procqx= new ArrayList();
        for(int i=0; i<serviciosHon.size(); i++){
            procqx.clear();
            procqx= serviciosHon.get(i).getServicioPrestado().buscaProcQxTipoQxServicioPrestado();
            if(procqx.size()>0){
            serviciosHon.get(i).getServicioPrestado().setProcedimientoRealizado(procqx.get(0));
            }
        }
    }
    
    public void obtieneEquipo(){
        List<String> equipo= new ArrayList<String>();
        for(int i=0; i<serviciosHon.size(); i++){
            equipo.clear();
            for(int j=0; j<servicios.size(); j++){
                if(serviciosHon.get(i).getServicioPrestado().getIdFolio().equals(servicios.get(j).getServicioPrestado().getIdFolio())){
                    if(servicios.get(j).getServicioPrestado().getMedico().getFolioPers()!= this.selectedDistribucionRegalias.getPersonal().getFolioPers()){
                        equipo.add(servicios.get(j).getServicioPrestado().getMedico().getNombreCompleto());
                    }
                }
            }
        }
    }
   
    public void cambiaVistaSituacion(List<DetalleHonorarios> lista){
        for(int i=0; i<lista.size(); i++){
            if(lista.get(i).getServicioPrestado().getSituacion().equals("N")){
                lista.get(i).getServicioPrestado().setSituacion("Nuevo");
            }else if(lista.get(i).getServicioPrestado().getSituacion().equals("P")){
                lista.get(i).getServicioPrestado().setSituacion("Pagado");
            }else if(lista.get(i).getServicioPrestado().getSituacion().equals("A")){
                lista.get(i).getServicioPrestado().setSituacion("Autorizado a credito");
            }else if(lista.get(i).getServicioPrestado().getSituacion().equals("Q")){
                lista.get(i).getServicioPrestado().setSituacion("Autorizado a paquete");
            }
        }
    }
    
    public void limpiar(){
        this.distribucion= new ArrayList<DistribucionRegalias>();
    }
    
    public void onRowEdit(RowEditEvent event) {
        DetalleHonorarios oDetHon= (DetalleHonorarios)event.getObject();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Cargo modificado",oDetHon.getServicioPrestado().getConcepPrestado().getDescripConcep()));
        String msj="";
        try {
            if(oDetHon.getServicioPrestado().getCostoCobrado()>=0){
                msj= oDetHon.getServicioPrestado().modificarCosto();
                msj= msj.substring(2, msj.length()-1);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Autorizar pago de honorarios", msj));
            }            
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Calcula pago de honorarios", "Error: No se pudo actualizar el pago"));    
        }
        this.calculaCuenta();
     }
    
    public void calculaCuenta(){
        this.totalHonorarios=0;
        for(int i=0; i<serviciosPers.size();i++){
            totalHonorarios+=serviciosPers.get(i).getAutorizado();
        }
        for(int i=0; i<serviciosHon.size(); i++){
            this.totalHonorarios+=serviciosHon.get(i).getAutorizado();
        }
    }
    
    public void guardarHonorarios(){
        PagoHonorarios pagoHon = new PagoHonorarios();
        pagoHon.setPersonalHospitalario(this.selectedDistribucionRegalias.getPersonal());
        pagoHon.setRegistro(new Date());
        pagoHon.setSituacion("0"); // 0 - Por autorizar
        try {
            pagoHon.setIdPagoHonorarios(pagoHon.insertar());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Calcula pago de honorarios", "Se ha guardado el pago de honorarios."));    
        } catch (Exception ex) {
            ex.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Calcula pago de honorarios", "Error: No se puede el guardar pago de honorarios."));    
        }
        List<DetalleHonorarios> detHon = new ArrayList<DetalleHonorarios>();
        for(int i=0; i<this.serviciosHon.size();i++){
            detHon.add(this.serviciosHon.get(i));
        }
        for(int i=0; i<this.serviciosPers.size();i++){
            detHon.add(this.serviciosPers.get(i));
        }
        try{
            for(int i=0; i<detHon.size();i++){
                detHon.get(i).setPagoHonorarios(pagoHon);
                System.out.println("PH"+detHon.get(i).getPagoHonorarios().getIdPagoHonorarios());
                System.out.println("SP"+detHon.get(i).getServicioPrestado().getIdFolio());
                String msj = detHon.get(i).insertar();
                System.out.println(msj);
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Calcula pago de honorarios", "Se han guardado los honorarios exitosamente."));    
        }catch (Exception ex){
            ex.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Calcula pago de honorarios", "Error: No se pueden guardar honorarios."));    
        }
    }

//--------------------------------------------------------------------------------------------------------------
    public void obtieneMedicos() {
        List<PersonalHospitalario> persHosp = new ArrayList<PersonalHospitalario>();
        List<String> personal = new ArrayList<String>();
        for (int i = 0; i < spMedicos.size(); i++) {
            personal.add(spMedicos.get(i).getMedico().getNombreCompleto());
        }
        HashSet hs = new HashSet();
        hs.addAll(personal);
        personal.clear();
        personal.addAll(hs);
        Collections.sort(personal);
        int c = 0;
        for (int i = 0; i < personal.size(); i++) {
            for (int j = 0; j < servicios.size(); j++) {
                if (personal.get(i).equals(servicios.get(j).getServicioPrestado().getMedico().getNombreCompleto()) && c == 0) {
                    persHosp.add(servicios.get(j).getServicioPrestado().getMedico());
                    c++;
                }
            }
            c = 0;
        }
        DistribucionRegalias tmp;
        List<Float> nums;
        for (int i = 0; i < persHosp.size(); i++) {
            tmp = new DistribucionRegalias();
            tmp.setPersonal(persHosp.get(i));
            tmp.setConceptos(conceptos);
            nums = cuentaConceptosMedico(persHosp.get(i).getFolioPers());
            tmp.setNumConceptos(nums);
            distribucion.add(tmp);
        }
    }

    public List<Float> cuentaConceptosMedico(int folioPers) {
        List<Float> numeros = new ArrayList<Float>();
        ConceptoIngreso cin = new ConceptoIngreso();
        int c = 0;
        try {
            for (int h = 0; h < conceptos.size(); h++) {
                if (conceptos.get(h).getCveConcep() == 0) {
                    c = 0;
                    for (int i = 0; i < honorarios.size(); i++) {
                        String x = cin.buscaNoHonPorConceptoMedico(folioPers, honorarios.get(i), fechaIniP, fechaFinP);
                        x = x.substring(2, x.length() - 1);
                        c += (float) Double.valueOf(x).intValue();
                    }
                    numeros.add((float) c);
                } else {
                    String x = cin.buscaNoHonPorConceptoMedico(folioPers, conceptos.get(h).getCveConcep(), fechaIniP, fechaFinP);
                    x = x.substring(2, x.length() - 1);
                    numeros.add((float) Double.valueOf(x).intValue());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return numeros;
    }
//--------------------------------------------------------------------------------------------------------------

    public void obtieneEnfermeras() {
        List<PersonalHospitalario> persHosp = new ArrayList<PersonalHospitalario>();
        List<String> personal = new ArrayList<String>();
        for (int i = 0; i < spEnfermeras.size(); i++) {
            personal.add(spEnfermeras.get(i).getMedico().getNombreCompleto());
        }
        HashSet hs = new HashSet();
        hs.addAll(personal);
        personal.clear();
        personal.addAll(hs);
        Collections.sort(personal);
        int c = 0;
        for (int i = 0; i < personal.size(); i++) {
            for (int j = 0; j < servicios.size(); j++) {
                if (personal.get(i).equals(servicios.get(j).getServicioPrestado().getMedico().getNombreCompleto()) && c == 0) {
                    persHosp.add(servicios.get(j).getServicioPrestado().getMedico());
                    c++;
                }
            }
            c = 0;
        }
        DistribucionRegalias tmp;
        List<Float> nums;
        for (int i = 0; i < persHosp.size(); i++) {
            tmp = new DistribucionRegalias();
            tmp.setPersonal(persHosp.get(i));
            tmp.setConceptos(conceptos);
            nums = cuentaConceptosEnfermera(persHosp.get(i).getFolioPers());
            tmp.setNumConceptos(nums);
            distribucion.add(tmp);
        }
    }

    public List<Float> cuentaConceptosEnfermera(int folioPers) {
        List<Float> numeros = new ArrayList<Float>();
        ConceptoIngreso cin = new ConceptoIngreso();
        int c = 0;
        try {
            for (int h = 0; h < conceptos.size(); h++) {
                if (conceptos.get(h).getCveConcep() == 0) {
                    c = 0;
                    for (int i = 0; i < honorarios.size(); i++) {
                        String x = cin.buscaNoHonPorConceptoEnfermeras(folioPers, honorarios.get(i), fechaIniP, fechaFinP);
                        x = x.substring(2, x.length() - 1);
                        c += (float) Double.valueOf(x).intValue();
                    }
                    numeros.add((float) c);
                } else {
                    String x = cin.buscaNoHonPorConceptoEnfermeras(folioPers, conceptos.get(h).getCveConcep(), fechaIniP, fechaFinP);
                    x = x.substring(2, x.length() - 1);
                    numeros.add((float) Double.valueOf(x).intValue());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return numeros;
    }

    //--------------------------------------------------------------------------------------------------------------
    public void obtieneTecnicos() {
        List<PersonalHospitalario> persHosp = new ArrayList<PersonalHospitalario>();
        List<String> personal = new ArrayList<String>();
        for (int i = 0; i < spTecnicos.size(); i++) {
            personal.add(spTecnicos.get(i).getMedico().getNombreCompleto());
        }
        HashSet hs = new HashSet();
        hs.addAll(personal);
        personal.clear();
        personal.addAll(hs);
        Collections.sort(personal);
        int c = 0;
        for (int i = 0; i < personal.size(); i++) {
            for (int j = 0; j < servicios.size(); j++) {
                if (personal.get(i).equals(servicios.get(j).getServicioPrestado().getMedico().getNombreCompleto()) && c == 0) {
                    persHosp.add(servicios.get(j).getServicioPrestado().getMedico());
                    c++;
                }
            }
            c = 0;
        }
        DistribucionRegalias tmp;
        List<Float> nums;
        for (int i = 0; i < persHosp.size(); i++) {
            tmp = new DistribucionRegalias();
            tmp.setPersonal(persHosp.get(i));
            tmp.setConceptos(conceptos);
            nums = cuentaConceptosTecnicos(persHosp.get(i).getFolioPers());
            tmp.setNumConceptos(nums);
            distribucion.add(tmp);
        }
    }

    public List<Float> cuentaConceptosTecnicos(int folioPers) {
        List<Float> numeros = new ArrayList<Float>();
        ConceptoIngreso cin = new ConceptoIngreso();
        int c=0;
        try{
            for (int h = 0; h < conceptos.size(); h++) {
                if (conceptos.get(h).getCveConcep() == 0) {
                    c = 0;
                    for (int i = 0; i < honorarios.size(); i++) {
                        String x = cin.buscaNoHonPorConceptoTecnicos(folioPers, honorarios.get(i), fechaIniP, fechaFinP);
                        x = x.substring(2, x.length() - 1);
                        c += (float) Double.valueOf(x).intValue();
                    }
                    numeros.add((float) c);
                }else{
                    String x = cin.buscaNoHonPorConceptoTecnicos(folioPers, conceptos.get(h).getCveConcep(), fechaIniP, fechaFinP);
                    x = x.substring(2, x.length() - 1);
                    numeros.add((float) Double.valueOf(x).intValue());
                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return numeros;
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

    public List<DistribucionRegalias> getDistribucion() {
        return distribucion;
    }
    public void setDistribucion(List<DistribucionRegalias> distribucion) {
        this.distribucion = distribucion;
    }

    public DistribucionRegalias getDist() {
        return new DistribucionRegalias();
    }

    public List<String> getNameColumnas() {
        return nameColumnas;
    }

    public DistribucionRegalias getSelectedDistribucionRegalias() {
        return selectedDistribucionRegalias;
    }
    public void setSelectedDistribucionRegalias(DistribucionRegalias selectedDistribucionRegalias) {
        this.selectedDistribucionRegalias = selectedDistribucionRegalias;
    }

    public List<DetalleHonorarios> getServiciosPers() {
        return serviciosPers;
    }
    public void setServiciosPers(List<DetalleHonorarios> serviciosPers) {
        this.serviciosPers = serviciosPers;
    }

    public List<DetalleHonorarios> getServiciosHon() {
        return serviciosHon;
    }
    public void setServiciosHon(List<DetalleHonorarios> serviciosHon) {
        this.serviciosHon = serviciosHon;
    }

    public float getTotalHonorarios() {
        return totalHonorarios;
    }
    public void setTotalHonorarios(float totalHonorarios) {
        this.totalHonorarios = totalHonorarios;
    }
}