package org.apli.jbs.contabilidadInterna;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import org.apli.modelbeans.contabilidadInterna.Gasto;
import org.apli.modelbeans.contabilidadInterna.Proveedor;
import org.apli.modelbeans.facturacion.cfdi.FacturaCFI;
import org.apli.modelbeans.facturacion.cfdi.v32.schema.SerieFiscal;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author Lily_LnBnd
 */
@ManagedBean(name="oBoveda")
@ViewScoped
public class ConsultarBovedaFiscalJB implements Serializable{
private List<FacturaCFI> listFacturas;
private FacturaCFI selectedFactura;
private List<Gasto> listGastos;
private Gasto selectedGasto;
private int nTipoFactura;
private String sSerie;
private String sFolio;
private int nProveedor;
private String sFactura;
private StreamedContent file;
    
    public ConsultarBovedaFiscalJB(){
    }
    
    public void habilitaBuscar(){
        if (nTipoFactura==1) {
            nProveedor=0;
            sFactura="";
        }else{
            sSerie="";
            sFolio="";
        }
    }
    
    public void buscar()throws Exception{
        listFacturas=new ArrayList();
        listGastos=new ArrayList();
        String sCondicion="";
        if (nTipoFactura==0){
            FacesMessage message = new FacesMessage("Error", "Seleccione el tipo de facturas que desea consultar.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }else{
            if (nTipoFactura==1){
                if ("".equals(sSerie)){
                    FacesMessage message = new FacesMessage("Error", "Indique la serie de las facturas que desea consultar");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }else{
                    sCondicion=" psnombreserie='"+sSerie+"'";
                    if (!"".equals(sFolio)&&sFolio!=null)
                        sCondicion=sCondicion + "and pnfolio="+sFolio;
                    listFacturas=new FacturaCFI().buscaBovedaInterna(sCondicion);
                }            
            }else{
                if (nProveedor==0){
                    FacesMessage message = new FacesMessage("Error", "Seleccione el proveedor de las facturas que desea consultar.");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }else{
                    sCondicion=" pnidprov="+nProveedor;
                    if(!"".equals(sFactura)&&sFactura!=null)
                        sCondicion=sCondicion+" and psfactura='"+sFactura+"'";
                    listGastos=new Gasto().buscaBovedaProveedores(sCondicion);
                }
            }
        }        
    }
    
    public StreamedContent getFile(){
        InputStream stream=null;
        file = null;
        if (selectedFactura== null &&
            selectedGasto ==null){
            FacesMessage message = new FacesMessage("Error", "Falta indicar factura a descargar.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }else{
            if (nTipoFactura==1){
                stream = ((ServletContext)FacesContext.getCurrentInstance().getExternalContext()
                .getContext()).getResourceAsStream(selectedFactura.getRutaXML()+selectedFactura.getNombreXML());
                
                file = new DefaultStreamedContent(stream, "text/xml", selectedFactura.getNombreXML());
            }else{
                System.out.println(selectedGasto.getRutaXML()+selectedGasto.getXML());
                stream = ((ServletContext)FacesContext.getCurrentInstance().getExternalContext()
                .getContext()).getResourceAsStream(selectedGasto.getRutaXML()+selectedGasto.getXML());
                System.out.println(stream);
                file = new DefaultStreamedContent(stream, "text/xml", selectedGasto.getXML()); 
            }
        }
        return file;
    }
    
    public String displayTable(int nVar){
        if (nVar==nTipoFactura)
            return "block";
        else
            return "none";
    }
    
    public void limpiar(){
        nTipoFactura=0;
        nProveedor=0;
        sFactura="";
        sSerie="";
        sFolio="";
        listFacturas=new ArrayList();
        listGastos=new ArrayList();
    }
    
    public List<FacturaCFI> getListFacturas() {
        return listFacturas;
    }

    public void setListFacturas(List<FacturaCFI> value) {
        listFacturas = value;
    }

    public FacturaCFI getSelectedFactura() {
        return selectedFactura;
    }

    public void setSelectedFactura(FacturaCFI value) {
        selectedFactura = value;
    }
    
    public List<Gasto> getListGastos() {
        return listGastos;
    }

    public void setListGastos(List<Gasto> value) {
        listGastos = value;
    }

    public Gasto getSelectedGasto() {
        return selectedGasto;
    }
    
    public void setSelectedGasto(Gasto value) {
        selectedGasto=value;
    }

    public List<SerieFiscal> getListSeries() throws Exception{
        return new SerieFiscal().getSeriesFiscales();
    }
    
    public List<Proveedor> getListProveedores() throws Exception{
        return new Proveedor().buscaProveedores();
    }

    public int getTipoFactura() {
        return nTipoFactura;
    }

    public void setTipoFactura(int value) {
        nTipoFactura = value;
    }

    public String getSerie() {
        return sSerie;
    }

    public void setSerie(String value) {
        sSerie = value;
    }

    public String getFolio() {
        return sFolio;
    }

    public void setFolio(String value) {
        sFolio = value;
    }

    public int getProveedor() {
        return nProveedor;
    }

    public void setProveedor(int value) {
        nProveedor = value;
    }

    public String getFactura() {
        return sFactura;
    }

    public void setFactura(String sFactura) {
        this.sFactura = sFactura;
    }
}
