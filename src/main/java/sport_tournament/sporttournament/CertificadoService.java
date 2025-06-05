package sport_tournament.sporttournament;

import database.Team;
import database.Tournament;
import database_manager.MatchDAO;
import database_manager.MatchResultDAO;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.File;
import java.io.IOException;

public class CertificadoService {

    public static void generarCertificado(Tournament torneo, Team campeon) {
        String fileName = "certificado_" +
                torneo.getTournamentName().replace(" ", "_") + "_" +
                campeon.getTeamName().replace(" ", "_") + ".pdf";

        String outputPath = "src/main/resources/Certificados/" + fileName;

        try (PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.LETTER);
            doc.addPage(page);
            PDPageContentStream content = new PDPageContentStream(doc, page);

            float pageWidth = PDRectangle.LETTER.getWidth();

          
            content.beginText();
            content.setFont(PDType1Font.HELVETICA_BOLD, 30);
            content.newLineAtOffset(centerText("Certificado de Campeonato", PDType1Font.HELVETICA_BOLD, 30, pageWidth), 700);
            content.showText("Certificado de Campeonato");
            content.endText();

         
            content.beginText();
            content.setFont(PDType1Font.HELVETICA, 18);
            content.newLineAtOffset(80, 640);
            content.showText("Otorgado al equipo destacado:");
            content.endText();

            content.beginText();
            content.setFont(PDType1Font.HELVETICA_BOLD, 22);
            content.newLineAtOffset(80, 610);
            content.showText(campeon.getTeamName());
            content.endText();

            content.beginText();
            content.setFont(PDType1Font.HELVETICA, 18);
            content.newLineAtOffset(80, 575);
            content.showText("Por su gran desempeño en el torneo:");
            content.endText();

            content.beginText();
            content.setFont(PDType1Font.HELVETICA_BOLD, 20);
            content.newLineAtOffset(80, 545);
            content.showText(torneo.getTournamentName());
            content.endText();

            int goles = new MatchResultDAO().getGolesTotales(torneo, campeon);
            int ganados = new MatchDAO().getPartidosGanados(torneo, campeon);

            content.beginText();
            content.setFont(PDType1Font.HELVETICA, 16);
            content.newLineAtOffset(80, 500);
            content.showText("Partidos ganados: " + ganados);
            content.endText();

            content.beginText();
            content.setFont(PDType1Font.HELVETICA, 16);
            content.newLineAtOffset(80, 475);
            content.showText("Goles anotados: " + goles);
            content.endText();

       
            content.beginText();
            content.setFont(PDType1Font.HELVETICA_OBLIQUE, 14);
            content.newLineAtOffset(centerText("“La victoria es el arte de la paciencia.” — Aristóteles", PDType1Font.HELVETICA_OBLIQUE, 14, pageWidth), 420);
            content.showText("“La victoria es el arte de la paciencia.” — Aristóteles");
            content.endText();

    
            if (campeon.getPhotoPath() != null) {
                File imgFile = new File("src/main/resources/" + campeon.getPhotoPath());
                if (imgFile.exists()) {
                    PDImageXObject pdImage = PDImageXObject.createFromFileByExtension(imgFile, doc);
                    content.drawImage(pdImage, 420, 550, 120, 120);
                }
            }

          
            content.beginText();
            content.setFont(PDType1Font.HELVETICA_OBLIQUE, 12);
            content.newLineAtOffset(80, 80);
            content.showText("Sistema desarrollado por Cristopher Ureña – Universidad Nacional, Campus Coto");
            content.endText();

            content.close();
            doc.save(outputPath);
            System.out.println("Certificado generado: " + outputPath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static float centerText(String text, PDType1Font font, int fontSize, float pageWidth) throws IOException {
        float textWidth = font.getStringWidth(text) / 1000 * fontSize;
        return (pageWidth - textWidth) / 2;
    }
}
