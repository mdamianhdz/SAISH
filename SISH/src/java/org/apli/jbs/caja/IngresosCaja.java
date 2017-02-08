package org.apli.jbs.caja;

import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.apli.jbs.UsuarioJB;
import org.apli.modelbeans.EpisodioMedico;
import org.apli.modelbeans.FormaPago;
import org.apli.modelbeans.OperacionCaja;
import org.apli.modelbeans.PersonalHospitalario;
import org.apli.modelbeans.ServicioPrestado;
import org.apli.modelbeans.Usuario;
import org.apli.modelbeans.Valida;
import org.primefaces.context.RequestContext;

/**
 * Clase para el manejo de información común de los ingresos en caja
 * @author BAOZ
 */
public class IngresosCaja implements Serializable{
    protected String sCveAutDscto="";
    protected String sCveAutPagoMenor="";
    protected boolean bAutDscto;
    protected boolean bAutPagoMenor;
    protected boolean bAutorizado = true;
    protected boolean bTransferencia = false;
    protected boolean bCheque = false;
    protected String sForma = "";
    protected ServicioPrestado selectedServ = new ServicioPrestado();
    protected OperacionCaja oOpeCaja;
    protected PersonalHospitalario oPersAutDscto;
    protected PersonalHospitalario oPersAutParcial;
    
    protected boolean bVerBanco;
    protected boolean bVerCtaBanco;
    protected boolean bVerFecRecep;
    protected boolean bVerNumDcto;
    protected boolean bVerTitularTarjeta;
    protected boolean bBotonPagar;
    
