package org.apli.jbs.promocion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apli.jbs.facturacion.CiudadJB;
import org.apli.jbs.facturacion.EstadoJB;
import org.apli.jbs.facturacion.MunicipioJB;
import org.apli.modelbeans.Ciudad;
import org.apli.modelbeans.CiudadCP;
import org.apli.modelbeans.ContactoCia;
import org.apli.modelbeans.Estado;
import org.apli.modelbeans.Municipio;
import org.apli.modelbeans.TipoConvenio;
import org.apli.modelbeans.TipoEmpresa;
import org.apli.modelbeans.ventaCredito.CompaniaCred;
import org.apli.modelbeans.ventaCredito.FormatoCiaCred;

/**
 *
 * @author Lily_LnBnd
 */
@ManagedBean(name="oCompC")
@ViewScoped
public class AdmCompaniasCredJB implements Serializable{
private List<CompaniaCred> listCompC;
private List<CompaniaCred> listCompCFiltrado;
private CompaniaCred selectedCompC;
private CompaniaCred nuevoCompC;
private List<FormatoCiaCred> listForm;
private FormatoCiaCred oForm;
private boolean bRenderNuevoForm=false;
private CiudadCP oCiudadCP;
private ContactoCia tempContacto;
    
    public AdmCompaniasCredJB(){
        listCompC=new ArrayList();
        llenaLista();
    }
    
