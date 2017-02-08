package org.apli.timbradoCFDI;

import org.apli.manejoArchivos.ManejoArchivos;
import org.apli.manejoArchivos.NombradoArchivosCarpetas;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import org.jdom2.*;
import org.jdom2.input.SAXBuilder;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.apli.modelbeans.facturacion.cfdi.v32.schema.Comprobante;
import org.apli.modelbeans.facturacion.schema.binder.DateTimeCustomBinder;
import org.apli.ws.facturacion.ArrayOfString;
/*
 * Autor: Isabel Espinoza
 * Objetivo: Clase para realizar el timbrado y cancelacion de CFDI
 */

public class Timbrado implements Serializable {

    FacesMessage msj = new FacesMessage();
    private String sXmlCancelado;
    private String sXmlTimbrado;
    private String sRfcReceptor;
    private String sRfcEmisor;
    private String sUsuario;
    private String sPassword;
    private String sPasswordEmisor;
    private String sUUID = "";
    private String serie;
    private String folio;
    private NombradoArchivosCarpetas oNombradoArchCar = new NombradoArchivosCarpetas();

    public Timbrado(String rfcEmisor, String rfcReceptor, String usuario, String password, String serie, String folio) {
        this.sRfcEmisor = rfcEmisor;
        this.sRfcReceptor = rfcReceptor;
        this.sUsuario = usuario;
        this.sPassword = password;
        this.serie = serie;
        this.folio = folio;
        oNombradoArchCar.setRfc(sRfcReceptor);
    }

    public void guardarXml(String tipo, String xml) throws IOException {
        String sFichero = "";
        if (tipo.equals("Timbrado")) {
            sFichero = oNombradoArchCar.getNombreCarpetaXML() + "/" + oNombradoArchCar.getFileXML(serie + folio);
        } else if (tipo.equals("Cancelado")) {
            sFichero = oNombradoArchCar.getNombreCarpetaXMLCancelados() + "/" + oNombradoArchCar.getFileXML(serie + folio);
        }
        FileWriter oArchivoEscritura = new FileWriter(sFichero);
        oArchivoEscritura.write(xml);
        oArchivoEscritura.close();
    }

    public String getCadenaDeXML() throws IOException {
        BufferedReader xmlLectura = null;
        File archivoBitacoraLectura = null;
        String archivoXML = oNombradoArchCar.getNombreCarpetaXML() + "/" + oNombradoArchCar.getFileXML(serie + folio);
        xmlLectura = ManejoArchivos.abrirArchivo(archivoBitacoraLectura, xmlLectura, archivoXML);
        String lecturaXML = "";
        while (xmlLectura.ready()) {
            lecturaXML = lecturaXML + xmlLectura.readLine();
        }
        return lecturaXML;
    }

    public boolean solicitarTimbreFiscal() throws IOException {
        String xml = getCadenaDeXML();
        ArrayOfString arreglo = Timbrado.timbrarCFDPrueba(this.sUsuario, this.sPassword, xml);
        List<String> resultado = arreglo.getString();
        if (resultado.get(0).equals("") && resultado.get(1).equals("") && resultado.get(2).equals("")) {
            this.sXmlTimbrado = resultado.get(3);
            guardarXml("Timbrado", this.sXmlTimbrado);
            return true;
        } else {
            String rst = "Error: " + resultado.get(0) + ", " + resultado.get(1);
            FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
            return true;
        }
    }

    public boolean solicitarCancelacionCFDI() throws IOException {
        ArrayOfString arregloUUID = new ArrayOfString();
        arregloUUID.getString().add(sUUID);
        ExternalContext extCont = FacesContext.getCurrentInstance().getExternalContext();
        String certificadoPKCS12Base64 = (extCont.getRealPath("//BovedaFiscal//Certificados//")) + "\\" + this.sRfcEmisor + ".pem";
        ArrayOfString arreglo = Timbrado.cancelarCFDI(sUsuario, sPassword, this.sRfcEmisor, arregloUUID, certificadoPKCS12Base64, this.sPasswordEmisor);
        List<String> resultado = arreglo.getString();
        if (resultado.get(1).equals("201")) {
            this.sXmlCancelado = resultado.get(2);
            guardarXml("Cancelado", this.sXmlCancelado);
            return true;
        } else {
            String rst = "Error: " + resultado.get(1);
            FacesMessage msj2 = new FacesMessage(FacesMessage.SEVERITY_ERROR, rst, "");
            FacesContext.getCurrentInstance().addMessage(null, msj2);
            return true;
        }
    }

