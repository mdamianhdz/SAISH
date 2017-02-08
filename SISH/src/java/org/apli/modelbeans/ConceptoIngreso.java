package org.apli.modelbeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import org.apli.AD.AccesoDatos;

/**
 * Representa conceptos de ingreso de dinero al Sanatorio
 *
 * @author BAOZ
 * @version 1.0
 * @updated 07-abr-2014 09:49:24 a.m.
 */
public class ConceptoIngreso extends Concepto implements Serializable {

    /**
     * I = ingreso
     */
    public static final String TIPO_OPE_INGR = "I";
    
    //CORREGIR CLAVES !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    public static final int CVE_ANTICIPO = 1117;
    
    //Constantes para tipos de conceptos de ingreso (dónde se registra el cargo)
    public static final String TIPO_CAJA="Caja";
    public static final String TIPO_CE="CE";
    public static final String TIPO_CTA_HOSP = "Cta";
    public static final String TIPO_QX="Qx";
    public static final String TIPO_TODO="Todas";
    
    //Constantes para tipo de búsqueda
    public static final String BUSQ_TODOS = "Todos";
    public static final String BUSQ_PAC = "PorPaciente";
    
    //Constantes para comportamiento de ingresos
    public static final String COMPORT_SERV_MED = "servmed";
    public static final String COMPORT_OTRO = "otro";
    public static final String COMPORT_MAT = "material";
    public static final String COMPORT_MED = "medicamento";

    
    
    private AreaFisica oAreaFisica = new AreaFisica();
    protected AccesoDatos oAD = null;
    private String sTipoConcIngr;
    protected ServicioMedico oServicioMedico = new ServicioMedico();
    private AreaDeServicio oAreaServicio = new AreaDeServicio();
    protected Medicamento oMedicamento = new Medicamento();
    protected MaterialCuracion oMaterialCuracion = new MaterialCuracion();
    private LineaIngreso oLineaIngreso = new LineaIngreso();
    private UnidadMedida oUnidadMedida;
    private String idGenerico;
    private boolean precioConCero = false;
    private float fMonto;
    private float fMontoNuevo;
    private int nDisponibilidadMax = 10;
    private ConceptoIngreServicio oConServ = new ConceptoIngreServicio();
    private boolean bSiena;
    private boolean bTipoPaquete;
    private float nPctRegalEnf;
    private float nPctRegalMed;
    private float nPctRegalGuard;
    private boolean bRegalEnf;
    private boolean bRegalMed;
    private boolean bRegalGuard;
    private float nPctIVA;
    private boolean bHonMedEnf;
    private boolean bPaqEmp;
    private boolean bInterconsul;
    private boolean bSoloEmpleado;
    private boolean bSoloMedico;
    
    //Información propia para el "carrito" de cargos/órdenes de servicio
    private int nCantidad = 1;
    private int nCantidadACobrar = 0;
    private int nTipoPrincipalPaga = 0;    
    private String sVerIndicadoPorMedico; //relacionado con honorarios externos
    private boolean bIndicadoPorMedico;
    
    public ConceptoIngreso() {
        
    }

    public ConceptoIngreso(AccesoDatos poAD) {
        oAD = poAD;
    }
    
    public boolean isHonorarios() {
    boolean bRet = false;
        if ((this.nCveConcep >= 282 && this.nCveConcep <= 306)
                  || (this.nCveConcep >= 330 && this.nCveConcep <= 343)
                  || (this.nCveConcep == 1119)) {
            bRet = true;
        }
        return bRet;
    }
    
    public static boolean isCveHonorarios(int nCve) {
    boolean bRet = false;
        if ((nCve >= 282 && nCve <= 306)
                  || (nCve >= 330 && nCve <= 343)
                  || (nCve == 1119)) {
            bRet = true;
        }
        return bRet;
    }

