package org.apli.modelbeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import org.apli.AD.AccesoDatos;

/**
 * Especificaci�n del contenido del paquete (concepto de ingreso, cantidad,
 * unidad de medida)
 *
 * @author BAOZ
 * @version 1.0
 * @created 17-jul.-2014 10:42:04
 */
public class DetallePaquete implements Serializable{

    /**
     * Cantidad de servicios del concepto de ingreso que incluye
     */
    private int nCant;
    /**
     * Costo del servicio por efectos del paquete
     */
    private float nCosto;
    /**
     * Unidad de medida del concepto de ingreso (tiene sentido en medicamentos y
     * material: piezas, cajas, mililitros, pastillas)
     */
    private UnidadMedida oUniMed;
    /**
     * Observaciones al concepto de ingreso espec�fico, por ejemplo "s�lo en
     * caso necesario"
     */
    private String sObs;
    private ConceptoIngreso oConceptoIngreso;
    private Paquete oPaquete;
    protected AccesoDatos oAD = null;
    private boolean bRegistrado = false;
    private boolean beliminar = false;
    private Habitacion oHabitacion;

    public DetallePaquete() {

    }

    public DetallePaquete(AccesoDatos poAD) {
        oAD = poAD;
    }
    
    public String getQueryInsertarDetallePaquete() throws Exception {
        String sQuery = "", sId="";
        if (this.oConceptoIngreso == null ) {
            throw new Exception("DetallePaquete. getQueryInsertarDetallePaquete(): Error de programación. Faltan datos.");
        } else {
            if (this.getPaquete()==null)
                sId = "currval('paquete_nidpaquete_seq')::int";
            else
                sId = this.getPaquete().getIdPaquete()+"";
            if (oConceptoIngreso.getTipoConcIngr().equals("Servicio medico") || 
                    oConceptoIngreso.getTipoConcIngr().equals("servmed")|| 
                    oConceptoIngreso.getTipoConcIngr().equals("otro ingreso")) {
                sQuery = "INSERT INTO tmp_resultados SELECT * FROM administrardetallepaq("
                        + "'insertar'::character varying,"+sId+"," + 
                        this.oConceptoIngreso.getCveConcep() + ", null, null, "+
                        this.oConceptoIngreso.getCantidad() + ", '" + 
                        this.oConceptoIngreso.getUnidadMedida().getCve() + 
                        "'::character(5), " + this.oConceptoIngreso.getMontoNuevo() + 
                        "::numeric, " + 
                        (this.sObs==null||this.sObs.equals("")?"null":"'"+this.sObs+"'")
                        + "::character varying);";
            }
            if (oConceptoIngreso.getTipoConcIngr().equals("medicamento")) {
                sQuery = "INSERT INTO tmp_resultados SELECT * FROM administrardetallepaq("+
                        "'insertar'::character varying,"+sId+"," + 
                        this.oConceptoIngreso.getCveConcep() + ", '" + 
                        this.oConceptoIngreso.getMedicamento().getCveMedicamento() + 
                        "'::character varying, null, " + 
                        this.oConceptoIngreso.getCantidad() + ", '" + 
                        this.oConceptoIngreso.getUnidadMedida().getCve() + 
                        "'::character(5), " + this.oConceptoIngreso.getMontoNuevo() + 
                        "::numeric, " + 
                        (this.sObs==null||this.sObs.equals("")?"null":"'"+this.sObs+"'")
                        + "::character varying);";
            }
            if (oConceptoIngreso.getTipoConcIngr().equals("material")) {
                sQuery = "INSERT INTO tmp_resultados SELECT * FROM administrardetallepaq("+
                        "'insertar'::character varying,"+sId+"," + 
                        this.oConceptoIngreso.getCveConcep() + ", null, '" + 
                        this.oConceptoIngreso.getMaterialCuracion().getCveMaterial() + 
                        "'::character varying, " + this.oConceptoIngreso.getCantidad() + 
                        ", '" + this.oConceptoIngreso.getUnidadMedida().getCve() + 
                        "'::character(5), " + this.oConceptoIngreso.getMontoNuevo() + 
                        "::numeric, " + 
                        (this.sObs==null||this.sObs.equals("")?"null":"'"+this.sObs+"'")
                        + "::character varying);";
            } 
        }
        return sQuery;
    }
    
    public String getQueryEliminaDetallePaquete() throws Exception {
        String sQuery = "";
        if (this.oPaquete == null) {
            throw new Exception("DetallePaquete. getQueryEliminaDetallePaquete(): Error de programación. Faltan datos.");
        } else {
            sQuery = "SELECT * FROM administrardetallepaq('eliminar'::character varying," + this.oPaquete.getIdPaquete() + "," + this.oConceptoIngreso.getCveConcep() + ", null, null, " + this.oConceptoIngreso.getCantidad() + ", '" + this.oConceptoIngreso.getUnidadMedida().getCve() + "'::character(5), " + this.oConceptoIngreso.getMontoNuevo() + "::numeric, '" + this.sObs + "'::character varying);";
        }
        return sQuery;
    }

