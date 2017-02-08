package org.apli.modelbeans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import org.apli.AD.AccesoDatos;

/**
 * Todo aquel usuario beneficiario directo de la atención médica (NORMA Oficial
 * Mexicana NOM-004-SSA3-2012, Del expediente clínico)
 *
 * @author BAOZ
 * @version 1.0
 * @created 07-abr-2014 01:12:15 p.m.
 */
public class Paciente extends Persona implements Serializable {

    /**
     * Folio (identificador) del paciente
     */
    private int nFolioPac;
    /**
     * Calle y n�mero (exterior e interior) del domicilio del paciente
     */
    private String sCalleYNum;
    /**
     * Ciudad o poblaci�n del domicilio del paciente (cuando no la encuentra por
     * CP)
     */
    private String sCiudadPoblacion;
    /**
     * Colonia del domicilio del paciente
     */
    private String sColonia;
    /**
     * CP del domicilio del paciente
     */
    private String sCP;
    private Municipio oMpio=new Municipio();
    private Estado oEdo = new Estado();
    private Ciudad oCiudad=new Ciudad();
    /**
     * Nombre del familiar responsable
     */
    private String sFamiliarCercano;
    /**
     * Lugar de nacimiento
     */
    private String sLugarNac;
    /**
     * Ocupaci�n principal del paciente
     */
    private String sOcupacion;
    /**
     * Tel�fono de casa
     */
    private String sTelCasa;
    /**
     * N�mero de tel�fono celular
     */
    private String sTelCelular;
    /**
     * Tipo de paciente PSH = paciente del Sanatorio Huerta PNSH = paciente NO
     * del Sanatorio Huerta
     */
    private String sTipo;
    public HistoriaClinica m_HistoriaClinica;
    public AreaDeServicio m_AreaDeServicio;
    private String sSeEntero;
    private Medico oMedRecomienda;
    private boolean bFallecido;
    private String sCorreoElectronico;
    private AccesoDatos oAD;
    
    public final static String TIPO_PSH="PSH";
    public final static String TIPO_PNSH = "PNSH";
    public final static String DESC_PARCIAL_VARIOS = "VARIOS";

    public Paciente(AccesoDatos poAD) {
        oAD = poAD;
    }

    public Paciente() {
        this.nFolioPac = 0;
        this.sNombre = "";
        this.sApellidoPaterno = "";
        this.sApellidoMaterno = "";
        this.sRFC = "";
        this.sCURP = "";
        this.sGenero = ' ';
    }

    public Paciente(int nFolioPaciente, Date dNacimiento, String srfc, String scurp, String snombre, String sApPaterno, String sApMaterno, char sGenero) {
        this.nFolioPac = nFolioPaciente;
        this.dNac = dNacimiento;
        this.sRFC = srfc;
        this.sCURP = scurp;
        this.sNombre = snombre;
        this.sApellidoPaterno = sApPaterno;
        this.sApellidoMaterno = sApMaterno;
        this.sGenero = sGenero;
    }


