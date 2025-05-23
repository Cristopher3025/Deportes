package sport_tournament.sporttournament;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;
import javafx.scene.control.Alert;

public class MainController {

    @FXML
    private AnchorPane contenidoPrincipal;

    public void mostrarDeportes() throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("sport.fxml"));
        contenidoPrincipal.getChildren().setAll(pane);
    }
    
    
    public void mostrarEquipos() throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("team.fxml"));
        contenidoPrincipal.getChildren().setAll(pane);
    }

    @FXML
    public void mostrarInformacionCreador() {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Sobre el creador");
        alerta.setHeaderText("Cristopher Ureña Valverde");
        alerta.setContentText(
            "Estudiante de Ingeniería en Sistemas\n" +
            "UNA - Sede Brunca\n" +
            "Apasionado por la filosofía, el vino y el código.\n\n" +
            "“El que tiene un porqué para vivir puede soportar casi cualquier cómo” — Nietzsche"
        );
        alerta.showAndWait();
        }

    @FXML
    public void salirAplicacion() {
     System.exit(0);
        }       


    @FXML
    public void mostrarTorneos() throws IOException {
    AnchorPane pane = FXMLLoader.load(getClass().getResource("torneo.fxml"));
    contenidoPrincipal.getChildren().setAll(pane);
    }


    public void mostrarEstadisticas() {
        // Pendiente
    }

    public void mostrarCertificados() {
        // Pendiente
    }
}
