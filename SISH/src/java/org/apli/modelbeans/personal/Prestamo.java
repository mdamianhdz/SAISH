package org.apli.modelbeans.personal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import org.apli.AD.AccesoDatos;
import org.apli.modelbeans.PersonalHospitalario;
import org.apache.log4j.Logger;
import org.apli.modelbeans.ConceptoEgreso;
import org.apli.modelbeans.Puesto;

/**
 *
 * @author Lily_LnBnd
 */
public class Prestamo implements Serializable {

    private int nFolioPrestamo;
    private PersonalHospitalario oPersonal;
    private Date dFechaRegistro;
    private float fMonto;
    private char cSituacion;
    private AccesoDatos oAD;
    private static final Logger LOG = Logger.getLogger(Prestamo.class.getName());
    private float fMontoPagado;
    private String sNombreOtro;
    private ConceptoEgreso oConcep;//agregado a partir de nov 2015

    public Prestamo() {
        oPersonal = new PersonalHospitalario();
        oConcep = new ConceptoEgreso();
        sNombreOtro = "";
    }
    
    public List<Prestamo> buscaPersonalConPrestamosPorPagar() throws Exception{
    List<Prestamo> listRet = null;
    Prestamo oPR;
    PersonalHospitalario oPH;
    Vector rst = null;
    String sQuery = "";
        sQuery = "select * from buscaPersonalTienePrestamo()";
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
                oPR = new Prestamo();
                oPH = new PersonalHospitalario();
                Vector vRowTemp = (Vector) rst.elementAt(i);
                oPH.setFolioPers(((Double) vRowTemp.elementAt(0)).intValue());
                oPH.setNombre((String)vRowTemp.elementAt(1));
                oPH.setApellidoPaterno((String)vRowTemp.elementAt(2));
                oPH.setApellidoMaterno((String)vRowTemp.elementAt(3));
                oPH.setRFC((String)vRowTemp.elementAt(4));
                oPH.setCURP((String)vRowTemp.elementAt(5));
                oPH.setPuesto(new Puesto());
                oPH.getPuesto().setDescrip((String)vRowTemp.elementAt(6));
                oPR.setPersonal(oPH);
                listRet.add(oPR);
            }
        }
        return listRet;
    }
    
    public List<Prestamo> buscaPersonalConPrestamosPorEntregar() throws Exception{
    List<Prestamo> listRet = null;
    Prestamo oPR;
    PersonalHospitalario oPH;
    Vector rst = null;
    String sQuery = "";
        sQuery = "select * from buscaPersonalTienePrestamoPorEntregar()";
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
                oPR = new Prestamo();
                oPH = new PersonalHospitalario();
                Vector vRowTemp = (Vector) rst.elementAt(i);
                oPH.setFolioPers(((Double) vRowTemp.elementAt(0)).intValue());
                oPH.setNombre((String)vRowTemp.elementAt(1));
                oPH.setApellidoPaterno((String)vRowTemp.elementAt(2));
                oPH.setApellidoMaterno((String)vRowTemp.elementAt(3));
                oPH.setRFC((String)vRowTemp.elementAt(4));
                oPH.setCURP((String)vRowTemp.elementAt(5));
                oPH.setPuesto(new Puesto());
                oPH.getPuesto().setDescrip((String)vRowTemp.elementAt(6));                
                oPR.setPersonal(oPH);
                oPR.setNombreOtro((String)vRowTemp.elementAt(7));
                listRet.add(oPR);
            }
        }
        return listRet;
    }
    
    
    public List<Prestamo> buscaPrestamoPersonalAutorizado() throws Exception{
    List<Prestamo> listRet = null;
    Prestamo oPR;
    PersonalHospitalario oPH;
        Vector rst = null;
        String sQuery = "";

        sQuery = "select * from buscaprestamopersonalAutorizado()";
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
                oPR = new Prestamo();
                Vector vRowTemp = (Vector) rst.elementAt(i);
                oPR.setFolioPrestamo(((Double) vRowTemp.elementAt(0)).intValue());
                oPR.setMontoPagado(calculaMontoPagosRealizados((((Double) vRowTemp.elementAt(0)).intValue())));
                oPH = new PersonalHospitalario();
                oPH = oPH.buscaDatosPersonal(((Double) vRowTemp.elementAt(1)).intValue());
                oPR.setPersonal(oPH);
                oPR.setMonto(((Double) vRowTemp.elementAt(2)).floatValue());
                oPR.setNombreOtro((String)vRowTemp.elementAt(3));
                if (!oPR.getNombreOtro().equals("")){
                    oPR.getPersonal().setNombre(oPR.getNombreOtro());
                }
                listRet.add(oPR);
            }
        }
        return listRet;
    }
    
    public List<Prestamo> buscaPrestamoPersonalEntregado(int nFolio) throws Exception {
        List<Prestamo> listRet = null;
        Prestamo oPR;
        PersonalHospitalario oPH;

        Vector rst = null;
        String sQuery = "";

        sQuery = "select * from buscaprestamopersonalentregado(" + nFolio + "::int2)";
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
                oPR = new Prestamo();
                Vector vRowTemp = (Vector) rst.elementAt(i);
                oPR.setFolioPrestamo(((Double) vRowTemp.elementAt(0)).intValue());
                oPR.setMontoPagado(calculaMontoPagosRealizados((((Double) vRowTemp.elementAt(0)).intValue())));
                oPH = new PersonalHospitalario();
                // oPH.setFolioPers(((Double)vRowTemp.elementAt(1)).intValue());
                oPH = oPH.buscaDatosPersonal(((Double) vRowTemp.elementAt(1)).intValue());
                oPR.setPersonal(oPH);
                oPR.setMonto(((Double) vRowTemp.elementAt(2)).floatValue());
                oPR.setNombreOtro((String)vRowTemp.elementAt(3));
                if (!oPR.getNombreOtro().equals("")){
                    oPR.getPersonal().setNombre(oPR.getNombreOtro());
                }
                listRet.add(oPR);
            }
        }

        return listRet;
    }

    public float calculaMontoPagosRealizados(int nFolioPres) throws Exception {
        Vector rst;
        String sQuery = "";

        sQuery = "select * from calculamontopagosprestamorealizados(" + nFolioPres + ")";

        if (getAD() == null) {
            setAD(new AccesoDatos());
            getAD().conectar();
        }
        rst = getAD().ejecutarConsulta(sQuery);
        getAD().desconectar();
        setAD(null);
        
        float MontoPagado = Float.parseFloat(rst.get(0).toString().substring(1, rst.get(0).toString().length() - 1));

        return MontoPagado;
    }

    public List<Prestamo> buscaPrestamoPersonal(int nFolio) throws Exception {
        List<Prestamo> listRet = null;
        Prestamo oPR;
        PersonalHospitalario oPH;

        Vector rst = null;
        String sQuery = "";

        sQuery = "select * from buscaprestamopersonal(" + nFolio + "::int2)";
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
                oPR = new Prestamo();
                Vector vRowTemp = (Vector) rst.elementAt(i);
                oPR.setFolioPrestamo(((Double) vRowTemp.elementAt(0)).intValue());
                oPH = new PersonalHospitalario();
                oPH = oPH.buscaDatosPersonal(((Double) vRowTemp.elementAt(1)).intValue());
                oPR.setPersonal(oPH);
                oPR.setMonto(((Double) vRowTemp.elementAt(2)).floatValue());
                oPR.setNombreOtro((String)vRowTemp.elementAt(3));
                if (!oPR.getNombreOtro().equals("")){
                    oPR.getPersonal().setNombre(oPR.getNombreOtro());
                }
                oPR.setConcep(new ConceptoEgreso());
                oPR.getConcep().setCveConcepEgr(((Double)vRowTemp.elementAt(4)).intValue());
                listRet.add(oPR);
            }
        }

        return listRet;
    }

    public void actualizaSituacionPrestamo(int nfolio, String sSituacion) throws Exception {
        Vector rst = null;
        String sQuery = "";

        sQuery = "select * from actualizarsituacionprestamo(" + nfolio + ",'" + sSituacion + "')";
        if (getAD() == null) {
            setAD(new AccesoDatos());
            getAD().conectar();
            rst = getAD().ejecutarConsulta(sQuery);
            getAD().desconectar();
            setAD(null);
        }
    }
