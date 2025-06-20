package com.example.co26seq07projet_bilan;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class ChatMenuController {

    @FXML
    private Button accountButton1;

    @FXML
    private HBox menuNavBarHbox;

    @FXML
    private VBox messageMenuVbox;

    @FXML
    private Button modifyButton;

    @FXML
    private Button newChatButton;

    @FXML
    void onAccountClickButton(ActionEvent event) throws IOException {
        Scene dataScene = HelloApplication.loadSettingsScene();
        HelloApplication.getInstance().getChangeStage().setScene(dataScene);
    }

    @FXML
    void onCreateChatClickButton(ActionEvent event) throws IOException {
        Scene dataScene = HelloApplication.loadInChatScene();
        HelloApplication.getInstance().getChangeStage().setScene(dataScene);
    }

    @FXML
    void onModifyClickButton(ActionEvent event) {

    }

}
