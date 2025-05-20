package a1.example.a2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import model.Pet;

import java.io.IOException;
import java.util.Optional;

import static a1.example.a2.Frame3Controller.showAlert;

public class PetCardController {
    @FXML
    private Label lblHappinessText;
    @FXML
    private Label lblHappinessValue;

    @FXML
    private Label lblHungerText;
    @FXML
    private Label lblHungerValue;

    @FXML
    private Label lblEnergyText;
    @FXML
    private Label lblEnergyValue;

    @FXML
    private Slider slHappiness;
    @FXML
    private Slider slHunger;
    @FXML
    private Slider slEnergy;

    @FXML
    private TextField tfNamePetCard;

    @FXML
    private Button btnEditSave;
    private boolean isEditMode = false; // Toggle-Zustand

    //Neu
    @FXML
    private StackPane spPetCard; // fx:id im FXML setzen

    private Pet currentPet;

    @FXML
    private Label lblPosition;

    private Frame4Controller frame4Controller;

    public void setFrame4Controller(Frame4Controller frame4Controller) {
        this.frame4Controller = frame4Controller;
    }



    public void setPet(Pet pet) { //ein Pet muss an diese Methode übnergeben werden
        this.currentPet = pet; // Das übergebene pet wird in der Instanzvariable currentPet gespeichert damit ich später im controller weiss welches Pet auf der Karte angezeigt wird
        tfNamePetCard.setText(pet.getPetName());
        lblPosition.setText("#: " + PetManager.getPosition(currentPet));

        // Set sliders to pet values
        slHappiness.setValue(Math.max(0, Math.min(100, pet.getHappiness()))); //Nimmt das übergebene pet und holt dessen Happiness) und setzt den wert zwischen 0-100
        slHunger.setValue(Math.max(0, Math.min(100, pet.getHunger())));
        slEnergy.setValue(Math.max(0, Math.min(100, pet.getEnergy())));

        // Show initial values in value labels
        lblHappinessValue.setText(String.valueOf((int) slHappiness.getValue()));
        lblHungerValue.setText(String.valueOf((int) slHunger.getValue()));
        lblEnergyValue.setText(String.valueOf((int) slEnergy.getValue()));

        // WICHTIG: Farben nochmal setzen
        SliderStyler.updateSliderBindings(slHappiness, lblHappinessValue);
        SliderStyler.updateSliderBindings(slHunger, lblHungerValue);
        SliderStyler.updateSliderBindings(slEnergy, lblEnergyValue);

        setEditable(false); // Ruft methode auf um Eingabefelder zu deaktivieren
    }

    @FXML
    private void toggleEditSave() { //kein ActionEvent weil das schon im scene builder
        if (!isEditMode) { //isEditMode ist normalzustand false also in dieser line true gemacht
            // Wechsle in Edit-Modus
            isEditMode = true;
            setEditable(true);
            btnEditSave.setText("Save");
        } else {
        // Fokus vom TextField nehmen, damit der neu eingegebene Wert auch "gespeichert" wird und falls Fehlermeldung diese nicht erst kommen würde wenn man neben die Karte klickt
            tfNamePetCard.setFocusTraversable(false);

            // Save-Modus
            if (validateAndSave()) {
                isEditMode = false;
                setEditable(false);
                btnEditSave.setText("Edit");
            }
        }
    }

    private void setEditable(boolean editable) {
        tfNamePetCard.setEditable(editable);
        slHappiness.setDisable(!editable); //Slider haben keine setEditable methode. wenn ich true übergebe kann man bearbeiten weil (not true)=false so setDisable false= weil nicht deaktiviert
        slHunger.setDisable(!editable);
        slEnergy.setDisable(!editable);
    }

    private boolean validateAndSave() {
        String Petname = tfNamePetCard.getText().trim();
        int happiness = (int) slHappiness.getValue();
        int hunger = (int) slHunger.getValue();
        int energy = (int) slEnergy.getValue();

        // Namenscheck
        if (Petname.isEmpty() || !Petname.matches("[a-zA-ZäöüÄÖÜß ]+")) {
            showAlert("Invalid Name", "Only letters allowed", "No numbers or symbols.", tfNamePetCard);
            return false;
        }

        if (PetManager.isDuplicate(currentPet, Petname, happiness, hunger, energy)) {
            showAlert("Duplicate Pet", "Already Exists", "Another pet with the same name, happiness, hunger and energy already exists.", btnEditSave);
            return false;
        }

        // Save
        currentPet.setPetName(Petname);
        currentPet.setHappiness(happiness);
        currentPet.setHunger(hunger);
        currentPet.setEnergy(energy);
        return true;
    }

    @FXML
    private Button btnDelete;

    //Getter da Instance private ich muss aber auf sie zugreifen können im Frame4 Controller
    public Button getBtnDelete() {
        return btnDelete;
    }

    //Delete Button
    @FXML
    private void deletePet(ActionEvent event) {
        // Stage sichern, bevor etwas entfernt wird!
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow(); // Muss stage sein wegen SetFullScreen z.b

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Delete Character");
        confirmAlert.setHeaderText("Are you sure you want to delete this character?");
        confirmAlert.setContentText("This action cannot be undone.");

        confirmAlert.initOwner(((Node) event.getSource()).getScene().getWindow()); // Damit Alert über der richtigen stage angezeigt wird. setzt die aktuelle scene als owner
        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {

            // 1. Pet aus PetManager entfernen
            PetManager.removePetById(currentPet);

            // 2. Prüfen ob noch andere Karten da sind
            Pane parent = (Pane) spPetCard.getParent(); // Rückgabe= Stack Pane raufkasten zu Pane da ich Pane brauche um get Children aufrufen, ich brauche aber parent weil spPetCard nur eine Node ist aber ich brauche einen Container
            parent.getChildren().remove(spPetCard); //get Children()= Gibt alle Nodes zurück des Panes das methode aufruft--> remove die spPetCard aus der Children Liste

            // 3. Prüfen ob noch Karten übrig sind
            boolean noCardsLeft = parent.getChildren().isEmpty();

            // 4. Wenn leer → zurück zu Frame2
            if (noCardsLeft) {
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("Frame2.fxml"));
                    stage.setScene(new Scene(root));
                    stage.setFullScreen(true);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // 5. Falls noch Karten da sind → reload Frame4
            else {
                frame4Controller.reloadFrame4(event); // übergibt den event der die reloadFrame4 ()
            }
        }
    }

    @FXML
    private void initialize() {
        slHappiness.valueProperty().addListener((obs, oldVal, newVal) -> {
            lblHappinessValue.setText(String.valueOf(newVal.intValue()));
        });

        slHunger.valueProperty().addListener((obs, oldVal, newVal) -> {
            lblHungerValue.setText(String.valueOf(newVal.intValue()));
        });

        slEnergy.valueProperty().addListener((obs, oldVal, newVal) -> {
            lblEnergyValue.setText(String.valueOf(newVal.intValue()));
        });
    }
}
