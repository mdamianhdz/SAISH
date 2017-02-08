package org.apli.jbs.contabilidadInterna;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apli.modelbeans.contabilidadInterna.CuentaBanco;
import org.apli.modelbeans.contabilidadInterna.MovimientoCtaBan;
import org.apli.modelbeans.ventaCredito.CompaniaCred;

/**
 *
 * @author Lily_LnBnd
 */
@ManagedBean(name = "oCedulaB")
@ViewScoped
public class ConsultarCedulaBancosJB implements Serializable {
private int selectedBanco;
private String selectedCuenta;
private List<CuentaBanco> listCuentas;
private Date dInicio;
private Date dFin;
private List<MovimientoCtaBan> listMovimientos;

    public ConsultarCedulaBancosJB() {
    }

    public void buscaCuentas() throws Exception {
        listCuentas = new CuentaBanco().buscaCuentas(selectedBanco);
    }

    public void validaFecha() {
        String mess = "";
        if (dInicio == null) {
            mess = "No ha especificado la fecha de inicio";
        } else if (dInicio.compareTo(dFin) > 0) {
            mess = "La fecha final del periodo debe ser posterior a la fecha de inicio";
        }
        if (!"".equals(mess)) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Consultar cédula", mess));
            context.getExternalContext().getFlash().setKeepMessages(true);
        }
    }

    public void buscar() throws Exception {
        String sCondicion = "", mess = "";
        if (selectedBanco == 0 || selectedCuenta.length() == 0) {
            mess = "Error de Validacion. Debe especificar el banco y la cuenta que desee consultar.";
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Consultar cédula", mess));
            context.getExternalContext().getFlash().setKeepMessages(true);
        } else {
            if (dInicio != null && dFin != null) 
                sCondicion = "pdfecmov between '" + dInicio + "' and '" + dFin + "'";
            listMovimientos = new MovimientoCtaBan().buscaCedulaCuenta(selectedBanco, selectedCuenta, sCondicion, dInicio);
        }
        if (dInicio != null && dFin != null) {
            listMovimientos.get(0).setFechaMov(dInicio);
            listMovimientos.get(listMovimientos.size() - 1).setFechaMov(dFin);
        }
        dInicio = null;
        dFin = null;
    }

    public float calculaSaldo(int nIdMov) {
        float nRet = 0.0f;
        if (nIdMov == 0) {
            return listMovimientos.get(0).getMonto();
        }
        for (int i = 0; i < listMovimientos.size(); i++) {
            if (i == 0) {
                nRet = listMovimientos.get(0).getMonto();
            } else {
                if (listMovimientos.get(i).getIdMovCtaBan() == nIdMov) {
                    if (listMovimientos.get(i).getConceptoMov().getTipoMovCta().equals("E")) {
                        nRet = nRet + listMovimientos.get(i).getMonto();
                    } else {
                        nRet = nRet - listMovimientos.get(i).getMonto();
                    }
                    return nRet;
                }
                if (listMovimientos.get(i).getConceptoMov().getTipoMovCta().equals("E")) {
                    nRet = nRet + listMovimientos.get(i).getMonto();
                } else {
                    nRet = nRet - listMovimientos.get(i).getMonto();
                }
            }
        }
        return nRet;
    }

    public List<CompaniaCred> getListBancos() throws Exception {
        return new CompaniaCred().buscaBancos();
    }

    public int getSelectedBanco() {
        return selectedBanco;
    }

    public void setSelectedBanco(int value) {
        selectedBanco = value;
    }

    public List<CuentaBanco> getListCuentas() throws Exception {
        return listCuentas;
    }

    public String getSelectedCuenta() {
        return selectedCuenta;
    }

    public void setSelectedCuenta(String value) {
        selectedCuenta = value;
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

    public List<MovimientoCtaBan> getListMovimientos() {
        return listMovimientos;
    }

    public void setListMovimientos(List<MovimientoCtaBan> value) {
        listMovimientos = value;
    }
}
