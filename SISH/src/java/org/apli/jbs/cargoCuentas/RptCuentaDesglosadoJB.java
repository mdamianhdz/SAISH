package org.apli.jbs.cargoCuentas;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.apli.jbs.PacienteJB;
import org.apli.modelbeans.EpisodioMedico;
import org.apli.modelbeans.Paciente;
import org.apli.modelbeans.ServicioPrestado;
import org.apli.modelbeans.TipoPrincipalPaga;

/**
 *
 * @author Juan
 */
@ManagedBean( name = "oRptCtaDesglosadoJB" )
@SessionScoped
public class RptCuentaDesglosadoJB
{
    private Paciente oPac;
    private EpisodioMedico oEpMed;
    private ServicioPrestado oServPres;
    private List<EpisodioMedico> arrEpMed;
    private List<ServicioPrestado> arrLineaServ;
    private List<ServicioPrestado> arrServPres;
    private List<TipoPrincipalPaga> arrTipos;
    private Date dFechaActual;
    private boolean bRenderEpisodios;
    private boolean bRenderRptCta;
    private int nIndex;
    
    public RptCuentaDesglosadoJB()
    {
        oPac = null;
        oEpMed = new EpisodioMedico();
        oServPres = new ServicioPrestado();
        arrEpMed = null;
        arrLineaServ = null;
        arrServPres = null;
        arrTipos = new TipoPrincipalPaga().buscaTiposPrincipalPaga();
        dFechaActual = new Date();
        bRenderEpisodios = false;
        bRenderRptCta = false;
        nIndex = 0;
    }
    
    public void limpiar()
    {
        oPac = null;
        oEpMed = new EpisodioMedico();
        oServPres = new ServicioPrestado();
        arrEpMed = null;
        arrLineaServ = null;
        arrServPres = null;
        arrTipos = new TipoPrincipalPaga().buscaTiposPrincipalPaga();
        dFechaActual = new Date();
        bRenderEpisodios = false;
        bRenderRptCta = false;
        nIndex = 0;
    }
    
    /**
     * Busca las cuentas del paciente seleccionado
     * @throws Exception Si no se ha seleccionado un paciente o el paciente no tiene una cuenta de hospital
     */
    public void buscarCuentasPaciente() throws Exception
    {
        String sMen = "";
        oPac = new PacienteJB().getPacienteSesion();
        
        if( oPac == null || oPac.getFolioPac() < 1 )
        {
            sMen = "No hay información de pacientes con los filtros indicados";
        }
        else
        {
            arrEpMed = oEpMed.buscarEpisodiosCuentaHospitalizacion( oPac.getFolioPac() );
            if( arrEpMed == null || arrEpMed.size() < 1 )
            {
                sMen = "El paciente elegido no tiene una cuenta de hospital";
                bRenderEpisodios = false;
            }
            else
            {
                bRenderEpisodios = true;
            }
        }
        
        if( sMen.compareTo( "" ) != 0 )
        {
            FacesContext context= FacesContext.getCurrentInstance();
            context.addMessage( null, new FacesMessage( "Reporte Cuenta Desglosado", sMen ) );
            
            System.out.println( sMen );
        }
    }
    
    /**
     * Genera el reporte de hospitalización
     * @throws Exception Si no hay cargos pendientes
     */
    public void generarReporteCuentaHospitalizacion() throws Exception
    {
        String sMen = "";
        
        if( oPac == null || oPac.getFolioPac() < 1 )
        {
            sMen = "No ha seleccionado un paciente";
        }
        else if( oEpMed == null || oEpMed.getCveepisodio() < 1 )
        {
            sMen = "No ha seleccionado una cuenta";
        }
        else
        {
            arrLineaServ = oServPres.buscarLineaServicioCargosEpisodioMedico( oEpMed.getCveepisodio(), oPac.getFolioPac() );
            arrServPres = oServPres.buscarCargosCuentaDesglosado( oEpMed.getCveepisodio(), oPac.getFolioPac() );
            if( arrLineaServ == null || arrServPres == null || arrLineaServ.size() < 1 || arrServPres.size() < 1 )
            {
                sMen = "El paciente no tiene cargos pendientes";
            }
            else
            {
                bRenderEpisodios = false;
                bRenderRptCta = true;
            }
        }
        
        System.out.println( "sMen = " + sMen );
        
        if( sMen.compareTo( "" ) != 0 )
        {
            FacesContext context= FacesContext.getCurrentInstance();
            context.addMessage( null, new FacesMessage( "Reporte Cuenta Desglosado", sMen ) );
            
            System.out.println( sMen );
        }
    }
    
