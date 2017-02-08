/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apli.generacionCodQr;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import javax.imageio.ImageIO;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.QRCodeWriter;
import java.io.Serializable;
/**
 * @author Isabel Espinoza Espinoza
 * @version 1.0
 * @created 07-oct.-2013 01:40:29 p. m.
 */
public class QrCode implements Serializable{
	//Generar código de barras bidimensional (código QR)"
	public void generarCodigoQR(String cadena, String rutaArchivo, int ancho, int alto){
		QrCode qr = new QrCode();
		File f = new File(rutaArchivo);
	    try {
	    	qr.generateQR(f, cadena,ancho,alto);
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	} 
	public File generateQR(File file, String text, int h, int w) throws Exception { 
		Charset charset = Charset.forName("ISO-8859-1");
	    CharsetEncoder encoder = charset.newEncoder();
	    byte[] b = null;
	    ByteBuffer bbuf = encoder.encode(CharBuffer.wrap(text));
	    b = bbuf.array();
	    String data = new String(b, "ISO-8859-1");
	    // BitMatrix para los datos
	    BitMatrix matrix = null;
	    QRCodeWriter writer = new QRCodeWriter();
	    matrix = writer.encode(data, com.google.zxing.BarcodeFormat.QR_CODE, w, h);
	    MatrixToImageWriter.writeToFile(matrix, "PNG", file);
	    return file;
	}
	public String decodificaQR(File file) throws Exception {
		FileInputStream inputStream = new FileInputStream(file);
	    BufferedImage image = ImageIO.read(inputStream);
	    //Convierte la imagen a un mapa de bits binario
	    LuminanceSource source = new BufferedImageLuminanceSource(image);
	    BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
	    // decodifica el barcode
	    QRCodeReader reader = new QRCodeReader();
	    Result result = reader.decode(bitmap);
	    return new String(result.getText().getBytes("UTF8"));
	}
}
