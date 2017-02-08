package org.apli.jbs.catalogos;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import org.apli.modelbeans.ManejoMsjsDB;
import org.apli.modelbeans.ConceptoIngreso;
import org.apli.modelbeans.AreaDeServicio;
import org.apli.modelbeans.AreaFisica;
import org.apli.modelbeans.LineaIngreso;
import org.primefaces.context.RequestContext;

/**
 * Control de ABC para conceptos de ingresos
 * @author BAOZ
 */
@ManagedBean(name = "oConcepIngrJB")
@ViewScoped
public class ConceptoIngresoJB implements Serializable{
private ConceptoIngreso oConcep=new ConceptoIngreso();
private ManejoMsjsDB oMenDB=new ManejoMsjsDB();
private List<ConceptoIngreso> lista;
private List<ConceptoIngreso> listaFiltrada;
private List<AreaDeServicio> listaAreaServ;
private List<AreaFisica> listaAreaFisica;
private List<LineaIngreso> listaLinIngr;
private ConceptoIngreso selected;
private ConceptoIngreso current;
private boolean bDisDatos;
private boolean bDisDatosDescrip;
private String sNomButton;
private int nOpe;

    /**
     * Creates a new instance of ConceptoIngresoJB
     */
    public ConceptoIngresoJB() {
        lista=new ArrayList<ConceptoIngreso>();
        listaAreaServ = new ArrayList<AreaDeServicio>();
        listaAreaFisica = new ArrayList<AreaFisica>();
        listaLinIngr = new ArrayList<LineaIngreso>();
        llenaLista();
        llenaOtrasListas();
    }
    
    private void llenaOtrasListas(){
        try{
            this.listaAreaServ.addAll((new AreaDeServicio()).buscaAreasServicio());
            this.listaAreaFisica.addAll((new AreaFisica()).buscaTodosAF());
            this.listaLinIngr = new ArrayList<LineaIngreso>(Arrays.asList(
                    (new LineaIngreso()).buscaTodas()
                    ));
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private void llenaLista(){
        try {
            getLista().addAll(oConcep.buscaTodos());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void confModificar(){
        if (getSelected()==null){
            FacesContext context= FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Error","Debe seleccionar un registro"));
            context.getExternalContext().getFlash().setKeepMessages(true);
        }else{
            current=selected;
            bDisDatos=true;
            bDisDatosDescrip=false;
            sNomButton="Guardar";
            nOpe=2;
            RequestContext.getCurrentInstance().execute("dlgEdicion.show()");
        }
    }
    
    public void confEliminar(){
        if (selected==null){
            FacesContext context= FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Error","Debe seleccionar un registro"));
            context.getExternalContext().getFlash().setKeepMessages(true);
        }else{
            current=selected;
            bDisDatos=true;
            bDisDatosDescrip=true;
            sNomButton="Eliminar";
            nOpe=3;
            RequestContext.getCurrentInstance().execute("dlgEdicion.show()");
        }
    }
    
    public void confInsertar(){
        selected=null;
        current=new ConceptoIngreso();
        bDisDatos=true; //no muestra clave para captura
        bDisDatosDescrip=false;
        sNomButton="Guardar";
        nOpe=1;
        System.out.println("Limpiado "+current.getCveConcep()+";");
    }
    
    public void guarda2(ActionEvent ae)throws Exception{
        guarda();
    }
    public void guarda()throws Exception{
        String mess="";
        Severity nivel;
        System.out.println("Viaja "+(new Date()));
        System.out.println(current.getTipoConcIngr());
        if(nOpe==1)
            mess=current.insertar();
        else{
            if(nOpe==3)
                mess=current.eliminar();
            else
                mess=current.modificar();
        }
        if (oMenDB.isValid(mess))
            nivel = FacesMessage.SEVERITY_INFO;
        else
            nivel = FacesMessage.SEVERITY_ERROR;
        FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(nivel, "Guardar", 
                oMenDB.manejoMensajes(mess)));
        lista.clear();
        llenaLista();
        nOpe = 0;
        selected = null;
        current = selected;
        RequestContext.getCurrentInstance().execute("dlgEdicion.hide();");
    }    

    /**
     * @return the oFrmPago
     */
    public ConceptoIngreso getConcepIngr() {
        return this.oConcep;
    }

    /**
     * @param oFrmPago the oFrmPago to set
     */
    public void setConcepIngr(ConceptoIngreso valor) {
        this.oConcep = valor;
    }

    /**
     * @return the lista
     */
    public List<ConceptoIngreso> getLista() {
        return lista;
    }

    /**
     * @param lista the lista to set
     */
    public void setLista(List<ConceptoIngreso> lista) {
        this.lista = lista;
    }

    /**
     * @return the selected
     */
    public ConceptoIngreso getSelected() {
        return selected;
    }

    /**
     * @param selected the selected to set
     */
    public void setSelected(ConceptoIngreso selected) {
        this.selected = selected;
    }

    /**
     * @return the current
     */
    public ConceptoIngreso getCurrent() {
        return current;
    }

    /**
     * @param current the current to set
     */
    public void setCurrent(ConceptoIngreso current) {
        this.current = current;
    }

    /**
     * @return the bDisDatos
     */
    public boolean getDisDatos() {
        return bDisDatos;
    }

    /**
     * @param bDisDatos the bDisDatos to set
     */
    public void setDisDatos(boolean bDisDatos) {
        this.bDisDatos = bDisDatos;
    }

    /**
     * @return the bDisDatosDescrip
     */
    public boolean getDisDatosDescrip() {
        return bDisDatosDescrip;
    }

    /**
     * @param bDisDatosDescrip the bDisDatosDescrip to set
     */
    public void setDisDatosDescrip(boolean bDisDatosDescrip) {
        this.bDisDatosDescrip = bDisDatosDescrip;
    }

    /**
     * @return the sNomButton
     */
    public String getNomButton() {
        return sNomButton;
    }

    /**
     * @param sNomButton the sNomButton to set
     */
    public void setNomButton(String sNomButton) {
        this.sNomButton = sNomButton;
    }

    /**
     * @return the nOpe
     */
    public int getOpe() {
        return nOpe;
    }

    /**
     * @param nOpe the nOpe to set
     */
    public void setOpe(int nOpe) {
        this.nOpe = nOpe;
    }
    
    public List<SelectItem> getListaAreasServ(){
        List<SelectItem> lLista=new ArrayList<SelectItem>();
            
        for (AreaDeServicio p: this.listaAreaServ){
            lLista.add(new SelectItem(p.getCve(),p.getDescrip()));
        }
        return lLista;
    }
    
    public List<SelectItem> getListaAreasFis(){
        List<SelectItem> lLista=new ArrayList<SelectItem>();
            
        for (AreaFisica p: this.listaAreaFisica){
            lLista.add(new SelectItem(p.getCve(),p.getDescrip()));
        }
        return lLista;
    }
    
    public List<SelectItem> getListaLinIngr(){
        List<SelectItem> lLista=new ArrayList<SelectItem>();
            
        for (LineaIngreso p: this.listaLinIngr){
            lLista.add(new SelectItem(p.getCveLin(),p.getDescrip()));
        }
        return lLista;
    }
    
    public List<ConceptoIngreso> getListaFiltrada() {
        return listaFiltrada;
    }

    
    public void setListaFiltrada(List<ConceptoIngreso> lista) {
        this.lista = listaFiltrada;
    }
}
