package org.example;


import java.io.IOException;

import static org.example.FileReader.FileReader.*;

public class Main {

    public static final int FONT_SIZE = 12;
    public static void main(String[] args) {
        String inPath = "C:\\Users\\josue\\OneDrive\\Desktop\\bible.txt";
        String outPath = "C:\\Users\\josue\\OneDrive\\Desktop\\";
        String key = "cardId";

        try {
            txtToPDF(inPath, outPath, key);
        } catch (IOException e) {
            System.out.println("ERROR " + e);
        }

    }
}