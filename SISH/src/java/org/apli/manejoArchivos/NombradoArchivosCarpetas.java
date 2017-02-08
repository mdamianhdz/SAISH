package org.apli.manejoArchivos;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.File;
import java.io.IOException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
/**
 * @author Isabel Espinoza Espinoza
 * @version 1.0
 */
@ManagedBean(name="oNombradoArchivos")
@SessionScoped
public class NombradoArchivosCarpetas implements Serializable{
    private Date oFecha = new Date();
    private SimpleDateFormat oSdfArchivo = new SimpleDateFormat("yyyyMMdd");
    private SimpleDateFormat oSdfCarpeta = new SimpleDateFormat("yyyyMM");
    private String sFile;
    private String sRfc;
    private ExternalContext extCont = FacesContext.getCurrentInstance().getExternalContext();
    private File folder;
    private String sFilePng;
    
    public String getFilePng(){
        return "/BovedaFiscal/Auxiliares/codigoQR.png";
    }
    public String getFilePngCompleta() throws IOException {
        folder = new File(extCont.getRealPath("//BovedaFiscal//Auxiliares"));
        sFilePng = folder.getCanonicalPath() + "/codigoQR.png";
        String cadenaA = "", cadenaB = "";
        for (int i = 0; i < sFilePng.length(); i++) 
            if (sFilePng.charAt(i) == 'b') 
                if (sFilePng.charAt(i + 1) == 'u') 
                    if (sFilePng.charAt(i + 2) == 'i') 
                        if (sFilePng.charAt(i + 3) == 'l') 
                            if (sFilePng.charAt(i + 4) == 'd') {
                                cadenaA = sFilePng.substring(0, i - 1);
                                cadenaB = sFilePng.substring(i + 5, sFilePng.length());
                            }
        this.sFilePng = cadenaA + cadenaB;
        return sFilePng;
    }

    public void setFilePng(String sFilePng) {
        this.sFilePng = sFilePng;
    }

    //Devuelve el RFC del que se esta trabajando
    public String getRfc() {
        return sRfc;
    }
    //Cambia el rfc del que se esta trabajando

    public void setRfc(String rfc) {
        this.sRfc = rfc;
    }
    //Obtiene el nombre de un archivo para cuando este sea xml, si no existe lo crea

    public String getFileXML(String complemento) {
        sFile = oSdfArchivo.format(oFecha) + "_" + sRfc +"_"+complemento+ ".xml";
        return sFile;
    }
    //Obtiene el nombre de una carpeta para cuando esta sea xml, si no existe la crea

    public String getNombreCarpetaXML() throws IOException {
        folder = new File(extCont.getRealPath("//BovedaFiscal//Propias//xml//" + oSdfCarpeta.format(oFecha) + "//"));
        folder.mkdirs();
        if (folder.isDirectory()) {
            return folder.getCanonicalPath();
        } else {
            return "No se creo la carpeta";
        }
    }

    public String getNombreCarpetaXMLCancelados() throws IOException {
        folder = new File(extCont.getRealPath("//BovedaFiscal//Propias//acusesCancelacion//" + oSdfCarpeta.format(oFecha) + "//"));
        folder.mkdirs();
        if (folder.isDirectory()) {
            return folder.getCanonicalPath();
        } else {
            return "No se creo la carpeta";
        }
    }
}