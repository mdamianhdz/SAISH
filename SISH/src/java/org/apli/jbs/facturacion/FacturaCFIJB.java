package org.apli.jbs.facturacion;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.apli.configuracion.Configuracion;
import org.apli.modelbeans.facturacion.cfdi.FacturaCFI;
import org.apli.timbradoCFDI.Timbrado;
/**
 * @author Isabel Espinoza Espinoza
 */
@ManagedBean(name="oFacturaCFI")
@SessionScoped
public class FacturaCFIJB  implements Serializable{

    private FacturaCFI oFactura = new FacturaCFI();
    private String msjError = "";
    private FacesMessage msj = new FacesMessage();
    boolean bValida = true;
    private String sContrasenia;
    private boolean bCancelacionValida;

    public void mostrarFactura() throws Exception {
        if (oFactura.getNombreSerie() == null | oFactura.getNombreSerie().equals("")) {
            msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR: No ha indicado la serie", "");
            FacesContext.getCurrentInstance().addMessage(null, msj);
            bValida = false;
        }
        if (oFactura.getFolio() == 0) {
            msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR: No ha indicado el folio", "");
            FacesContext.getCurrentInstance().addMessage(null, msj);
            bValida = false;
        }
        if (bValida) {
            msjError = oFactura.buscarFacturaParaCancelar();
        }
        if (msjError.contains("ERROR")) {
            msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, msjError, "");
            FacesContext.getCurrentInstance().addMessage(null, msj);
            limpiar();
        } else {
            this.bCancelacionValida = true;
        }
    }

    public void cancelarFactura() throws Exception {
        mostrarFactura();
        if (!this.sContrasenia.equals("")) {
            if (oFactura.getRfcEmisor() != null) {
                Timbrado timbrado = new Timbrado(oFactura.getRfcEmisor(), oFactura.getRfcReceptor(), Configuracion.usuarioTimbrado, Configuracion.passwordTimbrado,oFactura.getNombreSerie(),""+oFactura.getFolio());
                timbrado.setUUID(oFactura.getUuid());
                timbrado.setPasswordEmisor(sContrasenia);
                //Descomentar la siguiente línea para que se cancele usando el Servicio Web del PAC
                //if(timbrado.solicitarCancelacionCFDI()){     
                oFactura.generarQueryParaCancelarCFDI();
                msjError = oFactura.ejecutarQuery();
                if (msjError.contains("ERROR")) {
                    msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, msjError, "");
                    FacesContext.getCurrentInstance().addMessage(null, msj);
                } else {
                    FacesContext context = FacesContext.getCurrentInstance();
                    context.addMessage(null, new FacesMessage("", "La factura correspondiente ha sido cancelada"));
                    context.getExternalContext().getFlash().setKeepMessages(true);
                }
                //}
            }
            limpiar();
        } else {
            msj = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Proporcione una contraseña", "");
            FacesContext.getCurrentInstance().addMessage(null, msj);
        }
    }

    public void limpiar() throws Exception {
        oFactura = new FacturaCFI();
        msjError = "";
        msj = new FacesMessage();
        bValida = true;
        sContrasenia = "";
        bCancelacionValida = false;
    }

    public FacturaCFI getFactura() {
        return oFactura;
    }

    public void setFactura(FacturaCFI oFactura) {
        this.oFactura = oFactura;
    }

    public boolean isCancelacionValida() {
        return bCancelacionValida;
    }

    public void setCancelacionValida(boolean cancelacionValida) {
        this.bCancelacionValida = cancelacionValida;
    }

    public String getContrasenia() {
        return sContrasenia;
    }

    public void setContrasenia(String sContrasenia) {
        this.sContrasenia = sContrasenia;
    }
}
