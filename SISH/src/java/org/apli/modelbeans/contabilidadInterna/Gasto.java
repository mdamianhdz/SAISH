/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apli.modelbeans.contabilidadInterna;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import org.apli.AD.AccesoDatos;
import org.apli.modelbeans.AreaDeServicio;
import org.apli.modelbeans.AreaFisica;
import org.apli.modelbeans.ConceptoEgreso;
import org.apache.log4j.Logger;

/**
 *
 * @author Lily_LnBnd
 */
public class Gasto implements Serializable {

    private int nIdGasto;
    private String sDescripcion;
    private float nMonto;
    private String sFactura;
    private String sXML;
    private Proveedor oProv;
    private ConceptoEgreso oConcEgreso;
    private AreaFisica oAreaFis;
    private AreaDeServicio oAreaServ;
    private char cSituacion; //'0 = Pendiente de Pago; 1 = Pago autorizado; 2 = Pagado'
    private String sFormaPago;
    private MovimientoCtaBan oMovCtaBan;
    private Date dRegistro;
    private Date dPropPago;
    private Date dPago;
    private AccesoDatos oAD;
    private final String sRutaXML = "//BovedaFiscal//Proveedores//";
    private static final Logger LOG = Logger.getLogger(Gasto.class.getName());

    public Gasto() {
        oProv = new Proveedor();
        oConcEgreso = new ConceptoEgreso();
        oAreaFis = new AreaFisica();
        oAreaServ = new AreaDeServicio();
        oMovCtaBan = new MovimientoCtaBan();
    }

    public List<Gasto> buscaGastos(String sCondicion) throws Exception {
        Gasto oGasto = null;
        List<Gasto> listRet = null;
        Vector rst = null;
        String sQuery = "";

        sQuery = "select * from buscaGastos()";
        if (!sCondicion.equals("")) {
            sQuery = sQuery + " where " + sCondicion;
        }
        if (getAD() == null) {
            setAD(new AccesoDatos());
            getAD().conectar();
            rst = getAD().ejecutarConsulta(sQuery);
            getAD().desconectar();
            setAD(null);
        }
        if (rst != null) {
            listRet = new ArrayList();
            for (int i = 0; i < rst.size(); i++) {
                Vector vRowTemp = (Vector) rst.elementAt(i);
                oGasto = new Gasto();
                oGasto.setIdGasto(((Double) vRowTemp.elementAt(0)).intValue());
                oGasto.setDescripcion((String) vRowTemp.elementAt(1));
                oGasto.setMonto(((Double) vRowTemp.elementAt(2)).floatValue());
                oGasto.setFactura((String) vRowTemp.elementAt(3));
                oGasto.setXML((String) vRowTemp.elementAt(4));
                oGasto.setSituacion(((String) vRowTemp.elementAt(5)).charAt(0));
                oGasto.getProv().setIdProv(((Double) vRowTemp.elementAt(6)).intValue());
                oGasto.getProv().setNombreRazSoc(((String) vRowTemp.elementAt(7)));
                oGasto.getConcEgreso().setCveConcepEgr(((Double) vRowTemp.elementAt(8)).intValue());
                oGasto.getConcEgreso().setDescripcion(((String) vRowTemp.elementAt(9)));
                oGasto.getConcEgreso().getSublineaEgreso().setCveSublineaEgre(((Double) vRowTemp.elementAt(10)).intValue());
                oGasto.getConcEgreso().getSublineaEgreso().setDescripcion(((String) vRowTemp.elementAt(11)));
                oGasto.getConcEgreso().getSublineaEgreso().getLineaEgre().setCveLineaEgr(((Double) vRowTemp.elementAt(12)).intValue());
                oGasto.getConcEgreso().getSublineaEgreso().getLineaEgre().setDescripcion(((String) vRowTemp.elementAt(13)));
                oGasto.getAreaServ().setCve("" + ((Double) vRowTemp.elementAt(14)).intValue());
                oGasto.getAreaServ().setDescrip(((String) vRowTemp.elementAt(15)));
                oGasto.getAreaFis().setCve(((Double) vRowTemp.elementAt(16)).intValue());
                oGasto.getAreaFis().setDescrip(((String) vRowTemp.elementAt(17)));
                oGasto.setFormaPago(((String) vRowTemp.elementAt(18)));
                oGasto.setRegistro(((Date) vRowTemp.elementAt(19)));

                listRet.add(oGasto);
            }
        }
        return listRet;
    }

