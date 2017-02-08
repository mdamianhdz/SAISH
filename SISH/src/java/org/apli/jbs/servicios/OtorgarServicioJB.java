/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apli.jbs.servicios;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import org.apli.jbs.AreaServicioJB;
import org.apli.jbs.UsuarioJB;
import org.apli.modelbeans.AreaDeServicio;
import org.apli.modelbeans.PersonalHospitalario;
import org.apli.modelbeans.ServicioPrestado;
import org.apli.modelbeans.Usuario;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author ERJI
 */
@ManagedBean(name = "oOtorServ2")
@ViewScoped
public class OtorgarServicioJB implements Serializable, Converter {

    private List<ServicioPrestado> serviciosPrestados;
    private List<ServicioPrestado> serviciosSeleccionados = new ArrayList<ServicioPrestado>();
    private List<AreaDeServicio> areasServicios;
    private List<PersonalHospitalario> listaPersonalHop;
    private List<PersonalHospitalario> listaTecnicos;
    private List<PersonalHospitalario> listaEnfermeras;
    private AreaDeServicio oAreaServicio;

    public void llena() throws Exception {
        Usuario oUsr = new UsuarioJB().getSesionUsuario();
        if (oUsr != null) {
            AreaServicioJB.areasServicios = new ArrayList<AreaDeServicio>();
            areasServicios = new AreaDeServicio().todasAreasServiciosPorUsuario(
                    oUsr.getUsuario());
            AreaServicioJB.areasServicios = areasServicios;
        }

    }

    public void limpia() {
        serviciosPrestados = new ArrayList<ServicioPrestado>();
        serviciosSeleccionados = new ArrayList<ServicioPrestado>();
    }

    public void otorgarServicio() throws Exception {
        boolean validacion = true;

        if (serviciosSeleccionados.isEmpty()) {
            String rst = "Otorgar : Error de validación: no hay selección de servicios a otorgar";
            FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
        } else {

            for (int i = 0; i < serviciosSeleccionados.size(); i++) {
                if (serviciosSeleccionados.get(i).getPersHospRealiza() == null) {
                    validacion = false;
                    String rst = "Servicio con folio " + serviciosSeleccionados.get(i).getIdFolio() + " : Error de validación: no ha seleccionado quien realizó el servicio. ";
                    FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
                    FacesContext.getCurrentInstance().addMessage(null, msj2);
                    break;
                }
                if (serviciosSeleccionados.get(i).getBAuxdiag() == true && serviciosSeleccionados.get(i).getDFechaEntradaServ() == null) {
                    validacion = false;
                    String rst = "Servicio con folio " + serviciosSeleccionados.get(i).getIdFolio() + " : Error de validación: no ha seleccionado la fecha de entrada al servicio. ";
                    FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
                    FacesContext.getCurrentInstance().addMessage(null, msj2);
                    break;
                }
                if (serviciosSeleccionados.get(i).getBAuxdiag() == true && serviciosSeleccionados.get(i).getSAuxdiag().equals("")) {
                    validacion = false;
                    String rst = "Servicio con folio " + serviciosSeleccionados.get(i).getIdFolio() + " : Error de validación: no ha indicado el auxiliar diagnostica. ";
                    FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
                    FacesContext.getCurrentInstance().addMessage(null, msj2);
                    break;
                }
            }

            if (validacion == true) {
                for (int i = 0; i < serviciosSeleccionados.size(); i++) {
                    for (int j = 0; j < serviciosPrestados.size(); j++) {
                        if (serviciosSeleccionados.get(i).getIdFolio().equals(serviciosPrestados.get(j).getIdFolio())) {
                            serviciosPrestados.remove(j);
                            String msj = serviciosSeleccionados.get(i).otorgarServicio();
                            if (msj.indexOf("ERROR") > 0) {
                                FacesMessage msj2 = new FacesMessage(msj, "");
                                FacesContext.getCurrentInstance().addMessage(null, msj2);
                                validacion = false;
                                break;

                            } else {
                                FacesMessage msj2 = new FacesMessage(msj, "");
                                FacesContext.getCurrentInstance().addMessage(null, msj2);
                            }
                        }
                    }
                }

            }
            serviciosSeleccionados = new ArrayList<ServicioPrestado>();

        }
    }

    public void consultaServicios() throws Exception {
        serviciosPrestados = new ArrayList<ServicioPrestado>();
        serviciosPrestados = new ServicioPrestado().todosServsPrstsParaOtorg(
                oAreaServicio.getCve(), oAreaServicio.getAuxdiag());
        listaPersonalHop = new ArrayList<PersonalHospitalario>();
        listaPersonalHop = new PersonalHospitalario().buscaTodosPacientesArea(
                oAreaServicio.getCve());
        listaTecnicos = new ArrayList<PersonalHospitalario>();
        listaTecnicos = new PersonalHospitalario().buscaTodosPersonalTecnico();
        listaEnfermeras = new ArrayList<PersonalHospitalario>();
        listaEnfermeras = new PersonalHospitalario().buscaTodosPersonalEnfermeras();
    }

