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
import java.util.List;

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

        content.beginText();
        content.setFont(PDType1Font.HELVETICA_BOLD, 24);
        content.newLineAtOffset(100, 700);
        content.showText("Certificado de Campeonato");
        content.endText();

        content.beginText();
        content.setFont(PDType1Font.HELVETICA, 16);
        content.newLineAtOffset(100, 660);
        content.showText("Torneo: " + torneo.getTournamentName());
        content.endText();

        content.beginText();
        content.setFont(PDType1Font.HELVETICA_BOLD, 18);
        content.newLineAtOffset(100, 630);
        content.showText("Equipo Campeón: " + campeon.getTeamName());
        content.endText();

        int goles = new MatchResultDAO().getGolesTotales(torneo, campeon);
        int ganados = new MatchDAO().getPartidosGanados(torneo, campeon);

        content.beginText();
        content.setFont(PDType1Font.HELVETICA, 14);
        content.newLineAtOffset(100, 600);
        content.showText("Partidos ganados: " + ganados);
        content.endText();

        content.beginText();
        content.setFont(PDType1Font.HELVETICA, 14);
        content.newLineAtOffset(100, 580);
        content.showText("Cantidad de anotaciones: " + goles);
        content.endText();

        if (campeon.getPhotoPath() != null) {
            File imgFile = new File("src/main/resources/" + campeon.getPhotoPath());
            if (imgFile.exists()) {
                PDImageXObject pdImage = PDImageXObject.createFromFileByExtension(imgFile, doc);
                content.drawImage(pdImage, 350, 500, 150, 150);
            }
        }

        content.close();
        doc.save(outputPath);

        System.out.println("Certificado generado: " + outputPath);

    } catch (IOException e) {
        e.printStackTrace();
    }
}

}
