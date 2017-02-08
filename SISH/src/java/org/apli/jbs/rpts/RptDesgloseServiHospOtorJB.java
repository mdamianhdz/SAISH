
package org.apli.jbs.rpts;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apli.modelbeans.reportes.RptDesgloseServiciosHospOtorgados;

/**
 *Bean Administrado para el control del reporte de Desglose de Servicios
 * Hospitalizados Otorgados
 * @author Danny Hdz
 */
@ManagedBean(name = "oRptSerHospOtorJB")
@ViewScoped
public class RptDesgloseServiHospOtorJB implements Serializable{
    private Date fecha;
    private List<RptDesgloseServiciosHospOtorgados> oLista;
    private List<RptDesgloseServiciosHospOtorgados> oListaLineaCre;
    private List<RptDesgloseServiciosHospOtorgados> oListaLineaCon;
    
    private float fPrecioTotalNormal;
    private float fPrecioTotalCobrado;
    private float fTotContado;
    private float fTotCredito;
    
    public RptDesgloseServiHospOtorJB(){   }
        
    public void buscar(){
        RptDesgloseServiciosHospOtorgados oRpt = new RptDesgloseServiciosHospOtorgados();
        String msg="";
            try{
                oLista=oRpt.buscaRpt();
            }catch(Exception e){
                e.printStackTrace();
                FacesContext context= FacesContext.getCurrentInstance();
                msg="error al buscar el reporte";
                context.addMessage(null, new FacesMessage("Desglose Servicios Otorgados", msg));
            }
    }
    
    public List<RptDesgloseServiciosHospOtorgados> getLineaCre(){
        RptDesgloseServiciosHospOtorgados oRpt = new RptDesgloseServiciosHospOtorgados();
        String msg="";
            try{
                oListaLineaCre=oRpt.buscaRptLinea("CRE");
            }catch(Exception e){
                e.printStackTrace();
                FacesContext context= FacesContext.getCurrentInstance();
                msg="error al buscar el reporte";
                context.addMessage(null, new FacesMessage("Desglose Servicios Otorgados", msg));
            }
            return oListaLineaCre;
    }
    
    public List<RptDesgloseServiciosHospOtorgados> getLineaCon(){
        RptDesgloseServiciosHospOtorgados oRpt = new RptDesgloseServiciosHospOtorgados();
        String msg="";
            try{
                oListaLineaCon=oRpt.buscaRptLinea("CON");
            }catch(Exception e){
                e.printStackTrace();
                FacesContext context= FacesContext.getCurrentInstance();
                msg="error al buscar el reporte";
                context.addMessage(null, new FacesMessage("Desglose Servicios Otorgados", msg));
            }
            return oListaLineaCon;
    }
    
    public Float getTotPrecioNormal(){
        if(oLista==null)
            return 0f;
        else{
            for(int i=0;i<oLista.size();i++){
                fPrecioTotalNormal=fPrecioTotalNormal+oLista.get(i).getPrecio();
            }
        }
        return fPrecioTotalNormal;
    }
        
    public Float getTotal(){
        if(oLista==null)
            return 0f;
        else{
            for(int i=0; i<oLista.size();i++){
                fPrecioTotalCobrado=fPrecioTotalCobrado+oLista.get(i).getTotal();
                if(oLista.get(i).getCredOcont().compareTo("Contado")==0)
                    fTotContado=fTotContado+oLista.get(i).getTotal();
                else
                    fTotCredito=fTotCredito+oLista.get(i).getTotal();
            }
        }
        return fPrecioTotalCobrado;
    }
    
    public Float getTotContado(){
        return fTotContado;
    }
    
    public Float getTotCredito(){
        return fTotCredito;
    }
    
    public List<RptDesgloseServiciosHospOtorgados> getLista(){
        buscar();
        return oLista;
    }
    
    
    //PARA EXPORTAR ARCHIVOS
    public void preProcessPDF(Object document) throws IOException, 
            BadElementException, DocumentException {
        
    Document pdf = (Document) document;
    SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
    String sTit="DESGLOCE DE SERVICIOS HOSPITALIZADOS OTORGADOS";
       pdf.setPageSize(PageSize.A4.rotate());
       pdf.open();
       pdf.add(new Phrase(sTit+"\nFecha: " + formato.format(new Date())+"\n"));
       //pdf.add(new Phrase("Total Pagos de Préstamos: $ " +"       "+"\n"));
       
       //pdf.add(new Phrase("\t \t \t \t Hora \t Folio \t\t\t\t\t\t\t Folio del Personal \t\t\t\t\t\t\t\t\t\t Nombre Empleado \t Total del Préstamo \t Fecha del Préstamo  \t Folio del Préstamo \t Pagos Realizados \t Condición \t Pago del Dia \t Saldo por Pagar \t Forma de Pago"));
       //ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
       //String logo = servletContext.getRealPath("") + File.separator + "resources" + File.separator + "demo" + File.separator + "images" + File.separator + "prime_logo.png";
    }
    
}