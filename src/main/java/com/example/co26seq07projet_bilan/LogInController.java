package com.example.co26seq07projet_bilan;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.InputMethodEvent;

import java.io.IOException;

public class LogInController {

    @FXML
    public TextField inputPasswordTextField;

    @FXML
    public TextField inputPseudoTextField;

    @FXML
    private Button submitLogInButton;

    @FXML
    void onInputPasswordTextField(InputMethodEvent event) {

    }

    @FXML
    void onInputPseudoTextField(InputMethodEvent event) {

    }

    @FXML
    void onSubmitLogInClickButton(ActionEvent event) throws IOException {
        inputPasswordTextField.getText();
        inputPseudoTextField.getText();

        if (inputPasswordTextField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
        } else {
            Scene dataScene = HelloApplication.loadChatMenuScene();
            HelloApplication.getInstance().getChangeStage().setScene(dataScene);
        }
    }
}
