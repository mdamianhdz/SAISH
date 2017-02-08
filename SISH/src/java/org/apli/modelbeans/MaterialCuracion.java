package org.apli.modelbeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import org.apache.log4j.Logger;
import org.apli.AD.AccesoDatos;

/**
 *
 * @author MiguelAngel
 */
public class MaterialCuracion implements Serializable {

    private String sCveMaterial;
    private ConceptoIngreso conceptoIngreso;
    private String sDescripcion;
    private double nMonto;
    private float fMonto;
    private UnidadMedida oUnidadMedida;
    protected AccesoDatos oAD = null;
    private static final Logger LOG = Logger.getLogger(MaterialCuracion.class.getName());

    private String codigo;

    public MaterialCuracion() {
    }

    public MaterialCuracion(AccesoDatos poAD) {
        oAD = poAD;
    }

    public MaterialCuracion buscaMaterialCuracion(String cveMaterial) throws Exception {
        MaterialCuracion mc = null;
        Vector rst = null;
        String sQuery = "";

        if (cveMaterial.equals("")) {
            throw new Exception("MaterialCuracion.buscaMaterialCuracion: error de programación, faltan datos");
        } else {
            sQuery = "select * from buscaLlaveMaterialCuracion('" + cveMaterial + "')";
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
                mc = new MaterialCuracion();
                mc.setCveMaterial((String) vRowTemp.elementAt(0));
                ConceptoIngreso ci = new ConceptoIngreso();
                ci.setCveConcep(((Double) vRowTemp.elementAt(1)).intValue());
                mc.setConceptoIngreso(ci);
                mc.setDescripcion((String) vRowTemp.elementAt(2));
                mc.setCodigo((String) vRowTemp.elementAt(3));
            }
        }
        return mc;
    }

    /**
     *
     * @param valor Valor para buscar un material de curacion por descripcion
     * @return List<Medicamento> Lista de materiales de curacion encontrados. SE
     * DEVUELVE UNA LISTA DE OBJETOS DE MEDICAMENTOS YA QUE SE USA PARA PEDIDOS
     * A FARMACIA Y PARA FARMACIA SOLO EXISTEN **PRODUCTOS**
     */
    public List<Medicamento> buscaMaterialDeCuracionPorDescripcion(String valor) throws Exception {
        Medicamento oMed = null;
        List<Medicamento> listRet = null;
        Vector rst = null;
        String sQuery = "";
        if (valor.equals("")) {
            throw new Exception("MaterialCuracion.buscaMaterialDeCuracionPorDescripcion: error de programacion, faltan datos");
        } else {
            actualizarCostoMaterialCuracion(valor);
            sQuery = "SELECT * FROM buscaMaterialDeCuracionPorDescripcion('" + valor + "')";
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
                    oMed.setCodigo((String) vRowTemp.elementAt(3));
                    oMed.setCostoUnitario(((Double) vRowTemp.elementAt(4)).floatValue());
                    listRet.add(oMed);
                }
            }
            return listRet;
        }
    }

    /*
     Actualiza los costos de la tabla local de material de curacion respecto a la informacion de farmacia
     descripcion: Indica la cadena ingresada a buscar del material  solicitado.
     */
    public void actualizarCostoMaterialCuracion(String descripcion) throws Exception {
        Medicamento oMedTmp = null;
        List<Medicamento> resultadosBusqueda;
        Farmacia oFarmacia = null;
        if (!descripcion.equals("")) {
            oFarmacia = new Farmacia();
            oMedTmp = new Medicamento();
            oMedTmp.setNomMedicamento(descripcion);
            oFarmacia.setBranch("1");
            oFarmacia.setMedicamento(oMedTmp);
            resultadosBusqueda = oFarmacia.buscarPorDescripcion();
            LOG.trace("actualizarCostoMaterialCuracion()");
            Vector rst = null;
            String sQuery = "";
            int error = 0, correctas = 0;
            for (Medicamento m : resultadosBusqueda) {
                String response = "";
                if (m.getCodigo().equals("") || m.getCostoUnitario() == 0.0f || m.getNomMedicamento().equals("")) {
                    LOG.trace("MaterialCuracion: actualizarCostoMaterialCuracion(). No se han encontrado datos completos para actualizar Material de Curacion.");
                } else {
                    sQuery = "SELECT * FROM actualizarMaterialCuracionFarm('" + m.getCodigo() + "', " + m.getCostoUnitario() + ", '" + m.getNomMedicamento() + "')";
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

    public String getCveMaterial() {
        return sCveMaterial;
    }

    public void setCveMaterial(String sCveMaterial) {
        this.sCveMaterial = sCveMaterial;
    }

    public ConceptoIngreso getConceptoIngreso() {
        return conceptoIngreso;
    }

    public void setConceptoIngreso(ConceptoIngreso conceptoIngreso) {
        this.conceptoIngreso = conceptoIngreso;
    }

    public String getDescripcion() {
        return sDescripcion;
    }

    public void setDescripcion(String sDescripcion) {
        this.sDescripcion = sDescripcion;
    }

    public double getMonto() {
        return nMonto;
    }

    public void setMonto(double nMonto) {
        this.nMonto = nMonto;
    }

    public AccesoDatos getAD() {
        return oAD;
    }

    public void setAD(AccesoDatos oAD) {
        this.oAD = oAD;
    }

    public float getFMonto() {
        return fMonto;
    }

    public void setFMonto(float fMonto) {
        this.fMonto = fMonto;
    }

    public UnidadMedida getUnidadMedida() {
        return oUnidadMedida;
    }

    public void setUnidadMedida(UnidadMedida oUnidadMedida) {
        this.oUnidadMedida = oUnidadMedida;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    @Override
    public String toString() {
        return "MaterialCuracion{" + "sCveMaterial=" + sCveMaterial + ", conceptoIngreso=" + conceptoIngreso + ", sDescripcion=" + sDescripcion + ", nMonto=" + nMonto + ", fMonto=" + fMonto + ", oUnidadMedida=" + oUnidadMedida + ", oAD=" + oAD + '}';
    }
}