    public int buscaPacientePorServiPrest(String sFolioServPre) throws Exception {
        Vector rst = null;
        String sQuery = "";
        if (sFolioServPre.equals("")) {
            throw new Exception("Paciente.buscaPacientePorServiPrest: error de programación, faltan datos");
        } else {
            sQuery = "SELECT * FROM buscapacienteporservicioprestado('" + sFolioServPre + "');";
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
                nFolioPac = Integer.parseInt(new Valida().eliminaMSJCorchetes("" + rst.get(0)));
            }
            return nFolioPac;
        }
    }

    public PaqueteContratado buscaPacienteConPaqueteContratado() throws Exception {
        Vector rst = null;
        String sQuery = "";
        PaqueteContratado oPaqContra = null;
        Paquete oPaquete = null;
        if (this.nFolioPac == 0) {
            throw new Exception("Paciente.buscaPacienteConPaqueteContra: error de programación, faltan datos");
        } else {
            sQuery = "SELECT * FROM espacientedepaquete(" + nFolioPac + ");";
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
                oPaquete = new Paquete();
                oPaqContra = new PaqueteContratado();
                Vector vRowTemp = (Vector) rst.elementAt(0);
                oPaqContra.setIdpaqcont(((Double) vRowTemp.elementAt(0)).intValue());
                oPaquete.setIdPaquete(((Double) vRowTemp.elementAt(1)).intValue());
                oPaquete.setNombre(((String) vRowTemp.elementAt(2)));
                oPaquete.setCosto(((Double) vRowTemp.elementAt(3)).floatValue());
                TipoPaquete oTP = new TipoPaquete();
                oTP.setCve((String) vRowTemp.elementAt(4));
                if (((String) vRowTemp.elementAt(4)).equals("0")) {
                    oTP.setDescrip("Pediátrico");
                }
                if (((String) vRowTemp.elementAt(4)).equals("1")) {
                    oTP.setDescrip("Maternidad");
                }
                if (((String) vRowTemp.elementAt(4)).equals("2")) {
                    oTP.setDescrip("Quirúrgico");
                }
                if (((String) vRowTemp.elementAt(4)).equals("3")) {
                    oTP.setDescrip("Personalizado");
                }
                oPaquete.setTipoPaquete(oTP);
                oPaqContra.setSituacionPago(((String) vRowTemp.elementAt(5)));
                oPaqContra.setRegistro(((Date) vRowTemp.elementAt(6)));
                EpisodioMedico oEM = new EpisodioMedico();
                oEM.setCveepisodio(((Double) vRowTemp.elementAt(7)).intValue());
                Date dProbParto = ((Date) vRowTemp.elementAt(8));
                if (dProbParto != null) {
                    oPaqContra.setProbableparto(dProbParto);
                }
                oPaqContra.setEpisodioMedico(oEM);
                oPaqContra.setPaquete(oPaquete);
            }
            return oPaqContra;
        }
    }

    public PaqueteContratado buscaPacienteConPaqueteContratadoHeredado() throws Exception {
        Vector rst = null;
        String sQuery = "";
        Paquete oPaquete = null;
        PaqueteContratado oPaqContra = null;
        if (this.nFolioPac == 0) {
            throw new Exception("Paciente.buscaPacienteConPaqueteContratadoHeredado: error de programación, faltan datos");
        } else {
            sQuery = "SELECT * FROM espacientedepaqueteheredado(" + nFolioPac + ");";
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
                oPaquete = new Paquete();
                oPaqContra = new PaqueteContratado();
                Vector vRowTemp = (Vector) rst.elementAt(0);
                oPaqContra.setIdpaqcont(((Double) vRowTemp.elementAt(0)).intValue());
                oPaquete.setIdPaquete(((Double) vRowTemp.elementAt(1)).intValue());
                oPaquete.setNombre(((String) vRowTemp.elementAt(2)));
                oPaquete.setCosto(((Double) vRowTemp.elementAt(3)).floatValue());
                TipoPaquete oTP = new TipoPaquete();
                oTP.setCve((String) vRowTemp.elementAt(4));
                if (((String) vRowTemp.elementAt(4)).equals("0")) {
                    oTP.setDescrip("Pediátrico");
                }
                if (((String) vRowTemp.elementAt(4)).equals("1")) {
                    oTP.setDescrip("Maternidad");
                }
                if (((String) vRowTemp.elementAt(4)).equals("2")) {
                    oTP.setDescrip("Quirúrgico");
                }
                if (((String) vRowTemp.elementAt(4)).equals("3")) {
                    oTP.setDescrip("Personalizado");
                }
                oPaquete.setTipoPaquete(oTP);
                oPaqContra.setSituacionPago(((String) vRowTemp.elementAt(5)));
                oPaqContra.setRegistro(((Date) vRowTemp.elementAt(6)));
                EpisodioMedico oEM = new EpisodioMedico();
                oEM.setCveepisodio(((Double) vRowTemp.elementAt(7)).intValue());
                Date dProbParto = ((Date) vRowTemp.elementAt(8));
                if (dProbParto != null) {
                    oPaqContra.setProbableparto(dProbParto);
                }
                oPaqContra.setEpisodioMedico(oEM);
                oPaqContra.setPaquete(oPaquete);
            }
            return oPaqContra;
        }
    }

    public Paciente[] buscarTodosPacientesPorFiltro()throws Exception{
            Paciente[] arrRet = null;
            Paciente oPac=null;
            Vector rst = null;
            Vector<Paciente> vObj = null;
            String sQuery = "";
            String query2="WHERE ";
            int i=0, nTam = 0;
            sQuery =" SELECT * FROM buscarTodosPacientePorFiltro()";
            if(this.nFolioPac!=0){
                query2+= "poutnfoliopaciente="+this.nFolioPac+" AND ";
            }
            if(!this.sRFC.equals("")){
                query2+= "poutsrfc LIKE '%"+this.sRFC.toUpperCase()+"%' AND ";
            }
            if(!this.sCURP.equals("")){
                query2+= "poutscurp LIKE '%"+this.sCURP.toUpperCase()+"%' AND ";
            }
            if(!this.sNombre.equals("")){
                query2+= "poutsnombre LIKE '%"+this.sNombre.toUpperCase()+"%' AND ";
            }
            if(!this.sApellidoPaterno.equals("")){
                query2+= "poutsappaterno='"+this.sApellidoPaterno.toUpperCase()+"' AND ";
            }
            if(!this.sApellidoMaterno.equals("")){
                query2+= "poutsapmaterno='"+this.sApellidoMaterno.toUpperCase()+"' AND ";
            }
            query2= query2.substring(0, query2.length() -4);
            sQuery+=query2;
            sQuery = sQuery + "ORDER BY 5,6,4";
            System.out.println(sQuery);
            /*Si oAD es nulo, tiene que crearlo y conectarlo, de otro modo
            supone que ya viene conectado*/
            if (getAD() == null){
                setAD(new AccesoDatos());
                getAD().conectar();
                rst = getAD().ejecutarConsulta(sQuery);
                getAD().desconectar();
                setAD(null);
            }
            else{
                rst = getAD().ejecutarConsulta(sQuery);
            }
            if (rst != null) {
                vObj = new Vector<Paciente>();
                for (i = 0; i < rst.size(); i++) {
                    oPac = new Paciente();
                    Vector vRowTemp = (Vector)rst.elementAt(i);
                    /* nfoliopaciente(0), dnacimiento(1), scveedociv(2), snombre(3), sappaterno(4), sapmaterno(5), 
                       srfc(6), scurp(7), sgenero(8), scalleynumero(9), snombreotraciudad(10), sfamiliarcercano(11),
                       slugarnacimiento(12), socupacion(13), stelcasa(14), stelcelular(15), stipopac(16), scolonia(17), 
                       scomoseentero(18), nmedrecomienda(19), scveedo(20), scvemun(21), snumcd(22), scp(23) */
                    oPac.setFolioPac(((Double) vRowTemp.elementAt(0)).intValue());
                    oPac.setNac( (Date) vRowTemp.elementAt(1));
                        EstadoCivil tmpEdoCiv= new EstadoCivil();
                        tmpEdoCiv.setCve((String) vRowTemp.elementAt(2));
                    oPac.setEdoCiv(tmpEdoCiv);
                    oPac.setNombre((String) vRowTemp.elementAt(3));
                    oPac.setApellidoPaterno((String) vRowTemp.elementAt(4));
                    oPac.setApellidoMaterno((String) vRowTemp.elementAt(5));
                    
                    oPac.setRFC((String) vRowTemp.elementAt(6));
                    oPac.setCURP((String) vRowTemp.elementAt(7));
                    oPac.setGenero(((String) vRowTemp.elementAt(8)).charAt(0));
                    oPac.setCalleYNum((String) vRowTemp.elementAt(9));
                    oPac.setCiudadPoblacion((String) vRowTemp.elementAt(10));
                    oPac.setFamiliarCercano((String) vRowTemp.elementAt(11));
                    
                    oPac.setLugarNac((String) vRowTemp.elementAt(12));
                    oPac.setOcupacion((String) vRowTemp.elementAt(13));
                    oPac.setTelCasa((String) vRowTemp.elementAt(14));
                    oPac.setTelCelular((String) vRowTemp.elementAt(15));
                    oPac.setTipo(((String) vRowTemp.elementAt(16)));
                    oPac.setColonia((String) vRowTemp.elementAt(17));
                    
                    oPac.setSeEntero((String) vRowTemp.elementAt(18));
                        Medico tmpMed= new Medico();
                        tmpMed.setFolioPers(((Double) vRowTemp.elementAt(19)).intValue());
                    oPac.setMedRecomienda(tmpMed);
                        Estado tmpEdo= new Estado();
                        tmpEdo.setCve((String) vRowTemp.elementAt(20));
                    oPac.setEdo(tmpEdo);
                        Municipio tmpMpio= new Municipio();
                        tmpMpio.setCve((String) vRowTemp.elementAt(21));
                    oPac.setMpio(tmpMpio);
                        Ciudad tmpCd= new Ciudad();
                        tmpCd.setCve((String) vRowTemp.elementAt(22));
                    oPac.setCiudad(tmpCd);
                    oPac.setCP((String) vRowTemp.elementAt(23));
                    
                    oPac.setEdad(calculaEdad(oPac.getNac()));
                    oPac.setFallecido(((String)vRowTemp.elementAt(24)).equals("1"));
                       
                    vObj.add(oPac);
                }
                nTam = vObj.size();
                arrRet = new Paciente[nTam];

                for (i=0; i<nTam; i++){
                    arrRet[i] = vObj.elementAt(i);
                }
            }
            return arrRet;
        }
        
    

    public Paciente buscaPaciente(int pnFolioPac) throws Exception {
        Paciente oPac = new Paciente();
        Vector rst = null;
        String sQuery = "";

        if (pnFolioPac == 0) {
            throw new Exception("Paciente.buscaPaciente: error de programación, faltan datos");
        } else {
            sQuery = "select * from buscaLlavePaciente(" + pnFolioPac + ");";
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
                oPac.setFolioPac(((Double) vRowTemp.elementAt(0)).intValue());
                oPac.setNac((Date) vRowTemp.elementAt(1));
                EstadoCivil oEC = new EstadoCivil();
                oPac.setEdoCiv(oEC);
                oPac.getEdoCiv().setCve((String) vRowTemp.elementAt(2));
                oPac.setNombre((String) vRowTemp.elementAt(3));
                oPac.setApellidoPaterno((String) vRowTemp.elementAt(4));
                oPac.setApellidoMaterno((String) vRowTemp.elementAt(5));
                oPac.setRFC((String) vRowTemp.elementAt(6));
                oPac.setCURP((String) vRowTemp.elementAt(7));
                oPac.setGenero(((String) vRowTemp.elementAt(8)).charAt(0));
                oPac.setCalleYNum((String) vRowTemp.elementAt(9));
                oPac.setCiudadPoblacion((String) vRowTemp.elementAt(10));
                oPac.setFamiliarCercano((String) vRowTemp.elementAt(11));
                oPac.setLugarNac((String) vRowTemp.elementAt(12));
                oPac.setOcupacion((String) vRowTemp.elementAt(13));
                oPac.setTelCasa((String) vRowTemp.elementAt(14));
                oPac.setTelCelular((String) vRowTemp.elementAt(15));
                oPac.setTipo((String) vRowTemp.elementAt(16));
                //oPac.setTelCasa((String) vRowTemp.elementAt(17));
                oPac.setCP((String) vRowTemp.elementAt(18));
                oPac.setColonia((String) vRowTemp.elementAt(19));
                oPac.setSeEntero((String) vRowTemp.elementAt(20));
            }
        }
        return oPac;
    }

    public String[] buscaCiudad(String psCP) throws Exception {
        String[] arrRet = new String[6];
        Vector rst = null;
        String sQuery = "";

        if (psCP.equals("")) {
            throw new Exception("Paciente.buscaCiudad: error de programación, faltan datos");
        } else {
            sQuery = "select * from buscacdporcp('" + psCP + "')";
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
                arrRet[0] = ((String) vRowTemp.elementAt(0));
                arrRet[1] = ((String) vRowTemp.elementAt(1));
                arrRet[2] = ((String) vRowTemp.elementAt(2));
                arrRet[3] = ((String) vRowTemp.elementAt(3));
                arrRet[4] = ((String) vRowTemp.elementAt(4));
                arrRet[5] = ((String) vRowTemp.elementAt(5));
            }
        }
        return arrRet;
    }

    public Paciente[] buscarPacientesCuentaAbierta() throws Exception {
        Paciente[] arrRet = null;
        Paciente oPac = null;
        Vector rst = null;
        Vector<Paciente> vObj = null;
        String sQuery = "";
        int i = 0, nTam = 0;
        sQuery = " select * from buscarTodosPacienteCuentaAbierta() ";
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
            vObj = new Vector<Paciente>();
            for (i = 0; i < rst.size(); i++) {
                oPac = new Paciente();
                Vector vRowTemp = (Vector) rst.elementAt(i);
                /* nfoliopaciente(0), snombre(1), sappaterno(2), sapmaterno(3), srfc(4), stipopac(5) */
                oPac.setFolioPac(((Double) vRowTemp.elementAt(0)).intValue());
                oPac.setNombre((String) vRowTemp.elementAt(1));
                oPac.setApellidoPaterno((String) vRowTemp.elementAt(2));
                oPac.setApellidoMaterno((String) vRowTemp.elementAt(3));
                oPac.setRFC((String) vRowTemp.elementAt(4));
                oPac.setTipo((String) vRowTemp.elementAt(5));
                vObj.add(oPac);
            }
            nTam = vObj.size();
            arrRet = new Paciente[nTam];
            for (i = 0; i < nTam; i++) {
                arrRet[i] = vObj.elementAt(i);
            }
        }
        return arrRet;
    }

    public Paciente buscaPaciente() throws Exception {
        Paciente oPac = new Paciente();
        Vector rst = null;
        String sQuery = "";

        if (this.nFolioPac == 0) {
            throw new Exception("Paciente.buscaPaciente: error de programación, faltan datos");
        } else {
            sQuery = "select * from buscapacienteporfolio(" + this.nFolioPac + ");";
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
                /*  p.nFolioPaciente(0), p.dNacimiento(1), p.sCveEdoCiv(2), p.sNombre(3), p.sApPaterno(4), p.sApMaterno(5), p.sRFC(6),p.sCURP(7),
                 p.sGenero(8), p.sCalleYNumero(9), p.sNombreOtraCiudad(10), p.sFamiliarCercano(11), p.sLugarNacimiento(12), p.sOcupacion(13),
                 p.sTelCasa(14), p.sTelCelular(15), p.sTipoPac(16), p.sCP(17),
                 c.snumcd(18), c.sdescripcion(19),
                 m.scvemun(20), m.sdescripcion(21),
                 e.scveedo(22), e.sdescripcion(23)*/
                oPac.setFolioPac(((Double) vRowTemp.elementAt(0)).intValue());
                oPac.setNac((Date) vRowTemp.elementAt(1));
                this.oEdoCiv = new EstadoCivil();
                oPac.setEdoCiv(this.oEdoCiv);
                oPac.getEdoCiv().setCve((String) vRowTemp.elementAt(2));
                oPac.setNombre((String) vRowTemp.elementAt(3));
                oPac.setApellidoPaterno((String) vRowTemp.elementAt(4));
                oPac.setApellidoMaterno((String) vRowTemp.elementAt(5));
                oPac.setRFC((String) vRowTemp.elementAt(6));
                oPac.setCURP((String) vRowTemp.elementAt(7));
                oPac.setGenero(((String) vRowTemp.elementAt(8)).charAt(0));
                oPac.setCalleYNum((String) vRowTemp.elementAt(9));
                oPac.setCiudadPoblacion((String) vRowTemp.elementAt(10));
                oPac.setFamiliarCercano((String) vRowTemp.elementAt(11));
                oPac.setLugarNac((String) vRowTemp.elementAt(12));
                oPac.setOcupacion((String) vRowTemp.elementAt(13));
                oPac.setTelCasa((String) vRowTemp.elementAt(14));
                oPac.setTelCelular((String) vRowTemp.elementAt(15));
                oPac.setTipo((String) vRowTemp.elementAt(16));
                oPac.setCP((String) vRowTemp.elementAt(17));
                this.oCiudad = new Ciudad();
                oPac.setCiudad(this.oCiudad);
                oPac.getCiudad().setCve((String) vRowTemp.elementAt(18));
                oPac.getCiudad().setDescrip((String) vRowTemp.elementAt(19));
                this.oMpio = new Municipio();
                oPac.setMpio(this.oMpio);
                oPac.getMpio().setCve((String) vRowTemp.elementAt(20));
                oPac.getMpio().setDescrip((String) vRowTemp.elementAt(21));
                this.oEdo = new Estado();
                oPac.setEdo(this.oEdo);
                oPac.getEdo().setCve((String) vRowTemp.elementAt(22));
                oPac.getEdo().setDescrip((String) vRowTemp.elementAt(23));

                oPac.setColonia((String) vRowTemp.elementAt(24));
                oPac.setEdad(calculaEdad(oPac.getNac()));
            }
        }
        return oPac;
    }

    public Paciente buscaDatosBasicosPacientePorFolio() throws Exception {
        Paciente oPac = new Paciente();
        Vector rst = null;
        String sQuery = "";

        if (this.nFolioPac == 0) {
            throw new Exception("Paciente.buscaPaciente: error de programación, faltan datos");
        } else {
            sQuery = "select * from buscadatosbasicospacienteporfolio(" + this.nFolioPac + ");";
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
                /*  p.nFolioPaciente(0), p.dNacimiento(1), p.sCveEdoCiv(2), p.sNombre(3), p.sApPaterno(4), p.sApMaterno(5), p.sRFC(6),p.sCURP(7),
                 p.sGenero(8), p.sCalleYNumero(9), p.sNombreOtraCiudad(10), p.sFamiliarCercano(11), p.sLugarNacimiento(12), p.sOcupacion(13),
                 p.sTelCasa(14), p.sTelCelular(15), p.sTipoPac(16), p.sCP(17)*/
                oPac.setFolioPac(((Double) vRowTemp.elementAt(0)).intValue());
                oPac.setNac((Date) vRowTemp.elementAt(1));
                this.oEdoCiv = new EstadoCivil();
                oPac.setEdoCiv(this.oEdoCiv);
                oPac.getEdoCiv().setCve((String) vRowTemp.elementAt(2));
                oPac.setNombre((String) vRowTemp.elementAt(3));
                oPac.setApellidoPaterno((String) vRowTemp.elementAt(4));
                oPac.setApellidoMaterno((String) vRowTemp.elementAt(5));
                oPac.setRFC((String) vRowTemp.elementAt(6));
                oPac.setCURP((String) vRowTemp.elementAt(7));
                oPac.setGenero(((String) vRowTemp.elementAt(8)).charAt(0));
                oPac.setCalleYNum((String) vRowTemp.elementAt(9));
                oPac.setCiudadPoblacion((String) vRowTemp.elementAt(10));
                oPac.setFamiliarCercano((String) vRowTemp.elementAt(11));
                oPac.setLugarNac((String) vRowTemp.elementAt(12));
                oPac.setOcupacion((String) vRowTemp.elementAt(13));
                oPac.setTelCasa((String) vRowTemp.elementAt(14));
                oPac.setTelCelular((String) vRowTemp.elementAt(15));
                oPac.setTipo((String) vRowTemp.elementAt(16));
                oPac.setCP((String) vRowTemp.elementAt(17));
                oPac.setCorreoElectronico((String)vRowTemp.elementAt(18));
                oPac.setEdad(calculaEdad(oPac.getNac()));
            }
        }
        return oPac;
    }

    public Paciente buscaUltimoPacienteReg() throws Exception {
        Paciente oPac = new Paciente();
        Vector rst = null;
        String sQuery = "";
        Boolean bBol = true;

        sQuery = "select * from buscaultimopacientereg();";
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
            oPac.setFolioPac(((Double) vRowTemp.elementAt(0)).intValue());
        }
        return oPac;
    }

    public Paciente[] buscarPacientesRPTPagoHonorarios()throws Exception{
            Paciente[] arrRet = null;
            Paciente oPac=null;
            Vector rst = null;
            Vector<Paciente> vObj = null;
            String sQuery = "";
            int i=0, nTam = 0;
            sQuery =" SELECT DISTINCT outfoliopac, outnombrepac, outappaternopac, outapmaternopac FROM buscaTodosHonorariosRPTPagoHonorarios() ";
            System.out.println(sQuery);
            /*Si oAD es nulo, tiene que crearlo y conectarlo, de otro modo
            supone que ya viene conectado*/
            if (getAD() == null){
                setAD(new AccesoDatos());
                getAD().conectar();
                rst = getAD().ejecutarConsulta(sQuery);
                getAD().desconectar();
                setAD(null);
            }
            else{
                rst = getAD().ejecutarConsulta(sQuery);
            }
            if (rst != null) {
                vObj = new Vector<Paciente>();
                for (i = 0; i < rst.size(); i++) {
                    oPac = new Paciente();
                    Vector vRowTemp = (Vector)rst.elementAt(i);
                    /* nfoliopaciente(0), snombre(1), sappaterno(2), sapmaterno(3) */
                    oPac.setFolioPac(((Double) vRowTemp.elementAt(0)).intValue());
                    oPac.setNombre((String) vRowTemp.elementAt(1));
                    oPac.setApellidoPaterno((String) vRowTemp.elementAt(2));
                    oPac.setApellidoMaterno((String) vRowTemp.elementAt(3));
                    vObj.add(oPac);
                }
                nTam = vObj.size();
                arrRet = new Paciente[nTam];
                for (i=0; i<nTam; i++){
                    arrRet[i] = vObj.elementAt(i);
                }
            }
            return arrRet;
        }
        
    public Paciente[] buscarPacientesRPTCuentaPagoHonorarios()throws Exception{
        Paciente[] arrRet = null;
        Paciente oPac=null;
        Vector rst = null;
        Vector<Paciente> vObj = null;
        String sQuery = "";
        int i=0, nTam = 0;
        sQuery =" SELECT DISTINCT outfoliopac, outnombrepac, outappaternopac, outapmaternopac FROM buscaTodosHonorariosRPTPagoHonorarios() WHERE outsituacion='2' ";
        System.out.println(sQuery);
        /*Si oAD es nulo, tiene que crearlo y conectarlo, de otro modo
        supone que ya viene conectado*/
        if (getAD() == null){
            setAD(new AccesoDatos());
            getAD().conectar();
            rst = getAD().ejecutarConsulta(sQuery);
            getAD().desconectar();
            setAD(null);
        }
        else{
            rst = getAD().ejecutarConsulta(sQuery);
        }
        if (rst != null) {
            vObj = new Vector<Paciente>();
            for (i = 0; i < rst.size(); i++) {
                oPac = new Paciente();
                Vector vRowTemp = (Vector)rst.elementAt(i);
                /* nfoliopaciente(0), snombre(1), sappaterno(2), sapmaterno(3) */
                oPac.setFolioPac(((Double) vRowTemp.elementAt(0)).intValue());
                oPac.setNombre((String) vRowTemp.elementAt(1));
                oPac.setApellidoPaterno((String) vRowTemp.elementAt(2));
                oPac.setApellidoMaterno((String) vRowTemp.elementAt(3));
                vObj.add(oPac);
            }
            nTam = vObj.size();
            arrRet = new Paciente[nTam];
            for (i=0; i<nTam; i++){
                arrRet[i] = vObj.elementAt(i);
            }
        }
        return arrRet;
    }
       
    public int getFolioPac() {
        return nFolioPac;
    }

    public void setFolioPac(int nFolioPac) {
        this.nFolioPac = nFolioPac;
    }

    public String getCalleYNum() {
        return sCalleYNum;
    }

    public void setCalleYNum(String sCalleYNum) {
        this.sCalleYNum = sCalleYNum;
    }

    public String getCiudadPoblacion() {
        return sCiudadPoblacion;
    }

    public void setCiudadPoblacion(String sCiudadPoblacion) {
        this.sCiudadPoblacion = sCiudadPoblacion;
    }

    public String getColonia() {
        return sColonia;
    }

    public void setColonia(String sColonia) {
        this.sColonia = sColonia;
    }

    public String getCP() {
        return sCP;
    }

    public void setCP(String sCP) {
        this.sCP = sCP;
    }

    public Municipio getMpio() {
        return oMpio;
    }

    public void setMpio(Municipio oMpio) {
        this.oMpio = oMpio;
    }

    public Estado getEdo() {
        return oEdo;
    }

    public void setEdo(Estado oEdo) {
        this.oEdo = oEdo;
    }

    public Ciudad getCiudad() {
        return oCiudad;
    }

    public void setCiudad(Ciudad oCiudad) {
        this.oCiudad = oCiudad;
    }

    public String getFamiliarCercano() {
        return sFamiliarCercano;
    }

    public void setFamiliarCercano(String sFamiliarCercano) {
        this.sFamiliarCercano = sFamiliarCercano;
    }

    public String getLugarNac() {
        return sLugarNac;
    }

    public void setLugarNac(String sLugarNac) {
        this.sLugarNac = sLugarNac;
    }

    public String getOcupacion() {
        return sOcupacion;
    }

    public void setOcupacion(String sOcupacion) {
        this.sOcupacion = sOcupacion;
    }

    public String getTelCasa() {
        return sTelCasa;
    }

    public void setTelCasa(String sTelCasa) {
        this.sTelCasa = sTelCasa;
    }

    public String getTelCelular() {
        return sTelCelular;
    }

    public void setTelCelular(String sTelCelular) {
        this.sTelCelular = sTelCelular;
    }

    public String getTipo() {
        return sTipo;
    }

    public void setTipo(String sTipo) {
        this.sTipo = sTipo;
    }

    public HistoriaClinica getHistoriaClinica() {
        return m_HistoriaClinica;
    }

    public void setHistoriaClinica(HistoriaClinica m_HistoriaClinica) {
        this.m_HistoriaClinica = m_HistoriaClinica;
    }

    public AreaDeServicio getM_AreaDeServicio() {
        return m_AreaDeServicio;
    }

    public void setM_AreaDeServicio(AreaDeServicio m_AreaDeServicio) {
        this.m_AreaDeServicio = m_AreaDeServicio;
    }

    public void setNombreCompleto(String nombreCompleto) {
    }

    public void setSeEntero(String sSeEntero) {
        this.sSeEntero = sSeEntero;
    }

    public String getSeEntero() {
        return this.sSeEntero;
    }

    public void setAD(AccesoDatos oAD) {
        this.oAD = oAD;
    }

    public AccesoDatos getAD() {
        return oAD;
    }

    public Medico getMedRecomienda() {
        return oMedRecomienda;
    }

    public void setMedRecomienda(Medico oMedRecomienda) {
        this.oMedRecomienda = oMedRecomienda;
    }

    public boolean getFallecido() {
        return bFallecido;
    }

    public void setFallecido(boolean bFallecido) {
        this.bFallecido = bFallecido;
    }
    
    public String getCorreoElectronico(){
        return this.sCorreoElectronico;
    }
    
    public void setCorreoElectronico(String value){
        this.sCorreoElectronico = value;
    }
}