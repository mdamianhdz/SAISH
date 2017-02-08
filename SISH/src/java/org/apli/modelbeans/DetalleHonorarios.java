package org.apli.modelbeans;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import org.apli.AD.AccesoDatos;
import org.apli.modelbeans.ventaCredito.CompaniaCred;

/**
 *
 * @author HumbertMarin
 */
public class DetalleHonorarios implements Serializable{
    
    private PagoHonorarios oPagoHonorarios;
    private ServicioPrestado oServicioPrestado;
    private float nAutorizado;
    /**
      * Variable para el acceso a datos
      */
    protected AccesoDatos oAD = null;
    
    public DetalleHonorarios(){
        
    }
    
    /**
     * Agrega un detalle de honorarios a la base de datos
     * Regresa el número de registros afectados (insertados)
     **/
    public String insertar() throws Exception{
        Vector nRet = null;
        String sQuery = "";
        if ( this.oPagoHonorarios.getIdPagoHonorarios()==0 || this.oServicioPrestado.getIdFolio().equals("") ){
            throw new Exception("Detalle Honorarios.insertar: error de programación, faltan datos");
        }
        else {
            SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sQuery = " SELECT insertadetallehonorarios("+this.oPagoHonorarios.getIdPagoHonorarios()+",'"+this.oServicioPrestado.getIdFolio()+"',"+this.nAutorizado+")";
            System.out.println(sQuery);
            /*  Si oAD es nulo, tiene que crearlo y conectarlo, de otro modo
                supone que ya viene conectado*/
             if (getAD() == null){
                 setAD(new AccesoDatos());
                 getAD().conectar();
                 nRet = getAD().ejecutarConsulta(sQuery);
                 getAD().desconectar();
                 setAD(null);
             }else{
                 nRet = getAD().ejecutarConsulta(sQuery);
             }
        }
        return ""+nRet.get(0);
    }
    
