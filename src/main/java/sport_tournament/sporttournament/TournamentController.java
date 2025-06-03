package sport_tournament.sporttournament;

import database.Match;
import database.Sport;
import database.Team;
import database.Tournament;
import database.TournamentTeam;
import database_manager.MatchDAO;
import database_manager.SportDAO;
import database_manager.TournamentDAO;
import database_manager.TournamentTeamDAO;
import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.math.BigInteger;
import java.util.*;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.ListCell;

public class TournamentController {

    @FXML private TextField tf_nombre;
    @FXML private TextField tf_cantidad;
    @FXML private TextField tf_duracion;
    @FXML private ComboBox<String> combo_deporte;
    @FXML private ComboBox<Tournament> comboTorneo;
    @FXML private Label lbl_mensaje;

    @FXML private TableView<Tournament> tablaTorneos;
    @FXML private TableColumn<Tournament, String> colNombre;
    @FXML private TableColumn<Tournament, String> colDeporte;
    @FXML private TableColumn<Tournament, String> colEquipos;
    @FXML private TableColumn<Tournament, String> colDuracion;

    private final SportDAO sportDAO = new SportDAO();
    private final TournamentDAO torneoDAO = new TournamentDAO();
    private final ObservableList<Tournament> torneos = FXCollections.observableArrayList();
    private Tournament torneoSeleccionado;

    @FXML
    public void initialize() {
        colNombre.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTournamentName()));
        colDeporte.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getSportId().getSportName()));
        colEquipos.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTeamCount().toString()));
        colDuracion.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getMatchTimeMinutes().toString()));

        combo_deporte.getItems().setAll(sportDAO.findAll().stream().map(Sport::getSportName).collect(Collectors.toList()));

        cargarTorneos();

        tablaTorneos.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                torneoSeleccionado = newVal;
                tf_nombre.setText(torneoSeleccionado.getTournamentName());
            }
        });

        configurarComboTorneo();
    }

    private void cargarTorneos() {
        torneos.setAll(torneoDAO.findAll());
        tablaTorneos.setItems(torneos);
        configurarComboTorneo();
    }

    private void configurarComboTorneo() {
        List<Tournament> torneos = torneoDAO.findAll();
        comboTorneo.setItems(FXCollections.observableArrayList(torneos));

        comboTorneo.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Tournament item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getTournamentName());
            }
        });

        comboTorneo.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Tournament item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getTournamentName());
            }
        });
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

    @FXML
    private void generarLlaves() {
        Tournament torneo = torneoSeleccionado;
        if (torneo == null) {
            lbl_mensaje.setText("Selecciona un torneo antes de generar llaves.");
            return;
        }

        List<TournamentTeam> inscritos = new TournamentTeamDAO().findByTournament(torneo);
        if (inscritos.isEmpty()) {
            lbl_mensaje.setText("No hay equipos inscritos en este torneo.");
            return;
        }

        List<Team> equipos = new ArrayList<>();
        for (TournamentTeam tt : inscritos) {
            equipos.add(tt.getTeamId());
        }

        if (equipos.size() % 2 != 0) {
            Team equipoConPase = equipos.get(new Random().nextInt(equipos.size()));
            equipos.remove(equipoConPase);
            Match paseAutomatico = new Match();
            paseAutomatico.setTournamentId(torneo);
            paseAutomatico.setWinnerId(equipoConPase);
            paseAutomatico.setStatus("Finalizado");
            new MatchDAO().addMatch(paseAutomatico);
            lbl_mensaje.setText("El equipo '" + equipoConPase.getTeamName() + "' avanzó directamente a la siguiente ronda.");
        }

        Collections.shuffle(equipos);
        for (int i = 0; i < equipos.size(); i += 2) {
            Match match = new Match();
            match.setTournamentId(torneo);
            match.setTeam1Id(equipos.get(i));
            match.setTeam2Id(equipos.get(i + 1));
            match.setStatus("Pendiente");
            new MatchDAO().addMatch(match);
        }

        lbl_mensaje.setText("Llaves generadas correctamente.");
    }

    @FXML
    private void mostrarEsquemaTorneo() {
        Tournament torneoSeleccionado = comboTorneo.getSelectionModel().getSelectedItem();
        if (torneoSeleccionado == null) {
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setTitle("Esquema del Torneo");
            alerta.setHeaderText(null);
            alerta.setContentText("Selecciona un torneo antes de ver el esquema.");
            alerta.showAndWait();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("EsquemaTorneo.fxml"));
            Parent root = loader.load();
            EsquemaTorneoController controller = loader.getController();
            controller.setTorneoSeleccionado(torneoSeleccionado);
            Stage stage = new Stage();
            stage.setTitle("Esquema del Torneo: " + torneoSeleccionado.getTournamentName());
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void eliminarTorneo() {
        Tournament torneoSeleccionado = tablaTorneos.getSelectionModel().getSelectedItem();

        if (torneoSeleccionado == null) {
            lbl_mensaje.setText("Selecciona un torneo para eliminar.");
            return;
        }

        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Eliminar Torneo");
        alerta.setHeaderText("¿Estás seguro de eliminar el torneo?");
        alerta.setContentText("Esta acción no se puede deshacer.");

        Optional<ButtonType> resultado = alerta.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            torneoDAO.deleteTournament(torneoSeleccionado);
            lbl_mensaje.setText("Torneo eliminado correctamente.");
            cargarTorneos();
        }
    }
}
