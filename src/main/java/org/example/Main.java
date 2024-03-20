package org.example;


import org.example.LogConfig.LogConfig;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.example.FileReader.FileReader.*;
import static org.example.LogConfig.LogConfig.loadConfig;

public class Main {

    private static final Logger logger = Logger.getLogger(LogConfig.class.getName());

    public static void main(String[] args) {
        String inPath_ticket = ".\\ticket.txt";
        String inPath_voucher = ".\\voucher.txt";
        String outPath = ".\\";

        loadConfig();

        try {
            txtToPDF(inPath_ticket, outPath, "Chk", "ticket");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "TICKET NO ENCONTRADO ", e);
            return;
        }

        try {
            txtToPDF(inPath_voucher, outPath, "Chk", "voucher");
        } catch (IOException e) {
            logger.log(Level.WARNING, "Voucher no encontrado.", e);
        }

    }
}