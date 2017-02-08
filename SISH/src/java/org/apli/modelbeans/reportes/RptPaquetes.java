package org.apli.modelbeans.reportes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import org.apli.AD.AccesoDatos;
import org.apli.modelbeans.Paciente;
import org.apli.modelbeans.PaqueteContratado;
import org.apli.modelbeans.Paquete;
import org.apli.modelbeans.PersonalHospitalario;
import org.apli.modelbeans.TipoPaquete;

/**
 * Muestra la situacion de paquetes contratados,activos,finalizados y cancelados
 * @author AIMEE R
 */
public class RptPaquetes implements Serializable{
    
    private String sClaveO="";
    private String sDescO="";
    private Float nMontoPendientePagar;
    
    private PaqueteContratado sSituacion;
    private PaqueteContratado dRegistro;
    private TipoPaquete sTipoPq;
    private Paciente sNomPac;
    private Paquete nCostoPq;
    private PaqueteContratado nAnticipoPq;
    private PersonalHospitalario sPersP;
    private PersonalHospitalario sPersV;
    private PaqueteContratado sRazonCan;
    private AccesoDatos oAD;
    
    public RptPaquetes(){

    }
    
    public List<RptPaquetes> buscaPersonalContrata() throws Exception{
        RptPaquetes oRptPq= null;
        List<RptPaquetes> listRet = null;
        Vector rst = null;
        String sQuery = "";

        sQuery = "select distinct ph.nfoliopers,(case " +
                "when ph.sappaterno is not null then ph.sappaterno else '' end) ||' '|| " +
                "(case " +
                "when ph.sapmaterno is not null then ph.sapmaterno else '' end) ||' '|| " +
                "ph.snombre as personalcontrato " +
                "FROM personalhospitalario ph, paquetecontratado pc " +
                "where pc.nfoliopersvende=ph.nfoliopers";
        if(oAD==null){
            oAD=new AccesoDatos();
            oAD.conectar();
            rst=oAD.ejecutarConsulta(sQuery);
            oAD.desconectar();
            oAD=null;
        }else{
            rst=oAD.ejecutarConsulta(sQuery);
        }
        if (rst != null) {
            listRet = new ArrayList();
            for (int i = 0; i < rst.size(); i++) {
                Vector vRowTemp = (Vector) rst.elementAt(i);
                oRptPq= new RptPaquetes();
                //this.oPersV=new PersonalHospitalario();
                //oRptPq.getPaqueteContratadoPc().setPersonalVende(this.oPersV);
                //System.out.println("blabla"+((String)vRowTemp.elementAt(1)));
                //oRptPq.getPersVPc().setNombre((String) vRowTemp.elementAt(1));
                oRptPq.setDescLO((String)vRowTemp.elementAt(1));
                listRet.add(oRptPq);
            }
        } 
        return listRet;
    }

    public void setAD(AccesoDatos oAD) {
        this.oAD = oAD;
    }
    public AccesoDatos getAD() {
        return oAD;
    }
    
