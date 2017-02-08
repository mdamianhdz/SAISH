package org.apli.modelbeans;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import org.apli.AD.AccesoDatos;
import org.apli.modelbeans.ventaCredito.FormatoCiaCred;

/**
 * Historial del paciente, de acuerdo a la norma mexicana NOM-004-SSA3-2012
 *
 * @author BAOZ
 * @version 1.0
 * @created 29-abr.-2014 14:10:40
 */
public class HistoriaClinica implements Serializable {

    /**
     * Fecha-hora del registro de la historia cl�nica
     */
    private Date dReg;
    /**
     * Paciente
     */
    private Paciente oPac;
    /**
     * Datos del examen f�sico (signos vitales) con los que inicia la historia
     * cl�nica del paciente
     */
    private ExamenFisico oExFis;
    /**
     * M�dico que recomienda al paciente
     */
    private Medico oMedRecomienda;
    /**
     * Descripci�n de alergias
     */
    private String sAlergias;
    /**
     * Motivo de consulta y enfermedad actual
     */
    private String sMotivoConsEnfAct;
    /**
     * Qui�n sugiri� al paciente asistir al Sanatorio Huerta (cuando no se trata
     * de un m�dico)
     */
    private String sRecomendadoPor;
    private boolean bPerin;
    private boolean bNutri;
    private boolean bDes;
    private AccesoDatos oAD;
    public AntecedentesHeredoFamiliares m_AntecedentesHeredoFamiliares;
    public List<EpisodioMedico> listaEpisodios;
    public EpisodioMedico oEpisodioMedico;
    public AntecedentesNoPatol m_AntecedentesNoPatol;
    public AntecedPersPatol m_AntecedPersPatol;
    public AntecedentesGinecoObs m_AntecedentesGinecoObs;
    public AntecedPerinatales m_AntecedPerinatales;
    public AntecedNutricio m_AntecedNutricio;
    public Desarrollo m_Desarrollo;
    public EpisodioCiaCred oEpisodioCC;

    public HistoriaClinica() {
        oMedRecomienda = new Medico();
    }

