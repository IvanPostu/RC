
package com.ivan.rc.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;

/**
 *
 * @author ivan
 */
public abstract class Log4jConfiguration {

    public static void configure() {
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            URL res = loader.getResource("log4j2.dev.xml");
            File file = Paths.get(res.toURI()).toFile();
            InputStream inputStream = new FileInputStream(file);
            ConfigurationSource source = new ConfigurationSource(inputStream);
            Configurator.initialize(null, source);
        } catch (IOException | URISyntaxException ex) {
            System.out.println(ex);
            System.exit(-1);
        }
    }

}
