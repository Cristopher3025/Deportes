package sport_tournament.sporttournament;

import database.Sport;
import database_manager.SportDAO;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

public class SportController {

    @FXML private TextField tf_sport_name;
    @FXML private TextField tf_puntuaciones; 
    @FXML private Label lbl_mensaje;
    @FXML private TableView<Sport> tablaDeportes;
    @FXML private TableColumn<Sport, String> colNombre;
    @FXML private TableColumn<Sport, String> colImagen;
    @FXML private TableColumn<Sport, String> colId;
    @FXML private TextField tf_ball_path;

    private final SportDAO dao = new SportDAO();
    private final ObservableList<Sport> deportes = FXCollections.observableArrayList();

    
    private static final Map<String, List<Integer>> anotacionesPorDeporte = new HashMap<>();
    private static SportController instancia;

    public static List<Integer> getAnotaciones(String nombre) {
        return anotacionesPorDeporte.get(nombre.toLowerCase());
    }

    @FXML
    public void initialize() {
        instancia = this;

        colId.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getSportId().toString()));
        colNombre.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getSportName()));
        colImagen.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getBallImagePath()));
        cargarDeportes();

        tablaDeportes.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                tf_sport_name.setText(newVal.getSportName());
                List<Integer> valores = anotacionesPorDeporte.get(newVal.getSportName().toLowerCase());
                if (valores != null) {
                    tf_puntuaciones.setText(valores.toString().replace("[", "").replace("]", ""));
                } else {
                    tf_puntuaciones.setText("");
                }
            }
        });
    }

    private void cargarDeportes() {
        deportes.setAll(dao.findAll());
        tablaDeportes.setItems(deportes);
    }
    
    @FXML
    private void cargarImagenBalon(ActionEvent event) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Seleccionar imagen del balón");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg"));
        fc.setInitialDirectory(new File("imagenes/ball")); 

        File archivo = fc.showOpenDialog(null);
        if (archivo != null) {
            tf_ball_path.setText("imagenes/ball/" + archivo.getName());
        }
    }

    
    @FXML
    private void agregarNombre(ActionEvent event) {
        String nombre = tf_sport_name.getText().trim();
        String puntosRaw = tf_puntuaciones.getText().trim();

        if (nombre.isEmpty()) {
            lbl_mensaje.setText("Ingrese un nombre.");
            return;
        }

        List<Integer> valores = new ArrayList<>();
        try {
            for (String s : puntosRaw.split(",")) {
                valores.add(Integer.parseInt(s.trim()));
            }
        } catch (NumberFormatException e) {
            lbl_mensaje.setText("Valores de anotación inválidos.");
            return;
        }

        Sport sport = new Sport(nombre);
        dao.addSport(sport);
        anotacionesPorDeporte.put(nombre.toLowerCase(), valores);

        lbl_mensaje.setText("Deporte agregado con anotaciones.");
        cargarDeportes();
        sport.setBallImagePath(tf_ball_path.getText().trim());

    }

    @FXML
    private void buscarNombre(ActionEvent event) {
        String nombre = tf_sport_name.getText().trim();
        Sport s = dao.findSportByName(nombre);
        if (s != null) {
            tf_sport_name.setText(s.getSportName());
            List<Integer> valores = anotacionesPorDeporte.get(nombre.toLowerCase());
            tf_puntuaciones.setText(valores != null ? valores.toString().replace("[", "").replace("]", "") : "");
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
            anotacionesPorDeporte.remove(nombre.toLowerCase()); 
            lbl_mensaje.setText("Eliminado.");
            cargarDeportes();
        } else {
            lbl_mensaje.setText("No encontrado.");
        }
    }
}
