package org.apli.jbs.facturacion;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import org.apli.modelbeans.Ciudad;
import org.apli.modelbeans.CiudadCP;
import org.apli.modelbeans.Estado;
import org.apli.modelbeans.Municipio;
import org.apli.modelbeans.Pais;
import org.apli.modelbeans.facturacion.cfdi.v32.schema.Comprobante.Emisor;
import org.apli.modelbeans.facturacion.cfdi.v32.schema.Comprobante.Emisor.RegimenFiscal;
import org.apli.modelbeans.facturacion.cfdi.v32.schema.EmisorRegimenFiscal;
import org.apli.modelbeans.facturacion.cfdi.v32.schema.TUbicacion;
import org.apli.modelbeans.facturacion.cfdi.v32.schema.TUbicacionFiscal;

/**
 *
 * @author lleon
 */
@ManagedBean(name="oAdmEmisor")
@RequestScoped

public class AdmEmisoresJB implements Serializable{
    private static Emisor oSelectedEmisor;
    private static Emisor oNuevoEmisor;
    private List<Emisor> listEmisores; 
    private boolean bCheckDir;
    public CiudadCP oCiudadCP;
    private Estado oSelEstado;
    private static boolean bBandNuevoEmisor;
    private static List<String> listRegFis;
    private static String sSelectedRegimen;

    public AdmEmisoresJB() throws Exception{
        listEmisores=new ArrayList();
        llenaLista();
    }
    
    public void llenaLista() throws Exception{
        Emisor oEm=new Emisor();
        listEmisores=oEm.buscaTodos();
        EmisorRegimenFiscal[] arrRegFis = EmisorRegimenFiscal.values();
        List<String> listTemp= new ArrayList<String>(arrRegFis.length);
        for (EmisorRegimenFiscal arrRF : arrRegFis) {
            listTemp.add(arrRF.getRegimen());
        }
        this.setRegimenesFis(listTemp);
        if (bBandNuevoEmisor==true){
            for (int i=this.getRegimenesFis().size()-1; i>=0; i--){
                for (int j=0; j<oNuevoEmisor.getRegimenFiscal().size();j++){
                    if(oNuevoEmisor.getRegimenFiscal().get(j).getRegimen().equals(getRegimenesFis().get(i)))
                        listRegFis.remove(i);
                }
            }
        }else{
            if(oSelectedEmisor!=null){
                for (int i=this.getRegimenesFis().size()-1; i>=0; i--){
                    for (int j=0; j<oSelectedEmisor.getRegimenFiscal().size();j++){
                        if(oSelectedEmisor.getRegimenFiscal().get(j).getRegimen().equals(getRegimenesFis().get(i)))
                            listRegFis.remove(i);
                    }
                }
            }
        }
        this.setRegimenesFis(listTemp);
    }
    
    public void confNuevo() throws Exception{
        System.out.println("confNuevo");
        this.setBandNuevoEmisor(true);
        this.setNuevoEmisor(nuevoEm());
    }
    
    public void confModifica() throws Exception{
        System.out.println("confModifica");
        this.setBandNuevoEmisor(false);
    }

    public void setListaEmisores(List<Emisor> listEmisores){
        this.listEmisores=listEmisores;
    }
    
    public List<Emisor> getListaEmisores(){
        return listEmisores;
    }

    public Emisor getSelectedEmisor() {
        return AdmEmisoresJB.oSelectedEmisor;
    }

    public void setSelectedEmisor(Emisor oSelectedEmisor) throws Exception {
        AdmEmisoresJB.oSelectedEmisor = oSelectedEmisor;
        cargaDatos();
        llenaLista();
        for (int i=this.getRegimenesFis().size()-1; i>=0; i--){
            for (int j=0; j<oSelectedEmisor.getRegimenFiscal().size();j++){
                if(oSelectedEmisor.getRegimenFiscal().get(j).getRegimen().equals(getRegimenesFis().get(i)))
                    listRegFis.remove(i);
            }
        }
        sSelectedRegimen="";
    }

    public Emisor getNuevoEmisor() {
        return AdmEmisoresJB.oNuevoEmisor;
    }

