package org.apli.modelbeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.apache.log4j.Logger;
import org.apli.AD.AccesoDatos;

/**
 *
 * @author MiguelAngel
 */
public class Medicamento implements Serializable {

    private String sCveMedicamento;
    private String sNomMedicamento;
    private String sSustanciaActiva;
    private String sLaboratorio;
    private int nExistencia;
    private int nCantidad;
    private int nCantidadActualizada;
    private int nCveArea;
    private float fCostoUnitario;
    private List<Medicamento> resultados;
    private String mensaje;
    protected AccesoDatos oAD = null;
    private UnidadMedida oUnidadMedida;
    private ConceptoIngreso oConceptoIngreso;
    private boolean pedidoExterno = false;
    private static final Logger LOG = Logger.getLogger(Medicamento.class.getName());
    private static final String SEARCH_BY_DESCRIPTION = "D";
    private static final String SEARCH_BY_ACTIVE_SUBSTANCE = "S";

    private String codigo;
    
    public Medicamento() {
        this.oUnidadMedida = new UnidadMedida();
    }

    public Medicamento(AccesoDatos poAD) {
        oAD = poAD;
    }

    public Medicamento buscaMedicamentoVitrina(String cveMedicamento) throws Exception {
        Medicamento m = null;
        Vector rst = null;
        String sQuery = "";

        if (cveMedicamento.equals("")) {
            throw new Exception("Medicamento.buscaMedicamentoVitrina: error de programaciÃ³n, faltan datos");
        } else {
            sQuery = "SELECT * FROM buscaLlaveMedicamento('" + cveMedicamento + "')";
            if (this.getAD() == null) {
                this.setAD(new AccesoDatos());
                this.getAD().conectar();
                rst = this.getAD().ejecutarConsulta(sQuery);
                this.getAD().desconectar();
                setAD(null);
            } else {
                rst = this.getAD().ejecutarConsulta(sQuery);
            }

            if (rst != null && rst.size() == 1) {
                Vector vRowTemp = (Vector) rst.elementAt(0);
                m = new Medicamento();
                m.setCveMedicamento(((String) vRowTemp.elementAt(0)));
                ConceptoIngreso ci = new ConceptoIngreso();
                ci.setCveConcep(((Double) vRowTemp.elementAt(1)).intValue());
                m.setConceptoIngreso(ci);
                m.setNomMedicamento((String) vRowTemp.elementAt(2));
                m.setSustanciaActiva((String) vRowTemp.elementAt(3));
            }
        }
        return m;
    }

    public List<Medicamento> buscaMedicamentoPorDescripcion(String valor) throws Exception {
        Medicamento oMed = null;
        List<Medicamento> listRet = null;
        Vector rst = null;
        String sQuery = "";
        if (valor.equals("")) {
            throw new Exception("Medicamento.buscaMedicamentoPorDescripcion: error de programacion, faltan datos");
        } else {
            actualizarCostoMedicamento(valor, SEARCH_BY_DESCRIPTION);
            sQuery = "SELECT * FROM buscaMedicamentoPorDescripcion('" + valor + "')";
            LOG.trace(sQuery);
            if (oAD == null) {
                oAD = new AccesoDatos();
                oAD.conectar();
                rst = oAD.ejecutarConsulta(sQuery);
                oAD.desconectar();
                oAD = null;
            } else {
                rst = oAD.ejecutarConsulta(sQuery);
            }
            if (rst != null) {
                listRet = new ArrayList();
                for (int i = 0; i < rst.size(); i++) {
                    Vector vRowTemp = (Vector) rst.elementAt(i);
                    oMed = new Medicamento();
                    oMed.setCveMedicamento(((String) vRowTemp.elementAt(0)));
                    ConceptoIngreso ci = new ConceptoIngreso();
                    ci.setCveConcep(((Double) vRowTemp.elementAt(1)).intValue());
                    oMed.setConceptoIngreso(ci);
                    oMed.setNomMedicamento((String) vRowTemp.elementAt(2));
                    oMed.setSustanciaActiva((String) vRowTemp.elementAt(3));
                    oMed.setCodigo((String) vRowTemp.elementAt(4));
                    oMed.setCostoUnitario(((Double) vRowTemp.elementAt(5)).floatValue());
                    listRet.add(oMed);
                }
            }
            return listRet;
        }
    }
    
