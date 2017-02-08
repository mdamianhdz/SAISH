/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apli.jbs.utilidades;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.apli.modelbeans.Plantilla;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author JRuiz
 */
@ManagedBean(name="oPlantilla")
@SessionScoped
public class PlantillaJB implements Serializable {
    
    private Plantilla oPlantilla=new Plantilla();
    List<Plantilla> listaElementos;
    private String logo;
    private String logo2;
    private String pie;
    private String previo;
    private String saltosLinea="</br></br>";
    private String tema;
    private String tema2;
    
    
    public PlantillaJB() throws Exception{  
        ListElementos();
    }
    
    public void ListElementos() throws Exception{
        listaElementos=new ArrayList<Plantilla>();
        listaElementos=oPlantilla.todosElementosPlantilla();
        for(int i=0;i<listaElementos.size();i++){
            if(listaElementos.get(i).getVariable().equals("Logo")){
                logo=listaElementos.get(i).getValor();
            }
            if(listaElementos.get(i).getVariable().equals("Pie")){
                pie=listaElementos.get(i).getValor();
            }
            if(listaElementos.get(i).getVariable().equals("Tema")){
                tema=listaElementos.get(i).getValor();
            }            
        }
    }
    
    public void validaLogo(String s){
        if(s.equals("PSM")){
            logo2="/"+logo;
        }else{
            logo2="../"+logo;
        }
    }
    
    public boolean validaImg(String img){
        boolean condicion=false;
        if(img.indexOf(".jpg")>0 || img.indexOf(".gif")>0 ){
            condicion=true;
        }
        return condicion;
    }
    
     public boolean validaTextImg(String img){
        boolean condicion=true;
        if(img.indexOf(".jpg")>0 || img.indexOf(".gif")>0 ){
            condicion=false;
        }
        return condicion;
    }
     
     public boolean validaInput(String img){
        boolean condicion=true;
        if(img.equals("Tema") || img.equals("Logo") ){
            condicion=false;
        }
        return condicion;
    }
     
     public boolean validaFondo(String fond){
        boolean condicion=false;
        if(fond.equals("Fondo") ){
            condicion=true;
        }
        return condicion;
    }
     
    public boolean validaTema(String t){
        boolean condicion=false;
        if(t.equals("Tema") ){
            condicion=true;
        }
        return condicion;
    } 
    
    public void handleFileUpload(FileUploadEvent event) {
        try{
            previo="utilidades/"+event.getFile().getFileName();
            oPlantilla.setValor(previo);
            String rst=oPlantilla.actualizaLogo();
            if(rst.indexOf("ERROR")>0){
                FacesMessage msj2= new FacesMessage(rst,"");
                FacesContext.getCurrentInstance().addMessage(null, msj2);
            }else{
                ExternalContext extCont= FacesContext.getCurrentInstance().getExternalContext();
                File folder= new File(extCont.getRealPath("//utilidades//"));
                InputStream in=event.getFile().getInputstream();
                OutputStream out=new FileOutputStream (new File(folder, event.getFile().getFileName()));
                int read=0;
                byte[] bytes=new byte[1024];
                while((read=in.read(bytes))!=-1){
                    out.write(bytes,0,read);
                }
                in.close();
                out.flush();
                out.close();

                if(!logo.equals(previo)){
                    File archivo= new File(extCont.getRealPath(logo));
                    if(archivo.delete()){
                    }
                }
                    
                oPlantilla=new Plantilla();
                ListElementos();
                FacesContext context= FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage("La imagen ", event.getFile().getFileName() + " se actualizó."));
                context.getExternalContext().getFlash().setKeepMessages(true);
                logo2="../"+previo;
                context.getExternalContext().redirect("admPlantilla.xhtml?faces-redirect=true");
                    
            }            
        }catch(Exception e){            
            String rst=":( lo sentimos hay un error";
            FacesMessage msj2= new FacesMessage(rst,"");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
        }
    }
    
    public void actualiza(RowEditEvent event) throws Exception{
        String rst="";  
         
              try{                            
                rst=((Plantilla) event.getObject()).actualizaPlantilla();
                if(rst.indexOf("ERROR")>0){
                    FacesMessage msj2= new FacesMessage(rst,"");
                    FacesContext.getCurrentInstance().addMessage(null, msj2);
                }
                else{
                    FacesContext context= FacesContext.getCurrentInstance();
                    context.addMessage(null, new FacesMessage(rst,((Plantilla) event.getObject()).getVariable()));
                    context.getExternalContext().getFlash().setKeepMessages(true);
                    context.getExternalContext().redirect("admPlantilla.xhtml");
                }
             }catch(Exception e){
                   rst="Error.Exception";
                   FacesMessage msj2= new FacesMessage(rst,"");
                   FacesContext.getCurrentInstance().addMessage(null, msj2);
                   e.printStackTrace();
             }
             ListElementos();         
    }
    
    public void guardaTema() throws Exception {
    String rst="";
    Map<String, String> params = 
            FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        try{
            oPlantilla.setValor(tema2);
            rst=oPlantilla.actualizaTema();
            if(rst.indexOf("ERROR")>0){
                FacesMessage msj2= new FacesMessage(rst,"");
                FacesContext.getCurrentInstance().addMessage(null, msj2);
            }
            else{
                FacesContext context= FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage(rst,"Actualizado el tema: "+tema2));
                context.getExternalContext().getFlash().setKeepMessages(true);
                context.getExternalContext().redirect("admPlantilla.xhtml");
                
                tema2="";
            }
         }catch(Exception e){
            rst="Error.Exception";
            FacesMessage msj2= new FacesMessage(rst,"");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
            e.printStackTrace();
         }
         ListElementos();
         oPlantilla=new Plantilla();        
    }
    
    public void cancelar() throws IOException {
        tema2 = "";
        FacesContext context= FacesContext.getCurrentInstance();
        context.getExternalContext().getFlash().setKeepMessages(true);
        context.getExternalContext().redirect("admPlantilla.xhtml");
    }
    
    public List<Plantilla> getListElementosPlant(){
        return listaElementos;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getPie() {
        return pie;
    }

    public void setPie(String pie) {
        this.pie = pie;
    }

    public String getSaltosLinea() {
        return saltosLinea;
    }

    public void setSaltosLinea(String saltosLinea) {
        this.saltosLinea = saltosLinea;
    }

    public String getPrevio() {
        return previo;
    }

    public void setPrevio(String previo) {
        this.previo = previo;
    }

    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }
    
    public String getTema2() {
        return tema2;
    }

    public void setTema2(String tema2) {
        this.tema2 = tema2;
    }

    public String getLogo2() {
        return logo2;
    }

    public void setLogo2(String logo2) {
        this.logo2 = logo2;
    }
    
    public String getValorPie(){
        return "Pie";
    }
}
