/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apli.jbs.rentas;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import org.apli.modelbeans.facturacion.cfdi.v32.schema.Comprobante.Receptor;
import org.apli.modelbeans.rentas.ContratoRenta;
import org.apli.modelbeans.rentas.EspacioRentable;
import org.apli.modelbeans.rentas.PagoRenta;
import org.apli.modelbeans.rentas.TipoEspacio;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Lily_LnBnd
 */
@ManagedBean(name="oAdmRentas")
@RequestScoped
public class AdministrarRentasJB implements Serializable{
    private static int nTipoEspacio;
    private static List<EspacioRentable>  listEspacios;
    private static EspacioRentable currentEspacio;
    private static String sTipoRenta;
    private static String sValueBoton="";
    private static boolean bNuevoArrendatario=false;
    private static boolean bConsulta=false;
    
    public AdministrarRentasJB(){
        
    }
    
    public void buscaEspacios()throws Exception{
        String sCondicion="";
        if (nTipoEspacio!=0)
            sCondicion="pnidtipoesp="+nTipoEspacio;
        listEspacios=new EspacioRentable().buscaInfoEspacios(sCondicion);
        for (int i = 0; i < listEspacios.size(); i++) {
            if(listEspacios.get(i).isRentado()==false)
                listEspacios.get(i).setContratoRenta(new ContratoRenta());
        }
    }
    
    public String buscaAlertas(int nIdContrato)throws Exception{
        return new ContratoRenta().buscaAlertas(nIdContrato);
    }
    
    public void confOpe(int nVar) throws Exception{
        String mess="";
        switch(nVar){
            case 1:
                if (currentEspacio.isRentado()==false){
                    sValueBoton="Rentar";
                    bConsulta=false;
                }else{
                    mess="El espacio seleccionado ya se encuentra rentado";
                }
                break;
            case 2:
                if (currentEspacio.isRentado()==true){
                    sValueBoton="Consultar";
                    bConsulta=true;
                    currentEspacio.getContratoRenta().setListPagos(new PagoRenta().buscaPagosContrato(currentEspacio.getContratoRenta().getIdContratoRenta()));
                }else{
                    mess="El espacio seleccionado no esta rentado";
                }
                break;
            case 3:
                if (currentEspacio.isRentado()==true){
                    sValueBoton="Cancelar";
                    bConsulta=true;
                    currentEspacio.getContratoRenta().setListPagos(new PagoRenta().buscaPagosContrato(currentEspacio.getContratoRenta().getIdContratoRenta()));
                }else{
                    mess="El espacio seleccionado no esta rentado";
                }
                break;
            default:
                if (currentEspacio.isRentado()==true){
                    if (validaFechaRenovacion()==true){
                        sValueBoton="Renovar";
                        bConsulta=false;
                        sTipoRenta="";
                        currentEspacio.getContratoRenta().setInicio(nuevaFecha(currentEspacio.getContratoRenta().getFin()));
                        currentEspacio.getContratoRenta().setFin(null);
                        currentEspacio.getContratoRenta().setDeposito(0);
                        currentEspacio.getContratoRenta().setRentaMensual(0);
                    }else{
                        mess="El contrato debe renovarse con máximo un mes de anticipación";
                    }
                }else{
                    mess="El espacio seleccionado no esta rentado";
                }
                break;
        }
        if (!"".equals(mess)){
            FacesContext context= FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Administrar Rentas",mess));
            context.getExternalContext().getFlash().setKeepMessages(true);
        }else
            RequestContext.getCurrentInstance().execute("dlgRentas.show()");
    }
    
