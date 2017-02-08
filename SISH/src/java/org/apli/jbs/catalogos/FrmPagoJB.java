package org.apli.jbs.catalogos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.apli.modelbeans.ManejoMsjsDB;
import org.apli.modelbeans.FormaPago;
import org.primefaces.context.RequestContext;

/**
 * Control ABC para formas de pago
 * @author BAOZ
 */
@ManagedBean(name="oFrmPago")
@ViewScoped
public class FrmPagoJB implements Serializable {
private FormaPago oFrmPago=new FormaPago();
private ManejoMsjsDB oMenDB=new ManejoMsjsDB();
private List<FormaPago> lista;
private FormaPago selected;
private FormaPago current;
private boolean bDisDatos;
private boolean bDisDatosDescrip;
private String sNomButton;
private int nOpe;

    public FrmPagoJB() {
        lista=new ArrayList<FormaPago>();
        llenaLista();
    }
    
    private void llenaLista(){
        try {
            getLista().addAll(oFrmPago.buscaFormasPago());
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
        current=new FormaPago();
        bDisDatos=false;
        bDisDatosDescrip=false;
        sNomButton="Guardar";
        nOpe=1;
        System.out.println("Limpiado "+current.getCveFrmPago()+";");
    }
    
    public void guarda2(ActionEvent ae)throws Exception{
        guarda();
    }
    public void guarda()throws Exception{
        String mess="";
        Severity nivel;
        System.out.println("Viaja "+(new Date()));
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
    public FormaPago getFrmPago() {
        return oFrmPago;
    }

    /**
     * @param oFrmPago the oFrmPago to set
     */
    public void setFrmPago(FormaPago oFrmPago) {
        this.oFrmPago = oFrmPago;
    }

    /**
     * @return the lista
     */
    public List<FormaPago> getLista() {
        return lista;
    }

    /**
     * @param lista the lista to set
     */
    public void setLista(List<FormaPago> lista) {
        this.lista = lista;
    }

    /**
     * @return the selected
     */
    public FormaPago getSelected() {
        return selected;
    }

    /**
     * @param selected the selected to set
     */
    public void setSelected(FormaPago selected) {
        this.selected = selected;
    }

    /**
     * @return the current
     */
    public FormaPago getCurrent() {
        return current;
    }

    /**
     * @param current the current to set
     */
    public void setCurrent(FormaPago current) {
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
}
