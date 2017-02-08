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
import org.apli.modelbeans.EpisodioMedico;
import org.apli.modelbeans.ServicioPrestado;
import org.apli.modelbeans.Utilidad;
import org.primefaces.event.RowEditEvent;
import org.apli.modelbeans.Diagnostico;
 /**
 * ConsultaJB.java
 * JSF Managed Bean archivo que realiza el cierre de una cuenta
 * @author Humberto Marin Vega
 * Fecha: Junio 2014
 */
@ManagedBean(name="oCerrarCta")
@ViewScoped
public class CerrarCuentaJB  implements Serializable{
    private Date fecha;
    private Paciente oPac;
    private EpisodioMedico oEpMed;
    
    private Paciente[] pacientes;
    private List<String> nomPacientes;
    private List<ServicioPrestado> serviciosNoPagados;
    private List<ServicioPrestado> serviciosPaquete;
    private List<ServicioPrestado> serviciosPagosExterno;
    private List<ServicioPrestado> serviciosAnticipo;
    
    private List<ServicioPrestado> selectedServiciosParaDesc;
    private boolean disableDesc=false;
    private boolean disableTipo=true;
    private float descPorc;
    private float descPesos;
    private String sObsPrecio="";
    
    private String paciente;
    private float subtotal=0;
    private float iva=0;
    private float totalCuenta=0;
    private float totalPendientePago=0;
    private float totalPagos=0;
    private Utilidad oUtilidad;
    private boolean disable;

    
    public CerrarCuentaJB(){
        oPac= new Paciente();
        oEpMed= new EpisodioMedico();
        oUtilidad= new Utilidad();
        nomPacientes= new ArrayList<String>();
        this.serviciosNoPagados = new ArrayList <ServicioPrestado>();
        this.serviciosPaquete = new ArrayList<ServicioPrestado>();
        this.serviciosPagosExterno = new ArrayList <ServicioPrestado>();
        this.serviciosAnticipo = new ArrayList <ServicioPrestado>();
        this.selectedServiciosParaDesc = new ArrayList <ServicioPrestado>();
        obtieneFecha();
        obtienePacientesCtaAbierta();
        this.disable=false;
    }
    
