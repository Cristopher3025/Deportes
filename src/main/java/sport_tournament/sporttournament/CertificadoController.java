package sport_tournament.sporttournament;

import database.Team;
import database.Tournament;
import database.Match;
import database_manager.MatchDAO;
import database_manager.TournamentDAO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import javafx.scene.control.ListCell;

public class CertificadoController implements Initializable {

    @FXML private ComboBox<Tournament> comboTorneos;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<Tournament> todos = new TournamentDAO().findAll();
        List<Tournament> finalizados = new ArrayList<>();

        for (Tournament torneo : todos) {
            if ("Finalizado".equalsIgnoreCase(torneo.getEstado())) {
                finalizados.add(torneo);
            }
        }

        comboTorneos.setItems(FXCollections.observableArrayList(finalizados));

        comboTorneos.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Tournament item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getTournamentName());
            }
        });

        comboTorneos.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Tournament item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getTournamentName());
            }
        });
    }

    @FXML
    private void generarCertificado() {
        Tournament torneo = comboTorneos.getSelectionModel().getSelectedItem();
        if (torneo == null) return;

        List<Match> partidos = new MatchDAO().findByTournament(torneo);

        int rondaMaxima = partidos.stream()
            .filter(m -> m.getRoundNumber() != null)
            .mapToInt(Match::getRoundNumber)
            .max()
            .orElse(-1);

        List<Match> finales = partidos.stream()
        .filter(m -> m.getRoundNumber() != null && m.getRoundNumber() == rondaMaxima)
        .collect(Collectors.toList());

        List<Team> ganadores = finales.stream()
        .map(Match::getWinnerId)
        .filter(Objects::nonNull)
        .distinct()
        .collect(Collectors.toList());


        if (ganadores.size() != 1) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No se puede determinar el campeón");
            alert.setContentText("Verifica que todos los partidos hayan sido finalizados correctamente.");
            alert.showAndWait();
            return;
        }

        Team campeon = ganadores.get(0);

        CertificadoService.generarCertificado(torneo, campeon);

        String filePath = "src/main/resources/Certificados/certificado_" +
                torneo.getTournamentName().replace(" ", "_") + "_" +
                campeon.getTeamName().replace(" ", "_") + ".pdf";

        try {
            Desktop.getDesktop().open(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Certificado generado");
        alerta.setHeaderText(null);
        alerta.setContentText("El certificado se generó correctamente.");
        alerta.showAndWait();

        ((Stage) comboTorneos.getScene().getWindow()).close();
    }

}

