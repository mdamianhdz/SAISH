package org.apli.modelbeans;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import org.apli.AD.AccesoDatos;

/**
 * Médico (del hospital o externo)
 *
 * @author BAOZ
 * @version 1.0
 * @created 07-abr-2014 01:56:00 p.m.
 */
public class Medico extends PersonalHospitalario implements Serializable {

    /**
     * En el caso de m�dicos externos, indica si se tiene un convenio con �l o
     * no
     */
    private boolean bConvenio;
    /**
     * Especialidad m�dica
     */
    private Especialidad oEsp;
    /**
     * N�mero de Registro ante SSA
     */
    private String sRegSSA;
    /**
     * Indica si es un m�dico de staff (SH) o externo (NSH)
     */
    private String sTipo;
    private String sCedEsp;
    private String sHorarioEsp;
    public Paciente m_Paciente;
    private List<Medico> listMed;
    private AccesoDatos oAD;

    public Medico() {
    }

    public Medico(String sNombre, String sApPaterno, String sApMaterno, Especialidad oEsp, String sCedProf, boolean bActivo, Turno turno) {
        this.sNombre = sNombre;
        this.sApellidoPaterno = sApPaterno;
        this.sApellidoMaterno = sApMaterno;
        this.oEsp = oEsp;
        this.sCedProf = sCedProf;
        this.bActivo = bActivo;
        this.oTurno = turno;
    }

    public Medico(String snombre, String sappaterno, String sapmaterno) {
        this.sNombre = snombre;
        this.sApellidoPaterno = sappaterno;
        this.sApellidoMaterno = sapmaterno;
    }

    /**
     * Retorna la lista de m�dicos de staff (tipo = SH)
     *
     * @param nAct Indica si la busqueda es sobre 0 = inactivos 1 = activos 2 =
     * todos
     */
    public List<Medico> buscarMedSH(int nAct) {
        return null;
    }

