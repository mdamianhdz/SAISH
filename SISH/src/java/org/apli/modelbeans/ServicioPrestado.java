package org.apli.modelbeans;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import org.apli.AD.AccesoDatos;
import org.apli.modelbeans.ventaCredito.CompaniaCred;

/*
 * Servicios espec�ficos otorgados al paciente durante un evento particular. En el
 * caso de pacientes hospitalizados se le conoce como cargo
 * @author BAOZ
 * @version 1.0
 * @updated 22-Feb-2016
 */
public class ServicioPrestado implements Serializable {
private EpisodioMedico oEpisodioMedico;
private int nCantidad = 1;
private String sTipo;
private Medico oMedico = null;
private Paciente oPaciente = null;
private boolean bFacturable;
private Date dRealizado;
private Date dRegistro = null;
private float nCostoOriginal;
private float nCostoCobrado;
private String sConvQuienPaga;
/**
 * % de IVA aplicado (16 por ejemplo)
 */
private float nPctIVA;
/**
 * 0 = particular (aunque sea de paquete) 1 = empresa
 */
private int nQuienPaga;
/**
 * Folio del servicio Tipo del servicio + consecutivo
 */
private String sIdFolio;
/**
 * Observaciones respecto al servicio (requerimientos para el laboratorio,
 * condiciones para la consulta, etc.)
 */
private String sObs;
/**
 * Describe el procedimiento realizado en el caso de Consulta Externa, puede
 * ser vac�o
 */
/**
 * Exámen físico del paciente
 */
private ExamenFisico oEF;
/**
 * Indica si el paciente requiere otra consulta
 */
private Boolean bReqConsulta;
private Boolean bReqHosp;
private String sProcedimiento;
/**
 * Folio de la receta generada durante la consulta externa, puede ser vac�o
 */
private String sReceta;
/**
 * Situaci�n del servicio N = Nuevo P = Pagado A = Autorizado a cr�dito Q =
 * Autorizado paquete O = Otorgado
 */
private String sSituacion;
private boolean Otorgado;
/**
 * Concepto de ingreso (servicio, material,
 * medicamento SOLAMENTE)
 */
private ConceptoIngreso oConcepPrestado;
/**
 * M�dico que realiza servicio (consulta o interconsulta) si aplica
 *
 */
private PersonalHospitalario oPersHospRealiza = null;

 /**
 * Unidad de medida usada en Medicamento o Material
 *
 */
private UnidadMedida oUniMed;
private CompaniaCred oCompaniaCred = null;
private boolean bFechaEntradaServ = false;
private Date dFechaEntradaServ;
private AccesoDatos oAD;
private float fAnticipo;
private boolean bAuxdiag;
private String sAuxdiag;
private boolean bControlTv = false;
private boolean bControlClima = false;
private boolean bConcepUrgencia = false;
private boolean bLineaEndos = false;
private PersonalHospitalario oTecnico = null;
private PersonalHospitalario oEnfermera = null;
private ServProcQx oServProcQx;
private String sNumPoliza;
private Date dsalidaservicio;
private ProcedimientoRealizado m_ProcedimientoRealizado;
public PaqueteContratado oPaqueteContratado;
private Medico oMedicoSolicita = null;
private int nNumOrden = 0;

//Para el caso de cargos por honorarios médicos externos, indica si los indicó el médico o no
private boolean bIndicadoPorMedico;

/*
 * Datos miembro agregados para recibir de Farmacia
 */
private MaterialCuracion oMaterialCuracion;
private Medicamento oMedicamento;
private Date dRecepFarm;
private int nRequisicionFarm;
private String sSitFarmacia;
private boolean bRecibido;
private String sObsPrecio;
private List<String> equipoQx;
private int lineaIngreso;




    public ServicioPrestado() {
        this.oEpisodioMedico = new EpisodioMedico();
        this.oPaciente = new Paciente();
        this.oMedico = new Medico();
        this.oMedicoSolicita = new Medico();
    }