    public List<DetallePaquete> buscaTodosDetallesPaquete() throws Exception {
        List<DetallePaquete> listaDetsPaqs = new ArrayList<DetallePaquete>();
        if (this.oPaquete == null) {
            throw new Exception("DetallePaquete. buscaTodosDetallesPaquete(): Error de programación. Faltan datos.");
        } else {
            DetallePaquete oDP = null;
            Vector rst = null;
            Vector<DetallePaquete> vObj = null;

            String sQuery = "";
            int i = 0, nTam = 0;

            sQuery = "SELECT * FROM buscaDetallesPaq(" + oPaquete.getIdPaquete() + ");";
            if (getAD() == null) {
                setAD(new AccesoDatos());
                getAD().conectar();
                rst = getAD().ejecutarConsulta(sQuery);
                getAD().desconectar();
                setAD(null);
            } else {
                rst = getAD().ejecutarConsulta(sQuery);
            }
            if (rst != null) {
                vObj = new Vector<DetallePaquete>();
                for (i = 0; i < rst.size(); i++) {
                    oDP = new DetallePaquete();
                    oDP.setPaquete(oPaquete);
                    Vector vRowTemp = (Vector) rst.elementAt(i);
                    oConceptoIngreso = new ConceptoIngreso();
                    oConceptoIngreso.setCveConcep(((Double) vRowTemp.elementAt(1)).intValue());
                    oConceptoIngreso = oConceptoIngreso.buscaConceptoIngreso();
                    oConceptoIngreso.setIdGenerico("SO-" + ((Double) vRowTemp.elementAt(1)).intValue());
                    oConceptoIngreso.setTipoConcIngr("Servicio medico");
                    if (!((String) vRowTemp.elementAt(2)).equals("")) {
                        Medicamento oM = new Medicamento();
                        oM = oM.buscaMedicamentoVitrina((String) vRowTemp.elementAt(2));
                        oConceptoIngreso.setMedicamento(oM);
                        oConceptoIngreso.setDescripcion(oM.getNomMedicamento());
                        oConceptoIngreso.setIdGenerico("ME-" + (String) vRowTemp.elementAt(2));
                        oConceptoIngreso.setTipoConcIngr("medicamento");                    
                    }
                    if (!((String) vRowTemp.elementAt(3)).equals("")) {
                        MaterialCuracion oM = new MaterialCuracion();
                        oM=oM.buscaMaterialCuracion((String) vRowTemp.elementAt(3));
                        oConceptoIngreso.setMaterialCuracion(oM);
                        oConceptoIngreso.setDescripcion(oM.getDescripcion());
                        oConceptoIngreso.setIdGenerico("MC-" + (String) vRowTemp.elementAt(3));
                        oConceptoIngreso.setTipoConcIngr("material"); 
                    }
                    oConceptoIngreso.setCantidad(((Double) vRowTemp.elementAt(4)).intValue());
                    if (oConceptoIngreso.getDescripcion().contains("HABITACION")) {
                        String[] arrHab=oConceptoIngreso.getDescripcion().split(" ");
                        int numHab=Integer.parseInt(arrHab[1]);
                        Habitacion oHab=new Habitacion();
                        oDP.setHabitacion(oHab.buscaHabitacion(numHab));
                    }
                    UnidadMedida oUM = new UnidadMedida();
                    oUM = oUM.buscaUnidadMedidad((String) vRowTemp.elementAt(5));
                    oConceptoIngreso.setUnidadMedida(oUM);
                    oConceptoIngreso.setMonto(((Double) vRowTemp.elementAt(6)).floatValue());
                    oConceptoIngreso.setMontoNuevo(((Double) vRowTemp.elementAt(6)).floatValue());
                    oDP.setConceptoIngreso(oConceptoIngreso);
                    oDP.setObs((String) vRowTemp.elementAt(7));
                    oDP.setRegistrado(true);
                    listaDetsPaqs.add(oDP);
                }

            }
        }
        return listaDetsPaqs;
    }

    public int getCant() {
        return nCant;
    }

    public void setCant(int nCant) {
        this.nCant = nCant;
    }

    public float getCosto() {
        return nCosto;
    }

    public void setCosto(float nCosto) {
        this.nCosto = nCosto;
    }

    public UnidadMedida getUniMed() {
        return oUniMed;
    }

    public void setUniMed(UnidadMedida oUniMed) {
        this.oUniMed = oUniMed;
    }

    public String getObs() {
        return sObs;
    }

    public void setObs(String sObs) {
        this.sObs = sObs;
    }

    public ConceptoIngreso getConceptoIngreso() {
        return oConceptoIngreso;
    }

    public void setConceptoIngreso(ConceptoIngreso oConceptoIngreso) {
        this.oConceptoIngreso = oConceptoIngreso;
    }

    public Paquete getPaquete() {
        return oPaquete;
    }

    public void setPaquete(Paquete oPaquete) {
        this.oPaquete = oPaquete;
    }

    public AccesoDatos getAD() {
        return oAD;
    }

    public void setAD(AccesoDatos oAD) {
        this.oAD = oAD;
    } public boolean getRegistrado() {
        return bRegistrado;
    }

    public void setRegistrado(boolean bRegistrado) {
        this.bRegistrado = bRegistrado;
    }

    public boolean getEliminar() {
        return beliminar;
    }

    public void setEliminar(boolean beliminar) {
        this.beliminar = beliminar;
    }

    public Habitacion getHabitacion() {
        return oHabitacion;
    }

    public void setHabitacion(Habitacion oHabitacion) {
        this.oHabitacion = oHabitacion;
    }

 

}
