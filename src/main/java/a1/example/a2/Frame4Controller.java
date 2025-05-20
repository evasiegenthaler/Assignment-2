package a1.example.a2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Pet;

import java.io.File;
import java.io.IOException;

public class Frame4Controller {
    @FXML
    private TextField tfSearch4;

    @FXML
    private HBox hboxCardContainer;

    @FXML
    private Label lblCharacter;

    @FXML
    public void initialize() {
        // Beim Start von Frame4: Alle Pets durchgehen und Karten erstellen
        for (Pet pet : PetManager.getPetList()) { //Durchläuft jedes Pet auf der Liste
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("PetCard.fxml"));
                Parent card = loader.load(); //Lädt das fxml kann IOException auslösen!

                PetCardController petCardController = loader.getController(); //Holt den PetCardController
                petCardController.setPet(pet); //übergibt das pet was sich aktuell in der schleife befindet and die setPet () diese Methode füllt den Inhalt für die Karte

                // HIER Frame4Controller an die PetCard übergeben 👇
                petCardController.setFrame4Controller(this);

                // Fokus weg Delete Button--> von PetCard
                petCardController.getBtnDelete().setFocusTraversable(false); // Gibt mer den btnDelete der in der Pet Karte steckt und setzt dessen fokus auf false= kein fokus

                hboxCardContainer.getChildren().add(card); //getChildren= gibt mir eine Sammlung(Button,Slider) mit allem was in der hBox liegt und added dann den content von card=fxml jetzt wird es sichtbar

            } catch (IOException e) { //Fängt falls fxml nicht geladen werden kann
                e.printStackTrace(); //Falls der Fehler passiert wird er in die Konsole geschrieben
            }

        }
    }
    @FXML
    private void backToFrame2(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Frame2.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setFullScreen(true); // oder false, wenn du kein Fullscreen willst
        stage.show();
    }
    public void disableAutoFocus() {
        tfSearch4.setFocusTraversable(false);
    }

    @FXML
    private void exportPetsToExcel(ActionEvent event) { //Methode wird aufgerufen wenn ich auf den download button drücke in meinem UI
        FileChooser fileChooser = new FileChooser(); //Java Klasse die Dialogfenster erstellt. Das Design wird bestimmt durch welches Betriebssystem ich habe Linux, mac

        // Vorschlagen, dass nur Excel-Dateien gespeichert werden
        fileChooser.getExtensionFilters().add( //Methode get.ExtensionFilters() gibt mir eine Liste von Filterregeln zurück zu der ich dann meine dokumenttypen hinzufügen kann als hier excel
                new FileChooser.ExtensionFilter("Excel-Datei (*.xlsx)", "*.xlsx") //Stern=Platzhalter für beliebieger Dateiname solange er auf xlsx=excel endet
        );

        // Vorschlag für den Dateinamen
        fileChooser.setInitialFileName("PetsExport.xlsx");

        // Fenster öffnen
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow(); //Speicher die aktuelle Fensterinstanz (Stage) in stage, damit ich den file choser über meinem aktuellen fenster anzeigen kann
        java.io.File file = fileChooser.showSaveDialog(stage); //Öffnet das Speichern-Dialog Fenster und gibt den Pfad den der User auswählt als Objekt zurück dass in file gespeichert wird

        // Wenn der User ein File ausgewählt hat:
        if (file != null) { //file= null wenn user Abbrechen drückt. Ich kann gar keine Leere Liste exportieren da ich gar nicht zu diesem Frame komme wenn ich kein characters habe
            PetManager.exportPets(file.getAbsolutePath()); //ruft Methode aus Pet Manager auf und gibt den Speicherpfad mit den der User vorher ausgewählt hat
            // Datei wurde ausgewählt → speichern
        } else {
            // Benutzer hat abgebrochen → nichts tun
        }
    }

    @FXML
    private void importPetsFromExcel(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Excel-Datei (*.xlsx)", "*.xlsx")
        );

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            PetManager.importPets(file.getAbsolutePath());

            // Nach Import: Frame 4 neu laden
            reloadFrame4(event);
        } else {
            System.out.println("Import abgebrochen.");
        }
    }
    //Helper Method to reload Frame 4
    public void reloadFrame4(ActionEvent event) { // Der übergebene 'event' ist derselbe wie in deletePet(), somit enthält er weiterhin alle Infos zur ursprünglichen Stage
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Frame4.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setFullScreen(true); // wenn du Fullscreen möchtest
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void searchPet() {
        String searchTerm = tfSearch4.getText().trim();

        if (searchTerm.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Missing Input");
            alert.setHeaderText("No name entered");
            alert.setContentText("Please enter a name to search for a pet.");
            alert.initOwner(tfSearch4.getScene().getWindow());  // Das Alert über jetzigem Fenster ist
            alert.show();
            return;
        }

        // Suche durch alle Pets
        for (Pet pet : PetManager.getPetList()) {
            if (pet.getPetName().equalsIgnoreCase(searchTerm)) {
                // Pet gefunden
                Alert found = new Alert(Alert.AlertType.INFORMATION);
                found.setTitle("Pet Found");
                found.setHeaderText("We found a match!");
                found.setContentText(
                        "Name: " + pet.getPetName() + "\n" +
                                "Happiness: " + pet.getHappiness() + "\n" +
                                "Hunger: " + pet.getHunger() + "\n" +
                                "Energy: " + pet.getEnergy()
                );
                found.initOwner(tfSearch4.getScene().getWindow());  // Das Alert über jetzigem Fenster ist
                found.show();
                return;
            }
        }

        // Kein Treffer gefunden
        Alert notFound = new Alert(Alert.AlertType.INFORMATION);
        notFound.setTitle("No Match");
        notFound.setHeaderText("Pet not found");
        notFound.setContentText("No pet with the name \"" + searchTerm + "\" was found.");
        notFound.initOwner(tfSearch4.getScene().getWindow());  // Das Alert über jetzigem Fenster ist
        notFound.show();
    }

}
