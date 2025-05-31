package sport_tournament.sporttournament;

import database.Match;
import database.Team;
import database.Tournament;
import database_manager.MatchDAO;
import database_manager.TournamentTeamDAO;

import java.util.ArrayList;
import java.util.List;

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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
