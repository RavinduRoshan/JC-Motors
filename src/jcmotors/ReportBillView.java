/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcmotors;

import com.jtattoo.plaf.hifi.HiFiLookAndFeel;
import com.mysql.jdbc.PreparedStatement;
import java.awt.Container;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.HashMap;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.swing.JRViewer;

/**
 *
 * @author Dhanushka
 */
public class ReportBillView extends JFrame {

    String url = "jdbc:mysql://localhost:3306/jcmotors";
    String username = "root";
    String password = "";
    Connection con = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    String file = "";

    public ReportBillView(String filePath, HashMap parameters) {
        super("Bill Report");

        try {
            //get connection
            con = (Connection) DriverManager.getConnection(url, this.username, this.password); 
            
            JasperPrint print = JasperFillManager.fillReport(filePath, parameters, con);
            JRViewer viewer = new JRViewer(print);
            
            Container c = getContentPane();
            c.add(viewer);
            
            try{
                UIManager.setLookAndFeel(new HiFiLookAndFeel());
                SwingUtilities.updateComponentTreeUI(this);
            }catch(Exception e){
                System.out.println(e);
            }

        } catch (Exception e) {
            System.out.println(e);
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
    
    
    
    //============================================================================
    public ReportBillView(String f) {
        super("aaaa");

        try {
            System.out.println("1");
            con = (Connection) DriverManager.getConnection(url, this.username, this.password); //get connection
            
            System.out.println("2");
            JasperPrint print = JasperFillManager.fillReport(f, null, con);
            System.out.println("3");
            JRViewer viewer = new JRViewer(print);
            System.out.println("4");
            
            Container c = getContentPane();
            System.out.println("5");
            c.add(viewer);
            System.out.println("6");
            
            try{
                UIManager.setLookAndFeel(new HiFiLookAndFeel());
                SwingUtilities.updateComponentTreeUI(this);
            }catch(Exception e){
                System.out.println(e);
            }
            
        } catch (Exception e) {
            System.out.println(e);
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
