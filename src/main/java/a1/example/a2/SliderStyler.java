package a1.example.a2;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.Region;

public class SliderStyler {

    public static void updateSliderBindings(Slider slider, Label valueLabel) {
        // 1️⃣ Listener für Änderungen → Label updaten & Farbe setzen
        slider.valueProperty().addListener((obs, oldVal, newVal) -> {
            int value = newVal.intValue();
            valueLabel.setText(String.valueOf(value));
            bindSliderColor(slider, value); // Farbe aktualisieren
        });

        // 2️⃣ Startfarbe → warten bis der Slider wirklich angezeigt wird
        slider.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                //slider.applyCss();
               // slider.layout();
                int value = (int) slider.getValue();
                valueLabel.setText(String.valueOf(value));
                bindSliderColor(slider, value);
            }
        });
    }


    public static void bindSliderColor(Slider slider, int value) {
        String fillColor;
        if (value <= 10) {
            fillColor = "#8B0000"; // dunkelrot
        } else if (value <= 30) {
            fillColor = "#FF3B30"; // red
        } else if (value < 50) {
            fillColor = "#FF9500"; // orange
        } else {
            fillColor = "#00cc66"; // grün
        }


        // WARTE, bis Slider gerendert ist, dann suche Track-Node
        Platform.runLater(() -> {
            Region track = (Region) slider.lookup(".track");
            if (track != null) {
                track.setStyle(String.format(
                        "-fx-background-color: linear-gradient(to right, %s %d%%, white %d%%);",
                        fillColor, value, value
                ));
            }
        });
    }
}