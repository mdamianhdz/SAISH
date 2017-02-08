/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apli.jbs.ventaCredito;
import java.io.Serializable;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import org.apli.modelbeans.ventaCredito.CompaniaCred;
/**
 * Autor: Isabel Espinoza Espinoza
 * Fecha: Abril 2014
 */
@ManagedBean(name="oCompCredito")
@RequestScoped
public class CompaniaCredJB  implements Serializable{
    //Atributos
    boolean valida=false;
    static CompaniaCred oCompaniaCred=new CompaniaCred();
    boolean error;
    FacesMessage msj  = new FacesMessage();
    Pattern p;
    Matcher m;
    String sResult;
    private List<CompaniaCred> listCompaniaCred;
   
    //Métodos
    public void limpiar(){
        valida=false;
        error=false;
        msj  = new FacesMessage();
        p=null;
        m=null;
        sResult="";
        oCompaniaCred=new CompaniaCred();   
    }
    public CompaniaCred getCompaniaCredSeleccionada() {
        return oCompaniaCred;
    }
    public void setCompaniaCredSeleccionada(CompaniaCred companiaCred) {
        this.oCompaniaCred = companiaCred;
    }
    /*
     * Devuelve una lista de compañias de crédito
     */
    public List<CompaniaCred> getTodasCompaniasCredito() throws Exception{
        CompaniaCred oP=new CompaniaCred();
	try{
            listCompaniaCred = oP.getCompaniasCred();
	} catch(Exception e){
            e.printStackTrace();
	}
	return listCompaniaCred;
    }
    /*
     * Devuelve una lista de compañias de crédito que estan activas
     */
    public List<CompaniaCred> getTodasCompaniasCredActivas() throws Exception{
        CompaniaCred oP=new CompaniaCred();
	try{
            listCompaniaCred = oP.getCompaniasCredActivas();
	} catch(Exception e){
            e.printStackTrace();
	}
	return listCompaniaCred;
    }
    /*
     * Modifica el plazo que las compañias de crédito tienen para hacer el pago
     */
    public void modificarPlazoCom() throws Exception{
        System.out.println("plazo "+oCompaniaCred.getPlazoPago()+" id:"+oCompaniaCred.getIdEmp());
        valida=true;
        if(oCompaniaCred.getPlazoPago()<1){
            String rst="Plazo de pago: Error de validación: se necesita un valor.";
            FacesMessage msj2= new FacesMessage(FacesMessage.SEVERITY_ERROR,rst,"");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
            valida=false;
        }else if(oCompaniaCred.getIdEmp()==0){
            String rst="Nombre: Error de validación: se necesita un valor.";
            FacesMessage msj2= new FacesMessage(FacesMessage.SEVERITY_ERROR,rst,"");
            FacesContext.getCurrentInstance().addMessage(null, msj2);            
            valida=false;
        } 
        if(valida){
            String msj=oCompaniaCred.modificarPlazoDePago();
            System.out.println("El mensaje es "+msj);
            if(msj.contains("ERROR")){
                FacesMessage msj2= new FacesMessage(msj,"");
                FacesContext.getCurrentInstance().addMessage(null, msj2);                            
            }else{
                FacesMessage msj2= new FacesMessage("Se modifico exitosamente el plazo de pago","");
                FacesContext.getCurrentInstance().addMessage(null, msj2);  
            }
        }
    }
    /*
     * Verifica que el plazo a modificar sea correcto
     */
    public void verificaPlazoEmpresa(FacesContext context, UIComponent component, Object arg2)
			throws ValidatorException { 
        try{
            Integer num = (Integer)arg2;
            msj  = new FacesMessage();
            p = Pattern.compile("[0-9]{1,2}");
            m = p.matcher(""+num);
            error = m.matches();
            if (!error){
                sResult = "Error";
		((UIInput)component).setValid(false);
		msj.setDetail("Formato Incorrecto, número debe ser un digito");
		msj.setSummary("Plazo debe ser un digito");
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
}
