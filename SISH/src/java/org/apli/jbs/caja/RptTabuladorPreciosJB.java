package org.apli.jbs.caja;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.ServletContext;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apli.jbs.UsuarioJB;
import org.apli.modelbeans.TabuladorPrecios;
import org.apli.modelbeans.ventaCredito.CompaniaCred;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author BAOZ
 */
@ManagedBean(name = "oRptTabPreJB")
@ViewScoped
public class RptTabuladorPreciosJB implements Serializable{
private int nIdEmpSelec;
private List<CompaniaCred> arrListaCiaCred;
private List<TabuladorPrecios> arrRpt;
private List<TabuladorPrecios> arrFiltrado;
private String sTitTab="";
private StreamedContent arch;

    public RptTabuladorPreciosJB() {
        try{
            arrListaCiaCred = (new CompaniaCred()).buscaCiasCredNoSH();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void buscaTabulador(){
    FacesContext facesContext = FacesContext.getCurrentInstance();
    TabuladorPrecios oTab = new TabuladorPrecios();
        this.sTitTab = "";
        if (this.nIdEmpSelec>-1){
            try{
                oTab.getCiaCred().setIdEmp(nIdEmpSelec);
                arrRpt = oTab.buscaRptTabulador();
                this.sTitTab = "Tabulador de Precios";
            }catch(Exception e){
                e.printStackTrace();
            }
        }else{
            facesContext.addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Error", "Falta indicar la compañía"));
        }
    }
    
    public int getIdEmpSelec(){
        return this.nIdEmpSelec;
    }
    public void setIdEmpSelec(int value){
        this.nIdEmpSelec = value;
    }
    
    public List<CompaniaCred> getListaCiaCred(){
        return this.arrListaCiaCred;
    }
    
    public List<TabuladorPrecios> getListaPrecios(){
        return this.arrRpt;
    }
    
    public List<TabuladorPrecios> getFiltrado(){
        return this.arrFiltrado;
    }
    public void setFiltrado(List<TabuladorPrecios> value){
        this.arrFiltrado = value;
    }
    
    public String getTitTabla(){
        return this.sTitTab;
    }    
    
    /*Para exportar archivos*/
    public void preProcessPDF(Object document) throws IOException, 
            BadElementException, DocumentException {
    Document pdf = (Document) document;
    SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
    String sTit="TABULADOR DE PRECIOS DE ";
    CompaniaCred oCia;
        
        if (this.nIdEmpSelec == 0)
            sTit = sTit + " SANATORIO HUERTA";
        else{
            oCia = new CompaniaCred();
            try{
                sTit = sTit + oCia.buscaNombreEmpresa(nIdEmpSelec).getNomRazSoc();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        pdf.open();
        pdf.setPageSize(PageSize.LETTER);

        pdf.add(new Phrase(sTit+"\nFecha: " + formato.format(new Date())+"\n"));
        
        pdf.add(new Phrase("\t \t \t \t \t \t \t \t \t Línea de Ingreso \t Clave Interna \t \t \t Concepto de Ingreso \t Precio"));
 
        //ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        //String logo = servletContext.getRealPath("") + File.separator + "resources" + File.separator + "demo" + File.separator + "images" + File.separator + "prime_logo.png";
    }
    
    public StreamedContent getArch() {
    String sRuta="", sParcial="";
    InputStream stream = null;
        try{
            sParcial = "/archdescarga/Tabulador"+(
                    new UsuarioJB().getSesionUsuario().getUsuario()) + ".xlsx";
            sRuta = FacesContext.getCurrentInstance().getExternalContext().
            getRealPath("")+sParcial;
            escribirExcel(sRuta);
            stream = ((ServletContext)FacesContext.getCurrentInstance().getExternalContext()
                .getContext()).getResourceAsStream(sParcial);
            arch = new DefaultStreamedContent(stream, 
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", 
                    "Tabulador.xlsx");
        }catch(Exception e){
            arch = null;
            e.printStackTrace();
        }
        return arch;
    } 
    
    public void exportarExcel(ActionEvent ae){
    String sRuta="";
        try{
            sRuta = FacesContext.getCurrentInstance().getExternalContext().
            getRealPath("")+"/archdescarga/Tabulador"+(
                    new UsuarioJB().getSesionUsuario().getUsuario()) + ".xlsx";
            escribirExcel(sRuta);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void escribirExcel(String sRutaNom) throws Exception{
    XSSFWorkbook oWorkbook = new XSSFWorkbook();
    FileOutputStream fileOut = new FileOutputStream(sRutaNom);
    XSSFSheet oHoja = oWorkbook.createSheet("Hoja1");
    Map<String, Object[]> oDatos = new HashMap<String, Object[]>();
    XSSFRow oLin;
    XSSFCell oCelda;
    SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
    String sTit="TABULADOR DE PRECIOS DE ";
    CompaniaCred oCia;
    Set<String> newRows;
    int rownum = 0;
        
        if (this.nIdEmpSelec == 0)
            sTit = sTit + " SANATORIO HUERTA";
        else{
            oCia = new CompaniaCred();
            try{
                sTit = sTit + oCia.buscaNombreEmpresa(nIdEmpSelec).getNombreCorto();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        oLin = oHoja.createRow(rownum++);
        oCelda = oLin.createCell(0);
        oCelda.setCellValue(sTit);
        oLin = oHoja.createRow(rownum++);
        oCelda = oLin.createCell(0);
        oCelda.setCellValue("LINEA DE INGRESO");
        oCelda = oLin.createCell(1);
        oCelda.setCellValue("CLAVE INTERNA");
        oCelda = oLin.createCell(2);
        oCelda.setCellValue("CONCEPTO DE INGRESO");
        oCelda = oLin.createCell(3);
        oCelda.setCellValue("PRECIO");
        for(int i=0; i<this.arrRpt.size(); i++){
            oLin = oHoja.createRow(rownum++);
            oCelda = oLin.createCell(0);
            oCelda.setCellValue(this.arrRpt.get(i).getConcepIngr().
                    getLineaIngreso().getDescrip());
            oCelda = oLin.createCell(1);
            oCelda.setCellValue(this.arrRpt.get(i).getConcepIngr().
                    getCveConcep());
            oCelda = oLin.createCell(2);
            oCelda.setCellValue(this.arrRpt.get(i).getConcepIngr().
                    getDescripcion());
            oCelda = oLin.createCell(3);
            oCelda.setCellValue(new BigDecimal(this.arrRpt.get(i).getMonto()).
                    setScale(2, RoundingMode.HALF_UP).doubleValue());
        }
        
        oWorkbook.write(fileOut);
        fileOut.close();
    }
}
