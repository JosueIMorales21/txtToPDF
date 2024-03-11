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
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    private static String extractValue(String content, String keyword) {
        logger.log(Level.INFO, "Palabra a buscar: {0}", keyword);
        String patternString = keyword + ": \"(.*?)\"";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(content);

        if (matcher.find()) {
            logger.log(Level.INFO, "Palabra encontrada.");
            return matcher.group(1);
        } else {
            logger.log(Level.SEVERE, "Palabra no encontrada");
            return null;
        }
    }

    public static void createPDF(String content, String keyword, String output) throws IOException {
        logger.log(Level.INFO, "CONTENIDO: {0}", content);
        logger.log(Level.INFO, "Creando el archivo PDF...");

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            // Get the font
            Standard14Fonts.FontName font = Standard14Fonts.getMappedFontName("HELVETICA_BOLD");
            PDFont pdfFont=  new PDType1Font(font.HELVETICA_BOLD);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.setFont(pdfFont, 12);

                String[] lines = content.split("\\r?\\n");
                float y = 500; // Initial y-coordinate

                // Write each line to the PDF
                for (String line : lines) {
                    // Check if adding this line will exceed the page height
                    if (y - 12 < 0) {
                        // Add a new page
                        contentStream.endText();
                        contentStream.close();
                        page = new PDPage();
                        document.addPage(page);
                        contentStream.setFont(pdfFont, 12);
                        y = 500;
                    }

                    // Begin a new text object for each line
                    contentStream.beginText();
                    contentStream.newLineAtOffset(50, y);
                    contentStream.showText(line.trim());
                    contentStream.endText();
                    y -= 12; // Move to the next line
                }
            }

            String timestamp = Long.toString(System.currentTimeMillis());
            document.save(output + "_" + keyword + "_" + timestamp + ".pdf");
            logger.log(Level.INFO, "Archivo PDF creado correctamente.");
        }
    }

}

//Standard14Fonts.FontName font = Standard14Fonts.getMappedFontName("HELVETICA_BOLD");
//PDFont pdfFont=  new PDType1Font(font.HELVETICA_BOLD);