    public List<ServicioPrestado> buscRAPorPagarParticular(int nFolioPaciente) throws Exception {
        List<ServicioPrestado> listRet = new ArrayList<ServicioPrestado>();
        ServicioPrestado oSP;
        EpisodioMedico oEM;
        PaqueteContratado oPC;
        Paciente oPA;
        Habitacion oHN;
        Paquete oPQ;
        CompaniaCred oEmp;

        Vector rst = null;
        String sQuery = "";

        sQuery = "select * from buscaraporpagarparticular(" + nFolioPaciente + ")";
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
                oSP = new ServicioPrestado();
                Vector vRowTemp = (Vector) rst.elementAt(i);
                oSP.setIdFolio((String) vRowTemp.elementAt(0));
                oEM = new EpisodioMedico();
                oEM.setCveepisodio(((Double) vRowTemp.elementAt(1)).intValue());
                oPA = new Paciente();
                oPA.setFolioPac(((Double) vRowTemp.elementAt(2)).intValue());
                oHN = new Habitacion();
                oHN.setHab(((Double) vRowTemp.elementAt(3)).intValue());
                oEM.setHab(oHN);
                oPC = new PaqueteContratado();
                oPC.setIdpaqcont(((Double) vRowTemp.elementAt(4)).intValue());
                oPQ = new Paquete();
                oPQ.setIdPaquete(((Double) vRowTemp.elementAt(5)).intValue());
                oPC.setPaquete(oPQ);
                oPC.setAnticipo(((Double) vRowTemp.elementAt(6)).floatValue());
                oSP.setQuienPaga(((Double)vRowTemp.elementAt(7)).intValue());
                oEmp = new CompaniaCred();
                oEmp.setIdEmp(((Double)vRowTemp.elementAt(8)).intValue());
                oEmp.setNombreCorto((String)vRowTemp.elementAt(9));
                ConceptoIngreso oNvoCI = new ConceptoIngreso();
                oNvoCI.setCveConcep(((Double)vRowTemp.elementAt(10)).intValue());
                oNvoCI.setDescripcion((String)vRowTemp.elementAt(11));
                oSP.setPaqueteContratado(oPC);
                oPA = oPA.buscaPaciente(oPA.getFolioPac());
                oSP.setPaciente(oPA);
                oSP.setEpisodioMedico(oEM);
                oSP.setCostoOriginal(oPC.getAnticipo());
                oSP.setCostoCobrado(0);
                oSP.setCompaniaCred(oEmp);
                oSP.setConcepPrestado(oNvoCI);
                oSP.setConvQuienPaga((new Valida()).QuienPaga(oSP.getQuienPaga()));
                listRet.add(oSP);
            }
        }
        return listRet;
    }

    public List<ServicioPrestado> buscaAnticiposPorPagar(int nFolioPaciente) throws Exception {
        List<ServicioPrestado> listRet = new ArrayList<ServicioPrestado>();
        ServicioPrestado oSP;
        EpisodioMedico oEM;
        PaqueteContratado oPC;
        Paciente oPA;
        Habitacion oHN;
        Paquete oPQ;
        CompaniaCred oEmp;

        Vector rst = null;
        String sQuery = "";

        sQuery = "select * from buscaanticiposporpagar(" + nFolioPaciente + ")";
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
                oSP = new ServicioPrestado();
                Vector vRowTemp = (Vector) rst.elementAt(i);
                oSP.setIdFolio((String) vRowTemp.elementAt(0));
                oEM = new EpisodioMedico();
                oEM.setCveepisodio(((Double) vRowTemp.elementAt(1)).intValue());
                oPA = new Paciente();
                oPA.setFolioPac(((Double) vRowTemp.elementAt(2)).intValue());
                oHN = new Habitacion();
                oHN.setHab(((Double) vRowTemp.elementAt(3)).intValue());
                oEM.setHab(oHN);
                oPC = new PaqueteContratado();
                oPC.setIdpaqcont(((Double) vRowTemp.elementAt(4)).intValue());
                oPQ = new Paquete();
                oPQ.setIdPaquete(((Double) vRowTemp.elementAt(5)).intValue());
                oPC.setPaquete(oPQ);
                oPC.setAnticipo(((Double) vRowTemp.elementAt(6)).floatValue());
                oSP.setQuienPaga(((Double)vRowTemp.elementAt(7)).intValue());
                oEmp = new CompaniaCred();
                oEmp.setIdEmp(((Double)vRowTemp.elementAt(8)).intValue());
                oEmp.setNombreCorto((String)vRowTemp.elementAt(9));
                ConceptoIngreso oNvoCI = new ConceptoIngreso();
                oNvoCI.setCveConcep(((Double)vRowTemp.elementAt(10)).intValue());
                oNvoCI.setDescripcion((String)vRowTemp.elementAt(11));
                oSP.setPaqueteContratado(oPC);
                oPA = oPA.buscaPaciente(oPA.getFolioPac());
                oSP.setPaciente(oPA);
                oSP.setEpisodioMedico(oEM);
                oSP.setCostoOriginal(oPC.getAnticipo());
                oSP.setCostoCobrado(oPC.getAnticipo());
                oSP.setCompaniaCred(oEmp);
                oSP.setConcepPrestado(oNvoCI);
                oSP.setConvQuienPaga((new Valida()).QuienPaga(oSP.getQuienPaga()));
                listRet.add(oSP);
            }
        }
        return listRet;
    }

    public List<ServicioPrestado> buscaAbonoDsctoCredPorPagar(int nFolioPaciente) throws Exception {
        List<ServicioPrestado> listRet = new ArrayList<ServicioPrestado>();
        ServicioPrestado oSP;
        EpisodioMedico oEM;
        PaqueteContratado oPC;
        Paciente oPA;
        Habitacion oHN;
        Paquete oPQ;
        CompaniaCred oEmp;

        Vector rst = null;
        String sQuery = "";

        sQuery = "select * from buscaAbonoDsctoCredPorPagar(" + nFolioPaciente + ")";
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
                oSP = new ServicioPrestado();
                Vector vRowTemp = (Vector) rst.elementAt(i);
                oSP.setIdFolio((String) vRowTemp.elementAt(0));
                oEM = new EpisodioMedico();
                oEM.setCveepisodio(((Double) vRowTemp.elementAt(1)).intValue());
                oPA = new Paciente();
                oPA.setFolioPac(((Double) vRowTemp.elementAt(2)).intValue());
                oHN = new Habitacion();
                oHN.setHab(((Double) vRowTemp.elementAt(3)).intValue());
                oEM.setHab(oHN);
                oPC = new PaqueteContratado();
                oPC.setIdpaqcont(((Double) vRowTemp.elementAt(4)).intValue());
                oPQ = new Paquete();
                oPQ.setIdPaquete(((Double) vRowTemp.elementAt(5)).intValue());
                oPC.setPaquete(oPQ);
                oPC.setAnticipo(((Double) vRowTemp.elementAt(6)).floatValue());
                oSP.setQuienPaga(((Double)vRowTemp.elementAt(7)).intValue());
                oEmp = new CompaniaCred();
                oEmp.setIdEmp(((Double)vRowTemp.elementAt(8)).intValue());
                oEmp.setNombreCorto((String)vRowTemp.elementAt(9));
                ConceptoIngreso oNvoCI = new ConceptoIngreso();
                oNvoCI.setCveConcep(((Double)vRowTemp.elementAt(10)).intValue());
                oNvoCI.setDescripcion((String)vRowTemp.elementAt(11));
                oSP.setPaqueteContratado(oPC);
                oPA = oPA.buscaPaciente(oPA.getFolioPac());
                oSP.setPaciente(oPA);
                oSP.setEpisodioMedico(oEM);
                oSP.setCostoOriginal(oPC.getAnticipo());
                oSP.setCostoCobrado(oPC.getAnticipo());
                oSP.setCompaniaCred(oEmp);
                oSP.setConcepPrestado(oNvoCI);
                oSP.setConvQuienPaga((new Valida()).QuienPaga(oSP.getQuienPaga()));
                listRet.add(oSP);
            }
        }
        return listRet;
    }

    public List<ServicioPrestado> buscadepositosPaciente(int nFolioPac) throws Exception {
        System.out.println("buscaDepositosPaciente()");
        List<ServicioPrestado> listRet = new ArrayList<ServicioPrestado>();
        ServicioPrestado oSP;
        ConceptoIngreso oCI;
        String vBolean;
        Vector rst = null;
        String sQuery = "";

        sQuery = "SELECT * FROM buscadepositospaciente(" + nFolioPac + ")";
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
                oSP = new ServicioPrestado();
                Vector vRowTemp = (Vector) rst.elementAt(i);
                oSP.setIdFolio((String) vRowTemp.elementAt(0));
                Paciente oNvoPaciente = new Paciente();
                /**
                 * oNvoPaciente = oNvoPaciente.buscaPaciente(((Double)
                 * vRowTemp.elementAt(1)).intValue());
                 */
                oNvoPaciente.setFolioPac(((Double) vRowTemp.elementAt(1)).intValue());
                oSP.setPaciente(oNvoPaciente);
                oSP.setCostoOriginal(((Double) vRowTemp.elementAt(2)).floatValue());
                ConceptoIngreso oNvoCI = new ConceptoIngreso();
                oNvoCI = oNvoCI.buscaNombreConcepto(((Double) vRowTemp.elementAt(3)).intValue());
                oSP.setConcepPrestado(oNvoCI);

                listRet.add(oSP);
            }
        }
        return listRet;
    }

    public void actualizaSituacionServicio(String folio, String situacion) throws Exception {
        Vector rst = null;
        String sQuery = "";
        if (folio.isEmpty()) {
            throw new Exception("Faltan Datos");
        } else {
            sQuery = "select actualizarSituacionServicio('" + folio + "','" + situacion + "')";
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
        System.out.print(sObs);
    }

    public boolean autorizarDescuentoServ()throws Exception{
    boolean bRet = false;
    Vector rst = null, vRowTemp= null;
    String sQuery = "", sRes="";
    Valida oVal = new Valida();
        if (this.nCostoCobrado==0 ||
            oVal.stringIsNullOrEmpty(this.sObsPrecio))
            throw new Exception("ServicioPrestado.autorizarDescuentoServ: faltan datos");
        else{
            sQuery = "SELECT * FROM insertaDescuentoServicioExtHosp('"+
                    this.sIdFolio + "', " +
                    this.nCostoCobrado + ", '" +
                    this.sObsPrecio + "', " +
                    this.oEpisodioMedico.getCveepisodio() + ", " +
                    this.oPaciente.getFolioPac() + ", '" +
                    (this.sIdFolio.contains("E")?"E":"H") +
                    "')";
            System.out.println(sQuery);
            if (this.oAD==null){
                this.oAD = new AccesoDatos();
                this.oAD.conectar();
                rst = getAD().ejecutarConsulta(sQuery);
                this.oAD.desconectar();
                this.oAD = null;
            }else{
                rst = getAD().ejecutarConsulta(sQuery);
            }
            if (rst!=null){
                vRowTemp = (Vector)rst.elementAt(0);
                sRes = (String)vRowTemp.elementAt(0);
                if (sRes.equals("2")){
                    bRet = true;
                }
            }
        }
        return bRet;
    }
    
    /*Para uso de Autorización de Descuento/Crédito a particulares*/
    public List<ServicioPrestado> todosServsParaAutDsctoCred() 
                        throws Exception {
    List<ServicioPrestado> listRet = new ArrayList<ServicioPrestado>();
    ServicioPrestado oSP;
    Vector rst = null;
    String sQuery = "";
        if (this.oPaciente == null || this.oPaciente.getFolioPac()==0)
            throw new Exception("ServicioPrestado.todosServsParaAutDsctoCred: faltan datos");
        else{
            sQuery = "SELECT * FROM buscaTodosServiciosPrestadosParaAutDsctoCre("+
                    this.oPaciente.getFolioPac()+")";
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
                    oSP = new ServicioPrestado();
                    Vector vRowTemp = (Vector) rst.elementAt(i);
                    /*No debe considerar anticipos para cobrar en Venta de Servicio*/
                    if (((Double) (vRowTemp.elementAt(17))).intValue() != ConceptoIngreso.CVE_ANTICIPO){
                        oSP.setIdFolio((String) vRowTemp.elementAt(0));
                        Paciente oNvoPaciente = new Paciente();
                        oNvoPaciente = oNvoPaciente.buscaPaciente(((Double) vRowTemp.elementAt(2)).intValue());
                        oSP.setPaciente(oNvoPaciente);
                        Valida oValida = new Valida();
                        oSP.setFacturable(oValida.BinarioIntToBoolean((Integer.parseInt((String) vRowTemp.elementAt(3)))));
                        oSP.setRegistro((Date) vRowTemp.elementAt(5));
                        oSP.setCostoOriginal(((Double) vRowTemp.elementAt(6)).floatValue());
                        oSP.setCostoCobrado(oSP.getCostoOriginal());
                        oSP.setQuienPaga(((Double) vRowTemp.elementAt(8)).intValue());
                        if (oSP.getQuienPaga()>0) //empresa o paquete no se paga
                            oSP.setCostoOriginal(0f);
                        oSP.setConvQuienPaga(oValida.QuienPaga(oSP.getQuienPaga()));
                        oSP.setObs((String) vRowTemp.elementAt(13));

                        ConceptoIngreso oNvoCI = new ConceptoIngreso();
                        oNvoCI = oNvoCI.buscaNombreConcepto(((Double) (vRowTemp.elementAt(17))).intValue());
                        oSP.setConcepPrestado(oNvoCI);

                        CompaniaCred oCompCred = new CompaniaCred();
                        oCompCred = oCompCred.buscaNombreEmpresa(((Double) (vRowTemp.elementAt(20))).intValue());
                        oSP.setCompaniaCred(oCompCred);
                        oSP.setPctIVA(((Double) vRowTemp.elementAt(7)).floatValue());
                        oSP.setCantidad(((Double)(vRowTemp.elementAt(18))).intValue());
                        listRet.add(oSP);
                    }
                }
            }
        }
        return listRet;
    }

    
    /*Para uso de CAJA, pone ceros o costo a cobrar según corresponda*/
    public List<ServicioPrestado> todosServsPrstsNvs() throws Exception {
    List<ServicioPrestado> listRet = new ArrayList<ServicioPrestado>();
    ServicioPrestado oSP;
    Vector rst = null;
    String sQuery = "";
        if (this.getPaciente()==null ||
            this.getPaciente().getFolioPac()==0)
            throw new Exception("ServicioPrestado.todosServsPrstsNvs: faltan datos");
        else{
            sQuery = "SELECT * FROM buscaTodosServiciosPrestadosParaCaja("+
                    this.getPaciente().getFolioPac()+"::integer)";
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
                    oSP = new ServicioPrestado();
                    Vector vRowTemp = (Vector) rst.elementAt(i);
                    /*No debe considerar anticipos para cobrar en Venta de Servicio*/
                    if (((Double) (vRowTemp.elementAt(17))).intValue() != ConceptoIngreso.CVE_ANTICIPO){
                        oSP.setIdFolio((String) vRowTemp.elementAt(0));
                        Paciente oNvoPaciente = new Paciente();
                        oNvoPaciente = oNvoPaciente.buscaPaciente(((Double) vRowTemp.elementAt(2)).intValue());
                        oSP.setPaciente(oNvoPaciente);
                        Valida oValida = new Valida();
                        oSP.setFacturable(oValida.BinarioIntToBoolean((Integer.parseInt((String) vRowTemp.elementAt(3)))));
                        oSP.setRegistro((Date) vRowTemp.elementAt(5));
                        oSP.setCostoOriginal(((Double) vRowTemp.elementAt(6)).floatValue());
                        oSP.setCostoCobrado(oSP.getCostoOriginal());
                        oSP.setQuienPaga(((Double) vRowTemp.elementAt(8)).intValue());
                        if (oSP.getQuienPaga()==TipoPrincipalPaga.TIPO_EMP ||
                            oSP.getQuienPaga()==TipoPrincipalPaga.TIPO_PAQ) //empresa o paquete no se paga
                            oSP.setCostoOriginal(0f);
                        oSP.setConvQuienPaga(oValida.QuienPaga(oSP.getQuienPaga()));
                        oSP.setObs((String) vRowTemp.elementAt(13));

                        ConceptoIngreso oNvoCI = new ConceptoIngreso();
                        oNvoCI = oNvoCI.buscaNombreConcepto(((Double) (vRowTemp.elementAt(17))).intValue());
                        oSP.setConcepPrestado(oNvoCI);

                        CompaniaCred oCompCred = new CompaniaCred();
                        oCompCred = oCompCred.buscaNombreEmpresa(((Double) (vRowTemp.elementAt(20))).intValue());
                        oSP.setCompaniaCred(oCompCred);
                        
                        oSP.setNumOrden(((Double)(vRowTemp.elementAt(21))).intValue());
                        oSP.getConcepPrestado().setAreaServicio(new AreaDeServicio());
                        oSP.getConcepPrestado().getAreaServicio().setCve(((Double)vRowTemp.elementAt(22)).toString());
                        /**
                         *Agregado por Miguel Angel Damian Hernandez
                         */
                        oSP.setPctIVA(((Double) vRowTemp.elementAt(7)).floatValue());
                        oSP.setCantidad(((Double)(vRowTemp.elementAt(18))).intValue());
                        listRet.add(oSP);
                    }
                }
            }
        }
        return listRet;
    }

    /*Para uso de CAJA, obtiene posibles abonos*/
    public List<ServicioPrestado> buscaCuentaParaAbono() throws Exception{
    List<ServicioPrestado> lRet=null;
    ServicioPrestado oSP;
    Valida oValida = new Valida();
    Vector rst = null;
    String sQuery = "";
        if (this.oPaciente==null || this.oPaciente.getFolioPac()==0)
            throw new Exception("ServicioPrestado.buscaCuentaParaAbono: faltan datos");
        else{
            sQuery = "SELECT * FROM buscaCuentasParaAbono("+
                    this.oPaciente.getFolioPac()+")";
            if (getAD() == null) {
                setAD(new AccesoDatos());
                getAD().conectar();
                rst = getAD().ejecutarConsulta(sQuery);
                getAD().desconectar();
                setAD(null);
            } else {
                rst = getAD().ejecutarConsulta(sQuery);
            }
            if (rst!=null){
                lRet = new ArrayList();
                for (int i = 0; i < rst.size(); i++) {
                    oSP = new ServicioPrestado();
                    Vector vRowTemp = (Vector) rst.elementAt(i);
                    oSP.setEpisodioMedico(new EpisodioMedico());
                    oSP.getEpisodioMedico().setHab(new Habitacion());
                    Paciente oNvoPaciente = new Paciente();
                    oNvoPaciente = oNvoPaciente.buscaPaciente(((Double) vRowTemp.elementAt(0)).intValue());
                    oSP.setPaciente(oNvoPaciente);
                    oSP.getEpisodioMedico().setCveepisodio(((Double) vRowTemp.elementAt(1)).intValue());
                    oSP.getEpisodioMedico().getHab().setHab(((Double) vRowTemp.elementAt(2)).intValue());
                    oSP.setFacturable(oValida.BinarioStringToBoolean((String) vRowTemp.elementAt(3)));
                    oSP.getEpisodioMedico().setInicio((Date)vRowTemp.elementAt(4));
                    oSP.setCostoOriginal(((Double) vRowTemp.elementAt(5)).floatValue());
                    oSP.setCostoCobrado(((Double) vRowTemp.elementAt(6)).floatValue());
                    oSP.setAnticipo(((Double) vRowTemp.elementAt(7)).floatValue());
                    oSP.setIdFolio(oSP.getEpisodioMedico().getCveepisodio()+"");
                    if (oSP.getEpisodioMedico().getHab().getHab()>0)
                        lRet.add(oSP);
                }
            }
        }
        return lRet;
    }
    
    /*Para uso de CAJA, obtiene cuentas hospitalarias para cerrar*/
    public List<ServicioPrestado> buscaCuentaParaCerrar() throws Exception{
    List<ServicioPrestado> lRet=null;
    ServicioPrestado oSP;
    Valida oValida = new Valida();
    Vector rst = null;
    String sQuery = "";
        if (this.oPaciente==null || this.oPaciente.getFolioPac()==0)
            throw new Exception("ServicioPrestado.buscaCuentaParaCerrar: faltan datos");
        else{
            sQuery = "SELECT * FROM buscaCuentasPorCerrar("+
                    this.oPaciente.getFolioPac()+")";
            if (getAD() == null) {
                setAD(new AccesoDatos());
                getAD().conectar();
                rst = getAD().ejecutarConsulta(sQuery);
                getAD().desconectar();
                setAD(null);
            } else {
                rst = getAD().ejecutarConsulta(sQuery);
            }
            lRet = new ArrayList();
            if (rst!=null &&
                ((Double)((Vector) rst.elementAt(0)).elementAt(1)).intValue()>0 ){
                
                for (int i = 0; i < rst.size(); i++) {
                    oSP = new ServicioPrestado();
                    Vector vRowTemp = (Vector) rst.elementAt(i);
                    oSP.setEpisodioMedico(new EpisodioMedico());
                    oSP.getEpisodioMedico().setHab(new Habitacion());
                    Paciente oNvoPaciente = new Paciente();
                    oNvoPaciente = oNvoPaciente.buscaPaciente(((Double) vRowTemp.elementAt(0)).intValue());
                    oSP.setPaciente(oNvoPaciente);
                    oSP.getEpisodioMedico().setCveepisodio(((Double) vRowTemp.elementAt(1)).intValue());
                    oSP.getEpisodioMedico().getHab().setHab(((Double) vRowTemp.elementAt(2)).intValue());
                    oSP.setFacturable(oValida.BinarioStringToBoolean((String) vRowTemp.elementAt(3)));
                    oSP.getEpisodioMedico().setInicio((Date)vRowTemp.elementAt(4));
                    oSP.setCostoOriginal(((Double) vRowTemp.elementAt(5)).floatValue());
                    oSP.setCostoCobrado(((Double) vRowTemp.elementAt(6)).floatValue());
                    oSP.setAnticipo(((Double) vRowTemp.elementAt(7)).floatValue());
                    oSP.setIdFolio(oSP.getEpisodioMedico().getCveepisodio()+"");
                    oSP.setQuienPaga(((Double) vRowTemp.elementAt(8)).intValue());
                    CompaniaCred oCompCred = new CompaniaCred();
                    oCompCred = oCompCred.buscaNombreEmpresa(((Double) (vRowTemp.elementAt(9))).intValue());
                    oSP.setCompaniaCred(oCompCred);
                    
                    if (oSP.getAnticipo()<0)
                        oSP.setConvQuienPaga("Sanatorio Huerta");
                    else
                        oSP.setConvQuienPaga(oValida.QuienPaga(oSP.getQuienPaga()));
                    lRet.add(oSP);
                }
            }
        }
        return lRet;
    }
    
    public List<ServicioPrestado> serviciosPaciente(int nFolioPac) throws Exception {
        //Busca servicios de un paciente específico
        List<ServicioPrestado> listRet = new ArrayList<ServicioPrestado>();
        ServicioPrestado oSP;

        Vector rst = null;
        String sQuery = "";

        sQuery = "select * from buscaServiciosPaciente(" + nFolioPac + ") ";
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
                oSP = new ServicioPrestado();
                Vector vRowTemp = (Vector) rst.elementAt(i);
                oSP.setIdFolio((String) vRowTemp.elementAt(0));
                oSP.setRealizado((Date) vRowTemp.elementAt(1));
                oSP.setRegistro((Date) vRowTemp.elementAt(2));
                oSP.setConcepPrestado(new ConceptoIngreso());
                oSP.getConcepPrestado().setCveConcep(((Double) vRowTemp.elementAt(3)).intValue());
                oSP.getConcepPrestado().setDescripConcep((String) vRowTemp.elementAt(4));
                oSP.getEpisodioMedico().setMedTratante(new Medico());
                oSP.getEpisodioMedico().getMedTratante().setFolioPers(((Double) vRowTemp.elementAt(5)).intValue());
                oSP.getEpisodioMedico().getMedTratante().setNombre((String) vRowTemp.elementAt(6));
                oSP.getEpisodioMedico().getMedTratante().setApellidoPaterno((String) vRowTemp.elementAt(7));
                oSP.getEpisodioMedico().getMedTratante().setApellidoMaterno((String) vRowTemp.elementAt(8));
                ExamenFisico oEF1 = new ExamenFisico();
                oEF1.setCveExFis(((Double) vRowTemp.elementAt(9)).intValue());
                oEF1.setFrecCard(((Double) vRowTemp.elementAt(10)).intValue());
                oEF1.setFrecResp(((Double) vRowTemp.elementAt(11)).intValue());
                oEF1.setTAA(((Double) vRowTemp.elementAt(12)).intValue());
                oEF1.setTAB(((Double) vRowTemp.elementAt(13)).intValue());
                oEF1.setPeso(((Double) vRowTemp.elementAt(14)).floatValue());
                oEF1.setTalla(((Double) vRowTemp.elementAt(15)).floatValue());
                oEF1.setTemp(((Double) vRowTemp.elementAt(16)).floatValue());
                oSP.setExamenFisico(oEF1);
                oSP.setObs((String) vRowTemp.elementAt(17));
                oSP.setReceta((String) vRowTemp.elementAt(18));
                oSP.getEpisodioMedico().setMedRecibe(new Medico());
                oSP.getEpisodioMedico().getMedRecibe().setFolioPers(((Double) vRowTemp.elementAt(19)).intValue());
                oSP.getEpisodioMedico().getMedRecibe().setNombre((String) vRowTemp.elementAt(20));
                oSP.getEpisodioMedico().getMedRecibe().setApellidoPaterno((String) vRowTemp.elementAt(21));
                oSP.getEpisodioMedico().getMedRecibe().setApellidoMaterno((String) vRowTemp.elementAt(22));
                oSP.setMedico(new Medico());
                oSP.getMedico().setFolioPers(((Double) vRowTemp.elementAt(23)).intValue());
                oSP.getMedico().setNombre((String) vRowTemp.elementAt(24));
                oSP.getMedico().setApellidoPaterno((String) vRowTemp.elementAt(25));
                oSP.getMedico().setApellidoMaterno((String) vRowTemp.elementAt(26));
                oSP.getEpisodioMedico().setCveepisodio(((Double) vRowTemp.elementAt(27)).intValue());
                listRet.add(oSP);
            }
        }
        return listRet;
    }

    public boolean buscaInformacionExtraPaseSalida() throws Exception {
    boolean informacionExistente = false;
    Vector rst = null;
    String sQuery = "";

        sQuery = "SELECT * FROM buscainformacionextrapasesalida(" + 
                this.oPaciente.getFolioPac() + ", "+
                this.oEpisodioMedico.getCveepisodio()+");";
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
            Vector vRowTemp = (Vector) rst.elementAt(0);
            this.setIdFolio((String) vRowTemp.elementAt(0));
            this.setControlTv(((String) vRowTemp.elementAt(1)).equals("1"));
            this.setControlClima(((String) vRowTemp.elementAt(2)).equals("1"));
            this.setQuienPaga(((Double) vRowTemp.elementAt(3)).intValue());
            informacionExistente = true;
        }
        return informacionExistente;
    }

    public ServicioPrestado buscaServicioPrestadoDeHospitalizado(int nCveEpi) throws Exception {
        ServicioPrestado oSP = null;
        Vector rst = null;
        String sQuery = "";

        sQuery = "SELECT * FROM buscaservicioprestadodeingresohosp(" + nCveEpi + ")";
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
            oSP = new ServicioPrestado();
            Vector vRowTemp = (Vector) rst.elementAt(0);
            oSP.setIdFolio((String) vRowTemp.elementAt(0));
            oEpisodioMedico = new EpisodioMedico();
            oEpisodioMedico.buscaMedTratEpi(((Double) vRowTemp.elementAt(1)).intValue());
            oSP.setEpisodioMedico(oEpisodioMedico);
            Medico oNvoMedico = oEpisodioMedico.getMedTratante();
            oSP.setMedico(oNvoMedico);
            Paciente oNvoPaciente = new Paciente();
            oNvoPaciente = oNvoPaciente.buscaPaciente(((Double) vRowTemp.elementAt(2)).intValue());
            oSP.setFacturable(new Valida().BinarioStringToBoolean((String) vRowTemp.elementAt(3)));
            oSP.setPaciente(oNvoPaciente);
            oSP.setRegistro((Date) vRowTemp.elementAt(5));
            oSP.setCostoOriginal(((Double) vRowTemp.elementAt(6)).floatValue());
            oSP.setQuienPaga(((Double) vRowTemp.elementAt(8)).intValue());

            oSP.setObs((String) vRowTemp.elementAt(13));

        }
        return oSP;
    }

    public int buscaServiciosAuxiliaresDeDiag(int nFolioPac) throws Exception {

        Vector rst = null;
        String sQuery = "";
        int nServiciosAux = 0;

        if (nFolioPac == 0) {
            throw new Exception("Paciente.buscaServiciosAuxiliaresDeDiag.getFolioPac: error de programación, faltan datos");
        } else {
            sQuery = "SELECT * FROM buscaserviciosauxiliaresdediag(" + nFolioPac + ")";
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
                if (((Double) vRowTemp.elementAt(0)).intValue() != 0 && ((Double) vRowTemp.elementAt(1)).intValue() != 0) {
                    nServiciosAux = (((Double) vRowTemp.elementAt(0)).intValue());
                    this.nQuienPaga = (((Double) vRowTemp.elementAt(1)).intValue());
                }
            }
        }
        return nServiciosAux;
    }

    public List<ServicioPrestado> todosServsPrstsParaOtorg(String nArea, boolean bAuxdiag2) throws Exception {
    List<ServicioPrestado> listRet = new ArrayList<ServicioPrestado>();
    ServicioPrestado oSP;
    Valida oValida = new Valida();
    Vector rst = null;
        String sQuery = "";

        sQuery = "SELECT * FROM buscatodosserviciosprestadosparaOtorg(" + nArea + "::int2);";
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
                oSP = new ServicioPrestado();
                Vector vRowTemp = (Vector) rst.elementAt(i);
                oSP.setIdFolio((String) vRowTemp.elementAt(0));
                oEpisodioMedico = new EpisodioMedico();
                oEpisodioMedico.buscaMedTratEpi(((Double) vRowTemp.elementAt(1)).intValue());
                oSP.setEpisodioMedico(oEpisodioMedico);
                Medico oNvoMedico = oEpisodioMedico.getMedTratante();
                oSP.setMedico(oNvoMedico);
                Paciente oNvoPaciente = new Paciente();
                oNvoPaciente = oNvoPaciente.buscaPaciente(((Double) vRowTemp.elementAt(2)).intValue());
                oSP.setPaciente(oNvoPaciente);
                oSP.setRegistro((Date) vRowTemp.elementAt(5));
                oSP.setCostoOriginal(((Double) vRowTemp.elementAt(6)).floatValue());
                oSP.setQuienPaga(((Double) vRowTemp.elementAt(8)).intValue());
                oSP.setConvQuienPaga(oValida.QuienPaga(oSP.getQuienPaga()));
                oSP.setObs((String) vRowTemp.elementAt(13));
                ConceptoIngreso oCI = new ConceptoIngreso();
                oCI.setCveConcep(((Double) vRowTemp.elementAt(17)).intValue());
                oCI = oCI.buscaConceptoIngreso();
                oSP.setConcepPrestado(oCI);
                if (oCI.getDescripcion().contains("URGENCIA")) {
                    oSP.setConcepUrgencia(true);
                }
                if (oCI.getLineaIngreso().getCveLin() == 6) {
                    oSP.setLineaEndos(true);
                }
                oSP.setBAuxdiag(bAuxdiag2);
                listRet.add(oSP);
            }
        }
        return listRet;
    }

    public String insertarServicioPrestado(int idLinea, String idMatMed, 
            String sArea, String sTipoServicio) throws Exception {
        Vector rst = null;
        String sQuery = "";
        String sMedico = "";
        if (oMedico.getFolioPers() == 0) {
            sMedico = "null";
        } else {
            sMedico = "" + oMedico.getFolioPers();
        }

        if (sTipoServicio.equals("") || this.oPaciente == null || 
                this.dRegistro == null) {
            throw new Exception("ServicioPrestado.insertarServicioPrestado: error de programación, sTipoServicio faltan datos");
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd h:mm:ss");
            System.out.println(idMatMed+" "+sTipoServicio);
            if (sTipoServicio.equals(ConceptoIngreso.COMPORT_SERV_MED) || 
                    sTipoServicio.equals(ConceptoIngreso.COMPORT_OTRO) ||
                    (idMatMed==null || idMatMed.equals("0"))) {
                sQuery = "SELECT * from insertaServicioPrestado(" + idLinea + "::int2," + 
                        sArea + "::int2," + oEpisodioMedico.getCveepisodio() + "," + 
                        oPaciente.getFolioPac() + ",'" + 
                        new Valida().BooleanToBinarioString(this.bFacturable) + "'::character varying," + 
                        ((this.dRealizado == null) ? null : ("'" + sdf.format(dRealizado) + "'")) + "::timestamp without time zone,'" + 
                        this.dRegistro + "'::timestamp without time zone," + 
                        this.nCostoOriginal + ",16.00," + this.nQuienPaga + "::int2,null,null," + 
                        sMedico + ",'" + oUniMed.getCve() + "'::character varying," + 
                        (this.sObs==null||this.sObs.equals("")?"null":"'"+sObs+"'") + 
                        "::character varying, null::character varying, null::character varying,'N'::character varying," + 
                        this.oConcepPrestado.getCveConcep() + "::int2," + 
                        this.nCantidad + ",null::int8," + this.nCostoCobrado + 
                        "::numeric(10,2), "+ this.nNumOrden+"::int, "+
                        (this.bIndicadoPorMedico==true?"'1'":"'0'") +");";
            }
            else if (sTipoServicio.equals(ConceptoIngreso.COMPORT_MED)) {
                sQuery = "SELECT * from insertaServicioPrestado(" + 
                        idLinea + "::int2," + sArea + "::int2," + 
                        oEpisodioMedico.getCveepisodio() + "," + 
                        oPaciente.getFolioPac() + ",'" + 
                        new Valida().BooleanToBinarioString(this.bFacturable) + 
                        "'::character varying,null::timestamp without time zone,'" + 
                        this.dRegistro + "'::timestamp without time zone," + 
                        this.nCostoOriginal + ",16.00," + this.nQuienPaga + 
                        "::int2,null,'" + idMatMed + "'::character varying," + 
                        sMedico + ",'" + oUniMed.getCve() + "'::character varying,'" +
                        this.sObs + "'::character varying,' '::character varying,' '::character varying,'N'::character varying," + 
                        this.oConcepPrestado.getCveConcep() + "::int2," + 
                        this.nCantidad + ",null::int8," + this.nCostoCobrado + 
                        "::numeric(10,2), "+ this.nNumOrden+"::int);";
            }
            else if (sTipoServicio.equals(ConceptoIngreso.COMPORT_MAT)) {
                sQuery = "SELECT * from insertaServicioPrestado(" + idLinea + 
                        "::int2," + sArea + "::int2," + 
                        oEpisodioMedico.getCveepisodio() + "," + 
                        oPaciente.getFolioPac() + ",'" + 
                        new Valida().BooleanToBinarioString(this.bFacturable) + 
                        "'::character varying,null::timestamp without time zone,'" + 
                        this.dRegistro + "'::timestamp without time zone," + 
                        this.nCostoOriginal + ",16.00," + this.nQuienPaga + 
                        "::int2,'" + idMatMed + "'::character varying,null," + 
                        sMedico + ",'" + oUniMed.getCve() + "'::character varying,'" +
                        this.sObs + "'::character varying,' '::character varying,' '::character varying,'N'::character varying," + 
                        this.oConcepPrestado.getCveConcep() + "::int2," + 
                        this.nCantidad + ",null::int8," + this.nCostoCobrado + 
                        "::numeric(10,2), "+ this.nNumOrden+"::int);";
            }
            System.out.println("aaa"+sQuery);
            if (getAD() == null) {
                setAD(new AccesoDatos());
                getAD().conectar();
                rst = getAD().ejecutarConsulta(sQuery);
                getAD().desconectar();
                setAD(null);
            }
        }
        return new Valida().eliminaMSJCorchetes("" + (rst==null?"":rst.get(0)));
    }
    
    public void buscaNumOrden() throws Exception{
    Vector rst = null;
    String sQuery = "";
        sQuery = "SELECT nNumOrden FROM servicioPrestado WHERE nIdFolio='"+
                this.sIdFolio+"';";
        if (getAD() == null) {
            setAD(new AccesoDatos());
            getAD().conectar();
            rst = getAD().ejecutarConsulta(sQuery);
            getAD().desconectar();
            setAD(null);
        }
        if (rst!=null){
            this.nNumOrden = ((Double)((Vector)rst.elementAt(0)).elementAt(0)).intValue();
        }
    }

    public String otorgarServicio() throws Exception {
        Vector rst = null;
        String sQuery = "";

        if (sIdFolio.equals("") || oPersHospRealiza == null) {
            throw new Exception("Funcion.actualizaFechaEntradaServ: error de programación, faltan datos");
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd h:mm:ss");
            sQuery = "SELECT * from otorgarservicio('" + sIdFolio + "'::character varying(15)," + this.oPersHospRealiza.getFolioPers() + "," + ((this.dFechaEntradaServ != null) ? ("'" + sdf.format(this.dFechaEntradaServ) + "'") : "null") + "::timestamp without time zone,1::smallint, " + (this.sAuxdiag == null ? "null" : ("'" + sAuxdiag + "'::text")) + "," + ((this.getTecnico() == null) ? null : (this.getTecnico().getFolioPers())) + "," + ((this.getEnfermera() == null) ? null : (this.getEnfermera().getFolioPers())) + "," + ((this.dsalidaservicio != null) ? ("'" + sdf.format(this.dsalidaservicio) + "'") : "null") + "::timestamp without time zone);";
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

    public String actualizaFechaEntradaServ() throws Exception {
        Vector rst = null;
        String sQuery = "";
        String sMsj = "";
        if (this.dFechaEntradaServ != null) {
            if (sIdFolio.equals("") || oPaciente == null) {
                throw new Exception("Funcion.actualizaFechaEntradaServ: error de programación, faltan datos");
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd h:mm:ss");
                sQuery = "SELECT * from otorgarservicio('" + sIdFolio + "'::character varying(15)," + this.oPaciente.getFolioPac() + ",'" + sdf.format(this.dFechaEntradaServ) + "'::timestamp without time zone,2::smallint," + (this.sAuxdiag == null ? "null" : ("'" + sAuxdiag + "'::text")) + ",null,null," + ((this.dsalidaservicio != null) ? ("'" + sdf.format(this.dsalidaservicio) + "'") : "null") + "::timestamp without time zone);";
                if (getAD() == null) {
                    setAD(new AccesoDatos());
                    getAD().conectar();
                    rst = getAD().ejecutarConsulta(sQuery);
                    getAD().desconectar();
                    setAD(null);
                }
            }
            sMsj = "" + (rst==null?"":rst.get(0));
        } else {
            sMsj = "Datos actualizados del servicio :" + sIdFolio;
        }
        return sMsj;
    }

    public String actualizarRequesicion(int nRequesicion) throws Exception {
        Vector rst = null;
        String sQuery = "";

        if (sIdFolio.equals("") || nRequesicion == 0) {
            throw new Exception("Funcion.actualizarRequesicion: error de programación, faltan datos");
        } else {
            sQuery = "SELECT * from actualizarequesicionservicioprestado('" + sIdFolio + "'::character varying," + nRequesicion + ");";
            System.out.println("QueryACRQ:" + sQuery);
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

    public String guardaDatosCE(ServicioPrestado oSP) throws Exception {
        Vector rst = null;
        String sQuery = "";

        if (oSP == null) {
            throw new Exception("ServicioPrestado.guardaCE: error de programación, faltan datos");
        } else {
            sQuery = "select * from modificaServicioPEC('" + oSP.getIdFolio() + "', '" + oSP.getObs()
                    + "', '" + oSP.getReceta() + "', " + oSP.getReqConsulta() + ", " + oSP.getReqHosp() + ", "
                    + oSP.getEpisodioMedico().getMedRecibe().getFolioPers() + ", " + oSP.getMedRealiza().getFolioPers() + ")";
            System.out.println(sQuery);
            if (getAD() == null) {
                setAD(new AccesoDatos());
                getAD().conectar();
                rst = getAD().ejecutarConsulta(sQuery);
                getAD().desconectar();
                setAD(null);
            }
        }
        return "" + (rst==null?"":
        rst.get(0).toString().substring(1, rst.get(0).toString().length() - 1));
    }

    public List<ServicioPrestado> buscaServiciosPrestadosPorEpisodioMed() throws Exception {
        //Busca servicios de un paciente específico
        List<ServicioPrestado> listRet = new ArrayList<ServicioPrestado>();
        ServicioPrestado oSP;

        Vector rst = null;
        String sQuery = "";

        sQuery = "select * from buscarTodosServiciosPorEpisodioMedico(" + this.oEpisodioMedico.getCveepisodio() + ") ";
        
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
                oSP = new ServicioPrestado();
                Vector vRowTemp = (Vector) rst.elementAt(i);
                /* nidfolio(0), cveepisodio(1), nfoliopaciente(2), bfacturable(3), drealizado(4), dregistro(5), ncostooriginal(6), npctiva(7), 
                 nquienpaga(8), scvematerial(9), scvemedicamento(10), nfoliopers(11), ncveunimed(12), sobservaciones(13), sprocedimiento(14), 
                 sreceta(15), ssituacion(16), ncveconcepingr(17), sdescripcion(18), ncantidad(19), ncveexfis(20), ncostocobrado(21), nidemp(22) */
                oSP.setIdFolio((String) vRowTemp.elementAt(0));
                oEpisodioMedico = new EpisodioMedico();
                oEpisodioMedico.setCveepisodio(((Double) vRowTemp.elementAt(1)).intValue());
                oSP.setEpisodioMedico(oEpisodioMedico);
                oPaciente = new Paciente();
                oPaciente.setFolioPac(((Double) vRowTemp.elementAt(2)).intValue());
                oSP.setFacturable(((String) vRowTemp.elementAt(3)).equals("1"));
                oSP.setRealizado((Date) vRowTemp.elementAt(4));
                oSP.setRegistro((Date) vRowTemp.elementAt(5));
                oSP.setCostoOriginal(((Double) vRowTemp.elementAt(6)).floatValue());
                oSP.setPctIVA(((Double) vRowTemp.elementAt(7)).floatValue());
                oSP.setQuienPaga(((Double) vRowTemp.elementAt(8)).intValue());
                oSP.setReceta((String) vRowTemp.elementAt(10));
                oMedico = new Medico();
                oMedico.setFolioPers(((Double) vRowTemp.elementAt(11)).intValue());
                oSP.setMedico(oMedico);
                oUniMed = new UnidadMedida();
                oUniMed.setCve((String) vRowTemp.elementAt(12));
                oSP.setUniMed(oUniMed);
                oSP.setObs((String) vRowTemp.elementAt(13));
                oSP.setProcedimiento((String) vRowTemp.elementAt(14));
                oSP.setReceta((String) vRowTemp.elementAt(15));
                oSP.setSituacion((String) vRowTemp.elementAt(16));
                oConcepPrestado = new ConceptoIngreso();
                oConcepPrestado.setCveConcep(((Double) vRowTemp.elementAt(17)).intValue());
                oConcepPrestado.setDescripConcep((String) vRowTemp.elementAt(18));
                oSP.setConcepPrestado(oConcepPrestado);
                oSP.setCantidad(((Double) vRowTemp.elementAt(19)).intValue());
                oEF = new ExamenFisico();
                oEF.setCveExFis(((Double) vRowTemp.elementAt(20)).intValue());
                oSP.setExamenFisico(oEF);
                oSP.setCostoCobrado(((Double) vRowTemp.elementAt(21)).floatValue());
                if (oSP.getCostoCobrado() == 0) {
                    oSP.setCostoCobrado(oSP.getCostoOriginal());
                }
                oCompaniaCred = new CompaniaCred();
                oCompaniaCred.setIdEmp(((Double) vRowTemp.elementAt(22)).intValue());
                oSP.setCompaniaCred(oCompaniaCred);
                listRet.add(oSP);
            }
        }
        return listRet;
    }

    public String modificarObsPrecio() throws Exception {
        Vector rst = null;
        String sQuery = "";

        if (this.sIdFolio.equals("") && this.sObsPrecio.equals("")) {
            throw new Exception("ServicioPrestado.modificarObsPrecio: error de programación, faltan datos");
        } else {
            sQuery = "select * from modificaObsPrecioServPrestado('" + this.sIdFolio + "','" + this.sObsPrecio + "')";
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

    public String modificarCompaniaCred() throws Exception {
        Vector rst = null;
        String sQuery = "";

        if (this.sIdFolio.equals("") && this.oCompaniaCred.getIdEmp() == 0) {
            throw new Exception("ServicioPrestado.modificarCompaniaCredito: error de programación, faltan datos");
        } else {
            if (this.oCompaniaCred.getIdEmp() == 0) {
                sQuery = "select * from modificaCompaniaCredServPrestado('" + this.sIdFolio + "',null)";
            } else {
                sQuery = "select * from modificaCompaniaCredServPrestado('" + this.sIdFolio + "'," + this.oCompaniaCred.getIdEmp() + "::smallint)";
            }
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

    public String cancelarCargo() throws Exception {
        Vector rst = null;
        String sQuery = "";

        if (this.sIdFolio.equals("")) {
            throw new Exception("ServicioPrestado.cancelar cargo: error de programación, faltan datos");
        } else {
            sQuery = "select * from cancelarCargo('" + this.sIdFolio + "'," + this.oEpisodioMedico.getCveepisodio() + "," + this.oPaciente.getFolioPac() + ")";
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

    public List<ServicioPrestado> buscaTodosServiciosConsumidosPaquete(int nCveEpisodio) throws Exception {
        List<ServicioPrestado> listaServCons = new ArrayList<ServicioPrestado>();
        if (nCveEpisodio == 0) {
            throw new Exception("ServicioPrestado. buscaTodosServiciosConsumidosPaquete(): Error de programación. Faltan datos.");
        } else {
            ServicioPrestado oSP = null;
            Vector rst = null;
            Vector<ServicioPrestado> vObj = null;

            String sQuery = "";
            int i = 0, nTam = 0;

            sQuery = "SELECT * FROM buscadeserviciosconsumidospaquete(" + nCveEpisodio + ");";
            System.out.println("sQuery:" + sQuery);
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
                vObj = new Vector<ServicioPrestado>();
                for (i = 0; i < rst.size(); i++) {
                    oSP = new ServicioPrestado();
                    Vector vRowTemp = (Vector) rst.elementAt(i);
                    oSP.setIdFolio((String) vRowTemp.elementAt(0));
                    oSP.setRegistro((Date) vRowTemp.elementAt(1));
                    ConceptoIngreso oConceptoIngreso = new ConceptoIngreso();
                    oConceptoIngreso.setCveConcep(((Double) vRowTemp.elementAt(2)).intValue());
                    oConceptoIngreso = oConceptoIngreso.buscaConceptoIngreso();
                    oConceptoIngreso.setIdGenerico("SO-" + ((Double) vRowTemp.elementAt(2)).intValue());
                    oConceptoIngreso.setTipoConcIngr("Servicio medico");
                    if (!((String) vRowTemp.elementAt(3)).equals("")) {
                        Medicamento oM = new Medicamento();
                        oM = oM.buscaMedicamentoVitrina((String) vRowTemp.elementAt(3));
                        oConceptoIngreso.setMedicamento(oM);
                        oConceptoIngreso.setDescripcion(oM.getNomMedicamento());
                        oConceptoIngreso.setIdGenerico("ME-" + (String) vRowTemp.elementAt(3));
                        oConceptoIngreso.setTipoConcIngr("medicamento");
                    }
                    if (!((String) vRowTemp.elementAt(4)).equals("")) {
                        MaterialCuracion oM = new MaterialCuracion();
                        oM = oM.buscaMaterialCuracion((String) vRowTemp.elementAt(4));
                        oConceptoIngreso.setMaterialCuracion(oM);
                        oConceptoIngreso.setDescripcion(oM.getDescripcion());
                        oConceptoIngreso.setIdGenerico("MC-" + (String) vRowTemp.elementAt(4));
                        oConceptoIngreso.setTipoConcIngr("material");
                    }
                    oConceptoIngreso.setCantidad(((Double) vRowTemp.elementAt(5)).intValue());
                    UnidadMedida oUM = new UnidadMedida();
                    oUM = oUM.buscaUnidadMedidad((String) vRowTemp.elementAt(6));
                    oConceptoIngreso.setUnidadMedida(oUM);
                    oSP.setConcepPrestado(oConceptoIngreso);
                    listaServCons.add(oSP);
                }

            }
        }
        return listaServCons;
    }

    /**
     * @autor Miguel Angel Damian Hernandez
     * @fecha Agosto 2014
     * @version Agregado a v3.2
     * @description Busca servicios pendientes de recibir de Farmacia de un paciente específico
     */
    public List<ServicioPrestado> buscarServiciosPendientesFarmacia(int nFolioPac) throws Exception {
        List<ServicioPrestado> listRet = new ArrayList<ServicioPrestado>();
        ServicioPrestado oSP;

        Vector rst = null;
        String sQuery = "";

        sQuery = "SELECT * FROM buscaServiciosPrestadosPendFarmacia(" + nFolioPac + ") ";
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
                oSP = new ServicioPrestado();
                Vector vRowTemp = (Vector) rst.elementAt(i);
                Paciente paciente = new Paciente();
                paciente.setFolioPac(((Double) vRowTemp.elementAt(0)).intValue());
                oSP.setPaciente(paciente);
                String sDescripcionMat = (String) vRowTemp.elementAt(1).toString();
                if (!"".equals(sDescripcionMat) || sDescripcionMat != null) {
                    MaterialCuracion mc = new MaterialCuracion();
                    mc.setDescripcion(sDescripcionMat);
                    oSP.setMaterialCuracion(mc);
                }
                String sDescripcionMed = (String) vRowTemp.elementAt(2).toString();
                if (!"".equals(sDescripcionMed) || sDescripcionMed != null) {
                    Medicamento m = new Medicamento();
                    m.setNomMedicamento(sDescripcionMed);
                    oSP.setMedicamento(m);
                }
                UnidadMedida um = new UnidadMedida();
                um.setDescrip((String) vRowTemp.elementAt(3).toString());
                oSP.setUniMed(um);
                oSP.setCantidad(((Double) vRowTemp.elementAt(4)).intValue());
                oSP.setRecepFarm(((Date) vRowTemp.elementAt(5)));
                oSP.setRequisicionFarm(((Double) vRowTemp.elementAt(6)).intValue());
                oSP.setSitFarmacia((String) vRowTemp.elementAt(7).toString());
                oSP.setIdFolio((String) vRowTemp.elementAt(8).toString());
                EpisodioMedico em = new EpisodioMedico();
                em.setCveepisodio(((Double) vRowTemp.elementAt(9)).intValue());
                oSP.setEpisodioMedico(em);
                oSP.setRegistro(((Date) vRowTemp.elementAt(10)));
                oSP.setNumOrden((((Double) vRowTemp.elementAt(11)).intValue()));
                listRet.add(oSP);
            }
        }
        return listRet;
    }

    /**
     * @autor Miguel Angel Damian Hernandez
     * @fecha Agosto 2014
     * @version Agregado a v3.2
     * @description Cambia la situacion farmacia del Servicio Prestado: de E(Enviado a Farmaci) a R(Recibido de farmacia)
     */
    public String actualizaSituacionFarmacia() throws Exception {
        Vector rst = null;
        String sQuery = "";
        if (this.sIdFolio.equals("")) {
            throw new Exception("ServicioPrestado.actualizaSituacionFarmacia: error de programación, faltan datos");
        } else {
            sQuery = "select * from actualizaServPrestSitFarm('" + this.sIdFolio + "'," + this.oEpisodioMedico.getCveepisodio() + "," + this.oPaciente.getFolioPac() + ")";
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

    /**
     * @autor Miguel Angel Damian Hernandez
     * @fecha Diciembre 2014
     * @version Agregado a v5...
     * @description Obtiene valores necesarios para la impresion de ticket en registrarVentaDeServicio.xhtml
     */
    public ServicioPrestado buscaServicioPrestadoDeHospitalizado(int nCveServPrest, int nIdPac) throws Exception {
        ServicioPrestado oSP = null;
        Vector rst = null;
        String sQuery = "";

        sQuery = "SELECT * FROM buscaservicioprestadodeingresohosp(" + nCveServPrest + ")";
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

            oSP = new ServicioPrestado();
            Vector vRowTemp = (Vector) rst.elementAt(0);
            oSP.setIdFolio((String) vRowTemp.elementAt(0));
            oEpisodioMedico = new EpisodioMedico();
            oEpisodioMedico.buscaMedTratEpi(((Double) vRowTemp.elementAt(1)).intValue());
            oSP.setEpisodioMedico(oEpisodioMedico);
            Medico oNvoMedico = oEpisodioMedico.getMedTratante();
            oSP.setMedico(oNvoMedico);
            Paciente oNvoPaciente = new Paciente();
            oNvoPaciente = oNvoPaciente.buscaPaciente(((Double) vRowTemp.elementAt(2)).intValue());
            oSP.setFacturable(new Valida().BinarioStringToBoolean((String) vRowTemp.elementAt(3)));
            oSP.setPaciente(oNvoPaciente);
            oSP.setRegistro((Date) vRowTemp.elementAt(5));
            oSP.setCostoOriginal(((Double) vRowTemp.elementAt(6)).floatValue());
            oSP.setQuienPaga(((Double) vRowTemp.elementAt(8)).intValue());

            oSP.setObs((String) vRowTemp.elementAt(13));

        }
        return oSP;
    }

//--CIERRE Y PRECIERRE------------------------------------------------------------------------------------------------------------
    public List<ServicioPrestado> buscaServiciosPrestadosNoPagadosPorEpisodioMed() throws Exception {
        //Busca servicios de un paciente específico
        List<ServicioPrestado> listRet = new ArrayList<ServicioPrestado>();
        ServicioPrestado oSP;

        Vector rst = null;
        String sQuery = "";

        sQuery = "SELECT * FROM buscarTodosServiciosNoPagadosPorEpisodioMedico(" +
                this.oPaciente.getFolioPac() + ", " +
                this.oEpisodioMedico.getCveepisodio() + ") ";
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
                oSP = new ServicioPrestado();
                Vector vRowTemp = (Vector) rst.elementAt(i);
                /* nidfolio(0), cveepisodio(1), nfoliopaciente(2), bfacturable(3), drealizado(4), dregistro(5), ncostooriginal(6), npctiva(7), 
                 nquienpaga(8), scvematerial(9), scvemedicamento(10), nfoliopers(11), ncveunimed(12), sobservaciones(13), sprocedimiento(14), 
                 sreceta(15), ssituacion(16), ncveconcepingr(17), sdescripcion(18), ncantidad(19), ncveexfis(20), ncostocobrado(21), nidemp(22) */
                oSP.setIdFolio((String) vRowTemp.elementAt(0));
                oEpisodioMedico = new EpisodioMedico();
                oEpisodioMedico.setCveepisodio(((Double) vRowTemp.elementAt(1)).intValue());
                oSP.setEpisodioMedico(oEpisodioMedico);
                oPaciente = new Paciente();
                oPaciente.setFolioPac(((Double) vRowTemp.elementAt(2)).intValue());
                oSP.setFacturable(((String) vRowTemp.elementAt(3)).equals("1"));
                oSP.setRealizado((Date) vRowTemp.elementAt(4));
                oSP.setRegistro((Date) vRowTemp.elementAt(5));
                oSP.setCostoOriginal(((Double) vRowTemp.elementAt(6)).floatValue());
                oSP.setPctIVA(((Double) vRowTemp.elementAt(7)).floatValue());
                oSP.setQuienPaga(((Double) vRowTemp.elementAt(8)).intValue());
                oSP.setReceta((String) vRowTemp.elementAt(10));
                oMedico = new Medico();
                oMedico.setFolioPers(((Double) vRowTemp.elementAt(11)).intValue());
                oSP.setMedico(oMedico);
                oUniMed = new UnidadMedida();
                oUniMed.setCve((String) vRowTemp.elementAt(12));
                oSP.setUniMed(oUniMed);
                oSP.setObs((String) vRowTemp.elementAt(13));
                oSP.setProcedimiento((String) vRowTemp.elementAt(14));
                oSP.setReceta((String) vRowTemp.elementAt(15));
                oSP.setSituacion((String) vRowTemp.elementAt(16));
                oConcepPrestado = new ConceptoIngreso();
                oConcepPrestado.setCveConcep(((Double) vRowTemp.elementAt(17)).intValue());
                oConcepPrestado.setDescripConcep((String) vRowTemp.elementAt(18));
                oSP.setConcepPrestado(oConcepPrestado);
                oSP.setCantidad(((Double) vRowTemp.elementAt(19)).intValue());
                oEF = new ExamenFisico();
                oEF.setCveExFis(((Double) vRowTemp.elementAt(20)).intValue());
                oSP.setExamenFisico(oEF);
                oSP.setCostoCobrado(((Double) vRowTemp.elementAt(21)).floatValue());
                if (oSP.getCostoCobrado() == 0) {
                    oSP.setCostoCobrado(oSP.getCostoOriginal());
                }
                oCompaniaCred = new CompaniaCred();
                oCompaniaCred.setIdEmp(((Double) vRowTemp.elementAt(22)).intValue());
                oSP.setCompaniaCred(oCompaniaCred);
                oSP.setObsPrecio((String) vRowTemp.elementAt(23));
                oSP.setNumPoliza((String) vRowTemp.elementAt(24));
                oSP.setSitFarmacia((String) vRowTemp.elementAt(25));
                listRet.add(oSP);
            }
        }
        return listRet;
    }

    public List<ServicioPrestado> buscaServiciosPrestadosPorEpisodioMedPagosExterno() throws Exception {
        //Busca servicios de un paciente específico
        List<ServicioPrestado> listRet = new ArrayList<ServicioPrestado>();
        ServicioPrestado oSP;

        Vector rst = null;
        String sQuery = "";

        sQuery = "SELECT * FROM buscarTodosServiciosPorEpisodioMedicoPagosExternos(" + 
                this.oPaciente.getFolioPac() + ", " +
                this.oEpisodioMedico.getCveepisodio() + ") ";
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
                oSP = new ServicioPrestado();
                Vector vRowTemp = (Vector) rst.elementAt(i);
                /* nidfolio(0), cveepisodio(1), nfoliopaciente(2), bfacturable(3), drealizado(4), dregistro(5), ncostooriginal(6), npctiva(7), 
                 nquienpaga(8), scvematerial(9), scvemedicamento(10), nfoliopers(11), ncveunimed(12), sobservaciones(13), sprocedimiento(14), 
                 sreceta(15), ssituacion(16), ncveconcepingr(17), sdescripcion(18), ncantidad(19), ncveexfis(20), ncostocobrado(21), nidemp(22) */
                oSP.setIdFolio((String) vRowTemp.elementAt(0));
                oEpisodioMedico = new EpisodioMedico();
                oEpisodioMedico.setCveepisodio(((Double) vRowTemp.elementAt(1)).intValue());
                oSP.setEpisodioMedico(oEpisodioMedico);
                oPaciente = new Paciente();
                oPaciente.setFolioPac(((Double) vRowTemp.elementAt(2)).intValue());
                oSP.setFacturable(((String) vRowTemp.elementAt(3)).equals("1"));
                oSP.setRealizado((Date) vRowTemp.elementAt(4));
                oSP.setRegistro((Date) vRowTemp.elementAt(5));
                oSP.setCostoOriginal(((Double) vRowTemp.elementAt(6)).floatValue());
                oSP.setPctIVA(((Double) vRowTemp.elementAt(7)).floatValue());
                oSP.setQuienPaga(((Double) vRowTemp.elementAt(8)).intValue());
                oSP.setReceta((String) vRowTemp.elementAt(10));
                oMedico = new Medico();
                oMedico.setFolioPers(((Double) vRowTemp.elementAt(11)).intValue());
                oSP.setMedico(oMedico);
                oUniMed = new UnidadMedida();
                oUniMed.setCve((String) vRowTemp.elementAt(12));
                oSP.setUniMed(oUniMed);
                oSP.setObs((String) vRowTemp.elementAt(13));
                oSP.setProcedimiento((String) vRowTemp.elementAt(14));
                oSP.setReceta((String) vRowTemp.elementAt(15));
                oSP.setSituacion((String) vRowTemp.elementAt(16));
                oConcepPrestado = new ConceptoIngreso();
                oConcepPrestado.setCveConcep(((Double) vRowTemp.elementAt(17)).intValue());
                oConcepPrestado.setDescripConcep((String) vRowTemp.elementAt(18));
                oSP.setConcepPrestado(oConcepPrestado);
                oSP.setCantidad(((Double) vRowTemp.elementAt(19)).intValue());
                oEF = new ExamenFisico();
                oEF.setCveExFis(((Double) vRowTemp.elementAt(20)).intValue());
                oSP.setExamenFisico(oEF);
                oSP.setCostoCobrado(((Double) vRowTemp.elementAt(21)).floatValue());
                if (oSP.getCostoCobrado() == 0) {
                    oSP.setCostoCobrado(oSP.getCostoOriginal());
                }
                oCompaniaCred = new CompaniaCred();
                oCompaniaCred.setIdEmp(((Double) vRowTemp.elementAt(22)).intValue());
                oSP.setCompaniaCred(oCompaniaCred);
                oSP.setObsPrecio((String) vRowTemp.elementAt(23));
                oSP.setNumPoliza((String) vRowTemp.elementAt(24));
                oSP.setSitFarmacia((String) vRowTemp.elementAt(25));
                listRet.add(oSP);
            }
        }
        return listRet;
    }

    public List<ServicioPrestado> buscaServiciosPrestadosPorEpisodioMedAnticipos() throws Exception {
        //Busca servicios de un paciente específico
        List<ServicioPrestado> listRet = new ArrayList<ServicioPrestado>();
        ServicioPrestado oSP;

        Vector rst = null;
        String sQuery = "";

        sQuery = "SELECT * FROM buscarTodosServiciosPorEpisodioMedicoAnticipos(" +
                this.oPaciente.getFolioPac() + ", " +
                this.oEpisodioMedico.getCveepisodio() + ") ";
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
                oSP = new ServicioPrestado();
                Vector vRowTemp = (Vector) rst.elementAt(i);
                /* nidfolio(0), cveepisodio(1), nfoliopaciente(2), bfacturable(3), drealizado(4), dregistro(5), ncostooriginal(6), npctiva(7), 
                 nquienpaga(8), scvematerial(9), scvemedicamento(10), nfoliopers(11), ncveunimed(12), sobservaciones(13), sprocedimiento(14), 
                 sreceta(15), ssituacion(16), ncveconcepingr(17), sdescripcion(18), ncantidad(19), ncveexfis(20), ncostocobrado(21), nidemp(22) */
                oSP.setIdFolio((String) vRowTemp.elementAt(0));
                oEpisodioMedico = new EpisodioMedico();
                oEpisodioMedico.setCveepisodio(((Double) vRowTemp.elementAt(1)).intValue());
                oSP.setEpisodioMedico(oEpisodioMedico);
                oPaciente = new Paciente();
                oPaciente.setFolioPac(((Double) vRowTemp.elementAt(2)).intValue());
                oSP.setFacturable(((String) vRowTemp.elementAt(3)).equals("1"));
                oSP.setRealizado((Date) vRowTemp.elementAt(4));
                oSP.setRegistro((Date) vRowTemp.elementAt(5));
                oSP.setCostoOriginal(((Double) vRowTemp.elementAt(6)).floatValue());
                oSP.setPctIVA(((Double) vRowTemp.elementAt(7)).floatValue());
                oSP.setQuienPaga(((Double) vRowTemp.elementAt(8)).intValue());
                oSP.setReceta((String) vRowTemp.elementAt(10));
                oMedico = new Medico();
                oMedico.setFolioPers(((Double) vRowTemp.elementAt(11)).intValue());
                oSP.setMedico(oMedico);
                oUniMed = new UnidadMedida();
                oUniMed.setCve((String) vRowTemp.elementAt(12));
                oSP.setUniMed(oUniMed);
                oSP.setObs((String) vRowTemp.elementAt(13));
                oSP.setProcedimiento((String) vRowTemp.elementAt(14));
                oSP.setReceta((String) vRowTemp.elementAt(15));
                oSP.setSituacion((String) vRowTemp.elementAt(16));
                oConcepPrestado = new ConceptoIngreso();
                oConcepPrestado.setCveConcep(((Double) vRowTemp.elementAt(17)).intValue());
                oConcepPrestado.setDescripConcep((String) vRowTemp.elementAt(18));
                oSP.setConcepPrestado(oConcepPrestado);
                oSP.setCantidad(((Double) vRowTemp.elementAt(19)).intValue());
                oEF = new ExamenFisico();
                oEF.setCveExFis(((Double) vRowTemp.elementAt(20)).intValue());
                oSP.setExamenFisico(oEF);
                oSP.setCostoCobrado(((Double) vRowTemp.elementAt(21)).floatValue());
                if (oSP.getCostoCobrado() == 0) {
                    oSP.setCostoCobrado(oSP.getCostoOriginal());
                }
                oCompaniaCred = new CompaniaCred();
                oCompaniaCred.setIdEmp(((Double) vRowTemp.elementAt(22)).intValue());
                oSP.setCompaniaCred(oCompaniaCred);
                oSP.setObsPrecio((String) vRowTemp.elementAt(23));
                oSP.setNumPoliza((String) vRowTemp.elementAt(24));
                listRet.add(oSP);
            }
        }
        return listRet;
    }

    public String actualizaNumPoliza() throws Exception {
        Vector rst = null;
        String sQuery = "";

        if (this.sIdFolio.isEmpty() || this.sNumPoliza.isEmpty()) {
            throw new Exception("ServicioPrestado.actualizaNumPoliza: error de programación, faltan datos");
        } else {
            sQuery = "select * from actualizaNumPoliza('" + this.sIdFolio + "', '" + this.sNumPoliza + "')";
            System.out.println(sQuery);
            if (getAD() == null) {
                setAD(new AccesoDatos());
                getAD().conectar();
                rst = getAD().ejecutarConsulta(sQuery);
                getAD().desconectar();
                setAD(null);
            }
        }
        return " " + this.sNumPoliza;
    }

    public List<ServicioPrestado> buscaServiciosPrestadosPaqHonorariosMedico(Date fechaIni, Date fechaFin) throws Exception {
        //Busca servicios de un paciente específico
        List<ServicioPrestado> listRet = new ArrayList<ServicioPrestado>();
        ServicioPrestado oSP;

        Vector rst = null;
        String sQuery = "";

        sQuery = " SELECT * FROM buscaTodosHonorariosMedico ('" + fechaIni + "','" + fechaFin + "') , episodiomedico"
                + " WHERE poutnquienpaga =2 AND poutcveepisodio=cveepisodio AND ntipoprincipal=2 ";
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
                oSP = new ServicioPrestado();
                Vector vRowTemp = (Vector) rst.elementAt(i);
                /*  ph.nfoliopers(0), ph.snombre(1), ph.sappaterno(2), ph.sapmaterno(3), p.nfoliopaciente(4),
                 p.snombre(5), p.sappaterno(6), p.sapmaterno(7), ci.ncveconcepingr(8), ci.sdescripcion(9),
                 sp.nidfolio(10), sp.cveepisodio(11),  sp.bfacturable(12), sp.drealizado(13), sp.dregistro(14),
                 sp.ncostooriginal(15), sp.npctiva(16), sp.nquienpaga(17), sp.scvematerial(18), sp.scvemedicamento(19),
                 sp.nfoliopers(20), sp.ncveunimed(21), sp.sobservaciones(22), sp.sprocedimiento(23), sp.sreceta(24),
                 sp.ssituacion(25), sp.ncantidad(26), sp.ncveexfis(27), sp.ncostocobrado(28), sp.nidemp(29), 
                 sp.dentradaalserv(30), sp.sdiagaux(31) */
                Medico med = new Medico();
                med.setFolioPers(((Double) vRowTemp.elementAt(0)).intValue());
                med.setNombre((String) vRowTemp.elementAt(1));
                med.setApellidoPaterno((String) vRowTemp.elementAt(2));
                med.setApellidoMaterno((String) vRowTemp.elementAt(3));
                oSP.setMedico((Medico) med);

                Paciente oPac = new Paciente();
                oPac.setFolioPac(((Double) vRowTemp.elementAt(4)).intValue());
                oPac.setNombre((String) vRowTemp.elementAt(5));
                oPac.setApellidoPaterno((String) vRowTemp.elementAt(6));
                oPac.setApellidoMaterno((String) vRowTemp.elementAt(7));
                oSP.setPaciente(oPac);

                oConcepPrestado = new ConceptoIngreso();
                oConcepPrestado.setCveConcep(((Double) vRowTemp.elementAt(8)).intValue());
                oConcepPrestado.setDescripConcep((String) vRowTemp.elementAt(9));
                oSP.setConcepPrestado(oConcepPrestado);

                oSP.setIdFolio((String) vRowTemp.elementAt(10));
                oEpisodioMedico = new EpisodioMedico();
                oEpisodioMedico.setCveepisodio(((Double) vRowTemp.elementAt(11)).intValue());
                oSP.setEpisodioMedico(oEpisodioMedico);

                oSP.setFacturable(((String) vRowTemp.elementAt(12)).equals("1"));
                oSP.setRealizado((Date) vRowTemp.elementAt(13));
                oSP.setRegistro((Date) vRowTemp.elementAt(14));
                oSP.setCostoOriginal(((Double) vRowTemp.elementAt(15)).floatValue());
                oSP.setPctIVA(((Double) vRowTemp.elementAt(16)).floatValue());
                oSP.setQuienPaga(((Double) vRowTemp.elementAt(17)).intValue());

                //Medico medTrat= new Medico();
                //medTrat.setFolioPers(((Double) vRowTemp.elementAt(20)).intValue());
                //oSP.setMedico(medTrat);
                oUniMed = new UnidadMedida();
                oUniMed.setCve((String) vRowTemp.elementAt(21));
                oSP.setUniMed(oUniMed);
                oSP.setObs((String) vRowTemp.elementAt(22));
                oSP.setProcedimiento((String) vRowTemp.elementAt(23));
                oSP.setReceta((String) vRowTemp.elementAt(24));
                oSP.setSituacion((String) vRowTemp.elementAt(25));
                oSP.setCantidad(((Double) vRowTemp.elementAt(26)).intValue());
                oEF = new ExamenFisico();
                oEF.setCveExFis(((Double) vRowTemp.elementAt(27)).intValue());
                oSP.setExamenFisico(oEF);
                oSP.setCostoCobrado(((Double) vRowTemp.elementAt(28)).floatValue());
                //if(oSP.getCostoCobrado()==0){
                //    oSP.setCostoCobrado(oSP.getCostoOriginal());
                //}
                oCompaniaCred = new CompaniaCred();
                oCompaniaCred.setIdEmp(((Double) vRowTemp.elementAt(29)).intValue());
                oSP.setCompaniaCred(oCompaniaCred);

                listRet.add(oSP);
            }
        }
        return listRet;
    }

    public List<ServicioPrestado> buscaServiciosPrestadosPaqHonorariosEnfermeras(Date fechaIni, Date fechaFin) throws Exception {
        //Busca servicios de un paciente específico
        List<ServicioPrestado> listRet = new ArrayList<ServicioPrestado>();
        ServicioPrestado oSP;

        Vector rst = null;
        String sQuery = "";

        sQuery = " SELECT * FROM buscaTodosHonorariosEnfermeras ('" + fechaIni + "','" + fechaFin + "') , episodiomedico "
                + " WHERE poutnquienpaga =2 AND poutcveepisodio=cveepisodio AND ntipoprincipal=2";
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
                oSP = new ServicioPrestado();
                Vector vRowTemp = (Vector) rst.elementAt(i);
                /*  ph.nfoliopers(0), ph.snombre(1), ph.sappaterno(2), ph.sapmaterno(3), p.nfoliopaciente(4), p.snombre(5),
                 p.sappaterno(6), p.sapmaterno(7), ci.ncveconcepingr(8), ci.sdescripcion(9), sp.nidfolio(10), 
                 sp.cveepisodio(11), sp.bfacturable(12), sp.drealizado(13), sp.dregistro(14), sp.ncostooriginal(15), 
                 sp.npctiva(16), sp.nquienpaga(17), sp.scvematerial(18), sp.scvemedicamento(19), sp.nfoliopers(20), 
                 sp.ncveunimed(21), sp.sobservaciones(22), sp.sprocedimiento(23), sp.sreceta(24), sp.ssituacion(25), 
                 sp.ncantidad(26), sp.ncveexfis(27), sp.ncostocobrado(28), sp.nidemp(29), sp.dentradaalserv(30), 
                 sp.sdiagaux(31)  */
                Medico enf = new Medico(); //Guarda los datos de la enfermera
                enf.setFolioPers(((Double) vRowTemp.elementAt(0)).intValue());
                enf.setNombre((String) vRowTemp.elementAt(1));
                enf.setApellidoPaterno((String) vRowTemp.elementAt(2));
                enf.setApellidoMaterno((String) vRowTemp.elementAt(3));
                oSP.setMedico(enf);

                oPaciente = new Paciente();
                oPaciente.setFolioPac(((Double) vRowTemp.elementAt(4)).intValue());
                oPaciente.setNombre((String) vRowTemp.elementAt(5));
                oPaciente.setApellidoPaterno((String) vRowTemp.elementAt(6));
                oPaciente.setApellidoMaterno((String) vRowTemp.elementAt(7));
                oSP.setPaciente(oPaciente);

                oConcepPrestado = new ConceptoIngreso();
                oConcepPrestado.setCveConcep(((Double) vRowTemp.elementAt(8)).intValue());
                oConcepPrestado.setDescripConcep((String) vRowTemp.elementAt(9));
                oSP.setConcepPrestado(oConcepPrestado);

                oSP.setIdFolio((String) vRowTemp.elementAt(10));
                oEpisodioMedico = new EpisodioMedico();
                oEpisodioMedico.setCveepisodio(((Double) vRowTemp.elementAt(11)).intValue());
                oSP.setEpisodioMedico(oEpisodioMedico);

                oSP.setFacturable(((String) vRowTemp.elementAt(12)).equals("1"));
                oSP.setRealizado((Date) vRowTemp.elementAt(13));
                oSP.setRegistro((Date) vRowTemp.elementAt(14));
                oSP.setCostoOriginal(((Double) vRowTemp.elementAt(15)).floatValue());
                oSP.setPctIVA(((Double) vRowTemp.elementAt(16)).floatValue());
                oSP.setQuienPaga(((Double) vRowTemp.elementAt(17)).intValue());

                //Medico medTrat= new Medico();
                //medTrat.setFolioPers(((Double) vRowTemp.elementAt(20)).intValue());
                //oSP.setMedico(medTrat);
                oUniMed = new UnidadMedida();
                oUniMed.setCve((String) vRowTemp.elementAt(21));
                oSP.setUniMed(oUniMed);
                oSP.setObs((String) vRowTemp.elementAt(22));
                oSP.setProcedimiento((String) vRowTemp.elementAt(23));
                oSP.setReceta((String) vRowTemp.elementAt(24));
                oSP.setSituacion((String) vRowTemp.elementAt(25));
                oSP.setCantidad(((Double) vRowTemp.elementAt(26)).intValue());
                oEF = new ExamenFisico();
                oEF.setCveExFis(((Double) vRowTemp.elementAt(27)).intValue());
                oSP.setExamenFisico(oEF);
                oSP.setCostoCobrado(((Double) vRowTemp.elementAt(28)).floatValue());
                //if(oSP.getCostoCobrado()==0){
                //    oSP.setCostoCobrado(oSP.getCostoOriginal());
                //}
                oCompaniaCred = new CompaniaCred();
                oCompaniaCred.setIdEmp(((Double) vRowTemp.elementAt(29)).intValue());
                oSP.setCompaniaCred(oCompaniaCred);

                listRet.add(oSP);
            }
        }
        return listRet;
    }

    public List<ServicioPrestado> buscaServiciosPrestadosPaqHonorariosTecnicos(Date fechaIni, Date fechaFin) throws Exception {
        //Busca servicios de un paciente específico
        List<ServicioPrestado> listRet = new ArrayList<ServicioPrestado>();
        ServicioPrestado oSP;

        Vector rst = null;
        String sQuery = "";

        sQuery = " SELECT * FROM buscaTodosHonorariosTecnicos ('" + fechaIni + "','" + fechaFin + "') , episodiomedico"
                + " WHERE poutnquienpaga =2 AND poutcveepisodio=cveepisodio AND ntipoprincipal=2";
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
                oSP = new ServicioPrestado();
                Vector vRowTemp = (Vector) rst.elementAt(i);
                /*  ph.nfoliopers(0), ph.snombre(1), ph.sappaterno(2), ph.sapmaterno(3), p.nfoliopaciente(4), 
                 p.snombre(5), p.sappaterno(6), p.sapmaterno(7), ci.ncveconcepingr(8), ci.sdescripcion(9),
                 sp.nidfolio(10), sp.cveepisodio(11), sp.bfacturable(12), sp.drealizado(13), sp.dregistro(14), 
                 sp.ncostooriginal(15), sp.npctiva(16), sp.nquienpaga(17), sp.scvematerial(18), 
                 sp.scvemedicamento(19), sp.nfoliopers(20), sp.ncveunimed(21), sp.sobservaciones(22), 
                 sp.sprocedimiento(23), sp.sreceta(24), sp.ssituacion(25), sp.ncantidad(26), sp.ncveexfis(27), 
                 sp.ncostocobrado(28), sp.nidemp(29), sp.dentradaalserv(30), sp.sdiagaux(31)  */
                Medico tec = new Medico();
                tec.setFolioPers(((Double) vRowTemp.elementAt(0)).intValue());
                tec.setNombre((String) vRowTemp.elementAt(1));
                tec.setApellidoPaterno((String) vRowTemp.elementAt(2));
                tec.setApellidoMaterno((String) vRowTemp.elementAt(3));
                oSP.setMedico(tec);

                Paciente oPac = new Paciente();
                oPac.setFolioPac(((Double) vRowTemp.elementAt(4)).intValue());
                oPac.setNombre((String) vRowTemp.elementAt(5));
                oPac.setApellidoPaterno((String) vRowTemp.elementAt(6));
                oPac.setApellidoMaterno((String) vRowTemp.elementAt(7));
                oSP.setPaciente(oPac);

                oConcepPrestado = new ConceptoIngreso();
                oConcepPrestado.setCveConcep(((Double) vRowTemp.elementAt(8)).intValue());
                oConcepPrestado.setDescripConcep((String) vRowTemp.elementAt(9));
                oSP.setConcepPrestado(oConcepPrestado);

                oSP.setIdFolio((String) vRowTemp.elementAt(10));
                oEpisodioMedico = new EpisodioMedico();
                oEpisodioMedico.setCveepisodio(((Double) vRowTemp.elementAt(11)).intValue());
                oSP.setEpisodioMedico(oEpisodioMedico);

                oSP.setFacturable(((String) vRowTemp.elementAt(12)).equals("1"));
                oSP.setRealizado((Date) vRowTemp.elementAt(13));
                oSP.setRegistro((Date) vRowTemp.elementAt(14));
                oSP.setCostoOriginal(((Double) vRowTemp.elementAt(15)).floatValue());
                oSP.setPctIVA(((Double) vRowTemp.elementAt(16)).floatValue());
                oSP.setQuienPaga(((Double) vRowTemp.elementAt(17)).intValue());

                //Medico medTrat= new Medico();
                //medTrat.setFolioPers(((Double) vRowTemp.elementAt(20)).intValue());
                //oSP.setMedico(medTrat);
                oUniMed = new UnidadMedida();
                oUniMed.setCve((String) vRowTemp.elementAt(21));
                oSP.setUniMed(oUniMed);
                oSP.setObs((String) vRowTemp.elementAt(22));
                oSP.setProcedimiento((String) vRowTemp.elementAt(23));
                oSP.setReceta((String) vRowTemp.elementAt(24));
                oSP.setSituacion((String) vRowTemp.elementAt(25));
                oSP.setCantidad(((Double) vRowTemp.elementAt(26)).intValue());
                oEF = new ExamenFisico();
                oEF.setCveExFis(((Double) vRowTemp.elementAt(27)).intValue());
                oSP.setExamenFisico(oEF);
                oSP.setCostoCobrado(((Double) vRowTemp.elementAt(28)).floatValue());
                oCompaniaCred = new CompaniaCred();
                oCompaniaCred.setIdEmp(((Double) vRowTemp.elementAt(29)).intValue());
                oSP.setCompaniaCred(oCompaniaCred);

                listRet.add(oSP);
            }
        }
        return listRet;
    }

