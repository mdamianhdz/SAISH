/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apli.jbs.utilidades;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.html.simpleparser.HTMLWorker;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.apli.modelbeans.ConceptoIngreso;
import org.apli.modelbeans.DetallePaquete;
import org.apli.modelbeans.ServicioPrestado;
import org.apli.modelbeans.TipoPaquete;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author ERJI
 */
public class DocsPDFJB implements Serializable {

    public String creaDocInfoIngreso(String sNombrePac, int nHab, int nEpi) throws Exception {
        String nombrePDF = "";
        if (sNombrePac.equals("") || nHab == 0 || nEpi == 0) {
            throw new Exception("Funcion.creaContratoIngresHosp: error de programación, faltan datos");
        } else {
            nombrePDF = "docs/contratosCreadosPac/infoPacIngreHosp-" + nEpi + ".pdf";
            ExternalContext extCont = FacesContext.getCurrentInstance().getExternalContext();
            File folder = new File(extCont.getRealPath("//resources//"));
            String urlImg = extCont.getRealPath("//utilidades//");
            try {
                urlImg = urlImg.replace("\\utilidades", "\\");
                Document document = new Document(PageSize.LETTER, 70, 70, 30, 30);
                PdfWriter.getInstance(document, new FileOutputStream(new File(folder, "docs/contratosCreadosPac/infoPacIngreHosp-" + nEpi + ".pdf")));
                document.open();
                Phrase phrase = new Phrase(90);
                HTMLWorker htmlWorker = new HTMLWorker(document);
                String str = "<p align='center'><img align=\"center\" src='" + urlImg + new PlantillaJB().getLogo() + "' width=\"430\" height=\"70\"></p>\n"
                        + "<div style=\" line-height:95%\">\n";
                File myhtml = new File(folder, "docs/contratos/infoPacIngreHosp.html");
                FileInputStream fileinput = null;
                BufferedInputStream mybuffer = null;
                DataInputStream datainput = null;
                fileinput = new FileInputStream(myhtml);
                mybuffer = new BufferedInputStream(fileinput);
                datainput = new DataInputStream(mybuffer);
                Calendar c1 = GregorianCalendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMMMM-yyyy");
                int i = 0;
                while (datainput.available() != 0) {
                    i++;
                    String linea = datainput.readLine();

                    if (linea.indexOf("NOMBREPAC") > 0) {
                        linea = linea.replace("NOMBREPAC", sNombrePac.toUpperCase());
                    }
                    if (linea.indexOf("<u>FECHA</u>") > 0) {
                        linea = linea.replace("<u>FECHA</u>", "<u>" + sdf.format(c1.getTime()) + "</u>");
                    }
                    if (linea.indexOf("NO-HAB") > 0) {
                        linea = linea.replace("NO-HAB", "" + nHab);
                    }
                    str += linea + " ";
                }
                str += "</div>";
                htmlWorker.parse(new StringReader(str));
                document.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (DocumentException e) {
                e.printStackTrace();
            }

        }
        return nombrePDF;
    }

    public String creaContratoIngresHosp(String sNombrePac, String sNombreResp, int nEpi) throws FileNotFoundException, Exception {
        String nombrePDF = "";
        if (sNombrePac.equals("") || sNombreResp.equals("") || nEpi == 0) {
            throw new Exception("DocsPDFJB.creaContratoIngresHosp: error de programación, faltan datos");
        } else {
            nombrePDF = "docs/contratosCreadosPac/ContrtPacIngreHosp-" + nEpi + ".pdf";
            ExternalContext extCont = FacesContext.getCurrentInstance().getExternalContext();
            File folder = new File(extCont.getRealPath("//resources//"));
            String urlImg = extCont.getRealPath("//utilidades//");
            try {
                urlImg = urlImg.replace("\\utilidades", "\\");

                Document document = new Document(PageSize.LETTER, 70, 70, 30, 30);
                PdfWriter.getInstance(document, new FileOutputStream(new File(folder, "docs/contratosCreadosPac/ContrtPacIngreHosp-" + nEpi + ".pdf")));

                document.open();
                Phrase phrase = new Phrase(90);
                HTMLWorker htmlWorker = new HTMLWorker(document);
                String str = "<p align='center'><img align=\"center\" src='" + urlImg + new PlantillaJB().getLogo() + "' width=\"400\" height=\"119\"></p>\n"
                        + "<div style=\" line-height:100%\">\n";

                File myhtml = new File(folder, "docs/contratos/ContrtPacIngreHosp.html");
                FileInputStream fileinput = null;
                BufferedInputStream mybuffer = null;
                DataInputStream datainput = null;
                fileinput = new FileInputStream(myhtml);
                mybuffer = new BufferedInputStream(fileinput);
                datainput = new DataInputStream(mybuffer);
                Calendar c1 = GregorianCalendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMMMM-yyyy");
                List<String> meses = new ArrayList<String>();
                meses.add("ENERO");
                meses.add("FEBRERO");
                meses.add("MARZO");
                meses.add("ABRIL");
                meses.add("MAYO");
                meses.add("JUNIO");
                meses.add("JULIO");
                meses.add("AGOSTO");
                meses.add("SEPTIEMBRE");
                meses.add("OCTUBRE");
                meses.add("NOVIEMBRE");
                meses.add("DICIEMBRE");

                int i = 0;
                while (datainput.available() != 0) {
                    i++;
                    String linea = datainput.readLine();

                    if (linea.indexOf("NOMBREPAC") > 0) {
                        linea = linea.replace("NOMBREPAC", sNombrePac.toUpperCase());
                    }
                    if (linea.indexOf("NOMBRERESP") > 0) {
                        linea = linea.replace("NOMBRERESP", sNombreResp.toUpperCase());
                    }
                    if (linea.indexOf("FECHA-DIA") > 0) {
                        linea = linea.replace("FECHA-DIA", "" + c1.get(Calendar.DAY_OF_MONTH));
                    }
                    if (linea.indexOf("FECHA-MES") > 0) {
                        linea = linea.replace("FECHA-MES", "" + meses.get(c1.get(Calendar.MONTH)));
                    }
                    if (linea.indexOf("FECHA-AÑO") > 0) {
                        linea = linea.replace("FECHA-AÑO", "" + c1.get(Calendar.YEAR));
                    }
                    str += linea + " ";

                }
                str += "</div>";

                htmlWorker.parse(new StringReader(str));
                document.close();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (DocumentException e) {
                e.printStackTrace();
            }

        }
        return nombrePDF;
    }

    public String creaContratoPaquete(String sNombrePac, String sTutor, int nFolioPaquete, float nAnticipo, float nPrecioCesaria, float nBloqueo, String sNombrePaq, String sTipoHab,int nTipoPaquete,List<DetallePaquete> carritoServicios) 
            throws FileNotFoundException, Exception {
        String nombrePDF = "";
        if (sNombrePac.equals("") || nFolioPaquete == 0) {
            throw new Exception("Funcion.creaContratoIngresHosp: error de programación, faltan datos");
        } else {
            nombrePDF = "docs/contratosCreadosPac/ContratoPaquete-" + nFolioPaquete + ".pdf";
            ExternalContext extCont = FacesContext.getCurrentInstance().getExternalContext();
            File folder = new File(extCont.getRealPath("/resources/"));
            String urlImg = extCont.getRealPath("/utilidades/");
            try {
                //urlImg = urlImg.replace("/utilidades", "/");
                urlImg = urlImg.replace("\\utilidades", "\\");
                System.out.println(urlImg);

                Document document = new Document(PageSize.LETTER, 70, 70, 30, 30);
                PdfWriter.getInstance(document, new FileOutputStream(new File(folder, "docs/contratosCreadosPac/ContratoPaquete-" + nFolioPaquete + ".pdf")));

                document.open();
                Phrase phrase = new Phrase(90);
                
                HTMLWorker htmlWorker = new HTMLWorker(document);
                String str = "<p align='center'><img align=\"center\" src='" + urlImg + new PlantillaJB().getLogo() + "' width=\"400\" height=\"119\"></p>\n"
                        + "<div style=\" line-height:90%;font-size:10.0pt;\">\n";
                File myhtml = new File(folder, "docs/contratos/ContratoPaqueteEmbarazo.html");
                
                switch(nTipoPaquete){
                    case TipoPaquete.MATERNIDAD: 
                    myhtml = new File(folder, "docs/contratos/ContratoPaqueteEmbarazo.html");
                        break;
                    case TipoPaquete.PEDIATRICO:
                    myhtml = new File(folder, "docs/contratos/ContratoPaquetePediatrico.html");
                        break;
                    case TipoPaquete.QUIRURGICO:
                        myhtml = new File(folder, "docs/contratos/ContratoPaqueteOtros.html");
                        break;
                    default: 
                        myhtml = new File(folder, "docs/contratos/ContratoPaqueteOtros.html");
                        break;                
                }


                FileInputStream fileinput = null;
                BufferedInputStream mybuffer = null;
                DataInputStream datainput = null;
                fileinput = new FileInputStream(myhtml);
                mybuffer = new BufferedInputStream(fileinput);
                datainput = new DataInputStream(mybuffer);
                Calendar c1 = GregorianCalendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMMMM-yyyy");
                List<String> meses = new ArrayList<String>();
                meses.add("ENERO");
                meses.add("FEBRERO");
                meses.add("MARZO");
                meses.add("ABRIL");
                meses.add("MAYO");
                meses.add("JUNIO");
                meses.add("JULIO");
                meses.add("AGOSTO");
                meses.add("SEPTIEMBRE");
                meses.add("OCTUBRE");
                meses.add("NOVIEMBRE");
                meses.add("DICIEMBRE");

                
                String sTablaServicios="<ul>\n";
                
                for(int i=0;i<carritoServicios.size();i++){
                        
                        sTablaServicios+="<li>";
                        sTablaServicios+=(i+1)+".- "+carritoServicios.get(i).getConceptoIngreso().getDescripcion();
                        sTablaServicios+="</li>\n";
                        
                }
                sTablaServicios+="\n</ul>";
                System.out.println("servicios "+sTablaServicios);
                int i = 0;
                while (datainput.available() != 0) {
                    i++;
                    String linea = datainput.readLine();

                    if (linea.indexOf("NOMBREPAC") > 0) {
                        linea = linea.replace("NOMBREPAC", sNombrePac);
                    }
                    if (linea.indexOf("NOMBRETUTOR") > 0) {
                        linea = linea.replace("NOMBRETUTOR", sTutor);
                    }
                    if (linea.indexOf("ANNTTICCIPPO") > 0) {
                        linea = linea.replace("ANNTTICCIPPO", "" + nAnticipo);
                    }
                    if (linea.indexOf("PRECIOCESAREA") > 0) {
                        linea = linea.replace("PRECIOCESAREA", "" + nPrecioCesaria);
                    }
                    if (linea.indexOf("FECHA-DIA") > 0) {
                        linea = linea.replace("FECHA-DIA", "" + c1.get(Calendar.DAY_OF_MONTH));
                    }
                    if (linea.indexOf("FECHA-MES") > 0) {
                        linea = linea.replace("FECHA-MES", "" + meses.get(c1.get(Calendar.MONTH)));
                    }
                    if (linea.indexOf("FECHA-ANIO") > 0) {
                        linea = linea.replace("FECHA-ANIO", "" + c1.get(Calendar.YEAR));
                    }
                    if (linea.indexOf("NOMBREPAQUETE") > 0) {
                        linea = linea.replace("NOMBREPAQUETE", sNombrePaq);
                    }
                    //agregar linea en el html
                    if (linea.indexOf("LISTASERVICIOSPAQUETE") > 0) {
                        linea = linea.replace("LISTASERVICIOSPAQUETE", sTablaServicios);
                    }
                    str += linea + " ";

                }
                str += "</div>";

                
                htmlWorker.parse(new StringReader(str));

                if (nTipoPaquete==TipoPaquete.MATERNIDAD) {
                    document.newPage();
                    str = "";
                    str += "<p align='center'><img align=\"center\" src='" + urlImg + new PlantillaJB().getLogo() + "' width=\"400\" height=\"119\"></p>\n"
                            + "<div style=\" line-height:110%;font-size:9.0pt;\">\n";
                    File myhtml2 = new File(folder, "docs/contratos/InfoPaqMat.html");
                    FileInputStream fileinput2 = null;
                    BufferedInputStream mybuffer2 = null;
                    DataInputStream datainput2 = null;
                    fileinput2 = new FileInputStream(myhtml2);
                    mybuffer2 = new BufferedInputStream(fileinput2);
                    datainput2 = new DataInputStream(mybuffer2);

                    while (datainput2.available() != 0) {
                        String linea = datainput2.readLine();
                        if (linea.indexOf("NOMBREPAC") > 0) {
                            linea = linea.replace("NOMBREPAC", sNombrePac);
                        }
                        if (linea.indexOf("ANNTTICCIPPO") > 0) {
                            linea = linea.replace("ANNTTICCIPPO", "" + nAnticipo);
                        }
                        if (linea.indexOf("FECHACOMPLETA") > 0) {
                            linea = linea.replace("FECHACOMPLETA", "" + sdf.format(new Date()));
                        }
                        if (linea.indexOf("NOMBREPAQUETE") > 0) {
                            linea = linea.replace("NOMBREPAQUETE", sNombrePaq);
                        }
                        if (linea.indexOf("TTIIPPOOHHAABB") > 0) {
                            linea = linea.replace("TTIIPPOOHHAABB", "" + sTipoHab);
                        }
                        if (linea.contains("BBLLOOQQUUEEOO")) {
                            linea = linea.replace("BBLLOOQQUUEEOO", "" + nBloqueo);
                        }
                        str += linea + " ";
                    }
                    str += "</div>";
                    htmlWorker.parse(new StringReader(str));

                }
                document.close();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (DocumentException e) {
                e.printStackTrace();
            }

        }
        return nombrePDF;
    }

    public String creaPaseSalida(String sNombrePac, String sTipoPac, String entregaCtrlTv, String sEntregaCtrolServ, String sDocumentacion, String folioUltimo, int nHab, int nEpi, Date dFechaSalida,ServicioPrestado oServiciosPrestados[]) throws FileNotFoundException, Exception {

        String nombrePDF = "";
        if (sNombrePac.equals("") || sTipoPac.equals("") || entregaCtrlTv.equals("") || sEntregaCtrolServ.equals("") || sDocumentacion.equals("") || folioUltimo.equals("") || nHab == 0 || nEpi == 0 || dFechaSalida == null) {
            throw new Exception("Funcion.creaContratoIngresHosp: error de programación, faltan datos");
        } else {
            nombrePDF = "docs/contratosCreadosPac/paseSalida-" + nEpi + ".pdf";
            ExternalContext extCont = FacesContext.getCurrentInstance().getExternalContext();
            File folder = new File(extCont.getRealPath("//resources//"));
            String urlImg = extCont.getRealPath("//utilidades//");

            try {
                urlImg = urlImg.replace("\\utilidades", "\\");
                Document document = new Document(PageSize.LETTER, 70, 70, 25, 25);
                PdfWriter.getInstance(document, new FileOutputStream(new File(folder, "docs/contratosCreadosPac/paseSalida-" + nEpi + ".pdf")));

                document.open();
                Phrase phrase = new Phrase(60);
                HTMLWorker htmlWorker = new HTMLWorker(document);
                String str = "<body>";

                File myhtml = new File(folder, "docs/contratos/paseSalidaHosp.html");
                FileInputStream fileinput = null;
                BufferedInputStream mybuffer = null;
                DataInputStream datainput = null;
                fileinput = new FileInputStream(myhtml);
                mybuffer = new BufferedInputStream(fileinput);
                datainput = new DataInputStream(mybuffer);
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMMMM-yyyy");
                SimpleDateFormat sdf2 = new SimpleDateFormat("h:mm:ss");
                int i = 0;
                while (datainput.available() != 0) {
                    i++;
                    String linea = datainput.readLine();

                    if (linea.indexOf("IMAGEN") > 0) {
                        linea = linea.replace("IMAGEN", urlImg + new PlantillaJB().getLogo());
                    }
                    if (linea.indexOf("NOMBREPAC") > 0) {
                        linea = linea.replace("NOMBREPAC", sNombrePac);
                    }
                    if (linea.indexOf("EMPRESAPARTICULAR") > 0) {
                        linea = linea.replace("EMPRESAPARTICULAR", sTipoPac);
                    }
                    if (linea.indexOf("NOHAB") > 0) {
                        linea = linea.replace("NOHAB", "" + nHab);
                    }
                    if (linea.indexOf("FECHACOMPL") > 0) {
                        linea = linea.replace("FECHACOMPL", sdf.format(dFechaSalida));
                    }
                    if (linea.indexOf("FECHAHORA") > 0) {
                        linea = linea.replace("FECHAHORA", sdf2.format(dFechaSalida));
                    }
                    if (linea.indexOf("FOLIO") > 0) {
                        linea = linea.replace("FOLIO", folioUltimo);
                    }
                    if (linea.indexOf("CONTROLTV") > 0) {
                        linea = linea.replace("CONTROLTV", entregaCtrlTv);
                    }
                    if (linea.indexOf("CONTROLCLIMA") > 0) {
                        linea = linea.replace("CONTROLCLIMA", sEntregaCtrolServ);
                    }
                    if (linea.indexOf("RESULTADOESTUDIOS") > 0) {
                        linea = linea.replace("RESULTADOESTUDIOS", sDocumentacion);
                    }
                    if(oServiciosPrestados==null){

                        if (linea.indexOf("LISTASERVICIOSPRESTADOS") > 0) {
                            linea = linea.replace("LISTASERVICIOSPRESTADOS", "");
                        }
                        
                    }else{
                        String sListaServicios=construyeListaServiciosPrestados(oServiciosPrestados);
                        
                        if (linea.indexOf("LISTASERVICIOSPRESTADOS") > 0) {
                            linea = linea.replace("LISTASERVICIOSPRESTADOS",sListaServicios );
                        }
                    }

                    str += linea + " ";

                }
                str += "</body>";

                htmlWorker.parse(new StringReader(str));
                document.close();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        }
        return nombrePDF;
    }
    //devuelve una cadena con la lista de servicios prestado para mandarla al pase de salida
    public String construyeListaServiciosPrestados(ServicioPrestado oServiciosPrestados[]) throws Exception{

        
        //nota pasar a un archivo aparte
        String sServicios="";
        if(oServiciosPrestados!=null){
            ExternalContext extCont = FacesContext.getCurrentInstance().getExternalContext();
            File folder = new File(extCont.getRealPath("//resources//"));
            
            try{
            File myhtml = new File(folder, "docs/contratos/paseSalidaHospAnexoServPrestados.html");
            FileInputStream fileinput = null;
            BufferedInputStream mybuffer = null;
            DataInputStream datainput = null;
            fileinput = new FileInputStream(myhtml);
            mybuffer = new BufferedInputStream(fileinput);
            datainput = new DataInputStream(mybuffer);
            int i = 0;
                while (datainput.available() != 0) {
                    i++;
                    sServicios += datainput.readLine();
                    
                }
                
                for (ServicioPrestado oServiciosPrestado : oServiciosPrestados) {
                    sServicios += oServiciosPrestado.getConcepPrestado().getDescripConcep() + "<br/>";
                }
            
            }catch (IOException e) {
                e.printStackTrace();
            }
            
        }else{
            throw new Exception("Funcion.construyeListaDeServiciosPrestados: error de comportamiento. se requiere un vector no nulo");
        }
            
        
        sServicios+="</p></td></tr>";
        
        return sServicios;
    }
    
    public String creaSolicitudInterconsulta(List<ConceptoIngreso> servicios, 
            int epi, String pac, int hab, String tratante, String capturista) 
            throws FileNotFoundException, Exception {

        String nombrePDF = "";
        if (servicios.isEmpty() || epi == 0 || 
            pac.equals("") || tratante.equals("") || capturista.equals("")) {
            throw new Exception("Funcion.creaSolicitudInterconsulta: error de programación, faltan datos");
        } else {
            nombrePDF = "docs/contratosCreadosPac/solicitudInterconsulta-" + 
                    epi + ".pdf";
            ExternalContext extCont = FacesContext.getCurrentInstance().getExternalContext();
            File folder = new File(extCont.getRealPath("//resources//"));
            String urlImg = extCont.getRealPath("//utilidades//");

            try {
                urlImg = urlImg.replace("\\utilidades", "\\");
                Document document = new Document(PageSize.LETTER, 70, 70, 25, 25);
                PdfWriter.getInstance(document, new FileOutputStream(
                        new File(folder, 
                        "docs/contratosCreadosPac/solicitudInterconsulta-" + 
                        epi + ".pdf")));
                document.open();
                Phrase phrase = new Phrase(60);
                HTMLWorker htmlWorker = new HTMLWorker(document);

                for (int j = 0; j < servicios.size(); j++) {
                    String str = "<body>";
                    File myhtml = new File(folder, "docs/contratos/solicitudInterconsulta.html");
                    FileInputStream fileinput = null;
                    BufferedInputStream mybuffer = null;
                    DataInputStream datainput = null;
                    fileinput = new FileInputStream(myhtml);
                    mybuffer = new BufferedInputStream(fileinput);
                    datainput = new DataInputStream(mybuffer);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMMMM-yyyy");
                    SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
                    int i = 0;
                    while (datainput.available() != 0) {
                        i++;
                        String linea = datainput.readLine();

                        if (linea.indexOf("IMAGEN") > 0) {
                            linea = linea.replace("IMAGEN", urlImg + new PlantillaJB().getLogo());
                        }
                        if (linea.indexOf("NOMBREPAC") > 0) {
                            linea = linea.replace("NOMBREPAC", pac);
                        }
                        if (linea.indexOf("NOHAB") > 0) {
                            linea = linea.replace("NOHAB", (""+((hab==0)?"Consulta Externa":hab)));
                        }
                        if (linea.indexOf("FECHACOMPLETA") > 0) {
                            linea = linea.replace("FECHACOMPLETA", sdf.format(servicios.get(j).getConServ().getFechaInterconsulta()));
                        }
                        if (linea.indexOf("FECHAHORA") > 0) {
                            linea = linea.replace("FECHAHORA", sdf2.format(servicios.get(j).getConServ().getFechaInterconsulta()));
                        }
                        if (linea.indexOf("FOLIOSERV") > 0) {
                            linea = linea.replace("FOLIOSERV", servicios.get(j).getConServ().getFolioServ());
                        }
                        if (linea.indexOf("NOMBREMEDINTE") > 0) {
                            linea = linea.replace("NOMBREMEDINTE", "" + servicios.get(j).getConServ().getMedicoHonorarios().getNombreCompleto());
                        }
                        if (linea.indexOf("NOMBRECONC") > 0) {
                            linea = linea.replace("NOMBRECONC", servicios.get(j).getDescripcion());
                        }
                        if (linea.indexOf("NOMBREMEDTRA") > 0) {
                            linea = linea.replace("NOMBREMEDTRA", "" + tratante);
                        }
                        if (linea.indexOf("NOMBRECAP") > 0) {
                            linea = linea.replace("NOMBRECAP", "" + capturista);
                        }

                        str += linea + " ";

                    }
                    str += "</body>";
                    String str2 = str;
                    str2 += "<br><br><br>";
                    str2 += str;
                    htmlWorker.parse(new StringReader(str2));
                    if ((servicios.size() - 1) != i) {
                        document.newPage();
                    }

                }
                document.close();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        }
        return nombrePDF;
    }

    public String creaComprobanteProceQuirurgico(List<ServicioPrestado> servicios, int epi, String pac, int hab, String tratante) throws FileNotFoundException, Exception {

        String nombrePDF = "";
        if (servicios.isEmpty() || epi == 0 || pac.equals("") || hab == 0 || tratante.equals("")) {
            throw new Exception("Funcion.creaSolicitudInterconsulta: error de programación, faltan datos");
        } else {
            nombrePDF = "docs/contratosCreadosPac/comprobanteProceQuirurgico-" + epi + ".pdf";
            ExternalContext extCont = FacesContext.getCurrentInstance().getExternalContext();
            File folder = new File(extCont.getRealPath("//resources//"));
            String urlImg = extCont.getRealPath("//utilidades//");
            try {
                urlImg = urlImg.replace("\\utilidades", "\\");
                Document document = new Document(PageSize.LETTER, 70, 70, 25, 25);
                PdfWriter.getInstance(document, new FileOutputStream(new File(folder, "docs/contratosCreadosPac/comprobanteProceQuirurgico-" + epi + ".pdf")));
                document.open();
                Phrase phrase = new Phrase(60);
                HTMLWorker htmlWorker = new HTMLWorker(document);

                for (int j = 0; j < servicios.size(); j++) {
                    String str = "<body>";
                    File myhtml = new File(folder, "docs/contratos/comprobanteProceQuirurgico.html");
                    FileInputStream fileinput = null;
                    BufferedInputStream mybuffer = null;
                    DataInputStream datainput = null;
                    fileinput = new FileInputStream(myhtml);
                    mybuffer = new BufferedInputStream(fileinput);
                    datainput = new DataInputStream(mybuffer);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMMMM-yyyy h:mm:ss");
                    SimpleDateFormat sdf2 = new SimpleDateFormat("h:mm:ss");
                    int i = 0;
                    long tiempoInicial = servicios.get(j).getServProcQx().getProcedimientoRealizado().getIni().getTime();
                    long tiempoFinal = servicios.get(j).getServProcQx().getProcedimientoRealizado().getFin().getTime();
                    long resta = tiempoFinal - tiempoInicial;

                    while (datainput.available() != 0) {
                        i++;
                        String linea = datainput.readLine();

                        if (linea.indexOf("IMAGEN") > 0) {
                            linea = linea.replace("IMAGEN", urlImg + new PlantillaJB().getLogo());
                        }
                        if (linea.indexOf("NOMBREPAC") > 0) {
                            linea = linea.replace("NOMBREPAC", pac);
                        }
                        if (linea.indexOf("NOHAB") > 0) {
                            linea = linea.replace("NOHAB", "" + hab);
                        }
                        if (linea.indexOf("FECHAINICIO") > 0) {
                            linea = linea.replace("FECHAINICIO", sdf.format(servicios.get(j).getServProcQx().getProcedimientoRealizado().getIni()));
                        }
                        if (linea.indexOf("FECHAFIN") > 0) {
                            linea = linea.replace("FECHAFIN", sdf.format(servicios.get(j).getServProcQx().getProcedimientoRealizado().getFin()));
                        }
                        if (linea.indexOf("FOLIOSERV") > 0) {
                            linea = linea.replace("FOLIOSERV", servicios.get(j).getIdFolio());
                        }
                        if (linea.indexOf("PPRROOCCEE") > 0) {
                            linea = linea.replace("PPRROOCCEE", servicios.get(j).getServProcQx().getProcedimientoRealizado().getTipoProcQx().getDescripcion());
                        }
                        if (linea.indexOf("DDUURRAACCIIOONN") > 0) {
                            linea = linea.replace("DDUURRAACCIIOONN", "" + sdf2.format(new Date(resta)));
                        }
                        if (linea.indexOf("MEDENF") > 0) {
                            linea = linea.replace("MEDENF", "" + servicios.get(j).getMedico().getNombreCompleto());
                        }
                        if (linea.indexOf("NOMBREMEDTRA") > 0) {
                            linea = linea.replace("NOMBREMEDTRA", "" + tratante);
                        }
                        if (linea.indexOf("RROOLLF") > 0) {
                            linea = linea.replace("RROOLLF", "" + servicios.get(j).getServProcQx().getDescripRol());
                        }

                        str += linea + " ";

                    }
                    str += "</body>";
                    String str2 = str;
                    str2 += "<br><br><br>";
                    str2 += str;
                    htmlWorker.parse(new StringReader(str2));
                    if ((servicios.size() - 1) != i) {
                        document.newPage();
                    }

                }
                document.close();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        }
        return nombrePDF;
    }

    public String creaEncuestaHospitalYCheckList(int nEpi, String sNombrePac, boolean encuesta, boolean checklist, int nHab, Date dFechaIngreso, Date dFechaEgreso) throws FileNotFoundException, Exception {

        String nombrePDF = "";
        if (sNombrePac.equals("") || nEpi == 0) {
            throw new Exception("Funcion.creaContratoIngresHosp: error de programación, faltan datos");
        } else {
            if (encuesta || checklist) {

                nombrePDF = "docs/contratosCreadosPac/encuestaHospitalYCheckList-" + nEpi + ".pdf";
                ExternalContext extCont = FacesContext.getCurrentInstance().getExternalContext();
                File folder = new File(extCont.getRealPath("//resources//"));
                String urlImg = extCont.getRealPath("//utilidades//");

                try {
                    urlImg = urlImg.replace("\\utilidades", "\\");
                    Document document = new Document(PageSize.LETTER, 70, 70, 25, 25);
                    PdfWriter.getInstance(document, new FileOutputStream(new File(folder, "docs/contratosCreadosPac/encuestaHospitalYCheckList-" + nEpi + ".pdf")));
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMMMM-yyyy");
                    HTMLWorker htmlWorker = new HTMLWorker(document);
                    document.open();
                    String str = "";

                    if (encuesta) {

                        str += "<p align='center'><img align=\"center\" src='" + urlImg + new PlantillaJB().getLogo() + "' width=\"400\" height=\"119\"></p>\n"
                                + "<div style=\" line-height:110%\">\n";

                        File myhtml = new File(folder, "docs/contratos/encuestaHospital.html");
                        FileInputStream fileinput = null;
                        BufferedInputStream mybuffer = null;
                        DataInputStream datainput = null;
                        fileinput = new FileInputStream(myhtml);
                        mybuffer = new BufferedInputStream(fileinput);
                        datainput = new DataInputStream(mybuffer);

                        while (datainput.available() != 0) {
                            String linea = datainput.readLine();
                            if (linea.indexOf("NOMBREPAC") > 0) {
                                linea = linea.replace("NOMBREPAC", sNombrePac);
                            }
                            if (linea.indexOf("FECHACOMPL") > 0) {
                                linea = linea.replace("FECHACOMPL", sdf.format(new Date()));
                            }
                            str += linea + " ";
                        }
                        str += "</div>";

                        htmlWorker.parse(new StringReader(str));
                        if (checklist) {
                            document.newPage();
                        }

                    }
                    if (checklist) {

                        str = "";
                        str += "<p align='center'><img align=\"center\" src='" + urlImg + new PlantillaJB().getLogo() + "' width=\"400\" height=\"119\"></p>\n"
                                + "<div style=\" line-height:110%\">\n";

                        File myhtml = new File(folder, "docs/contratos/checkListExpedienteClinico.html");
                        FileInputStream fileinput = null;
                        BufferedInputStream mybuffer = null;
                        DataInputStream datainput = null;
                        fileinput = new FileInputStream(myhtml);
                        mybuffer = new BufferedInputStream(fileinput);
                        datainput = new DataInputStream(mybuffer);

                        while (datainput.available() != 0) {
                            String linea = datainput.readLine();

                            if (linea.indexOf("NOMBREPAC") > 0) {
                                linea = linea.replace("NOMBREPAC", sNombrePac);
                            }
                            if (linea.indexOf("NOHAB") > 0) {
                                linea = linea.replace("NOHAB", "" + nHab);
                            }
                            if (linea.indexOf("FECHAINGRESO") > 0) {
                                linea = linea.replace("FECHAINGRESO", sdf.format(dFechaIngreso));
                            }
                            if (linea.indexOf("FECHAEGRESO") > 0) {
                                linea = linea.replace("FECHAEGRESO", sdf.format(dFechaEgreso));
                            }
                            str += linea + " ";
                        }
                        str += "</div>";
                        htmlWorker.parse(new StringReader(str));

                    }
                    document.close();

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
            }
        }
        return nombrePDF;
    }
    
     public String creaPagare(String sNombreRes, float nCantidadPagar, String sCantidadPagar, float nInteres, String sFechaPago, int nEpi) throws Exception {
        String nombrePDF = "";
        System.out.println(sNombreRes+" - "+nCantidadPagar+" - "+sCantidadPagar+" - "+ nInteres+" - "+sFechaPago+" - "+nEpi);
        if (sNombreRes.equals("") || nCantidadPagar==0 || sCantidadPagar.equals("") || nInteres==0 || sFechaPago.equals("") || nEpi==0) {
            throw new Exception("Funcion.creaContratoIngresHosp: error de programación, faltan datos");
        } else {
            nombrePDF = "docs/contratosCreadosPac/pagare-" + nEpi + ".pdf";
            ExternalContext extCont = FacesContext.getCurrentInstance().getExternalContext();
            File folder = new File(extCont.getRealPath("//resources//"));
            String urlImg = extCont.getRealPath("/");
            try {
                //urlImg = urlImg.replace("\\utilidades", "\\");

                Document document = new Document(PageSize.LETTER, 70, 70, 30, 30);
                PdfWriter.getInstance(document, new FileOutputStream(new File(folder, "docs/contratosCreadosPac/pagare-" + nEpi + ".pdf")));

                document.open();
                Phrase phrase = new Phrase(90);
                HTMLWorker htmlWorker = new HTMLWorker(document);
                String str = "<p align='center'><img align=\"center\" src='" + urlImg + new PlantillaJB().getLogo() + "' width=\"430\" height=\"70\"></p>\n"
                        + "<div style=\" line-height:95%\">\n";
                File myhtml = new File(folder, "docs/contratos/pagare.html");
                FileInputStream fileinput = null;
                BufferedInputStream mybuffer = null;
                DataInputStream datainput = null;
                fileinput = new FileInputStream(myhtml);
                mybuffer = new BufferedInputStream(fileinput);
                datainput = new DataInputStream(mybuffer);
                Calendar c1 = GregorianCalendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMMMM-yyyy");

                int i = 0;
                while (datainput.available() != 0) {
                    i++;
                    String linea = datainput.readLine();

                    if (linea.indexOf("NOMBRERES") > 0) {
                        linea = linea.replace("NOMBRERES", sNombreRes);
                    }
                    if (linea.indexOf("DIAPAGO") > 0) {
                        linea = linea.replace("DIAPAGO", sFechaPago  );
                    }
                    if (linea.indexOf("CANTIDADPAGO") > 0) {
                        linea = linea.replace("CANTIDADPAGO", "" + nCantidadPagar);
                    }
                    if (linea.indexOf("LETRAPAGO") > 0) {
                        linea = linea.replace("LETRAPAGO", "" + sCantidadPagar);
                    }
                    if (linea.indexOf("INTERESPAGO") > 0) {
                        linea = linea.replace("INTERESPAGO", "" + nInteres);
                    }
                    str += linea + " ";

                }
                str += "</div>";

                htmlWorker.parse(new StringReader(str));
                document.close();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (DocumentException e) {
                e.printStackTrace();
            }

        }
        return nombrePDF;
    }


}
