module com.example.co26seq07projet_bilan {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;
    requires java.desktop;


    opens com.example.co26seq07projet_bilan to javafx.fxml;
    exports com.example.co26seq07projet_bilan;
}