    public List<Medico> buscaTodos() throws Exception {
        List<Medico> listRet = null;
        Medico oMed;

        Vector rst = null;
        String sQuery = "";

        sQuery = "select * from buscaNombresMedicos();  ";
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
                oMed = new Medico();
                Vector vRowTemp = (Vector) rst.elementAt(i);
                oMed.setFolioPers(((Double) vRowTemp.elementAt(0)).intValue());
                oMed.setNombre((String) vRowTemp.elementAt(1));
                oMed.setApellidoPaterno((String) vRowTemp.elementAt(2));
                oMed.setApellidoMaterno((String) vRowTemp.elementAt(3));
                oMed.setTipo((String) vRowTemp.elementAt(4));
                listRet.add(oMed);
            }
        }
        return listRet;
    }

    public Medico buscaMedicoRegistrado(String cedula) throws Exception {
        Medico oMed = new Medico();

        Vector rst = null;
        String sQuery = "";

        sQuery = "select * from buscamedicoregistrado('" + cedula + "');";
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
                oMed = new Medico();
                Vector vRowTemp = (Vector) rst.elementAt(i);
                oMed.setFolioPers(((Double) vRowTemp.elementAt(0)).intValue());
                oMed.setNombre((String) vRowTemp.elementAt(1));
                oMed.setApellidoPaterno((String) vRowTemp.elementAt(2));
                oMed.setApellidoMaterno((String) vRowTemp.elementAt(3));

            }
        }
        return oMed;
    }

    public List<Medico> buscaTodosMedicoExterno() throws Exception {

        Vector rst = null;
        String sQuery = "";
        List<Medico> listRet = null;
        Medico oMed = new Medico();
        sQuery = "select * from buscatodosmedicoexterno();";
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
                oMed = new Medico();
                Vector vRowTemp = (Vector) rst.elementAt(i);
                oMed.setFolioPers(((Double) vRowTemp.elementAt(0)).intValue());
                oMed.setNombre((String) vRowTemp.elementAt(1));
                oMed.setApellidoPaterno((String) vRowTemp.elementAt(2));
                oMed.setApellidoMaterno((String) vRowTemp.elementAt(3));
                listRet.add(oMed);
            }
        }
        return listRet;
    }

    public Medico buscaMedicoNombre(int id) throws Exception {
        Medico oMed = new Medico();

        Vector rst = null;
        String sQuery = "";

        sQuery = "select * from buscanombremedico(" + id + ");";
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
                oMed = new Medico();
                Vector vRowTemp = (Vector) rst.elementAt(i);
                oMed.setFolioPers(((Double) vRowTemp.elementAt(0)).intValue());
                oMed.setNombre((String) vRowTemp.elementAt(1));
                oMed.setApellidoPaterno((String) vRowTemp.elementAt(2));
                oMed.setApellidoMaterno((String) vRowTemp.elementAt(3));

            }
        }
        return oMed;
    }

    /**
     * Busca la lista de todos los medicos registrados, regresa un arreglo de
     * Medicos
     *
     */
    public Medico[] buscarTodosMedicosAgenda() throws Exception {
        Medico[] arrRet = null;
        Medico oMdco = null;
        Vector rst = null;
        Vector<Medico> vObj = null;
        String sQuery = "";
        int i = 0, nTam = 0;
        sQuery = "select * from buscarTodosMedicosAgenda() ";
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
            vObj = new Vector<Medico>();

            for (i = 0; i < rst.size(); i++) {
                oMdco = new Medico();
                Vector vRowTemp = (Vector) rst.elementAt(i);
                // nfolioPers (0), snombre(1), sappaterno(2), sapmaterno(3), srfc(4), scurp(5), 
                // turno.scveturno(6), turno.shorario(7), scedprof(8), ncveesp(9), sdescripcion(10),
                // scedesp(11), shorarioespecial(12)
                oMdco.setFolioPers(((Double) vRowTemp.elementAt(0)).intValue());
                oMdco.setNombre((String) vRowTemp.elementAt(1));
                oMdco.setApellidoPaterno((String) vRowTemp.elementAt(2));
                oMdco.setApellidoMaterno((String) vRowTemp.elementAt(3));
                oMdco.setRFC((String) vRowTemp.elementAt(4));
                oMdco.setCURP((String) vRowTemp.elementAt(5));
                oMdco.setActivo(true);
                String horaTmp = (String) vRowTemp.elementAt(12);
                if (horaTmp.equals("")) {
                    oMdco.setTurno(new Turno((String) vRowTemp.elementAt(6), (String) vRowTemp.elementAt(7)));
                } else {
                    oMdco.setTurno(new Turno("Horario especial", horaTmp));
                }
                String cedprofesp = (String) vRowTemp.elementAt(11);
                if (cedprofesp.equals("")) {
                    oMdco.setCedProf((String) vRowTemp.elementAt(8));
                } else {
                    oMdco.setCedProf(cedprofesp);
                }
                oMdco.setEsp(new Especialidad(((Double) vRowTemp.elementAt(9)).intValue(), (String) vRowTemp.elementAt(10)));
                vObj.add(oMdco);
            }
            nTam = vObj.size();
            arrRet = new Medico[nTam];
            for (i = 0; i < nTam; i++) {
                arrRet[i] = vObj.elementAt(i);
            }
        }
        return arrRet;
    }

    /**
     * Busca la lista de todos los medicos registrados por especialidad, Regresa
     * un arreglo de Medicos (con datos basicos como el nombre, apellidos
     * paternos y maternos)
     *
     */
    public Medico[] buscarTodosMedicosPorEspecialidad() throws Exception {
        Medico[] arrRet = null;
        Medico oMdco = null;
        Vector rst = null;
        Vector<Medico> vObj = null;
        String sQuery = "";
        int i = 0, nTam = 0;
        sQuery = " select * from buscartodosmedicosporespecialidad('" + this.oEsp.getDescrip() + "')";
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
            vObj = new Vector<Medico>();

            for (i = 0; i < rst.size(); i++) {
                oMdco = new Medico();
                Vector vRowTemp = (Vector) rst.elementAt(i);
                oMdco.setFolioPers(((Double) vRowTemp.elementAt(0)).intValue());
                oMdco.setNombre((String) vRowTemp.elementAt(1));
                oMdco.setApellidoPaterno((String) vRowTemp.elementAt(2));
                oMdco.setApellidoMaterno((String) vRowTemp.elementAt(3));
                vObj.add(oMdco);
            }
            nTam = vObj.size();
            arrRet = new Medico[nTam];

            for (i = 0; i < nTam; i++) {
                arrRet[i] = vObj.elementAt(i);
            }
        }
        return arrRet;
    }

    /**
     * Busca la lista de todos los medicos con regalias, regresa un arreglo de
     * Medicos
     *
     */
    public Medico[] buscarTodosMedicosConRegalias(Date fechaIni, Date fechaFin) throws Exception {
        Medico[] arrRet = null;
        Medico oMdco = null;
        Vector rst = null;
        Vector<Medico> vObj = null;
        String sQuery = "";
        int i = 0, nTam = 0;
        sQuery = "select * from buscarMedicosConRegalias('" + fechaIni + "','" + fechaFin + "') ";
        System.out.println(sQuery);
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
            vObj = new Vector<Medico>();

            for (i = 0; i < rst.size(); i++) {
                oMdco = new Medico();
                Vector vRowTemp = (Vector) rst.elementAt(i);
                // 	m.nfoliopers, ph.snombre, ph.sappaterno, ph.sapmaterno, m.ncveesp, m.bconvenio, m.stipo, m.sregssa, m.scedesp
                oMdco.setFolioPers(((Double) vRowTemp.elementAt(0)).intValue());
                oMdco.setNombre((String) vRowTemp.elementAt(1));
                oMdco.setApellidoPaterno((String) vRowTemp.elementAt(2));
                oMdco.setApellidoMaterno((String) vRowTemp.elementAt(3));
                oMdco.setEsp(new Especialidad(((Double) vRowTemp.elementAt(4)).intValue()));
                oMdco.setConvenio(((String) vRowTemp.elementAt(5)).equals("1"));
                //oMdco.setRegSSA(((String) vRowTemp.elementAt(6)));
                oMdco.setCedProf((String) vRowTemp.elementAt(7));
                vObj.add(oMdco);
            }
            nTam = vObj.size();
            arrRet = new Medico[nTam];
            for (i = 0; i < nTam; i++) {
                arrRet[i] = vObj.elementAt(i);
            }
        }
        return arrRet;
    }

    /**
     * Busca la lista de todos los medicos con regalias, regresa un arreglo de
     * Medicos
     *
     */
    public Medico[] buscarTodosMedicosGuardiaConRegalias(Date fechaIni, Date fechaFin) throws Exception {
        Medico[] arrRet = null;
        Medico oMdco = null;
        Vector rst = null;
        Vector<Medico> vObj = null;
        String sQuery = "";
        int i = 0, nTam = 0;
        sQuery = "select * from buscarMedicosGuardiaConRegalias('" + fechaIni + "','" + fechaFin + "') ";
        System.out.println(sQuery);
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
            vObj = new Vector<Medico>();

            for (i = 0; i < rst.size(); i++) {
                oMdco = new Medico();
                Vector vRowTemp = (Vector) rst.elementAt(i);
                oMdco.setFolioPers(((Double) vRowTemp.elementAt(0)).intValue());
                oMdco.setNombre((String) vRowTemp.elementAt(1));
                oMdco.setApellidoPaterno((String) vRowTemp.elementAt(2));
                oMdco.setApellidoMaterno((String) vRowTemp.elementAt(3));
                oMdco.setEsp(new Especialidad(((Double) vRowTemp.elementAt(4)).intValue()));
                oMdco.setConvenio(((String) vRowTemp.elementAt(5)).equals("1"));
                //oMdco.setRegSSA(((String) vRowTemp.elementAt(6)));
                oMdco.setCedProf((String) vRowTemp.elementAt(7));
                vObj.add(oMdco);
            }
            nTam = vObj.size();
            arrRet = new Medico[nTam];
            for (i = 0; i < nTam; i++) {
                arrRet[i] = vObj.elementAt(i);
            }
        }
        return arrRet;
    }

    public boolean isExterno(int nFolioPers) throws Exception {
        boolean bRet = false;
        Vector rst = null;
        String sQuery = "select * from validaMedicoExterno(" + nFolioPers + ")";
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
            Vector vRowTemp = (Vector) rst.elementAt(0);
            if ("t".equals(vRowTemp.get(0).toString())) {
                bRet = true;
            }
        }
        return bRet;
    }
    
    public ArrayList<Medico> buscaTodosParaCaja(int nIdPac, int nCveLin) throws Exception{
    ArrayList<Medico> listRet=null;
    Medico oMed = null;
    Vector rst = null;
    String sQuery = "";
        sQuery = "select * from buscaMedicosParaCaja( "+
                nIdPac+","+nCveLin+"::smallint )";
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
            listRet=new ArrayList();
            for (int i = 0; i < rst.size(); i++) {
                Vector vRowTemp = (Vector) rst.elementAt(i);
                oMed=new Medico();
                oMed.setFolioPers(((Double) vRowTemp.elementAt(0)).intValue());
                oMed.setApellidoPaterno((String) vRowTemp.elementAt(1));
                oMed.setApellidoMaterno((String) vRowTemp.elementAt(2));
                oMed.setNombre((String) vRowTemp.elementAt(3));
                listRet.add(oMed);
            }
        }
        return listRet;
    }

    public List<Medico> buscaTodoPersonal() throws Exception {
        Medico oMed = null;
        Especialidad oE=null;
        List<Medico> listRet=null;
        Vector rst = null;
        String sQuery = "";
        sQuery = "select * from buscatodospersonalhospitalario()";
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
            listRet=new ArrayList();
            for (int i = 0; i < rst.size(); i++) {
                Vector vRowTemp = (Vector) rst.elementAt(i);
                oMed=new Medico();
                oE=new Especialidad();
                oMed.setEsp(oE);
                oMed.setFolioPers(((Double) vRowTemp.elementAt(0)).intValue());
                oMed.setNac((Date) vRowTemp.elementAt(1));
                oMed.getEdoCiv().setCve((String) vRowTemp.elementAt(2));
                oMed.getEdoCiv().setDescrip((String) vRowTemp.elementAt(3));
                oMed.setNombre((String) vRowTemp.elementAt(4));
                oMed.setApellidoPaterno((String) vRowTemp.elementAt(5));
                oMed.setApellidoMaterno((String) vRowTemp.elementAt(6));
                oMed.setRFC((String)vRowTemp.elementAt(7));
                oMed.setCURP((String)vRowTemp.elementAt(8));
                oMed.setGenero(vRowTemp.elementAt(9).toString().charAt(0));
                oMed.setActivo(cambiaBoolean(((String)vRowTemp.elementAt(10)).charAt(0)));
                oMed.getTurno().setCve((String)vRowTemp.elementAt(11));
                oMed.getTurno().setDesc((String)vRowTemp.elementAt(12));
                oMed.getTurno().setHorario((String)vRowTemp.elementAt(13));
                oMed.getPuesto().setCve((String)vRowTemp.elementAt(14));
                oMed.getPuesto().setDescrip((String)vRowTemp.elementAt(15));
                oMed.setCedProf((String)vRowTemp.elementAt(16));
                oMed.getUsuario().setUsuario((String)vRowTemp.elementAt(17));
                oMed.setSalario(((Double)vRowTemp.elementAt(18)).floatValue());
                oMed.getAreaFunc().setCveAreaFun(((Double) vRowTemp.elementAt(19)).intValue());
                oMed.getAreaFunc().setDescripcion((String)vRowTemp.elementAt(20));
                oMed.getEsp().setCve(((Double) vRowTemp.elementAt(21)).intValue());
                if(!((String)vRowTemp.elementAt(22)).equals("")) {
                    oMed.setConvenio(cambiaBoolean(((String)vRowTemp.elementAt(22)).charAt(0)));
                }
                oMed.setTipo((String)vRowTemp.elementAt(23));
                oMed.setRegSSA((String)vRowTemp.elementAt(24));
                oMed.setCedEsp((String)vRowTemp.elementAt(25));
                oMed.setHorarioEsp((String)vRowTemp.elementAt(26));
                listRet.add(oMed);
            }
        }
        return listRet;
    }
    
    public List<Medico> buscaTodoMedicosExternos() throws Exception {
        Medico oMed = null;
        Especialidad oE=null;
        List<Medico> listRet=null;
        Vector rst = null;
        String sQuery = "";
        sQuery = "select * from buscatodosmedicoexterno()";
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
            listRet=new ArrayList();
            for (int i = 0; i < rst.size(); i++) {
                Vector vRowTemp = (Vector) rst.elementAt(i);
                oMed=new Medico();
                oE=new Especialidad();
                oMed.setEsp(oE);
                oMed.setFolioPers(((Double) vRowTemp.elementAt(0)).intValue());
                oMed.setNac((Date) vRowTemp.elementAt(1));
                oMed.getEdoCiv().setCve((String) vRowTemp.elementAt(2));
                oMed.getEdoCiv().setDescrip((String) vRowTemp.elementAt(3));
                oMed.setNombre((String) vRowTemp.elementAt(4));
                oMed.setApellidoPaterno((String) vRowTemp.elementAt(5));
                oMed.setApellidoMaterno((String) vRowTemp.elementAt(6));
                oMed.setRFC((String)vRowTemp.elementAt(7));
                oMed.setCURP((String)vRowTemp.elementAt(8));
                oMed.setGenero(vRowTemp.elementAt(9).toString().charAt(0));
                if(!((String)vRowTemp.elementAt(10)).equals(""))
                    oMed.setActivo(cambiaBoolean(((String)vRowTemp.elementAt(10)).charAt(0)));
                oMed.getTurno().setCve((String)vRowTemp.elementAt(11));
                oMed.getTurno().setDesc((String)vRowTemp.elementAt(12));
                oMed.getTurno().setHorario((String)vRowTemp.elementAt(13));
                oMed.getPuesto().setCve((String)vRowTemp.elementAt(14));
                oMed.getPuesto().setDescrip((String)vRowTemp.elementAt(15));
                oMed.setCedProf((String)vRowTemp.elementAt(16));
                oMed.getUsuario().setUsuario((String)vRowTemp.elementAt(17));
                oMed.getEsp().setCve(((Double) vRowTemp.elementAt(18)).intValue());
                if(!((String)vRowTemp.elementAt(19)).equals(""))
                    oMed.setConvenio(cambiaBoolean(((String)vRowTemp.elementAt(19)).charAt(0)));
                oMed.setTipo((String)vRowTemp.elementAt(20));
                oMed.setRegSSA((String)vRowTemp.elementAt(21));
                oMed.setCedEsp((String)vRowTemp.elementAt(22));
                listRet.add(oMed);
            }
        }
        return listRet;
    }
    
    
    public String guardaPersonalHospitalario(Medico oMed,List<AreaDeServicio> vServ) throws Exception{
        Vector rst = null;
        String sQuery = "", sRet = "";
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        if (oMed==null){
            throw new Exception("Personal hospitalario.guarda: error de programación, faltan datos");
	}else {
             sQuery="SELECT * from insertapersonalhospitalario('"+
                    format.format(oMed.getNac())+"'::date,"+
                    isNull(oMed.getEdoCiv().getCve())+ "::character,'"+
                    oMed.getNombre().toUpperCase()+ "'::character varying,'"+
                    oMed.getApellidoPaterno().toUpperCase() + "'::character varying,"+
                    isNull(oMed.getApellidoMaterno()).toUpperCase() + "::character varying,'"+
                    oMed.getRFC().toUpperCase() + "'::character varying,'"+
                    oMed.getCURP().toUpperCase() + "'::character varying,'"+
                    oMed.getGenero() + "'::character,'"+
                    cambiaBoolean(oMed.getActivo()) + "'::character,'"+
                    oMed.getTurno().getCve() + "'::character varying,'"+
                    oMed.getPuesto().getCve() + "'::character varying,"+
                    isNull(oMed.getCedProf()) + "::character varying,"+
                    oMed.getSalario() + "::numeric,"+
                    isCero(oMed.getAreaFunc().getCveAreaFun())+ "::smallint,"+
                    oMed.getEsp().getCve() + "::smallint,'"+
                    cambiaBoolean(oMed.getConvenio()) + "'::character,"+
                    isNull(oMed.getRegSSA()) + "::character varying,"+
                    isNull(oMed.getCedEsp()) + "::character varying,"+
                    isNull(oMed.getHorarioEsp()) + "::character varying,"+
                    "ARRAY"+vServ+"::integer[]);";
            if (getAD() == null){
                setAD(new AccesoDatos());
		getAD().conectar();
		rst = getAD().ejecutarConsulta(sQuery);
		getAD().desconectar();
                setAD(null);
            }
            if (rst!=null){
                sRet = rst.get(0).toString().substring(1, rst.get(0).toString().length() - 1);
            }
	}
        return sRet;
    }
    
    public String modificaPersonalHospitalario(Medico oMed,List<AreaDeServicio> vServ) throws Exception{
        Vector rst = null;
        String sQuery = "", sRet = "";
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        
        if (oMed==null){
            throw new Exception("Personal hospitalario.modifica: error de programación, faltan datos");
	}else {
             sQuery="SELECT * from modificapersonalhospitalario("+
                    oMed.getFolioPers()+"::integer,'"+
                    format.format(oMed.getNac())+"'::date,"+
                    isNull(oMed.getEdoCiv().getCve())+ "::character,'"+
                    oMed.getNombre().toUpperCase()+ "'::character varying,'"+
                    oMed.getApellidoPaterno().toUpperCase() + "'::character varying,"+
                    isNull(oMed.getApellidoMaterno()).toUpperCase() + "::character varying,'"+
                    oMed.getRFC().toUpperCase() + "'::character varying,'"+
                    oMed.getCURP().toUpperCase() + "'::character varying,'"+
                    oMed.getGenero() + "'::character,'"+
                    cambiaBoolean(oMed.getActivo()) + "'::character,'"+
                    oMed.getTurno().getCve() + "'::character varying,'"+
                    oMed.getPuesto().getCve() + "'::character varying,"+
                    isNull(oMed.getCedProf()) + "::character varying,"+
                    oMed.getSalario() + "::numeric,"+
                    isCero(oMed.getAreaFunc().getCveAreaFun())+ "::smallint,"+
                    oMed.getEsp().getCve() + "::smallint,'"+
                    cambiaBoolean(oMed.getConvenio()) + "'::character,"+
                    isNull(oMed.getRegSSA()+"") + "::character varying,"+
                    isNull(oMed.getCedEsp()+"") + "::character varying,"+
                    isNull(oMed.getHorarioEsp()) + "::character varying,"+
                    "ARRAY"+vServ+"::integer[]);";
            System.out.println(sQuery);
            if (getAD() == null){
                setAD(new AccesoDatos());
		getAD().conectar();
		rst = getAD().ejecutarConsulta(sQuery);
		getAD().desconectar();
                setAD(null);
            } 
            if (rst!=null){
                sRet = rst.get(0).toString().substring(1, rst.get(0).toString().length() - 1);
            }
	}
        return sRet;
    }

    //=============== SET & GET ===============//
    public boolean getConvenio() {
        return bConvenio;
    }

    public void setConvenio(boolean bConvenio) {
        this.bConvenio = bConvenio;
    }

    public Especialidad getEsp() {
        return oEsp;
    }

    public void setEsp(Especialidad oEsp) {
        this.oEsp = oEsp;
    }

    public String  getRegSSA() {
        return sRegSSA;
    }

    public void setRegSSA(String  sRegSSA) {
        this.sRegSSA = sRegSSA;
    }

    public String getTipo() {
        return sTipo;
    }

    public void setTipo(String sTipo) {
        this.sTipo = sTipo;
    }

    public Paciente getPaciente() {
        return m_Paciente;
    }

    public void setPaciente(Paciente m_Paciente) {
        this.m_Paciente = m_Paciente;
    }
    
    /**
     * @return the sCedEsp
     */
    public String getCedEsp() {
        return sCedEsp;
    }

    /**
     * @param sCedEsp the sCedEsp to set
     */
    public void setCedEsp(String sCedEsp) {
        this.sCedEsp = sCedEsp;
    }

    /**
     * @return the sHorarioEsp
     */
    public String getHorarioEsp() {
        return sHorarioEsp;
    }

    /**
     * @param sHorarioEsp the sHorarioEsp to set
     */
    public void setHorarioEsp(String sHorarioEsp) {
        this.sHorarioEsp = sHorarioEsp;
    }
    
    public String isNull(String param){
        if(param==null || param.equals("") )
            param=null;
        else
            param="'"+param+"'";
        return param;
    }
    
    public String isCero(int nCve){
        String sCad="";
        if(nCve==0)
            sCad=null;
        else
            sCad=nCve+"";
        return sCad;
    }
}