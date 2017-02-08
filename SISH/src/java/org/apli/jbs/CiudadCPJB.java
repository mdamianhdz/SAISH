package org.apli.jbs;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import org.apli.modelbeans.CiudadCP;

/**
 * Autor: Isabel Espinoza Espinoza
 * Fecha: Mayo 2014
 */
@ManagedBean(name="oCiudadCP")
@RequestScoped
public class CiudadCPJB {
    private CiudadCP oCiudadCP=new CiudadCP();
    private List<CiudadCP> oCiudadesCP;
    public void setCiudadesCP(List<CiudadCP> oCiudadesCP) {
        this.oCiudadesCP = oCiudadesCP;
    }
     public List<CiudadCP> getCiudadesCP() throws Exception{
        List<CiudadCP> list=new ArrayList<CiudadCP>();
        CiudadCP oP=new CiudadCP();
	try{
            list = oP.getCodPostales();
	} catch(Exception e){
            e.printStackTrace();
	}
	return list;
    }
     
     public CiudadCPJB() throws Exception{
         
            oCiudadesCP = new CiudadCP().getCodPostales();
 
     }
    public CiudadCP getCiudadCP() {
        return oCiudadCP;
    }

    public void setCiudadCP(CiudadCP oCiudadCP) {
        this.oCiudadCP = oCiudadCP;
    }
    
}
