package org.apli.jbs.rpts;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apli.modelbeans.reportes.RptResultadosMes;

/**
 * Bean administrado para el control del reporte de resultados del periodo
 * @author BAOZ
 */
@ManagedBean(name = "oRptResMesJB")
@ViewScoped
public class RptResMesJB implements Serializable{
private Date dIni;
private Date dFin;
private RptResultadosMes oRptRes;
private float nSumaIngresos;
private float nSumaVentas;
    
    public RptResMesJB() {
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
    String mess="";
    FacesContext context= FacesContext.getCurrentInstance();
    List<RptResultadosMes> ret;
        oRptRes = new RptResultadosMes();
        
        if (dIni == null || dFin == null){
            mess = "Falta indicar fecha(s)";
            context.addMessage(null, new FacesMessage("Reporte de Resultados",mess));
        }
        else{
            oRptRes.setIni(dIni);
            oRptRes.setFin(dFin);
            try{
                ret = oRptRes.buscarRpt();
                if (!ret.isEmpty()){
                    oRptRes = ret.get(0);
                    oRptRes.setIni(dIni);
                    oRptRes.setFin(dFin);
                    nSumaIngresos = oRptRes.getAbonos()+oRptRes.getAnticipos()+
                        oRptRes.getFiniquitos()+ oRptRes.getMontoOtrosIngr()+
                        oRptRes.getMontoOtrosPaq() + oRptRes.getMontoPaqMaternidad()+
                        oRptRes.getMontoPaqPediatricos()+oRptRes.getMontoPrestamos()+
                        oRptRes.getMontoRA()+oRptRes.getMontoRentas()+
                        oRptRes.getVtasContExt()+oRptRes.getVtasContHosp()+
                        oRptRes.getVtasCredExt()+oRptRes.getVtasCredHosp();
                    nSumaVentas = oRptRes.getVtasContExt()+oRptRes.getVtasContHosp()+
                        oRptRes.getVtasCredExt()+oRptRes.getVtasCredHosp();
                }
            }catch(Exception e){
                e.printStackTrace();
                mess = "Error al buscar el reporte";
                context.addMessage(null, new FacesMessage("Reporte de Resultados",mess));
            }
        }
    }
    
    public String getRangoFechas(){
    String sRet = "Reporte de Resultados ";
    SimpleDateFormat fmtTxt = new SimpleDateFormat("dd/MM/yyyy");
        if (dIni != null)
            sRet = sRet + " del "+ fmtTxt.format(dIni);
        if (dFin != null)
            sRet = sRet + " al " + fmtTxt.format(dFin);
        return sRet;
    }
    
    public float getSumaIngresos(){
        return nSumaIngresos;
    }
    
    public float getSumaVentas(){
        return nSumaVentas;
    }
    
    public RptResultadosMes getRpt(){
        return this.oRptRes;
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
