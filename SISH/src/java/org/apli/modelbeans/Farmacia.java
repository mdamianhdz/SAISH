package org.apli.modelbeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.ws.WebServiceException;
import org.apache.log4j.Logger;
import org.apli.ws.farmacia.ArrayOfProduct;
import org.apli.ws.farmacia.ArrayOfProductBase;
import org.apli.ws.farmacia.Message;
import org.apli.ws.farmacia.Product;
import org.apli.ws.farmacia.ProductBase;
import org.apli.ws.farmacia.Requisition;

/**
 *
 * @author MiguelAngel
 */
public class Farmacia implements Serializable {

    private int nCveRequisicion;
    private String sBranch;
    private Paciente paciente;
    private String mensaje;
    private String nCveAreaFisica;
    private int nHabitacion;
    private String sEmpresa;
    private List<Medicamento> medicamentos;
    /*
     * Propiedades necesarias para busqueda de medicamentos, solicitud y modificacion de requisiciones a farmacia
     */
    private Medicamento medicamento;
    private Requisition requisition;
    private Message response;
    private static final Logger LOG = Logger.getLogger(Farmacia.class.getName());

    public void registrarPedido() {
        if (this.sBranch.equals("") && this.paciente == null && this.nHabitacion == 0 && this.medicamentos.isEmpty()) {
            this.nCveRequisicion = -1;
            this.mensaje = new StringBuilder().append("Registrar Pedido: Faltan datos.").toString();
        } else {
            try {
                this.requisition = new Requisition();
                this.response = new Message();
                this.requisition.setBranch(this.sBranch);
                this.requisition.setPatientFolio(this.paciente.getFolioPac());
                this.requisition.setPatientName(this.paciente.getNombreCompleto());
                this.requisition.setRoom(this.nHabitacion);
                this.requisition.setProducts(Farmacia.Utils.parseToArrayOfProductBase(this.medicamentos));
                LOG.trace(this.medicamentos);
                LOG.trace(this.requisition);
                this.response = createRequisition(this.requisition);
                if (this.response.getCode() < 0) {
                    this.nCveRequisicion = -1;
                    this.mensaje = this.response.getDescription().getValue();
                } else {
                    LOG.trace("Response by Farmacia: [" + this.response.getDescription().getValue() + "]");
                    this.nCveRequisicion = this.response.getCode();
                    this.mensaje = "Sistema farmacia: Pedido registrado correctamente";
                }
            } catch (WebServiceException e) {
                this.mensaje = new StringBuilder().append("Error de comunicación: No se pudo conectar con el sistema de farmacia").toString();
                LOG.error(this.mensaje, e);
                this.nCveRequisicion = -1;
            }
        }
    }

    public void modificarPedido() {
        if (this.sBranch.equals("") && this.paciente == null && this.nHabitacion == 0 && this.medicamentos.isEmpty()) {
            this.nCveRequisicion = -1;
            this.mensaje = new StringBuilder().append("Modificar Pedido: Faltan datos.").toString();
        } else {
            try {
                this.requisition = new Requisition();
                this.response = new Message();
                this.requisition.setBranch(this.sBranch);
                this.requisition.setPatientFolio(this.paciente.getFolioPac());
                this.requisition.setPatientName(this.paciente.getNombreCompleto());
                this.requisition.setRoom(this.nHabitacion);
                this.requisition.setProducts(Farmacia.Utils.parseToArrayOfProductBase(this.medicamentos));
                LOG.trace(this.medicamentos);
                this.response = modifyRequisition(this.requisition);
                if (this.response.getCode() < 0) {
                    this.nCveRequisicion = -1;
                    this.mensaje = this.response.getDescription().getValue();
                } else {
                    LOG.trace("Response by Farmacia: [" + this.response.getDescription().getValue() + "]");
                    this.nCveRequisicion = this.response.getCode();
                    this.mensaje = "Sistema farmacia: Pedido modificado correctamente";
                }
            } catch (WebServiceException e) {
                this.mensaje = new StringBuilder().append("Error de comunicación: No se pudo conectar con el sistema de farmacia").toString();
                LOG.error(this.mensaje, e);
                this.nCveRequisicion = -1;
            }
        }
    }

    public void cancelarPedido() {
        if (this.sBranch.equals("") && this.nCveRequisicion == 0) {
            this.nCveRequisicion = -1;
            this.mensaje = new StringBuilder().append("Cancelar Pedido: Faltan datos.").toString();
        } else {
            try {
                this.response = cancelRequisition(this.sBranch, this.nCveRequisicion);
                if (this.response.getCode() < 0) {
                    this.nCveRequisicion = -1;
                    this.mensaje = this.response.getDescription().getValue();
                } else {
                    LOG.trace("Response by Farmacia: [" + this.response.getDescription().getValue() + "]");
                    this.nCveRequisicion = this.response.getCode();
                    this.mensaje = "Sistema farmacia: Pedido cancelado correctamente";
                }
            } catch (WebServiceException e) {
                this.mensaje = new StringBuilder().append("Error de comunicación: No se pudo conectar con el sistema de farmacia").toString();
                LOG.error(this.mensaje, e);
                this.nCveRequisicion = -1;
            }
        }
    }

