package sport_tournament.sporttournament;

import database.Match;
import database.MatchResult;
import database.Team;
import database_manager.MatchDAO;
import database_manager.MatchResultDAO;

import java.io.IOException;
import java.math.BigInteger;
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
    @FXML private TextField tf_goles1;
    @FXML private TextField tf_goles2;
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

    @FXML
    private void registrarResultado() {
        int index = comboPartidos.getSelectionModel().getSelectedIndex();
        if (index < 0) {
            lbl_mensaje.setText("Seleccione un partido.");
            return;
        }

        try {
            int goles1 = Integer.parseInt(tf_goles1.getText().trim());
            int goles2 = Integer.parseInt(tf_goles2.getText().trim());

            Match partido = partidos.get(index);
            String nombreDeporte = partido.getTournamentId().getSportId().getSportName().toLowerCase();
            List<Integer> permitidos = SportController.getAnotaciones(nombreDeporte);

            if (permitidos != null && (!permitidos.contains(goles1) || !permitidos.contains(goles2))) {
                lbl_mensaje.setText("Puntajes no válidos para el deporte: " + nombreDeporte);
                return;
            }

            MatchResult r1 = new MatchResult();
            r1.setMatchId(partido);
            r1.setTeamId(partido.getTeam1Id());
            r1.setGoals(BigInteger.valueOf(goles1));
            resultDAO.addResult(r1);

            MatchResult r2 = new MatchResult();
            r2.setMatchId(partido);
            r2.setTeamId(partido.getTeam2Id());
            r2.setGoals(BigInteger.valueOf(goles2));
            resultDAO.addResult(r2);

            partido.setStatus("Finalizado");
            matchDAO.updateMatch(partido);

            lbl_mensaje.setText("Resultado registrado.");
        } catch (NumberFormatException e) {
            lbl_mensaje.setText("Goles inválidos.");
        }
    }
}

