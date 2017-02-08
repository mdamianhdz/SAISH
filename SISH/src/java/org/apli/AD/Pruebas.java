package org.apli.AD;

import java.io.BufferedReader;
import java.io.File;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.apli.generacionFormatoImp.GeneradorFormatoImpreso;

import org.apli.manejoArchivos.ManejoArchivos;
import org.apli.modelbeans.facturacion.cfdi.v32.schema.Comprobante;
import org.apli.modelbeans.facturacion.cfdi.v32.schema.TUbicacion;
import org.apli.modelbeans.facturacion.cfdi.v32.schema.TUbicacionFiscal;

@ViewScoped
@ManagedBean(name = "manager")
public class Pruebas {
    public void generarCadenaOriginal(GeneradorFormatoImpreso genFormatoImp) throws Exception {
        String archivoTxt = "C:/Users/Isa/Desktop/SANATORIO HUERTA/SISH 3.8/SISH/web/BovedaFiscal/Auxiliares/cadenaOriginal.txt";
        //String archivoTxt = "/xslt/formatoImpreso/cadenaOriginal.txt";
        String archivoXSLCadenaOriginal = "/xslt/cadenaoriginal_3_2.xslt";
        genFormatoImp.generarCadenaOriginal(archivoXSLCadenaOriginal, archivoTxt);
        BufferedReader cadenaLectura = null;
        File archivoBitacoraLectura = null;
        cadenaLectura = ManejoArchivos.abrirArchivo(archivoBitacoraLectura, cadenaLectura, archivoTxt);
        String cadenaOriginal = "";
        while (cadenaLectura.ready())
            cadenaOriginal = cadenaOriginal + cadenaLectura.readLine();
        System.out.println("Cadena original " + cadenaOriginal);
    }
    public static void main(String arg[]) throws Exception {
        Comprobante comp = new Comprobante();
        Comprobante.Emisor emisor = new Comprobante.Emisor();
        emisor.setNombre("Ismael ");
        emisor.setRfc("QWER12345tyd");
        TUbicacionFiscal tub = new TUbicacionFiscal();
        tub.setPais("México");
        emisor.setDomicilioFiscal(tub);
        TUbicacion tu = new TUbicacion();
        tu.setPais("Mexico");
        Comprobante.Receptor rec = new Comprobante.Receptor();
        rec.setNombre("ismaelin");
        rec.setRfc("EIEI832415WER");
        rec.setDomicilio(tu);
        comp.setEmisor(emisor);
        comp.setReceptor(rec);
        
        GeneradorFormatoImpreso ge = new GeneradorFormatoImpreso(comp, "org.apli.modelbeans.facturacion.cfdi");
        Pruebas pruebas=new Pruebas();
        pruebas.generarCadenaOriginal(ge);
        
    }
}