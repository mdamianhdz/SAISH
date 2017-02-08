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
import org.apli.modelbeans.PersonalHospitalario;
import org.apli.modelbeans.ProcedimientoRealizado;
import org.primefaces.event.RowEditEvent;

/**
 * AutorizarPagoHonorariosJB.java 
 * JSF Managed Bean archivo que realiza la
 * autorizacion del pago de honorarios
 *
 * @author Humberto Marin Vega      Fecha: Agosto 2014
 */
@ManagedBean(name = "oAutPHon")
@ViewScoped
public class AutorizarPagoHonorariosJB implements Serializable {

    private DetalleHonorarios oDetHon;
    private List<DetalleHonorarios> detallesHonorarios;
    private List<ConceptoIngreso> conceptos;
    private List<String> nameColumnas;
    private List<Integer> honorarios;
    private List<DistribucionRegalias> distribucion;
    private List<DetalleHonorarios> serviciosPers;
    private List<DetalleHonorarios> serviciosHon; 
    private float totalHonorarios;
    
    private DistribucionRegalias selectedDistribucionRegalias;
    
    public AutorizarPagoHonorariosJB() {
       oDetHon = new DetalleHonorarios();
       detallesHonorarios = new ArrayList<DetalleHonorarios>();
       distribucion = new ArrayList<DistribucionRegalias>();
       selectedDistribucionRegalias = new DistribucionRegalias();
       serviciosPers = new ArrayList<DetalleHonorarios>();
       serviciosHon = new ArrayList<DetalleHonorarios>();
       this.totalHonorarios=0; 
    }
    
