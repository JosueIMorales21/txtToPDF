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

                // Split content into paragraphs
                String[] paragraphs = content.split("\\n\\s*\\n");

                float y = 700; // Initial y-coordinate

                // Iterate over each paragraph
                for (String paragraph : paragraphs) {
                    // Split paragraph into words
                    String[] words = paragraph.split("\\s+"); // split by whitespace
                    StringBuilder currentLine = new StringBuilder();

                    // Iterate over each word in the paragraph
                    for (String word : words) {
                        float width = pdfFont.getStringWidth(currentLine + " " + word) / 1000 * 12;
                        if (width > 500) { // Width of the page - Margin
                            contentStream.beginText();
                            contentStream.newLineAtOffset(50, y);
                            contentStream.showText(currentLine.toString());
                            contentStream.endText();
                            y -= 12; // Move to the next line
                            currentLine.setLength(0);
                        }
                        currentLine.append(word).append(" ");
                    }

                    // Write the last line of the paragraph
                    contentStream.beginText();
                    contentStream.newLineAtOffset(50, y);
                    contentStream.showText(currentLine.toString());
                    contentStream.endText();
                    y -= 12; // Move to the next line

                    // Add extra space between paragraphs
                    y -= 12;
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
