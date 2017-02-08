/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apli.jbs.facturacion;
import java.io.Serializable;
import org.apli.modelbeans.Estado;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
/**
 * Autor: Isabel Espinoza Espinoza
 * Fecha: Abril 2014
 */
@ManagedBean(name="oEstado")
@RequestScoped
public class EstadoJB implements Serializable{
    public Estado oEstado=new Estado();
    private List<Estado> listaEstados;
     public static List<org.apli.modelbeans.Estado> listEstadosDF;
     public static List<org.apli.modelbeans.Estado> listEstadosLE;
     
     public EstadoJB() {
        listaEstados=new ArrayList();
        llenaLista();
    }
     
     private void llenaLista(){
        try {
            listaEstados.addAll(oEstado.getEstados());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
     
    public List<Estado> getListaEstados(){
        return listaEstados;
    }
    
    public Estado getEstado() {
        return oEstado;
    }

    public void setEstado(Estado oEdo) {
        this.oEstado = oEdo;
    }
}