    public void insertaGastoCompra(String sdescrip, float nmonto, int nidprov, int ncveconcepegr, int ncveareaserv, int ncveareafis, String nfolio) throws Exception {
    Vector rst = null;
    String sQuery = "";
    String ssituacion = "2";
        setPago(new Date());
        setPropPago(new Date());
        setRegistro(new Date());
        sQuery = "select * from insertagastocompra('" + sdescrip + "'," + 
                nmonto + "," + nidprov + "," + ncveconcepegr + "," + 
                ncveareaserv + "," + ncveareafis + ",'" + ssituacion + "','" + 
                this.getRegistro() + "'::timestamp without time zone,'" + 
                this.getPropPago() + "'::date,'" + 
                this.getPago() + "'::timestamp without time zone,'" + nfolio + "')";
        LOG.trace(new StringBuilder().append("Query: [").append(sQuery).append("]").toString());
        if (getAD() == null) {
            setAD(new AccesoDatos());
            getAD().conectar();
            rst = getAD().ejecutarConsulta(sQuery);
            getAD().desconectar();
            setAD(null);
        } else {
            rst = getAD().ejecutarConsulta(sQuery);
        }

    }

    public String guardaGasto(Gasto oGasto) throws Exception {
        Vector rst = null;
        String sQuery = "", sRet = "";

        if (oGasto == null) {
            throw new Exception("Gasto.guarda: error de programación, faltan datos");
        } else {
            sQuery = "select * from insertaGasto('" + oGasto.getDescripcion() + "', " + oGasto.getMonto() + ", '" + oGasto.getFactura() + "', '" + oGasto.getXML()
                    + "', " + oGasto.getProv().getIdProv() + "::int2, " + oGasto.getConcEgreso().getCveConcepEgr() + ", " + oGasto.getAreaServ().getCve()
                    + "::int2, " + oGasto.getAreaFis().getCve() + "::int2, '" + oGasto.getSituacion() + "')";
            if (getAD() == null) {
                setAD(new AccesoDatos());
                getAD().conectar();
                rst = getAD().ejecutarConsulta(sQuery);
                getAD().desconectar();
                setAD(null);
            } else {
                rst = getAD().ejecutarConsulta(sQuery);
            }
            if (rst != null) //trae id gasto
                sRet = rst.get(0).toString();
        }
        return sRet;
    }

    public String modificaGasto(Gasto oGasto) throws Exception {
        Vector rst = null;
        String sQuery = "", sRet = "";

        if (oGasto == null) {
            throw new Exception("Gasto.guarda: error de programación, faltan datos");
        } else {
            sQuery = "select * from modificaGasto(" + oGasto.getIdGasto() + ",'" + oGasto.getDescripcion() + "', " + oGasto.getMonto() + ", '" + oGasto.getFactura() + "', '" + oGasto.getXML()
                    + "', " + oGasto.getProv().getIdProv() + "::int2, " + oGasto.getConcEgreso().getCveConcepEgr() + ", " + oGasto.getAreaServ().getCve()
                    + "::int2, " + oGasto.getAreaFis().getCve() + "::int2, '" + oGasto.getSituacion() + "')";
            if (getAD() == null) {
                setAD(new AccesoDatos());
                getAD().conectar();
                rst = getAD().ejecutarConsulta(sQuery);
                getAD().desconectar();
                setAD(null);
            } else {
                rst = getAD().ejecutarConsulta(sQuery);
            }
            if (rst != null)
                sRet = rst.get(0).toString().substring(1, rst.get(0).toString().length() - 1);
        }
        return sRet; 
    }

    public String recibirFactura(Gasto oGasto) throws Exception {
        Vector rst = null;
        String sQuery = "", sRet = "";

        if (oGasto == null) {
            throw new Exception("Gasto.recibirFactura: error de programación, faltan datos");
        } else {
            sQuery = "select * from recibirFacturaProv(" + oGasto.getIdGasto() + ", '" + oGasto.getFactura() + "', '" + oGasto.getXML() + "');";
            System.out.println(sQuery);
            if (getAD() == null) {
                setAD(new AccesoDatos());
                getAD().conectar();
                rst = getAD().ejecutarConsulta(sQuery);
                getAD().desconectar();
                setAD(null);
            } else {
                rst = getAD().ejecutarConsulta(sQuery);
            }
            if (rst != null)
                sRet = rst.get(0).toString().substring(1, rst.get(0).toString().length() - 1);
        }
        return sRet;
    }

    public String eliminaGasto(Gasto oGasto) throws Exception {
        Vector rst = null;
        String sQuery = "", sRet = "";

        if (oGasto == null) {
            throw new Exception("Gasto.eliminaGasto: error de programación, faltan datos");
        } else {
            sQuery = "select * from eliminaGasto(" + oGasto.getIdGasto() + ")";
            System.out.println(sQuery);
            if (getAD() == null) {
                setAD(new AccesoDatos());
                getAD().conectar();
                rst = getAD().ejecutarConsulta(sQuery);
                getAD().desconectar();
                setAD(null);
            } else {
                rst = getAD().ejecutarConsulta(sQuery);
            }
        }
        return "" + rst.get(0).toString().substring(1, rst.get(0).toString().length() - 1);
    }

