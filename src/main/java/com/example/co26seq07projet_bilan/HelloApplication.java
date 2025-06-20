package com.example.co26seq07projet_bilan;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    private static HelloApplication application;
    private Stage change = new Stage();

    @Override
    public void start(Stage stage) throws IOException {
        // Initialisation de la référence vers l'application
        application = this;
        // Chargement de la Scene du premier écran
        Scene loginScene = loadLoginScene();
        // Mise en place et affichage de la Scene du premier écran dans la Stage de notre application
        this.change.setTitle("LiveChatApp");
        this.change.setScene(loginScene);
        this.change.show();
        this.change.setResizable(false);
    }

    // Méthode de chargement de la Scene du premier écran
    public static Scene loadLoginScene() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("log-in.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        return scene;
    }

    // Méthode de chargement de la Scene du deuxième écran
    public static Scene loadChatMenuScene() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("chat-menu.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        return scene;
    }

    // Méthode de chargement de la Scene du troisième écran
    public static Scene loadInChatScene() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("in-chat.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        return scene;
    }

    // Méthode de chargement de la Scene du deuxième écran
    public static Scene loadSettingsScene() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("settings.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        return scene;
    }

    public static void main(String[] args) {
        launch();
    }

    // Getter permettant de récupérer la référence vers l'application
    public static HelloApplication getInstance() {
        return application;
    }

    // Getter permettant de récupérer la Stage de l'application
    public Stage getChangeStage() {
        return change;
    }


}
