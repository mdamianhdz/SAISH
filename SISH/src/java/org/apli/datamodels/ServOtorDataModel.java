/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apli.datamodels;

import java.util.List;
import javax.faces.model.ListDataModel;
import org.apli.modelbeans.ServicioPrestado;
import org.primefaces.model.SelectableDataModel;

/**
 *
 * @author JRuiz
 */
public class ServOtorDataModel extends ListDataModel<ServicioPrestado> implements SelectableDataModel<ServicioPrestado>{

    public ServOtorDataModel(){
    }
    
    public ServOtorDataModel(List<ServicioPrestado> data){
        super(data);
    }
    
    @Override
    public Object getRowKey(ServicioPrestado t) {
        return t.getIdFolio();
    }

    @Override
    public ServicioPrestado getRowData(String rowKey) {
        
        List<ServicioPrestado> servicioPrestados=(List<ServicioPrestado>) getWrappedData();
        for(ServicioPrestado servicio: servicioPrestados){
            if(servicio.getIdFolio().equals(rowKey)){
                return servicio;
            }
        }
        
        return null;
        
    }
    
}
