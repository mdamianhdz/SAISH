package org.apli.jbs.personal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import org.apli.modelbeans.AreaDeServicio;
import org.apli.modelbeans.ConceptoNomina;
import org.apli.modelbeans.DetalleNomina;
import org.apli.modelbeans.Nomina;
import org.apli.modelbeans.PersonalHospitalario;
import org.primefaces.context.RequestContext;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author Lily_LnBnd
 */
@ManagedBean(name="oCaptNom")
@RequestScoped
public class CapturarNominaJB implements Serializable{
    List<AreaDeServicio> listAreas;
    private static List<PersonalHospitalario> listPersonal;
    private static List<PersonalHospitalario> listPersonalNom;
    private static List<PersonalHospitalario> selectedPersonalNom;
    private static PersonalHospitalario selectedPersonal=new PersonalHospitalario();
    private int nFolio;
    private String sNombre;
    private String sApPat;
    private String sApMat;
    private int nArea;
    private static Date dInicio;
    private static Date dFin;
    private List<DetalleNomina> listPercepciones;
    private List<DetalleNomina> listDeducciones;
    private static int selectedPercepcion;
    private static int selectedDeduccion;
    private static Date dNomIni;
    private static Date dNomFin;
    private static Date dNomPago;
    
    public CapturarNominaJB() {
        
    }
    
    public void buscarPersonal()throws Exception{
        String sCondicion="";
        if (nFolio>0)
            sCondicion="pnfoliopers="+nFolio;
        else{
            if (!"".equals(sNombre))
                sCondicion=sCondicion+"psnombre='"+sNombre+"'";
            if (!"".equals(sApPat))
                if ("".equals(sCondicion))
                    sCondicion="psappaterno='"+sApPat+"'";
                else
                    sCondicion=sCondicion+" AND psappaterno='"+sApPat+"'";
            if (!"".equals(sApMat))
                if ("".equals(sCondicion))
                    sCondicion="psapmaterno='"+sApMat+"'";
                else
                    sCondicion=sCondicion+" AND psapmaterno='"+sApMat+"'";
            if (nArea>0)
                if ("".equals(sCondicion))
                    sCondicion="pncvearea="+nArea;
                else
                    sCondicion=sCondicion+" AND pncvearea="+nArea;
            if (dInicio!=null && dFin!=null)
                if ("".equals(sCondicion))
                    sCondicion="pdpago BETWEEN '"+dInicio+"' AND '"+dFin+"'";
                else
                    sCondicion=sCondicion+" AND pdpago BETWEEN '"+dInicio+"' AND '"+dFin+"'";
        }
        listPersonal=new PersonalHospitalario().buscaPersonalNomina(sCondicion);            
    }
    
    public void validaFecha(){
        String mess="";
        if (dInicio==null)
            mess="No ha especificado la fecha de inicio";
        else
            if (dInicio.compareTo(dFin)>0)
                mess="La fecha final del periodo debe ser posterior a la fecha de inicio";
        if (!"".equals(mess)){
            FacesContext context= FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Capturar Nómina",mess));
            context.getExternalContext().getFlash().setKeepMessages(true);
        }
    }
    
    public void validaFecha2(){
        String mess="";
        if (dNomIni==null)
            mess="No ha especificado la fecha de inicio";
        else
            if (dNomFin!=null)
                if (dNomIni.compareTo(dNomFin)>0){
                    mess="La fecha final del periodo debe ser posterior a la fecha de inicio";
                    dNomFin=null;
                }
        if (!"".equals(mess)){
            FacesContext context= FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Generar Nómina",mess));
            context.getExternalContext().getFlash().setKeepMessages(true);
        }
    }
    
    public void agregaPercepcion() throws Exception{
        List<ConceptoNomina> temp=getPercepciones();
        DetalleNomina oDN;
        for (int i = 0; i < temp.size(); i++) {
            if (temp.get(i).getCveConcepNom()==selectedPercepcion) {
                oDN=new DetalleNomina();
                oDN.setConcNom(temp.get(i));
                oDN.setInicio(selectedPersonal.getNomina().getInicio());
                oDN.setFin(selectedPersonal.getNomina().getFin());
                oDN.setMonto(0.0f);
                selectedPersonal.getListConceptos().add(oDN);
            }
        }
    }
    
