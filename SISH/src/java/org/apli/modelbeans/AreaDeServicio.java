package org.apli.modelbeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import org.apli.AD.AccesoDatos;

/**
 * Representa las �reas del hospital que ofrecen servicios con ingresos
 * rastreables (auditables)
 *
 * @author BAOZ
 * @version 1.0
 * @updated 14-mar-2014 11:40:50 a.m.
 */
public class AreaDeServicio implements Serializable {

    /**
     * Indica si el �rea de servicio (laboratorio, Rayos X, etc.) maneja agenda
     * de citas o no
     */
    public static final String TodasAreas = "T";
    public static final String AreaServicioPrestado = "CE";
    public static final String AreaServicioCargoCuenta = "Cta";
    public static final String AreaServicioQx = "Qx";
    public static final String AreaServicioCaja = "Caja";
    private boolean bAgenda;
    private boolean bauxdiag;
    private boolean bcargos;
    /**
     * Clave del �rea de servicio
     */
    private String sCve;
    /**
     * Descripci�n del �rea de servicio
     */
    private String sDescrip;
    /**
     * Texto que representa el horario de atenci�n del servicio
     */
    private String sHorario;
    //public Concepto m_Concepto;
    protected AccesoDatos oAD = null;

    public AreaDeServicio() {
    }

