package org.apli.jbs.cobranza;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apli.jbs.PacienteJB;
import org.apli.modelbeans.CiudadCP;
import org.apli.modelbeans.ConceptoIngreso;
import org.apli.modelbeans.ManejoMsjsDB;
import org.apli.modelbeans.Paciente;
import org.apli.modelbeans.ServicioPrestado;
import org.apli.modelbeans.cobranza.AvalCredParticular;
import org.apli.modelbeans.cobranza.CreditoParticular;
import org.apli.modelbeans.cobranza.PagareCredPart;
import org.primefaces.context.RequestContext;

/**
 * Bean administrado para el registro de los créditos otorgados a particulares
 * @author BAOZ
 */
@ManagedBean(name = "oRegCredPartJB")
@ViewScoped
public class RegistroCredParticularJB implements Serializable{
private Paciente oPac;
public ManejoMsjsDB oMenDB=new ManejoMsjsDB();
private CreditoParticular oCredPart;
private List<ServicioPrestado> oListaServExt;
private List<ServicioPrestado> oListaServHosp;
private ServicioPrestado oExtSeleccionado, oHospSeleccionado, 
                         oSPDsctoSelec;
private boolean bBotonServExt=false, bBotonServHosp = false;
private float nPagoIni=0.0f;
private int nCantPagares=0;

//JMHG
private Date dFechaActual;
private final String[] _UNIDADES =
    {
        "CERO",
        "UNO",
        "DOS",
        "TRES",
        "CUATRO",
        "CINCO",
        "SEIS",
        "SIETE",
        "OCHO",
        "NUEVE"
    };
private final String[] _DECENAS =
    {
        "DIEZ",
        "ONCE",
        "DOCE",
        "TRECE",
        "CATORCE",
        "QUINCE",
        "DIECISEIS",
        "DIECISIETE",
        "DIECIOCHO",
        "DIECINUEVE",
        "VEINTE",
        "", // 11- nunca usado
        "VEINTI",
        "TREINTA",
        "CUARENTA",
        "CINCUENTA",
        "SESENTA",
        "SETENTA",
        "OCHENTA",
        "NOVENTA"
    };
private final String[] _CENTENAS =
    {
        "",
        "CIENTO",
        "DOSCIENTOS",
        "TRESCIENTOS",
        "CUATROCIENTOS",
        "QUINIENTOS",
        "SEISCIENTOS",
        "SETECIENTOS",
        "OCHOCIENTOS",
        "NOVECIENTOS"
    };
//----

    public RegistroCredParticularJB() {
    }
    
    public void limpiaPagina(){
        oSPDsctoSelec = new ServicioPrestado();
        oCredPart = new CreditoParticular();
        oExtSeleccionado = new ServicioPrestado();
        oHospSeleccionado = new ServicioPrestado();
        oCredPart.setAvalesCredPart(new ArrayList<AvalCredParticular>());
        oCredPart.getAvalesCredPart().add(new AvalCredParticular());
        oCredPart.getAvalesCredPart().add(new AvalCredParticular());
        oCredPart.setPagaresCredPart(new ArrayList<PagareCredPart>());  
        oCredPart.setCdCP(new CiudadCP()); 
        oCredPart.getAvalesCredPart().get(0).setCdCP(new CiudadCP());
        oCredPart.getAvalesCredPart().get(1).setCdCP(new CiudadCP());
        //JMHG
        dFechaActual = new Date();
        //----
    }
    
    public boolean validaPaciente() {
        boolean validacion = false;
        if (oPac != null) {
            validacion = true;
        }
        return validacion;
    }
    
    /**
     * Busca los servicios externos y las cuentas de hospital de particulares
     * a las que se les puede registrar un descuento o un crédito 
     */
    public void buscaServCtas(){
    ServicioPrestado oSP = new ServicioPrestado();
    FacesMessage msg;
        oPac = new PacienteJB().getPacienteSesion();
        try{
            if (oPac != null){
                oSP.setPaciente(oPac);
                oListaServExt = oSP.todosServsParaAutDsctoCred();
                oListaServHosp = oSP.buscaCuentaParaCerrar();
            }
        }catch(Exception e){
            msg = new FacesMessage("Aviso", "Error al buscar servicios pendientes");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            e.printStackTrace();
        }
    }
    
