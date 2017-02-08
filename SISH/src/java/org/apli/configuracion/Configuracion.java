package org.apli.configuracion;
import java.io.Serializable;
/**
 * @author Isabel Espinoza Espinoza
 */
public class Configuracion implements Serializable{
    public static String    version="3.2",
    //Variables para datos del envio de correo
                            correoHost="mail.smtp.host",
                            correoServidor="smtp.gmail.com",
                            correoHabilitado="mail.smtp.starttls.enable",
                            correoPuertoTipo="mail.smtp.port",
                            correoPuerto="587",
                            correoUsuario="mail.smtp.user",
                            correoAutenticacion="mail.smtp.auth",
                            correoIsHabilitado="true",
                            correoIsAutenticacion="true",
                            correoRemitente= "facturacion.sanatoriohuerta@gmail.com",
                            correoPassword="facturacion_1420",
    //---------------------------------Valores del timbrado----------------------------------------------
                            usuarioTimbrado="DEMO930116L51",
                            passwordTimbrado="EWAH6G1Dy=";
}
