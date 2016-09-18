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
public class TableOrder extends AbstractTableModel {

    private static final String[] COLUMN_NAMES = {"Stock ID", "Part Type", "Part Name", "Part Model", "Quantity", "Unit Price",
        "With Discount", "Total"};
    private static ArrayList<Stock> list;
    private Calculations cal = new Calculations();

    TableOrder(ArrayList<Stock> sList) {
        list = sList;
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
                return list.get(rowIndex).getStockID();
            case 1:
                return list.get(rowIndex).getPartType();
            case 2:
                return list.get(rowIndex).getPartName();
            case 3:
                return list.get(rowIndex).getPartModel();
            case 4:
                return list.get(rowIndex).getQuantity();
            case 5:
                return list.get(rowIndex).getPrice();
            case 6:
                double price = list.get(rowIndex).getPrice();
                double discount = list.get(rowIndex).getDiscount();
                double priceDiscount = cal.priceWithDiscount(price, discount);
                return priceDiscount;
            case 7:
                price = list.get(rowIndex).getPrice();
                discount = list.get(rowIndex).getDiscount();
                priceDiscount = cal.priceWithDiscount(price, discount);
                double total = cal.quantityTotalPrice(priceDiscount, list.get(rowIndex).getQuantity());
                return total;
            default:
                return "Error!";
        }
    }

}