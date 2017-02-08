package org.apli.modelbeans;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import org.apli.AD.AccesoDatos;
import org.apli.modelbeans.ventaCredito.CompaniaCred;
import org.apli.modelbeans.ventaCredito.FormatoCiaCred;

/**
 * Conjunto de servicios que se le proporcionan a un paciente desde su inicio
 * hasta su alta, independientemente de si es externo u hospitalizado
 *
 * @author BAOZ
 * @version 1.0
 * @created 29-abr.-2014 14:10:48
 */
public class EpisodioMedico implements Serializable {

    private int cveepisodio = 0;
    /**
     * Conjunto de compañías de crédito que cubren todo o parte del episodio
     * médico
     */
    private CompaniaCred[] arrCiaCred;
    /**
     * Conjunto de formatos recibidos y revisados del paciente respecto a una
     * compañía de crédito
     */
    private FormatoCiaCred arrFmtRevisados;
    /**
     * Indica si el paciente desea factura o no. Aplica para el caso de que
     * existan servicios que no cubre la empresa de crédito
     */
    private boolean bDeseaFactura;
    /**
     * Fecha en que el paciente es dado de alta
     */
    private Date dAlta;
    /**
     * Fecha de inicio del episodio (consulta inicial o inicio de
     * hospitalización)
     */
    private Date dInicio;
    /**
     * Indica la clasificación principal del paciente respecto a su pago 0 = de
     * contado, 1 = de crédito, 2 = de paquete. Es principal porque en un mismo
     * episodio (hospitalización por ejemplo), es factible que el paciente pague
     * una parte y otra el seguro
     */
    private int nTipoPrincipal;
    /**
     * Diagnostico principal de egreso
     */
    private Diagnostico oDxEgreso;
    /**
     * Diagnóstico principal de ingreso
     */
    private Diagnostico oDxIngreso;
    /**
     * Habitación en caso de que el paciente haya sido hospitalizado
     */
    private Habitacion oHab;
    private Habitacion oHabReg = new Habitacion();
    /**
     * Médico tratante, responsable principal del paciente durante ese evento
     * particular
     */
    private Medico oMedTratante = null;
    private Medico oMedRecibe = null;
    private Medico oMedRecom = null;
    private boolean bReqCons;
    private ServicioPrestado m_ServicioPrestado;
    private String sSituacionCuenta;
    private String sAutorizaSalidaCred;
    private boolean bReqAtnPostQx = true;
    private String sTipoAlta;
    private String sfamiliarresponsablecta;
    private String sfamiliaracompana;
    private String sRazonAltaVoluntTrasl;
    private String sDestino;
    private AccesoDatos oAD;
    private String sfamiliarinformacion;
    
    /*Agregados para Hoja de Ingreso a Hospital (reunión 5-abr-2016)*/
    private String sDomicilioFamiliar;
    private String sTelefonoFamiliar;
    private String sEmpresaFamiliar;
    private String sDomicilioEmpresaFamiliar;
    private String sTelefonoEmpresaFamiliar;
    
    //JMHG
    private String sPosibleAlta;
    //===
    
    
    public EpisodioMedico() {
        oMedTratante = new Medico();
        oMedRecom = new Medico();
        oMedRecibe = new Medico();
        oDxIngreso = new Diagnostico();
        oDxEgreso = new Diagnostico();
        this.oHab = new Habitacion();
    }
    
