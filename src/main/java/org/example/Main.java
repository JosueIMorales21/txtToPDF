package org.example;


import java.io.IOException;

import static org.example.FileReader.FileReader.*;
import static org.example.LogConfig.LogConfig.loadConfig;
import static org.example.Properties.ConfigProperties.KEY_WORD;
import static org.example.Properties.ConfigProperties.loadProperties;

public class Main {

    public static void main(String[] args) {
        String inPath = "C:\\Users\\josue\\OneDrive\\Desktop\\bible.txt";
        String outPath = "C:\\Users\\josue\\OneDrive\\Desktop\\";

        try {
            loadProperties();
            loadConfig();
            txtToPDF(inPath, outPath, KEY_WORD);
        } catch (IOException e) {
            System.out.println("ERROR " + e);
        }

    }
}