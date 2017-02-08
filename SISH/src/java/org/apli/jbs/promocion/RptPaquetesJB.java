package org.apli.jbs.promocion;

import java.io.Serializable;
import java.util.ArrayList;

import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apli.modelbeans.reportes.RptPaquetes;

/**
 * Control de Reportes de la situacion de Paquetes Contratados, Activados, Finalizados y Cancelados,
 * por tipo de paquete,personal que contrata o todos
 * @author AIMEE R
 */
@ManagedBean (name="oRptPqPromo")
@ViewScoped
public class RptPaquetesJB implements Serializable{

    private Date fechaini;
    private Date fechafin;
    private String ope="";
    private String tpq="";
    private String sentencia="";
    private String personal="";
    
    private List<RptPaquetes> listRptPq;
    
    private RptPaquetes oRptPq;
    private RptPaquetes[] arrRptPq;
    
    private boolean bTodos=false;
    private boolean bTipoPaquete=false;
    private boolean bTipoPersonal=false;
    
    public RptPaquetesJB() {
        
    }
    
    public void validaFecha(){
        String mess="";
        if (fechaini==null)
            mess="No ha especificado la fecha de inicio";
        else
            if (fechafin!=null)
                if (fechaini.compareTo(fechafin)>0)
                    mess="La fecha final del periodo debe ser posterior a la fecha de inicio";
        if (!"".equals(mess)){
            FacesContext context= FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Reporte de Paquetes",mess));
            context.getExternalContext().getFlash().setKeepMessages(true);
        }
    }
    
