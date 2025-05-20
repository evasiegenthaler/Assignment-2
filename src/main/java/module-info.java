module a1.example.a1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    // Apache POI Module für Excel
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;

    opens a1.example.a2 to javafx.fxml;
    exports a1.example.a2;
}