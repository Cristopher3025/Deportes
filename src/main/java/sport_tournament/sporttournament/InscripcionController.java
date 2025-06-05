package sport_tournament.sporttournament;

import database.Team;
import database.Tournament;
import database.TournamentTeam;
import database_manager.TeamDAO;
import database_manager.TournamentDAO;
import database_manager.TournamentTeamDAO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class InscripcionController {

    @FXML private ComboBox<String> comboTorneo;
    @FXML private ListView<String> listaEquipos;
    @FXML private Label lbl_mensaje;

    private final TournamentDAO torneoDAO = new TournamentDAO();
    private final TeamDAO teamDAO = new TeamDAO();
    private final TournamentTeamDAO inscripcionDAO = new TournamentTeamDAO();

    private List<Tournament> torneos;

    @FXML
    public void initialize() {
        torneos = torneoDAO.findAll();
        List<String> nombres = new ArrayList<>();
        for (Tournament torneo : torneos) {
            nombres.add(torneo.getTournamentName());
        }
        comboTorneo.getItems().setAll(nombres);

        comboTorneo.setOnAction(event -> cargarEquipos());

        
        listaEquipos.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    private void cargarEquipos() {
        String nombreTorneo = comboTorneo.getValue();
        Tournament torneo = torneos.stream()
                .filter(x -> x.getTournamentName().equals(nombreTorneo))
                .findFirst()
                .orElse(null);

        if (torneo != null) {
            List<Team> disponibles = teamDAO.findBySport(torneo.getSportId());
            List<String> nombresEquipos = new ArrayList<>();
            for (Team equipo : disponibles) {
                nombresEquipos.add(equipo.getTeamName());
            }
            listaEquipos.setItems(FXCollections.observableArrayList(nombresEquipos));
        }
    }

    @FXML
    private void inscribirEquipos() {
        String nombreTorneo = comboTorneo.getValue();
        List<String> equiposSeleccionados = listaEquipos.getSelectionModel().getSelectedItems();

        if (nombreTorneo == null || equiposSeleccionados.isEmpty()) {
            SoundManager.playSound("error.mp3");
            lbl_mensaje.setText("Seleccione torneo y equipos.");
            return;
        }

        Tournament torneo = torneos.stream()
                .filter(t -> t.getTournamentName().equals(nombreTorneo))
                .findFirst()
                .orElse(null);

        for (String nombreEquipo : equiposSeleccionados) {
            Team equipo = teamDAO.findByName(nombreEquipo);
            TournamentTeam tt = new TournamentTeam();
            tt.setTournamentId(torneo);
            tt.setTeamId(equipo);
            tt.setPosition(BigInteger.ZERO); 
            inscripcionDAO.inscribir(tt);
        }

        lbl_mensaje.setText("Equipos inscritos correctamente.");
    }
}
