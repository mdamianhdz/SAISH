/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apli.jbs.facturacion;

import java.io.Serializable;

/**
 *
 * @author Isa
 */
public class ConvertidorNumeroLetra  implements Serializable{
    public String convertirNumeroALetras(String num, String tipoMoneda){
        String resultado;
        double entero, nro; 
        String dec="";
        nro=Double.parseDouble(num); //Con decimales
        entero=Math.floor(Double.parseDouble(num));  //sin decimales
        Integer decimal=(int)Math.round(Math.floor((nro-entero)*100));  //parte decimal
        if (decimal >= 0){
            if (decimal >= 10){
                dec = " " + tipoMoneda + " " + decimal.toString() + "/100 M.N.)";
            }
            else{
                dec = " " + tipoMoneda + " 0" + decimal.toString() + "/100 M.N.)";
            }
        }
        resultado = convertirNumeroALetras(entero)+dec;
        return "(" + resultado;
    }
    public  String convertirNumeroALetras(double value){
        String Num2Text = "";
        value=Math.floor(value);
        if (value == 0) Num2Text = "CERO";
        else if (value == 1.0) Num2Text = "UNO";
        else if (value == 2.0) Num2Text = "DOS";
        else if (value == 3.0) Num2Text = "TRES";
        else if (value == 4.0) Num2Text = "CUATRO";
        else if (value == 5.0) Num2Text = "CINCO";
        else if (value == 6.0) Num2Text = "SEIS";
        else if (value == 7.0) Num2Text = "SIETE";
        else if (value == 8.0) Num2Text = "OCHO";
        else if (value == 9.0) Num2Text = "NUEVE";
        else if (value == 10.0) Num2Text = "DIEZ";
        else if (value == 11.0) Num2Text = "ONCE";
        else if (value == 12.0) Num2Text = "DOCE";
        else if (value == 13.0) Num2Text = "TRECE";
        else if (value == 14.0) Num2Text = "CATORCE";
        else if (value == 15.0) Num2Text = "QUINCE";
        else if (value < 20.0) Num2Text = "DIECI" + convertirNumeroALetras(value - 10);
        else if (value == 20.0) Num2Text = "VEINTE";
        else if (value < 30.0) Num2Text = "VEINTI" + convertirNumeroALetras(value - 20);
        else if (value == 30.0) Num2Text = "TREINTA";
        else if (value == 40.0) Num2Text = "CUARENTA";
        else if (value == 50.0) Num2Text = "CINCUENTA";
        else if (value == 60.0) Num2Text = "SESENTA";
        else if (value == 70.0) Num2Text = "SETENTA";
        else if (value == 80.0) Num2Text = "OCHENTA";
        else if (value == 90.0) Num2Text = "NOVENTA";
        else if (value < 100.0) Num2Text = convertirNumeroALetras(Math.floor(value / 10) * 10) + " Y " + convertirNumeroALetras(value % 10);
        else if (value == 100.0) Num2Text = "CIEN";
        else if (value < 200.0) Num2Text = "CIENTO " + convertirNumeroALetras(value - 100);
        else if ((value == 200.0) || (value == 300.0) || (value == 400.0) || (value == 600.0) || (value == 800.0)){ Num2Text = convertirNumeroALetras(Math.floor(value / 100)) + "CIENTOS";}
        else if (value == 500.0) Num2Text = "QUINIENTOS";
        else if (value == 700.0) Num2Text = "SETECIENTOS";
        else if (value == 900.0) Num2Text = "NOVECIENTOS";
        else if (value < 1000.0) {
        	Num2Text = convertirNumeroALetras(Math.floor(value / 100) * 100) + " " + convertirNumeroALetras(value % 100);
        }
        else if (value == 1000.0) Num2Text = "MIL";
        else if (value < 2000.0) Num2Text = "MIL " + convertirNumeroALetras(value % 1000);
        else if (value < 1000000.0){
            Num2Text = convertirNumeroALetras(Math.floor(value / 1000)) + " MIL";
            if ((value % 1000) > 0){ Num2Text = Num2Text + " " + convertirNumeroALetras(value % 1000);}
        }
        else if (value == 1000000.0) Num2Text = "UN MILLON";
        else if (value < 2000000.0) Num2Text = "UN MILLON " + convertirNumeroALetras(value % 1000000);
        else if (value < 1000000000000.00){
            Num2Text = convertirNumeroALetras(Math.floor(value / 1000000)) + " MILLONES ";
            if ((value - Math.floor(value / 1000000) * 1000000) > 0) Num2Text = Num2Text + " " + convertirNumeroALetras(value - Math.floor(value / 1000000) * 1000000);
        }
        else if (value == 1000000000000.00) Num2Text = "UN BILLON";
        else if (value < 2000000000000.00) Num2Text = "UN BILLON " + convertirNumeroALetras(value - Math.floor(value / 1000000000000.00) * 1000000000000.00);
        else{
            Num2Text = convertirNumeroALetras(Math.floor(value / 1000000000000.00)) + " BILLONES";
            if ((value - Math.floor(value / 1000000000000.00) * 1000000000000.00) > 0) Num2Text = Num2Text + " " + convertirNumeroALetras(value - Math.floor(value / 1000000000000.00) * 1000000000000.00);
        }
        return Num2Text;
    }
}
