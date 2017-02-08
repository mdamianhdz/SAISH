/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apli.jbs.caja;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apli.modelbeans.caja.CorteCaja;

/**
 *
 * @author Lily_LnBnd
 */
@ManagedBean(name="oConsCC")
@ViewScoped
public class ConsultarCorteCajaJB implements Serializable{
    private Date dInicio;
    private Date dFin;
    private List<CorteCaja> listaCortes;
    private CorteCaja selectedCorte;
    private float nHosp;
    private float nExt;
    private float nTotalE;
    
    public ConsultarCorteCajaJB(){
        
    }
    
    public void validaFecha(){
        String mess="";
        if (dInicio==null)
            mess="No ha especificado la fecha de inicio";
        else
            if (dFin!=null)
                if (dInicio.compareTo(dFin)>0)
                    mess="La fecha final del periodo debe ser posterior a la fecha de inicio";
        if (!"".equals(mess)){
            FacesContext context= FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Consultar Corte de Caja",mess));
            context.getExternalContext().getFlash().setKeepMessages(true);
        }
    }
    
    public void buscar()throws Exception{
        String sCondicion="", mess="";
        if (dInicio!=null && dFin!=null)
                sCondicion="pdregistro between '"+dInicio+"' and '"+dFin+"'";
            listaCortes=new CorteCaja().buscaCortesCaja(sCondicion);
    }
    
    public float calculaSaldo(int nFolioOp, float nMonto){
       float nRet=0.0f;
       if (nFolioOp==0)
           return selectedCorte.getListOperaciones().get(0).getMonto();
       for (int i = 0; i < selectedCorte.getListOperaciones().size(); i++) {
           if (i==0){
               nRet=selectedCorte.getListOperaciones().get(0).getMonto();
           }else{
               if (selectedCorte.getListOperaciones().get(i).getOpeCC().getOpeCaja().getFolio()==nFolioOp && selectedCorte.getListOperaciones().get(i).getMonto()==nMonto){
                    if (selectedCorte.getListOperaciones().get(i).getTipoOperacion().equals("I"))
                        nRet=nRet+selectedCorte.getListOperaciones().get(i).getMonto();
                    else
                        nRet=nRet-selectedCorte.getListOperaciones().get(i).getMonto();
                    return nRet;
               }
               if (selectedCorte.getListOperaciones().get(i).getTipoOperacion().equals("I"))
                   nRet=nRet+selectedCorte.getListOperaciones().get(i).getMonto();
               else
                   nRet=nRet-selectedCorte.getListOperaciones().get(i).getMonto();
           }
       }
       return nRet;
    }
    
    public void calculaDetalleIngresos(){
        nHosp=0; nExt=0; nTotalE=0;
        for (int i = 1; i < selectedCorte.getListOperaciones().size()-1; i++) {
            if (selectedCorte.getListOperaciones().get(i).getTipoOperacion().equals("I")){
                if(selectedCorte.getListOperaciones().get(i).getTipoIngreso().equals("H"))
                    nHosp=nHosp+selectedCorte.getListOperaciones().get(i).getMonto();
                else
                    nExt=nExt+selectedCorte.getListOperaciones().get(i).getMonto();
            }else{
                nTotalE=nTotalE+selectedCorte.getListOperaciones().get(i).getMonto();
            }
        }
    }
    
    public Date getInicio() {
        return dInicio;
    }

    public void setInicio(Date value) {
        dInicio = value;
    }

    public Date getFin() {
        return dFin;
    }

    public void setFin(Date value) {
        dFin = value;
    }

    public List<CorteCaja> getListaCortes() {
        return listaCortes;
    }

    public void setListaCortes(List<CorteCaja> value) {
        listaCortes = value;
    }

    public CorteCaja getSelectedCorte() {
        return selectedCorte;
    }

    public void setSelectedCorte(CorteCaja value) {
        selectedCorte = value;
    }
    
    public float getHosp() {
        return nHosp;
    }

    public void setHosp(float value) {
        nHosp = value;
    }

    public float getExt() {
        return nExt;
    }

    public void setExt(float value) {
        nExt = value;
    }
    
    public float getTotalE() {
        return nTotalE;
    }

    public void setTotalE(float value) {
        nTotalE = value;
    }
}
