package org.apli.modelbeans.contabilidadInterna;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import org.apli.AD.AccesoDatos;
import org.apli.modelbeans.FormaPago;
import org.apli.modelbeans.facturacion.cfdi.FacturaCFI;

/**
 *
 * @author Lily_LnBnd
 */
public class MovimientoCtaBan implements Serializable{
    private int nIdMovCtaBan;
    private CuentaBanco oCuentaBanco;
    private ConceptoMovCtaBan oConceptoMov;
    private FormaPago oFormaPago;
    private Date dFechaMov;
    private Float nMonto;
    private String sNumDocto;
    private final String sReferenciaF="Pago de Pedido ";
    private String sReferencia;
    private Gasto oGasto;
    private AccesoDatos oAD;
    
    public MovimientoCtaBan(){
        oCuentaBanco=new CuentaBanco();
        oConceptoMov=new ConceptoMovCtaBan();
        oFormaPago=new FormaPago(); 
    }
    
    public String guardar(Gasto oGasto)throws Exception{
        Vector rst = null;
        String sQuery = "", sRet="";
        
        if (oGasto==null){
             throw new Exception("Movimiento.guarda: error de programación, faltan datos");
        }else{
            sQuery="select * from guardaMovimiento("+oGasto.getMovCtaBan().getCuentaBanco().getBanco().getIdEmp()+"::int2, '"+
                    oGasto.getMovCtaBan().getCuentaBanco().getNumCtaBan()+"', "+oGasto.getMonto()+", '"+oGasto.getMovCtaBan().getNumDocto()+
                    "', '"+oGasto.getMovCtaBan().getFormaPago().getCveFrmPago()+"', '"+oGasto.getMovCtaBan().getReferenciaF()+" "+oGasto.getIdGasto()+
                    "', "+oGasto.getIdGasto()+", "+validaFecha(oGasto.getPropPago())+")";
            if (getAD() == null){
                setAD(new AccesoDatos());
                getAD().conectar();
                rst = getAD().ejecutarConsulta(sQuery);
                getAD().desconectar();
                setAD(null);
            }else{
                rst = getAD().ejecutarConsulta(sQuery);
            }
        }
        return "" + rst.get(0).toString().substring(1, rst.get(0).toString().length() - 1);
    }
    
    public String validaFecha(Date dFec){
        if (dFec==null)
            return "null";
        else
            return "'"+dFec+"'";
    }
    
    public List<MovimientoCtaBan> buscaMovimientos(String sCondicion)throws Exception{
        MovimientoCtaBan oMov=null;
        List<MovimientoCtaBan> listRet=null;
        Vector rst = null;
        String sQuery = "";
        
        sQuery = "select * from buscamovimientos()";
        if (!sCondicion.equals(""))
            sQuery=sQuery+" where "+sCondicion;
        if (getAD() == null){
            setAD(new AccesoDatos());
            getAD().conectar();
        }
        rst = getAD().ejecutarConsulta(sQuery);
        getAD().desconectar();
        setAD(null);
        if (rst != null) {
            listRet=new ArrayList();
            for (int i = 0; i < rst.size(); i++) {
                Vector vRowTemp = (Vector)rst.elementAt(i); 
               
                oMov=new MovimientoCtaBan();
                oMov.setIdMovCtaBan(((Double) vRowTemp.elementAt(0)).intValue());
                oMov.getCuentaBanco().getBanco().setIdEmp(((Double) vRowTemp.elementAt(1)).intValue());
                oMov.getCuentaBanco().getBanco().setNombreCorto(((String) vRowTemp.elementAt(2)));        
                oMov.getCuentaBanco().setNumCtaBan((String) vRowTemp.elementAt(3)); 
                oMov.getCuentaBanco().setDescripcion((String) vRowTemp.elementAt(4));                         
                oMov.getConceptoMov().setConcepMovCta(((Double) vRowTemp.elementAt(5)).intValue());   
                oMov.getConceptoMov().setDescripcion((String) vRowTemp.elementAt(6)); 
                oMov.getConceptoMov().setTipoMovCta((String) vRowTemp.elementAt(7)); 
                oMov.getFormaPago().setCveFrmPago(((String) vRowTemp.elementAt(8)));
                oMov.getFormaPago().setDescripcion(((String) vRowTemp.elementAt(9)));
                oMov.setFechaMov(((Date) vRowTemp.elementAt(10)));
                oMov.setMonto(((Double) vRowTemp.elementAt(11)).floatValue());
                oMov.setNumDocto(((String) vRowTemp.elementAt(12)));
                oMov.setReferencia(((String) vRowTemp.elementAt(13)));
                listRet.add(oMov);
            }
        }
        return listRet;
    }  
    
