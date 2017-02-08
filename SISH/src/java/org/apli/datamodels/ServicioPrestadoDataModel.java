package org.apli.datamodels;

import java.io.Serializable;
import java.util.List;  
import javax.faces.model.ListDataModel;
import org.apli.modelbeans.ServicioPrestado;  
import org.primefaces.model.SelectableDataModel;
  
public class ServicioPrestadoDataModel extends ListDataModel<ServicioPrestado> implements SelectableDataModel<ServicioPrestado>, Serializable {    
  
    public ServicioPrestadoDataModel() {  
    }  
  
    public ServicioPrestadoDataModel(List<ServicioPrestado> data) {  
        super(data);  
    }  
      
    @Override  
    public ServicioPrestado getRowData(String rowKey) {  
        
        List<ServicioPrestado> servicios = (List<ServicioPrestado>) getWrappedData();  
          
        for(ServicioPrestado sp : servicios) {  
            if(sp.getIdFolio().equals(rowKey))  
                return sp;  
        }  
          
        return null;  
    }  
  
    @Override  
    public Object getRowKey(ServicioPrestado sp) {  
        return sp.getIdFolio();  
    }  
}
