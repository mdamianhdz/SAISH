package org.apli.modelbeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import org.apli.AD.AccesoDatos;

/**
 * Descuentos que considera el paquete a nivel de l�nea de ingreso (por ejemplo,
 * "20% de descuento en todos los estudios de laboratorio")
 *
 * @author BAOZ
 * @version 1.0
 * @created 17-jul.-2014 10:42:06
 */
public class OtrosDsctosPaq implements Serializable {

    /**
     * Porcentaje de descuento (10 % por ejemplo) a aplicar
     */
    private AreaDeServicio oAreaServicio;
    private LineaIngreso oLineaIngreso;
    private Paquete oPaquete;
    private float nPctDscto;
    protected AccesoDatos oAD = null;

    private boolean bRegistrado = false;
    private boolean beliminar = false;

    public OtrosDsctosPaq() {

    }

    public OtrosDsctosPaq(AccesoDatos poAD) {
        oAD = poAD;
    }
/*
    public String getQueryInsertarDescuesto() throws Exception {
        String sQuery = "";
        if (this.oPaquete == null || this.oLineaIngreso == null || this.nPctDscto == 0.0) {
            throw new Exception("OtrosDsctosPaq. getQueryInsertarDescuesto(): Error de programación. Faltan datos.");
        } else {
            sQuery = "SELECT * FROM administrarotrosdesctspaq('insertar'::character varying," + this.oPaquete.getIdPaquete() + " , " + this.oLineaIngreso.getCveLin() + "::smallint, " + this.nPctDscto + "::numeric);";
        }
        return sQuery;
    }
    */
    
    public String getQueryInsertarDescuesto() throws Exception {
        String sQuery = "";
        if (this.oLineaIngreso == null || this.nPctDscto == 0.0) {
            throw new Exception("OtrosDsctosPaq. getQueryInsertarDescuesto(): Error de programación. Faltan datos.");
        } else {
            if (this.getPaquete() != null)
                sQuery = "INSERT INTO tmp_resultados SELECT * FROM administrarotrosdesctspaq("+
                    "'insertar'::character varying,"+this.getPaquete().getIdPaquete()+", " + 
                    this.oLineaIngreso.getCveLin() + "::smallint, " + 
                    this.nPctDscto + "::numeric);";
            else
                sQuery = "INSERT INTO tmp_resultados SELECT * FROM administrarotrosdesctspaq("+
                    "'insertar'::character varying,currval('paquete_nidpaquete_seq')::int, " + 
                    this.oLineaIngreso.getCveLin() + "::smallint, " + 
                    this.nPctDscto + "::numeric);";
        }
        return sQuery;
    }
    public String getQueryEliminaDescuento() throws Exception {
        String sQuery = "";
        if (this.oPaquete == null) {
            throw new Exception("OtrosDsctosPaq. getQueryEliminaDetallePaquete(): Error de programación. Faltan datos.");
        } else {
            sQuery = "SELECT * FROM administrarotrosdesctspaq('eliminar'::character varying," + this.oPaquete.getIdPaquete() + " , " + this.oLineaIngreso.getCveLin() + "::smallint, " + this.nPctDscto + "::numeric);";
        }
        return sQuery;
    }

    public List<OtrosDsctosPaq> buscaTodosDsctosPaq() throws Exception {
        List<OtrosDsctosPaq> listaDesctosPaqs = new ArrayList<OtrosDsctosPaq>();
        if (this.oPaquete == null) {
            throw new Exception("OtrosDsctosPaq. buscaTodosDsctosPaq(): Error de programación. Faltan datos.");
        } else {
            OtrosDsctosPaq oDP = null;
            Vector rst = null;
            Vector<OtrosDsctosPaq> vObj = null;

            String sQuery = "";
            int i = 0, nTam = 0;

            sQuery = "SELECT * FROM buscaLlaveotrosdsctospaq(" + oPaquete.getIdPaquete() + ");";

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
                vObj = new Vector<OtrosDsctosPaq>();
                for (i = 0; i < rst.size(); i++) {
                    Vector vRowTemp = (Vector) rst.elementAt(i);
                    oDP = new OtrosDsctosPaq();
                    oDP.setPaquete(oPaquete);
                    LineaIngreso oLI = new LineaIngreso();
                    oLI.setCveLin(((Double) vRowTemp.elementAt(2)).intValue());
                    oLI = oLI.buscaLineaIngreso();
                    oDP.setLineaIngreso(oLI);
                    oDP.setPctDscto(((Double) vRowTemp.elementAt(3)).floatValue());
                    oDP.setRegistrado(true);
                    listaDesctosPaqs.add(oDP);
                }

            }
        }
        return listaDesctosPaqs;
    }

    public AreaDeServicio getAreaServicio() {
        return oAreaServicio;
    }

    public void setAreaServicio(AreaDeServicio oAreaServicio) {
        this.oAreaServicio = oAreaServicio;
    }

    public LineaIngreso getLineaIngreso() {
        return oLineaIngreso;
    }

    public void setLineaIngreso(LineaIngreso oLineaIngreso) {
        this.oLineaIngreso = oLineaIngreso;
    }

    public Paquete getPaquete() {
        return oPaquete;
    }

    public void setPaquete(Paquete oPaquete) {
        this.oPaquete = oPaquete;
    }

    public float getPctDscto() {
        return nPctDscto;
    }

    public void setPctDscto(float nPctDscto) {
        this.nPctDscto = nPctDscto;
    }

    public AccesoDatos getAD() {
        return oAD;
    }

    public void setAD(AccesoDatos oAD) {
        this.oAD = oAD;
    }

    public boolean getRegistrado() {
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

}
