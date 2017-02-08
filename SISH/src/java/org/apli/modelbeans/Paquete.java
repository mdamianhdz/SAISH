package org.apli.modelbeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import org.apli.AD.AccesoDatos;

/**
 * Paquete m�dico que ampara un conjunto de servicios a un monto menor que si se
 * pagara de forma individual cada uno
 *
 * @author BAOZ
 * @version 1.0
 * @created 17-jul.-2014 10:42:02
 */
public class Paquete implements Serializable {

    /**
     * Indica si el paquete est� activo (lo pueden contratar) o no
     */
    private boolean bActivo;
    /**
     * Monto del anticipo en el caso de pago por parcialidades
     */
    private float nAnticipo;
    /**
     * Cantidad de parcialidades a pagar (adem�s del anticipo)
     */
    private short nCantParcial;
    /**
     * Costo del paquete
     */
    private float nCosto;
    /**
     * Identificador del paquete, se genera de forma autom�tica
     */
    private int nIdPaquete;
    private TipoPaquete oTipoPaquete;
    /**
     * Nombre del paquete, generalmente es una copia de la descripci�n del tipo
     * pero puede variar
     */
    private String sNombre;
    /**
     * Observaciones generales del paquete (manejo de urgencias, limitaciones,
     * casos especiales de anticipos)
     */
    private String sObs;
    /**
     * T = total P = parcialidades
     */
    private String sTipoPago;
    public ConceptoIngreso m_ConceptoIngreso;
    protected AccesoDatos oAD = null;
    private boolean bContratado = false;

    public Paquete() {

    }

    public Paquete(AccesoDatos poAD) {
        oAD = poAD;
    }

    public String insertaPaquete(List<OtrosDsctosPaq> carritoDescuentos, 
            List<DetallePaquete> carritoServicios) throws Exception {
    Vector rst = null;
    String sQuery = "", sRet = "Error al guardar el paquete";
        if (this.oTipoPaquete.getCve().equals("") || this.sNombre.equals("") || 
                this.nCosto == 0.0 || this.sTipoPago.equals("") || 
                carritoServicios.size()<1) {
            throw new Exception("Paquete. insertaPaquete(): Error de programación. Faltan datos.");
        } else {
            sQuery = "SELECT * INTO TEMP tmp_resultados from administrarpaquetes('insertar'::character varying, 0, '" + 
                    this.oTipoPaquete.getCve() + "'::character, '" + 
                    this.sNombre.toUpperCase() + "'::character varying, " + this.nCosto + "::numeric, '" + 
                    this.sTipoPago + "'::character, " + this.nAnticipo + "::numeric, " + 
                    this.nCantParcial + "::smallint, " + 
                    (this.sObs==null||this.sObs.equals("")?"null":"'"+this.sObs+"'") + 
                    "::character varying);";
            for (int i = 0; i < carritoServicios.size(); i++) {
                sQuery += carritoServicios.get(i).getQueryInsertarDetallePaquete();
                System.out.println("En carrito");
            }
            for (int i = 0; i < carritoDescuentos.size(); i++) {
                sQuery += carritoDescuentos.get(i).getQueryInsertarDescuesto();
            }
            sQuery = sQuery + " SELECT * FROM tmp_resultados; ";
            if (getAD() == null) {
                setAD(new AccesoDatos());
                getAD().conectar();
                rst = getAD().ejecutarConsulta(sQuery);
                getAD().desconectar();
                setAD(null);
            }
            if (rst != null && rst.size() > 0) {
                sRet = "Ok";
            }
        }
        return sRet;
    }

