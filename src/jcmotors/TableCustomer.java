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
public class TableCustomer extends AbstractTableModel{
    
    private static final String[] COLUMN_NAMES = {"Customer Name", "Address", "Contact"};
    private static ArrayList<Customer> list;

    TableCustomer(ArrayList<Customer> cList) {
        list = cList;
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
                return list.get(rowIndex).getName();
            case 1:
                return list.get(rowIndex).getAddress();
            case 2:
                return list.get(rowIndex).getContact();
            default:
                return "Error";
        }
    }
    
}
