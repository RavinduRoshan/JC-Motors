/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jcmotors;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Dhanushka
 */
public class TableParts extends AbstractTableModel{

    private static final String[] COLUMN_NAMES = {"Part ID", "Part Name", "Type", "Model", "Stock"};
    private static ArrayList<Part> list;
    
    TableParts(ArrayList<Part> pList){
        list = pList;
    }
    
    @Override
    public int getRowCount() {
        return list.size();
    }
    
    public String getColumnName(int columnIndex){
        return COLUMN_NAMES[columnIndex];
    }

    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch(columnIndex){
            case 0:
                return list.get(rowIndex).getId();
            case 1:
                return list.get(rowIndex).getName();
            case 2:
                return list.get(rowIndex).getType();
            case 3:
                return list.get(rowIndex).getModel();
            case 4:
                return list.get(rowIndex).getStock();
            default:
                return "Error!";
        }
    }
    
}
