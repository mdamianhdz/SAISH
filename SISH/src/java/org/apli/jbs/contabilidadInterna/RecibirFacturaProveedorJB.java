/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apli.jbs.contabilidadInterna;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.apli.modelbeans.contabilidadInterna.Gasto;
import org.apli.modelbeans.contabilidadInterna.Proveedor;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Lily_LnBnd
 */
@ManagedBean(name="oFactProv")
@RequestScoped
public class RecibirFacturaProveedorJB implements Serializable{
    private static List<Gasto> listGastos;
    private static Gasto selectedGasto;
    private static int selectedProv;
    private static UploadedFile file;
    public static InputStream in;
    
    public RecibirFacturaProveedorJB(){
        
    }
    
    public void buscar() throws Exception{
        String sCondicion="";
        if (selectedProv>0)
            sCondicion=" pnidprov="+selectedProv;
        listGastos=new Gasto().buscaGastosXML(sCondicion);
    }
    
    public void handleFileUpload(FileUploadEvent event) throws IOException{
        file=event.getFile();
        in=file.getInputstream();
        FacesMessage message = new FacesMessage("Factura XML", file.getFileName() + "se ha cargado correctamente.");
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
    
    public void recibir()throws Exception{
        String mess="";
        if (selectedGasto==null||selectedGasto.getFactura().length()==0||file==null)
            mess="Error de Validación, faltan datos";
        else{
            selectedGasto.setXML(file.getFileName());
            mess=selectedGasto.recibirFactura(selectedGasto);
            guardaArchivo(selectedGasto.getIdGasto());
        }
        FacesMessage message = new FacesMessage("Error", mess);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
    
    public void guardaArchivo(int idGasto)throws IOException{
        System.out.println("file:"+file.getFileName());
        ExternalContext extCont= FacesContext.getCurrentInstance().getExternalContext();
        File folder= new File(extCont.getRealPath("//BovedaFiscal//Proveedores//"));
        OutputStream out=new FileOutputStream (new File(folder, ""+idGasto+"_"+file.getFileName()));
        int read=0;
        byte[] bytes=new byte[1024];
        while((read=in.read(bytes))!=-1){
            out.write(bytes,0,read);
        }
        in.close();
        out.flush();
        out.close();
    }
    
    public String cambiaSituacion(char cVar){
        if (cVar=='0')
            return "PENDIENTE DE PAGO";
        else
            if (cVar=='1')
                return "PAGO AUTORIZADO";
            else
                return "PAGADO";
    }

    public List<Gasto> getListGastos() {
        return listGastos;
    }

    public void setListGastos(List<Gasto> listGastos) {
        RecibirFacturaProveedorJB.listGastos = listGastos;
    }

    public Gasto getSelectedGasto() {
        return selectedGasto;
    }

    public void setSelectedGasto(Gasto selectedGasto) {
        RecibirFacturaProveedorJB.selectedGasto = selectedGasto;
    }

    public int getSelectedProv() {
        return selectedProv;
    }

    public void setSelectedProv(int selectedProv) {
        RecibirFacturaProveedorJB.selectedProv = selectedProv;
    }
    
    public List<Proveedor> getListProveedores() throws Exception{
        return new Proveedor().buscaProveedores();
    }
}