    public List<AreaDeServicio> todasAreasServicios(String cTipo) throws Exception {
        AreaDeServicio arrP[] = null, oA = null;
        Vector rst = null;
        Vector<AreaDeServicio> vObj = null;
        List<AreaDeServicio> listAreas = new ArrayList();

        String sQuery = "";
        int i = 0, nTam = 0;

        sQuery = "select * from buscatodasareasservicio('" + cTipo + "')";

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
            vObj = new Vector<AreaDeServicio>();
            for (i = 0; i < rst.size(); i++) {
                oA = new AreaDeServicio();
                Vector vRowTemp = (Vector) rst.elementAt(i);

                oA.setCve("" + ((Double) vRowTemp.elementAt(0)).intValue());
                oA.setDescrip((String) vRowTemp.elementAt(1));
                //oA.setAgenda((Boolean) vRowTemp.elementAt(2));
                oA.setHorario((String) vRowTemp.elementAt(3));
//              oA.setAuxdiagiag((Boolean) vRowTemp.elementAt(4));
//                oA.setCargos((Boolean) vRowTemp.elementAt(5));
                listAreas.add(oA);
            }
            nTam = vObj.size();
            arrP = new AreaDeServicio[nTam];

            for (i = 0; i < nTam; i++) {
                arrP[i] = vObj.elementAt(i);
            }
        }
        return listAreas;
    }

    public List<AreaDeServicio> todasAreasServiciosPorUsuario(String sUsuario) throws Exception {
        AreaDeServicio arrP[] = null, oA = null;
        Vector rst = null;
        Vector<AreaDeServicio> vObj = null;
        List<AreaDeServicio> listAreas = new ArrayList();

        String sQuery = "";
        int i = 0, nTam = 0;

        sQuery = "select * from buscatodasareasservicioPorUsuario('" + sUsuario + "');";

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
            vObj = new Vector<AreaDeServicio>();
            for (i = 0; i < rst.size(); i++) {
                oA = new AreaDeServicio();
                Vector vRowTemp = (Vector) rst.elementAt(i);

                oA.setCve("" + ((Double) vRowTemp.elementAt(0)).intValue());
                oA.setDescrip((String) vRowTemp.elementAt(1));
                //oA.setAgenda((Boolean) vRowTemp.elementAt(2));
                oA.setHorario((String) vRowTemp.elementAt(3));
                oA.setAuxdiag(vRowTemp.elementAt(4).equals("1"));
//                oA.setCargos((Boolean) vRowTemp.elementAt(5));
                listAreas.add(oA);
            }
            nTam = vObj.size();
            arrP = new AreaDeServicio[nTam];

            for (i = 0; i < nTam; i++) {
                arrP[i] = vObj.elementAt(i);
            }
        }
        return listAreas;
    }

    /**
     * Busca la lista de todas las areas de servicio registradas que permiten 
     * usar agendas, regresa un arreglo de áreas
     *
     */
    public AreaDeServicio[] buscarTodasAreasDeServicioAgenda() throws Exception {
        AreaDeServicio[] arrRet = null;
        AreaDeServicio oAreaS = null;
        Vector rst = null;
        Vector<AreaDeServicio> vObj = null;
        String sQuery = "";
        int i = 0, nTam = 0;

        sQuery = " SELECT * FROM buscaTodosAreaServicioAgenda()";
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
            vObj = new Vector<AreaDeServicio>();
            for (i = 0; i < rst.size(); i++) {
                oAreaS = new AreaDeServicio();
                Vector vRowTemp = (Vector) rst.elementAt(i);
                oAreaS.setCve("" + ((Double) vRowTemp.elementAt(0)).intValue());
                oAreaS.setDescrip((String) vRowTemp.elementAt(1));
                String ag = "" + vRowTemp.elementAt(2);
                if (ag.equals("0")) {
                    oAreaS.setAgenda(false);
                } else {
                    oAreaS.setAgenda(true);
                }
                oAreaS.setHorario((String) vRowTemp.elementAt(3));
                vObj.add(oAreaS);
            }
            nTam = vObj.size();
            arrRet = new AreaDeServicio[nTam];

            for (i = 0; i < nTam; i++) {
                arrRet[i] = vObj.elementAt(i);
            }
        }
        return arrRet;
    }
    
    public List<AreaDeServicio> buscaAreasServicio()throws Exception{
        List<AreaDeServicio> listRet=null;
        AreaDeServicio oArea;
        
        Vector rst = null;
        String sQuery = "";

        sQuery = "select * from buscaareasservicio();";
        System.out.println(sQuery);
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
            listRet=new ArrayList();
            for (int i = 0; i < rst.size(); i++) {
                oArea = new AreaDeServicio();
                Vector vRowTemp = (Vector)rst.elementAt(i);
                oArea.setCve(""+((Double) vRowTemp.elementAt(0)).intValue());
                oArea.setDescrip((String) vRowTemp.elementAt(1));               
                listRet.add(oArea);
            }
        }
        return listRet;
    }
    
    public List<AreaDeServicio> buscaAreasSConceptoEgreso(int nCveConceptoEgreso) throws Exception{
        List<AreaDeServicio> listRet=null;
        AreaDeServicio oAS;
        
        Vector rst = null;
        String sQuery = "";

        sQuery="select * from buscaAreasSConceptoEgreso("+nCveConceptoEgreso+")";     
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
                oAS=new AreaDeServicio();
                Vector vRowTemp = (Vector)rst.elementAt(i);
                oAS.setCve(""+((Double)vRowTemp.elementAt(0)).intValue());
                oAS.setDescrip((String)vRowTemp.elementAt(1));
                listRet.add(oAS);
            }
        }
        return listRet; 
    }

    
      public List<AreaDeServicio> buscaTarget(int nFolio) throws Exception {
        AreaDeServicio oA = null;
        Vector rst = null;
        Vector<AreaDeServicio> vObj = null;
        List<AreaDeServicio> listAreas = new ArrayList();

        String sQuery = "";
        int i = 0;

        sQuery = "select * from areaservicio AS ars,personalareaserv AS pas "
                + "where ars.ncveareaserv=pas.ncveareaserv and pas.nfoliopers='"+nFolio+"'"
                + "order by ars.sdescripcion";
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
            vObj = new Vector<AreaDeServicio>();
            for (i = 0; i < rst.size(); i++) {
                oA = new AreaDeServicio();
                Vector vRowTemp = (Vector) rst.elementAt(i);
                oA.setCve("" + ((Double) vRowTemp.elementAt(0)).intValue());
                oA.setDescrip((String) vRowTemp.elementAt(1));
                oA.setHorario((String) vRowTemp.elementAt(3));
                listAreas.add(oA);
            }
        }
        return listAreas;
    }
      
    public List<AreaDeServicio> buscaSource(int nFolio) throws Exception {
        AreaDeServicio oA = null;
        Vector rst = null;
        Vector<AreaDeServicio> vObj = null;
        List<AreaDeServicio> listAreas = new ArrayList();

        String sQuery = "";
        int i = 0;

        sQuery = "select * from areaservicio where ncveareaserv not in"
                + "(select ncveareaserv from personalareaserv where nfoliopers='"+nFolio+"') "
                + "order by sdescripcion";
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
            vObj = new Vector<AreaDeServicio>();
            for (i = 0; i < rst.size(); i++) {
                oA = new AreaDeServicio();
                Vector vRowTemp = (Vector) rst.elementAt(i);
                oA.setCve("" +((Double) vRowTemp.elementAt(0)).intValue());
                oA.setDescrip((String) vRowTemp.elementAt(1));
                oA.setHorario((String) vRowTemp.elementAt(3));
                listAreas.add(oA);
            }
        }
        return listAreas;
    }

    public boolean getAgenda() {
        return bAgenda;
    }

    public void setAgenda(boolean bAgenda) {
        this.bAgenda = bAgenda;
    }

    public String getCve() {
        return sCve;
    }

    public void setCve(String sCve) {
        this.sCve = sCve;
    }

    public String getDescrip() {
        return sDescrip;
    }

    public void setDescrip(String sDescrip) {
        this.sDescrip = sDescrip;
    }

    public String getHorario() {
        return sHorario;
    }

    public void setHorario(String sHorario) {
        this.sHorario = sHorario;
    }

    public AccesoDatos getAD() {
        return oAD;
    }

    public void setAD(AccesoDatos oAD) {
        this.oAD = oAD;
    }

    public boolean getAuxdiag() {
        return bauxdiag;
    }

    public void setAuxdiag(boolean bauxdiag) {
        this.bauxdiag = bauxdiag;
    }

    public boolean getCargos() {
        return bcargos;
    }

    public void setCargos(boolean bcargos) {
        this.bcargos = bcargos;
    }

    @Override
    public String toString() {
        return "AreaDeServicio{" + "bAgenda=" + bAgenda + ", bauxdiag=" + bauxdiag + ", bcargos=" + bcargos + ", sCve=" + sCve + ", sDescrip=" + sDescrip + ", sHorario=" + sHorario + ", oAD=" + oAD + '}';
}
    
}