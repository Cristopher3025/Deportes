package sport_tournament.sporttournament;

import database.Match;
import database.Team;
import database_manager.MatchDAO;
import database_manager.MatchResultDAO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class ResultadoController {

    @FXML private ComboBox<String> comboPartidos;
    @FXML private Label lblEquipo1;
    @FXML private Label lblEquipo2;
    @FXML private Label lbl_mensaje;

    @FXML private TableView<Match> tablaPartidos;
    @FXML private TableColumn<Match, String> colEquipo1;
    @FXML private TableColumn<Match, String> colEquipo2;
    @FXML private TableColumn<Match, String> colEstado;

    private final MatchDAO matchDAO = new MatchDAO();
    private final MatchResultDAO resultDAO = new MatchResultDAO();

    private List<Match> partidos;

    @FXML
    public void initialize() {
        colEquipo1.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTeam1Id().getTeamName()));
        colEquipo2.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTeam2Id().getTeamName()));
        colEstado.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus()));

        partidos = matchDAO.findPendientes();

        if (partidos == null || partidos.isEmpty()) {
            lbl_mensaje.setText("No hay partidos pendientes.");
            return;
        }

        List<String> nombres = new ArrayList<>();
        for (Match m : partidos) {
            nombres.add(m.getTeam1Id().getTeamName() + " vs " + m.getTeam2Id().getTeamName());
        }

        comboPartidos.getItems().setAll(nombres);
        comboPartidos.setOnAction(e -> cargarDatos());

        tablaPartidos.setItems(FXCollections.observableArrayList(partidos));
    }

    private void cargarDatos() {
        int index = comboPartidos.getSelectionModel().getSelectedIndex();
        if (index >= 0) {
            Match partido = partidos.get(index);
            lblEquipo1.setText(partido.getTeam1Id().getTeamName());
            lblEquipo2.setText(partido.getTeam2Id().getTeamName());
        }
    }

    @FXML
    private void simularPartido() {
        Match partido = tablaPartidos.getSelectionModel().getSelectedItem();
        if (partido == null) {
            lbl_mensaje.setText("Seleccione un partido");
            return;
        }

        Team equipo1 = partido.getTeam1Id();
        Team equipo2 = partido.getTeam2Id();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("simulacion_partido.fxml"));
            Parent root = loader.load();

            SimulacionPartidoController controller = loader.getController();
            controller.inicializar(equipo1, equipo2, partido);

            Stage stage = new Stage();
            stage.setTitle("Simulación de Partido");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
