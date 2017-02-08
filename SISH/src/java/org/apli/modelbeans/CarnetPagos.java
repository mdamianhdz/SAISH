package org.apli.modelbeans;

/**
 * Relaci�n de pagos que debe realizar un paciente cuando contrata un paquete;
 * generalmente s�lo aplica para maternidad pero se presentan excepciones. No se
 * considera el anticipo, s�lo las parcialidades
 *
 * @author BAOZ
 * @version 1.0
 * @created 23-jul.-2014 11:26:57
 */
public class CarnetPagos {

    /**
     * Indica si ya se cubri� el pago o no
     */
    private boolean bPagado;
    /**
     * Monto de la parcialidad a cubrir
     */
    private float nMonto;
    /**
     * Descripci�n de cu�ndo debe cubrir el pago, por ejemplo "antes del s�ptimo
     * mes", "antes de hospitalizarse"
     */
    private String sPagarAl;
    private int nidpaqcont;

    public CarnetPagos() {

    }
    
    public String getQueryInsertarCarnet() throws Exception {
        String sQuery = "";
        if (nidpaqcont == 0 || sPagarAl.equals("") || this.nMonto == 0.0) {
            throw new Exception("CarnetPagos. getQueryInsertarCarnet(): Error de programación. Faltan datos.");
        } else {
            sQuery = "SELECT * FROM insertacarnetpagos("+nidpaqcont+", "+nMonto+"::numeric, '"+sPagarAl+"'::character varying);"; 
        }
        return sQuery;
    }

    public boolean getPagado() {
        return bPagado;
    }

    public void setPagado(boolean bPagado) {
        this.bPagado = bPagado;
    }

    public float getMonto() {
        return nMonto;
    }

    public void setMonto(float nMonto) {
        this.nMonto = nMonto;
    }

    public String getPagarAl() {
        return sPagarAl;
    }

    public void setPagarAl(String sPagarAl) {
        this.sPagarAl = sPagarAl;
    }

    public int getIdpaqcont() {
        return nidpaqcont;
    }

    public void setIdpaqcont(int nidpaqcont) {
        this.nidpaqcont = nidpaqcont;
    }

}