    public ConceptoIngreso buscaNombreConcepto(int id) throws Exception {
        ConceptoIngreso oConcepIngr = new ConceptoIngreso();
        Vector rst = null;
        String sQuery = "";
        
        sQuery = "select t1.ncveconcepingr, t1.sdescripcion, t1.ncvelineaingr, t2.sdescripcion from concepingreso t1, lineaingreso t2 where ncveconcepingr=" + id + "  and t2.ncvelineaingr = t1.ncvelineaingr;";
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
            
            for (int i = 0; i < rst.size(); i++) {
                oConcepIngr = new ConceptoIngreso();
                Vector vRowTemp = (Vector) rst.elementAt(i);
                oConcepIngr.setCveConcep(((Double) vRowTemp.elementAt(0)).intValue());
                oConcepIngr.setDescripConcep((String) vRowTemp.elementAt(1));
                oConcepIngr.setLineaIngreso(new LineaIngreso());
                oConcepIngr.getLineaIngreso().setCveLin(((Double) vRowTemp.elementAt(2)).intValue());
                oConcepIngr.getLineaIngreso().setDescrip((String) vRowTemp.elementAt(3));
            }
        }
         return oConcepIngr;
    }
    
    public List<ConceptoIngreso> buscaServicios(LineaIngreso linea, 
            int idPac, int nCveEpi, String sBusqueda, String sLugarCargo,
            AreaDeServicio oArea) 
            throws Exception {
    List<ConceptoIngreso> listaServicios = new ArrayList<ConceptoIngreso>();
    ConceptoIngreso oCI = null;
    Vector rst = null;
    Vector<ConceptoIngreso> vObj = null;
    String sQuery = "", sTipo = "", sCodMedMat="", sCveMedMat="";
    float nMontoNormal = 0.0f, nMontoDscto = 0.0f;
    int i = 0;

        sQuery = "select * from buscaTodosServiciosParaCargo(" + 
                linea.getCveLin() + "::int2," + idPac + "," + nCveEpi + ", '"+ 
                sBusqueda + "'::character varying,'" + sLugarCargo + "'::text);";
        System.out.println(sQuery);
        if (this.oAD == null) {
            this.oAD=new AccesoDatos();
            this.oAD.conectar();
            rst = this.oAD.ejecutarConsulta(sQuery);
            this.oAD.desconectar();
            this.oAD=null;
        } else {
            rst = this.oAD.ejecutarConsulta(sQuery);
        }
        if (rst != null) {
            vObj = new Vector<ConceptoIngreso>();
            for (i = 0; i < rst.size(); i++) {
                oCI = new ConceptoIngreso();
                Vector vRowTemp = (Vector) rst.elementAt(i);
                sCveMedMat = (String) vRowTemp.elementAt(0);
                oCI.setCveConcep(((Double) vRowTemp.elementAt(1)).intValue());
                oCI.setDescripcion((String) vRowTemp.elementAt(2));
                nMontoNormal = ((Double) vRowTemp.elementAt(3)).floatValue();
                nMontoDscto = ((Double) vRowTemp.elementAt(4)).floatValue();
                sTipo = (String) vRowTemp.elementAt(5);
                sCodMedMat = (String) vRowTemp.elementAt(6);
                
                if (((String) vRowTemp.elementAt(7)).equals("0"))
                    oCI.setSoloEmpleado(false);
                else
                    oCI.setSoloEmpleado(true);
                
                if (((String) vRowTemp.elementAt(8)).equals("0"))
                    oCI.setSoloMedico(false);
                else
                    oCI.setSoloMedico(true);
                
                oCI.setTipoConcIngr(sTipo);
                oCI.setMonto(nMontoNormal);
                if (sTipo.equals(ConceptoIngreso.COMPORT_SERV_MED)
                          || sTipo.equals(ConceptoIngreso.COMPORT_OTRO)) {
                    oServicioMedico = new ServicioMedico();
                    oServicioMedico.setMonto(nMontoNormal);
                    oServicioMedico.setCveConcep(oCI.getCveConcep());
                    oCI.setIdGenerico("SO-" + oCI.getCveConcep());
                }
                
                if (nMontoNormal == 0.0f
                          || oCI.getDescripcion().contains("SIN COSTO")) {
                    oCI.setPrecioConCero(true);
                }
                if (sTipo.equals(ConceptoIngreso.COMPORT_MED)) {
                    oMedicamento = new Medicamento();
                    oMedicamento.setCveMedicamento(sCveMedMat);
                    oMedicamento.setCostoUnitario(nMontoNormal);
                    oMedicamento.setCodigo(sCodMedMat);
                    oMedicamento.setNomMedicamento(oCI.sDescripConcep);
                    
                    oCI.setIdGenerico("ME-" + (String) vRowTemp.elementAt(0));
                }
                if (sTipo.equals(ConceptoIngreso.COMPORT_MAT)) {
                    oMaterialCuracion = new MaterialCuracion();
                    oMaterialCuracion.setCveMaterial(sCveMedMat);
                    oMaterialCuracion.setFMonto(nMontoNormal);
                    oMaterialCuracion.setCodigo(sCodMedMat);
                    oMaterialCuracion.setDescripcion(oCI.sDescripConcep);
                    
                    oCI.setIdGenerico("MC-" + (String) vRowTemp.elementAt(0));
                }
                if (nMontoDscto > 0.0f) {
                    oCI.setMontoNuevo(nMontoDscto);
                } else {
                    oCI.setMontoNuevo(nMontoNormal);
                }
                oCI.setServicioMedico(oServicioMedico);
                oCI.setMedicamento(oMedicamento);
                oCI.setMaterialCuracion(oMaterialCuracion);
                oCI.setAreaServicio(oArea);
                oCI.setLineaIngreso(linea);
                listaServicios.add(oCI);
            }
        }
        return listaServicios;
    }

    public List<ConceptoIngreso> buscaServiciosMedicosMatMed(LineaIngreso linea,
            AreaDeServicio areaDeServicio, int idPac, String sBusqueda) 
            throws Exception {
        List<ConceptoIngreso> listaServicios = new ArrayList<ConceptoIngreso>();
        ConceptoIngreso oCI = null;
        Vector rst = null;
        Vector<ConceptoIngreso> vObj = null;

        String sQuery = "";
        int i = 0;

        sQuery = "select * from buscatodosserviciosmedicosmatmed(" + linea.getCveLin() + "::int2," + idPac + ",'" + sBusqueda + "'::character varying);";
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
        if (rst != null && rst.size() > 0) {
            vObj = new Vector<ConceptoIngreso>();
            for (i = 0; i < rst.size(); i++) {
                oCI = new ConceptoIngreso();
                Vector vRowTemp = (Vector) rst.elementAt(i);
                String sTipo = (String) vRowTemp.elementAt(4);
                if (sTipo.equals("medicamento")) {
                    oMedicamento = new Medicamento();
                    oMedicamento.setCveMedicamento((String) vRowTemp.elementAt(0));
                    oMedicamento.setCostoUnitario(((Double) vRowTemp.elementAt(3)).floatValue());
                    oMedicamento.setCodigo((String) vRowTemp.elementAt(5));
                    oMedicamento.setNomMedicamento((String) vRowTemp.elementAt(2));
                    if (oMedicamento.getCostoUnitario() == 0.0 || ((String) vRowTemp.elementAt(2)).contains("SIN COSTO")) {
                        oCI.setPrecioConCero(true);
                    }
                    oCI.setIdGenerico("ME-" + (String) vRowTemp.elementAt(0));
                }
                if (sTipo.equals("material")) {
                    oMaterialCuracion = new MaterialCuracion();
                    oMaterialCuracion.setCveMaterial((String) vRowTemp.elementAt(0));
                    oMaterialCuracion.setFMonto(((Double) vRowTemp.elementAt(3)).floatValue());
                    oMaterialCuracion.setCodigo((String) vRowTemp.elementAt(5));
                    oMaterialCuracion.setDescripcion((String) vRowTemp.elementAt(2));
                    if (oMaterialCuracion.getFMonto() == 0.0 || ((String) vRowTemp.elementAt(2)).contains("SIN COSTO")) {
                        oCI.setPrecioConCero(true);
                    }
                    oCI.setIdGenerico("MC-" + (String) vRowTemp.elementAt(0));
                }
                if (sTipo.equals("servmed")) {
                    oServicioMedico = new ServicioMedico();
                    oCI.setIdGenerico("SO-" + ((Double) vRowTemp.elementAt(1)).intValue());
                    oServicioMedico.setMonto(((Double) vRowTemp.elementAt(3)).floatValue());
                }

                oCI.setMonto(((Double) vRowTemp.elementAt(3)).floatValue());
                oCI.setMontoNuevo(((Double) vRowTemp.elementAt(3)).floatValue());
                oCI.setCveConcep(((Double) vRowTemp.elementAt(1)).intValue());
                oCI.setDescripcion((String) vRowTemp.elementAt(2));
                oCI.setTipoConcIngr((String) vRowTemp.elementAt(4));
                oCI.setMedicamento(oMedicamento);
                oCI.setMaterialCuracion(oMaterialCuracion);
                oCI.setServicioMedico(oServicioMedico);
                oCI.setAreaServicio(areaDeServicio);
                oCI.setLineaIngreso(linea);
                listaServicios.add(oCI);

            }
        }
        return listaServicios;
    }

    public ConceptoIngreso buscaConceptoIngreso() throws Exception {
    ConceptoIngreso oCI = null;
    Vector rst = null;
    Vector<ConceptoIngreso> vObj = null;
    String sQuery = "";

        sQuery = "SELECT * FROM buscaLlaveconcepingreso(" + this.getCveConcep() + ");";
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
            vObj = new Vector<ConceptoIngreso>();
            Vector vRowTemp = (Vector) rst.elementAt(0);
            oCI = new ConceptoIngreso();
            oCI.setCveConcep(((Double) vRowTemp.elementAt(0)).intValue());
            oCI.setDescripcion((String) vRowTemp.elementAt(1));
            AreaDeServicio oArea = new AreaDeServicio();
            oArea.setCve("" + ((Double) vRowTemp.elementAt(2)).intValue());
            oArea.setDescrip((String) vRowTemp.elementAt(11));
            oCI.setAreaServicio(oArea);
            AreaFisica oAreaFis = new AreaFisica();
            oAreaFis.setCve(((Double) vRowTemp.elementAt(3)).intValue());
            oCI.setAreaFisica(oAreaFis);
            LineaIngreso oLI = new LineaIngreso();
            oLI.setCveLin(((Double) vRowTemp.elementAt(4)).intValue());
            oLI.setDescrip(((String) vRowTemp.elementAt(12)));
            oCI.setLineaIngreso(oLI);
            if (((String) vRowTemp.elementAt(5)).equals("1")) {
                oCI.setRegalMed(true);
            }
            if (((String) vRowTemp.elementAt(6)).equals("1")) {
                oCI.setRegalGuard(true);
            }
            if (((String) vRowTemp.elementAt(7)).equals("1")) {
                oCI.setRegalEnf(true);
            }
            oCI.setPctRegalEnf(((Double) vRowTemp.elementAt(8)).floatValue());
            oCI.setPctRegalMed(((Double) vRowTemp.elementAt(9)).floatValue());
            oCI.setPctRegalGuard(((Double) vRowTemp.elementAt(10)).floatValue());
            oCI.setPctIVA(((Double) vRowTemp.elementAt(13)).floatValue());
            if (((String) vRowTemp.elementAt(14)).equals("1")) {
                oCI.setHonMedEnf(true);
            }
            if (((String) vRowTemp.elementAt(15)).equals("1")) {
                oCI.setPaqEmp(true);
            }
            oCI.setTipoConcIngr((String) vRowTemp.elementAt(16));
            oCI.setMonto(((Double) vRowTemp.elementAt(17)).floatValue());
        }
        return oCI;
    }

    public boolean validaExistenciaMax() {
        boolean existente = false;
        Medicamento oMedicamentoDisp = new Medicamento();
        Farmacia farmacia = new Farmacia();
        String matmed = "";
        if (this.sTipoConcIngr.equals("medicamento")) {
            matmed = this.oMedicamento.getCodigo();
            oMedicamentoDisp.setCveMedicamento(matmed);
            oMedicamentoDisp.setNomMedicamento(oMedicamento.getNomMedicamento());
        } else {
            matmed = this.oMaterialCuracion.getCodigo();
            oMedicamentoDisp.setCveMedicamento(matmed);
            oMedicamentoDisp.setNomMedicamento(oMaterialCuracion.getDescripcion());
        }
        farmacia.setBranch("1");
        farmacia.setMedicamento(oMedicamentoDisp);
        oMedicamentoDisp = farmacia.obtenerDatosFaltantes();
        if (oMedicamentoDisp != null) {
            nDisponibilidadMax = oMedicamentoDisp.getExistencia();
            if (nDisponibilidadMax > 0) {
                existente = true;
            }
            //Actualizacion de precio con respecto a farmacia
            this.fMontoNuevo = oMedicamentoDisp.getCostoUnitario();
        }
        return existente;
    }

    public boolean validaSoloExistencia() {
        boolean existente = false;
        Medicamento oMedicamentoDisp = new Medicamento();
        Farmacia farmacia = new Farmacia();
        String matmed = "";
        if (this.sTipoConcIngr.equals("medicamento")) {
            matmed = this.oMedicamento.getCveMedicamento();
            oMedicamentoDisp.setCveMedicamento(matmed);
            oMedicamentoDisp.setNomMedicamento(oMedicamento.getNomMedicamento());
        } else {
            matmed = this.oMaterialCuracion.getCveMaterial();
            oMedicamentoDisp.setCveMedicamento(matmed);
            oMedicamentoDisp.setNomMedicamento(oMaterialCuracion.getDescripcion());
        }
        farmacia.setBranch("1");
        farmacia.setMedicamento(oMedicamentoDisp);
        oMedicamentoDisp = farmacia.obtenerDatosFaltantes();
        if (oMedicamentoDisp != null) {
            existente = true;
            //Actualizacion de precio con respecto a farmacia
            this.fMontoNuevo = oMedicamentoDisp.getCostoUnitario();
        }
        return existente;
    }

