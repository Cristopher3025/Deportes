package sport_tournament.sporttournament;

import database.Match;
import database.Team;
import database.Tournament;
import database_manager.MatchDAO;
import database_manager.TournamentDAO;
import database_manager.TournamentTeamDAO;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TorneoService {

    private final MatchDAO matchDAO = new MatchDAO();
    private final TournamentTeamDAO ttDAO = new TournamentTeamDAO();

    public void avanzarSiEsNecesario(Tournament torneo) {
        List<Match> partidos = matchDAO.findByTournament(torneo);
        List<Match> finalizados = new ArrayList<>();
        List<Match> pendientes = new ArrayList<>();

        for (Match m : partidos) {
            if ("Finalizado".equalsIgnoreCase(m.getStatus())) {
                finalizados.add(m);
            } else {
                pendientes.add(m);
            }
        }

        if (!pendientes.isEmpty()) {
            return;
        }

        List<Team> ganadores = new ArrayList<>();
        for (Match m : finalizados) {
            ganadores.add(m.getWinnerId());
        }

        if (ganadores.size() == 1) {
            mostrarCampeon(ganadores.get(0));
            return;
        }

        generarNuevaRonda(torneo, ganadores);
    }

    private void generarNuevaRonda(Tournament torneo, List<Team> ganadores) {
        for (int i = 0; i < ganadores.size(); i += 2) {
            if (i + 1 >= ganadores.size()) break;

            Match nuevo = new Match();
            nuevo.setTournamentId(torneo);
            nuevo.setTeam1Id(ganadores.get(i));
            nuevo.setTeam2Id(ganadores.get(i + 1));
            nuevo.setStatus("Pendiente");
            matchDAO.addMatch(nuevo);
        }
    }

    private void mostrarCampeon(Team campeon) {
        try {
            CampeonController.mostrar(campeon);


            Tournament torneo = matchDAO.findAll().stream()
                .filter(m -> campeon.equals(m.getWinnerId()))
                .map(Match::getTournamentId)
                .findFirst()
                .orElse(null);

            if (torneo != null) {
                torneo.setEstado("Finalizado");
                new TournamentDAO().updateTournament(torneo);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void mostrarEsquemaTorneo() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("EsquemaTorneo.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Esquema del Torneo");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

