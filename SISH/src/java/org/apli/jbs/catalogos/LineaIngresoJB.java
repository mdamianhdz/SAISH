package org.apli.jbs.catalogos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import org.apli.modelbeans.LineaIngreso;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Gigi
 */
@ManagedBean (name="oLineaIng")
@RequestScoped
public class LineaIngresoJB implements Serializable {
private LineaIngreso oLineaIng=new LineaIngreso();
private static List<LineaIngreso> listaIng;
private static LineaIngreso selectedLinea;
private static LineaIngreso currentLinea;
private int nCveLin;
private String sDesc;
private static boolean bDisDatos;
private static int estado;

    /**
     * Creates a new instance of LineaIngreso
     */
    public LineaIngresoJB() {
        listaIng=new ArrayList();
        llenaLista(listaIng);
    }
    
    private void llenaLista(List<LineaIngreso> lista){
        try{
            lista.addAll(Arrays.asList(getLineaIng().buscaTodas()));
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public void confLineaIng(){
        if(selectedLinea==null){
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Error","Debe seleccionar un registro"));
            context.getExternalContext().getFlash().setKeepMessages(true);
        }else{
            currentLinea=selectedLinea;
            bDisDatos=false;
            estado=2;
            RequestContext.getCurrentInstance().execute("dlgUsu.show()");
        }
    }
    
    public void confLineaIngDelete()throws Exception{
        //String mess="";
        if(selectedLinea==null){
            FacesContext context=FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Error","Debe seleccionar un registro"));
            context.getExternalContext().getFlash().setKeepMessages(true);            
        }
        else{
            setListaIngresos((List<LineaIngreso>) new ArrayList());
            llenaLista(getListaIngresos());
            RequestContext.getCurrentInstance().execute("dlgUsu.show()");
            currentLinea=selectedLinea;
            bDisDatos=true;
            estado=3;
        }
    }
    
    public void confNueva(){
        selectedLinea=null;
        currentLinea=new LineaIngreso();
        currentLinea.setDescrip(sDesc);
        bDisDatos=false;
        estado=1;
    }
    
    public void guardar()throws Exception{
        String mess="";
        System.out.println("Estado: "+estado);
        if(estado==1){
            mess=currentLinea.insertar();
            FacesContext context=FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Registrar.",mess));
            context.getExternalContext().getFlash().setKeepMessages(true);
        }else if(estado==2){
            mess=currentLinea.modificar();
            FacesContext context=FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Modificar.",mess));
            context.getExternalContext().getFlash().setKeepMessages(true);
        }else{
            mess=currentLinea.eliminar();
            FacesContext context=FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Eliminar.",mess));
            context.getExternalContext().getFlash().setKeepMessages(true);
        }
        FacesContext context=FacesContext.getCurrentInstance();
        //context.addMessage(null, new FacesMessage("Guardar.",mess));
        context.getExternalContext().getFlash().setKeepMessages(true);
        if("La Línea de Ingreso ha sido registrada exitosamente".equals(mess)||
           "La Línea de Ingreso ha sido modificada exitosamente".equals(mess)||
           "La Línea de Ingreso ha sido eliminada exitosamente".equals(mess)){
            listaIng=new ArrayList();
            llenaLista(listaIng);
            RequestContext.getCurrentInstance().execute("dlgUsu.show()");
        }
    }

    /**
     * @return the oLineaIng
     */
    public LineaIngreso getLineaIng() {
        return oLineaIng;
    }

    /**
     * @param oLineaIng the oLineaIng to set
     */
    public void setLineaIng(LineaIngreso oLineaIng) {
        this.oLineaIng = oLineaIng;
    }

    /**
     * @return the nCveLin
     */
    public int getCveLin() {
        return nCveLin;
    }

    /**
     * @param nCveLin the nCveLin to set
     */
    public void setCveLin(int nCveLin) {
        this.nCveLin = nCveLin;
    }

    /**
     * @return the sDesc
     */
    public String getDesc() {
        return sDesc;
    }

    /**
     * @param sDesc the sDesc to set
     */
    public void setDesc(String sDesc) {
        this.sDesc = sDesc;
    }
    /**
     * @return the listaIng
     */
    public List<LineaIngreso> getListaIngresos() {
        return listaIng;
    }

    /**
     * @param aListaIng the listaIng to set
     */
    public void setListaIngresos(List<LineaIngreso> aListaIng) {
        listaIng = aListaIng;
    }

    /**
     * @return the selectedLinea
     */
    public LineaIngreso getSelectedLinea() {
        return selectedLinea;
    }

    /**
     * @param aSelectedLinea the selectedLinea to set
     */
    public void setSelectedLinea(LineaIngreso aSelectedLinea) {
        selectedLinea = aSelectedLinea;
    }

    /**
     * @return the currentLinea
     */
    public LineaIngreso getCurrentLinea() {
        return currentLinea;
    }

    /**
     * @param aCurrentLinea the currentLinea to set
     */
    public void setCurrentLinea(LineaIngreso aCurrentLinea) {
        currentLinea = aCurrentLinea;
    }

    /**
     * @return the bDisDatos
     */
    public boolean isDisDatos() {
        return bDisDatos;
    }

    /**
     * @param abDisDatos the bDisDatos to set
     */
    public void setDisDatos(boolean abDisDatos) {
        bDisDatos = abDisDatos;
    }
    
}