    public IngresosCaja(){
    Usuario oUsr;
        try{
            bBotonPagar=false;
            oUsr = new UsuarioJB().getSesionUsuario();
            oOpeCaja = new OperacionCaja();
            oOpeCaja.setFrmPago(new FormaPago());
            oOpeCaja.setPersCapt((new PersonalHospitalario().buscaPersonalPorCveUsuario(oUsr.getUsuario())));
            oPersAutDscto = new PersonalHospitalario();
            oPersAutParcial = new PersonalHospitalario();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void validaForma() {
        if (sForma.startsWith(FormaPago.CVE_CHQ)){
            this.bVerBanco = true;
            this.bVerCtaBanco = false;
            this.bVerFecRecep = false;
            this.bVerNumDcto = true;
            this.bVerTitularTarjeta = false;
        } else if (sForma.startsWith(FormaPago.CVE_DEP)||
                   sForma.startsWith(FormaPago.CVE_TRE)){
            this.bVerBanco = false;
            this.bVerCtaBanco = true;
            this.bVerFecRecep = true;
            this.bVerNumDcto = false;
            this.bVerTitularTarjeta = false;
        }else if (sForma.startsWith(FormaPago.CVE_TDC)||
                  sForma.startsWith(FormaPago.CVE_TDD)){
            this.bVerBanco = true;
            this.bVerCtaBanco = false;
            this.bVerFecRecep = false;
            this.bVerNumDcto = true;
            this.bVerTitularTarjeta = true;
        }else{
            this.bVerBanco = false;
            this.bVerCtaBanco = false;
            this.bVerFecRecep = false;
            this.bVerNumDcto = false;
            this.bVerTitularTarjeta = false;
        }
        
        int result = sForma.compareToIgnoreCase("CHQ  ");
        int result2 = sForma.compareToIgnoreCase("TRE  ");
        if (result == 0) {
            bCheque = true;
            bTransferencia = false;
        } else if (result2 == 0) {
            bTransferencia = true;
            bCheque = false;
        } else {
            bCheque = false;
            bTransferencia = false;
        }
    }
    
    public void seleccion(){
        if (selectedServ == null||
            selectedServ.getIdFolio()==null || selectedServ.getIdFolio().equals("")) {
            FacesMessage msg = new FacesMessage("Aviso", "Falta seleccionar pago");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('selDialog').show()");
        }
    }
    
    public float getDesctoTicket(){
    float nRet = 0.0f;
        nRet = this.selectedServ.getCostoCobrado() - this.selectedServ.getCostoOriginal();
        
        return nRet;
    }
    
     public boolean isAutorizado(){
        return this.bAutorizado;
    }
    
    public boolean getDeshabilitaCapturaMonto(){
    boolean bRet = true;
        if (bAutDscto || bAutPagoMenor)
            bRet = false;
        return bRet;
    }
    
    public String getForma() {
        return sForma;
    }

    public void setForma(String sForma) {
        this.sForma = sForma;
    }
    
    public boolean isTransferencia() {
        return bTransferencia;
    }
    
    public void setTransferencia(boolean bTransferencia) {
        this.bTransferencia = bTransferencia;
    }
    
    public boolean isCheque() {
        return bCheque;
    }
    
    public void setCheque(boolean bCheque) {
        this.bCheque = bCheque;
    }
    
    public boolean isVerBanco() {
        return bVerBanco;
    }
    
    public void setVerBanco(boolean bVerBanco) {
        this.bVerBanco = bVerBanco;
    }
    
    public boolean isVerCtaBanco() {
        return bVerCtaBanco;
    }
    
    public void setVerCtaBanco(boolean bVerCtaBanco) {
        this.bVerCtaBanco = bVerCtaBanco;
    }

    public boolean isVerFecRecep() {
        return bVerFecRecep;
    }
    
    public void setVerFecRecep(boolean bVerFecRecep) {
        this.bVerFecRecep = bVerFecRecep;
    }
    
    public boolean isVerNumDcto() {
        return bVerNumDcto;
    }
    
    public void setVerNumDcto(boolean bVerNumDcto) {
        this.bVerNumDcto = bVerNumDcto;
    }
    
    public boolean isVerTitularTarjeta() {
        return bVerTitularTarjeta;
    }
    
    public void setVerTitularTarjeta(boolean bVerTitularTarjeta) {
        this.bVerTitularTarjeta = bVerTitularTarjeta;
    }
    
    public ServicioPrestado getSelectedServ() {
        return selectedServ;
    }
    
    public void setSelectedServ(ServicioPrestado selectedServ) {
        this.selectedServ = selectedServ;
    }
    
    public double obtenerCifra(String param) {
        if (this.selectedServ != null) {
            if (param.equals("iva")) {
                return new Valida().obtieneIva(this.selectedServ.getCostoOriginal(), this.selectedServ.getPctIVA());
            } else if ((param.equals("subtotal"))) {
                return new Valida().obtieneSubtotal(this.selectedServ.getCostoOriginal(), this.selectedServ.getPctIVA());
            }
        }
        return 0;
    }
    
    public String getDescripcionForma() throws Exception {
    FormaPago fp = new FormaPago();
    String s="";
        if (!sForma.equals("")){            
            fp.setCveFrmPago(sForma);
            fp.buscar();
            s = fp.getDescripcion();
        }
        return s;
    }

    public String getDescripcionEmpresa() {
    String sDesc="NO IDENTIFICADO";
        if (this.selectedServ != null && 
            this.selectedServ.getConvQuienPaga() != null && 
           !this.selectedServ.getConvQuienPaga().equals("")) {
            if (this.selectedServ.getConvQuienPaga().equalsIgnoreCase("Particular")) {
                sDesc= "PARTICULAR";
            } else if (this.selectedServ.getConvQuienPaga().equalsIgnoreCase("Empresa")) {
                if (this.selectedServ != null && this.selectedServ.getCompaniaCred() != null && this.selectedServ.getCompaniaCred().getNombreCorto() != null) {
                    sDesc= this.selectedServ.getCompaniaCred().getNombreCorto();
                } 
            }
        }
        return sDesc;
    }

    public String getMedicoTratante() throws Exception {
        EpisodioMedico episodioMedico = new EpisodioMedico();
        if (this.selectedServ != null && this.selectedServ.getPaciente() != null && this.selectedServ.getPaciente().getFolioPac() != 0) {
            episodioMedico.tieneEpisodioMedicoPaciente(this.selectedServ.getPaciente().getFolioPac());
            this.selectedServ.setMedico(episodioMedico.getMedTratante());
            return this.selectedServ.getMedico().getNombreCompleto();
        } else {
            return "NO IDENTIFICADO";
        }
    }
    
    public void setOpeCaja(OperacionCaja value){
        this.oOpeCaja = value;
    }
    
    public OperacionCaja getOpeCaja(){
        return this.oOpeCaja;
    }
    
    public void habilitaBoton(){
        if (this.selectedServ==null)
            bBotonPagar=true;
        else
            bBotonPagar=false;
    }
    
    public boolean getBotonPagar(){
        return this.bBotonPagar;
    }
}
