package org.apli.manejoArchivos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.Serializable;
/**
 * @author Isabel Espinoza Espinoza
 * @version 1.0
 */
public class ManejoArchivos implements Serializable{
	//Abrir un archivo para escribir en el
	public static PrintWriter abrirArchivo(FileWriter f,PrintWriter p,String ruta){
            try{
                f = new FileWriter(ruta,true);
                p = new PrintWriter(f);        
            }
            catch (Exception ex){
            }	
            return p;
	}
	//Abrir un archivo para leer en el
	public static BufferedReader abrirArchivo(File f,BufferedReader p,String ruta){
            try{
        	f = new File(ruta);
        	p = new BufferedReader (new FileReader(f));        
            }
            catch (Exception ex){
            }	
            return p;
	}
	//Cerrar un archivo
	public static void cerrarArchivo(PrintWriter p){
            try{
                if (null != p){
                	p.close();
                }
            }catch (Exception ex1){
            }	
	}
}