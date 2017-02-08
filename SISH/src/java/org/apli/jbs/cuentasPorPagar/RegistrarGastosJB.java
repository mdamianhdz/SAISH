package org.apli.jbs.cuentasPorPagar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.apli.modelbeans.AreaDeServicio;
import org.apli.modelbeans.AreaFisica;
import org.apli.modelbeans.ConceptoEgreso;
import org.apli.modelbeans.contabilidadInterna.Gasto;
import org.apli.modelbeans.contabilidadInterna.LineaEgreso;
import org.apli.modelbeans.contabilidadInterna.Proveedor;
import org.apli.modelbeans.contabilidadInterna.SublineaEgreso;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Lily_LnBnd
 */
@ManagedBean(name="oRegGastos")
@ViewScoped
public class RegistrarGastosJB implements Serializable{
private int nProv, nAreaFis;
private String sAreaServ;
private List<Gasto> listGastos;
private List<AreaFisica> listAreaF;
private List<AreaDeServicio> listAreaS;
private List<SublineaEgreso> listSublineas;
private List<ConceptoEgreso> listConceptoEgr;
private Gasto currentGasto;
private boolean bDeducible=false;
private UploadedFile file;
private boolean bReadOnly=false;
private boolean bRenderBotonOp=true;
public InputStream in;
    
    public RegistrarGastosJB() throws Exception{
        listAreaS=new AreaDeServicio().buscaAreasServicio();
        listAreaF=new AreaFisica().buscaTodosAF();
    }
    
    public void buscaGastos()throws Exception{
        String sCondicion="";
        if (nProv!=0)
            sCondicion="pnIdProv="+nProv;
        if (nAreaFis!=0){
            if (sCondicion.equals(""))
                sCondicion="pnCveAreaFis="+nAreaFis;
            else
                sCondicion=sCondicion+" and pnCveAreaFis="+nAreaFis;
        }
        if (!(sAreaServ==null||sAreaServ.equals("S"))){
            if (sCondicion.equals(""))
                sCondicion="pnCveAreaServ="+sAreaServ;
            else
                sCondicion=sCondicion+" and pnCveAreaServ="+sAreaServ;
        }
        listGastos=new Gasto().buscaGastos(sCondicion);
        
    }
    
    public void nuevoGasto(){
        bRenderBotonOp=true;
        currentGasto=new Gasto();
        currentGasto.setIdGasto(0);
        listSublineas=new ArrayList();
        listConceptoEgr=new ArrayList();
        listAreaF=new ArrayList();
        listAreaS=new ArrayList();    
        file=null;
        bReadOnly=false;
    }
    
    public void consultaGasto(Gasto oGasto, String sOp) throws Exception{
        System.out.println("consultaGasto");
        currentGasto=oGasto;
        bReadOnly=false;
        bRenderBotonOp=true;
        listSublineas=new SublineaEgreso().buscaSublineas(currentGasto.getConcEgreso().getSublineaEgreso().getLineaEgre().getCveLineaEgr());
        listConceptoEgr=new ConceptoEgreso().buscaConceptosEgreso(currentGasto.getConcEgreso().getSublineaEgreso().getCveSublineaEgre());
        listAreaF=new AreaFisica().buscaAreasFConceptoEgreso(currentGasto.getConcEgreso().getCveConcepEgr());
        listAreaS=new AreaDeServicio().buscaAreasSConceptoEgreso(currentGasto.getConcEgreso().getCveConcepEgr());
        if (!oGasto.getFactura().equals(""))
            bDeducible=true;
        if (sOp.equals("C")||sOp.equals("E"))
            bReadOnly=true;
        if (sOp.equals("E"))
            bRenderBotonOp=false;
    }
    