    public int buscaIdGasto() throws Exception {
        int nRet = 0;
        Vector rst = null;
        String sQuery = "";

        sQuery = "select max(nidgasto) from gasto";
        System.out.println(sQuery);
        if (getAD() == null) {
            setAD(new AccesoDatos());
            getAD().conectar();
        }
        rst = getAD().ejecutarConsulta(sQuery);
        getAD().desconectar();
        setAD(null);
        if (rst != null) {
            Vector vRowTemp = (Vector) rst.elementAt(0);
            nRet = (((Double) vRowTemp.elementAt(0)).intValue());
        }
        return nRet;
    }

    public List<Gasto> buscaCheques(String sCondicion) throws Exception {
        Gasto oGasto = null;
        List<Gasto> listRet = null;
        Vector rst = null;
        String sQuery = "";

        sQuery = "select * from buscaCheques()";
        if (!sCondicion.equals("")) {
            sQuery = sQuery + " where " + sCondicion;
        }
        System.out.println(sQuery);
        if (getAD() == null) {
            setAD(new AccesoDatos());
            getAD().conectar();
        }
        rst = getAD().ejecutarConsulta(sQuery);
        getAD().desconectar();
        setAD(null);
        if (rst != null) {
            listRet = new ArrayList();
            for (int i = 0; i < rst.size(); i++) {
                Vector vRowTemp = (Vector) rst.elementAt(i);

                oGasto = new Gasto();
                oGasto.setIdGasto(((Double) vRowTemp.elementAt(0)).intValue());
                oGasto.getMovCtaBan().setNumDocto((String) vRowTemp.elementAt(1));
                oGasto.setPropPago((Date) vRowTemp.elementAt(2));
                oGasto.setDescripcion((String) vRowTemp.elementAt(3));
                oGasto.setMonto(((Double) vRowTemp.elementAt(4)).floatValue());
                oGasto.getProv().setIdProv(((Double) vRowTemp.elementAt(5)).intValue());

                listRet.add(oGasto);
            }
        }
        return listRet;
    }

    public String entregarCheques(List<Gasto> listCheques) throws Exception {
        Vector rst = null;
        String sQuery = "", sRet = "";

        if (listCheques == null) {
            throw new Exception("Gasto.guarda: error de programación, faltan datos");
        } else {
            for (int i = 0; i < listCheques.size(); i++) {
                sQuery = sQuery + "select * from entregarCheque(" + listCheques.get(i).getIdGasto() + ");\n";
            }
            if (getAD() == null) {
                setAD(new AccesoDatos());
                getAD().conectar();
                rst = getAD().ejecutarConsulta(sQuery);
                getAD().desconectar();
                setAD(null);
            } else {
                rst = getAD().ejecutarConsulta(sQuery);
            }
        }
        System.out.println("'" + rst.get(0).toString().substring(1, rst.get(0).toString().length() - 1) + "'");
        if ("Se han marcado el cheque como entregado".equals(rst.get(0).toString().substring(1, rst.get(0).toString().length() - 1))) {
            sRet = "OK";
        } else if ("Multiple ResultSets were returned by the query".equals(rst.get(0).toString().substring(1, rst.get(0).toString().length() - 1))) {
            sRet = "OK";
        } else {
            sRet = "ERROR";
        }
        return sRet;
    }

    public List<Gasto> buscaGastosXML(String sCondicion) throws Exception {
        Gasto oGasto = null;
        List<Gasto> listRet = null;
        Vector rst = null;
        String sQuery = "";

        sQuery = "select * from buscaGastosXML()";
        if (!sCondicion.equals("")) {
            sQuery = sQuery + " where " + sCondicion;
        }
        System.out.println(sQuery);
        if (getAD() == null) {
            setAD(new AccesoDatos());
            getAD().conectar();
        }
        rst = getAD().ejecutarConsulta(sQuery);
        getAD().desconectar();
        setAD(null);
        if (rst != null) {
            listRet = new ArrayList();
            for (int i = 0; i < rst.size(); i++) {
                Vector vRowTemp = (Vector) rst.elementAt(i);

                oGasto = new Gasto();
                oGasto.setIdGasto(((Double) vRowTemp.elementAt(0)).intValue());
                oGasto.setDescripcion((String) vRowTemp.elementAt(1));
                oGasto.setRegistro((Date) vRowTemp.elementAt(2));
                oGasto.setMonto(((Double) vRowTemp.elementAt(3)).floatValue());
                oGasto.setFactura((String) vRowTemp.elementAt(4));
                oGasto.getProv().setIdProv(((Double) vRowTemp.elementAt(5)).intValue());
                oGasto.getProv().setNombreRazSoc(((String) vRowTemp.elementAt(6)));
                oGasto.setSituacion(((String) vRowTemp.elementAt(7)).charAt(0));
                oGasto.getConcEgreso().setCveConcepEgr(((Double) vRowTemp.elementAt(8)).intValue());
                oGasto.getConcEgreso().setDescripcion(((String) vRowTemp.elementAt(9)));

                listRet.add(oGasto);
            }
        }
        return listRet;
    }