    private void obtienePacientesCtaAbierta(){
        try{
            pacientes= oPac.buscarPacientesCuentaAbierta();
            for(int i=0; i<pacientes.length;i++){
                nomPacientes.add(pacientes[i].getNombre()+" "+pacientes[i].getApellidoPaterno()+" "+pacientes[i].getApellidoMaterno());
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    public void datosEpisodio(){
        for(int i=0; i<pacientes.length;i++){
            if((pacientes[i].getNombre()+" "+pacientes[i].getApellidoPaterno()+" "+pacientes[i].getApellidoMaterno()).equals(paciente)){
                try {
                    oPac=pacientes[i];
                   //EpisodioMedico oEM= new EpisodioMedico();
                   oEpMed=oEpMed.datosCierreCuenta(oPac.getFolioPac());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
       }
        iva=0;
        totalCuenta=0;
        totalPagos=0;
        totalPendientePago=0;
        subtotal=0;
        obtieneServicios();
        this.disable=false;
    }
    
    public void obtieneServicios(){
        ServicioPrestado oSP= new ServicioPrestado();
        oSP.setEpisodioMedico(oEpMed);
        oSP.setPaciente(oPac);
        try {
            this.serviciosNoPagados = oSP.buscaServiciosPrestadosNoPagadosPorEpisodioMed();
            this.serviciosPagosExterno = oSP.buscaServiciosPrestadosPorEpisodioMedPagosExterno();
            this.serviciosAnticipo = oSP.buscaServiciosPrestadosPorEpisodioMedAnticipos();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        obtieneServiciosPaquete();
        calculaCuenta();
    }
    
    public void obtieneServiciosPaquete(){
        List<ServicioPrestado> servicios = new ArrayList<ServicioPrestado>();
        servicios.addAll(this.serviciosNoPagados);
        serviciosNoPagados.clear();
        for(int i=0; i<servicios.size(); i++){
            if(servicios.get(i).getQuienPaga()==2){
                this.serviciosPaquete.add(servicios.get(i));
            }else{
                this.serviciosNoPagados.add(servicios.get(i));
            }
        }
        
    }
    
   public void calculaCuenta(){
        float preciosiniva=0;
        subtotal=0;
        iva=0;
        totalCuenta=0;
        totalPagos=0;
        for(int i=0; i< this.serviciosNoPagados.size();i++){
            preciosiniva=( this.serviciosNoPagados.get(i).getCostoCobrado()*100)/(100+ this.serviciosNoPagados.get(i).getPctIVA());
            subtotal+=preciosiniva;
            iva+=preciosiniva*this.serviciosNoPagados.get(i).getPctIVA()/100;
            totalCuenta+= this.serviciosNoPagados.get(i).getCostoCobrado();
        }
        for(int i=0; i<this.serviciosPagosExterno.size(); i++){
            preciosiniva=( this.serviciosPagosExterno.get(i).getCostoCobrado()*100)/(100+this.serviciosPagosExterno.get(i).getPctIVA());
            subtotal+=preciosiniva;
            iva+=preciosiniva*this.serviciosPagosExterno.get(i).getPctIVA()/100;
            totalCuenta+=this.serviciosPagosExterno.get(i).getCostoCobrado();
            totalPagos+=this.serviciosPagosExterno.get(i).getCostoCobrado();
        }
        for(int i=0; i<this.serviciosAnticipo.size(); i++){
            totalPagos+=this.serviciosAnticipo.get(i).getCostoCobrado();
        }
        totalCuenta=subtotal+iva;
        totalPendientePago=totalCuenta-totalPagos;
    }
    
    public void cerrarCuenta(){
        String msj;
        if(verificaPedidosFarmacia()){
            if(totalPendientePago==0){
                if (this.oEpMed.getDxEgreso()==null || 
                    this.oEpMed.getDxEgreso().getCve() == null ||
                    this.oEpMed.getDxEgreso().getCve().startsWith("9999"))
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Falta diagnóstico de egreso" ));
                else{
                    try {
                        System.out.println("Tipo alta"+ this.oEpMed.getTipoAlta());
                        System.out.println("Diag Egre "+ this.oEpMed.getDxEgreso().getCve());
                        msj=this.oEpMed.cerrarCuenta(this.oPac.getFolioPac());
                        msj= msj.substring(2, msj.length()-1);
                        if (msj.contains("ERROR")){
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Error en base de datos, no se puede cerrar cuenta" ));
                            System.out.println(msj);
                        }else{
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Cuenta cerrada", msj ));
                            this.disable=true;
                            this.disableDesc=false;
                        }
                    } 
                    catch (Exception ex) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se puede cerrar cuenta" ));
                        ex.printStackTrace();
                    }
                }
            }else{
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Cerrar cuenta", "No se puede cerrar cuenta, la cuenta no ha sido liquidada en su totalidad." ));
            }
        }else{
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cerrar cuenta", "No se puede cerrar cuenta, se encuentran pedidos enviados a farmacia." ));
        }
    }
    
    public boolean verificaPedidosFarmacia(){
        boolean cerrar=true;
        for(int i=0; i<this.serviciosNoPagados.size(); i++){
            if(serviciosNoPagados.get(i).getSitFarmacia().equals("E") || serviciosNoPagados.get(i).getSitFarmacia().equals("e")){
                cerrar=false;
            }
        }
        for(int i=0; i<this.serviciosPagosExterno.size(); i++){
            if(serviciosPagosExterno.get(i).getSitFarmacia().equals("E") || serviciosPagosExterno.get(i).getSitFarmacia().equals("e")){
                cerrar=false;
            }
        }
        return cerrar;
    }
    
    private void obtieneFecha(){
        fecha= new Date();
    }  
    
    public void noSeleccionado(){
         System.out.println("Sel.desc.: "+this.selectedServiciosParaDesc.size());
    }
    
    public void aplicaDescuentoPorc(){
        boolean encontrado=false;
        if(this.selectedServiciosParaDesc.isEmpty()){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Cerrar cuenta", "Seleccione algun concepto para aplicar el descuento." ));    
        }else if( sObsPrecio.isEmpty()){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Cerrar cuenta", "Escriba las observaciones para el descuento." ));
        }else{
            try{
                oUtilidad.setVariable("CveAutModifMonto");
                encontrado =oUtilidad.buscaCveAutSalidadCredito();
                if(encontrado){
                    System.out.println("Sel.desc.: "+this.selectedServiciosParaDesc.size());
                    System.out.println("No.Desc.: "+this.descPorc);
                    for(int i=0; i<this.selectedServiciosParaDesc.size(); i++){
                        selectedServiciosParaDesc.get(i).setObsPrecio(sObsPrecio);
                        selectedServiciosParaDesc.get(i).setCostoCobrado( selectedServiciosParaDesc.get(i).getCostoCobrado()-selectedServiciosParaDesc.get(i).getCostoCobrado()*descPorc/100);
                        String msj = selectedServiciosParaDesc.get(i).modificarObsPrecio();
                        String msj2= selectedServiciosParaDesc.get(i).modificarCosto();
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Cerrar cuenta", "Se aplico el descuento." ));
                    }
                }else{
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Cerrar cuenta", "No se pudo autorizar el descuento." ));
                }
            } catch (Exception ex) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Cerrar cuenta", "No se pudo aplicar el descuento." ));
                ex.printStackTrace();
            }
        }
    }
    
    public void aplicaDescuentoPesos(){
        boolean encontrado=false;
        if(this.selectedServiciosParaDesc.isEmpty()){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Cerrar cuenta", "Seleccione algun concepto para aplicar el descuento." ));    
        }else if( sObsPrecio.isEmpty()){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Cerrar cuenta", "Escriba las observaciones para el descuento." ));
        }else{
            try {
                oUtilidad.setVariable("CveAutModifMonto");
                encontrado =oUtilidad.buscaCveAutSalidadCredito();
                if(encontrado){
                    System.out.println("Sel.desc.: "+this.selectedServiciosParaDesc.size());
                    System.out.println("No.Desc.: "+this.descPesos); 
                    System.out.println("Obs."+this.sObsPrecio);
                    float descuento= descPesos/selectedServiciosParaDesc.size();
                    for(int i=0; i<selectedServiciosParaDesc.size(); i++){
                        selectedServiciosParaDesc.get(i).setObsPrecio(sObsPrecio);
                        selectedServiciosParaDesc.get(i).setCostoCobrado( selectedServiciosParaDesc.get(i).getCostoCobrado()-descuento );
                
                        String msj = selectedServiciosParaDesc.get(i).modificarObsPrecio();
                        String msj2= selectedServiciosParaDesc.get(i).modificarCosto();
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Cerrar cuenta", "Se aplico el descuento." ));
                    }
                }else{
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Cerrar cuenta", "No se pudo autorizar el descuento." ));
                }
            } catch (Exception ex) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Cerrar cuenta", "No se pudo aplicar el descuento." ));
                ex.printStackTrace();
            }
        }
    }
    
    public void limpiarAgenda(){
         this.paciente="";
         this.oEpMed= new EpisodioMedico();
         this.oPac= new Paciente();
         this.serviciosNoPagados.clear();
         this.serviciosPaquete.clear();
         this.serviciosPagosExterno.clear();
         this.serviciosAnticipo.clear();
         this.nomPacientes.clear();
         obtienePacientesCtaAbierta();
         this.subtotal=0;
         this.iva=0;
         this.totalCuenta=0;
         this.totalPagos=0;
         this.totalPendientePago=0;
         this.descPesos=0;
         this.descPorc=0;
         this.sObsPrecio="";
         this.selectedServiciosParaDesc.clear();
         this.disableDesc=false;
    }
    
     public void onRowEdit(RowEditEvent event) {
         ServicioPrestado oServP= (ServicioPrestado)event.getObject();
         String msj="";
         boolean encontrado=false;
         try {
            oUtilidad.setVariable("CveAutModifMonto");
            encontrado =oUtilidad.buscaCveAutSalidadCredito();
            if(encontrado){
                msj= oServP.modificarCosto();
                System.out.println(msj);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Cardo modificado", oServP.getConcepPrestado().getDescripConcep() ));
            }else{
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Cerrar cuenta", "No se puede realizar el cambio." ));
            }
         } catch (Exception ex) {
             Logger.getLogger(CerrarCuentaJB.class.getName()).log(Level.SEVERE, null, ex);
         }
         this.obtieneServicios();
     }
    

    public boolean isDisable() {
       return disable;
    }
    public void setDisable(boolean disable) {
       this.disable = disable;
    }
    
    //=============== SET & GET ===============//
    
    public List<String> getPacientes(){
        return nomPacientes;
    }
    
    public Date getFecha(){
        return fecha;
    }
    public void setFecha(Date fecha){
        this.fecha=fecha;
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
    
    public List<ServicioPrestado> getServiciosPaquete() {
        return serviciosPaquete;
    }
    public void setServiciosPaquete(List<ServicioPrestado> serviciosPaquete) {
        this.serviciosPaquete = serviciosPaquete;
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
    
    public String getNombrePaciente(){
        return paciente;
    }
    public void setNombrePaciente(String nompaciente){
        this.paciente=nompaciente;
    }

    //--- cuenta
    public float getSubtotal() {
        return subtotal;
    }
    public void setSubtotal(float subtotal) {
        this.subtotal = subtotal;
    }

    public float getIva() {
        return iva;
    }
    public void setIva(float iva) {
        this.iva = iva;
    }

    public float getTotalCuenta() {
        return totalCuenta;
    }
    public void setTotalCuenta(float totalCuenta) {
        this.totalCuenta = totalCuenta;
    }

    public float getTotalPendientePago() {
        return totalPendientePago;
    }
    public void setTotalPendientePago(float totalPendientePago) {
        this.totalPendientePago = totalPendientePago;
    }

    public float getTotalPagos() {
        return totalPagos;
    }
    public void setTotalPagos(float totalPagos) {
        this.totalPagos = totalPagos;
    }

    public List<ServicioPrestado> getSelectedServiciosParaDesc() {
        return selectedServiciosParaDesc;
    }
    public void setSelectedServiciosParaDesc(List<ServicioPrestado> selectedServiciosParaDesc) {
        this.selectedServiciosParaDesc = selectedServiciosParaDesc;
    }   

    public boolean isDisableDesc() {
        return disableDesc;
    }
    public void setDisableDesc(boolean disableDesc) {
        this.disableDesc = disableDesc;
    }

    public boolean isDisableTipo() {
        return disableTipo;
    }
    public void setDisableTipo(boolean disableTipo) {
        this.disableTipo = disableTipo;
    }

    public float getDescPorc() {
        return descPorc;
    }
    public void setDescPorc(float descPorc) {
        this.descPorc = descPorc;
    }

    public float getDescPesos() {
        return descPesos;
    }
    public void setDescPesos(float descPesos) {
        this.descPesos = descPesos;
    }

    public String getObsPrecio() {
        return sObsPrecio;
    }
    public void setObsPrecio(String sObsPrecio) {
        this.sObsPrecio = sObsPrecio;
    }
    
    public Utilidad getUtilidad() {
        return oUtilidad;
    }
    public void setUtilidad(Utilidad oUtilidad) {
        this.oUtilidad = oUtilidad;
    }
    
    public List<Diagnostico> getDiagnosticos() {
        return ListaDiagnosticoJB.diagnosticos;
    }
    public void setDiagnosticos(List diagnosticos) {
        ListaDiagnosticoJB.diagnosticos = diagnosticos;
    }
}