    public void buscaReportesPaquetesContratados(){
        if(this.ope.equals("T")){
            try{
                oRptPq=new RptPaquetes();
                listRptPq=new ArrayList<RptPaquetes>();
                arrRptPq= (RptPaquetes[])oRptPq.buscaPaquetesContratados(fechaini, fechafin, sentencia);
                for(int i=0; i<arrRptPq.length; i++){
                    RptPaquetes temp= arrRptPq[i];
                    listRptPq.add(temp);
                }
            } catch(Exception e){
                e.printStackTrace();
            }
        } else if(this.ope.equals("Pq")){
            try{
                oRptPq=new RptPaquetes();
                listRptPq=new ArrayList<RptPaquetes>();
                sentencia=" tipopaquete='"+tpq+"' AND ";
                arrRptPq= (RptPaquetes[])oRptPq.buscaPaquetesContratados(fechaini, fechafin, sentencia);
                for(int i=0; i<arrRptPq.length; i++){
                    RptPaquetes temp= arrRptPq[i];
                    listRptPq.add(temp);
                }
            } catch(Exception e){
                e.printStackTrace();
            }
        } else if(this.ope.equals("Pc")){
            try{
                oRptPq=new RptPaquetes();
                listRptPq=new ArrayList<RptPaquetes>();
                sentencia=" personalcontrato='"+personal+"' AND ";
                arrRptPq= (RptPaquetes[])oRptPq.buscaPaquetesContratados(fechaini, fechafin, sentencia);
                for(int i=0; i<arrRptPq.length; i++){
                    RptPaquetes temp= arrRptPq[i];
                    listRptPq.add(temp);
                }
            } catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    
    public void buscaReportesPaquetesActivados(){
        if(this.ope.equals("T")){
            try{
                oRptPq=new RptPaquetes();
                listRptPq=new ArrayList<RptPaquetes>();
                arrRptPq= (RptPaquetes[])oRptPq.buscaPaquetesActivados(fechaini, fechafin, sentencia);
                for(int i=0; i<arrRptPq.length; i++){
                    RptPaquetes temp= arrRptPq[i];
                    listRptPq.add(temp);
                }
            } catch(Exception e){
                e.printStackTrace();
            }
        } else if(this.ope.equals("Pq")){
            try{
                oRptPq=new RptPaquetes();
                listRptPq=new ArrayList<RptPaquetes>();
                sentencia=" tipopaquete='"+tpq+"' AND ";
                arrRptPq= (RptPaquetes[])oRptPq.buscaPaquetesActivados(fechaini, fechafin, sentencia);
                for(int i=0; i<arrRptPq.length; i++){
                    RptPaquetes temp= arrRptPq[i];
                    listRptPq.add(temp);
                }
            } catch(Exception e){
                e.printStackTrace();
            }
        } else if(this.ope.equals("Pc")){
            try{
                oRptPq=new RptPaquetes();
                listRptPq=new ArrayList<RptPaquetes>();
                sentencia=" personalcontrato='"+personal+"' AND ";
                arrRptPq= (RptPaquetes[])oRptPq.buscaPaquetesActivados(fechaini, fechafin, sentencia);
                for(int i=0; i<arrRptPq.length; i++){
                    RptPaquetes temp= arrRptPq[i];
                    listRptPq.add(temp);
                }
            } catch(Exception e){
                e.printStackTrace();
            }
        }        
    }
    
    public void buscaReportesPaquetesFinalizados(){
        if(this.ope.equals("T")){
            try{
                oRptPq=new RptPaquetes();
                listRptPq=new ArrayList<RptPaquetes>();
                arrRptPq= (RptPaquetes[])oRptPq.buscaPaquetesFinalizados(fechaini, fechafin, sentencia);
                for(int i=0; i<arrRptPq.length; i++){
                    RptPaquetes temp= arrRptPq[i];
                    listRptPq.add(temp);
                }
            } catch(Exception e){
                e.printStackTrace();
            }
        } else if(this.ope.equals("Pq")){
            try{
                oRptPq=new RptPaquetes();
                listRptPq=new ArrayList<RptPaquetes>();
                sentencia=" tipopaquete='"+tpq+"' AND ";
                arrRptPq= (RptPaquetes[])oRptPq.buscaPaquetesFinalizados(fechaini, fechafin, sentencia);
                for(int i=0; i<arrRptPq.length; i++){
                    RptPaquetes temp= arrRptPq[i];
                    listRptPq.add(temp);
                }
            } catch(Exception e){
                e.printStackTrace();
            }
        } else if(this.ope.equals("Pc")){
            try{
                oRptPq=new RptPaquetes();
                listRptPq=new ArrayList<RptPaquetes>();
                sentencia=" personalcontrato='"+personal+"' AND ";
                arrRptPq= (RptPaquetes[])oRptPq.buscaPaquetesFinalizados(fechaini, fechafin, sentencia);
                for(int i=0; i<arrRptPq.length; i++){
                    RptPaquetes temp= arrRptPq[i];
                    listRptPq.add(temp);
                }
            } catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    
    public void buscaReportesPaquetesCancelados(){
        if(this.ope.equals("T")){
            try{
                oRptPq=new RptPaquetes();
                listRptPq=new ArrayList<RptPaquetes>();
                arrRptPq= (RptPaquetes[])oRptPq.buscaPaquetesCancelados(fechaini, fechafin, sentencia);
                for(int i=0; i<arrRptPq.length; i++){
                    RptPaquetes temp= arrRptPq[i];
                    listRptPq.add(temp);
                }
            } catch(Exception e){
                e.printStackTrace();
            }
        } else if(this.ope.equals("Pq")){
            try{
                oRptPq=new RptPaquetes();
                listRptPq=new ArrayList<RptPaquetes>();
                sentencia=" tipopaquete='"+tpq+"' AND ";
                arrRptPq= (RptPaquetes[])oRptPq.buscaPaquetesCancelados(fechaini, fechafin, sentencia);
                for(int i=0; i<arrRptPq.length; i++){ 
                    RptPaquetes temp= arrRptPq[i];
                    listRptPq.add(temp);
                }
            } catch(Exception e){
                e.printStackTrace();
            }
        } else if(this.ope.equals("Pc")){
            try{
                oRptPq=new RptPaquetes();
                listRptPq=new ArrayList<RptPaquetes>();
                sentencia=" personalcontrato='"+personal+"' AND ";
                arrRptPq= (RptPaquetes[])oRptPq.buscaPaquetesCancelados(fechaini, fechafin, sentencia);
                for(int i=0; i<arrRptPq.length; i++){
                    RptPaquetes temp= arrRptPq[i];
                    listRptPq.add(temp);
                }
            } catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    
    public void validaOpe() {
        int result = ope.compareToIgnoreCase("T"); //todos
        int result2 = ope.compareToIgnoreCase("Pq"); //tipos de paquete
        int result3=ope.compareToIgnoreCase("Pc"); //personal que contrata

        if (result == 0) {
            bTodos= true;
            bTipoPaquete= false;
            bTipoPersonal=false;
        } else if (result2 == 0) {
            bTipoPaquete= true;
            bTodos= false;
            bTipoPersonal=false;
        } else if(result3==0){
            bTipoPersonal=true;
            bTodos=false;
            bTipoPaquete=false;
        }
        else {
            bTodos= false;
            bTipoPaquete= false;
            bTipoPersonal=false;
        }
    }

    
    public List<RptPaquetes> getPersonalContrato() throws Exception {
        this.listRptPq= new ArrayList();
        this.oRptPq=new RptPaquetes();
        this.listRptPq= oRptPq.buscaPersonalContrata();
        return this.listRptPq;
    }
    
    public void setTodosb(boolean bTodos){
        this.bTodos=bTodos;
    }
    public boolean getTodosb(){
        return bTodos;
    }
    
    public void setTipoPaqueteb(boolean bTipoPaquete){
        this.bTipoPaquete=bTipoPaquete;
    }
    public boolean getTipoPaqueteb(){
        return bTipoPaquete;
    }
    
    public void setTipoPersonalb(boolean bTipoPersonal){
        this.bTipoPersonal=bTipoPersonal;
    }
    public boolean getTipoPersonalb(){
        return bTipoPersonal;
    }
    
    public void setFechaini(Date fechaini){
        this.fechaini=fechaini;
    }
    public Date getFechaini(){
        return fechaini;
    }
    
    public void setFechafin(Date fechafin){
        this.fechafin=fechafin;
    }
    public Date getFechafin(){
        return fechafin;
    }
    
    public void setRptPaquetes(RptPaquetes oRptPq){
        this.oRptPq=oRptPq;
    }
    public RptPaquetes getRptPaquetes(){
        return oRptPq;
    }
    
    public void setOpe(String ope){
        this.ope=ope;
    }
    public String getOpe(){
        return ope;
    }
    
    public void setSentencia(String sentencia){
        this.sentencia=sentencia;
    }
    public String getSentencia(){
        return sentencia;
    }
    
    public void setTpq(String tpq){
        this.tpq=tpq;
    }
    public String getTpq(){
        return tpq;
    }
    
    public void setPersonal(String personal){
        this.personal=personal;
    }
    public String getPersonal(){
        return personal;
    }
    
    public List<RptPaquetes> getListaRptPquetes() {
        return listRptPq;
    }
    public void setListaRptPaquetes(List<RptPaquetes> value) {
        listRptPq= value;
    }
    
}