    public Date nuevaFecha(Date dFecha){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dFecha);
        calendar.add(Calendar.DATE, 1); 
        return calendar.getTime();
    }
    
    public void confNuevoArr(){
        setNuevoArrendatario(true);
    }
    
    public void calculaMensualidad(){
        System.out.println("calculaMensualidad");
        if (sTipoRenta.equals("Mensual"))
            currentEspacio.getContratoRenta().setRentaMensual(currentEspacio.getRentaMensual());
        else
            currentEspacio.getContratoRenta().setRentaMensual(currentEspacio.getRentaAnual()/12);
    }
    
    public void validaFecha(){
        String mess="";
        if (currentEspacio.getContratoRenta().getInicio()==null)
            mess="No ha especificado la fecha de inicio";
        else
            if (currentEspacio.getContratoRenta().getFin()!=null)
                if (currentEspacio.getContratoRenta().getInicio().compareTo(currentEspacio.getContratoRenta().getFin())>0)
                    mess="La fecha final del periodo debe ser posterior a la fecha de inicio";
        if (!"".equals(mess)){
            FacesContext context= FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Administrar Rentas",mess));
            context.getExternalContext().getFlash().setKeepMessages(true);
        }
    }
    
    public void calculaPagosContrato(){
        currentEspacio.setContratoRenta(currentEspacio.getContratoRenta().calculaPagosContrato(currentEspacio.getContratoRenta()));
        if (sValueBoton.equals("Renovar"))
            currentEspacio.getContratoRenta().getListPagos().remove(0);
    }
    
    public void guardar()throws Exception{
        String mess="";
        if(sValueBoton.equals("Rentar")){
            mess=new EspacioRentable().rentar(currentEspacio);
        }
        if(sValueBoton.equals("Cancelar")){
            mess=new EspacioRentable().cancelar(currentEspacio);
        }
        if(sValueBoton.equals("Renovar")){
            mess=new EspacioRentable().rentar(currentEspacio);
        }
        FacesContext context= FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Administrar Rentas",mess));
        context.getExternalContext().getFlash().setKeepMessages(true);
        setNuevoArrendatario(false);
        buscaEspacios();
    }
    
    public boolean validaFechaRenovacion(){
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(currentEspacio.getContratoRenta().getFin());
        Calendar calActual=Calendar.getInstance();
        long dif=calendar.getTimeInMillis()-calActual.getTimeInMillis();
        long difDias=dif/(24*60*60*1000);
        System.out.println("dif: "+difDias);
        if (difDias<=31)
            return true;
        else
            return false;
    }

    public int getTipoEspacio() {
        return nTipoEspacio;
    }

    public void setTipoEspacio(int nTipoEspacio) {
        AdministrarRentasJB.nTipoEspacio = nTipoEspacio;
    }

    public List<EspacioRentable> getListEspacios() {
        return listEspacios;
    }

    public void setListEspacios(List<EspacioRentable> listEspacios) {
        AdministrarRentasJB.listEspacios = listEspacios;
    }
    
    public List<Receptor> getListReceptores() throws Exception {
        return new Receptor().getTodosReceptores();
    }

    public EspacioRentable getCurrentEspacio() {
        return currentEspacio;
    }

    public void setCurrentEspacio(EspacioRentable currentEspacio) {
        AdministrarRentasJB.currentEspacio = currentEspacio;
    }
    
    public List<TipoEspacio> getTiposEspacio() throws Exception{
        return new TipoEspacio().buscaTipos();
    }

    public String getTipoRenta() {
        return sTipoRenta;
    }

    public  void setTipoRenta(String sTipoRenta) {
        AdministrarRentasJB.sTipoRenta = sTipoRenta;
    }
    
    public String getValueBoton() {
        return sValueBoton;
    }

    public  void setValueBoton(String sValueBoton) {
        AdministrarRentasJB.sValueBoton = sValueBoton;
    }

    public boolean isNuevoArrendatario() {
        return bNuevoArrendatario;
    }

    public void setNuevoArrendatario(boolean bNuevoArrendatario) {
        AdministrarRentasJB.bNuevoArrendatario = bNuevoArrendatario;
    }
    
    public boolean isConsulta() {
        return bConsulta;
    }

    public void setConsulta(boolean bConsulta) {
        AdministrarRentasJB.bConsulta = bConsulta;
    }
    
}
