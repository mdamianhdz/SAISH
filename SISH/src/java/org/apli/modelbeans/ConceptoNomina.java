package org.apli.modelbeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import org.apli.AD.AccesoDatos;

/**
 *
 * @author Lily_LnBnd
 */
public class ConceptoNomina implements Serializable{
    private int nCveConcepNom;
    private char cTipoConNom; //P=Percepción; D=Deducción
    private String sDescripcion;
    private AccesoDatos oAD;
    
    public ConceptoNomina(){    
    }
    
    public List<ConceptoNomina> buscaPercepciones() throws Exception{
        List<ConceptoNomina> listRet=new ArrayList();
        ConceptoNomina oCN;
        
        Vector rst = null;
            String sQuery = "";

            sQuery="select * from buscapercepciones()";     
            if (getAD() == null){
                    setAD(new AccesoDatos());
                    getAD().conectar();
                    rst = getAD().ejecutarConsulta(sQuery);
                    getAD().desconectar();
                    setAD(null);
            }else{
                    rst = getAD().ejecutarConsulta(sQuery);
            }
               
            if (rst != null) {
                listRet = new ArrayList();
                
                for (int i = 0; i < rst.size(); i++) {
                    Vector vRowTemp = (Vector)rst.elementAt(i);
                    oCN=new ConceptoNomina();
                    oCN.setCveConcepNom(((Double)vRowTemp.elementAt(0)).intValue());
                    oCN.setDescripcion((String)vRowTemp.elementAt(1));
                    oCN.setTipoConNom('P');
                    listRet.add(oCN);   
                }
            }
        return listRet;
    }
    
    public List<ConceptoNomina> buscaDeducciones() throws Exception{
        List<ConceptoNomina> listRet=new ArrayList();
        ConceptoNomina oCN;
        
        Vector rst = null;
            String sQuery = "";

            sQuery="select * from buscadeducciones()";     
            if (getAD() == null){
                    setAD(new AccesoDatos());
                    getAD().conectar();
                    rst = getAD().ejecutarConsulta(sQuery);
                    getAD().desconectar();
                    setAD(null);
            }else{
                    rst = getAD().ejecutarConsulta(sQuery);
            }
               
            if (rst != null) {
                listRet = new ArrayList();
                
                for (int i = 0; i < rst.size(); i++) {
                    Vector vRowTemp = (Vector)rst.elementAt(i);
                    oCN=new ConceptoNomina();
                    oCN.setCveConcepNom(((Double)vRowTemp.elementAt(0)).intValue());
                    oCN.setDescripcion((String)vRowTemp.elementAt(1));
                    oCN.setTipoConNom('D');
                    listRet.add(oCN);   
                }
            }
        return listRet;
    }

    public String agregarConceptoNomina(ConceptoNomina oConceptoNomina) throws Exception{
        Vector rst=null;
        String sQuery="";
        
        if(oConceptoNomina==null){
            throw new Exception("ConceptoNomina.guarda: Erro de Programación, Faltan Datos");
        }
        else{
            sQuery="select * from insertarconceptonomina('"+oConceptoNomina.getCveConcepNom()+"','"+oConceptoNomina.getTipoConNom()+"','"+oConceptoNomina.getDescripcion()+"')";
            
            if(getAD()==null){
                setAD(new AccesoDatos());
                getAD().conectar();
                rst=getAD().ejecutarConsulta(sQuery);
                getAD().desconectar();
                setAD(null);
            }
            System.out.println(rst.get(0));
        }
        return ""+rst.get(0).toString().substring(1,rst.get(0).toString().length()-1);
    }
    
    public String modificarConceptoNomina(ConceptoNomina oConceptoNomina) throws Exception{
        Vector rst=null;
        String sQuery="";
        
        if(oConceptoNomina==null){
            throw new Exception("ConceptoEgreso.modifica: Error de Programación, Faltas Datos");
        }
        else{
            sQuery="select * from modificarconceptonomina('"+oConceptoNomina.getCveConcepNom()+"','"+oConceptoNomina.getTipoConNom()+"','"+oConceptoNomina.getDescripcion()+"')";
            if(getAD()==null){
                setAD(new AccesoDatos());
                getAD().conectar();
                rst=getAD().ejecutarConsulta(sQuery);
                getAD().desconectar();
                setAD(null);
            }
        }
        return rst.get(0).toString().substring(1,rst.get(0).toString().length()-1);
    }
    
    public String eliminarConceptoNomina(ConceptoNomina oConceptoNomina)throws Exception{
        Vector rst=null;
        String sQuery="";
        
        if(oConceptoNomina==null){
            throw new Exception("ConceptoNomina.elimina: Error de Programación, Faltan Datos");
        }
        else{
            sQuery="select * from eliminarconceptonomina('"+oConceptoNomina.getCveConcepNom()+"','"+oConceptoNomina.getTipoConNom()+"','"+oConceptoNomina.getDescripcion()+"')";
            
            if(getAD()==null){
                setAD(new AccesoDatos());
                getAD().conectar();
                rst=getAD().ejecutarConsulta(sQuery);
                getAD().desconectar();
                setAD(null);
            }
        }
        return rst.get(0).toString().substring(1,rst.get(0).toString().length()-1);
    }
    
    public int getCveConcepNom() {
        return nCveConcepNom;
    }

    public void setCveConcepNom(int nCveConcepNom) {
        this.nCveConcepNom = nCveConcepNom;
    }

    public char getTipoConNom() {
        return cTipoConNom;
    }

    public void setTipoConNom(char cTipoConNom) {
        this.cTipoConNom = cTipoConNom;
    }

    public String getDescripcion() {
        return sDescripcion;
    }

    public void setDescripcion(String sDescripcion) {
        this.sDescripcion = sDescripcion;
    }
    
    public AccesoDatos getAD(){
        return oAD;
    }
    
    public void setAD(AccesoDatos oAD){
        this.oAD=oAD;
    }
}
