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
public class InvMedVitrina implements Serializable {

    private Vitrina vitrina;
    private Medicamento medicamento;
    private int nCantidad;
    private String sLugar;
    protected AccesoDatos oAD = null;

    public InvMedVitrina() {
        vitrina=new Vitrina();
        medicamento=new Medicamento();
    }

    public InvMedVitrina(AccesoDatos poAD) {
        oAD = poAD;
    }

    public InvMedVitrina[] buscaTodosPorVitrina() throws Exception {
        InvMedVitrina arrRet[] = null, oInv = null;
        Vector rst = null;
        Vector<InvMedVitrina> vObj = null;
        String sQuery = "";
        int i = 0, nTam = 0;

        if (this.vitrina.getCveVitrina() == 0) {
            throw new Exception("InvMatVitrina.buscaTodosPorVitrina: error de programaciÃ³n, faltan datos");
        } else {
            sQuery = "SELECT * FROM buscallavevitrinainvmedvitrina(" + this.vitrina.getCveVitrina() + "::smallint)";
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
            vObj = new Vector<InvMedVitrina>();
            for (i = 0; i < rst.size(); i++) {
                oInv = new InvMedVitrina();
                Vector vRowTemp = (Vector) rst.elementAt(i);
                vitrina = new Vitrina();
                oInv.setVitrina(vitrina.buscaVitrina(((Double) vRowTemp.elementAt(0)).shortValue()));
                medicamento = new Medicamento();
                oInv.setMedicamento(medicamento.buscaMedicamentoVitrina((String) vRowTemp.elementAt(1)));
                oInv.setCantidad(((Double) vRowTemp.elementAt(2)).intValue());
                oInv.setLugar((String) vRowTemp.elementAt(3).toString());
                vObj.add(oInv);
            }
            nTam = vObj.size();
            arrRet = new InvMedVitrina[nTam];

            for (i = 0; i < nTam; i++) {
                arrRet[i] = vObj.elementAt(i);
            }
        }
        return arrRet;
    }

    public String modificarInvMedVitrina() throws Exception {
        Vector rst = null;
        String sQuery = "";
        if (this.vitrina.getCveVitrina() == 0 || "".equals(this.medicamento.getCveMedicamento()) || this.nCantidad == 0) {
            throw new Exception("InvMatVitrina.modificarInvMatVitrina: error de programaciÃ³n, faltan datos");
        } else {
            sQuery = "SELECT * FROM modificaInvMedVitrina(" + this.vitrina.getCveVitrina() + "::smallint, '" + this.medicamento.getCveMedicamento() + "', " + this.nCantidad + "::smallint)";

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

    
    public List<InvMedVitrina> buscaInventario(int nCveVitrina) throws Exception{
        List<InvMedVitrina> listRet=null;
        InvMedVitrina oIMedV;
        Vitrina oV;
        
        Vector rst = null;
        String sQuery = "";

        sQuery="select * from buscaInvMedVitrina("+nCveVitrina+"::int2);";  
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
                oIMedV=new InvMedVitrina();
                oIMedV.getMedicamento().setCveMedicamento((String)vRowTemp.elementAt(0));
                oIMedV.getMedicamento().setNomMedicamento((String)vRowTemp.elementAt(1));
                oIMedV.setLugar((String)vRowTemp.elementAt(2));
                oIMedV.setCantidad(((Double)vRowTemp.elementAt(3)).intValue());
                oV=new Vitrina();
                oV.setCveVitrina((short)nCveVitrina);
                oIMedV.setVitrina(oV);
                listRet.add(oIMedV);
            }
        }
        System.out.println(listRet.size());
        return listRet;
    }
    
    public String modifica(InventarioVitrina oMed)throws Exception{
        Vector rst = null;
        String sQuery = "";

        sQuery="select * from modificaMedVitrina("+oMed.getVitrina()+"::int2, '"+oMed.getCve()+"', "+oMed.getExistencia()+"::int2);"; 
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

    public Medicamento getMedicamento() {
        return medicamento;
    }

    public void setMedicamento(Medicamento medicamento) {
        this.medicamento = medicamento;
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
        return "InvMedVitrina{" + "vitrina=" + vitrina + ", medicamento=" + medicamento + ", nCantidad=" + nCantidad + ", sLugar=" + sLugar + ", oAD=" + oAD + '}';
    }
}
