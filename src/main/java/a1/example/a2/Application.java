package a1.example.a2;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException { //throws= Fehler kann hier auftreten aber ich kümmere mich hier nicht darum weitergegebn an JavaFX framework=Dieses behandelt die exception--> propagated exception
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("Frame1.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1920, 1080);

        // CSS einbinden
        scene.getStylesheets().add(Application.class.getResource("style.css").toExternalForm()); //toExternalForm() wandelt das css in ein String um weil add kann keine URL benutzen nur String
        stage.setScene(scene); //Die scene 1 wird in das stage eingesetzt. Ohne scene bleibt das Fenster leer
        stage.setFullScreen(true);
        stage.show(); //öffnet und zeigt das Fenster an

        // ESC = Fullscreen toggeln
        scene.setOnKeyPressed(e -> { //e=event (handling), anynumous method=lambda expression. In front of arrow is parameter then we make arrow and say what we want to do
            //says this method is called when pressed ESC. Der Ausdruck e -> { ... } ist ein Lambda-Ausdruck. Methode erwartet Objekt vom Typ EventHandler<KeyEvent> = e das Ding das übergeben wird wenn ich ESC drücke
            if (e.getCode() == KeyCode.ESCAPE) { //.getCode() java methode == possible because KeyCode is an enum and an enum can be compared with ==
                stage.setFullScreen(false); //setFullScreen() Java Klass
            }
        });
    }

    public static void main(String[] args) {
        launch(); // launch()= methode von der Application Class, sie startet die gesamte JavaFX runtime
        // Ablauf: main() → launch() → start()
    }
}