    public List<Gasto> buscaBovedaProveedores(String sCondicion) throws Exception {
        Gasto oGasto = null;
        List<Gasto> listRet = null;
        Vector rst = null;
        String sQuery = "";

        sQuery = "select * from buscaBovedaProveedores()";
        if (!sCondicion.equals("")) {
            sQuery = sQuery + " where " + sCondicion;
        }
        if (getAD() == null) {
            setAD(new AccesoDatos());
            getAD().conectar();
        }
        rst = getAD().ejecutarConsulta(sQuery);
        getAD().desconectar();
        setAD(null);
        if (rst != null) {
            listRet = new ArrayList();
            for (int i = 0; i < rst.size(); i++) {
                Vector vRowTemp = (Vector) rst.elementAt(i);

                oGasto = new Gasto();
                oGasto.setIdGasto(((Double) vRowTemp.elementAt(0)).intValue());
                oGasto.setFactura((String) vRowTemp.elementAt(1));
                oGasto.setRegistro((Date) vRowTemp.elementAt(2));
                oGasto.setMonto(((Double) vRowTemp.elementAt(3)).floatValue());
                oGasto.setXML((String) vRowTemp.elementAt(4));
                oGasto.getProv().setIdProv(((Double) vRowTemp.elementAt(0)).intValue());

                listRet.add(oGasto);
            }
        }
        return listRet;
    }

    public int getIdGasto() {
        return nIdGasto;
    }

    public void setIdGasto(int nIdGasto) {
        this.nIdGasto = nIdGasto;
    }

    public String getDescripcion() {
        return sDescripcion;
    }

    public void setDescripcion(String sDescripcion) {
        this.sDescripcion = sDescripcion;
    }

    public float getMonto() {
        return nMonto;
    }

    public void setMonto(float nMonto) {
        this.nMonto = nMonto;
    }

    public String getFactura() {
        return sFactura;
    }

    public void setFactura(String sFactura) {
        this.sFactura = sFactura;
    }

    public String getXML() {
        return sXML;
    }

    public void setXML(String sXML) {
        this.sXML = sXML;
    }

    public Proveedor getProv() {
        return oProv;
    }

    public void setProv(Proveedor oProv) {
        this.oProv = oProv;
    }

    public ConceptoEgreso getConcEgreso() {
        return oConcEgreso;
    }

    public void setConcEgreso(ConceptoEgreso oConcEgreso) {
        this.oConcEgreso = oConcEgreso;
    }

    public AreaFisica getAreaFis() {
        return oAreaFis;
    }

    public void setAreaFis(AreaFisica oAreaFis) {
        this.oAreaFis = oAreaFis;
    }

    public AreaDeServicio getAreaServ() {
        return oAreaServ;
    }

    public void setAreaServ(AreaDeServicio oAreaServ) {
        this.oAreaServ = oAreaServ;
    }

    public char getSituacion() {
        return cSituacion;
    }

    public void setSituacion(char cSituacion) {
        this.cSituacion = cSituacion;
    }

    public String getFormaPago() {
        return sFormaPago;
    }

    public void setFormaPago(String sFormaPago) {
        this.sFormaPago = sFormaPago;
    }

    public AccesoDatos getAD() {
        return oAD;
    }

    public void setAD(AccesoDatos oAD) {
        this.oAD = oAD;
    }

    public Date getRegistro() {
        return dRegistro;
    }

    public void setRegistro(Date dRegistro) {
        this.dRegistro = dRegistro;
    }

    public Date getPropPago() {
        return dPropPago;
    }

    public void setPropPago(Date dPropPago) {
        this.dPropPago = dPropPago;
    }

    public Date getPago() {
        return dPago;
    }

    public void setPago(Date dPago) {
        this.dPago = dPago;
    }

    public MovimientoCtaBan getMovCtaBan() {
        return oMovCtaBan;
    }

    public void setMovCtaBan(MovimientoCtaBan oMovCtaBan) {
        this.oMovCtaBan = oMovCtaBan;
    }

    public String getRutaXML() {
        return sRutaXML;
    }
}
