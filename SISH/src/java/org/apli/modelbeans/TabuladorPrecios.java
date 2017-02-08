package org.apli.modelbeans;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import org.apli.AD.AccesoDatos;
import org.apli.modelbeans.ventaCredito.CompaniaCred;

/**
 * Entidad que representa al tabulador de precios por empresa
 * @author BAOZ
 */
public class TabuladorPrecios {
    private CompaniaCred oCiaCred = new CompaniaCred();
    private ConceptoIngreso oConcepIngr = new ConceptoIngreso();
    private float nMonto = 0.0f;
    private AccesoDatos oAD = null;
    
    public String actualizar() throws Exception{
    Vector rst = null;
    String sQuery = "";
        if (this.oCiaCred==null || this.oCiaCred.getIdEmp()==0 ||
            this.oConcepIngr==null || this.oConcepIngr.getCveConcep()==0){
            throw new Exception("TabuladorPrecios.actualizar: faltan datos");
	}else {
            sQuery="SELECT * from actualizaTabuladorPrecios("+
                    this.oCiaCred.getIdEmp() + "::smallint, " +
                    this.oConcepIngr.getCveConcep() + ", " +
                    this.nMonto + ");";
            if(oAD==null){
                oAD=new AccesoDatos();
                oAD.conectar();
                rst=oAD.ejecutarConsulta(sQuery);
                oAD.desconectar();
                oAD=null;
            }else{
                rst=oAD.ejecutarConsulta(sQuery); //regresa el código
            }
        }
        return ""+(rst==null?"":
                rst.get(0).toString().substring(1, 
                rst.get(0).toString().length() - 1));
    }
    
    public List<TabuladorPrecios> buscaRptTabulador() throws Exception{
    List<TabuladorPrecios> arrRes=null;
    Vector rst = null;
    String sQuery="";
        if (this.oCiaCred==null){
            throw new Exception("TabuladorPrecios.buscaRptTabulador: faltan datos");
	}else {
            sQuery="SELECT * from buscaTabuladorPrecios(0::smallint, "+
                    this.oCiaCred.getIdEmp() + "::smallint);";
            if(oAD==null){
                oAD=new AccesoDatos();
                oAD.conectar();
                rst=oAD.ejecutarConsulta(sQuery);
                oAD.desconectar();
                oAD=null;
            }else{
                rst=oAD.ejecutarConsulta(sQuery); //regresa el código
            }
            if (rst != null) {
                arrRes = new ArrayList();
                for (int i = 0; i < rst.size(); i++) {
                    TabuladorPrecios oTab = new TabuladorPrecios();
                    Vector vRowTemp = (Vector) rst.elementAt(i);
                    oTab.getConcepIngr().getLineaIngreso().setDescrip(
                            (String) vRowTemp.elementAt(0));
                    oTab.getConcepIngr().setCveConcep(
                            ((Double)vRowTemp.elementAt(1)).intValue());
                    oTab.getConcepIngr().setDescripcion(
                            (String) vRowTemp.elementAt(2));
                    oTab.setMonto(((Double)vRowTemp.elementAt(3)).floatValue());
                    arrRes.add(oTab);
                }
            }
        }
        return arrRes;
    }
    
    /**
     * @return the oCiaCred
     */
    public CompaniaCred getCiaCred() {
        return oCiaCred;
    }

    /**
     * @param oCiaCred the oCiaCred to set
     */
    public void setCiaCred(CompaniaCred oCiaCred) {
        this.oCiaCred = oCiaCred;
    }

    /**
     * @return the oConcepIngr
     */
    public ConceptoIngreso getConcepIngr() {
        return oConcepIngr;
    }

    /**
     * @param oConcepIngr the oConcepIngr to set
     */
    public void setConcepIngr(ConceptoIngreso oConcepIngr) {
        this.oConcepIngr = oConcepIngr;
    }

    /**
     * @return the nMonto
     */
    public float getMonto() {
        return nMonto;
    }

    /**
     * @param nMonto the nMonto to set
     */
    public void setMonto(float nMonto) {
        this.nMonto = nMonto;
    }
}
