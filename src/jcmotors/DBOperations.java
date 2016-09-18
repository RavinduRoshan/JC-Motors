
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcmotors;

import com.mysql.jdbc.PreparedStatement;
import java.sql.Connection;
import java.util.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 *
 * @author Dhanushka
 */
public class DBOperations {

    String url = "jdbc:mysql://localhost:3306/jcmotors";
    String username = "root";
    String password = "";
    Connection con = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    /**
     * compare username and password
     *
     * @param username
     * @param password
     * @return 0 - login successful
     * @return 1 - login unsuccessful
     * @return 2 - database error
     */
    int login(String username, String password) {
        try {
            con = (Connection) DriverManager.getConnection(url, this.username, this.password); //get connection
            String query = "SELECT username, password FROM user WHERE username = '" + username + "'";
            pst = (PreparedStatement) con.prepareStatement(query);

            rs = pst.executeQuery();

            while (rs.next()) {
                if (username.equals(rs.getString(1)) && password.equals(rs.getString(2))) { //check the username and password
                    return 0;
                } else {
                    return 1;
                }
            }

        } catch (Exception e) {
            System.out.println(e);
            return 2;
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
            }
        }
        return 1;
    }

    //* ==========  B E L O W  F U N C T I O N S  A R E  C O M M O N  F O R  M A I N  F R A M E  ========== *\\
    /**
     * This function get the values for drop downs in the search part
     *
     * @param column
     * @param table
     * @return
     */
    String[] dropDownManage(String column, String table) {
        try {
            con = (Connection) DriverManager.getConnection(url, username, password); //get connection
            String query = "SELECT COUNT(DISTINCT " + column + " ) FROM " + table;
            pst = (PreparedStatement) con.prepareStatement(query);

            rs = pst.executeQuery();
            int length = 0;
            while (rs.next()) {
                length = rs.getInt(1);
            }
            //System.out.println(length);
            String[] array = new String[length];

            query = "SELECT DISTINCT " + column + " FROM " + table + " ORDER BY " + column;
            pst = (PreparedStatement) con.prepareStatement(query);
            rs = pst.executeQuery();

            length = 0;
            while (rs.next()) {
                array[length] = rs.getString(1);
                length += 1;
            }
            return array;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
            }
        }
    }

    /**
     * return the max value of the column
     *
     * @param column
     * @param table
     * @return
     */
    String getMaxRecord(String column, String table) {
        try {
            con = (Connection) DriverManager.getConnection(url, username, password); //get connection
            String query = "SELECT MAX(" + column + ") FROM " + table + ";";
            pst = (PreparedStatement) con.prepareStatement(query);

            rs = pst.executeQuery();
            String lastID = "";
            while (rs.next()) {
                lastID = rs.getString(1);
            }

            return lastID;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
            }
        }
    }

    /**
     * this returns a string value. Can use to find any string record
     *
     * @param column
     * @param whereColumn
     * @param whereValue
     * @param table
     * @return
     */
    String searchStringRecord(String column, String whereColumn, String whereValue, String table) {
        try {
            String record = "";
            con = (Connection) DriverManager.getConnection(url, username, password); //get connection
            String query = "SELECT " + column + " FROM " + table + " WHERE " + whereColumn + " = '" + whereValue + "'";
            pst = (PreparedStatement) con.prepareStatement(query);

            rs = pst.executeQuery();

            while (rs.next()) {
                record = rs.getString(1);
            }

            return record;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
            }
        }
    }

    /**
     * this returns a integer value. Can use to find any integer record
     *
     * @param column
     * @param whereColumn
     * @param whereValue
     * @param table
     * @return
     */
    Integer searchIntRecord(String column, String whereColumn, String whereValue, String table) {
        try {
            Integer record = 0;
            con = (Connection) DriverManager.getConnection(url, username, password); //get connection
            String query = "SELECT " + column + " FROM " + table + " WHERE " + whereColumn + " = '" + whereValue + "'";
            pst = (PreparedStatement) con.prepareStatement(query);

            rs = pst.executeQuery();

            while (rs.next()) {
                record = rs.getInt(1);
            }

            return record;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
            }
        }
    }

    /**
     * This function use to delete a record
     *
     * @param table
     * @param column
     * @param value
     * @return
     */
    boolean deleteRecord(String table, String column, String value) {
        try {
            con = (Connection) DriverManager.getConnection(url, username, password); //get connection
            String query = "DELETE FROM " + table + " WHERE " + column + " = '" + value + "'";
            pst = (PreparedStatement) con.prepareStatement(query);
            pst.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
            }
        }
    }

    /**
     * This function use to delete all record
     *
     * @param table
     * @return
     */
    boolean deleteAllRecord(String table) {
        try {
            con = (Connection) DriverManager.getConnection(url, username, password); //get connection
            String query = "DELETE FROM " + table;
            pst = (PreparedStatement) con.prepareStatement(query);
            pst.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("deleteAllRecord() --> " + e);
            return false;
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
            }
        }
    }
    
    /**
     * this returns number of rows in the table. When a exception throws it
     * returns -1
     *
     * @param table
     * @return
     */
    int checkTheTableEmpty(String table) {
        int count = -1;
        try {
            con = (Connection) DriverManager.getConnection(url, username, password); //get connection
            String query = "SELECT COUNT(*) FROM "+ table;
            pst = (PreparedStatement) con.prepareStatement(query);
            
            rs = pst.executeQuery();
            while (rs.next()) {
                count = rs.getInt(1);
            }
            
            return count;
        } catch (Exception e) {
            System.out.println("checkTheTableEmpty()->" + e);
            return count;
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
            }
        }
    }

    //* ==========  B E L O W  F U N C T I O N S  F O R  P A R T  M A N A G E M E N T  T A B  ========== *\\
    /**
     * add a new part
     *
     * @param part
     * @return
     */
    boolean addPart(Part part) {
        try {
            con = (Connection) DriverManager.getConnection(url, username, password); //get connection
            String query = "INSERT INTO part (part_id, part_name, type, model, stock) VALUES (?, ?, ?, ?, ?)";
            pst = (PreparedStatement) con.prepareStatement(query);

            //assign values to the query
            pst.setString(1, part.getId());
            pst.setString(2, part.getName());
            pst.setString(3, part.getType());
            pst.setString(4, part.getModel());
            pst.setString(5, part.getStock());

            //execute the query
            pst.executeUpdate();

            return true;

        } catch (Exception e) {
            System.out.println(e);
            return false;
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
            }
        }
    }

    /**
     * this return all the data of the part table as array list
     *
     * @return
     */
    ArrayList<Part> getParts() {
        try {
            ArrayList<Part> list = new ArrayList<Part>();

            con = (Connection) DriverManager.getConnection(url, username, password); //get connection
            String query = "SELECT * FROM part ORDER BY part_id";
            pst = (PreparedStatement) con.prepareStatement(query);

            rs = pst.executeQuery();

            while (rs.next()) {
                Part p = new Part();
                p.setId(rs.getString(1));
                p.setName(rs.getString(2));
                p.setType(rs.getString(3));
                p.setModel(rs.getString(4));
                p.setStock(rs.getString(5));

                list.add(p);
            }

            return list;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
            }
        }
    }

    /**
     * this function updates part details
     *
     * @param part
     * @return
     */
    boolean updatePart(Part part) {
        try {
            con = (Connection) DriverManager.getConnection(url, username, password); //get connection
            String query = "UPDATE part "
                    + "SET part_name = '" + part.getName() + "', "
                    + "type = '" + part.getType() + "', "
                    + "model = '" + part.getModel() + "', "
                    + "stock = '" + part.getStock() + "' "
                    + "WHERE part_id = '" + part.getId() + "'";
            pst = (PreparedStatement) con.prepareStatement(query);
            pst.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
            }
        }
    }

    /**
     * check the parameter part id is in the stock table if mentioned part id is
     * in the stock table then return true else return false
     *
     * @param part
     * @return
     */
    boolean checkPartInStockTable(Part part) {
        try {
            con = (Connection) DriverManager.getConnection(url, username, password); //get connection
            String query = "SELECT * FROM stock WHERE part_id = '" + part.getId() + "'";
            pst = (PreparedStatement) con.prepareStatement(query);

            rs = pst.executeQuery();

            int count = 0;
            while (rs.next()) {
                count += 1;
            }

            if (count > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            System.out.println(e);
            return true;
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
            }
        }
    }

    /**
     * when add a new part, check that part already exists the system. If it is
     * then return true. Else return false
     *
     * @param part
     * @return
     */
    boolean isPartExists(Part part) {
        try {
            con = (Connection) DriverManager.getConnection(url, username, password); //get connection
            String query = "SELECT * FROM part "
                    + "WHERE part_name = '" + part.getName() + "' AND "
                    + "type = '" + part.getType() + "' AND "
                    + "model = '" + part.getModel() + "' AND "
                    + "stock = '" + part.getStock() + "'";
            pst = (PreparedStatement) con.prepareStatement(query);

            rs = pst.executeQuery();

            int count = 0;
            while (rs.next()) {
                count += 1;
            }

            if (count > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            System.out.println(e);
            return true;
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
            }
        }
    }

    /**
     * This use to search record of part table
     *
     * @param columns
     * @param values
     * @return
     */
    ArrayList<Part> searchPart(String[] columns, String[] values) {
        try {
            ArrayList<Part> list = new ArrayList<Part>();

            // below if statements check what query should execute
            String query = "";
            if (columns.length == 1) {
                query = "SELECT * FROM part WHERE " + columns[0] + " = '" + values[0] + "'";
            } else if (columns.length == 2) {
                query = "SELECT * FROM part WHERE " + columns[0] + " = '" + values[0] + "' AND "
                        + columns[1] + " = '" + values[1] + "'";
            } else if (columns.length == 3) {
                query = "SELECT * FROM part WHERE " + columns[0] + " = '" + values[0] + "' AND "
                        + columns[1] + " = '" + values[1] + "' AND "
                        + columns[2] + " = '" + values[2] + "'";
            } else if (columns.length == 4) {
                query = "SELECT * FROM part WHERE " + columns[0] + " = '" + values[0] + "' AND "
                        + columns[1] + " = '" + values[1] + "' AND "
                        + columns[2] + " = '" + values[2] + "' AND "
                        + columns[3] + " = '" + values[3] + "'";
            } else if (columns.length == 5) {
                query = "SELECT * FROM part WHERE " + columns[0] + " = '" + values[0] + "' AND "
                        + columns[1] + " = '" + values[1] + "' AND "
                        + columns[2] + " = '" + values[2] + "' AND "
                        + columns[3] + " = '" + values[3] + "' AND "
                        + columns[4] + " = '" + values[4] + "'";
            }

            con = (Connection) DriverManager.getConnection(url, username, password); //get connection
            pst = (PreparedStatement) con.prepareStatement(query);

            rs = pst.executeQuery();

            while (rs.next()) {
                Part p = new Part();
                p.setId(rs.getString(1));
                p.setName(rs.getString(2));
                p.setType(rs.getString(3));
                p.setModel(rs.getString(4));
                p.setStock(rs.getString(5));

                list.add(p); //add part object to the array list
            }

            return list;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
            }
        }
    }

    //* ==========  B E L O W  F U N C T I O N S  F O R  S T O C K  M A N A G E M E N T  T A B  ========== *\\
    /**
     * this return all the data of the stock table as array list
     *
     * @return
     */
    ArrayList<Stock> getStocks() {
        try {
            ArrayList<Stock> list = new ArrayList<Stock>();

            con = (Connection) DriverManager.getConnection(url, username, password); //get connection
            String query = "SELECT * FROM stock "
                    + "INNER JOIN part "
                    + "ON stock.part_id = part.part_id "
                    + "INNER JOIN dealer "
                    + "ON stock.dealer_id = dealer.dealer_id "
                    + "ORDER BY stock_id";
            pst = (PreparedStatement) con.prepareStatement(query);

            rs = pst.executeQuery();

            while (rs.next()) {
                Stock stock = new Stock();
                stock.setStockID(rs.getString(1));
                stock.setPartID(rs.getString(2));
                stock.setDealerID(rs.getString(3));
                stock.setCost(rs.getDouble(4));
                stock.setPrice(rs.getDouble(5));
                stock.setDiscount(rs.getDouble(6));
                stock.setQuantity(rs.getInt(7));
                stock.setAddDate(rs.getDate(8));
                //stock.setPartID(rs.getString(9));
                stock.setPartName(rs.getString(10));
                stock.setPartType(rs.getString(11));
                stock.setPartModel(rs.getString(12));
                stock.setPartStock(rs.getString(13));
                //stock.setDealerID(rs.getString(14));
                stock.setDealerName(rs.getString(15));
                stock.setDealerEmail(rs.getString(16));
                stock.setDealerContact(rs.getString(17));

                list.add(stock);
            }

            return list;
        } catch (Exception e) {
            System.out.println("getStocks() -> " + e);
            return null;
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
            }
        }
    }

    /**
     * this return all the data of the dealer table as array list
     *
     * @return
     */
    ArrayList<Dealer> getDealers() {
        try {
            ArrayList<Dealer> list = new ArrayList<Dealer>();

            con = (Connection) DriverManager.getConnection(url, username, password); //get connection
            String query = "SELECT * FROM dealer";
            pst = (PreparedStatement) con.prepareStatement(query);

            rs = pst.executeQuery();

            while (rs.next()) {
                Dealer dealer = new Dealer();
                dealer.setId(rs.getString(1));
                dealer.setName(rs.getString(2));
                dealer.setEmail(rs.getString(3));
                dealer.setContact(rs.getString(4));

                list.add(dealer);
            }

            return list;
        } catch (Exception e) {
            System.out.println("getDealers() -> " + e);
            return null;
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
            }
        }
    }

    /**
     * add a new stock
     *
     * @param stock
     * @return
     */
    boolean addStock(Stock stock) {
        try {
            con = (Connection) DriverManager.getConnection(url, username, password); //get connection
            String query = "INSERT INTO stock "
                    + "(stock_id, part_id, dealer_id, cost, price, discount, quantity, date) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            pst = (PreparedStatement) con.prepareStatement(query);

            //convert util date to sql date
            java.util.Date date = new java.util.Date(stock.getAddDate().getTime());
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());

            //assign values to the query
            pst.setString(1, stock.getStockID());
            pst.setString(2, stock.getPartID());
            pst.setString(3, stock.getDealerID());
            pst.setDouble(4, stock.getCost());
            pst.setDouble(5, stock.getPrice());
            pst.setDouble(6, stock.getDiscount());
            pst.setInt(7, stock.getQuantity());
            pst.setDate(8, sqlDate);

            //execute the query
            pst.executeUpdate();

            return true;

        } catch (Exception e) {
            System.out.println("addStock() -> " + e);
            return false;
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
            }
        }
    }

    /**
     * This use to search record of stock table
     *
     * @param columns
     * @param values
     * @return
     */
    ArrayList<Stock> searchStock(String[] columns, String[] values, String[] table) {
        try {
            ArrayList<Stock> list = new ArrayList<Stock>();

            // below if statements check what query should execute
            String query = "";
            if (columns.length == 1) {
                query = "SELECT * FROM stock "
                        + "INNER JOIN part ON stock.part_id = part.part_id "
                        + "INNER JOIN dealer ON stock.dealer_id = dealer.dealer_id "
                        + "WHERE " + table[0] + "." + columns[0] + " = '" + values[0] + "'";
            } else if (columns.length == 2) {
                query = "SELECT * FROM stock "
                        + "INNER JOIN part ON stock.part_id = part.part_id "
                        + "INNER JOIN dealer ON stock.dealer_id = dealer.dealer_id "
                        + "WHERE " + table[0] + "." + columns[0] + " = '" + values[0] + "' AND "
                        + table[1] + "." + columns[1] + " = '" + values[1] + "'";
            } else if (columns.length == 3) {
                query = "SELECT * FROM stock "
                        + "INNER JOIN part ON stock.part_id = part.part_id "
                        + "INNER JOIN dealer ON stock.dealer_id = dealer.dealer_id "
                        + "WHERE " + table[0] + "." + columns[0] + " = '" + values[0] + "' AND "
                        + table[1] + "." + columns[1] + " = '" + values[1] + "' AND "
                        + table[2] + "." + columns[2] + " = '" + values[2] + "'";
            } else if (columns.length == 4) {
                query = "SELECT * FROM stock "
                        + "INNER JOIN part ON stock.part_id = part.part_id "
                        + "INNER JOIN dealer ON stock.dealer_id = dealer.dealer_id "
                        + "WHERE " + table[0] + "." + columns[0] + " = '" + values[0] + "' AND "
                        + table[1] + "." + columns[1] + " = '" + values[1] + "' AND "
                        + table[2] + "." + columns[2] + " = '" + values[2] + "' AND "
                        + table[3] + "." + columns[3] + " = '" + values[3] + "'";
            } else if (columns.length == 5) {
                query = "SELECT * FROM stock "
                        + "INNER JOIN part ON stock.part_id = part.part_id "
                        + "INNER JOIN dealer ON stock.dealer_id = dealer.dealer_id "
                        + "WHERE " + table[0] + "." + columns[0] + " = '" + values[0] + "' AND "
                        + table[1] + "." + columns[1] + " = '" + values[1] + "' AND "
                        + table[2] + "." + columns[2] + " = '" + values[2] + "' AND "
                        + table[3] + "." + columns[3] + " = '" + values[3] + "' AND "
                        + table[4] + "." + columns[4] + " = '" + values[4] + "'";
            } else if (columns.length == 6) {
                query = "SELECT * FROM stock "
                        + "INNER JOIN part ON stock.part_id = part.part_id "
                        + "INNER JOIN dealer ON stock.dealer_id = dealer.dealer_id "
                        + "WHERE " + table[0] + "." + columns[0] + " = '" + values[0] + "' AND "
                        + table[1] + "." + columns[1] + " = '" + values[1] + "' AND "
                        + table[2] + "." + columns[2] + " = '" + values[2] + "' AND "
                        + table[3] + "." + columns[3] + " = '" + values[3] + "' AND "
                        + table[4] + "." + columns[4] + " = '" + values[4] + "' AND "
                        + table[5] + "." + columns[5] + " = '" + values[5] + "'";
            }

            con = (Connection) DriverManager.getConnection(url, username, password); //get connection
            pst = (PreparedStatement) con.prepareStatement(query);

            rs = pst.executeQuery();

            while (rs.next()) {
                Stock stock = new Stock();
                stock.setStockID(rs.getString(1));
                stock.setPartID(rs.getString(2));
                stock.setDealerID(rs.getString(3));
                stock.setCost(rs.getDouble(4));
                stock.setPrice(rs.getDouble(5));
                stock.setDiscount(rs.getDouble(6));
                stock.setQuantity(rs.getInt(7));
                stock.setAddDate(rs.getDate(8));
                //stock.setPartID(rs.getString(9));
                stock.setPartName(rs.getString(10));
                stock.setPartType(rs.getString(11));
                stock.setPartModel(rs.getString(12));
                stock.setPartStock(rs.getString(13));
                //stock.setDealerID(rs.getString(14));
                stock.setDealerName(rs.getString(15));
                stock.setDealerEmail(rs.getString(16));
                stock.setDealerContact(rs.getString(17));

                list.add(stock); //add part object to the array list
            }

            return list;
        } catch (Exception e) {
            System.out.println("search stock -> " + e);
            return null;
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
            }
        }
    }

    /**
     * this function updates part details
     *
     * @param part
     * @return
     */
    boolean updateStock(Stock stock) {
        try {

            //convert util date to sql date
            java.util.Date date = new java.util.Date(stock.getAddDate().getTime());
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());

            con = (Connection) DriverManager.getConnection(url, username, password); //get connection
            String query = "UPDATE stock "
                    + "SET part_id = '" + stock.getPartID() + "', "
                    + "dealer_id = '" + stock.getDealerID() + "', "
                    + "cost = " + stock.getCost() + ", "
                    + "price = " + stock.getPrice() + ", "
                    + "discount = " + stock.getDiscount() + ", "
                    + "quantity = " + stock.getQuantity() + ", "
                    + "date = '" + sqlDate + "' "
                    + "WHERE stock_id = '" + stock.getStockID() + "'";
            pst = (PreparedStatement) con.prepareStatement(query);
            pst.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
            }
        }
    }

    /**
     * Add a new dealer
     *
     * @param dealer
     * @return
     */
    boolean addDealer(Dealer dealer) {
        try {
            con = (Connection) DriverManager.getConnection(url, username, password); //get connection
            String query = "INSERT INTO dealer "
                    + "(dealer_id, dealer_name, email, contact) "
                    + "VALUES (?, ?, ?, ?)";
            pst = (PreparedStatement) con.prepareStatement(query);

            //assign values to the query
            pst.setString(1, dealer.getId());
            pst.setString(2, dealer.getName());
            pst.setString(3, dealer.getEmail());
            pst.setString(4, dealer.getContact());

            //execute the query
            pst.executeUpdate();

            return true;

        } catch (Exception e) {
            System.out.println("addDealer() -> " + e);
            return false;
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
            }
        }
    }

    /**
     * when add and update a new dealer or already exists dealer, check that
     * dealer details already exists the system. If it is then return true. Else
     * return false
     *
     * @param dealer
     * @return
     */
    boolean isDealerExists(Dealer dealer) {
        try {
            con = (Connection) DriverManager.getConnection(url, username, password); //get connection
            String query = "SELECT * FROM dealer "
                    + "WHERE dealer_name = '" + dealer.getName() + "' AND "
                    + "email = '" + dealer.getEmail() + "' AND "
                    + "contact = '" + dealer.getContact() + "'";
            pst = (PreparedStatement) con.prepareStatement(query);

            rs = pst.executeQuery();

            int count = 0;
            while (rs.next()) {
                count += 1;
            }

            if (count > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            System.out.println(e);
            return true;
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
            }
        }
    }

    /**
     * this function updates dealer details
     *
     * @param dealer
     * @return
     */
    boolean updateDealer(Dealer dealer) {
        try {
            con = (Connection) DriverManager.getConnection(url, username, password); //get connection
            String query = "UPDATE dealer "
                    + "SET dealer_name = '" + dealer.getName() + "', "
                    + "email = '" + dealer.getEmail() + "', "
                    + "contact = '" + dealer.getContact() + "' "
                    + "WHERE dealer_id = '" + dealer.getId() + "'";
            pst = (PreparedStatement) con.prepareStatement(query);
            pst.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
            }
        }
    }

    /**
     * This use to search record of dealer table
     *
     * @param columns
     * @param values
     * @return
     */
    ArrayList<Dealer> searchDealer(String[] columns, String[] values) {
        try {
            ArrayList<Dealer> list = new ArrayList<Dealer>();

            // below if statements check what query should execute
            String query = "";
            if (columns.length == 1) {
                query = "SELECT * FROM dealer WHERE " + columns[0] + " = '" + values[0] + "'";
            } else if (columns.length == 2) {
                query = "SELECT * FROM dealer WHERE " + columns[0] + " = '" + values[0] + "' AND "
                        + columns[1] + " = '" + values[1] + "'";
            } else if (columns.length == 3) {
                query = "SELECT * FROM dealer WHERE " + columns[0] + " = '" + values[0] + "' AND "
                        + columns[1] + " = '" + values[1] + "' AND "
                        + columns[2] + " = '" + values[2] + "'";
            } else if (columns.length == 4) {
                query = "SELECT * FROM dealer WHERE " + columns[0] + " = '" + values[0] + "' AND "
                        + columns[1] + " = '" + values[1] + "' AND "
                        + columns[2] + " = '" + values[2] + "' AND "
                        + columns[3] + " = '" + values[3] + "'";
            }

            con = (Connection) DriverManager.getConnection(url, username, password); //get connection
            pst = (PreparedStatement) con.prepareStatement(query);

            rs = pst.executeQuery();

            while (rs.next()) {
                Dealer d = new Dealer();
                d.setId(rs.getString(1));
                d.setName(rs.getString(2));
                d.setEmail(rs.getString(3));
                d.setContact(rs.getString(4));

                list.add(d); //add part object to the array list
            }

            return list;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
            }
        }
    }

    /**
     * check the parameter dealer id is in the stock table if mentioned dealer
     * id is in the stock table then return true else return false
     *
     * @param dealer
     * @return
     */
    boolean checkDealerInStockTable(Dealer dealer) {
        try {
            con = (Connection) DriverManager.getConnection(url, username, password); //get connection
            String query = "SELECT * FROM stock WHERE dealer_id = '" + dealer.getId() + "'";
            pst = (PreparedStatement) con.prepareStatement(query);

            rs = pst.executeQuery();

            int count = 0;
            while (rs.next()) {
                count += 1;
            }

            if (count > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            System.out.println(e);
            return true;
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
            }
        }
    }

    //* ===============  B E L O W  F U N C T I O N S  F O R  P U R C H A S E S  T A B  =============== *\\
    /**
     * this adds a new part to order table or increase quantity of already
     * exists part in order table. this returns 0: if update a stock in order
     * table, 1 : if add a new stock to the order table, 2 : if not enough stock
     * according to mentioned part, 3 : if there some error occurred
     *
     * @param stock
     * @param quantity
     */
    int addNewPartToOrder(Stock stock, int quantity) {
        try {
            String queryOne = "";
            String queryTwo = "";

            if (isStockIDInOrder(stock) == 0) { //check the stock is already in the order table
                int stockQty = searchIntRecord("quantity", "stock_id", stock.getStockID(), "stock"); //get the stock qty

                if (stockQty >= quantity) { // check the stock qty > need qty
                    int nowQty = searchIntRecord("quantity", "stock_id", stock.getStockID(), "temp_order"); //get qty
                    int newQty = nowQty + quantity; // manage qty

                    con = (Connection) DriverManager.getConnection(url, username, password); //get connection
                    queryOne = "UPDATE temp_order SET quantity = " + newQty + " "
                            + "WHERE stock_id = '" + stock.getStockID() + "'";
                    pst = (PreparedStatement) con.prepareStatement(queryOne);

                    //execute the query for order table
                    pst.executeUpdate();

                    newQty = stockQty - quantity; // manage qty
                    queryTwo = "UPDATE stock SET quantity = " + newQty + " "
                            + "WHERE stock_id = '" + stock.getStockID() + "'";
                    pst = (PreparedStatement) con.prepareStatement(queryTwo);

                    //execute the query for stock table
                    pst.executeUpdate();

                    return 0;
                } else {
                    return 2;
                }

            } else if (isStockIDInOrder(stock) == 1) { //check the stock is already in the order table
                int stockQty = searchIntRecord("quantity", "stock_id", stock.getStockID(), "stock"); //get the stock qty

                if (stockQty >= quantity) { // check the stock qty > need qty
                    con = (Connection) DriverManager.getConnection(url, username, password); //get connection
                    queryOne = "INSERT INTO temp_order "
                            + "(stock_id, part_id, quantity) "
                            + "VALUES (?, ?, ?)";
                    pst = (PreparedStatement) con.prepareStatement(queryOne);
                    //assign values to the query
                    pst.setString(1, stock.getStockID());
                    pst.setString(2, stock.getPartID());
                    pst.setInt(3, quantity);

                    //execute the query for order table
                    pst.executeUpdate();

                    int newQty = stockQty - quantity; // manage qty
                    queryTwo = "UPDATE stock SET quantity = " + newQty + " "
                            + "WHERE stock_id = '" + stock.getStockID() + "'";
                    pst = (PreparedStatement) con.prepareStatement(queryTwo);

                    //execute the query for stock table
                    pst.executeUpdate();
                    return 1;
                } else {
                    return 2;
                }

            } else {
                return 3;
            }
        } catch (Exception e) {
            System.out.println(e);
            return 3;
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
            }
        }
    }

    /**
     * check the stock is already in the order table. this returns 0 : if stock
     * is already in the order table, 1 : if stock is not in the order table, 2
     * : if there some error occurred
     *
     * @param stock
     * @return
     */
    int isStockIDInOrder(Stock stock) {
        try {
            con = (Connection) DriverManager.getConnection(url, username, password); //get connection
            String query = "SELECT * FROM temp_order WHERE stock_id = '" + stock.getStockID() + "'";
            pst = (PreparedStatement) con.prepareStatement(query);

            int count = 0;
            rs = pst.executeQuery();

            while (rs.next()) {
                count += 1;
            }

            if (count > 0) {
                return 0; // if stock is already in the order table
            } else {
                return 1; // if stock is not in the order table
            }
        } catch (Exception e) {
            System.out.println("checkStockIDInOrder() --> " + e);
            return 2; // if there some error occurred
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
            }
        }
    }

    /**
     * this return all the data of the temp_order table as array list
     *
     * @return
     */
    ArrayList<Stock> getOrders() {
        try {
            ArrayList<Stock> list = new ArrayList<Stock>();

            con = (Connection) DriverManager.getConnection(url, username, password); //get connection
            String query = "SELECT * FROM temp_order "
                    + "INNER JOIN stock "
                    + "ON temp_order.stock_id = stock.stock_id "
                    + "INNER JOIN part "
                    + "ON temp_order.part_id = part.part_id ";
            pst = (PreparedStatement) con.prepareStatement(query);

            rs = pst.executeQuery();

            while (rs.next()) {
                Stock stock = new Stock();
                stock.setStockID(rs.getString(1));
                stock.setPartID(rs.getString(2));
                stock.setQuantity(rs.getInt(3));
                //stock.setStockID(rs.getString(4));
                //stock.setPartID(rs.getString(5));
                stock.setDealerID(rs.getString(6));
                stock.setCost(rs.getDouble(7));
                stock.setPrice(rs.getDouble(8));
                stock.setDiscount(rs.getDouble(9));
                //stock.setQuantity(rs.getInt(10));
                stock.setAddDate(rs.getDate(11));
                //stock.setPartID(rs.getString(12));
                stock.setPartName(rs.getString(13));
                stock.setPartType(rs.getString(14));
                stock.setPartModel(rs.getString(15));
                stock.setPartStock(rs.getString(16));

                list.add(stock);
            }

            return list;
        } catch (Exception e) {
            System.out.println("getOrders() -> " + e);
            return null;
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
            }
        }
    }
    
    /**
     * add a new order to service table
     *
     * @param service
     * @return
     */
    boolean addService(Service service) {
        try {
            con = (Connection) DriverManager.getConnection(url, username, password); //get connection
            String query = "INSERT INTO service "
                    + "(service_id, stock_id, quantity, price) "
                    + "VALUES (?, ?, ?, ?)";
            pst = (PreparedStatement) con.prepareStatement(query);

            //assign values to the query
            pst.setString(1, service.getServiceID());
            pst.setString(2, service.getStockID());
            pst.setInt(3, service.getQuantity());
            pst.setDouble(4, service.getDiscountPrice());
            
            //execute the query
            pst.executeUpdate();

            return true;

        } catch (Exception e) {
            System.out.println(e);
            return false;
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
            }
        }
    }
    
    /**
     * add a new payment to payment table
     *
     * @param payment
     * @return
     */
    boolean addPayment(Payment payment) {
        try {
            con = (Connection) DriverManager.getConnection(url, username, password); //get connection
            String query = "INSERT INTO payment "
                    + "(service_id, total, payment, balance, type, cheque_no, bank, date, next_service) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            pst = (PreparedStatement) con.prepareStatement(query);

            //assign values to the query
            pst.setString(1, payment.getServiceID());
            pst.setDouble(2, payment.getTotal());
            pst.setDouble(3, payment.getPayment());
            pst.setDouble(4, payment.getBalance());
            pst.setString(5, payment.getType());
            pst.setString(6, payment.getChequeNo());
            pst.setString(7, payment.getBank());
            
            //convert util date to sql date
            java.util.Date date = new java.util.Date(payment.getDate().getTime());
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
            
            pst.setDate(8, sqlDate);
            pst.setString(9, payment.getNextService());
            
            //execute the query
            pst.executeUpdate();

            return true;

        } catch (Exception e) {
            System.out.println(e);
            return false;
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
            }
        }
    }

    //* ==========  B E L O W  F U N C T I O N S  F O R  Q U O T A T I O N  T A B  ========== *\\
    /**
     * check the parameter dealer id is in the stock table if mentioned dealer
     * id is in the stock table then return true else return false
     *
     * @param dealer
     * @return
     */
    boolean checkCustomerInQuotationTable(Customer customer) {
        try {
            con = (Connection) DriverManager.getConnection(url, username, password); //get connection
            String query = "SELECT * FROM quotation WHERE customer_id = '" + customer.getId() + "'";
            pst = (PreparedStatement) con.prepareStatement(query);

            rs = pst.executeQuery();

            int count = 0;
            while (rs.next()) {
                count += 1;
            }

            if (count > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            System.out.println(e);
            return true;
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
            }
        }
    }

    /**
     * Add a new customer
     *
     * @param customer
     * @return
     */
    boolean addCustomer(Customer customer) {
        try {
            con = (Connection) DriverManager.getConnection(url, username, password); //get connection
            String query = "INSERT INTO customer "
                    + "(customer_id, customer_name, address, contact, email) "
                    + "VALUES (?, ?, ?, ?, ?)";
            pst = (PreparedStatement) con.prepareStatement(query);

            //assign values to the query
            pst.setString(1, customer.getId());
            pst.setString(2, customer.getName());
            pst.setString(3, customer.getAddress());
            pst.setString(4, customer.getContact());
            pst.setString(5, customer.getEmail());

            //execute the query
            pst.executeUpdate();

            return true;

        } catch (Exception e) {
            System.out.println("addCustomer() -> " + e);
            return false;
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
            }
        }
    }

    /**
     * Add selected customer
     *
     * @param customer
     * @return
     */
    boolean addSelectedCustomer(Customer customer) {
        try {
            con = (Connection) DriverManager.getConnection(url, username, password); //get connection
            String query = "INSERT INTO selected_customer "
                    + "(customer_id, customer_name, address, contact, email) "
                    + "VALUES (?, ?, ?, ?, ?)";
            pst = (PreparedStatement) con.prepareStatement(query);

            //assign values to the query
            pst.setString(1, customer.getId());
            pst.setString(2, customer.getName());
            pst.setString(3, customer.getAddress());
            pst.setString(4, customer.getContact());
            pst.setString(5, customer.getEmail());

            //execute the query
            pst.executeUpdate();

            return true;

        } catch (Exception e) {
            System.out.println("addSelectedCustomer() -> " + e);
            return false;
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
            }
        }
    }

    /**
     * before adding an instance to selected customer table check whether there
     * is already added one. If it is then return true. Else return false
     *
     * @param cutomer
     * @return
     */
    boolean isEmptySelectedCustomer() {
        try {
            con = (Connection) DriverManager.getConnection(url, username, password); //get connection
            String query = "SELECT * FROM selected_customer ";
            pst = (PreparedStatement) con.prepareStatement(query);

            rs = pst.executeQuery();

            int count = 0;
            while (rs.next()) {
                count += 1;
            }

            if (count > 0) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            System.out.println(e);
            return true;
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
            }
        }
    }

    /**
     * when add and update a new customer or already exists customer, check that
     * customer details already exists the system. If it is then return true.
     * Else return false
     *
     * @param cutomer
     * @return
     */
    boolean isCustomerExists(Customer customer) {
        try {
            con = (Connection) DriverManager.getConnection(url, username, password); //get connection
            String query = "SELECT * FROM customer "
                    + "WHERE customer_name = '" + customer.getName() + "' AND "
                    + "address = '" + customer.getAddress() + "' AND "
                    + "contact = '" + customer.getContact() + "' AND "
                    + "email = '" + customer.getEmail() + "'";
            pst = (PreparedStatement) con.prepareStatement(query);

            rs = pst.executeQuery();

            int count = 0;
            while (rs.next()) {
                count += 1;
            }

            if (count > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            System.out.println(e);
            return true;
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
            }
        }
    }

    /**
     * this return all the data of the customer table as array list
     *
     * @return
     */
    ArrayList<Customer> getCustomers() {
        try {
            ArrayList<Customer> list = new ArrayList<Customer>();

            con = (Connection) DriverManager.getConnection(url, username, password); //get connection
            String query = "SELECT * FROM customer";
            pst = (PreparedStatement) con.prepareStatement(query);

            rs = pst.executeQuery();

            while (rs.next()) {
                Customer customer = new Customer();
                customer.setId(rs.getString(1));
                customer.setName(rs.getString(2));
                customer.setAddress(rs.getString(3));
                customer.setContact(rs.getString(4));
                customer.setEmail(rs.getString(5));

                list.add(customer);
            }

            return list;
        } catch (Exception e) {
            System.out.println("getCustomers() -> " + e);
            return null;
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
            }
        }
    }

    /**
     * when add and update a new customerer or already exists customer, check
     * that customer details already exists the system. If it is then return
     * true. Else return false
     *
     * @param customer
     * @return
     */
    boolean updateCustomer(Customer customer) {
        try {
            con = (Connection) DriverManager.getConnection(url, username, password); //get connection
            String query = "UPDATE customer "
                    + "SET customer_name = '" + customer.getName() + "', "
                    + "address = '" + customer.getAddress() + "', "
                    + "contact = '" + customer.getContact() + "', "
                    + "email = '" + customer.getEmail() + "' "
                    + "WHERE customer_id = '" + customer.getId() + "'";
            pst = (PreparedStatement) con.prepareStatement(query);
            pst.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
            }
        }
    }

    /**
     * This use to search record of customer table
     *
     * @param columns
     * @param values
     * @return
     */
    ArrayList<Customer> searchCustomer(String[] columns, String[] values) {
        try {
            ArrayList<Customer> list = new ArrayList<Customer>();

            // below if statements check what query should execute
            String query = "";
            if (columns.length == 1) {
                query = "SELECT * FROM customer WHERE " + columns[0] + " = '" + values[0] + "'";
            } else if (columns.length == 2) {
                query = "SELECT * FROM customer WHERE " + columns[0] + " = '" + values[0] + "' AND "
                        + columns[1] + " = '" + values[1] + "'";
            }

            con = (Connection) DriverManager.getConnection(url, username, password); //get connection
            pst = (PreparedStatement) con.prepareStatement(query);

            rs = pst.executeQuery();

            while (rs.next()) {
                Customer c = new Customer();
                c.setId(rs.getString(1));
                c.setName(rs.getString(2));
                c.setAddress(rs.getString(3));
                c.setContact(rs.getString(4));
                c.setEmail(rs.getString(5));

                list.add(c); //add part object to the array list
            }

            return list;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
            }
        }
    }

    Customer getSelectedCustomer() {
        try {

            // below if statements check what query should execute
            String query = "SELECT * FROM selected_customer";

            con = (Connection) DriverManager.getConnection(url, username, password); //get connection
            pst = (PreparedStatement) con.prepareStatement(query);

            rs = pst.executeQuery();

            Customer c = new Customer();
            while (rs.next()) {

                c.setId(rs.getString(1));
                c.setName(rs.getString(2));
                c.setAddress(rs.getString(3));
                c.setContact(rs.getString(4));
                c.setEmail(rs.getString(5));

            }

            return c;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
            }
        }
    }

    boolean clearSelectedCustomer() {
        try {
            con = (Connection) DriverManager.getConnection(url, username, password); //get connection
            String query = "TRUNCATE TABLE selected_customer";
            pst = (PreparedStatement) con.prepareStatement(query);
            pst.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
            }
        }
    }

    /**
     * add a new stock
     *
     * @param stock
     * @return
     */
    boolean addSelectedParts(Stock stock, int qty) {
        try {
            con = (Connection) DriverManager.getConnection(url, username, password); //get connection
            String query = "INSERT INTO selected_parts "
                    + "(stock_id, part_id, quantity, price) "
                    + "VALUES (?, ?, ?, ?)";
            pst = (PreparedStatement) con.prepareStatement(query);

            //convert util date to sql date
            //java.util.Date date = new java.util.Date(stock.getAddDate().getTime());
            //java.sql.Date sqlDate = new java.sql.Date(date.getTime());
            //assign values to the query
            Double pri = stock.getPrice() * (1 - stock.getDiscount() / 100) * qty;

            pst.setString(1, stock.getStockID());
            pst.setString(2, stock.getPartID());
            pst.setInt(3, qty);
            pst.setDouble(4, pri);

            //execute the query
            pst.executeUpdate();

            return true;

        } catch (Exception e) {
            System.out.println(e);
            return false;
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
            }
        }
    }

    boolean isQtyEnough(Stock stock, int qty) {
        try {

            String query = "SELECT * FROM selected_parts WHERE stock_id = '" + stock.getStockID() + "'";
            con = (Connection) DriverManager.getConnection(url, username, password); //get connection
            pst = (PreparedStatement) con.prepareStatement(query);

            rs = pst.executeQuery();

            int addedQty = 0;
            while (rs.next()) {

                addedQty += rs.getInt(3);
            }

            /*int addedQty = Integer.parseInt(rs.getString(2));*/
            int stockQty = stock.getQuantity();

            if (addedQty + qty <= stockQty) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            System.out.println(e);
            return false;
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
            }
        }
    }

    boolean increaseQty(Stock stock, int qty) {
        try {
            con = (Connection) DriverManager.getConnection(url, username, password); //get connection
            String query = "UPDATE selected_parts "
                    + "SET quantity = quantity + '" + qty + "', "
                    + "price = price + '" + stock.getPrice() * (1 - (stock.getDiscount() / 100)) * qty + "' "
                    + "WHERE stock_id = '" + stock.getStockID() + "'";
            pst = (PreparedStatement) con.prepareStatement(query);
            pst.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
            }
        }
    }

    boolean isSelectedPart(String st_id) {
        try {
            con = (Connection) DriverManager.getConnection(url, username, password); //get connection
            String query = "SELECT * FROM selected_parts "
                    + "WHERE stock_id = '" + st_id + "'";
            pst = (PreparedStatement) con.prepareStatement(query);

            rs = pst.executeQuery();

            int count = 0;
            while (rs.next()) {
                count += 1;
            }

            if (count > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            System.out.println(e);
            return true;
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
            }
        }
    }

    /**
     * this return all the data of the customer table as array list
     *
     * @return
     */
    ArrayList<SelectedParts> getSelectedParts() {
        try {
            ArrayList<SelectedParts> list = new ArrayList<SelectedParts>();

            con = (Connection) DriverManager.getConnection(url, username, password); //get connection
            String query = "SELECT selected_parts.stock_id, part_name, type, model, selected_parts.quantity, stock.price, stock.discount, selected_parts.price "
                    + "FROM selected_parts "
                    + "INNER JOIN stock ON selected_parts.stock_id = stock.stock_id "
                    + "INNER JOIN part ON selected_parts.part_id = part.part_id";
            pst = (PreparedStatement) con.prepareStatement(query);

            rs = pst.executeQuery();

            while (rs.next()) {
                SelectedParts sp = new SelectedParts();
                sp.setStock_id(rs.getString(1));
                sp.setPart_name(rs.getString(2));
                sp.setType(rs.getString(3));
                sp.setModel(rs.getString(4));
                sp.setQuantity(rs.getInt(5));
                sp.setUnit_price(rs.getDouble(6));
                sp.setWith_discount(rs.getDouble(6) * (1 - rs.getDouble(7) / 100));
                sp.setValue(sp.getWith_discount() * sp.getQuantity());

                list.add(sp);

            }

            return list;
        } catch (Exception e) {
            System.out.println("getSelectedParts() -> " + e);
            return null;
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
            }
        }
    }

    Double getQuotationTotal() {
        try {
            con = (Connection) DriverManager.getConnection(url, username, password); //get connection
            String query = "SELECT * FROM selected_parts";
            pst = (PreparedStatement) con.prepareStatement(query);

            rs = pst.executeQuery();

            Double total = 0.0;
            while (rs.next()) {
                total += rs.getDouble(4);
            }

            return total;
        } catch (Exception e) {
            System.out.println("getQuotationTotal() -> " + e);
            return null;
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
            }
        }
    }

    boolean clearSelectedParts() {
        try {
            con = (Connection) DriverManager.getConnection(url, username, password); //get connection
            String query = "TRUNCATE TABLE selected_parts";
            pst = (PreparedStatement) con.prepareStatement(query);
            pst.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
            }
        }
    }

    boolean changeQty(SelectedParts sp, int qty) {
        try {
            con = (Connection) DriverManager.getConnection(url, username, password); //get connection
            String query = "UPDATE selected_parts "
                    + "SET quantity = '" + qty + "', "
                    + "price = '" + sp.getWith_discount() * qty + "' "
                    + "WHERE stock_id = '" + sp.getStock_id() + "'";
            pst = (PreparedStatement) con.prepareStatement(query);
            pst.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
            }
        }
    }

    boolean addService(SelectedParts sp, String service_id) {
        try {
            con = (Connection) DriverManager.getConnection(url, username, password); //get connection
            String query = "INSERT INTO service "
                    + "(service_id, stock_id, quantity, price) "
                    + "VALUES (?, ?, ?, ?)";
            pst = (PreparedStatement) con.prepareStatement(query);

            //assign values to the query
            pst.setString(1, service_id);
            pst.setString(2, sp.getStock_id());
            pst.setInt(3, sp.getQuantity());
            pst.setDouble(4, sp.getValue());

            //execute the query
            pst.executeUpdate();

            return true;

        } catch (Exception e) {
            System.out.println("addService() -> " + e);
            return false;
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
            }
        }
    }

    boolean isEmptySelectedParts() {
        try {
            con = (Connection) DriverManager.getConnection(url, username, password); //get connection
            String query = "SELECT * FROM selected_parts ";
            pst = (PreparedStatement) con.prepareStatement(query);

            rs = pst.executeQuery();

            int count = 0;
            while (rs.next()) {
                count += 1;
            }

            if (count == 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            System.out.println(e);
            return true;
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
            }
        }
    }

    boolean addQuotation(Quotation qtn) {
        try {
            con = (Connection) DriverManager.getConnection(url, username, password); //get connection
            String query = "INSERT INTO quotation "
                    + "(service_id, customer_id, service_charge, total, date, username) "
                    + "VALUES (?, ?, ?, ?, ?, ?)";
            pst = (PreparedStatement) con.prepareStatement(query);

            //assign values to the query
            pst.setString(1, qtn.getService_id());
            pst.setString(2, qtn.getCustomer_id());
            pst.setDouble(3, qtn.getService_charge());
            pst.setDouble(4, qtn.getTotal());
            pst.setString(5, qtn.getDate());
            pst.setString(6, qtn.getUsername());

            //execute the query
            pst.executeUpdate();

            return true;

        } catch (Exception e) {
            System.out.println("addQuotation() -> " + e);
            return false;
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
            }
        }
    }

    /**
     * get stock by searching stock ID
     *
     * @param id
     * @return
     */
    Stock getStockByID(String id) {
        // ArrayList<Stock> list = new ArrayList<Stock>();
        try {
            con = (Connection) DriverManager.getConnection(url, username, password); //get connection
            String query = "SELECT * FROM stock WHERE stock_id = '" + id + "'";
            pst = (PreparedStatement) con.prepareStatement(query);

            rs = pst.executeQuery();
            Stock st = new Stock();
            while (rs.next()) {

                st.setStockID(rs.getString(1));
                st.setPartID(rs.getString(2));
                st.setDealerID(rs.getString(3));
                st.setCost(rs.getDouble(4));
                st.setPrice(rs.getDouble(5));
                st.setDiscount(rs.getInt(6));
                st.setQuantity(rs.getInt(7));
                st.setAddDate(rs.getDate(8));

            }
            return st;

        } catch (Exception e) {
            System.out.println("getStockByID() -> " + e);
            return null;
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
            }
        }
    }
}
