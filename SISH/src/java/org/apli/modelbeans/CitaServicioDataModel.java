package org.apli.modelbeans;

import java.util.List;  
import javax.faces.model.ListDataModel;  
import org.primefaces.model.SelectableDataModel;  

public class CitaServicioDataModel extends ListDataModel<CitaServicio> implements SelectableDataModel<CitaServicio> {    
  
    public CitaServicioDataModel() {  
    }  
  
    public CitaServicioDataModel(List<CitaServicio> data) {  
        super(data);  
    }  
      
    @Override
    public CitaServicio getRowData(String rowKey) {  
        //In a real app, a more efficient way like a query by rowKey should be implemented to deal with huge data  
        List<CitaServicio> pacientes = (List<CitaServicio>) getWrappedData();  
        System.out.println("Entro getRowData");
        for(CitaServicio ciserv : pacientes) {  
            if(ciserv.getFecCita().equals(rowKey))  
                return ciserv;  
        }  
        return null;  
    }  
  
    @Override
    public Object getRowKey(CitaServicio  ciserv) {  
        return ciserv.getFecCita();  
    }  

}                     