    /**
     * Muestra el diálogo para descuentos a partir de la información de un
     * servicio externo (no cuenta de hospital) seleccionado
     */ 
    public void seleccionaDsctoExterno(){
        if (oExtSeleccionado == null||
            oExtSeleccionado.getIdFolio()==null || 
            oExtSeleccionado.getIdFolio().equals("")) {
            FacesMessage msg = new FacesMessage("Aviso", "Falta seleccionar servicio");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            oSPDsctoSelec = oExtSeleccionado;
            RequestContext.getCurrentInstance().execute("PF('dlgDescuentoClte').show()");
        }
    }
    
    /**
     * Muestra el diálogo para descuentos a partir de la información de una
     * cuenta de hospital seleccionada
     */ 
    public void seleccionaDsctoHospital(){
        if (oHospSeleccionado == null||
            oHospSeleccionado.getIdFolio()==null || 
            oHospSeleccionado.getIdFolio().equals("")) {
            FacesMessage msg = new FacesMessage("Aviso", "Falta seleccionar cuenta");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            oSPDsctoSelec = oHospSeleccionado;
            oSPDsctoSelec.setCostoCobrado(oSPDsctoSelec.getAnticipo());
            oSPDsctoSelec.setConcepPrestado(new ConceptoIngreso());
            oSPDsctoSelec.getConcepPrestado().setDescripConcep("CUENTA HOSPITAL");
            RequestContext.getCurrentInstance().execute("PF('dlgDescuentoClte').show()");
        }
    }
    
    /**
     * Muestra el diálogo para autorizar créditos a particulares a partir de un
     * servicio externo o una cuenta de hospital seleccionada
     */ 
    public void seleccionaCred(){
        if ((oHospSeleccionado == null||
            oHospSeleccionado.getIdFolio()==null || 
            oHospSeleccionado.getIdFolio().equals("")) &&
            (oExtSeleccionado == null||
            oExtSeleccionado.getIdFolio()==null || 
            oExtSeleccionado.getIdFolio().equals(""))) {
            FacesMessage msg = new FacesMessage("Aviso", "Falta seleccionar elemento");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            if (oExtSeleccionado != null &&
                oExtSeleccionado.getIdFolio()!=null){
                oCredPart.setServPres(oExtSeleccionado);
                oCredPart.setEpiMed(oExtSeleccionado.getEpisodioMedico());
            } else {
                oCredPart.setServPres(oHospSeleccionado);
                oCredPart.getServPres().setCostoCobrado(oCredPart.getServPres().getAnticipo());
                oCredPart.setEpiMed(oHospSeleccionado.getEpisodioMedico());
            }       
            calculaMontoCred();
            context.execute("PF('dlgCreditoClte').show()");
        }
    }
    
    /**
     * Almacena la información del descuento autorizado sobre un servicio
     * externo o una cuenta de hospital
     */ 
    public void registraDscto(){
    FacesMessage msg;
        try{
            if (oSPDsctoSelec.autorizarDescuentoServ()){
                msg = new FacesMessage("Aviso", "El descuento se almacenó exitosamente");
                RequestContext.getCurrentInstance().execute("PF('dlgDescuentoClte').hide()");
            }else{
                msg = new FacesMessage("Aviso", "Error al guardar el descuento");
            }
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }catch(Exception e){
            msg = new FacesMessage("Aviso", "Error al guardar el descuento");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            e.printStackTrace();
        }
    }
    
