/**
 * Representa la compañía (empresa, aseguradora, banco, escuela) que cubre un
 * tratamiento médico
 *
 * @author Isabel Espinoza /BAOZ
 * @version 1.0
 * @created 07-abr-2014 
 */
package org.apli.modelbeans.ventaCredito;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import org.apli.AD.AccesoDatos;
import org.apli.modelbeans.ContactoCia;
import org.apli.modelbeans.facturacion.cfdi.v32.schema.TUbicacion;

public class CompaniaCred implements Serializable {

    /**
     * Indica si la compañía está activa o inactiva
     */
    private char bActiva;
    /**
     * Identificador de la empresa, es una numeración interna
     */
    private int nIdEmp;
    private int nPlazoPago;
    /**
     * Nombre corto asignado a la empresa
     */
    private String sNombreCorto;
    /**
     * Nombre oficial o razón social de la empresa
     */
    private String sNomRazSoc;
    /**
     * Políticas de pago que sigue la compañía para cubrir los servicios
     * otorgados por el sanatorio
     */
    private String sPoliticasPago;
    /**
     * RFC de la empresa
     */
    private String sRFC;
    private int nTipoCia;
    private TUbicacion oDomicilioLocal;
    private String sTelLocal;
    private List<ContactoCia> listContactos;
    private short nTipoConvenio;
    
    protected AccesoDatos oAD = null;

    public CompaniaCred() {
        oDomicilioLocal = new TUbicacion();

    }

    public CompaniaCred buscaNombreEmpresa(int id) throws Exception {
        CompaniaCred oCompCred = new CompaniaCred();

        Vector rst = null;
        String sQuery = "";

        //sQuery = "select nidemp, snombrecorto from companiacred where nidemp=" + id + ";";
        sQuery = "select * from buscanombreempresa(" + id + ")";
        if (getAD() == null) {
            setAD(new AccesoDatos());
            getAD().conectar();
            rst = getAD().ejecutarConsulta(sQuery);
            getAD().desconectar();
            setAD(null);
        } else {
            rst = getAD().ejecutarConsulta(sQuery);
        }
        if (rst != null) {
            for (int i = 0; i < rst.size(); i++) {
                oCompCred = new CompaniaCred();
                Vector vRowTemp = (Vector) rst.elementAt(i);
                oCompCred.setIdEmp(((Double) vRowTemp.elementAt(0)).intValue());
                oCompCred.setNombreCorto((String) vRowTemp.elementAt(1));
            }
        }
        return oCompCred;
    }

    public List<CompaniaCred> getCompaniasCred() throws Exception {
        CompaniaCred oP = null;
        Vector rst = null;
        Vector<CompaniaCred> vObj = null;
        List<CompaniaCred> listCompaniasCred = new ArrayList();
        String sQuery = "";
        int i = 0, nTam = 0;
        sQuery = "select * from buscacompaniascred()";
        if (getAD() == null) {
            setAD(new AccesoDatos());
            getAD().conectar();
        }
        rst = getAD().ejecutarConsulta(sQuery);
        getAD().desconectar();
        setAD(null);
        if (rst != null) {
            vObj = new Vector<CompaniaCred>();
            for (i = 0; i < rst.size(); i++) {
                oP = new CompaniaCred();
                Vector vRowTemp = (Vector) rst.elementAt(i);
                oP.setIdEmp(((Double) vRowTemp.elementAt(0)).intValue());
                oP.setNombreCorto((String) vRowTemp.elementAt(1));
                oP.setNomRazSoc((String) vRowTemp.elementAt(2));
                oP.setRFC((String) vRowTemp.elementAt(3));
                oP.setPoliticasPago((String) vRowTemp.elementAt(4));
                oP.setActiva(((String) vRowTemp.elementAt(5)).charAt(0));
                oP.setTipoCia(((Double) vRowTemp.elementAt(6)).intValue());
                oP.setPlazoPago(((Double) vRowTemp.elementAt(7)).intValue());
                oP.setTelLocal((String)vRowTemp.elementAt(9));
                oP.setTipoConvenio(((Double)vRowTemp.elementAt(10)).shortValue());
                listCompaniasCred.add(oP);
            }
        }
        return listCompaniasCred;
    }
    
