/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apli.modelbeans;

import java.io.Serializable;
import java.util.Date;
/**
 * Representa los datos que debe llevar los formatos por forma de pago
 * (Cheque,Transferencia,Vouchers,Fichas de Deposito)
 * @author AIMEE R
 */
public abstract class FormatoFormaPago implements Serializable{
    
    protected Date dFecha;
    protected String sNombre="";
    protected float nMonto=0.0f;
    protected String sConcepto="";
    protected String sFactura="";
    protected int nFolio=0;
    protected float nTotal=0.0f;
    
    public FormatoFormaPago(){
    }
    
    //Sets y gets para cada formato
    public Date getFecha(){
        return dFecha;
    }
    public void setFecha(Date dFecha){
        this.dFecha=dFecha;
    }
    
    public String getNombre(){
        return sNombre;
    }
    public void setNombre(String sNombre){
        this.sNombre=sNombre;
    }
    
    public String getConcepto(){
        return sConcepto;
    }
    public void setConcepto(String sConcepto){
        this.sConcepto=sConcepto;
    }
    
    public float getMonto(){
        return nMonto;
    }
    public void setMonto(float nMonto){
        this.nMonto=nMonto;
    }
    
    public String getFactura(){
        return sFactura;
    }
    public void setFactura(String sFactura){
        this.sFactura=sFactura;
    }
    
    public int getFolio(){
        return nFolio;
    }
    public void setFolio(int nFolio){
        this.nFolio=nFolio;
    }
    
    public float getTotal(){
        return nTotal;
    }
    public void setTotal(float nTotal){
        this.nTotal=nTotal;
    }
}
