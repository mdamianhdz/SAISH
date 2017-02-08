/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apli.modelbeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import org.apli.AD.AccesoDatos;
import org.apli.modelbeans.ventaCredito.CompaniaCred;
import org.apli.modelbeans.ventaCredito.FormatoCiaCred;

/**
 *
 * @author Usuario5
 */
public class EpisodioCiaCred implements Serializable {
    
    private Paciente oPaciente;
    private EpisodioMedico oEpisodioM;
    private CompaniaCred oCompCred;
    private String sNumPoliza="0";
    private String sNumSiniestro;
    
    private String sNumAuto2;
    private String sNombreQuienAuto2;
    private String sMedioAuto;
    private String sObsAuto;
    private boolean compañiaRegAuto=false;
    private boolean compañiaNueva=false;
    private boolean eliminaEmpresa=false;
    private FormatoCiaCred oFormatoCiaCred=new FormatoCiaCred();    
    private int idFmt;
    private List<FormatoCiaCred> formatos=new ArrayList<FormatoCiaCred>();
    
    protected AccesoDatos oAD = null;
    
    public EpisodioCiaCred(){
       oEpisodioM=new EpisodioMedico();
       oPaciente=new Paciente();
       oCompCred=new CompaniaCred();
       formatos=new ArrayList();
    }
    
    public EpisodioCiaCred(AccesoDatos oAD ){
        this.oAD = oAD;
    }
    
    public String getRegistraAutorizacionYDocumt(String sFolioServicio, int nIdfmt) throws Exception{
        Vector rst = null;
        String sQuery = "";        
        
        if ( sFolioServicio.equals("")  ) {
            throw new Exception("Funcion.registraAutorizacionYDocum: error de programación, faltan datos");
	}else {
            sQuery="SELECT * FROM registraautorizacionydocumentacion('"+sFolioServicio+"',0, "+oCompCred.getIdEmp()+"::int2, '"+sNumPoliza+"'::character varying, '"+sNumSiniestro+"'::character varying, '"+sNumAuto2+"'::character varying,'"+sNombreQuienAuto2+"'::character varying, '"+sMedioAuto+"'::character varying, '"+sObsAuto+"'::text,"+nIdfmt+");";
       	}
        return sQuery;
    }
    
     public String getActualizaAutorizacionYDocum(String sOperacion, int nCveEpiMed, int nFolioPac, int fmt) throws Exception {

        Vector rst = null;
        String sQuery = "";

        if (sOperacion.equals("")) {
            throw new Exception("Funcion.registraAutorizacionYDocum: sOperacion error de programación, faltan datos");
        }
        if (nFolioPac == 0) {
            throw new Exception("Funcion.registraAutorizacionYDocum: nFolioPac error de programación, faltan datos");
        }
        if (nCveEpiMed == 0) {
            throw new Exception("Funcion.registraAutorizacionYDocum: nCveEpiMed error de programación, faltan datos");
        }
        if (sNumPoliza.equals("")) {
            throw new Exception("Funcion.registraAutorizacionYDocum: sNumPoliza error de programación, faltan datos");
        }
        if (sNumSiniestro.equals("")) {
            throw new Exception("Funcion.registraAutorizacionYDocum: sNumSiniestro error de programación, faltan datos");
        }
        if (sNumAuto2.equals("")) {
            throw new Exception("Funcion.registraAutorizacionYDocum: sNumAuto2 error de programación, faltan datos");
        }
        if (sNombreQuienAuto2.equals("")) {
            throw new Exception("Funcion.registraAutorizacionYDocum: sNombreQuienAuto2 error de programación, faltan datos");
        }
        if (sMedioAuto.equals("")) {
            throw new Exception("Funcion.registraAutorizacionYDocum: sMedioAuto error de programación, faltan datos");
        }
        if (sObsAuto.equals("")) {
            throw new Exception("Funcion.registraAutorizacionYDocum: sObsAuto error de programación, faltan datos");
        }
        if (fmt==0) {
            throw new Exception("Funcion.registraAutorizacionYDocum: fmt error de programación, faltan datos");
        }
        if (oCompCred.getIdEmp() == 0) {
            throw new Exception("Funcion.registraAutorizacionYDocum: oCompCred.getIdEmp() error de programación, faltan datos");
        } else {
            sQuery = "SELECT * FROM actualizaregistraautorizacionydocumentacion('" + sOperacion + "'::character varying," + nCveEpiMed + "," + nFolioPac + ", " + oCompCred.getIdEmp() + "::int2, '" + sNumPoliza + "'::character varying, '" + sNumSiniestro + "'::character varying, '" + sNumAuto2 + "'::character varying,'" + sNombreQuienAuto2 + "'::character varying, '" + sMedioAuto + "'::character varying, '" + sObsAuto + "'::text," + fmt + "::int2);";
        }
        return sQuery;
    }
    
