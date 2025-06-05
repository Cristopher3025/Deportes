package sport_tournament.sporttournament;

import database.Match;
import database.Team;
import database.Tournament;
import database_manager.MatchDAO;
import database_manager.TournamentDAO;
import database_manager.TournamentTeamDAO;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TorneoService {
    private final MatchDAO matchDAO = new MatchDAO();
    private final TournamentTeamDAO ttDAO = new TournamentTeamDAO();
    private final TournamentDAO torneoDAO = new TournamentDAO();

    public void avanzarSiEsNecesario(Tournament torneo) {
        List<Match> partidos = matchDAO.findByTournament(torneo);
        Map<Integer, List<Match>> rondas = new TreeMap<>();

        for (Match m : partidos) {
            Integer ronda = m.getRoundNumber();
            if (ronda == null) ronda = 0;
            rondas.computeIfAbsent(ronda, k -> new ArrayList<>()).add(m);
        }

        int ultimaRonda = rondas.keySet().stream().max(Integer::compareTo).orElse(0);
        List<Match> rondaActual = rondas.get(ultimaRonda);

        boolean todosFinalizados = rondaActual.stream()
                .allMatch(m -> "Finalizado".equalsIgnoreCase(m.getStatus()));

        if (!todosFinalizados) return;

        List<Team> ganadores = new ArrayList<>();
        for (Match m : rondaActual) {
            if (m.getWinnerId() != null) {
                ganadores.add(m.getWinnerId());
            }
        }

        if (ganadores.size() == 1) {
            mostrarCampeon(ganadores.get(0));
        } else {
            generarNuevaRonda(torneo, ganadores, ultimaRonda + 1);
        }
    }

    private void generarNuevaRonda(Tournament torneo, List<Team> ganadores, int ronda) {
        Collections.shuffle(ganadores);

       
        int size = ganadores.size();
        int siguientePotencia2 = 1;
        while (siguientePotencia2 < size) {
            siguientePotencia2 *= 2;
        }

        int byes = siguientePotencia2 - size;
        for (int i = 0; i < byes; i++) {
            ganadores.add(null);
        }

        for (int i = 0; i < ganadores.size(); i += 2) {
            Team t1 = ganadores.get(i);
            Team t2 = ganadores.get(i + 1);

            if (t1 == null && t2 == null) continue;

            Match nuevo = new Match();
            nuevo.setTournamentId(torneo);
            nuevo.setRoundNumber(ronda);

            if (t1 != null && t2 != null) {
                nuevo.setTeam1Id(t1);
                nuevo.setTeam2Id(t2);
                nuevo.setStatus("Pendiente");
            } else {
                Team autoGanador = t1 != null ? t1 : t2;
                nuevo.setWinnerId(autoGanador);
                nuevo.setStatus("Finalizado");
            }

            matchDAO.addMatch(nuevo);
        }

        avanzarSiEsNecesario(torneo);
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
                torneoDAO.updateTournament(torneo);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
