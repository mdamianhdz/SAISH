/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apli.jbs.catalogos;

import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import org.apli.modelbeans.Turno;

/**
 *
 * @author Daniel
 */
@ManagedBean(name="oTurno")
@RequestScoped
public class TurnoJB {
private List<Turno> listaTurnos;
    /**
     * Creates a new instance of TurnoJB
     */
    public TurnoJB() {
        listaTurnos=new ArrayList();
        llenaLista();
    }
    
    public Turno oTu=new Turno();

    private void llenaLista(){
        try {
            listaTurnos.addAll(oTu.buscaTodosTurnos());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
     public List<Turno> getListaTurnos(){
        return listaTurnos;
    }
    
}