    public String guardarMovimiento(MovimientoCtaBan oMov, List<FacturaCFI> listFact)throws Exception{
        Vector rst = null;
        String sQuery = "", sRet="";
        
        if (oMov==null){
             throw new Exception("Movimiento.guarda: error de programación, faltan datos");
        }else{
            if (oMov.getIdMovCtaBan()==0){
                sQuery="select * from guardamovimiento("+oMov.getCuentaBanco().getBanco().getIdEmp()+"::int2, '"+oMov.getCuentaBanco().getNumCtaBan()+
                        "', '"+oMov.getFechaMov()+"', "+oMov.getMonto()+", "+oMov.getConceptoMov().getConcepMovCta()+"::int2, '"+oMov.getNumDocto()+
                        "', '"+oMov.getFormaPago().getCveFrmPago()+"', '"+oMov.getReferencia()+"');\n";
                if (listFact!=null){
                    for (int i = 0; i < listFact.size(); i++) {
                        sQuery=sQuery+"select * from insertaDepCFDI('"+listFact.get(i).getNombreSerie() +"', "+listFact.get(i).getFolio() +"::int2);\n";
                    }
                }
            }else{
                sQuery="select * from guardamovimiento("+oMov.getCuentaBanco().getBanco().getIdEmp()+"::int2, '"+oMov.getCuentaBanco().getNumCtaBan()+
                        "', '"+oMov.getFechaMov()+"', "+oMov.getMonto()+", "+oMov.getConceptoMov().getConcepMovCta()+"::int2, '"+oMov.getNumDocto()+
                        "', '"+oMov.getFormaPago().getCveFrmPago()+"', '"+oMov.getReferencia()+"', "+oMov.getIdMovCtaBan()+");\n";
                if (listFact!=null){
                    for (int i = 0; i < listFact.size(); i++) {
                        sQuery=sQuery+"select * from insertaDepCFDI('"+listFact.get(i).getNombreSerie() +"', "+listFact.get(i).getFolio() +"::int2, "+oMov.getIdMovCtaBan()+");\n";
                    }
                }
            }
            if (getAD() == null){
                setAD(new AccesoDatos());
                getAD().conectar();
                rst = getAD().ejecutarConsulta(sQuery);
                getAD().desconectar();
                setAD(null);
            }else{
                rst = getAD().ejecutarConsulta(sQuery);
            }
        }
        if ("Se ha registrado el movimiento satisfactoriamente".equals(rst.get(0).toString().substring(1, rst.get(0).toString().length()-1)))
            sRet="OK";
        else
            if ("Multiple ResultSets were returned by the query".equals(rst.get(0).toString().substring(1, rst.get(0).toString().length()-1)))
                sRet="OK";
            else
                sRet="ERROR";
        return sRet;
    }
    
    public String eliminarMovimiento(int nIdMov)throws Exception{
        Vector rst = null;
        String sQuery = "";
        
        if (nIdMov==0){
             throw new Exception("Movimiento.elimina: error de programación, faltan datos");
        }else{
            sQuery="select * from eliminaMovimiento("+nIdMov+")";
            if (getAD() == null){
                setAD(new AccesoDatos());
                getAD().conectar();
                rst = getAD().ejecutarConsulta(sQuery);
                getAD().desconectar();
                setAD(null);
            }else{
                rst = getAD().ejecutarConsulta(sQuery);
            }
        }
        return "" + rst.get(0).toString().substring(1, rst.get(0).toString().length() - 1);
    }
    
    public List<MovimientoCtaBan> buscaCedulaCuenta(int nIdEmp, String sNumCuenta, String sCondicion, Date dFecha)throws Exception{
        System.out.println("adios :D");
        MovimientoCtaBan oMov=null;
        List<MovimientoCtaBan> listRet=null;
        Vector rst = null;
        String sQuery = "";
        
        sQuery = "select * from buscaCedulaCuenta("+nIdEmp+"::int2,'"+sNumCuenta+"')";
        if (!sCondicion.equals(""))
            sQuery=sQuery+" where "+sCondicion;
        if (getAD() == null){
            setAD(new AccesoDatos());
            getAD().conectar();
        }
        rst = getAD().ejecutarConsulta(sQuery);
        getAD().desconectar();
        setAD(null);
        if (rst != null) {
            listRet=new ArrayList();
            oMov=new MovimientoCtaBan();
            oMov.setIdMovCtaBan(0);
            oMov.setFechaMov(null);
            oMov.getConceptoMov().setDescripcion("SALDO INICIAL"); 
            oMov.setReferencia("");
            oMov.getConceptoMov().setTipoMovCta(""); 
            oMov.setMonto(calculaSaldoInicial(nIdEmp, sNumCuenta, dFecha));
            oMov.getCuentaBanco().setNumCtaBan(sNumCuenta);
            oMov.getCuentaBanco().getBanco().setIdEmp(nIdEmp);

            listRet.add(oMov);
            for (int i = 0; i < rst.size(); i++) {
                Vector vRowTemp = (Vector)rst.elementAt(i); 
               
                oMov=new MovimientoCtaBan();
                oMov.setIdMovCtaBan(((Double) vRowTemp.elementAt(0)).intValue());
                oMov.setFechaMov(((Date) vRowTemp.elementAt(1)));
                oMov.getConceptoMov().setDescripcion((String) vRowTemp.elementAt(2)); 
                oMov.setReferencia(((String) vRowTemp.elementAt(3)));
                oMov.getConceptoMov().setTipoMovCta((String) vRowTemp.elementAt(4)); 
                oMov.setMonto(((Double) vRowTemp.elementAt(5)).floatValue());
                oMov.getCuentaBanco().setNumCtaBan(sNumCuenta);
                oMov.getCuentaBanco().getBanco().setIdEmp(nIdEmp);
                oMov.setNumDocto((String) vRowTemp.elementAt(6));
                oMov.setGasto(new Gasto());
                oMov.getGasto().setProv(new Proveedor());
                oMov.getGasto().getProv().setNombreRazSoc((String) vRowTemp.elementAt(7));
                
                listRet.add(oMov);
            }
            oMov=new MovimientoCtaBan();
            oMov.setIdMovCtaBan(-1);
            oMov.setFechaMov(null);
            oMov.getConceptoMov().setDescripcion("SALDO FINAL"); 
            oMov.setReferencia("");
            oMov.getConceptoMov().setTipoMovCta(""); 
            oMov.setMonto(0.0f);
            oMov.getCuentaBanco().setNumCtaBan(sNumCuenta);
            oMov.getCuentaBanco().getBanco().setIdEmp(nIdEmp);
            listRet.add(oMov);
        }
        return listRet;
    }
    
