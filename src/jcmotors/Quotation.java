/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jcmotors;

/**
 *
 * @author Ravindu
 */
public class Quotation {
    private String service_id;
    private String customer_id;
    private Double service_charge;
    private Double total;
    private String date;
    private String username;

    /**
     * @return the service_id
     */
    public String getService_id() {
        return service_id;
    }

    /**
     * @param service_id the service_id to set
     */
    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    /**
     * @return the customer_id
     */
    public String getCustomer_id() {
        return customer_id;
    }

    /**
     * @param customer_id the customer_id to set
     */
    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    /**
     * @return the service_charge
     */
    public Double getService_charge() {
        return service_charge;
    }

    /**
     * @param service_charge the service_charge to set
     */
    public void setService_charge(Double service_charge) {
        this.service_charge = service_charge;
    }

    /**
     * @return the total
     */
    public Double getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(Double total) {
        this.total = total;
    }

    /**
     * @return the date
     */
    public String getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }
}
