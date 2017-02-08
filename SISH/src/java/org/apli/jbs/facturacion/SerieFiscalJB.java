package org.apli.jbs.facturacion;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import org.apli.modelbeans.facturacion.cfdi.v32.schema.SerieFiscal;
/**
 * Clase para ABC de series fiscales
 * Autor: Isabel Espinoza Espinoza
 * Fecha: Abril 2014
 */
@ManagedBean(name="oSerieFiscal")
@SessionScoped
public class SerieFiscalJB  implements Serializable{
    private boolean valida=false;
    private boolean error;
    private FacesMessage msj  = new FacesMessage();
    private Pattern p;
    private Matcher m;
    private String sResult;
    static private SerieFiscal oSerie=new SerieFiscal();
    private List<SerieFiscal> oSeries;
    private boolean bCancelacionEliminacion;
    private boolean bBotonActivado;
    private String sMensajeError="";
    
    public void limpiar(){
        valida=false;
        error=false;
        msj  = new FacesMessage();
        p=null;
        m=null;
        bBotonActivado=true;
        sResult="";
    }
    public void crearSerie(){
        oSerie=new SerieFiscal();
        bBotonActivado=true;
    }
    public SerieFiscalJB() {
        try {
            oSeries = new SerieFiscal().getSeriesFiscales();
        } catch (Exception ex) {
            Logger.getLogger(SerieFiscalJB.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 
    public void insertar() throws Exception{
        valida=true;
        System.out.println("nombre "+oSerie.getNombre()+" Tipo "+oSerie.getTipo());
        if(oSerie.getNombre()==null || (oSerie.getNombre()!=null && oSerie.getNombre().equals(""))){
            String rst="Nombre: Error de validación, se necesita un valor.";
            FacesMessage msj2= new FacesMessage(FacesMessage.SEVERITY_ERROR,rst,"");
            FacesContext.getCurrentInstance().addMessage(null, msj2);            
            valida=false;
        }
        if(oSerie.getTipo()==null){
            String rst="Tipo: Error de validación, se necesita un valor.";
            FacesMessage msj2= new FacesMessage(FacesMessage.SEVERITY_ERROR,rst,"");
            FacesContext.getCurrentInstance().addMessage(null, msj2);            
            valida=false;
        } 
        System.out.println("serie "+oSerie.getFolio());
        if(valida){
            String mensaje=oSerie.insertarSerieFiscal();
            if(mensaje.contains("ERROR")){
                FacesMessage msj2= new FacesMessage(FacesMessage.SEVERITY_ERROR,mensaje,"");
                FacesContext.getCurrentInstance().addMessage(null, msj2);                            
            }else{
                FacesMessage msj2= new FacesMessage("Se ha creado la serie");
                FacesContext.getCurrentInstance().addMessage(null, msj2);   
                oSeries = new SerieFiscal().getSeriesFiscales();
                bBotonActivado=false;
            }
        }
    }    
    public void verificarEliminacionCancelacion(SerieFiscal oSerieN) throws Exception{
        String mensaje=oSerieN.verificaPosibilidadEliminarActualizar();
        if(mensaje.contains("ERROR")){
            this.sMensajeError="No es posible eliminar o actualizar la serie fiscal porque ya hay facturas creadas para esta serie";
            bCancelacionEliminacion=false;
            bBotonActivado=false;
        }else{
            bCancelacionEliminacion=true;
            bBotonActivado=true;
        }
    }
    public void eliminar() throws Exception{
            String mensaje=oSerie.eliminarSerieFiscal();
            if(mensaje.contains("ERROR")){
                    FacesMessage msj2= new FacesMessage(FacesMessage.SEVERITY_ERROR,mensaje,"");
                    FacesContext.getCurrentInstance().addMessage(null, msj2);                            
            }else{
                    FacesMessage msj2= new FacesMessage("Se ha eliminado la serie");
                    FacesContext.getCurrentInstance().addMessage(null, msj2);  
                    oSeries = new SerieFiscal().getSeriesFiscales();
                    bBotonActivado=false;
            }
            limpiar();
        
    }
    public void modificar() throws Exception{
            String mensaje;
            if(oSerie.getTipo()==null){
                FacesMessage msj2= new FacesMessage(FacesMessage.SEVERITY_ERROR,"Tipo: Error de validación, se necesita un valor.","");
                FacesContext.getCurrentInstance().addMessage(null, msj2);            
            } else{
                mensaje=oSerie.modificarSerieFiscal();
                if(mensaje.contains("ERROR")){
                    FacesMessage msj2= new FacesMessage(FacesMessage.SEVERITY_ERROR,mensaje,"");
                    FacesContext.getCurrentInstance().addMessage(null, msj2);                            
                }else{
                    FacesMessage msj2= new FacesMessage("Se ha actualizado la serie");
                    FacesContext.getCurrentInstance().addMessage(null, msj2);  
                    bBotonActivado=false;
                }
            }
            limpiar();
    }
    //Verifica que el folio para la serie contenga caracteres validos
     public void verificaFolio(FacesContext context, UIComponent component, Object arg2)
			throws ValidatorException { 
        try{
            Integer num = (Integer)arg2;
            msj  = new FacesMessage();
            p = Pattern.compile("[0-9]{1,20}");
            m = p.matcher(""+num);
            error = m.matches();
            if(!error){
                sResult = "Error";
		((UIInput)component).setValid(false);
		msj.setDetail("Formato Incorrecto, número debe ser un digito");
		msj.setSummary("Fólio debe ser un digito");
		context.addMessage(component.getClientId(context), msj);
            }  	
        }
        catch(NumberFormatException ex){
                    ((UIInput)component).setValid(false);
                    msj.setDetail("El valor introducido no fue número");
                    msj.setSummary("Valor 'número' incorrecto");
                    context.addMessage(component.getClientId(context), msj);
        }
            
    }  
     //Verifica que el nombre para la serie contenga caracteres validos
    public void verificaNombre(FacesContext context, UIComponent component, Object arg2)
			throws ValidatorException {              
            String colonia = (String)arg2;
            msj  = new FacesMessage();
            p = Pattern.compile("[a-zA-Z0-9]{1,25}");
            m = p.matcher(colonia);
            error = m.matches();
            if (!error){
                sResult = "Error";
		((UIInput)component).setValid(false);
		msj.setDetail("Formato Incorrecto, nombre de la serie debe ser conformada solo por letras o números");	
                msj.setSummary("Nombre incorrecto");
		context.addMessage(component.getClientId(context), msj);
            }  
    } 
    public List<SerieFiscal> getSeries(){
        return oSeries;
    }
    //Modifica la serie fiscal
    public void setSerieFiscal(SerieFiscal serie){
        this.oSerie = serie;
    }
    //Devuelve la serie fiscal buscandola a partir de un identificador
    public SerieFiscal getSerieFiscal() throws Exception{
        return oSerie;
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
    public boolean getCancelacionEliminacion() {
        return bCancelacionEliminacion;
    }

    public void setCancelacionEliminacion(boolean bCancelacionEliminacion) {
        this.bCancelacionEliminacion = bCancelacionEliminacion;
    }
}