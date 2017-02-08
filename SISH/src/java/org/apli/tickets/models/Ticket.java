package org.apli.tickets.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import org.apache.log4j.Logger;
import org.apli.AD.AccesoDatos;
import org.apli.modelbeans.Utilidad;
import org.apli.modelbeans.Valida;
import org.apli.modelbeans.ventaCredito.CompaniaCred;
import org.apli.tickets.Paciente;
import org.apli.tickets.Servicio;

/**
 *
 * @author mdamian
 */
public class Ticket implements Serializable {

    private AccesoDatos oAD;
    private static final Logger LOG = Logger.getLogger(Ticket.class.getName());

    public Ticket() {
    }

    public String buscarTicketExterno(String folioOpe, boolean bReimpresion) throws Exception {
        String content = "";
        Vector rst = null, rst2 = null;
        String sQuery = "", sQuery2 = "";
        int fOpeBD = 0;
        ServicioExterno oServExt;
        Paciente paciente;
        ArrayList<Servicio> arrServicios;

        if (folioOpe.equals("")) {
            throw new Exception("Ticket.buscarTicketExterno: Error de programación, faltan datos");
        } else {
            fOpeBD = Integer.parseInt(folioOpe);
            sQuery = "select * from buscarTicketBasicoExterno (" + fOpeBD + ");";
            System.out.println(sQuery);

            if (getAD() == null) {
                setAD(new AccesoDatos());
                getAD().conectar();
                rst = getAD().ejecutarConsulta(sQuery);
                getAD().desconectar();
                setAD(null);
            } else {
                rst = getAD().ejecutarConsulta(sQuery);
            }
            if (rst != null && rst.size() > 0) {
                Vector vRowTemp = (Vector) rst.elementAt(0);
                oServExt = new ServicioExterno();
                oServExt.setFolioope(((Double) vRowTemp.elementAt(0)).intValue() + "");
                oServExt.setHora(oServExt.labelTime((Date) vRowTemp.elementAt(1)));
                oServExt.setFecha(oServExt.labelDate((Date) vRowTemp.elementAt(1)));
                String nameUser = vRowTemp.elementAt(3).toString() + " " + vRowTemp.elementAt(4).toString() + " " + vRowTemp.elementAt(5).toString();
                oServExt.setUsuario(nameUser);
                oServExt.setFmapago((String) vRowTemp.elementAt(2));
                oServExt.setMensaje(new Utilidad().buscaMensajeTicket());
                oServExt.setReimpresion(bReimpresion);
                //Inicia una busqueda de los Servicios Prestados asignados a una operacion de caja
                sQuery2 = "select * from buscarTicketComplementarioExterno (" + fOpeBD + ");";
                System.out.println(sQuery2);

                if (getAD() == null) {
                    setAD(new AccesoDatos());
                    getAD().conectar();
                    rst2 = getAD().ejecutarConsulta(sQuery2);
                    getAD().desconectar();
                    setAD(null);
                } else {
                    rst2 = getAD().ejecutarConsulta(sQuery2);
                }
                if (rst2 != null) {
                    arrServicios = new ArrayList<Servicio>();
                    for (int i = 0; i < rst2.size(); i++) {
                        Vector vRowTemp2 = (Vector) rst2.get(i);
                        Servicio servicio = new Servicio();
                        paciente = new Paciente();
                        paciente.setFolio(((Double) vRowTemp2.elementAt(6)).intValue());
                        paciente.setNombre((String) vRowTemp2.elementAt(7) + " " + (String) vRowTemp2.elementAt(8) + " " + (String) vRowTemp2.elementAt(9));
                        oServExt.setPaciente(paciente);
                        CompaniaCred cc = new CompaniaCred();
                        int idEmpresa = (vRowTemp2.elementAt(5) != null) ? ((Double) vRowTemp2.elementAt(5)).intValue() : 0;
                        String company = (idEmpresa != 0) ? cc.buscaNombreEmpresa(idEmpresa).getNombreCorto() : "Particulares";
                        oServExt.setEmpresa(company);
                        String tmpDoctor = (String) vRowTemp2.elementAt(14) + " " + (String) vRowTemp2.elementAt(15) + " " + (String) vRowTemp2.elementAt(16);
                        oServExt.setDoctor((tmpDoctor.trim().isEmpty()) ? "No identificado" : tmpDoctor);
                        servicio.setDescripcion((String) vRowTemp2.elementAt(2));
                        servicio.setCantidad(((Double) vRowTemp2.elementAt(10)).intValue());
                        servicio.setPrecio((Double) vRowTemp2.elementAt(11) + "");
                        servicio.setIva(((Double) vRowTemp2.elementAt(12)).floatValue());
                        servicio.setPrecioCobrado((Double) vRowTemp2.elementAt(13) + "");
                        arrServicios.add(servicio);
                    }
                    oServExt.setServicios(arrServicios);
                    oServExt.calcularCifras();
                    content = oServExt.convertToJSON(oServExt);
                    LOG.trace("Ticket Externos: [[" + content + "]]");
                }
                //Termina busqueda de servicios prestados
            }
        }
        return content;
    }

