package a1.example.a2;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import model.NormalPet;
import model.Pet;


public class Frame3Controller {

    private Stage popupStage;
    private Pet currentPet; // ← das fehlt noch ganz oben

    @FXML private Rectangle dimBackground;
    @FXML private TextField tfName3;
    @FXML private Label lblHappinessValue3;
    @FXML private Label lblHungerValue3;
    @FXML private Label lblEnergyValue3;
    @FXML private Slider slHappiness3;
    @FXML private Slider slHunger3;
    @FXML private Slider slEnergy3;
    @FXML private ImageView ivFrame1;
    @FXML private StackPane spFrame1;

    public Pet getCurrentPet() {
        return currentPet;
    }

    public void setPopupStage(Stage popupStage) {
        this.popupStage = popupStage;
    }

    public void disableAutoFocus() {
        tfName3.setFocusTraversable(false);
    }

    @FXML
    private void savePet() {
        String name = tfName3.getText().trim(); // trim()=entfernt alle Leerzeichen am Anfang und Ende des Texts

        if (name.isEmpty() || !name.matches("[a-zA-ZäöüÄÖÜß ]+")) {
            showAlert("Invalid Name", "Only letters allowed", "Please enter a valid name (no numbers or special characters).", tfName3);
            return;
        }
        //Werte von Slidern weil getValue() ein double zurückgibt cast zu (int)
        int happiness = (int) slHappiness3.getValue();
        int hunger = (int) slHunger3.getValue();
        int energy = (int) slEnergy3.getValue();

        if (PetManager.isDuplicate(currentPet, name, happiness, hunger, energy)) {
            Frame3Controller.showAlert("Duplicate Pet", "Already Exists",
                    "Another pet with the same name, happiness, hunger and energy already exists.", tfName3);
            return ;
        }
        // 1. Neues Pet erstellen
        Pet newPet = new NormalPet(name, happiness, hunger, energy);
        PetManager.addPet(newPet); //added Pet zu LinkedMap

        popupStage.close();
    }

    @FXML
    private void cancelPet(ActionEvent event) {
        tfName3.clear();
        slHappiness3.setValue(50);
        slHunger3.setValue(50);
        slEnergy3.setValue(50);

        lblHappinessValue3.setText("50");
        lblHungerValue3.setText("50");
        lblEnergyValue3.setText("50");
    }

    @FXML
    private void initialize() {
        if (slHappiness3 != null && slHunger3 != null && slEnergy3 != null) {
            slHappiness3.setValue(50);
            slHunger3.setValue(50);
            slEnergy3.setValue(50);
            lblHappinessValue3.setText("50");
            lblHungerValue3.setText("50");
            lblEnergyValue3.setText("50");

            SliderStyler.updateSliderBindings(slHappiness3, lblHappinessValue3);
            SliderStyler.updateSliderBindings(slHunger3, lblHungerValue3);
            SliderStyler.updateSliderBindings(slEnergy3, lblEnergyValue3);
        }

        if (ivFrame1 != null && spFrame1 != null) {
            ivFrame1.fitWidthProperty().bind(spFrame1.widthProperty());
            ivFrame1.fitHeightProperty().bind(spFrame1.heightProperty());
            ivFrame1.setPreserveRatio(false);
        }
    }


    public static void showAlert(String title, String header, String content, Node anyNodeInScene) { //Brauche die Node damit ich weiss in welchem Fenster der Alert sein muss
        Alert alert = new Alert(Alert.AlertType.ERROR); //Alert Klasse hat ein eingebettetes Enum names AlertType...
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        Stage stage = (Stage) anyNodeInScene.getScene().getWindow(); //getWindow kann nur ein Window zurückgeben darum Ergebnis als (Stage)=casten weil Stage eine Unterklasse von Window ist
        alert.initOwner(stage); //Das zurückgegebene (Window) Stage wird als Owner gesetzt. Sodass alert jetzt weiss über welchem fenster es aufploppen muss

        alert.showAndWait(); // Zeigt das alert und wartet bis der User eine Aktion macht z.b Ok klickt
    }
}