    public float calculaSaldoInicial(int nIdEmp, String sNumCuenta, Date dFecha)throws Exception{
        float nRet=0.0f;
        String sQuery = "";
        Vector rst = null;
        
        if (dFecha==null)
            sQuery = "select * from calculaSaldoInicial("+nIdEmp+"::int2, '"+sNumCuenta+"',"+dFecha+")";
        else
            sQuery = "select * from calculaSaldoInicial("+nIdEmp+"::int2, '"+sNumCuenta+"','"+dFecha+"')";
        
        if (getAD() == null){
            setAD(new AccesoDatos());
            getAD().conectar();
        }
        rst = getAD().ejecutarConsulta(sQuery);
        getAD().desconectar();
        setAD(null);
        if (rst != null) {
            Vector vRowTemp = (Vector)rst.elementAt(0); 
            nRet=(((Double) vRowTemp.elementAt(0)).floatValue());   
        }
        return nRet;
    }
    

    public int getIdMovCtaBan() {
        return nIdMovCtaBan;
    }

    public void setIdMovCtaBan(int nIdMovCtaBan) {
        this.nIdMovCtaBan = nIdMovCtaBan;
    }

    public CuentaBanco getCuentaBanco() {
        return oCuentaBanco;
    }

    public void setCuentaBanco(CuentaBanco oCuentaBanco) {
        this.oCuentaBanco = oCuentaBanco;
    }

    public ConceptoMovCtaBan getConceptoMov() {
        return oConceptoMov;
    }

    public void setConceptoMov(ConceptoMovCtaBan oConceptoMov) {
        this.oConceptoMov = oConceptoMov;
    }

    public FormaPago getFormaPago() {
        return oFormaPago;
    }

    public void setFormaPago(FormaPago oFormaPago) {
        this.oFormaPago = oFormaPago;
    }

    public Date getFechaMov() {
        return dFechaMov;
    }

    public void setFechaMov(Date dFechaMov) {
        this.dFechaMov = dFechaMov;
    }

    public Float getMonto() {
        return nMonto;
    }

    public void setMonto(Float nMonto) {
        this.nMonto = nMonto;
    }

    public String getNumDocto() {
        return sNumDocto;
    }

    public void setNumDocto(String sNumDocto) {
        this.sNumDocto = sNumDocto;
    }

    public AccesoDatos getAD() {
        return oAD;
    }

    public void setAD(AccesoDatos oAD) {
        this.oAD = oAD;
    }
    
    public String getReferenciaF(){
        return sReferenciaF;
    }

    public String getReferencia() {
        return sReferencia;
    }

    public void setReferencia(String sReferencia) {
        this.sReferencia = sReferencia;
    }

    public Gasto getGasto() {
        return oGasto;
    }

    public void setGasto(Gasto oGasto) {
        this.oGasto = oGasto;
    }

    @Override
    public String toString() {
        return "MovimientoCtaBan{" + "nIdMovCtaBan=" + nIdMovCtaBan + ", oCuentaBanco=" + oCuentaBanco + ", oConceptoMov=" + oConceptoMov + ", oFormaPago=" + oFormaPago + ", dFechaMov=" + dFechaMov + ", nMonto=" + nMonto + ", sNumDocto=" + sNumDocto + ", sReferenciaF=" + sReferenciaF + ", sReferencia=" + sReferencia + ", oGasto=" + oGasto + ", oAD=" + oAD + '}';
    }
    
    
    
}