    public void agregaDeduccion() throws Exception{
        List<ConceptoNomina> temp=getDeducciones();
        DetalleNomina oDN;
        for (int i = 0; i < temp.size(); i++) {
            if (temp.get(i).getCveConcepNom()==selectedDeduccion) {
                oDN=new DetalleNomina();
                oDN.setConcNom(temp.get(i));
                oDN.setInicio(selectedPersonal.getNomina().getInicio());
                oDN.setFin(selectedPersonal.getNomina().getFin());
                oDN.setMonto(0.0f);
                selectedPersonal.getListConceptos().add(oDN);
            }
        }
    }
    
    public void onRowEdit(RowEditEvent event)throws Exception{
        selectedPersonal=new PersonalHospitalario().calculaNomina(selectedPersonal);
    }
    
    public void eliminaConcepto(DetalleNomina oDN){
        selectedPersonal.getListConceptos().remove(oDN);
        selectedPersonal=new PersonalHospitalario().calculaNomina(selectedPersonal);  
    }
    
    public void guardaNomina() throws Exception{
        String mess="";
        mess=new DetalleNomina().guardaNomina(selectedPersonal);
        FacesContext context= FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Guardar Nómina",mess));
        context.getExternalContext().getFlash().setKeepMessages(true);
        this.buscarPersonal();
    }
    
    public void confGenerarNom(){
        selectedPersonalNom=new ArrayList();
        dNomIni=null;
        dNomFin=null;
        dNomPago=null;
    }
    
    public void generarNomina()throws Exception{
        String mess="";
        if (selectedPersonalNom==null||dNomIni==null||dNomFin==null||dNomPago==null){
            mess="Error de Validación. Faltan datos";
            FacesContext context= FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Generar Nómina",mess));
            context.getExternalContext().getFlash().setKeepMessages(true);
        }else{
            mess=new Nomina().generaNomina(selectedPersonalNom, dNomIni, dNomFin, dNomPago);
            FacesContext context= FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Generar Nómina",mess));
            context.getExternalContext().getFlash().setKeepMessages(true);
            RequestContext rc = RequestContext.getCurrentInstance();
            rc.execute("dlgNueva.hide()");
        }
    }

    public List<AreaDeServicio> getListAreas() throws Exception{
        listAreas=new AreaDeServicio().buscaAreasServicio();
        return listAreas;
    }

    public void setListAreas(List<AreaDeServicio> listAreas) {
        this.listAreas = listAreas;
    }
    
    public List<PersonalHospitalario> getListPersonal() {
        return listPersonal;
    }

    public void setListPersonal(List<PersonalHospitalario> listPersonal) {
        CapturarNominaJB.listPersonal = listPersonal;
    }

    public PersonalHospitalario getSelectedPersonal() {
        return selectedPersonal;
    }

    public void setSelectedPersonal(PersonalHospitalario selectedPersonal) {
        CapturarNominaJB.selectedPersonal = selectedPersonal;
    }  

    public int getFolio() {
        return nFolio;
    }

    public void setFolio(int nFolio) {
        this.nFolio = nFolio;
    }

    public String getNombre() {
        return sNombre;
    }

    public void setNombre(String sNombre) {
        this.sNombre = sNombre;
    }

    public String getApPat() {
        return sApPat;
    }

    public void setApPat(String sApPat) {
        this.sApPat = sApPat;
    }

    public String getApMat() {
        return sApMat;
    }

    public void setApMat(String sApMat) {
        this.sApMat = sApMat;
    }

    public int getArea() {
        return nArea;
    }

    public void setArea(int nArea) {
        this.nArea = nArea;
    }

    public Date getInicio() {
        return dInicio;
    }

    public void setInicio(Date dInicio) {
        CapturarNominaJB.dInicio = dInicio;
    }

