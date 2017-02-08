package org.apli.jbs.facturacion;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apli.modelbeans.Ciudad;
import org.apli.modelbeans.CiudadCP;
import org.apli.modelbeans.Estado;
import org.apli.modelbeans.Municipio;
import org.apli.modelbeans.facturacion.cfdi.v32.schema.Comprobante;
/**
 * Autor: Isabel Espinoza Espinoza
 * Fecha: Abril 2014
 */
@ManagedBean(name="oReceptor")
@ViewScoped
public class ReceptorJB  implements Serializable{
    private boolean bDatoHabilitado;
    private boolean bBotonActivado;
    private String sMensajeError="";
    private boolean valida=false;
    private Comprobante.Receptor oReceptorSeleccionado=new Comprobante.Receptor();
    public List<Comprobante.Receptor> receptores;  
    public CiudadCP oCiudadCP=new CiudadCP();
    Estado estado;
    Estado oEstado;
    Municipio municipio;
    Municipio oMunicipio;
    Ciudad ciudad;
    Ciudad oCiudad;
    String sCodigoPostal;
    public ReceptorJB() {
        try {
            receptores = new Comprobante.Receptor().getTodosReceptores();
            EstadoJB.listEstadosDF=new Estado().getEstados();
            MunicipioJB.listMunicipiosDF=new ArrayList<Municipio>();
            CiudadJB.listCiudadesDF=new ArrayList<Ciudad>();
        } catch (Exception ex) {
            Logger.getLogger(ReceptorJB.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 
    public void crearReceptor() throws Exception{
        EstadoJB.listEstadosDF=new Estado().getEstados();
        oReceptorSeleccionado=new Comprobante.Receptor();
        bBotonActivado=true;
        bDatoHabilitado=false;
        sCodigoPostal=null;
        oEstado=null;
        oMunicipio=null;
        oCiudad=null;
    }
    public void insertar() throws Exception {   
        valida=true;
        if(oReceptorSeleccionado.getRfc().equals("")||
                oReceptorSeleccionado.getRfc()==null || 
                oReceptorSeleccionado.getRfc().length()<12){
            FacesMessage msj2= new FacesMessage(FacesMessage.SEVERITY_ERROR,"RFC: Error de validación, se necesita un valor de 12 a 13 caracteres.","");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
            valida=false;
        }
        if (bDatoHabilitado==true && oReceptorSeleccionado.getDomicilio().getEdo()==null){
            FacesMessage msj2= new FacesMessage(FacesMessage.SEVERITY_ERROR,"Debe seleccionar un estado","");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
            valida=false;
        }
        if(valida){
            if(oReceptorSeleccionado.getDomicilio().getEdo()==null)
                oReceptorSeleccionado.getDomicilio().setEdo(new Estado());
            if(oReceptorSeleccionado.getDomicilio().getMun()==null)
                oReceptorSeleccionado.getDomicilio().setMun(new Municipio());
            if(oReceptorSeleccionado.getDomicilio().getCd()==null)
                oReceptorSeleccionado.getDomicilio().setCd(new Ciudad());
            String respuesta=oReceptorSeleccionado.insertarReceptor();
            if (respuesta.contains("ERROR")){
                FacesMessage msj2= new FacesMessage(FacesMessage.SEVERITY_ERROR,respuesta,"");
                FacesContext.getCurrentInstance().addMessage(null, msj2);
            }else{
                FacesMessage msj2= new FacesMessage("Se registro correctamente el receptor");
                FacesContext.getCurrentInstance().addMessage(null, msj2);
                receptores = new Comprobante.Receptor().getTodosReceptores();
                bBotonActivado=false;
            }
        }
    }
    public void habilitarActualizar(Comprobante.Receptor rec) throws Exception{
        EstadoJB.listEstadosDF=new Estado().getEstados();
        oReceptorSeleccionado=rec;
        bDatoHabilitado=false;
        bBotonActivado=true;
        estado=new Estado();
        ciudad=new Ciudad();
        municipio=new Municipio();
        sCodigoPostal=null;
        oEstado=null;
        oMunicipio=null;
        oCiudad=null;
        String sCodigoPostalAux="";
        if(oReceptorSeleccionado.getDomicilio()!=null){
            if(oReceptorSeleccionado.getDomicilio().getCodigoPostal()!=null){
                bDatoHabilitado=true;
                sCodigoPostal=oReceptorSeleccionado.getDomicilio().getCodigoPostal();
                for(int x=0;x<sCodigoPostal.length();x++){
                    if(sCodigoPostal.charAt(x)!=' ')
                        sCodigoPostalAux=sCodigoPostalAux+sCodigoPostal.charAt(x);
                }
                if(sCodigoPostalAux.length()>0){
                    sCodigoPostal=sCodigoPostalAux;
                    this.buscaDomicilioPorCP();
                }
            }
            if(oReceptorSeleccionado.getDomicilio().getEdo().getCve()!=null && sCodigoPostal==null){
                bDatoHabilitado=true;
                buscarDomicilioPorEdo();
                if(oReceptorSeleccionado.getDomicilio().getMun().getCve()!=null)
                    this.buscarDomicilioPorMun();
                if(oReceptorSeleccionado.getDomicilio().getCd()!=null){
                    if(oReceptorSeleccionado.getDomicilio().getCd().getCve()!=null)
                        oCiudad=oReceptorSeleccionado.getDomicilio().getCd();
                }
            }
        }
        
    }
    public void modificar() throws Exception{
        if (oEstado!=null){
            oReceptorSeleccionado.getDomicilio().setEdo(oEstado);
        }else{
            oReceptorSeleccionado.getDomicilio().setEdo(new Estado());
        }
        if (oMunicipio!=null){
            oReceptorSeleccionado.getDomicilio().setMun(oMunicipio);
        }else{
            oReceptorSeleccionado.getDomicilio().setMun(new Municipio());
        }
        if (oCiudad!=null){
            oReceptorSeleccionado.getDomicilio().setCd(oCiudad);
        }else{
            oReceptorSeleccionado.getDomicilio().setCd(new Ciudad());
        }
        valida=true;
        if(oReceptorSeleccionado.getRfc().equals("")||
                oReceptorSeleccionado.getRfc()==null || 
                oReceptorSeleccionado.getRfc().length()<12){
            FacesMessage msj2= new FacesMessage(FacesMessage.SEVERITY_ERROR,"RFC: Error de validación, se necesita un valor de 12 a 13 caracteres.","");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
            valida=false;
        }
        if (bDatoHabilitado==true && oReceptorSeleccionado.getDomicilio().getEdo()==null){
            FacesMessage msj2= new FacesMessage(FacesMessage.SEVERITY_ERROR,"Debe seleccionar un estado","");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
            valida=false;
        }
        if (valida){
            String respuesta=oReceptorSeleccionado.modificarReceptor();
            if (respuesta.contains("ERROR")){
                    FacesMessage msj2= new FacesMessage(FacesMessage.SEVERITY_ERROR,respuesta,"");
                    FacesContext.getCurrentInstance().addMessage(null, msj2);
            }else{
                    FacesMessage msj2= new FacesMessage("Se modifico correctamente el receptor");
                    FacesContext.getCurrentInstance().addMessage(null, msj2);
                    bBotonActivado=false;
            }
        }
    }
    public void verificarEliminacion(Comprobante.Receptor oRece) throws Exception{
        String mensaje=oRece.verificaPosibilidadEmilinar();
        if(mensaje.contains("ERROR")){
            this.sMensajeError="No se puede eliminar el receptor porque ya hay elementos asociados a él";
            bDatoHabilitado=false;
            bBotonActivado=false;
        }else{
            bDatoHabilitado=true;
            bBotonActivado=true;
        }
    }
    public void eliminar() throws Exception{
            String respuesta=oReceptorSeleccionado.eliminarReceptor();
            if (respuesta.contains("ERROR")){
                FacesMessage msj2= new FacesMessage(FacesMessage.SEVERITY_ERROR,respuesta,"");
                FacesContext.getCurrentInstance().addMessage(null, msj2);
            }else{
                FacesMessage msj2= new FacesMessage("Se elimino correctamente el receptor");
                FacesContext.getCurrentInstance().addMessage(null, msj2);
                receptores = new Comprobante.Receptor().getTodosReceptores();
                bBotonActivado=false;
            }
    }
    public void buscaDomicilioPorCP() throws Exception{
        sCodigoPostal=oReceptorSeleccionado.getDomicilio().getCodigoPostal();
        String sCodigoPostalAux="";
        for(int x=0;x<sCodigoPostal.length();x++){
            if(sCodigoPostal.charAt(x)!=' ')
                sCodigoPostalAux=sCodigoPostalAux+sCodigoPostal.charAt(x);
        }
        sCodigoPostal=sCodigoPostalAux;
        if(!sCodigoPostal.equals("")){
            List<Estado> lista=new ArrayList<Estado>();
            lista=oCiudadCP.buscaPorCP(sCodigoPostal); //lista de estados
            if(!lista.isEmpty()){
                if(lista.size()==1){
                    oReceptorSeleccionado.getDomicilio().setEdo(lista.get(0));
                    oEstado=oReceptorSeleccionado.getDomicilio().getEdo();
                    MunicipioJB.listMunicipiosDF=oCiudadCP.buscaPorEdo(sCodigoPostal, oEstado.getCve());
                }
            }else{
                oReceptorSeleccionado.getDomicilio().setEdo(new Estado());
                oReceptorSeleccionado.getDomicilio().setMun(new Municipio());
                oReceptorSeleccionado.getDomicilio().setCd(new Ciudad());
                return;
            }
            if(!MunicipioJB.listMunicipiosDF.isEmpty())
                if(MunicipioJB.listMunicipiosDF.size()==1){
                    oReceptorSeleccionado.getDomicilio().setMun(MunicipioJB.listMunicipiosDF.get(0));
                    oMunicipio=oReceptorSeleccionado.getDomicilio().getMun();
                    CiudadJB.listCiudadesDF=oCiudadCP.buscaPorMun(  sCodigoPostal, oEstado.getCve(),oMunicipio.getCve());
                    if(!CiudadJB.listCiudadesDF.isEmpty())
                        if(CiudadJB.listCiudadesDF.size()==1){
                            oReceptorSeleccionado.getDomicilio().setCd(CiudadJB.listCiudadesDF.get(0));   
                            oCiudad=oReceptorSeleccionado.getDomicilio().getCd();
                        }
                }
            oReceptorSeleccionado.getDomicilio().setObjPais(oCiudadCP.buscaPais(sCodigoPostal));   
        }else{
            oReceptorSeleccionado.getDomicilio().setEdo(new Estado());
            oReceptorSeleccionado.getDomicilio().setMun(new Municipio());
            oReceptorSeleccionado.getDomicilio().setCd(new Ciudad());
            MunicipioJB.listMunicipiosDF=new ArrayList<Municipio>();
            CiudadJB.listCiudadesDF=new ArrayList<Ciudad>();
        }
    }
    public void buscarDomicilioPorEdo()throws Exception{
        oEstado=oReceptorSeleccionado.getDomicilio().getEdo();
        List<Municipio> municipios=new ArrayList<Municipio>();
        List<Ciudad> ciudades=new ArrayList<Ciudad>();
        boolean bandera=false;
        if(sCodigoPostal!=null){
            if(!sCodigoPostal.equals("")){
                municipios=oCiudadCP.buscaPorEdo( sCodigoPostal,oEstado.getCve());
                if(!municipios.isEmpty()){
                    MunicipioJB.listMunicipiosDF=municipios;
                    if(municipios.size()==1){
                        oReceptorSeleccionado.getDomicilio().setMun(municipios.get(0));
                        oMunicipio=oReceptorSeleccionado.getDomicilio().getMun();
                        ciudades=oCiudadCP.buscaPorMun( sCodigoPostal, oEstado.getCve(), oMunicipio.getCve());
                        if(!ciudades.isEmpty())
                            CiudadJB.listCiudadesDF=ciudades;
                    }
                }else{
                   oReceptorSeleccionado.getDomicilio().setMun(new Municipio()); 
                   oReceptorSeleccionado.getDomicilio().setCd(new Ciudad());
                }
                oReceptorSeleccionado.getDomicilio().setObjPais(oCiudadCP.buscaPais(sCodigoPostal));
            }else{
                bandera=true;
            }
        }
        if(oEstado!=null || bandera){
            if(!oEstado.getCve().equals("") || bandera){
                municipios=oCiudadCP.buscaPorEdo(oEstado.getCve());
                if(!municipios.isEmpty()){
                    MunicipioJB.listMunicipiosDF=municipios;
                    if(municipios.size()==1){
                        oReceptorSeleccionado.getDomicilio().setMun(municipios.get(0));
                        oMunicipio=oReceptorSeleccionado.getDomicilio().getMun();
                        ciudades=oCiudadCP.buscaPorMun( oEstado.getCve(),oMunicipio.getCve());
                        if(!ciudades.isEmpty())
                            CiudadJB.listCiudadesDF=ciudades;
                    }  
                }
            }
        }
    }
    public void buscarDomicilioPorMun()throws Exception{   
        boolean bandera=false;
        oMunicipio=oReceptorSeleccionado.getDomicilio().getMun();
        List<Ciudad> ciudades=new ArrayList<Ciudad>();
        if(sCodigoPostal!=null && oEstado.getCve()!=null && oMunicipio.getCve()!=null){
            if(!sCodigoPostal.equals("") && !oEstado.getCve().equals("") && !oMunicipio.getCve().equals("")){
                ciudades=oCiudadCP.buscaPorMun( sCodigoPostal,oEstado.getCve(),oMunicipio.getCve());
                if(!ciudades.isEmpty())
                    CiudadJB.listCiudadesDF=ciudades;
            }
            if(sCodigoPostal.equals("") && !oEstado.getCve().equals("") && !oMunicipio.getCve().equals(""))
                bandera=true;
        }
        if((oEstado.getCve()!=null && oMunicipio.getCve()!=null)||bandera){
            if(!oEstado.getCve().equals("")&& !oMunicipio.getCve().equals("")||bandera){
                ciudades=oCiudadCP.buscaPorMun(oEstado.getCve(),oMunicipio.getCve());
                if(!ciudades.isEmpty())
                    CiudadJB.listCiudadesDF=ciudades;
            }
        }
    }
    public void actCiudad()throws Exception{
        oCiudad=oReceptorSeleccionado.getDomicilio().getCd();
    }
    public boolean isBotonActivado() {
        return bBotonActivado;
    }
    public void setBotonActivado(boolean bBotonActivado) {
        this.bBotonActivado = bBotonActivado;
    }
    public String getMensajeError() {
        return sMensajeError;
    }
    public void setMensajeError(String sMensajeError) {
        this.sMensajeError = sMensajeError;
    }
    public boolean isDatoHabilitado() {
        return bDatoHabilitado;
    }
    public void setDatoHabilitado(boolean bCancelacionEliminacion) {
        this.bDatoHabilitado = bCancelacionEliminacion;
    }
    public Comprobante.Receptor getReceptorSeleccionado() {
        return oReceptorSeleccionado;
    }
    public void setReceptorSeleccionado(Comprobante.Receptor emisor) {
        this.oReceptorSeleccionado = emisor;
    }
    public List<Estado> getListEdo() throws Exception {
        return EstadoJB.listEstadosDF;
    }
    public List<Municipio> getListMun() throws Exception {
        return MunicipioJB.listMunicipiosDF;
    }
    public void setListMun(List<Municipio> listMunDF) {
        MunicipioJB.listMunicipiosDF = listMunDF;
    }
    public List<Ciudad> getListCd() {
        return CiudadJB.listCiudadesDF;
    }
    public void setListCd(List<Ciudad> listCdDF) {
        CiudadJB.listCiudadesDF = listCdDF;
    }
    public void setListEdo(List<Estado> listEdoDF) {
        EstadoJB.listEstadosDF = listEdoDF;
    }
    public List<Comprobante.Receptor> getReceptores() {
        return receptores;
    }
    public void setReceptores(List<Comprobante.Receptor> receptores) {
        this.receptores = receptores;
    }
    public void setListEdoDF(List<Estado> listEdoDF) {
        EstadoJB.listEstadosDF = listEdoDF;
    }
}