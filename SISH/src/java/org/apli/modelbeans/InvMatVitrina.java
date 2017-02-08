package org.apli.modelbeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import org.apli.AD.AccesoDatos;

/**
 *
 * @author MiguelAngel
 */
public class InvMatVitrina implements Serializable {

    private Vitrina vitrina;
    private MaterialCuracion materialCuracion;
    private int nCantidad;
    private String sLugar;
    protected AccesoDatos oAD = null;

    public InvMatVitrina() {
        vitrina=new Vitrina();
        materialCuracion=new MaterialCuracion();
    }

    public InvMatVitrina(AccesoDatos poAD) {
        oAD = poAD;
    }

    public InvMatVitrina[] buscaTodosPorVitrina() throws Exception {
        InvMatVitrina arrRet[] = null, oInv = null;
        Vector rst = null;
        Vector<InvMatVitrina> vObj = null;
        String sQuery = "";
        int i = 0, nTam = 0;

        if (this.vitrina.getCveVitrina() == 0) {
            throw new Exception("InvMatVitrina.buscaTodosPorVitrina: error de programación, faltan datos");
        } else {
            sQuery = "SELECT * FROM buscaLlaveVitrinaInvMatVitrina(" + this.vitrina.getCveVitrina() + "::smallint)";
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

        if (rst != null) {
            vObj = new Vector<InvMatVitrina>();
            for (i = 0; i < rst.size(); i++) {
                oInv = new InvMatVitrina();
                Vector vRowTemp = (Vector) rst.elementAt(i);
                vitrina = new Vitrina();
                oInv.setVitrina(vitrina.buscaVitrina(((Double) vRowTemp.elementAt(0)).shortValue()));
                materialCuracion = new MaterialCuracion();
                oInv.setMaterialCuracion(materialCuracion.buscaMaterialCuracion((String) vRowTemp.elementAt(1)));
                oInv.setCantidad(((Double) vRowTemp.elementAt(2)).intValue());
                oInv.setLugar((String) vRowTemp.elementAt(3).toString());
                vObj.add(oInv);
            }
            nTam = vObj.size();
            arrRet = new InvMatVitrina[nTam];

            for (i = 0; i < nTam; i++) {
                arrRet[i] = vObj.elementAt(i);
            }
        }
        return arrRet;
    }

    public String modificarInvMatVitrina() throws Exception {
        Vector rst = null;
        String sQuery = "";
        if (this.vitrina.getCveVitrina() == 0 || this.materialCuracion.getCveMaterial().equals("") || this.nCantidad == 0) {
            throw new Exception("InvMatVitrina.modificarInvMatVitrina: error de programación, faltan datos");
        } else {
            sQuery = "SELECT * FROM modificaInvMatVitrina(" + this.vitrina.getCveVitrina() + "::smallint, " + this.nCantidad + "::smallint, '" + this.materialCuracion.getCveMaterial() + "')";

            if (getAD() == null) {
                setAD(new AccesoDatos());
                getAD().conectar();
                rst = getAD().ejecutarConsulta(sQuery);
                getAD().desconectar();
                setAD(null);
            }
        }
        return new StringBuilder().append(rst.get(0)).toString();
    }
    
    public List<InvMatVitrina> buscaInventario(int nCveVitrina) throws Exception{
        List<InvMatVitrina> listRet=null;
        InvMatVitrina oIMatV;
        Vitrina oV;
        
        Vector rst = null;
        String sQuery = "";

        sQuery="select * from buscaInvMatVitrina("+nCveVitrina+"::int2);"; 
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
                Vector vRowTemp = (Vector)rst.elementAt(i);
                oIMatV=new InvMatVitrina();
                oIMatV.getMaterialCuracion().setCveMaterial((String)vRowTemp.elementAt(0));
                oIMatV.getMaterialCuracion().setDescripcion((String)vRowTemp.elementAt(1));
                oIMatV.setLugar((String)vRowTemp.elementAt(2));
                oIMatV.setCantidad(((Double)vRowTemp.elementAt(3)).intValue());
                oV=new Vitrina();
                oV.setCveVitrina((short)nCveVitrina);
                oIMatV.setVitrina(oV);
                listRet.add(oIMatV);
            }
        }
        System.out.println(listRet.size());
        return listRet;
    }
    
    public String modifica(InventarioVitrina oMat)throws Exception{
        Vector rst = null;
        String sQuery = "";

        sQuery="select * from modificaMatVitrina("+oMat.getVitrina()+"::int2, '"+oMat.getCve()+"', "+oMat.getExistencia()+"::int2);"; 
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
        return ""+rst.get(0).toString().substring(1,rst.get(0).toString().length()-1);
    }

    public Vitrina getVitrina() {
        return vitrina;
    }

    public void setVitrina(Vitrina vitrina) {
        this.vitrina = vitrina;
    }

    public MaterialCuracion getMaterialCuracion() {
        return materialCuracion;
    }

    public void setMaterialCuracion(MaterialCuracion materialCuracion) {
        this.materialCuracion = materialCuracion;
    }

    public int getCantidad() {
        return nCantidad;
    }

    public void setCantidad(int nCantidad) {
        this.nCantidad = nCantidad;
    }

    public String getLugar() {
        return sLugar;
    }

    public void setLugar(String sLugar) {
        this.sLugar = sLugar;
    }

    public AccesoDatos getAD() {
        return oAD;
    }

    public void setAD(AccesoDatos oAD) {
        this.oAD = oAD;
    }

    @Override
    public String toString() {
        return "InvMatVitrina{" + "vitrina=" + vitrina + ", materialCuracion=" + materialCuracion + ", nCantidad=" + nCantidad + ", sLugar=" + sLugar + ", oAD=" + oAD + '}';
    }
}