    public String ingresaPacienteHospital(String sTipoPaciente, 
            List<EpisodioCiaCred> compañiasAutorizadas) throws Exception {
        Vector rst = null;
        String msj = "";
        String sQuery = "";
        String sQuery2 = "";
        String sMedTrat = "";
        String sMedreco = "";
        String sMedreci = "";
        String sFolioServicio = "";
        Valida oVal=new Valida();
        boolean validacionServicio = true;
        if (this.getEpisodioMedico().getMedTratante() == null ||
            this.getEpisodioMedico().getMedTratante().getFolioPers()==0) {
            sMedTrat = "null";
        } else {
            sMedTrat = "" + this.getEpisodioMedico().getMedTratante().getFolioPers();
        }
        if (this.getEpisodioMedico().getMedRecom() == null||
            this.getEpisodioMedico().getMedRecom().getFolioPers()==0) {
            sMedreco = "null";
        } else {
            sMedreco = "" + this.getEpisodioMedico().getMedRecom().getFolioPers();
        }
        if (this.getEpisodioMedico().getMedRecibe() == null||
            this.getEpisodioMedico().getMedRecibe().getFolioPers()==0) {
            sMedreci = "null";
        } else {
            sMedreci = "" + this.getEpisodioMedico().getMedRecibe().getFolioPers();
        }
        if (this.getEpisodioMedico().getDxIngreso()==null){
            this.getEpisodioMedico().setDxIngreso(new Diagnostico());
            this.getEpisodioMedico().getDxIngreso().setCve("9999");
        }
        if (this.getPaciente() == null || 
            this.getEpisodioMedico().getDxIngreso() == null || 
            this.getEpisodioMedico().getHab() == null || 
            this.getEpisodioMedico().getFamiliaracompana().equals("") || 
            this.getEpisodioMedico().getResponsableCuenta().equals("")) {
            throw new Exception("HistoriaClinica.ingresaPacienteHospital: error de programación, faltan datos");
        } else {
            if (sTipoPaciente.equals("1")) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd h:mm:ss");
                sQuery = "SELECT * FROM ingresarahospital(" + sTipoPaciente + 
                        ",0,'" + this.getPaciente().getNombre().toUpperCase() + "','" + 
                        this.getPaciente().getApellidoPaterno().toUpperCase() + "'," + 
                        oVal.cadenaParaBase(this.getPaciente().getApellidoMaterno().toUpperCase()) + ", '" + 
                        sdf.format(this.getPaciente().getNac()) + "'::date, '" + 
                        this.getPaciente().getGenero() + "'," + 
                        sMedTrat + ",'" + this.getPaciente().getTipo() + "'," + 
                        sMedreco + "," + this.getEpisodioMedico().getHab().getHab() + ",'" +
                        oVal.BooleanToBinarioString(this.getEpisodioMedico().getServicioPrestado().getFacturable()) + "'::character varying," + 
                        this.getEpisodioMedico().getServicioPrestado().getQuienPaga() + "::int2,'" +
                        new Date() + "'::date," + 
                        oVal.cadenaParaBase(this.getEpisodioMedico().getServicioPrestado().getObs()) + "::character varying, '" + 
                        this.getEpisodioMedico().getDxIngreso().getCve() + "'::character(6)," + 
                        sMedreci + ",'" + this.getEpisodioMedico().getResponsableCuenta() + "'::character varying(100),'" + 
                        this.getEpisodioMedico().getFamiliaracompana() + "'::character varying(100),'" + 
                        ((this.getPaciente().getFallecido()) ? 1 : 0) + "'::character," + 
                        ((this.getEpisodioMedico().getServicioPrestado().getControlTv()) ? 1 : 0) + "::smallint," + 
                        ((this.getEpisodioMedico().getServicioPrestado().getControlClima()) ? 1 : 0) + "::smallint, " + 
                        this.getEpisodioMedico().getServicioPrestado().getAnticipo() + "::numeric(10,2),'" + 
                        this.getEpisodioMedico().getFamiliarinformacion() + "'::character varying, " +
                        oVal.cadenaParaBase(this.getPaciente().getCorreoElectronico())+"::character varying, " +
                        oVal.cadenaParaBase(this.getEpisodioMedico().getDomicilioFamiliar())+"::character varying, " +
                        oVal.cadenaParaBase(this.getEpisodioMedico().getTelefonoFamiliar())+"::character varying, " +
                        oVal.cadenaParaBase(this.getEpisodioMedico().getEmpresaFamiliar())+"::character varying, " +
                        oVal.cadenaParaBase(this.getEpisodioMedico().getDomicilioEmpresaFamiliar())+"::character varying, " +
                        oVal.cadenaParaBase(this.getEpisodioMedico().getTelefonoEmpresaFamiliar())+"::character varying " +
                        ");";
            } else {
                sQuery = 
                        "SELECT * FROM ingresarahospital(" + sTipoPaciente + "::integer," +
                        this.getPaciente().getFolioPac() + 
                        "::integer,null::character varying,null::character varying,null::character varying,null::timestamp without time zone, null::character, " +
                        sMedTrat + "::integer,' '::character," + sMedreco + "::integer," + 
                        this.getEpisodioMedico().getHab().getHab() + "::integer,'" +
                        new Valida().BooleanToBinarioString(this.getEpisodioMedico().getServicioPrestado().getFacturable()) + "'::character," + 
                        this.getEpisodioMedico().getServicioPrestado().getQuienPaga() + "::smallint,'" + 
                        new Date() + "'::timestamp without time zone," + 
                        oVal.cadenaParaBase(this.getEpisodioMedico().getServicioPrestado().getObs()) + "::character varying, '" + 
                        this.getEpisodioMedico().getDxIngreso().getCve() + "'::character(6)," + 
                        sMedreci + "::integer,'" + this.getEpisodioMedico().getResponsableCuenta() + 
                        "'::character varying(100),'" + this.getEpisodioMedico().getFamiliaracompana() + 
                        "'::character varying(100),'" + ((this.getPaciente().getFallecido()) ? 1 : 0) + 
                        "'::character," + ((this.getEpisodioMedico().getServicioPrestado().getControlTv()) ? 1 : 0) + 
                        "::smallint," + ((this.getEpisodioMedico().getServicioPrestado().getControlClima()) ? 1 : 0) + 
                        "::smallint, " + this.getEpisodioMedico().getServicioPrestado().getAnticipo() + 
                        "::numeric(10,2),'" + this.getEpisodioMedico().getFamiliarinformacion() + 
                        "'::character varying, " +
                        oVal.cadenaParaBase(this.getPaciente().getCorreoElectronico())+"::character varying, " +
                        oVal.cadenaParaBase(this.getEpisodioMedico().getDomicilioFamiliar())+"::character varying, " +
                        oVal.cadenaParaBase(this.getEpisodioMedico().getTelefonoFamiliar())+"::character varying, " +
                        oVal.cadenaParaBase(this.getEpisodioMedico().getEmpresaFamiliar())+"::character varying, " +
                        oVal.cadenaParaBase(this.getEpisodioMedico().getDomicilioEmpresaFamiliar())+"::character varying, " +
                        oVal.cadenaParaBase(this.getEpisodioMedico().getTelefonoEmpresaFamiliar())+"::character varying " +
                        ");";
            }
            System.out.println(sQuery);
            if (getAD() == null) {
                setAD(new AccesoDatos());
                getAD().conectar();
                rst = getAD().ejecutarConsulta(sQuery);
                sFolioServicio = new Valida().eliminaMSJCorchetes("" + rst.get(0));
                if (sFolioServicio.indexOf("ERROR") > 0) {
                    validacionServicio = false;
                }
                if (validacionServicio == true) {
                    if (compañiasAutorizadas != null) {
                        for (int i = 0; i < compañiasAutorizadas.size(); i++) {
                            sQuery2 += compañiasAutorizadas.get(i).getRegistraAutorizacionYDocumt(sFolioServicio, 0);
                            for (int j = 0; j < compañiasAutorizadas.get(i).getFormatos().size(); j++) {
                                if (compañiasAutorizadas.get(i).getFormatos().get(j).getDocumento()) {
                                    sQuery2 += compañiasAutorizadas.get(i).getRegistraAutorizacionYDocumt(sFolioServicio, compañiasAutorizadas.get(i).getFormatos().get(j).getIdFmt());
                                }
                            }
                        }
                        rst = getAD().ejecutarConsulta(sQuery2);
                        
                        msj = new Valida().eliminaMSJCorchetes("" + rst.get(0));
                        if (msj.indexOf("ERROR") > 0) {
                            sFolioServicio = msj;
                        }
                    }
                }
                getAD().desconectar();
                setAD(null);
            }
        }
        return sFolioServicio;
    }

    public String actualzaIngresaPacienteHospital(String sFolioServicio, int nEpiMed, List<EpisodioCiaCred> compañiasAutorizadas) throws Exception {
        Vector rst = null;
        String msj = "";
        String sQuery = "";
        String sQuery2 = "";
        boolean validacionServicio = true;
        if (sFolioServicio.equals("") || nEpiMed == 0 || this.getEpisodioMedico().getHab().getHab() == 0 || this.getEpisodioMedico().getHabReg().getHab() == 0) {
            throw new Exception("Funcion.actualzaIngresaPacienteHospital: error de programación, faltan datos");
        } else {
            sQuery = "SELECT * FROM actualizaingresarahospital(" + this.getPaciente().getFolioPac() + ", '" + sFolioServicio + "'::character varying, " + nEpiMed + "," + this.getEpisodioMedico().getHab().getHab() + "," + this.getEpisodioMedico().getHabReg().getHab() + ", " + this.getEpisodioMedico().getServicioPrestado().getQuienPaga() + "::int2, '" + this.getEpisodioMedico().getServicioPrestado().getObs() + "'::character varying);";
            if (getAD() == null) {
                setAD(new AccesoDatos());
                getAD().conectar();
                rst = getAD().ejecutarConsulta(sQuery);
                msj = "";
                msj = new Valida().eliminaMSJCorchetes("" + rst.get(0));
                if (msj.indexOf("ERROR") > 0) {
                    validacionServicio = false;
                }
                if (validacionServicio == true) {
                    if (compañiasAutorizadas != null) {
                        for (int i = 0; i < compañiasAutorizadas.size(); i++) {
                            if (compañiasAutorizadas.get(i).getCompCred().getIdEmp() == 1) {
                                if (compañiasAutorizadas.get(i).getFormatos().get(0).getDocumento() == false && compañiasAutorizadas.get(i).getFormatos().get(1).getDocumento() == false && (compañiasAutorizadas.get(i).getFormatos().get(0).getDocumentoreg() == true || compañiasAutorizadas.get(i).getFormatos().get(1).getDocumentoreg() == true)) {
                                    sQuery2 += compañiasAutorizadas.get(i).getActualizaAutorizacionYDocum("eliminaEmp", nEpiMed, this.getPaciente().getFolioPac(), 3);
                                } else {

                                    for (int j = 0; j < compañiasAutorizadas.get(i).getFormatos().size(); j++) {
                                        if (compañiasAutorizadas.get(i).getFormatos().get(j).getDocumento() == false && compañiasAutorizadas.get(i).getFormatos().get(j).getDocumentoreg() == true) {
                                            sQuery2 += compañiasAutorizadas.get(i).getActualizaAutorizacionYDocum("eliminaDoc", nEpiMed, this.getPaciente().getFolioPac(), compañiasAutorizadas.get(i).getFormatos().get(j).getIdFmt());
                                        }
                                        if ((compañiasAutorizadas.get(i).getFormatos().get(j).getDocumentoreg() == false && compañiasAutorizadas.get(i).getFormatos().get(j).getDocumento() == true)) {
                                            sQuery2 += compañiasAutorizadas.get(i).getRegistraAutorizacionYDocumt(sFolioServicio, compañiasAutorizadas.get(i).getFormatos().get(j).getIdFmt());
                                        }
                                    }

                                }
                            } else if (compañiasAutorizadas.get(i).getCompCred().getIdEmp() != 1) {
                                if (compañiasAutorizadas.get(i).getEliminaEmpresa() == true) {
                                    sQuery2 += compañiasAutorizadas.get(i).getActualizaAutorizacionYDocum("eliminaEmp", nEpiMed, this.getPaciente().getFolioPac(), 3);
                                } else {
                                    sQuery2 += compañiasAutorizadas.get(i).getActualizaAutorizacionYDocum("actualiza", nEpiMed, this.getPaciente().getFolioPac(), 3);

                                    for (int j = 0; j < compañiasAutorizadas.get(i).getFormatos().size(); j++) {
                                        System.out.println("Emp:" + compañiasAutorizadas.get(i).getCompCred().getNombreCorto() + ", regOri:" + compañiasAutorizadas.get(i).getFormatos().get(j).getDocumentoreg() + ", regNuevo:" + compañiasAutorizadas.get(i).getFormatos().get(j).getDocumento());
                                        if (compañiasAutorizadas.get(i).getFormatos().get(j).getDocumento() == false && compañiasAutorizadas.get(i).getFormatos().get(j).getDocumentoreg() == true) {
                                            sQuery2 += compañiasAutorizadas.get(i).getActualizaAutorizacionYDocum("eliminaDoc", nEpiMed, this.getPaciente().getFolioPac(), compañiasAutorizadas.get(i).getFormatos().get(j).getIdFmt());
                                        }
                                        if ((compañiasAutorizadas.get(i).getFormatos().get(j).getDocumentoreg() == false && compañiasAutorizadas.get(i).getFormatos().get(j).getDocumento() == true)) {
                                            System.out.println("Registrará");
                                            sQuery2 += compañiasAutorizadas.get(i).getRegistraAutorizacionYDocumt(sFolioServicio, compañiasAutorizadas.get(i).getFormatos().get(j).getIdFmt());
                                        }
                                    }
                                }
                            }
                        }
                        System.out.println("sQuery2:" + sQuery2);
                        rst = getAD().ejecutarConsulta(sQuery2);

                        msj = new Valida().eliminaMSJCorchetes("" + rst.get(0));
                        if (msj.indexOf("ERROR") > 0) {
                            sFolioServicio = msj;
                        }
                    }
                }
                getAD().desconectar();
                setAD(null);
            }
        }

        return sFolioServicio;
    }

    public String ingresaRecienNacidoaHospital(List<HistoriaClinica> nacidos, 
            String sApellPat, String sApellMat, int folioMadre) throws Exception {
        Vector rst = null;
        String msj = "";
        String sQuery = "";
        String sQuery2 = "";
        String sMedTrat = "";
        String sMedreco = "";
        String sMedreci = "";
        if (this.getEpisodioMedico().getMedTratante() == null ||
            this.getEpisodioMedico().getMedTratante().getFolioPers() == 0) {
            sMedTrat = "null";
        } else {
            sMedTrat = "" + this.getEpisodioMedico().getMedTratante().getFolioPers();
        }
        if (this.getEpisodioMedico().getMedRecom() == null ||
            this.getEpisodioMedico().getMedRecom().getFolioPers()==0) {
            sMedreco = sMedTrat;
        } else {
            sMedreco = "" + this.getEpisodioMedico().getMedRecom().getFolioPers();
        }
        if (this.getEpisodioMedico().getMedRecibe() == null ||
            this.getEpisodioMedico().getMedRecibe().getFolioPers()==0) {
            sMedreci = "null";
        } else {
            sMedreci = "" + this.getEpisodioMedico().getMedRecibe().getFolioPers();
        }
        String sFolioServicio = "";
        if (this.getEpisodioMedico().getFamiliaracompana().equals("") || this.getEpisodioMedico().getResponsableCuenta().equals("") || sApellPat.equals("") || sApellMat.equals("") || folioMadre == 0) {
            throw new Exception("Funcion.ingresaRecienNacido: error de programación, faltan datos");
        } else {
            if (getAD() == null) {
                setAD(new AccesoDatos());
                getAD().conectar();
                for (int i = 0; i < nacidos.size(); i++) {
                    String sDiagn = "";
                    if (nacidos.get(i).getAntecedPerinatales().getDiag().getCve() == null) {
                        if (nacidos.size()>1)
                            sDiagn = "Z376";
                        else
                            if (nacidos.get(i).getPaciente().getFallecido())
                                sDiagn = "Z371";
                            else
                                sDiagn = "Z370";
                    } else {
                        sDiagn = nacidos.get(i).getAntecedPerinatales().getDiag().getCve();
                    }
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd h:mm:ss");
                    if (nacidos.get(i).getPaciente().getNombre().equals("") || sApellPat.equals("") || sApellMat.equals("")) {
                        throw new Exception("Funcion.ingresaRecienNacido: error de programación, faltan datos en los datos de los recien nacidos");
                    } else if (nacidos.get(i).getPaciente().getNac() == null) {
                        throw new Exception("Funcion.ingresaRecienNacido: error de programación, faltan dato de datenac para recien nacido");
                    } else if (nacidos.get(i).getPaciente().getGenero() == ' ') {
                        throw new Exception("Funcion.ingresaRecienNacido: error de programación, faltan dato de genero para para recien nacido");
                    } else if (nacidos.get(i).getPaciente().getTipo().equals("")) {
                        throw new Exception("Funcion.ingresaRecienNacido: error de programación, faltan dato de tipo para para recien nacido");
                    } else if (nacidos.get(i).getEpisodioMedico().getHab() == null) {
                        throw new Exception("Funcion.ingresaRecienNacido: error de programación, faltan dato de habitación para para recien nacido");
                    } else if (nacidos.get(i).getAntecedPerinatales().getObs().equals("")) {
                        throw new Exception("Funcion.ingresaRecienNacido: error de programación, faltan dato de observaciones para para recien nacido");
                    } else {
                        sQuery = "SELECT * FROM ingresarahospital(1,0,'" + 
                                nacidos.get(i).getPaciente().getNombre().toUpperCase() + "','" + 
                                sApellPat.toUpperCase() + "','" + 
                                sApellMat.toUpperCase() + "', '" + 
                                sdf.format(nacidos.get(i).getPaciente().getNac()) + 
                                "'::timestamp without time zone, '" + nacidos.get(i).getPaciente().getGenero() + "'," + 
                                sMedTrat + ",'" + nacidos.get(i).getPaciente().getTipo() + "'," + sMedreco + "," +
                                nacidos.get(i).getEpisodioMedico().getHab().getHab() + ",'" + 
                                new Valida().BooleanToBinarioString(this.getEpisodioMedico().getServicioPrestado().getFacturable()) + 
                                "'::character varying," + 
                                this.getEpisodioMedico().getServicioPrestado().getQuienPaga() + "::int2,'" + 
                                new Date() + "'::date,'" + nacidos.get(i).getAntecedPerinatales().getObs() + 
                                "'::character varying,'" + sDiagn + "'::character(6)," + sMedreci + ",'" + 
                                this.getEpisodioMedico().getResponsableCuenta() + "'::character varying(100),'" + 
                                this.getEpisodioMedico().getFamiliaracompana() + "'::character varying(100),'" + 
                                ((nacidos.get(i).getPaciente().getFallecido()) ? 1 : 0) + 
                                "'::character," + 
                                ((this.getEpisodioMedico().getServicioPrestado().getControlTv()) ? 1 : 0) + "::smallint," + ((this.getEpisodioMedico().getServicioPrestado().getControlClima()) ? 1 : 0) + "::smallint, " + 
                                this.getEpisodioMedico().getServicioPrestado().getAnticipo() + "::numeric(10,2),'" + this.getEpisodioMedico().getFamiliarinformacion() + "'::character varying, null::character varying);";
                        System.out.println(sQuery);
                        rst = getAD().ejecutarConsulta(sQuery);
                        sFolioServicio = new Valida().eliminaMSJCorchetes("" + rst.get(0));
                        if (sFolioServicio.contains("ERROR")) {
                            break;
                        } else {
                            int foliopac = 0;
                            foliopac = nacidos.get(i).getPaciente().buscaPacientePorServiPrest(sFolioServicio);
                            AntecedPerinatales oAP = new AntecedPerinatales();
                            sQuery2 = oAP.getQueryGuardaAntecedentesPerinat(foliopac, nacidos.get(i).getAntecedPerinatales());
                            rst = getAD().ejecutarConsulta(sQuery2);
                            String msj2 = "" + rst.get(0);
                            if (msj2.indexOf("ERROR") > 0) {
                                sFolioServicio = msj2;
                                break;
                            } else {
                                int nIdPac = new Paciente().buscaPacientePorServiPrest(sFolioServicio);
                                String sQuery3 = this.registrarRelacionMadreHijo(folioMadre, nIdPac);
                                rst = getAD().ejecutarConsulta(sQuery3);
                                String msj3 = "" + rst.get(0);
                                if (msj3.indexOf("ERROR") > 0) {
                                    sFolioServicio = msj3;
                                    break;
                                } else {
                                    nacidos.get(i).getEpisodioMedico().getServicioPrestado().setIdFolio(sFolioServicio);

                                }
                            }
                        }
                    }
                }
                getAD().desconectar();
                setAD(null);
            }
        }
        return sFolioServicio;
    }

    public String registrarRelacionMadreHijo(int nFolioMadre, int nFolioHijo) throws Exception {
        String sQuery = "";
        if (nFolioMadre == 0 || nFolioHijo == 0) {
            throw new Exception("HistoriaClinica.registrarRelacionMadreHijo: error de programación, faltan datos");
        } else {
            sQuery = "SELECT * FROM registrahijorelacionmama(" + nFolioMadre + "," + nFolioHijo + ");";
        }
        return sQuery;
    }

    public String egresaPacienteHospital(int nCveEpi, int atencionPostQuiru, Date dAlta) throws Exception {

        Vector rst = null;
        String sQuery = "";
        if (nCveEpi == 0 || dAlta == null) {
            throw new Exception("HistoriaClinica.egresaPacienteHospital: error de programación, faltan datos");
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd h:mm:ss");
            sQuery = "SELECT * FROM egresarpacientehospital(" + nCveEpi + "," + atencionPostQuiru + ", '" + sdf.format(dAlta) + "'::timestamp without time zone);";
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

    public List<EpisodioMedico> getListaEpisodios() {
        return listaEpisodios;
    }

    public void setListaEpisodios(List<EpisodioMedico> listaEpisodios) {
        this.listaEpisodios = listaEpisodios;
    }

    public void buscaEpisodiosMedicos() {
    }

    public EpisodioMedico getEpisodioMedico() {
        return oEpisodioMedico;
    }

    public void setEpisodioMedico(EpisodioMedico oEpisodioMedico) {
        this.oEpisodioMedico = oEpisodioMedico;
    }

    public Paciente getPaciente() {
        return oPac;
    }

    public void setPaciente(Paciente oPac) {
        this.oPac = oPac;
    }

    public Medico getMedRecomienda() {
        return oMedRecomienda;
    }

    public void setMedRecomienda(Medico oMedRecomienda) {
        this.oMedRecomienda = oMedRecomienda;
    }

    public String getAlergias() {
        return sAlergias;
    }

    public void setAlergias(String sAlergias) {
        this.sAlergias = sAlergias;
    }

    public String getMotivoConsEnfAct() {
        return sMotivoConsEnfAct;
    }

    public void setMotivoConsEnfAct(String sMotivoConsEnfAct) {
        this.sMotivoConsEnfAct = sMotivoConsEnfAct;
    }

    public ExamenFisico getExamenFisico() {
        return oExFis;
    }

    public void setExamenFisico(ExamenFisico oExFis) {
        this.oExFis = oExFis;
    }

    public Date getReg() {
        return dReg;
    }

    public void setReg(Date dReg) {
        this.dReg = dReg;
    }

    public AntecedentesHeredoFamiliares getAntecedentesHeredoFamiliares() {
        return m_AntecedentesHeredoFamiliares;
    }

    public void setAntecedentesHeredoFamiliares(AntecedentesHeredoFamiliares m_AntecedentesHeredoFamiliares) {
        this.m_AntecedentesHeredoFamiliares = m_AntecedentesHeredoFamiliares;
    }

    public AntecedentesNoPatol getAntecedentesNoPatol() {
        return m_AntecedentesNoPatol;
    }

    public void setAntecedentesNoPatol(AntecedentesNoPatol m_AntecedentesNoPatol) {
        this.m_AntecedentesNoPatol = m_AntecedentesNoPatol;
    }

    public AntecedPersPatol getAntecedPersPatol() {
        return m_AntecedPersPatol;
    }

    public void setAntecedPersPatol(AntecedPersPatol m_AntecedPersPatol) {
        this.m_AntecedPersPatol = m_AntecedPersPatol;
    }

    public AntecedentesGinecoObs getAntecedentesGinecoObs() {
        return m_AntecedentesGinecoObs;
    }

    public void setAntecedentesGinecoObs(AntecedentesGinecoObs m_AntecedentesGinecoObs) {
        this.m_AntecedentesGinecoObs = m_AntecedentesGinecoObs;
    }

    public AntecedPerinatales getAntecedPerinatales() {
        return m_AntecedPerinatales;
    }

    public void setAntecedPerinatales(AntecedPerinatales m_AntecedPerinatales) {
        this.m_AntecedPerinatales = m_AntecedPerinatales;
    }

    public AntecedNutricio getAntecedNutricio() {
        return m_AntecedNutricio;
    }

    public void setAntecedNutricio(AntecedNutricio m_AntecedNutricio) {
        this.m_AntecedNutricio = m_AntecedNutricio;
    }

    public Desarrollo getDesarrollo() {
        return m_Desarrollo;
    }

    public void setDesarrollo(Desarrollo m_Desarrollo) {
        this.m_Desarrollo = m_Desarrollo;
    }

    public String getRecomendadoPor() {
        return sRecomendadoPor;
    }

    public void setRecomendadoPor(String sRecomendadoPor) {
        this.sRecomendadoPor = sRecomendadoPor;
    }

    public void setAD(AccesoDatos oAD) {
        this.oAD = oAD;
    }

    public AccesoDatos getAD() {
        return oAD;
    }

    public boolean isPerin() {
        return bPerin;
    }

    public void setPerin(boolean bPerin) {
        this.bPerin = bPerin;
    }

    public boolean isNutri() {
        return bNutri;
    }

    public void setNutri(boolean bNutri) {
        this.bNutri = bNutri;
    }

    public boolean isDes() {
        return bDes;
    }

    public void setDes(boolean bDes) {
        this.bDes = bDes;
    }

    public EpisodioCiaCred getEpisodioCC() {
        return oEpisodioCC;
    }

    public void setEpisodioCC(EpisodioCiaCred oEpisodioCC) {
        this.oEpisodioCC = oEpisodioCC;
    }

    public HistoriaClinica buscaHC(Paciente oPac) throws Exception {
        HistoriaClinica oHC = new HistoriaClinica();
        oHC.setPaciente(oPac);
        oExFis = new ExamenFisico();
        AntecedentesHeredoFamiliares oAHF = new AntecedentesHeredoFamiliares();
        AntecedentesGinecoObs oAG = new AntecedentesGinecoObs();
        AntecedentesNoPatol oANP = new AntecedentesNoPatol();
        AntecedPersPatol oAPP = new AntecedPersPatol();
        AntecedPerinatales oAP = new AntecedPerinatales();
        AntecedNutricio oAN = new AntecedNutricio();
        Desarrollo oD = new Desarrollo();
        EpisodioMedico oEM = new EpisodioMedico();
        ServicioPrestado oSP = new ServicioPrestado();
        Medico oMed = new Medico();
        Medico oMedRec = new Medico();
        Medico oMedTrat = new Medico();
        EstadoCivil oEC = new EstadoCivil();
        Diagnostico oDiag = new Diagnostico();
        Estado oEdo = new Estado();
        Municipio oMpio = new Municipio();
        Ciudad oCd = new Ciudad();
        if (oPac.getFolioPac() == 0) {
            oHC.setPaciente(oPac);
            oHC.setExamenFisico(oExFis);
            oHC.setAntecedentesHeredoFamiliares(oAHF);
            oHC.setAntecedentesGinecoObs(oAG);
            oHC.setAntecedentesNoPatol(oANP);
            oHC.setAntecedPersPatol(oAPP);
            oHC.setAntecedPerinatales(oAP);
            oHC.setAntecedNutricio(oAN);
            oHC.setDesarrollo(oD);
            oEM.setMedTratante(oMedTrat);
            oHC.setEpisodioMedico(oEM);
            oHC.getEpisodioMedico().setServicioPrestado(oSP);
            oHC.getEpisodioMedico().setDxIngreso(oDiag);
            oHC.setMedRecomienda(oMed);
            oHC.getPaciente().setEdoCiv(oEC);
            oHC.getPaciente().setMedRecomienda(oMedRec);
            oHC.getPaciente().setEdo(oEdo);
            oHC.getPaciente().setMpio(oMpio);
            oHC.getPaciente().setCiudad(oCd);
            oHC.setEpisodioCC(new EpisodioCiaCred());
        } else {
            Vector rst = null;
            String sQuery = "";

            sQuery = "select * from buscahistorial(" + oPac.getFolioPac() + ");";
            if (this.getAD() == null) {
                this.setAD(new AccesoDatos());
                this.getAD().conectar();
                rst = this.getAD().ejecutarConsulta(sQuery);
            } else {
                rst = this.getAD().ejecutarConsulta(sQuery);
            }

            if (rst != null && rst.size() == 1) {
                Vector vRowTemp = (Vector) rst.elementAt(0);
                oPac.setFolioPac(((Double) vRowTemp.elementAt(0)).intValue());
                oHC.setReg((Date) vRowTemp.elementAt(1));
                oPac.setApellidoPaterno((String) vRowTemp.elementAt(2));
                oPac.setApellidoMaterno((String) vRowTemp.elementAt(3));
                oPac.setNombre((String) vRowTemp.elementAt(4));
                oPac.setNac((Date) vRowTemp.elementAt(5));
                oPac.setLugarNac((String) vRowTemp.elementAt(6));
                oPac.setRFC((String) vRowTemp.elementAt(7));
                oPac.setCURP((String) vRowTemp.elementAt(8));
                oPac.setEdoCiv(oEC);
                oPac.getEdoCiv().setCve((String) vRowTemp.elementAt(9));
                oPac.setGenero(((String) vRowTemp.elementAt(10)).charAt(0));
                oPac.setFamiliarCercano((String) vRowTemp.elementAt(11));
                oPac.setOcupacion((String) vRowTemp.elementAt(12));
                oPac.setSeEntero((String) vRowTemp.elementAt(13));
                oHC.setRecomendadoPor((String) vRowTemp.elementAt(14));
                oMed.setFolioPers(((Double) vRowTemp.elementAt(15)).intValue());
                oHC.setMedRecomienda(oMed);
                oPac.setMedRecomienda(oMed);
                oPac.setTipo((String) vRowTemp.elementAt(16));
                oPac.setCalleYNum((String) vRowTemp.elementAt(17));
                oPac.setColonia((String) vRowTemp.elementAt(18));
                oPac.setCiudadPoblacion((String) vRowTemp.elementAt(19));
                oPac.setCP((String) vRowTemp.elementAt(20));
                oPac.setTelCasa((String) vRowTemp.elementAt(21));
                oPac.setTelCelular((String) vRowTemp.elementAt(22));
                oHC.setAlergias((String) vRowTemp.elementAt(23));
                oHC.setMotivoConsEnfAct((String) vRowTemp.elementAt(24));
                oExFis.setCveExFis(((Double) vRowTemp.elementAt(25)).intValue());
                oMedRec.setFolioPers(((Double) vRowTemp.elementAt(26)).intValue());
                oEdo.setCve((String) vRowTemp.elementAt(27));
                oEdo.setDescrip((String) vRowTemp.elementAt(28));
                oPac.setEdo(oEdo);
                oMpio.setCve((String) vRowTemp.elementAt(29));
                oMpio.setDescrip((String) vRowTemp.elementAt(30));
                oPac.setMpio(oMpio);
                oCd.setCve((String) vRowTemp.elementAt(31));
                oCd.setDescrip((String) vRowTemp.elementAt(32));
                oPac.setCiudad(oCd);
                oEM.setMedRecom(oMedRec);
                oMedTrat.setFolioPers(((Double) vRowTemp.elementAt(33)).intValue());
                oEM.setMedTratante(oMedTrat);
                oEM.setCveepisodio(((Double) vRowTemp.elementAt(34)).intValue());
                oPac.setCorreoElectronico((String)vRowTemp.elementAt(35));
                oEM.getMedRecibe().setFolioPers(((Double) vRowTemp.elementAt(36)).intValue());
                oHC.setPaciente(oPac);
                oHC.setEpisodioMedico(oEM);
                System.out.println(oHC.getPaciente().getCiudad().getDescrip());
            }
            sQuery = "select * from buscaLlaveExamenFisico(" + oExFis.getCveExFis() + ");";
            rst = this.getAD().ejecutarConsulta(sQuery);
            if (rst != null && rst.size() == 1) {
                Vector vRowTemp = (Vector) rst.elementAt(0);
                oExFis.setCveExFis(((Double) vRowTemp.elementAt(0)).intValue());
                oExFis.setFrecCard(((Double) vRowTemp.elementAt(1)).intValue());
                oExFis.setFrecResp(((Double) vRowTemp.elementAt(2)).intValue());
                oExFis.setPeso(((Double) vRowTemp.elementAt(3)).floatValue());
                oExFis.setTAA(((Double) vRowTemp.elementAt(4)).intValue());
                oExFis.setTAB(((Double) vRowTemp.elementAt(5)).intValue());
                oExFis.setTalla(((Double) vRowTemp.elementAt(6)).floatValue());
                oExFis.setTemp(((Double) vRowTemp.elementAt(7)).floatValue());
            }
            oHC.setExamenFisico(oExFis);
            sQuery = "select * from buscallaveantecedentesheredofam(" + oHC.getPaciente().getFolioPac() + ");";
            rst = this.getAD().ejecutarConsulta(sQuery);
            if (rst != null && rst.size() == 1) {
                Vector vRowTemp = (Vector) rst.elementAt(0);
                oAHF.setAlcoholismo((String) vRowTemp.elementAt(1));
                oAHF.setAlergias((String) vRowTemp.elementAt(2));
                oAHF.setAltMentales((String) vRowTemp.elementAt(3));
                oAHF.setAsma((String) vRowTemp.elementAt(4));
                oAHF.setCancer((String) vRowTemp.elementAt(5));
                oAHF.setCongenitos((String) vRowTemp.elementAt(6));
                oAHF.setConvulsiones((String) vRowTemp.elementAt(7));
                oAHF.setDiabetes((String) vRowTemp.elementAt(8));
                oAHF.setDrogadiccion((String) vRowTemp.elementAt(9));
                oAHF.setEnfisema((String) vRowTemp.elementAt(10));
                oAHF.setEpilepsia((String) vRowTemp.elementAt(11));
                oAHF.setFamCardiopatias((String) vRowTemp.elementAt(12));
                oAHF.setHAS((String) vRowTemp.elementAt(13));
                oAHF.setIAM((String) vRowTemp.elementAt(14));
                oAHF.setLitiasis((String) vRowTemp.elementAt(15));
                oAHF.setTabaquismo((String) vRowTemp.elementAt(16));
                oAHF.setTuberculosis((String) vRowTemp.elementAt(17));
            }
            oHC.setAntecedentesHeredoFamiliares(oAHF);
            sQuery = "select * from buscallaveantecedentesperspatol(" + oHC.getPaciente().getFolioPac() + ");";
            rst = this.getAD().ejecutarConsulta(sQuery);
            if (rst != null && rst.size() == 1) {
                Vector vRowTemp = (Vector) rst.elementAt(0);
                oAPP.setAlergicos((String) vRowTemp.elementAt(1));
                oAPP.setCardiopatias((String) vRowTemp.elementAt(2));
                oAPP.setDiabetes((String) vRowTemp.elementAt(3));
                oAPP.setHosp((String) vRowTemp.elementAt(4));
                oAPP.setHTA((String) vRowTemp.elementAt(5));
                oAPP.setQx((String) vRowTemp.elementAt(6));
                oAPP.setTransf((String) vRowTemp.elementAt(7));
                oAPP.setTraumat((String) vRowTemp.elementAt(8));
            }
            oHC.setAntecedPersPatol(oAPP);
            sQuery = "select * from buscallaveantecedentesnopatol(" + oHC.getPaciente().getFolioPac() + ");";
            rst = this.getAD().ejecutarConsulta(sQuery);
            if (rst != null && rst.size() == 1) {
                Vector vRowTemp = (Vector) rst.elementAt(0);
                oANP.setAguaPot(convierteBool((String) vRowTemp.elementAt(1)));
                oANP.setAlcoholismo(convierteBool((String) vRowTemp.elementAt(2)));
                oANP.setDrenaje(convierteBool((String) vRowTemp.elementAt(3)));
                oANP.setElectricidad(convierteBool((String) vRowTemp.elementAt(4)));
                oANP.setServSanit(convierteBool((String) vRowTemp.elementAt(5)));
                oANP.setTabaquismo(convierteBool((String) vRowTemp.elementAt(6)));
                oANP.setAnimales((String) vRowTemp.elementAt(7));
                oANP.setCuadroVac((String) vRowTemp.elementAt(8));
                oANP.setDieta((String) vRowTemp.elementAt(9));
                oANP.setTipoCasaHab((String) vRowTemp.elementAt(10));
            }
            oHC.setAntecedentesNoPatol(oANP);
            sQuery = "select * from buscallaveantecedentesginecoobs(" + oHC.getPaciente().getFolioPac() + ");";
            rst = this.getAD().ejecutarConsulta(sQuery);
            if (rst != null && rst.size() == 1) {
                Vector vRowTemp = (Vector) rst.elementAt(0);
                oAG.setFecUltNac((Date) vRowTemp.elementAt(1));
                oAG.setFUR((Date) vRowTemp.elementAt(2));
                oAG.setUltPapanicolau((Date) vRowTemp.elementAt(3));
                oAG.setIVSA(((Double) vRowTemp.elementAt(4)).intValue());
                oAG.setMenarquia(((Double) vRowTemp.elementAt(5)).intValue());
                oAG.setPesoProd(((Double) vRowTemp.elementAt(6)).floatValue());
                oAG.setRitmoDias(((Double) vRowTemp.elementAt(7)).intValue());
                oAG.setRitmoFrec(((Double) vRowTemp.elementAt(8)).intValue());
                oAG.setResultadoPap((String) vRowTemp.elementAt(9));
            }
            oHC.setAntecedentesGinecoObs(oAG);
            sQuery = "select * from buscallaveantecedentesperinat(" + oHC.getPaciente().getFolioPac() + ");";
            rst = this.getAD().ejecutarConsulta(sQuery);
            if (rst != null && rst.size() == 1) {
                Vector vRowTemp = (Vector) rst.elementAt(0);
                oAP.setPeso(((Double) vRowTemp.elementAt(1)).intValue());
                oAP.setTalla(((Double) vRowTemp.elementAt(2)).intValue());
                oAP.setEmbControlado((String) vRowTemp.elementAt(3));
                oAP.setEmbNormal((String) vRowTemp.elementAt(4));
                oAP.setNeonatoSano((String) vRowTemp.elementAt(5));
                oAP.setObs((String) vRowTemp.elementAt(6));
                oAP.setPartoNormal((String) vRowTemp.elementAt(7));
                oAP.setTipoparto((String) vRowTemp.elementAt(8));
                oAP.getDiag().setCve((String) vRowTemp.elementAt(9));
                oAP.setPerimcefal(((Double) vRowTemp.elementAt(10)).intValue());
                oAP.setApgar((String) vRowTemp.elementAt(11));
                oAP.getDiag().setDescrip((String) vRowTemp.elementAt(12));
                oHC.setPerin(true);
            } else {
                oHC.setPerin(false);
            }
            oHC.setAntecedPerinatales(oAP);
            sQuery = "select * from buscallaveantecedentesnutricio(" + oHC.getPaciente().getFolioPac() + ");";
            rst = this.getAD().ejecutarConsulta(sQuery);
            if (rst != null && rst.size() == 1) {
                Vector vRowTemp = (Vector) rst.elementAt(0);
                oAN.setAblactacion(((Double) vRowTemp.elementAt(1)).intValue());
                oAN.setDuracionLact(((Double) vRowTemp.elementAt(2)).intValue());
                oAN.setBiberonActual((String) vRowTemp.elementAt(3));
                oAN.setFormActual((String) vRowTemp.elementAt(4));
                oAN.setLactanciaMaterna((String) vRowTemp.elementAt(5));
                oAN.setNomFormula((String) vRowTemp.elementAt(6));
                oHC.setNutri(true);
            } else {
                oHC.setNutri(false);
            }
            oHC.setAntecedNutricio(oAN);
            sQuery = "select * from buscallavedesarrollo(" + oHC.getPaciente().getFolioPac() + ");";
            rst = this.getAD().ejecutarConsulta(sQuery);
            if (rst != null && rst.size() == 1) {
                Vector vRowTemp = (Vector) rst.elementAt(0);
                oD.setControlCefalico(((Double) vRowTemp.elementAt(1)).intValue());
                oD.setCtrlEsfinteres(((Double) vRowTemp.elementAt(2)).intValue());
                oD.setDisilabos(((Double) vRowTemp.elementAt(3)).intValue());
                oD.setGateo(((Double) vRowTemp.elementAt(4)).intValue());
                oD.setMarcha(((Double) vRowTemp.elementAt(5)).intValue());
                oD.setPrimerDiente(((Double) vRowTemp.elementAt(6)).intValue());
                oD.setSentarse(((Double) vRowTemp.elementAt(7)).intValue());
                oD.setComportamiento((String) vRowTemp.elementAt(8));
                oD.setEscolaridadRend((String) vRowTemp.elementAt(9));
                oHC.setDes(true);
            } else {
                oHC.setDes(false);
            }
            oHC.setDesarrollo(oD);
            sQuery = "select * from buscaImpresionDiag(" + oHC.getPaciente().getFolioPac() + ");";
            rst = this.getAD().ejecutarConsulta(sQuery);
            if (rst != null && rst.size() == 1) {
                Vector vRowTemp = (Vector) rst.elementAt(0);
                oDiag.setCve((String) vRowTemp.elementAt(0));
                oSP.setReceta((String) vRowTemp.elementAt(1));
                oSP.setObs((String) vRowTemp.elementAt(2));
                oEM.setAlta((Date) vRowTemp.elementAt(3));
                if (!oDiag.getCve().equals(""))
                    oDiag = oDiag.buscaDiagnostico(oDiag.getCve());
            }
            sQuery = "select * from buscaDatosLaborales(" + oHC.getPaciente().getFolioPac() + ");";
            rst = this.getAD().ejecutarConsulta(sQuery);
            if (rst != null && rst.size() == 1) {
                Vector vRowTemp = (Vector) rst.elementAt(0);
                oHC.getPaciente().getDatosLaborales().getEmpresa().setIdEmp(((Double) vRowTemp.elementAt(0)).intValue());
                oHC.getPaciente().getDatosLaborales().setOtraEmpresa((String) vRowTemp.elementAt(1));
                oHC.getPaciente().getDatosLaborales().setSucursal((String) vRowTemp.elementAt(2));
                oHC.getPaciente().getDatosLaborales().setNumEmpleado((String) vRowTemp.elementAt(3));
                oHC.getPaciente().getDatosLaborales().setOcupacion((String) vRowTemp.elementAt(4));
                oHC.getPaciente().getDatosLaborales().setJefeInmediato((String) vRowTemp.elementAt(5));
                oHC.getPaciente().getDatosLaborales().setNomEmpleado((String) vRowTemp.elementAt(6));
            }
            oHC.setEpisodioMedico(oEM);
            oHC.getEpisodioMedico().setServicioPrestado(oSP);
            oHC.getEpisodioMedico().setDxIngreso(oDiag);
            EpisodioCiaCred oEpCC = new EpisodioCiaCred();
            oEpCC.setEpisodioM(oHC.getEpisodioMedico());
            oEpCC.setPaciente(oHC.getPaciente());
            oHC.setEpisodioCC(oEpCC);

            sQuery = "select * from buscaDocumentos(" + oHC.getEpisodioCC().getPaciente().getFolioPac() + ", " + oHC.getEpisodioCC().getEpisodioM().getCveepisodio() + ");";
            rst = this.getAD().ejecutarConsulta(sQuery);
            this.getAD().desconectar();
            setAD(null);
            if (rst != null && rst.size() > 0) {
                Vector vRowTemp = (Vector) rst.elementAt(0);
                oHC.getEpisodioCC().getCompCred().setIdEmp(((Double) vRowTemp.elementAt(0)).intValue());
                oHC.getEpisodioCC().getCompCred().setNombreCorto((String) vRowTemp.elementAt(1));
                for (int i = 0; i < rst.size(); i++) {
                    vRowTemp = (Vector) rst.elementAt(i);
                    FormatoCiaCred fmt = new FormatoCiaCred();
                    fmt.setIdFmt(((Double) vRowTemp.elementAt(2)).intValue());
                    fmt.setNomFmt((String) vRowTemp.elementAt(3));
                    oHC.getEpisodioCC().getFormatos().add(fmt);
                }
            }
        }
        return oHC;
    }

    public String guardaDatosGenerales(HistoriaClinica oHC) throws Exception {
    Vector rst = null;
    String sQuery = "", sRet = "";
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd h:mm:ss");
    
        if (oHC == null || oHC.getPaciente().getNombre().equals("") || 
                oHC.getPaciente().getApellidoPaterno().equals("") || 
                oHC.getPaciente().getNac() == null) {
            throw new Exception("Historia Clínica. Guarda Datos Generales: Error de programación. Faltan datos.");
        } else {
            sQuery = "select * from guardaDatosGralesPaciente(" + 
                    oHC.getPaciente().getFolioPac() + ",";
            if (oHC.getReg() == null) {
                sQuery = sQuery +  "null";
            } else {
                sQuery = sQuery + "'" + oHC.getReg() + "'";
            }
            sQuery = sQuery + "::date, '" + 
                    oHC.getPaciente().getNombre().toUpperCase() + "'::character varying, '" + 
                    oHC.getPaciente().getApellidoPaterno().toUpperCase() + "'::character varying, " + 
                    validaString(oHC.getPaciente().getApellidoMaterno()).toUpperCase() + "::character varying, '" + 
                    sdf.format(oHC.getPaciente().getNac()) + "'::timestamp without time zone, " + 
                    validaString(oHC.getPaciente().getLugarNac()) + "::character varying, '" + 
                    oHC.getPaciente().getGenero() + "'::character, " + 
                    validaString(oHC.getPaciente().getRFC()).toUpperCase() + "::character varying, " + 
                    validaString(oHC.getPaciente().getCURP()).toUpperCase() + "::character varying, " + 
                    validaString(oHC.getPaciente().getEdoCiv().getCve()) + "::character, " + 
                    validaString(oHC.getPaciente().getFamiliarCercano()).toUpperCase() + "::character varying, " + 
                    validaString(oHC.getPaciente().getOcupacion()).toUpperCase() + "::character varying, " + 
                    validaString(oHC.getPaciente().getSeEntero()).toUpperCase() + "::character varying, " + 
                    validaString(oHC.getRecomendadoPor()).toUpperCase() + "::character varying,  " + 
                    convierteMed(oHC.getMedRecomienda().getFolioPers()) + ", '" + 
                    oHC.getPaciente().getTipo() + "'::char(4), " + 
                    validaString(oHC.getPaciente().getCalleYNum()).toUpperCase() + "::character varying, " + 
                    validaString(oHC.getPaciente().getColonia()).toUpperCase() + "::character varying, " + 
                    validaString(oHC.getPaciente().getCiudadPoblacion()).toUpperCase() + "::character varying," + 
                    validaString(oHC.getPaciente().getCP()) + "::character(5), " + 
                    validaString(oHC.getPaciente().getEdo().getCve()) + "::character, " + 
                    validaString(oHC.getPaciente().getMpio().getCve()) + "::character, " + 
                    validaString(oHC.getPaciente().getCiudad().getCve()) + "::character, " + 
                    validaString(oHC.getPaciente().getTelCasa()) + "::character varying, " + 
                    validaString(oHC.getPaciente().getTelCelular()) + "::character varying, " + 
                    validaString(oHC.getAlergias()).toUpperCase() + "::character varying, " + 
                    validaString(oHC.getMotivoConsEnfAct()).toUpperCase() + "::character varying, " + 
                    oHC.getExamenFisico().getFrecCard() + "::int2" + ", " + 
                    oHC.getExamenFisico().getFrecResp() + "::int2" + ", " + 
                    oHC.getExamenFisico().getPeso() + ", " + 
                    oHC.getExamenFisico().getTAA() + "::int2" + ", " + 
                    oHC.getExamenFisico().getTAB() + "::int2" + ", " + 
                    oHC.getExamenFisico().getTalla() + ", " + 
                    oHC.getExamenFisico().getTemp() + ", " + 
                    convierteMed(oHC.getEpisodioMedico().getMedRecibe().getFolioPers()) + 
                    "," + convierteMed(oHC.getEpisodioMedico().getMedTratante().getFolioPers()) + 
                    ", '0', "+ (new Valida()).cadenaParaBase(this.getPaciente().getCorreoElectronico())+
                    ", " + (new Valida().cadenaParaBase(oHC.getEpisodioMedico().getDomicilioFamiliar())) +
                    ", " + (new Valida().cadenaParaBase(oHC.getEpisodioMedico().getTelefonoFamiliar()))+
                    ", " + (new Valida().cadenaParaBase(oHC.getEpisodioMedico().getEmpresaFamiliar())) +
                    ", " + (new Valida().cadenaParaBase(oHC.getEpisodioMedico().getDomicilioEmpresaFamiliar()))+
                    ", " + (new Valida().cadenaParaBase(oHC.getEpisodioMedico().getTelefonoEmpresaFamiliar()))+
                    ");";
            if (oHC.getEpisodioCC().getCompCred().getIdEmp() > 0) {
                sQuery = sQuery + "\nselect * from insertaepisodiociacred(" + oHC.getEpisodioCC().getPaciente().getFolioPac() + ", "
                        + oHC.getEpisodioCC().getEpisodioM().getCveepisodio() + ", " + oHC.getEpisodioCC().getCompCred().getIdEmp() + "::int2, " + oHC.getEpisodioMedico().getMedTratante().getFolioPers() + ");";
                for (int i = 0; i < oHC.getEpisodioCC().getFormatos().size(); i++) {
                    sQuery = sQuery + "\nselect * from insertaepisodiociacreddocum(" + oHC.getEpisodioCC().getPaciente().getFolioPac() + ", "
                            + oHC.getEpisodioCC().getEpisodioM().getCveepisodio() + ", " + oHC.getEpisodioCC().getCompCred().getIdEmp() + "::int2, "
                            + oHC.getEpisodioCC().getFormatos().get(i).getIdFmt() + ");";
                }
            } else {
                if (oHC.getPaciente().getFolioPac() > 0) {
                    sQuery = sQuery + "\nselect * from limpiaepisodiociacred(" + oHC.getEpisodioCC().getPaciente().getFolioPac() + ", "
                            + oHC.getEpisodioCC().getEpisodioM().getCveepisodio() + ", " + oHC.getEpisodioCC().getCompCred().getIdEmp() + "::int2);";
                }
            }
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
            if ("Multiple ResultSets were returned by the query".equals(rst.get(0).toString().substring(1, rst.get(0).toString().length() - 1))) {
                sRet = "Se han guardado los Datos Generales del paciente";
            } else if ("Se han almacenado los datos generales del paciente exitosamente".equals(rst.get(0).toString().substring(1, rst.get(0).toString().length() - 1))) {
                sRet = "Se han guardado los Datos Generales del paciente";
            } else {
                sRet = "Se produjo un error mientras se almacenaban los datos. Vuelva a intentarlo";
            }
        }
        return sRet;
    }

    public boolean convierteBool(String var) {
        boolean ret = false;
        if (var.equals("1")) {
            ret = true;
        }
        return ret;
    }

    public Object convierteMed(int val) {
        if (val == 0) {
            return null;
        } else {
            return val;
        }
    }

    public String validaString(String val) {
        if (val == null|| val.trim().equals("")) {
            return "null";
        } else {
            return "'"+val+"'";
        }
    }
}