    private void llenaLista(){
        try{
            listCompC=new CompaniaCred().buscaCompanias();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void cambiaEstado(int nIdEmp, char cActiva){
        String mess;
        try{
            mess=new CompaniaCred().modificaEstado(nIdEmp, cActiva);
            llenaLista();
        }catch(Exception e){
            e.printStackTrace();
            mess = "Error al cambiar el estado";
        }
        FacesContext context= FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Estado Compañía",mess));
    }
    
    public void cambiaEdoForm(int nIdFmt, boolean cActiva){
        System.out.println("cambiaEdoForm");
        String mess="";
        try{
            mess=new FormatoCiaCred().modificaEstado(nIdFmt, cActiva);
            buscaFormatos(selectedCompC);
        }catch(Exception e){
            e.printStackTrace();
            mess = "Error al modificar el estado del formato";
        }
        FacesContext context= FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Estado Formato",mess));
        context.getExternalContext().getFlash().setKeepMessages(true);
    }
    
    public void confNuevo(){
        this.nuevoCompC=new CompaniaCred();
        nuevoCompC.setListContactos(new ArrayList());
        this.tempContacto=new ContactoCia(); 
    }
    
    public void guardaNuevo(){
        String mess="";
        try{
            mess = new CompaniaCred().insertaCompC(this.nuevoCompC );
            confNuevo();
            llenaLista();
        }catch(Exception e){
            e.printStackTrace();
            mess= "Error al guardar la compañía";
        }
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Guardar", mess));
    }
    
    public void confModifica(CompaniaCred oCC){
        this.selectedCompC=oCC;
        this.tempContacto=new ContactoCia();
        oCiudadCP=new CiudadCP();
        try{
            EstadoJB.listEstadosDF=oCiudadCP.buscaPorCP(selectedCompC.getDomicilioLocal().getCodigoPostal());
            for (int i=0; i<getListEdoDF().size();i++){
               if(getListEdoDF().get(i).getCve().equals(selectedCompC.getDomicilioLocal().getEdo().getCve()))
                   getListEdoDF().remove(i);
            }
            selectedCompC.getDomicilioLocal().setObjPais(oCiudadCP.buscaPais(selectedCompC.getDomicilioLocal().getCodigoPostal()));
            MunicipioJB.listMunicipiosDF=oCiudadCP.buscaPorEdo(selectedCompC.getDomicilioLocal().getCodigoPostal(), 
                    selectedCompC.getDomicilioLocal().getEdo().getCve());
            for (int i=0; i<getListMunDF().size();i++){
               if(getListMunDF().get(i).getCve().equals(selectedCompC.getDomicilioLocal().getMun().getCve()))
                   getListMunDF().remove(i);
            }
            CiudadJB.listCiudadesDF=oCiudadCP.buscaPorMun(selectedCompC.getDomicilioLocal().getCodigoPostal(), 
                    selectedCompC.getDomicilioLocal().getEdo().getCve(),
                    selectedCompC.getDomicilioLocal().getMun().getCve());
            for (int i=0; i<getListCdDF().size();i++){
               if(getListCdDF().get(i).getCve().equals(selectedCompC.getDomicilioLocal().getCd().getCve()))
                   getListCdDF().remove(i);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void modificaCC(){
        String mess="";
        try{
            mess= new CompaniaCred().modificarCompC(this.selectedCompC );
            llenaLista();
        }catch(Exception e){
            e.printStackTrace();
            mess = "Error al modificar";
        }
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Guardar", mess));
    }
    
    public void buscaFormatos(CompaniaCred oCC){
        this.selectedCompC=oCC;
        try{
            listForm=new FormatoCiaCred().buscaFormatos(oCC.getIdEmp());
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void confNuevoForm(){
        bRenderNuevoForm=true;
        oForm=new FormatoCiaCred();
    }
    
    public void agregarFormato(){
        bRenderNuevoForm=false;
        oForm.setCC(selectedCompC);
        String mess="";
        try{
            mess=new FormatoCiaCred().insertaFormato(oForm);
            buscaFormatos(selectedCompC);
        }catch(Exception e){
            e.printStackTrace();
            mess= "Error al agregar el formato";
        }
        FacesContext context= FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Agregar Formato",mess));
        context.getExternalContext().getFlash().setKeepMessages(true);
    }
    
    public String cambiaTipo(int var){
    TipoEmpresa oTipEmp = new TipoEmpresa();
    String sDescrip = "";
        oTipEmp.setTipo(var);
        try{
            if (oTipEmp.buscar())
                sDescrip = oTipEmp.getDescripcion();
        }catch(Exception e){
            e.printStackTrace();
        }
        return sDescrip;
    }
    
    public String cambiaTipoConv(short value){
    String sDescrip = "";
    TipoConvenio oTipConv = new TipoConvenio();
        oTipConv.setTipo(value);
        try{
            if (oTipConv.buscar())
                sDescrip = oTipConv.getDescripcion();
        }catch(Exception e){
            e.printStackTrace();
        }
        return sDescrip;
    }
    
    public boolean renderEstado(char var){
        if (var=='1')
            return true;
        else 
            return false;
    }
    
    public void buscaDomPorCP(){
        nuevoCompC.getDomicilioLocal().getEdo().setDescrip("");
        nuevoCompC.getDomicilioLocal().getMun().setDescrip("");
        nuevoCompC.getDomicilioLocal().getCd().setDescrip("");
        oCiudadCP=new CiudadCP();
        try{
            EstadoJB.listEstadosDF=oCiudadCP.buscaPorCP(
                    nuevoCompC.getDomicilioLocal().getCodigoPostal());
            MunicipioJB.listMunicipiosDF=new ArrayList();
            CiudadJB.listCiudadesDF=new ArrayList();
            nuevoCompC.getDomicilioLocal().setObjPais(
                    oCiudadCP.buscaPais(nuevoCompC.getDomicilioLocal().
                    getCodigoPostal()));
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void buscarDomPorEdo(){
        nuevoCompC.getDomicilioLocal().getMun().setDescrip("");
        nuevoCompC.getDomicilioLocal().getCd().setDescrip("");
        oCiudadCP=new CiudadCP();
        try{
            MunicipioJB.listMunicipiosDF=oCiudadCP.buscaPorEdo(
                    nuevoCompC.getDomicilioLocal().getCodigoPostal(), 
                nuevoCompC.getDomicilioLocal().getEdo().getCve());
        }catch(Exception e){
            e.printStackTrace();
        }
        CiudadJB.listCiudadesDF=new ArrayList();
    }
    
    public void buscarDomPorMun(){
        nuevoCompC.getDomicilioLocal().getCd().setDescrip("");
        oCiudadCP=new CiudadCP();
        try{
        CiudadJB.listCiudadesDF=oCiudadCP.buscaPorMun(
                nuevoCompC.getDomicilioLocal().getCodigoPostal(), 
                nuevoCompC.getDomicilioLocal().getEdo().getCve(),
                nuevoCompC.getDomicilioLocal().getMun().getCve());
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void buscaDomPorCPM(){
        selectedCompC.getDomicilioLocal().getEdo().setDescrip("");
        selectedCompC.getDomicilioLocal().getMun().setDescrip("");
        selectedCompC.getDomicilioLocal().getCd().setDescrip("");
        oCiudadCP=new CiudadCP();
        try{
        EstadoJB.listEstadosDF=oCiudadCP.buscaPorCP(
                selectedCompC.getDomicilioLocal().getCodigoPostal());
        MunicipioJB.listMunicipiosDF=new ArrayList();
        CiudadJB.listCiudadesDF=new ArrayList();
        selectedCompC.getDomicilioLocal().setObjPais(
                oCiudadCP.buscaPais(selectedCompC.getDomicilioLocal().
                getCodigoPostal()));
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void buscarDomPorEdoM(){
        selectedCompC.getDomicilioLocal().getMun().setDescrip("");
        selectedCompC.getDomicilioLocal().getCd().setDescrip("");
        oCiudadCP=new CiudadCP();
        try{
            MunicipioJB.listMunicipiosDF=oCiudadCP.buscaPorEdo(
                    selectedCompC.getDomicilioLocal().getCodigoPostal(), 
                selectedCompC.getDomicilioLocal().getEdo().getCve());
        }catch(Exception e){
            e.printStackTrace();
        }
        CiudadJB.listCiudadesDF=new ArrayList();
    }
    
    public void buscarDomPorMunM(){
        selectedCompC.getDomicilioLocal().getCd().setDescrip("");
        oCiudadCP=new CiudadCP();
        try{
            CiudadJB.listCiudadesDF=oCiudadCP.buscaPorMun(
                selectedCompC.getDomicilioLocal().getCodigoPostal(), 
                selectedCompC.getDomicilioLocal().getEdo().getCve(),
                selectedCompC.getDomicilioLocal().getMun().getCve());
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void agregaContacto(String val){
        if (this.tempContacto==null || tempContacto.getNombre().equals("") || 
                tempContacto.getApPaterno().equals("")||
                tempContacto.getTipo().equals("")){
            FacesContext context= FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Error","Faltan datos!"));
            context.getExternalContext().getFlash().setKeepMessages(true);
        }else{
            if ("nuevo".equals(val))
                nuevoCompC.getListContactos().add(tempContacto);
            else
                selectedCompC.getListContactos().add(tempContacto);
            tempContacto=new ContactoCia();
        }
    }
    
    public void eliminaContacto(ContactoCia oCont, String val){
        if ("nuevo".equals(val)){
            for(int i=0;i<nuevoCompC.getListContactos().size();i++){
                if (nuevoCompC.getListContactos().get(i).getNombre().equals(oCont.getNombre()) &&
                        nuevoCompC.getListContactos().get(i).getApPaterno().equals(oCont.getApPaterno()) &&
                        nuevoCompC.getListContactos().get(i).getTipo().equals(oCont.getTipo())) {
                    nuevoCompC.getListContactos().remove(i);
                    break;
                }
            }
        }else{
            for(int i=0;i<selectedCompC.getListContactos().size();i++){
                if (selectedCompC.getListContactos().get(i).getNombre().equals(oCont.getNombre()) &&
                        selectedCompC.getListContactos().get(i).getApPaterno().equals(oCont.getApPaterno()) &&
                        selectedCompC.getListContactos().get(i).getTipo().equals(oCont.getTipo())) {
                    selectedCompC.getListContactos().remove(i);
                    break;
                }
            }
        }
    }
    
    public List<CompaniaCred> getListCompC() {
        return listCompC;
    }

    public void setListCompC(List<CompaniaCred> listCompC) {
        this.listCompC = listCompC;
    }
    
    public List<CompaniaCred> getListaCompCFiltrado() {
        return listCompCFiltrado;
    }
    public void setListaCompCFiltrado(List<CompaniaCred> valor) {
        this.listCompCFiltrado = valor;
    }

    public CompaniaCred getSelectedCompC() {
        return this.selectedCompC;
    }

    public void setSelectedCompC(CompaniaCred selectedCompC) {
        this.selectedCompC = selectedCompC;
    }

    public CompaniaCred getNuevoCompC() {
        return nuevoCompC;
    }

    public void setNuevoCompC(CompaniaCred nuevoCompC) {
        this.nuevoCompC = nuevoCompC;
    } 

    public List<FormatoCiaCred> getListForm() {
        return listForm;
    }

    public void setListForm(List<FormatoCiaCred> listForm) {
        this.listForm = listForm;
    }

    public FormatoCiaCred getForm() {
        return oForm;
    }

    public void setForm(FormatoCiaCred oForm) {
        this.oForm = oForm;
    }
    
    public boolean isRenderNuevoForm(){
        return bRenderNuevoForm;
    }
    
    public void setRenderNuevoForm(boolean bRenderNuevoForm){
        this.bRenderNuevoForm=bRenderNuevoForm;
    }
    
    public List<Estado> getListEdoDF() {
        return EstadoJB.listEstadosDF;
    }

    public void setListEdoDF(List<Estado> listEdoDF) {
        EstadoJB.listEstadosDF = listEdoDF;
    }
    
    public List<Municipio> getListMunDF() {
        return MunicipioJB.listMunicipiosDF;
    }

    public void setListMunDF(List<Municipio> listMunDF) {
        MunicipioJB.listMunicipiosDF = listMunDF;
    }

    public List<Ciudad> getListCdDF() {
        return CiudadJB.listCiudadesDF;
    }

    public void setListCdDF(List<Ciudad> listCdDF) {
        CiudadJB.listCiudadesDF = listCdDF;
    }

    public ContactoCia getTempContacto() {
        return tempContacto;
    }

    public void setTempContacto(ContactoCia tempContacto) {
        this.tempContacto = tempContacto;
    }
    
    public TipoEmpresa[] getListaTiposEmpresas(){
        return (new TipoEmpresa()).buscarTodas();
    }
    
    public List<TipoConvenio> getListaTiposConv(){
        return (new TipoConvenio()).getTodas();
    }
}