    public RptPaquetes[] buscaPaquetesContratados(Date fechaini,Date fechafin,String sentencia) throws Exception{
      RptPaquetes[] arrRet=null;
      RptPaquetes oRptPq=null;
      Vector rst=null;
      Vector<RptPaquetes> vObj=null;
      String sQuery="";
      int i=0,nTam=0;
      sQuery="SELECT * FROM buscaSituacionTodosPaquetesContratados('"+fechaini+"'::date,'"+fechafin+"'::date) WHERE "+sentencia+" (situacion='0' or situacion='1')";
      System.out.println(sQuery);
      
      //Si objeto para acceso a datos es nulo, entonces vuelve a conectar y desconectar, de lo contrario, sigue conectado y solo hace consulta
      if(getAD()==null){
          setAD(new AccesoDatos());
          getAD().conectar();
          rst=getAD().ejecutarConsulta(sQuery);
          getAD().desconectar();
          setAD(null);
      }
      else{
          rst=getAD().ejecutarConsulta(sQuery);
      }
      
      if(rst!=null){
          vObj=new Vector<RptPaquetes>();
          for(i=0;i<rst.size();i++){
              oRptPq=new RptPaquetes();
              Vector vRowTemp=(Vector)rst.elementAt(i);
              //--------------
              PaqueteContratado oTPqcs=new PaqueteContratado();
              if(((String)vRowTemp.elementAt(1)).equals("0")){
                  oTPqcs.setSituacionPaq("Contratado");
              }
              else if(((String)vRowTemp.elementAt(1)).equals("1")){
                  oTPqcs.setSituacionPaq("En Proceso");
              }
              oRptPq.setSituacion(oTPqcs);
              //---------------
              PaqueteContratado oTPqr=new PaqueteContratado();
              oTPqr.setRegistro((Date)vRowTemp.elementAt(2));
              oRptPq.setRegistro(oTPqr);
              //--------------
              //Tipos de Paquetes
              TipoPaquete oTTPq=new TipoPaquete();
              if(((String)vRowTemp.elementAt(3)).equals("0")){
                  oTTPq.setDescrip("Pediátrico");
              }
              else if(((String)vRowTemp.elementAt(3)).equals("1")){
                  oTTPq.setDescrip("Maternidad");
              }
              else if(((String)vRowTemp.elementAt(3)).equals("2")){
                  oTTPq.setDescrip("Quirúrgico");
              }
              else if(((String)vRowTemp.elementAt(3)).equals("3")){
                  oTTPq.setDescrip("Personalizado");
              }
              oRptPq.setTipoPq(oTTPq); 
              //----------------
              Paciente oTPac=new Paciente();
              oTPac.setNombre((String)vRowTemp.elementAt(4));
              oRptPq.setNombrePac(oTPac); 
              //----------------
              Paquete oTPaq=new Paquete();
              oTPaq.setCosto(((Double)vRowTemp.elementAt(5)).floatValue());
              oRptPq.setCosto(oTPaq);
              //----------------
              PaqueteContratado oTPqca=new PaqueteContratado();
              oTPqca.setAnticipo(((Double)vRowTemp.elementAt(6)).floatValue());
              oRptPq.setAnticipo(oTPqca);
              //----------------
              oRptPq.setMontoPendientePagar(((Double)vRowTemp.elementAt(7)).floatValue());
              //----------------
              PersonalHospitalario oTPersP=new PersonalHospitalario();
              oTPersP.setNombre((String)vRowTemp.elementAt(8));
              oRptPq.setPersP(oTPersP);
              //----------------
              PersonalHospitalario oTPersV=new PersonalHospitalario();
              oTPersV.setNombre((String)vRowTemp.elementAt(9));
              oRptPq.setPersV(oTPersV);
              //----------------
              
              vObj.add(oRptPq);
          }
          nTam=vObj.size();
          arrRet=new RptPaquetes[nTam];
          for(i=0;i<nTam;i++){
              arrRet[i]=vObj.elementAt(i);
          }
      }
      return arrRet;
    }
    
