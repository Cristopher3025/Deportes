package sport_tournament.sporttournament;

import database.Sport;
import database.Tournament;
import database_manager.SportDAO;
import database_manager.TournamentDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class TournamentController {

    @FXML private TextField tf_nombre;
    @FXML private TextField tf_cantidad;
    @FXML private TextField tf_duracion;
    @FXML private ComboBox<String> combo_deporte;
    @FXML private Label lbl_mensaje;

    @FXML private TableView<Tournament> tablaTorneos;
    @FXML private TableColumn<Tournament, String> colNombre;
    @FXML private TableColumn<Tournament, String> colDeporte;
    @FXML private TableColumn<Tournament, String> colEquipos;
    @FXML private TableColumn<Tournament, String> colDuracion;

    private final SportDAO sportDAO = new SportDAO();
    private final TournamentDAO torneoDAO = new TournamentDAO();
    private final ObservableList<Tournament> torneos = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colNombre.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTournamentName()));
        colDeporte.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getSportId().getSportName()));
        colEquipos.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTeamCount().toString()));
        colDuracion.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getMatchTimeMinutes().toString()));

        
        List<String> nombres = new ArrayList<>();
        for (Sport s : sportDAO.findAll()) {
            nombres.add(s.getSportName());
        }
        combo_deporte.getItems().setAll(nombres);

        cargarTorneos();
    }

    private void cargarTorneos() {
        torneos.setAll(torneoDAO.findAll());
        tablaTorneos.setItems(torneos);
    }

    @FXML
    private void registrarTorneo() {
        String nombre = tf_nombre.getText().trim();
        String deporteNombre = combo_deporte.getValue();
        String strCantidad = tf_cantidad.getText().trim();
        String strDuracion = tf_duracion.getText().trim();

        if (nombre.isEmpty() || deporteNombre == null || strCantidad.isEmpty() || strDuracion.isEmpty()) {
            lbl_mensaje.setText("Complete todos los campos.");
            return;
        }

        try {
            BigInteger cantidad = new BigInteger(strCantidad);
            BigInteger duracion = new BigInteger(strDuracion);
            Sport deporte = sportDAO.findSportByName(deporteNombre);

            Tournament torneo = new Tournament();
            torneo.setTournamentName(nombre);
            torneo.setTeamCount(cantidad);
            torneo.setMatchTimeMinutes(duracion);
            torneo.setSportId(deporte);
            torneoDAO.addTournament(torneo);

            lbl_mensaje.setText("Torneo registrado.");
            cargarTorneos();
        } catch (Exception e) {
            lbl_mensaje.setText("Datos inválidos.");
        }
    }
}
