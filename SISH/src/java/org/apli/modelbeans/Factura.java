/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apli.modelbeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Lily_LnBnd
 */
public class Factura implements Serializable {
    
    private int nNumFolio;
    private int nSerie;
    private String sEstado;
    private String sExpidioFact;
    private Date dFecExpedicion;
    private Paciente oPaciente;
    private double dImporte;

    public int getNumFolio() {
        return nNumFolio;
    }

    public void setNumFolio(int nNumFolio) {
        this.nNumFolio = nNumFolio;
    }

    public int getSerie() {
        return nSerie;
    }

    public void setSerie(int nSerie) {
        this.nSerie = nSerie;
    }

    public String getEstado() {
        return sEstado;
    }

    public void setEstado(String sEstado) {
        this.sEstado = sEstado;
    }

    public String getExpidioFact() {
        return sExpidioFact;
    }

    public void setExpidioFact(String sExpidioFact) {
        this.sExpidioFact = sExpidioFact;
    }

    public Date getFecExpedicion() {
        return dFecExpedicion;
    }

    public void setFecExpedicion(Date dFecExpedicion) {
        this.dFecExpedicion = dFecExpedicion;
    }

    public Paciente getPaciente() {
        return oPaciente;
    }

    public void setPaciente(Paciente oPaciente) {
        this.oPaciente = oPaciente;
    }

    public double getImporte() {
        return dImporte;
    }

    public void setImporte(double dImporte) {
        this.dImporte = dImporte;
    }
    
    public List<Factura> buscaPendientesCobro(){
        List<Factura> listFact=new ArrayList();
        Factura oF1=new Factura();
        oF1.setEstado("Pendiente de Pago");
        oF1.setExpidioFact("Isabel Espinpoza");
        oF1.setFecExpedicion(new Date(2014,1,10));
        oF1.setImporte(3450.0);
        oF1.setNumFolio(123);
        oF1.setPaciente(new Paciente());
        oF1.getPaciente().setNombre("Alejandro");
        oF1.getPaciente().setApellidoPaterno("Hernández");
        oF1.setSerie(321);
        listFact.add(oF1);
        Factura oF2=new Factura();
        oF2.setEstado("Pendiente de Pago");
        oF2.setExpidioFact("Isabel Espinpoza");
        oF2.setFecExpedicion(new Date(2014,1,12));
        oF2.setImporte(1249.5);
        oF2.setNumFolio(456);
        oF2.setPaciente(new Paciente());
        oF2.getPaciente().setNombre("Eric");
        oF2.getPaciente().setApellidoPaterno("Aguilar");
        oF2.setSerie(654);
        listFact.add(oF2);
        Factura oF3=new Factura();
        oF3.setEstado("Pendiente de Pago");
        oF3.setExpidioFact("Humberto Marín");
        oF3.setFecExpedicion(new Date(2014,1,16));
        oF3.setImporte(10954.0);
        oF3.setNumFolio(789);
        oF3.setPaciente(new Paciente());
        oF3.getPaciente().setNombre("Fermín");
        oF3.getPaciente().setApellidoPaterno("Díaz");
        oF3.setSerie(987);
        listFact.add(oF3);
        Factura oF4=new Factura();
        oF4.setEstado("Pendiente de Pago");
        oF4.setExpidioFact("Isabel Espinpoza");
        oF4.setFecExpedicion(new Date(2014,1,23));
        oF4.setImporte(1456.2);
        oF4.setNumFolio(147);
        oF4.setPaciente(new Paciente());
        oF4.getPaciente().setNombre("Rosa");
        oF4.getPaciente().setApellidoPaterno("Durán");
        oF4.setSerie(741);
        listFact.add(oF4);
        Factura oF5=new Factura();
        oF5.setEstado("Pendiente de Pago");
        oF5.setExpidioFact("Humberto Marín");
        oF5.setFecExpedicion(new Date(2014,2,11));
        oF5.setImporte(654.30);
        oF5.setNumFolio(258);
        oF5.setPaciente(new Paciente());
        oF5.getPaciente().setNombre("Angelica");
        oF5.getPaciente().setApellidoPaterno("Velázquez");
        oF5.setSerie(852);
        listFact.add(oF5);
        System.out.println("listFact="+listFact.size());
        return listFact;
    }
    
    
    
}
