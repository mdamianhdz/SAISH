/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apli.jbs.rentas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apli.modelbeans.facturacion.cfdi.v32.schema.Comprobante.Emisor;
import org.apli.modelbeans.rentas.CondRenta;
import org.apli.modelbeans.rentas.EspacioRentable;
import org.apli.modelbeans.rentas.TipoEspacio;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Lily_LnBnd
 */
@ManagedBean(name="oAdmEsp")
@ViewScoped
public class AdministrarEspaciosJB implements Serializable{
    private int nTipoEspacio;
    private List<EspacioRentable>  listEspacios;
    private EspacioRentable selectedEspacio;
    private EspacioRentable currentEspacio;
    private String sValueBoton="";
    private boolean bConsulta=false;
    private CondRenta selectedCondRenta;
    private String sCondicion;
    
    public AdministrarEspaciosJB(){
        
    }
    
    public void buscaEspacios()throws Exception{
        String sCond="";
        if (nTipoEspacio!=0)
            sCond="pnidtipoesp="+nTipoEspacio;
        listEspacios=new EspacioRentable().buscaEspacios(sCond);
    }
    
    public void confOpe(int nVar)throws Exception{
        String mess="";
        if (selectedEspacio==null && nVar!=1)
            mess="Error! Debe seleccionar un espacio rentable.";
        else{
            switch(nVar){
                case 1:
                    sValueBoton="Guardar";
                    bConsulta=false;
                    currentEspacio=new EspacioRentable();
                    currentEspacio.setIdEspacio(0);
                    currentEspacio.setListCondiciones(new ArrayList());
                    break;
                case 2:
                    currentEspacio=selectedEspacio;
                    if (currentEspacio.isRentado()==true){
                        if (validaFechaModificar()==true){
                            sValueBoton="Modificar";
                            bConsulta=false;
                        }else
                            mess="El espacio seleccionado está rentado y su contrato no está próximo a vencer";
                    }else{
                        sValueBoton="Modificar";
                        bConsulta=false;
                    }
                    break;
                case 3:
                    currentEspacio=selectedEspacio;
                    sValueBoton="Inactivar";
                    selectedEspacio.setActivo(true);
                    mess=new EspacioRentable().cambiarEstado(currentEspacio.getIdEspacio());
                    buscaEspacios();
                    break;
                case 4:
                    currentEspacio=selectedEspacio;
                    sValueBoton="Activar";
                    selectedEspacio.setActivo(false);
                    mess=new EspacioRentable().cambiarEstado(currentEspacio.getIdEspacio());
                    buscaEspacios();
                    break;
                default:
                    currentEspacio=selectedEspacio;
                    sValueBoton="Consultar";
                    bConsulta=true;
                    break;
            }
        }
        if (!mess.equals("")){
            FacesContext context= FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Administrar Espacios Rentables",mess));
            context.getExternalContext().getFlash().setKeepMessages(true);
        }else
            RequestContext.getCurrentInstance().execute("dlgEspacios.show()");
    }
    
    public boolean validaFechaModificar()throws Exception{
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(new EspacioRentable().buscaFechaFin(currentEspacio.getIdEspacio()));
        Calendar calActual=Calendar.getInstance();
        long dif=calendar.getTimeInMillis()-calActual.getTimeInMillis();
        long difDias=dif/(24*60*60*1000);
        System.out.println("dif: "+difDias);
        if (difDias<=15)
            return true;
        else
            return false;
    }
    
    public void agregarCondicion(){
        if (sCondicion.length()>0){
            CondRenta oCR=new CondRenta();
            oCR.setDescripcion(sCondicion);
            currentEspacio.getListCondiciones().add(oCR);
        }
    }
    
    public void eliminarCondicion(){
        if (selectedCondRenta!=null)
            currentEspacio.getListCondiciones().remove(selectedCondRenta);
    }
    
    public void guardar()throws Exception{
        String mess="";
        if (currentEspacio==null)
            mess="Error de Validación. Faltan Datos.";
        else
            try{
                mess=new EspacioRentable().guardarEspacioRentable(currentEspacio);
                buscaEspacios();
            }catch(Exception e){
                e.printStackTrace();
                mess = "Error al almacenar el espacio";
            }
        FacesContext context= FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Administrar Espacios Rentables",mess));
        
    }
    
    public int getTipoEspacio() {
        return nTipoEspacio;
    }

    public void setTipoEspacio(int value) {
        nTipoEspacio = value;
    }
    
    public List<EspacioRentable> getListEspacios() {
        return listEspacios;
    }

    public void setListEspacios(List<EspacioRentable> value) {
        listEspacios = value;
    }
    
    public EspacioRentable getSelectedEspacio() {
        return selectedEspacio;
    }

    public void setSelectedEspacio(EspacioRentable value) {
        selectedEspacio = value;
    }
    
    public EspacioRentable getCurrentEspacio() {
        return currentEspacio;
    }

    public void setCurrentEspacio(EspacioRentable value) {
        currentEspacio = value;
    }
    
    public List<TipoEspacio> getTiposEspacio() throws Exception{
        return new TipoEspacio().buscaTipos();
    }
    
    public List<Emisor> getArrendadores() throws Exception{
        return new Emisor().buscaArrendadores();
    }
    
    public String getValueBoton() {
        return sValueBoton;
    }

    public  void setValueBoton(String value) {
        sValueBoton = value;
    }
    
    public boolean isConsulta() {
        return bConsulta;
    }

    public void setConsulta(boolean value) {
        bConsulta = value;
    }

    public CondRenta getSelectedCondRenta() {
        return selectedCondRenta;
    }

    public void setSelectedCondRenta(CondRenta value) {
        selectedCondRenta = value;
    }

    public String getCondicion() {
        return sCondicion;
    }

    public void setCondicion(String value) {
        sCondicion = value;
    } 
}
