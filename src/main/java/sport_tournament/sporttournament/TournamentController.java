package sport_tournament.sporttournament;

import database.Match;
import database.Team;
import database.Tournament;
import database.TournamentTeam;
import database_manager.MatchDAO;
import database_manager.SportDAO;
import database_manager.TournamentDAO;
import database_manager.TournamentTeamDAO;
import java.io.IOException;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.ListCell;
import java.util.Random;

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
    private final MatchDAO matchDAO = new MatchDAO();
    private final ObservableList<Tournament> torneos = FXCollections.observableArrayList();

    private Tournament torneoSeleccionado;

    @FXML
    public void initialize() {
        colNombre.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTournamentName()));
        colDeporte.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getSportId().getSportName()));
        colEquipos.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTeamCount().toString()));
        colDuracion.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getMatchTimeMinutes().toString()));

        combo_deporte.getItems().setAll(sportDAO.findAll().stream().map(s -> s.getSportName()).collect(Collectors.toList()));
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
        List<Tournament> torneosList = torneoDAO.findAll();
        comboTorneo.setItems(FXCollections.observableArrayList(torneosList));

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
            SoundManager.playSound("error.mp3");
            lbl_mensaje.setText("Complete todos los campos.");
            return;
        }

        try {
            BigInteger cantidad = new BigInteger(strCantidad);
            BigInteger duracion = new BigInteger(strDuracion);
            var deporte = sportDAO.findSportByName(deporteNombre);
            Tournament torneo = new Tournament();
            torneo.setTournamentName(nombre);
            torneo.setTeamCount(cantidad);
            torneo.setMatchTimeMinutes(duracion);
            torneo.setSportId(deporte);
            torneo.setEstado(null);
            torneoDAO.addTournament(torneo);
            lbl_mensaje.setText("Torneo registrado.");
            cargarTorneos();
        } catch (Exception e) {
            SoundManager.playSound("error.mp3");
            lbl_mensaje.setText("Datos inválidos.");
        }
    }

    @FXML
    private void generarLlaves() {
        Tournament torneo = torneoSeleccionado;
        if (torneo == null) {
            SoundManager.playSound("error.mp3");
            lbl_mensaje.setText("Selecciona un torneo antes de generar llaves.");
            return;
        }

        List<TournamentTeam> inscritos = new TournamentTeamDAO().findByTournament(torneo);
        if (inscritos.isEmpty()) {
            SoundManager.playSound("error.mp3");
            lbl_mensaje.setText("No hay equipos inscritos en este torneo.");
            return;
        }

        List<Team> equipos = inscritos.stream().map(TournamentTeam::getTeamId).collect(Collectors.toList());
        Collections.shuffle(equipos); 

        int totalEquipos = equipos.size();
        int siguientePotencia2 = 1;
        while (siguientePotencia2 < totalEquipos) {
            siguientePotencia2 *= 2;
        }

        int byes = siguientePotencia2 - totalEquipos;
        for (int i = 0; i < byes; i++) {
            equipos.add(null); 
        }

        int roundNumber = 0;
        for (int i = 0; i < equipos.size(); i += 2) {
            Team t1 = equipos.get(i);
            Team t2 = equipos.get(i + 1);

            Match match = new Match();
            match.setTournamentId(torneo);
            match.setRoundNumber(roundNumber);

            if (t1 != null && t2 != null) {
                match.setTeam1Id(t1);
                match.setTeam2Id(t2);
                SoundManager.playSound("error.mp3");
                match.setStatus("Pendiente");
            } else {
                Team paseDirecto = (t1 != null) ? t1 : t2;
                match.setWinnerId(paseDirecto);
                match.setStatus("Finalizado");
                lbl_mensaje.setText("El equipo '" + paseDirecto.getTeamName() + "' avanzó automáticamente.");
            }

            matchDAO.addMatch(match);
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
            SoundManager.playSound("error.mp3");
            alerta.setContentText("Selecciona un torneo antes de ver el esquema.");
            alerta.showAndWait();
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("EsquemaTorneo.fxml"));
            Parent root = loader.load();
            EsquemaTorneoController controller = loader.getController();
            controller.setTorneoSeleccionado(torneoSeleccionado);
            controller.cargarEsquema();

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
            SoundManager.playSound("error.mp3");
            lbl_mensaje.setText("Selecciona un torneo para eliminar.");
            return;
        }

        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Eliminar Torneo");
        SoundManager.playSound("error.mp3");
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