    /**
     * Busca la lista de compañías de crédito activas que NO son el
     * propio hospital
     */
    public List<CompaniaCred> buscaCiasCredNoSH() throws Exception {
        CompaniaCred oP = null;
        Vector rst = null;
        Vector<CompaniaCred> vObj = null;
        List<CompaniaCred> listCompaniasCred = new ArrayList();
        String sQuery = "";
        int i = 0, nTam = 0;
        sQuery = "select * from buscaCiasCred()";
        if (getAD() == null) {
            setAD(new AccesoDatos());
            getAD().conectar();
        }
        rst = getAD().ejecutarConsulta(sQuery);
        getAD().desconectar();
        setAD(null);
        if (rst != null) {
            vObj = new Vector<CompaniaCred>();
            for (i = 0; i < rst.size(); i++) {
                oP = new CompaniaCred();
                Vector vRowTemp = (Vector) rst.elementAt(i);
                oP.setIdEmp(((Double) vRowTemp.elementAt(0)).intValue());
                oP.setNombreCorto((String) vRowTemp.elementAt(1));
                listCompaniasCred.add(oP);
            }
        }
        return listCompaniasCred;
    }

    public List<CompaniaCred> buscaCompanias() throws Exception {
        CompaniaCred oComp = null;
        ContactoCia oCont = null;
        Vector rst = null;
        List<CompaniaCred> listRet = null;

        String sQuery = "";
        sQuery = "select * from buscadatoscompanias()";
        if (getAD() == null) {
            setAD(new AccesoDatos());
            getAD().conectar();
        }
        rst = getAD().ejecutarConsulta(sQuery);
        getAD().desconectar();
        setAD(null);
        if (rst != null) {
            listRet = new ArrayList();
            for (int i = 0; i < rst.size(); i++) {
                Vector vRowTemp = (Vector) rst.elementAt(i);
                if (i == 0) {
                    oComp = new CompaniaCred();
                    oComp.setIdEmp(((Double) vRowTemp.elementAt(0)).intValue());
                    oComp.setNombreCorto((String) vRowTemp.elementAt(1));
                    oComp.setNomRazSoc((String) vRowTemp.elementAt(2));
                    oComp.setRFC((String) vRowTemp.elementAt(3));
                    oComp.setPoliticasPago((String) vRowTemp.elementAt(4));
                    oComp.setActiva(((String) vRowTemp.elementAt(5)).charAt(0));
                    oComp.setTipoCia(((Double) vRowTemp.elementAt(6)).intValue());
                    oComp.setPlazoPago(((Double) vRowTemp.elementAt(7)).intValue());
                    oComp.setTelLocal((String) vRowTemp.elementAt(8));

                    oComp.getDomicilioLocal().setIdTUbicacion(((Double) vRowTemp.elementAt(9)).intValue());
                    oComp.getDomicilioLocal().setCodigoPostal((String) vRowTemp.elementAt(10));
                    oComp.getDomicilioLocal().getEdo().setCve((String) vRowTemp.elementAt(11));
                    oComp.getDomicilioLocal().getEdo().setDescrip((String) vRowTemp.elementAt(12));
                    oComp.getDomicilioLocal().getMun().setCve((String) vRowTemp.elementAt(13));
                    oComp.getDomicilioLocal().getMun().setDescrip((String) vRowTemp.elementAt(14));
                    oComp.getDomicilioLocal().getCd().setCve((String) vRowTemp.elementAt(15));
                    oComp.getDomicilioLocal().getCd().setDescrip((String) vRowTemp.elementAt(16));
                    oComp.getDomicilioLocal().setCalle((String) vRowTemp.elementAt(17));
                    oComp.getDomicilioLocal().setNoExterior((String) vRowTemp.elementAt(18));
                    oComp.getDomicilioLocal().setNoInterior((String) vRowTemp.elementAt(19));
                    oComp.getDomicilioLocal().setColonia((String) vRowTemp.elementAt(20));
                    oComp.getDomicilioLocal().setReferencia((String) vRowTemp.elementAt(21));

                    oCont = new ContactoCia();
                    oCont.setNumContacto(((Double) vRowTemp.elementAt(22)).intValue());
                    oCont.setNombre((String) vRowTemp.elementAt(23));
                    oCont.setApPaterno((String) vRowTemp.elementAt(24));
                    oCont.setApMaterno((String) vRowTemp.elementAt(25));
                    oCont.setTelefono((String) vRowTemp.elementAt(26));
                    oCont.setCorreo((String) vRowTemp.elementAt(27));
                    oCont.setTipo((String) vRowTemp.elementAt(28));
                    oComp.setListContactos(new ArrayList());
                    oComp.getListContactos().add(oCont);

                    listRet.add(oComp);
                } else {
                    if ((listRet.get(listRet.size() - 1)).getIdEmp() == ((Double) vRowTemp.elementAt(0)).intValue()) {
                        oCont = new ContactoCia();
                        oCont.setNumContacto(((Double) vRowTemp.elementAt(22)).intValue());
                        oCont.setNombre((String) vRowTemp.elementAt(23));
                        oCont.setApPaterno((String) vRowTemp.elementAt(24));
                        oCont.setApMaterno((String) vRowTemp.elementAt(25));
                        oCont.setTelefono((String) vRowTemp.elementAt(26));
                        oCont.setCorreo((String) vRowTemp.elementAt(27));
                        oCont.setTipo((String) vRowTemp.elementAt(28));
                        oComp.getListContactos().add(oCont);
                    } else {
                        oComp = new CompaniaCred();
                        oComp.setIdEmp(((Double) vRowTemp.elementAt(0)).intValue());
                        oComp.setNombreCorto((String) vRowTemp.elementAt(1));
                        oComp.setNomRazSoc((String) vRowTemp.elementAt(2));
                        oComp.setRFC((String) vRowTemp.elementAt(3));
                        oComp.setPoliticasPago((String) vRowTemp.elementAt(4));
                        oComp.setActiva(((String) vRowTemp.elementAt(5)).charAt(0));
                        oComp.setTipoCia(((Double) vRowTemp.elementAt(6)).intValue());
                        oComp.setPlazoPago(((Double) vRowTemp.elementAt(7)).intValue());
                        oComp.setTelLocal((String) vRowTemp.elementAt(8));

                        oComp.getDomicilioLocal().setIdTUbicacion(((Double) vRowTemp.elementAt(9)).intValue());
                        oComp.getDomicilioLocal().setCodigoPostal((String) vRowTemp.elementAt(10));
                        oComp.getDomicilioLocal().getEdo().setCve((String) vRowTemp.elementAt(11));
                        oComp.getDomicilioLocal().getEdo().setDescrip((String) vRowTemp.elementAt(12));
                        oComp.getDomicilioLocal().getMun().setCve((String) vRowTemp.elementAt(13));
                        oComp.getDomicilioLocal().getMun().setDescrip((String) vRowTemp.elementAt(14));
                        oComp.getDomicilioLocal().getCd().setCve((String) vRowTemp.elementAt(15));
                        oComp.getDomicilioLocal().getCd().setDescrip((String) vRowTemp.elementAt(16));
                        oComp.getDomicilioLocal().setCalle((String) vRowTemp.elementAt(17));
                        oComp.getDomicilioLocal().setNoExterior((String) vRowTemp.elementAt(18));
                        oComp.getDomicilioLocal().setNoInterior((String) vRowTemp.elementAt(19));
                        oComp.getDomicilioLocal().setColonia((String) vRowTemp.elementAt(20));
                        oComp.getDomicilioLocal().setReferencia((String) vRowTemp.elementAt(21));

                        oCont = new ContactoCia();
                        oCont.setNumContacto(((Double) vRowTemp.elementAt(22)).intValue());
                        oCont.setNombre((String) vRowTemp.elementAt(23));
                        oCont.setApPaterno((String) vRowTemp.elementAt(24));
                        oCont.setApMaterno((String) vRowTemp.elementAt(25));
                        oCont.setTelefono((String) vRowTemp.elementAt(26));
                        oCont.setCorreo((String) vRowTemp.elementAt(27));
                        oCont.setTipo((String) vRowTemp.elementAt(28));
                        oComp.setListContactos(new ArrayList());
                        oComp.getListContactos().add(oCont);

                        listRet.add(oComp);
                    }
                }
            }
        }
        return listRet;
    }

