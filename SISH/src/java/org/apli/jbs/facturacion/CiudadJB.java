/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apli.jbs.facturacion;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
/**
 * Autor: Lily LnBnd
 */
@ManagedBean(name="oCiudadJB")
@RequestScoped

public class CiudadJB {
    
    public static List<org.apli.modelbeans.Ciudad> listCiudadesDF;
    public static List<org.apli.modelbeans.Ciudad> listCiudadesLE;

}
