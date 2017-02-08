/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apli.jbs.recepcion;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.apli.modelbeans.reportes.RptHospitalizacion;

/**
 *
 * @author Juan
 */
@ManagedBean(name="oPacHospHoy")
@SessionScoped
public class PacientesHospitalizadosHoyJB
{
    private Date fechaActual;
    private RptHospitalizacion oRHosp;
    private RptHospitalizacion[] datosPacsHosp, datosOri;
    private List<RptHospitalizacion> arrHosps;
    
    /**
     * Crea nueva instancia de PacientesHospitalizadosHoyJB con los datos por default
     */
    public PacientesHospitalizadosHoyJB()
    {
        fechaActual = new Date();
        oRHosp = new RptHospitalizacion();
        oRHosp.setSinAlta( true );
        arrHosps = new ArrayList();
        datosPacsHosp = null;
        datosOri = null;
    }
    
    /**
     * Limpia las variables a su estado default
     */
    public void limpiarPacientesHosp()
    {
        fechaActual= new Date();
        oRHosp = new RptHospitalizacion();
        oRHosp.setSinAlta( true );
        arrHosps = new ArrayList();
        datosPacsHosp = null;
        datosOri = null;
    }
    
    /**
     * Busca los pacientes hospitalizados el día de hoy
     */
    public void buscaPacientesHospitalizadosHoy()
    {
        String sMen = "";
        try
        {
            datosPacsHosp = oRHosp.getPacientesHospitalizados( fechaActual, fechaActual );
            if( datosPacsHosp == null || datosPacsHosp.length < 1 )
            {
                sMen = "No hay información de pacientes hospitalizados en la fecha actual";
            }
            else
            {
                datosOri = new RptHospitalizacion[ datosPacsHosp.length ];
                RptHospitalizacion datos;
                for( int i = 0; i < datosOri.length; i++ )
                {
                    datos = new RptHospitalizacion();
                    datos.getPaciente().setFolioPac( datosPacsHosp[ i ].getPaciente().getFolioPac() );
                    datos.getEpMed().setCveepisodio( datosPacsHosp[ i ].getEpMed().getCveepisodio() );
                    datos.getEpMed().setPosibleAlta( datosPacsHosp[ i ].getEpMed().getPosibleAlta() );
                    
                    datosOri[ i ] = datos;
                }
            }
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }
        
        if( sMen.compareTo( "" ) != 0 )
        {
            FacesContext context= FacesContext.getCurrentInstance();
            context.addMessage( null, new FacesMessage( "Pacientes Hospitalizados", sMen ) );
        }
    }
    
    /**
     * Actualiza las posibles altas de los pacientes
     */
    public void actualizarPosiblesAltas()
    {
        String sMen = "No se actualizaron las posibles altas de pacientes";
        
        revisarActualizaciones();
        
        if( arrHosps != null && arrHosps.size() > 0 )
        {
            int nTam = arrHosps.size();
            int[] arrNFoliosPacs = new int[ nTam ];
            int[] arrCveEpisodios = new int[ nTam ];
            String[] arrSPosiblesAltas = new String[ nTam ];
            RptHospitalizacion tempRHosp;
            int nAfectados = -1;
            
            for( int i = 0; i < nTam; i++ )
            {
                tempRHosp = arrHosps.get( i );
                arrNFoliosPacs[ i ] = tempRHosp.getPaciente().getFolioPac();
                arrCveEpisodios[ i ] = tempRHosp.getEpMed().getCveepisodio();
                arrSPosiblesAltas[ i ] = tempRHosp.getEpMed().getPosibleAlta();
            }
            try
            {
                nAfectados = oRHosp.getEpMed().actualizarPosiblesAltasPacs( arrNFoliosPacs, arrCveEpisodios, arrSPosiblesAltas );
            }
            catch( Exception e )
            {
                e.printStackTrace();
            }
            
            if( nAfectados > 0 )
            {
                sMen = "Se actualizaron las posibles altas de pacientes";
            }
        }
        else
        {
            sMen = "No hay cambios en las posibles altas de pacientes";
        }
        
        FacesContext context= FacesContext.getCurrentInstance();
        context.addMessage( null, new FacesMessage( "Pacientes Hospitalizados", sMen ) );
    }
    
    /**
     * Revisa que los pacientes seleccionados tengan un cambio en su posible alta.
     * Esto es para no enviar datos innecesarios a la consulta de la base de datos.
     */
    private void revisarActualizaciones()
    {
        String sAct = "", sOld = "";
        for( int i = 0; i < datosPacsHosp.length; i++ )
        {
            sAct = datosPacsHosp[ i ].getEpMed().getPosibleAlta();
            sOld = datosOri[ i ].getEpMed().getPosibleAlta();
            if( sAct.compareTo( sOld ) != 0 )
            {
                arrHosps.add( datosPacsHosp[ i ] );
            }
        }
    }
    
//=============== SET & GET ===============//
    public Date getFechaActual()
    {
        return fechaActual;
    }

    public void setFechaActual( Date fechaActual )
    {
        this.fechaActual = fechaActual;
    }

    public RptHospitalizacion getRHosp()
    {
        return oRHosp;
    }

    public void setRHosp( RptHospitalizacion oRHosp )
    {
        this.oRHosp = oRHosp;
    }

    public RptHospitalizacion[] getDatosPacsHosp()
    {
        return datosPacsHosp;
    }

    public void setDatosPacsHosp( RptHospitalizacion[] datosPacsHosp )
    {
        this.datosPacsHosp = datosPacsHosp;
    }
}
