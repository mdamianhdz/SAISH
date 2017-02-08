package org.apli.datamodels;

import java.util.List;  
import javax.faces.model.ListDataModel;
import org.apli.modelbeans.Factura;  
import org.primefaces.model.SelectableDataModel;
  
public class FacturaDataModel extends ListDataModel<Factura> implements SelectableDataModel<Factura> {    
  
    public FacturaDataModel() {  
    }  
  
    public FacturaDataModel(List<Factura> data) {  
        super(data);  
    }  
      
    @Override  
    public Factura getRowData(String rowKey) {  
        
        List<Factura> fact = (List<Factura>) getWrappedData();  
          
        for(Factura f : fact) {  
            if(f.getNumFolio()==Integer.parseInt(rowKey))
                return f;  
        }  
          
        return null;  
    }  
  
    @Override  
    public Object getRowKey(Factura f) {  
        return f.getNumFolio();  
    }  
}
