package org.example.Properties;

import org.example.LogConfig.LogConfig;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConfigProperties {

    private static final Logger logger = Logger.getLogger(LogConfig.class.getName());

    private static final String PROPERTIES_FILE = "C:\\Users\\josue\\OneDrive\\Desktop\\config.properties";
    //private static final String PROPERTIES_FILE = ".\\config.properties";

    public static String KEY_WORD;
    public static String FONT_SIZE;

    public static void loadProperties() {
        logger.log(Level.INFO, "Cargando valores CONFIG.PROPERTIES...");
        Properties properties = new Properties();
        try (InputStream input = new FileInputStream(PROPERTIES_FILE)) {
            properties.load(input);

            KEY_WORD = properties.getProperty("KEY_WORD");
            FONT_SIZE = properties.getProperty("FONT_SIZE");

            logger.log(Level.INFO, "Valores CONFIG.PROPERTIES cargados correctamente");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al cargar valores CONFIG.PROPERTIES. {0}", e);
        }
    }
}