//--REGALIAS------------------------------------------------------------------------------------------------------------
    public List<ConceptoIngreso> buscarConceptosEnfermeria() throws Exception {
        List<ConceptoIngreso> arrRet = new ArrayList<ConceptoIngreso>();
        ConceptoIngreso oCI = null;
        Vector rst = null;
        String sQuery = "";
        int i = 0;
        sQuery = " select * from buscaTodasConceptosEnfermeria() ";
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
            arrRet = new ArrayList<ConceptoIngreso>();
            for (i = 0; i < rst.size(); i++) {
                oCI = new ConceptoIngreso();
                Vector vRowTemp = (Vector) rst.elementAt(i);
                /* ncveconcepingr(0), sdescripcion(1) */
                oCI.setCveConcep(((Double) vRowTemp.elementAt(0)).intValue());
                oCI.setDescripConcep((String) vRowTemp.elementAt(1));
                arrRet.add(oCI);
            }
        }
        return arrRet;
    }

    public String buscaNoRegaliasPorConceptoDia(int noConcepto, Date dia, Date fechaIni, Date fechaFin) throws Exception {
        Vector rst = null;
        String sQuery = "";

        if (noConcepto == 0) {
            throw new Exception("ConceptoIngreso.AutorizacionPagoRegaliasEnf.: error de programación, faltan datos");
        } else {
            sQuery = "select * from buscaNoRegaliasPorConceptoDia ('" + dia + "'," + noConcepto + ",'" + fechaIni + "', '" + fechaFin + "');";
            System.out.println(sQuery);
            if (getAD() == null) {
                setAD(new AccesoDatos());
                getAD().conectar();
                rst = getAD().ejecutarConsulta(sQuery);
                getAD().desconectar();
                setAD(null);
            }
        }
        return " " + (rst == null ? "" : rst.get(0).toString());
    }

    public String buscaNoRegaliasPorConceptoDiaRPTEnfermera(int noConcepto, Date dia, Date fechaIni, Date fechaFin) throws Exception {
        Vector rst = null;
        String sQuery = "";

        if (noConcepto == 0) {
            throw new Exception("ConceptoIngreso.AutorizacionPagoRegaliasEnf.: error de programación, faltan datos");
        } else {
            sQuery = "select * from buscaNoRegaliasPorConceptoDiaRPTEnfermera('" + dia + "'," + noConcepto + ",'" + fechaIni + "', '" + fechaFin + "');";
            //System.out.println(sQuery);
            if (getAD() == null) {
                setAD(new AccesoDatos());
                getAD().conectar();
                rst = getAD().ejecutarConsulta(sQuery);
                getAD().desconectar();
                setAD(null);
            }
        }
        return " " + (rst == null ? "" : rst.get(0).toString());
    }

    public int buscaCveConcepEgrMedExt() throws Exception {
        Vector nRet = null;
        String sQuery = "";
        String msj = "";
        if (this.nCveConcep == 0) {
            throw new Exception("Pago Regalias.insertar: error de programación, faltan datos");
        } else {
            sQuery = " SELECT * FROM buscaCveConcepEgrMedExt(" + this.nCveConcep + ")";
            System.out.println(sQuery);
            /*Si oAD es nulo, tiene que crearlo y conectarlo, de otro modo
             supone que ya viene conectado*/
            if (getAD() == null) {
                setAD(new AccesoDatos());
                getAD().conectar();
                nRet = getAD().ejecutarConsulta(sQuery);
                getAD().desconectar();
                setAD(null);
            } else {
                nRet = getAD().ejecutarConsulta(sQuery);
            }
            msj = "" + nRet.get(0);
            msj = msj.substring(1, msj.length() - 3);

        }
        return Integer.parseInt(msj);
    }

