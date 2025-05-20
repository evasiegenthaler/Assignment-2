package a1.example.a2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Frame1Controller {
    @FXML private StackPane spFrame1;
    @FXML private ImageView ivFrame1;

    @FXML
    public void switchToFrame2(ActionEvent event) throws IOException { //übergibt Event-Objekt=event also der Klick, throws IOException: Falls beim Laden der FXML-Datei ein Fehler passiert (z.B. Datei nicht gefunden), wirft die Methode eine IO Exception=Input/Output.
        Parent root = FXMLLoader.load(getClass().getResource("Frame2.fxml")); //Parent ist der allgemeinste Typ, der alle Container-Klassen erbt.
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow(); //speichert das in der variable stage
        stage.setScene(new Scene(root)); //Holte die stage und erstellt eine neue scene mit dem Inhalt root= die fxml datei
        stage.setFullScreen(true); //wechselt in Vollbildschirmmodus, java FX method--gehört zur stage klasse
        stage.show(); // zeigt die stage--methode aus stage klasse
    }
    @FXML
    private void initialize() {
        ivFrame1.fitWidthProperty().bind(spFrame1.widthProperty()); //Bindet die breite des Bildes and die breite des Containers l
        ivFrame1.fitHeightProperty().bind(spFrame1.heightProperty());
        ivFrame1.setPreserveRatio(false); // Bild füllt den kompletten Bereich, auch wenn verzerrt
    }

}
