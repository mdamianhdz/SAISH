package org.apli.jbs.catalogos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import org.apli.modelbeans.Especialidad;
import org.primefaces.context.RequestContext;

/**
 * Bean administrado para el control de ABC de especialidades médicas
 * @author AliberGA
 */
@ManagedBean(name="oEspecialidad")
@RequestScoped
public class EspecialidadJB {
    private List<Especialidad> listaEspecialidades;
    public Especialidad oEsp=new Especialidad();

    public Especialidad oEspecialidad=new Especialidad();
    private static List<Especialidad> listaEsp;
    private static Especialidad selectedEspecialidad;
    private static Especialidad currentEspecialidad;
     private static boolean bDisDatos;
     private static String C;

    public EspecialidadJB() {
        listaEspecialidades=new ArrayList();
        llenaLista();
    }
    
    private void llenaLista(){
        try {
            listaEspecialidades.addAll(Arrays.asList(oEsp.buscarTodasEspecialidades()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
     public List<Especialidad> getListaEspecialidades(){
        return listaEspecialidades;
            }
                 
            
        }
