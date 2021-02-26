package com.ivan.rc.lab2;

/**
 *
 * @author ivan
 */
public class LabTwoEntryPoint {

   
    public void run() {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainWindow().setVisible(true);
            }
        });
    }

}
