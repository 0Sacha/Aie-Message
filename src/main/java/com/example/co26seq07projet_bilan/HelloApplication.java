package com.example.co26seq07projet_bilan;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("chat-menu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 402, 663);
        stage.setTitle("aïe messages");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
