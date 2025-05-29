package sport_tournament.sporttournament;

import database.Sport;
import database.Team;
import database_manager.SportDAO;
import database_manager.TeamDAO;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

public class TeamController {

    @FXML private TextField tf_team_name;
    @FXML private TextField tf_photo_path;
    @FXML private ComboBox<String> combo_deportes;
    @FXML private Label lbl_mensaje;
    @FXML private TableView<Team> tablaEquipos;
    @FXML private TableColumn<Team, String> colId;
    @FXML private TableColumn<Team, String> colNombre;
    @FXML private TableColumn<Team, String> colFoto;
    @FXML private TableColumn<Team, String> colDeporte;

    private final TeamDAO teamDAO = new TeamDAO();
    private final SportDAO sportDAO = new SportDAO();
    private final ObservableList<Team> equipos = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTeamId().toString()));
        colNombre.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTeamName()));
        colFoto.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getPhotoPath()));
        colDeporte.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getSportId().getSportName()));

        List<Sport> deportes = sportDAO.findAll();
        List<String> nombres = new ArrayList<>();
        for (Sport s : deportes) {
        nombres.add(s.getSportName());
        }
        combo_deportes.getItems().setAll(nombres);


        cargarEquipos();
    }

    private void cargarEquipos() {
        equipos.setAll(teamDAO.findAll());
        tablaEquipos.setItems(equipos);
    }

    @FXML
    private void cargarImagen(ActionEvent event) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Seleccionar imagen del equipo");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imágenes JPG", "*.jpg"));
        fc.setInitialDirectory(new File("src/main/resources/imagenes/teams"));

        File archivo = fc.showOpenDialog(null);
        if (archivo != null) {
            tf_photo_path.setText("imagenes/teams/" + archivo.getName());
        }
    }


    @FXML
    private void agregarEquipo(ActionEvent event) {
        String nombre = tf_team_name.getText().trim();
        String foto = tf_photo_path.getText().trim();
        String deporteNombre = combo_deportes.getValue();

        if (!nombre.isEmpty() && deporteNombre != null) {
            Sport deporte = sportDAO.findSportByName(deporteNombre);
            if (deporte != null) {
                Team nuevo = new Team();
                nuevo.setTeamName(nombre);
                nuevo.setPhotoPath(foto);
                nuevo.setSportId(deporte);
                teamDAO.addTeam(nuevo);
                lbl_mensaje.setText("Equipo agregado.");
                cargarEquipos();
            } else {
                lbl_mensaje.setText("Deporte no encontrado.");
            }
        } else {
            lbl_mensaje.setText("Complete todos los campos.");
        }
    }

    @FXML
    private void buscarEquipo(ActionEvent event) {
        String nombre = tf_team_name.getText().trim();
        Team equipo = teamDAO.findByName(nombre);
        if (equipo != null) {
            tf_team_name.setText(equipo.getTeamName());
            tf_photo_path.setText(equipo.getPhotoPath());
            combo_deportes.setValue(equipo.getSportId().getSportName());
            lbl_mensaje.setText("Equipo encontrado.");
        } else {
            lbl_mensaje.setText("No existe.");
        }
    }

    @FXML
    private void modificarEquipo(ActionEvent event) {
        String nombre = tf_team_name.getText().trim();
        Team equipo = teamDAO.findByName(nombre);
        if (equipo != null) {
            equipo.setPhotoPath(tf_photo_path.getText().trim());
            teamDAO.updateTeam(equipo);
            lbl_mensaje.setText("Modificado.");
            cargarEquipos();
        } else {
            lbl_mensaje.setText("No existe.");
        }
    }

    @FXML
    private void eliminarEquipo(ActionEvent event) {
        String nombre = tf_team_name.getText().trim();
        Team equipo = teamDAO.findByName(nombre);
        if (equipo != null) {
            teamDAO.deleteTeam(equipo);
            lbl_mensaje.setText("Eliminado.");
            cargarEquipos();
        } else {
            lbl_mensaje.setText("No existe.");
        }
    }
}
