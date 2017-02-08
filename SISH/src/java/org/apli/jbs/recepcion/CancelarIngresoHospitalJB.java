package org.apli.jbs.recepcion;

import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.apli.jbs.PacienteJB;
import org.apli.modelbeans.EpisodioMedico;
import org.apli.modelbeans.Paciente;

/**
 *
 * @author Juan
 */
@ManagedBean( name = "oCancelarHospJB" )
@SessionScoped
public class CancelarIngresoHospitalJB
{
    private EpisodioMedico oEpMed;
    private Paciente oPac;
    private List<EpisodioMedico> arrEpMed;
    private Date dFechaHoy;
    private boolean bMostrarPanel;
    
    /**
     * Inicia los valores por default
     */
    public CancelarIngresoHospitalJB()
    {
        oEpMed = new EpisodioMedico();
        oPac = null;
        arrEpMed = null;
        dFechaHoy = new Date();
        bMostrarPanel = false;
    }
    
    /**
     * Reinicia los valores default
     */
    public void limpiar()
    {
        oEpMed = new EpisodioMedico();
        oPac = null;
        arrEpMed = null;
        dFechaHoy = new Date();
        bMostrarPanel = false;
    }
    
    /**
     * Busca el paciente para la cancelación de ingreso al hospital
     * @throws Exception En caso de que no exista el paciente indicado
     */
    public void buscarPacienteIngresado() throws Exception
    {
        arrEpMed = null;
        String sMen = "";
        oPac = new PacienteJB().getPacienteSesion();
        
        if( oPac == null || oPac.getFolioPac() < 1 )
        {
            sMen = "No ha ingresado un paciente";
        }
        else
        {
            arrEpMed = oEpMed.buscarPacienteCancelarIngresoHospital( oPac.getFolioPac(), dFechaHoy );
            if( arrEpMed == null || arrEpMed.size() < 1 )
            {
                sMen = "El paciente elegido no tiene un ingreso hospitalario del mismo día o ya tiene cargos hospitalarios por servicios en curso";
            }
        }
        
        if( sMen.compareTo( "" ) != 0 )
        {
            FacesContext context= FacesContext.getCurrentInstance();
            context.addMessage( null, new FacesMessage( "Cancelar Ingreso Hospital", sMen ) );
            
            System.out.println( sMen );
        }
        else
        {
            bMostrarPanel = true;
        }
    }
    
    /**
     * Cancela el ingreo al hospital del paciente indicado
     * @throws Exception En caso de que falten datos
     */
    public void cancelarIngresoHospital() throws Exception
    {
        String sMen = "";
        int nAfectados = -1;
        
        if( oPac == null || oPac.getFolioPac() < 1 || oEpMed == null || oEpMed.getCveepisodio() < 1 )
        {
            sMen = "No se realizó ningún cambio";
            throw new Exception( "CancelarIngresoHospitalJB.cancelarIngresoHospital: Faltan datos" );
        }
        else
        {
            nAfectados = oEpMed.cancelarIngresoHospital( oPac.getFolioPac() );
            if( nAfectados > 0 )
            {
                sMen = "Se canceló correctamente el ingreso al hospital del paciente";
                limpiar();
            }
            else
            {
                sMen = "No se pudo cancelar el ingreso al hospital del paciente"; 
            }
        }
        
        FacesContext context= FacesContext.getCurrentInstance();
        context.addMessage( null, new FacesMessage( "Cancelar Ingreso Hospital", sMen ) );

        System.out.println( sMen );
    }


//=============== SET & GET ===============//
    public EpisodioMedico getEpisodioMedico()
    {
        return oEpMed;
    }

    public void setEpisodioMedico( EpisodioMedico oEpMed )
    {
        this.oEpMed = oEpMed;
    }

    public Paciente getPaciente()
    {
        return oPac;
    }

    public void setPaciente( Paciente oPac )
    {
        this.oPac = oPac;
    }

    public List<EpisodioMedico> getArrEpisodioMedico()
    {
        return arrEpMed;
    }

    public void setArrEpisodioMedico( List<EpisodioMedico> arrEpMed )
    {
        this.arrEpMed = arrEpMed;
    }

    public Date getFechaHoy()
    {
        return dFechaHoy;
    }

    public void setFechaHoy( Date dFechaHoy )
    {
        this.dFechaHoy = dFechaHoy;
    }

    public boolean isMostrarPanel()
    {
        return bMostrarPanel;
    }

    public void setMostrarPanel( boolean bMostrarPanel )
    {
        this.bMostrarPanel = bMostrarPanel;
    }
}
