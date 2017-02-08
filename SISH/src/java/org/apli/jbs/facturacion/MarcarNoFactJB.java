/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apli.jbs.facturacion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apli.jbs.PacienteJB;
import org.apli.modelbeans.OpeCajaConcepto;
import org.apli.modelbeans.Paciente;

/**
 *
 * @author lleon
 */
@ManagedBean(name="oNoFact")
@ViewScoped
public class MarcarNoFactJB implements Serializable{
    private Paciente oPaciente = new Paciente();
    private List<OpeCajaConcepto> listHospital;
    private List<OpeCajaConcepto> listExternos;
    private List<OpeCajaConcepto> listRentas;
    private OpeCajaConcepto oSelectedHosp;
    private OpeCajaConcepto oSelectedExt;
    private OpeCajaConcepto oSelectedRentas;
    private int nSelectedFiltro;
    private boolean bDisPaciente=true;
    private String sDisplayHosp="none";
    private String sDisplayExt="none";
    private String sDisplayRentas="none";
    private static String sQuienAut;
    private static String sMotivo;
    
    public MarcarNoFactJB(){
        switch (nSelectedFiltro){
            case 1:
                sDisplayHosp="block";
                break;
            case 2:
                sDisplayExt="block";
                break;
            case 3:
                sDisplayRentas="block";
                break;
        }
    }
    
    public void habilitaPaciente(){
        System.out.println("habilitaPaciente "+nSelectedFiltro);
        if (nSelectedFiltro==1 || nSelectedFiltro==2)
            bDisPaciente=false;
        else
            bDisPaciente=true;  
        oPaciente=new Paciente();
    }
    
    public void selPaciente(){
        oPaciente=new PacienteJB().getPacienteSesion();
    }
    
    public void buscar() throws Exception{
        System.out.println(oPaciente);
        System.out.println("'"+oPaciente+"'");
        switch(nSelectedFiltro){
            case 1:
                sDisplayHosp="block";
                sDisplayExt="none";
                sDisplayRentas="none";
                listHospital=new ArrayList();
                listExternos=new ArrayList();
                listRentas=new ArrayList();
                if (oPaciente==null||oPaciente.getFolioPac()==0)
                    listHospital=new OpeCajaConcepto().buscaHospitalarios(0);
                else
                    listHospital=new OpeCajaConcepto().buscaHospitalarios(oPaciente.getFolioPac());
                break;
           case 2:
               sDisplayHosp="none";
               sDisplayExt="block";
               sDisplayRentas="none";
               listHospital=new ArrayList();
               listExternos=new ArrayList();
               listRentas=new ArrayList();
               if (oPaciente==null||oPaciente.getFolioPac()==0)
                   listExternos=new OpeCajaConcepto().buscaExternos(0);
               else
                   listExternos=new OpeCajaConcepto().buscaExternos(oPaciente.getFolioPac());
               break;
           case 3:
               sDisplayHosp="none";
               sDisplayExt="none";
               sDisplayRentas="block";
               listHospital=new ArrayList();
               listExternos=new ArrayList();
               listRentas=new ArrayList();
               listRentas=new OpeCajaConcepto().buscaRentas();
               
        }
        oPaciente=null;
    }
    
    public void marcarNoFact() throws Exception{
        String mess="";
        System.out.println("sQuienAut "+sQuienAut);
        System.out.println("sMotivo "+sMotivo);
        if (sQuienAut.equals("")||sMotivo.equals("")){
            mess="Error de Validación, faltan datos!";
        }else{
            switch(nSelectedFiltro){
                case 1:
                    oSelectedHosp.getOpeCaja().setQuienAutoriza(MarcarNoFactJB.sQuienAut);
                    oSelectedHosp.getOpeCaja().setMotivo(MarcarNoFactJB.sMotivo);
                    mess=new OpeCajaConcepto().marcarNoFact(oSelectedHosp.getOpeCaja());
                    listHospital.remove(oSelectedHosp);
                    break;
                case 2:
                    oSelectedExt.getOpeCaja().setQuienAutoriza(MarcarNoFactJB.sQuienAut);
                    oSelectedExt.getOpeCaja().setMotivo(MarcarNoFactJB.sMotivo);
                    mess=new OpeCajaConcepto().marcarNoFact(oSelectedExt.getOpeCaja());
                    listExternos.remove(oSelectedExt);
                    break;
                case 3:
                    oSelectedRentas.getOpeCaja().setQuienAutoriza(MarcarNoFactJB.sQuienAut);
                    oSelectedRentas.getOpeCaja().setMotivo(MarcarNoFactJB.sMotivo);
                    mess=new OpeCajaConcepto().marcarNoFact(oSelectedRentas.getOpeCaja());
                    listRentas.remove(oSelectedRentas);
                    break;
            }
        }
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Marcar No Facturable", mess));
    }

    public List<OpeCajaConcepto> getListHospital() {
        return listHospital;
    }

    public void setListHospital(List<OpeCajaConcepto> value) {
        listHospital = value;
    }

    public List<OpeCajaConcepto> getListExternos() {
        return listExternos;
    }

    public void setListExternos(List<OpeCajaConcepto> val) {
        listExternos = val;
    }

    public List<OpeCajaConcepto> getListRentas() {
        return listRentas;
    }

    public void setListRentas(List<OpeCajaConcepto> val) {
        listRentas = val;
    }
    
    public Paciente getPaciente(){
        return oPaciente;
    }
    
    public void setFolioPaciente(Paciente val){
        oPaciente=val;
    }
    
    public int getSelectedFiltro(){
        return nSelectedFiltro;
    }
    
    public void setSelectedFiltro(int val){
        nSelectedFiltro=val;
    }
    
    public boolean isDisPaciente(){
        return this.bDisPaciente;
    }
    
    public void setDisPaciente(boolean bDisPaciente){
        this.bDisPaciente=bDisPaciente;
    }
    
    public OpeCajaConcepto getSelectedHosp(){
        return oSelectedHosp;
    }
    public void setSelectedHosp(OpeCajaConcepto val){
        System.out.println("selectedHosp "+val);
        oSelectedHosp=val;
    }
    
    public OpeCajaConcepto getSelectedExt(){
        return oSelectedExt;
    }
    public void setSelectedExt(OpeCajaConcepto val){
        oSelectedExt=val;
    }
    
    public OpeCajaConcepto getSelectedRentas(){
        return oSelectedRentas;
    }
    public void setSelectedRentas(OpeCajaConcepto val){
        oSelectedRentas=val;
    }
 
    public String getDisplayHosp() {
        return sDisplayHosp;
    }

    public void setDisplay(String sDisplayHosp) {
        this.sDisplayHosp = sDisplayHosp;
    }
    
    public String getDisplayExt() {
        return sDisplayExt;
    }

    public void setDisplayExt(String sDisplayExt) {
        this.sDisplayExt = sDisplayExt;
    }
    
    public String getDisplayRentas() {
        return sDisplayRentas;
    }

    public void setDisplayRentas(String sDisplayRentas) {
        this.sDisplayRentas = sDisplayRentas;
    }

    public String getQuienAut() {
        return sQuienAut;
    }

    public void setQuienAut(String sQuienAut) {
        MarcarNoFactJB.sQuienAut = sQuienAut;
    }

    public String getMotivo() {
        return sMotivo;
    }

    public void setMotivo(String sMotivo) {
        MarcarNoFactJB.sMotivo = sMotivo;
    }
}