    public RptPaquetes[] buscaPaquetesActivados(Date fechaini,Date fechafin,String sentencia) throws Exception{
      RptPaquetes[] arrRet=null;
      RptPaquetes oRptPq=null;
      Vector rst=null;
      Vector<RptPaquetes> vObj=null;
      String sQuery="";
      int i=0,nTam=0;
      sQuery="SELECT * FROM buscaSituacionTodosPaquetesContratados('"+fechaini+"'::date,'"+fechafin+"'::date) WHERE "+sentencia+" situacion='2'";
      System.out.println(sQuery);
      
      //Si objeto para acceso a datos es nulo, entonces vuelve a conectar y desconectar, de lo contrario, sigue conectado y solo hace consulta
      if(getAD()==null){
          setAD(new AccesoDatos());
          getAD().conectar();
          rst=getAD().ejecutarConsulta(sQuery);
          getAD().desconectar();
          setAD(null);
      }
      else{
          rst=getAD().ejecutarConsulta(sQuery);
      }
      
      if(rst!=null){
          vObj=new Vector<RptPaquetes>();
          for(i=0;i<rst.size();i++){
              oRptPq=new RptPaquetes();
              Vector vRowTemp=(Vector)rst.elementAt(i);
              //---------------
              PaqueteContratado oTPqr=new PaqueteContratado();
              oTPqr.setRegistro((Date)vRowTemp.elementAt(2));
              oRptPq.setRegistro(oTPqr);
              //--------------
              //Tipos de Paquetes
              TipoPaquete oTTPq=new TipoPaquete();
              if(((String)vRowTemp.elementAt(3)).equals("0")){
                  oTTPq.setDescrip("Pediátrico");
              }
              else if(((String)vRowTemp.elementAt(3)).equals("1")){
                  oTTPq.setDescrip("Maternidad");
              }
              else if(((String)vRowTemp.elementAt(3)).equals("2")){
                  oTTPq.setDescrip("Quirúrgico");
              }
              else if(((String)vRowTemp.elementAt(3)).equals("3")){
                  oTTPq.setDescrip("Personalizado");
              }
              oRptPq.setTipoPq(oTTPq); 
              //----------------
              Paciente oTPac=new Paciente();
              oTPac.setNombre((String)vRowTemp.elementAt(4));
              oRptPq.setNombrePac(oTPac); 
              //----------------
              Paquete oTPaq=new Paquete();
              oTPaq.setCosto(((Double)vRowTemp.elementAt(5)).floatValue());
              oRptPq.setCosto(oTPaq);
              //----------------
              PaqueteContratado oTPqca=new PaqueteContratado();
              oTPqca.setAnticipo(((Double)vRowTemp.elementAt(6)).floatValue());
              oRptPq.setAnticipo(oTPqca);
              //----------------
              oRptPq.setMontoPendientePagar(((Double)vRowTemp.elementAt(7)).floatValue());
              //----------------
              PersonalHospitalario oTPersP=new PersonalHospitalario();
              oTPersP.setNombre((String)vRowTemp.elementAt(8));
              oRptPq.setPersP(oTPersP);
              //----------------
              PersonalHospitalario oTPersV=new PersonalHospitalario();
              oTPersV.setNombre((String)vRowTemp.elementAt(9));
              oRptPq.setPersV(oTPersV);
              //----------------
              
              vObj.add(oRptPq);
          }
          nTam=vObj.size();
          arrRet=new RptPaquetes[nTam];
          for(i=0;i<nTam;i++){
              arrRet[i]=vObj.elementAt(i);
          }
      }
      return arrRet;
    }
    
    public RptPaquetes[] buscaPaquetesFinalizados(Date fechaini,Date fechafin,String sentencia) throws Exception{
      RptPaquetes[] arrRet=null;
      RptPaquetes oRptPq=null;
      Vector rst=null;
      Vector<RptPaquetes> vObj=null;
      String sQuery="";
      int i=0,nTam=0;
      sQuery="SELECT * FROM buscaSituacionTodosPaquetesContratados('"+fechaini+"'::date,'"+fechafin+"'::date) WHERE "+sentencia+" situacion='3'";
      System.out.println(sQuery);
      
      //Si objeto para acceso a datos es nulo, entonces vuelve a conectar y desconectar, de lo contrario, sigue conectado y solo hace consulta
      if(getAD()==null){
          setAD(new AccesoDatos());
          getAD().conectar();
          rst=getAD().ejecutarConsulta(sQuery);
          getAD().desconectar();
          setAD(null);
      }
      else{
          rst=getAD().ejecutarConsulta(sQuery);
      }
      
      if(rst!=null){
          vObj=new Vector<RptPaquetes>();
          for(i=0;i<rst.size();i++){
              oRptPq=new RptPaquetes();
              Vector vRowTemp=(Vector)rst.elementAt(i);
              //--------------
              PaqueteContratado oTPqcs=new PaqueteContratado();
              if(((String)vRowTemp.elementAt(1)).equals("0")){
                  oTPqcs.setSituacionPaq("Contratado");
              }
              else if(((String)vRowTemp.elementAt(1)).equals("1")){
                  oTPqcs.setSituacionPaq("En Proceso");
              }
              oRptPq.setSituacion(oTPqcs);
              //---------------
              PaqueteContratado oTPqr=new PaqueteContratado();
              oTPqr.setRegistro((Date)vRowTemp.elementAt(2));
              oRptPq.setRegistro(oTPqr);
              //--------------
              //Tipos de Paquetes
              TipoPaquete oTTPq=new TipoPaquete();
              if(((String)vRowTemp.elementAt(3)).equals("0")){
                  oTTPq.setDescrip("Pediátrico");
              }
              else if(((String)vRowTemp.elementAt(3)).equals("1")){
                  oTTPq.setDescrip("Maternidad");
              }
              else if(((String)vRowTemp.elementAt(3)).equals("2")){
                  oTTPq.setDescrip("Quirúrgico");
              }
              else if(((String)vRowTemp.elementAt(3)).equals("3")){
                  oTTPq.setDescrip("Personalizado");
              }
              oRptPq.setTipoPq(oTTPq); 
              //----------------
              Paciente oTPac=new Paciente();
              oTPac.setNombre((String)vRowTemp.elementAt(4));
              oRptPq.setNombrePac(oTPac); 
              //----------------
              Paquete oTPaq=new Paquete();
              oTPaq.setCosto(((Double)vRowTemp.elementAt(5)).floatValue());
              oRptPq.setCosto(oTPaq);
              //----------------
              PaqueteContratado oTPqca=new PaqueteContratado();
              oTPqca.setAnticipo(((Double)vRowTemp.elementAt(6)).floatValue());
              oRptPq.setAnticipo(oTPqca);
              //----------------
              oRptPq.setMontoPendientePagar(((Double)vRowTemp.elementAt(7)).floatValue());
              //----------------
              PersonalHospitalario oTPersP=new PersonalHospitalario();
              oTPersP.setNombre((String)vRowTemp.elementAt(8));
              oRptPq.setPersP(oTPersP);
              //----------------
              PersonalHospitalario oTPersV=new PersonalHospitalario();
              oTPersV.setNombre((String)vRowTemp.elementAt(9));
              oRptPq.setPersV(oTPersV);
              //----------------
              
              vObj.add(oRptPq);
          }
          nTam=vObj.size();
          arrRet=new RptPaquetes[nTam];
          for(i=0;i<nTam;i++){
              arrRet[i]=vObj.elementAt(i);
          }
      }
      return arrRet;
    }
    
