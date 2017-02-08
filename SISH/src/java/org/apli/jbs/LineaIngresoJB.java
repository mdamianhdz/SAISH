package org.apli.jbs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import org.apli.modelbeans.LineaIngreso;

/**
 *
 * @author JRuiz
 */
@ManagedBean
@ApplicationScoped
public class LineaIngresoJB {
    private static List<LineaIngreso> lineas;
    
    public LineaIngresoJB(){
        
    }
    
    public static List<LineaIngreso> getLineas(){
        if (lineas == null){
            System.out.println("aplic");
            try{
                lineas = new ArrayList<LineaIngreso>(Arrays.asList(
                        (new LineaIngreso()).buscaTodas()));
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return lineas;
    }
    
    public static LineaIngreso getUnaLinea(int nCve){
    LineaIngreso oRet = null;
        getLineas();
        for (LineaIngreso l : lineas) {  
            if (l.getCveLin() == nCve) {  
                oRet = l;  
            }  
        }
        return oRet;
    }
}