//--HONORARIOS------------------------------------------------------------------------------------------------------------
    public String buscaNoHonPorConceptoMedico(int folioPers, int cveConcepIngr, Date fechaIni, Date fechaFin) throws Exception {
        Vector rst = null;
        String sQuery = "";

        if (folioPers == 0 && cveConcepIngr == 0) {
            throw new Exception("ConceptoIngreso.calculaPagodeHonorarios: error de programación, faltan datos");
        } else {
            sQuery = "select * from buscaNoHonPorConceptoMedico(" + folioPers + "," + cveConcepIngr + ",'" + fechaIni + "', '" + fechaFin + "');";
            //System.out.println(sQuery);
            if (getAD() == null) {
                setAD(new AccesoDatos());
                getAD().conectar();
                rst = getAD().ejecutarConsulta(sQuery);
                getAD().desconectar();
                setAD(null);
            }
        }
        return " " + (rst == null ? "" : rst.get(0).toString());
    }

    public String buscaNoHonPorConceptoEnfermeras(int folioPers, int cveConcepIngr, Date fechaIni, Date fechaFin) throws Exception {
        Vector rst = null;
        String sQuery = "";

        if (folioPers == 0 && cveConcepIngr == 0) {
            throw new Exception("ConceptoIngreso.calculaPagodeHonorarios: error de programación, faltan datos");
        } else {
            sQuery = "select * from buscaNoHonPorConceptoEnfermeras(" + folioPers + "," + cveConcepIngr + ",'" + fechaIni + "', '" + fechaFin + "');";
            //System.out.println(sQuery);
            if (getAD() == null) {
                setAD(new AccesoDatos());
                getAD().conectar();
                rst = getAD().ejecutarConsulta(sQuery);
                getAD().desconectar();
                setAD(null);
            }
        }
        return " " + (rst == null ? "" : rst.get(0).toString());
    }

    public String buscaNoHonPorConceptoTecnicos(int folioPers, int cveConcepIngr, Date fechaIni, Date fechaFin) throws Exception {
        Vector rst = null;
        String sQuery = "";

        if (folioPers == 0 && cveConcepIngr == 0) {
            throw new Exception("ConceptoIngreso.calculaPagodeHonorarios: error de programación, faltan datos");
        } else {
            sQuery = "select * from buscaNoHonPorConceptoTecnicos(" + folioPers + "," + cveConcepIngr + ",'" + fechaIni + "', '" + fechaFin + "');";
            //System.out.println(sQuery);
            if (getAD() == null) {
                setAD(new AccesoDatos());
                getAD().conectar();
                rst = getAD().ejecutarConsulta(sQuery);
                getAD().desconectar();
                setAD(null);
            }
        }
        return " " + (rst == null ? "" : rst.get(0).toString());
    }

    public String buscaNoHonorariosPorAutorizarPorConcepto(int folioPers, int cveConcepIngr) throws Exception {
        Vector rst = null;
        String sQuery = "";

        if (folioPers == 0 && cveConcepIngr == 0) {
            throw new Exception("ConceptoIngreso.calculaPagodeHonorarios: error de programación, faltan datos");
        } else {
            sQuery = "select * from  buscaNoHonorariosPorAutorizarPorConcepto(" + folioPers + "," + cveConcepIngr + ");";
            //System.out.println(sQuery);
            if (getAD() == null) {
                setAD(new AccesoDatos());
                getAD().conectar();
                rst = getAD().ejecutarConsulta(sQuery);
                getAD().desconectar();
                setAD(null);
            }
        }
        return " " + (rst == null ? "" : rst.get(0).toString());
    }

