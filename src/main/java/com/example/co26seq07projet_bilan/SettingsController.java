package com.example.co26seq07projet_bilan;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class SettingsController {

    @FXML
    private Button ExitFromSettingsButton;

    @FXML
    private ImageView avatarImageView;

    @FXML
    private Label changePseudoLabel;

    @FXML
    private HBox menuNavBarHbox;

    @FXML
    private Button modifyAvatarButton;

    @FXML
    private Button modifyPseudoButton;

    @FXML
    private Label modifyPseudoLabel;

    @FXML
    private Button toggleDarkLightModeButton;

    @FXML
    void onExitSettingsClickButton(ActionEvent event) throws IOException {
        Scene dataScene = HelloApplication.loadChatMenuScene();
        HelloApplication.getInstance().getChangeStage().setScene(dataScene);
    }

    @FXML
    void onModifyAvatarClickButton(ActionEvent event) {

    }

    @FXML
    void onModifyPseudoClickButton(ActionEvent event) {

    }

    @FXML
    void onToggleDarkLightModeClickButton(ActionEvent event) {

    }

}
