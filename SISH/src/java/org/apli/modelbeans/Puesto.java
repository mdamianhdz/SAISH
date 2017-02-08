package org.apli.modelbeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import org.apli.AD.AccesoDatos;

/**
 * Catálogo de puestos o categor�as de trabajo dentro del hospital. Incluye una
 * opción "externo" que aplica a los m�dicos (o algunos t�cnicos radi�logos) que
 * no son parte del hospital pero que realizan alguna labor espor�dica en �l
 * @author BAOZ
 * @version 1.0
 * @created 07-abr-2014 10:21:40 a.m.
 */
public class Puesto implements Serializable {

    @Override
    public String toString() {
        return "Puesto{" + "nSalarioMensual=" + nSalarioMensual + ", sCve=" + sCve + ", sDescrip=" + sDescrip + ", oAD=" + oAD + '}';
    }

    
    
	/**
	 * Monto del salario mensual en pesos
	 */
	public float nSalarioMensual;
	/**
	 * Clave del puesto
	 */
	public String sCve;
	/**
	 * Descripci�n del puesto
	 */
	public String sDescrip;
        
        public AccesoDatos oAD;

	public Puesto(){

	}        
        public List<Puesto> buscaTodosPuestos()throws Exception{
        List<Puesto> listRet=null;
        Puesto oP;
        Vector rst;
        
        String sQuery="select * from buscatodospuestos();";
        
        if (getAD()==null){
            setAD(new AccesoDatos());
            getAD().conectar();
        }
        rst=getAD().ejecutarConsulta(sQuery);
        getAD().desconectar();
        setAD(null);
        
        if(rst!=null){
            listRet=new ArrayList();
            for (int i = 0; i < rst.size(); i++) {
                Vector vRowTemp=(Vector) rst.get(i);
                oP=new Puesto();
                oP.setCve((String)vRowTemp.get(0));
                oP.setDescrip((String)vRowTemp.get(1));
                listRet.add(oP);
            }
        }
        return listRet;
    }

        public String buscaDescripcionPuesto(String sClavePuesto) throws Exception{
            Puesto oPuesto= new Puesto();
            String desPuesto="";
            
            Vector rst=null;
            String sQuery="";
            
            sQuery="select * from buscapuesto('"+sClavePuesto+"')";
            
            if(getAD()==null){
                setAD(new AccesoDatos());
                getAD().conectar();
                rst=getAD().ejecutarConsulta(sQuery);
                getAD().desconectar();
                setAD(null);
            }else{
                rst=getAD().ejecutarConsulta(sQuery);
            }
            if(rst !=null){
                for(int i=0; i<rst.size(); i++){
                    oPuesto= new Puesto();
                    Vector vRowTemp= (Vector) rst.elementAt(i);
         
                    oPuesto.setDescrip((String)vRowTemp.elementAt(1));
                    desPuesto=oPuesto.getDescrip();
                }
            }
            return desPuesto;
        }

    /**
     * @return the oAD
     */
    public AccesoDatos getAD() {
        return oAD;
    }

    /**
     * @param oAD the oAD to set
     */
    public void setAD(AccesoDatos oAD) {
        this.oAD = oAD;
    }

    /**
     * @return the nSalarioMensual
     */
    public float getSalarioMensual() {
        return nSalarioMensual;
    }

    /**
     * @param nSalarioMensual the nSalarioMensual to set
     */
    public void setSalarioMensual(float nSalarioMensual) {
        this.nSalarioMensual = nSalarioMensual;
    }

    /**
     * @return the sCve
     */
    public String getCve() {
        return sCve;
    }

    /**
     * @param sCve the sCve to set
     */
    public void setCve(String sCve) {
        this.sCve = sCve;
    }

    /**
     * @return the sDescrip
     */
    public String getDescrip() {
        return sDescrip;
    }

    /**
     * @param sDescrip the sDescrip to set
     */
    public void setDescrip(String sDescrip) {
        this.sDescrip = sDescrip;
    }
}