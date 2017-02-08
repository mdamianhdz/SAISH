/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apli.generacionFormatoImp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;
import javax.xml.bind.*;
import javax.xml.bind.util.JAXBSource;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;
import org.apli.modelbeans.facturacion.cfdi.v32.schema.Comprobante;
import org.apli.modelbeans.facturacion.common.URIResolverImpl;
import org.xhtmlrenderer.pdf.ITextRenderer;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import java.io.Serializable;
/**
 * @author Isabel Espinoza Espinoza
 * @version 1.0
 */
//genera el html a partir de clases, y el html lo genera en pdf
public class GeneradorFormatoImpreso implements Serializable{
    private final Comprobante document;
    private final static Joiner JOINER = Joiner.on(':');
    private final JAXBContext context;	  
    public GeneradorFormatoImpreso(Comprobante in,String... contexts) throws Exception {
	   this.context = getContext(contexts);
	   this.document = in;
    }
    private static JAXBContext getContext(String[] contexts) throws Exception {
	   List<String> ctx = Lists.asList("org.apli.modelbeans.facturacion.cfdi.v32.schema", contexts);
	   return JAXBContext.newInstance(JOINER.join(ctx));
    }  
    public void generarHTML(String archivoXSL,String archivoHTML)throws Exception{
	    JAXBSource in = new JAXBSource(context, document); 
	    TransformerFactory factory;   
	    factory = TransformerFactory.newInstance();
	    factory.setURIResolver(new URIResolverImpl());
	    Transformer transformer = factory.newTransformer(new StreamSource(getClass().getResourceAsStream(archivoXSL)));
	    StreamResult result = new StreamResult(archivoHTML);
	    transformer.transform(in, result);
            
    }
    public void generarCadenaOriginal(String archivoXSL,String archivoTxt)throws Exception{
        System.out.println("archivo txt "+archivoTxt);
        System.out.println("archivo xsl "+archivoXSL);
	    JAXBSource in = new JAXBSource(context, document); 
	    TransformerFactory factory;   
	    factory = TransformerFactory.newInstance();
	    factory.setURIResolver(new URIResolverImpl());
	    Transformer transformer = factory.newTransformer(new StreamSource(getClass().getResourceAsStream(archivoXSL)));
	    StreamResult result = new StreamResult(archivoTxt);
	    transformer.transform(in, result);
    }
    public void generarPDF(String archivoHTML,String archivoPDF){
    	try{
	        String url = new File(archivoHTML).toURI().toURL().toString();
	        OutputStream os = new FileOutputStream(archivoPDF);
	        ITextRenderer renderer = new ITextRenderer();
	        renderer.setDocument(url);
	        renderer.layout();
	        renderer.createPDF(os);
	        os.close();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }    
}