    public ServicioExterno buscarTicketExterno(boolean bReimpresion, String folioOpe) throws Exception {
        String content = "";
        Vector rst = null, rst2 = null;
        String sQuery = "", sQuery2 = "";
        int fOpeBD = 0;
        ServicioExterno oServExt = new ServicioExterno();
        Paciente paciente;
        ArrayList<Servicio> arrServicios;

        if (folioOpe.equals("")) {
            throw new Exception("Ticket.buscarTicketExterno: Error de programación, faltan datos");
        } else {
            fOpeBD = Integer.parseInt(folioOpe);
            sQuery = "select * from buscarTicketBasicoExterno (" + fOpeBD + ");";
            System.out.println(sQuery);

            if (getAD() == null) {
                setAD(new AccesoDatos());
                getAD().conectar();
                rst = getAD().ejecutarConsulta(sQuery);
                getAD().desconectar();
                setAD(null);
            } else {
                rst = getAD().ejecutarConsulta(sQuery);
            }
            if (rst != null && rst.size() > 0) {
                Vector vRowTemp = (Vector) rst.elementAt(0);
                oServExt.setFolioope(((Double) vRowTemp.elementAt(0)).intValue() + "");
                oServExt.setHora(oServExt.labelTime((Date) vRowTemp.elementAt(1)));
                oServExt.setFecha(oServExt.labelDate((Date) vRowTemp.elementAt(1)));
                String nameUser = vRowTemp.elementAt(3).toString() + " " + vRowTemp.elementAt(4).toString() + " " + vRowTemp.elementAt(5).toString();
                oServExt.setUsuario(nameUser);
                oServExt.setFmapago((String) vRowTemp.elementAt(2));
                oServExt.setMensaje(new Utilidad().buscaMensajeTicket());
                oServExt.setReimpresion(bReimpresion);
                //Inicia una busqueda de los Servicios Prestados asignados a una operacion de caja
                sQuery2 = "select * from buscarTicketComplementarioExterno (" + fOpeBD + ");";
                System.out.println(sQuery2);

                if (getAD() == null) {
                    setAD(new AccesoDatos());
                    getAD().conectar();
                    rst2 = getAD().ejecutarConsulta(sQuery2);
                    getAD().desconectar();
                    setAD(null);
                } else {
                    rst2 = getAD().ejecutarConsulta(sQuery2);
                }
                if (rst2 != null) {
                    arrServicios = new ArrayList<Servicio>();
                    for (int i = 0; i < rst2.size(); i++) {
                        Vector vRowTemp2 = (Vector) rst2.get(i);
                        Servicio servicio = new Servicio();
                        paciente = new Paciente();
                        paciente.setFolio(((Double) vRowTemp2.elementAt(6)).intValue());
                        paciente.setNombre((String) vRowTemp2.elementAt(7) + " " + (String) vRowTemp2.elementAt(8) + " " + (String) vRowTemp2.elementAt(9));
                        oServExt.setPaciente(paciente);
                        CompaniaCred cc = new CompaniaCred();
                        int idEmpresa = (vRowTemp2.elementAt(5) != null) ? ((Double) vRowTemp2.elementAt(5)).intValue() : 0;
                        String company = (idEmpresa != 0) ? cc.buscaNombreEmpresa(idEmpresa).getNombreCorto() : "Particulares";
                        oServExt.setEmpresa(company);
                        String tmpDoctor = (String) vRowTemp2.elementAt(14) + " " + (String) vRowTemp2.elementAt(15) + " " + (String) vRowTemp2.elementAt(16);
                        oServExt.setDoctor((tmpDoctor.trim().isEmpty()) ? "No identificado" : tmpDoctor);
                        servicio.setDescripcion((String) vRowTemp2.elementAt(2));
                        servicio.setCantidad(((Double) vRowTemp2.elementAt(10)).intValue());
                        servicio.setPrecio((Double) vRowTemp2.elementAt(11) + "");
                        servicio.setIva(((Double) vRowTemp2.elementAt(12)).floatValue());
                        servicio.setPrecioCobrado((Double) vRowTemp2.elementAt(13) + "");
                        arrServicios.add(servicio);
                    }
                    oServExt.setServicios(arrServicios);
                    oServExt.calcularCifras();
                    content = oServExt.convertToJSON(oServExt);
                    LOG.trace("Ticket Externos: [[" + content + "]]");
                }
                //Termina busqueda de servicios prestados
            }
        }
        return oServExt;
    }