    public List<Medicamento> buscarPorSustanciaActiva() {
        List<Medicamento> list;
        ArrayOfProduct arrayOfProduct;
        if (this.sBranch.equals("") && this.getMedicamento().getSustanciaActiva().equals("")) {
            this.mensaje = new StringBuilder().append("Buscar por sustancia activa: Faltan datos.").toString();
            return null;
        } else {
            try {
                arrayOfProduct = searchByActiveSubstance(this.getMedicamento().getSustanciaActiva(), this.sBranch);
            } catch (WebServiceException e) {
                this.mensaje = new StringBuilder().append("Error de comunicación: No se pudo conectar con el sistema de farmacia").toString();
                LOG.error(this.mensaje, e);
                return null;
            }
            list = Farmacia.Utils.parseToListObject(arrayOfProduct);
        }
        LOG.trace(list);
        return list;
    }

    public List<Medicamento> buscarPorDescripcion() {
        List<Medicamento> list;
        ArrayOfProduct arrayOfProduct;
        if (this.sBranch.equals("") && this.getMedicamento().getNomMedicamento().equals("")) {
            this.mensaje = new StringBuilder().append("Buscar por descripcion: Faltan datos.").toString();
            return null;
        } else {
            try {
                arrayOfProduct = searchByDescription(this.getMedicamento().getNomMedicamento(), this.sBranch);
            } catch (WebServiceException e) {
                this.mensaje = new StringBuilder().append("Error de comunicación: No se pudo conectar con el sistema de farmacia").toString();
                LOG.error(this.mensaje, e);
                return null;
            }
            list = Utils.parseToListObject(arrayOfProduct);
        }
        LOG.trace(list);
        return list;
    }

    public Medicamento obtenerDatosFaltantes() {
        List<Medicamento> list;
        ArrayOfProduct results;
        Medicamento oMedicamento = new Medicamento();
        boolean encontrado = false;
        if (this.sBranch.equals("") && this.getMedicamento().getCodigo().equals("") && this.getMedicamento().getNomMedicamento().equals("")) {
            this.mensaje = new StringBuilder().append("Obtener datos: Faltan datos.").toString();
            return null;
        } else {
            try {
                results = searchByDescription(this.getMedicamento().getNomMedicamento(), this.sBranch);
            } catch (WebServiceException e) {
                this.mensaje = new StringBuilder().append("Error de comunicación: No se pudo conectar con el sistema de farmacia").toString();
                LOG.trace(mensaje, e);
                return null;
            }
            list = Utils.parseToListObject(results);
            LOG.trace("Size de list: " + list.size());
            if (list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    oMedicamento = (Medicamento) list.get(i);
                    LOG.trace("Content Object: [" + oMedicamento + "]");
                    if (oMedicamento.getCodigo().equals(this.getMedicamento().getCveMedicamento())) {
                        this.getMedicamento().setCostoUnitario(oMedicamento.getCostoUnitario());
                        this.getMedicamento().setLaboratorio(oMedicamento.getLaboratorio());
                        this.getMedicamento().setExistencia(oMedicamento.getExistencia());
                        encontrado = true;
                        LOG.trace(oMedicamento);
                    }
                    LOG.trace("Medicamento en posicion " + i + " =" + oMedicamento);
                }
                LOG.trace("Medicamento enviado: " + this.getMedicamento());
            } else {
                this.mensaje = new StringBuilder().append("Error de comunicación: No se pudo conectar con el sistema de farmacia").toString();
                LOG.trace(mensaje);
                return null;
            }
            if (!encontrado) {
                this.mensaje = new StringBuilder().append("Error de consulta: Se encontraron medicamentos pero ninguno coincide con la clave que se envio").toString();
                LOG.trace(mensaje);
                return null;
            }
        }
        return oMedicamento;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public List<Medicamento> getPedido() {
        return medicamentos;
    }

    public void setPedido(List<Medicamento> pedido) {
        this.medicamentos = pedido;
    }

    public int getCveRequisicion() {
        return nCveRequisicion;
    }

    public void setCveRequisicion(int nCveRequisicion) {
        this.nCveRequisicion = nCveRequisicion;
    }

    public String getCveAreaFisica() {
        return nCveAreaFisica;
    }

    public void setCveAreaFisica(String nCveAreaFisica) {
        this.nCveAreaFisica = nCveAreaFisica;
    }

    public int getHabitacion() {
        return nHabitacion;
    }

    public void setHabitacion(int nHabitacion) {
        this.nHabitacion = nHabitacion;
    }

