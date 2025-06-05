package sport_tournament.sporttournament;

import javafx.scene.Scene;

public class ThemeManager {
    private static String temaActual = "light-theme";

    public static void aplicarTema(Scene scene, String nombreTema) {
        scene.getStylesheets().clear();
        scene.getStylesheets().add(
            ThemeManager.class.getResource("/styles/" + nombreTema + ".css").toExternalForm()
        );
        temaActual = nombreTema;
    }

    public static String getTemaActual() {
        return temaActual;
    }
}
