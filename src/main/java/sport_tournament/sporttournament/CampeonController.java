package sport_tournament.sporttournament;

import database.Team;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

public class CampeonController {

    @FXML private ImageView imgEquipo;
    @FXML private Label lblNombreEquipo;

    public void inicializar(Team equipoCampeon) {
        lblNombreEquipo.setText(equipoCampeon.getTeamName());

      
        Image imagen = new Image(getClass().getResource("/" + equipoCampeon.getPhotoPath()).toExternalForm());
        imgEquipo.setImage(imagen);

       
        ScaleTransition scale = new ScaleTransition(Duration.seconds(2), imgEquipo);
        scale.setFromX(0.5);
        scale.setFromY(0.5);
        scale.setToX(1.5);
        scale.setToY(1.5);
        scale.setAutoReverse(true);
        scale.setCycleCount(4);
        scale.play();

        
        FadeTransition fade = new FadeTransition(Duration.seconds(2), lblNombreEquipo);
        fade.setFromValue(0.0);
        fade.setToValue(1.0);
        fade.setCycleCount(6);
        fade.setAutoReverse(true);
        fade.play();
    }
        public static void mostrar(Team equipo) throws Exception {
        FXMLLoader loader = new FXMLLoader(CampeonController.class.getResource("campeon.fxml"));
        Parent root = loader.load();

        CampeonController controller = loader.getController();
        controller.inicializar(equipo);

        Stage stage = new Stage();
        stage.setTitle("¡Campeón del Torneo!");
        stage.setScene(new Scene(root));
        stage.show();
    }

}