    public void setNuevoEmisor(Emisor oNuevoEmisor) throws Exception {
        AdmEmisoresJB.oNuevoEmisor = oNuevoEmisor;
        llenaLista();
    }

    public boolean isBandNuevoEmisor() {
        return AdmEmisoresJB.bBandNuevoEmisor;
    }

    public void setBandNuevoEmisor(boolean bNuevoEmisor) {
        AdmEmisoresJB.bBandNuevoEmisor = bNuevoEmisor;
    }

    public List<Estado> getListEdoDF() {
        return EstadoJB.listEstadosDF;
    }

    public void setListEdoDF(List<Estado> listEdoDF) {
        EstadoJB.listEstadosDF = listEdoDF;
    }

    public List<Municipio> getListMunDF() {
        return MunicipioJB.listMunicipiosDF;
    }

    public void setListMunDF(List<Municipio> listMunDF) {
        MunicipioJB.listMunicipiosDF = listMunDF;
    }

    public List<Ciudad> getListCdDF() {
        return CiudadJB.listCiudadesDF;
    }

    public void setListCdDF(List<Ciudad> listCdDF) {
        CiudadJB.listCiudadesDF = listCdDF;
    }
    
    public List<Estado> getListEdoLE() {
        return EstadoJB.listEstadosLE;
    }

    public void setListEdoLE(List<Estado> listEdoLE) {
        EstadoJB.listEstadosLE = listEdoLE;
    }

    public List<Municipio> getListMunLE() {
        return MunicipioJB.listMunicipiosLE;
    }

    public void setListMunLE(List<Municipio> listMunLE) {
        MunicipioJB.listMunicipiosLE = listMunLE;
    }

    public List<Ciudad> getListCdLE() {
        return CiudadJB.listCiudadesLE;
    }

    public void setListCdLE(List<Ciudad> listCdLE) {
        CiudadJB.listCiudadesLE = listCdLE;
    }

    public Estado getSelEstado() {
        return oSelEstado;
    }

    public void setSelEstado(Estado oSelEstado) {
        this.oSelEstado = oSelEstado;
    }

    public boolean isCheckDir() {
        if (this.isBandNuevoEmisor()==true){
            if (oNuevoEmisor!=null)
                if (oNuevoEmisor.getDom_fiscal_dom_exp()=='1')
                    return true;
                else
                    return false;
            else
                return bCheckDir;
        }else{
            if (oSelectedEmisor!=null)
                if (oSelectedEmisor.getDom_fiscal_dom_exp()=='0')
                    return false;
                else
                    return true;
            else
                return bCheckDir;
        }
    }

    public void setCheckDir(boolean bCheckDir) {
        if (this.isBandNuevoEmisor()==true){
            if (bCheckDir==false)
                oNuevoEmisor.setDom_fiscal_dom_exp('0');
            else
                oNuevoEmisor.setDom_fiscal_dom_exp('1');
            this.bCheckDir = bCheckDir;
        }else{
            if (bCheckDir==false)
                oSelectedEmisor.setDom_fiscal_dom_exp('0');
            else
                oSelectedEmisor.setDom_fiscal_dom_exp('1');
            this.bCheckDir = bCheckDir;
        }
    }

    public String getSelectedRegimen() {
        return sSelectedRegimen;
    }

    public void setSelectedRegimen(String sSelectedRegimen) {
        AdmEmisoresJB.sSelectedRegimen = sSelectedRegimen;
    }
    
    
    public void validaMismaDireccion(String var){
        if ("nuevo".equals(var)){
            if (isCheckDir()){
                mismoDFyLE(oNuevoEmisor.getDomicilioFiscal(), oNuevoEmisor.getExpedidoEn());
            //    oNuevoEmisor.setExpedidoEn(oNuevoEmisor.getDomicilioFiscal());
                EstadoJB.listEstadosLE=EstadoJB.listEstadosDF;
                MunicipioJB.listMunicipiosLE=MunicipioJB.listMunicipiosDF;
                CiudadJB.listCiudadesLE=CiudadJB.listCiudadesDF;
            }else{
                oNuevoEmisor.setExpedidoEn(new TUbicacion());
                EstadoJB.listEstadosLE=new ArrayList();
                MunicipioJB.listMunicipiosLE=new ArrayList();
                CiudadJB.listCiudadesLE=new ArrayList();
            }
        }else{
            if (isCheckDir()){
                mismoDFyLE(oSelectedEmisor.getDomicilioFiscal(), oSelectedEmisor.getExpedidoEn());
                //oSelectedEmisor.setExpedidoEn(oSelectedEmisor.getDomicilioFiscal());
                EstadoJB.listEstadosLE=EstadoJB.listEstadosDF;
                MunicipioJB.listMunicipiosLE=MunicipioJB.listMunicipiosDF;
                CiudadJB.listCiudadesLE=CiudadJB.listCiudadesDF;
            }else{
                oSelectedEmisor.setExpedidoEn(new TUbicacion());
                EstadoJB.listEstadosLE=new ArrayList();
                MunicipioJB.listMunicipiosLE=new ArrayList();
                CiudadJB.listCiudadesLE=new ArrayList();
            }
        }
    }
    