    public void buscaSublineas(){
        try{
            listSublineas=new SublineaEgreso().buscaSublineas(currentGasto.getConcEgreso().getSublineaEgreso().getLineaEgre().getCveLineaEgr());
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void buscaConceptos(){
        try{
            listConceptoEgr=new ConceptoEgreso().buscaConceptosEgreso(currentGasto.getConcEgreso().getSublineaEgreso().getCveSublineaEgre());
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void buscaAreas(){
        try{
            listAreaF=new AreaFisica().buscaAreasFConceptoEgreso(currentGasto.getConcEgreso().getCveConcepEgr());
            listAreaS=new AreaDeServicio().buscaAreasSConceptoEgreso(currentGasto.getConcEgreso().getCveConcepEgr());
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void guardaGasto() {
        String mess="";
        if(currentGasto==null||currentGasto.getDescripcion() == null||currentGasto.getDescripcion().equals("")||currentGasto.getMonto()==0||
                currentGasto.getProv().getIdProv()==0|| currentGasto.getSituacion()==' '|| currentGasto.getSituacion()=='S'||
                currentGasto.getConcEgreso().getSublineaEgreso().getLineaEgre().getCveLineaEgr()==0||
                currentGasto.getConcEgreso().getSublineaEgreso().getCveSublineaEgre()==0||
                currentGasto.getConcEgreso().getCveConcepEgr()==0||
                (currentGasto.getAreaFis().getCve()==0&&(currentGasto.getAreaServ().getCve()==null||currentGasto.getAreaServ().getCve().equals("")))) {
            FacesMessage message = new FacesMessage("Error", "Error de Validación, faltan datos");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }else{
            if (bDeducible==true){
                if (currentGasto.getFactura() == null || currentGasto.getFactura().equals("")){
                    FacesMessage message = new FacesMessage("Error de Validación", "No se ha especificado la factura");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }else{
                    if (file==null){
                        FacesMessage message = new FacesMessage("Error de Validación", "No se ha cargado el archivo XML.");
                        FacesContext.getCurrentInstance().addMessage(null, message);
                    }else{
                        currentGasto.setXML(file.getFileName());
                        try{
                            if (currentGasto.getIdGasto()==0){
                                mess=currentGasto.guardaGasto(currentGasto);
                                guardaArchivo(currentGasto.buscaIdGasto());
                            }
                            else{
                                mess=currentGasto.modificaGasto(currentGasto);
                                guardaArchivo(currentGasto.getIdGasto());
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                        FacesMessage message = new FacesMessage("Guardar", mess);
                        FacesContext.getCurrentInstance().addMessage(null, message);
                    }
                }
            }else{
                currentGasto.setFactura("");
                currentGasto.setXML("");
                try{
                    if (currentGasto.getIdGasto()==0){
                        mess=currentGasto.guardaGasto(currentGasto);
                    }else{
                        mess=currentGasto.modificaGasto(currentGasto);
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
                FacesMessage message = new FacesMessage("Guardar", mess);
                FacesContext.getCurrentInstance().addMessage(null, message);
                try{
                    buscaGastos();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
    
    public void eliminaGasto(){
        try{
            String mess=currentGasto.eliminaGasto(currentGasto);
            FacesMessage message = new FacesMessage("Guardar", mess);
            FacesContext.getCurrentInstance().addMessage(null, message);
            buscaGastos();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void handleFileUpload(FileUploadEvent event){
        file=event.getFile();
        try{
            in=file.getInputstream();
            FacesMessage message = new FacesMessage("Factura XML", file.getFileName() + "se ha cargado correctamente.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void guardaArchivo(int idGasto){
        ExternalContext extCont= FacesContext.getCurrentInstance().getExternalContext();
        File folder= new File(extCont.getRealPath("//BovedaFiscal//Proveedores//"));
        try{
            OutputStream out=new FileOutputStream (new File(folder, ""+idGasto+"_"+file.getFileName()));
            int read=0;
            byte[] bytes=new byte[1024];
            while((read=in.read(bytes))!=-1){
                out.write(bytes,0,read);
            }
            in.close();
            out.flush();
            out.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public String cambiaSituacion(char val){
        String ret="";
        if (val=='0')
            ret="Por autorizar";
        if (val=='1')
            ret="Pago Autorizado";
        if (val=='2')
            ret="Pagado";
        return ret;  
    }
    
    public boolean renderBotones(char val){
        if (val=='0')
            return true;
        else
            return false;
    }

    public boolean isDeducible() {
        return bDeducible;
    }

    public void setDeducible(boolean value) {
        System.out.println(bDeducible);
        if (bDeducible==false){
            currentGasto.setXML("");
            currentGasto.setFactura("");
        }
        bDeducible = value;
    }

    public List<Gasto> getListGastos() {
        return listGastos;
    }

    public void setListCompras(List<Gasto> value) {
        listGastos = value;
    }

    public List<AreaFisica> getListAreaF(){
        return listAreaF;
    }

    public void setListAreaF(List<AreaFisica> value) {
        listAreaF = value;
    }

    public List<Proveedor> getListProvedores() throws Exception{
        return new Proveedor().buscaProveedores();
    }
    
    public List<AreaDeServicio> getListAreaS(){
        return listAreaS;
    }

    public void setListAreaS(List<AreaDeServicio> value) {
        listAreaS = value;
    }

    public Gasto getCurrentGasto() {
        return currentGasto;
    }

    public void setCurrentGasto(Gasto value) {
        currentGasto= value;
    }
    
    public List<LineaEgreso> getListLineasEgreso() throws Exception{
        return new LineaEgreso().buscaTodosLineaEgreso();
    }
    
    public List<SublineaEgreso> getListSublineas() throws Exception{
        return listSublineas;
    }
    
    public void getListLineasEgreso(List<SublineaEgreso> value){
        listSublineas=value;
    }

    public List<ConceptoEgreso> getListConceptoEgr() {
        return listConceptoEgr;
    }

    public void setListConceptoEgr(List<ConceptoEgreso> value) {
        listConceptoEgr = value;
    }

    public int getProv() {
        return nProv;
    }

    public void setProv(int value) {
        nProv = value;
    }

    public int getAreaFis() {
        return nAreaFis;
    }

    public void setAreaFis(int value) {
        nAreaFis = value;
    }

    public String getAreaServ() {
        return sAreaServ;
    }

    public void setAreaServ(String value) {
        sAreaServ = value;
    }

    public boolean isReadOnly() {
        return bReadOnly;
    }

    public void setReadOnly(boolean value) {
        bReadOnly = value;
    }
    
    public boolean isRenderBotonOp() {
        return bRenderBotonOp;
    }

    public void setRenderBotonOp(boolean value) {
        bRenderBotonOp = value;
    }
}