    public boolean esFacturableEpisodio() throws Exception {
        boolean facturable = false;

        Vector rst = null;
        String sQuery = "";

        sQuery = "select * from esFacturableCuenta(" + this.cveepisodio + ");";
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
            if (rst.size() > 0) {
                facturable=new Valida().eliminaMSJCorchetes(""+rst.get(0)).equals("1");
            }
        }
        return facturable;
    }

    public void buscaDiagnosticoActual(int id) throws Exception {
        Diagnostico arrS[] = null;
        Vector rst = null;
        Vector<Diagnostico> vObj = null;

        String sQuery = "";
        int i = 0, nTam = 0;

        if (id == 0) {
            throw new Exception("EpisodioMedico.buscaDiagnosticoActual: error de programación, faltan datos");
        } else {

            sQuery = "select * from buscadiagactpaciente(" + id + ");";

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

                vObj = new Vector<Diagnostico>();
                for (i = 0; i < rst.size(); i++) {
                    oDxIngreso = new Diagnostico();
                    Vector vRowTemp = (Vector) rst.elementAt(i);
                    setCveepisodio(((Double) vRowTemp.elementAt(0)).intValue());
                    oDxIngreso.setDescrip((String) vRowTemp.elementAt(1));

                }
                nTam = vObj.size();
                arrS = new Diagnostico[nTam];

                for (i = 0; i < nTam; i++) {
                    arrS[i] = vObj.elementAt(i);
                }
            }
            if (rst==null || rst.isEmpty()) {
                oDxIngreso = new Diagnostico();
                oDxIngreso.setDescrip("Sin diagnóstico actual");
            }
        }
    }

    public boolean buscaPacienteHospitalizado(int idpaciente) throws Exception {
        boolean hospitalizado = false;
        Vector rst = null;
        String sQuery = "";

        sQuery = "select * from buscaPacienteHospitalizado(" + idpaciente + ");";
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
            if (rst.size() > 0) {
                hospitalizado = true;
                Vector vRowTemp = (Vector) rst.elementAt(0);
                oMedTratante = new Medico();
                setCveepisodio(((Double) vRowTemp.elementAt(0)).intValue());
                oMedTratante = oMedTratante.buscaMedicoNombre(((Double) vRowTemp.elementAt(1)).intValue());
                if (!((String) vRowTemp.elementAt(2)).equals("")) {
                    oDxIngreso = new Diagnostico();
                    oDxIngreso = oDxIngreso.buscaDiagnostico((String) vRowTemp.elementAt(2));
                }
                if (!((String) vRowTemp.elementAt(3)).equals("")) {
                    oDxEgreso = new Diagnostico();
                    oDxEgreso = oDxEgreso.buscaDiagnostico((String) vRowTemp.elementAt(3));
                }
                this.bReqAtnPostQx = new Valida().BinarioStringToBoolean((String) vRowTemp.elementAt(4));
                this.setTipoAlta((String) vRowTemp.elementAt(5));
                oHab = new Habitacion();
                oHab.setHab(((Double) vRowTemp.elementAt(6)).intValue());
                this.setResponsableCuenta(((String) vRowTemp.elementAt(8)));
                this.setFamiliaracompana(((String) vRowTemp.elementAt(9)));
                oMedRecibe = new Medico();
                oMedRecibe = oMedRecibe.buscaMedicoNombre(((Double) vRowTemp.elementAt(10)).intValue());
                this.setFamiliarinformacion(((String) vRowTemp.elementAt(11)));
            }
        }
        return hospitalizado;
    }

    public boolean tieneEpisodioMedicoPaciente(int idpaciente) throws Exception {
        boolean tieneEpisodio = false;
        EpisodioMedico arrS[] = null;
        Vector rst = null;
        Vector<EpisodioMedico> vObj = null;

        String sQuery = "";
        int i = 0, nTam = 0;

        if (idpaciente == 0) {
            throw new Exception("EpisodioMedico.tieneEpisodioMedicoPaciente: error de programación, faltan datos");
        } else {

            sQuery = "select * from buscaEpisodioAbiertoPac(" + idpaciente + ");";

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
                tieneEpisodio = true;
                vObj = new Vector<EpisodioMedico>();
                for (i = 0; i < rst.size(); i++) {
                    Vector vRowTemp = (Vector) rst.elementAt(i);
                    oMedTratante = new Medico();
                    setCveepisodio(((Double) vRowTemp.elementAt(0)).intValue());
                    oMedTratante = oMedTratante.buscaMedicoNombre(((Double) vRowTemp.elementAt(1)).intValue());
                    oMedRecibe = new Medico();
                    oMedRecibe = oMedRecibe.buscaMedicoNombre(((Double) vRowTemp.elementAt(2)).intValue());
                    this.nTipoPrincipal = ((Double)vRowTemp.elementAt(3)).intValue();
                }
                nTam = vObj.size();
                arrS = new EpisodioMedico[nTam];

                for (i = 0; i < nTam; i++) {
                    arrS[i] = vObj.elementAt(i);
                }
            }
        }
        return tieneEpisodio;
    }

    public boolean tieneEpisodioMedicoPacienteHospitalizado(int idpaciente) throws Exception {
        boolean hospitalizado = false;
        Vector rst = null;
        String sQuery = "";
        int i = 0;

        if (idpaciente == 0) {
            throw new Exception("EpisodioMedico.tieneEpisodioMedicoPacienteHospitalizado: error de programación, faltan datos");
        } else {
            sQuery = "select * from buscaepisodioabiertopacconhab(" + idpaciente + ");";

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
                for (i = 0; i < rst.size(); i++) {
                    Vector vRowTemp = (Vector) rst.elementAt(i);
                    oMedTratante = new Medico();
                    setCveepisodio(((Double) vRowTemp.elementAt(0)).intValue());
                    oMedTratante = oMedTratante.buscaMedicoNombre(((Double) vRowTemp.elementAt(1)).intValue());
                    oHab = new Habitacion();
                    oHab.setHab(((Double) vRowTemp.elementAt(2)).intValue());
                    this.setResponsableCuenta(((String) vRowTemp.elementAt(3)));
                    oMedRecibe = new Medico();
                    int medRe = ((Double) vRowTemp.elementAt(4)).intValue();
                    if (medRe != 0) {
                        oMedRecibe = oMedRecibe.buscaMedicoNombre(((Double) vRowTemp.elementAt(4)).intValue());
                    }
                    String diag = (String) vRowTemp.elementAt(5);
                    oDxIngreso = new Diagnostico();
                    if (!diag.equals("")) {
                        oDxIngreso = oDxIngreso.buscaDiagnostico((String) vRowTemp.elementAt(5));
                        oDxIngreso.setCve((String) vRowTemp.elementAt(5));
                    }
                    this.setFamiliarinformacion((String) vRowTemp.elementAt(6));
                    this.setDomicilioFamiliar((String) vRowTemp.elementAt(7));
                    this.setTelefonoFamiliar((String) vRowTemp.elementAt(8));
                    this.setEmpresaFamiliar((String) vRowTemp.elementAt(9));
                    this.setDomicilioEmpresaFamiliar((String) vRowTemp.elementAt(10));
                    this.setTelefonoEmpresaFamiliar((String) vRowTemp.elementAt(11));
                    this.setInicio((Date) vRowTemp.elementAt(12));
                }
            }
        }
        if (rst==null || rst.size() > 0) {
            hospitalizado = true;
        }
        return hospitalizado;
    }

    public boolean buscaPacienteHospitalizadoParaEgresar(int idpaciente) throws Exception {
        boolean hospitalizado = false;
        Vector rst = null;
        String sQuery = "";

        sQuery = "select * from buscaPacienteHospitalizadoparaegresar(" + idpaciente + ");";
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
            if (rst.size() > 0) {
                hospitalizado = true;
                Vector vRowTemp = (Vector) rst.elementAt(0);
                oMedTratante = new Medico();
                setCveepisodio(((Double) vRowTemp.elementAt(0)).intValue());
                oMedTratante = oMedTratante.buscaMedicoNombre(((Double) vRowTemp.elementAt(1)).intValue());
                oDxIngreso = new Diagnostico();
                oDxIngreso = oDxIngreso.buscaDiagnostico((String) vRowTemp.elementAt(2));
                String diag = (String) vRowTemp.elementAt(2);
                if (!diag.equals("")) {
                    oDxIngreso = new Diagnostico();
                    oDxIngreso = oDxIngreso.buscaDiagnostico((String) vRowTemp.elementAt(2));
                    oDxIngreso.setCve((String) vRowTemp.elementAt(2));
                }
                String diag2 = (String) vRowTemp.elementAt(3);
                if (!diag2.equals("")) {
                    oDxEgreso = new Diagnostico();
                    oDxEgreso = oDxEgreso.buscaDiagnostico((String) vRowTemp.elementAt(3));
                    oDxEgreso.setCve((String) vRowTemp.elementAt(3));
                }

                this.bReqAtnPostQx = new Valida().BinarioStringToBoolean((String) vRowTemp.elementAt(4));
                this.setTipoAlta((String) vRowTemp.elementAt(5));
                oHab = new Habitacion();
                oHab.setHab(((Double) vRowTemp.elementAt(6)).intValue());
                this.setResponsableCuenta(((String) vRowTemp.elementAt(8)));
                this.setFamiliaracompana(((String) vRowTemp.elementAt(9)));
                oMedRecibe = new Medico();
                oMedRecibe = oMedRecibe.buscaMedicoNombre(((Double) vRowTemp.elementAt(10)).intValue());
                dInicio = (Date) vRowTemp.elementAt(11);
            }
        }
        return hospitalizado;
    }

    public void buscaMedTratEpi(int idEpisodio) throws Exception {
        EpisodioMedico arrS[] = null;
        Vector rst = null;
        Vector<EpisodioMedico> vObj = null;

        String sQuery = "";
        int i = 0, nTam = 0;

        if (idEpisodio == 0) {
            throw new Exception("EpisodioMedico.buscaMedTratEpi: error de programación, faltan datos");
        } else {

            sQuery = "select * from buscamedepisomed(" + idEpisodio + "::int2);";

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

                vObj = new Vector<EpisodioMedico>();
                for (i = 0; i < rst.size(); i++) {
                    Vector vRowTemp = (Vector) rst.elementAt(i);
                    oMedTratante = new Medico();
                    oMedTratante = oMedTratante.buscaMedicoNombre(((Double) vRowTemp.elementAt(0)).intValue());

                }
                nTam = vObj.size();
                arrS = new EpisodioMedico[nTam];

                for (i = 0; i < nTam; i++) {
                    arrS[i] = vObj.elementAt(i);
                }
            }
        }

    }

