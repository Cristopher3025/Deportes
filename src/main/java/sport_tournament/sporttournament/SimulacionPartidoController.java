package sport_tournament.sporttournament;

import database.Match;
import database.MatchResult;
import database.Team;
import database.Tournament;
import database_manager.MatchDAO;
import database_manager.MatchResultDAO;
import database_manager.TournamentDAO;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

public class SimulacionPartidoController {
    @FXML private ImageView img_equipo1;
    @FXML private ImageView img_equipo2;
    @FXML private ImageView img_balon;
    @FXML private Label lbl_nombre1;
    @FXML private Label lbl_nombre2;
    @FXML private Label lbl_marcador;
    @FXML private Label lbl_timer;
    @FXML private Button btn_finalizar;
    @FXML private AnchorPane root;

    private Team equipo1;
    private Team equipo2;
    private Match partido;
    private int goles1 = 0;
    private int goles2 = 0;
    private Timeline temporizador;
    private int segundosRestantes;

    public void inicializar(Team t1, Team t2, Match partido) {
        this.equipo1 = t1;
        this.equipo2 = t2;
        this.partido = partido;
        lbl_nombre1.setText(t1.getTeamName());
        lbl_nombre2.setText(t2.getTeamName());

        img_equipo1.setImage(new Image(getClass().getResource("/" + t1.getPhotoPath()).toExternalForm()));
        img_equipo2.setImage(new Image(getClass().getResource("/" + t2.getPhotoPath()).toExternalForm()));

        String rutaBalon = partido.getTournamentId().getSportId().getBallImagePath();
        if (rutaBalon != null && !rutaBalon.isEmpty()) {
            try {
                img_balon.setImage(new Image(getClass().getResource("/" + rutaBalon).toExternalForm()));
            } catch (Exception e) {
                img_balon.setImage(new Image(getClass().getResource("/imagenes/ball/football.jpg").toExternalForm()));
            }
        } else {
            img_balon.setImage(new Image(getClass().getResource("/imagenes/ball/football.jpg").toExternalForm()));
        }

        habilitarArrastre();
        actualizarMarcador();
        iniciarTemporizador();
    }

    private void habilitarArrastre() {
        img_balon.setOnMousePressed(e -> {
            img_balon.setTranslateX(0);
            img_balon.setTranslateY(0);
        });

        img_balon.setOnMouseDragged(e -> {
            img_balon.setLayoutX(e.getSceneX() - img_balon.getFitWidth() / 2);
            img_balon.setLayoutY(e.getSceneY() - img_balon.getFitHeight() / 2);
        });

        img_balon.setOnMouseReleased(this::detectarGol);
    }

    private void detectarGol(MouseEvent e) {
        if (img_balon.getBoundsInParent().intersects(img_equipo1.getBoundsInParent())) {
            goles1++;
        } else if (img_balon.getBoundsInParent().intersects(img_equipo2.getBoundsInParent())) {
            goles2++;
        }
        img_balon.setLayoutX(300);
        img_balon.setLayoutY(200);
        actualizarMarcador();
        SoundManager.playSound("anotacion.mp3");
    }

    private void actualizarMarcador() {
        lbl_marcador.setText(goles1 + " - " + goles2);
    }

    private void iniciarTemporizador() {
        int minutos = partido.getTournamentId().getMatchTimeMinutes().intValue();
        segundosRestantes = minutos * 60;
        lbl_timer.setText("Tiempo restante: " + segundosRestantes + "s");
        temporizador = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            segundosRestantes--;
            lbl_timer.setText("Tiempo restante: " + segundosRestantes + "s");
            if (segundosRestantes <= 0) {
                temporizador.stop();
                finalizarPartido();
            }
        }));
        temporizador.setCycleCount(segundosRestantes);
        temporizador.play();
    }

    @FXML
    private void finalizarPartido() {
        if (btn_finalizar.isDisabled()) return;
        if (temporizador != null) temporizador.stop();
        SoundManager.playSound("pitazo.mp3");
        MatchResultDAO resultDAO = new MatchResultDAO();

        MatchResult r1 = new MatchResult();
        r1.setMatchId(partido);
        r1.setTeamId(equipo1);
        r1.setGoals(BigInteger.valueOf(goles1));
        resultDAO.addResult(r1);

        MatchResult r2 = new MatchResult();
        r2.setMatchId(partido);
        r2.setTeamId(equipo2);
        r2.setGoals(BigInteger.valueOf(goles2));
        resultDAO.addResult(r2);

        if (goles1 != goles2) {
            partido.setWinnerId(goles1 > goles2 ? equipo1 : equipo2);
            partido.setStatus("Finalizado");
            new MatchDAO().updateMatch(partido);
            btn_finalizar.setDisable(true);
            actualizarMarcador();
            new TorneoService().avanzarSiEsNecesario(partido.getTournamentId());
            
        } else {
            try {
                SoundManager.playSound("error.mp3");

                FXMLLoader loader = new FXMLLoader(getClass().getResource("desempate.fxml"));
                Parent root = loader.load();
                DesempateController controller = loader.getController();
                controller.inicializar(equipo1, equipo2, () -> {
                    Team ganador = controller.getGanador();
                    if (ganador != null) {
                        partido.setWinnerId(ganador);
                        partido.setStatus("Finalizado");
                        new MatchDAO().updateMatch(partido);
                        btn_finalizar.setDisable(true);
                        actualizarMarcador();
                        new TorneoService().avanzarSiEsNecesario(partido.getTournamentId());
                        
                    }
                });
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Desempate");
                stage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void mostrarCampeonSiAplica(Match partido) {
        MatchDAO matchDAO = new MatchDAO();
        List<Match> restantes = matchDAO.findByTournament(partido.getTournamentId())
            .stream()
            .filter(m -> !"Finalizado".equalsIgnoreCase(m.getStatus()))
            .collect(Collectors.toList());
        if (restantes.isEmpty()) {
            Tournament torneo = partido.getTournamentId();
            torneo.setEstado("Finalizado");
            new TournamentDAO().updateTournament(torneo);
            Team campeon = partido.getWinnerId();
            try {
                CampeonController.mostrar(campeon);
                CertificadoService.generarCertificado(torneo, campeon);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