    public String buscarTicketAnticipoCta(String folioOpe, boolean bReimpresion) throws Exception {
        String content = "";
        Vector rst = null;
        String sQuery = "";
        int fOpeBD = 0;
        PagoAnticipoCuentaHospital pagoAnticipoCuentaHospital;
        Paciente paciente;
        ArrayList<Servicio> arrServicios;

        if (folioOpe.equals("")) {
            throw new Exception("Ticket.buscarTicketAnticipoCta: Error de programación, faltan datos");
        } else {
            fOpeBD = Integer.parseInt(folioOpe);
            sQuery = "select * from buscarTicketBasicoCompAnticipo (" + fOpeBD + ");";
            System.out.println(sQuery);

            if (getAD() == null) {
                setAD(new AccesoDatos());
                getAD().conectar();
                rst = getAD().ejecutarConsulta(sQuery);
                getAD().desconectar();
                setAD(null);
            } else {
                rst = getAD().ejecutarConsulta(sQuery);
            }

            if (rst != null && rst.size() > 0) {
                Vector vRowTemp = (Vector) rst.elementAt(0);
                Servicio servicio = new Servicio();
                paciente = new Paciente();
                arrServicios = new ArrayList<Servicio>();
                pagoAnticipoCuentaHospital = new PagoAnticipoCuentaHospital();
                pagoAnticipoCuentaHospital.setFolioope(((Double) vRowTemp.elementAt(0)).intValue() + "");
                pagoAnticipoCuentaHospital.setHora(pagoAnticipoCuentaHospital.labelTime((Date) vRowTemp.elementAt(1)));
                pagoAnticipoCuentaHospital.setFecha(pagoAnticipoCuentaHospital.labelDate((Date) vRowTemp.elementAt(1)));
                pagoAnticipoCuentaHospital.setFoliohosp(((Double) vRowTemp.elementAt(7)).intValue() + "");
                pagoAnticipoCuentaHospital.setReimpresion(bReimpresion);
                pagoAnticipoCuentaHospital.setFmapago((String) vRowTemp.elementAt(2));
                String nameUser = vRowTemp.elementAt(3).toString() + " " + vRowTemp.elementAt(4).toString() + " " + vRowTemp.elementAt(5).toString();
                pagoAnticipoCuentaHospital.setUsuario(nameUser);
                paciente.setFolio(((Double) vRowTemp.elementAt(9)).intValue());
                paciente.setNombre((String) vRowTemp.elementAt(10) + " " + (String) vRowTemp.elementAt(11) + " " + (String) vRowTemp.elementAt(12));
                pagoAnticipoCuentaHospital.setPaciente(paciente);
                servicio.setDescripcion((String) vRowTemp.elementAt(6));
                servicio.setCantidad(((Double) vRowTemp.elementAt(13)).intValue());
                servicio.setPrecio((Double) vRowTemp.elementAt(16) + "");//Asignamos el costo Cobrado para que el ticket contemple el valor, ayq eu esta basado en varible con nombre "Precio"
                servicio.setIva(((Double) vRowTemp.elementAt(15)).floatValue());
                servicio.setPrecioCobrado((Double) vRowTemp.elementAt(16) + "");
                arrServicios.add(servicio);
                pagoAnticipoCuentaHospital.setServicios(arrServicios);
                pagoAnticipoCuentaHospital.calcularCifras();
                content = pagoAnticipoCuentaHospital.convertToJSON(pagoAnticipoCuentaHospital);
                LOG.trace("Ticket PagoAnticipoCuentaHospital: [[" + content + "]]");

            }
        }
        return content;
    }

