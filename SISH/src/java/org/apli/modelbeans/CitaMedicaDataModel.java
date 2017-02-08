package org.apli.modelbeans;

import java.util.List;  
import javax.faces.model.ListDataModel;  
import org.primefaces.model.SelectableDataModel;  

public class CitaMedicaDataModel extends ListDataModel<CitaMedica> implements SelectableDataModel<CitaMedica> {    
  
    public CitaMedicaDataModel() {  
    }  
  
    public CitaMedicaDataModel(List<CitaMedica> data) {  
        super(data);  
        for(CitaMedica cita : data) {  
           System.out.println(cita.getFecCita()+"-"+cita.getPaciente().getNombre());
        }
    }  
      
    public CitaMedica getRowData(String rowKey) {  
        //In a real app, a more efficient way like a query by rowKey should be implemented to deal with huge data  
        List<CitaMedica> citas = (List<CitaMedica>) getWrappedData();  
        for(CitaMedica cita : citas) {  
            if(cita.getFecCita().toString().equals(rowKey) )  
                return cita;  
        }  
        return null;  
    }  
  
    @Override
    public Object getRowKey(CitaMedica  cita) {  
        return cita.getFecCita();  
    }  

}                     