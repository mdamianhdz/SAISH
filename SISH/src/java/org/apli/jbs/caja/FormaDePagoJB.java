package org.apli.jbs.caja;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.apli.modelbeans.FormaPago;
import org.apli.modelbeans.Banco;

/**
 * Encapsula control para el manejo de formas de pago relacionados con caja
 * Update 09/03/2016 agregado vouchers y fichas de deposito --@AIMEE R
 * @author Manuel
 */
@ManagedBean (name="oFormaDePago")
@SessionScoped
public class FormaDePagoJB implements Serializable {
private String sForma;
private List<FormaPago> listFP;
private FormaPago oFP;
private boolean bTransferencia = false;
private boolean bCheque = false;
private boolean bVouchers=false;
private boolean bFichasDeposito=false;
private List<String> listB= new ArrayList();
 
    public FormaDePagoJB() {
    }

     public void validaForma() {
        System.out.print("Metodo valida Forma");
        int result = sForma.compareToIgnoreCase("CHQ  "); //cheque
        int result2 = sForma.compareToIgnoreCase("TRE  "); //transferencias
        int result3=sForma.compareToIgnoreCase("DEP "); //depositos
        int result4=sForma.compareToIgnoreCase("TDC "); //credito
        int result5=sForma.compareToIgnoreCase("TDD "); // debito

        if (result == 0) {
            System.out.print("Cheque");
            bCheque = true;
            bTransferencia = false;
            bVouchers=false;
            bFichasDeposito=false;
        } else if (result2 == 0) {
            System.out.print("Transferencia");
            bTransferencia = true;
            bCheque = false;
            bVouchers=false;
            bFichasDeposito=false;
        } else if(result3==0){
            System.out.print("Fichas de Deposito");
            bFichasDeposito=true;
            bCheque=false;
            bVouchers=false;
            bTransferencia=true;
        } else if(result4==0 || result5==0){
            System.out.print("Vouchers");
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
            System.out.print("Else   ");
            System.out.print("Result  " + result);
            System.out.print("Result2   " + result2);
            System.out.print("Result3     "+result3);
            System.out.print("Result4     "+result4);
            System.out.print("Result5     "+result5);
        }

        System.out.print("Saliendo Metodo ValidaForma  " + sForma);
    }
     
    public List<FormaPago> getFormasDePago() throws Exception {
        this.listFP = new ArrayList();
        this.oFP=new FormaPago();
        this.listFP = oFP.buscaFormasPago();
        return this.listFP;
    }
    
    public List<String> getBancos(){
        this.listB=new ArrayList();
        this.oFP= new FormaPago();
        this.listB=(new Banco()).getBancos();
        return this.listB;    
    }
    
    /**
     * @return the sForma
     */
    public String getForma() {
        return sForma;
    }

    /**
     * @param sForma the sForma to set
     */
    public void setForma(String sForma) {
        this.sForma = sForma;
    }

    /**
     * @return the listFP
     */
    public List<FormaPago> getListFP() {
        return listFP;
    }

    /**
     * @param listFP the listFP to set
     */
    public void setListFP(List<FormaPago> listFP) {
        this.listFP = listFP;
    }

    /**
     * @return the oFP
     */
    public FormaPago getFP() {
        return oFP;
    }

    /**
     * @param oFP the oFP to set
     */
    public void setFP(FormaPago oFP) {
        this.oFP = oFP;
    }

    /**
     * @return the bTransferencia
     */
    public boolean isTransferencia() {
        return bTransferencia;
    }

    /**
     * @param bTransferencia the bTransferencia to set
     */
    public void setTransferencia(boolean bTransferencia) {
        this.bTransferencia = bTransferencia;
    }

    /**
     * @return the bCheque
     */
    public boolean isCheque() {
        return bCheque;
    }

    /**
     * @param bCheque the bCheque to set
     */
    public void setCheque(boolean bCheque) {
        this.bCheque = bCheque;
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
    
    
    /**
     * @return the listB
     */
    public List<String> getListB() {
        return listB;
    }

    /**
     * @param listB the listB to set
     */
    public void setListB(List<String> listB) {
        this.listB = listB;
    }
}
