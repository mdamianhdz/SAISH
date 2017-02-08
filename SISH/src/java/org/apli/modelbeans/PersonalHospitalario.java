package org.apli.modelbeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import org.apli.AD.AccesoDatos;
import org.apli.modelbeans.contabilidadInterna.AreaFuncionamiento;

/**
 * Representa a los profesionales de la salud (m�dicos, enfermeras, t�cnicos) y
 * personal administrativo que labora en el hospital (contratado directamente o
 * como externo)
 *
 * @author BAOZ
 * @version 1.0
 * @created 07-abr-2014 01:57:20 p.m.
 */
public class PersonalHospitalario extends Persona implements Serializable {

    /**
     * Indica si el personal est� en activo (true) o no (false) Un personal
     * inactivo aparece en las listas para reportes pero no en las listas para
     * asignaciones (citas por ejemplo)
     */
    protected boolean bActivo;
    /**
     * Identificador del personal
     */
    protected int nFolioPers = 0;
    /**
     * Tipo de puesto que ocupa
     */
    protected Puesto oPuesto;
    /**
     * Turno del personal
     */
    protected Turno oTurno;
    /**
     * C�dula profesional
     */
    protected String sCedProf;
    private AccesoDatos oAD;
    private AreaDeServicio oAS;
    private Nomina oNomina;
    private Usuario oUsu;
    private AreaFuncionamiento oAreaFunc;
    private List<DetalleNomina> listConceptos;
    private float nSalario;
    private float nPercepciones;
    private float nDeducciones;


    public PersonalHospitalario() {
        listConceptos = new ArrayList();
        oNomina = new Nomina();
        oEdoCiv = new EstadoCivil();
        oTurno = new Turno();
        oPuesto = new Puesto();
        oUsu = new Usuario();
        oAreaFunc = new AreaFuncionamiento();
    }

