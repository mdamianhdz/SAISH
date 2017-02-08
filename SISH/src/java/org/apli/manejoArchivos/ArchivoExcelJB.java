package org.apli.manejoArchivos;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.primefaces.event.FileUploadEvent;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apli.modelbeans.ManejoMsjsDB;
import org.apli.modelbeans.TabuladorPrecios;
import org.apli.modelbeans.ventaCredito.CompaniaCred;

/**
 * Bean administrado para el manejo de archivo de excel relacionado con 
 * tabuladores de precios
 * @author BAOZ
 */
@ManagedBean(name = "oArchXlsxJB")
@ViewScoped
public class ArchivoExcelJB implements Serializable{
private int nIdEmpSelec;
private List<CompaniaCred> arrListaCiaCred;
private ManejoMsjsDB oMenDB=new ManejoMsjsDB();

    public ArchivoExcelJB() {
        try{
            arrListaCiaCred = (new CompaniaCred()).buscaCiasCredNoSH();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void cargaTabulador(FileUploadEvent event) throws IOException{
    FacesContext facesContext = FacesContext.getCurrentInstance();
    String sRuta=facesContext.getExternalContext().getRealPath("")+"/archs",
           sNomArch="", sTxtMsj="";
    OutputStream out = null;
    InputStream filecontent = null;
    FacesMessage msg=null;
    ArrayList<String> arrErrores;
        if (this.nIdEmpSelec>0){
            try {
                sNomArch = event.getFile().getFileName();
                uploadFile(event, sRuta, sNomArch);
                arrErrores = leerTabulador(sRuta, sNomArch);
                if (arrErrores== null || arrErrores.isEmpty()){
                    sTxtMsj = event.getFile().getFileName() + " almacenado.";
                    System.out.println(sTxtMsj);
                }
                else{
                    sTxtMsj = "Archivo almacenado parcialmente. \n";
                    System.out.println(sTxtMsj);
                    for (int i = 0; i< arrErrores.size(); i++){
                        sTxtMsj = sTxtMsj + "\n" + arrErrores.get(i);
                    }
                }
                msg = new FacesMessage("Resultado", sTxtMsj );  
                System.out.println(sTxtMsj);
                FacesContext.getCurrentInstance().addMessage(null, msg);
            } catch (Exception e) {
                e.printStackTrace();
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error",event.getFile().getFileName() + " no almacenado."));
                System.out.println("Error no almacenado"+event.getFile().getFileName());
            }finally {
                if (out != null) {
                    out.close();
                }
                if (filecontent != null) {
                    filecontent.close();
                }
            }
        }else{
            facesContext.addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Error", "Falta indicar la compañía de crédito"));
        }
    }
    
    private void uploadFile(FileUploadEvent event, String sRuta, 
            String sNomArch)throws IOException{
    OutputStream out = null;
    InputStream filecontent = null;
    byte[] bytes = new byte[1024];
    int nLeidos = 0;
        try {
            out = new FileOutputStream(new File(sRuta + File.separator
                    + sNomArch));
            filecontent =event.getFile().getInputstream();
            while ((nLeidos = filecontent.read(bytes)) != -1) {
                out.write(bytes, 0, nLeidos);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (out != null) {
                out.close();
            }
            if (filecontent != null) {
                filecontent.close();
            }
        }
    }
    
    private ArrayList<String> leerTabulador(String sRuta, 
            String sNomArch) throws IOException{
    File fLectura;
    FileInputStream fis;
    XSSFWorkbook oWorkbook;
    XSSFSheet oHoja;
    XSSFRow oLin;
    Iterator<Row> iLineas;
    XSSFCell oCelda;
    Iterator<Cell> iCeldas;
    ArrayList<String> arrLinErr= new ArrayList<String>();
    TabuladorPrecios oTabula;
    String sRes="";
        fLectura = new File(sRuta + File.separator
                + sNomArch);
        fis = new FileInputStream(fLectura);
        //Abrir libro de excel
        oWorkbook = new XSSFWorkbook(fis);
        //Posicionarse en la primera hoja
        oHoja = oWorkbook.getSheetAt(0);
        //Obtener lista de líneas
        iLineas = oHoja.rowIterator();            
        //Recorrer cada línea
        while (iLineas.hasNext()){
            oLin = (XSSFRow)iLineas.next();
            if (oLin.getRowNum()>0){ //La línea cero es título-- y la uno es cabeceras
                //Obtener lista de celdas
                iCeldas = oLin.cellIterator();
                oTabula = new TabuladorPrecios();
                oTabula.getCiaCred().setIdEmp(nIdEmpSelec);
                /*Dentro de try-catch para obtener lista de errores*/
                try{
                    //Recorrer cada celda, se espera 0-Línea Ingreso (cadena),
                    //1-IdInterno (entero), 2-Concepto (cadena), 3-Monto (flotante)
                    while (iCeldas.hasNext()){
                        oCelda=(XSSFCell)iCeldas.next();
                        if (oCelda.getColumnIndex()==1 ||
                            oCelda.getColumnIndex()==3){
                            if (oCelda.getColumnIndex()==1 &&
                                oCelda.getCellType() == Cell.CELL_TYPE_NUMERIC){
                                oTabula.getConcepIngr().setCveConcep(
                                        (new Double(oCelda.getNumericCellValue()
                                        )).intValue());
                            }
                            else if (oCelda.getColumnIndex()==3 &&
                                oCelda.getCellType() == Cell.CELL_TYPE_NUMERIC){
                                oTabula.setMonto((new Double(
                                   oCelda.getNumericCellValue())).floatValue());
                            }
                            else{
                                arrLinErr.add("Error en línea: "+(oLin.getRowNum()+1)+
                                        " , columna: "+ (oCelda.getColumnIndex()+1));
                            }
                        }
                    }
                    sRes = oTabula.actualizar();
                    if (!oMenDB.isValid(sRes))
                        arrLinErr.add("\nError al almacenar la línea : "+
                                oLin.getRowNum());
                }catch(Exception e){
                    e.printStackTrace();
                    arrLinErr.add("Error en línea: "+oLin.getRowNum());
                }
            }
        }
        return arrLinErr;
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
}
