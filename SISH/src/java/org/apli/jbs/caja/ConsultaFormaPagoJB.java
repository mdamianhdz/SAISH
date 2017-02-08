package org.apli.jbs.caja;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.apli.modelbeans.Vouchers;
import org.apli.modelbeans.Transferencia;
import org.apli.modelbeans.Cheque;
import org.apli.modelbeans.FichaDeposito;

/**
 * Muestra el desglose por forma de pago de un corte de caja
 * @author AIMEE R
 */
@ManagedBean (name="oConsFrmPago")
@ViewScoped
public class ConsultaFormaPagoJB implements Serializable{

    private Date fechacorte;
    private String formapago="";
    private Vouchers oV; 
    private Vouchers[] arrV;
    
    private Transferencia oT;
    private Transferencia[] arrT;
    
    private Cheque oC;
    private Cheque[] arrC;
    
    private FichaDeposito oFD;
    private FichaDeposito[] arrFD;
    
    private List<Vouchers> listaV= new ArrayList();
    private List<Transferencia> listaT= new ArrayList();
    private List<Cheque> listaC= new ArrayList();
    private List<FichaDeposito> listaFD= new ArrayList();
    
    private boolean bTransferencia = false;
    private boolean bCheque = false;
    private boolean bVouchers=false;
    private boolean bFichasDeposito=false;
    
    public ConsultaFormaPagoJB() {
    }
    
    public void buscarDetalleFP(){
        System.out.println(formapago);
        if(this.formapago.equals("TDC  ") || this.formapago.equals("TDD  ")){
            try{
                oV=new Vouchers();
                listaV=new ArrayList<Vouchers>();
                arrV= (Vouchers[])oV.buscaVouchers(fechacorte);
                for(int i=0; i<arrV.length; i++){
                    Vouchers temp= arrV[i];
                    listaV.add(temp);
                }
                oV.calculaMontoTotalVouchers(fechacorte);
            } catch(Exception e){
                e.printStackTrace();
            }
        }
        else if(this.formapago.equals("CHQ  ")){
            try{
                oC=new Cheque();
                listaC=new ArrayList<Cheque>();
                arrC=(Cheque[])oC.buscaCheques(fechacorte);
                for(int i=0;i<arrC.length;i++){
                    Cheque temp=arrC[i];
                    listaC.add(temp);
                }
                oC.calculaMontoTotalCheques(fechacorte);
            } catch(Exception e){
                e.printStackTrace();
            }
        }
        else if(this.formapago.equals("TRE  ")){
            try{
                oT=new Transferencia();
                listaT=new ArrayList<Transferencia>();
                arrT=(Transferencia[])oT.buscaTransferencias(fechacorte);
                for(int i=0;i<arrT.length;i++){
                    Transferencia temp=arrT[i];
                    listaT.add(temp);
                }
                oT.calculaMontoTotalTransferencia(fechacorte);
            } catch(Exception e){
                e.printStackTrace();
            }
        }
        else if(this.formapago.equals("DEP  ")){
            try{
                oFD=new FichaDeposito();
                listaFD=new ArrayList<FichaDeposito>();
                arrFD=(FichaDeposito[])oFD.buscaFichasDeposito(fechacorte);
                for(int i=0;i<arrFD.length;i++){
                    FichaDeposito temp=arrFD[i];
                    listaFD.add(temp);
                }
                oFD.calculaMontoTotalFichaDeposito(fechacorte);
            } catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    
    
    
    public void validaForma() {
        int result = formapago.compareToIgnoreCase("CHQ  "); //cheque
        int result2 = formapago.compareToIgnoreCase("TRE  "); //transferencias
        int result3=formapago.compareToIgnoreCase("DEP "); //depositos
        int result4=formapago.compareToIgnoreCase("TDC "); //credito
        int result5=formapago.compareToIgnoreCase("TDD "); // debito

        if (result == 0) {
            bCheque = true;
            bTransferencia = false;
            bVouchers=false;
            bFichasDeposito=false;
        } else if (result2 == 0) {
            bTransferencia = true;
            bCheque = false;
            bVouchers=false;
            bFichasDeposito=false;
        } else if(result3==0){
            bFichasDeposito=true;
            bCheque=false;
            bVouchers=false;
            bTransferencia=true;
        } else if(result4==0 || result5==0){
            bVouchers=true;
            bCheque=false;
            bTransferencia=false;
            bFichasDeposito=false;
        }
        else {
            bCheque = false;
            bTransferencia = false;
            bVouchers=false;
            bFichasDeposito=false;
        }
    }
    
    public Vouchers getVouchers(){
        return oV;
    }
    public Cheque getCheque(){
        return oC;
    }
    public Transferencia getTransferencia(){
        return oT;
    }
    public FichaDeposito getFichaDeposito(){
        return oFD;
    }
    
    public void setFechaCorte(Date fechacorte){
        this.fechacorte=fechacorte;
    }
    public Date getFechaCorte(){
        return fechacorte;
    }
    
    public Vouchers[] getArrV(){
        return arrV;
    }
    public Cheque[] getArrC(){
        return arrC;
    }
    public Transferencia[] getArrT(){
        return arrT;
    }
    public FichaDeposito[] getArrFD(){
        return arrFD;
    }
    public String getForma(){
        return formapago;
    }
    public void setForma(String  formapago){
        this.formapago=formapago;
    }
    
    public List<Vouchers> getListaVouchers() {
        return listaV;
    }
    public void setListaVouchers(List<Vouchers> value) {
        listaV= value;
    }
    
    public List<Cheque> getListaCheque() {
        return listaC;
    }
    public void setListaCheque(List<Cheque> value) {
        listaC= value;
    }
    
    public List<Transferencia> getListaTransferencia() {
        return listaT;
    }
    public void setListaTransferencia(List<Transferencia> value) {
        listaT= value;
    }
    
    public List<FichaDeposito> getListaFichaDeposito() {
        return listaFD;
    }
    public void setListaFichaDeposito(List<FichaDeposito> value) {
        listaFD= value;
    }
    
    

    /** return bVouchers */
    public boolean isVoucher(){
        return bVouchers;
    }
    
    /**@param bVouchers set**/
    
    public void setVouchers(boolean bVouchers){
        this.bVouchers = bVouchers;
    }
    
    /**return bFichasDeposito**/
    public boolean isFichaDeposito(){
        return bFichasDeposito;
    }
    
    /**@param bFichasDeposito set**/
    
    public void setFichasDeposito(boolean bFichasDeposito){
        this.bFichasDeposito=bFichasDeposito;
    }
}