    public String getEmpresa() {
        return sEmpresa;
    }

    public void setEmpresa(String sEmpresa) {
        this.sEmpresa = sEmpresa;
    }

    public String getBranch() {
        return sBranch;
    }

    public void setBranch(String sBranch) {
        this.sBranch = sBranch;
    }

    public Medicamento getMedicamento() {
        return medicamento;
    }

    public void setMedicamento(Medicamento medicamento) {
        this.medicamento = medicamento;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    @Override
    public String toString() {
        return "Farmacia{" + "nCveRequisicion=" + nCveRequisicion + ", sBranch=" + sBranch + ", paciente=" + paciente + ", mensaje=" + mensaje + ", nCveAreaFisica=" + nCveAreaFisica + ", nHabitacion=" + nHabitacion + ", sEmpresa=" + sEmpresa + ", requisicion=" + medicamentos + ", medicamento=" + medicamento + ", requisition=" + requisition + ", response=" + response + '}';
    }

    private static Message createRequisition(org.apli.ws.farmacia.Requisition requisition) {
        org.apli.ws.farmacia.ServiceSiena service = new org.apli.ws.farmacia.ServiceSiena();
        org.apli.ws.farmacia.ISiena port = service.getBasicHttpBindingISiena();
        return port.createRequisition(requisition);
    }

    private static Message modifyRequisition(org.apli.ws.farmacia.Requisition requisition) {
        org.apli.ws.farmacia.ServiceSiena service = new org.apli.ws.farmacia.ServiceSiena();
        org.apli.ws.farmacia.ISiena port = service.getBasicHttpBindingISiena();
        return port.modifyRequisition(requisition);
    }

    private static Message cancelRequisition(java.lang.String branch, java.lang.Integer folio) {
        org.apli.ws.farmacia.ServiceSiena service = new org.apli.ws.farmacia.ServiceSiena();
        org.apli.ws.farmacia.ISiena port = service.getBasicHttpBindingISiena();
        return port.cancelRequisition(branch, folio);
    }

    private static ArrayOfProduct searchByActiveSubstance(java.lang.String activeSubstance, java.lang.String branch) {
        org.apli.ws.farmacia.ServiceSiena service = new org.apli.ws.farmacia.ServiceSiena();
        org.apli.ws.farmacia.ISiena port = service.getBasicHttpBindingISiena();
        return port.searchByActiveSubstance(branch, activeSubstance);
    }

    private static ArrayOfProduct searchByDescription(java.lang.String description, java.lang.String branch) {
        org.apli.ws.farmacia.ServiceSiena service = new org.apli.ws.farmacia.ServiceSiena();
        org.apli.ws.farmacia.ISiena port = service.getBasicHttpBindingISiena();
        return port.searchByDescription(branch, description);
    }

    public static final class Utils {

        /**
         *
         * @param medicamentos Lista de objetos de tipo Medicamento
         * @return ArrayOfProductBase Lista de Product
         */
        public static ArrayOfProductBase parseToArrayOfProductBase(List<Medicamento> medicamentos) {
            ArrayOfProductBase arrayOfProductBase = new ArrayOfProductBase();
            ProductBase productBase;
            for (int i = 0; i < medicamentos.size(); i++) {
                productBase = new ProductBase();
                if (medicamentos.get(i).getCodigo() != null) {
                    productBase.setCode(medicamentos.get(i).getCodigo());
                } else {
                productBase.setCode(medicamentos.get(i).getCveMedicamento());
                }
                if (medicamentos.get(i).getCantidadActualizada() != 0) {
                productBase.setQuantity(medicamentos.get(i).getCantidadActualizada());
                } else {
                    productBase.setQuantity(medicamentos.get(i).getCantidad());
                }
                arrayOfProductBase.getProductBase().add(productBase);
            }
            LOG.trace(arrayOfProductBase);
            return arrayOfProductBase;
        }

        /**
         *
         * @param medicamentos Objeto ArrayOfProduct,
         * @return List<Medicamento> Lista de objetos de tipo Medicamento
         */
        public static List<Medicamento> parseToListObject(ArrayOfProduct medicamentos) {
            Medicamento medicamento;
            Product product;
            List<Medicamento> list = new ArrayList<Medicamento>();
            for (int i = 0; i < medicamentos.getProduct().size(); i++) {
                product = medicamentos.getProduct().get(i);
                medicamento = new Medicamento();
                medicamento.setCveMedicamento(product.getCode());
                medicamento.setCodigo(product.getCode());
                medicamento.setNomMedicamento(product.getDescription());
                medicamento.setSustanciaActiva(product.getActiveSubstance());
                medicamento.setLaboratorio(product.getLaboratory().getValue());
                medicamento.setExistencia(product.getQuantity());
                medicamento.setCostoUnitario((float) (product.getPrice()));
                list.add(medicamento);
            }
            return list;
        }
    }
}