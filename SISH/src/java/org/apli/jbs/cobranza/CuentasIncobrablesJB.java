/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apli.jbs.cobranza;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import org.apli.modelbeans.ContraRecibo;
import org.apli.modelbeans.ventaCredito.CompaniaCred;

/**
 *
 * @author ERJI
 */
@ManagedBean(name = "oCuentIncrbl")
@SessionScoped
public class CuentasIncobrablesJB implements Serializable, Converter {

    private List<String> lTipos = new ArrayList<String>();
    private List<CompaniaCred> lEmpresas = new ArrayList<CompaniaCred>();
    private CompaniaCred oCompañia = null;
    private String sTipo = "";
    private String sPor;
    private String sEmpresa = "";
    private String sIdPers = "";
    private boolean bid = false;
    private boolean bEmpresa = false;
    private Date dFechaInicio;
    private Date dFechaFinal;
    private List<ContraRecibo> lCuentas;
    private List<ContraRecibo> lCuentasSeleccionadas = new ArrayList<ContraRecibo>();

    public CuentasIncobrablesJB() throws Exception {

        System.out.println("CuentasIncobrablesJB()");
        lEmpresas = new ArrayList<CompaniaCred>();
        lCuentasSeleccionadas = new ArrayList<ContraRecibo>();
        lTipos = new ArrayList<String>();
        lTipos.add("Empresa");
        lTipos.add("Persona");
        sTipo = "Empresa";

        lEmpresas = new CompaniaCred().getCompaniasCredActivas();

    }

    public void realizaBusquedaCuentas() throws Exception {

        if (sTipo.equals("1")) {
            lCuentas = new ArrayList<ContraRecibo>();
            ContraRecibo oCR = new ContraRecibo();
            lCuentas = oCR.buscaFacturasAIncobrables(sTipo, "", oCompañia.getRFC(), dFechaInicio, dFechaFinal);
        }
        if (sTipo.equals("2")) {
            lCuentas = new ArrayList<ContraRecibo>();
            ContraRecibo oCR = new ContraRecibo();
            lCuentas = oCR.buscaFacturasAIncobrables(sTipo, sIdPers, "", dFechaInicio, dFechaFinal);
        }

    }

    public void validaBusqueda() {

        if (sTipo.equals("2")) {
            bid = true;
            bEmpresa = false;
        } else {
            bid = false;
            bEmpresa = true;
        }

    }

    public void limpia() {
        lCuentas = new ArrayList<ContraRecibo>();
        sTipo = "";
        sPor = "";
        sEmpresa = "";
        bid = false;
        bEmpresa = false;
        dFechaInicio = null;
        dFechaFinal = null;

    }

    public void realizarCuentasIncobrables() throws Exception {
        if (lCuentasSeleccionadas.isEmpty()) {

            String rst = "Cuentas incobrables : Error de validación: no hay selección de cuentas incobrables";
            FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
        } else {

            for (int i = 0; i < lCuentasSeleccionadas.size(); i++) {
                for (int j = 0; j < lCuentas.size(); j++) {
                    if (lCuentasSeleccionadas.get(i).getNumContraRecibo() == lCuentas.get(j).getNumContraRecibo()) {
                        ContraRecibo oCR = new ContraRecibo();
                        String msj = oCR.marcarIncobrable(lCuentasSeleccionadas.get(i).getFactura().getFolio(), lCuentasSeleccionadas.get(i).getFactura().getNombreSerie());
                        if (msj.indexOf("ERROR") > 0) {
                            FacesMessage msj2 = new FacesMessage(msj, "");
                            FacesContext.getCurrentInstance().addMessage(null, msj2);
                            break;

                        } else {

                            FacesMessage msj2 = new FacesMessage(msj, "");
                            FacesContext.getCurrentInstance().addMessage(null, msj2);

                        }
                        lCuentas.remove(j);
                    }
                }
            }
            //RequestContext.getCurrentInstance().execute("dlgservicios.show()");

        }

    }

    public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {
        if (submittedValue.trim().equals("")) {
            return null;
        } else {
            try {

                int number = Integer.parseInt(submittedValue);

                for (CompaniaCred c : lEmpresas) {
                    if (c.getIdEmp() == number) {
                        return c;
                    }
                }

            } catch (NumberFormatException exception) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid player"));
            }
        }

        return null;
    }

    public String getAsString(FacesContext facesContext, UIComponent component, Object value) {
        if (value == null || value.equals("")) {
            return "";
        } else {
            return String.valueOf(((CompaniaCred) value).getIdEmp());
        }
    }

    public CompaniaCred getCompañia() {
        return oCompañia;
    }

    public void setCompañia(CompaniaCred oCompañia) {
        this.oCompañia = oCompañia;
    }

    public List<String> getTipos() {
        return lTipos;
    }

    public void setTipos(List<String> lTipos) {
        this.lTipos = lTipos;
    }

    public String getTipo() {
        return sTipo;
    }

    public void setTipo(String sTipo) {
        this.sTipo = sTipo;
    }

    public String getPor() {
        return sPor;
    }

    public void setPor(String sPor) {
        this.sPor = sPor;
    }

    public boolean getId() {
        return bid;
    }

    public String getEmpresa() {
        return sEmpresa;
    }

    public void setEmpresa(String sEmpresa) {
        this.sEmpresa = sEmpresa;
    }

    public List<CompaniaCred> getEmpresas() {
        return lEmpresas;
    }

    public void setEmpresas(List<CompaniaCred> lEmpresas) {
        this.lEmpresas = lEmpresas;
    }

    public boolean getIsEmpresa() {
        return bEmpresa;
    }

    public Date getFechaInicio() {
        return dFechaInicio;
    }

    public void setFechaInicio(Date dInicio) {
        this.dFechaInicio = dInicio;
    }

    public Date getFechaFinal() {
        return dFechaFinal;
    }

    public void setFechaFinal(Date dFinal) {
        this.dFechaFinal = dFinal;
    }

    public List<ContraRecibo> getCuentas() {
        return lCuentas;
    }

    public void setCuentas(List<ContraRecibo> cuentasEmpresa) {
        this.lCuentas = cuentasEmpresa;
    }

    public List<ContraRecibo> getCuentasSeleccionadas() {
        return lCuentasSeleccionadas;
    }

    public void setCuentasSeleccionadas(List<ContraRecibo> lCuentasSeleccionadas) {
        this.lCuentasSeleccionadas = lCuentasSeleccionadas;
    }

    public String getIdPers() {
        return sIdPers;
    }

    public void setIdPers(String sIdPers) {
        this.sIdPers = sIdPers;
    }
}
