package org.apli.modelbeans;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Clase con validacion de RFC,CURP,Fecha,Cedula profesional,UUID,SSA 
 * Autor: Jesús Vázquez Ruiz Fecha: Febrero 2014
 */
public class Valida implements Serializable {

    /**
     * Regresa verdadero si el RFC es válido
     */
    public boolean validaRFC(String sRfc) {

        boolean validacion = true;
        Pattern p = Pattern.compile("[a-zA-Z]{4}[0-9]{6}[a-zA-Z]{2}[0-9]{1}");
        Matcher matcher = p.matcher(sRfc);
        validacion = matcher.matches();

        return validacion;
    }

    /**
     * Regresa verdadero si el CURP es valido
     */
    public boolean validaCURP(String sCurp) {

        boolean validacion = true;
        Pattern p = Pattern.compile("[a-zA-Z]{4}[0-9]{6}[HhMm][a-zA-Z]{5}[0-9]{2}");
        Matcher matcher = p.matcher(sCurp);
        validacion = matcher.matches();

        return validacion;
    }

    /**
     * Regresa verdadero si la Fecha es válida
     */
    public boolean ValidaFecha(String sFecha) {
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        boolean validacion = false;
        try {
            Pattern p = Pattern.compile("[0-9]{4}[-][0-9]{2}[-][0-9]{2}");
            Matcher matcher = p.matcher(sFecha);
            validacion = matcher.matches();

            if (validacion) {
                Date fecha = new Date();
                fecha = formato.parse(sFecha);
                validacion = true;
            }

        } catch (Exception ex) {
            validacion = false;
        }
        return validacion;
    }

    /**
     * Regresa verdadero si la Cédula Profesional es válida
     */
    public boolean validaCedulaProf(String sCP) {

        boolean validacion = true;
        Pattern p = Pattern.compile("[0-9]{6}");
        Matcher matcher = p.matcher(sCP);
        validacion = matcher.matches();

        return validacion;
    }

    /**
     * Regresa verdadero si la UUID es válida
     */
    public boolean validaUUID(String sUUID) {

        boolean validacion = true;
        Pattern p = Pattern.compile("[0-9,a-fA-F]{8}[-][0-9,a-fA-F]{4}[-][0-9,a-fA-F]{4}[-][0-9,a-fA-F]{4}[-][0-9,a-fA-Z]{12}");
        Matcher matcher = p.matcher(sUUID);
        validacion = matcher.matches();

        return validacion;
    }

    /**
     * Regresa verdadero si el SSA es válido
     */
    public boolean validaSSA(String sSSA) {

        boolean validacion = true;
        Pattern p = Pattern.compile("[0-9]{5}");
        Matcher matcher = p.matcher(sSSA);
        validacion = matcher.matches();

        return validacion;
    }

    public String BooleanToBinarioString(boolean bBooleano) {
        String sBinario;
        if (bBooleano == true) {
            sBinario = "1";
        } else {
            sBinario = "0";
        }
        return sBinario;
    }

    public int BooleanToBinarioInt(boolean bBooleano) {
        int nBinario;
        if (bBooleano == true) {
            nBinario = 1;
        } else {
            nBinario = 0;
        }
        return nBinario;
    }

    public boolean BinarioStringToBoolean(String sBooleano) {
        boolean bBooleano;
        if (sBooleano.equals("1")) {
            bBooleano = true;
        } else {
            bBooleano = false;
        }
        return bBooleano;
    }

    public boolean BinarioIntToBoolean(int nBooleano) {
        boolean bBooleano;
        if (nBooleano == 1) {
            bBooleano = true;
        } else {
            bBooleano = false;
        }
        return bBooleano;
    }

    public String eliminaMSJCorchetes(String msj) {
        String msjLimpio = msj.substring(1, msj.length() - 1);
        return msjLimpio;
    }

    public String QuienPaga(int ncve) {
    String scve = "";
    TipoPrincipalPaga oTip = new TipoPrincipalPaga();
        oTip.setTipo(ncve);
        System.out.println(ncve);
        try{
            if (oTip.buscar())
                scve = oTip.getDescripcion();
            else{
                scve = "Error";
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return scve;
    }

    public String validaSituacion(String sSituacion) {
        String situacion = "";

        if (sSituacion.equalsIgnoreCase("E")) {
            situacion = "Entregado";
        } else if (sSituacion.equalsIgnoreCase("C")) {
            situacion = "Cancelado";
        }
        return situacion;
    }

    public double obtieneIva(float total, float pctIva) {
        if (pctIva == 0) {
            return 0;
        } else {
            return redondeaCifra((total / ((pctIva / 100) + 1)) * (pctIva / 100));
        }
    }

    public double obtieneSubtotal(float total, float pctIva) {
        if (pctIva == 0) {
            return redondeaCifra(total);
        } else {
            return redondeaCifra(total / ((pctIva / 100) + 1));
        }
    }

    public double redondeaCifra(float param) {
        return Math.round(param * 100.0) / 100.0;
    }
    
    public boolean stringIsNullOrEmpty(String sVal){
    boolean bRet=false;
        if (sVal == null || sVal.trim().equals(""))
            bRet = true;
        return bRet;
    }
    
    public String cadenaParaBase(String sVal){
    String sRet = "";
        if (stringIsNullOrEmpty(sVal))
            sRet = "null";
        else
            sRet = "'"+sVal+"'";
        return sRet;
    }
    
    
}