    public void onCellEdit(CellEditEvent event) {

        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();

        if (newValue != null && !newValue.equals(oldValue)) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Personal seleccionado:" + ((PersonalHospitalario) newValue).getNombre(), "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public void actualizaFechaEntraServ(RowEditEvent event) throws Exception {
    String rst = "";
        try {
            Date dFechaEntrada = ((ServicioPrestado) event.getObject()).getDFechaEntradaServ();
            Date dFechaSalida = ((ServicioPrestado) event.getObject()).getSalidaservicio();
            if (dFechaEntrada != null && dFechaSalida != null) {
                if (dFechaEntrada.getTime() < dFechaSalida.getTime()) {
                    rst = ((ServicioPrestado) event.getObject()).actualizaFechaEntradaServ();
                    if (rst.indexOf("ERROR") > 0) {
                        FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
                        FacesContext.getCurrentInstance().addMessage(null, msj2);
                    } else {
                        FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_INFO, rst, "");
                        FacesContext.getCurrentInstance().addMessage(null, msj2);
                    }
                } else {
                    FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, "La fecha de salida del servicio no puede ser menor a la de ingreso", "");
                    FacesContext.getCurrentInstance().addMessage(null, msj2);
                    FacesContext.getCurrentInstance().validationFailed();
                }
            } else {
                rst = ((ServicioPrestado) event.getObject()).actualizaFechaEntradaServ();
                if (rst.indexOf("ERROR") > 0) {
                    FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
                    FacesContext.getCurrentInstance().addMessage(null, msj2);
                } else {
                    FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_INFO, rst, "");
                    FacesContext.getCurrentInstance().addMessage(null, msj2);
                }
            }

        } catch (Exception e) {
            rst = "Error.Exception";
            FacesMessage msj2 = new FacesMessage(rst, "");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
            e.printStackTrace();
        }

    }

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {
        if (submittedValue.trim().equals("")) {
            return null;
        } else {
            try {
                if (component.getId().equals("Tecnicos")) {
                    int num = Integer.parseInt(submittedValue);
                    for (PersonalHospitalario oPH : this.listaTecnicos) {
                        if (oPH.getFolioPers() == num) {
                            return oPH;
                        }
                    }
                }
                if (component.getId().equals("Personal")) {
                    int num = Integer.parseInt(submittedValue);
                    for (PersonalHospitalario oPH : this.listaPersonalHop) {
                        if (oPH.getFolioPers() == num) {
                            return oPH;
                        }
                    }
                }
                if (component.getId().equals("Enfermeras")) {
                    int num = Integer.parseInt(submittedValue);
                    for (PersonalHospitalario oPH : this.listaEnfermeras) {
                        if (oPH.getFolioPers() == num) {
                            return oPH;
                        }
                    }
                }
            } catch (NumberFormatException exception) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "No es un paquete válido"));
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent component, Object value) {
        if (value == null || value.equals("")) {
            return "";
        } else {
            String valor = "";

            if (component.getId().equals("Tecnicos")) {
                return valor = String.valueOf(((PersonalHospitalario) value).getFolioPers());
            }
            if (component.getId().equals("Personal")) {
                return valor = String.valueOf(((PersonalHospitalario) value).getFolioPers());
            }
            if (component.getId().equals("Enfermeras")) {
                return valor = String.valueOf(((PersonalHospitalario) value).getFolioPers());
            }
            return valor;
        }
    }

    public List<ServicioPrestado> getServiciosPrestados() {
        return serviciosPrestados;
    }

    public void setServiciosPrestados(List<ServicioPrestado> serviciosPrestados) {
        this.serviciosPrestados = serviciosPrestados;
    }

    public List<ServicioPrestado> getServiciosSeleccionados() {
        return serviciosSeleccionados;
    }

    public void setServiciosSeleccionados(List<ServicioPrestado> serviciosSeleccionados) {
        this.serviciosSeleccionados = serviciosSeleccionados;
    }

    public List<AreaDeServicio> getAreasServicios() {
        return areasServicios;
    }

    public void setAreasServicios(List<AreaDeServicio> areasServicios) {
        this.areasServicios = areasServicios;
    }

    public AreaDeServicio getAreaServicio() {
        return oAreaServicio;
    }

    public void setAreaServicio(AreaDeServicio oAreaServicio) {
        this.oAreaServicio = oAreaServicio;
    }

    public List<PersonalHospitalario> getListaPersonalHop() {
        return listaPersonalHop;
    }

    public void setListaPersonalHop(List<PersonalHospitalario> listaPersonalHop) {
        this.listaPersonalHop = listaPersonalHop;
    }

    public List<PersonalHospitalario> getListaTecnicos() {
        return listaTecnicos;
    }

    public void setListaTecnicos(List<PersonalHospitalario> listaTecnicos) {
        this.listaTecnicos = listaTecnicos;
    }

    public List<PersonalHospitalario> getListaEnfermeras() {
        return listaEnfermeras;
    }

    public void setListaEnfermeras(List<PersonalHospitalario> listaEnfermeras) {
        this.listaEnfermeras = listaEnfermeras;
    }
}