    public List<String> getRegimenesFis() throws Exception{
        return listRegFis;
    } 
    
    public void setRegimenesFis(List<String> listRegFis){
        AdmEmisoresJB.listRegFis=listRegFis;
    }
    
    public void cargaDatos()throws Exception{
        oCiudadCP=new CiudadCP();
        setListEdoDF(oCiudadCP.buscaPorCP(oSelectedEmisor.getDomicilioFiscal().getCodigoPostal()));
        for (int i=0; i<getListEdoDF().size();i++){
           if(getListEdoDF().get(i).getCve().equals(oSelectedEmisor.getDomicilioFiscal().getEdo().getCve()))
               getListEdoDF().remove(i);
        }
        this.setListEdoLE(oCiudadCP.buscaPorCP(oSelectedEmisor.getExpedidoEn().getCodigoPostal()));
        for (int i=0; i<getListEdoLE().size();i++){
           if(getListEdoLE().get(i).getCve().equals(oSelectedEmisor.getExpedidoEn().getEdo().getCve()))
               getListEdoLE().remove(i);
        }
        this.setListMunDF(oCiudadCP.buscaPorEdo(oSelectedEmisor.getDomicilioFiscal().getCodigoPostal(),
                oSelectedEmisor.getDomicilioFiscal().getEdo().getCve()));
        for (int i=0; i<getListMunDF().size();i++){
           if(getListMunDF().get(i).getCve().equals(oSelectedEmisor.getDomicilioFiscal().getMun().getCve()))
               getListMunDF().remove(i);
        }
        this.setListMunLE(oCiudadCP.buscaPorEdo(oSelectedEmisor.getExpedidoEn().getCodigoPostal(),
                oSelectedEmisor.getExpedidoEn().getEdo().getCve()));
        for (int i=0; i<getListMunLE().size();i++){
           if(getListMunLE().get(i).getCve().equals(oSelectedEmisor.getExpedidoEn().getMun().getCve()))
               getListMunLE().remove(i);
        }
        this.setListCdDF(oCiudadCP.buscaPorMun(oSelectedEmisor.getDomicilioFiscal().getCodigoPostal(), 
                oSelectedEmisor.getDomicilioFiscal().getEdo().getCve(),
                oSelectedEmisor.getDomicilioFiscal().getMun().getCve()));
        for (int i=0; i<getListCdDF().size();i++){
           if(getListCdDF().get(i).getCve().equals(oSelectedEmisor.getDomicilioFiscal().getCd().getCve()))
               getListCdDF().remove(i);
        }
        this.setListCdLE(oCiudadCP.buscaPorMun(oSelectedEmisor.getExpedidoEn().getCodigoPostal(), 
                oSelectedEmisor.getExpedidoEn().getEdo().getCve(),
                oSelectedEmisor.getExpedidoEn().getMun().getCve()));
        for (int i=0; i<getListCdLE().size();i++){
           if(getListCdLE().get(i).getCve().equals(oSelectedEmisor.getExpedidoEn().getCd().getCve()))
               getListCdLE().remove(i);
        }
    }
    public void buscaDomFisPorCP(String var) throws Exception{
        if ("nuevo".equals(var)){
            oNuevoEmisor.getDomicilioFiscal().getEdo().setDescrip("");
            oNuevoEmisor.getDomicilioFiscal().getMun().setDescrip("");
            oNuevoEmisor.getDomicilioFiscal().getCd().setDescrip("");
            oCiudadCP=new CiudadCP();
            EstadoJB.listEstadosDF=oCiudadCP.buscaPorCP(oNuevoEmisor.getDomicilioFiscal().getCodigoPostal());
            MunicipioJB.listMunicipiosDF=new ArrayList();
            CiudadJB.listCiudadesDF=new ArrayList();
            oNuevoEmisor.getDomicilioFiscal().setObjPais(oCiudadCP.buscaPais(oNuevoEmisor.getDomicilioFiscal().getCodigoPostal()));
            if(oNuevoEmisor.getDom_fiscal_dom_exp()=='1'){
                System.out.println("busca por CP: mismas direcciones");
                mismoDFyLE(oNuevoEmisor.getDomicilioFiscal(), oNuevoEmisor.getExpedidoEn());
               // oNuevoEmisor.setExpedidoEn(oNuevoEmisor.getDomicilioFiscal());
                EstadoJB.listEstadosLE=EstadoJB.listEstadosDF;
                MunicipioJB.listMunicipiosLE=new ArrayList();
                CiudadJB.listCiudadesLE=new ArrayList(); 
            }
        }else{
            oSelectedEmisor.getDomicilioFiscal().getEdo().setDescrip("");
            oSelectedEmisor.getDomicilioFiscal().getMun().setDescrip("");
            oSelectedEmisor.getDomicilioFiscal().getCd().setDescrip("");
            oCiudadCP=new CiudadCP();
            EstadoJB.listEstadosDF=oCiudadCP.buscaPorCP(oSelectedEmisor.getDomicilioFiscal().getCodigoPostal());
            MunicipioJB.listMunicipiosDF=new ArrayList();
            CiudadJB.listCiudadesDF=new ArrayList();
            oSelectedEmisor.getDomicilioFiscal().setObjPais(oCiudadCP.buscaPais(oSelectedEmisor.getDomicilioFiscal().getCodigoPostal()));
            if(oSelectedEmisor.getDom_fiscal_dom_exp()=='1'){
                mismoDFyLE(oSelectedEmisor.getDomicilioFiscal(), oSelectedEmisor.getExpedidoEn());
                // oSelectedEmisor.setExpedidoEn(oSelectedEmisor.getDomicilioFiscal());
                EstadoJB.listEstadosLE=EstadoJB.listEstadosDF;
                MunicipioJB.listMunicipiosLE=new ArrayList();
                CiudadJB.listCiudadesLE=new ArrayList(); 
            }
            
        }
    }
    
