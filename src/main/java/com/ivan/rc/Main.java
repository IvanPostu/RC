package com.ivan.rc;

import com.ivan.rc.configuration.Log4jConfiguration;
import com.ivan.rc.lab3.MainWindow;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author ivan
 */
public class Main {

    static {
        Log4jConfiguration.configure();
    }

    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                logger.info("Run method is called");
                new MainWindow().setVisible(true);
            }
        });
    }

}