    public void buscarHonorarosPorAutorizar() {
        detallesHonorarios.clear();
        try {
            detallesHonorarios = oDetHon.buscaDetalleHonorarios();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        for(int i=0; i<detallesHonorarios.size(); i++){
            System.out.println("Dist.: "+detallesHonorarios.get(i).getServicioPrestado().getIdFolio()+" - "+
                    detallesHonorarios.get(i).getPagoHonorarios().getPersonalHospitalario().getFolioPers()+" - "+
                    detallesHonorarios.get(i).getServicioPrestado().getConcepPrestado().getCveConcep()+" - "+
                    detallesHonorarios.get(i).getAutorizado());
        }
        this.obtieneConceptos();
        this.obtienePersonal();
    }
    
    public void obtieneConceptos() {
        conceptos = new ArrayList<ConceptoIngreso>();
        honorarios = new ArrayList<Integer>();
        List<Integer> nameConceptos = new ArrayList<Integer>();
        for (int i = 0; i < detallesHonorarios.size(); i++) {
            nameConceptos.add(detallesHonorarios.get(i).getServicioPrestado().getConcepPrestado().getCveConcep());
        }
        HashSet hs = new HashSet();
        hs.addAll(nameConceptos);
        nameConceptos.clear();
        nameConceptos.addAll(hs);
        int c=0;
        for(int i=0; i<nameConceptos.size(); i++){
            if (ConceptoIngreso.isCveHonorarios(nameConceptos.get(i))){
                honorarios.add(nameConceptos.get(i));
                //nameConceptos.remove(j);
            }
        }
        for(int i=0; i<honorarios.size(); i++){
            for(int j=0; j<nameConceptos.size(); j++){
                 if(honorarios.get(i)==nameConceptos.get(j)){
                     nameConceptos.remove(j);
                 }
            }
        }
        for (int i = 0; i < nameConceptos.size(); i++) {
            for (int j = 0; j < detallesHonorarios.size(); j++) {
                if (nameConceptos.get(i) == detallesHonorarios.get(j).getServicioPrestado().getConcepPrestado().getCveConcep() && c == 0) {
                    conceptos.add(detallesHonorarios.get(j).getServicioPrestado().getConcepPrestado());
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
        nameColumnas.add("SITUACIÓN");
        nameColumnas.add("AUTORIZAR");
    }
    
    public void obtienePersonal() {
        List<PersonalHospitalario> persHosp = new ArrayList<PersonalHospitalario>();
        List<String> personal = new ArrayList<String>();
        for (int i = 0; i < detallesHonorarios.size(); i++) {
            personal.add(detallesHonorarios.get(i).getPagoHonorarios().getPersonalHospitalario().getNombreCompleto());
        }
        HashSet hs = new HashSet();
        hs.addAll(personal);
        personal.clear();
        personal.addAll(hs);
        Collections.sort(personal);
        int c = 0;
        for (int i = 0; i < personal.size(); i++) {
            for (int j = 0; j < detallesHonorarios.size(); j++) {
                if (personal.get(i).equals(detallesHonorarios.get(j).getPagoHonorarios().getPersonalHospitalario().getNombreCompleto()) && c == 0) {
                    persHosp.add(detallesHonorarios.get(j).getPagoHonorarios().getPersonalHospitalario());
                    c++;
                }
            }
            c = 0;
        }
        DistribucionRegalias tmp;
        List<Float> nums;
        distribucion.clear();
        for (int i = 0; i < persHosp.size(); i++) {
            tmp = new DistribucionRegalias();
            tmp.setPersonal(persHosp.get(i));
            tmp.setConceptos(conceptos);
            nums = cuentaConceptosMedico(persHosp.get(i).getFolioPers());
            tmp.setNumConceptos(nums);
            for(int j=0; j<detallesHonorarios.size(); j++){
                if(persHosp.get(i).getFolioPers()==detallesHonorarios.get(j).getPagoHonorarios().getPersonalHospitalario().getFolioPers()){
                    if(detallesHonorarios.get(j).getPagoHonorarios().getSituacion().equals("0"))
                        tmp.setSituacion("Por autorizar");
                    if(detallesHonorarios.get(j).getPagoHonorarios().getSituacion().equals("1"))
                        tmp.setSituacion("Autorizado");
                }
            }
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
                        String x = cin.buscaNoHonorariosPorAutorizarPorConcepto(folioPers, honorarios.get(i));
                        x = x.substring(2, x.length() - 1);
                        c += (float) Double.valueOf(x).intValue();
                    }
                    numeros.add((float) c);
                } else {
                    String x = cin. buscaNoHonorariosPorAutorizarPorConcepto(folioPers, conceptos.get(h).getCveConcep());
                    x = x.substring(2, x.length() - 1);
                    numeros.add((float) Double.valueOf(x).intValue());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return numeros;
    }
    
    public void obtenerDatos() {
        if(this.selectedDistribucionRegalias.getPersonal()==null){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Autorizar pago de honorarios", "Error: Faltan datos."));    
        }else{
            serviciosPers.clear();
            serviciosHon.clear();
            System.out.println("*** OBTENER DATOS PERSONAL ***");
            System.out.println("this.selected" + this.selectedDistribucionRegalias.getPersonal().getNombreCompleto());
            for(int i=0; i<detallesHonorarios.size(); i++){
                if(selectedDistribucionRegalias.getPersonal().getFolioPers() == detallesHonorarios.get(i).getPagoHonorarios().getPersonalHospitalario().getFolioPers() ){
                    System.out.println("detallesHonorarios-"+detallesHonorarios.get(i).getServicioPrestado().getIdFolio());
                    if(detallesHonorarios.get(i).getServicioPrestado().getConcepPrestado().isHonorarios()){
                        serviciosHon.add(detallesHonorarios.get(i));
                    }else{
                        serviciosPers.add(detallesHonorarios.get(i));
                    }
                }
            }
            
            for(int i=0; i<serviciosHon.size();i++){
                System.out.println("serviciosHon-"+serviciosHon.get(i).getServicioPrestado().getIdFolio());
            }
            for(int i=0; i<serviciosPers.size();i++){
                System.out.println("serviciosPers-"+serviciosPers.get(i).getServicioPrestado().getIdFolio());
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
                this.obtieneEquipo();
            } catch (Exception ex) {
                Logger.getLogger(CalculaPagoHonorariosJB.class.getName()).log(Level.SEVERE, null, ex);
            }
            this.calculaCuenta();
        }
    }
    
    public void onRowEdit(RowEditEvent event) {
        DetalleHonorarios oServP= (DetalleHonorarios)event.getObject();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Cargo modificado",oServP.getServicioPrestado().getConcepPrestado().getDescripConcep()));
        String msj="";
        try {
            if(oServP.getServicioPrestado().getCostoCobrado()>=0){
                msj= oServP.getServicioPrestado().modificarCosto();
                msj= msj.substring(2, msj.length()-1);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Autorizar pago de honorarios", msj));
            }
            if(oServP.getAutorizado() >=0){
                msj= oServP.modificaMontoAutorizadoDetalleHonorarios();
                msj= msj.substring(2, msj.length()-1);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Autorizar pago de honorarios", msj));
            }
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Autorizar pago de honorarios", "Error: Nose pudo actualizar el cargo."));    
        }
        this.calculaCuenta();
     }
    
    public void autorizarPagoHonorarios() throws Exception{
        try{
            for(int i=0; i<detallesHonorarios.size(); i++){
                if(detallesHonorarios.get(i).getPagoHonorarios().getPersonalHospitalario().getFolioPers()==selectedDistribucionRegalias.getPersonal().getFolioPers()){
                    System.out.println("IdPagoHon: "+detallesHonorarios.get(i).getPagoHonorarios().getIdPagoHonorarios()+" FolioPers: "+detallesHonorarios.get(i).getPagoHonorarios().getPersonalHospitalario().getFolioPers());
                    detallesHonorarios.get(i).getPagoHonorarios().setAutoriz(new Date());
                    String msj= detallesHonorarios.get(i).getPagoHonorarios().autorizarPagoHonorarios();
                    msj= msj.substring(2, msj.length()-1);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Autorizar pago de honorarios", msj));
                }
            }
        }catch(Exception ex){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Autorizar pago de honorarios", "Error: no se pueden autorizar los pagos de honorarios, favor de verificar sus daotos."));    
        }
    }
    
//--------------------------------------------------------------------------------------------------------------
        
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
    
    public void obtieneEquipo() throws Exception{
        List<String> equipo= new ArrayList<String>();
        List<PersonalHospitalario> tmpPh= new ArrayList<PersonalHospitalario>();
        for(int i=0; i<serviciosHon.size(); i++){
            equipo.clear();
            tmpPh.clear();
            tmpPh= serviciosHon.get(i).getServicioPrestado().buscaEquipoPersonalMedUrgencia();
            for(int k=0; k<tmpPh.size();k++){
                if(tmpPh.get(k).getFolioPers()!= this.selectedDistribucionRegalias.getPersonal().getFolioPers()){
                    equipo.add(tmpPh.get(k).getNombreCompleto());
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
    
    public void calculaCuenta(){
        this.totalHonorarios=0;
        for(int i=0; i<serviciosPers.size();i++){
            totalHonorarios+=serviciosPers.get(i).getAutorizado();
        }
        for(int i=0; i<serviciosHon.size(); i++){
            this.totalHonorarios+=serviciosHon.get(i).getAutorizado();
        }
        System.out.println("Total honorarios:"+this.totalHonorarios);
    }
    
   //=============== SET & GET ===============//    
    public DistribucionRegalias getDist() {
        return new DistribucionRegalias();
    }

    public List<String> getNameColumnas() {
        return nameColumnas;
    }
    public void setNameColumnas(List<String> nameColumnas) {
        this.nameColumnas = nameColumnas;
    }

    public List<DistribucionRegalias> getDistribucion() {
        return distribucion;
    }
    public void setDistribucion(List<DistribucionRegalias> distribucion) {
        this.distribucion = distribucion;
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