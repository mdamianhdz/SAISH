package org.apli.jbs.contabilidadInterna;

import java.io.Serializable;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apli.modelbeans.contabilidadInterna.Proveedor;
import org.apli.modelbeans.contabilidadInterna.Gasto;

/**
 *
 * @author Lily_LnBnd
 */
@ManagedBean(name="oCheques")
@ViewScoped
public class EntregarChequesJB implements Serializable{
    private List<Gasto> listCheques;
    private List<Gasto> selectedCheques;
    private int selectedProv;
    
    public EntregarChequesJB(){
        
    }
    
    public void buscar()throws Exception{
        String sCondicion="";
        if (selectedProv!=0)
            sCondicion=sCondicion+" pnidprov="+selectedProv;
        listCheques=new Gasto().buscaCheques(sCondicion);
    }
    
    public void entregar() throws Exception{
        String mess= new Gasto().entregarCheques(selectedCheques);
        if (mess.equals("OK")){
            if (selectedCheques.size()==1)
                mess="El cheque: "+selectedCheques.get(0).getMovCtaBan().getNumDocto()+ "se ha marcado como entregado.";
            else{
                mess="Los cheques: ";
                for (int i = 0; i < selectedCheques.size()-1; i++) {
                    mess=mess+selectedCheques.get(i).getMovCtaBan().getNumDocto()+", ";
                }
                mess=mess+selectedCheques.get(selectedCheques.size()-1).getMovCtaBan().getNumDocto()+" se han marcado como entregados.";
            }      
        }else
            mess="Se produjo un error al marcar los cheques como entregados. Vuelva a intentarlo.";
        buscar();
        FacesMessage message = new FacesMessage("Entregar Cheques", mess);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public List<Gasto> getListCheques() {
        return listCheques;
    }

    public void setListCheques(List<Gasto> value) {
        listCheques = value;
    }

    public List<Gasto> getSelectedCheques() {
        return selectedCheques;
    }

    public void setSelectedCheques(List<Gasto> value) {
        selectedCheques = value;
    }
    
    public List<Proveedor> getListProveedores()throws Exception{
        return new Proveedor().buscaProveedores();
    }

    public int getSelectedProv() {
        return selectedProv;
    }

    public void setSelectedProv(int value) {
        selectedProv = value;
    }     
}