    public List<DetalleHonorarios> buscaDetalleHonorarios() throws Exception {
        //Busca servicios de un paciente específico
        List<DetalleHonorarios> listRet=new ArrayList<DetalleHonorarios>();
        DetalleHonorarios oDetHon;

        Vector rst = null;
        String sQuery = "";

        sQuery="SELECT * FROM buscaTodosHonorariosPorAutorizar2() ";
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
                oDetHon = new DetalleHonorarios();
                Vector vRowTemp = (Vector)rst.elementAt(i);
                /*  hn.nidpagohonorarios(0), hn.nfoliopers(1), hn.dregistro(2), hn.ssituacion(3),
                    dh.nautorizado(4), hs.snombre(5), hs.sappaterno(6), hs.sapmaterno(7), p.nfoliopaciente(8),
                    p.snombre(9), p.sappaterno(10), p.sapmaterno(11), ci.ncveconcepingr(12), ci.sdescripcion(13), 
                    sp.nidfolio(14), sp.cveepisodio(15), sp.bfacturable(16), sp.drealizado(17), sp.dregistro(18),
                    sp.ncostooriginal(19), sp.npctiva(20), sp.nquienpaga(21), sp.scvematerial(22), sp.scvemedicamento(23),
                    sp.nfoliopers(24), sp.ncveunimed(25), sp.sobservaciones(26), sp.sprocedimiento(27), sp.sreceta(28),
                    sp.ssituacion(29), sp.ncantidad(30), sp.ncveexfis(31), sp.ncostocobrado(32), sp.nidemp(33),
                    sp.dentradaalserv(34), sp.sdiagaux(35) */
                oServicioPrestado = new ServicioPrestado();
                
                oPagoHonorarios = new PagoHonorarios();
                oPagoHonorarios.setIdPagoHonorarios(((Double) vRowTemp.elementAt(0)).intValue());
                oPagoHonorarios.setRegistro((Date) vRowTemp.elementAt(2));
                oPagoHonorarios.setSituacion((String) vRowTemp.elementAt(3));
                
                PersonalHospitalario oPH = new PersonalHospitalario();
                oPH.setFolioPers(((Double) vRowTemp.elementAt(1)).intValue());
                oPH.setNombre((String) vRowTemp.elementAt(5));
                oPH.setApellidoPaterno((String) vRowTemp.elementAt(6));
                oPH.setApellidoMaterno((String) vRowTemp.elementAt(7));
                
                oPagoHonorarios.setPersonalHospitalario(oPH);
                
                Paciente oPaciente= new Paciente();
                oPaciente.setFolioPac(((Double)vRowTemp.elementAt(8)).intValue());
                oPaciente.setNombre((String) vRowTemp.elementAt(9));
                oPaciente.setApellidoPaterno((String) vRowTemp.elementAt(10));
                oPaciente.setApellidoMaterno((String) vRowTemp.elementAt(11));
                oServicioPrestado.setPaciente(oPaciente);
                
                ConceptoIngreso oCP = new ConceptoIngreso();
                oCP.setCveConcep(((Double) vRowTemp.elementAt(12)).intValue());
                oCP.setDescripConcep((String) vRowTemp.elementAt(13));
                
                oServicioPrestado.setIdFolio((String) vRowTemp.elementAt(14));
                oServicioPrestado.setConcepPrestado(oCP);
                oServicioPrestado.setFacturable(((String) vRowTemp.elementAt(16)).equals("1"));
                oServicioPrestado.setRealizado((Date) vRowTemp.elementAt(17));
                oServicioPrestado.setRegistro((Date) vRowTemp.elementAt(18));
                //Por cuestiones de presentacion grafica y para no anexar otro campo que no
                // se utilizaria en monto autorizado lo guardo en costo original
                oServicioPrestado.setCostoOriginal(((Double) vRowTemp.elementAt(4)).floatValue());
                oServicioPrestado.setPctIVA(((Double) vRowTemp.elementAt(20)).floatValue());
                oServicioPrestado.setQuienPaga(((Double) vRowTemp.elementAt(21)).intValue());
                Medico medTrat= new Medico();
                medTrat.setFolioPers(((Double) vRowTemp.elementAt(24)).intValue());
                oServicioPrestado.setMedico(medTrat);
                UnidadMedida oUniMed = new UnidadMedida();
                oUniMed.setCve((String) vRowTemp.elementAt(25));
                oServicioPrestado.setUniMed(oUniMed);
                oServicioPrestado.setObs((String) vRowTemp.elementAt(26));
                oServicioPrestado.setProcedimiento((String) vRowTemp.elementAt(27));
                oServicioPrestado.setReceta((String) vRowTemp.elementAt(28));
                oServicioPrestado.setSituacion((String) vRowTemp.elementAt(29));
                oServicioPrestado.setCantidad(((Double) vRowTemp.elementAt(30)).intValue());
                ExamenFisico oEF= new ExamenFisico();
                oEF.setCveExFis(((Double) vRowTemp.elementAt(31)).intValue());
                oServicioPrestado.setExamenFisico(oEF);
                oServicioPrestado.setCostoCobrado(((Double) vRowTemp.elementAt(32)).floatValue());
                
                CompaniaCred oCompaniaCred= new CompaniaCred();
                oCompaniaCred.setIdEmp(((Double) vRowTemp.elementAt(33)).intValue());
                oServicioPrestado.setCompaniaCred(oCompaniaCred);
                
                oDetHon.setPagoHonorarios(oPagoHonorarios);
                oDetHon.setServicioPrestado(oServicioPrestado);
                oDetHon.setAutorizado(((Double) vRowTemp.elementAt(4)).floatValue());
               
                listRet.add(oDetHon);
            }
        }
        return listRet;
    }
    
    public String modificaMontoAutorizadoDetalleHonorarios() throws Exception{
            Vector rst = null;
            String sQuery = "";
        
            if (this.oPagoHonorarios.getIdPagoHonorarios()==0 && this.oServicioPrestado.getIdFolio()==null && this.nAutorizado<0 ) {
                throw new Exception("DetalleHonorarios.modificaMontoAutorizadoDetalleHonorarios: error de programación, faltan datos");
            }else {
                sQuery="SELECT * FROM modificaMontoAutorizadoDetalleHonorarios("+this.oPagoHonorarios.getIdPagoHonorarios()+",'"+this.oServicioPrestado.getIdFolio()+"',"+this.nAutorizado+")";
                System.out.println(sQuery);
                if (getAD() == null){
                    setAD(new AccesoDatos());
                    getAD().conectar();
                    rst = getAD().ejecutarConsulta(sQuery);
                    getAD().desconectar();
                    setAD(null);
                }  
            }
            return " "+rst.get(0);
     }
    
     
    public List<DetalleHonorarios> buscaTodosRPTPagoHonorarios() throws Exception {
        //Busca servicios de un paciente específico
        List<DetalleHonorarios> listRet=new ArrayList<DetalleHonorarios>();
        DetalleHonorarios oDetHon;

        Vector rst = null;
        String sQuery = "";

        sQuery="SELECT * FROM buscaTodosHonorariosRPTPagoHonorarios() ";
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
                oDetHon = new DetalleHonorarios();
                Vector vRowTemp = (Vector)rst.elementAt(i);
                /*  hn.nidpagohonorarios(0), hn.nfoliopers(1), hn.dregistro(2), hn.ssituacion(3), dh.nautorizado(4), 
                    ph.snombre(5), ph.sappaterno(6), ph.sapmaterno(7), ph.scvepuesto(8),
                    p.nfoliopaciente(9), p.snombre(10), p.sappaterno(11), p.sapmaterno(12), p.stipopac(13),
                    ci.ncveconcepingr(14), ci.sdescripcion(15), 
                    sp.nidfolio(16), sp.cveepisodio(17), sp.bfacturable(18), sp.drealizado(19), sp.dregistro(20), 
                    sp.ncostooriginal(21), sp.npctiva(22), sp.nquienpaga(23), sp.scvematerial(24), sp.scvemedicamento(), 
                    sp.nfoliopers(), sp.ncveunimed(), sp.sobservaciones(), sp.sprocedimiento(), sp.sreceta(),
                    sp.ssituacion(), sp.ncantidad(), sp.ncveexfis(), sp.ncostocobrado(), sp.nidemp(), 
                    sp.dentradaalserv(), sp.sdiagaux()  */
                oPagoHonorarios = new PagoHonorarios();
                oServicioPrestado = new ServicioPrestado();
                
                oPagoHonorarios.setIdPagoHonorarios(((Double) vRowTemp.elementAt(0)).intValue());
                oPagoHonorarios.setRegistro((Date) vRowTemp.elementAt(2));
                oPagoHonorarios.setSituacion((String) vRowTemp.elementAt(3));
                
                PersonalHospitalario oPH = new PersonalHospitalario();
                oPH.setFolioPers(((Double) vRowTemp.elementAt(1)).intValue());
                oPH.setNombre((String) vRowTemp.elementAt(5));
                oPH.setApellidoPaterno((String) vRowTemp.elementAt(6));
                oPH.setApellidoMaterno((String) vRowTemp.elementAt(7));
                Puesto oPuesto = new Puesto();
                oPuesto.setCve((String) vRowTemp.elementAt(8));
                oPH.setPuesto(oPuesto);
                
                oPagoHonorarios.setPersonalHospitalario(oPH);
                
                Paciente oPaciente= new Paciente();
                oPaciente.setFolioPac(((Double)vRowTemp.elementAt(9)).intValue());
                oPaciente.setNombre((String) vRowTemp.elementAt(10));
                oPaciente.setApellidoPaterno((String) vRowTemp.elementAt(11));
                oPaciente.setApellidoMaterno((String) vRowTemp.elementAt(12));
                oPaciente.setTipo((String) vRowTemp.elementAt(13));
                oServicioPrestado.setPaciente(oPaciente);
                
                ConceptoIngreso oCP = new ConceptoIngreso();
                oCP.setCveConcep(((Double) vRowTemp.elementAt(14)).intValue());
                oCP.setDescripConcep((String) vRowTemp.elementAt(15));
                
                /*  sp.nidfolio(16), sp.cveepisodio(17), em.nnumhab(18) sp.bfacturable(19), sp.drealizado(20),
                    sp.dregistro(21), sp.ncostooriginal(22), sp.npctiva(23), sp.nquienpaga(24), sp.scvematerial(25),
                    sp.scvemedicamento(26), sp.nfoliopers(27), sp.ncveunimed(28), sp.sobservaciones(29), sp.sprocedimiento(30),
                    sp.sreceta(31), sp.ssituacion(32), sp.ncantidad(33), sp.ncveexfis(34), sp.ncostocobrado(35), sp.nidemp(36), 
                    sp.dentradaalserv(37), sp.sdiagaux(38)*/
                oServicioPrestado.setIdFolio((String) vRowTemp.elementAt(16));
                EpisodioMedico oEpMed = new EpisodioMedico();
                oEpMed.setCveepisodio(((Double) vRowTemp.elementAt(17)).intValue());
                Habitacion oHab= new Habitacion();
                oHab.setHab(((Double) vRowTemp.elementAt(18)).intValue());
                oEpMed.setHab(oHab);
                oServicioPrestado.setEpisodioMedico(oEpMed);
                oServicioPrestado.setConcepPrestado(oCP);
                oServicioPrestado.setFacturable(((String) vRowTemp.elementAt(19)).equals("1"));
                oServicioPrestado.setRealizado((Date) vRowTemp.elementAt(20));
                oServicioPrestado.setRegistro((Date) vRowTemp.elementAt(21));
                oServicioPrestado.setCostoOriginal(((Double) vRowTemp.elementAt(22)).floatValue());
                oServicioPrestado.setPctIVA(((Double) vRowTemp.elementAt(23)).floatValue());
                oServicioPrestado.setQuienPaga(((Double) vRowTemp.elementAt(24)).intValue());
                Medico medTrat= new Medico();
                medTrat.setFolioPers(((Double) vRowTemp.elementAt(27)).intValue());
                oServicioPrestado.setMedico(medTrat);
                UnidadMedida oUniMed = new UnidadMedida();
                oUniMed.setCve((String) vRowTemp.elementAt(28));
                oServicioPrestado.setUniMed(oUniMed);
                oServicioPrestado.setObs((String) vRowTemp.elementAt(29));
                oServicioPrestado.setProcedimiento((String) vRowTemp.elementAt(30));
                oServicioPrestado.setReceta((String) vRowTemp.elementAt(31));
                oServicioPrestado.setSituacion((String) vRowTemp.elementAt(32));
                oServicioPrestado.setCantidad(((Double) vRowTemp.elementAt(33)).intValue());
                ExamenFisico oEF= new ExamenFisico();
                oEF.setCveExFis(((Double) vRowTemp.elementAt(34)).intValue());
                oServicioPrestado.setExamenFisico(oEF);
                oServicioPrestado.setCostoCobrado(((Double) vRowTemp.elementAt(35)).floatValue());
                CompaniaCred oCompaniaCred= new CompaniaCred();
                oCompaniaCred.setIdEmp(((Double) vRowTemp.elementAt(36)).intValue());
                oServicioPrestado.setCompaniaCred(oCompaniaCred);
                
                oDetHon.setPagoHonorarios(oPagoHonorarios);
                oDetHon.setServicioPrestado(oServicioPrestado);
                oDetHon.setAutorizado(((Double) vRowTemp.elementAt(4)).floatValue());
               
                listRet.add(oDetHon);
            }
        }
        return listRet;
    }
    
    public List<DetalleHonorarios> buscaTodosRPTPagoHonorariosPorPersHosp(int folioPers) throws Exception {
        //Busca servicios de un paciente específico
        List<DetalleHonorarios> listRet=new ArrayList<DetalleHonorarios>();
        DetalleHonorarios oDetHon;

        Vector rst = null;
        String sQuery = "";

        sQuery="SELECT * FROM buscaTodosHonorariosRPTPagoHonorarios() WHERE outfoliopers="+folioPers;
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
                oDetHon = new DetalleHonorarios();
                Vector vRowTemp = (Vector)rst.elementAt(i);
                /*  hn.nidpagohonorarios(0), hn.nfoliopers(1), hn.dregistro(2), hn.ssituacion(3), dh.nautorizado(4), 
                    ph.snombre(5), ph.sappaterno(6), ph.sapmaterno(7), ph.scvepuesto(8),
                    p.nfoliopaciente(9), p.snombre(10), p.sappaterno(11), p.sapmaterno(12), p.stipopac(13),
                    ci.ncveconcepingr(14), ci.sdescripcion(15), 
                    sp.nidfolio(16), sp.cveepisodio(17), sp.bfacturable(18), sp.drealizado(19), sp.dregistro(20), 
                    sp.ncostooriginal(21), sp.npctiva(22), sp.nquienpaga(23), sp.scvematerial(24), sp.scvemedicamento(), 
                    sp.nfoliopers(), sp.ncveunimed(), sp.sobservaciones(), sp.sprocedimiento(), sp.sreceta(),
                    sp.ssituacion(), sp.ncantidad(), sp.ncveexfis(), sp.ncostocobrado(), sp.nidemp(), 
                    sp.dentradaalserv(), sp.sdiagaux()  */
                oServicioPrestado = new ServicioPrestado();
               
                oPagoHonorarios = new PagoHonorarios();
                oPagoHonorarios.setIdPagoHonorarios(((Double) vRowTemp.elementAt(0)).intValue());
                oPagoHonorarios.setRegistro((Date) vRowTemp.elementAt(2));
                oPagoHonorarios.setSituacion((String) vRowTemp.elementAt(3));
                
                PersonalHospitalario oPH = new PersonalHospitalario();
                oPH.setFolioPers(((Double) vRowTemp.elementAt(1)).intValue());
                oPH.setNombre((String) vRowTemp.elementAt(5));
                oPH.setApellidoPaterno((String) vRowTemp.elementAt(6));
                oPH.setApellidoMaterno((String) vRowTemp.elementAt(7));
                Puesto oPuesto = new Puesto();
                oPuesto.setCve((String) vRowTemp.elementAt(8));
                oPH.setPuesto(oPuesto);
                
                oPagoHonorarios.setPersonalHospitalario(oPH);
                
                Paciente oPaciente= new Paciente();
                oPaciente.setFolioPac(((Double)vRowTemp.elementAt(9)).intValue());
                oPaciente.setNombre((String) vRowTemp.elementAt(10));
                oPaciente.setApellidoPaterno((String) vRowTemp.elementAt(11));
                oPaciente.setApellidoMaterno((String) vRowTemp.elementAt(12));
                oPaciente.setTipo((String) vRowTemp.elementAt(13));
                oServicioPrestado.setPaciente(oPaciente);
                
                ConceptoIngreso oCP = new ConceptoIngreso();
                oCP.setCveConcep(((Double) vRowTemp.elementAt(14)).intValue());
                oCP.setDescripConcep((String) vRowTemp.elementAt(15));
                
                /*  sp.nidfolio(16), sp.cveepisodio(17), em.nnumhab(18) sp.bfacturable(19), sp.drealizado(20),
                    sp.dregistro(21), sp.ncostooriginal(22), sp.npctiva(23), sp.nquienpaga(24), sp.scvematerial(25),
                    sp.scvemedicamento(26), sp.nfoliopers(27), sp.ncveunimed(28), sp.sobservaciones(29), sp.sprocedimiento(30),
                    sp.sreceta(31), sp.ssituacion(32), sp.ncantidad(33), sp.ncveexfis(34), sp.ncostocobrado(35), sp.nidemp(36), 
                    sp.dentradaalserv(37), sp.sdiagaux(38)*/
                oServicioPrestado.setIdFolio((String) vRowTemp.elementAt(16));
                EpisodioMedico oEpMed = new EpisodioMedico();
                oEpMed.setCveepisodio(((Double) vRowTemp.elementAt(17)).intValue());
                Habitacion oHab= new Habitacion();
                oHab.setHab(((Double) vRowTemp.elementAt(18)).intValue());
                oEpMed.setHab(oHab);
                oServicioPrestado.setEpisodioMedico(oEpMed);
                oServicioPrestado.setConcepPrestado(oCP);
                oServicioPrestado.setFacturable(((String) vRowTemp.elementAt(19)).equals("1"));
                oServicioPrestado.setRealizado((Date) vRowTemp.elementAt(20));
                oServicioPrestado.setRegistro((Date) vRowTemp.elementAt(21));
                oServicioPrestado.setCostoOriginal(((Double) vRowTemp.elementAt(22)).floatValue());
                oServicioPrestado.setPctIVA(((Double) vRowTemp.elementAt(23)).floatValue());
                oServicioPrestado.setQuienPaga(((Double) vRowTemp.elementAt(24)).intValue());
                Medico medTrat= new Medico();
                medTrat.setFolioPers(((Double) vRowTemp.elementAt(27)).intValue());
                oServicioPrestado.setMedico(medTrat);
                UnidadMedida oUniMed = new UnidadMedida();
                oUniMed.setCve((String) vRowTemp.elementAt(28));
                oServicioPrestado.setUniMed(oUniMed);
                oServicioPrestado.setObs((String) vRowTemp.elementAt(29));
                oServicioPrestado.setProcedimiento((String) vRowTemp.elementAt(30));
                oServicioPrestado.setReceta((String) vRowTemp.elementAt(31));
                oServicioPrestado.setSituacion((String) vRowTemp.elementAt(32));
                oServicioPrestado.setCantidad(((Double) vRowTemp.elementAt(33)).intValue());
                ExamenFisico oEF= new ExamenFisico();
                oEF.setCveExFis(((Double) vRowTemp.elementAt(34)).intValue());
                oServicioPrestado.setExamenFisico(oEF);
                oServicioPrestado.setCostoCobrado(((Double) vRowTemp.elementAt(35)).floatValue());
                CompaniaCred oCompaniaCred= new CompaniaCred();
                oCompaniaCred.setIdEmp(((Double) vRowTemp.elementAt(36)).intValue());
                oServicioPrestado.setCompaniaCred(oCompaniaCred);
                
                oDetHon.setPagoHonorarios(oPagoHonorarios);
                oDetHon.setServicioPrestado(oServicioPrestado);
                oDetHon.setAutorizado(((Double) vRowTemp.elementAt(4)).floatValue());
               
                listRet.add(oDetHon);
            }
        }
        return listRet;
    }
    
    public List<DetalleHonorarios> buscaTodosRPTPagoHonorariosPorPaciente(int folioPac) throws Exception {
        //Busca servicios de un paciente específico
        List<DetalleHonorarios> listRet=new ArrayList<DetalleHonorarios>();
        DetalleHonorarios oDetHon;

        Vector rst = null;
        String sQuery = "";

        sQuery="SELECT * FROM buscaTodosHonorariosRPTPagoHonorarios() WHERE outfoliopac="+folioPac;
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
                oDetHon = new DetalleHonorarios();
                Vector vRowTemp = (Vector)rst.elementAt(i);
                /*  hn.nidpagohonorarios(0), hn.nfoliopers(1), hn.dregistro(2), hn.ssituacion(3), dh.nautorizado(4), 
                    ph.snombre(5), ph.sappaterno(6), ph.sapmaterno(7), ph.scvepuesto(8),
                    p.nfoliopaciente(9), p.snombre(10), p.sappaterno(11), p.sapmaterno(12), p.stipopac(13),
                    ci.ncveconcepingr(14), ci.sdescripcion(15), 
                    sp.nidfolio(16), sp.cveepisodio(17), sp.bfacturable(18), sp.drealizado(19), sp.dregistro(20), 
                    sp.ncostooriginal(21), sp.npctiva(22), sp.nquienpaga(23), sp.scvematerial(24), sp.scvemedicamento(), 
                    sp.nfoliopers(), sp.ncveunimed(), sp.sobservaciones(), sp.sprocedimiento(), sp.sreceta(),
                    sp.ssituacion(), sp.ncantidad(), sp.ncveexfis(), sp.ncostocobrado(), sp.nidemp(), 
                    sp.dentradaalserv(), sp.sdiagaux()  */
                oServicioPrestado = new ServicioPrestado();
                oPagoHonorarios = new PagoHonorarios();
                oPagoHonorarios.setIdPagoHonorarios(((Double) vRowTemp.elementAt(0)).intValue());
                oPagoHonorarios.setRegistro((Date) vRowTemp.elementAt(2));
                oPagoHonorarios.setSituacion((String) vRowTemp.elementAt(3));
                
                PersonalHospitalario oPH = new PersonalHospitalario();
                oPH.setFolioPers(((Double) vRowTemp.elementAt(1)).intValue());
                oPH.setNombre((String) vRowTemp.elementAt(5));
                oPH.setApellidoPaterno((String) vRowTemp.elementAt(6));
                oPH.setApellidoMaterno((String) vRowTemp.elementAt(7));
                Puesto oPuesto = new Puesto();
                oPuesto.setCve((String) vRowTemp.elementAt(8));
                oPH.setPuesto(oPuesto);
                
                oPagoHonorarios.setPersonalHospitalario(oPH);
                
                Paciente oPaciente= new Paciente();
                oPaciente.setFolioPac(((Double)vRowTemp.elementAt(9)).intValue());
                oPaciente.setNombre((String) vRowTemp.elementAt(10));
                oPaciente.setApellidoPaterno((String) vRowTemp.elementAt(11));
                oPaciente.setApellidoMaterno((String) vRowTemp.elementAt(12));
                oPaciente.setTipo((String) vRowTemp.elementAt(13));
                oServicioPrestado.setPaciente(oPaciente);
                
                ConceptoIngreso oCP = new ConceptoIngreso();
                oCP.setCveConcep(((Double) vRowTemp.elementAt(14)).intValue());
                oCP.setDescripConcep((String) vRowTemp.elementAt(15));
                
                /*  sp.nidfolio(16), sp.cveepisodio(17), em.nnumhab(18) sp.bfacturable(19), sp.drealizado(20),
                    sp.dregistro(21), sp.ncostooriginal(22), sp.npctiva(23), sp.nquienpaga(24), sp.scvematerial(25),
                    sp.scvemedicamento(26), sp.nfoliopers(27), sp.ncveunimed(28), sp.sobservaciones(29), sp.sprocedimiento(30),
                    sp.sreceta(31), sp.ssituacion(32), sp.ncantidad(33), sp.ncveexfis(34), sp.ncostocobrado(35), sp.nidemp(36), 
                    sp.dentradaalserv(37), sp.sdiagaux(38)*/
                oServicioPrestado.setIdFolio((String) vRowTemp.elementAt(16));
                EpisodioMedico oEpMed = new EpisodioMedico();
                oEpMed.setCveepisodio(((Double) vRowTemp.elementAt(17)).intValue());
                Habitacion oHab= new Habitacion();
                oHab.setHab(((Double) vRowTemp.elementAt(18)).intValue());
                oEpMed.setHab(oHab);
                oServicioPrestado.setEpisodioMedico(oEpMed);
                oServicioPrestado.setConcepPrestado(oCP);
                oServicioPrestado.setFacturable(((String) vRowTemp.elementAt(19)).equals("1"));
                oServicioPrestado.setRealizado((Date) vRowTemp.elementAt(20));
                oServicioPrestado.setRegistro((Date) vRowTemp.elementAt(21));
                oServicioPrestado.setCostoOriginal(((Double) vRowTemp.elementAt(22)).floatValue());
                oServicioPrestado.setPctIVA(((Double) vRowTemp.elementAt(23)).floatValue());
                oServicioPrestado.setQuienPaga(((Double) vRowTemp.elementAt(24)).intValue());
                Medico medTrat= new Medico();
                medTrat.setFolioPers(((Double) vRowTemp.elementAt(27)).intValue());
                oServicioPrestado.setMedico(medTrat);
                UnidadMedida oUniMed = new UnidadMedida();
                oUniMed.setCve((String) vRowTemp.elementAt(28));
                oServicioPrestado.setUniMed(oUniMed);
                oServicioPrestado.setObs((String) vRowTemp.elementAt(29));
                oServicioPrestado.setProcedimiento((String) vRowTemp.elementAt(30));
                oServicioPrestado.setReceta((String) vRowTemp.elementAt(31));
                oServicioPrestado.setSituacion((String) vRowTemp.elementAt(32));
                oServicioPrestado.setCantidad(((Double) vRowTemp.elementAt(33)).intValue());
                ExamenFisico oEF= new ExamenFisico();
                oEF.setCveExFis(((Double) vRowTemp.elementAt(34)).intValue());
                oServicioPrestado.setExamenFisico(oEF);
                oServicioPrestado.setCostoCobrado(((Double) vRowTemp.elementAt(35)).floatValue());
                CompaniaCred oCompaniaCred= new CompaniaCred();
                oCompaniaCred.setIdEmp(((Double) vRowTemp.elementAt(36)).intValue());
                oServicioPrestado.setCompaniaCred(oCompaniaCred);
                
                oDetHon.setPagoHonorarios(oPagoHonorarios);
                oDetHon.setServicioPrestado(oServicioPrestado);
                oDetHon.setAutorizado(((Double) vRowTemp.elementAt(4)).floatValue());
               
                listRet.add(oDetHon);
            }
        }
        return listRet;
    }
    
     

    //=============== SET & GET ===============//
    
    public PagoHonorarios getPagoHonorarios() {
        return oPagoHonorarios;
    }
    public void setPagoHonorarios(PagoHonorarios oPagoHonorarios) {
        this.oPagoHonorarios = oPagoHonorarios;
    }

    public ServicioPrestado getServicioPrestado() {
        return oServicioPrestado;
    }
    public void setServicioPrestado(ServicioPrestado oSP) {
        this.oServicioPrestado = oSP;
    }

    public float getAutorizado() {
        return nAutorizado;
    }
    public void setAutorizado(float nAutorizado) {
        this.nAutorizado = nAutorizado;
    }
   
    public AccesoDatos getAD() {
        return oAD;
    }
    public void setAD(AccesoDatos oAD) {
        this.oAD = oAD;
    }
}