    public RptPaquetes[] buscaPaquetesCancelados(Date fechaini,Date fechafin,String sentencia) throws Exception{
      RptPaquetes[] arrRet=null;
      RptPaquetes oRptPq=null;
      Vector rst=null;
      Vector<RptPaquetes> vObj=null;
      String sQuery="";
      int i=0,nTam=0;
      sQuery="SELECT * FROM buscaSituacionTodosPaquetesContratados('"+fechaini+"'::date,'"+fechafin+"'::date) WHERE "+sentencia+" situacion='4'";
      System.out.println(sQuery);
      
      //Si objeto para acceso a datos es nulo, entonces vuelve a conectar y desconectar, de lo contrario, sigue conectado y solo hace consulta
      if(getAD()==null){
          setAD(new AccesoDatos());
          getAD().conectar();
          rst=getAD().ejecutarConsulta(sQuery);
          getAD().desconectar();
          setAD(null);
      }
      else{
          rst=getAD().ejecutarConsulta(sQuery);
      }
      
      if(rst!=null){
          vObj=new Vector<RptPaquetes>();
          for(i=0;i<rst.size();i++){
              oRptPq=new RptPaquetes();
              Vector vRowTemp=(Vector)rst.elementAt(i);
              //--------------
              PaqueteContratado oTPqcs=new PaqueteContratado();
              if(((String)vRowTemp.elementAt(1)).equals("0")){
                  oTPqcs.setSituacionPaq("Contratado");
              }
              else if(((String)vRowTemp.elementAt(1)).equals("1")){
                  oTPqcs.setSituacionPaq("En Proceso");
              }
              oRptPq.setSituacion(oTPqcs);
              //---------------
              PaqueteContratado oTPqr=new PaqueteContratado();
              oTPqr.setRegistro((Date)vRowTemp.elementAt(2));
              oRptPq.setRegistro(oTPqr);
              //--------------
              //Tipos de Paquetes
              TipoPaquete oTTPq=new TipoPaquete();
              if(((String)vRowTemp.elementAt(3)).equals("0")){
                  oTTPq.setDescrip("Pediátrico");
              }
              else if(((String)vRowTemp.elementAt(3)).equals("1")){
                  oTTPq.setDescrip("Maternidad");
              }
              else if(((String)vRowTemp.elementAt(3)).equals("2")){
                  oTTPq.setDescrip("Quirúrgico");
              }
              else if(((String)vRowTemp.elementAt(3)).equals("3")){
                  oTTPq.setDescrip("Personalizado");
              }
              oRptPq.setTipoPq(oTTPq); 
              //----------------
              Paciente oTPac=new Paciente();
              oTPac.setNombre((String)vRowTemp.elementAt(4));
              oRptPq.setNombrePac(oTPac); 
              //----------------
              Paquete oTPaq=new Paquete();
              oTPaq.setCosto(((Double)vRowTemp.elementAt(5)).floatValue());
              oRptPq.setCosto(oTPaq);
              //----------------
              PaqueteContratado oTPqca=new PaqueteContratado();
              oTPqca.setAnticipo(((Double)vRowTemp.elementAt(6)).floatValue());
              oRptPq.setAnticipo(oTPqca);
              //----------------
              oRptPq.setMontoPendientePagar(((Double)vRowTemp.elementAt(7)).floatValue());
              //----------------
              PersonalHospitalario oTPersP=new PersonalHospitalario();
              oTPersP.setNombre((String)vRowTemp.elementAt(8));
              oRptPq.setPersP(oTPersP);
              //----------------
              PersonalHospitalario oTPersV=new PersonalHospitalario();
              oTPersV.setNombre((String)vRowTemp.elementAt(9));
              oRptPq.setPersV(oTPersV);
              //----------------
              PaqueteContratado oTPqcc=new PaqueteContratado();
              oTPqcc.setRazonCanc((String)vRowTemp.elementAt(10));
              oRptPq.setRazon(oTPqcc);
              
              vObj.add(oRptPq);
          }
          nTam=vObj.size();
          arrRet=new RptPaquetes[nTam];
          for(i=0;i<nTam;i++){
              arrRet[i]=vObj.elementAt(i);
          }
      }
      return arrRet;
    }
    
