package org.example.FileReader;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.example.LogConfig.LogConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileReader {

    private static final Logger logger = Logger.getLogger(LogConfig.class.getName());

    public static void txtToPDF(String input, String output, String keyWord, String kind) throws IOException{
            String content = readFileToString(input);
            String keyValue = extractValue(content, keyWord);
            createPDF(content, kind, keyValue, output);
    }

    private static String readFileToString(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new java.io.FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            logger.log(Level.INFO, "Archivo encontrado.");
        }
        return content.toString();
    }

    private static String extractValue(String content, String keyword) throws IOException {

        // Modify the pattern to match "Chk" followed by a space and then the value
        String patternString = "\\b" + keyword + "\\s+(\\d+)";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(content);

        if (matcher.find()) {
            return matcher.group(1);
        } else {
            logger.log(Level.SEVERE, "Palabra clave sin coincidencias.");
            throw new IOException();
        }
    }


    private static void createPDF(String content, String kind, String keyword, String output) throws IOException {

        // Definir las medidas de la página
        final int PAGE_WIDTH = 500;
        final int MARGIN = 50;
        int fontSize = 12;

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            // Definir la fuente
            PDFont pdfFont=  new PDType1Font(Standard14Fonts.FontName.TIMES_ROMAN);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.setFont(pdfFont, fontSize);

                // Separar el contenido en párrafos
                String[] lines = content.split("\\r?\\n");

                float y = page.getMediaBox().getHeight() - MARGIN;

                for (String line : lines) {
                    StringBuilder currentLine = new StringBuilder();
                    String[] words = line.split("\\s+");

                    for (String word : words) {
                        float width = pdfFont.getStringWidth(currentLine + " " + word) / 1000 * fontSize;
                        if (width > PAGE_WIDTH) {
                            contentStream.beginText();
                            contentStream.newLineAtOffset(MARGIN, y);
                            contentStream.showText(currentLine.toString());
                            contentStream.endText();
                            y -= fontSize; // Pasar al siguiente renglón
                            currentLine.setLength(0);
                        }
                        currentLine.append(word).append(" ");
                    }

                    // Escribir el texto restante
                    contentStream.beginText();
                    contentStream.newLineAtOffset(MARGIN, y);
                    contentStream.showText(currentLine.toString());
                    contentStream.endText();
                    y -= fontSize; // Pasar al siguiente renglón
                }
            }

            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyy");
            String formattedDate = currentDate.format(formatter);
            String logTitle = kind + "_" + keyword + "_" + formattedDate + ".pdf";
            String title = output + logTitle;
            logger.log(Level.INFO, "Nombre del PDF: {0}", logTitle);
            document.save(title);
            logger.log(Level.INFO, "Archivo PDF creado correctamente.");
        }
    }

}
