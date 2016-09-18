/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jcmotors;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Ravindu
 */
public class TableQuotationParts extends AbstractTableModel{
    
    private static final String[] COLUMN_NAMES = {"Part Name", "Type", "Model", "Quantity", "Unit Price", "With Discount", "Value"};
    private static ArrayList<SelectedParts> list;
    
    TableQuotationParts(ArrayList<SelectedParts> spList){
        list = spList;
    }

    @Override
    public int getRowCount() {
        return list.size();
    }
    
    public String getColumnName(int columnIndex) {
        return COLUMN_NAMES[columnIndex];
    }

    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return list.get(rowIndex).getPart_name();
            case 1:
                return list.get(rowIndex).getType();
            case 2:
                return list.get(rowIndex).getModel();
            case 3:
                return list.get(rowIndex).getQuantity();
            case 4:
                return list.get(rowIndex).getUnit_price();
            case 5:
                return list.get(rowIndex).getWith_discount();
            case 6:
                return list.get(rowIndex).getValue();
            default:
                return "Error";
        }
    }
    
}