    public void buscaLugExpPorCP(String var) throws Exception{
        if ("nuevo".equals(var)){
            oNuevoEmisor.getExpedidoEn().getEdo().setDescrip("");
            oNuevoEmisor.getExpedidoEn().getMun().setDescrip("");
            oNuevoEmisor.getExpedidoEn().getCd().setDescrip("");
            oCiudadCP=new CiudadCP();
            EstadoJB.listEstadosLE=oCiudadCP.buscaPorCP(oNuevoEmisor.getExpedidoEn().getCodigoPostal());
            MunicipioJB.listMunicipiosLE=new ArrayList();
            CiudadJB.listCiudadesLE=new ArrayList(); 
            oNuevoEmisor.getExpedidoEn().setObjPais(oCiudadCP.buscaPais(oNuevoEmisor.getExpedidoEn().getCodigoPostal()));
        }else{
            oSelectedEmisor.getExpedidoEn().getEdo().setDescrip("");
            oSelectedEmisor.getExpedidoEn().getMun().setDescrip("");
            oSelectedEmisor.getExpedidoEn().getCd().setDescrip("");
            oCiudadCP=new CiudadCP();
            EstadoJB.listEstadosLE=oCiudadCP.buscaPorCP(oSelectedEmisor.getExpedidoEn().getCodigoPostal());
            MunicipioJB.listMunicipiosLE=new ArrayList();
            CiudadJB.listCiudadesLE=new ArrayList(); 
            oSelectedEmisor.getExpedidoEn().setObjPais(oCiudadCP.buscaPais(oSelectedEmisor.getExpedidoEn().getCodigoPostal()));
        } 
    }
    
