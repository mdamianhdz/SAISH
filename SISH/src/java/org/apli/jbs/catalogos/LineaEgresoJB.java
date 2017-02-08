package org.apli.jbs.catalogos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import org.apli.modelbeans.contabilidadInterna.LineaEgreso;
import org.primefaces.context.RequestContext;

/**
 *
 * @author eduardo
 */
@ManagedBean (name="oLineaEgreso")
@RequestScoped
public class LineaEgresoJB implements Serializable {
    public LineaEgreso oListaEgreso=new LineaEgreso();
    private static List<LineaEgreso> listaEgreso;
    private static LineaEgreso selectedEgreso;
    private static LineaEgreso currentEgreso;
    private static boolean bDisDatos;
    public static String sDesc;
    public int  nCveLiEg;
    private static int estado;

    /**
     * Creates a new instance of LineaEgresoJB
     */
    public LineaEgresoJB() {
        listaEgreso=new ArrayList();
        llenaLista(listaEgreso);
    }
    
    public void llenaLista(List<LineaEgreso> lista){
        try {
            lista.addAll(oListaEgreso.buscaTodosLineaEgreso());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void agregarEgreso(){
        selectedEgreso=null;
        currentEgreso=new LineaEgreso();
        
        currentEgreso.setDescripcion(sDesc);
        bDisDatos=false;
        estado=1;
    }
    
    public void modificarEgreso(){
        if(selectedEgreso==null){
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Error","Debe seleccionar un registro"));
            context.getExternalContext().getFlash().setKeepMessages(true);
        }
        else{
            currentEgreso=selectedEgreso;
            bDisDatos=false;
            estado=2;
            RequestContext.getCurrentInstance().execute("dlgUsu.show()");
        }
    }
    
    public void eliminarEgreso()throws Exception{
        if(selectedEgreso==null){
            FacesContext context=FacesContext.getCurrentInstance();
            context.addMessage(null,new FacesMessage("Error","Debe seleccionar un registro"));
            context.getExternalContext().getFlash().setKeepMessages(true);
        }
        else{
            currentEgreso=selectedEgreso;
            bDisDatos=true;
            estado=3;
            setListaEgreso((List<LineaEgreso>) new ArrayList());
            llenaLista(getListaEgreso());
            RequestContext.getCurrentInstance().execute("dlgUsu.show()");
        }
    }
    
    public void guardar()throws Exception{
        String mess="";
        
        FacesContext context=FacesContext.getCurrentInstance();
        if(estado==1){
            mess=currentEgreso.guardaEgreso(currentEgreso);
            context.addMessage(null,new FacesMessage("registrar.",mess));
        }
        else{
            if(estado==2){
                mess=currentEgreso.modificaEgreso(currentEgreso);
                context.addMessage(null, new FacesMessage("Modificar.",mess));
            }
            else{
                mess=currentEgreso.eliminaEgreso(currentEgreso);
                context.addMessage(null,new FacesMessage("Eliminar.",mess));
            }
        }
        
        context.getExternalContext().getFlash().setKeepMessages(true);
        if("La Línea de Egreso ha sido registrada exitosamente".equals(mess)||
           "La Línea de Egreso ha sido modificada exitosamente".equals(mess)||
           "La Línea de Egreso ha sido eliminada exitosamente".equals(mess)){
            listaEgreso=new ArrayList();
            llenaLista(listaEgreso);
            RequestContext.getCurrentInstance().execute("dlgUsu.show()");
        }
    }    
    
    public LineaEgreso getoLineaEgreso() {
        return oListaEgreso;
    }

    public void setoLineaEgreso(LineaEgreso oListaEgreso) {
        this.oListaEgreso = oListaEgreso;
    }
    
    public int getCveLiEg() {
        return nCveLiEg;
    }

    public void setCveLiEg(int nCveLiEg) {
        this.nCveLiEg = nCveLiEg;
    }

    public String getDesc() {
        return sDesc;
    }

    public void setDesc(String sDesc) {
        this.sDesc = sDesc;
    }
    
    public List<LineaEgreso> getListaEgreso() {
        return listaEgreso;
    }

    
    public void setListaEgreso(List<LineaEgreso> listaEgreso) {
        this.listaEgreso = listaEgreso;
    }

    public LineaEgreso getSelectedEgreso() {
        return selectedEgreso;
    }

    public void setSelectedEgreso(LineaEgreso selectedEgreso) {
        this.selectedEgreso = selectedEgreso;
    }

    public LineaEgreso getCurrentEgreso() {
        return currentEgreso;
    }

    public void setCurrentEgreso(LineaEgreso currentEgreso) {
        this.currentEgreso = currentEgreso;
    }

    public boolean getDisDatos() {
        return bDisDatos;
    }

    public void setDisDatos(boolean bDisDatos) {
        this.bDisDatos = bDisDatos;
    }
}
