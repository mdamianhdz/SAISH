package org.apli.jbs.facturacion;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import javax.faces.application.FacesMessage;
import org.apli.manejoArchivos.NombradoArchivosCarpetas;
import org.apli.modelbeans.facturacion.cfdi.CFDv32;
import org.apli.modelbeans.facturacion.cfdi.v32.schema.Comprobante;
import org.apli.modelbeans.facturacion.security.KeyLoaderEnumeration;
import org.apli.modelbeans.facturacion.security.factory.KeyLoaderFactory;
import org.apli.generacionCodQr.QrCode;
import org.apli.generacionFormatoImp.GeneradorFormatoImpreso;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apli.manejoArchivos.ManejoArchivos;
import org.apli.timbradoCFDI.Timbrado;

/**
 * @author Isabel Espinoza Espinoza
 */
@ManagedBean(name = "oCfdi")
@ViewScoped
public class CFDi implements Serializable {

    public String cadenaOriginal;
    private QrCode codigoQr;
    private String archivoXML;
    private PrivateKey key = null;
    private X509Certificate cert = null;
    private CFDv32 cfdi;
    private Comprobante sellado;
    private NombradoArchivosCarpetas oNombradoArchCarp = new NombradoArchivosCarpetas();
    private Timbrado timbrado;
    private FacesMessage msj = new FacesMessage();

    public CFDi(CFDv32 cfdi, QrCode q, Timbrado timbrado) throws FileNotFoundException, Exception {
        this.cfdi = cfdi;
        this.codigoQr = q;
        this.timbrado = timbrado;
    }

    public CFDi() {
    }

    public String getArchivoPNG() {
        return oNombradoArchCarp.getFilePng();
    }

    public String getCadenaOriginal() {
        return cadenaOriginal;
    }

    public boolean facturar(String serie, String folio,String passwd, String archivoKey, String archivoCert, String rfc) throws FileNotFoundException, Exception {
        boolean bandera = true;
        oNombradoArchCarp.setRfc(rfc);
        try {
            this.key = KeyLoaderFactory.createInstance(KeyLoaderEnumeration.PRIVATE_KEY_LOADER, archivoKey, passwd).getKey();
        } catch (org.apli.modelbeans.facturacion.exceptions.KeyException e) {
            e.printStackTrace();
            //bandera = false;
        }
        if (bandera) {
            this.cert = KeyLoaderFactory.createInstance(KeyLoaderEnumeration.PUBLIC_KEY_LOADER, new FileInputStream(archivoCert)).getKey();
            sellado = cfdi.sellarComprobante(key, cert);
            archivoXML = oNombradoArchCarp.getNombreCarpetaXML() + "/" + oNombradoArchCarp.getFileXML(serie+folio);
            codigoQr.generarCodigoQR(sellado.getSello(), oNombradoArchCarp.getFilePngCompleta(), 200, 200);
            cfdi.validar();
            cfdi.verificar();
            cfdi.guardar(new FileOutputStream(archivoXML));
        }
        return bandera;
    }

    public void generarCadenaOriginal(GeneradorFormatoImpreso genFormatoImp, String ruta) throws Exception {
        String archivoXSLCadenaOriginal = "/xslt/cadenaoriginal_3_2.xslt";
        genFormatoImp.generarCadenaOriginal(archivoXSLCadenaOriginal, ruta);
        BufferedReader cadenaLectura = null;
        File archivoBitacoraLectura = null;
        cadenaLectura = ManejoArchivos.abrirArchivo(archivoBitacoraLectura, cadenaLectura, ruta);
        cadenaOriginal = "";
        while (cadenaLectura.ready()) {
            cadenaOriginal = cadenaOriginal + cadenaLectura.readLine();
        }
    }

    public void timbrar() throws IOException {
        boolean valida = timbrado.solicitarTimbreFiscal();
        if (valida) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("", "Se ha timbrado el CFDI"));
            context.getExternalContext().getFlash().setKeepMessages(true);
        } else {
            msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Por el momento no fue posible timbrar el CFDI", "");
            FacesContext.getCurrentInstance().addMessage(null, msj);
        }
    }
}