    public void buscarDomFisPorEdo(String var)throws Exception{
        if ("nuevo".equals(var)){
            oNuevoEmisor.getDomicilioFiscal().getMun().setDescrip("");
            oNuevoEmisor.getDomicilioFiscal().getCd().setDescrip("");
            oCiudadCP=new CiudadCP();
            MunicipioJB.listMunicipiosDF=oCiudadCP.buscaPorEdo(oNuevoEmisor.getDomicilioFiscal().getCodigoPostal(), 
                    oNuevoEmisor.getDomicilioFiscal().getEdo().getCve());
            CiudadJB.listCiudadesDF=new ArrayList();
            if(oNuevoEmisor.getDom_fiscal_dom_exp()=='1'){
                mismoDFyLE(oNuevoEmisor.getDomicilioFiscal(), oNuevoEmisor.getExpedidoEn());
              //   oNuevoEmisor.setExpedidoEn(oNuevoEmisor.getDomicilioFiscal());
                MunicipioJB.listMunicipiosLE=MunicipioJB.listMunicipiosDF;
                CiudadJB.listCiudadesLE=new ArrayList(); 
            }
        }else{
            oSelectedEmisor.getDomicilioFiscal().getMun().setDescrip("");
            oSelectedEmisor.getDomicilioFiscal().getCd().setDescrip("");
            oCiudadCP=new CiudadCP();
            MunicipioJB.listMunicipiosDF=oCiudadCP.buscaPorEdo(oSelectedEmisor.getDomicilioFiscal().getCodigoPostal(), 
                    oSelectedEmisor.getDomicilioFiscal().getEdo().getCve());
            CiudadJB.listCiudadesDF=new ArrayList();
            System.out.println(oSelectedEmisor.getDom_fiscal_dom_exp());
            if(oSelectedEmisor.getDom_fiscal_dom_exp()=='1'){
                mismoDFyLE(oSelectedEmisor.getDomicilioFiscal(), oSelectedEmisor.getExpedidoEn());
                //oSelectedEmisor.setExpedidoEn(oSelectedEmisor.getDomicilioFiscal());
                MunicipioJB.listMunicipiosLE=MunicipioJB.listMunicipiosDF;
                CiudadJB.listCiudadesLE=new ArrayList(); 
            } 
        }
    }
    
    public void buscarLugExpPorEdo(String var)throws Exception{
        if ("nuevo".equals(var)){
            oNuevoEmisor.getExpedidoEn().getMun().setDescrip("");
            oNuevoEmisor.getExpedidoEn().getCd().setDescrip("");
            oCiudadCP=new CiudadCP();
            MunicipioJB.listMunicipiosLE=oCiudadCP.buscaPorEdo(oNuevoEmisor.getExpedidoEn().getCodigoPostal(), 
                    oNuevoEmisor.getExpedidoEn().getEdo().getCve());
            CiudadJB.listCiudadesLE=new ArrayList();
        }else{
            oSelectedEmisor.getExpedidoEn().getMun().setDescrip("");
            oSelectedEmisor.getExpedidoEn().getCd().setDescrip("");
            oCiudadCP=new CiudadCP();
            MunicipioJB.listMunicipiosLE=oCiudadCP.buscaPorEdo(oSelectedEmisor.getExpedidoEn().getCodigoPostal(), 
                    oSelectedEmisor.getExpedidoEn().getEdo().getCve());
            CiudadJB.listCiudadesLE=new ArrayList();
        }
    }
    