    public List<CompaniaCred> getCompaniasCredActivas() throws Exception {
        CompaniaCred oP = null;
        Vector rst = null;
        Vector<CompaniaCred> vObj = null;
        List<CompaniaCred> listCompaniasCred = new ArrayList();
        String sQuery = "";
        int i = 0;
        sQuery = "select * from buscacompaniascredactivas()";
        if (getAD() == null) {
            setAD(new AccesoDatos());
            getAD().conectar();
        }
        rst = getAD().ejecutarConsulta(sQuery);
        getAD().desconectar();
        setAD(null);
        if (rst != null) {
            vObj = new Vector<CompaniaCred>();
            for (i = 0; i < rst.size(); i++) {
                oP = new CompaniaCred();
                Vector vRowTemp = (Vector) rst.elementAt(i);
                oP.setIdEmp(((Double) vRowTemp.elementAt(0)).intValue());
                oP.setNombreCorto((String) vRowTemp.elementAt(1));
                oP.setNomRazSoc((String) vRowTemp.elementAt(2));
                oP.setRFC((String) vRowTemp.elementAt(3));
                oP.setPoliticasPago((String) vRowTemp.elementAt(4));
                oP.setActiva(((String) vRowTemp.elementAt(5)).charAt(0));
                oP.setTipoCia(((Double) vRowTemp.elementAt(6)).intValue());
                oP.setPlazoPago(((Double) vRowTemp.elementAt(7)).intValue());
                oP.setTelLocal((String)vRowTemp.elementAt(9));
                oP.setTipoConvenio(((Double)vRowTemp.elementAt(10)).shortValue());
                listCompaniasCred.add(oP);
            }
        }
        return listCompaniasCred;
    }
/*
    public List<CompaniaCred> todasCompaniasCredActivasPorTipo() throws Exception {
        CompaniaCred oP = null;
        Vector rst = null;
        Vector<CompaniaCred> vObj = null;
        List<CompaniaCred> listCompaniasCred = new ArrayList();
        String sQuery = "";
        int i = 0;
        sQuery = "SELECT * FROM buscaTodosCompaniaCredPorTipo();";
        if (getAD() == null) {
            setAD(new AccesoDatos());
            getAD().conectar();
        }
        rst = getAD().ejecutarConsulta(sQuery);
        getAD().desconectar();
        setAD(null);
        if (rst != null) {
            vObj = new Vector<CompaniaCred>();
            for (i = 0; i < rst.size(); i++) {
                oP = new CompaniaCred();
                Vector vRowTemp = (Vector) rst.elementAt(i);
                oP.setIdEmp(((Double) vRowTemp.elementAt(0)).intValue());
                oP.setNombreCorto((String) vRowTemp.elementAt(1));
                oP.setNomRazSoc((String) vRowTemp.elementAt(2));
                oP.setRFC((String) vRowTemp.elementAt(3));
                oP.setPoliticasPago((String) vRowTemp.elementAt(4));
                oP.setActiva(((String) vRowTemp.elementAt(5)).charAt(0));
                oP.setTipoCia(((Double) vRowTemp.elementAt(6)).intValue());
                oP.setPlazoPago(((Double) vRowTemp.elementAt(7)).intValue());
                oP.setTelLocal((String)vRowTemp.elementAt(9));
                oP.setTipoConvenio(((Double)vRowTemp.elementAt(10)).shortValue());
                listCompaniasCred.add(oP);
            }
        }
        return listCompaniasCred;
    }
*/
    /**
     * Modifica plazo de pago de un Compañia crediticia
     */
    public String modificarPlazoDePago() throws Exception {
        Vector rst = null;
        String sQuery = "";
        if (this.nPlazoPago == 0) {
            throw new Exception("CompaniaCred.modificarPlazoDePago: error de programación, faltan datos");
        } else {
            sQuery = "select * from modificaplazopago(" + this.nIdEmp + "::int2," + this.nPlazoPago + "::int2)";
            //supone que ya viene conectado
            if (getAD() == null) {
                setAD(new AccesoDatos());
                getAD().conectar();
            }
            rst = getAD().ejecutarConsulta(sQuery);
            getAD().desconectar();
            setAD(null);
        }
        return " " + rst.get(0);
    }