//--REGALIAS------------------------------------------------------------------------------------------------------------
    public List<ServicioPrestado> buscaServiciosPrestadosRegaliasMedico(Date fechaIni, Date fechaFin) throws Exception {
        List<ServicioPrestado> listRet = new ArrayList<ServicioPrestado>();
        ServicioPrestado oSP;

        Vector rst = null;
        String sQuery = "";

        sQuery = "select * from buscaTodasRegaliasMedico(" + this.oMedico.getFolioPers() + ",'" + fechaIni + "','" + fechaFin + "') ";
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
                oSP = new ServicioPrestado();
                Vector vRowTemp = (Vector) rst.elementAt(i);
                /*  ci.ncveconcepingr(0), ci.sdescripcion(1), ci.npctregalmed(2), sp.nidfolio(3), sp.cveepisodio(4),
                 sp.nfoliopaciente(5), sp.bfacturable(6), sp.drealizado(7), sp.dregistro(8), sp.ncostooriginal(9),
                 sp.npctiva(10), sp.nquienpaga(11), sp.scvematerial(12), sp.scvemedicamento(13), sp.nfoliopers(14),
                 sp.ncveunimed(15), sp.sobservaciones(16), sp.sprocedimiento(17), sp.sreceta(18), sp.ssituacion(19), 
                 sp.ncantidad(20), sp.ncveexfis(21), sp.ncostocobrado(22), sp.nidemp(23), sp.dentradaalserv(24),
                 sp.sdiagaux(25), em.nmedreferencia(26) */
                oConcepPrestado = new ConceptoIngreso();
                oConcepPrestado.setCveConcep(((Double) vRowTemp.elementAt(0)).intValue());
                oConcepPrestado.setDescripConcep((String) vRowTemp.elementAt(1));
                oConcepPrestado.setPctRegalMed(((Double) vRowTemp.elementAt(2)).floatValue());
                oSP.setConcepPrestado(oConcepPrestado);
                oSP.setIdFolio((String) vRowTemp.elementAt(3));
                oEpisodioMedico = new EpisodioMedico();
                oEpisodioMedico.setCveepisodio(((Double) vRowTemp.elementAt(4)).intValue());
                oSP.setEpisodioMedico(oEpisodioMedico);
                oPaciente = new Paciente();
                oPaciente.setFolioPac(((Double) vRowTemp.elementAt(5)).intValue());
                oSP.setPaciente(oPaciente);
                oSP.setFacturable(((String) vRowTemp.elementAt(6)).equals("1"));
                oSP.setRealizado((Date) vRowTemp.elementAt(7));
                oSP.setRegistro((Date) vRowTemp.elementAt(8));
                oSP.setCostoOriginal(((Double) vRowTemp.elementAt(9)).floatValue());
                oSP.setPctIVA(((Double) vRowTemp.elementAt(10)).floatValue());
                oSP.setQuienPaga(((Double) vRowTemp.elementAt(11)).intValue());
                Medico medTrat = new Medico();
                medTrat.setFolioPers(((Double) vRowTemp.elementAt(14)).intValue());
                oSP.setMedico(medTrat);
                oUniMed = new UnidadMedida();
                oUniMed.setCve((String) vRowTemp.elementAt(15));
                oSP.setUniMed(oUniMed);
                oSP.setObs((String) vRowTemp.elementAt(16));
                oSP.setProcedimiento((String) vRowTemp.elementAt(17));
                oSP.setReceta((String) vRowTemp.elementAt(18));
                oSP.setSituacion((String) vRowTemp.elementAt(19));
                oSP.setCantidad(((Double) vRowTemp.elementAt(20)).intValue());
                oEF = new ExamenFisico();
                oEF.setCveExFis(((Double) vRowTemp.elementAt(21)).intValue());
                oSP.setExamenFisico(oEF);
                oSP.setCostoCobrado(((Double) vRowTemp.elementAt(22)).floatValue());
                if (oSP.getCostoCobrado() == 0) {
                    oSP.setCostoCobrado(oSP.getCostoOriginal());
                }
                oCompaniaCred = new CompaniaCred();
                oCompaniaCred.setIdEmp(((Double) vRowTemp.elementAt(23)).intValue());
                oSP.setCompaniaCred(oCompaniaCred);
                listRet.add(oSP);
            }
        }
        return listRet;
    }

    public List<ServicioPrestado> buscaServiciosPrestadosRegaliasEnfermeras(Date fechaIni, Date fechaFin) throws Exception {
        List<ServicioPrestado> listRet = new ArrayList<ServicioPrestado>();
        ServicioPrestado oSP;

        Vector rst = null;
        String sQuery = "";

        sQuery = "select * from buscaTodasRegaliasEnfermera ('" + fechaIni + "','" + fechaFin + "') ";
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
                oSP = new ServicioPrestado();
                Vector vRowTemp = (Vector) rst.elementAt(i);
                /*  ci.ncveconcepingr(0), ci.sdescripcion(1), ci.npctregalenf(2), sp.nidfolio(3), sp.cveepisodio(4), 
                 sp.nfoliopaciente(5), sp.bfacturable(6), sp.drealizado(7), sp.dregistro(8), sp.ncostooriginal(9), 
                 sp.npctiva(10), sp.nquienpaga(11), sp.scvematerial(12), sp.scvemedicamento(13), sp.nfoliopers(14), 
                 sp.ncveunimed(15), sp.sobservaciones(16), sp.sprocedimiento(17), sp.sreceta(18), sp.ssituacion(19),
                 sp.ncantidad(20), sp.ncveexfis(21),	sp.ncostocobrado(22), sp.nidemp(23), sp.dentradaalserv(24),
                 sp.sdiagaux(25)  */
                oConcepPrestado = new ConceptoIngreso();
                oConcepPrestado.setCveConcep(((Double) vRowTemp.elementAt(0)).intValue());
                oConcepPrestado.setDescripConcep((String) vRowTemp.elementAt(1));
                oConcepPrestado.setPctRegalEnf(((Double) vRowTemp.elementAt(2)).floatValue());
                oSP.setConcepPrestado(oConcepPrestado);
                oSP.setIdFolio((String) vRowTemp.elementAt(3));
                oEpisodioMedico = new EpisodioMedico();
                oEpisodioMedico.setCveepisodio(((Double) vRowTemp.elementAt(4)).intValue());
                oSP.setEpisodioMedico(oEpisodioMedico);
                oPaciente = new Paciente();
                oPaciente.setFolioPac(((Double) vRowTemp.elementAt(5)).intValue());
                oSP.setFacturable(((String) vRowTemp.elementAt(6)).equals("1"));
                oSP.setRealizado((Date) vRowTemp.elementAt(7));
                oSP.setRegistro((Date) vRowTemp.elementAt(8));
                oSP.setCostoOriginal(((Double) vRowTemp.elementAt(9)).floatValue());
                oSP.setPctIVA(((Double) vRowTemp.elementAt(10)).floatValue());
                oSP.setQuienPaga(((Double) vRowTemp.elementAt(11)).intValue());
                Medico medTrat = new Medico();
                medTrat.setFolioPers(((Double) vRowTemp.elementAt(14)).intValue());
                oSP.setMedico(medTrat);
                oUniMed = new UnidadMedida();
                oUniMed.setCve((String) vRowTemp.elementAt(15));
                oSP.setUniMed(oUniMed);
                oSP.setObs((String) vRowTemp.elementAt(16));
                oSP.setProcedimiento((String) vRowTemp.elementAt(17));
                oSP.setReceta((String) vRowTemp.elementAt(18));
                oSP.setSituacion((String) vRowTemp.elementAt(19));
                oSP.setCantidad(((Double) vRowTemp.elementAt(20)).intValue());
                oEF = new ExamenFisico();
                oEF.setCveExFis(((Double) vRowTemp.elementAt(21)).intValue());
                oSP.setExamenFisico(oEF);
                oSP.setCostoCobrado(((Double) vRowTemp.elementAt(22)).floatValue());
                if (oSP.getCostoCobrado() == 0) {
                    oSP.setCostoCobrado(oSP.getCostoOriginal());
                }
                oCompaniaCred = new CompaniaCred();
                oCompaniaCred.setIdEmp(((Double) vRowTemp.elementAt(23)).intValue());
                oSP.setCompaniaCred(oCompaniaCred);
                listRet.add(oSP);
            }
        }
        return listRet;
    }

    public List<ServicioPrestado> buscaServiciosPrestadosRPTRegaliasEnfermeras(Date fechaIni, Date fechaFin) throws Exception {
        List<ServicioPrestado> listRet = new ArrayList<ServicioPrestado>();
        ServicioPrestado oSP;

        Vector rst = null;
        String sQuery = "";

        sQuery = "SELECT * FROM buscaTodasRegaliasRPTEnfermera ('" + fechaIni + "','" + fechaFin + "') ";
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
                oSP = new ServicioPrestado();
                Vector vRowTemp = (Vector) rst.elementAt(i);
                /*  ci.ncveconcepingr(0), ci.sdescripcion(1), ci.npctregalenf(2), sp.nidfolio(3), sp.cveepisodio(4), 
                 sp.nfoliopaciente(5), sp.bfacturable(6), sp.drealizado(7), sp.dregistro(8), sp.ncostooriginal(9), 
                 sp.npctiva(10), sp.nquienpaga(11), sp.scvematerial(12), sp.scvemedicamento(13), sp.nfoliopers(14), 
                 sp.ncveunimed(15), sp.sobservaciones(16), sp.sprocedimiento(17), sp.sreceta(18), sp.ssituacion(19),
                 sp.ncantidad(20), sp.ncveexfis(21),	sp.ncostocobrado(22), sp.nidemp(23), sp.dentradaalserv(24),
                 sp.sdiagaux(25)  */
                oConcepPrestado = new ConceptoIngreso();
                oConcepPrestado.setCveConcep(((Double) vRowTemp.elementAt(0)).intValue());
                oConcepPrestado.setDescripConcep((String) vRowTemp.elementAt(1));
                oConcepPrestado.setPctRegalEnf(((Double) vRowTemp.elementAt(2)).floatValue());
                oSP.setConcepPrestado(oConcepPrestado);
                oSP.setIdFolio((String) vRowTemp.elementAt(3));
                oEpisodioMedico = new EpisodioMedico();
                oEpisodioMedico.setCveepisodio(((Double) vRowTemp.elementAt(4)).intValue());
                oSP.setEpisodioMedico(oEpisodioMedico);
                oPaciente = new Paciente();
                oPaciente.setFolioPac(((Double) vRowTemp.elementAt(5)).intValue());
                oSP.setFacturable(((String) vRowTemp.elementAt(6)).equals("1"));
                oSP.setRealizado((Date) vRowTemp.elementAt(7));
                oSP.setRegistro((Date) vRowTemp.elementAt(8));
                oSP.setCostoOriginal(((Double) vRowTemp.elementAt(9)).floatValue());
                oSP.setPctIVA(((Double) vRowTemp.elementAt(10)).floatValue());
                oSP.setQuienPaga(((Double) vRowTemp.elementAt(11)).intValue());
                Medico medTrat = new Medico();
                medTrat.setFolioPers(((Double) vRowTemp.elementAt(14)).intValue());
                oSP.setMedico(medTrat);
                oUniMed = new UnidadMedida();
                oUniMed.setCve((String) vRowTemp.elementAt(15));
                oSP.setUniMed(oUniMed);
                oSP.setObs((String) vRowTemp.elementAt(16));
                oSP.setProcedimiento((String) vRowTemp.elementAt(17));
                oSP.setReceta((String) vRowTemp.elementAt(18));
                oSP.setSituacion((String) vRowTemp.elementAt(19));
                oSP.setCantidad(((Double) vRowTemp.elementAt(20)).intValue());
                oEF = new ExamenFisico();
                oEF.setCveExFis(((Double) vRowTemp.elementAt(21)).intValue());
                oSP.setExamenFisico(oEF);
                oSP.setCostoCobrado(((Double) vRowTemp.elementAt(22)).floatValue());
                if (oSP.getCostoCobrado() == 0) {
                    oSP.setCostoCobrado(oSP.getCostoOriginal());
                }
                oCompaniaCred = new CompaniaCred();
                oCompaniaCred.setIdEmp(((Double) vRowTemp.elementAt(23)).intValue());
                oSP.setCompaniaCred(oCompaniaCred);
                listRet.add(oSP);
            }
        }
        return listRet;
    }

    public List<ServicioPrestado> buscaServiciosPrestadosRegaliasMedicoGuardia(Date fechaIni, Date fechaFin) throws Exception {
        List<ServicioPrestado> listRet = new ArrayList<ServicioPrestado>();
        ServicioPrestado oSP;

        Vector rst = null;
        String sQuery = "";

        sQuery = "SELECT * FROM buscaTodasRegaliasMedicoGuardia(" + this.oMedico.getFolioPers() + ",'" + fechaIni + "','" + fechaFin + "') ";
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
                oSP = new ServicioPrestado();
                Vector vRowTemp = (Vector) rst.elementAt(i);
                /*  ci.ncveconcepingr(0), ci.sdescripcion(1), ci.npctregalguard(2), sp.nidfolio(3), sp.cveepisodio(4), 
                 sp.nfoliopaciente(5), p.sappaterno(6), p.sapmaterno(7), p.snombre(8), p.stipopac(9), sp.bfacturable(10), 
                 sp.drealizado(11), sp.dregistro(12), sp.ncostooriginal(13), sp.npctiva(14), sp.nquienpaga(15), sp.scvematerial(16),
                 sp.scvemedicamento(17), sp.nfoliopers(18), sp.ncveunimed(19), sp.sobservaciones(20), sp.sprocedimiento(21), 
                 sp.sreceta(22), sp.ssituacion(23), sp.ncantidad(24), sp.ncveexfis(25),sp.ncostocobrado(26), sp.nidemp(27),
                 sp.dentradaalserv(28), sp.sdiagaux(29), em.nmedreferencia(30) */
                oConcepPrestado = new ConceptoIngreso();
                oConcepPrestado.setCveConcep(((Double) vRowTemp.elementAt(0)).intValue());
                oConcepPrestado.setDescripConcep((String) vRowTemp.elementAt(1));
                oConcepPrestado.setPctRegalGuard(((Double) vRowTemp.elementAt(2)).floatValue());
                oSP.setConcepPrestado(oConcepPrestado);
                oSP.setIdFolio((String) vRowTemp.elementAt(3));
                oEpisodioMedico = new EpisodioMedico();
                oEpisodioMedico.setCveepisodio(((Double) vRowTemp.elementAt(4)).intValue());
                oSP.setEpisodioMedico(oEpisodioMedico);
                oPaciente = new Paciente();
                oPaciente.setFolioPac(((Double) vRowTemp.elementAt(5)).intValue());
                oPaciente.setApellidoPaterno((String) vRowTemp.elementAt(6));
                oPaciente.setApellidoMaterno((String) vRowTemp.elementAt(7));
                oPaciente.setNombre((String) vRowTemp.elementAt(8));
                oPaciente.setTipo((String) vRowTemp.elementAt(9));
                oSP.setPaciente(oPaciente);
                oSP.setFacturable(((String) vRowTemp.elementAt(10)).equals("1"));
                oSP.setRealizado((Date) vRowTemp.elementAt(11));
                oSP.setRegistro((Date) vRowTemp.elementAt(12));
                oSP.setCostoOriginal(((Double) vRowTemp.elementAt(13)).floatValue());
                oSP.setPctIVA(((Double) vRowTemp.elementAt(14)).floatValue());
                oSP.setQuienPaga(((Double) vRowTemp.elementAt(15)).intValue());
                Medico medTrat = new Medico();
                medTrat.setFolioPers(((Double) vRowTemp.elementAt(18)).intValue());
                oSP.setMedico(medTrat);
                oUniMed = new UnidadMedida();
                oUniMed.setCve((String) vRowTemp.elementAt(19));
                oSP.setUniMed(oUniMed);
                oSP.setObs((String) vRowTemp.elementAt(20));
                oSP.setProcedimiento((String) vRowTemp.elementAt(21));
                oSP.setReceta((String) vRowTemp.elementAt(22));
                oSP.setSituacion((String) vRowTemp.elementAt(23));
                oSP.setCantidad(((Double) vRowTemp.elementAt(24)).intValue());
                oEF = new ExamenFisico();
                oEF.setCveExFis(((Double) vRowTemp.elementAt(25)).intValue());
                oSP.setExamenFisico(oEF);
                oSP.setCostoCobrado(((Double) vRowTemp.elementAt(26)).floatValue());
                if (oSP.getCostoCobrado() == 0) {
                    oSP.setCostoCobrado(oSP.getCostoOriginal());
                }
                oCompaniaCred = new CompaniaCred();
                oCompaniaCred.setIdEmp(((Double) vRowTemp.elementAt(27)).intValue());
                oSP.setCompaniaCred(oCompaniaCred);
                listRet.add(oSP);
            }
        }
        return listRet;
    }

    public List<ServicioPrestado> buscaServiciosPrestadosRPTRegaliasMedicoGuardia(Date fechaIni, Date fechaFin) throws Exception {
        List<ServicioPrestado> listRet = new ArrayList<ServicioPrestado>();
        ServicioPrestado oSP;

        Vector rst = null;
        String sQuery = "";

        sQuery = "SELECT * FROM buscaTodasRegaliasRPTMedicoGuardia(" + this.oMedico.getFolioPers() + ",'" + fechaIni + "','" + fechaFin + "') ";
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
                oSP = new ServicioPrestado();
                Vector vRowTemp = (Vector) rst.elementAt(i);
                /*  ci.ncveconcepingr(0), ci.sdescripcion(1), ci.npctregalguard(2), sp.nidfolio(3), sp.cveepisodio(4), 
                 sp.nfoliopaciente(5), p.sappaterno(6), p.sapmaterno(7), p.snombre(8), p.stipopac(9), sp.bfacturable(10), 
                 sp.drealizado(11), sp.dregistro(12), sp.ncostooriginal(13), sp.npctiva(14), sp.nquienpaga(15), sp.scvematerial(16),
                 sp.scvemedicamento(17), sp.nfoliopers(18), sp.ncveunimed(19), sp.sobservaciones(20), sp.sprocedimiento(21), 
                 sp.sreceta(22), sp.ssituacion(23), sp.ncantidad(24), sp.ncveexfis(25),sp.ncostocobrado(26), sp.nidemp(27),
                 sp.dentradaalserv(28), sp.sdiagaux(29), em.nmedreferencia(30) */
                oConcepPrestado = new ConceptoIngreso();
                oConcepPrestado.setCveConcep(((Double) vRowTemp.elementAt(0)).intValue());
                oConcepPrestado.setDescripConcep((String) vRowTemp.elementAt(1));
                oConcepPrestado.setPctRegalGuard(((Double) vRowTemp.elementAt(2)).floatValue());
                oSP.setConcepPrestado(oConcepPrestado);
                oSP.setIdFolio((String) vRowTemp.elementAt(3));
                oEpisodioMedico = new EpisodioMedico();
                oEpisodioMedico.setCveepisodio(((Double) vRowTemp.elementAt(4)).intValue());
                oSP.setEpisodioMedico(oEpisodioMedico);
                oPaciente = new Paciente();
                oPaciente.setFolioPac(((Double) vRowTemp.elementAt(5)).intValue());
                oPaciente.setApellidoPaterno((String) vRowTemp.elementAt(6));
                oPaciente.setApellidoMaterno((String) vRowTemp.elementAt(7));
                oPaciente.setNombre((String) vRowTemp.elementAt(8));
                oPaciente.setTipo((String) vRowTemp.elementAt(9));
                oSP.setPaciente(oPaciente);
                oSP.setFacturable(((String) vRowTemp.elementAt(10)).equals("1"));
                oSP.setRealizado((Date) vRowTemp.elementAt(11));
                oSP.setRegistro((Date) vRowTemp.elementAt(12));
                oSP.setCostoOriginal(((Double) vRowTemp.elementAt(13)).floatValue());
                oSP.setPctIVA(((Double) vRowTemp.elementAt(14)).floatValue());
                oSP.setQuienPaga(((Double) vRowTemp.elementAt(15)).intValue());
                Medico medTrat = new Medico();
                medTrat.setFolioPers(((Double) vRowTemp.elementAt(18)).intValue());
                oSP.setMedico(medTrat);
                oUniMed = new UnidadMedida();
                oUniMed.setCve((String) vRowTemp.elementAt(19));
                oSP.setUniMed(oUniMed);
                oSP.setObs((String) vRowTemp.elementAt(20));
                oSP.setProcedimiento((String) vRowTemp.elementAt(21));
                oSP.setReceta((String) vRowTemp.elementAt(22));
                oSP.setSituacion((String) vRowTemp.elementAt(23));
                oSP.setCantidad(((Double) vRowTemp.elementAt(24)).intValue());
                oEF = new ExamenFisico();
                oEF.setCveExFis(((Double) vRowTemp.elementAt(25)).intValue());
                oSP.setExamenFisico(oEF);
                oSP.setCostoCobrado(((Double) vRowTemp.elementAt(26)).floatValue());
                if (oSP.getCostoCobrado() == 0) {
                    oSP.setCostoCobrado(oSP.getCostoOriginal());
                }
                oCompaniaCred = new CompaniaCred();
                oCompaniaCred.setIdEmp(((Double) vRowTemp.elementAt(27)).intValue());
                oSP.setCompaniaCred(oCompaniaCred);
                listRet.add(oSP);
            }
        }
        return listRet;
    }

    public List<ServicioPrestado> buscaServiciosPrestadosRPTRegaliasMedico(Date fechaIni, Date fechaFin) throws Exception {
        List<ServicioPrestado> listRet = new ArrayList<ServicioPrestado>();
        ServicioPrestado oSP;

        Vector rst = null;
        String sQuery = "";

        sQuery = "select * from buscaTodasRegaliasRPTMedicoExterno(" + this.oMedico.getFolioPers() + ",'" + fechaIni + "','" + fechaFin + "') ";
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
                oSP = new ServicioPrestado();
                Vector vRowTemp = (Vector) rst.elementAt(i);
                /*  ci.ncveconcepingr(0), ci.sdescripcion(1), ci.npctregalmed(2), sp.nidfolio(3), sp.cveepisodio(4),
                 sp.nfoliopaciente(5), sp.bfacturable(6), sp.drealizado(7), sp.dregistro(8), sp.ncostooriginal(9),
                 sp.npctiva(10), sp.nquienpaga(11), sp.scvematerial(12), sp.scvemedicamento(13), sp.nfoliopers(14),
                 sp.ncveunimed(15), sp.sobservaciones(16), sp.sprocedimiento(17), sp.sreceta(18), sp.ssituacion(19), 
                 sp.ncantidad(20), sp.ncveexfis(21), sp.ncostocobrado(22), sp.nidemp(23), sp.dentradaalserv(24),
                 sp.sdiagaux(25), em.nmedreferencia(26), dr.nmontocalculado(27) */
                oConcepPrestado = new ConceptoIngreso();
                oConcepPrestado.setCveConcep(((Double) vRowTemp.elementAt(0)).intValue());
                oConcepPrestado.setDescripConcep((String) vRowTemp.elementAt(1));
                oConcepPrestado.setPctRegalMed(((Double) vRowTemp.elementAt(2)).floatValue());
                oSP.setConcepPrestado(oConcepPrestado);
                oSP.setIdFolio((String) vRowTemp.elementAt(3));
                oEpisodioMedico = new EpisodioMedico();
                oEpisodioMedico.setCveepisodio(((Double) vRowTemp.elementAt(4)).intValue());
                oSP.setEpisodioMedico(oEpisodioMedico);
                oPaciente = new Paciente();
                oPaciente.setFolioPac(((Double) vRowTemp.elementAt(5)).intValue());
                oSP.setPaciente(oPaciente);
                oSP.setFacturable(((String) vRowTemp.elementAt(6)).equals("1"));
                oSP.setRealizado((Date) vRowTemp.elementAt(7));
                oSP.setRegistro((Date) vRowTemp.elementAt(8));
                //En costo original queda el valor para el reporte (costo cobrado)
                oSP.setCostoOriginal(((Double) vRowTemp.elementAt(22)).floatValue());
                if (oSP.getCostoCobrado()<=0.0f)
                    oSP.setCostoOriginal(((Double) vRowTemp.elementAt(9)).floatValue());
                oSP.setPctIVA(((Double) vRowTemp.elementAt(10)).floatValue());
                oSP.setQuienPaga(((Double) vRowTemp.elementAt(11)).intValue());
                Medico medTrat = new Medico();
                medTrat.setFolioPers(((Double) vRowTemp.elementAt(14)).intValue());
                oSP.setMedico(medTrat);
                oUniMed = new UnidadMedida();
                oUniMed.setCve((String) vRowTemp.elementAt(15));
                oSP.setUniMed(oUniMed);
                oSP.setObs((String) vRowTemp.elementAt(16));
                oSP.setProcedimiento((String) vRowTemp.elementAt(17));
                oSP.setReceta((String) vRowTemp.elementAt(18));
                //oSP.setSituacion((String) vRowTemp.elementAt(19));
                oSP.setCantidad(((Double) vRowTemp.elementAt(20)).intValue());
                oEF = new ExamenFisico();
                oEF.setCveExFis(((Double) vRowTemp.elementAt(21)).intValue());
                oSP.setExamenFisico(oEF);
                oCompaniaCred = new CompaniaCred();
                oCompaniaCred.setIdEmp(((Double) vRowTemp.elementAt(23)).intValue());
                oSP.setCompaniaCred(oCompaniaCred);
                //Costo cobrado se queda con el monto de la regalía calculada
                oSP.setCostoCobrado(((Double) vRowTemp.elementAt(27)).floatValue());
                //Queda con la situación de la REGALÍA
                oSP.setSituacion((String) vRowTemp.elementAt(28));
                listRet.add(oSP);
            }
        }
        return listRet;
    }

    public List<ServicioPrestado> buscaServiciosPrestadosRegaliasLaboratorioMedicoExt(Date fechaIni, Date fechaFin) throws Exception {
        //Busca servicios de un paciente específico
        List<ServicioPrestado> listRet = new ArrayList<ServicioPrestado>();
        ServicioPrestado oSP;

        Vector rst = null;
        String sQuery = "";

        sQuery = "SELECT * FROM buscaTodasRegaliasLaboratorioMedicoExt(" + this.oMedico.getFolioPers() + ",'" + fechaIni + "','" + fechaFin + "') ";
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
                oSP = new ServicioPrestado();
                Vector vRowTemp = (Vector) rst.elementAt(i);
                /*  ci.ncveconcepingr(0), ci.sdescripcion(1), ci.npctregalmed(2), sp.nidfolio(3), sp.cveepisodio(4),
                 sp.nfoliopaciente(5), sp.bfacturable(6), sp.drealizado(7), sp.dregistro(8), sp.ncostooriginal(9),
                 sp.npctiva(10), sp.nquienpaga(11), sp.scvematerial(12), sp.scvemedicamento(13), sp.nfoliopers(14),
                 sp.ncveunimed(15), sp.sobservaciones(16), sp.sprocedimiento(17), sp.sreceta(18), sp.ssituacion(19), 
                 sp.ncantidad(20), sp.ncveexfis(21), sp.ncostocobrado(22), sp.nidemp(23), sp.dentradaalserv(24),
                 sp.sdiagaux(25), em.nmedreferencia(26) */
                oConcepPrestado = new ConceptoIngreso();
                oConcepPrestado.setCveConcep(((Double) vRowTemp.elementAt(0)).intValue());
                oConcepPrestado.setDescripConcep((String) vRowTemp.elementAt(1));
                oConcepPrestado.setPctRegalMed(((Double) vRowTemp.elementAt(2)).floatValue());
                oSP.setConcepPrestado(oConcepPrestado);
                oSP.setIdFolio((String) vRowTemp.elementAt(3));
                oEpisodioMedico = new EpisodioMedico();
                oEpisodioMedico.setCveepisodio(((Double) vRowTemp.elementAt(4)).intValue());
                oSP.setEpisodioMedico(oEpisodioMedico);
                oPaciente = new Paciente();
                oPaciente.setFolioPac(((Double) vRowTemp.elementAt(5)).intValue());
                oSP.setPaciente(oPaciente);
                oSP.setFacturable(((String) vRowTemp.elementAt(6)).equals("1"));
                oSP.setRealizado((Date) vRowTemp.elementAt(7));
                oSP.setRegistro((Date) vRowTemp.elementAt(8));
                oSP.setCostoOriginal(((Double) vRowTemp.elementAt(9)).floatValue());
                oSP.setPctIVA(((Double) vRowTemp.elementAt(10)).floatValue());
                oSP.setQuienPaga(((Double) vRowTemp.elementAt(11)).intValue());
                Medico medTrat = new Medico();
                medTrat.setFolioPers(((Double) vRowTemp.elementAt(14)).intValue());
                oSP.setMedico(medTrat);
                oUniMed = new UnidadMedida();
                oUniMed.setCve((String) vRowTemp.elementAt(15));
                oSP.setUniMed(oUniMed);
                oSP.setObs((String) vRowTemp.elementAt(16));
                oSP.setProcedimiento((String) vRowTemp.elementAt(17));
                oSP.setReceta((String) vRowTemp.elementAt(18));
                oSP.setSituacion((String) vRowTemp.elementAt(19));
                oSP.setCantidad(((Double) vRowTemp.elementAt(20)).intValue());
                oEF = new ExamenFisico();
                oEF.setCveExFis(((Double) vRowTemp.elementAt(21)).intValue());
                oSP.setExamenFisico(oEF);
                oSP.setCostoCobrado(((Double) vRowTemp.elementAt(22)).floatValue());
                if (oSP.getCostoCobrado() == 0) {
                    oSP.setCostoCobrado(oSP.getCostoOriginal());
                }
                oCompaniaCred = new CompaniaCred();
                oCompaniaCred.setIdEmp(((Double) vRowTemp.elementAt(23)).intValue());
                oSP.setCompaniaCred(oCompaniaCred);
                listRet.add(oSP);
            }
        }
        return listRet;
    }

