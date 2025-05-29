package sport_tournament.sporttournament;

import database.Team;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class DesempateController {

    @FXML private Label lblPregunta;
    @FXML private Button btnEquipo1;
    @FXML private Button btnEquipo2;

    private Team ganador;
    private Team equipo1;
    private Team equipo2;
    private Runnable callback;

    public void inicializar(Team equipo1, Team equipo2, Runnable callback) {
        this.equipo1 = equipo1;
        this.equipo2 = equipo2;
        this.callback = callback;

        lblPregunta.setText("Selecciona el ganador del desempate:");
        btnEquipo1.setText(equipo1.getTeamName());
        btnEquipo2.setText(equipo2.getTeamName());
    }

    @FXML
    private void seleccionarGanador1() {
        ganador = equipo1;
        cerrar();
    }

    @FXML
    private void seleccionarGanador2() {
        ganador = equipo2;
        cerrar();
    }

    private void cerrar() {
        if (callback != null) {
            callback.run();
        }
        ((Stage) lblPregunta.getScene().getWindow()).close();
    }

    public Team getGanador() {
        return ganador;
    }
}

