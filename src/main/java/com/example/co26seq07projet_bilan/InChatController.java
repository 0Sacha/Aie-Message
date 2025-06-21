package com.example.co26seq07projet_bilan;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InChatController {

    @FXML
    private Button ExitButton;

    @FXML
    private Button accountButton;

    @FXML
    public TextField inputInChatMessageTextField;

    @FXML
    private HBox menuNavBarHbox;

    @FXML
    private Button sendButton;

    @FXML
    private Label theNameOfConvLabel;

    @FXML
    private VBox messagesVBox;

    private int currentConversationId;

    public void setConversationData(int conversationId, String conversationTitle) {
        this.currentConversationId = conversationId;
        theNameOfConvLabel.setText(conversationTitle);
        loadMessages();
    }

    private void loadMessages() {
        new Thread(() -> {
            List<String> messages = new ArrayList<>();

            try (Connection conn = DatabaseConnector.getConnection()) {
                // Sécurité : on vérifie que l'utilisateur est bien dans la conversation
                String check = "SELECT * FROM conversations_users WHERE conversation_id = ? AND user_id = ?";
                PreparedStatement checkStmt = conn.prepareStatement(check);
                checkStmt.setInt(1, currentConversationId);
                checkStmt.setInt(2, CurrentUser.getId());
                ResultSet checkResult = checkStmt.executeQuery();
                if (!checkResult.next()) return;

                String sql = """
                    SELECT u.pseudo, m.content 
                    FROM messages m 
                    JOIN users u ON m.sender_id = u.id 
                    WHERE m.conversation_id = ? 
                    ORDER BY m.timestamp ASC
                """;

                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, currentConversationId);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    String pseudo = rs.getString("pseudo");
                    String content = rs.getString("content");
                    messages.add(pseudo + " : " + content);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            Platform.runLater(() -> {
                messagesVBox.getChildren().clear();
                for (String msg : messages) {
                    Label msgLabel = new Label(msg);
                    msgLabel.setStyle("-fx-background-color: #E0E0E0; -fx-padding: 8px; -fx-background-radius: 8px;");
                    messagesVBox.getChildren().add(msgLabel);
                }
            });
        }).start();
    }

    @FXML
    void onAccountClickButton(ActionEvent event) {
        // à implémenter
    }

    @FXML
    void onExitClickButton(ActionEvent event) throws IOException {
        Scene dataScene = HelloApplication.loadChatMenuScene();
        HelloApplication.getInstance().getChangeStage().setScene(dataScene);
    }

    @FXML
    void onSendClickButton(ActionEvent event) {
        String message = inputInChatMessageTextField.getText().trim();
        if (message.isEmpty()) return;
        inputInChatMessageTextField.clear();

        new Thread(() -> {
            try (Connection conn = DatabaseConnector.getConnection()) {
                String sql = "INSERT INTO messages (conversation_id, content, sender_id) VALUES (?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, currentConversationId);
                ps.setString(2, message);
                ps.setInt(3, CurrentUser.getId());
                ps.executeUpdate();
                loadMessages();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