    // set y gets
    public String getClaveLO(){
        return sClaveO;
    }
    public void setClaveLO(String sClaveO){
        this.sClaveO=sClaveO;
    }
    
    public String getDescLO(){
        return sDescO;
    }
    public void setDescLO(String sDescO){
        this.sDescO=sDescO;
    }
    
    public void setMontoPendientePagar(Float nMonto){
        this.nMontoPendientePagar=nMonto;
    }
    public Float getMontoPendientePagar(){
        return nMontoPendientePagar;
    }
    //situacion
    public PaqueteContratado getSituacion(){
        return sSituacion;
    }
    public void setSituacion(PaqueteContratado situacion){
        this.sSituacion=situacion;
    }
    
    //registro
    public PaqueteContratado getRegistro(){
        return dRegistro;
    }
    public void setRegistro(PaqueteContratado registro){
        this.dRegistro=registro;
    }
    //tipopaquete
    public TipoPaquete getTipoPq(){
        return sTipoPq;
    }
    public void setTipoPq(TipoPaquete tipo){
        this.sTipoPq=tipo;
    }
    //nombrepaciente
    public Paciente getNombrePac(){
        return sNomPac;
    }
    public void setNombrePac(Paciente nombrePac){
        this.sNomPac=nombrePac;
    }
    //costo paquete
    public Paquete getCosto(){
        return nCostoPq;
    }
    public void setCosto(Paquete costo){
        this.nCostoPq=costo;
    }
    //anticipo
    public PaqueteContratado getAnticipo(){
        return nAnticipoPq;
    }
    public void setAnticipo(PaqueteContratado anticipo){
        this.nAnticipoPq=anticipo;
    }
    //Personal promo
    public PersonalHospitalario getPersP(){
        return sPersP;
    }
    public void setPersP(PersonalHospitalario oPersP){
        this.sPersP=oPersP;
    }
    //Personal vende
    public PersonalHospitalario getPersV(){
        return sPersV;
    }
    public void setPersV(PersonalHospitalario oPersV){
        this.sPersV=oPersV;
    }
    //razon canc
    public PaqueteContratado getRazon(){
        return sRazonCan;
    }
    public void setRazon(PaqueteContratado razon){
        this.sRazonCan=razon;
    }    
}