//--CIERRE Y PRECIERRE------------------------------------------------------------------------------------------------------------
    public EpisodioMedico datosCierreCuenta(int pnFolioPac) throws Exception {
        EpisodioMedico oEpMed = new EpisodioMedico();
        Vector rst = null;
        String sQuery = "";

        if (pnFolioPac == 0) {
            throw new Exception("EpisodioMedico.datosCierreCuenta: error de programación, faltan datos");
        } else {
            sQuery = "select * from buscarDatosCierreCuenta (" + pnFolioPac + ")";
            System.out.println(sQuery);
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
                /*  ep.cveepisodio (0), ep.nfoliopaciente(1), ep.bfactura(2), ep.dinicio(3), ep.dalta(4), ep.ntipoprincipal(5), 
                 ep.scvediagingreso(6), di.sdescripcion(7),ep.nnumhab(8), ph.snombre(9), ph.sappaterno(10), ph.sapmaterno(11), 
                 ep.ssituacioncuenta(12), sfamiliarresponsablecta(13) */
                oEpMed.setCveepisodio(((Double) vRowTemp.elementAt(0)).intValue());
                oEpMed.setDeseaFactura(((String) vRowTemp.elementAt(2)).equals("1"));
                oEpMed.setInicio((Date) vRowTemp.elementAt(3));
                oEpMed.setAlta((Date) vRowTemp.elementAt(4));
                oEpMed.setTipoPrincipal(((Double) vRowTemp.elementAt(5)).intValue());
                oEpMed.setDxIngreso(new Diagnostico((String) vRowTemp.elementAt(6), (String) vRowTemp.elementAt(7)));
                oEpMed.setHab(new Habitacion(((Double) vRowTemp.elementAt(8)).intValue()));
                oEpMed.setMedTratante(new Medico((String) vRowTemp.elementAt(9), (String) vRowTemp.elementAt(10), (String) vRowTemp.elementAt(11)));
                oEpMed.setSituacionCuenta((String) vRowTemp.elementAt(12));
                oEpMed.setResponsableCuenta((String) vRowTemp.elementAt(13));
                oEpMed.setAutorizaSalidaCred("");
                oEpMed.setTipoAlta("");
                oEpMed.setRazonAltaVoluntTrasl("");
                oEpMed.setDestino("");
                oEpMed.setReqAtnPostQx(true);
            }
        }
        return oEpMed;
    }

    public CompaniaCred[] buscaCompaniasCredEpisodioMed(int folioPac) throws Exception {
        CompaniaCred[] arrRet = null;
        CompaniaCred oCCred = null;
        Vector rst = null;
        Vector<CompaniaCred> vObj = null;
        String sQuery = "";
        int i = 0, nTam = 0;
        sQuery = " SELECT * FROM buscaCompaniasCredEpisodioMed(" + folioPac + "," + this.cveepisodio + ")";
        /*Si oAD es nulo, tiene que crearlo y conectarlo, de otro modo
         supone que ya viene conectado*/
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
            vObj = new Vector<CompaniaCred>();
            for (i = 0; i < rst.size(); i++) {
                oCCred = new CompaniaCred();
                Vector vRowTemp = (Vector) rst.elementAt(i);
                /*  nidemp(0), snombrecorto(1), snomrazsoc(2), srfc(3), spoliticaspago(4),
                 bactiva(5), ntipocia(6), nplazopago(7) */
                oCCred.setIdEmp(((Double) vRowTemp.elementAt(0)).intValue());
                oCCred.setNombreCorto((String) vRowTemp.elementAt(1));
                oCCred.setNomRazSoc((String) vRowTemp.elementAt(2));
                oCCred.setRFC((String) vRowTemp.elementAt(3));
                oCCred.setPoliticasPago((String) vRowTemp.elementAt(4));
                oCCred.setActiva(((String) vRowTemp.elementAt(5)).charAt(0));
                oCCred.setTipoCia(((Double) vRowTemp.elementAt(6)).intValue());
                oCCred.setPlazoPago(((Double) vRowTemp.elementAt(7)).intValue());

                vObj.add(oCCred);
            }
            nTam = vObj.size();
            arrRet = new CompaniaCred[nTam];

            for (i = 0; i < nTam; i++) {
                arrRet[i] = vObj.elementAt(i);
            }
        }
        return arrRet;
    }

    public String cerrarCuenta(int folioPac) throws Exception {
        Vector rst = null;
        String sQuery = "";

        if (folioPac==0 || this.cveepisodio == 0 ||
            this.oDxEgreso==null || this.oDxEgreso.getCve().equals("") ||
            this.sTipoAlta.equals("") || this.sTipoAlta.equals("-1")) {
            throw new Exception("EpisodioMedico.cerrarCuenta: error de programación, faltan datos");
        } else {
            sQuery = "select * from cerrarCuenta(" + folioPac + "," + 
                    this.cveepisodio + ", '" +
                    this.oDxEgreso.getCve() + "', '"+
                    (this.bReqAtnPostQx?"1":"0") + "', '"+
                    this.sTipoAlta + "', " +
                    (this.sRazonAltaVoluntTrasl.equals("")?"null":"'"+this.sRazonAltaVoluntTrasl+"'") + ", " +
                    (this.sDestino.equals("")?"null":"'"+this.sDestino+"'") + ")";
            System.out.println(sQuery);
            if (getAD() == null) {
                setAD(new AccesoDatos());
                getAD().conectar();
                rst = getAD().ejecutarConsulta(sQuery);
                getAD().desconectar();
                setAD(null);
            }
        }
        return " " + (rst==null?"":rst.get(0));
    }

    public String precerrarCuenta(int folioPac) throws Exception {
        Vector rst = null;
        String sQuery = "";

        if (folioPac==0 || this.cveepisodio == 0 ||
            this.oDxEgreso==null || this.oDxEgreso.getCve().equals("") ||
            this.sTipoAlta.equals("") || this.sTipoAlta.equals("-1")) {
            throw new Exception("EpisodioMedico.PrecerrarCuenta: error de programación, faltan datos");
        } else {
            sQuery = "select * from PrecerrarCuenta(" + folioPac + "," + 
                    this.cveepisodio + ", '" +
                    this.oDxEgreso.getCve() + "', '"+
                    (this.bReqAtnPostQx?"1":"0") + "', '"+
                    this.sTipoAlta + "', " +
                    (this.sRazonAltaVoluntTrasl.equals("")?"null":"'"+this.sRazonAltaVoluntTrasl+"'") + ", " +
                    (this.sDestino.equals("")?"null":"'"+this.sDestino+"'") + ")";
            System.out.println(sQuery);
            if (getAD() == null) {
                setAD(new AccesoDatos());
                getAD().conectar();
                rst = getAD().ejecutarConsulta(sQuery);
                getAD().desconectar();
                setAD(null);
            }
        }
        return " " + (rst==null?"":rst.get(0).toString());
    }

    public String autorizarSalidaCredito(int folioPac) throws Exception {
        Vector rst = null;
        String sQuery = "";

        if (folioPac == 0 || this.cveepisodio == 0) {
            throw new Exception("EpisodioMedico.autorizarSalidaCredito: error de programación, faltan datos");
        } else {
            // autorizaSalidaCredito( pnFolioPaciente, pnCveEpisodio, pnAutorizaSalidaCred )
            sQuery = "select * from autorizaSalidaCredito(" + folioPac + "," + this.cveepisodio + ", md5('"+this.sAutorizaSalidaCred+"'))";
            if (getAD() == null) {
                setAD(new AccesoDatos());
                getAD().conectar();
                rst = getAD().ejecutarConsulta(sQuery);
                getAD().desconectar();
                setAD(null);
            }
        }
        return " " + (rst==null?"":rst.get(0).toString());
    }

    public List<String> buscaNumPolizasPorCiaCred(int nFolioPac, int idEmpCia) throws Exception {
        //Busca servicios de un paciente específico
        List<String> listRet = new ArrayList<String>();

        Vector rst = null;
        String sQuery = "";

        sQuery = "select * from buscaPolizasAseg(" + nFolioPac + "," + this.cveepisodio + "," + idEmpCia + ") ";
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
                Vector vRowTemp = (Vector) rst.elementAt(i);
                listRet.add((String) vRowTemp.elementAt(0));
            }
        }
        return listRet;
    }
    
    public float buscaCostoPaqueteParaHonorarios() throws Exception{
        List<Float> listRet=new ArrayList<Float>();
        Vector rst = null;
        String sQuery = "";
        if (this.cveepisodio==0) {
            throw new Exception("EpisodioMedico.buscaCostoPaqueteParaHonorarios: error de programación, faltan datos.");
        }else {
            sQuery="SELECT * FROM buscaCostoPaqueteParaHonorarios ("+this.cveepisodio+")";
            //System.out.println(sQuery);
            if (getAD() == null){
                setAD(new AccesoDatos());
                getAD().conectar();
                rst = getAD().ejecutarConsulta(sQuery);
                getAD().desconectar();
                setAD(null);
            }else{
                rst = getAD().ejecutarConsulta(sQuery);
            }
            if (rst != null) {
                listRet = new ArrayList();
                Vector vRowTemp = (Vector)rst.elementAt(0);
                
                listRet.add(((Double) vRowTemp.elementAt(0)).floatValue());
            }
        }
        return (listRet.size()>0?listRet.get(0):null);
     }
    
    public int buscaNoProcedimientosPorEpisodioMedico(Date dIni, Date dfin) throws Exception{
        List<Integer> listRet=new ArrayList<Integer>();
        Vector rst = null;
        String sQuery = "";
        if (this.cveepisodio==0) {
            throw new Exception("EpisodioMedico.buscaNoProcedimientosPorEpisodioMedico: error de programación, faltan datos.");
        }else {
            SimpleDateFormat fmtTxt = new SimpleDateFormat("dd-MM-yyyy");
            sQuery="SELECT * FROM  buscaNoProcedimientosPorEpisodioMedico ("+this.cveepisodio+",'"+fmtTxt.format(dIni)+"','"+fmtTxt.format(dfin)+"')";
            System.out.println(sQuery);
            if (getAD() == null){
                setAD(new AccesoDatos());
                getAD().conectar();
                rst = getAD().ejecutarConsulta(sQuery);
                getAD().desconectar();
                setAD(null);
            }else{
                rst = getAD().ejecutarConsulta(sQuery);
            }
            if (rst != null) {
                listRet = new ArrayList();
                Vector vRowTemp = (Vector)rst.elementAt(0);
                
                listRet.add(((Double) vRowTemp.elementAt(0)).intValue());
            }
        }
        return (listRet.size()>0?listRet.get(0):-1);
     }

    public List<EpisodioMedico> buscaTodosEpisodioMedicoPaq(Date fechaIni, Date fechaFin) throws Exception {
        //Busca servicios de un paciente específico
        List<EpisodioMedico> listRet=new ArrayList<EpisodioMedico>();
        EpisodioMedico oEpMed;

        Vector rst = null;
        String sQuery = "";

        sQuery="SELECT * FROM buscaTodosEpisodioMedicoPaq ('"+fechaIni+"','"+fechaFin+"')";
        System.out.println(sQuery);
        if (getAD() == null){
                setAD(new AccesoDatos());
                getAD().conectar();
                rst = getAD().ejecutarConsulta(sQuery);
                getAD().desconectar();
                setAD(null);
        }else{
                rst = getAD().ejecutarConsulta(sQuery);
        }
               
        if (rst != null) {
            listRet = new ArrayList();
            for (int i = 0; i < rst.size(); i++) {
                oEpMed = new EpisodioMedico();
                Vector vRowTemp = (Vector)rst.elementAt(i);
                /*  nfoliopaciente, cveepisodio */
                Paciente oPac = new Paciente();
                oPac.setFolioPac(((Double) vRowTemp.elementAt(0)).intValue());
                oEpMed.setCveepisodio(((Double) vRowTemp.elementAt(1)).intValue());
                
                listRet.add(oEpMed);
            }
        }
        return listRet;
    }
    


