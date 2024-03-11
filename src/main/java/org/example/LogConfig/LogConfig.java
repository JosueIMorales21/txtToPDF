package org.example.LogConfig;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class LogConfig {

    private static final Logger logger = Logger.getLogger(LogConfig.class.getName());

    public static void loadConfig() {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            String currentDate = dateFormat.format(new Date());
            String logFileName = "log" + currentDate + ".log";

            String logFilePath = "\\" + logFileName;
            //String logFilePath = ".\\" + logFileName;

            FileHandler fileHandler = new FileHandler(logFilePath, true);
            fileHandler.setFormatter(new CustomFormatter());
            logger.addHandler(fileHandler);
            writeSeparator();
        } catch (Exception e) {
            System.out.println("Error al crear el log: " + e);
        }
    }

    private static void writeSeparator() {
        logger.info("-------------------------");
        logger.info("-------------------------");
    }
}