    /**
     * Almacena la información de un crédito a particular autorizado sobre un
     * servicio externo o una cuenta de hospital
     */ 
    public void registraCredito(){
    FacesMessage msg;
    String mess;
    FacesMessage.Severity nivel;
        try{
            if (validaTotalPagares()){
                //Si trae episodio, se trata de una cuenta de hospital
                //y el ID del servicio prestado sólo sirvió para efectos
                //de PF, por lo que debe quitarse antes de ir a la bd
                System.out.println(oCredPart.getEpiMed().getCveepisodio());
                if (oCredPart.getEpiMed().getCveepisodio()>0)
                    this.oCredPart.getServPres().setIdFolio("");
                mess=oCredPart.guardaCreditoParticular(nPagoIni);
                if (oMenDB.isValid(mess))
                    nivel = FacesMessage.SEVERITY_INFO;   
                else
                    nivel = FacesMessage.SEVERITY_ERROR;
                msg = new FacesMessage(nivel,"Guardar",oMenDB.manejoMensajes(mess));
                if (nivel.equals(FacesMessage.SEVERITY_INFO)){
                    RequestContext.getCurrentInstance().execute("PF('dlgCreditoClte').hide()");
                    RequestContext.getCurrentInstance().execute("PF('dlgImprimirPagaresVar').show()");//JMHG
                }
            }
            else{
                msg = new FacesMessage("Aviso", "La suma de los pagarés es diferente al monto del crédito");
            }
            FacesContext.getCurrentInstance().addMessage(null, msg);
                
        }catch(Exception e){
            msg = new FacesMessage("Aviso", "Error al guardar el crédito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            e.printStackTrace();
        }
    }
    
    /**
     * Manda la pantalla a su estado inicial
     */ 
    public void regresar() throws Exception {
        limpiaPagina();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.getExternalContext().redirect("registraDsctoAutCredito.xhtml?faces-redirect=true");
    }
        
    /**
     * Habilita o deshabilita el botón para selección de un servicio externo
     */
    public void habilitaBotonServExt(){
        if (this.oExtSeleccionado==null)
            bBotonServExt=true;
        else
            bBotonServExt=false;
    }

    /**
     * Habilita o deshabilita el botón para selección de una cuenta de hospital
     */
    public void habilitaBotonServHosp(){
        if (this.oHospSeleccionado==null)
            bBotonServHosp=true;
        else
            bBotonServHosp=false;
    }
    
    public void reiniciaListaPagares(){
        this.oCredPart.setPagaresCredPart(new ArrayList());
        for (int i=0; i< this.nCantPagares; i++){
            PagareCredPart oPag = new PagareCredPart();
            oPag.setCredPart(oCredPart);
            this.oCredPart.getPagaresCredPart().add(oPag);            
        }
    }
    
    /**
     * Calcula el monto del crédito en función del pago inicial
     */
    public void calculaMontoCred(){
        oCredPart.setMontoCred(oCredPart.getServPres().getCostoCobrado()-nPagoIni);
    }
    
    /**
     * Calcula el total de la suma de pagarés
     */
    public float getTotalPagares(){
    float nRet=0.0f;
        if (this.oCredPart != null &&
            this.oCredPart.getPagaresCredPart() != null &&
            !this.oCredPart.getPagaresCredPart().isEmpty()){
            for(int i=0; i<this.oCredPart.getPagaresCredPart().size();i++){
                nRet = nRet + this.oCredPart.getPagaresCredPart().get(i).getMontoPagare();
            }
        }
        return nRet;
    }
    
    /**
     * Indica si la suma de pagarés es válida (entre el monto del crédito y
     * el monto de crédito con interés a un año)
     */
    public boolean validaTotalPagares(){
    boolean bRet = false;
    float nMin=0.0f, nMax = 0.0f;
        if (this.oCredPart != null){
            nMin = this.oCredPart.getMontoCred();
            nMax = nMin *(1+ ((this.oCredPart.getPctInteres()/100)*12));
            if (getTotalPagares()>=nMin && getTotalPagares()<= nMax)
                bRet = true;
        }
        System.out.println("Min "+nMin+" max "+ nMax + " paga "+ getTotalPagares());
        return bRet;
    }
    
    //JMHG
    /**
     * Genera el monto ingresado a su equivalente en texto
     * @param nMonto Monto a convertir a texto
     * @return Monto en texto junto con los centavos
     */
    public String generarMontoTexto( float nMonto )
    {
        String sRet = "", sAux = "";
        List<Integer> arrNums = new ArrayList();
        int nMontoNeto = (int)( nMonto );
        int nRest = nMontoNeto;
        int n = -1;
        int z = -1;
        boolean bNull = false;
        
        if( nMontoNeto != 0 )
        {
            do
            {
                arrNums.add( nRest % 10 );
                nRest = (int)( nRest / 10 );
            }while( nRest != 0 );
            
            for( int i = arrNums.size() - 1; i > -1; i-- )
            {
                n = arrNums.get( i );
                switch( i )
                {
                    case 11:
                    case 8:
                    case 5:
                    case 2:
                        if( ( n == 1 ) && ( arrNums.get( i - 1 ) == 0 ) && ( arrNums.get( i - 2 ) == 0 ) )
                        {
                            sAux += "CIEN";
                        }
                        else
                        {
                            sAux += _CENTENAS[ n ];
                        }
                        break;
                    case 10:
                    case 7:
                    case 4:
                    case 1:
                        z = arrNums.get( i - 1 );
                        switch( n )
                        {
                            case 0:
                                bNull = true;
                                break;
                            case 1:
                                sAux += _DECENAS[ z ];
                                break;
                            case 2:
                                if( z == 0 )
                                {
                                    sAux += _DECENAS[ 10 ];
                                }
                                else
                                {
                                    sAux += _DECENAS[ 12 ] + _UNIDADES[ z ];
                                }
                                break;
                            default:
                                sAux += _DECENAS[ n + 10 ];
                        }
                        break;
                    case 12:
                    case 9:
                    case 6:
                    case 3:
                    case 0:
                        if( n != 0 )
                        {                            
                            if( n == 1 )
                            {
                                if( i == arrNums.size() - 1 )
                                {
                                    sAux += "UN";
                                }
                                else
                                {
                                    z = arrNums.get( i + 1 );
                                    if( z != 1 && z != 2 )
                                    {
                                        if( z == 0 )
                                        {
                                            sAux += _UNIDADES[ n ];
                                        }
                                        else
                                        {
                                            sAux += "Y " + _UNIDADES[ n ];
                                        }
                                    }
                                }
                                
                            }
                            else
                            {
                                if( arrNums.size() > 1 )
                                {
                                    if( i != ( arrNums.size() - 1 ) )
                                    {
                                        z = arrNums.get( i + 1 );
                                        if( z != 1 && z != 2 )
                                        {
                                            if( z == 0 )
                                            {
                                                sAux += _UNIDADES[ n ];
                                            }
                                            else
                                            {
                                                sAux += "Y " + _UNIDADES[ n ];
                                            }
                                        }
                                    }
                                    else
                                    {
                                        sAux += _UNIDADES[ n ];
                                    }
                                }
                                else
                                {
                                    sAux += _UNIDADES[ n ];
                                }
                            }
                        }
                        else
                        {
                            bNull = true;
                        }
                        switch( i )
                        {
                            case 12:
                                if( n == 1 )
                                {
                                    sAux += " BILLON";
                                }
                                else
                                {
                                    sAux += " BILLONES";
                                }
                                break;
                            case 9:
                                if( n == 1 )
                                {
                                    sAux += " MIL MILLON";
                                }
                                else
                                {
                                    sAux += " MIL MILLONES";
                                }
                                break;
                            case 6:
                                if( n == 1 )
                                {
                                    sAux += " MILLON";
                                }
                                else
                                {
                                    sAux += " MILLONES";
                                }
                                break;
                            case 3:
                                sAux += " MIL";
                                break;
                        }
                        break;
                }
                if( !bNull )
                {
                    sAux += " ";
                }
                bNull = false;
            }   
        }
        else
        {
            sAux = "CERO ";
        }
        
        sRet += sAux + " PESOS M.N. " + Math.round( ( nMonto - nMontoNeto ) * 100 ) + "/100";
        
        return sRet;
    }
    
    /**
     * Verifica si es la ultima iteración para los pagarés
     * @param bUltima Boolean si es la ultima iteración
     * @return Salto de página en caso de que no sea la ultima iteración
     */
    public String ultimaIteracion( boolean bUltima )
    {
        if( !bUltima )
            return "page-break-after: always;";
        return "";
    }
    //----
    
    /**
     * @return the oCredPart
     */
    public CreditoParticular getCredPart() {
        return oCredPart;
    }

    /**
     * @param oCredPart the oCredPart to set
     */
    public void setCredPart(CreditoParticular value) {
        this.oCredPart = value;
    }

    /**
     * @return the oListaServExt
     */
    public List<ServicioPrestado> getListaServExt() {
        return oListaServExt;
    }

    /**
     * @param oListaServExt the oListaServExt to set
     */
    public void setListaServExt(List<ServicioPrestado> value) {
        this.oListaServExt = value;
    }

    /**
     * @return the oListaServHosp
     */
    public List<ServicioPrestado> getListaServHosp() {
        return oListaServHosp;
    }

    /**
     * @param oListaServHosp the oListaServHosp to set
     */
    public void setListaServHosp(List<ServicioPrestado> value) {
        this.oListaServHosp = value;
    }

    /**
     * @return the oExtSeleccionado
     */
    public ServicioPrestado getExtSeleccionado() {
        return oExtSeleccionado;
    }

    /**
     * @param oExtSeleccionado the oExtSeleccionado to set
     */
    public void setExtSeleccionado(ServicioPrestado value) {
        this.oExtSeleccionado = value;
    }

    /**
     * @return the oHospSeleccionado
     */
    public ServicioPrestado getHospSeleccionado() {
        return oHospSeleccionado;
    }

    /**
     * @param oHospSeleccionado the oHospSeleccionado to set
     */
    public void setHospSeleccionado(ServicioPrestado value) {
        this.oHospSeleccionado = value;
    }

    /**
     * @return the oSPDsctoSelec
     */
    public ServicioPrestado getSPDsctoSelec() {
        return oSPDsctoSelec;
    }

    /**
     * @param oSPDsctoSelec the oSPDsctoSelec to set
     */
    public void setSPDsctoSelec(ServicioPrestado value) {
        this.oSPDsctoSelec = value;
    }
    
    public Paciente getPaciente() {
        return oPac;
    }
    
    public void setPaciente(Paciente value) {
        this.oPac = value;
    }
    
    public boolean getBotonServExt(){
        return this.bBotonServExt;
    }
    
    public boolean getBotonServHosp(){
        return this.bBotonServHosp;
    }
    
    public String getConceptoDespliegue(){
    String sRet="";
        if (oCredPart.getServPres() != null &&
            oCredPart.getEpiMed() != null)
            if (oCredPart.getEpiMed().getCveepisodio()==0)
                sRet = oCredPart.getServPres().getConcepPrestado().getDescripConcep();
            else
                sRet = "CUENTA DE HOSPITAL";
        System.out.println(sRet);
        return sRet;
    }

    /**
     * @return the nPagoIni
     */
    public float getPagoIni() {
        return nPagoIni;
    }

    /**
     * @param nPagoIni the nPagoIni to set
     */
    public void setPagoIni(float nPagoIni) {
        this.nPagoIni = nPagoIni;
    }

    /**
     * @return the nCantPagares
     */
    public int getCantPagares() {
        return nCantPagares;
    }

    /**
     * @param nCantPagares the nCantPagares to set
     */
    public void setCantPagares(int nCantPagares) {
        this.nCantPagares = nCantPagares;
    }
    
    //JMHG
    public Date getFechaActual()
    {
        return dFechaActual;
}
    
    public void setFechaActual( Date dFechaActual )
    {
        this.dFechaActual = dFechaActual;
    }
    //----
}
