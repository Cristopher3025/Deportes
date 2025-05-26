package sport_tournament.sporttournament;

import database.Match;
import database.Tournament;
import database_manager.MatchDAO;
import database_manager.TournamentDAO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.List;

public class PartidoController {

    @FXML private ComboBox<String> comboTorneo;
    @FXML private TableView<Match> tablaPartidos;
    @FXML private TableColumn<Match, String> colEquipo1;
    @FXML private TableColumn<Match, String> colEquipo2;
    @FXML private TableColumn<Match, String> colEstado;

    private final TournamentDAO torneoDAO = new TournamentDAO();
    private final MatchDAO matchDAO = new MatchDAO();
    private List<Tournament> torneos;

    @FXML
    public void initialize() {
        colEquipo1.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTeam1Id().getTeamName()));
        colEquipo2.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTeam2Id().getTeamName()));
        colEstado.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getStatus()));

        torneos = torneoDAO.findAll();
        List<String> nombres = new ArrayList<>();
        for (Tournament t : torneos) {
            nombres.add(t.getTournamentName());
        }
        comboTorneo.getItems().setAll(nombres);
    }

    @FXML
    private void buscarPartidos() {
        String nombreTorneo = comboTorneo.getValue();
        if (nombreTorneo == null) return;

        Tournament torneo = torneos.stream()
                .filter(t -> t.getTournamentName().equals(nombreTorneo))
                .findFirst()
                .orElse(null);

        if (torneo != null) {
            List<Match> partidos = matchDAO.findByTournament(torneo);
            tablaPartidos.setItems(FXCollections.observableArrayList(partidos));
        }
    }
}
