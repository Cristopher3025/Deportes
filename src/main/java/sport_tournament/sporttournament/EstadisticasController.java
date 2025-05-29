package sport_tournament.sporttournament;

import database.Match;
import database.MatchResult;
import database.Team;
import database.Tournament;
import database.TournamentTeam;
import database_manager.MatchDAO;
import database_manager.MatchResultDAO;
import database_manager.TeamDAO;
import database_manager.TournamentTeamDAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.*;
import java.util.stream.Collectors;

public class EstadisticasController {

    @FXML private ComboBox<String> comboEquipos;
    @FXML private TableView<EstadisticaTorneo> tablaTorneos;
    @FXML private TableColumn<EstadisticaTorneo, String> colTorneo;
    @FXML private TableColumn<EstadisticaTorneo, String> colPosicion;
    @FXML private TableColumn<EstadisticaTorneo, String> colGanados;
    @FXML private TableColumn<EstadisticaTorneo, String> colGoles;
    @FXML private TableColumn<EstadisticaTorneo, String> colPuntos;

    @FXML private Label lblPartidos;
    @FXML private Label lblGanados;
    @FXML private Label lblGoles;
    @FXML private Label lblPuntos;

    @FXML private ListView<String> listaRanking;

    private final TeamDAO teamDAO = new TeamDAO();
    private final TournamentTeamDAO torneoTeamDAO = new TournamentTeamDAO();
    private final MatchDAO matchDAO = new MatchDAO();
    private final MatchResultDAO resultDAO = new MatchResultDAO();

    private List<Team> equipos;

    @FXML
    public void initialize() {
        colTorneo.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().torneo));
        colPosicion.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(String.valueOf(data.getValue().posicion)));
        colGanados.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(String.valueOf(data.getValue().ganados)));
        colGoles.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(String.valueOf(data.getValue().goles)));
        colPuntos.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(String.valueOf(data.getValue().puntos)));

        equipos = teamDAO.findAll();
        comboEquipos.getItems().setAll(equipos.stream().map(Team::getTeamName).collect(Collectors.toList()));

        comboEquipos.setOnAction(e -> mostrarEstadisticas());

        cargarRankingGlobal();
    }

    private void mostrarEstadisticas() {
        String nombre = comboEquipos.getValue();
        if (nombre == null) return;

        Team equipo = teamDAO.findByName(nombre);
        if (equipo == null) return;

        List<TournamentTeam> participaciones = torneoTeamDAO.findAll()
                .stream().filter(tt -> tt.getTeamId().equals(equipo)).collect(Collectors.toList());

        List<EstadisticaTorneo> estadisticas = new ArrayList<>();
        int totalGoles = 0;
        int totalGanados = 0;
        int totalPuntos = 0;

        for (TournamentTeam tt : participaciones) {
            Tournament torneo = tt.getTournamentId();
            List<Match> matches = matchDAO.findByTournament(torneo);
            int ganados = 0;
            int goles = 0;
            int puntos = 0;

            for (Match m : matches) {
                if (!"Finalizado".equals(m.getStatus())) continue;

                List<MatchResult> resultados = resultDAO.findByMatch(m);
                for (MatchResult r : resultados) {
                    if (r.getTeamId().equals(equipo)) {
                        goles += r.getGoals().intValue();
                    }
                }

                if (m.getWinnerId() != null && m.getWinnerId().equals(equipo)) {
                    ganados++;
                    if (goles > 0) {
                        puntos += 3;
                    } else {
                        puntos += 2; 
                    }
                }
            }

            estadisticas.add(new EstadisticaTorneo(torneo.getTournamentName(), tt.getPosition().intValue(), ganados, goles, puntos));

            totalGoles += goles;
            totalGanados += ganados;
            totalPuntos += puntos;
        }

        tablaTorneos.setItems(FXCollections.observableArrayList(estadisticas));
        lblPartidos.setText("Partidos jugados: " + estadisticas.size());
        lblGanados.setText("Partidos ganados: " + totalGanados);
        lblGoles.setText("Goles anotados: " + totalGoles);
        lblPuntos.setText("Puntos totales: " + totalPuntos);
    }

    private void cargarRankingGlobal() {
        Map<Team, Integer> ranking = new HashMap<>();

        for (Team t : teamDAO.findAll()) {
            int puntos = 0;
            List<Match> matches = matchDAO.findAll();

            for (Match m : matches) {
                if (!"Finalizado".equals(m.getStatus())) continue;

                if (m.getWinnerId() != null && m.getWinnerId().equals(t)) {
                    List<MatchResult> resultados = resultDAO.findByMatch(m);
                    boolean tieneGoles = resultados.stream()
                            .anyMatch(r -> r.getTeamId().equals(t) && r.getGoals().intValue() > 0);
                    puntos += tieneGoles ? 3 : 2;
                }
            }
            ranking.put(t, puntos);
        }

        List<Map.Entry<Team, Integer>> ordenado = ranking.entrySet().stream()
                .sorted((a, b) -> b.getValue() - a.getValue()).collect(Collectors.toList());

        List<String> rankingStrings = new ArrayList<>();
        int pos = 1;
        for (Map.Entry<Team, Integer> entry : ordenado) {
            rankingStrings.add(pos++ + ". " + entry.getKey().getTeamName() + " - " + entry.getValue() + " pts");
        }

        listaRanking.setItems(FXCollections.observableArrayList(rankingStrings));
    }

    private static class EstadisticaTorneo {
        String torneo;
        int posicion;
        int ganados;
        int goles;
        int puntos;

        public EstadisticaTorneo(String torneo, int posicion, int ganados, int goles, int puntos) {
            this.torneo = torneo;
            this.posicion = posicion;
            this.ganados = ganados;
            this.goles = goles;
            this.puntos = puntos;
        }
    }
}

