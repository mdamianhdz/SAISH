package org.apli.jbs.promocion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.servlet.http.HttpSession;
import org.apli.jbs.AreaServicioJB;
import org.apli.jbs.UnidadMedidaJB;
import org.apli.modelbeans.AreaDeServicio;
import org.apli.modelbeans.ConceptoIngreso;
import org.apli.modelbeans.DetallePaquete;
import org.apli.modelbeans.LineaIngreso;
import org.apli.modelbeans.OtrosDsctosPaq;
import org.apli.modelbeans.Paquete;
import org.apli.modelbeans.TipoPaquete;
import org.apli.modelbeans.UnidadMedida;
import org.primefaces.event.CellEditEvent;

/**
 *
 * @author ERJI
 */
@ManagedBean(name = "AdministrarPaquetesJB")
@ViewScoped
public class AdministrarPaquetesJB implements Serializable, Converter {
private float totalCarrito = 0;
private float totalCarrito2 = 0;
private float nDescuento;
private String observaciones = "NA";
private String sTipo = "";
private boolean panelParcialidades = false;
private boolean panelGeneral = false;
private boolean soloLectura = false;
private String sOperacion = "";
private AreaDeServicio areaDeServicio;
private LineaIngreso lineaIngreso;
private AreaDeServicio areaDeServicio2;
private LineaIngreso lineaIngreso2;
private Paquete oPaquete = new Paquete();
private ConceptoIngreso oServicioSeleccionado;
private List<ConceptoIngreso> servicios = new ArrayList<ConceptoIngreso>();
private List<AreaDeServicio> areasDeServicio;
private List<DetallePaquete> carritoServicios = new ArrayList<DetallePaquete>();
private List<DetallePaquete> carritoServicios2 = new ArrayList<DetallePaquete>();
private List<UnidadMedida> listaUnidades;
private List<OtrosDsctosPaq> carritoDescuentos = new ArrayList<OtrosDsctosPaq>();
private List<OtrosDsctosPaq> carritoDescuentos2 = new ArrayList<OtrosDsctosPaq>();
private List<Paquete> paquetes = new ArrayList<Paquete>();
private UnidadMedida oUnidadMedida;
private ArrayList<LineaIngreso> lineasIngreso= null;
private ArrayList<LineaIngreso> lineasIngreso2= null;

    public AdministrarPaquetesJB() {
        oPaquete = new Paquete();
    }

    public void buscaPaquete() throws Exception {
        oPaquete = new Paquete();
        carritoServicios2 = new ArrayList<DetallePaquete>();
        carritoDescuentos2 = new ArrayList<OtrosDsctosPaq>();
        paquetes = oPaquete.buscaTodosPaquetes();
    }

    public void llena(Paquete oPaq, String sOpe) throws Exception {
        sOperacion = sOpe;
        if (sOpe.equals("Consultar")) {
            oPaquete = oPaq;
            panelGeneral = true;
            soloLectura = true;
            DetallePaquete oDP = new DetallePaquete();
            oDP.setPaquete(oPaquete);
            this.carritoServicios2 = oDP.buscaTodosDetallesPaquete();
            OtrosDsctosPaq oODP = new OtrosDsctosPaq();
            oODP.setPaquete(oPaquete);
            carritoDescuentos2 = oODP.buscaTodosDsctosPaq();
            this.actualizaTotal2();
            if (oPaquete.getTipoPago().equals("T")) {
                this.sTipo = "Total";
            }
            if (oPaquete.getTipoPago().equals("P")) {
                this.sTipo = "Parcialidades";
                this.panelParcialidades = true;
            }

        }
        if (sOpe.equals("Modificar")) {
            AreaDeServicio oAreaServicio = new AreaDeServicio();
            AreaServicioJB.areasServicios = new ArrayList<AreaDeServicio>();
            AreaServicioJB.areasServicios = oAreaServicio.todasAreasServicios(AreaDeServicio.TodasAreas);
            areasDeServicio = AreaServicioJB.areasServicios;
            listaUnidades = new ArrayList<UnidadMedida>();
            listaUnidades = new UnidadMedida().buscatodasunidades();
            UnidadMedidaJB.unidadesMedidas = listaUnidades;
            soloLectura = false;
            panelGeneral = true;
            oPaquete = oPaq;
            DetallePaquete oDP = new DetallePaquete();
            oDP.setPaquete(oPaquete);
            carritoServicios2 = oDP.buscaTodosDetallesPaquete();
            OtrosDsctosPaq oODP = new OtrosDsctosPaq();
            oODP.setPaquete(oPaquete);
            carritoDescuentos2 = oODP.buscaTodosDsctosPaq();
            actualizaTotal2();
            if (oPaquete.getTipoPago().equals("T")) {
                this.sTipo = "Total";
            }
            if (oPaquete.getTipoPago().equals("P")) {
                this.sTipo = "Parcialidades";
                this.panelParcialidades = true;
            }
        }
        if (sOpe.equals("Desactivar")) {
            panelGeneral = false;
            oPaquete = oPaq;
            String msj = oPaquete.desactivarPaquete();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, msj, "");
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, msg);
            context.getExternalContext().getFlash().setKeepMessages(true);

