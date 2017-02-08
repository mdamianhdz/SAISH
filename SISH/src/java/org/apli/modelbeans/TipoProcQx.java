package org.apli.modelbeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import org.apli.AD.AccesoDatos;

/**
 * Cat�logo de procedimientos quir�rgicos, basado en CIE-10 PCS, considera s�lo
 * el subconjunto de elementos posibles, dado el hospital, hasta la posici�n 3
 * de la codificaci�n de claves
 *
 * @author BAOZ
 * @version 1.0
 * @created 04-ago.-2014 15:55:10
 */
public class TipoProcQx implements Serializable {

    /**
     * Clave a 7 posiciones seg�n CIE-10 PCS S�lo toma las primeras 3
     * (m�dico-quir�rgico u obst�trico, sistema org�nico, procedimiento) el
     * resto usa asteriscos para el momento en que se utilizara completo
     */
    private String sCveTipoProcQx;
    /**
     * Descripci�n corta del procedimiento quir�rgico
     */
    private String sDescripcion;
    private AccesoDatos oAD;

    public TipoProcQx(AccesoDatos poAD) {
        oAD = poAD;
    }

    public TipoProcQx() {

    }

    public void finalize() throws Throwable {

    }

    public List<TipoProcQx> buscaTodosTipoProcQx() throws Exception {
        TipoProcQx oTPX = new TipoProcQx();
        List<TipoProcQx> listaCirugias = new ArrayList<TipoProcQx>();
        Vector rst = null;
        Vector<TipoProcQx> vObj = null;
        String sQuery = "";
        sQuery = "SELECT * FROM buscaTodostipoprocqx();";
        if (this.getAD() == null) {
            this.setAD(new AccesoDatos());
            this.getAD().conectar();
            rst = this.getAD().ejecutarConsulta(sQuery);
            this.getAD().desconectar();
            setAD(null);
        } else {
            rst = this.getAD().ejecutarConsulta(sQuery);
        }
        if (rst != null && rst.size() > 0) {
            vObj = new Vector<TipoProcQx>();
            for (int i = 0; i < rst.size(); i++) {
                oTPX = new TipoProcQx();
                Vector vRowTemp = (Vector) rst.elementAt(i);
                oTPX.setCveTipoProcQx((String) vRowTemp.elementAt(0));
                oTPX.setDescripcion((String) vRowTemp.elementAt(1));
                listaCirugias.add(oTPX);
            }
        }
        return listaCirugias;
    }

    public List<TipoProcQx> buscarTodosPorFiltro(String sTipo, String sSistema,
            String sLocalizacion, String sAbordaje, String sDispositivo)
            throws Exception {
        List<TipoProcQx> listTipoProcQx = null;
        TipoProcQx oTip = null;
        Vector rst = null;
        String sQuery = "";
        int i = 0;

        sQuery = "SELECT * FROM buscaFiltrosTipoProcQx("
                + (sTipo == null || sTipo.equals("") ? "null" : "'" + sTipo + "'") + ", "
                + (sTipo == null || sSistema.equals("") ? "null" : "'" + sSistema + "'") + ", "
                + (sTipo == null || sLocalizacion.equals("") ? "null" : "'" + sLocalizacion + "'") + ", "
                + (sTipo == null || sAbordaje.equals("") ? "null" : "'" + sAbordaje + "'") + ", "
                + (sTipo == null || sDispositivo.equals("") ? "null" : "'" + sDispositivo + "'") + "); ";
        if (oAD == null) {
            oAD = new AccesoDatos();
            oAD.conectar();
            rst = oAD.ejecutarConsulta(sQuery);
            oAD.desconectar();
            oAD = null;
        } else {
            oAD.conectar();
            rst = oAD.ejecutarConsulta(sQuery);
            oAD.desconectar();
        }
        System.out.println(sQuery);
        if (rst != null && rst.size()>0) {
            listTipoProcQx=new ArrayList<TipoProcQx>();
            for (i = 0; i < rst.size(); i++) {
                oTip = new TipoProcQx();
                Vector vRowTemp = (Vector) rst.elementAt(i);
                oTip.setCveTipoProcQx((String) vRowTemp.elementAt(0));
                oTip.setDescripcion((String) vRowTemp.elementAt(1));
                listTipoProcQx.add(oTip);
            }
            
        }
        return listTipoProcQx;
    }

    public AccesoDatos getAD() {
        return oAD;
    }

    public void setAD(AccesoDatos oAD) {
        this.oAD = oAD;
    }

    public String getCveTipoProcQx() {
        return sCveTipoProcQx;
    }

    public void setCveTipoProcQx(String sCveTipoProcQx) {
        this.sCveTipoProcQx = sCveTipoProcQx;
    }

    public String getDescripcion() {
        return sDescripcion;
    }

    public void setDescripcion(String sDescripcion) {
        this.sDescripcion = sDescripcion;
    }

}
