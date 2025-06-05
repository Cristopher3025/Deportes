package sport_tournament.sporttournament;

import database.Match;
import database.Tournament;
import database_manager.MatchDAO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.*;

public class EsquemaTorneoController implements Initializable {

    private Tournament torneoSeleccionado;

    public void setTorneoSeleccionado(Tournament torneo) {
        this.torneoSeleccionado = torneo;
    }

    @FXML
    private Canvas canvasEsquema;

    private final MatchDAO matchDAO = new MatchDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
       
    }

    private void dibujarEsquema() {
        GraphicsContext gc = canvasEsquema.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvasEsquema.getWidth(), canvasEsquema.getHeight());

        List<Match> todosLosPartidos = matchDAO.findByTournament(torneoSeleccionado);
        Map<Integer, List<Match>> rondas = agruparPorRonda(todosLosPartidos);

        int niveles = rondas.size();
        int espacioVertical = 80;
        int inicioX = 50;
        int anchoNivel = 220;

        String[] etiquetasRondas = { "Fase Inicial", "Octavos", "Cuartos", "Semifinal", "Final" };

        Map<Match, Double[]> coordenadas = new HashMap<>();

        for (Map.Entry<Integer, List<Match>> ronda : rondas.entrySet()) {
            int rondaNum = ronda.getKey();
            List<Match> partidos = ronda.getValue();
            int yBase = 100;
            int x = inicioX + rondaNum * anchoNivel;

            String etiqueta = rondaNum < etiquetasRondas.length ? etiquetasRondas[rondaNum] : "Ronda " + (rondaNum + 1);
            gc.setFill(Color.BLUE);
            gc.setFont(new Font("Arial", 18));
            gc.fillText(etiqueta, x, 30);

            for (int i = 0; i < partidos.size(); i++) {
                Match match = partidos.get(i);
                int y = yBase + i * espacioVertical * 2;

                String texto = match.getTeam1Id().getTeamName() + " vs " + match.getTeam2Id().getTeamName();
                gc.setFill(Color.BLACK);
                gc.setFont(new Font("Arial", 14));
                gc.fillText(texto, x, y);

                if ("Finalizado".equals(match.getStatus()) && match.getWinnerId() != null) {
                    gc.setFill(Color.GREEN);
                    gc.fillText("Ganador: " + match.getWinnerId().getTeamName(), x, y + 15);
                }

               
                coordenadas.put(match, new Double[] { (double) x, (double) y });
            }
        }

       
        for (Match match : coordenadas.keySet()) {
            if (match.getWinnerId() != null) {
                Match siguiente = encontrarSiguienteMatch(match.getWinnerId(), match.getRoundNumber() + 1, todosLosPartidos);
                if (siguiente != null && coordenadas.containsKey(siguiente)) {
                    Double[] origen = coordenadas.get(match);
                    Double[] destino = coordenadas.get(siguiente);
                    gc.setStroke(Color.GRAY);
                    gc.setLineWidth(1.5);
                    gc.strokeLine(origen[0] + 100, origen[1] + 7, destino[0], destino[1] + 7);
                }
            }
        }
    }

    private Map<Integer, List<Match>> agruparPorRonda(List<Match> partidos) {
        Map<Integer, List<Match>> rondas = new TreeMap<>();
        for (Match match : partidos) {
            if (match.getRoundNumber() == null) continue;
            rondas.computeIfAbsent(match.getRoundNumber(), k -> new ArrayList<>()).add(match);
        }
        return rondas;
    }
    
    public void cargarEsquema() {
        if (torneoSeleccionado != null) {
            dibujarEsquema();
        }
    }

    private Match encontrarSiguienteMatch(database.Team ganador, int rondaBuscada, List<Match> partidos) {
        for (Match m : partidos) {
            if (m.getRoundNumber() != null && m.getRoundNumber() == rondaBuscada) {
                if (m.getTeam1Id().equals(ganador) || m.getTeam2Id().equals(ganador)) {
                    return m;
                }
            }
        }
        return null;
    }
}


