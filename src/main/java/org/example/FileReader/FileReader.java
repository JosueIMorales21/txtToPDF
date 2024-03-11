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

import static org.example.Main.FONT_SIZE;

public class FileReader {

    private static final Logger logger = Logger.getLogger(LogConfig.class.getName());

    public static void txtToPDF(String input, String output, String keyWord) throws IOException{
            String content = readFileToString(input);
            String keyValue = extractValue(content, keyWord);
            createPDF(content, keyValue, output);
    }

    private static String readFileToString(String filePath) throws IOException {
        logger.log(Level.INFO, "Leyendo el archivo...");
        StringBuilder content = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new java.io.FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            logger.log(Level.INFO, "Lectura realizada con éxito.");
        }
        return content.toString();
    }

    private static String extractValue(String content, String keyword) throws IOException {
        logger.log(Level.INFO, "Palabra a buscar: {0}", keyword);

        String patternString = "\\b" + keyword + "\\s*:\\s*(\\S+)";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(content);

        if (matcher.find()) {
            logger.log(Level.INFO, "Palabra encontrada.");
            return matcher.group(1);
        } else {
            logger.log(Level.SEVERE, "Palabra no encontrada");
            throw new IOException();
        }
    }

    private static void createPDF(String content, String keyword, String output) throws IOException {
        logger.log(Level.INFO, "Creando el archivo PDF...");

        // Set page parameters
        final int PAGE_WIDTH = 500; // Maximum width of each line
        final int MARGIN = 50;

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            // Get the font
            Standard14Fonts.FontName font = Standard14Fonts.getMappedFontName("TIMES_ROMAN");
            PDFont pdfFont=  new PDType1Font(font.TIMES_ROMAN);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.setFont(pdfFont, FONT_SIZE);

                // Split content into lines
                String[] lines = content.split("\\r?\\n");

                float y = page.getMediaBox().getHeight() - MARGIN; // Initial y-coordinate

                for (String line : lines) {
                    StringBuilder currentLine = new StringBuilder();
                    String[] words = line.split("\\s+");

                    for (String word : words) {
                        float width = pdfFont.getStringWidth(currentLine + " " + word) / 1000 * FONT_SIZE;
                        if (width > PAGE_WIDTH) {
                            contentStream.beginText();
                            contentStream.newLineAtOffset(MARGIN, y);
                            contentStream.showText(currentLine.toString());
                            contentStream.endText();
                            y -= FONT_SIZE; // Move to the next line
                            currentLine.setLength(0); // Clear the current line
                        }
                        currentLine.append(word).append(" ");
                    }

                    // Write the remaining text on the line
                    contentStream.beginText();
                    contentStream.newLineAtOffset(MARGIN, y);
                    contentStream.showText(currentLine.toString());
                    contentStream.endText();
                    y -= FONT_SIZE; // Move to the next line
                }
            }

            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyy");
            String formattedDate = currentDate.format(formatter);
            document.save(output + keyword + "_" + formattedDate + ".pdf");
            logger.log(Level.INFO, "Archivo PDF creado correctamente.");
        }
    }

}

//Standard14Fonts.FontName font = Standard14Fonts.getMappedFontName("HELVETICA_BOLD");
//PDFont pdfFont=  new PDType1Font(font.HELVETICA_BOLD);
