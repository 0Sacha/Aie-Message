package com.example.co26seq07projet_bilan;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LogInController {

    @FXML
    public TextField inputPasswordTextField;

    @FXML
    public TextField inputPseudoTextField;

    @FXML
    private Button submitLogInButton;

    @FXML
    void onSubmitLogInClickButton(ActionEvent event) throws IOException {
        String pseudo = inputPseudoTextField.getText().trim();
        String password = inputPasswordTextField.getText().trim();

        if (pseudo.isEmpty() || password.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Pseudo et mot de passe requis.");
            alert.show();
            return;
        }

        try (Connection conn = DatabaseConnector.getConnection()) {
            String sql = "SELECT id, password FROM users WHERE pseudo = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, pseudo);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String dbPassword = rs.getString("password");
                int userId = rs.getInt("id");

                if (dbPassword.equals(password)) {
                    CurrentUser.setUser(userId, pseudo);
                    Scene dataScene = HelloApplication.loadChatMenuScene();
                    HelloApplication.getInstance().getChangeStage().setScene(dataScene);
                } else {
                    showError("Mot de passe incorrect.");
                }
            } else {
                showError("Utilisateur non trouvé.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Erreur de connexion.");
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.show();
    }
}