    /**
     * Separa los detalles de cada línea de servicio
     * @param nLineaServ El índice de la línea de servicio a detallar
     * @return Lista de los detalles por línea de servicio
     */
    public List<ServicioPrestado> buscarCargos( int nLineaServ )
    {
        List<ServicioPrestado> arrRet = null;
        ServicioPrestado oSP = null;
        String sLinea = "", sCargo = "";
        boolean bEnd = false;
        
        if( (arrLineaServ != null) && (nLineaServ < arrLineaServ.size() ))
        {
            sLinea = arrLineaServ.get( nLineaServ ).getConcepPrestado().getLineaIngreso().getDescrip();
            if( arrServPres != null && nIndex < arrServPres.size() )
            {
                arrRet = new ArrayList<ServicioPrestado>();
                while( !bEnd && nIndex < arrServPres.size() )
                {
                    oSP = arrServPres.get( nIndex );
                    sCargo = oSP.getConcepPrestado().getLineaIngreso().getDescrip();
                    if( sLinea.compareTo( sCargo ) == 0 )
                    {
                        arrRet.add( oSP );
                        nIndex++;
                    }
                    else
                    {
                        bEnd = true;
                    }
                }
            }
        }
        
        return arrRet;
    }
    
// =========================== SET & GET =========================== //
    public Paciente getPaciente()
    {
        return oPac;
    }

    public void setPaciente( Paciente oPac )
    {
        this.oPac = oPac;
    }

    public EpisodioMedico getEpisodioMedico()
    {
        return oEpMed;
    }

    public void setEpisodioMedico( EpisodioMedico oEpMed )
    {
        this.oEpMed = oEpMed;
    }

    public ServicioPrestado getServicioPrestado()
    {
        return oServPres;
    }

    public void setServicioPrestado( ServicioPrestado oServPres )
    {
        this.oServPres = oServPres;
    }

    public List<EpisodioMedico> getArrEpisodioMedico()
    {
        return arrEpMed;
    }

    public void setArrEpisodioMedico( List<EpisodioMedico> arrEpMed )
    {
        this.arrEpMed = arrEpMed;
    }

    public List<ServicioPrestado> getArrLineaServicio()
    {
        return arrLineaServ;
    }

    public void setArrLineaServicio( List<ServicioPrestado> arrLineaServ )
    {
        this.arrLineaServ = arrLineaServ;
    }

    public List<ServicioPrestado> getArrServicioPrestado()
    {
        return arrServPres;
    }

    public void setArrServicioPrestado( List<ServicioPrestado> arrServPres )
    {
        this.arrServPres = arrServPres;
    }
    
    public Date getFechaActual()
    {
        return dFechaActual;
    }
    
    public void setFechaActual( Date dFechaActual )
    {
        this.dFechaActual = dFechaActual;
    }

    public boolean isRenderEpisodios()
    {
        return bRenderEpisodios;
    }

    public void setRenderEpisodios( boolean bRenderEpisodios )
    {
        this.bRenderEpisodios = bRenderEpisodios;
    }
    
    public boolean isRenderReporteCuenta()
    {
        return bRenderRptCta;
    }

    public void setRenderReporteCuenta( boolean bRenderRptCta )
    {
        this.bRenderRptCta = bRenderRptCta;
    }
    
    public String getTipoPaciente()
    {
        if( oEpMed != null && oEpMed.getCveepisodio() > 0 && oEpMed.getTipoPrincipal() < arrTipos.size() )
            return arrTipos.get( oEpMed.getTipoPrincipal() ).getDescripcion().toUpperCase();
        else
            return "SIN TIPO";
    }
    
    public String buscarTipoPaciente( int nTipo )
    {
        if( nTipo > -1 && nTipo < arrTipos.size() )
            return arrTipos.get( nTipo ).getDescripcion().toUpperCase();
        else
            return "SIN TIPO";
    }
}
