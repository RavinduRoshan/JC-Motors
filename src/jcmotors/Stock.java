/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jcmotors;

import java.util.Date;

/**
 *
 * @author Dhanushka
 */
public class Stock {
    private String stockID;
    private String partID;
    private String dealerID;
    private double cost;
    private double price;
    private double discount;
    private int quantity;
    private Date addDate;
    private String partName;
    private String dealerName;
    private String partType;
    private String partModel;
    private String partStock;
    private String dealerEmail;
    private String dealerContact;

    /**
     * @return the stockID
     */
    public String getStockID() {
        return stockID;
    }

    /**
     * @param stockID the stockID to set
     */
    public void setStockID(String stockID) {
        this.stockID = stockID;
    }

    /**
     * @return the partID
     */
    public String getPartID() {
        return partID;
    }

    /**
     * @param partID the partID to set
     */
    public void setPartID(String partID) {
        this.partID = partID;
    }

    /**
     * @return the dealerID
     */
    public String getDealerID() {
        return dealerID;
    }

    /**
     * @param dealerID the dealerID to set
     */
    public void setDealerID(String dealerID) {
        this.dealerID = dealerID;
    }

    /**
     * @return the cost
     */
    public double getCost() {
        return cost;
    }

    /**
     * @param cost the cost to set
     */
    public void setCost(double cost) {
        this.cost = cost;
    }

    /**
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * @return the discount
     */
    public double getDiscount() {
        return discount;
    }

    /**
     * @param discount the discount to set
     */
    public void setDiscount(double discount) {
        this.discount = discount;
    }

    /**
     * @return the quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }



    /**
     * @return the partName
     */
    public String getPartName() {
        return partName;
    }

    /**
     * @param partName the partName to set
     */
    public void setPartName(String partName) {
        this.partName = partName;
    }

    /**
     * @return the dealerName
     */
    public String getDealerName() {
        return dealerName;
    }

    /**
     * @param dealerName the dealerName to set
     */
    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    /**
     * @return the partType
     */
    public String getPartType() {
        return partType;
    }

    /**
     * @param partType the partType to set
     */
    public void setPartType(String partType) {
        this.partType = partType;
    }

    /**
     * @return the partModel
     */
    public String getPartModel() {
        return partModel;
    }

    /**
     * @param partModel the partModel to set
     */
    public void setPartModel(String partModel) {
        this.partModel = partModel;
    }

    /**
     * @return the partStock
     */
    public String getPartStock() {
        return partStock;
    }

    /**
     * @param partStock the partStock to set
     */
    public void setPartStock(String partStock) {
        this.partStock = partStock;
    }

    /**
     * @return the addDate
     */
    public Date getAddDate() {
        return addDate;
    }

    /**
     * @param addDate the addDate to set
     */
    public void setAddDate(Date addDate) {
        this.addDate = addDate;
    }

    /**
     * @return the dealerEmail
     */
    public String getDealerEmail() {
        return dealerEmail;
    }

    /**
     * @param dealerEmail the dealerEmail to set
     */
    public void setDealerEmail(String dealerEmail) {
        this.dealerEmail = dealerEmail;
    }

    /**
     * @return the dealerContact
     */
    public String getDealerContact() {
        return dealerContact;
    }

    /**
     * @param dealerContact the dealerContact to set
     */
    public void setDealerContact(String dealerContact) {
        this.dealerContact = dealerContact;
    }
}