    public List<Medicamento> buscaMedicamentoPorSustanciaActiva(String valor) throws Exception {
        Medicamento oMed = null;
        List<Medicamento> listRet = null;
        Vector rst = null;
        String sQuery = "";
        if (valor.equals("")) {
            throw new Exception("Medicamento.buscaMedicamentoPorSustanciaActiva: error de programacion, faltan datos");
        } else {
            actualizarCostoMedicamento(valor, SEARCH_BY_ACTIVE_SUBSTANCE);
            sQuery = "SELECT * FROM buscaMedicamentoPorSustanciaActiva('" + valor + "')";
            LOG.trace(sQuery);
            if (oAD == null) {
                oAD = new AccesoDatos();
                oAD.conectar();
                rst = oAD.ejecutarConsulta(sQuery);
                oAD.desconectar();
                oAD = null;
            } else {
                rst = oAD.ejecutarConsulta(sQuery);
            }
            if (rst != null) {
                listRet = new ArrayList();
                for (int i = 0; i < rst.size(); i++) {
                    Vector vRowTemp = (Vector) rst.elementAt(i);
                    oMed = new Medicamento();
                    oMed.setCveMedicamento(((String) vRowTemp.elementAt(0)));
                    ConceptoIngreso ci = new ConceptoIngreso();
                    ci.setCveConcep(((Double) vRowTemp.elementAt(1)).intValue());
                    oMed.setConceptoIngreso(ci);
                    oMed.setNomMedicamento((String) vRowTemp.elementAt(2));
                    oMed.setSustanciaActiva((String) vRowTemp.elementAt(3));
                    oMed.setCodigo((String) vRowTemp.elementAt(4));
                    oMed.setCostoUnitario(((Double) vRowTemp.elementAt(5)).floatValue());
                    listRet.add(oMed);
                }
            }
            return listRet;
        }
    }
    
    /*
     Actualiza los costos de la tabla local de medicamento respecto a la informacion de farmacia
     descripcion: Indica la cadena ingresada a buscar del medicamento solicitado.
     */
    public void actualizarCostoMedicamento(String valor, String typeSearch) throws Exception {
        Medicamento oMedTmp = null;
        List<Medicamento> resultadosBusqueda = null;
        Farmacia oFarmacia = null;
        if (!valor.equals("")) {
            oFarmacia = new Farmacia();
            oMedTmp = new Medicamento();
            oFarmacia.setBranch("1");
            if (typeSearch.equals(SEARCH_BY_DESCRIPTION)) {
                oMedTmp.setNomMedicamento(valor);
                oFarmacia.setMedicamento(oMedTmp);
                resultadosBusqueda = oFarmacia.buscarPorDescripcion();
            } else if (typeSearch.equals(SEARCH_BY_ACTIVE_SUBSTANCE)) {
                oMedTmp.setSustanciaActiva(valor);
                oFarmacia.setMedicamento(oMedTmp);
                resultadosBusqueda = oFarmacia.buscarPorSustanciaActiva();
            } else {
                resultadosBusqueda = new ArrayList<Medicamento>();
                LOG.trace("Metodo de busqueda a farmacia no detectado para actualizacion en SISH");
            }
            LOG.trace("actualizarCostoMedicamento()");
            Vector rst = null;
            String sQuery = "";
            int error = 0, correctas = 0;
            for (Medicamento m : resultadosBusqueda) {
                String response = "";
                if (m.getCodigo().equals("") || m.getCostoUnitario() == 0.0f || m.getNomMedicamento().equals("")) {
                    LOG.trace("Medicamento: actualizarCostoMedicamento(). No se han encontrado datos completos para actualizar Medicamento.");
                } else {
                    sQuery = "SELECT * FROM actualizarCostoMedicamentoFarm('" + m.getCodigo() + "', " + m.getCostoUnitario() + ", '" + m.getNomMedicamento() + "', '" + m.getSustanciaActiva() + "')";
                    if (oAD == null) {
                        oAD = new AccesoDatos();
                        oAD.conectar();
                        rst = oAD.ejecutarConsulta(sQuery);
                        oAD.desconectar();
                        oAD = null;
                    } else {
                        rst = oAD.ejecutarConsulta(sQuery);
                    }
                    response = (rst == null) ? "" : new Valida().eliminaMSJCorchetes(rst.get(0).toString());
                    if (response.indexOf("ERROR") > 0) {
                        error++;
                    } else if (response.indexOf("SUCCESS") > 0) {
                        correctas++;
                    }
                    LOG.trace(response);
                }
            }
            LOG.trace("Informe de actualizacion: erroneas: [" + error + "], correctas: [" + correctas + "]");
        }
    }

    public String getCveMedicamento() {
        return sCveMedicamento;
    }

    public void setCveMedicamento(String nCveMedicamento) {
        this.sCveMedicamento = nCveMedicamento;
    }

    public String getNomMedicamento() {
        return sNomMedicamento;
    }

    public void setNomMedicamento(String sNomMedicamento) {
        this.sNomMedicamento = sNomMedicamento;
    }

    public String getSustanciaActiva() {
        return sSustanciaActiva;
    }

    public void setSustanciaActiva(String sSustanciaActiva) {
        this.sSustanciaActiva = sSustanciaActiva;
    }

    public String getLaboratorio() {
        return sLaboratorio;
    }

    public void setLaboratorio(String sLaboratorio) {
        this.sLaboratorio = sLaboratorio;
    }

    public int getExistencia() {
        return nExistencia;
    }

    public void setExistencia(int nExistencia) {
        this.nExistencia = nExistencia;
    }

    public int getCantidad() {
        return nCantidad;
    }

    public void setCantidad(int cantidad) {
        this.nCantidad = cantidad;
    }

    public int getCantidadActualizada() {
        return nCantidadActualizada;
    }