//--------------------------------------------------------------------------------------------------------------
    public String guardaImpresionDiag(int nFolio, EpisodioMedico oEM) throws Exception {
        System.out.println("guardaImpresionDiag()");
        Vector rst = null;
        String sQuery = "";

        if (nFolio == 0) {
            throw new Exception("EpisodioMedico.guardaImpresionDiag: Error de programación. Faltan datos.");
        } else {
            sQuery = "select * from guardaImpDiag(" + nFolio + ", '" + oEM.getDxIngreso().getCve() + "', '" + oEM.isReqCons()
                    + "', '" + oEM.getServicioPrestado().getObs() + "', '" + oEM.getServicioPrestado().getReceta() + "');";
            System.out.println(sQuery);
            if (getAD() == null) {
                setAD(new AccesoDatos());
                getAD().conectar();
                rst = getAD().ejecutarConsulta(sQuery);
                getAD().desconectar();
                setAD(null);
            }
        }
        return "" + (rst==null?"":rst.get(0));
    }

    public void buscaTipoPrincEpi(int idpaciente) throws Exception {
    Vector rst = null;
    Vector<EpisodioMedico> vObj = null;
    String sQuery = "";
    int i = 0;

        if (idpaciente == 0) {
            throw new Exception("EpisodioMedico.buscaTipoPrincEpi: error de programación, faltan datos");
        } else {

            sQuery = "SELECT * FROM buscaTipoPrincEpisoMed(" + idpaciente + "::int);";

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
System.out.println(rst);
                vObj = new Vector<EpisodioMedico>();
                for (i = 0; i < rst.size(); i++) {
                    Vector vRowTemp = (Vector) rst.elementAt(i);
                    this.nTipoPrincipal = ((Double) vRowTemp.elementAt(0)).intValue();
                }
            }
        }
    }

    //JMHG
    /**
     * Actualiza las posibles altas de los pacientes seleccionados
     * @param arrFoliosPacientes Folios de los pacientes seleccionados
     * @param arrCvesEpisodios Claves de episodios seleccionados
     * @param arrSPosiblesAltas Valores de posibles altas seleccionadas
     * @return Número de datos afectados
     * @throws Exception 
     */
    public int actualizarPosiblesAltasPacs( int[] arrFoliosPacientes,
        int[] arrCvesEpisodios, String[] arrSPosiblesAltas ) throws Exception
    {
        int nAfectados = 0;
        String sFolios = "", sCveEpisodios = "", sPosiblesAltas = "", sQuery = "";
        Vector rst = null;
        
        for( int i = 0; i < arrFoliosPacientes.length; i++ )
        {
            sFolios += "" + arrFoliosPacientes[ i ] + ", ";
            sCveEpisodios += "" + arrCvesEpisodios[ i ] + ", ";
            sPosiblesAltas += arrSPosiblesAltas[ i ] + ", ";
        }
        sFolios = sFolios.substring( 0, sFolios.lastIndexOf( ", " ) );
        sCveEpisodios = sCveEpisodios.substring( 0, sCveEpisodios.lastIndexOf( ", " ) );
        sPosiblesAltas = sPosiblesAltas.substring( 0, sPosiblesAltas.lastIndexOf( ", " ) );
        
        sQuery = "SELECT * FROM actualizarPosiblesAltasPacs( " +
            "ARRAY[" + sFolios + "]::integer[], " +
            "ARRAY[" + sCveEpisodios + "]::integer[], " +
            "ARRAY[" + sPosiblesAltas + "]::character[] );";
        
        if( getAD() == null )
        {
            setAD( new AccesoDatos() );
            getAD().conectar();
            rst = getAD().ejecutarConsulta( sQuery );
            getAD().desconectar();
            setAD( null );
        }
        else
        {
            rst = getAD().ejecutarConsulta( sQuery );
            getAD().desconectar();
            setAD( null );
        }
        
        if( rst != null )
        {
            Vector rowTemp = (Vector)rst.elementAt( 0 );
            nAfectados = ( (Double)rowTemp.elementAt( 0 ) ).intValue();
        }
        
        return nAfectados;
    }
    
    /**
     * Busca las cuentas cerradas y/o precerradas de un paciente
     * @param nFolioPaciente Folio del paciente
     * @return Lista de tipo EpisodioMedico con las cuentas cerradas y/o precerradas del paciente
     * @throws Exception Si no se ingreso un folio correcto
     */
    public List<EpisodioMedico> buscarPacienteCuentasNoAbiertas( int nFolioPaciente ) throws Exception
    {
        EpisodioMedico epMedico = null;
        List<EpisodioMedico> listEpisodios = new ArrayList<EpisodioMedico>();
        String sQuery = "";
        Vector rst = null;
        
        if( nFolioPaciente < 1 )
        {
            throw new Exception( "EpisodioMedico.buscarPacienteCuentaNoAbierta: Faltan datos" );
        }
        else
        {
            sQuery = "SELECT * FROM buscarPacienteCuentaNoAbierta( " + nFolioPaciente + " );";
            if( getAD() == null )
            {
                setAD( new AccesoDatos() );
                getAD().conectar();
                rst = getAD().ejecutarConsulta( sQuery );
                getAD().desconectar();
                setAD( null );
            }
            else
            {
                rst = getAD().ejecutarConsulta( sQuery );
                getAD().desconectar();
                setAD( null );
            }

            if( rst != null && rst.size() > 0 )
            {
                Vector rowTemp = null;
                for( int i = 0; i < rst.size(); i++ )
                {
                    rowTemp = (Vector)rst.elementAt( i );
                    epMedico = new EpisodioMedico();
                    epMedico.setServicioPrestado( new ServicioPrestado() );

                    epMedico.setCveepisodio( ( (Double)rowTemp.elementAt( 0 ) ).intValue() );
                    epMedico.getServicioPrestado().setCostoCobrado( ( (Double)rowTemp.elementAt( 1 ) ).floatValue() );
                    epMedico.getMedTratante().setNombre( (String)rowTemp.elementAt( 2 ) );
                    epMedico.getMedTratante().setApellidoPaterno( (String)rowTemp.elementAt( 3 ) );
                    epMedico.getMedTratante().setApellidoMaterno( (String)rowTemp.elementAt( 4 ) );
                    epMedico.getHab().setHab( ( (Double)rowTemp.elementAt( 5 ) ).intValue() );
                    epMedico.setSituacionCuenta( (String)rowTemp.elementAt( 6 )  );

                    listEpisodios.add( epMedico );
                }
            }
        }
        return listEpisodios;
    }
    
    /**
     * Marca la situación de la cuenta como abierta
     * @param nFolioPaciente Folio del paciente
     * @return Número de datos afectados
     * @throws Exception Si el folio o la clave de episodio son incorrectas
     */
    public int reabrirCuenta( int nFolioPaciente ) throws Exception
    {
        String sQuery = "";
        Vector rst = null;
        int nAfectados = -1;
        
        if( nFolioPaciente < 1 || cveepisodio < 1 )
        {
            throw new Exception( "EpisodioMedico.reabrirCuenta: Faltan datos" );
        }
        else
        {
            sQuery = "SELECT * FROM actualizarEpisodioMedicoReabrirCuenta( " +
                nFolioPaciente + ", " +
                cveepisodio + " );";
            if( getAD() == null )
            {
                setAD( new AccesoDatos() );
                getAD().conectar();
                rst = getAD().ejecutarConsulta( sQuery );
                getAD().desconectar();
                setAD( null );
            }
            else
            {
                rst = getAD().ejecutarConsulta( sQuery );
                getAD().desconectar();
                setAD( null );
            }
            
            if( rst != null && rst.size() > 0 )
            {
                Vector rowTemp = (Vector)rst.elementAt( 0 );
                nAfectados = ( (Double)rowTemp.elementAt( 0 ) ).intValue();
            }
        }
        
        return nAfectados;
    }
    
    /**
     * Busca el paciente, indicado por número de folio, para su cancelación de ingreso al hospital
     * @param nFolioPaciente Folio del paciente a buscar
     * @param dFechaHoy La fecha de hoy
     * @return Lista de datos de la búsqueda
     * @throws Exception En caso de algún error o que no exista el folio del paciente
     */
    public List<EpisodioMedico> buscarPacienteCancelarIngresoHospital( int nFolioPaciente, Date dFechaHoy ) throws Exception
    {
        String sQuery = "";
        Vector rst = null;
        List<EpisodioMedico> arrRet = null;
        EpisodioMedico epMed = null;
        
        if( nFolioPaciente < 1 )
        {
            throw new Exception( "EpisodioMedico.buscarPacienteCancelarIngresoHospital: Faltan datos" );
        }
        else
        {
            sQuery = "SELECT * FROM buscarPacienteCancelarIngreso(" + 
                    nFolioPaciente + "::integer, " + 
                    "'" + dFechaHoy + "'::date);";
            if( getAD() == null )
            {
                setAD( new AccesoDatos() );
                getAD().conectar();
                rst = getAD().ejecutarConsulta( sQuery );
                getAD().desconectar();
                setAD( null );
            }
            else
            {
                rst = getAD().ejecutarConsulta( sQuery );
            }
            
            if( rst != null && rst.size() == 1 )
            {
                arrRet = new ArrayList<EpisodioMedico>();
                Vector vRowTemp;
                for( int i = 0; i < rst.size(); i++ )
                {
                    vRowTemp = (Vector)rst.elementAt( i );
                    epMed = new EpisodioMedico();
                    
                    epMed.getDxIngreso().setDescrip( (String)vRowTemp.elementAt( 0 ) );
                    epMed.getHab().setHab( ( (Double)vRowTemp.elementAt( 1 ) ).intValue() );
                    epMed.getMedTratante().setNombre( (String)vRowTemp.elementAt( 2 ) );
                    epMed.getMedTratante().setApellidoPaterno( (String)vRowTemp.elementAt( 3 ) );
                    epMed.getMedTratante().setApellidoMaterno( (String)vRowTemp.elementAt( 4 ) );
                    epMed.setInicio( (Date)vRowTemp.elementAt( 5 ) );
                    epMed.setCveepisodio( ( (Double)vRowTemp.elementAt( 6 ) ).intValue() );
                    epMed.setTipoPrincipal( ( (Double)vRowTemp.elementAt( 7 ) ).intValue() );
                    
                    arrRet.add( epMed );
                }
            }
        }
        
        return arrRet;
    }
    
    /**
     * Cancela el ingreso al hospital del paciente
     * @param nFolioPaciente Folio del paciente a cancelar
     * @return El número de datos afectados en la cancelación
     * @throws Exception En caso de algún error o que falten datos
     */
    public int cancelarIngresoHospital( int nFolioPaciente ) throws Exception
    {
        int nAfectados = -1;
        String sQuery = "";
        Vector rst = null;
        
        if( nFolioPaciente < 1 || cveepisodio < 1 || oHab == null || oHab.getHab() < 1 || nTipoPrincipal < 0 )
        {
            throw new Exception( "EpisodioMedico.cancelarIngresoHospital: Faltan datos" );
        }
        else
        {
            sQuery = "SELECT * FROM actualizarHospitalizacionPacienteCancelar(" +
                nFolioPaciente + "::integer, " + 
                cveepisodio + "::integer, " +
                oHab.getHab() + "::smallint, " +
                nTipoPrincipal + "::smallint);";
            if( getAD() == null )
            {
                setAD( new AccesoDatos() );
                getAD().conectar();
                rst = getAD().ejecutarConsulta( sQuery );
                getAD().desconectar();
                setAD( null );
            }
            else
            {
                rst = getAD().ejecutarConsulta( sQuery );
            }
            
            if( rst != null && rst.size() == 1 )
            {
                Vector vRowTemp = (Vector)rst.elementAt( 0 );
                nAfectados = ( (Double)vRowTemp.elementAt( 0 ) ).intValue();
            }
        }
        
        return nAfectados;
    }
    
    
    /**
     * Busca los episodios médicos con cuenta hospitalización (con habitación)
     * @param nFolioPaciente Folio del paciente a buscar
     * @return Lista de EpisodioMedico
     * @throws Exception En caso de que el folio del paciente no sea correcto
     */
    public List<EpisodioMedico> buscarEpisodiosCuentaHospitalizacion( int nFolioPaciente ) throws Exception
    {
        List<EpisodioMedico> arrRet = null;
        Vector rst = null;
        EpisodioMedico oEpMed = null;
        String sQuery = "";
        if( nFolioPaciente < 1 )
        {
            throw new Exception( "EpisodioMedico.buscarEpisodiosCuentaHospitalizacion: Faltan datos" );
        }
        else
        {
            sQuery = "SELECT * FROM buscarEpisodioCuentaHosp(" +
                nFolioPaciente + "::integer);";
            if( getAD() == null )
            {
                setAD( new AccesoDatos() );
                getAD().conectar();
                rst = getAD().ejecutarConsulta( sQuery );
                getAD().desconectar();
                setAD( null );
            }
            else
            {
                rst = getAD().ejecutarConsulta( sQuery );
            }
            
            if( rst != null && rst.size() > 0 )
            {
                arrRet = new ArrayList<EpisodioMedico>();
                Vector vRowTemp;
                for( int i = 0; i < rst.size(); i++ )
                {
                    vRowTemp = (Vector)rst.elementAt( i );
                    oEpMed = new EpisodioMedico();
                    
                    oEpMed.setInicio( (Date)vRowTemp.elementAt( 0 ) );
                    oEpMed.setAlta( (Date)vRowTemp.elementAt( 1 ) );
                    oEpMed.setTipoPrincipal( ( (Double)vRowTemp.elementAt( 2 ) ).intValue() );
                    oEpMed.getMedTratante().setApellidoPaterno( (String)vRowTemp.elementAt( 3 ) );
                    oEpMed.getMedTratante().setApellidoMaterno( (String)vRowTemp.elementAt( 4 ) );
                    oEpMed.getMedTratante().setNombre( (String)vRowTemp.elementAt( 5 ) );
                    oEpMed.getDxIngreso().setDescrip( (String)vRowTemp.elementAt( 6 ) );
                    oEpMed.setCveepisodio( ( (Double)vRowTemp.elementAt( 7 ) ).intValue() );
                    oEpMed.getHab().setHab( ( (Double)vRowTemp.elementAt( 8 ) ).intValue() );
                    
                    arrRet.add( oEpMed );
                }
            }
        }
        
        return arrRet;
    }
    //====

