package sport_tournament.sporttournament;

import database.Sport;
import database_manager.SportDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class SportController {

    @FXML private TextField tf_sport_name;
    @FXML private Label lbl_mensaje;
    @FXML private TableView<Sport> tablaDeportes;
    @FXML private TableColumn<Sport, String> colNombre;
    @FXML private TableColumn<Sport, String> colImagen;
    @FXML private TableColumn<Sport, String> colId;

    private final SportDAO dao = new SportDAO();
    private final ObservableList<Sport> deportes = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getSportId().toString()));
        colNombre.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getSportName()));
        colImagen.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getBallImagePath()));
        cargarDeportes();
    }

    private void cargarDeportes() {
        deportes.setAll(dao.findAll());
        tablaDeportes.setItems(deportes);
    }

    @FXML
    private void agregarNombre(ActionEvent event) {
        String nombre = tf_sport_name.getText().trim();
        if (!nombre.isEmpty()) {
            Sport sport = new Sport(nombre);
            dao.addSport(sport);
            lbl_mensaje.setText("Agregado: " + nombre);
            cargarDeportes();
        } else {
            lbl_mensaje.setText("Ingrese un nombre.");
        }
    }

    @FXML
    private void buscarNombre(ActionEvent event) {
        String nombre = tf_sport_name.getText().trim();
        Sport s = dao.findSportByName(nombre);
        if (s != null) {
            tf_sport_name.setText(s.getSportName());
            lbl_mensaje.setText("Encontrado.");
        } else {
            lbl_mensaje.setText("No existe.");
        }
    }

    @FXML
    private void modificarNombre(ActionEvent event) {
        String nombre = tf_sport_name.getText().trim();
        Sport s = dao.findSportByName(nombre);
        if (s != null) {
            s.setSportName(nombre + " (modificado)");
            dao.updateSport(s);
            lbl_mensaje.setText("Modificado.");
            cargarDeportes();
        } else {
            lbl_mensaje.setText("No encontrado.");
        }
    }

    @FXML
    private void eliminarNombre(ActionEvent event) {
        String nombre = tf_sport_name.getText().trim();
        Sport s = dao.findSportByName(nombre);
        if (s != null) {
            dao.deleteSport(s);
            lbl_mensaje.setText("Eliminado.");
            cargarDeportes();
        } else {
            lbl_mensaje.setText("No encontrado.");
        }
    }
}