//--------------------------------------------------------------------------------------------------------------
//---Catálogo (ABC)-------------------------------------------------------------
    public List<ConceptoIngreso> buscaTodos() 
            throws Exception {
    List<ConceptoIngreso> lista = new ArrayList<ConceptoIngreso>();
    ConceptoIngreso oCI = null;
    Vector rst = null;
    Vector<ConceptoIngreso> vObj = null;
    String sQuery = "";
    int i = 0;

        sQuery = "select * from buscaTodosConcepIngreso()";
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
            vObj = new Vector<ConceptoIngreso>();
            for (i = 0; i < rst.size(); i++) {
                oCI = new ConceptoIngreso();
                Vector vRowTemp = (Vector) rst.elementAt(i);
                oCI.setCveConcep(((Double) vRowTemp.elementAt(0)).intValue());
                oCI.setDescripcion((String) vRowTemp.elementAt(1));
                AreaDeServicio oArea = new AreaDeServicio();
                oArea.setCve("" + ((Double) vRowTemp.elementAt(2)).intValue());
                oCI.setAreaServicio(oArea);
                AreaFisica oAreaFis = new AreaFisica();
                oAreaFis.setCve(((Double) vRowTemp.elementAt(3)).intValue());
                oCI.setAreaFisica(oAreaFis);
                LineaIngreso oLI = new LineaIngreso();
                oLI.setCveLin(((Double) vRowTemp.elementAt(4)).intValue());
                oLI.setDescrip((String) vRowTemp.elementAt(16));
                oCI.setLineaIngreso(oLI);
                if (((String) vRowTemp.elementAt(5)).equals("1")) {
                    oCI.setRegalMed(true);
                }
                if (((String) vRowTemp.elementAt(6)).equals("1")) {
                    oCI.setRegalGuard(true);
                }
                if (((String) vRowTemp.elementAt(7)).equals("1")) {
                    oCI.setRegalEnf(true);
                }
                oCI.setPctRegalEnf(((Double) vRowTemp.elementAt(8)).floatValue());
                oCI.setPctRegalMed(((Double) vRowTemp.elementAt(9)).floatValue());
                oCI.setPctRegalGuard(((Double) vRowTemp.elementAt(10)).floatValue());
                oCI.setPctIVA(((Double) vRowTemp.elementAt(11)).floatValue());
                if (((String) vRowTemp.elementAt(12)).equals("1")) {
                    oCI.setHonMedEnf(true);
                }
                if (((String) vRowTemp.elementAt(13)).equals("1")) {
                    oCI.setPaqEmp(true);
                }
                oCI.setTipoConcIngr((String) vRowTemp.elementAt(14));
                oCI.setMonto(((Double) vRowTemp.elementAt(15)).floatValue());
                lista.add(oCI);
            }
        }
        return lista;
    }
    
    public String insertar() throws Exception {
    Vector rst = null;
    String sQuery = "";
        
        if (this.sDescripConcep.equals("")
                  || this.oAreaServicio == null
                  || this.oAreaServicio.getCve().equals("")) {
            throw new Exception("ConceptoIngreso.insertar: error de programación, faltan datos");
        } else {
            sQuery = "SELECT * from insertaConcepIngreso('"
                      + this.sDescripConcep + "'::varchar,"
                      + this.oAreaServicio.getCve() + "::smallint,"
                      + (this.oAreaFisica == null || this.oAreaFisica.getCve() < 1 ? "null" : this.oAreaFisica.getCve()) + "::smallint,"
                      + (this.oLineaIngreso == null || this.oLineaIngreso.getCveLin() == 0 ? "null" : this.oLineaIngreso.getCveLin()) + "::smallint,"
                      + (this.bRegalMed == true ? "'1'" : "'0'") + "::character(1),"
                      + (this.bRegalGuard == true ? "'1'" : "'0'") + "::character(1),"
                      + (this.bRegalEnf == true ? "'1'" : "'0'") + "::character(1),"
                      + (this.nPctRegalEnf == 0 ? "null" : this.nPctRegalEnf) + "::numeric(4,2),"
                      + (this.nPctRegalMed == 0 ? "null" : this.nPctRegalMed) + "::numeric(4,2),"
                      + (this.nPctRegalGuard == 0 ? "null" : this.nPctRegalGuard) + "::numeric(4,2),"
                      + (this.nPctIVA == 0 ? "null" : this.nPctIVA) + "::numeric(4,2),"
                      + (this.bHonMedEnf == true ? "'1'" : "'0'") + "::character(1),"
                      + (this.bPaqEmp == true ? "'1'" : "'0'") + "::character(1),"
                      + this.fMonto + "::numeric(10,2), '" + sTipoConcIngr + "'::varchar(15)"
                      + ")";
            System.out.println(sQuery);
            if (oAD == null) {
                oAD = new AccesoDatos();
                oAD.conectar();
                rst = oAD.ejecutarConsulta(sQuery);
                oAD.desconectar();
                oAD = null;
            } else {
                rst = oAD.ejecutarConsulta(sQuery); //regresa el código
            }
	}
        return "" + (rst == null ? "" : rst.get(0).toString().substring(1, rst.get(0).toString().length() - 1));
    }        
        
    public String modificar() throws Exception {
    Vector rst = null;
    String sQuery = "";
        
        if (this.nCveConcep == 0
                  || this.sDescripConcep.equals("")
                  || this.oAreaServicio == null
                  || this.oAreaServicio.getCve().equals("")) {
            throw new Exception("ConceptoIngreso.modificar: error de programación, faltan datos");
        } else {
            sQuery = "select * from modificaConcepIngreso("
                      + this.nCveConcep + "::integer,'"
                      + this.sDescripConcep + "'::varchar,"
                      + this.oAreaServicio.getCve() + "::smallint,"
                      + (this.oAreaFisica == null || this.oAreaFisica.getCve() < 1 ? "null" : this.oAreaFisica.getCve()) + "::smallint,"
                      + (this.oLineaIngreso == null || this.oLineaIngreso.getCveLin() == 0 ? "null" : this.oLineaIngreso.getCveLin()) + "::smallint,"
                      + (this.bRegalMed == true ? "'1'" : "'0'") + "::character(1),"
                      + (this.bRegalGuard == true ? "'1'" : "'0'") + "::character(1),"
                      + (this.bRegalEnf == true ? "'1'" : "'0'") + "::character(1),"
                      + (this.nPctRegalEnf == 0 ? "null" : this.nPctRegalEnf) + "::numeric(4,2),"
                      + (this.nPctRegalMed == 0 ? "null" : this.nPctRegalMed) + "::numeric(4,2),"
                      + (this.nPctRegalGuard == 0 ? "null" : this.nPctRegalGuard) + "::numeric(4,2),"
                      + (this.nPctIVA == 0 ? "null" : this.nPctIVA) + "::numeric(4,2),"
                      + (this.bHonMedEnf == true ? "'1'" : "'0'") + "::character(1),"
                      + (this.bPaqEmp == true ? "'1'" : "'0'") + "::character(1),"
                      + this.fMonto + "::numeric(10,2), '" + sTipoConcIngr + "'::varchar(15)"
                      + ")";
            System.out.println(sQuery);
            if (oAD == null) {
                oAD = new AccesoDatos();
                oAD.conectar();
                rst = oAD.ejecutarConsulta(sQuery);
                oAD.desconectar();
                oAD = null;
            } else {
                rst = oAD.ejecutarConsulta(sQuery); //regresa el código
            } 
	}
        return "" + (rst == null ? "" : rst.get(0).toString().substring(1, rst.get(0).toString().length() - 1));
    }
     
    public String eliminar() throws Exception {
    Vector rst = null;
    String sQuery = "";
        
        if (this.nCveConcep == 0) {
            throw new Exception("ConceptoIngreso.eliminar: error de programación, faltan datos");
        } else {
            sQuery = "select * from eliminaConcepIngreso("
                      + this.nCveConcep + "::integer)";
            System.out.println(sQuery);
            if (oAD == null) {
                oAD = new AccesoDatos();
                oAD.conectar();
                rst = oAD.ejecutarConsulta(sQuery);
                oAD.desconectar();
                oAD = null;
            } else {
                rst = oAD.ejecutarConsulta(sQuery); //regresa el código
            } 
	}
        return "" + (rst == null ? "" : rst.get(0).toString().substring(1, rst.get(0).toString().length() - 1));
    }
