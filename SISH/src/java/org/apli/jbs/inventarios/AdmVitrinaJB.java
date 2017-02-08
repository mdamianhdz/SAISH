package org.apli.jbs.inventarios;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import org.apli.modelbeans.AreaFisica;
import org.apli.modelbeans.InvMatVitrina;
import org.apli.modelbeans.InvMedVitrina;
import org.apli.modelbeans.InventarioVitrina;
import org.apli.modelbeans.Vitrina;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author Lily_LnBnd
 */
@ManagedBean(name="oVitrina")
@RequestScoped
public class AdmVitrinaJB implements Serializable{
    private static List<AreaFisica> listAreasF;
    private static Vitrina selectedVitrina=new Vitrina();
    private static Vitrina oRegVitrina=new Vitrina();
    private static List<Vitrina> listVitrinas;
    private static List<InventarioVitrina> listInventario;
    private static String sDescVitrina;
    private static int selectedAreaF;
    
    public AdmVitrinaJB() throws Exception{
        listAreasF=new ArrayList();
        listAreasF=new AreaFisica().buscaTodosAF();
    }
    
    public void buscar() throws Exception{
        System.out.println("buscar()");
        listVitrinas=new ArrayList();
        listVitrinas=new Vitrina().busca(sDescVitrina, selectedAreaF);
    }
    
    public void confNuevo(){
        AdmVitrinaJB.oRegVitrina=new Vitrina();
    }
    
    public void guardaNuevo() throws Exception{
        String mess=new Vitrina().insertaVitrina(oRegVitrina);
        listVitrinas=new Vitrina().busca(sDescVitrina, selectedAreaF);
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Guardar", mess));
    }
    
    public void buscaInventario()throws Exception{
        listInventario=new ArrayList();
        listInventario=new InventarioVitrina().getInventarioVitrina(
                new InvMatVitrina().buscaInventario(selectedVitrina.getCveVitrina()),
                new InvMedVitrina().buscaInventario(selectedVitrina.getCveVitrina()));
    }
    
    public void onRowEdit(RowEditEvent event)throws Exception{
        String mess="";
        InventarioVitrina oRow=(InventarioVitrina) event.getObject();
        if ("MAT".equals(oRow.getTipo()))
            mess=new InvMatVitrina().modifica(oRow);
        else
            mess=new InvMedVitrina().modifica(oRow);
        FacesContext context= FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Modifica Vitrina",mess));
        context.getExternalContext().getFlash().setKeepMessages(true);
    }
    
    public void modifica() throws Exception{
        String mess="";
        mess=new Vitrina().modifica(selectedVitrina);
        buscar();
        FacesContext context= FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Modifica Vitrina",mess));
        context.getExternalContext().getFlash().setKeepMessages(true);
    }
    
    public void cambiaEstado() throws Exception{
        String mess="";
        mess=new Vitrina().modificaEstado(selectedVitrina);
        buscar();
        FacesContext context= FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Estado Vitrina",mess));
        context.getExternalContext().getFlash().setKeepMessages(true);
    }
    
    public void limpia(){
        oRegVitrina=new Vitrina();
    }

    public List<AreaFisica> getListAreasF() {
        return listAreasF;
    }
    public void setListAreasF(List<AreaFisica> listAreasF) {
        AdmVitrinaJB.listAreasF = listAreasF;
    }
    
    public List<Vitrina> getListVitrinas() {
        return listVitrinas;
    }
    public void setListVitrinas(List<Vitrina> listVitrinas) {
        AdmVitrinaJB.listVitrinas = listVitrinas;
    }

    public List<InventarioVitrina> getListInventario() {
        return listInventario;
    }
    public void setListInventario(List<InventarioVitrina> listInventario) {
        AdmVitrinaJB.listInventario = listInventario;
    }
    
    
    public Vitrina getSelectedVitrina() {
        return selectedVitrina;
    }
    public void setSelectedVitrina(Vitrina selectedVitrina) {
        AdmVitrinaJB.selectedVitrina = selectedVitrina;
    }
    
    public Vitrina getRegVitrina() {
        return oRegVitrina;
    }
    public void setRegVitrina(Vitrina oRegVitrina) {
        AdmVitrinaJB.oRegVitrina = oRegVitrina;
    }

    public int getSelectedAreaF() {
        return selectedAreaF;
    }
    public void setSelectedAreaF(int selectedAreaF) {
        AdmVitrinaJB.selectedAreaF = selectedAreaF;
    }  
    
    public String getDescVitrina(){
        return sDescVitrina;
    }
    public void setDescVitrina(String sDescVitrina){
        AdmVitrinaJB.sDescVitrina=sDescVitrina;
    }
}
