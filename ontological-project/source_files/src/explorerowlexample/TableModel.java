 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package explorerowlexample;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;


public class TableModel extends AbstractTableModel{

    public TableModel(ArrayList<String> list, int columCou) {
        this.list = list;
        this.columCou = columCou;
    }
    
    ArrayList<String> list;
    private int columCou;

    public void setColumCou(int columCou) {
        this.columCou = columCou;
    }

    @Override
    public String getColumnName(int column) {
        return list.get(column); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public int getRowCount() {
       return (list.size()/4)-1;
    }

    @Override
    public int getColumnCount() {
        return columCou;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        
        int value = (columCou*rowIndex+columnIndex%columCou)+columCou;
        if(list.size()>value){
            return list.get(value);
        }
        else{
            return "Erro";
        }    
    }
    
    
}