      public List<EpisodioCiaCred> todosEpisodiosCiaCred(int nFolioPac, int nEpiMed) throws Exception {
        
        List<EpisodioCiaCred> listRet=new ArrayList<EpisodioCiaCred>();
        EpisodioCiaCred oECC;

        Vector rst = null;
        String sQuery = "";

        sQuery="SELECT * FROM buscatodosregistrosautorizadosporepimedpac("+nFolioPac+","+nEpiMed+");";     
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
                oECC = new EpisodioCiaCred(); 
                Vector vRowTemp = (Vector)rst.elementAt(i); 
                oPaciente=new Paciente();
                oPaciente.setFolioPac(((Double)vRowTemp.elementAt(0)).intValue());
                oECC.setPaciente(oPaciente);
                oEpisodioM=new EpisodioMedico();
                oEpisodioM.setCveepisodio(((Double)vRowTemp.elementAt(1)).intValue());
                oECC.setEpisodioM(oEpisodioM);
                oCompCred=new CompaniaCred();
                oCompCred.setIdEmp(((Double)vRowTemp.elementAt(2)).intValue());
                oCompCred.setNombreCorto((String)vRowTemp.elementAt(9));
                formatos=new FormatoCiaCred().todosFormatosPorEmpresa(oCompCred.getIdEmp());
                sQuery="SELECT * FROM buscatodosdocsautorizados("+nFolioPac+","+nEpiMed+","+oCompCred.getIdEmp()+"::int2);";
                Vector rst2 = null;
                rst2 = getAD().ejecutarConsulta(sQuery);
                if (rst != null) {
                    for (int j = 0; j < rst2.size(); j++) {
                        Vector vRowTemp2 = (Vector)rst2.elementAt(j); 
                        int idFormato=((Double)vRowTemp2.elementAt(1)).intValue();
                        for(int k=0;k<formatos.size();k++){
                            if(formatos.get(k).getIdFmt()==idFormato){
                                formatos.get(k).setDocumentoreg(true);
                                formatos.get(k).setDocumento(true);
                            }
                        }
                        oECC.setIdFmt(((Double)vRowTemp2.elementAt(1)).intValue());
                    }
                }
                oECC.setFormatos(formatos);
                oECC.setCompCred(oCompCred);
                oECC.setNumPoliza((String)vRowTemp.elementAt(3));
                oECC.setNumSiniestro((String)vRowTemp.elementAt(4));
                oECC.setNumAuto2((String)vRowTemp.elementAt(5));
                oECC.setNombreQuienAuto2((String)vRowTemp.elementAt(6));
                oECC.setSMedioAuto((String)vRowTemp.elementAt(7));
                oECC.setSObsAuto((String)vRowTemp.elementAt(8));
                oECC.setCompañiaRegAuto(true);
                listRet.add(oECC);
            }
        }
        
        getAD().desconectar();
        setAD(null);
        return listRet;
    } 
    
    public AccesoDatos getAD() {
        return oAD;
    }
    public void setAD(AccesoDatos oAD) {
        this.oAD = oAD;
    }

    public Paciente getPaciente() {
        return oPaciente;
    }

    public void setPaciente(Paciente oPaciente) {
        this.oPaciente = oPaciente;
    }

    public EpisodioMedico getEpisodioM() {
        return oEpisodioM;
    }

    public void setEpisodioM(EpisodioMedico oEpisodioM) {
        this.oEpisodioM = oEpisodioM;
    }

    public CompaniaCred getCompCred() {
        return oCompCred;
    }

    public void setCompCred(CompaniaCred oCompCred) {
        this.oCompCred = oCompCred;
    }

    public String getNumPoliza() {
        return sNumPoliza;
    }

    public void setNumPoliza(String sNumPoliza) {
        this.sNumPoliza = sNumPoliza;
    }

    public String getNumSiniestro() {
        return sNumSiniestro;
    }

    public void setNumSiniestro(String sNumSiniestro) {
        this.sNumSiniestro = sNumSiniestro;
    }
   
    public String getSMedioAuto() {
        return sMedioAuto;
    }

    public void setSMedioAuto(String sMedioAuto) {
        this.sMedioAuto = sMedioAuto;
    }

    public String getSObsAuto() {
        return sObsAuto;
    }

    public void setSObsAuto(String sObsAuto) {
        this.sObsAuto = sObsAuto;
    }  

    public String getNumAuto2() {
        return sNumAuto2;
    }

    public void setNumAuto2(String sNumAuto2) {
        this.sNumAuto2 = sNumAuto2;
    }

    public String getNombreQuienAuto2() {
        return sNombreQuienAuto2;
    }

    public void setNombreQuienAuto2(String sNombreQuienAuto2) {
        this.sNombreQuienAuto2 = sNombreQuienAuto2;
    }

    public boolean getCompañiaRegAuto() {
        return compañiaRegAuto;
    }

    public void setCompañiaRegAuto(boolean compañiaRegAuto) {
        this.compañiaRegAuto = compañiaRegAuto;
    }

    public boolean getCompañiaNueva() {
        return compañiaNueva;
    }

    public void setCompañiaNueva(boolean compañiaNueva) {
        this.compañiaNueva = compañiaNueva;
    }

    public boolean getEliminaEmpresa() {
        return eliminaEmpresa;
    }

    public void setEliminaEmpresa(boolean eliminaEmpresa) {
        this.eliminaEmpresa = eliminaEmpresa;
    }

    public int getIdFmt() {
        return idFmt;
    }

    public void setIdFmt(int idFmt) {
        this.idFmt = idFmt;
    }

    public List<FormatoCiaCred> getFormatos() {
        return formatos;
    }

    public void setFormatos(List<FormatoCiaCred> formatos) {
        this.formatos = formatos;
    }

    public FormatoCiaCred getFormatoCiaCred() {
        return oFormatoCiaCred;
    }

    public void setFormatoCiaCred(FormatoCiaCred oFormatoCiaCred) {
        this.oFormatoCiaCred = oFormatoCiaCred;
    }
    
}