    public void buscarDomFisPorMun(String var)throws Exception{
        if ("nuevo".equals(var)){
            oNuevoEmisor.getDomicilioFiscal().getCd().setDescrip("");
            oCiudadCP=new CiudadCP();
            CiudadJB.listCiudadesDF=oCiudadCP.buscaPorMun(oNuevoEmisor.getDomicilioFiscal().getCodigoPostal(), 
                    oNuevoEmisor.getDomicilioFiscal().getEdo().getCve(),
                    oNuevoEmisor.getDomicilioFiscal().getMun().getCve());
            if(oNuevoEmisor.getDom_fiscal_dom_exp()=='1'){
                mismoDFyLE(oNuevoEmisor.getDomicilioFiscal(), oNuevoEmisor.getExpedidoEn());
               // oNuevoEmisor.setExpedidoEn(oNuevoEmisor.getDomicilioFiscal());
                CiudadJB.listCiudadesLE=CiudadJB.listCiudadesDF;
            } 
        }else{
            oSelectedEmisor.getDomicilioFiscal().getCd().setDescrip("");
            oCiudadCP=new CiudadCP();
            CiudadJB.listCiudadesDF=oCiudadCP.buscaPorMun(oSelectedEmisor.getDomicilioFiscal().getCodigoPostal(), 
                    oSelectedEmisor.getDomicilioFiscal().getEdo().getCve(),
                    oSelectedEmisor.getDomicilioFiscal().getMun().getCve());
            if(oSelectedEmisor.getDom_fiscal_dom_exp()=='1'){
                mismoDFyLE(oSelectedEmisor.getDomicilioFiscal(), oSelectedEmisor.getExpedidoEn());
          //        oSelectedEmisor.setExpedidoEn(oSelectedEmisor.getDomicilioFiscal());
                CiudadJB.listCiudadesLE=CiudadJB.listCiudadesDF;
            } 
        }
    }
    
    public void buscarLugExpPorMun(String var)throws Exception{
        if ("nuevo".equals(var)){
            oNuevoEmisor.getExpedidoEn().getCd().setDescrip("");
            oCiudadCP=new CiudadCP();
            CiudadJB.listCiudadesLE=oCiudadCP.buscaPorMun(oNuevoEmisor.getExpedidoEn().getCodigoPostal(), 
                    oNuevoEmisor.getExpedidoEn().getEdo().getCve(),
                    oNuevoEmisor.getExpedidoEn().getMun().getCve());
        }else{
            oSelectedEmisor.getExpedidoEn().getCd().setDescrip("");
            oCiudadCP=new CiudadCP();
            CiudadJB.listCiudadesLE=oCiudadCP.buscaPorMun(oSelectedEmisor.getExpedidoEn().getCodigoPostal(), 
                    oSelectedEmisor.getExpedidoEn().getEdo().getCve(),
                    oSelectedEmisor.getExpedidoEn().getMun().getCve());
        }
    }
    
    public void copiaDatos(String var){
        if ("nuevo".equals(var)){
            if(oNuevoEmisor.getDom_fiscal_dom_exp()=='1'){
                mismoDFyLE(oNuevoEmisor.getDomicilioFiscal(), oNuevoEmisor.getExpedidoEn());
               // oNuevoEmisor.setExpedidoEn(oNuevoEmisor.getDomicilioFiscal());
            }
        }else{
            if(oSelectedEmisor.getDom_fiscal_dom_exp()=='1'){
                mismoDFyLE(oSelectedEmisor.getDomicilioFiscal(), oSelectedEmisor.getExpedidoEn());
              //  oSelectedEmisor.setExpedidoEn(oSelectedEmisor.getDomicilioFiscal());
            }
        }
    }
    
    public void agregaRF(String var) throws Exception{
        System.out.println(this.getSelectedRegimen());
        RegimenFiscal r=new RegimenFiscal();
        r.setRegimen(this.getSelectedRegimen());
        if ("nuevo".equals(var))
            oNuevoEmisor.getRegimenFiscal().add(r); 
        else
            oSelectedEmisor.getRegimenFiscal().add(r); 
        getRegimenesFis().remove(getSelectedRegimen());
    }
    
    public void eliminaRegFis(String em, String var) throws Exception{
        System.out.println("eliminaRegFis: '"+em+"'-'"+var+"'");
        RegimenFiscal r=new RegimenFiscal();
        r.setRegimen(var);
        System.out.println("Régimen:'"+r.getRegimen()+"'");
        if ("nuevo".equals(em)){
            oNuevoEmisor.getRegimenFiscal().remove(r); 
        }else{
            System.out.println("reg:"+r.getRegimen());
            int index=0;
            for (int i=0; i<oSelectedEmisor.getRegimenFiscal().size(); i++){
                if (oSelectedEmisor.getRegimenFiscal().get(i).getRegimen().equals(var))
                    index=i;
            }
            oSelectedEmisor.getRegimenFiscal().remove(index);
        }
        getRegimenesFis().add(var);
    }
    