    private static ArrayOfString timbrarCFDPrueba(String usuario, String password, String cadena) {
        org.apli.ws.facturacion.WSTFD service = new org.apli.ws.facturacion.WSTFD();
        org.apli.ws.facturacion.WSTFDSoap port = service.getWSTFDSoap();
        return port.timbrarCFDPrueba(usuario, password, cadena);
    }

    private static ArrayOfString cancelarCFDI(String usuario, String password, String rfcEmisor, org.apli.ws.facturacion.ArrayOfString listaCFDI, String certificadoPKCS12Base64, String passwordPKCS12) {
        org.apli.ws.facturacion.WSTFD service = new org.apli.ws.facturacion.WSTFD();
        org.apli.ws.facturacion.WSTFDSoap port = service.getWSTFDSoap();
        return port.cancelarCFDI(usuario, password, rfcEmisor, listaCFDI, certificadoPKCS12Base64, passwordPKCS12);
    }

    public void getDatosTimbrado(Comprobante comprobante) throws Exception {
        String archivoXML = oNombradoArchCar.getNombreCarpetaXML() + "/" + oNombradoArchCar.getFileXML(serie + folio);
        SAXBuilder builder = new SAXBuilder();
        File xmlFile = new File(archivoXML);
        try {
            Document document = (Document) builder.build(xmlFile);
            Element rootNode = document.getRootElement();
            List<?> listaNodos = rootNode.getChildren();
            List<Attribute> listaAtributos = rootNode.getAttributes();
            for (int i = 0; i < listaAtributos.size(); i++) {
                if (listaAtributos.get(i).getName().equals("noCertificado")) {
                    comprobante.setCertificado(listaAtributos.get(i).getValue());
                }
            }
            for (int i = 0; i < listaNodos.size(); i++) {
                Element nodo = (Element) listaNodos.get(i);
                if (nodo.getName().equals("Complemento")) {
                    List<?> listaNodosComplemento = nodo.getChildren();
                    for (int j = 0; j < listaNodosComplemento.size(); j++) {
                        Element componente = (Element) listaNodosComplemento.get(j);
                        if (componente.getName().equals("TimbreFiscalDigital")) {
                            List<Attribute> atributosX = componente.getAttributes();
                            for (int m = 0; m < atributosX.size(); m++) {
                                System.out.println("Nombre " + atributosX.get(m).getName());
                                if (atributosX.get(m).getName().equals("UUID")) {
                                    comprobante.setUuid(atributosX.get(m).getValue());
                                } else if (atributosX.get(m).getName().equals("noCertificadoSAT")) {
                                    comprobante.setNoCertificadoSAT(atributosX.get(m).getValue());
                                } else if (atributosX.get(m).getName().equals("selloSAT")) {
                                    comprobante.setSelloSAT(atributosX.get(m).getValue());
                                } else if (atributosX.get(m).getName().equals("selloCFD")) {
                                    comprobante.setSello(atributosX.get(m).getValue());
                                } else if (atributosX.get(m).getName().equals("FechaTimbrado")) {
                                    comprobante.setFechaCertificacion(DateTimeCustomBinder.parseDateTime(atributosX.get(m).getValue()));
                                    System.out.println("fecha de timradi " + comprobante.getFechaCertificacion());
                                }
                            }
                            break;
                        }
                    }
                }
            }
        } catch (IOException io) {
            System.out.println(io.getMessage());
        } catch (JDOMException jdomex) {
            System.out.println(jdomex.getMessage());
        }
    }

    public String getUsuario() {
        return sUsuario;
    }

    public void setUsuario(String usuario) {
        this.sUsuario = usuario;
    }

    public String getPassword() {
        return sPassword;
    }

    public void setPassword(String password) {
        this.sPassword = password;
    }

    public String getRfcReceptor() {
        return sRfcReceptor;
    }

    public void setRfcReceptor(String rec) {
        sRfcReceptor = rec;
    }

    public String getXmlTimbrado() {
        return sXmlTimbrado;
    }

    public String getRfcEmisor() {
        return sRfcEmisor;
    }

    public void setRfcEmisor(String sRfcEmisor) {
        this.sRfcEmisor = sRfcEmisor;
    }

    public String getUUID() {
        return sUUID;
    }

    public void setUUID(String sUUID) {
        this.sUUID = sUUID;
    }

    public String getPasswordEmisor() {
        return sPasswordEmisor;
    }

    public void setPasswordEmisor(String sPasswordEmisor) {
        this.sPasswordEmisor = sPasswordEmisor;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }
}