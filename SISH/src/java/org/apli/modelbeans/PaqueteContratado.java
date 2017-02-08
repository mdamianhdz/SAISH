package org.apli.modelbeans;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import org.apli.AD.AccesoDatos;

/**
 * Relaci�n entre pacientes y paquetes que contratan
 *
 * @author BAOZ
 * @version 1.0
 * @created 23-jul.-2014 11:26:08
 */
public class PaqueteContratado implements Serializable {

    /**
     * Fecha-hora de activaci�n del paquete (ingreso al hospital en el caso de
     * paquetes de maternidad o quir�rgicos, ejecuci�n del primer servicio en
     * cualquier otro caso)
     */
    private Date dActivado;
    /**
     * Fecha-hora de cancelaci�n del paquete
     */
    private Date dCancelacion;
    /**
     * Fecha-hora de registro de la contrataci�n del paquete
     */
    private Date dRegistro;
    /**
     * Monto del anticipo (puede ser el 100% del monto del paquete o una
     * fracci�n, en cuyo caso deber� cubrir parcialidades)
     */
    private float nAnticipo;
    /**
     * Paciente que contrata el paquete
     */
    private Paciente oPaciente;
    /**
     * Personal que autoriza la contrataci�n del paquete v�a abonos en lugar de
     * pago total
     */
    private PersonalHospitalario oPersonalAutoriza;
    /**
     * Personal del hospital que autoriza la cancelaci�n del paquete
     */
    private PersonalHospitalario oPersonalAutorizCanc;
    /**
     * Personal del hospital que brind� los informes que derivaron en la
     * contrataci�n del paquete
     */
    private PersonalHospitalario oPersonalPromo;
    /**
     * Personal del hospital que registra la venta (contrataci�n) del paquete
     */
    private PersonalHospitalario oPersonalVende;
    /**
     * Nombre del padre o tutor que firmar� el contrato en el caso de paquetes
     * pedi�tricos
     */
    private String sPadreoTutor = "";
    /**
     * Raz�n por la cual se cancela el paquete
     */
    private String sRazonCanc;
    /**
     * Situaci�n del paquete contratado respecto a los pagos 0 = Pendiente de
     * Pago 1 = Pago parcial 2 = Pagado
     */
    private String situacionPago;
    /**
     * Situaci�n del paquete respecto a los servicios 0 = Contratado 1 = En
     * Proceso (uso de servicios previos a hosp) 2 = Activado (cuando se
     * hospitaliza) 3 = Terminado 4 = Cancelado
     */
    private String situacionPaq;
    private String sContraniaAutorizacion;
    private String sTipo;
    private int nidpaqcont;
    private Paquete m_Paquete;
    private CarnetPagos m_CarnetPagos;
    private EpisodioMedico oEpisodioMedico;
    protected AccesoDatos oAD = null;
    private boolean bFacturable;
    private String sFolioServ;
    private float nCesareaUrgencia;
    private float nBloqueo;
    private Date dprobableparto;
    private boolean baltoriesgo;

    public PaqueteContratado() {
    }

    public PaqueteContratado(AccesoDatos poAD) {
        oAD = poAD;
    }