    public String modificaEstado(int nIdEmp, char cActiva) throws Exception {
        Vector rst = null;
        String sQuery = "";

        sQuery = "select * from cambiaestadocompaniacred(" + nIdEmp + "::int2,'" + cambiaEstado(cActiva) + "')";
        if (getAD() == null) {
            setAD(new AccesoDatos());
            getAD().conectar();
            rst = getAD().ejecutarConsulta(sQuery);
            getAD().desconectar();
            setAD(null);
        } else {
            rst = getAD().ejecutarConsulta(sQuery);
        }
        return "" + rst.get(0).toString().substring(1, rst.get(0).toString().length() - 1);
    }

    public String insertaCompC(CompaniaCred oCC) throws Exception {
        Vector rst = null;
        String sQuery = "", sRet = "";

        if (oCC == null) {
            throw new Exception("CompaniaCred.inserta: error de programación, faltan datos");
        } else {
            sQuery = "select * from insertacompaniacred('" + 
                    oCC.getNombreCorto().toUpperCase() + "', '" + 
                    oCC.getNomRazSoc().toUpperCase()+ "', '" + 
                    oCC.getRFC().toUpperCase() + "', '" + 
                    oCC.getPoliticasPago().toUpperCase() + "', " + 
                    oCC.getTipoCia() + "::int2, " + oCC.getPlazoPago() + 
                    "::int2, '" + oCC.getTelLocal() + "', '"+ 
                    oCC.getDomicilioLocal().getCodigoPostal() + "', '" + 
                    oCC.getDomicilioLocal().getEdo().getCve() + "', '" + 
                    oCC.getDomicilioLocal().getMun().getCve() + "', '" + 
                    oCC.getDomicilioLocal().getCd().getCve() + "', '" + 
                    oCC.getDomicilioLocal().getCalle().toUpperCase() + "', '" + 
                    oCC.getDomicilioLocal().getNoExterior() + "', '" + 
                    oCC.getDomicilioLocal().getNoInterior() + "', '" + 
                    oCC.getDomicilioLocal().getColonia().toUpperCase() + "', '" + 
                    oCC.getDomicilioLocal().getReferencia().toUpperCase() + "', " + 
                    oCC.getTipoConvenio()+"::int2);";
            for (int i = 0; i < oCC.getListContactos().size(); i++) {
                sQuery = sQuery + "\n select * from insertacontactocia((select currval('companiacred_nidemp_seq')::int2),'" + oCC.getListContactos().get(i).getNombre()
                        + "','" + oCC.getListContactos().get(i).getApPaterno() + "','" + cambiaNulos(oCC.getListContactos().get(i).getApMaterno())
                        + "','" + cambiaNulos(oCC.getListContactos().get(i).getTelefono()) + "','" + cambiaNulos(oCC.getListContactos().get(i).getCorreo())
                        + "','" + oCC.getListContactos().get(i).getTipo() + "');";
            }
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
        if ("Multiple ResultSets were returned by the query".equals(rst.get(0).toString().substring(1, rst.get(0).toString().length() - 1))) {
            sRet = "Se registró la compañia " + oCC.getNombreCorto() + " satisfactoriamente";
        } else {
            sRet = "Se produjo un error mientras se almacenaban los datos. Vuelva a intentarlo";
        }
        return sRet;
    }

    public String modificarCompC(CompaniaCred oCC) throws Exception {
        Vector rst = null;
        String sQuery = "", sRet = "";

        if (oCC == null) {
            throw new Exception("CompaniaCred.modifica: error de programación, faltan datos");
        } else {
            sQuery = "select * from modificaCompCred("+oCC.getIdEmp()+"::int2, '" +
                    oCC.getNombreCorto().toUpperCase() + "', '" + 
                    oCC.getNomRazSoc().toUpperCase() + "', '" + 
                    oCC.getRFC().toUpperCase() + "', '" + 
                    oCC.getPoliticasPago().toUpperCase() + "', " + 
                    oCC.getTipoCia() + "::int2, " + oCC.getPlazoPago() + 
                    "::int2, '" + oCC.getTelLocal() + "', " + 
                    oCC.getDomicilioLocal().getIdTUbicacion() + ",'" + 
                    oCC.getDomicilioLocal().getCodigoPostal() + "', '" + 
                    oCC.getDomicilioLocal().getEdo().getCve() + "', '" + 
                    oCC.getDomicilioLocal().getMun().getCve() + "', '" + 
                    oCC.getDomicilioLocal().getCd().getCve() + "', '" + 
                    oCC.getDomicilioLocal().getCalle().toUpperCase() + "', '" + 
                    oCC.getDomicilioLocal().getNoExterior() + "', '" + 
                    oCC.getDomicilioLocal().getNoInterior() + "', '" + 
                    oCC.getDomicilioLocal().getColonia().toUpperCase() + "', '" + 
                    oCC.getDomicilioLocal().getReferencia().toUpperCase() + "'," +
                    oCC.getTipoConvenio() + "::int2);\ndelete from contactocia where nidemp=" + 
                    oCC.getIdEmp() + "::int2;";
            for (int i = 0; i < oCC.getListContactos().size(); i++) {
                sQuery = sQuery + "\n select * from insertacontactocia((select max(nidemp) from companiacred),'" + 
                        oCC.getListContactos().get(i).getNombre().toUpperCase()
                        + "','" + oCC.getListContactos().get(i).getApPaterno().toUpperCase() + 
                        "','" + cambiaNulos(oCC.getListContactos().get(i).getApMaterno())
                        + "','" + cambiaNulos(oCC.getListContactos().get(i).getTelefono()) 
                        + "','" + cambiaNulos(oCC.getListContactos().get(i).getCorreo())
                        + "','" + oCC.getListContactos().get(i).getTipo() + "');";
            }
            System.out.println(sQuery);
            if (getAD() == null) {
                setAD(new AccesoDatos());
                getAD().conectar();
                rst = getAD().ejecutarConsulta(sQuery);
                getAD().desconectar();
                setAD(null);
            } else {
                try{
                    rst = getAD().ejecutarConsulta(sQuery);
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        }
        if ("Multiple ResultSets were returned by the query".equals(rst.get(0).toString().substring(1, rst.get(0).toString().length() - 1))) {
            sRet = "Se modificó la compañia " + oCC.getNombreCorto() + " satisfactoriamente";
        } else {
            sRet = "Se produjo un error mientras se almacenaban los datos. Vuelva a intentarlo";
        }
        return sRet;
    }

    public List<CompaniaCred> buscaEmpresas() throws Exception {
        List<CompaniaCred> listRet = new ArrayList();
        CompaniaCred oCC;

        Vector rst = null;
        String sQuery = "";

        sQuery = "select * from buscaEmpresas()";

        if (getAD() == null) {
            setAD(new AccesoDatos());
            getAD().conectar();
            rst = getAD().ejecutarConsulta(sQuery);
            getAD().desconectar();
            setAD(null);
        } else {
            rst = getAD().ejecutarConsulta(sQuery);
        }

        if (rst != null) {
            listRet = new ArrayList();
            for (int i = 0; i < rst.size(); i++) {
                Vector vRowTemp = (Vector) rst.elementAt(i);
                oCC = new CompaniaCred();
                oCC.setIdEmp(((Double) vRowTemp.elementAt(0)).intValue());
                oCC.setNombreCorto((String) vRowTemp.elementAt(1));
                listRet.add(oCC);
            }
        }
        return listRet;
    }

    public List<CompaniaCred> buscaBancos() throws Exception {
        List<CompaniaCred> listRet = new ArrayList();
        CompaniaCred oCC;

        Vector rst = null;
        String sQuery = "";

        sQuery = "select * from buscaBancos()";
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

        if (rst != null) {
            listRet = new ArrayList();
            for (int i = 0; i < rst.size(); i++) {
                Vector vRowTemp = (Vector) rst.elementAt(i);
                oCC = new CompaniaCred();
                oCC.setIdEmp(((Double) vRowTemp.elementAt(0)).intValue());
                oCC.setNombreCorto((String) vRowTemp.elementAt(1));
                listRet.add(oCC);
            }
        }
        return listRet;
    }

    public List<CompaniaCred> buscaTodasCompanias() throws Exception {
        List<CompaniaCred> listRet = new ArrayList();
        CompaniaCred oCC;

        Vector rst = null;
        String sQuery = "";

        sQuery = "select * from buscatodascompanias()";

        if (getAD() == null) {
            setAD(new AccesoDatos());
            getAD().conectar();
            rst = getAD().ejecutarConsulta(sQuery);
            getAD().desconectar();
            setAD(null);
        } else {
            rst = getAD().ejecutarConsulta(sQuery);
        }

        if (rst != null) {
            listRet = new ArrayList();
            for (int i = 0; i < rst.size(); i++) {
                Vector vRowTemp = (Vector) rst.elementAt(i);
                oCC = new CompaniaCred();
                oCC.setIdEmp(((Double) vRowTemp.elementAt(0)).intValue());
                oCC.setNombreCorto((String) vRowTemp.elementAt(1));
                listRet.add(oCC);
            }
        }
        return listRet;
    }

    public char cambiaEstado(char var) {
        if (var == '0') {
            return '1';
        } else {
            return '0';
        }
    }

    public String cambiaNulos(String var) {
        if (var == null) {
            return "";
        } else {
            return var;
        }
    }

    public int getTipoCia() {
        return nTipoCia;
    }

    public void setTipoCia(int nTipoCia) {
        this.nTipoCia = nTipoCia;
    }

    public String getRFC() {
        return sRFC;
    }

    public void setRFC(String sRFC) {
        this.sRFC = sRFC;
    }


    public char getActiva() {
        return bActiva;
    }

    public void setActiva(char bActiva) {
        this.bActiva = bActiva;
    }

    public int getIdEmp() {
        return nIdEmp;
    }

    public void setIdEmp(int nIdEmp) {
        this.nIdEmp = nIdEmp;
    }

    public int getPlazoPago() {
        return nPlazoPago;
    }

    public void setPlazoPago(int nPlazoPago) {
        this.nPlazoPago = nPlazoPago;
    }

    public String getNomRazSoc() {
        return sNomRazSoc;
    }

    public void setNomRazSoc(String sNomRazSoc) {
        this.sNomRazSoc = sNomRazSoc;
    }

    public String getNombreCorto() {
        return sNombreCorto;
    }

    public void setNombreCorto(String sNombreCorto) {
        this.sNombreCorto = sNombreCorto;
    }

    public String getPoliticasPago() {
        return sPoliticasPago;
    }

    public void setPoliticasPago(String sPoliticasPago) {
        this.sPoliticasPago = sPoliticasPago;
    }

    public AccesoDatos getAD() {
        return oAD;
    }

    public void setAD(AccesoDatos oAD) {
        this.oAD = oAD;
    }

    public TUbicacion getDomicilioLocal() {
        return oDomicilioLocal;
    }

    public void setDomicilioLocal(TUbicacion oDomicilioLocal) {
        this.oDomicilioLocal = oDomicilioLocal;
    }

    public String getTelLocal() {
        return sTelLocal;
    }

    public void setTelLocal(String sTelLocal) {
        this.sTelLocal = sTelLocal;
    }
    
    public short getTipoConvenio(){
        return this.nTipoConvenio;
    }
    public void setTipoConvenio(short value){
        this.nTipoConvenio = value;
    }

    public List<ContactoCia> getListContactos() {
        return listContactos;
    }

    public void setListContactos(List<ContactoCia> listContactos) {
        this.listContactos = listContactos;
    }
}