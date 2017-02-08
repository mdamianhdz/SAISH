/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apli.modelbeans;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import org.apli.AD.AccesoDatos;
import org.apli.modelbeans.facturacion.cfdi.FacturaCFI;
import org.apli.modelbeans.ventaCredito.CompaniaCred;

/**
 *
 * @author Lily_LnBnd
 */
public class ContraRecibo implements Serializable {

    private int nNumContraRecibo;
    private Date dFecVencimiento;
    private String sCompania;
    private String sRecibioFactura;
    private Date dFecRecepcion;
    private Date dFecProbPago;
    private String sObservaciones;
    private String sGuiaEnvio;
    private List<Llamada> listLlamadas;
    private List<FacturaCFI> listFacturas;
    private AccesoDatos oAD;
    private FacturaCFI oFactura;
    private CompaniaCred oCompaniaCred;
    private Paciente oPaciente;
    private CompaniaCred oCompCred;

    public ContraRecibo(){
        listLlamadas = new ArrayList();
        listFacturas = new ArrayList();
    }

    public ContraRecibo(AccesoDatos poAD) {
        oAD = poAD;
    }

    public List<ContraRecibo> buscaFacturasAIncobrables(String tipo, String idpac, String rfc, Date fechaini, Date fechafin) throws Exception {

        List<ContraRecibo> listRet = new ArrayList<ContraRecibo>();
        ContraRecibo oCR;

        Vector rst = null;
        String sQuery = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd h:mm:ss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        if (tipo.equals("1")) {
            sQuery = "SELECT * FROM buscaTodosFacturasparaIncobrablesEmpr(1,null,'" + rfc + "'::character varying,'" + sdf.format(fechaini) + "'::timestamp without time zone,'" + sdf2.format(fechafin) + " 23:59:59'::timestamp without time zone) ";
        } else {
            sQuery = "SELECT * FROM buscaTodosFacturasparaIncobrablesEmpr(2," + idpac + ",''::character varying,'" + sdf.format(fechaini) + "'::timestamp without time zone,'" + sdf2.format(fechafin) + " 23:59:59' ::timestamp without time zone) ";
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
        System.out.println("sQueryCI:"+sQuery);
         System.out.println("rst:"+rst);
        if (rst != null && rst.size()>0) {
            listRet = new ArrayList();
            for (int i = 0; i < rst.size(); i++) {
                oCR = new ContraRecibo();
                Vector vRowTemp = (Vector) rst.elementAt(i);
                oCR.setCompania((String) vRowTemp.elementAt(0));

                FacturaCFI of = new FacturaCFI();
                of.setExpedicion((Date) vRowTemp.elementAt(1));
                of.setFolio(((Double) vRowTemp.elementAt(2)).intValue());
                of.setNombreSerie((String) vRowTemp.elementAt(3));
                of.setRfcReceptor((String) vRowTemp.elementAt(4));

                Paciente op = new Paciente();
                op.setApellidoPaterno((String) vRowTemp.elementAt(5));
                op.setApellidoMaterno((String) vRowTemp.elementAt(6));
                op.setNombre((String) vRowTemp.elementAt(7));

                of.setPaciente(op);
                of.setImporteTotal(((Double) vRowTemp.elementAt(8)).floatValue());

                oCR.setFactura(of);
                oCR.setNumContraRecibo(((Double) vRowTemp.elementAt(9)).intValue());
                oCR.setFecRecepcion((Date) vRowTemp.elementAt(10));
                oCR.setFecProbPago((Date) vRowTemp.elementAt(11));
                oCR.setObservaciones((String) vRowTemp.elementAt(12));

                listRet.add(oCR);
            }
        }
        return listRet;

    }

    public String marcarIncobrable(int nFolio, String sNombreSerie) throws Exception {
        boolean validacion = false;
        Vector rst = null;
        String sQuery = "";
        String sMedico = "";

        if (sNombreSerie.equals("") || nFolio == 0) {
            throw new Exception("Funcion.marcarIncobrable: error de programación, faltan datos");
        } else {

            sQuery = "SELECT * FROM marcarincobrable(" + nFolio + "::int2,'" + sNombreSerie + "');";


            if (getAD() == null) {
                setAD(new AccesoDatos());
                getAD().conectar();
                rst = getAD().ejecutarConsulta(sQuery);
                getAD().desconectar();
                setAD(null);
            }
        }
        return " " + rst.get(0);
    }

    public ContraRecibo generaContraRecibo(FacturaCFI[] arrFac) throws Exception {
        ContraRecibo oCR = new ContraRecibo();
        Vector rst = null;
        String sQuery = "";

        sQuery = "select * from guardaContrarecibo('" + arrFac[0].getReceptor().getRfc() + "');";
        System.out.println(sQuery);
        if (this.getAD() == null) {
            this.setAD(new AccesoDatos());
            this.getAD().conectar();
            rst = this.getAD().ejecutarConsulta(sQuery);
        } else {
            rst = this.getAD().ejecutarConsulta(sQuery);

        }

        if (rst != null && rst.size() == 1) {
            Vector vRowTemp = (Vector) rst.elementAt(0);
            oCR = new ContraRecibo();
            oCR.setNumContraRecibo(((Double) vRowTemp.elementAt(0)).intValue());
            oCR.setFecVencimiento((Date) vRowTemp.elementAt(1));
        }
        for (int i = 0; i < arrFac.length; i++) {
            sQuery = "select * from guardaFactContra(" + oCR.getNumContraRecibo()
                    + ",'" + arrFac[i].getNombreSerie() + "'," + arrFac[i].getFolio() + "::int2);";
            rst = this.getAD().ejecutarConsulta(sQuery);
        }
        sQuery = "select * from modificaEpCiaCred(" + arrFac[0].getEpCiaCred().getPaciente().getFolioPac()
                + "," + arrFac[0].getEpCiaCred().getEpisodioM().getCveepisodio() + " ,"
                + arrFac[0].getEpCiaCred().getCompCred().getIdEmp() + "::int2, '"
                + arrFac[0].getEpCiaCred().getNumPoliza() + "','" + arrFac[0].getEpCiaCred().getNumSiniestro() + "')";
        System.out.println(sQuery);
        rst = this.getAD().ejecutarConsulta(sQuery);
        this.getAD().desconectar();
        setAD(null);
        return oCR;
    }

    public List<ContraRecibo> buscaEnProceso() throws Exception {
        List<ContraRecibo> listRet = null;
        ContraRecibo oCR;
        CompaniaCred oCC;

        Vector rst = null;
        String sQuery = "";

        sQuery = "select * from buscaContraRecibos()";
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
            listRet = new ArrayList();
            for (int i = 0; i < rst.size(); i++) {
                oCR = new ContraRecibo();
                Vector vRowTemp = (Vector)rst.elementAt(i);
                oCR.setNumContraRecibo(((Double) vRowTemp.elementAt(0)).intValue());
                oCR.setFecVencimiento((Date)vRowTemp.elementAt(1));
                oCR.setRecibioFactura((String)vRowTemp.elementAt(2));
                oCR.setFecRecepcion((Date)vRowTemp.elementAt(3));
                oCR.setFecProbPago((Date)vRowTemp.elementAt(4));
                oCR.setObservaciones((String)vRowTemp.elementAt(5));
                oCC=new CompaniaCred();
                oCC.setNombreCorto((String)vRowTemp.elementAt(6));
                oCR.setCompCred(oCC);
                oCR.setGuiaEnvio((String)vRowTemp.elementAt(7));
                listRet.add(oCR);
            }
        }
        return listRet;
    }

