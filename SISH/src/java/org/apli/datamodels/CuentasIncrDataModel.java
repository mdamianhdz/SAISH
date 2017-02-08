/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apli.datamodels;

import java.util.List;
import javax.faces.model.ListDataModel;
import org.apli.modelbeans.CuentaIncobrables;
import org.primefaces.model.SelectableDataModel;

/**
 *
 * @author ERJI
 */
public class CuentasIncrDataModel extends ListDataModel<CuentaIncobrables> implements SelectableDataModel<CuentaIncobrables> {
    
    public CuentasIncrDataModel(){
    }
    
    public CuentasIncrDataModel(List<CuentaIncobrables> data){
        super(data);
    }
    
    @Override
    public Object getRowKey(CuentaIncobrables c) {
        return c.getFolio();
    }

    @Override
    public CuentaIncobrables getRowData(String rowKey) {
        
        List<CuentaIncobrables> servicioPrestados=(List<CuentaIncobrables>) getWrappedData();
        for(CuentaIncobrables cuenta: servicioPrestados){
            if(cuenta.getFolio().equals(rowKey)){
                return cuenta;
            }
        }
        
        return null;
        
    }
    
}