//=============== SET & GET ===============//
    public int getCveepisodio() {
        return cveepisodio;
    }

    public void setCveepisodio(int cveepisodio) {
        this.cveepisodio = cveepisodio;
    }

    public CompaniaCred[] getArrCiaCred() {
        return arrCiaCred;
    }

    public void setArrCiaCred(CompaniaCred[] arrCiaCred) {
        this.arrCiaCred = arrCiaCred;
    }

    public FormatoCiaCred getArrFmtRevisados() {
        return arrFmtRevisados;
    }

    public void setArrFmtRevisados(FormatoCiaCred arrFmtRevisados) {
        this.arrFmtRevisados = arrFmtRevisados;
    }

    public boolean getDeseaFactura() {
        return bDeseaFactura;
    }

    public void setDeseaFactura(boolean bDeseaFactura) {
        this.bDeseaFactura = bDeseaFactura;
    }

    public Date getAlta() {
        return dAlta;
    }

    public void setAlta(Date dAlta) {
        this.dAlta = dAlta;
    }

    public Date getInicio() {
        return dInicio;
    }

    public void setInicio(Date dInicio) {
        this.dInicio = dInicio;
    }

    public int getTipoPrincipal() {
        return nTipoPrincipal;
    }

    public void setTipoPrincipal(int nTipoPrincipal) {
        this.nTipoPrincipal = nTipoPrincipal;
    }

    public Diagnostico getDxEgreso() {  
        return oDxEgreso;
    }

    public void setDxEgreso(Diagnostico oDxEgreso) {
        this.oDxEgreso = oDxEgreso;
    }

    public Diagnostico getDxIngreso() {      
        return oDxIngreso;
    }

    public void setDxIngreso(Diagnostico oDxIngreso) {
        this.oDxIngreso = oDxIngreso;
    }

    public boolean getReqCons() {
        return bReqCons;
    }

    public void setReqCons(boolean bReqCons) {
        this.bReqCons = bReqCons;
    }

    public Habitacion getHab() {
        return oHab;
    }

    public void setHab(Habitacion oHab) {
        this.oHab = oHab;
    }

    public Medico getMedTratante() {
        return oMedTratante;
    }

    public void setMedTratante(Medico oTratante) {
        this.oMedTratante = oTratante;
    }

    public Medico getMedRecibe() {
        return oMedRecibe;
    }

    public void setMedRecibe(Medico oRecibe) {
        this.oMedRecibe = oRecibe;
    }

    public Medico getMedRecom() {
        return oMedRecom;
    }

    public void setMedRecom(Medico oMedRecom) {
        this.oMedRecom = oMedRecom;
    }

    public String getSituacionCuenta() {
        return sSituacionCuenta;
    }

    public void setSituacionCuenta(String situacioncuenta) {
        this.sSituacionCuenta = situacioncuenta;
    }

    public AccesoDatos getAD() {
        return oAD;
    }

    public void setAD(AccesoDatos oAD) {
        this.oAD = oAD;
    }

    public ServicioPrestado getServicioPrestado() {
        return m_ServicioPrestado;
    }

    public void setServicioPrestado(ServicioPrestado m_ServicioPrestado) {
        this.m_ServicioPrestado = m_ServicioPrestado;
    }

    public boolean isReqCons() {
        return bReqCons;
    }

    public String getAutorizaSalidaCred() {
        return sAutorizaSalidaCred;
    }

    public void setAutorizaSalidaCred(String autorizaSalidaCred) {
        this.sAutorizaSalidaCred = autorizaSalidaCred;
    }

    public boolean isReqAtnPostQx() {
        return bReqAtnPostQx;
    }

    public void setReqAtnPostQx(boolean bReqAtnPostQx) {
        this.bReqAtnPostQx = bReqAtnPostQx;
    }

    public String getTipoAlta() {
        return sTipoAlta;
    }

    public void setTipoAlta(String sTipoAlta) {
        this.sTipoAlta = sTipoAlta;
    }

    public String getResponsableCuenta() {
        return sfamiliarresponsablecta;
    }

    public void setResponsableCuenta(String sResponsableCuenta) {
        this.sfamiliarresponsablecta = sResponsableCuenta;
    }

    public String getFamiliaracompana() {
        return sfamiliaracompana;
    }

    public void setFamiliaracompana(String sfamiliaracompana) {
        this.sfamiliaracompana = sfamiliaracompana;
    }

    public Habitacion getHabReg() {
        return oHabReg;
    }

    public void setHabReg(Habitacion oHabitacionReg) {
        this.oHabReg = oHabitacionReg;
    }

    public String getRazonAltaVoluntTrasl() {
        return sRazonAltaVoluntTrasl;
    }

    public void setRazonAltaVoluntTrasl(String sRazonAltaVoluntTrasl) {
        this.sRazonAltaVoluntTrasl = sRazonAltaVoluntTrasl;
    }

    public String getDestino() {
        return sDestino;
    }

    public void setDestino(String sDestino) {
        this.sDestino = sDestino;
    }

    public String getFamiliarinformacion() {
        return sfamiliarinformacion;
    }

    public void setFamiliarinformacion(String sfamiliarinformacion) {
        this.sfamiliarinformacion = sfamiliarinformacion;
    }
    
    
    public String getDomicilioFamiliar(){return this.sDomicilioFamiliar;}
    public void setDomicilioFamiliar(String val){this.sDomicilioFamiliar=val;}
    public String getTelefonoFamiliar(){return this.sTelefonoFamiliar;}
    public void setTelefonoFamiliar(String val){this.sTelefonoFamiliar=val;}
    public String getEmpresaFamiliar(){return this.sEmpresaFamiliar;}
    public void setEmpresaFamiliar(String val){this.sEmpresaFamiliar=val;}
    public String getDomicilioEmpresaFamiliar(){return this.sDomicilioEmpresaFamiliar;}
    public void setDomicilioEmpresaFamiliar(String val){this.sDomicilioEmpresaFamiliar=val;}
    public String getTelefonoEmpresaFamiliar(){return this.sTelefonoEmpresaFamiliar;}
    public void setTelefonoEmpresaFamiliar(String val){this.sTelefonoEmpresaFamiliar=val;}
    //JMHG
    public String getPosibleAlta(){return this.sPosibleAlta;}
    public void setPosibleAlta(String val){this.sPosibleAlta = val;}

    public boolean getPosibleAltaBol(){return sPosibleAlta.compareToIgnoreCase( "1" ) == 0;}
    public void setPosibleAltaBol( boolean bPosibleAlta ){this.sPosibleAlta = ( bPosibleAlta == true ? "1" : "0" );}
    //====
}