//--HONORARIOS------------------------------------------------------------------------------------------------------------
    public List<ServicioPrestado> buscaServiciosPrestadosHonorariosMedico(Date fechaIni, Date fechaFin) throws Exception {
        List<ServicioPrestado> listRet = new ArrayList<ServicioPrestado>();
        ServicioPrestado oSP;

        Vector rst = null;
        String sQuery = "";

        sQuery = "select * from buscaTodosHonorariosMedico ('" + fechaIni + "','" + fechaFin + "') ";
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
                oSP = new ServicioPrestado();
                Vector vRowTemp = (Vector) rst.elementAt(i);
                /*  ph.nfoliopers(0), ph.snombre(1), ph.sappaterno(2), ph.sapmaterno(3), p.nfoliopaciente(4),
                 p.snombre(5), p.sappaterno(6), p.sapmaterno(7), ci.ncveconcepingr(8), ci.sdescripcion(9),
                 sp.nidfolio(10), sp.cveepisodio(11),  sp.bfacturable(12), sp.drealizado(13), sp.dregistro(14),
                 sp.ncostooriginal(15), sp.npctiva(16), sp.nquienpaga(17), sp.scvematerial(18), sp.scvemedicamento(19),
                 sp.nfoliopers(20), sp.ncveunimed(21), sp.sobservaciones(22), sp.sprocedimiento(23), sp.sreceta(24),
                 sp.ssituacion(25), sp.ncantidad(26), sp.ncveexfis(27), sp.ncostocobrado(28), sp.nidemp(29), 
                 sp.dentradaalserv(30), sp.sdiagaux(31) */
                Medico med = new Medico();
                med.setFolioPers(((Double) vRowTemp.elementAt(0)).intValue());
                med.setNombre((String) vRowTemp.elementAt(1));
                med.setApellidoPaterno((String) vRowTemp.elementAt(2));
                med.setApellidoMaterno((String) vRowTemp.elementAt(3));
                oSP.setMedico((Medico) med);

                Paciente oPac = new Paciente();
                oPac.setFolioPac(((Double) vRowTemp.elementAt(4)).intValue());
                oPac.setNombre((String) vRowTemp.elementAt(5));
                oPac.setApellidoPaterno((String) vRowTemp.elementAt(6));
                oPac.setApellidoMaterno((String) vRowTemp.elementAt(7));
                oSP.setPaciente(oPac);

                oConcepPrestado = new ConceptoIngreso();
                oConcepPrestado.setCveConcep(((Double) vRowTemp.elementAt(8)).intValue());
                oConcepPrestado.setDescripConcep((String) vRowTemp.elementAt(9));
                oSP.setConcepPrestado(oConcepPrestado);

                oSP.setIdFolio((String) vRowTemp.elementAt(10));
                oEpisodioMedico = new EpisodioMedico();
                oEpisodioMedico.setCveepisodio(((Double) vRowTemp.elementAt(11)).intValue());
                oSP.setEpisodioMedico(oEpisodioMedico);

                oSP.setFacturable(((String) vRowTemp.elementAt(12)).equals("1"));
                oSP.setRealizado((Date) vRowTemp.elementAt(13));
                oSP.setRegistro((Date) vRowTemp.elementAt(14));
                //oSP.setCostoOriginal(((Double) vRowTemp.elementAt(15)).floatValue());
                oSP.setPctIVA(((Double) vRowTemp.elementAt(16)).floatValue());
                oSP.setQuienPaga(((Double) vRowTemp.elementAt(17)).intValue());

                //Medico medTrat= new Medico();
                //medTrat.setFolioPers(((Double) vRowTemp.elementAt(20)).intValue());
                //oSP.setMedico(medTrat);
                oUniMed = new UnidadMedida();
                oUniMed.setCve((String) vRowTemp.elementAt(21));
                oSP.setUniMed(oUniMed);
                oSP.setObs((String) vRowTemp.elementAt(22));
                oSP.setProcedimiento((String) vRowTemp.elementAt(23));
                oSP.setReceta((String) vRowTemp.elementAt(24));
                oSP.setSituacion((String) vRowTemp.elementAt(25));
                oSP.setCantidad(((Double) vRowTemp.elementAt(26)).intValue());
                oEF = new ExamenFisico();
                oEF.setCveExFis(((Double) vRowTemp.elementAt(27)).intValue());
                oSP.setExamenFisico(oEF);
                oSP.setCostoCobrado(((Double) vRowTemp.elementAt(28)).floatValue());
                //if(oSP.getCostoCobrado()==0){
                //    oSP.setCostoCobrado(oSP.getCostoOriginal());
                //}
                oCompaniaCred = new CompaniaCred();
                oCompaniaCred.setIdEmp(((Double) vRowTemp.elementAt(29)).intValue());
                oSP.setCompaniaCred(oCompaniaCred);

                listRet.add(oSP);
            }
        }
        return listRet;
    }

    public List<ServicioPrestado> buscaServiciosPrestadosHonorariosEnfermeras(Date fechaIni, Date fechaFin) throws Exception {
        List<ServicioPrestado> listRet = new ArrayList<ServicioPrestado>();
        ServicioPrestado oSP;

        Vector rst = null;
        String sQuery = "";

        sQuery = "SELECT * FROM buscaTodosHonorariosEnfermeras ('" + fechaIni + "','" + fechaFin + "') ";
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
                oSP = new ServicioPrestado();
                Vector vRowTemp = (Vector) rst.elementAt(i);
                /*  ph.nfoliopers(0), ph.snombre(1), ph.sappaterno(2), ph.sapmaterno(3), p.nfoliopaciente(4), p.snombre(5),
                 p.sappaterno(6), p.sapmaterno(7), ci.ncveconcepingr(8), ci.sdescripcion(9), sp.nidfolio(10), 
                 sp.cveepisodio(11), sp.bfacturable(12), sp.drealizado(13), sp.dregistro(14), sp.ncostooriginal(15), 
                 sp.npctiva(16), sp.nquienpaga(17), sp.scvematerial(18), sp.scvemedicamento(19), sp.nfoliopers(20), 
                 sp.ncveunimed(21), sp.sobservaciones(22), sp.sprocedimiento(23), sp.sreceta(24), sp.ssituacion(25), 
                 sp.ncantidad(26), sp.ncveexfis(27), sp.ncostocobrado(28), sp.nidemp(29), sp.dentradaalserv(30), 
                 sp.sdiagaux(31)  */
                Medico enf = new Medico(); //Guarda los datos de la enfermera
                enf.setFolioPers(((Double) vRowTemp.elementAt(0)).intValue());
                enf.setNombre((String) vRowTemp.elementAt(1));
                enf.setApellidoPaterno((String) vRowTemp.elementAt(2));
                enf.setApellidoMaterno((String) vRowTemp.elementAt(3));
                oSP.setMedico(enf);

                oPaciente = new Paciente();
                oPaciente.setFolioPac(((Double) vRowTemp.elementAt(4)).intValue());
                oPaciente.setNombre((String) vRowTemp.elementAt(5));
                oPaciente.setApellidoPaterno((String) vRowTemp.elementAt(6));
                oPaciente.setApellidoMaterno((String) vRowTemp.elementAt(7));
                oSP.setPaciente(oPaciente);

                oConcepPrestado = new ConceptoIngreso();
                oConcepPrestado.setCveConcep(((Double) vRowTemp.elementAt(8)).intValue());
                oConcepPrestado.setDescripConcep((String) vRowTemp.elementAt(9));
                oSP.setConcepPrestado(oConcepPrestado);

                oSP.setIdFolio((String) vRowTemp.elementAt(10));
                oEpisodioMedico = new EpisodioMedico();
                oEpisodioMedico.setCveepisodio(((Double) vRowTemp.elementAt(11)).intValue());
                oSP.setEpisodioMedico(oEpisodioMedico);

                oSP.setFacturable(((String) vRowTemp.elementAt(12)).equals("1"));
                oSP.setRealizado((Date) vRowTemp.elementAt(13));
                oSP.setRegistro((Date) vRowTemp.elementAt(14));
                //oSP.setCostoOriginal(((Double) vRowTemp.elementAt(15)).floatValue());
                oSP.setPctIVA(((Double) vRowTemp.elementAt(16)).floatValue());
                oSP.setQuienPaga(((Double) vRowTemp.elementAt(17)).intValue());

                //Medico medTrat= new Medico();
                //medTrat.setFolioPers(((Double) vRowTemp.elementAt(20)).intValue());
                //oSP.setMedico(medTrat);
                oUniMed = new UnidadMedida();
                oUniMed.setCve((String) vRowTemp.elementAt(21));
                oSP.setUniMed(oUniMed);
                oSP.setObs((String) vRowTemp.elementAt(22));
                oSP.setProcedimiento((String) vRowTemp.elementAt(23));
                oSP.setReceta((String) vRowTemp.elementAt(24));
                oSP.setSituacion((String) vRowTemp.elementAt(25));
                oSP.setCantidad(((Double) vRowTemp.elementAt(26)).intValue());
                oEF = new ExamenFisico();
                oEF.setCveExFis(((Double) vRowTemp.elementAt(27)).intValue());
                oSP.setExamenFisico(oEF);
                oSP.setCostoCobrado(((Double) vRowTemp.elementAt(28)).floatValue());
                //if(oSP.getCostoCobrado()==0){
                //    oSP.setCostoCobrado(oSP.getCostoOriginal());
                //}
                oCompaniaCred = new CompaniaCred();
                oCompaniaCred.setIdEmp(((Double) vRowTemp.elementAt(29)).intValue());
                oSP.setCompaniaCred(oCompaniaCred);

                listRet.add(oSP);
            }
        }
        return listRet;
    }

    public List<ServicioPrestado> buscaServiciosPrestadosHonorariosTecnicos(Date fechaIni, Date fechaFin) throws Exception {
        List<ServicioPrestado> listRet = new ArrayList<ServicioPrestado>();
        ServicioPrestado oSP;

        Vector rst = null;
        String sQuery = "";

        sQuery = "SELECT * FROM buscaTodosHonorariosTecnicos ('" + fechaIni + "','" + fechaFin + "') ";
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
                oSP = new ServicioPrestado();
                Vector vRowTemp = (Vector) rst.elementAt(i);
                /*  ph.nfoliopers(0), ph.snombre(1), ph.sappaterno(2), ph.sapmaterno(3), p.nfoliopaciente(4), 
                 p.snombre(5), p.sappaterno(6), p.sapmaterno(7), ci.ncveconcepingr(8), ci.sdescripcion(9),
                 sp.nidfolio(10), sp.cveepisodio(11), sp.bfacturable(12), sp.drealizado(13), sp.dregistro(14), 
                 sp.ncostooriginal(15), sp.npctiva(16), sp.nquienpaga(17), sp.scvematerial(18), 
                 sp.scvemedicamento(19), sp.nfoliopers(20), sp.ncveunimed(21), sp.sobservaciones(22), 
                 sp.sprocedimiento(23), sp.sreceta(24), sp.ssituacion(25), sp.ncantidad(26), sp.ncveexfis(27), 
                 sp.ncostocobrado(28), sp.nidemp(29), sp.dentradaalserv(30), sp.sdiagaux(31)  */
                Medico tec = new Medico();
                tec.setFolioPers(((Double) vRowTemp.elementAt(0)).intValue());
                tec.setNombre((String) vRowTemp.elementAt(1));
                tec.setApellidoPaterno((String) vRowTemp.elementAt(2));
                tec.setApellidoMaterno((String) vRowTemp.elementAt(3));
                oSP.setMedico(tec);

                Paciente oPac = new Paciente();
                oPac.setFolioPac(((Double) vRowTemp.elementAt(4)).intValue());
                oPac.setNombre((String) vRowTemp.elementAt(5));
                oPac.setApellidoPaterno((String) vRowTemp.elementAt(6));
                oPac.setApellidoMaterno((String) vRowTemp.elementAt(7));
                oSP.setPaciente(oPac);

                oConcepPrestado = new ConceptoIngreso();
                oConcepPrestado.setCveConcep(((Double) vRowTemp.elementAt(8)).intValue());
                oConcepPrestado.setDescripConcep((String) vRowTemp.elementAt(9));
                oSP.setConcepPrestado(oConcepPrestado);

                oSP.setIdFolio((String) vRowTemp.elementAt(10));
                oEpisodioMedico = new EpisodioMedico();
                oEpisodioMedico.setCveepisodio(((Double) vRowTemp.elementAt(11)).intValue());
                oSP.setEpisodioMedico(oEpisodioMedico);

                oSP.setFacturable(((String) vRowTemp.elementAt(12)).equals("1"));
                oSP.setRealizado((Date) vRowTemp.elementAt(13));
                oSP.setRegistro((Date) vRowTemp.elementAt(14));
                //oSP.setCostoOriginal(((Double) vRowTemp.elementAt(15)).floatValue());
                oSP.setPctIVA(((Double) vRowTemp.elementAt(16)).floatValue());
                oSP.setQuienPaga(((Double) vRowTemp.elementAt(17)).intValue());

                //Medico medTrat= new Medico();
                //medTrat.setFolioPers(((Double) vRowTemp.elementAt(20)).intValue());
                //oSP.setMedico(medTrat);
                oUniMed = new UnidadMedida();
                oUniMed.setCve((String) vRowTemp.elementAt(21));
                oSP.setUniMed(oUniMed);
                oSP.setObs((String) vRowTemp.elementAt(22));
                oSP.setProcedimiento((String) vRowTemp.elementAt(23));
                oSP.setReceta((String) vRowTemp.elementAt(24));
                oSP.setSituacion((String) vRowTemp.elementAt(25));
                oSP.setCantidad(((Double) vRowTemp.elementAt(26)).intValue());
                oEF = new ExamenFisico();
                oEF.setCveExFis(((Double) vRowTemp.elementAt(27)).intValue());
                oSP.setExamenFisico(oEF);
                oSP.setCostoCobrado(((Double) vRowTemp.elementAt(28)).floatValue());
                oCompaniaCred = new CompaniaCred();
                oCompaniaCred.setIdEmp(((Double) vRowTemp.elementAt(29)).intValue());
                oSP.setCompaniaCred(oCompaniaCred);

                listRet.add(oSP);
            }
        }
        return listRet;
    }

    public List<PersonalHospitalario> buscaEquipoPersonalMedUrgencia() throws Exception {
        List<PersonalHospitalario> listRet = new ArrayList<PersonalHospitalario>();
        ServicioPrestado oSP;

        Vector rst = null;
        String sQuery = "";

        sQuery = "select * from buscaEquipoPersonalMedUrgencia('" + this.sIdFolio + "') ";
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
                /* ph.nfoliopers, ph.snombre, ph.sappaterno, ph.sappaterno */
                PersonalHospitalario tmpPH = new PersonalHospitalario();
                tmpPH.setFolioPers(((Double) vRowTemp.elementAt(0)).intValue());
                tmpPH.setNombre((String) vRowTemp.elementAt(1));
                tmpPH.setApellidoPaterno((String) vRowTemp.elementAt(2));
                tmpPH.setApellidoMaterno((String) vRowTemp.elementAt(3));
                listRet.add(tmpPH);
            }
        }
        return listRet;
    }

    public String modificarCosto() throws Exception {
        Vector rst = null;
        String sQuery = "";

        if (this.sIdFolio.equals("") && this.nCostoCobrado == 0) {
            throw new Exception("ServicioPrestado.modificarCosto: error de programación, faltan datos");
        } else {
            sQuery = "select * from modificaCostoCobradoServPrestado('" + this.sIdFolio + "'," + this.nCostoCobrado + ")";
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

    public String modificarCostoOriginal() throws Exception {
        Vector rst = null;
        String sQuery = "";

        if (this.sIdFolio.equals("") && this.nCostoOriginal == 0) {
            throw new Exception("ServicioPrestado.modificarCostoOriginal: error de programación, faltan datos");
        } else {
            sQuery = "select * from modificaCostoOriginalServPrestado('" + this.sIdFolio + "'," + this.nCostoOriginal + ")";
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

    public List<ProcedimientoRealizado> buscaProcQxTipoQxServicioPrestado() throws Exception {
        List<ProcedimientoRealizado> listRet = new ArrayList<ProcedimientoRealizado>();
        ProcedimientoRealizado oPR;

        Vector rst = null;
        String sQuery = "";

        sQuery = "SELECT * FROM buscaProcQxTipoQxServicioPrestado('" + this.sIdFolio + "')";
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
                oPR = new ProcedimientoRealizado();
                Vector vRowTemp = (Vector) rst.elementAt(i);
                /* pr.dini, pr.dfin , pr.snotamedica, pr.stipoprog, tp.scvetipoprocqx, tp.sdescripcion */
                oPR.setIni((Date) vRowTemp.elementAt(0));
                oPR.setFin((Date) vRowTemp.elementAt(1));
                oPR.setNotaMedica((String) vRowTemp.elementAt(2));
                oPR.setTipoProg((String) vRowTemp.elementAt(3));
                if (oPR.getTipoProg().equals("U")) {
                    oPR.setTipoProg("Urgencia");
                }
                TipoProcQx tmp = new TipoProcQx();
                tmp.setCveTipoProcQx((String) vRowTemp.elementAt(4));
                tmp.setDescripcion((String) vRowTemp.elementAt(5));
                oPR.setTipoProcQx(tmp);
                oPR.setDuracion((String) vRowTemp.elementAt(6));
                listRet.add(oPR);
            }
        }
        return listRet;
    }
    
    public List<ServicioPrestado> buscaServiciosPrestadosYLineaIngresoPorEpisodioMed() throws Exception {
        List<ServicioPrestado> listRet = new ArrayList<ServicioPrestado>();
        ServicioPrestado oSP;

        Vector rst = null;
        String sQuery = "";

        sQuery = "select * from buscarServiciosYLineaIngresoporEpisodioMedico(" + this.oEpisodioMedico.getCveepisodio() + ") ";
        
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
                oSP = new ServicioPrestado();
                Vector vRowTemp = (Vector) rst.elementAt(i);
                
                oConcepPrestado = new ConceptoIngreso();
                oConcepPrestado.setDescripConcep((String) vRowTemp.elementAt(0));
                oSP.setConcepPrestado(oConcepPrestado);
                oSP.setLineaIngreso(((Double) vRowTemp.elementAt(1)).intValue());
                
                listRet.add(oSP);
            }
        }
        return listRet;
    }

   //JMHG
    /**
     * Busca las líneas de servicio del episodio médico y paciente ingresado
     * @param cveEpisodio Clave del episodio médico
     * @param nFolioPaciente Folio del paciente
     * @return Lista de ServicioPrestado con el resumen de las líneas de servicio y sus precios totales
     * @throws Exception Si falta la clave del episodio médico o el folio del paciente
     */
    public List<ServicioPrestado> buscarLineaServicioCargosEpisodioMedico( int cveEpisodio, int nFolioPaciente ) throws Exception
    {
        List<ServicioPrestado> arrRet = null;
        Vector rst = null;
        ServicioPrestado oSP = null;
        String sQuery = "";
        
        if( cveEpisodio < 1 || nFolioPaciente < 1 )
        {
            throw new Exception("ServicioPrestado.buscarLineaServicioCargosEpisodioMedico: Faltan datos");
        }
        else
        {
            sQuery = "SELECT * FROM buscarLineaServicioCargosEpMed(" +
                cveEpisodio + "::integer, " +
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
                arrRet = new ArrayList<ServicioPrestado>();
                Vector vRowTemp;
                for( int i = 0; i < rst.size(); i++ )
                {
                    vRowTemp = (Vector)rst.elementAt( i );
                    oSP = new ServicioPrestado();
                    oSP.setConcepPrestado( new ConceptoIngreso() );
                    
                    oSP.getConcepPrestado().getLineaIngreso().setDescrip( (String)vRowTemp.elementAt( 0 ) );
                    oSP.setCostoCobrado( ( (Double)vRowTemp.elementAt( 1 ) ).floatValue() ); // Total por Linea de Servicio
                    
                    arrRet.add( oSP );
                }
            }
        }
        
        return arrRet;
    }
    
    /**
     * Busca los detalles de la cuenta del episodio médico y el paciente seleccionado
     * @param cveEpisodio Clave del episodio médico
     * @param nFolioPaciente Folio del paciente
     * @return Lista de ServicioPrestado con el desglose de la cuenta
     * @throws Exception Si falta la clave del episodio médico o el folio del paciente
     */
    public List<ServicioPrestado> buscarCargosCuentaDesglosado( int cveEpisodio, int nFolioPaciente ) throws Exception
    {
        List<ServicioPrestado> arrRet = null;
        Vector rst = null;
        ServicioPrestado oSP = null;
        String sQuery = "";
        
        if( cveEpisodio < 1 || nFolioPaciente < 1 )
        {
            throw new Exception("ServicioPrestado.buscarLineaServicioCargosEpMed: Faltan datos");
        }
        else
        {
            sQuery = "SELECT * FROM buscarCargosCuentaDesglosado(" +
                cveEpisodio + "::integer, " +
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
                arrRet = new ArrayList<ServicioPrestado>();
                Vector vRowTemp;
                for( int i = 0; i < rst.size(); i++ )
                {
                    vRowTemp = (Vector)rst.elementAt( i );
                    oSP = new ServicioPrestado();
                    oSP.setConcepPrestado( new ConceptoIngreso() );
                    
                    oSP.getConcepPrestado().getLineaIngreso().setDescrip( (String)vRowTemp.elementAt( 0 ) );
                    oSP.setRegistro( (Date)vRowTemp.elementAt( 1 ) );
                    oSP.setIdFolio( (String)vRowTemp.elementAt( 2 ) );
                    oSP.getConcepPrestado().setDescripConcep( (String)vRowTemp.elementAt( 3 ) );
                    oSP.setCostoOriginal( ( (Double)vRowTemp.elementAt( 4 ) ).floatValue() ); // Precio sin IVA
                    oSP.setPctIVA( ( (Double)vRowTemp.elementAt( 5 ) ).floatValue() ); // Cantidad de IVA cobrado, NO es el porcentage
                    oSP.setCostoCobrado( ( (Double)vRowTemp.elementAt( 6 ) ).floatValue() ); // Precio con IVA
                    
                    arrRet.add( oSP );
                }
            }
        }
        
        return arrRet;
    }
    //====