    public String buscarTicketAnticipoPaq(String folioOpe, boolean bReimpresion) throws Exception {
        String content = "";
        Vector rst = null;
        String sQuery = "";
        int fOpeBD = 0;
        PagoAnticipoPaquete pagoAnticipoPaquete;
        Paciente paciente;
        ArrayList<Servicio> arrServicios;

        if (folioOpe.equals("")) {
            throw new Exception("Ticket.buscarTicketAnticipoPaq: Error de programación, faltan datos");
        } else {
            fOpeBD = Integer.parseInt(folioOpe);
            sQuery = "select * from buscarTicketBasicoCompAnticipoPaq (" + fOpeBD + ");";
            System.out.println(sQuery);

            if (getAD() == null) {
                setAD(new AccesoDatos());
                getAD().conectar();
                rst = getAD().ejecutarConsulta(sQuery);
                getAD().desconectar();
                setAD(null);
            } else {
                rst = getAD().ejecutarConsulta(sQuery);
            }

            if (rst != null && rst.size() > 0) {
                Vector vRowTemp = (Vector) rst.elementAt(0);
                Servicio servicio = new Servicio();
                paciente = new Paciente();
                arrServicios = new ArrayList<Servicio>();
                pagoAnticipoPaquete = new PagoAnticipoPaquete();
                pagoAnticipoPaquete.setFolioope(((Double) vRowTemp.elementAt(0)).intValue() + "");
                pagoAnticipoPaquete.setHora(pagoAnticipoPaquete.labelTime((Date) vRowTemp.elementAt(1)));
                pagoAnticipoPaquete.setFecha(pagoAnticipoPaquete.labelDate((Date) vRowTemp.elementAt(1)));
                pagoAnticipoPaquete.setReimpresion(bReimpresion);
                pagoAnticipoPaquete.setPaquete((String) vRowTemp.elementAt(15));
                pagoAnticipoPaquete.setFmapago((String) vRowTemp.elementAt(2));
                String nameUser = vRowTemp.elementAt(3).toString() + " " + vRowTemp.elementAt(4).toString() + " " + vRowTemp.elementAt(5).toString();
                pagoAnticipoPaquete.setUsuario(nameUser);
                paciente.setFolio(((Double) vRowTemp.elementAt(7)).intValue());
                paciente.setNombre((String) vRowTemp.elementAt(8) + " " + (String) vRowTemp.elementAt(9) + " " + (String) vRowTemp.elementAt(10));
                pagoAnticipoPaquete.setPaciente(paciente);
                servicio.setDescripcion((String) vRowTemp.elementAt(6));
                servicio.setCantidad(((Double) vRowTemp.elementAt(11)).intValue());
                servicio.setPrecio((Double) vRowTemp.elementAt(14) + "");//Asignamos el costo Cobrado para que el ticket contemple el valor, ayq eu esta basado en varible con nombre "Precio"
                servicio.setIva(((Double) vRowTemp.elementAt(13)).floatValue());
                servicio.setPrecioCobrado((Double) vRowTemp.elementAt(14) + "");
                arrServicios.add(servicio);
                pagoAnticipoPaquete.setServicios(arrServicios);
                pagoAnticipoPaquete.setCuenta(((Double) vRowTemp.elementAt(16)).floatValue() + "");
                pagoAnticipoPaquete.setAbonos(((Double) vRowTemp.elementAt(17)).floatValue() + "");
                pagoAnticipoPaquete.setPago((Double) vRowTemp.elementAt(14) + "");
                pagoAnticipoPaquete.setDebe(((Double) vRowTemp.elementAt(18)).floatValue() + "");
                content = pagoAnticipoPaquete.convertToJSON(pagoAnticipoPaquete);
                LOG.trace("Ticket PagoAnticipoCuentaPaquete: [[" + content + "]]");
            }
        }
        return content;
    }

//    public String buscarTicketCierreDeCuenta(String folioOpe, boolean bReimpresion){
//        
//    }
    public String buscarTicketOrdenDeServicio(int folioOpe, boolean bReimpresion) throws Exception {
        String content = "";
        Vector rst = null;
        String sQuery = "";
        OrdenDeServicio ordenDeServicio;
        Paciente paciente;
        ArrayList<Servicio> arrServicios;

        if (folioOpe == 0) {
            throw new Exception("Ticket.buscarTicketOrdenDeServicio: Error de programación, faltan datos");
        } else {
            sQuery = "select * from buscarTicketBasicoCompOrdenDeServicio(" + folioOpe + ");";
            System.out.println(sQuery);

            if (getAD() == null) {
                setAD(new AccesoDatos());
                getAD().conectar();
                rst = getAD().ejecutarConsulta(sQuery);
                getAD().desconectar();
                setAD(null);
            } else {
                rst = getAD().ejecutarConsulta(sQuery);
            }

//            poutncostooriginal numeric,                   7
////	      poutsnombremed character varying,             11 
//            poutsappaternomed character varying,             12
//            poutsapmaternomed character varying,              13
//            poutsnombreempresa character varying             14
            if (rst != null && rst.size() > 0) {
                arrServicios = new ArrayList<Servicio>();
                ordenDeServicio = new OrdenDeServicio();
                ordenDeServicio.setReimpresion(bReimpresion);
                for (int i = 0; i < rst.size(); i++) {
                    Vector vRowTemp = (Vector) rst.get(i);
                    Servicio servicio = new Servicio();
                    paciente = new Paciente();
                    ordenDeServicio.setFecha(ordenDeServicio.labelDate((Date) vRowTemp.elementAt(0)));
                    ordenDeServicio.setHora(ordenDeServicio.labelTime((Date) vRowTemp.elementAt(0)));
                    ordenDeServicio.setFolioorden(((Double) vRowTemp.elementAt(1)).intValue() + "");
                    paciente.setFolio(((Double) vRowTemp.elementAt(2)).intValue());
                    paciente.setNombre((String) vRowTemp.elementAt(3) + " " + (String) vRowTemp.elementAt(4) + " " + (String) vRowTemp.elementAt(5));
                    ordenDeServicio.setPaciente(paciente);
                    servicio.setCantidad(((Double) vRowTemp.elementAt(6)).intValue());
                    servicio.setDescripcion((String) vRowTemp.elementAt(10));
                    servicio.setPrecio((Double) vRowTemp.elementAt(8) + "");//Asignamos el costo Cobrado para que el ticket contemple el valor, ayq eu esta basado en varible con nombre "Precio"
                    servicio.setIva(((Double) vRowTemp.elementAt(9)).floatValue());
                    servicio.setPrecioCobrado((Double) vRowTemp.elementAt(8) + "");
                    arrServicios.add(servicio);
                }//Cierre de FOR
                ordenDeServicio.setServicios(arrServicios);
                ordenDeServicio.calcularCifras();
                content = ordenDeServicio.convertToJSON(ordenDeServicio);
                LOG.trace("Ticket OrdenDeServicio: [[" + content + "]]");
            }
        }
        return content;
    }

    public String buscarAreaDeServicioParaTicket(String folioOpe) throws Exception {
        Vector rst = null;
        String sQuery = "";
        int fOpeBD = 0;
        if (folioOpe.equals("")) {
            throw new Exception("Ticket.buscarAreaDeServicioParaTicket: Error de programación, faltan datos");
        } else {
            fOpeBD = Integer.parseInt(folioOpe);
            if (fOpeBD != 0) {
                sQuery = "SELECT * FROM buscarAreaDeServicioExternoTicket(" + fOpeBD + ");";

                if (getAD() == null) {
                    setAD(new AccesoDatos());
                    getAD().conectar();
                    rst = getAD().ejecutarConsulta(sQuery);
                    getAD().desconectar();
                    setAD(null);
                } else {
                    rst = getAD().ejecutarConsulta(sQuery);
                }
            }
            if (rst != null) {
                return "" + new Valida().eliminaMSJCorchetes(rst.get(0).toString());
            } else {
                return "No se han encontrado areas para el folio indicado";
            }
        }

    }

    public AccesoDatos getAD() {
        return oAD;
    }

    public void setAD(AccesoDatos oAD) {
        this.oAD = oAD;
    }

}
