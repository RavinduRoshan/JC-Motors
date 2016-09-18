/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcmotors;

import com.jtattoo.plaf.hifi.HiFiLookAndFeel;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

/**
 *
 * @author Dhanushka
 */
public class UpdateStock extends javax.swing.JFrame {

    DBOperations dbOps = new DBOperations();

    /**
     * Creates new form UpdateStock
     */
    public UpdateStock() {
        initComponents();
        this.setLocationRelativeTo(null);
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("icon.png")));
        limitTextField(); //limit the cost cent and price cent text fields

        /**
         * Execute an action when an item on the combo box is selected [closed]
         * Create an ActionListener for the JComboBox component. Give the part
         * name applicable to selected part id of ddPartID combo box That part
         * name assign to txtPartName text field
         */
        ddPartID.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                // Get the source of the component, which is our combo box.
                JComboBox comboBox = (JComboBox) event.getSource();

                Object selected = comboBox.getSelectedItem(); //get the combo box selected item
                //search the part name according to selected part id
                String partName = dbOps.searchStringRecord("part_name", "part_id", selected.toString(), "part");
                if (partName != null) {
                    txtPartName.setText(partName);
                } else {
                    txtPartName.setText("null");
                }
            }
        });

        /**
         * Execute an action when an item on the combo box is selected [closed]
         * Create an ActionListener for the JComboBox component. Give the dealer
         * name applicable to selected dealer id of ddDealerID combo box That
         * dealer name assign to txtDealerName text field
         */
        ddDealerID.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                // Get the source of the component, which is our combo box.
                JComboBox comboBox = (JComboBox) event.getSource();

                Object selected = comboBox.getSelectedItem(); //get the combo box selected item
                //search the part name according to selected part id
                String partName = dbOps.searchStringRecord("dealer_name", "dealer_id", selected.toString(), "dealer");
                if (partName != null) {
                    txtDealerName.setText(partName);
                } else {
                    txtDealerName.setText("null");
                }
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel9 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        txtStockID = new javax.swing.JTextField();
        ddDealerID = new javax.swing.JComboBox();
        txtCostRs = new javax.swing.JTextField();
        txtPriceRs = new javax.swing.JTextField();
        txtDiscount = new javax.swing.JTextField();
        txtQuantity = new javax.swing.JTextField();
        btnUpdate = new javax.swing.JButton();
        txtPartName = new javax.swing.JTextField();
        txtDealerName = new javax.swing.JTextField();
        txtPriceCent = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        txtCostCent = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        ddDate = new org.freixas.jcalendar.JCalendarCombo();
        ddPartID = new javax.swing.JComboBox();
        jLabel28 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Update A Stock");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(182, 182, 182)), " update a stock", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 12))); // NOI18N
        jPanel9.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        jPanel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel12.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("Stock ID");
        jPanel9.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 90, -1));

        jLabel15.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel15.setText("Cost");
        jPanel9.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 200, 90, -1));

        jLabel16.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel16.setText("Price");
        jPanel9.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 230, 90, -1));

        jLabel17.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel17.setText("Discount");
        jPanel9.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 260, 90, -1));

        jLabel18.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel18.setText("Quantity");
        jPanel9.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 290, 90, -1));

        txtStockID.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtStockID.setEnabled(false);
        jPanel9.add(txtStockID, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 50, 230, 20));

        ddDealerID.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        ddDealerID.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "dealer 1", "dealer 2", "dealer 3", "dealer 4", "dealer 5" }));
        jPanel9.add(ddDealerID, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 140, 230, -1));

        txtCostRs.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtCostRs.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jPanel9.add(txtCostRs, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 200, 180, 20));

        txtPriceRs.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPriceRs.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jPanel9.add(txtPriceRs, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 230, 180, 20));

        txtDiscount.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtDiscount.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jPanel9.add(txtDiscount, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 260, 200, 20));

        txtQuantity.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jPanel9.add(txtQuantity, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 290, 230, 20));

        btnUpdate.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnUpdate.setText("Update");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });
        jPanel9.add(btnUpdate, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 320, 100, -1));

        txtPartName.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPartName.setEnabled(false);
        jPanel9.add(txtPartName, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 110, 230, 20));

        txtDealerName.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtDealerName.setEnabled(false);
        jPanel9.add(txtDealerName, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 170, 230, 20));

        txtPriceCent.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPriceCent.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtPriceCent.setText("00");
        jPanel9.add(txtPriceCent, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 230, 30, 20));

        jLabel25.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel25.setText("%");
        jPanel9.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 260, 30, 20));

        jLabel26.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel26.setText(".");
        jPanel9.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 200, 20, 20));

        txtCostCent.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtCostCent.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtCostCent.setText("00");
        jPanel9.add(txtCostCent, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 200, 30, 20));

        jLabel27.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel27.setText(".");
        jPanel9.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 230, 20, 20));

        jLabel13.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText("Part ID");
        jPanel9.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 90, -1));

        jLabel14.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel14.setText("Dealer ID");
        jPanel9.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, 90, -1));

        jLabel19.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel19.setText("Date");
        jPanel9.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 90, -1));

        ddDate.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jPanel9.add(ddDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 20, 230, -1));

        ddPartID.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        ddPartID.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "part 1", "part 2", "part 3", "part 4", "part 5" }));
        jPanel9.add(ddPartID, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 80, 230, -1));

        jLabel28.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel28.setText("Part Name");
        jPanel9.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 90, -1));

        jLabel24.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel24.setText("Dealer Name");
        jPanel9.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, 90, -1));

        getContentPane().add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 380, 360));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * load all drop downs
     */
    void loadAllDropDowns() {

        /**
         * this for Part Management tab get values to the drop downs from "part"
         * table
         */
        String[] partID = dbOps.dropDownManage("part_id", "part");
        String[] dealerID = dbOps.dropDownManage("dealer_id", "dealer");

        if (partID != null) {   // this validation for, when the dropDownManage() throws exception
            ddPartID.setModel(new javax.swing.DefaultComboBoxModel(partID));
        }
        if (dealerID != null) {   // this validation for, when the dropDownManage() throws exception
            ddDealerID.setModel(new javax.swing.DefaultComboBoxModel(dealerID));
        }
    }

    /**
     * Set the values to the fields
     *
     * @param stock
     */
    void setFields(Stock stock) {
        txtStockID.setText(stock.getStockID());
        txtPartName.setText(stock.getPartName());
        txtDealerName.setText(dbOps.searchStringRecord("dealer_name", "dealer_id", stock.getDealerID(), "dealer"));
        ddDate.setDate(stock.getAddDate());
        ddPartID.setSelectedItem(stock.getPartID());
        ddDealerID.setSelectedItem(stock.getDealerID());

        //create cost for text fields 
        double cost = stock.getCost();
        String stCost = cost + "";
        String[] aCost = stCost.split("\\.");

        txtCostRs.setText(aCost[0]);
        txtCostCent.setText(aCost[1]);

        //create price for text fields 
        double price = stock.getPrice();
        String stPrice = price + "";
        String[] aPrice = stPrice.split("\\.");

        txtPriceRs.setText(aPrice[0]);
        txtPriceCent.setText(aPrice[1]);

        txtDiscount.setText(stock.getDiscount() + "");
        txtQuantity.setText(stock.getQuantity() + "");
    }

    //set the text field length
    void limitTextField() {

        txtCostCent.setDocument(new TextField(2));
        txtCostCent.setText("00");

        txtPriceCent.setDocument(new TextField(2));
        txtPriceCent.setText("00");
    }

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        try {
            //create new Part and assign date
            Stock stock = new Stock();
            stock.setStockID(txtStockID.getText());
            stock.setPartID(ddPartID.getSelectedItem().toString());
            stock.setDealerID(ddDealerID.getSelectedItem().toString());

            String stCost = txtCostRs.getText() + "." + txtCostCent.getText();
            double cost = Double.parseDouble(stCost); // in here can be throws a exception
            stock.setCost(cost);

            String stPrice = txtPriceRs.getText() + "." + txtPriceCent.getText();
            double price = Double.parseDouble(stPrice); // in here can be throws a exception
            stock.setPrice(price);

            stock.setDiscount(Double.parseDouble(txtDiscount.getText())); // in here can be throws a exception
            stock.setQuantity(Integer.parseInt(txtQuantity.getText())); // in here can be throws a exception
            stock.setAddDate(ddDate.getDate()); // in here can be throws a exception

            //update above stock to the db
            boolean result = dbOps.updateStock(stock);

            if (result) {
                JOptionPane.showMessageDialog(this, "Successfully Updated!");
                this.dispose(); //current window will disappear
            } else {
                JOptionPane.showMessageDialog(this, "Error occured!");
            }

        } catch (Exception e) {
            System.out.println("button update --> " + e);
            JOptionPane.showMessageDialog(this, "Please enter correct details!");
        }
    }//GEN-LAST:event_btnUpdateActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(UpdateStock.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(UpdateStock.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(UpdateStock.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(UpdateStock.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(new HiFiLookAndFeel());
                } catch (Exception e) {
                }
                new UpdateStock().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnUpdate;
    private org.freixas.jcalendar.JCalendarCombo ddDate;
    private javax.swing.JComboBox ddDealerID;
    private javax.swing.JComboBox ddPartID;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JTextField txtCostCent;
    private javax.swing.JTextField txtCostRs;
    private javax.swing.JTextField txtDealerName;
    private javax.swing.JTextField txtDiscount;
    private javax.swing.JTextField txtPartName;
    private javax.swing.JTextField txtPriceCent;
    private javax.swing.JTextField txtPriceRs;
    private javax.swing.JTextField txtQuantity;
    private javax.swing.JTextField txtStockID;
    // End of variables declaration//GEN-END:variables
}