            FacesContext facesContext = FacesContext.getCurrentInstance();
            facesContext.getExternalContext().redirect("administrarPaquetes.xhtml");

        }
        if (sOpe.equals("Activar")) {
            panelGeneral = false;
            oPaquete = oPaq;
            String msj = oPaquete.activarPaquete();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, msj, "");
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, msg);
            context.getExternalContext().getFlash().setKeepMessages(true);

            FacesContext facesContext = FacesContext.getCurrentInstance();
            facesContext.getExternalContext().redirect("administrarPaquetes.xhtml");
        }
    }

    public void llena(String oPaq, String sOpe) throws Exception {
        System.out.println("Limpia "+sOpe);
        sOperacion = sOpe;
        if (sOpe.equals("Nuevo")) {
            this.limpiaServicios();
            oPaquete = new Paquete();
            TipoPaquete oTP = new TipoPaquete();
            oPaquete.setTipoPaquete(oTP);
            AreaDeServicio oAreaServicio = new AreaDeServicio();
            AreaServicioJB.areasServicios = new ArrayList<AreaDeServicio>();
            AreaServicioJB.areasServicios = oAreaServicio.todasAreasServicios(AreaDeServicio.TodasAreas);
            areasDeServicio = AreaServicioJB.areasServicios;
            listaUnidades = new ArrayList<UnidadMedida>();
            listaUnidades = new UnidadMedida().buscatodasunidades();
            UnidadMedidaJB.unidadesMedidas = listaUnidades;
            soloLectura = false;
            panelGeneral = true;
            carritoServicios = new ArrayList<DetallePaquete>();
            carritoDescuentos = new ArrayList<OtrosDsctosPaq>();
            lineaIngreso = new LineaIngreso();
            lineaIngreso2 = new LineaIngreso();
        }
    }

    public void actualizaListaServicios() {
        servicios = new ArrayList<ConceptoIngreso>();
        ConceptoIngreso oSM = new ConceptoIngreso();
        try{
            servicios = oSM.buscaServiciosMedicosMatMed(lineaIngreso, 
                    areaDeServicio, 0, "Todos");
            FacesContext facesContext = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) facesContext.getExternalContext().
                      getSession(false);
            session.setAttribute("listaServicios", servicios);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void actualizaListaLineas() {
        lineasIngreso = new ArrayList<LineaIngreso>();
        LineaIngreso oLI = new LineaIngreso();
        try {
            lineasIngreso = (ArrayList)oLI.todasLineasIng(
                    areaDeServicio.getCve(), ConceptoIngreso.TIPO_TODO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actualizaListaLineas2() {
        lineasIngreso2 = new ArrayList<LineaIngreso>();
        LineaIngreso oLI = new LineaIngreso();
        try {
            lineasIngreso2 = (ArrayList)oLI.todasLineasIng(
                    areaDeServicio2.getCve(), ConceptoIngreso.TIPO_TODO);
            lineaIngreso2 = new LineaIngreso();
            System.out.println(lineaIngreso2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void agregaServicio() {
        if (areaDeServicio == null) {
            String rst = "Área para paquete : Error de validación: se necesita un valor.";
            FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
        } else if (oUnidadMedida == null) {
            String rst = "Unidad de Medida  : Error de validación: se necesita un valor.";
            FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
        } else if (lineaIngreso == null || lineaIngreso.getCveLin()<1) {
            String rst = "Linea ingreso para paquete : Error de validación: se necesita un valor.";
            FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
        } else if (oServicioSeleccionado == null) {
            String rst = "Servicio de paquete  : Error de validación: se necesita un valor.";
            FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
        } else {
            boolean encontradoEnRegistrado = false;
            for (int i = 0; i < carritoServicios2.size(); i++) {
                if (oServicioSeleccionado.getIdGenerico().equals(carritoServicios2.get(i).getConceptoIngreso().getIdGenerico()) && carritoServicios2.get(i).getEliminar() == false) {
                    encontradoEnRegistrado = true;
                }
            }
            if (!encontradoEnRegistrado) {
                boolean encontrado = false;
                for (int i = 0; i < carritoServicios.size(); i++) {
                    if (carritoServicios.get(i).getConceptoIngreso().getIdGenerico().equals(oServicioSeleccionado.getIdGenerico())) {
                        encontrado = true;
                    }
                }
                if (!encontrado) {
                    if (oServicioSeleccionado.getTipoConcIngr().equals("medicamento") || 
                            oServicioSeleccionado.getTipoConcIngr().equals("material")) {
                        if (oServicioSeleccionado.validaSoloExistencia()) {
                            oServicioSeleccionado.setAreaServicio(areaDeServicio);
                            DetallePaquete oDetPaq = new DetallePaquete();
                            oServicioSeleccionado.setUnidadMedida(oUnidadMedida);
                            oDetPaq.setConceptoIngreso(oServicioSeleccionado);
                            oDetPaq.setObs(observaciones);
                            carritoServicios.add(oDetPaq);
                        } else {
                            String rst = oServicioSeleccionado.getDescripcion() + " : Sin existencia.";
                            FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
                            FacesContext.getCurrentInstance().addMessage(null, msj2);
                        }
                    } else {
                        DetallePaquete oDetPaq = new DetallePaquete();
                        oServicioSeleccionado.setAreaServicio(areaDeServicio);
                        oServicioSeleccionado.setUnidadMedida(oUnidadMedida);
                        oDetPaq.setConceptoIngreso(oServicioSeleccionado);
                        oDetPaq.setObs(observaciones);
                        carritoServicios.add(oDetPaq);
                    }
                    actualizaTotal();
                    areaDeServicio = null;
                    lineaIngreso = new LineaIngreso();
                    oServicioSeleccionado = null;
                    oUnidadMedida = null;
                } else {
                    String rst = oServicioSeleccionado.getDescripcion() + " : Ya fue agregado, si desea agregar este servicio necesita eliminarlo en la sección de 'Servicios Nuevos'.";
                    FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
                    FacesContext.getCurrentInstance().addMessage(null, msj2);
                }
            } else {
                String rst = oServicioSeleccionado.getDescripcion() + " : Ya está registrado, si desea agregar este servicio necesita marcarlo como eliminado en la sección de 'Servicios Registrados'.";
                FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
                FacesContext.getCurrentInstance().addMessage(null, msj2);
            }
        }
    }

    public void agregaDescuento() {
        if (areaDeServicio2 == null) {
            String rst = "Área para descuento : Error de validación: se necesita un valor.";
            FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
        } else if (lineaIngreso2 == null || lineaIngreso2.getCveLin()<1) {
            String rst = "Línea ingreso para descuento : Error de validación: se necesita un valor.";
            FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
        } else if (this.nDescuento == 0.0) {
            String rst = "Descuento : Error de validación: se necesita un valor.";
            FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
        } else {
            boolean encontradoRegistrado = false;
            for (int i = 0; i < carritoDescuentos2.size(); i++) {
                if (carritoDescuentos2.get(i).getLineaIngreso().getCveLin() == lineaIngreso2.getCveLin() && carritoDescuentos2.get(i).getEliminar() == false) {
                    encontradoRegistrado = true;
                }
            }
            if (!encontradoRegistrado) {
                boolean encontrado = false;
                for (int i = 0; i < carritoDescuentos.size(); i++) {
                    if (carritoDescuentos.get(i).getAreaServicio().getCve().equals(areaDeServicio2.getCve()) && carritoDescuentos.get(i).getLineaIngreso().getCveLin() == lineaIngreso2.getCveLin()) {
                        encontrado = true;
                    }
                }
                if (!encontrado) {
                    OtrosDsctosPaq oOtrsDsc = new OtrosDsctosPaq();
                    oOtrsDsc.setAreaServicio(areaDeServicio2);
                    oOtrsDsc.setLineaIngreso(lineaIngreso2);
                    oOtrsDsc.setPctDscto(nDescuento);
                    carritoDescuentos.add(oOtrsDsc);
                    areaDeServicio2 = null;
                    lineaIngreso2 = new LineaIngreso();
                    nDescuento = 0;
                } else {
                    String rst = lineaIngreso2.getDescrip() + " : Ya fue agregada, si desea agregar esta línea necesita eliminarla en la sección de 'Descuentos Nuevos'.";
                    FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
                    FacesContext.getCurrentInstance().addMessage(null, msj2);
                }
            } else {
                String rst = lineaIngreso2.getDescrip() + " : Ya está registrada, si desea agregar esta línea necesita marcarla como eliminada en la sección de 'Descuentos Registrados'.";
                FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
                FacesContext.getCurrentInstance().addMessage(null, msj2);
            }
        }
    }

    public void eliminaServicioInt(int id) {
        for (int i = 0; i < carritoServicios.size(); i++) {
            if (carritoServicios.get(i).getConceptoIngreso().getTipoConcIngr().equals("Servicio medico") || carritoServicios.get(i).getConceptoIngreso().getTipoConcIngr().equals("otro ingreso")) {
                if (carritoServicios.get(i).getConceptoIngreso().getCveConcep() == id) {
                    carritoServicios.remove(i);
                    break;
                }
            }
        }
        actualizaTotal();
    }

    public void eliminaServicioRegistrado(DetallePaquete ServicioRegistrado) {
        boolean encontradoRegistrado = false;
        for (int i = 0; i < carritoServicios.size(); i++) {
            if (carritoServicios.get(i).getConceptoIngreso().getIdGenerico().equals(ServicioRegistrado.getConceptoIngreso().getIdGenerico())) {
                encontradoRegistrado = true;
            }
        }
        if (encontradoRegistrado == true) {
            ServicioRegistrado.setEliminar(true);
            String rst = ServicioRegistrado.getConceptoIngreso().getDescripcion() + " : No puede desmarcar este servicio ya que no se permitirá tener servicios repetidos; si desea conservarlo elimine el servicio que se encuentra en la sección 'Servicios Nuevos'.";
            FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
        }
    }

    public void eliminaDescuentoRegistrado(OtrosDsctosPaq descuentoRegistrado) {
        boolean encontradoRegistrado = false;
        for (int i = 0; i < carritoDescuentos.size(); i++) {
            if (carritoDescuentos.get(i).getLineaIngreso().getCveLin() == descuentoRegistrado.getLineaIngreso().getCveLin()) {
                encontradoRegistrado = true;
            }
        }
        if (encontradoRegistrado == true) {
            descuentoRegistrado.setEliminar(true);
            String rst = descuentoRegistrado.getLineaIngreso().getDescrip() + " : No puede desmarcar esta línea ya que no se permitirá tener líneas repetidas; si desea conservarla elimine la línea que se encuentra en la sección 'Descuentos Nuevos'.";
            FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
        }
    }

    public void eliminaServicioString(String id) {
        for (int i = 0; i < carritoServicios.size(); i++) {

            if (carritoServicios.get(i).getConceptoIngreso().getTipoConcIngr().equals("medicamento")) {
                if (carritoServicios.get(i).getConceptoIngreso().getMedicamento().getCveMedicamento().equals(id)) {
                    carritoServicios.remove(i);
                    break;
                }
            }
            if (carritoServicios.get(i).getConceptoIngreso().getTipoConcIngr().equals("material")) {
                if (carritoServicios.get(i).getConceptoIngreso().getMaterialCuracion().getCveMaterial().equals(id)) {
                    carritoServicios.remove(i);
                    break;
                }
            }
        }
        actualizaTotal();
    }

    public void eliminaDescuento(String idArea, int idLinea) {
        for (int i = 0; i < carritoDescuentos.size(); i++) {
            if (carritoDescuentos.get(i).getAreaServicio().getCve().equals(idArea) && carritoDescuentos.get(i).getLineaIngreso().getCveLin() == idLinea) {
                carritoDescuentos.remove(i);
            }
        }
    }

    public void actualizaTotal() {
        totalCarrito = 0;
        for (int i = 0; i < carritoServicios.size(); i++) {
            totalCarrito += (carritoServicios.get(i).getConceptoIngreso().getMontoNuevo() * carritoServicios.get(i).getConceptoIngreso().getCantidad());
        }
    }

    public void actualizaTotal2() {
        totalCarrito2 = 0;
        for (int i = 0; i < carritoServicios2.size(); i++) {
            totalCarrito2 += (carritoServicios2.get(i).getConceptoIngreso().getMontoNuevo() * carritoServicios2.get(i).getConceptoIngreso().getCantidad());
        }
    }

    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();

        if (newValue != null && !newValue.equals(oldValue)) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Se ha indicado la unidad de medida :" + ((UnidadMedida) newValue).getDescrip() + " ", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public List<ConceptoIngreso> obtenerServicios(String s) {
        List<ConceptoIngreso> servSelect = new ArrayList<ConceptoIngreso>();
        try {

            if (s.trim().equals("")) {
                return new ArrayList<ConceptoIngreso>();
            }
            for (ConceptoIngreso srv : servicios) {
                if (srv.getDescripcion().contains(s) || 
                    srv.getDescripcion().toLowerCase().contains(s.toLowerCase())) {
                    servSelect.add(srv);
                }
            }
            return servSelect;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void limpiaServicios() {
        carritoServicios = new ArrayList<DetallePaquete>();
        totalCarrito = 0;
        areaDeServicio = null;
        lineaIngreso = new LineaIngreso();
        lineaIngreso2 = new LineaIngreso();
        areaDeServicio2 = null;
        oServicioSeleccionado = null;
        servicios = new ArrayList<ConceptoIngreso>();
        lineasIngreso = new ArrayList<LineaIngreso>();
        lineasIngreso2 = new ArrayList<LineaIngreso>();
        observaciones = "";
        listaUnidades = new ArrayList<UnidadMedida>();
        carritoDescuentos = new ArrayList<OtrosDsctosPaq>();
        carritoServicios2 = new ArrayList<DetallePaquete>();
        carritoDescuentos2 = new ArrayList<OtrosDsctosPaq>();
        nDescuento = 0;
        sTipo = "";
        areasDeServicio = null;
        panelParcialidades = false;
        oPaquete = new Paquete();
        panelGeneral = false;
        soloLectura = false;
    }

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {
        if (submittedValue.trim().equals("")) {
            return null;
        } else {
            try {
                for (ConceptoIngreso ci : servicios) {
                    if (ci.getIdGenerico().equals(submittedValue)) {
                        return ci;
                    }
                }
            } catch (NumberFormatException exception) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid player"));
            }
        }

        return null;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent component, Object value) {
        if (value == null || value.equals("")) {
            return "";
        } else {
            return String.valueOf(((ConceptoIngreso) value).getIdGenerico());
        }
    }

    public void validaTipo() {
        if (sTipo.equals("Total")) {
            panelParcialidades = false;
        }
        if (sTipo.equals("Parcialidades")) {
            panelParcialidades = true;
        }
    }

    public void registraPaquete() throws Exception {
        boolean validacionUnidadmd = false;
        for (int i = 0; i < carritoServicios.size(); i++) {
            if (carritoServicios.get(i).getConceptoIngreso().getUnidadMedida() == null) {
                validacionUnidadmd = true;
            }
        }
        if (validacionUnidadmd == true) {
            String rst = "Unidad de medida : Error de validación: Todo servicio agregado deberá tener una unidad de medida.";
            FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
            FacesContext.getCurrentInstance().addMessage(null, msj2);

        } else if (oPaquete.getTipoPaquete().getCve() == null || oPaquete.getTipoPaquete().getCve().equals("")) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Tipo del paquete : Error de validación: se necesita un valor.", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else if (oPaquete.getNombre().equals("")) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Nombre del paquete : Error de validación: se necesita un valor.", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else if (oPaquete.getCosto() == 0.0) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Costo total del paquete : Error de validación: se necesita un valor.", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else if (sTipo == null) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, 
                    "Tipo de pago del paquete : se necesita un valor.", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else if (sTipo.equals("Parcialidades") && 
                oPaquete.getAnticipo() == 0.0) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, 
                    "Anticipo del paquete : se necesita un valor.", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else if (sTipo.equals("Parcialidades") && 
                oPaquete.getCantParcial() == 0) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, 
           "Cantidad de parcialidades del paquete : se necesita un valor.", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else if (!carritoServicios.isEmpty()) {
            if (sTipo.equals("Total")) {
                oPaquete.setTipoPago("T");
            }
            if (sTipo.equals("Parcialidades")) {
                oPaquete.setTipoPago("P");
            }
            String msj = oPaquete.insertaPaquete(this.carritoDescuentos, 
                    this.carritoServicios);
            if (msj.contains("ERROR")) {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, 
                        msj, "");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            } else {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, 
                        "¡Se registró exitosamente el paquete: " + 
                        oPaquete.getNombre() + "!", "");
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, msg);
                context.getExternalContext().getFlash().setKeepMessages(true);

                FacesContext facesContext = FacesContext.getCurrentInstance();
                facesContext.getExternalContext().redirect(
                        "administrarPaquetes.xhtml");
            }
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, 
            "¡No ha agregado ningún servicio al paquete que desea registrar.!", 
                    "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public void actualizaPaquete() throws Exception {

        int serviciosTotales = carritoServicios.size() + 
                carritoServicios2.size();
        for (int i = 0; i < carritoServicios2.size(); i++) {
            if (carritoServicios2.get(i).getEliminar() == true) {
                serviciosTotales--;
            }
        }
        boolean validacionUnidadmd = false;
        for (int i = 0; i < carritoServicios.size(); i++) {
            if (carritoServicios.get(i).getConceptoIngreso().getUnidadMedida()== 
                    null) {
                validacionUnidadmd = true;
            }
        }
        if (serviciosTotales == 0) {
            String rst = "!Se necesita al menos un servicio activo";
            FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    rst, "");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
        } else if (validacionUnidadmd == true) {
            String rst = "Unidad de medida : falta una unidad de medida.";
            FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    rst, "");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
        } else if (oPaquete.getTipoPaquete().getCve() == null) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, 
                    "Tipo del paquete : se necesita un valor.", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else if (oPaquete.getNombre().equals("")) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, 
                    "Nombre del paquete : se necesita un valor.", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else if (oPaquete.getCosto() == 0.0) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, 
                    "Costo total del paquete : se necesita un valor.", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else if (sTipo == null) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Tipo de pago del paquete : Error de validación: se necesita un valor.", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else if (sTipo.equals("Parcialidades") && oPaquete.getAnticipo() == 0.0) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Anticipo del paquete : Error de validación: se necesita un valor.", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else if (sTipo.equals("Parcialidades") && oPaquete.getCantParcial() == 0) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Cantidad de parcialidades del paquete : Error de validación: se necesita un valor.", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else if (oPaquete.getObs().equals("")) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Otras observaciones del paquete : Error de validación: se necesita un valor.", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            if (sTipo.equals("Total")) {
                oPaquete.setTipoPago("T");
                oPaquete.setCantParcial((short) 0);
                oPaquete.setAnticipo((float) 0.0);

            }
            if (sTipo.equals("Parcialidades")) {
                oPaquete.setTipoPago("P");
            }
            String msj = oPaquete.modificaPaquete(this.carritoDescuentos, this.carritoDescuentos2, this.carritoServicios, this.carritoServicios2);
            if (!msj.contains("ERROR")) {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "¡El paquete: " + oPaquete.getNombre() + " ha sido actualizado exitosamente!", "");
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, msg);
                context.getExternalContext().getFlash().setKeepMessages(true);

                FacesContext facesContext = FacesContext.getCurrentInstance();
                facesContext.getExternalContext().redirect("administrarPaquetes.xhtml");
            } else {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, msj, "");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }
        }
    }

    public List<AreaDeServicio> getAreasDeServicio() {
        return areasDeServicio;
    }

    public void setAreasDeServicio(List<AreaDeServicio> areasDeServicio) {
        this.areasDeServicio = areasDeServicio;
    }

    public AreaDeServicio getAreaDeServicio() {
        return areaDeServicio;
    }

    public void setAreaDeServicio(AreaDeServicio areaDeServicio) {
        this.areaDeServicio = areaDeServicio;
    }

    public AreaDeServicio getAreaDeServicio2() {
        return areaDeServicio2;
    }

    public void setAreaDeServicio2(AreaDeServicio areaDeServicio) {
        this.areaDeServicio2 = areaDeServicio;
    }

    public List<LineaIngreso> getLineasIngreso() {
        return lineasIngreso;
    }

    public List<LineaIngreso> getLineasIngreso2() {
        return lineasIngreso2;
    }

    public float getTotalCarrito() {
        return totalCarrito;
    }

    public void setTotalCarrito(float value) {
        totalCarrito = value;
    }

    public float getTotalCarrito2() {
        return totalCarrito2;
    }

    public void setTotalCarrito2(float value) {
        totalCarrito2 = value;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public LineaIngreso getLineaIngreso() {
        return lineaIngreso;
    }

    public void setLineaIngreso(LineaIngreso lineaIngreso) {
        this.lineaIngreso = lineaIngreso;
    }

    public LineaIngreso getLineaIngreso2() {
        return lineaIngreso2;
    }

    public void setLineaIngreso2(LineaIngreso lineaIngreso) {
        this.lineaIngreso2 = lineaIngreso;
    }

    public ConceptoIngreso getServicioSeleccionado() {
        return oServicioSeleccionado;
    }

    public void setServicioSeleccionado(ConceptoIngreso value) {
        this.oServicioSeleccionado = value;
    }

    public List<ConceptoIngreso> getServicios() {
        return servicios;
    }

    public void setServicios(List<ConceptoIngreso> value) {
        servicios = value;
    }

    public List<DetallePaquete> getCarritoServicios() {
        return carritoServicios;
    }

    public void setCarritoServicios(List<DetallePaquete> carritoServicios) {
        this.carritoServicios = carritoServicios;
    }

    public List<DetallePaquete> getCarritoServicios2() {
        return carritoServicios2;
    }

    public void setCarritoServicios2(List<DetallePaquete> carritoServicios) {
        this.carritoServicios2 = carritoServicios;
    }

    public List<UnidadMedida> getListaUnidades() {
        return listaUnidades;
    }

    public void setListaUnidades(List<UnidadMedida> listaUnidades) {
        this.listaUnidades = listaUnidades;
    }

    public List<OtrosDsctosPaq> getCarritoDescuentos() {
        return carritoDescuentos;
    }

    public void setCarritoDescuentos(List<OtrosDsctosPaq> carritoDescuentos) {
        this.carritoDescuentos = carritoDescuentos;
    }

    public List<OtrosDsctosPaq> getCarritoDescuentos2() {
        return carritoDescuentos2;
    }

    public void setCarritoDescuentos2(List<OtrosDsctosPaq> carritoDescuentos) {
        this.carritoDescuentos2 = carritoDescuentos;
    }

    public float getDescuento() {
        return nDescuento;
    }

    public void setDescuento(float nDescuento) {
        this.nDescuento = nDescuento;
    }

    public String getTipo() {
        return sTipo;
    }

    public void setTipo(String sTipo) {
        this.sTipo = sTipo;
    }

    public boolean getPanelParcialidades() {
        return panelParcialidades;
    }

    public void setPanelParcialidades(boolean panelParcialidades) {
        this.panelParcialidades = panelParcialidades;
    }

    public Paquete getPaquete() {
        return oPaquete;
    }

    public void setPaquete(Paquete oPaquete) {
        this.oPaquete = oPaquete;
    }

    public List<Paquete> getPaquetes() {
        return paquetes;
    }

    public void setPaquetes(List<Paquete> paquetes) {
        this.paquetes = paquetes;
    }

    public boolean getPanelGeneral() {
        return panelGeneral;
    }

    public void setPanelGeneral(boolean panelGeneral) {
        this.panelGeneral = panelGeneral;
    }

    public boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(boolean soloLectura) {
        this.soloLectura = soloLectura;
    }

    public String getOperacion() {
        return sOperacion;
    }

    public void setOperacion(String sOperacion) {
        this.sOperacion = sOperacion;
    }

    public UnidadMedida getUnidadMedida() {
        return oUnidadMedida;
    }

    public void setUnidadMedida(UnidadMedida oUnidadMedida) {
        this.oUnidadMedida = oUnidadMedida;
    }
}
