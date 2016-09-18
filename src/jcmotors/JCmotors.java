/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jcmotors;

import com.jtattoo.plaf.hifi.HiFiLookAndFeel;
import javax.swing.UIManager;

/**
 *
 * @author Dhanushka
 */
public class JCmotors {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(new HiFiLookAndFeel());
                } catch (Exception e) {
                }
                new Login().setVisible(true);
            }
        });
    }
    
}
