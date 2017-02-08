package org.apli.modelbeans.ventaCredito;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import org.apli.AD.AccesoDatos;



/**
 * Representa los formatos que cada compa��a de cr�dito solicita para hacer v�lido
 * el tratamiento y cubrir su costo
 * @author BAOZ
 * @version 1.0
 * @created 12-may-2014 04:44:30 p.m.
 */
public class FormatoCiaCred implements Serializable {

    /**
     * Representa un identificador interno del formato
     */
    private int nIdFmt;
    /**
     * Nombre del formato
     */
    private String sNomFmt;
    private CompaniaCred oCC;
    private boolean bActiva;
    private AccesoDatos oAD;
    private boolean documento;
    private boolean documentoreg;
    public FormatoCiaCred(){
        oCC=new CompaniaCred();
    }
    
    public List<FormatoCiaCred> buscaFormatos(int nIdEmp)throws Exception{
        List<FormatoCiaCred> listRet=null;
        FormatoCiaCred oForm;
        CompaniaCred oCompC;
        
        Vector rst = null;
        String sQuery = "";

        sQuery="select * from buscaFormatosCia("+nIdEmp+"::int2);";
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
                oForm=new FormatoCiaCred();
                oCompC=new CompaniaCred();
                Vector vRowTemp = (Vector)rst.elementAt(i);
                oForm.setIdFmt(((Double)vRowTemp.elementAt(0)).intValue());
                oForm.setNomFmt((String)vRowTemp.elementAt(1));
                oCompC.setIdEmp(((Double)vRowTemp.elementAt(2)).intValue());
                oForm.setActiva(cambia(((String)vRowTemp.elementAt(3)).charAt(0)));
                oForm.setCC(oCompC);
                listRet.add(oForm);
            }
        }
        return listRet;
    }
    
    public String modificaEstado(int nIdFmt, boolean cActiva)throws Exception{
        Vector rst = null;
        String sQuery = "";

        sQuery="select * from cambiaestadoformato("+nIdFmt+"::int2,'"+cambiaEstado(cActiva)+"')";
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
    
    public String insertaFormato(FormatoCiaCred oForm)throws Exception{
        Vector rst = null;
        String sQuery = "";

        sQuery="select * from insertaformato('"+oForm.getNomFmt()+"',"+oForm.getCC().getIdEmp()+"::int2, null::character)";
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
    
        public List<FormatoCiaCred> todosFormatosPorEmpresa(int nImp) throws Exception {
        
        List<FormatoCiaCred> listRet=new ArrayList<FormatoCiaCred>();
        FormatoCiaCred oFCC;

        Vector rst = null;
        String sQuery = "";

        sQuery="SELECT * FROM buscatodosformatosEmp("+nImp+");";     
        if (getAD() == null){
                setAD(new AccesoDatos());
                getAD().conectar();
                rst = getAD().ejecutarConsulta(sQuery);
                
        }else{
                rst = getAD().ejecutarConsulta(sQuery);
        }
               
        if (rst != null) {
            listRet = new ArrayList();
            for (int i = 0; i < rst.size(); i++) {
                oFCC = new FormatoCiaCred(); 
                Vector vRowTemp = (Vector)rst.elementAt(i);                 
                oFCC.setIdFmt(((Double)vRowTemp.elementAt(0)).intValue());
                oFCC.setNomFmt((String)vRowTemp.elementAt(1));
                CompaniaCred oCompCred=new CompaniaCred();
                oCompCred.setIdEmp(((Double)vRowTemp.elementAt(2)).intValue());
                oFCC.setCC(oCompCred);                
                listRet.add(oFCC);
            }
        }
        
        getAD().desconectar();
        setAD(null);
        return listRet;
    }
    
    public boolean cambia(char val){
        if (val=='0')
            return false;
        else 
            return true;
    }
    
    public char cambiaEstado(boolean val){
        if (val==false)
            return '1';
        else 
            return '0';
    }

    public int getIdFmt() {
        return nIdFmt;
    }

    public void setIdFmt(int nIdFmt) {
        this.nIdFmt = nIdFmt;
    }

    public String getNomFmt() {
        return sNomFmt;
    }

    public void setNomFmt(String sNomFmt) {
        this.sNomFmt = sNomFmt;
    }

    public CompaniaCred getCC() {
        return oCC;
    }

    public void setCC(CompaniaCred oCC) {
        this.oCC = oCC;
    }

    public AccesoDatos getAD() {
        return oAD;
    }

    public void setAD(AccesoDatos oAD) {
        this.oAD = oAD;
    }

    public boolean isActiva() {
        return bActiva;
    }

    public void setActiva(boolean bActiva) {
        this.bActiva = bActiva;
    }
    
      public boolean getDocumento() {
        return documento;
    }

    public void setDocumento(boolean documento) {
        this.documento = documento;
    }

    public boolean getDocumentoreg() {
        return documentoreg;
    }

    public void setDocumentoreg(boolean documentoreg) {
        this.documentoreg = documentoreg;
    }
}