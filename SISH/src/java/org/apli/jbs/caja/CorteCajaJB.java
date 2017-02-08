package org.apli.jbs.caja;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apli.modelbeans.caja.CorteCaja;
import org.apli.modelbeans.caja.DatosCorteCaja;

/**
 *
 * @author Lily_LnBnd
 */
@ManagedBean(name="oCorteCaja")
@ViewScoped
public class CorteCajaJB implements Serializable{
    private List<DatosCorteCaja> listOperaciones;
    private float nHosp;
    private float nExt;
    private float nTotalE;
    private float nNoHosp;
    private float nGtosPend;
    
    public CorteCajaJB(){
        listOperaciones=new ArrayList();
    }
    
    public void buscarOperaciones()throws Exception{
        listOperaciones=new DatosCorteCaja().buscaDatosCorteCaja();
        calculaDetalleIngresos();
    }
    
    public float calculaSaldo(int nFolioOp, float nMonto){
       float nRet=0.0f;
       if (nFolioOp==0)
           return listOperaciones.get(0).getMonto();
       for (int i = 0; i < listOperaciones.size(); i++) {
           if (i==0){
               nRet=listOperaciones.get(0).getMonto();
           }else{
               if (listOperaciones.get(i).getOpeCC().getOpeCaja().getFolio()==nFolioOp && listOperaciones.get(i).getMonto()==nMonto){
                    if (listOperaciones.get(i).getTipoOperacion().equals("I"))
                        nRet=nRet+listOperaciones.get(i).getMonto();
                    else
                        nRet=nRet-listOperaciones.get(i).getMonto();
                    return nRet;
               }
               if (listOperaciones.get(i).getTipoOperacion().equals("I"))
                   nRet=nRet+listOperaciones.get(i).getMonto();
               else
                   nRet=nRet-listOperaciones.get(i).getMonto();
           }
       }
       return nRet;
    }
     
    public void calculaDetalleIngresos(){
        nHosp=0; nExt=0; nTotalE=0; nNoHosp=0;
        for (int i = 1; i < listOperaciones.size()-1; i++) {
            if (listOperaciones.get(i).getTipoOperacion().equals("I")){
                if(listOperaciones.get(i).getTipoIngreso().equals("H"))
                    nHosp=nHosp+listOperaciones.get(i).getMonto();
                else if(listOperaciones.get(i).getTipoIngreso().equals("E"))
                    nExt=nExt+listOperaciones.get(i).getMonto();
                else
                    nNoHosp = nNoHosp+listOperaciones.get(i).getMonto();
            }else{
                nTotalE=nTotalE+listOperaciones.get(i).getMonto();
            }
        }
        System.out.println(nHosp+" "+nExt+" "+nNoHosp);
    }
    
    public void guardarCorte()throws Exception{
        buscarOperaciones();
        String mess=new CorteCaja().guardaCorteCaja(listOperaciones, getCurrentDate());
        FacesContext context= FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Corte de Caja",mess));
        context.getExternalContext().getFlash().setKeepMessages(true);
    }

    public List<DatosCorteCaja> getListOperaciones() {
        return listOperaciones;
    }

    public void setListOperaciones(List<DatosCorteCaja> val) {
        listOperaciones = val;
    } 

    public Date getCurrentDate() {
        return new Date();
    }

    public float getHosp() {
        return nHosp;
    }

    public void setHosp(float val) {
        nHosp = val;
    }

    public float getExt() {
        return nExt;
    }

    public void setExt(float val) {
        nExt = val;
    }
    
    public float getTotalE() {
        return nTotalE;
    }

    public void setTotalE(float val) {
        nTotalE = val;
    }

    /**
     * @return the nNoHosp
     */
    public float getNoHosp() {
        return nNoHosp;
    }

    /**
     * @param nNoHosp the nNoHosp to set
     */
    public void setNoHosp(float nNoHosp) {
        this.nNoHosp = nNoHosp;
    }

    /**
     * @return the nGtosPend
     */
    public float getGtosPend() {
        return nGtosPend;
    }

    /**
     * @param nGtosPend the nGtosPend to set
     */
    public void setGtosPend(float nGtosPend) {
        this.nGtosPend = nGtosPend;
    }
}
