/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcmotors;

/**
 *
 * @author Dhanushka
 */
public class IDCreations {

    String newID = "";

    /**
     * create a new part id
     *
     * @param id
     * @return
     */
    String createPartID(String id) { // This parameter string(id) should be like JCxxxxx
        id = id.substring(2); // get the id without "JC"
        int number = Integer.parseInt(id); //convert that number string to integer

        if (number == 99999) {
            newID = "XXXXXXX"; //because the number should have only 5 digits
        } else {
            number += 1; //increment that number by 1
            newID = number + ""; //convert that integer to string
            int length = newID.length(); //get the length of that string

            if (length < 5) { //check the length is less than 5
                for (int i = length; i < 5; i++) { //add zeros in front of the string untill the length is 5
                    newID = "0" + newID;
                }
            }

            newID = "JC" + newID; //add "JC" as pre-letters of the part id which the length is 5
        }
        return newID;
    }

    /**
     * create a new stock id
     *
     * @param id
     * @return
     */
    String createStockID(String id) { // This parameter string(id) should be like Sxxxxxxxxx
        id = id.substring(1); // get the id without "S"
        int number = Integer.parseInt(id); //convert that number string to integer

        if (number == 999999999) {
            newID = "XXXXXXXXXX"; //because the number should have only 9 digits
        } else {
            number += 1; //increment that number by 1
            newID = number + ""; //convert that integer to string
            int length = newID.length(); //get the length of that string

            if (length < 9) { //check the length is less than 9
                for (int i = length; i < 9; i++) { //add zeros in front of the string untill the length is 9
                    newID = "0" + newID;
                }
            }

            newID = "S" + newID; //add "S" as pre-letter of the stock id which the length is 9
        }
        return newID;
    }

    /**
     * create a new stock id
     *
     * @param id
     * @return
     */
    String createDealerID(String id) { // This parameter string(id) should be like Dxxxxxxxxx
        id = id.substring(1); // get the id without "D"
        int number = Integer.parseInt(id); //convert that number string to integer

        if (number == 999999) {
            newID = "XXXXXXX"; //because the number should have only 6 digits
        } else {
            number += 1; //increment that number by 1
            newID = number + ""; //convert that integer to string
            int length = newID.length(); //get the length of that string

            if (length < 6) { //check the length is less than 9
                for (int i = length; i < 6; i++) { //add zeros in front of the string untill the length is 6
                    newID = "0" + newID;
                }
            }

            newID = "D" + newID; //add "D" as pre-letter of the dealer id which the length is 6
        }
        return newID;
    }

    /**
     * create a new customer id
     *
     * @param id
     * @return
     */
    String createCustomerID(String id) { // This parameter string(id) should be like Cxxxxxxxxx
        id = id.substring(1); // get the id without "C"
        int number = Integer.parseInt(id); //convert that number string to integer

        if (number == 999999) {
            newID = "XXXXXXX"; //because the number should have only 6 digits
        } else {
            number += 1; //increment that number by 1
            newID = number + ""; //convert that integer to string
            int length = newID.length(); //get the length of that string

            if (length < 6) { //check the length is less than 9
                for (int i = length; i < 6; i++) { //add zeros in front of the string untill the length is 6
                    newID = "0" + newID;
                }
            }

            newID = "C" + newID; //add "D" as pre-letter of the dealer id which the length is 6
        }
        return newID;
    }

    /**
     * create new service id
     *
     * @param id
     * @return
     */
    String createServiceID(String id) { // This parameter string(id) should be like SVCxxxx
        id = id.substring(3); // get the id without "SVC"
        int number = Integer.parseInt(id); //convert that number string to integer

        if (number == 9999999) {
            newID = "XXXXXXXXXX"; //because the number should have only 6 digits
        } else {
            number += 1; //increment that number by 1
            newID = number + ""; //convert that integer to string
            int length = newID.length(); //get the length of that string

            if (length < 6) { //check the length is less than 9
                for (int i = length; i < 6; i++) { //add zeros in front of the string untill the length is 6
                    newID = "0" + newID;
                }
            }
            newID = "SVC" + newID; //add "D" as pre-letter of the dealer id which the length is 6
        }
        return newID;
    }
}
