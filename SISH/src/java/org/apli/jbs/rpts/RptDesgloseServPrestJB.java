package org.apli.jbs.rpts;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apli.modelbeans.reportes.RptDesgloseServiciosPrestados;

/**
 * Bean administrado para el control del reporte de Desglose de Servicios
 * Prestado; pide dos fechas, muestra tabla
 * @author BAOZ
 */
@ManagedBean(name = "oRptDesServPrestJB")
@ViewScoped
public class RptDesgloseServPrestJB implements Serializable{
private Date dIni;
private Date dFin;
private List<RptDesgloseServiciosPrestados> oLista;
    public RptDesgloseServPrestJB() {
    }
    public void validaFecha(){
        String mess="";
        if (dIni==null)
            mess="No ha especificado la fecha de inicio";
        else
            if (dFin!=null)
                if (dIni.compareTo(dFin)>0)
                    mess="La fecha final del periodo debe ser posterior a la fecha de inicio";
        if (!mess.equals("")){
            FacesContext context= FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Desglose de Servicios Prestados",mess));
        }
    }
    
    public void buscar(){
    RptDesgloseServiciosPrestados oRpt = new RptDesgloseServiciosPrestados();
    String mess="";
        oRpt.setIni(dIni);
        oRpt.setFin(dFin);
        try{
            oLista = oRpt.buscarRpt();
        }catch(Exception e){
            e.printStackTrace();
            FacesContext context= FacesContext.getCurrentInstance();
            mess = "Error al buscar el reporte";
            context.addMessage(null, new FacesMessage("Desglose de Servicios Prestados",mess));
        }
    }
    
    public List<RptDesgloseServiciosPrestados> getLista(){
        return oLista;
    }
    
    public String getRangoFechas(){
    String sRet = "Desglose de Servicios Otorgados <br/>";
    SimpleDateFormat fmtTxt = new SimpleDateFormat("dd/MM/yyyy");
        if (dIni != null)
            sRet = sRet + " del "+ fmtTxt.format(dIni);
        if (dFin != null)
            sRet = sRet + " al " + fmtTxt.format(dFin);
        return sRet;
    }
    
    public Date getIni() {
        return dIni;
    }

    public void setIni(Date dIni) {
        this.dIni = dIni;
    }
    
    public Date getFin() {
        return dFin;
    }
    
    public void setFin(Date dFin) {
        this.dFin = dFin;
    }
}
