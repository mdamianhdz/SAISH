package org.apli.modelbeans;

/**
 * Clase para encapsular las constantes de tipo de empresa
 * @author BAOZ
 */
public class TipoEmpresa {
    private int nTipo=0;
    private String sDescripcion="";
    private TipoEmpresa[] tipos;
    
    public TipoEmpresa(){
        tipos = new TipoEmpresa[5];
        tipos[0] = new TipoEmpresa(1, "Banco");
        tipos[1] = new TipoEmpresa(2, "Aseguradora");
        tipos[2] = new TipoEmpresa(3, "Empresa");
        tipos[3] = new TipoEmpresa(4, "Municipio");
        tipos[4] = new TipoEmpresa(5, "Otro");
    }
    
    public TipoEmpresa(int ncve, String sNom){
        this.nTipo = ncve;
        this.sDescripcion = sNom;
    }
    
    public TipoEmpresa[] buscarTodas(){
        return tipos;
    }
    
    public boolean buscar() throws Exception{
    boolean bRet = false;
        if (this.nTipo==0)
            throw new Exception("TipoEmpresa.buscar: faltan datos");
        else{
            if (nTipo>=1 && nTipo <=5){
                this.sDescripcion = tipos[nTipo-1].getDescripcion();
                bRet = true;
            }
        }
        return bRet;
    }
    
    public int getTipo(){
        return this.nTipo;
    }
    
    public void setTipo(int value){
        this.nTipo=value;
    }
    
    public String getDescripcion(){
        return this.sDescripcion;
    }
    
    public void setDescripcion(String value){
        this.sDescripcion = value;
    }
}
