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
public class TableStock extends AbstractTableModel{
    
    private static final String[] COLUMN_NAMES = {"Stock ID", "Part Name", "Part Type", "Part Model", "Cost Rs.", "Price Rs.", "Discount %", "Quantity"};
    private static ArrayList<Stock> list;

    TableStock(ArrayList<Stock> sList) {
        list = sList;
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
                return list.get(rowIndex).getStockID();
            case 1:
                return list.get(rowIndex).getPartName();
            case 2:
                return list.get(rowIndex).getPartType();
            case 3:
                return list.get(rowIndex).getPartModel();
            case 4:
                return list.get(rowIndex).getCost();
            case 5:
                return list.get(rowIndex).getPrice();
            case 6:
                return list.get(rowIndex).getDiscount();
            case 7:
                return list.get(rowIndex).getQuantity();
            default:
                return "Error!";
        }
    }
    
}
