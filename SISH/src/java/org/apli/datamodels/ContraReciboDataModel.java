package org.apli.datamodels;

import java.util.List;  
import javax.faces.model.ListDataModel;
import org.apli.modelbeans.ContraRecibo;  
import org.primefaces.model.SelectableDataModel;
  
public class ContraReciboDataModel extends ListDataModel<ContraRecibo> implements SelectableDataModel<ContraRecibo> {    
  
    public ContraReciboDataModel() {  
    }  
  
    public ContraReciboDataModel(List<ContraRecibo> data) {  
        super(data);  
    }  
      
    @Override  
    public ContraRecibo getRowData(String rowKey) {  
        
        List<ContraRecibo> contraR = (List<ContraRecibo>) getWrappedData();  
          
        for(ContraRecibo cr : contraR) {  
            if(cr.getNumContraRecibo()==Integer.parseInt(rowKey))
                return cr;  
        }  
          
        return null;  
    }  
  
    @Override  
    public Object getRowKey(ContraRecibo cr) {  
        return cr.getNumContraRecibo();  
    }  
}
