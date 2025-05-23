package sport_tournament.sporttournament;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;

public class MainController {

    @FXML
    private AnchorPane contenidoPrincipal;

    public void mostrarDeportes() throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("sport.fxml"));
        contenidoPrincipal.getChildren().setAll(pane);
    }

    public void mostrarEquipos() {
        // Pendiente
    }

    public void mostrarTorneos() {
        // Pendiente
    }

    public void mostrarEstadisticas() {
        // Pendiente
    }

    public void mostrarCertificados() {
        // Pendiente
    }
}
