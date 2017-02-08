package org.apli.modelbeans.reportes;

import java.io.Serializable;
import java.util.Date;
import java.util.Vector;
import org.apli.AD.AccesoDatos;
import org.apli.modelbeans.EpisodioMedico;
import org.apli.modelbeans.Paciente;

/**
 *
 * @author juan
 */
public class RptHospitalizacion implements Serializable{
   protected EpisodioMedico oEpMed;
   protected Paciente oPaciente;
   private AccesoDatos oAD = null;
   //JMHG
   private boolean bPacsHoy;
   //----
   
   public RptHospitalizacion(){
       oPaciente= new Paciente();
       oEpMed= new EpisodioMedico();
       bPacsHoy = false;
   }
   
   
   public RptHospitalizacion[] getPacientesHospitalizados(Date fechaInicio,Date fechaFin) throws Exception{
   RptHospitalizacion[] arrRet=null;
            RptHospitalizacion oRptHosp=null;
            Vector rst = null;
            Vector<RptHospitalizacion> vObj = null;
            String sQuery = "";
            int i=0, nTam = 0;
            sQuery ="select * from buscarpacienteshospitalizadosenrangofecha('"+fechaInicio+
                    "','"+fechaFin+"')";
            //JMHG
            // Verifica si es la consulta de pacientes del día de hoy el cual requiere un cierto orden
            // y que no tengan una fecha de alta ya registrada.
            if( bPacsHoy )
            {
                sQuery = sQuery + " WHERE outDAlta IS NULL ORDER BY outdFechaHosp DESC;";
            }
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
                vObj = new Vector<RptHospitalizacion>();
                RptHospitalizacion tempHosp;
              
                for (i = 0; i < rst.size(); i++) {
                    oRptHosp = new RptHospitalizacion();
                    
                    Paciente oPacTemp= new Paciente();
                    EpisodioMedico oEpMedTemp=new EpisodioMedico();
                    
		    Vector vRowTemp = (Vector)rst.elementAt(i);
                    
                    oEpMedTemp.setInicio((Date)vRowTemp.elementAt(0));
                    
                    oPacTemp.setNombre((String)vRowTemp.elementAt(1));
                    oPacTemp.setApellidoPaterno((String)vRowTemp.elementAt(2));
                    oPacTemp.setApellidoMaterno((String)vRowTemp.elementAt(3));
                    
                    oEpMedTemp.getHab().setHab( ((Double)vRowTemp.elementAt(4)).intValue());
                    
                    oEpMedTemp.getMedTratante().setNombre((String)vRowTemp.elementAt(5));
                    oEpMedTemp.getMedTratante().setApellidoPaterno((String)vRowTemp.elementAt(6));
                    oEpMedTemp.getMedTratante().setApellidoMaterno((String)vRowTemp.elementAt(7));
                                        
                    oEpMedTemp.getDxIngreso().setDescrip((String)vRowTemp.elementAt(8));
                    
                    oEpMedTemp.setAlta((Date)vRowTemp.elementAt(9));
                    
                    //JMHG
                    oPacTemp.setFolioPac(((Double)vRowTemp.elementAt(10)).intValue());
                    oEpMedTemp.setCveepisodio(((Double)vRowTemp.elementAt(11)).intValue());
                    oEpMedTemp.setPosibleAlta((String)vRowTemp.elementAt(12));
                    //====
                    
                    oRptHosp.setEpMed(oEpMedTemp);
                    oRptHosp.setPaciente(oPacTemp);
                    
                    vObj.add(oRptHosp);
                }
                nTam = vObj.size();
                arrRet = new RptHospitalizacion[nTam];

                for (i=0; i<nTam; i++){
                    arrRet[i] = vObj.elementAt(i);
                }
            }
            
            return arrRet;
    }

    /**
     * @return the oAD
     */
    public AccesoDatos getAD() {
        return getoAD();
    }

    /**
     * @param oAD the oAD to set
     */
    public void setAD(AccesoDatos oAD) {
        this.setoAD(oAD);
    }

    /**
     * @return the paciente
     */
    public Paciente getPaciente() {
        return oPaciente;
    }

    /**
     * @param paciente the paciente to set
     */
    public void setPaciente(Paciente paciente) {
        this.oPaciente = paciente;
    }

    /**
     * @return the oEpMed
     */
    public EpisodioMedico getEpMed() {
        return oEpMed;
    }

    /**
     * @param oEpMed the oEpMed to set
     */
    public void setEpMed(EpisodioMedico oEpMed) {
        this.oEpMed = oEpMed;
    }

    /**
     * @return the oAD
     */
    public AccesoDatos getoAD() {
        return oAD;
    }

    /**
     * @param oAD the oAD to set
     */
    public void setoAD(AccesoDatos oAD) {
        this.oAD = oAD;
    }
    
    //JMHG
    public boolean getSinAlta()
    {
        return bPacsHoy;
}
    
    public void setSinAlta( boolean bSinAlta )
    {
        this.bPacsHoy = bSinAlta;
    }
    //====
}
