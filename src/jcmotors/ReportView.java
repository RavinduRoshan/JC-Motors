/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jcmotors;

import com.jtattoo.plaf.hifi.HiFiLookAndFeel;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import java.awt.Container;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.HashMap;
import javax.swing.*;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.swing.JRViewer;
/**
 *
 * @author Ravindu
 */
public class ReportView extends JFrame{
    
    String url = "jdbc:mysql://localhost:3306/jcmotors";
    String username = "root";
    String password = "";
    java.sql.Connection con = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    String file = "";
    
    
    public ReportView(String fileName)
    {
        this(fileName, null);
    }
    public ReportView(String fileName, HashMap para){
        super("Quotation");
        this.setLocationRelativeTo(null);
        
        try {
            //get connection
            con = (java.sql.Connection) DriverManager.getConnection(url, this.username, this.password); 
            
            JasperPrint print = JasperFillManager.fillReport(fileName, para, con);
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
        setBounds(10,10,900,700);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
    }
    
    
}