    public List<PersonalHospitalario> buscaPersonalAreaFuncionamiento(int narea) throws Exception{
        List<PersonalHospitalario> listRet=null;
        PersonalHospitalario oPH;
        
        Vector rst=null;
        String sQuery="";
        
        sQuery= "select * from  buscapersonalporareadefuncionamiento("+narea+")";
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
                oPH = new PersonalHospitalario();
                Vector vRowTemp = (Vector) rst.elementAt(i);
                oPH.setFolioPers(((Double) vRowTemp.elementAt(0)).intValue());
                oPH.setNombre((String) vRowTemp.elementAt(3));
                oPH.setApellidoPaterno((String) vRowTemp.elementAt(4));
                oPH.setApellidoMaterno((String) vRowTemp.elementAt(5));
                listRet.add(oPH);
            }
        }
        return listRet;
    }
    
    public List<PersonalHospitalario> buscaPersonalPrestamo() throws Exception {
        List<PersonalHospitalario> listRet = null;
        PersonalHospitalario oPH;
        Puesto oP;

        Vector rst = null;
        String sQuery = "";

        sQuery = "select * from buscaPersonalPrestamo()";
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
                oPH = new PersonalHospitalario();
                Vector vRowTemp = (Vector) rst.elementAt(i);
                oPH.setFolioPers(((Double) vRowTemp.elementAt(0)).intValue());
                oPH.setNombre((String) vRowTemp.elementAt(1));
                oPH.setApellidoPaterno((String) vRowTemp.elementAt(2));
                oPH.setApellidoMaterno((String) vRowTemp.elementAt(3));
                oPH.setRFC((String) vRowTemp.elementAt(4));
                oPH.setCURP((String) vRowTemp.elementAt(5));
                oP = new Puesto();
                oP.setDescrip((String) vRowTemp.elementAt(6));
                oPH.setPuesto(oP);
                listRet.add(oPH);
            }
        }
        return listRet;
    }
    
    public PersonalHospitalario buscaDatosPersonal(int nFolio) throws Exception{
        PersonalHospitalario oPH = null;
        Puesto oPT;
        Vector rst=null;
        String sQuery="";
        
        sQuery = "select * from buscaPersonalHospitalario(" + nFolio + ")";
        if(getAD()==null){
            setAD(new AccesoDatos());
            getAD().conectar();
            rst=getAD().ejecutarConsulta(sQuery);
            getAD().desconectar();
            setAD(null);
        }else{
            rst=getAD().ejecutarConsulta(sQuery);
        }
        if(rst !=null){
            for(int i=0; i<rst.size(); i++){
                oPH= new PersonalHospitalario();
                Vector vRowTemp= (Vector) rst.elementAt(i);
                oPH.setFolioPers(((Double) vRowTemp.elementAt(0)).intValue());
                oPH.setNombre((String) vRowTemp.elementAt(1));
                oPH.setApellidoPaterno((String) vRowTemp.elementAt(2));
                oPH.setApellidoMaterno((String) vRowTemp.elementAt(3));
                oPH.setRFC((String) vRowTemp.elementAt(4));
                oPH.setCURP((String) vRowTemp.elementAt(5));

                oPT = new Puesto();
                oPT.setCve((String) vRowTemp.elementAt(6));
                oPT.setDescrip(oPT.buscaDescripcionPuesto((String) vRowTemp.elementAt(6)));
                oPT.setSalarioMensual(((Double) vRowTemp.elementAt(7)).floatValue());

                oPH.setPuesto(oPT);
            }
        }
        return oPH;
    }

    public List<PersonalHospitalario> buscaPersonal(int nFolio) throws Exception {
        List<PersonalHospitalario> listRet = null;
        PersonalHospitalario oPH;
        Puesto oPT;

        Vector rst = null;
        String sQuery = "";

        sQuery = "select * from buscaPersonalHospitalario(" + nFolio + ")";

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
                oPH = new PersonalHospitalario();
                Vector vRowTemp = (Vector) rst.elementAt(i);
                oPH.setFolioPers(((Double) vRowTemp.elementAt(0)).intValue());
                oPH.setNombre((String) vRowTemp.elementAt(1));
                oPH.setApellidoPaterno((String) vRowTemp.elementAt(2));
                oPH.setApellidoMaterno((String) vRowTemp.elementAt(3));
                oPH.setRFC((String) vRowTemp.elementAt(4));
                oPH.setCURP((String) vRowTemp.elementAt(5));

                oPT = new Puesto();
                oPT.setCve((String) vRowTemp.elementAt(6));
                oPT.setDescrip(oPT.buscaDescripcionPuesto((String) vRowTemp.elementAt(6)));
                oPT.setSalarioMensual(((Double) vRowTemp.elementAt(7)).floatValue());

                oPH.setPuesto(oPT);
                listRet.add(oPH);

            }
        }
        return listRet;
    }
    
    public List<PersonalHospitalario> buscaPersonalNomina() throws Exception {
        List<PersonalHospitalario> listRet = null;
        PersonalHospitalario oPH;
        Puesto oP;

        Vector rst = null;
        String sQuery = "";

        sQuery = "select * from buscaPersonalNomina()";
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
                oPH = new PersonalHospitalario();
                Vector vRowTemp = (Vector) rst.elementAt(i);
                oPH.setFolioPers(((Double) vRowTemp.elementAt(0)).intValue());
                oPH.setNombre((String) vRowTemp.elementAt(1));
                oPH.setApellidoPaterno((String) vRowTemp.elementAt(2));
                oPH.setApellidoMaterno((String) vRowTemp.elementAt(3));
                oPH.setRFC((String) vRowTemp.elementAt(4));
                oP = new Puesto();
                oP.setDescrip((String) vRowTemp.elementAt(5));
                oPH.setPuesto(oP);
                listRet.add(oPH);
            }
        }
        return listRet;
    }

    public List<PersonalHospitalario> buscaPersonalNomina(String sCondicion) throws Exception {
        List<PersonalHospitalario> listRet = null;
        PersonalHospitalario oPH;

        Vector rst = null;
        String sQuery = "";

        sQuery = "select * from buscadatosnomina()";
        if (!"".equals(sCondicion)) {
            sQuery = sQuery + " WHERE " + sCondicion;
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

        if (rst != null) {
            listRet = new ArrayList();
            for (int i = 0; i < rst.size(); i++) {
                Vector vRowTemp = (Vector) rst.elementAt(i);
                if (i == 0) {
                    oPH = new PersonalHospitalario();
                    oPH.setFolioPers(((Double) vRowTemp.elementAt(0)).intValue());
                    oPH.setNombre((String) vRowTemp.elementAt(1));
                    oPH.setApellidoPaterno((String) vRowTemp.elementAt(2));
                    oPH.setApellidoMaterno((String) vRowTemp.elementAt(3));
                    oPH.setRFC((String) vRowTemp.elementAt(4));
                    oAS = new AreaDeServicio();
                    oAS.setCve("" + (Double) vRowTemp.elementAt(5));
                    oAS.setDescrip((String) vRowTemp.elementAt(6));
                    oPH.setAreaServ(oAS);
                    oPuesto = new Puesto();
                    oPuesto.setDescrip((String) vRowTemp.elementAt(7));
                    oPH.setPuesto(oPuesto);
                    oPH.setSalario(((Double) vRowTemp.elementAt(8)).floatValue());
                    oNomina = new Nomina();
                    oNomina.setInicio((Date) vRowTemp.elementAt(9));
                    oNomina.setFin((Date) vRowTemp.elementAt(10));
                    oNomina.setPago((Date) vRowTemp.elementAt(11));
                    oPH.setNomina(oNomina);
                    listConceptos = new ArrayList();
                    DetalleNomina oDetNom = new DetalleNomina();
                    oDetNom.setInicio((Date) vRowTemp.elementAt(9));
                    oDetNom.setFin((Date) vRowTemp.elementAt(10));
                    if (((Double) vRowTemp.elementAt(12)).intValue() > 0) {
                        ConceptoNomina oConcNom = new ConceptoNomina();
                        oConcNom.setCveConcepNom(((Double) vRowTemp.elementAt(12)).intValue());
                        oConcNom.setTipoConNom(((String) vRowTemp.elementAt(13)).charAt(0));
                        oConcNom.setDescripcion((String) vRowTemp.elementAt(14));
                        oDetNom.setConcNom(oConcNom);
                        oDetNom.setMonto(((Double) vRowTemp.elementAt(15)).floatValue());
                        listConceptos.add(oDetNom);
                    }
                    oPH.setListConceptos(listConceptos);
                    listRet.add(oPH);
                } else {
                    if (listRet.get(listRet.size() - 1).getFolioPers() == ((Double) vRowTemp.elementAt(0)).intValue()) {
                        if (listRet.get(listRet.size() - 1).getNomina().getInicio().equals((Date) vRowTemp.elementAt(9))
                                && listRet.get(listRet.size() - 1).getNomina().getFin().equals((Date) vRowTemp.elementAt(10))) {
                            DetalleNomina oDetNom = new DetalleNomina();
                            oDetNom.setInicio((Date) vRowTemp.elementAt(9));
                            oDetNom.setFin((Date) vRowTemp.elementAt(10));
                            ConceptoNomina oConcNom = new ConceptoNomina();
                            oConcNom.setCveConcepNom(((Double) vRowTemp.elementAt(12)).intValue());
                            oConcNom.setTipoConNom(((String) vRowTemp.elementAt(13)).charAt(0));
                            oConcNom.setDescripcion((String) vRowTemp.elementAt(14));
                            oDetNom.setConcNom(oConcNom);
                            oDetNom.setMonto(((Double) vRowTemp.elementAt(15)).floatValue());
                            listRet.get(listRet.size() - 1).getListConceptos().add(oDetNom);
                        } else {
                            oPH = new PersonalHospitalario();
                            oPH.setFolioPers(((Double) vRowTemp.elementAt(0)).intValue());
                            oPH.setNombre((String) vRowTemp.elementAt(1));
                            oPH.setApellidoPaterno((String) vRowTemp.elementAt(2));
                            oPH.setApellidoMaterno((String) vRowTemp.elementAt(3));
                            oPH.setRFC((String) vRowTemp.elementAt(4));
                            oAS = new AreaDeServicio();
                            oAS.setCve("" + ((Double) vRowTemp.elementAt(5)).intValue());
                            oAS.setDescrip((String) vRowTemp.elementAt(6));
                            oPH.setAreaServ(oAS);
                            oPuesto = new Puesto();
                            oPuesto.setDescrip((String) vRowTemp.elementAt(7));
                            oPH.setPuesto(oPuesto);
                            oPH.setSalario(((Double) vRowTemp.elementAt(8)).floatValue());
                            oNomina = new Nomina();
                            oNomina.setInicio((Date) vRowTemp.elementAt(9));
                            oNomina.setFin((Date) vRowTemp.elementAt(10));
                            oNomina.setPago((Date) vRowTemp.elementAt(11));
                            oPH.setNomina(oNomina);
                            listConceptos = new ArrayList();
                            DetalleNomina oDetNom = new DetalleNomina();
                            oDetNom.setInicio((Date) vRowTemp.elementAt(9));
                            oDetNom.setFin((Date) vRowTemp.elementAt(10));
                            if (((Double) vRowTemp.elementAt(12)).intValue() > 0) {
                                ConceptoNomina oConcNom = new ConceptoNomina();
                                oConcNom.setCveConcepNom(((Double) vRowTemp.elementAt(12)).intValue());
                                oConcNom.setTipoConNom(((String) vRowTemp.elementAt(13)).charAt(0));
                                oConcNom.setDescripcion((String) vRowTemp.elementAt(14));
                                oDetNom.setConcNom(oConcNom);
                                oDetNom.setMonto(((Double) vRowTemp.elementAt(15)).floatValue());
                                listConceptos.add(oDetNom);
                            }
                            oPH.setListConceptos(listConceptos);
                            listRet.add(oPH);
                        }
                    } else {
                        oPH = new PersonalHospitalario();
                        oPH.setFolioPers(((Double) vRowTemp.elementAt(0)).intValue());
                        oPH.setNombre((String) vRowTemp.elementAt(1));
                        oPH.setApellidoPaterno((String) vRowTemp.elementAt(2));
                        oPH.setApellidoMaterno((String) vRowTemp.elementAt(3));
                        oPH.setRFC((String) vRowTemp.elementAt(4));
                        oAS = new AreaDeServicio();
                        oAS.setCve("" + ((Double) vRowTemp.elementAt(5)).intValue());
                        oAS.setDescrip((String) vRowTemp.elementAt(6));
                        oPH.setAreaServ(oAS);
                        oPuesto = new Puesto();
                        oPuesto.setDescrip((String) vRowTemp.elementAt(7));
                        oPH.setPuesto(oPuesto);
                        oPH.setSalario(((Double) vRowTemp.elementAt(8)).floatValue());
                        oNomina = new Nomina();
                        oNomina.setInicio((Date) vRowTemp.elementAt(9));
                        oNomina.setFin((Date) vRowTemp.elementAt(10));
                        oNomina.setPago((Date) vRowTemp.elementAt(11));
                        oPH.setNomina(oNomina);
                        listConceptos = new ArrayList();
                        DetalleNomina oDetNom = new DetalleNomina();
                        oDetNom.setInicio((Date) vRowTemp.elementAt(9));
                        oDetNom.setFin((Date) vRowTemp.elementAt(10));
                        if (((Double) vRowTemp.elementAt(12)).intValue() > 0) {
                            ConceptoNomina oConcNom = new ConceptoNomina();
                            oConcNom.setCveConcepNom(((Double) vRowTemp.elementAt(12)).intValue());
                            oConcNom.setTipoConNom(((String) vRowTemp.elementAt(13)).charAt(0));
                            oConcNom.setDescripcion((String) vRowTemp.elementAt(14));
                            oDetNom.setConcNom(oConcNom);
                            oDetNom.setMonto(((Double) vRowTemp.elementAt(15)).floatValue());
                            listConceptos.add(oDetNom);
                        }
                        oPH.setListConceptos(listConceptos);
                        listRet.add(oPH);
                    }
                }
            }
            for (int i = 0; i < listRet.size(); i++) {
                listRet.set(i, calculaNomina(listRet.get(i)));
            }
        }
        return listRet;
    }

    public PersonalHospitalario calculaNomina(PersonalHospitalario oPH) {
        System.out.println("calculaNomina");
        float nP = 0.0f, nD = 0.0f;
        for (int j = 0; j < oPH.getListConceptos().size(); j++) {
            if (oPH.getListConceptos().get(j).getConcNom().getTipoConNom() == 'P') {
                nP = nP + oPH.getListConceptos().get(j).getMonto();
            } else {
                nD = nD + oPH.getListConceptos().get(j).getMonto();
            }
        }
        oPH.setPercepciones(nP);
        oPH.setDeducciones(nD);

        return oPH;
    }

    public PersonalHospitalario buscaPersonalPorCveUsuario(String sCveUsuario) throws Exception {
        PersonalHospitalario oPH = null;
        Vector rst = null;
        Vector<PersonalHospitalario> vObj = null;
        String sQuery = "";
        int i = 0;

        sQuery = "SELECT * FROM buscapersonalporusuario('" + sCveUsuario + "'::character varying);";
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
            vObj = new Vector<PersonalHospitalario>();
            oPH = new PersonalHospitalario();
            Vector vRowTemp = (Vector) rst.elementAt(0);
            oPH.setFolioPers(((Double) vRowTemp.elementAt(0)).intValue());
            oPH.setNombre((String) vRowTemp.elementAt(1));
            oPH.setApellidoPaterno((String) vRowTemp.elementAt(2));
            oPH.setApellidoMaterno((String) vRowTemp.elementAt(3));

        }
        return oPH;
    }

    public PersonalHospitalario buscaLlavePersonal() throws Exception {
        PersonalHospitalario oPH = new PersonalHospitalario();

        Vector rst = null;
        String sQuery = "";

        sQuery = "select * from buscaLlavepersonal(" + this.nFolioPers + ")";
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
            for (int i = 0; i < rst.size(); i++) {
                Vector vRowTemp = (Vector) rst.elementAt(0);
                oPH.setFolioPers(((Double) vRowTemp.elementAt(0)).intValue());
                oPH.setNombre((String) vRowTemp.elementAt(1));
                oPH.setApellidoPaterno((String) vRowTemp.elementAt(2));
                oPH.setApellidoMaterno((String) vRowTemp.elementAt(3));
            }
        }
        return oPH;
    }

    public List<PersonalHospitalario> buscaTodosPersonalAutorizacion() throws Exception {
        List<PersonalHospitalario> listaRegConPromotores = new ArrayList<PersonalHospitalario>();
        PersonalHospitalario oPH = null;
        Vector rst = null;
        Vector<PersonalHospitalario> vObj = null;
        String sQuery = "";
        int i = 0;

        sQuery = "SELECT * FROM buscartodospersonalautorizacion();";
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
            vObj = new Vector<PersonalHospitalario>();
            for (i = 0; i < rst.size(); i++) {
                oPH = new PersonalHospitalario();
                Vector vRowTemp = (Vector) rst.elementAt(i);
                oPH.setFolioPers(((Double) vRowTemp.elementAt(0)).intValue());
                oPH.setNombre((String) vRowTemp.elementAt(1));
                oPH.setApellidoPaterno((String) vRowTemp.elementAt(2));
                oPH.setApellidoMaterno((String) vRowTemp.elementAt(3));
                listaRegConPromotores.add(oPH);
            }
        }
        return listaRegConPromotores;
    }

    public List<PersonalHospitalario> buscaTodosPersonalActivo() throws Exception {
        List<PersonalHospitalario> lista = new ArrayList<PersonalHospitalario>();
        PersonalHospitalario oPH = null;
        Vector rst = null;
        Vector<PersonalHospitalario> vObj = null;
        String sQuery = "";
        int i = 0;

        sQuery = "SELECT * FROM buscarTodosPersonalActivo();";
        if (oAD == null) {
            oAD = new AccesoDatos();
            oAD.conectar();
            rst = oAD.ejecutarConsulta(sQuery);
           oAD.desconectar();
            oAD = null;
        } else {
            rst = oAD.ejecutarConsulta(sQuery);
        }
        if (rst != null) {
            vObj = new Vector<PersonalHospitalario>();
            for (i = 0; i < rst.size(); i++) {
                oPH = new PersonalHospitalario();
                Vector vRowTemp = (Vector) rst.elementAt(i);
                oPH.setFolioPers(((Double) vRowTemp.elementAt(0)).intValue());
                oPH.setNombre((String) vRowTemp.elementAt(1));
                oPH.setApellidoPaterno((String) vRowTemp.elementAt(2));
                oPH.setApellidoMaterno((String) vRowTemp.elementAt(3));
                lista.add(oPH);
            }
        }
        return lista;
    }

    
    public List<PersonalHospitalario> buscaTodosPersonalTecnico() throws Exception {
        List<PersonalHospitalario> listaRegConPromotores = new ArrayList<PersonalHospitalario>();
        PersonalHospitalario oPH = null;
        Vector rst = null;
        Vector<PersonalHospitalario> vObj = null;
        String sQuery = "";
        int i = 0;

        sQuery = "SELECT * FROM buscartodospersonaltecnico();";
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
            vObj = new Vector<PersonalHospitalario>();
            for (i = 0; i < rst.size(); i++) {
                oPH = new PersonalHospitalario();
                Vector vRowTemp = (Vector) rst.elementAt(i);
                oPH.setFolioPers(((Double) vRowTemp.elementAt(0)).intValue());
                oPH.setNombre((String) vRowTemp.elementAt(1));
                oPH.setApellidoPaterno((String) vRowTemp.elementAt(2));
                oPH.setApellidoMaterno((String) vRowTemp.elementAt(3));
                listaRegConPromotores.add(oPH);
            }
        }
        return listaRegConPromotores;
    }

    public List<PersonalHospitalario> buscaTodosPersonalEnfermeras() throws Exception {
        List<PersonalHospitalario> listaEnf = new ArrayList<PersonalHospitalario>();
        PersonalHospitalario oPH = null;
        Vector rst = null;
        Vector<PersonalHospitalario> vObj = null;
        String sQuery = "";
        int i = 0;

        sQuery = "SELECT * FROM buscartodospersonalenfermeras();";
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
            vObj = new Vector<PersonalHospitalario>();
            for (i = 0; i < rst.size(); i++) {
                oPH = new PersonalHospitalario();
                Vector vRowTemp = (Vector) rst.elementAt(i);
                oPH.setFolioPers(((Double) vRowTemp.elementAt(0)).intValue());
                oPH.setNombre((String) vRowTemp.elementAt(1));
                oPH.setApellidoPaterno((String) vRowTemp.elementAt(2));
                oPH.setApellidoMaterno((String) vRowTemp.elementAt(3));
                listaEnf.add(oPH);
            }
        }
        return listaEnf;
    }

    public List<PersonalHospitalario> buscaTodosPacientesArea(String nArea) throws Exception {
        List<PersonalHospitalario> listRet = null;
        PersonalHospitalario oPH;

        Vector rst = null;
        String sQuery = "";

        sQuery = "select * from buscatodospersonalareaserv(" + nArea + "::int2)  ";
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
                oPH = new PersonalHospitalario();
                Vector vRowTemp = (Vector) rst.elementAt(i);
                oPH.setFolioPers(((Double) vRowTemp.elementAt(0)).intValue());
                oPH.setNombre((String) vRowTemp.elementAt(1));
                oPH.setApellidoPaterno((String) vRowTemp.elementAt(2));
                oPH.setApellidoMaterno((String) vRowTemp.elementAt(3));
                listRet.add(oPH);
            }
        }
        return listRet;
    }
       
    public PersonalHospitalario buscaPersonalInterno(int nFolio) throws Exception {
        PersonalHospitalario oPH = null;

        Vector rst = null;
        String sQuery = "";
        sQuery = "select * from buscaPersonalInterno()";
        if (nFolio>0)
            sQuery = sQuery+" where pnFolio="+nFolio;
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
            for (int i = 0; i < rst.size(); i++) {
                Vector vRowTemp = (Vector) rst.elementAt(0);
                oPH=new PersonalHospitalario();
                oPH.setFolioPers(((Double) vRowTemp.elementAt(0)).intValue());
                oPH.setNombre((String) vRowTemp.elementAt(1));
                oPH.setApellidoPaterno((String) vRowTemp.elementAt(2));
                oPH.setApellidoMaterno((String) vRowTemp.elementAt(3));
            }
        }
        return oPH;
    }
    
    public List<PersonalHospitalario> buscaPersonalInterno() throws Exception {
        PersonalHospitalario oPH = null;
        List<PersonalHospitalario> listRet=null;
        Vector rst = null;
        String sQuery = "";
        sQuery = "select * from buscaPersonalInterno()";
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
                oPH=new PersonalHospitalario();
                oPH.setFolioPers(((Double) vRowTemp.elementAt(0)).intValue());
                oPH.setNombre((String) vRowTemp.elementAt(1));
                oPH.setApellidoPaterno((String) vRowTemp.elementAt(2));
                oPH.setApellidoMaterno((String) vRowTemp.elementAt(3));
                listRet.add(oPH);
            }
        }
        return listRet;
    }
    
    public PersonalHospitalario[] buscaTodasJefasEnfermeria()throws Exception{
    PersonalHospitalario[] arrRet = null;
    PersonalHospitalario oPH=null;
    Vector rst = null;
    Vector<PersonalHospitalario> vObj = null;
    String sQuery = "";
    Turno oT;
    Puesto oP;
    int i=0, nTam = 0;
        sQuery =" SELECT * FROM buscaTodasJefasEnfermeria() ";
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
            vObj = new Vector<PersonalHospitalario>();
            for (i = 0; i < rst.size(); i++) {
                oPH = new PersonalHospitalario();
                Vector vRowTemp = (Vector)rst.elementAt(i);
                /*  ph.nfoliopers, ph.dnacimiento, ph.snombre, ph.sappaterno, ph.sapmaterno, ph.srfc, ph.scurp, 
                    ph.bactivo, ph.scveturno, ph.scvepuesto  */
                oPH.setFolioPers(((Double) vRowTemp.elementAt(0)).intValue());
                oPH.setNac( (Date) vRowTemp.elementAt(1));
                oPH.setNombre((String) vRowTemp.elementAt(2));
                oPH.setApellidoPaterno((String) vRowTemp.elementAt(3));
                oPH.setApellidoMaterno((String) vRowTemp.elementAt(4));
                oPH.setRFC((String) vRowTemp.elementAt(5));
                oPH.setCURP((String) vRowTemp.elementAt(6));
                oPH.setActivo(((String) vRowTemp.elementAt(7)).equals("1"));
                oT = new Turno();
                oT.setCve((String) vRowTemp.elementAt(8) );
                oPH.setTurno(oT);
                oP = new Puesto();
                oP.setCve((String) vRowTemp.elementAt(9));
                oPH.setPuesto(oP);
                vObj.add(oPH);
            }
            nTam = vObj.size();
            arrRet = new PersonalHospitalario [nTam];

            for (i=0; i<nTam; i++){
                arrRet[i] = vObj.elementAt(i);
            }
        }
        return arrRet;
    }
        
    public List<PersonalHospitalario> buscaEnfermeraRPT(Date dinicio, Date dfin) throws Exception{
            List<PersonalHospitalario> listRet=null;
            PersonalHospitalario oPH;

            Vector rst = null;
            String sQuery = "";

            sQuery="select * from buscaEnfermeraRPT ('"+dinicio+"','"+dfin+"')";     
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
                oPH = new PersonalHospitalario();
                Vector vRowTemp = (Vector)rst.elementAt(i);
                /*  ph.nfoliopers, ph.snombre, ph.sappaterno, ph.sapmaterno */
                oPH.setFolioPers(((Double)vRowTemp.elementAt(0)).intValue());
                oPH.setNombre((String)vRowTemp.elementAt(1));
                oPH.setApellidoPaterno((String)vRowTemp.elementAt(2));
                oPH.setApellidoMaterno((String)vRowTemp.elementAt(3));
                listRet.add(oPH);
            }
        }
        return listRet;
    }
    
    public List<PersonalHospitalario> buscarPersonalHospitalarioRPTPagoHonorarios() throws Exception{
            List<PersonalHospitalario> listRet=null;
            PersonalHospitalario oPH;

            Vector rst = null;
            String sQuery = "";

            sQuery=" SELECT DISTINCT outfoliopers, outnombreph, outappaternoph, outapmaternoph, outcvepuesto FROM buscaTodosHonorariosRPTPagoHonorarios()  ";     
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
                oPH = new PersonalHospitalario();
                Vector vRowTemp = (Vector)rst.elementAt(i);
                /*  ph.nfoliopers, ph.snombre, ph.sappaterno, ph.sapmaterno */
                oPH.setFolioPers(((Double)vRowTemp.elementAt(0)).intValue());
                oPH.setNombre((String)vRowTemp.elementAt(1));
                oPH.setApellidoPaterno((String)vRowTemp.elementAt(2));
                oPH.setApellidoMaterno((String)vRowTemp.elementAt(3));
                oPuesto = new Puesto();
                oPuesto.setCve((String)vRowTemp.elementAt(4));
                oPH.setPuesto(oPuesto);
                listRet.add(oPH);
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

    public boolean isbActivo() {
        return bActivo;
    }

    public void setActivo(boolean bActivo) {
        this.bActivo = bActivo;
    }

    public int getFolioPers() {
        return nFolioPers;
    }

    public void setFolioPers(int nFolioPers) {
        this.nFolioPers = nFolioPers;
    }

    public String getCedProf() {
        return sCedProf;
    }

    public void setCedProf(String sCedProf) {
        this.sCedProf = sCedProf;
    }

    public boolean getActivo() {
        return bActivo;
    }

    public Puesto getPuesto() {
        return oPuesto;
    }

    public void setPuesto(Puesto oPuesto) {
        this.oPuesto = oPuesto;
    }
    
     public Usuario getUsuario() {
        return oUsu;
    }

    public void setUsuario(Usuario oUsu) {
        this.oUsu= oUsu;
    }
    
    public AreaFuncionamiento getAreaFunc() {
        return oAreaFunc;
    }

    public void setAreaFunc(AreaFuncionamiento oAF) {
        this.oAreaFunc = oAF;
    }

    
    public Turno getTurno() {
        return oTurno;
    }

    public void setTurno(Turno oTurno) {
        this.oTurno = oTurno;
    }

    public AreaDeServicio getAreaServ() {
        return oAS;
    }

    public void setAreaServ(AreaDeServicio oAS) {
        this.oAS = oAS;
    }

    public Nomina getNomina() {
        return oNomina;
    }

    public void setNomina(Nomina oNomina) {
        this.oNomina = oNomina;
    }

    public List<DetalleNomina> getListConceptos() {
        return listConceptos;
    }

    public void setListConceptos(List<DetalleNomina> listConceptos) {
        this.listConceptos = listConceptos;
    }

    public float getSalario() {
        return nSalario;
    }

    public void setSalario(float nSalario) {
        this.nSalario = nSalario;
    }

    public float getPercepciones() {
        return nPercepciones;
    }

    public void setPercepciones(float nPercepciones) {
        this.nPercepciones = nPercepciones;
    }

    public float getDeducciones() {
        return nDeducciones;
    }

    public void setDeducciones(float nDeducciones) {
        this.nDeducciones = nDeducciones;
    }

    public String getCompositeKey() {
        return nFolioPers + "." + oNomina.getInicio() + "." + oNomina.getFin();
    }
    
    public boolean cambiaBoolean(char cVar){
        if (cVar=='0')
            return false;
        else 
            return true;
    }
    
    public char cambiaBoolean(boolean bVar){
        if (bVar==true)
            return '1';
        else 
            return '0';
    }

}