//------------------------------------------------------------------------------
    
    public List<ConceptoIngreso> obtenerServicios(String s, 
            ArrayList<ConceptoIngreso> servicios) {
        List<ConceptoIngreso> servSelect = new ArrayList<ConceptoIngreso>();
        s = s.toUpperCase();
        try {

            if (s.trim().equals("")) {
                return new ArrayList<ConceptoIngreso>();
            }
            for (ConceptoIngreso srv : servicios) {
                if (srv.getDescripcion().contains(s)
                          || srv.getDescripcion().toUpperCase().contains(
                                    s.toLowerCase())) {
                    servSelect.add(srv);
                }
            }
            return servSelect;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    

    private AccesoDatos getAD() {
        return oAD;
    }

    private void setAD(AccesoDatos oAD) {
        this.oAD = oAD;
    }

    public ServicioMedico getServicioMedico() {
        return oServicioMedico;
    }

    public void setServicioMedico(ServicioMedico oServicioMedico) {
        this.oServicioMedico = oServicioMedico;
    }

    public AreaDeServicio getAreaServicio() {
        return oAreaServicio;
    }

    public void setAreaServicio(AreaDeServicio oAreaServicio) {
        this.oAreaServicio = oAreaServicio;
    }

    public Medicamento getMedicamento() {
        return oMedicamento;
    }

    public void setMedicamento(Medicamento oMedicamento) {
        this.oMedicamento = oMedicamento;
    }

    public MaterialCuracion getMaterialCuracion() {
        return oMaterialCuracion;
    }

    public void setMaterialCuracion(MaterialCuracion oMaterialCuracion) {
        this.oMaterialCuracion = oMaterialCuracion;
    }

    public String getTipoConcIngr() {
        return sTipoConcIngr;
    }

    public void setTipoConcIngr(String sTipoConcIngr) {
        this.sTipoConcIngr = sTipoConcIngr;
    }

    public LineaIngreso getLineaIngreso() {
        return oLineaIngreso;
    }

    public void setLineaIngreso(LineaIngreso oLineaIngreso) {
        this.oLineaIngreso = oLineaIngreso;
    }

    public UnidadMedida getUnidadMedida() {
        return oUnidadMedida;
    }

    public void setUnidadMedida(UnidadMedida oUnidadMedida) {
        this.oUnidadMedida = oUnidadMedida;
    }

    public int getCantidad() {
        return nCantidad;
    }

    public void setCantidad(int nCantidad) {
        this.nCantidad = nCantidad;
    }

    public String getIdGenerico() {
        return idGenerico;
    }

    public void setIdGenerico(String idGenerico) {
        this.idGenerico = idGenerico;
    }

    public boolean getPrecioConCero() {
        return precioConCero;
    }

    public void setPrecioConCero(boolean precioConCero) {
        this.precioConCero = precioConCero;
    }

    public float getMonto() {
        return fMonto;
    }

    public void setMonto(float fMonto) {
        this.fMonto = fMonto;
    }

    public float getMontoNuevo() {
        return fMontoNuevo;
    }

    public void setMontoNuevo(float fMontoNuevo) {
        this.fMontoNuevo = fMontoNuevo;
    }

    public int getDisponibilidadMax() {
        return nDisponibilidadMax;
    }

    public void setDisponibilidadMax(int nDisponibilidadMax) {
        this.nDisponibilidadMax = nDisponibilidadMax;
    }

    public ConceptoIngreServicio getConServ() {
        return oConServ;
    }

    public void setConServ(ConceptoIngreServicio oConServ) {
        this.oConServ = oConServ;
    }

    public boolean getSiena() {
        return bSiena;
    }

    public void setSiena(boolean bSiena) {
        this.bSiena = bSiena;
    }

    public float getPctRegalEnf() {
        return nPctRegalEnf;
    }

    public void setPctRegalEnf(float nPctRegalEnf) {
        this.nPctRegalEnf = nPctRegalEnf;
    }

    public float getPctRegalMed() {
        return nPctRegalMed;
    }

    public void setPctRegalMed(float nPctRegalMed) {
        this.nPctRegalMed = nPctRegalMed;
    }

    public float getPctRegalGuard() {
        return nPctRegalGuard;
    }

    public void setPctRegalGuard(float nPctRegalGuard) {
        this.nPctRegalGuard = nPctRegalGuard;
    }

    public int getCantidadACobrar() {
        return nCantidadACobrar;
    }

    public void setCantidadACobrar(int nCantidadACobrar) {
        this.nCantidadACobrar = nCantidadACobrar;
    }

    public boolean getTipoPaquete() {
        return bTipoPaquete;
    }

    public void setTipoPaquete(boolean bTipoPaquete) {
        this.bTipoPaquete = bTipoPaquete;
    }

    /**
     * @return the bRegalEnf
     */
    public boolean getRegalEnf() {
        return bRegalEnf;
    }

    /**
     * @param bRegalEnf the bRegalEnf to set
     */
    public void setRegalEnf(boolean bRegalEnf) {
        this.bRegalEnf = bRegalEnf;
    }

    /**
     * @return the bRegalMed
     */
    public boolean getRegalMed() {
        return bRegalMed;
    }

    /**
     * @param bRegalMed the bRegalMed to set
     */
    public void setRegalMed(boolean bRegalMed) {
        this.bRegalMed = bRegalMed;
    }

    /**
     * @return the bRegalGuard
     */
    public boolean getRegalGuard() {
        return bRegalGuard;
    }

    /**
     * @param bRegalGuard the bRegalGuard to set
     */
    public void setRegalGuard(boolean bRegalGuard) {
        this.bRegalGuard = bRegalGuard;
    }

    /**
     * @return the nPctIVA
     */
    public float getPctIVA() {
        return nPctIVA;
    }

    /**
     * @param nPctIVA the nPctIVA to set
     */
    public void setPctIVA(float nPctIVA) {
        this.nPctIVA = nPctIVA;
    }

    /**
     * @return the bHonMedEnf
     */
    public boolean getHonMedEnf() {
        return bHonMedEnf;
    }

    /**
     * @param bHonMedEnf the bHonMedEnf to set
     */
    public void setHonMedEnf(boolean bHonMedEnf) {
        this.bHonMedEnf = bHonMedEnf;
    }

    /**
     * @return the bPaqEmp
     */
    public boolean getPaqEmp() {
        return bPaqEmp;
    }

    /**
     * @param bPaqEmp the bPaqEmp to set
     */
    public void setPaqEmp(boolean bPaqEmp) {
        this.bPaqEmp = bPaqEmp;
    }

    /**
     * @return the oAreaFisica
     */
    public AreaFisica getAreaFisica() {
        return oAreaFisica;
    }

    /**
     * @param oAreaFisica the oAreaFisica to set
     */
    public void setAreaFisica(AreaFisica oAreaFisica) {
        this.oAreaFisica = oAreaFisica;
    }
    
    public int getTipoPrincipalPaga() {
        return this.nTipoPrincipalPaga;
    }
    
    public void setTipoPrincipalPaga(int value) {
        this.nTipoPrincipalPaga = value;
    }
    
    
    public String getVerIndicadoPorMed(){
        return this.sVerIndicadoPorMedico;
    }
    public void setVerIndicadoPorMed(String valor){
        this.sVerIndicadoPorMedico = valor;
    }
    
    public boolean getIndicadoPorMedico(){
        return this.bIndicadoPorMedico;
    }
    public void setIndicadoPorMedico(boolean valor){
        this.bIndicadoPorMedico = valor;
    }

    /**
     * @return the bInterconsul
     */
    public boolean getInterconsul() {
        return bInterconsul;
    }

    /**
     * @param bInterconsul the bInterconsul to set
     */
    public void setInterconsul(boolean bInterconsul) {
        this.bInterconsul = bInterconsul;
    }

    /**
     * @return the bSoloEmpleado
     */
    public boolean getSoloEmpleado() {
        return bSoloEmpleado;
    }

    /**
     * @param bSoloEmpleado the bSoloEmpleado to set
     */
    public void setSoloEmpleado(boolean bSoloEmpleado) {
        this.bSoloEmpleado = bSoloEmpleado;
    }

    /**
     * @return the bSoloMedico
     */
    public boolean getSoloMedico() {
        return bSoloMedico;
    }

    /**
     * @param bSoloMedico the bSoloMedico to set
     */
    public void setSoloMedico(boolean bSoloMedico) {
        this.bSoloMedico = bSoloMedico;
    }
    
    /**
     * @return the COMPORT_SERV_MED
     */
    public String getComportServMed() {
        return COMPORT_SERV_MED;
    }

    /**
     * @return the COMPORT_OTRO
     */
    public String getComportOtro() {
        return COMPORT_OTRO;
    }

    /**
     * @return the COMPORT_MAT
     */
    public String getComportMat() {
        return COMPORT_MAT;
    }

    /**
     * @return the COMPORT_MED
     */
    public String getComportMed() {
        return COMPORT_MED;
    }
}
