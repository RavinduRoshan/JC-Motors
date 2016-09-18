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
public class TableStockDealer extends AbstractTableModel {

    private static final String[] COLUMN_NAMES = {"Dealer ID", "Dealer Name"};
    private static ArrayList<Dealer> list;

    TableStockDealer(ArrayList<Dealer> dList) {
        list = dList;
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
                return list.get(rowIndex).getId();
            case 1:
                return list.get(rowIndex).getName();
            default:
                return "Error";
        }
    }
}
