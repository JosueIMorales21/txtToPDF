package org.example.Properties;

import org.example.LogConfig.LogConfig;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConfigProperties {

    private static final Logger logger = Logger.getLogger(LogConfig.class.getName());

    private static final String PROPERTIES_FILE = "";
    //private static final String PROPERTIES_FILE = ".\\config.properties";

    public static String HOLA;

    public static void loadProperties() {
        logger.log(Level.INFO, "Cargando valores CONFIG.PROPERTIES...");
        Properties properties = new Properties();
        try (InputStream input = new FileInputStream(PROPERTIES_FILE)) {
            properties.load(input);

            HOLA = properties.getProperty("HOLA");

            logger.log(Level.INFO, "Valores CONFIG.PROPERTIES cargados correctamente");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al cargar valores CONFIG.PROPERTIES. {0}", e);
        }
    }
}