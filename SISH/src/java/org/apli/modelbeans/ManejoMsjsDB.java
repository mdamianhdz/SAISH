package org.apli.modelbeans;

import java.io.Serializable;

/**
 * Clase para constantes de mensajes de ABC devueltos por BD
 * @author Daniel
 */
public class ManejoMsjsDB implements Serializable  {
    public String manejoMensajes(String mess) throws Exception{
        String smensaje="";
	switch(mess.charAt(0)){
		case '1':
			smensaje="Se ha insertado el registro exitosamente.";
			break;
		case '2':
			smensaje="Se ha modificado el registro exitosamente.";
			break;
                case '3':
			smensaje="Se ha eliminado el registro exitosamente.";
			break;
                case '4':
			smensaje="Ya existe un registro con la descripción ingresada.";
			break;
                case '5':
			smensaje="Ya existe un registro con la clave ingresada.";
			break;
                case '6':
			smensaje="No existe un registro con la clave solicitada.";
			break;
                case '7':
			smensaje="La información relacionada no existe.";
			break;
		default:
			smensaje="Error en la base de datos.";
			break;
	}
        return smensaje;
    }
    
    public boolean isValid(String smess){
        boolean bFlag=false;
        try{
            if((Integer.parseInt(smess))<4&&(Integer.parseInt(smess))>0)
                bFlag=true;
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return bFlag;
    }
}
