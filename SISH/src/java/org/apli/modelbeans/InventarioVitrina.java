/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apli.modelbeans;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;
import org.apache.log4j.Logger;

/**
 *
 * @author Lily_LnBnd
 */
public class InventarioVitrina implements Serializable {
    private int nVitrina;
    private String sTipo;
    private String sCve;
    private String sDesc;
    private String sLugar;
    private int nExistencia;
    private InvMatVitrina matVitrina;
    private InvMedVitrina medVitrina;
    private List<InventarioVitrina> invVitrinas;
    private static final Logger LOG = Logger.getLogger(InventarioVitrina.class.getName());

    
    public List<InventarioVitrina> getInventarioVitrina(List<InvMatVitrina> listMat, List<InvMedVitrina> listMed){
        List<InventarioVitrina> listRet=new ArrayList();
        InventarioVitrina oIV;
        for (int i=0; i<listMat.size();i++){
            oIV=new InventarioVitrina();
            oIV.setVitrina(listMat.get(i).getVitrina().getCveVitrina());
            oIV.setTipo("MAT");
            oIV.setCve(listMat.get(i).getMaterialCuracion().getCveMaterial());
            oIV.setDesc(listMat.get(i).getMaterialCuracion().getDescripcion());
            oIV.setLugar(listMat.get(i).getLugar());
            oIV.setExistencia(listMat.get(i).getCantidad());
            listRet.add(oIV);
        }
        for (int i=0; i<listMed.size();i++){
            oIV=new InventarioVitrina();
            oIV.setVitrina(listMed.get(i).getVitrina().getCveVitrina());
            oIV.setTipo("MED");
            oIV.setCve(listMed.get(i).getMedicamento().getCveMedicamento());
            oIV.setDesc(listMed.get(i).getMedicamento().getNomMedicamento());
            oIV.setLugar(listMed.get(i).getLugar());
            oIV.setExistencia(listMed.get(i).getCantidad());
            listRet.add(oIV);
        }
        return listRet;
    }
    
    public String modificarInvVitrina() {
        int nReg = 0;
        if (this.invVitrinas.isEmpty()) {
            return null;
        } else {
            for (int i = 0; i < this.invVitrinas.size(); i++) {
                if (this.invVitrinas.get(i).getMatVitrina() != null) {
                    matVitrina = this.invVitrinas.get(i).getMatVitrina();
                    try {
                        if (!this.invVitrinas.get(i).getMatVitrina().modificarInvMatVitrina().equals("")) {
                            nReg++;
                            LOG.trace("Se modifico el material con clave: " + this.invVitrinas.get(i).getMatVitrina().getMaterialCuracion().getCveMaterial());
                            LOG.trace("Cantidad:" + this.invVitrinas.get(i).getMatVitrina().getCantidad());
                        }
                    } catch (Exception ex) {
                        LOG.error(new StringBuilder()
                                .append("No se ha podido realizar la modificacion del inventario de material con clave: ")
                                .append(this.invVitrinas.get(i).getMatVitrina().getMaterialCuracion().getCveMaterial())
                                .toString());
                    }
                } else if (this.invVitrinas.get(i).getMedVitrina() != null) {
                    try {
                        if (!this.invVitrinas.get(i).getMedVitrina().modificarInvMedVitrina().equals("")) {
                            LOG.trace("Se ha modificado el medicamento con clave:" + this.invVitrinas.get(i).getMedVitrina().getMedicamento().getCveMedicamento());
                            LOG.trace("Cantidad:" + this.invVitrinas.get(i).getMedVitrina().getCantidad());
                            nReg++;
                        }
                    } catch (Exception ex) {
                        LOG.error(new StringBuilder().append("No se ha podido realizar la modificacion del inventario de medicamento con clave: ")
                                .append(this.invVitrinas.get(i).getMedVitrina().getMedicamento().getCveMedicamento())
                                .toString());
                    }
                }
            }
        }
        return new StringBuilder().append(nReg).toString();
    }

    
    public int getVitrina(){
        return nVitrina;
    }
    
    public void setVitrina(int nVitrina){
        this.nVitrina=nVitrina;
    }
    
    public String getTipo() {
        return sTipo;
    }

    public void setTipo(String sTipo) {
        this.sTipo = sTipo;
    }
    
    public String getCve() {
        return sCve;
    }

    public void setCve(String sCve) {
        this.sCve = sCve;
    }

    public String getDesc() {
        return sDesc;
    }

    public void setDesc(String sDesc) {
        this.sDesc = sDesc;
    }

    public String getLugar() {
        return sLugar;
    }

    public void setLugar(String sLugar) {
        this.sLugar = sLugar;
    }

    public int getExistencia() {
        return nExistencia;
    }

    public void setExistencia(int nExistencia) {
        this.nExistencia = nExistencia;
    }
    
    public InvMatVitrina getMatVitrina() {
        return matVitrina;
    }

    public void setMatVitrina(InvMatVitrina matVitrina) {
        this.matVitrina = matVitrina;
    }

    public InvMedVitrina getMedVitrina() {
        return medVitrina;
    }

    public void setMedVitrina(InvMedVitrina medVitrina) {
        this.medVitrina = medVitrina;
    }

    public List<InventarioVitrina> getInvVitrinas() {
        return invVitrinas;
    }

    public void setInvVitrinas(List<InventarioVitrina> invVitrinas) {
        this.invVitrinas = invVitrinas;
    }
    
    
    
}