    public String insertaPaqueteContratado(List<CarnetPagos> pagos) throws Exception {

        Vector rst = null;
        String sQuery = "";
        String msj = "";
        boolean validacion = false;
        Vector<Paquete> vObj = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd h:mm:ss");
        if (this.sTipo.equals("TotalAParcial")) {
            if (sContraniaAutorizacion.equals("") || oPersonalAutoriza.getFolioPers() == 0) {
                throw new Exception("PaqueteContratado. insertaPaqueteContratado(): Error de programación. Faltan datos.Tipo de pago a parcial, la clave es vacia");
            }
        }
        if (m_Paquete == null || oPersonalVende == null || oPaciente == null || dRegistro == null || oEpisodioMedico.getMedTratante() == null) {
            throw new Exception("PaqueteContratado. insertaPaqueteContratado(): Error de programación. Faltan datos.");
        } else {
            sQuery = "SELECT * from insertapaquetecontratado(" + 
                    (sContraniaAutorizacion==null||sContraniaAutorizacion.equals("")?"null":"'"+sContraniaAutorizacion+"'") + "::character varying,'" + 
                    this.sTipo + "'::character varying, " + 
                    ((this.oEpisodioMedico.getMedTratante().getFolioPers() == 0) ? "null" : (oEpisodioMedico.getMedTratante().getFolioPers())) + ", '" +
                    ((bFacturable == true) ? 1 : 0) + "'::character(1), " + 
                    m_Paquete.getIdPaquete() + "," + 
                    ((oPersonalAutoriza == null) ? "null" : (oPersonalAutoriza.getFolioPers())) + ", " +
                    oPersonalVende.getFolioPers() + ", " + 
                    ((oPersonalPromo == null) ? "null" : (oPersonalPromo.getFolioPers())) + "," + 
                    oPaciente.getFolioPac() + ", '" + 
                    sdf.format(dRegistro) + "'::timestamp without time zone, null, " + 
                    nAnticipo + "::numeric, " + 
                    (sPadreoTutor.equals("") ? null : "'"+sPadreoTutor+"'") + "::character varying , " + 
                    nCesareaUrgencia + "::numeric, " + nBloqueo + "::numeric," +
                    ((this.dprobableparto == null) ? "null" : "'" + this.dprobableparto+"'") + "::date,'" +
                    ((this.baltoriesgo == false) ? "0" : "1") + "'::character(1));";
            System.out.println(sQuery);
            if (getAD() == null) {
                setAD(new AccesoDatos());
                getAD().conectar();
                rst = getAD().ejecutarConsulta(sQuery);
                System.out.println("Sq:" + sQuery);
            }
            if (rst != null && rst.size() > 0) {
                vObj = new Vector<Paquete>();
                Vector vRowTemp = (Vector) rst.elementAt(0);
                if (!("" + rst.get(0)).contains("ERROR")) {
                    if (!((String) vRowTemp.elementAt(1)).contains("ADVERTENCIA")) {
                        try {
                            this.nidpaqcont = ((Double) vRowTemp.elementAt(0)).intValue();
                            this.sFolioServ = (String) vRowTemp.elementAt(1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        sQuery = "";
                        try {
                            if (pagos.size() > 0) {
                                for (int i = 0; i < pagos.size(); i++) {
                                    pagos.get(i).setIdpaqcont(nidpaqcont);
                                    sQuery += pagos.get(i).getQueryInsertarCarnet();
                                }
                                rst = getAD().ejecutarConsulta(sQuery);
                                if (rst != null && rst.size() > 0) {
                                    if (("" + rst.get(0)).contains("ERROR")) {
                                        throw new Exception("Paquete. insertaPaquete(): Error al insertar detalle al paquete.Detalle:" + rst.get(0));
                                    }
                                }
                            }
                            msj = "" + nidpaqcont;
                        } catch (Exception e2) {
                            e2.printStackTrace();
                            System.out.println(" PaqueteContratado.insertaPaqueteContratado():" + rst.get(0));
                        }
                    } else {
                        msj = (String) vRowTemp.elementAt(1);
                    }
                } else {
                    msj = "" + rst.get(0);
                }
            }

            getAD().desconectar();
            setAD(null);
        }
        return msj;
    }

    public String cancelarPaqueteContratado() throws Exception {

        Vector rst = null;
        String sQuery = "";
        String msj = "";
        if (sContraniaAutorizacion.equals("") || nidpaqcont == 0 || oPersonalAutorizCanc == null || oEpisodioMedico == null || sRazonCanc.equals("")) {
            throw new Exception("PaqueteContratado. insertaPaqueteContratado(): Error de programación. Faltan datos.");
        } else {
            sQuery = "SELECT * from cancelapaquetecontratado('" + this.sContraniaAutorizacion + "'::character varying," + this.nidpaqcont + "," + this.oPersonalAutorizCanc.getFolioPers() + "," + this.oEpisodioMedico.getCveepisodio() + ", '" + this.sRazonCanc + "'::character varying);";
            if (getAD() == null) {
                setAD(new AccesoDatos());
                getAD().conectar();
                rst = getAD().ejecutarConsulta(sQuery);
            }
            if (rst != null && rst.size() > 0) {
                if (!("" + rst.get(0)).contains("ERROR")) {
                    if (!("" + rst.get(0)).contains("ADVERTENCIA")) {
                        msj = "" + rst.get(0);
                    } else {
                        msj = "" + rst.get(0);
                    }
                } else {
                    msj = "" + rst.get(0);
                }
            }
            getAD().desconectar();
            setAD(null);
        }
        return msj;
    }

    public String cambiaPaqueteContratado() throws Exception {

        Vector rst = null;
        String sQuery = "";
        String msj = "";
        if (nidpaqcont == 0 || m_Paquete == null) {
            throw new Exception("PaqueteContratado. cambiaPaqueteContratado(): Error de programación. Faltan datos.");
        } else {
            sQuery = "SELECT * from cambiapaquetecontratado(" + this.nidpaqcont + "," + this.m_Paquete.getIdPaquete() + ");";
            if (getAD() == null) {
                setAD(new AccesoDatos());
                getAD().conectar();
                rst = getAD().ejecutarConsulta(sQuery);
            }
            if (rst != null && rst.size() > 0) {
                if (!("" + rst.get(0)).contains("ERROR")) {
                    if (!("" + rst.get(0)).contains("ADVERTENCIA")) {
                        msj = "" + rst.get(0);
                    } else {
                        msj = "" + rst.get(0);
                    }
                } else {
                    msj = "" + rst.get(0);
                }
            }
            getAD().desconectar();
            setAD(null);
        }
        return msj;
    }

    public float montoPagadoPorPaquete(int nCveEpisodio) throws Exception {

        Vector rst = null;
        String sQuery = "";
        float monto = 0;
        Vector<Paquete> vObj = null;
        if (nCveEpisodio == 0) {
            throw new Exception("PaqueteContratado. montoPagadoPorPaquete(): Error de programación. Faltan datos.");
        } else {
            sQuery = "SELECT * from montopagoadoporpaquete(" + nCveEpisodio + ");";
            if (getAD() == null) {
                setAD(new AccesoDatos());
                getAD().conectar();
                rst = getAD().ejecutarConsulta(sQuery);
                getAD().desconectar();
                setAD(null);
            }
            if (rst != null && rst.size() > 0) {
                vObj = new Vector<Paquete>();
                Vector vRowTemp = (Vector) rst.elementAt(0);
                monto = ((Double) vRowTemp.elementAt(0)).floatValue();
            }
        }
        return monto;
    }

    public void actualizaSituacionPagoPaqCont(int nIdPaqCont) throws Exception {
        Vector rst = null;
        String sQuery = "";
        float monto = 0;
        Vector<Paquete> vObj = null;

        sQuery = "select * from actualizasituacionpagopaquetecontratado("+nIdPaqCont+");";
        if (getAD() == null) {
            setAD(new AccesoDatos());
            getAD().conectar();
            rst = getAD().ejecutarConsulta(sQuery);
            getAD().desconectar();
            setAD(null);
        }
        System.out.print("" + (rst==null?"":rst.get(0)) + "");

    }

    public Date getActivado() {
        return dActivado;
    }

    public void setActivado(Date dActivado) {
        this.dActivado = dActivado;
    }

    public Date getCancelacion() {
        return dCancelacion;
    }

    public void setCancelacion(Date dCancelacion) {
        this.dCancelacion = dCancelacion;
    }

    public Date getRegistro() {
        return dRegistro;
    }

    public void setRegistro(Date dRegistro) {
        this.dRegistro = dRegistro;
    }

    public float getAnticipo() {
        return nAnticipo;
    }

    public void setAnticipo(float nAnticipo) {
        this.nAnticipo = nAnticipo;
    }

    public Paciente getPaciente() {
        return oPaciente;
    }

    public void setPaciente(Paciente oPaciente) {
        this.oPaciente = oPaciente;
    }

    public PersonalHospitalario getPersonalAutoriza() {
        return oPersonalAutoriza;
    }

    public void setPersonalAutoriza(PersonalHospitalario oPersonalAutoriza) {
        this.oPersonalAutoriza = oPersonalAutoriza;
    }

    public PersonalHospitalario getPersonalAutorizCanc() {
        return oPersonalAutorizCanc;
    }

    public void setPersonalAutorizCanc(PersonalHospitalario oPersonalAutorizCanc) {
        this.oPersonalAutorizCanc = oPersonalAutorizCanc;
    }

    public PersonalHospitalario getPersonalPromo() {
        return oPersonalPromo;
    }

    public void setPersonalPromo(PersonalHospitalario oPersonalPromo) {
        this.oPersonalPromo = oPersonalPromo;
    }

    public PersonalHospitalario getPersonalVende() {
        return oPersonalVende;
    }

    public void setPersonalVende(PersonalHospitalario oPersonalVende) {
        this.oPersonalVende = oPersonalVende;
    }

    public String getPadreoTutor() {
        return sPadreoTutor;
    }

    public void setPadreoTutor(String sPadreoTutor) {
        this.sPadreoTutor = sPadreoTutor;
    }

    public String getRazonCanc() {
        return sRazonCanc;
    }

    public void setRazonCanc(String sRazonCanc) {
        this.sRazonCanc = sRazonCanc;
    }

    public String getSituacionPago() {
        return this.situacionPago;
    }

    public void setSituacionPago(String sSituacionPago) {
        this.situacionPago = sSituacionPago;
    }

    public String getSituacionPaq() {
        return situacionPaq;
    }

    public void setSituacionPaq(String sSituacionPaq) {
        this.situacionPaq = sSituacionPaq;
    }
    
    public String getDescSitPaq(){
    String sVal="";
        if (situacionPaq.equals("0"))
            sVal = "Contratado";
        else if (situacionPaq.equals("1"))
            sVal = "En proceso";
        else if (situacionPaq.equals("2"))
            sVal = "Activado";
        else if (situacionPaq.equals("3"))
            sVal = "Terminado";
        else if (situacionPaq.equals("4"))
            sVal = "Cancelado";
        return sVal;
    }

    public Paquete getPaquete() {
        return m_Paquete;
    }

    public void setPaquete(Paquete m_Paquete) {
        this.m_Paquete = m_Paquete;
    }

    public CarnetPagos getCarnetPagos() {
        return m_CarnetPagos;
    }

    public void setCarnetPagos(CarnetPagos m_CarnetPagos) {
        this.m_CarnetPagos = m_CarnetPagos;
    }

    public EpisodioMedico getEpisodioMedico() {
        return oEpisodioMedico;
    }

    public void setEpisodioMedico(EpisodioMedico oEpisodioMedico) {
        this.oEpisodioMedico = oEpisodioMedico;
    }

    public AccesoDatos getAD() {
        return oAD;
    }

    public void setAD(AccesoDatos oAD) {
        this.oAD = oAD;
    }

    public int getIdpaqcont() {
        return nidpaqcont;
    }

    public void setIdpaqcont(int nidpaqcont) {
        this.nidpaqcont = nidpaqcont;
    }

    public String getContraniaAutorizacion() {
        return sContraniaAutorizacion;
    }

    public void setContraniaAutorizacion(String sContraniaAutorizacion) {
        this.sContraniaAutorizacion = sContraniaAutorizacion;
    }

    public String getTipo() {
        return sTipo;
    }

    public void setTipo(String sTipo) {
        this.sTipo = sTipo;
    }

    public boolean getFacturable() {
        return bFacturable;
    }

    public void setFacturable(boolean bFacturable) {
        this.bFacturable = bFacturable;
    }

    public String getFolioServ() {
        return sFolioServ;
    }

    public void setFolioServ(String sFolioServ) {
        this.sFolioServ = sFolioServ;
    }

    public float getCesareaUrgencia() {
        return nCesareaUrgencia;
    }

    public void setCesareaUrgencia(float nCesareaUrgencia) {
        this.nCesareaUrgencia = nCesareaUrgencia;
    }

    public float getBloqueo() {
        return nBloqueo;
    }

    public void setBloqueo(float nBloqueo) {
        this.nBloqueo = nBloqueo;
    }

    public Date getProbableparto() {
        return dprobableparto;
    }

    public void setProbableparto(Date dprobableparto) {
        this.dprobableparto = dprobableparto;
    }

    public boolean getAltoriesgo() {
        return baltoriesgo;
    }

    public void setAltoriesgo(boolean baltoriesgo) {
        this.baltoriesgo = baltoriesgo;
    }
}