    public void buscaRFC(String var) throws Exception{
        boolean ret;
        if ("nuevo".equals(var)){
            ret=new Emisor().buscaEmisor(oNuevoEmisor.getRfc());
            if (ret==true){
                String mess="Ya existe un emisor registrado con el RFC: "+oNuevoEmisor.getRfc();
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage("ERROR", mess));
                oNuevoEmisor.setRfc("");
            }
        }else{
            ret=new Emisor().buscaEmisor(oSelectedEmisor.getRfc());
            if (ret==true){
                String mess="Ya existe un emisor registrado con el RFC: "+oNuevoEmisor.getRfc();
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage("ERROR", mess));
                oSelectedEmisor.setRfc("");
            }
        }
            
    }
    
    public void guardar(String var) throws Exception{
        System.out.println("guardar "+var);
        String mess;
        Emisor oEm=new Emisor();
        if ("nuevo".equals(var)){
            if (oNuevoEmisor.getRfc().equals("")||oNuevoEmisor.getDomicilioFiscal()==null||oNuevoEmisor.getDomicilioFiscal().getCodigoPostal().equals("")||
                    oNuevoEmisor.getDomicilioFiscal().getEdo().getCve().equals("")||oNuevoEmisor.getDomicilioFiscal().getMun().getCve().equals("")||
                    oNuevoEmisor.getDomicilioFiscal().getCalle().equals("")||oNuevoEmisor.getDomicilioFiscal().getObjPais().getDescrip().equals("")){
                mess="Error de validación. Faltan datos.";
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage("Guardar", mess));
            }else{
                if (oNuevoEmisor.getDom_fiscal_dom_exp()=='0'){
                    if (oNuevoEmisor.getExpedidoEn()==null||oNuevoEmisor.getExpedidoEn().getCodigoPostal().equals("")||
                            oNuevoEmisor.getExpedidoEn().getEdo().getCve().equals("")||oNuevoEmisor.getExpedidoEn().getMun().getCve().equals("")||
                            oNuevoEmisor.getExpedidoEn().getCalle().equals("")||oNuevoEmisor.getExpedidoEn().getObjPais().getDescrip().equals("")){
                        mess="Error de validación. Faltan datos.";
                        FacesContext context = FacesContext.getCurrentInstance();
                        context.addMessage(null, new FacesMessage("Guardar", mess));
                    }else{
                        if (oNuevoEmisor.getRegimenFiscal().size()>=1){
                            mess=oEm.guardaEmisor(oNuevoEmisor);
                            llenaLista();
                            FacesContext context = FacesContext.getCurrentInstance();
                            context.addMessage(null, new FacesMessage("Guardar", mess));
                        }else{
                            mess="Error de validación. Faltan datos.";
                            FacesContext context = FacesContext.getCurrentInstance();
                            context.addMessage(null, new FacesMessage("Guardar", mess));
                        }
                    }
                }else{
                    if (oNuevoEmisor.getRegimenFiscal().size()>=1){
                        mess=oEm.guardaEmisor(oNuevoEmisor);
                        llenaLista();
                        FacesContext context = FacesContext.getCurrentInstance();
                        context.addMessage(null, new FacesMessage("Guardar", mess));
                    }else{
                        mess="Error de validación. Faltan datos.";
                        FacesContext context = FacesContext.getCurrentInstance();
                        context.addMessage(null, new FacesMessage("Guardar", mess));
                    }
                }
            }
        }else{
            if (oSelectedEmisor.getRfc().equals("")||oSelectedEmisor.getDomicilioFiscal()==null||oSelectedEmisor.getDomicilioFiscal().getCodigoPostal().equals("")||
                    oSelectedEmisor.getDomicilioFiscal().getEdo().getCve().equals("")||oSelectedEmisor.getDomicilioFiscal().getMun().getCve().equals("")||
                    oSelectedEmisor.getDomicilioFiscal().getCalle().equals("")||oSelectedEmisor.getDomicilioFiscal().getObjPais().getDescrip().equals("")){
                mess="Error de validación. Faltan datos.";
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage("Guardar", mess));
            }else{
                if (oSelectedEmisor.getDom_fiscal_dom_exp()=='0'){
                    if (oSelectedEmisor.getExpedidoEn()==null||oSelectedEmisor.getExpedidoEn().getCodigoPostal().equals("")||
                            oSelectedEmisor.getExpedidoEn().getEdo().getCve().equals("")||oSelectedEmisor.getExpedidoEn().getMun().getCve().equals("")||
                            oSelectedEmisor.getExpedidoEn().getCalle().equals("")||oSelectedEmisor.getExpedidoEn().getObjPais().getDescrip().equals("")){
                        mess="Error de validación. Faltan datos.";
                        FacesContext context = FacesContext.getCurrentInstance();
                        context.addMessage(null, new FacesMessage("Guardar", mess));
                    }else{
                        if (oSelectedEmisor.getRegimenFiscal().size()>=1){
                            mess=oEm.modificaEmisor(oSelectedEmisor);
                            llenaLista();
                            FacesContext context = FacesContext.getCurrentInstance();
                            context.addMessage(null, new FacesMessage("Guardar", mess));
                        }else{
                            mess="Error de validación. Faltan datos.";
                            FacesContext context = FacesContext.getCurrentInstance();
                            context.addMessage(null, new FacesMessage("Guardar", mess));
                        }
                    }
                }else{
                    if (oSelectedEmisor.getRegimenFiscal().size()>=1){
                        mess=oEm.modificaEmisor(oSelectedEmisor);
                        llenaLista();
                        FacesContext context = FacesContext.getCurrentInstance();
                        context.addMessage(null, new FacesMessage("Guardar", mess));
                    }else{
                        mess="Error de validación. Faltan datos.";
                        FacesContext context = FacesContext.getCurrentInstance();
                        context.addMessage(null, new FacesMessage("Guardar", mess));
                    }
                }
            }
        }
    }
    
    public void eliminar() throws Exception{
        Emisor oEm=new Emisor();
        String mess=oEm.eliminaEmisor(oSelectedEmisor.getRfc());
        llenaLista();
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Guardar", mess));
    }
    
    public Emisor nuevoEm(){
        Emisor oEm=new Emisor();
        TUbicacionFiscal oDF=new TUbicacionFiscal();
        
        Pais oPDF=new Pais();
        Estado oEDF=new Estado();
        Municipio oMDF=new Municipio();
        Ciudad oCDF=new Ciudad();
        oDF.setObjPais(oPDF);
        oDF.setEdo(oEDF);
        oDF.setMun(oMDF);
        oDF.setCd(oCDF);
        oEm.setDomicilioFiscal(oDF);
        TUbicacion oLE=new TUbicacion();
        Pais oPLE=new Pais();
        Estado oELE=new Estado();
        Municipio oMLE=new Municipio();
        Ciudad oCLE=new Ciudad();
        oLE.setObjPais(oPLE);
        oLE.setEdo(oELE);
        oLE.setMun(oMLE);
        oLE.setCd(oCLE);
        oEm.setExpedidoEn(oLE);
        List<RegimenFiscal> listRF=new ArrayList();
        oEm.setRegimenFiscal(listRF);
        return oEm;
    }
    
    public void metodo(){
        System.out.println("metodo()");
        System.out.println("Emisor1:"+oNuevoEmisor.getRfc());
        System.out.println("Emisor2:"+oNuevoEmisor.getDomicilioFiscal().getCodigoPostal());
    }
    
    public void cancelar() throws IOException{
        FacesContext context= FacesContext.getCurrentInstance();
        context.getExternalContext().redirect("emisores.xhtml");
    }
    
    public void mismoDFyLE(TUbicacionFiscal oDF, TUbicacion oLE){
        oLE.setCodigoPostal(oDF.getCodigoPostal());
        oLE.setEdo(oDF.getEdo());
        oLE.setMun(oDF.getMun());
        oLE.setCd(oDF.getCd());
        oLE.setColonia(oDF.getColonia());
        oLE.setCalle(oDF.getCalle());
        oLE.setNoExterior(oDF.getNoExterior());
        oLE.setNoInterior(oDF.getNoInterior());
        oLE.setObjPais(oDF.getObjPais());
        oLE.setReferencia(oDF.getReferencia());

    }
}