/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcmotors;

import java.util.ArrayList;

/**
 *
 * @author Dhanushka
 */
public class Calculations {

    /**
     * this returns the price of part after the discount when creating a order
     *
     * @param price
     * @param discount
     * @return
     */
    double priceWithDiscount(double price, double discount) {
        double value = 0;
        value = (price * (100 - discount)) / 100;
        return value;
    }
    
    /**
     * this returns the total price of a part quantity when creating a order
     * @param price
     * @param quantity
     * @return 
     */
    double quantityTotalPrice(double price, int quantity){
        double value = 0;
        value = price * quantity;
        return value;
    }
    
    /**
     * this returns the total price of all parts when creating a order
     * @param list
     * @return 
     */
    double calculateTotal(ArrayList<Double> list){
        double value = 0;
        for(int i = 0; i < list.size(); i++){
            value += 1;
        }
        return value;
    }
    
    /**
     * this return the returns the balance of payment when creating a order
     * @param total
     * @param payment
     * @return 
     */
    double calculateBalance(double total, double payment){
        double value = 0;
        value = payment - total;
        return value;
    }
}