    public Date getFin() {
        return dFin;
    }

    public void setFin(Date dFin) {
        CapturarNominaJB.dFin = dFin;
    }

    public List<DetalleNomina> getListPercepciones() {
        listPercepciones=new ArrayList();
        if (selectedPersonal!=null)
            for (int i = 0; i < selectedPersonal.getListConceptos().size(); i++) {
                if(selectedPersonal.getListConceptos().get(i).getConcNom().getTipoConNom()=='P')
                    listPercepciones.add(selectedPersonal.getListConceptos().get(i));
            }
        return listPercepciones;
    }

    public void setListPercepciones(List<DetalleNomina> listPercepciones) {
        this.listPercepciones = listPercepciones;
    }

    public List<DetalleNomina> getListDeducciones() {
        listDeducciones=new ArrayList();
        if (selectedPersonal!=null)
            for (int i = 0; i < selectedPersonal.getListConceptos().size(); i++) {
                if(selectedPersonal.getListConceptos().get(i).getConcNom().getTipoConNom()=='D')
                    listDeducciones.add(selectedPersonal.getListConceptos().get(i));
            }
        return listDeducciones;
    }

    public void setListDeducciones(List<DetalleNomina> listDeducciones) {
        this.listDeducciones = listDeducciones;
    }
    
    public List<ConceptoNomina> getPercepciones() throws Exception{
        List<ConceptoNomina> listP =new ConceptoNomina().buscaPercepciones();
        for (int i = listP.size()-1; i >=0; i--) {
            for (int j = 0; j < listPercepciones.size(); j++) {
                if (listP.get(i).getCveConcepNom()==listPercepciones.get(j).getConcNom().getCveConcepNom()){
                    listP.remove(i);
                    if (listP.isEmpty())
                        break;
                }
            }
            
        }
        return listP;
    }
    
    public List<ConceptoNomina> getDeducciones() throws Exception{
        List<ConceptoNomina> listD =new ConceptoNomina().buscaDeducciones();
        for (int i = listD.size()-1; i >=0; i--) {
            for (int j = 0; j < listDeducciones.size(); j++) {
                if (listD.get(i).getCveConcepNom()==listDeducciones.get(j).getConcNom().getCveConcepNom()){
                    listD.remove(i);
                    if (listD.isEmpty())
                        break;
                }
            }
            
        }
        return listD;
    }

    public int getSelectedPercepcion() {
        return selectedPercepcion;
    }

    public void setSelectedPercepcion(int selectedPercepcion) {
        CapturarNominaJB.selectedPercepcion = selectedPercepcion;
    }

    public int getSelectedDeduccion() {
        return selectedDeduccion;
    }

    public void setSelectedDeduccion(int selectedDeduccion) {
        System.out.println("setSelectedDeduccion:"+selectedDeduccion);
        CapturarNominaJB.selectedDeduccion = selectedDeduccion;
    }

    public Date getNomIni() {
        return dNomIni;
    }

    public void setNomIni(Date dNomIni) {
        CapturarNominaJB.dNomIni = dNomIni;
    }

    public Date getNomFin() {
        return dNomFin;
    }

    public void setNomFin(Date dNomFin) {
        CapturarNominaJB.dNomFin = dNomFin;
    }

    public Date getNomPago() {
        return dNomPago;
    }

    public void setNomPago(Date dNomPago) {
        CapturarNominaJB.dNomPago = dNomPago;
    }

    public List<PersonalHospitalario> getListPersonalNom() throws Exception {
        return (new PersonalHospitalario().buscaPersonalNomina());
    }

    public void setListPersonalNom(List<PersonalHospitalario> listPersonalNom) {
        CapturarNominaJB.listPersonalNom = listPersonalNom;
    }

    public List<PersonalHospitalario> getSelectedPersonalNom() {
        return selectedPersonalNom;
    }

    public void setSelectedPersonalNom(List<PersonalHospitalario> selectedPersonalNom) {
        CapturarNominaJB.selectedPersonalNom = selectedPersonalNom;
    }

}