    public void setCantidadActualizada(int nCantidadActualizada) {
        if (nCantidadActualizada < 0) {
            nCantidadActualizada = 0;
        } else {
            if (0 == this.nExistencia && 0 == this.nCantidadActualizada) {//Preparado para realizar un pedido a farmacia enviado desde un vale de reposicion
                this.nCantidadActualizada = nCantidadActualizada;

            } else if (pedidoExterno) {
                this.nCantidadActualizada = nCantidadActualizada;
            } else if (null != this && nCantidadActualizada > this.nExistencia) {
                LOG.trace(new StringBuilder()
                        .append("Existencia de medicamento seleccionado:")
                        .append(this.nExistencia)
                        .append(" cantidad de pedido:")
                        .append(nCantidadActualizada));
                String rst = new StringBuilder("Busqueda: Error de validacion: no es posible realizar pedido mayor a existencia").toString();
                FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
                FacesContext.getCurrentInstance().addMessage(null, msj2);
            } else {
                this.nCantidadActualizada = nCantidadActualizada;
            }
        }
    }

    public List<Medicamento> getResultados() {
        return resultados;
    }

    public void setResultados(List<Medicamento> resultados) {
        this.resultados = resultados;
    }

    public int getCveArea() {
        return nCveArea;
    }

    public void setCveArea(int nCveArea) {
        this.nCveArea = nCveArea;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public AccesoDatos getAD() {
        return oAD;
    }

    public void setAD(AccesoDatos oAD) {
        this.oAD = oAD;
    }

    public float getCostoUnitario() {
        return fCostoUnitario;
    }

    public void setCostoUnitario(float fCostoUnitario) {
        this.fCostoUnitario = fCostoUnitario;
    }

    public UnidadMedida getUnidadMedida() {
        return oUnidadMedida;
    }

    public void setUnidadMedida(UnidadMedida oUnidadMedida) {
        this.oUnidadMedida = oUnidadMedida;
    }

    public ConceptoIngreso getConceptoIngreso() {
        return oConceptoIngreso;
    }

    public void setConceptoIngreso(ConceptoIngreso oConceptoIngreso) {
        this.oConceptoIngreso = oConceptoIngreso;
    }

    public boolean getPedidoExterno() {
        return pedidoExterno;
    }

    public void setPedidoExterno(boolean pedidoExterno) {
        this.pedidoExterno = pedidoExterno;
    }

    public static final class Utils {

        /**
         *
         * @param medicamentos Lista de objetos de tipo Medicamento
         * @return List<String> Lista de cadenas, cada cadena contiene un objeto
         * Medicamento separando cada propiedad por un pipe '|'
         */
        public static List<String> parseToListString(List<Medicamento> medicamentos) {
            List<String> list = new ArrayList<String>();
            String medicamento;
            for (int i = 0; i < medicamentos.size(); i++) {
                medicamento = new StringBuilder().append(medicamentos.get(i).getCveMedicamento()).append("|").append(medicamentos.get(i).getCantidad()).toString();
                list.add(medicamento);
            }
            return list;
        }

        /**
         *
         * @param medicamentos Lista de cadenas, cada cadena contiene un objeto
         * Medicamento separando cada propiedad por un pipe '|'
         * @return List<Medicamento> Lista de objetos de tipo Medicamento
         */
        public static List<Medicamento> parseToListObject(List<String> medicamentos) {
            Medicamento medicamento;
            List<Medicamento> list = new ArrayList<Medicamento>();
            String[] sTmpMedicam;
            for (int i = 0; i < medicamentos.size(); i++) {
                medicamento = new Medicamento();
                sTmpMedicam = medicamentos.get(i).split("\\|");
                medicamento.setCveMedicamento(sTmpMedicam[0]);
                medicamento.setNomMedicamento(sTmpMedicam[1]);
                medicamento.setSustanciaActiva(sTmpMedicam[2]);
                medicamento.setLaboratorio(sTmpMedicam[3]);
                medicamento.setExistencia(Integer.valueOf(sTmpMedicam[4]));
                medicamento.setCostoUnitario(Float.valueOf(sTmpMedicam[5]));
                list.add(medicamento);
            }
            return list;
        }
    }

    public String getCodigo() {
        return codigo;
    }
    
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    
    @Override
    public String toString() {
        return "Medicamento{" + "sCveMedicamento=" + sCveMedicamento + ", sNomMedicamento=" + sNomMedicamento + ", sSustanciaActiva=" + sSustanciaActiva + ", sLaboratorio=" + sLaboratorio + ", nExistencia=" + nExistencia + ", nCantidad=" + nCantidad + ", nCantidadActualizada=" + nCantidadActualizada + ", nCveArea=" + nCveArea + ", fCostoUnitario=" + fCostoUnitario + ", resultados=" + resultados + ", mensaje=" + mensaje + ", oAD=" + oAD + ", oUnidadMedida=" + oUnidadMedida + ", oConceptoIngreso=" + oConceptoIngreso + ", pedidoExterno=" + pedidoExterno + '}';
    }
}