    public String modificaPaquete(List<OtrosDsctosPaq> carritoDescuentos, 
                            List<OtrosDsctosPaq> carritoDescuentosRegistrados, 
                            List<DetallePaquete> carritoServicios, 
                            List<DetallePaquete> carritoServiciosRegistrados) 
            throws Exception {

        Vector rst = null;
        String sQuery = "";
        String msj = "";
        if (this.oTipoPaquete.getCve().equals("") || this.sNombre.equals("") || this.nCosto == 0.0 || this.sTipoPago.equals("") || this.sObs.equals("")) {
            throw new Exception("Paquete. insertaPaquete(): Error de programación. Faltan datos.");
        } else {

            sQuery = "SELECT * INTO TEMP tmp_resultados from administrarpaquetes('modificar'::character varying, " + 
                    this.nIdPaquete + ", '" + this.oTipoPaquete.getCve() + "'::character, '" + 
                    this.sNombre.toUpperCase() + "'::character varying, " + this.nCosto + "::numeric, '" + 
                    this.sTipoPago + "'::character, " + this.nAnticipo + "::numeric, " + 
                    this.nCantParcial + "::smallint, " + 
                    (this.sObs==null||this.sObs.equals("")?"null":"'"+this.sObs+"'") +
                    "::character varying);";
            for (int i = 0; i < carritoServiciosRegistrados.size(); i++) {
                if (carritoServiciosRegistrados.get(i).getEliminar() == true) {
                    sQuery += carritoServiciosRegistrados.get(i).getQueryEliminaDetallePaquete();
                }
            }
            for (int i = 0; i < carritoDescuentosRegistrados.size(); i++) {
                if (carritoDescuentosRegistrados.get(i).getEliminar() == true) {
                    sQuery += carritoDescuentosRegistrados.get(i).getQueryEliminaDescuento();
                }
            }
            for (int i = 0; i < carritoServicios.size(); i++) {
                Paquete oPaq = new Paquete();
                oPaq.setIdPaquete(nIdPaquete);
                carritoServicios.get(i).setPaquete(oPaq);
                sQuery += carritoServicios.get(i).getQueryInsertarDetallePaquete();
            }
            for (int i = 0; i < carritoDescuentos.size(); i++) {
                Paquete oPaq = new Paquete();
                oPaq.setIdPaquete(nIdPaquete);
                carritoDescuentos.get(i).setPaquete(oPaq);
                sQuery += carritoDescuentos.get(i).getQueryInsertarDescuesto();
            }
            sQuery = sQuery + " SELECT * FROM tmp_resultados; ";
            if (getAD() == null) {
                setAD(new AccesoDatos());
                getAD().conectar();
                rst = getAD().ejecutarConsulta(sQuery);
            }
            if (rst != null && rst.size() > 0) {
                System.out.println("M:" + rst.get(0));
                msj = "" + rst.get(0);
                if (msj.contains("ERROR")) {
                    msj = "¡ERROR " + rst.get(0);
                    throw new Exception("Paquete. modificaPaquete(): Error al insertar detalle al paquete.Detalle:" + rst.get(0));
                } else {
                    msj = "¡El paquete: " + this.getNombre() + " ha sido actualizado exitosamente!";
                }
            }
            getAD().desconectar();
            setAD(null);
        }
        return msj;
    }

    public List<Paquete> buscaTodosPaquetes() throws Exception {
        List<Paquete> listaPaqs = new ArrayList<Paquete>();
        Paquete oP = null;
        Vector rst = null;
        Vector<Paquete> vObj = null;

        String sQuery = "";
        int i = 0, nTam = 0;

        sQuery = "SELECT * FROM buscatodospaquetes();";

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
            vObj = new Vector<Paquete>();
            for (i = 0; i < rst.size(); i++) {
                oP = new Paquete();
                Vector vRowTemp = (Vector) rst.elementAt(i);

                oP.setIdPaquete(((Double) vRowTemp.elementAt(0)).intValue());
                oP.setActivo(((String) vRowTemp.elementAt(1)).equals("1"));
                TipoPaquete oTP = new TipoPaquete();
                oTP.setCve((String) vRowTemp.elementAt(2));
                if (((String) vRowTemp.elementAt(2)).equals("0")) {
                    oTP.setDescrip("Pediátrico");
                }
                if (((String) vRowTemp.elementAt(2)).equals("1")) {
                    oTP.setDescrip("Maternidad");
                }
                if (((String) vRowTemp.elementAt(2)).equals("2")) {
                    oTP.setDescrip("Quirúrgico");
                }
                if (((String) vRowTemp.elementAt(2)).equals("3")) {
                    oTP.setDescrip("Personalizado");
                }
                oP.setTipoPaquete(oTP);
                oP.setNombre((String) vRowTemp.elementAt(3));
                oP.setCosto(((Double) vRowTemp.elementAt(4)).floatValue());
                oP.setTipoPago((String) vRowTemp.elementAt(5));
                oP.setAnticipo(((Double) vRowTemp.elementAt(6)).floatValue());
                oP.setCantParcial(((Double) vRowTemp.elementAt(7)).shortValue());
                oP.setObs((String) vRowTemp.elementAt(8));
                oP.setContratado(this.esPaqueteContrado(oP.getIdPaquete()));
                listaPaqs.add(oP);

            }

        }