/*
    public String registraPagoPrestamo(int pnfoliopres,String psFormaPago, 
        String psDatosPago, float pnMonto) throws Exception {
        Vector rst = null;
        String sQuery = "";

        sQuery = "select * from insertaPagoPrestamo("+pnfoliopres+",'"+psFormaPago+"',"+
                (psDatosPago==null||psDatosPago.equals("")?"null": "'"+psDatosPago+"'" ) +
                ","+pnMonto+ ")";
        if (getAD() == null) {
            setAD(new AccesoDatos());
            getAD().conectar();
            rst = getAD().ejecutarConsulta(sQuery);
            getAD().desconectar();
            setAD(null);
        }
        return (rst==null?"ERROR":rst.get(0)+"");
    }
*/
    public String registrarPrestamo(Prestamo oPres) throws Exception {
        Vector rst;
        String sQuery = "";

        sQuery = "select * from insertaPrestamo(" + 
                oPres.getPersonal().getFolioPers() + ",'" + 
                oPres.getFechaRegistro() + "'," + 
                oPres.getMonto()
                + ",'" + oPres.getSituacion() + "', "+
                (oPres.getNombreOtro().equals("")?"null":"'"+oPres.getNombreOtro()+"'") + 
                ", "+
                oPres.getConcep().getCveConcepEgr() +
                ");";
        System.out.println(sQuery);
        if (getAD() == null) {
            setAD(new AccesoDatos());
            getAD().conectar();
        }
        rst = getAD().ejecutarConsulta(sQuery);
        getAD().desconectar();
        setAD(null);

        return rst.get(0).toString().substring(1, rst.get(0).toString().length() - 1);
    }

    public int getFolioPrestamo() {
        return nFolioPrestamo;
    }

    public void setFolioPrestamo(int nFolioPrestamo) {
        this.nFolioPrestamo = nFolioPrestamo;
    }

    public PersonalHospitalario getPersonal() {
        return oPersonal;
    }

    public void setPersonal(PersonalHospitalario oPersonal) {
        this.oPersonal = oPersonal;
    }

    public Date getFechaRegistro() {
        return dFechaRegistro;
    }

    public void setFechaRegistro(Date dFechaRegistro) {
        this.dFechaRegistro = dFechaRegistro;
    }

    public float getMonto() {
        return fMonto;
    }

    public void setMonto(float fMonto) {
        this.fMonto = fMonto;
    }

    public char getSituacion() {
        return cSituacion;
    }

    public void setSituacion(char cSituacion) {
        this.cSituacion = cSituacion;
    }

    public AccesoDatos getAD() {
        return oAD;
    }

    public void setAD(AccesoDatos oAD) {
        this.oAD = oAD;
    }

    /**
     * @return the fMontoPagado
     */
    public float getMontoPagado() {
        return fMontoPagado;
    }

    /**
     * @param fMontoPagado the fMontoPagado to set
     */
    public void setMontoPagado(float fMontoPagado) {
        this.fMontoPagado = fMontoPagado;
    }
    
    public String getNombreOtro(){
        return this.sNombreOtro;
    }
    public void setNombreOtro(String valor){
        this.sNombreOtro = valor;
    }
    
    public ConceptoEgreso getConcep(){
        return this.oConcep;
    }
    public void setConcep(ConceptoEgreso valor){
        this.oConcep=valor;
    }
    
    public float getMontoPendiente() {
        return this.fMonto - this.fMontoPagado;
    }
}