    public List<ContraRecibo> buscaEnProceso(int nNumCR) throws Exception {
        List<ContraRecibo> listRet = null;
        ContraRecibo oCR;
        CompaniaCred oCC;

        Vector rst = null;
        String sQuery = "";

        sQuery = "select * from buscaContraRecibos(" + nNumCR + ")";
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
            listRet = new ArrayList();
            for (int i = 0; i < rst.size(); i++) {
                oCR = new ContraRecibo();
                Vector vRowTemp = (Vector)rst.elementAt(i);
                oCR.setNumContraRecibo(((Double) vRowTemp.elementAt(0)).intValue());
                oCR.setFecVencimiento((Date)vRowTemp.elementAt(1));
                oCR.setRecibioFactura((String)vRowTemp.elementAt(2));
                oCR.setFecRecepcion((Date)vRowTemp.elementAt(3));
                oCR.setFecProbPago((Date)vRowTemp.elementAt(4));
                oCR.setObservaciones((String)vRowTemp.elementAt(5));
                oCC=new CompaniaCred();
                oCC.setNombreCorto((String)vRowTemp.elementAt(6));
                oCR.setCompCred(oCC);
                oCR.setGuiaEnvio((String)vRowTemp.elementAt(7));
                listRet.add(oCR);
            }
        }
        return listRet;
    }

    public List<ContraRecibo> buscaEnProceso(int nTipo, String sCompania) throws Exception {
        List<ContraRecibo> listRet = null;
        ContraRecibo oCR;
        CompaniaCred oCC;

        Vector rst = null;
        String sQuery = "";

        sQuery = "select * from buscaContraRecibos(" + nTipo + "::int2, '" + sCompania + "')";
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
            listRet = new ArrayList();
            for (int i = 0; i < rst.size(); i++) {
                oCR = new ContraRecibo();
                Vector vRowTemp = (Vector)rst.elementAt(i);
                oCR.setNumContraRecibo(((Double) vRowTemp.elementAt(0)).intValue());
                oCR.setFecVencimiento((Date)vRowTemp.elementAt(1));
                oCR.setRecibioFactura((String)vRowTemp.elementAt(2));
                oCR.setFecRecepcion((Date)vRowTemp.elementAt(3));
                oCR.setFecProbPago((Date)vRowTemp.elementAt(4));
                oCR.setObservaciones((String)vRowTemp.elementAt(5));
                oCC=new CompaniaCred();
                oCC.setNombreCorto((String)vRowTemp.elementAt(6));
                oCR.setCompCred(oCC);
                oCR.setGuiaEnvio((String)vRowTemp.elementAt(7));
                listRet.add(oCR);
            }
        }
        return listRet;
    }

    public String guardaContraRecibo(ContraRecibo oCR) throws Exception {
        Vector rst = null;
        String sQuery = "";

        if (oCR == null) {
            throw new Exception("Contra Recibo. Guarda ContraRecibo: Error de programación. Faltan datos.");
        } else {
            sQuery="select * from modificaContraRecibo("+oCR.getNumContraRecibo()+",'"+
                    oCR.getRecibioFactura()+"','"+oCR.getFecRecepcion()+"','"+oCR.getFecProbPago()+
                    "','"+oCR.getObservaciones()+"','"+oCR.getGuiaEnvio()+"')";
            if (getAD() == null) {
                setAD(new AccesoDatos());
                getAD().conectar();
                rst = getAD().ejecutarConsulta(sQuery);
            } else {
                rst = getAD().ejecutarConsulta(sQuery);
            }
            sQuery="select * from eliminaLlamadas("+oCR.getNumContraRecibo()+")";
            rst=getAD().ejecutarConsulta(sQuery); 
            for (int i=0; i<oCR.getLlamadas().size();i++){
                sQuery="select * from guardaLlamada("+oCR.getNumContraRecibo()+",'"+
                        oCR.getLlamadas().get(i).getFecLlamada()+"','"+
                        oCR.getLlamadas().get(i).getRecibioLlamada()+"','"+
                        oCR.getLlamadas().get(i).getResultado()+"', '"+
                        oCR.getLlamadas().get(i).getRealizoLlamada()+"','"+
                        oCR.getLlamadas().get(i).getTipo()+"')";
                rst=getAD().ejecutarConsulta(sQuery); 
            }
            getAD().desconectar();
            setAD(null);
        }
        return ""+rst.get(0).toString().substring(1,rst.get(0).toString().length()-1);
    }
    
    public List<ContraRecibo> buscaFacturasCredito(String sCondicion) throws Exception {
        List<ContraRecibo> listRet = null;
        ContraRecibo oCR;
        CompaniaCred oCC;

        Vector rst = null;
        String sQuery = "";

        sQuery = "select * from buscaFacturasCredito()";
        if (sCondicion.length()>0)
            sQuery=sQuery+" where "+sCondicion;
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

        if (rst != null) {
            listRet = new ArrayList();
            for (int i = 0; i < rst.size(); i++) {
                Vector vRowTemp = (Vector)rst.elementAt(i);
                oCR = new ContraRecibo();
                oFactura=new FacturaCFI();
                oFactura.setExpedicion((Date)vRowTemp.elementAt(0));
                oFactura.setNombreSerie((String)vRowTemp.elementAt(1));
                oFactura.setFolio(((Double) vRowTemp.elementAt(2)).intValue());
                oFactura.setFormaPago((String)vRowTemp.elementAt(3));
                oFactura.setCompCred(new CompaniaCred());
                oFactura.getCompCred().setRFC((String)vRowTemp.elementAt(4));
                oFactura.getCompCred().setIdEmp(((Double) vRowTemp.elementAt(5)).intValue());
                oFactura.getCompCred().setNombreCorto((String)vRowTemp.elementAt(6));
                oFactura.setOpCC(new OpeCajaConcepto());
                oFactura.getOpCC().getServPrest().getPaciente().setFolioPac(((Double) vRowTemp.elementAt(7)).intValue());
                oFactura.getOpCC().getServPrest().getPaciente().setNombre((String)vRowTemp.elementAt(8));
                oFactura.getOpCC().getServPrest().getPaciente().setApellidoPaterno((String)vRowTemp.elementAt(9));
                oFactura.getOpCC().getServPrest().getPaciente().setApellidoMaterno((String)vRowTemp.elementAt(10));
                oFactura.setImporteTotal(((Double) vRowTemp.elementAt(11)).floatValue());
                oCR.setFactura(oFactura);
                oCR.setNumContraRecibo(((Double) vRowTemp.elementAt(12)).intValue());
                oCR.setFecRecepcion((Date)vRowTemp.elementAt(13));
                oCR.setFecProbPago((Date)vRowTemp.elementAt(14));
                
                listRet.add(oCR);
            }
        }
        return listRet;
    }

    public AccesoDatos getAD() {
        return oAD;
    }

    public void setAD(AccesoDatos oAD) {
        this.oAD = oAD;
    }

    public int getNumContraRecibo() {
        return nNumContraRecibo;
    }

    public void setNumContraRecibo(int nNumContraRecibo) {
        this.nNumContraRecibo = nNumContraRecibo;
    }

    public Date getFecVencimiento() {
        return dFecVencimiento;
    }

    public void setFecVencimiento(Date dFecVencimiento) {
        this.dFecVencimiento = dFecVencimiento;
    }

    public String getCompania() {
        return sCompania;
    }

    public void setCompania(String sCompania) {
        this.sCompania = sCompania;
    }

    public String getRecibioFactura() {
        return sRecibioFactura;
    }

    public void setRecibioFactura(String sRecibioFactura) {
        this.sRecibioFactura = sRecibioFactura;
    }

    public Date getFecRecepcion() {
        return dFecRecepcion;
    }

    public void setFecRecepcion(Date dFecRecepcion) {
        this.dFecRecepcion = dFecRecepcion;
    }

    public Date getFecProbPago() {
        return dFecProbPago;
    }

    public void setFecProbPago(Date dFecProbPago) {
        this.dFecProbPago = dFecProbPago;
    }

    public String getObservaciones() {
        return sObservaciones;
    }

    public void setObservaciones(String sObservaciones) {
        this.sObservaciones = sObservaciones;
    }
    
    public String getGuiaEnvio() {
        return sGuiaEnvio;
    }

    public void setGuiaEnvio(String sGuiaEnvio) {
        this.sGuiaEnvio = sGuiaEnvio;
    }

    public List<Llamada> getLlamadas() {
        return listLlamadas;
    }

    public void setLlamadas(List<Llamada> listLlamadas) {
        this.listLlamadas = listLlamadas;
    }

    public List<FacturaCFI> getFacturas() {
        return listFacturas;
    }

    public void setFacturas(List<FacturaCFI> listFacturas) {
        this.listFacturas = listFacturas;
    }

    public FacturaCFI getFactura() {
        return oFactura;
    }

    public void setFactura(FacturaCFI oFactura) {
        this.oFactura = oFactura;
    }

    public CompaniaCred getCompaniaCred() {
        return oCompaniaCred;
    }

    public void setCompaniaCred(CompaniaCred oCompaniaCred) {
        this.oCompaniaCred = oCompaniaCred;
    }

    public Paciente getPaciente() {
        return oPaciente;
    }

    public void setPaciente(Paciente oPaciente) {
        this.oPaciente = oPaciente;
    }

    public CompaniaCred getCompCred() {
        return oCompCred;
    }

    public void setCompCred(CompaniaCred oCompCred) {
        this.oCompCred = oCompCred;
    }
}