//--------------------------------------------------------------------------------------------------------------
     
    public void setEquipoQx(List<String> equipoQx) {
        this.equipoQx = equipoQx;
    }

    public List<String> getEquipoQx() {
        return equipoQx;
    }

    public boolean getFacturable() {
        return bFacturable;
    }

    public void setFacturable(boolean bFacturable) {
        this.bFacturable = bFacturable;
    }

    public float getCostoOriginal() {
        return nCostoOriginal;
    }

    public void setCostoOriginal(float nCostoOriginal) {
        this.nCostoOriginal = nCostoOriginal;
    }

    public float getPctIVA() {
        return nPctIVA;
    }

    public void setPctIVA(float nPctIVA) {
        this.nPctIVA = nPctIVA;
    }

    public int getQuienPaga() {
        return nQuienPaga;
    }

    public void setQuienPaga(int nQuienPaga) {
        this.nQuienPaga = nQuienPaga;
    }

    public String getIdFolio() {
        return sIdFolio;
    }

    public void setIdFolio(String sIdFolio) {
        this.sIdFolio = sIdFolio;
    }

    public String getObs() {
        return sObs;
    }

    public void setObs(String sObs) {
        this.sObs = sObs;
    }

    public String getProcedimiento() {
        return sProcedimiento;
    }

    public void setProcedimiento(String sProcedimiento) {
        this.sProcedimiento = sProcedimiento;
    }

    public String getReceta() {
        return sReceta;
    }

    public void setReceta(String sReceta) {
        this.sReceta = sReceta;
    }

    public ConceptoIngreso getConcepPrestado() {
        return oConcepPrestado;
    }

    public void setConcepPrestado(ConceptoIngreso oConcepPrestado) {
        this.oConcepPrestado = oConcepPrestado;
    }

    public Medico getMedRealiza() {
        return oMedico;
    }

    public void setMedRealiza(Medico oMedico) {
        this.oMedico = oMedico;
    }

    public Medico getMedico() {
        return oMedico;
    }

    public void setMedico(Medico oMedico) {
        this.oMedico = oMedico;
    }

    public Paciente getPaciente() {
        return oPaciente;
    }

    public void setPaciente(Paciente oPaciente) {
        this.oPaciente = oPaciente;
    }

    public String getTipo() {
        return sTipo;
    }

    public void setTipo(String sTipo) {
        this.sTipo = sTipo;
    }

    public Date getRealizado() {
        return dRealizado;
    }

    public void setRealizado(Date dRealizado) {
        this.dRealizado = dRealizado;
    }

    public Date getRegistro() {
        return dRegistro;
    }

    public void setRegistro(Date dRegistrado) {
        this.dRegistro = dRegistrado;
    }

    public boolean isOtorgado() {
        return Otorgado;
    }

    public void setOtorgado(boolean Otorgado) {
        this.Otorgado = Otorgado;
    }

    public void setExamenFisico(ExamenFisico oEF) {
        this.oEF = oEF;
    }

    public ExamenFisico getExamenFisico() {
        return oEF;
    }

    public void setReqConsulta(Boolean bReqConsulta) {
        this.bReqConsulta = bReqConsulta;
    }

    public Boolean getReqConsulta() {
        return bReqConsulta;
    }

    public void setReqHosp(Boolean bReqHosp) {
        this.bReqHosp = bReqHosp;
    }

    public Boolean getReqHosp() {
        return bReqHosp;
    }

    public AccesoDatos getAD() {
        return oAD;
    }

    public void setAD(AccesoDatos oAD) {
        this.oAD = oAD;
    }

    public String getSituacion() {
        return sSituacion;
    }

    public void setSituacion(String sSituacion) {
        this.sSituacion = sSituacion;
    }

    public int getCantidad() {
        return nCantidad;
    }

    public void setCantidad(int nCantida) {
        this.nCantidad = nCantida;
    }

    public EpisodioMedico getEpisodioMedico() {
        return oEpisodioMedico;
    }

    public void setEpisodioMedico(EpisodioMedico oEpisodioMedico) {
        this.oEpisodioMedico = oEpisodioMedico;
    }

    public UnidadMedida getUniMed() {
        return oUniMed;
    }

    public void setUniMed(UnidadMedida oUniMed) {
        this.oUniMed = oUniMed;
    }

    public PersonalHospitalario getPersHospRealiza() {
        return oPersHospRealiza;
    }

    public void setPersHospRealiza(PersonalHospitalario oPersHospRealiza) {
        this.oPersHospRealiza = oPersHospRealiza;
    }

    public CompaniaCred getCompaniaCred() {
        return oCompaniaCred;
    }

    public void setCompaniaCred(CompaniaCred oCompaniaCred) {
        this.oCompaniaCred = oCompaniaCred;
    }

    public float getCostoCobrado() {
        return nCostoCobrado;
    }

    public void setCostoCobrado(float nCostoCobrado) {
        this.nCostoCobrado = nCostoCobrado;
    }

    public boolean getFechaEntradaServ() {
        return bFechaEntradaServ;
    }

    public void setFechaEntradaServ(boolean bFechaEntradaServ) {
        this.bFechaEntradaServ = bFechaEntradaServ;
    }

    public Date getDFechaEntradaServ() {
        return dFechaEntradaServ;
    }

    public void setDFechaEntradaServ(Date dFechaEntradaServ) {
        this.dFechaEntradaServ = dFechaEntradaServ;
    }

    public boolean getBAuxdiag() {
        return bAuxdiag;
    }

    public void setBAuxdiag(boolean bAuxdiag) {
        this.bAuxdiag = bAuxdiag;
    }

    public String getSAuxdiag() {
        return sAuxdiag;
    }

    public void setSAuxdiag(String sAuxdiag) {
        this.sAuxdiag = sAuxdiag;
    }

    public float getAnticipo() {
        return fAnticipo;
    }

    public void setAnticipo(float fAnticipo) {
        this.fAnticipo = fAnticipo;
    }

    public boolean getControlTv() {
        return bControlTv;
    }

    public void setControlTv(boolean bControlTv) {
        this.bControlTv = bControlTv;
    }

    public boolean getControlClima() {
        return bControlClima;
    }

    public void setControlClima(boolean bControlClima) {
        this.bControlClima = bControlClima;
    }

    public boolean getConcepUrgencia() {
        return bConcepUrgencia;
    }

    public void setConcepUrgencia(boolean bConcepUrgencia) {
        this.bConcepUrgencia = bConcepUrgencia;
    }

    public boolean getLineaEndos() {
        return bLineaEndos;
    }

    public void setLineaEndos(boolean bLineaEndos) {
        this.bLineaEndos = bLineaEndos;
    }

    public PersonalHospitalario getTecnico() {
        return oTecnico;
    }

    public void setTecnico(PersonalHospitalario oTecnico) {
        this.oTecnico = oTecnico;
    }

    public PersonalHospitalario getEnfermera() {
        return oEnfermera;
    }

    public void setEnfermera(PersonalHospitalario oEnfermera) {
        this.oEnfermera = oEnfermera;
    }

    public ServProcQx getServProcQx() {
        return oServProcQx;
    }

    public void setServProcQx(ServProcQx oServProcQx) {
        this.oServProcQx = oServProcQx;
    }
    /*
     * Getter y setter agregados para recibir de farmacia
     */

    public MaterialCuracion getMaterialCuracion() {
        return oMaterialCuracion;
    }

    public void setMaterialCuracion(MaterialCuracion oMaterialCuracion) {
        this.oMaterialCuracion = oMaterialCuracion;
    }

    public Medicamento getMedicamento() {
        return oMedicamento;
    }

    public void setMedicamento(Medicamento oMedicamento) {
        this.oMedicamento = oMedicamento;
    }

    public Date getRecepFarm() {
        return dRecepFarm;
    }

    public void setRecepFarm(Date dRecepFarm) {
        this.dRecepFarm = dRecepFarm;
    }

    public int getRequisicionFarm() {
        return nRequisicionFarm;
    }

    public void setRequisicionFarm(int nRequisicionFarm) {
        this.nRequisicionFarm = nRequisicionFarm;
    }

    public String getSitFarmacia() {
        return sSitFarmacia;
    }

    public void setSitFarmacia(String sSitFarmacia) {
        this.sSitFarmacia = sSitFarmacia;
    }

    public boolean isRecibido() {
        return bRecibido;
    }

    public void setRecibido(boolean bRecibido) {
        this.bRecibido = bRecibido;
    }

    public ProcedimientoRealizado getProcedimientoRealizado() {
        return m_ProcedimientoRealizado;
    }

    public void setProcedimientoRealizado(ProcedimientoRealizado m_ProcedimientoRealizado) {
        this.m_ProcedimientoRealizado = m_ProcedimientoRealizado;
    }

    public String getObsPrecio() {
        return sObsPrecio;
    }

    public void setObsPrecio(String sObsPrecio) {
        this.sObsPrecio = sObsPrecio;
    }

    public String getNumPoliza() {
        return sNumPoliza;
    }

    public void setNumPoliza(String sNumPoliza) {
        this.sNumPoliza = sNumPoliza;
    }

    public Date getSalidaservicio() {
        return dsalidaservicio;
    }

    public void setSalidaservicio(Date dsalidaservicio) {
        this.dsalidaservicio = dsalidaservicio;
    }    

    /**
     * @return the sConvQuienPaga
     */
    public String getConvQuienPaga() {
        return sConvQuienPaga;
    }

    /**
     * @param sConvQuienPaga the sConvQuienPaga to set
     */
    public void setConvQuienPaga(String sConvQuienPaga) {
        this.sConvQuienPaga = sConvQuienPaga;
    }

    /**
     * @return the oPaqueteContratado
     */
    public PaqueteContratado getPaqueteContratado() {
        return oPaqueteContratado;
    }

    /**
     * @param oPaqueteContratado the oPaqueteContratado to set
     */
    public void setPaqueteContratado(PaqueteContratado oPaqueteContratado) {
        this.oPaqueteContratado = oPaqueteContratado;
    }

    /**
     * @return the oMedicoSolicita
     */
    public Medico getMedicoSolicita() {
        return oMedicoSolicita;
    }

    /**
     * @param oMedicoSolicita the oMedicoSolicita to set
     */
    public void setMedicoSolicita(Medico valor) {
        this.oMedicoSolicita = valor;
    }

    /**
     * @return the nNumOrden
     */
    public int getNumOrden() {
        return nNumOrden;
    }

    /**
     * @param nNumOrden the nNumOrden to set
     */
    public void setNumOrden(int nNumOrden) {
        this.nNumOrden = nNumOrden;
    }
    
    public boolean getIndicadoPorMedico(){
        return this.bIndicadoPorMedico;
    }
    public void setIndicadoPorMedico(boolean valor){
        this.bIndicadoPorMedico = valor;
    }
    /**
     * @return the lineaIngreso
     */
    public int getLineaIngreso() {
        return lineaIngreso;
    }

    /**
     * @param lineaIngreso the lineaIngreso to set
     */
    public void setLineaIngreso(int lineaIngreso) {
        this.lineaIngreso = lineaIngreso;
    }

}
