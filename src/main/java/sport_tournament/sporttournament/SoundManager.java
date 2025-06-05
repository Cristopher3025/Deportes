package sport_tournament.sporttournament;

import javafx.scene.media.AudioClip;

import java.net.URL;

public class SoundManager {

    public static void playSound(String filename) {
        try {
            URL soundURL = SoundManager.class.getResource("/sounds/" + filename);
            if (soundURL != null) {
                AudioClip clip = new AudioClip(soundURL.toExternalForm());
                clip.play();
            } else {
                System.err.println("Sonido no encontrado: " + filename);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
