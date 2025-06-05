package sport_tournament.sporttournament;

import database.Match;
import database.Team;
import database.Tournament;
import database_manager.MatchDAO;
import database_manager.TournamentDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;


import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class MainController {

    @FXML
    private AnchorPane contenidoPrincipal;

    @FXML
    private ComboBox<String> comboTema;

    @FXML
    public void initialize() {
        comboTema.getItems().addAll("light-theme", "dark-theme", "sport-theme", "classic-theme");
        comboTema.setValue(ThemeManager.getTemaActual());
    }

    @FXML
    private void cambiarTema() {
        String nuevoTema = comboTema.getValue();
        Scene escena = comboTema.getScene();
        ThemeManager.aplicarTema(escena, nuevoTema);
    }

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
    public void mostrarInscripcion() throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("inscripcion.fxml"));
        contenidoPrincipal.getChildren().setAll(pane);
    }

    @FXML
    public void mostrarTorneos() throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("torneo.fxml"));
        contenidoPrincipal.getChildren().setAll(pane);
    }

    @FXML
    public void mostrarPartidos() throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("partidos.fxml"));
        contenidoPrincipal.getChildren().setAll(pane);
    }

    @FXML
    public void mostrarResultados() throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("resultado.fxml"));
        contenidoPrincipal.getChildren().setAll(pane);
    }

    @FXML
    public void mostrarEstadisticas() throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("estadisticas.fxml"));
        contenidoPrincipal.getChildren().setAll(pane);
    }

    public void mostrarCertificados() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("certificado.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Generar Certificado");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void mostrarEsquemaTorneo() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("esquema_torneo.fxml"));
            Parent root = loader.load();

            EsquemaTorneoController controller = loader.getController();
            Tournament ultimo = obtenerTorneoFinalizado();
            if (ultimo == null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                SoundManager.playSound("error.mp3");
                alert.setHeaderText("No hay torneo finalizado aún.");
                alert.showAndWait();
                return;
            }
            controller.setTorneoSeleccionado(ultimo);

            Stage stage = new Stage();
            stage.setTitle("Esquema del Torneo");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Tournament obtenerTorneoFinalizado() {
        List<Tournament> torneos = new TournamentDAO().findAll();
        for (int i = torneos.size() - 1; i >= 0; i--) {
            Tournament torneo = torneos.get(i);
            if ("Finalizado".equalsIgnoreCase(torneo.getEstado())) {
                return torneo;
            }
        }
        return null;
    }

    public Team obtenerUltimoCampeon() {
        List<Tournament> torneos = new TournamentDAO().findAll();
        for (int i = torneos.size() - 1; i >= 0; i--) {
            Tournament torneo = torneos.get(i);
            List<Match> partidos = new MatchDAO().findByTournament(torneo);
            boolean todosFinalizados = partidos.stream().allMatch(m -> "Finalizado".equalsIgnoreCase(m.getStatus()));
            if (todosFinalizados) {
                List<Team> ganadores = partidos.stream().map(Match::getWinnerId).distinct().collect(Collectors.toList());
                if (ganadores.size() == 1) return ganadores.get(0);
            }
        }
        return null;
    }

    public Tournament obtenerTorneoGanado(Team equipo) {
        List<Tournament> torneos = new TournamentDAO().findAll();
        for (int i = torneos.size() - 1; i >= 0; i--) {
            Tournament torneo = torneos.get(i);
            List<Match> partidos = new MatchDAO().findByTournament(torneo);
            boolean todosFinalizados = partidos.stream().allMatch(m -> "Finalizado".equalsIgnoreCase(m.getStatus()));
            if (todosFinalizados) {
                long ganados = partidos.stream().filter(m -> equipo.equals(m.getWinnerId())).count();
                long total = partidos.stream().map(Match::getWinnerId).distinct().count();
                if (ganados > 0 && total == 1) return torneo;
            }
        }
        return null;
    }
}
