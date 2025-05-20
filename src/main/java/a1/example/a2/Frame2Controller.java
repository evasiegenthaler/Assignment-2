package a1.example.a2;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.util.Optional;

public class Frame2Controller {

    @FXML private Button btnShow;
    @FXML private ImageView ivFrame2;
    @FXML private StackPane spFrame2;
    @FXML private Rectangle dimBackground;

    @FXML
    public void switchToFrame3(ActionEvent event) throws IOException {
        dimBackground.setVisible(true); //lässt das vierreck erscheinen das dimmt

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Frame3.fxml"));
        Parent popupContent = fxmlLoader.load(); //lädt fxml in die variable popupContent

        Stage popupStage = new Stage(); //Erstellt ein neues Fenster
        popupStage.setScene(new Scene(popupContent)); //Nimmt das Fenster und lädt eine neue Szene rein nämlich popupContent was das fxml file ist
        popupStage.initModality(Modality.WINDOW_MODAL); //Blockiert nur das Fenster das als owner gesetzt wird also das Hauptfenster
        popupStage.initOwner(((Node) event.getSource()).getScene().getWindow()); // setzt das Hauptfenster (von dem der Klick kam) als "Owner"
        popupStage.setResizable(false); // Benutzer kann die grösse des Fensters nicht ändern

        Frame3Controller controller = fxmlLoader.getController(); //Holt den Frame3 Controller
        controller.setPopupStage(popupStage); // Gibt dem Controller Zugriff auf das Fenster Frame3
        controller.disableAutoFocus();

        popupStage.setOnHidden(e -> dimBackground.setVisible(false)); //setOnHiden= event handler methode, die sagt wenn das Fenster geschlossen wird dann füre folgenden Code aus.
        popupStage.show(); //Zeigt die popupStage
    }

    @FXML
    public void switchToFrame4(ActionEvent event) throws IOException { //Event Objekt= wann wurde die komponente geklickt, in welcher stage war das
        if (PetManager.getPetList().isEmpty()) {
            Frame3Controller.showAlert("No Pets Yet", "No Characters Available", "Please create a character before viewing the list.", btnShow);
            return;
        }
        Parent root = FXMLLoader.load(getClass().getResource("Frame4.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow(); //event.getSource das Rückgabe Objekt wird auf Node gecastet damit getScene () gerufen kann
        stage.setScene(new Scene(root));
        stage.setFullScreen(true);
        stage.show();
    }

    @FXML
    public void quitApplication(ActionEvent event) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Exit Application");
        confirmAlert.setHeaderText("Are you sure you want to quit?");
        confirmAlert.setContentText("All unsaved progress will be lost.");

        confirmAlert.initOwner(((Node) event.getSource()).getScene().getWindow());
        Optional<ButtonType> result = confirmAlert.showAndWait(); // Wartet was User drückt. Das Ergebnis ist ein Optional weil User könnte ok drücken aber fenster auch einfach schliessen. Dann wäre kein wert vorhanden

        if (result.isPresent() && result.get() == ButtonType.OK) { //Das resultat entspricht Ok dann wird Spiel beendet. Kein else weil wenn nicht dan läuft spiel einfach weiter
            Platform.exit(); //Java spezifisch beenden
            System.exit(0); //allg Java beendet
        }
    }

    @FXML
    private void initialize() {
            ivFrame2.fitWidthProperty().bind(spFrame2.widthProperty());
            ivFrame2.fitHeightProperty().bind(spFrame2.heightProperty());
            ivFrame2.setPreserveRatio(false);

            dimBackground.widthProperty().bind(spFrame2.widthProperty());
            dimBackground.heightProperty().bind(spFrame2.heightProperty());
    }
}