        return listaPaqs;
    }

    public List<Paquete> buscaTodosPaquetesTipo() throws Exception {
        List<Paquete> listaPaqs = new ArrayList<Paquete>();

        Paquete oP = null;
        Vector rst = null;
        Vector<Paquete> vObj = null;

        String sQuery = "";
        int i = 0;

        if (this.oTipoPaquete.getCve().equals("")) {
            throw new Exception("Paquete. buscaTodosPaquetesTipo(): Error de programación. Faltan datos.");
        } else {

            sQuery = "SELECT * FROM buscaTodosPaquetesTipo('" + this.oTipoPaquete.getCve() + "'::CHARACTER(1));";

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
                vObj = new Vector<Paquete>();
                for (i = 0; i < rst.size(); i++) {
                    oP = new Paquete();
                    Vector vRowTemp = (Vector) rst.elementAt(i);

                    oP.setIdPaquete(((Double) vRowTemp.elementAt(0)).intValue());
                    oP.setActivo(((String) vRowTemp.elementAt(1)).equals("1"));
                    TipoPaquete oTP = new TipoPaquete();
                    oTP.setCve((String) vRowTemp.elementAt(2));
                    if (((String) vRowTemp.elementAt(2)).equals("0")) {
                        oTP.setDescrip("Pediátrico");
                    }
                    if (((String) vRowTemp.elementAt(2)).equals("1")) {
                        oTP.setDescrip("Maternidad");
                    }
                    if (((String) vRowTemp.elementAt(2)).equals("2")) {
                        oTP.setDescrip("Quirúrgico");
                    }
                    if (((String) vRowTemp.elementAt(2)).equals("3")) {
                        oTP.setDescrip("Personalizado");
                    }
                    oP.setTipoPaquete(oTP);
                    oP.setNombre((String) vRowTemp.elementAt(3));
                    oP.setCosto(((Double) vRowTemp.elementAt(4)).floatValue());
                    oP.setTipoPago((String) vRowTemp.elementAt(5));
                    oP.setAnticipo(((Double) vRowTemp.elementAt(6)).floatValue());
                    oP.setCantParcial(((Double) vRowTemp.elementAt(7)).shortValue());
                    oP.setObs((String) vRowTemp.elementAt(8));
                    oP.setContratado(this.esPaqueteContrado(oP.getIdPaquete()));
                    listaPaqs.add(oP);

                }

            }
        }
        return listaPaqs;
    }

    public boolean esPaqueteContrado(int nIdPaq) throws Exception {
    Vector rst = null;

        String sQuery = "";
        int i = 0, nTam = 0;

        sQuery = "SELECT * FROM buscasipaquetecontratado(" + nIdPaq + ");";

        if (getAD() == null) {
            setAD(new AccesoDatos());
            getAD().conectar();
            rst = getAD().ejecutarConsulta(sQuery);
            getAD().desconectar();
            setAD(null);
        } else {
            rst = getAD().ejecutarConsulta(sQuery);
        }
        if (rst != null && rst.size() > 0) {
            if ((new Valida().eliminaMSJCorchetes("" + rst.get(0))).contains("1")) {
                bContratado = true;
            } else {
                bContratado = false;
            }
        }
        return bContratado;
    }

    public String desactivarPaquete() throws Exception {

        Vector rst = null;
        String msj = "";

        String sQuery = "";
        int i = 0, nTam = 0;

        sQuery = "SELECT * from administrarpaquetes('desactivar'::character varying, " + this.nIdPaquete + ", '" + this.oTipoPaquete.getCve() + "'::character, '" + this.sNombre + "'::character varying, " + this.nCosto + "::numeric, '" + this.sTipoPago + "'::character, " + this.nAnticipo + "::numeric, " + this.nCantParcial + "::smallint, '" + this.sObs + "'::character varying);";

        if (getAD() == null) {
            setAD(new AccesoDatos());
            getAD().conectar();
            rst = getAD().ejecutarConsulta(sQuery);
            getAD().desconectar();
            setAD(null);
        } else {
            rst = getAD().ejecutarConsulta(sQuery);
        }
        if (rst != null && rst.size() > 0) {
            msj = "" + rst.get(0);
            if (msj.contains("ERROR")) {
                msj = "¡ERROR " + rst.get(0);
                throw new Exception("Paquete. modificaPaquete(): Error al insertar detalle al paquete.Detalle:" + rst.get(0));
            }
        }
        return msj;
    }

    public String activarPaquete() throws Exception {

        Vector rst = null;
        String msj = "";

        String sQuery = "";
        int i = 0, nTam = 0;

        sQuery = "SELECT * from administrarpaquetes('activar'::character varying, " + this.nIdPaquete + ", '" + this.oTipoPaquete.getCve() + "'::character, '" + this.sNombre + "'::character varying, " + this.nCosto + "::numeric, '" + this.sTipoPago + "'::character, " + this.nAnticipo + "::numeric, " + this.nCantParcial + "::smallint, '" + this.sObs + "'::character varying);";

        if (getAD() == null) {
            setAD(new AccesoDatos());
            getAD().conectar();
            rst = getAD().ejecutarConsulta(sQuery);
            getAD().desconectar();
            setAD(null);
        } else {
            rst = getAD().ejecutarConsulta(sQuery);
        }
        if (rst != null && rst.size() > 0) {
            msj = "" + rst.get(0);
            if (msj.contains("ERROR")) {
                msj = "¡ERROR " + rst.get(0);
                throw new Exception("Paquete. modificaPaquete(): Error al insertar detalle al paquete.Detalle:" + rst.get(0));
            }
        }
        return msj;
    }

    public boolean getActivo() {
        return bActivo;
    }

    public void setActivo(boolean bActivo) {
        this.bActivo = bActivo;
    }

    public float getAnticipo() {
        return nAnticipo;
    }

    public void setAnticipo(float nAnticipo) {
        this.nAnticipo = nAnticipo;
    }

    public short getCantParcial() {
        return nCantParcial;
    }

    public void setCantParcial(short nCantParcial) {
        this.nCantParcial = nCantParcial;
    }

    public float getCosto() {
        return nCosto;
    }

    public void setCosto(float nCosto) {
        this.nCosto = nCosto;
    }

    public int getIdPaquete() {
        return nIdPaquete;
    }

    public void setIdPaquete(int nIdPaquete) {
        this.nIdPaquete = nIdPaquete;
    }

    public TipoPaquete getTipoPaquete() {
        return oTipoPaquete;
    }

    public void setTipoPaquete(TipoPaquete oTipoPaquete) {
        this.oTipoPaquete = oTipoPaquete;
    }

    public String getNombre() {
        return sNombre;
    }

    public void setNombre(String sNombre) {
        this.sNombre = sNombre;
    }

    public String getObs() {
        return sObs;
    }

    public void setObs(String sObs) {
        this.sObs = sObs;
    }

    public String getTipoPago() {
        return sTipoPago;
    }

    public void setTipoPago(String sTipoPago) {
        this.sTipoPago = sTipoPago;
    }

    public ConceptoIngreso getConceptoIngreso() {
        return m_ConceptoIngreso;
    }

    public void setConceptoIngreso(ConceptoIngreso m_ConceptoIngreso) {
        this.m_ConceptoIngreso = m_ConceptoIngreso;
    }

    public AccesoDatos getAD() {
        return oAD;
    }

    public void setAD(AccesoDatos oAD) {
        this.oAD = oAD;
    }

    public boolean getContratado() {
        return bContratado;
    }

    public void setContratado(boolean bContratado) {
        this.bContratado = bContratado;
    }

}
