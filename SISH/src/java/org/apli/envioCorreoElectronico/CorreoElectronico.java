/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apli.envioCorreoElectronico;
import java.io.Serializable;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
/**
 * @author Isabel Espinoza Espinoza
 * @version 1.0
 */
public class CorreoElectronico implements Serializable{
	private Properties props;
	private Session sesion;
	private String remitente;
	private String password;
	private BodyPart texto;
	private BodyPart adjuntoXML;
	private MimeMultipart multiParte;
	private MimeMessage message;
	private Transport transporte;
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRemitente() {
		return remitente;
	}
	public void setRemitente(String remitente) {
		this.remitente = remitente;
	}
	public void configurarCuentaCorreo(String host,String servidor,String habilitado,
					String isHabilitado,String puertoTipo, String puerto, 
					String usuario,String autenticacion,String isAutenticacion){
            props = new Properties();
            props.put(host, servidor);
            props.setProperty(habilitado,isHabilitado);
            props.setProperty(puertoTipo, puerto);
            props.setProperty(usuario, remitente);
            props.setProperty(autenticacion,isAutenticacion);
            sesion = Session.getDefaultInstance(props, null);
	}
	//Entregar CFDI: envío por correo electrónico
	public boolean enviarCFDI(String destinatario, String asunto, String mensaje,
			String archivoXML,String nombreXML){
        try{
            // Texto del mensaje
            texto = new MimeBodyPart();
            texto.setText(mensaje);       
            // Archivo adjunto
            adjuntoXML = new MimeBodyPart();
            adjuntoXML.setDataHandler(new DataHandler(new FileDataSource(archivoXML))); 
            adjuntoXML.setFileName(nombreXML); 
            multiParte = new MimeMultipart();
            multiParte.addBodyPart(texto);
            multiParte.addBodyPart(adjuntoXML);
            // Se compone el correo indicando remitente, destinatario, asunto y contenido.
            message = new MimeMessage(sesion);
            message.setFrom(new InternetAddress(remitente));
            message.addRecipient(Message.RecipientType.TO,new InternetAddress(destinatario));
            message.setSubject(asunto);
            message.setContent(multiParte);          
            // Se envia el correo.
            transporte = sesion.getTransport("smtp");
            transporte.connect(remitente, password);
            transporte.sendMessage(message, message.getAllRecipients());
            transporte.close();
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}



