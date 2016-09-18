/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcmotors;

import com.jtattoo.plaf.hifi.HiFiLookAndFeel;//
import java.awt.Toolkit;//
import java.awt.event.ActionEvent;//
import java.awt.event.ActionListener;//
import java.awt.event.WindowEvent;//
import java.awt.event.WindowListener;//
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import javax.swing.JComboBox;//
import javax.swing.JOptionPane;//
import javax.swing.JTable;//
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import javax.swing.table.DefaultTableCellRenderer;
//import javax.swing.table.TableCellRenderer;//

/**
 *
 * @author Dhanushka
 */
public class MainFrame extends javax.swing.JFrame {

    DBOperations dbOps = new DBOperations();
    IDCreations idc = new IDCreations();
    Calculations cal = new Calculations();
    ArrayList<Part> pList;
    ArrayList<Stock> sList, soList; //sList : stock table, soList : stock order table
    ArrayList<Dealer> dList;
    ArrayList<Service> serList;
    double purchasesTotal = 0;
    double serviceCharge = 0;
    double payment = 0;
    double balance = 0;
    ArrayList<SelectedParts> spList;
    public double svc_chrge = 0.00;
    public boolean flag = true;

    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        initComponents();
        this.setLocationRelativeTo(null);
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("icon.png")));
        loadAllTables(); //load all tables
        loadDropDowns(); //load all dropdowns to the main frame part management
        limitTextField(); //limit the text field stock management
        panelVisibility(); //set the visibility of cheque panel, cash panel and default panel in purchases tab

        //create a new part id for next new part
        int partRows = dbOps.checkTheTableEmpty("part"); //check the no of row of table
        if (partRows == 0) {
            txtPartId.setText("JC00001");
        } else if (partRows > 0) {
            String partID = dbOps.getMaxRecord("part_id", "part"); //get the last part id
            if (partID != null) {
                partID = idc.createPartID(partID);
                txtPartId.setText(partID); //set new part id to the add section text field in Part Management tab
            } else {
                txtPartId.setText("XXXXXXX");
            }
        } else {
            txtPartId.setText("XXXXXXX");
        }

        // create new stock id for next part in stock management tab
        int stockRows = dbOps.checkTheTableEmpty("stock");//check the no of row of table
        if (stockRows == 0) {
            txtStockID.setText("S000000001");
        } else if (stockRows > 0) {
            String stockID = dbOps.getMaxRecord("stock_id", "stock"); //get the last stock id
            if (stockID != null) {
                stockID = idc.createStockID(stockID);
                txtStockID.setText(stockID); //set new stock id to the add section text field
            } else {
                txtStockID.setText("XXXXXXXXXX");
            }
        } else {
            txtStockID.setText("XXXXXXXXXX");
        }

        /**
         * Execute an action when an item on the combo box is selected [closed]
         * Create an ActionListener for the JComboBox component. Give the part
         * name applicable to selected part id of ddStockPartID combo box That
         * part name assign to txtStockPartName text field
         */
        ddStockPartID.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                // Get the source of the component, which is our combo box.
                JComboBox comboBox = (JComboBox) event.getSource();

                Object selected = comboBox.getSelectedItem(); //get the combo box selected item
                //search the part name according to selected part id
                String partName = dbOps.searchStringRecord("part_name", "part_id", selected.toString(), "part");
                if (partName != null) {
                    txtStockPartName.setText(partName);
                } else {
                    txtStockPartName.setText("null");
                }
            }
        });

        /**
         * Execute an action when an item on the combo box is selected [closed]
         * Create an ActionListener for the JComboBox component. Give the dealer
         * name applicable to selected dealer id of ddStockDealerID combo box.
         * That dealer name assign to txtStockDealerName text field
         */
        ddStockDealerID.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                // Get the source of the component, which is our combo box.
                JComboBox comboBox = (JComboBox) event.getSource();

                Object selected = comboBox.getSelectedItem(); //get the combo box selected item
                //search the part name according to selected part id
                String partName = dbOps.searchStringRecord("dealer_name", "dealer_id", selected.toString(), "dealer");
                if (partName != null) {
                    txtStockDealerName.setText(partName);
                } else {
                    txtStockDealerName.setText("null");
                }
            }
        });

        /**
         * Execute an action when an item on the combo box is selected [closed]
         * Create an ActionListener for the JComboBox component. Give the dealer
         * name applicable to selected dealer id of ddStockDealerID combo box.
         * That dealer name assign to txtStockDealerName text field
         */
        ddPaymentType.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                // Get the source of the component, which is our combo box.
                JComboBox comboBox = (JComboBox) event.getSource();
                String select = ddPaymentType.getSelectedItem().toString();
                Object selected = comboBox.getSelectedItem(); //get the combo box selected item
                if (select.equals("Cash")) {

                    pnlPurchasesCash.setVisible(true);
                    pnlPurchasesCheque.setVisible(false);
                    pnlPurchasesDefault.setVisible(false);

                } else if (select.equals("Cheque")) {
                    pnlPurchasesCash.setVisible(false);
                    pnlPurchasesCheque.setVisible(true);
                    pnlPurchasesDefault.setVisible(false);
                } else {
                    pnlPurchasesCash.setVisible(false);
                    pnlPurchasesCheque.setVisible(false);
                    pnlPurchasesDefault.setVisible(true);
                }
            }
        });
    }

    /**
     * set the visibility of cheque panel, cash panel and default panel in
     * purchases tab
     */
    void panelVisibility() {
        pnlPurchasesCash.setVisible(false);
        pnlPurchasesCheque.setVisible(false);
        pnlPurchasesDefault.setVisible(true);
    }

    /**
     * This method load all tables
     */
    void loadAllTables() {
        loadTablePart(); //load part table to the main frame part management and stock management
        loadTableStock(); //load stock table to the main frame stock management
        loadTableStockDealer(); //load stock dealer table to the main frame stock management
        loadTableStockPart(); //load stock part table to the main frame stock management
        loadTablePurchasesStock(); //load purchases stock table to the main frame purchases
        loadTablePurchasesOrder(); //load purchases order table to the main frame purchases
        loadTableQuotationStock();
        loadTextFields();
        loadTableQuotation();
    }

    void loadTableQuotation() {
        spList = dbOps.getSelectedParts();
        if (spList != null) {    // this validation for, when the getStocks() throws exception
            TableQuotationParts spDetails = new TableQuotationParts(spList);
            tblQuotationParts.setModel(spDetails);
        }
    }
 
    /**
     * This method load data to the tblParts table in part management tab
     */
    void loadTablePart() {
        pList = dbOps.getParts();
        if (pList != null) {    // this validation for, when the getParts() throws exception
            TableParts pDetails = new TableParts(pList);
            tblParts.setModel(pDetails);
        }
    }

    /**
     * This method load data to the tblParts table in part management tab
     */
    void loadTableStockPart() {
        pList = dbOps.getParts();
        if (pList != null) {    // this validation for, when the getParts() throws exception
            TableStockParts pDetails = new TableStockParts(pList);
            tblStockPart.setModel(pDetails);
        }
    }

    /**
     * This method load data to the tblStock table in stock management tab
     */
    void loadTableStock() {
        sList = dbOps.getStocks();
        if (sList != null) {    // this validation for, when the getStocks() throws exception
            TableStock sDetails = new TableStock(sList);
            tblStock.setModel(sDetails);
        }
    }

    /**
     * This method load data to the tblStockDealer table in stock management tab
     */
    void loadTableStockDealer() {
        dList = dbOps.getDealers();
        if (dList != null) {    // this validation for, when the getDealers() throws exception
            TableStockDealer dDetails = new TableStockDealer(dList);
            tblStockDealer.setModel(dDetails);

            //Set the table column size
            tblStockDealer.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
            tblStockDealer.getColumnModel().getColumn(0).setPreferredWidth(80);
            tblStockDealer.getColumnModel().getColumn(1).setPreferredWidth(178);

            //Set the table column allignment
            DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
            rightRenderer.setHorizontalAlignment(SwingConstants.CENTER);
            tblStockDealer.getColumnModel().getColumn(0).setCellRenderer(rightRenderer);
        }
    }

    /**
     * This method load data to the tblPurchasesStock table in Purchases tab
     */
    void loadTablePurchasesStock() {
        sList = dbOps.getStocks();
        if (sList != null) {    // this validation for, when the getStocks() throws exception
            TablePurchasesStock psDetails = new TablePurchasesStock(sList);
            tblPurchasesStock.setModel(psDetails);
        }
    }

    /**
     * This method load data to the tblPurchasesStock table in Purchases tab
     */
    void loadTablePurchasesOrder() {
        soList = dbOps.getOrders();
        if (soList != null) {    // this validation for, when the getStocks() throws exception
            TableOrder oDetails = new TableOrder(soList);
            tblPurchasesOrder.setModel(oDetails);
        }

        //calculate order table price total
        purchasesTotal = 0;
        double price = 0;
        double discount = 0;
        double qty = 0;
        for (int i = 0; i < soList.size(); i++) {
            price = soList.get(i).getPrice();
            discount = soList.get(i).getDiscount();
            qty = soList.get(i).getQuantity();

            purchasesTotal = ((price * (100 - discount)) / 100) * qty + purchasesTotal;
        }

        purchasesTotal += serviceCharge; // to add the service charge 
        lblTotal.setText("Total Rs.  " + purchasesTotal);

        /*
         //set ListSelectionListener to get the quantity of selected row of order table
         tblPurchasesOrder.setRowSelectionAllowed(true);
         ListSelectionModel cellSelectionModel = tblPurchasesOrder.getSelectionModel();
         cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

         cellSelectionModel.addListSelectionListener(new ListSelectionListener() {

         @Override
         public void valueChanged(ListSelectionEvent e) {
         String selectedData = null;

         int selectedRow = tblPurchasesOrder.getSelectedRow();

         selectedData = tblPurchasesOrder.getValueAt(selectedRow, 4).toString();

         //System.out.println("Selected: " + selectedData);
         spnPurchasesUpdateQuantity.setValue(new Integer(selectedData));
         }

         });*/
    }

    /**
     * This method load data to the tblParts table in part management tab
     */
    void loadTableQuotationStock() {
        sList = dbOps.getStocks();
        if (sList != null) {    // this validation for, when the getStocks() throws exception
            TableQuotationStock psDetails = new TableQuotationStock(sList);
            tblQuotationStocks.setModel(psDetails);

            //set a button for last column of the table
            //TableButton buttonEditor = new TableButton();
            //tblStockStock1.getColumnModel().getColumn(7).setCellRenderer(new TButton());
            //set custom editor to teams column
            //tblStockStock1.getColumnModel().getColumn(7).setCellEditor(new ButtonEditor(new JTextField()));
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtPartId = new javax.swing.JTextField();
        txtPartName = new javax.swing.JTextField();
        btnAddPart = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        ddAddStock = new javax.swing.JComboBox();
        ddAddType = new javax.swing.JComboBox();
        ddAddModel = new javax.swing.JComboBox();
        jPanel7 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        btnSearchPart = new javax.swing.JButton();
        ddPartID = new javax.swing.JComboBox();
        ddName = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        ddType = new javax.swing.JComboBox();
        ddModel = new javax.swing.JComboBox();
        ddStock = new javax.swing.JComboBox();
        cbPartID = new javax.swing.JCheckBox();
        cbPartName = new javax.swing.JCheckBox();
        cbType = new javax.swing.JCheckBox();
        cbModel = new javax.swing.JCheckBox();
        cbStock = new javax.swing.JCheckBox();
        btnViewAllParts = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        btnUpdatePart = new javax.swing.JButton();
        btnDeletePart = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblParts = new javax.swing.JTable();
        jLabel11 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        txtStockID = new javax.swing.JTextField();
        ddStockDealerID = new javax.swing.JComboBox();
        txtStockCostRs = new javax.swing.JTextField();
        txtStockPriceRs = new javax.swing.JTextField();
        txtStockDiscount = new javax.swing.JTextField();
        txtStockQuantity = new javax.swing.JTextField();
        btnStockAdd = new javax.swing.JButton();
        txtStockPartName = new javax.swing.JTextField();
        txtStockDealerName = new javax.swing.JTextField();
        txtStockPriceCent = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        txtStockCostCent = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        ddStockAddDate = new org.freixas.jcalendar.JCalendarCombo();
        ddStockPartID = new javax.swing.JComboBox();
        jLabel28 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        ddStockID = new javax.swing.JComboBox();
        ddStockSearchPartID = new javax.swing.JComboBox();
        ddStockSearchDealerID = new javax.swing.JComboBox();
        cdStockID = new javax.swing.JCheckBox();
        cbStockPartID = new javax.swing.JCheckBox();
        cbStockDealerID = new javax.swing.JCheckBox();
        btnStockSearch = new javax.swing.JButton();
        btnViewAllStocks = new javax.swing.JButton();
        jLabel29 = new javax.swing.JLabel();
        ddStockSearchPartName = new javax.swing.JComboBox();
        jLabel30 = new javax.swing.JLabel();
        ddStockSearchDealerName = new javax.swing.JComboBox();
        cbStockPartName = new javax.swing.JCheckBox();
        cbStockDealerName = new javax.swing.JCheckBox();
        jLabel23 = new javax.swing.JLabel();
        ddStockSearchDate = new org.freixas.jcalendar.JCalendarCombo();
        cbStockDate = new javax.swing.JCheckBox();
        jPanel11 = new javax.swing.JPanel();
        btnStockUpdate = new javax.swing.JButton();
        btnStockDelete = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblStock = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblStockDealer = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblStockPart = new javax.swing.JTable();
        btnStockDetails = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();
        jLabel31 = new javax.swing.JLabel();
        ddSearchPartName = new javax.swing.JComboBox();
        btnStockSearchPart = new javax.swing.JButton();
        jPanel13 = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        ddSearchDealerName = new javax.swing.JComboBox();
        btnStockSearchDealer = new javax.swing.JButton();
        btnDealerDetails = new javax.swing.JButton();
        jPanel14 = new javax.swing.JPanel();
        btnStockAddDealer = new javax.swing.JButton();
        btnStockUpdateDealer = new javax.swing.JButton();
        btnStockDeleteDealer = new javax.swing.JButton();
        btnRefreshParts = new javax.swing.JButton();
        btnRefreshDealers = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblPurchasesOrder = new javax.swing.JTable();
        jPanel16 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tblPurchasesStock = new javax.swing.JTable();
        jPanel17 = new javax.swing.JPanel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        ddPurchasesPartModel = new javax.swing.JComboBox();
        ddPurchasesSearchPartType = new javax.swing.JComboBox();
        cbPurchasesPartModel = new javax.swing.JCheckBox();
        cbPurchasesPartType = new javax.swing.JCheckBox();
        btnPurchasesSearch = new javax.swing.JButton();
        btnPurchasesViewAllStocks = new javax.swing.JButton();
        jLabel36 = new javax.swing.JLabel();
        ddPurchasesSearchPartName = new javax.swing.JComboBox();
        cbPurchasesPartName = new javax.swing.JCheckBox();
        jLabel38 = new javax.swing.JLabel();
        ddPurchasesSearchDate = new org.freixas.jcalendar.JCalendarCombo();
        cbPurchasesDate = new javax.swing.JCheckBox();
        jPanel18 = new javax.swing.JPanel();
        lblTotal = new javax.swing.JLabel();
        jPanel19 = new javax.swing.JPanel();
        jLabel35 = new javax.swing.JLabel();
        btnAddOrder = new javax.swing.JButton();
        spnPurchasesOrderQuantity = new javax.swing.JSpinner();
        btnRemovePart = new javax.swing.JButton();
        jLabel46 = new javax.swing.JLabel();
        spnPurchasesUpdateQuantity = new javax.swing.JSpinner();
        btnPurchasesChangeQuantity = new javax.swing.JButton();
        btnClearOrderTable = new javax.swing.JButton();
        pnlPurchasesCheque = new javax.swing.JPanel();
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        txtPurchasesChequeNo = new javax.swing.JTextField();
        txtPurchasesBank = new javax.swing.JTextField();
        pnlPurchasesDefault = new javax.swing.JPanel();
        jLabel37 = new javax.swing.JLabel();
        pnlPurchasesCash = new javax.swing.JPanel();
        txtPurchasesPaymentCent = new javax.swing.JTextField();
        jLabel40 = new javax.swing.JLabel();
        txtPurchasesPaymentRs = new javax.swing.JTextField();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        lblBalance = new javax.swing.JLabel();
        btnCalculate = new javax.swing.JButton();
        ddPaymentType = new javax.swing.JComboBox();
        btnCreateBill = new javax.swing.JButton();
        btnPurchasesAddServiceCharge = new javax.swing.JButton();
        jLabel39 = new javax.swing.JLabel();
        btnPurchasesRemoveServiceCharge = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        tblQuotationParts = new javax.swing.JTable();
        jPanel22 = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        tblQuotationStocks = new javax.swing.JTable();
        jPanel23 = new javax.swing.JPanel();
        jLabel43 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        ddStockStockID1 = new javax.swing.JComboBox();
        ddStockSearchPartID1 = new javax.swing.JComboBox();
        cbStockID1 = new javax.swing.JCheckBox();
        cbStockPartID1 = new javax.swing.JCheckBox();
        btnQuotationSearchQtn = new javax.swing.JButton();
        btnQuotationViewAllStocks = new javax.swing.JButton();
        jLabel48 = new javax.swing.JLabel();
        ddStockSearchPartName1 = new javax.swing.JComboBox();
        cbStockPartName1 = new javax.swing.JCheckBox();
        jLabel49 = new javax.swing.JLabel();
        cbStockDate1 = new javax.swing.JCheckBox();
        ddQutationSearchDate = new org.freixas.jcalendar.JCalendarCombo();
        jPanel24 = new javax.swing.JPanel();
        lblTotal1 = new javax.swing.JLabel();
        lblQuotationTotal = new javax.swing.JLabel();
        btnQuotationCustomer = new javax.swing.JButton();
        btnQuotationCreate = new javax.swing.JButton();
        btnQuotationClear = new javax.swing.JButton();
        jPanel25 = new javax.swing.JPanel();
        jLabel53 = new javax.swing.JLabel();
        lblQuotationlCusName = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        lblQuotationAddress = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        lblQuotationlContact = new javax.swing.JLabel();
        btnQuotationChoose = new javax.swing.JButton();
        btnQuotationRemovePart = new javax.swing.JButton();
        spnQuotationQty = new javax.swing.JSpinner();
        jLabel56 = new javax.swing.JLabel();
        btnQuotationChangeQty = new javax.swing.JButton();
        jPanel27 = new javax.swing.JPanel();
        jLabel51 = new javax.swing.JLabel();
        lblServiceCharge = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        txtServiceChargeRs = new javax.swing.JTextField();
        jLabel52 = new javax.swing.JLabel();
        txtServiceChargeCent = new javax.swing.JTextField();
        btnQuotationAdd = new javax.swing.JButton();
        jPanel26 = new javax.swing.JPanel();
        jPanel29 = new javax.swing.JPanel();
        jLabel61 = new javax.swing.JLabel();
        jLabel62 = new javax.swing.JLabel();
        jLabel63 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        ddStockAddDate1 = new org.freixas.jcalendar.JCalendarCombo();
        ddStockAddDate2 = new org.freixas.jcalendar.JCalendarCombo();
        jLabel64 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jPanel30 = new javax.swing.JPanel();
        jLabel65 = new javax.swing.JLabel();
        jLabel66 = new javax.swing.JLabel();
        txtNewPartType = new javax.swing.JTextField();
        txtNewPartModel = new javax.swing.JTextField();
        btnAdMngAddModel = new javax.swing.JButton();
        btnAdMngAddType = new javax.swing.JButton();
        jPanel31 = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel32 = new javax.swing.JPanel();
        jScrollPane10 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jPanel33 = new javax.swing.JPanel();
        jScrollPane11 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jPanel34 = new javax.swing.JPanel();
        jLabel68 = new javax.swing.JLabel();
        jComboBox4 = new javax.swing.JComboBox();
        jButton6 = new javax.swing.JButton();
        jPanel35 = new javax.swing.JPanel();
        jScrollPane12 = new javax.swing.JScrollPane();
        lstQuotation = new javax.swing.JList();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jLabel57 = new javax.swing.JLabel();
        jLabel58 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        btnAdvMngCreateBill = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("JC Motors Main Frame");
        setMinimumSize(new java.awt.Dimension(1366, 768));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTabbedPane1.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        jTabbedPane1.setAlignmentY(0.0F);
        jTabbedPane1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(182, 182, 182)));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(182, 182, 182)), " add a new part ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 12))); // NOI18N
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Part id");
        jPanel5.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 80, 20));

        jLabel2.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Part Name");
        jPanel5.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 80, 20));

        jLabel3.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Type");
        jPanel5.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 80, -1));

        jLabel4.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Model");
        jPanel5.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 80, 20));

        txtPartId.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPartId.setEnabled(false);
        txtPartId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPartIdActionPerformed(evt);
            }
        });
        jPanel5.add(txtPartId, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 20, 240, 20));

        txtPartName.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jPanel5.add(txtPartName, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 50, 240, 20));

        btnAddPart.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnAddPart.setText("Add");
        btnAddPart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddPartActionPerformed(evt);
            }
        });
        jPanel5.add(btnAddPart, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 170, 100, -1));

        jLabel5.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Stock");
        jPanel5.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, 80, 20));

        ddAddStock.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        ddAddStock.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Other", "Hero" }));
        jPanel5.add(ddAddStock, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 140, 240, 20));

        ddAddType.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        ddAddType.setMaximumRowCount(5);
        ddAddType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Type 1", "Type 2", "Type 3", "Type 4", "Type 5" }));
        jPanel5.add(ddAddType, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 80, 240, -1));

        ddAddModel.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        ddAddModel.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Model 1", "Model 2", "Model 3", "Model 4", "Model 5", " " }));
        jPanel5.add(ddAddModel, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 110, 240, -1));

        jPanel1.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 180, 360, 210));
        jPanel5.getAccessibleContext().setAccessibleName("Part Management");

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(182, 182, 182)), " search a part ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 12))); // NOI18N
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel7.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 105, -1, -1));

        btnSearchPart.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnSearchPart.setText("Search");
        btnSearchPart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchPartActionPerformed(evt);
            }
        });
        jPanel7.add(btnSearchPart, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 180, 110, -1));

        ddPartID.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        ddPartID.setMaximumRowCount(5);
        ddPartID.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel7.add(ddPartID, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 30, 230, -1));

        ddName.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        ddName.setMaximumRowCount(5);
        ddName.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel7.add(ddName, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 60, 230, -1));

        jLabel6.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Part ID");
        jPanel7.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 80, -1));

        jLabel7.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Type");
        jPanel7.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 80, -1));

        jLabel8.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("Part Name");
        jPanel7.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 80, -1));

        jLabel9.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("Model");
        jPanel7.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, 80, -1));

        jLabel10.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("Stock");
        jPanel7.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 150, 80, -1));

        ddType.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        ddType.setMaximumRowCount(5);
        ddType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel7.add(ddType, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 90, 230, -1));

        ddModel.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        ddModel.setMaximumRowCount(5);
        ddModel.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel7.add(ddModel, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 120, 230, -1));

        ddStock.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        ddStock.setMaximumRowCount(5);
        ddStock.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel7.add(ddStock, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 150, 230, -1));

        cbPartID.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jPanel7.add(cbPartID, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 30, 20, 20));

        cbPartName.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        cbPartName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbPartNameActionPerformed(evt);
            }
        });
        jPanel7.add(cbPartName, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 60, -1, -1));

        cbType.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jPanel7.add(cbType, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 90, -1, -1));

        cbModel.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        cbModel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbModelActionPerformed(evt);
            }
        });
        jPanel7.add(cbModel, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 120, -1, -1));

        cbStock.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jPanel7.add(cbStock, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 150, -1, -1));

        btnViewAllParts.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnViewAllParts.setText("View All Parts");
        btnViewAllParts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewAllPartsActionPerformed(evt);
            }
        });
        jPanel7.add(btnViewAllParts, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 180, 130, -1));

        jPanel1.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 396, 360, 220));

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(182, 182, 182)), " update or delete a part ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 12))); // NOI18N
        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnUpdatePart.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnUpdatePart.setText("Update Part");
        btnUpdatePart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdatePartActionPerformed(evt);
            }
        });
        jPanel8.add(btnUpdatePart, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 20, 160, -1));

        btnDeletePart.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnDeletePart.setText("Delete Part");
        btnDeletePart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeletePartActionPerformed(evt);
            }
        });
        jPanel8.add(btnDeletePart, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 160, -1));

        jPanel1.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 620, 360, 60));

        tblParts.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        tblParts.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Part ID", "Part Name", "Type", "Model", "Bike"
            }
        ));
        jScrollPane1.setViewportView(tblParts);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(402, 7, 920, 670));

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jcmotors/main frame logo.png"))); // NOI18N
        jPanel1.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 380, 170));

        jTabbedPane1.addTab("Parts Management", jPanel1);

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(182, 182, 182)));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(182, 182, 182)), " add a new stock ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 12))); // NOI18N
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

        ddStockDealerID.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        ddStockDealerID.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "dealer 1", "dealer 2", "dealer 3", "dealer 4", "dealer 5" }));
        jPanel9.add(ddStockDealerID, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 140, 230, -1));

        txtStockCostRs.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtStockCostRs.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jPanel9.add(txtStockCostRs, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 200, 180, 20));

        txtStockPriceRs.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtStockPriceRs.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jPanel9.add(txtStockPriceRs, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 230, 180, 20));

        txtStockDiscount.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtStockDiscount.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jPanel9.add(txtStockDiscount, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 260, 200, 20));

        txtStockQuantity.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jPanel9.add(txtStockQuantity, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 290, 230, 20));

        btnStockAdd.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnStockAdd.setText("Add");
        btnStockAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStockAddActionPerformed(evt);
            }
        });
        jPanel9.add(btnStockAdd, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 320, 100, -1));

        txtStockPartName.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtStockPartName.setEnabled(false);
        jPanel9.add(txtStockPartName, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 110, 230, 20));

        txtStockDealerName.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtStockDealerName.setEnabled(false);
        jPanel9.add(txtStockDealerName, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 170, 230, 20));

        txtStockPriceCent.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtStockPriceCent.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtStockPriceCent.setText("00");
        jPanel9.add(txtStockPriceCent, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 230, 30, 20));

        jLabel25.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel25.setText("%");
        jPanel9.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 260, 30, 20));

        jLabel26.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel26.setText(".");
        jPanel9.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 200, 20, 20));

        txtStockCostCent.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtStockCostCent.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtStockCostCent.setText("00");
        jPanel9.add(txtStockCostCent, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 200, 30, 20));

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

        ddStockAddDate.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jPanel9.add(ddStockAddDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 20, 230, -1));

        ddStockPartID.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        ddStockPartID.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "part 1", "part 2", "part 3", "part 4", "part 5" }));
        ddStockPartID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ddStockPartIDActionPerformed(evt);
            }
        });
        jPanel9.add(ddStockPartID, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 80, 230, -1));

        jLabel28.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel28.setText("Part Name");
        jPanel9.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 90, -1));

        jLabel24.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel24.setText("Dealer Name");
        jPanel9.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, 90, -1));

        jPanel2.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 360, 360));

        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(182, 182, 182)), " search a stock ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 12))); // NOI18N
        jPanel10.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel20.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel20.setText("Stock ID");
        jPanel10.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 90, -1));

        jLabel21.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel21.setText("Part ID");
        jPanel10.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 90, -1));

        jLabel22.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel22.setText("Dealer ID");
        jPanel10.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, 90, -1));

        ddStockID.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        ddStockID.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "stock 1", "stock 2", "stock 3", "stock 4", "stock 5" }));
        jPanel10.add(ddStockID, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 50, 220, -1));

        ddStockSearchPartID.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        ddStockSearchPartID.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "part 1", "part 2", "part 3", "part 4", "part 5" }));
        jPanel10.add(ddStockSearchPartID, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 110, 220, -1));

        ddStockSearchDealerID.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        ddStockSearchDealerID.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "dealer 1", "dealer 2", "dealer 3", "dealer 4", "dealer 5" }));
        jPanel10.add(ddStockSearchDealerID, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 170, 220, -1));
        jPanel10.add(cdStockID, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 50, 20, 20));
        jPanel10.add(cbStockPartID, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 110, 20, 20));

        cbStockDealerID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbStockDealerIDActionPerformed(evt);
            }
        });
        jPanel10.add(cbStockDealerID, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 170, 20, 20));

        btnStockSearch.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnStockSearch.setText("Search");
        btnStockSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStockSearchActionPerformed(evt);
            }
        });
        jPanel10.add(btnStockSearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 200, 100, -1));

        btnViewAllStocks.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnViewAllStocks.setText("View All Stocks");
        btnViewAllStocks.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewAllStocksActionPerformed(evt);
            }
        });
        jPanel10.add(btnViewAllStocks, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 200, 140, -1));

        jLabel29.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel29.setText("Part Name");
        jPanel10.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 90, -1));

        ddStockSearchPartName.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        ddStockSearchPartName.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "part 1", "part 2", "part 3", "part 4", "part 5" }));
        jPanel10.add(ddStockSearchPartName, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 80, 220, -1));

        jLabel30.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel30.setText("Dealer Name");
        jPanel10.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, 90, -1));

        ddStockSearchDealerName.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        ddStockSearchDealerName.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "dealer 1", "dealer 2", "dealer 3", "dealer 4", "dealer 5" }));
        jPanel10.add(ddStockSearchDealerName, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 140, 220, -1));
        jPanel10.add(cbStockPartName, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 80, 20, -1));
        jPanel10.add(cbStockDealerName, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 140, 20, -1));

        jLabel23.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel23.setText("Date");
        jPanel10.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 90, -1));

        ddStockSearchDate.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jPanel10.add(ddStockSearchDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 20, 220, -1));
        jPanel10.add(cbStockDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 20, 20, 20));

        jPanel2.add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 374, 360, 240));

        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(182, 182, 182)), " delete or update a stock ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 12))); // NOI18N
        jPanel11.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnStockUpdate.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnStockUpdate.setText("Update Stock");
        btnStockUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStockUpdateActionPerformed(evt);
            }
        });
        jPanel11.add(btnStockUpdate, new org.netbeans.lib.awtextra.AbsoluteConstraints(185, 20, 160, -1));

        btnStockDelete.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnStockDelete.setText("Delete Stock");
        btnStockDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStockDeleteActionPerformed(evt);
            }
        });
        jPanel11.add(btnStockDelete, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 160, -1));

        jPanel2.add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 620, 360, 60));

        tblStock.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        tblStock.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Stock ID", "Part Name", "Part Type", "Part Model", "Cost", "Price", "Discount", "Quantity"
            }
        ));
        jScrollPane2.setViewportView(tblStock);

        jPanel2.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 10, 940, 360));

        tblStockDealer.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        tblStockDealer.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Dealer ID", "Dealer Name"
            }
        ));
        jScrollPane3.setViewportView(tblStockDealer);

        jPanel2.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 380, 260, 150));

        tblStockPart.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        tblStockPart.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Part ID", "Part Name", "Model", "Type"
            }
        ));
        jScrollPane4.setViewportView(tblStockPart);

        jPanel2.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 380, 670, 240));

        btnStockDetails.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnStockDetails.setText("Stock Details");
        btnStockDetails.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStockDetailsActionPerformed(evt);
            }
        });
        jPanel2.add(btnStockDetails, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 654, 130, -1));

        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(182, 182, 182)), " search a part ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 12))); // NOI18N
        jPanel12.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel31.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel31.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel31.setText("Part Name");
        jPanel12.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 24, 70, -1));

        ddSearchPartName.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        ddSearchPartName.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "part 1", "part 2", "part 3", "part 4", "part 5" }));
        ddSearchPartName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ddSearchPartNameActionPerformed(evt);
            }
        });
        jPanel12.add(ddSearchPartName, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 20, 210, -1));

        btnStockSearchPart.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnStockSearchPart.setText("Search");
        btnStockSearchPart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStockSearchPartActionPerformed(evt);
            }
        });
        jPanel12.add(btnStockSearchPart, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 20, -1, -1));

        jPanel2.add(jPanel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 620, 400, 60));

        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(182, 182, 182)), " search a dealer ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 12))); // NOI18N
        jPanel13.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel32.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel32.setText("Dealer Name");
        jPanel13.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 24, -1, -1));

        ddSearchDealerName.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        ddSearchDealerName.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "dealer 1", "dealer 2", "dealer 3", "dealer 4", "dealer 5" }));
        jPanel13.add(ddSearchDealerName, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 20, 190, -1));

        btnStockSearchDealer.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnStockSearchDealer.setText("Search");
        btnStockSearchDealer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStockSearchDealerActionPerformed(evt);
            }
        });
        jPanel13.add(btnStockSearchDealer, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 20, -1, -1));

        jPanel2.add(jPanel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 620, 390, 60));

        btnDealerDetails.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnDealerDetails.setText("Dealer Details");
        btnDealerDetails.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDealerDetailsActionPerformed(evt);
            }
        });
        jPanel2.add(btnDealerDetails, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 624, 130, -1));

        jPanel14.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(182, 182, 182)), " manage dealers ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 12))); // NOI18N
        jPanel14.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnStockAddDealer.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnStockAddDealer.setText("Add");
        btnStockAddDealer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStockAddDealerActionPerformed(evt);
            }
        });
        jPanel14.add(btnStockAddDealer, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 70, -1));

        btnStockUpdateDealer.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnStockUpdateDealer.setText("Update");
        btnStockUpdateDealer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStockUpdateDealerActionPerformed(evt);
            }
        });
        jPanel14.add(btnStockUpdateDealer, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 20, 90, -1));

        btnStockDeleteDealer.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnStockDeleteDealer.setText("Delete");
        btnStockDeleteDealer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStockDeleteDealerActionPerformed(evt);
            }
        });
        jPanel14.add(btnStockDeleteDealer, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 20, 80, -1));

        jPanel2.add(jPanel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 534, 260, 55));

        btnRefreshParts.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnRefreshParts.setText("All Parts");
        btnRefreshParts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshPartsActionPerformed(evt);
            }
        });
        jPanel2.add(btnRefreshParts, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 594, 130, -1));

        btnRefreshDealers.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnRefreshDealers.setText("All Dealers");
        btnRefreshDealers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshDealersActionPerformed(evt);
            }
        });
        jPanel2.add(btnRefreshDealers, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 594, 130, -1));

        jTabbedPane1.addTab("Stock Management", jPanel2);

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(182, 182, 182)));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel15.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(182, 182, 182)), " parts of order ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 12))); // NOI18N
        jPanel15.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tblPurchasesOrder.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        tblPurchasesOrder.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Stock ID", "Part Type", "Part Name", "Part Model", "Quantity", "Unit Price", "With Discount", "Total"
            }
        ));
        jScrollPane5.setViewportView(tblPurchasesOrder);

        jPanel15.add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 920, 210));

        jPanel3.add(jPanel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 940, 240));

        jPanel16.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(182, 182, 182)), " parts of stock ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 12))); // NOI18N
        jPanel16.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tblPurchasesStock.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        tblPurchasesStock.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Stock ID", "Part Type", "Part Name", "Part Model", "Price", "Discount", "Quantity", "Add Part"
            }
        ));
        jScrollPane6.setViewportView(tblPurchasesStock);

        jPanel16.add(jScrollPane6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 1290, 320));

        jPanel3.add(jPanel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 330, 1310, 350));

        jPanel17.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(182, 182, 182)), " search a stock ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 12))); // NOI18N
        jPanel17.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel33.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel33.setText("Part Model");
        jPanel17.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 90, -1));

        jLabel34.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel34.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel34.setText("Part Type");
        jPanel17.add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 90, -1));

        ddPurchasesPartModel.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        ddPurchasesPartModel.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "model 1", "model 2", "model 3", "model 4", "model 5" }));
        jPanel17.add(ddPurchasesPartModel, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 50, 220, -1));

        ddPurchasesSearchPartType.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        ddPurchasesSearchPartType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "type 1", "type 2", "type 3", "type 4", "type 5" }));
        jPanel17.add(ddPurchasesSearchPartType, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 110, 220, -1));
        jPanel17.add(cbPurchasesPartModel, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 50, 20, 20));
        jPanel17.add(cbPurchasesPartType, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 110, 20, 20));

        btnPurchasesSearch.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnPurchasesSearch.setText("Search");
        btnPurchasesSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPurchasesSearchActionPerformed(evt);
            }
        });
        jPanel17.add(btnPurchasesSearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 140, 100, -1));

        btnPurchasesViewAllStocks.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnPurchasesViewAllStocks.setText("View All Stocks");
        btnPurchasesViewAllStocks.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPurchasesViewAllStocksActionPerformed(evt);
            }
        });
        jPanel17.add(btnPurchasesViewAllStocks, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 140, 140, -1));

        jLabel36.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel36.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel36.setText("Part Name");
        jPanel17.add(jLabel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 90, -1));

        ddPurchasesSearchPartName.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        ddPurchasesSearchPartName.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "part 1", "part 2", "part 3", "part 4", "part 5" }));
        jPanel17.add(ddPurchasesSearchPartName, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 80, 220, -1));
        jPanel17.add(cbPurchasesPartName, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 80, 20, -1));

        jLabel38.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel38.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel38.setText("Date");
        jPanel17.add(jLabel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 90, -1));

        ddPurchasesSearchDate.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jPanel17.add(ddPurchasesSearchDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 20, 220, -1));
        jPanel17.add(cbPurchasesDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 20, 20, 20));

        jPanel3.add(jPanel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 10, 360, 180));

        jPanel18.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(182, 182, 182)));
        jPanel18.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTotal.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        lblTotal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTotal.setText("Total Rs. 00000.00");
        jPanel18.add(lblTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 7, 340, -1));

        jPanel3.add(jPanel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 230, 360, 30));

        jPanel19.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(182, 182, 182)), " order parts management", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 12))); // NOI18N
        jPanel19.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel35.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel35.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel35.setText("Add Quantity");
        jPanel19.add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 120, -1));

        btnAddOrder.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnAddOrder.setText("Add Part");
        btnAddOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddOrderActionPerformed(evt);
            }
        });
        jPanel19.add(btnAddOrder, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 46, 190, -1));

        spnPurchasesOrderQuantity.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jPanel19.add(spnPurchasesOrderQuantity, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 48, 150, 20));

        btnRemovePart.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnRemovePart.setText("Remove Part");
        btnRemovePart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemovePartActionPerformed(evt);
            }
        });
        jPanel19.add(btnRemovePart, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 16, 200, -1));

        jLabel46.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel46.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel46.setText("Update Quantity");
        jPanel19.add(jLabel46, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 120, -1));

        spnPurchasesUpdateQuantity.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jPanel19.add(spnPurchasesUpdateQuantity, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 18, 150, 20));

        btnPurchasesChangeQuantity.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnPurchasesChangeQuantity.setText("Change Quantity");
        btnPurchasesChangeQuantity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPurchasesChangeQuantityActionPerformed(evt);
            }
        });
        jPanel19.add(btnPurchasesChangeQuantity, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 16, 190, -1));

        btnClearOrderTable.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnClearOrderTable.setText("Clear Order Table");
        btnClearOrderTable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearOrderTableActionPerformed(evt);
            }
        });
        jPanel19.add(btnClearOrderTable, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 46, 200, -1));

        jPanel3.add(jPanel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 250, 730, 80));

        pnlPurchasesCheque.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel44.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel44.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel44.setText("Cheque No ");
        pnlPurchasesCheque.add(jLabel44, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 80, -1));

        jLabel45.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel45.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel45.setText("Bank");
        pnlPurchasesCheque.add(jLabel45, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 80, -1));

        txtPurchasesChequeNo.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPurchasesChequeNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPurchasesChequeNoActionPerformed(evt);
            }
        });
        pnlPurchasesCheque.add(txtPurchasesChequeNo, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 10, 240, 20));

        txtPurchasesBank.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        pnlPurchasesCheque.add(txtPurchasesBank, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 40, 240, 20));

        jPanel3.add(pnlPurchasesCheque, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 260, 364, 68));

        pnlPurchasesDefault.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel37.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel37.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel37.setText("Please select CASH or CHEQUE to pay");
        pnlPurchasesDefault.add(jLabel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 300, 20));

        jPanel3.add(pnlPurchasesDefault, new org.netbeans.lib.awtextra.AbsoluteConstraints(980, 277, 320, 40));

        pnlPurchasesCash.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtPurchasesPaymentCent.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPurchasesPaymentCent.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtPurchasesPaymentCent.setText("00");
        txtPurchasesPaymentCent.setBorder(null);
        pnlPurchasesCash.add(txtPurchasesPaymentCent, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 10, 30, 20));

        jLabel40.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel40.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel40.setText(".");
        pnlPurchasesCash.add(jLabel40, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 10, 20, 20));

        txtPurchasesPaymentRs.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtPurchasesPaymentRs.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtPurchasesPaymentRs.setBorder(null);
        pnlPurchasesCash.add(txtPurchasesPaymentRs, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 10, 220, -1));

        jLabel41.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel41.setText("Payment : Rs.");
        pnlPurchasesCash.add(jLabel41, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, -1, -1));

        jLabel42.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel42.setText("Balance : Rs.");
        pnlPurchasesCash.add(jLabel42, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, -1, -1));

        lblBalance.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lblBalance.setText("balance");
        pnlPurchasesCash.add(lblBalance, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 40, 120, -1));

        btnCalculate.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnCalculate.setText("Calculate");
        btnCalculate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCalculateActionPerformed(evt);
            }
        });
        pnlPurchasesCash.add(btnCalculate, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 40, 130, -1));

        jPanel3.add(pnlPurchasesCash, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 260, 364, 68));

        ddPaymentType.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        ddPaymentType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Cash", "Cheque" }));
        jPanel3.add(ddPaymentType, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 260, 200, 25));

        btnCreateBill.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnCreateBill.setText("Create Bill");
        btnCreateBill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreateBillActionPerformed(evt);
            }
        });
        jPanel3.add(btnCreateBill, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 294, 200, 30));

        btnPurchasesAddServiceCharge.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnPurchasesAddServiceCharge.setText("Add");
        btnPurchasesAddServiceCharge.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPurchasesAddServiceChargeActionPerformed(evt);
            }
        });
        jPanel3.add(btnPurchasesAddServiceCharge, new org.netbeans.lib.awtextra.AbsoluteConstraints(1070, 195, 120, -1));

        jLabel39.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel39.setText("Service Charge");
        jPanel3.add(jLabel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 200, 100, -1));

        btnPurchasesRemoveServiceCharge.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnPurchasesRemoveServiceCharge.setText("Remove");
        btnPurchasesRemoveServiceCharge.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPurchasesRemoveServiceChargeActionPerformed(evt);
            }
        });
        jPanel3.add(btnPurchasesRemoveServiceCharge, new org.netbeans.lib.awtextra.AbsoluteConstraints(1200, 195, 120, -1));

        jTabbedPane1.addTab("Purchases", jPanel3);

        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(182, 182, 182)));

        jPanel20.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel21.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(182, 182, 182)), " order parts ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 12))); // NOI18N
        jPanel21.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tblQuotationParts.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        tblQuotationParts.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Part Type", "Part Name", "Part Model", "Quantity", "Unit Price", "With Discount", "Total"
            }
        ));
        jScrollPane7.setViewportView(tblQuotationParts);

        jPanel21.add(jScrollPane7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 920, 170));

        jPanel20.add(jPanel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 940, 200));

        jPanel22.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(182, 182, 182)), " parts of stock ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 12))); // NOI18N
        jPanel22.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tblQuotationStocks.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        tblQuotationStocks.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Stock ID", "Part Type", "Part Name", "Part Model", "Price", "Discount", "Quantity", "Add Part"
            }
        ));
        jScrollPane8.setViewportView(tblQuotationStocks);

        jPanel22.add(jScrollPane8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 1290, 360));

        jPanel20.add(jPanel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 290, 1310, 390));

        jPanel23.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(182, 182, 182)), " search a stock ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 12))); // NOI18N
        jPanel23.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel43.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel43.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel43.setText("Stock ID");
        jPanel23.add(jLabel43, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 90, -1));

        jLabel47.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel47.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel47.setText("Part ID");
        jPanel23.add(jLabel47, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 90, -1));

        ddStockStockID1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        ddStockStockID1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "stock 1", "stock 2", "stock 3", "stock 4", "stock 5" }));
        jPanel23.add(ddStockStockID1, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 50, 220, -1));

        ddStockSearchPartID1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        ddStockSearchPartID1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "part 1", "part 2", "part 3", "part 4", "part 5" }));
        jPanel23.add(ddStockSearchPartID1, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 110, 220, -1));
        jPanel23.add(cbStockID1, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 50, 20, 20));
        jPanel23.add(cbStockPartID1, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 110, 20, 20));

        btnQuotationSearchQtn.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnQuotationSearchQtn.setText("Search");
        btnQuotationSearchQtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuotationSearchQtnActionPerformed(evt);
            }
        });
        jPanel23.add(btnQuotationSearchQtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 140, 100, -1));

        btnQuotationViewAllStocks.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnQuotationViewAllStocks.setText("View All Stocks");
        btnQuotationViewAllStocks.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuotationViewAllStocksActionPerformed(evt);
            }
        });
        jPanel23.add(btnQuotationViewAllStocks, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 140, 130, -1));

        jLabel48.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel48.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel48.setText("Part Name");
        jPanel23.add(jLabel48, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 90, -1));

        ddStockSearchPartName1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        ddStockSearchPartName1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "part 1", "part 2", "part 3", "part 4", "part 5" }));
        jPanel23.add(ddStockSearchPartName1, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 80, 220, -1));
        jPanel23.add(cbStockPartName1, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 80, 20, -1));

        jLabel49.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel49.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel49.setText("Date");
        jPanel23.add(jLabel49, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 90, -1));
        jPanel23.add(cbStockDate1, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 20, 20, 20));

        ddQutationSearchDate.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jPanel23.add(ddQutationSearchDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 20, 220, -1));

        jPanel20.add(jPanel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 10, 360, 180));

        jPanel24.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(182, 182, 182)));
        jPanel24.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTotal1.setFont(new java.awt.Font("Arial", 1, 15)); // NOI18N
        lblTotal1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTotal1.setText("Total Rs. ");
        jPanel24.add(lblTotal1, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 7, 70, -1));

        lblQuotationTotal.setFont(new java.awt.Font("Arial", 1, 15)); // NOI18N
        lblQuotationTotal.setText("0.00");
        jPanel24.add(lblQuotationTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 7, 110, 20));

        jPanel20.add(jPanel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 200, 360, 30));

        btnQuotationCustomer.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnQuotationCustomer.setText("Customer");
        btnQuotationCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuotationCustomerActionPerformed(evt);
            }
        });
        jPanel20.add(btnQuotationCustomer, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 64, 200, -1));

        btnQuotationCreate.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnQuotationCreate.setText("Create ");
        btnQuotationCreate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuotationCreateActionPerformed(evt);
            }
        });
        jPanel20.add(btnQuotationCreate, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 265, 200, -1));

        btnQuotationClear.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnQuotationClear.setText("Clear All");
        btnQuotationClear.setMargin(new java.awt.Insets(2, 8, 2, 8));
        btnQuotationClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuotationClearActionPerformed(evt);
            }
        });
        jPanel20.add(btnQuotationClear, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 36, 200, -1));

        jPanel25.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(182, 182, 182)), " customer details ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 12))); // NOI18N
        jPanel25.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel53.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel53.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel53.setText("Name : ");
        jPanel25.add(jLabel53, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 80, -1));

        lblQuotationlCusName.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lblQuotationlCusName.setText("- Not Set -");
        jPanel25.add(lblQuotationlCusName, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 20, 210, -1));

        jLabel55.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel55.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel55.setText("Address : ");
        jPanel25.add(jLabel55, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, 80, -1));

        lblQuotationAddress.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lblQuotationAddress.setText("- Not Set -");
        jPanel25.add(lblQuotationAddress, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 40, 210, -1));

        jLabel54.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel54.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel54.setText("Contact : ");
        jPanel25.add(jLabel54, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 60, 80, -1));

        lblQuotationlContact.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lblQuotationlContact.setText("- Not Set -");
        jPanel25.add(lblQuotationlContact, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 60, 210, -1));

        jPanel20.add(jPanel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 350, 90));

        btnQuotationChoose.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnQuotationChoose.setText("Choose");
        btnQuotationChoose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuotationChooseActionPerformed(evt);
            }
        });
        jPanel20.add(btnQuotationChoose, new org.netbeans.lib.awtextra.AbsoluteConstraints(1170, 238, 150, -1));

        btnQuotationRemovePart.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnQuotationRemovePart.setText("Remove Part");
        btnQuotationRemovePart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuotationRemovePartActionPerformed(evt);
            }
        });
        jPanel20.add(btnQuotationRemovePart, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 8, 200, -1));
        jPanel20.add(spnQuotationQty, new org.netbeans.lib.awtextra.AbsoluteConstraints(1020, 240, 140, 20));

        jLabel56.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel56.setText("Quantity");
        jPanel20.add(jLabel56, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 240, -1, 20));

        btnQuotationChangeQty.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnQuotationChangeQty.setText("Change Qty");
        btnQuotationChangeQty.setMargin(new java.awt.Insets(2, 12, 2, 12));
        btnQuotationChangeQty.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuotationChangeQtyActionPerformed(evt);
            }
        });
        jPanel20.add(btnQuotationChangeQty, new org.netbeans.lib.awtextra.AbsoluteConstraints(1170, 265, 150, -1));

        jPanel27.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(182, 182, 182)), " service charge manage ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 12))); // NOI18N
        jPanel27.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel51.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel51.setText("Service Charge : Rs.");
        jPanel27.add(jLabel51, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 140, 20));

        lblServiceCharge.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lblServiceCharge.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblServiceCharge.setText("0.00");
        jPanel27.add(lblServiceCharge, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 20, 220, 20));

        jLabel50.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel50.setText("Service Charge :  Rs.");
        jPanel27.add(jLabel50, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, -1, -1));

        txtServiceChargeRs.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtServiceChargeRs.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtServiceChargeRs.setText("0");
        txtServiceChargeRs.setBorder(null);
        jPanel27.add(txtServiceChargeRs, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 50, 110, 20));

        jLabel52.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel52.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel52.setText(".");
        jPanel27.add(jLabel52, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 50, 20, 20));

        txtServiceChargeCent.setEditable(false);
        txtServiceChargeCent.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtServiceChargeCent.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtServiceChargeCent.setText("00");
        txtServiceChargeCent.setBorder(null);
        txtServiceChargeCent.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        jPanel27.add(txtServiceChargeCent, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 50, 30, 20));

        btnQuotationAdd.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnQuotationAdd.setText("Add");
        btnQuotationAdd.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btnQuotationAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuotationAddActionPerformed(evt);
            }
        });
        jPanel27.add(btnQuotationAdd, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 50, 50, -1));

        jPanel20.add(jPanel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 0, 380, 90));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, 1331, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel20, javax.swing.GroupLayout.DEFAULT_SIZE, 690, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Quotation", jPanel4);

        jPanel26.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(182, 182, 182)));
        jPanel26.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel29.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(182, 182, 182)), " profit ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 12))); // NOI18N
        jPanel29.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel61.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel61.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel61.setText("Start Date");
        jPanel29.add(jLabel61, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 70, -1));

        jLabel62.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel62.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel62.setText("End Date");
        jPanel29.add(jLabel62, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 70, -1));

        jLabel63.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel63.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel63.setText("Profit");
        jPanel29.add(jLabel63, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 40, -1));

        jButton2.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jButton2.setText("Calculate Profit");
        jPanel29.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 120, 160, -1));

        ddStockAddDate1.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jPanel29.add(ddStockAddDate1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 60, 250, -1));

        ddStockAddDate2.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jPanel29.add(ddStockAddDate2, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 30, 250, -1));

        jLabel64.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel64.setText("Rs.");
        jPanel29.add(jLabel64, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 90, -1, -1));

        jTextField1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jPanel29.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 90, 250, 20));

        jButton3.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jButton3.setText("Get Report");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel29.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, 160, -1));

        jPanel26.add(jPanel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 520, 350, 160));

        jPanel30.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(182, 182, 182)), " add part type & model", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 12))); // NOI18N
        jPanel30.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel65.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel65.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel65.setText("New Part Type");
        jPanel30.add(jLabel65, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 100, -1));

        jLabel66.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel66.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel66.setText("New Part Model");
        jPanel30.add(jLabel66, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 100, -1));

        txtNewPartType.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jPanel30.add(txtNewPartType, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 30, 210, 20));

        txtNewPartModel.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtNewPartModel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNewPartModelActionPerformed(evt);
            }
        });
        jPanel30.add(txtNewPartModel, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 60, 210, 20));

        btnAdMngAddModel.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnAdMngAddModel.setText("Add Model");
        jPanel30.add(btnAdMngAddModel, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 90, 130, -1));

        btnAdMngAddType.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnAdMngAddType.setText("Add Type");
        btnAdMngAddType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdMngAddTypeActionPerformed(evt);
            }
        });
        jPanel30.add(btnAdMngAddType, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 90, 120, -1));

        jPanel26.add(jPanel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 390, 350, 130));

        jPanel31.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(182, 182, 182)), " payment ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 12))); // NOI18N
        jPanel31.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Service ID", "Total", "Payment", "Balance", "Bank", "Cheque No", "Date", "Next Service"
            }
        ));
        jScrollPane9.setViewportView(jTable1);

        jPanel31.add(jScrollPane9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 930, 320));

        jPanel26.add(jPanel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 330, 950, 350));

        jPanel32.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(182, 182, 182)), " service ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 12))); // NOI18N
        jPanel32.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Part Name", "Part Type", "Part Model", "Qty", "Price Rs."
            }
        ));
        jScrollPane10.setViewportView(jTable2);

        jPanel32.add(jScrollPane10, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 730, 290));

        jPanel26.add(jPanel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 10, 750, 320));

        jPanel33.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(182, 182, 182)), " parts ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 12))); // NOI18N
        jPanel33.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Part Name", "Part Type", "Part Model"
            }
        ));
        jScrollPane11.setViewportView(jTable3);

        jPanel33.add(jScrollPane11, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 330, 290));

        jPanel26.add(jPanel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 350, 320));

        jPanel34.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(182, 182, 182)), " search service ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 12))); // NOI18N
        jPanel34.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel68.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel68.setText("Service ID");
        jPanel34.add(jLabel68, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 23, 70, -1));

        jComboBox4.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel34.add(jComboBox4, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 20, 150, -1));

        jButton6.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jButton6.setText("Search");
        jPanel34.add(jButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 20, 90, -1));

        jPanel26.add(jPanel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 330, 350, 60));

        jPanel35.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(182, 182, 182)), " quotation ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 12))); // NOI18N
        jPanel35.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lstQuotation.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lstQuotation.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane12.setViewportView(lstQuotation);

        jPanel35.add(jScrollPane12, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 170, 140));

        jTextField2.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jPanel35.add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 260, 170, 20));

        jTextField3.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jPanel35.add(jTextField3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 215, 170, 20));

        jLabel57.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel57.setText("service charge");
        jPanel35.add(jLabel57, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 195, -1, -1));

        jLabel58.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel58.setText("total");
        jPanel35.add(jLabel58, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 240, -1, -1));

        jComboBox1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel35.add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 170, -1));

        jPanel26.add(jPanel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 10, 190, 290));

        btnAdvMngCreateBill.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnAdvMngCreateBill.setText("Create Bill");
        jPanel26.add(btnAdvMngCreateBill, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 304, 190, -1));

        jTabbedPane1.addTab("Advance Management", jPanel26);

        getContentPane().add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 1336, 723));
        jTabbedPane1.getAccessibleContext().setAccessibleDescription("Part managements and view all parts");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    //clear the text fields
    void clearFields() {
        txtPartName.setText("");
        txtStockPartName.setText("");
        txtStockDealerName.setText("");
        txtStockCostRs.setText("");
        txtStockCostCent.setText("00");
        txtStockPriceRs.setText("");
        txtStockPriceCent.setText("00");
        txtStockDiscount.setText("");
        txtStockQuantity.setText("");
    }

    //set the text field length
    void limitTextField() {

        txtStockCostCent.setDocument(new TextField(2));
        txtStockCostCent.setText("00");

        txtStockPriceCent.setDocument(new TextField(2));
        txtStockPriceCent.setText("00");

        txtPurchasesPaymentCent.setDocument(new TextField(2));
        txtPurchasesPaymentCent.setText("00");
    }

    void loadTextFields() {
        Customer sc = dbOps.getSelectedCustomer();
        lblQuotationlCusName.setText(sc.getName());
        lblQuotationlContact.setText(sc.getContact());
        lblQuotationAddress.setText(sc.getAddress());

        Double Total = dbOps.getQuotationTotal() + svc_chrge;
        lblQuotationTotal.setText(String.valueOf(Total));
        lblServiceCharge.setText(String.valueOf(svc_chrge));
    }

    /**
     * load all drop downs values
     */
    void loadDropDowns() {

        /**
         * this for Part Management tab get values to the drop downs from "part"
         * table
         */
        String[] partID = dbOps.dropDownManage("part_id", "part");
        String[] partName = dbOps.dropDownManage("part_name", "part");
        String[] partType = dbOps.dropDownManage("type", "part");
        String[] partModel = dbOps.dropDownManage("model", "part");
        String[] partStock = dbOps.dropDownManage("stock", "part");

        if (partID != null) {   // this validation for, when the dropDownManage() throws exception
            ddPartID.setModel(new javax.swing.DefaultComboBoxModel(partID));
        }
        if (partName != null) { //this validation for, when the dropDownManage() throws exception
            ddName.setModel(new javax.swing.DefaultComboBoxModel(partName));
        }
        if (partType != null) {   // this validation for, when the dropDownManage() throws exception
            ddType.setModel(new javax.swing.DefaultComboBoxModel(partType));
            ddPurchasesSearchPartType.setModel(new javax.swing.DefaultComboBoxModel(partType));
        }
        if (partModel != null) {   // this validation for, when the dropDownManage() throws exception
            ddModel.setModel(new javax.swing.DefaultComboBoxModel(partModel));
            ddPurchasesPartModel.setModel(new javax.swing.DefaultComboBoxModel(partModel));
        }
        if (partStock != null) {   // this validation for, when the dropDownManage() throws exception
            ddStock.setModel(new javax.swing.DefaultComboBoxModel(partStock));
        }

        /**
         * this for Stock Management tab get values to the drop downs from
         * "stock" table and "dealer" table this also for purchases tab
         */
        String[] dealerID = dbOps.dropDownManage("dealer_id", "dealer");
        String[] dealerName = dbOps.dropDownManage("dealer_name", "dealer");
        String[] stockID = dbOps.dropDownManage("stock_id", "stock");

        if (partID != null) {   // this validation for, when the dropDownManage() throws exception
            ddStockPartID.setModel(new javax.swing.DefaultComboBoxModel(partID));
            ddStockSearchPartID.setModel(new javax.swing.DefaultComboBoxModel(partID));
            ddStockSearchPartID1.setModel(new javax.swing.DefaultComboBoxModel(partID));
        }
        if (dealerID != null) {   // this validation for, when the dropDownManage() throws exception
            ddStockDealerID.setModel(new javax.swing.DefaultComboBoxModel(dealerID));
            ddStockSearchDealerID.setModel(new javax.swing.DefaultComboBoxModel(dealerID));
        }
        if (stockID != null) {   // this validation for, when the dropDownManage() throws exception
            ddStockID.setModel(new javax.swing.DefaultComboBoxModel(stockID));
            ddStockStockID1.setModel(new javax.swing.DefaultComboBoxModel(stockID));
        }
        if (partName != null) {   // this validation for, when the dropDownManage() throws exception
            ddStockSearchPartName.setModel(new javax.swing.DefaultComboBoxModel(partName));
            ddSearchPartName.setModel(new javax.swing.DefaultComboBoxModel(partName));
            ddPurchasesSearchPartName.setModel(new javax.swing.DefaultComboBoxModel(partName));
            ddStockSearchPartName1.setModel(new javax.swing.DefaultComboBoxModel(partName));
        }
        if (dealerName != null) {   // this validation for, when the dropDownManage() throws exception
            ddStockSearchDealerName.setModel(new javax.swing.DefaultComboBoxModel(dealerName));
            ddSearchDealerName.setModel(new javax.swing.DefaultComboBoxModel(dealerName));
        }
    }

    private void txtPartIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPartIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPartIdActionPerformed

    private void btnAddPartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddPartActionPerformed

        if (txtPartId.getText().equals("XXXXXXX")) {
            JOptionPane.showMessageDialog(this, "You can not add new part!");
        } else if (txtPartName.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Please enter part name!");
        } else {
            //create new Part and assign date
            Part part = new Part();
            part.setId(txtPartId.getText());
            part.setName(txtPartName.getText());
            part.setType(ddAddType.getSelectedItem().toString());
            part.setModel(ddAddModel.getSelectedItem().toString());
            part.setStock(ddAddStock.getSelectedItem().toString());

            //add above part already exists the system
            boolean result = dbOps.isPartExists(part);

            if (!result) {
                //add above new part to the db
                result = dbOps.addPart(part);

                if (result) {
                    JOptionPane.showMessageDialog(this, "Part added successfully!");
                    this.clearFields();
                    this.loadAllTables();
                    this.loadDropDowns();
                    //this.dispose(); current window will disappear
                } else {
                    JOptionPane.showMessageDialog(this, "Error occured!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "This part is already in the system!");
            }
        }

        // create new part id for next part
        String partID = dbOps.getMaxRecord("part_id", "part"); //get the last part id
        if (partID != null) {
            partID = idc.createPartID(partID);
            txtPartId.setText(partID); //set new part id to the add section text field
        } else {
            txtPartId.setText("XXXXXXX");
        }
    }//GEN-LAST:event_btnAddPartActionPerformed

    private void cbPartNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbPartNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbPartNameActionPerformed

    private void btnUpdatePartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdatePartActionPerformed
        int row = tblParts.getSelectedRow();
        if (row != -1) {
            UpdatePart up = new UpdatePart();
            up.setVisible(true);
            up.setFields(pList.get(tblParts.getSelectedRow()));

            up.addWindowListener(new WindowListener() {

                @Override
                public void windowOpened(WindowEvent e) {

                }

                @Override
                public void windowClosing(WindowEvent e) {

                }

                @Override
                public void windowClosed(WindowEvent e) {
                    loadAllTables();
                    loadDropDowns();
                }

                @Override
                public void windowIconified(WindowEvent e) {

                }

                @Override
                public void windowDeiconified(WindowEvent e) {

                }

                @Override
                public void windowActivated(WindowEvent e) {

                }

                @Override
                public void windowDeactivated(WindowEvent e) {

                }
            });
        } else {
            JOptionPane.showMessageDialog(this, "Please select a row");
        }

    }//GEN-LAST:event_btnUpdatePartActionPerformed

    private void btnDeletePartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeletePartActionPerformed
        int row = tblParts.getSelectedRow();
        if (row != -1) { // check a row is selected
            boolean check = dbOps.checkPartInStockTable(pList.get(tblParts.getSelectedRow()));
            if (!check) { // check the selected part is in the stock

                String table = "part";
                String column = "part_id";
                String value = pList.get(tblParts.getSelectedRow()).getId();

                boolean result = dbOps.deleteRecord(table, column, value); // delete part
                if (result) { // check the selected part successfully deleted
                    JOptionPane.showMessageDialog(this, "Part is successfully deleted!");
                    loadAllTables();
                    loadDropDowns();
                } else {
                    JOptionPane.showMessageDialog(this, "Error occured!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "You can't delete this part. This part is already in the stock!");
            }

        } else {
            JOptionPane.showMessageDialog(this, "Please select a row to delete");
        }

        // create new part id for next part part management tab
        String partID = dbOps.getMaxRecord("part_id", "part"); //get the last part id
        if (partID != null) {
            partID = idc.createPartID(partID);
            txtPartId.setText(partID); //set new part id to the add section text field
        } else {
            txtPartId.setText("XXXXXXX");
        }
    }//GEN-LAST:event_btnDeletePartActionPerformed

    private void cbModelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbModelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbModelActionPerformed

    private void btnSearchPartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchPartActionPerformed
        int count = 0;
        if (cbPartID.isSelected()) {
            count += 1;
        }
        if (cbPartName.isSelected()) {
            count += 1;
        }
        if (cbType.isSelected()) {
            count += 1;
        }
        if (cbModel.isSelected()) {
            count += 1;
        }
        if (cbStock.isSelected()) {
            count += 1;
        }

        if (count > 0) {
            String[] column = new String[count];
            String[] value = new String[count];

            int index = 0;
            if (cbPartID.isSelected()) {
                column[index] = "part_id";
                value[index] = ddPartID.getSelectedItem().toString();
                index += 1;
            }
            if (cbPartName.isSelected()) {
                column[index] = "part_name";
                value[index] = ddName.getSelectedItem().toString();
                index += 1;
            }
            if (cbType.isSelected()) {
                column[index] = "type";
                value[index] = ddType.getSelectedItem().toString();
                index += 1;
            }
            if (cbModel.isSelected()) {
                column[index] = "model";
                value[index] = ddModel.getSelectedItem().toString();
                index += 1;
            }
            if (cbStock.isSelected()) {
                column[index] = "stock";
                value[index] = ddStock.getSelectedItem().toString();
                index += 1;
            }

            if (count == 0) {
                JOptionPane.showMessageDialog(this, "Please select part details to search!");
            } else {
                pList = dbOps.searchPart(column, value);
                if (pList == null) {
                    JOptionPane.showMessageDialog(this, "Selected part is not exists the system!");
                    this.loadTablePart();
                } else {
                    TableParts pDetails = new TableParts(pList);
                    tblParts.setModel(pDetails);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select part details to search!");
        }
    }//GEN-LAST:event_btnSearchPartActionPerformed

    private void btnViewAllPartsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewAllPartsActionPerformed
        this.loadTablePart();
        this.loadDropDowns();
    }//GEN-LAST:event_btnViewAllPartsActionPerformed

    private void cbStockDealerIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbStockDealerIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbStockDealerIDActionPerformed

    private void btnStockSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStockSearchActionPerformed
        // search stock
        int count = 0;
        if (cbStockDate.isSelected()) {
            count += 1;
        }
        if (cdStockID.isSelected()) {
            count += 1;
        }
        if (cbStockPartName.isSelected()) {
            count += 1;
        }
        if (cbStockPartID.isSelected()) {
            count += 1;
        }
        if (cbStockDealerName.isSelected()) {
            count += 1;
        }
        if (cbStockDealerID.isSelected()) {
            count += 1;
        }

        if (count > 0) {
            String[] column = new String[count];
            String[] value = new String[count];
            String[] table = new String[count];

            int index = 0;
            if (cbStockDate.isSelected()) {

                //convert util date to sql date
                java.util.Date date = new java.util.Date(ddStockSearchDate.getDate().getTime());
                java.sql.Date sqlDate = new java.sql.Date(date.getTime());

                column[index] = "date";
                value[index] = sqlDate.toString();
                table[index] = "stock";
                index += 1;
            }
            if (cdStockID.isSelected()) {
                column[index] = "stock_id";
                value[index] = ddStockID.getSelectedItem().toString();
                table[index] = "stock";
                index += 1;
            }
            if (cbStockPartName.isSelected()) {
                column[index] = "part_name";
                value[index] = ddStockSearchPartName.getSelectedItem().toString();
                table[index] = "part";
                index += 1;
            }
            if (cbStockPartID.isSelected()) {
                column[index] = "part_id";
                value[index] = ddStockSearchPartID.getSelectedItem().toString();
                table[index] = "part";
                index += 1;
            }
            if (cbStockDealerName.isSelected()) {
                column[index] = "dealer_name";
                value[index] = ddStockSearchDealerName.getSelectedItem().toString();
                table[index] = "dealer";
                index += 1;
            }
            if (cbStockDealerID.isSelected()) {
                column[index] = "dealer_id";
                value[index] = ddStockSearchDealerID.getSelectedItem().toString();
                table[index] = "dealer";
                index += 1;
            }

            if (count == 0) {
                JOptionPane.showMessageDialog(this, "Please select stock details to search!");
            } else {
                sList = dbOps.searchStock(column, value, table);
                if (sList == null) {
                    JOptionPane.showMessageDialog(this, "Selected stock is not exists the system!");
                    this.loadTablePart();
                } else {
                    TableStock sDetails = new TableStock(sList);
                    tblStock.setModel(sDetails);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select stock details to search!");
        }
    }//GEN-LAST:event_btnStockSearchActionPerformed

    private void btnStockSearchPartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStockSearchPartActionPerformed
        String[] column = {"part_name"};
        String[] value = {ddSearchPartName.getSelectedItem().toString()};
        pList = dbOps.searchPart(column, value);

        TableStockParts pDetails = new TableStockParts(pList);
        tblStockPart.setModel(pDetails);
    }//GEN-LAST:event_btnStockSearchPartActionPerformed

    private void ddSearchPartNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ddSearchPartNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ddSearchPartNameActionPerformed

    private void btnStockAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStockAddActionPerformed
        if (txtStockID.getText().equals("XXXXXXXXXX")) { // check, can add new stock? 
            JOptionPane.showMessageDialog(this, "You can not add new stock!");
        } else {
            // check, did select part id and dealer id
            if (txtStockPartName.getText().equals("") || txtStockDealerName.getText().equals("")) {
                JOptionPane.showMessageDialog(this, "Please select part id and dealer id!");
            } else {
                if (txtStockCostRs.getText().equals("") // check all input fields filled
                        || txtStockCostCent.getText().equals("")
                        || txtStockPriceRs.getText().equals("")
                        || txtStockPriceCent.getText().equals("")
                        || txtStockDiscount.getText().equals("")
                        || txtStockQuantity.getText().equals("")) {
                    JOptionPane.showMessageDialog(this, "Please enter all details!");
                } else {

                    // create cost, price, discount and quantity as string
                    String stCost = txtStockCostRs.getText() + "." + txtStockCostCent.getText();
                    String stPrice = txtStockPriceRs.getText() + "." + txtStockPriceCent.getText();
                    String stDiscount = txtStockDiscount.getText();
                    String stQuantity = txtStockQuantity.getText();

                    try {   // check cost, price, discount and quantity correctly entered
                        // convert above string values to double and integer
                        double cost = Double.parseDouble(stCost);
                        double price = Double.parseDouble(stPrice);
                        double discount = Double.parseDouble(stDiscount);
                        int quantity = Integer.parseInt(stQuantity);

                        //create new stock object and add values
                        Stock stock = new Stock();
                        stock.setStockID(txtStockID.getText());
                        stock.setPartID(ddStockPartID.getSelectedItem().toString());
                        stock.setDealerID(ddStockDealerID.getSelectedItem().toString());
                        stock.setCost(cost);
                        stock.setPrice(price);
                        stock.setDiscount(discount);
                        stock.setQuantity(quantity);
                        stock.setAddDate(ddStockAddDate.getDate());

                        //add above new stock to the db
                        boolean result = dbOps.addStock(stock);

                        if (result) {
                            JOptionPane.showMessageDialog(this, "New stock added successfully!");
                            this.clearFields();
                            this.loadAllTables(); // load stock table in stock management tab
                            this.loadDropDowns();
                            this.clearFields();
                            //this.dispose(); current window will disappear

                            // create new part id for next part part management tab
                            String stockID = dbOps.getMaxRecord("stock_id", "stock"); //get the last part id
                            if (stockID != null) {
                                stockID = idc.createStockID(stockID);
                                txtStockID.setText(stockID); //set new part id to the add section text field
                            } else {
                                txtStockID.setText("XXXXXXX");
                            }

                        } else {
                            JOptionPane.showMessageDialog(this, "Error occured!");
                        }
                    } catch (Exception e) {
                        System.out.println(e);
                        JOptionPane.showMessageDialog(this, "Please enter below details correctly"
                                + "\nCost"
                                + "\nPrice"
                                + "\nDiscount"
                                + "\nQuantity");
                    }
                }
            }
        }
    }//GEN-LAST:event_btnStockAddActionPerformed

    private void btnViewAllStocksActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewAllStocksActionPerformed
        loadTableStock();
        loadDropDowns();
    }//GEN-LAST:event_btnViewAllStocksActionPerformed

    private void btnStockUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStockUpdateActionPerformed
        int row = tblStock.getSelectedRow();
        if (row != -1) {
            UpdateStock us = new UpdateStock();
            us.setVisible(true);
            us.loadAllDropDowns(); //load all drop downs
            us.setFields(sList.get(tblStock.getSelectedRow()));
            us.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

            us.addWindowListener(new WindowListener() {

                @Override
                public void windowOpened(WindowEvent e) {

                }

                @Override
                public void windowClosing(WindowEvent e) {

                }

                @Override
                public void windowClosed(WindowEvent e) {
                    loadAllTables();
                    loadDropDowns();
                }

                @Override
                public void windowIconified(WindowEvent e) {

                }

                @Override
                public void windowDeiconified(WindowEvent e) {

                }

                @Override
                public void windowActivated(WindowEvent e) {

                }

                @Override
                public void windowDeactivated(WindowEvent e) {

                }
            });
        } else {
            JOptionPane.showMessageDialog(this, "Please select a row");
        }
    }//GEN-LAST:event_btnStockUpdateActionPerformed

    private void btnStockDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStockDeleteActionPerformed
        int row = tblStock.getSelectedRow();
        if (row != -1) { // check a row is selected

            String table = "stock";
            String column = "stock_id";
            String value = sList.get(tblStock.getSelectedRow()).getStockID();

            boolean result = dbOps.deleteRecord(table, column, value); //delete stock record
            if (result) { // check the selected part successfully deleted
                JOptionPane.showMessageDialog(this, "Stock is successfully deleted!");
                loadAllTables();
                loadDropDowns();
            } else {
                JOptionPane.showMessageDialog(this, "Error occured!");
            }

        } else {
            JOptionPane.showMessageDialog(this, "Please select a row to delete");
        }

        // create new part id for next part part management tab
        String stockID = dbOps.getMaxRecord("stock_id", "stock"); //get the last part id
        if (stockID != null) {
            stockID = idc.createStockID(stockID);
            txtStockID.setText(stockID); //set new part id to the add section text field
        } else {
            txtStockID.setText("XXXXXXX");
        }
    }//GEN-LAST:event_btnStockDeleteActionPerformed

    private void btnRefreshDealersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshDealersActionPerformed
        loadTableStockDealer();
    }//GEN-LAST:event_btnRefreshDealersActionPerformed

    private void btnRefreshPartsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshPartsActionPerformed
        loadTableStockPart();
    }//GEN-LAST:event_btnRefreshPartsActionPerformed

    private void btnStockAddDealerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStockAddDealerActionPerformed

        AddDealer ad = new AddDealer();
        ad.setVisible(true);
        ad.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        ad.addWindowListener(new WindowListener() {

            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {

            }

            @Override
            public void windowClosed(WindowEvent e) {
                loadAllTables();
                loadDropDowns();
            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });
    }//GEN-LAST:event_btnStockAddDealerActionPerformed

    private void btnStockUpdateDealerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStockUpdateDealerActionPerformed
        int row = tblStockDealer.getSelectedRow();
        if (row != -1) {
            UpdateDealer ud = new UpdateDealer();
            ud.setVisible(true);
            ud.setFields(dList.get(tblStockDealer.getSelectedRow()));
            ud.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

            ud.addWindowListener(new WindowListener() {

                @Override
                public void windowOpened(WindowEvent e) {

                }

                @Override
                public void windowClosing(WindowEvent e) {

                }

                @Override
                public void windowClosed(WindowEvent e) {
                    loadAllTables();
                    loadDropDowns();
                }

                @Override
                public void windowIconified(WindowEvent e) {

                }

                @Override
                public void windowDeiconified(WindowEvent e) {

                }

                @Override
                public void windowActivated(WindowEvent e) {

                }

                @Override
                public void windowDeactivated(WindowEvent e) {

                }
            });
        } else {
            JOptionPane.showMessageDialog(this, "Please select a row");
        }
    }//GEN-LAST:event_btnStockUpdateDealerActionPerformed

    private void btnStockSearchDealerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStockSearchDealerActionPerformed
        String[] column = {"dealer_name"};
        String[] value = {ddSearchDealerName.getSelectedItem().toString()};
        dList = dbOps.searchDealer(column, value);

        TableStockDealer dDetails = new TableStockDealer(dList);
        tblStockDealer.setModel(dDetails);

        //Set the table column size
        tblStockDealer.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tblStockDealer.getColumnModel().getColumn(0).setPreferredWidth(80);
        tblStockDealer.getColumnModel().getColumn(1).setPreferredWidth(178);

        //Set the table column allignment
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        tblStockDealer.getColumnModel().getColumn(0).setCellRenderer(rightRenderer);
    }//GEN-LAST:event_btnStockSearchDealerActionPerformed

    private void btnStockDeleteDealerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStockDeleteDealerActionPerformed
        int row = tblStockDealer.getSelectedRow();
        if (row != -1) { // check a row is selected
            boolean check = dbOps.checkDealerInStockTable(dList.get(tblStockDealer.getSelectedRow()));
            if (!check) { // check the selected dealer is in the stock

                String table = "dealer";
                String column = "dealer_id";
                String value = dList.get(tblStockDealer.getSelectedRow()).getId();

                boolean result = dbOps.deleteRecord(table, column, value); // delete dealer
                if (result) { // check the selected dealer successfully deleted
                    JOptionPane.showMessageDialog(this, "Dealer is successfully deleted!");
                    loadAllTables();
                    loadDropDowns();
                } else {
                    JOptionPane.showMessageDialog(this, "Error occured!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "You can't delete this Dealer. This dealer is already in the stock!");
            }

        } else {
            JOptionPane.showMessageDialog(this, "Please select a row to delete");
        }
    }//GEN-LAST:event_btnStockDeleteDealerActionPerformed

    private void btnDealerDetailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDealerDetailsActionPerformed
        int row = tblStockDealer.getSelectedRow();
        if (row != -1) {
            DetailsDealer dd = new DetailsDealer();
            dd.setVisible(true);
            dd.setLabels(dList.get(tblStockDealer.getSelectedRow()));
            dd.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        } else {
            JOptionPane.showMessageDialog(this, "Please select a row");
        }
    }//GEN-LAST:event_btnDealerDetailsActionPerformed

    private void btnStockDetailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStockDetailsActionPerformed
        int row = tblStock.getSelectedRow();
        if (row != -1) {
            DetailsStock ds = new DetailsStock();
            ds.setVisible(true);
            ds.setLabels(sList.get(tblStock.getSelectedRow()));
            ds.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        } else {
            JOptionPane.showMessageDialog(this, "Please select a row");
        }
    }//GEN-LAST:event_btnStockDetailsActionPerformed

    private void btnPurchasesSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPurchasesSearchActionPerformed
        // search stock
        int count = 0;
        if (cbPurchasesDate.isSelected()) {
            count += 1;
        }
        if (cbPurchasesPartModel.isSelected()) {
            count += 1;
        }
        if (cbPurchasesPartName.isSelected()) {
            count += 1;
        }
        if (cbPurchasesPartType.isSelected()) {
            count += 1;
        }

        if (count > 0) {
            String[] column = new String[count];
            String[] value = new String[count];
            String[] table = new String[count];

            int index = 0;
            if (cbPurchasesDate.isSelected()) {

                //convert util date to sql date
                java.util.Date date = new java.util.Date(ddPurchasesSearchDate.getDate().getTime());
                java.sql.Date sqlDate = new java.sql.Date(date.getTime());

                column[index] = "date";
                value[index] = sqlDate.toString();
                table[index] = "stock";
                index += 1;
            }
            if (cbPurchasesPartModel.isSelected()) {
                column[index] = "model";
                value[index] = ddPurchasesPartModel.getSelectedItem().toString();
                table[index] = "part";
                index += 1;
            }
            if (cbPurchasesPartName.isSelected()) {
                column[index] = "part_name";
                value[index] = ddPurchasesSearchPartName.getSelectedItem().toString();
                table[index] = "part";
                index += 1;
            }
            if (cbPurchasesPartType.isSelected()) {
                column[index] = "type";
                value[index] = ddPurchasesSearchPartType.getSelectedItem().toString();
                table[index] = "part";
                index += 1;
            }

            if (count == 0) {
                JOptionPane.showMessageDialog(this, "Please select stock details to search!");
            } else {
                sList = dbOps.searchStock(column, value, table);
                if (sList == null) {
                    JOptionPane.showMessageDialog(this, "Selected stock is not exists the system!");
                    this.loadTablePurchasesStock();
                } else {
                    TablePurchasesStock psDetails = new TablePurchasesStock(sList);
                    tblPurchasesStock.setModel(psDetails);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select stock details to search!");
        }
    }//GEN-LAST:event_btnPurchasesSearchActionPerformed

    private void btnPurchasesViewAllStocksActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPurchasesViewAllStocksActionPerformed
        loadTablePurchasesStock();
        loadDropDowns();
    }//GEN-LAST:event_btnPurchasesViewAllStocksActionPerformed

    private void btnCalculateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalculateActionPerformed
        String stPayment = txtPurchasesPaymentRs.getText() + "." + txtPurchasesPaymentCent.getText();
        try {
            payment = Double.parseDouble(stPayment);
            balance = cal.calculateBalance(purchasesTotal, payment);
            lblBalance.setText(balance + "");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Please enter payment correctly!");
        }
    }//GEN-LAST:event_btnCalculateActionPerformed

    private void ddStockPartIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ddStockPartIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ddStockPartIDActionPerformed

    private void btnAddOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddOrderActionPerformed
        try {
            String stQty = spnPurchasesOrderQuantity.getValue().toString();
            int qty = Integer.parseInt(stQty);

            if (qty > 0) {
                int row = tblPurchasesStock.getSelectedRow();

                if (row != -1) {

                    int result = dbOps.addNewPartToOrder(sList.get(row), qty);

                    if (result == 2) {
                        JOptionPane.showMessageDialog(this, "There are not enough stock quantity!");
                        spnPurchasesOrderQuantity.setValue(0);
                    } else if (result == 3) {
                        JOptionPane.showMessageDialog(this, "Error occurred!");
                        spnPurchasesOrderQuantity.setValue(0);
                    } else {
                        loadAllTables();
                        spnPurchasesOrderQuantity.setValue(0);
                    }

                } else {
                    JOptionPane.showMessageDialog(this, "Please select a row");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please enter quantity as a positive number");
                spnPurchasesOrderQuantity.setValue(0);
            }

        } catch (ExceptionInInitializerError e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(this, "Please enter quantity as a number");
            spnPurchasesOrderQuantity.setValue(0);
        }
    }//GEN-LAST:event_btnAddOrderActionPerformed

    private void btnClearOrderTableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearOrderTableActionPerformed
        int stockQty = 0;
        int orderQty = 0;
        for (int i = 0; i < soList.size(); i++) {
            stockQty = dbOps.searchIntRecord("quantity", "stock_id", soList.get(i).getStockID(), "stock");
            orderQty = dbOps.searchIntRecord("quantity", "stock_id", soList.get(i).getStockID(), "temp_order");
            soList.get(i).setQuantity(stockQty + orderQty);
            dbOps.updateStock(soList.get(i));
        }
        boolean result = dbOps.deleteAllRecord("temp_order");
        if (result) {
            loadAllTables();
            JOptionPane.showMessageDialog(this, "Successfully clean the order table!");
        } else {
            JOptionPane.showMessageDialog(this, "Error occurred while deleting a order table!");
        }
    }//GEN-LAST:event_btnClearOrderTableActionPerformed

    private void btnPurchasesChangeQuantityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPurchasesChangeQuantityActionPerformed
        try {
            String stQty = spnPurchasesUpdateQuantity.getValue().toString();
            int qty = Integer.parseInt(stQty);

            int row = tblPurchasesOrder.getSelectedRow();

            if (row != -1) {

                String comparison = "";
                if (qty > 0) {
                    int stockQty = dbOps.searchIntRecord("quantity", "stock_id", soList.get(row).getStockID(), "stock");
                    if (stockQty >= qty) {
                        comparison = "Positive";
                    } else {
                        comparison = "ErrorPositive";
                    }
                } else if (qty < 0) {
                    int orderQty = dbOps.searchIntRecord("quantity", "stock_id", soList.get(row).getStockID(), "temp_order");
                    if (orderQty > -(qty)) {
                        comparison = "Negative";
                    } else if (orderQty == -(qty)) {
                        comparison = "NegativeEqual";
                    } else {
                        comparison = "NegativeError";
                    }
                } else { //qty = 0
                    comparison = "Error";
                }

                //update order table
                if (comparison.equals("Positive")) {
                    dbOps.addNewPartToOrder(soList.get(row), qty);
                    loadAllTables();
                    spnPurchasesUpdateQuantity.setValue(0);

                } else if (comparison.equals("ErrorPositive")) {
                    JOptionPane.showMessageDialog(this, "There are not enough stock quantity to update!");

                } else if (comparison.equals("Negative")) {
                    dbOps.addNewPartToOrder(soList.get(row), qty);
                    loadAllTables();
                    spnPurchasesUpdateQuantity.setValue(0);

                } else if (comparison.equals("NegativeEqual")) {
                    dbOps.addNewPartToOrder(soList.get(row), qty);
                    dbOps.deleteRecord("temp_order", "stock_id", soList.get(row).getStockID());
                    loadAllTables();
                    spnPurchasesUpdateQuantity.setValue(0);

                } else if (comparison.equals("NegativeError")) {
                    JOptionPane.showMessageDialog(this, "There are not enough order quantity to reduce!");

                } else if (comparison.equals("Error")) {
                    JOptionPane.showMessageDialog(this, "Please enter quantity to update!");

                } else {
                    JOptionPane.showMessageDialog(this, "Error occured!");
                }

            } else {
                JOptionPane.showMessageDialog(this, "Please select a row");
            }

        } catch (ExceptionInInitializerError e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(this, "Please enter quantity as a number");
            spnPurchasesOrderQuantity.setValue(0);
        }
    }//GEN-LAST:event_btnPurchasesChangeQuantityActionPerformed

    private void btnRemovePartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemovePartActionPerformed
        int row = tblPurchasesOrder.getSelectedRow();

        if (row != -1) {
            int orderQty = dbOps.searchIntRecord("quantity", "stock_id", soList.get(row).getStockID(), "temp_order");
            dbOps.addNewPartToOrder(soList.get(row), -(orderQty));
            dbOps.deleteRecord("temp_order", "stock_id", soList.get(row).getStockID());
            loadAllTables();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a row");
        }
    }//GEN-LAST:event_btnRemovePartActionPerformed

    private void btnPurchasesAddServiceChargeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPurchasesAddServiceChargeActionPerformed
        if (serviceCharge == 0) {
            String stCharger = JOptionPane.showInputDialog(this, "Enter service charge Rs."); //get the input value
            if (stCharger != null) { //if doesn't clicked the cancel button
                try {
                    serviceCharge = Double.parseDouble(stCharger);
                    loadTablePurchasesOrder(); // this display the total with service charge
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Enter a number as the service charge!");
                }
            }

        } else {
            JOptionPane.showMessageDialog(this, "Service charge is already added!");
        }
    }//GEN-LAST:event_btnPurchasesAddServiceChargeActionPerformed

    private void btnPurchasesRemoveServiceChargeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPurchasesRemoveServiceChargeActionPerformed
        if (serviceCharge != 0) {
            serviceCharge = 0;
            loadTablePurchasesOrder();
            JOptionPane.showMessageDialog(this, "Service charge is removed!");
        } else {
            JOptionPane.showMessageDialog(this, "Didn't add a service charge to remove!");
        }
    }//GEN-LAST:event_btnPurchasesRemoveServiceChargeActionPerformed

    private void btnCreateBillActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateBillActionPerformed
        //create a new part id for next new part
        String serviceID = "";
        int orderRows = dbOps.checkTheTableEmpty("payment"); //check the no of row of table
        if (orderRows == 0) {
            serviceID = "SVC0000001";
        } else if (orderRows > 0) {
            serviceID = dbOps.getMaxRecord("service_id", "payment"); //get the last part id
            if (serviceID != null) {
                serviceID = idc.createServiceID(serviceID);
            } else {
                serviceID = "XXXXXXXXXX";
            }
        } else {
            serviceID = "XXXXXXXXXX";
        }

        ArrayList<Service> serviceList = new ArrayList<Service>();
        for (Stock stock : soList) {
            Service service = new Service();
            service.setServiceID(serviceID);
            service.setStockID(stock.getStockID());
            service.setQuantity(stock.getQuantity());
            double discountPrice = (stock.getPrice() * (100 - stock.getDiscount())) / 100;
            service.setDiscountPrice(discountPrice);
            serviceList.add(service);
        }

        Payment payment = new Payment();
        payment.setServiceID(serviceID);
        payment.setTotal(purchasesTotal);
        if (ddPaymentType.getSelectedItem().equals("Cash")) {
            String p = txtPurchasesPaymentRs.getText() + "." 
                    + txtPurchasesPaymentCent.getText();
            try{
                double pay = Double.parseDouble(p);
                payment.setPayment(pay);
                double balance = pay - purchasesTotal;
                payment.setBalance(balance);
                payment.setType(ddPaymentType.getSelectedItem().toString());
                payment.setDate(new java.util.Date());
                for(Service svc : serviceList){
                    dbOps.addService(svc);
                }
                if(dbOps.addPayment(payment)){
                    JOptionPane.showMessageDialog(this, "Successful!");
                }else{
                    JOptionPane.showMessageDialog(this, "Error!");
                }
            }catch(Exception e){
                JOptionPane.showMessageDialog(this, "Please enter payment correctly!");
            }
        } else {
            if (!txtPurchasesChequeNo.equals("")) {
                payment.setChequeNo(txtPurchasesChequeNo.getText());
                payment.setBank(txtPurchasesBank.getText());
                payment.setType(ddPaymentType.getSelectedItem().toString());
                payment.setDate(new java.util.Date());
                for(Service svc : serviceList){
                    dbOps.addService(svc);
                }
                if(dbOps.addPayment(payment)){
                    JOptionPane.showMessageDialog(this, "Successful!");
                }else{
                    JOptionPane.showMessageDialog(this, "Error!");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please enter cheque number!");
            }
        }
    }//GEN-LAST:event_btnCreateBillActionPerformed

    private void btnQuotationSearchQtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuotationSearchQtnActionPerformed
        // TODO add your handling code here:
        // search stock
        int count = 0;
        if (cbStockDate1.isSelected()) {
            count += 1;
        }
        if (cbStockID1.isSelected()) {
            count += 1;
        }
        if (cbStockPartName1.isSelected()) {
            count += 1;
        }
        if (cbStockPartID1.isSelected()) {
            count += 1;
        }

        if (count > 0) {
            String[] column = new String[count];
            String[] value = new String[count];
            String[] table = new String[count];

            int index = 0;
            if (cbStockDate1.isSelected()) {

                //convert util date to sql date
                java.util.Date date = new java.util.Date(ddQutationSearchDate.getDate().getTime());
                java.sql.Date sqlDate = new java.sql.Date(date.getTime());

                column[index] = "date";
                value[index] = sqlDate.toString();
                table[index] = "stock";
                index += 1;
            }
            if (cbStockID1.isSelected()) {
                column[index] = "stock_id";
                value[index] = ddStockStockID1.getSelectedItem().toString();
                table[index] = "stock";
                index += 1;
            }
            if (cbStockPartName1.isSelected()) {
                column[index] = "part_name";
                value[index] = ddStockSearchPartName1.getSelectedItem().toString();
                table[index] = "part";
                index += 1;
            }
            if (cbStockPartID1.isSelected()) {
                column[index] = "part_id";
                value[index] = ddStockSearchPartID1.getSelectedItem().toString();
                table[index] = "part";
                index += 1;
            }

            if (count == 0) {
                JOptionPane.showMessageDialog(this, "Please select stock details to search!");
            } else {
                sList = dbOps.searchStock(column, value, table);
                if (sList == null) {
                    JOptionPane.showMessageDialog(this, "Selected stock is not exists the system!");
                    this.loadTableStock();
                } else {
                    TableQuotationStock sDetails = new TableQuotationStock(sList);
                    tblQuotationStocks.setModel(sDetails);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select stock details to search!");
        }
    }//GEN-LAST:event_btnQuotationSearchQtnActionPerformed

    private void btnQuotationViewAllStocksActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuotationViewAllStocksActionPerformed
        // TODO add your handling code here:
        loadTableQuotationStock();
    }//GEN-LAST:event_btnQuotationViewAllStocksActionPerformed

    private void btnQuotationCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuotationCustomerActionPerformed
        // TODO add your handling code here:
        selectCustomer Sc = new selectCustomer();
        Sc.setVisible(true);
        Sc.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        Sc.addWindowListener(new WindowListener() {

            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {

            }

            @Override
            public void windowClosed(WindowEvent e) {
                loadAllTables();
                loadDropDowns();
                loadTextFields();
            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }

        });
    }//GEN-LAST:event_btnQuotationCustomerActionPerformed

    private void btnQuotationCreateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuotationCreateActionPerformed
        // TODO add your handling code here:
        if (dbOps.isEmptySelectedParts() && dbOps.isEmptySelectedCustomer()) {
            JOptionPane.showMessageDialog(this, "Please add parts and customer!");
        } else if (dbOps.isEmptySelectedParts()) {
            JOptionPane.showMessageDialog(this, "There is no any parts added for quotation!");
        } else if (dbOps.isEmptySelectedCustomer()) {
            JOptionPane.showMessageDialog(this, "Please add the customer!");
        } else {
            if (flag) {
                String Service_ID = dbOps.getMaxRecord("service_id", "service");
                if (Service_ID != null) {
                    Service_ID = idc.createServiceID(Service_ID);

                } else {
                    Service_ID = "SVC000000";
                }
                //System.out.println(Service_ID);
                spList = dbOps.getSelectedParts();

                int len = spList.size();
                int count = 0;
                for (int i = 0; i < len; i++) {
                    SelectedParts sp = new SelectedParts();
                    sp = spList.get(i);

                    boolean result = dbOps.addService(sp, Service_ID);
                    if (result) {
                        count++;
                    } else {

                    }
                }
                if (count == len) {
                    //get customer
                    Customer c = new Customer();
                    c = dbOps.getSelectedCustomer();
                    String cus_id = c.getId();

                    String username = "admin";
                    Double total = Double.parseDouble(lblQuotationTotal.getText());

                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Calendar cal = Calendar.getInstance();

                    Quotation qtn = new Quotation();
                    qtn.setService_id(Service_ID);
                    qtn.setCustomer_id(cus_id);
                    qtn.setService_charge(svc_chrge);
                    qtn.setTotal(total);
                    qtn.setDate(dateFormat.format(cal.getTime()));
                    qtn.setUsername(username);

                    boolean rslt = dbOps.addQuotation(qtn);
                    if (rslt) {
                        boolean clr = dbOps.clearSelectedParts();
                        boolean clr1 = dbOps.clearSelectedCustomer();
                        if (clr && clr1) {
                            flag = true;
                            
                            HashMap para = new HashMap(); //quotation window 
                            para.put("service_id", Service_ID);
                            ReportView rep = new ReportView("C:\\Users\\Ravindu\\Desktop\\Hemantha\\jcmotors\\src\\jcmotors\\q1.jasper", para);
                            rep.setVisible(true);
                            rep.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                            
                            JOptionPane.showMessageDialog(this, "Quotation created successfully!");
                        } else {
                            JOptionPane.showMessageDialog(this, "Error occured 3!");
                            flag = false;
                        }

                    } else {
                        JOptionPane.showMessageDialog(this, "Error occured 1!");
                    }

                } else {
                    JOptionPane.showMessageDialog(this, "Error occured 2!");
                    flag = false;
                }

            } else {
                JOptionPane.showMessageDialog(this, "Already added!");
            }
            svc_chrge = 0.00;
            loadAllTables();
            loadTextFields();
            txtServiceChargeRs.setText("0");

        }

    }//GEN-LAST:event_btnQuotationCreateActionPerformed

    private void btnQuotationClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuotationClearActionPerformed
        // TODO add your handling code here:
        boolean result = dbOps.clearSelectedParts();

        if (result) {
            JOptionPane.showMessageDialog(this, "Successfully removed all!");
        } else {
            JOptionPane.showMessageDialog(this, "Error Occured!");
        }
        spnQuotationQty.setValue(0);
        loadAllTables();
        flag = true;
    }//GEN-LAST:event_btnQuotationClearActionPerformed

    private void btnQuotationChooseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuotationChooseActionPerformed
        // TODO add your handling code here:
        int row = tblQuotationStocks.getSelectedRow();
        if (row != -1) { // check a row is selected
            Stock ss = sList.get(tblQuotationStocks.getSelectedRow());
            String qs = spnQuotationQty.getValue().toString();
            int qty = Integer.parseInt(qs);

            int availableQty = ss.getQuantity();

            if (qty > 0) {
                if (availableQty - qty >= 0) {
                    boolean result = dbOps.isSelectedPart(ss.getStockID());
                    if (!result) {
                        result = dbOps.addSelectedParts(ss, qty);
                        if (result) {
                            JOptionPane.showMessageDialog(this, "Slected Successfully!");
                        } else {
                            JOptionPane.showMessageDialog(this, "Error occured!");
                        }

                    } else {
                        result = dbOps.isQtyEnough(ss, qty);
                        if (result) {
                            result = dbOps.increaseQty(ss, qty);
                            if (result) {
                                JOptionPane.showMessageDialog(this, "Quantity incresed!");
                            } else {
                                JOptionPane.showMessageDialog(this, "Error occured!");
                            }

                        } else {
                            JOptionPane.showMessageDialog(this, "Stock is not enough!");
                        }
                        //
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Stock is not Enough!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please enter quantity");
            }
            spnQuotationQty.setValue(0);
            loadAllTables();

        } else {
            JOptionPane.showMessageDialog(this, "Please select a row");
        }
    }//GEN-LAST:event_btnQuotationChooseActionPerformed

    private void btnQuotationRemovePartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuotationRemovePartActionPerformed
        // TODO add your handling code here:
        int row = tblQuotationParts.getSelectedRow();
        if (row != -1) { // check a row is selected
            SelectedParts Sp = new SelectedParts();
            Sp = spList.get(tblQuotationParts.getSelectedRow());
            boolean result = dbOps.deleteRecord("selected_parts", "stock_id", Sp.getStock_id());

            if (result) {
                JOptionPane.showMessageDialog(this, "Removed Successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Error Occured!");
            }
            spnQuotationQty.setValue(0);
            loadAllTables();

        } else {
            JOptionPane.showMessageDialog(this, "Please select a row to delete");
        }
    }//GEN-LAST:event_btnQuotationRemovePartActionPerformed

    private void btnQuotationChangeQtyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuotationChangeQtyActionPerformed
        // TODO add your handling code here:
        int row = tblQuotationParts.getSelectedRow();
        if (row != -1) { // check a row is selected
            SelectedParts Sp = new SelectedParts();
            Sp = spList.get(tblQuotationParts.getSelectedRow());

            String qs = spnQuotationQty.getValue().toString();
            int qty = Integer.parseInt(qs);

            Stock ss = new Stock();
            ss = dbOps.getStockByID(Sp.getStock_id());

            int availableQty = ss.getQuantity();

            if (qty > 0) {
                if (availableQty - qty >= 0) {
                    boolean result = dbOps.changeQty(Sp, qty);
                    if (result) {
                        JOptionPane.showMessageDialog(this, "Quantity changed!");
                    } else {
                        JOptionPane.showMessageDialog(this, "Error occured!");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Stock is not Enough!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please enter quantity");
            }
            spnQuotationQty.setValue(0);
            loadAllTables();

        } else {
            JOptionPane.showMessageDialog(this, "Please select a row to change quantity");
        }
    }//GEN-LAST:event_btnQuotationChangeQtyActionPerformed

    private void btnQuotationAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuotationAddActionPerformed
        // TODO add your handling code here:
        try {
            if (txtServiceChargeRs.getText().length() == 0 || Integer.parseInt(txtServiceChargeRs.getText()) == 0) {
                JOptionPane.showMessageDialog(this, "Please enter service charge value!");
            } else {
                Double svc = svc_chrge;
                try {
                    svc_chrge = Integer.parseInt(txtServiceChargeRs.getText()) + Double.parseDouble(txtServiceChargeCent.getText()) / 100;
                    lblServiceCharge.setText(String.valueOf(svc_chrge));
                    if (svc != 0.0) {
                        JOptionPane.showMessageDialog(this, "Service Charge Changed!");
                    } else {
                        JOptionPane.showMessageDialog(this, "Service Charge Added Successfully!");
                    }

                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Invalid Input!");

                }

            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid Input!");
            svc_chrge = 0.00;

        }
        loadTextFields();
        loadAllTables();
        txtServiceChargeRs.setText("0");
    }//GEN-LAST:event_btnQuotationAddActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void txtNewPartModelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNewPartModelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNewPartModelActionPerformed

    private void btnAdMngAddTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdMngAddTypeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAdMngAddTypeActionPerformed

    private void txtPurchasesChequeNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPurchasesChequeNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPurchasesChequeNoActionPerformed

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
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(new HiFiLookAndFeel());
                } catch (Exception e) {
                }
                new MainFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdMngAddModel;
    private javax.swing.JButton btnAdMngAddType;
    private javax.swing.JButton btnAddOrder;
    private javax.swing.JButton btnAddPart;
    private javax.swing.JButton btnAdvMngCreateBill;
    private javax.swing.JButton btnCalculate;
    private javax.swing.JButton btnClearOrderTable;
    private javax.swing.JButton btnCreateBill;
    private javax.swing.JButton btnDealerDetails;
    private javax.swing.JButton btnDeletePart;
    private javax.swing.JButton btnPurchasesAddServiceCharge;
    private javax.swing.JButton btnPurchasesChangeQuantity;
    private javax.swing.JButton btnPurchasesRemoveServiceCharge;
    private javax.swing.JButton btnPurchasesSearch;
    private javax.swing.JButton btnPurchasesViewAllStocks;
    private javax.swing.JButton btnQuotationAdd;
    private javax.swing.JButton btnQuotationChangeQty;
    private javax.swing.JButton btnQuotationChoose;
    private javax.swing.JButton btnQuotationClear;
    private javax.swing.JButton btnQuotationCreate;
    private javax.swing.JButton btnQuotationCustomer;
    private javax.swing.JButton btnQuotationRemovePart;
    private javax.swing.JButton btnQuotationSearchQtn;
    private javax.swing.JButton btnQuotationViewAllStocks;
    private javax.swing.JButton btnRefreshDealers;
    private javax.swing.JButton btnRefreshParts;
    private javax.swing.JButton btnRemovePart;
    private javax.swing.JButton btnSearchPart;
    private javax.swing.JButton btnStockAdd;
    private javax.swing.JButton btnStockAddDealer;
    private javax.swing.JButton btnStockDelete;
    private javax.swing.JButton btnStockDeleteDealer;
    private javax.swing.JButton btnStockDetails;
    private javax.swing.JButton btnStockSearch;
    private javax.swing.JButton btnStockSearchDealer;
    private javax.swing.JButton btnStockSearchPart;
    private javax.swing.JButton btnStockUpdate;
    private javax.swing.JButton btnStockUpdateDealer;
    private javax.swing.JButton btnUpdatePart;
    private javax.swing.JButton btnViewAllParts;
    private javax.swing.JButton btnViewAllStocks;
    private javax.swing.JCheckBox cbModel;
    private javax.swing.JCheckBox cbPartID;
    private javax.swing.JCheckBox cbPartName;
    private javax.swing.JCheckBox cbPurchasesDate;
    private javax.swing.JCheckBox cbPurchasesPartModel;
    private javax.swing.JCheckBox cbPurchasesPartName;
    private javax.swing.JCheckBox cbPurchasesPartType;
    private javax.swing.JCheckBox cbStock;
    private javax.swing.JCheckBox cbStockDate;
    private javax.swing.JCheckBox cbStockDate1;
    private javax.swing.JCheckBox cbStockDealerID;
    private javax.swing.JCheckBox cbStockDealerName;
    private javax.swing.JCheckBox cbStockID1;
    private javax.swing.JCheckBox cbStockPartID;
    private javax.swing.JCheckBox cbStockPartID1;
    private javax.swing.JCheckBox cbStockPartName;
    private javax.swing.JCheckBox cbStockPartName1;
    private javax.swing.JCheckBox cbType;
    private javax.swing.JCheckBox cdStockID;
    private javax.swing.JComboBox ddAddModel;
    private javax.swing.JComboBox ddAddStock;
    private javax.swing.JComboBox ddAddType;
    private javax.swing.JComboBox ddModel;
    private javax.swing.JComboBox ddName;
    private javax.swing.JComboBox ddPartID;
    private javax.swing.JComboBox ddPaymentType;
    private javax.swing.JComboBox ddPurchasesPartModel;
    private org.freixas.jcalendar.JCalendarCombo ddPurchasesSearchDate;
    private javax.swing.JComboBox ddPurchasesSearchPartName;
    private javax.swing.JComboBox ddPurchasesSearchPartType;
    private org.freixas.jcalendar.JCalendarCombo ddQutationSearchDate;
    private javax.swing.JComboBox ddSearchDealerName;
    private javax.swing.JComboBox ddSearchPartName;
    private javax.swing.JComboBox ddStock;
    private org.freixas.jcalendar.JCalendarCombo ddStockAddDate;
    private org.freixas.jcalendar.JCalendarCombo ddStockAddDate1;
    private org.freixas.jcalendar.JCalendarCombo ddStockAddDate2;
    private javax.swing.JComboBox ddStockDealerID;
    private javax.swing.JComboBox ddStockID;
    private javax.swing.JComboBox ddStockPartID;
    private org.freixas.jcalendar.JCalendarCombo ddStockSearchDate;
    private javax.swing.JComboBox ddStockSearchDealerID;
    private javax.swing.JComboBox ddStockSearchDealerName;
    private javax.swing.JComboBox ddStockSearchPartID;
    private javax.swing.JComboBox ddStockSearchPartID1;
    private javax.swing.JComboBox ddStockSearchPartName;
    private javax.swing.JComboBox ddStockSearchPartName1;
    private javax.swing.JComboBox ddStockStockID1;
    private javax.swing.JComboBox ddType;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton6;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel35;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JLabel lblBalance;
    private javax.swing.JLabel lblQuotationAddress;
    private javax.swing.JLabel lblQuotationTotal;
    private javax.swing.JLabel lblQuotationlContact;
    private javax.swing.JLabel lblQuotationlCusName;
    private javax.swing.JLabel lblServiceCharge;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JLabel lblTotal1;
    private javax.swing.JList lstQuotation;
    private javax.swing.JPanel pnlPurchasesCash;
    private javax.swing.JPanel pnlPurchasesCheque;
    private javax.swing.JPanel pnlPurchasesDefault;
    private javax.swing.JSpinner spnPurchasesOrderQuantity;
    private javax.swing.JSpinner spnPurchasesUpdateQuantity;
    private javax.swing.JSpinner spnQuotationQty;
    private javax.swing.JTable tblParts;
    private javax.swing.JTable tblPurchasesOrder;
    private javax.swing.JTable tblPurchasesStock;
    private javax.swing.JTable tblQuotationParts;
    private javax.swing.JTable tblQuotationStocks;
    private javax.swing.JTable tblStock;
    private javax.swing.JTable tblStockDealer;
    private javax.swing.JTable tblStockPart;
    private javax.swing.JTextField txtNewPartModel;
    private javax.swing.JTextField txtNewPartType;
    private javax.swing.JTextField txtPartId;
    private javax.swing.JTextField txtPartName;
    private javax.swing.JTextField txtPurchasesBank;
    private javax.swing.JTextField txtPurchasesChequeNo;
    private javax.swing.JTextField txtPurchasesPaymentCent;
    private javax.swing.JTextField txtPurchasesPaymentRs;
    private javax.swing.JTextField txtServiceChargeCent;
    private javax.swing.JTextField txtServiceChargeRs;
    private javax.swing.JTextField txtStockCostCent;
    private javax.swing.JTextField txtStockCostRs;
    private javax.swing.JTextField txtStockDealerName;
    private javax.swing.JTextField txtStockDiscount;
    private javax.swing.JTextField txtStockID;
    private javax.swing.JTextField txtStockPartName;
    private javax.swing.JTextField txtStockPriceCent;
    private javax.swing.JTextField txtStockPriceRs;
    private javax.swing.JTextField txtStockQuantity;
    // End of variables declaration//GEN-END:variables

}
