package com.example.co26seq07projet_bilan;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public void initialize() {
        loadConversations();
    }

    private void loadConversations() {
        new Thread(() -> {
            List<Conversation> conversations = new ArrayList<>();
            try (Connection conn = DatabaseConnector.getConnection()) {
                String sql = """
                    SELECT c.id, c.title, (
                        SELECT content FROM messages
                        WHERE conversation_id = c.id
                        ORDER BY timestamp DESC LIMIT 1
                    ) AS last_message
                    FROM conversations c
                    JOIN conversations_users cu ON cu.conversation_id = c.id
                    WHERE cu.user_id = ?
                """;

                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, CurrentUser.getId());
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    conversations.add(new Conversation(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("last_message") != null ? rs.getString("last_message") : ""
                    ));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            Platform.runLater(() -> displayConversations(conversations));
        }).start();
    }

    private void displayConversations(List<Conversation> conversations) {
        messageMenuVbox.getChildren().clear();

        for (Conversation conv : conversations) {
            VBox convBox = new VBox();
            Label titleLabel = new Label(conv.title);
            titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
            Label lastMessageLabel = new Label(conv.lastMessage);
            lastMessageLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: gray;");

            convBox.getChildren().addAll(titleLabel, lastMessageLabel);
            convBox.setStyle("-fx-padding: 10px; -fx-border-color: #ddd; -fx-border-width: 0 0 1 0;");
            convBox.setOnMouseClicked((MouseEvent e) -> openConversation(conv));

            messageMenuVbox.getChildren().add(convBox);
        }
    }

    private void openConversation(Conversation conv) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("in-chat.fxml"));
            Scene scene = new Scene(loader.load());

            InChatController controller = loader.getController();
            controller.setConversationData(conv.id, conv.title);

            HelloApplication.getInstance().getChangeStage().setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onAccountClickButton(javafx.event.ActionEvent event) throws IOException {
        Scene dataScene = HelloApplication.loadSettingsScene();
        HelloApplication.getInstance().getChangeStage().setScene(dataScene);
    }

    @FXML
    void onCreateChatClickButton(javafx.event.ActionEvent event) {
        // Nouvelle méthode pour gérer la création de conversation
        showUserChoiceDialog();
    }

    @FXML
    void onModifyClickButton(javafx.event.ActionEvent event) {
        // à implémenter
    }

    private void showUserChoiceDialog() {
        new Thread(() -> {
            List<String> users = new ArrayList<>();

            // Récupérer la liste des utilisateurs (autres que l'utilisateur courant)
            try (Connection conn = DatabaseConnector.getConnection()) {
                String sql = "SELECT pseudo FROM users WHERE id != ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, CurrentUser.getId());
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    users.add(rs.getString("pseudo"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            Platform.runLater(() -> {
                if (users.isEmpty()) {
                    // Pas d'autres utilisateurs disponibles
                    System.out.println("Aucun autre utilisateur disponible.");
                    return;
                }

                ChoiceDialog<String> dialog = new ChoiceDialog<>(users.get(0), users);
                dialog.setTitle("Nouvelle conversation");
                dialog.setHeaderText("Sélectionnez un utilisateur");
                dialog.setContentText("Avec qui voulez-vous discuter ?");

                Optional<String> result = dialog.showAndWait();
                result.ifPresent(selectedUser -> {
                    creerConversationAvec(selectedUser);
                });
            });
        }).start();
    }

    private void creerConversationAvec(String username) {
        new Thread(() -> {
            try (Connection conn = DatabaseConnector.getConnection()) {
                // 1. Trouver l'ID utilisateur sélectionné
                String getUserIdSql = "SELECT id FROM users WHERE pseudo = ?";
                PreparedStatement psUser = conn.prepareStatement(getUserIdSql);
                psUser.setString(1, username);
                ResultSet rsUser = psUser.executeQuery();

                if (!rsUser.next()) {
                    System.err.println("Utilisateur introuvable : " + username);
                    return;
                }
                int userId = rsUser.getInt("id");

                // 2. Vérifier si une conversation entre ces 2 utilisateurs existe déjà
                String checkConvSql = """
                    SELECT c.id FROM conversations c
                    JOIN conversations_users cu1 ON cu1.conversation_id = c.id AND cu1.user_id = ?
                    JOIN conversations_users cu2 ON cu2.conversation_id = c.id AND cu2.user_id = ?
                    GROUP BY c.id
                    HAVING COUNT(DISTINCT cu1.user_id, cu2.user_id) = 2
                """;

                PreparedStatement psCheck = conn.prepareStatement(checkConvSql);
                psCheck.setInt(1, CurrentUser.getId());
                psCheck.setInt(2, userId);
                ResultSet rsCheck = psCheck.executeQuery();

                if (rsCheck.next()) {
                    int existingConvId = rsCheck.getInt("id");
                    System.out.println("Conversation existante trouvée avec id : " + existingConvId);
                    // Ouvrir la conversation existante
                    Platform.runLater(() -> openConversationById(existingConvId));
                    return;
                }

                // 3. Créer une nouvelle conversation
                String insertConvSql = "INSERT INTO conversations (title) VALUES (?)";
                PreparedStatement psInsertConv = conn.prepareStatement(insertConvSql, Statement.RETURN_GENERATED_KEYS);
                String title = "Conversation avec " + username;
                psInsertConv.setString(1, title);
                psInsertConv.executeUpdate();

                ResultSet generatedKeys = psInsertConv.getGeneratedKeys();
                if (!generatedKeys.next()) {
                    System.err.println("Erreur création conversation");
                    return;
                }
                int newConvId = generatedKeys.getInt(1);

                // 4. Ajouter les utilisateurs à la conversation
                String insertUserConvSql = "INSERT INTO conversations_users (conversation_id, user_id) VALUES (?, ?)";
                PreparedStatement psInsertUserConv = conn.prepareStatement(insertUserConvSql);
                psInsertUserConv.setInt(1, newConvId);
                psInsertUserConv.setInt(2, CurrentUser.getId());
                psInsertUserConv.executeUpdate();

                psInsertUserConv.setInt(2, userId);
                psInsertUserConv.executeUpdate();

                System.out.println("Nouvelle conversation créée avec id : " + newConvId);

                // 5. Ouvrir la nouvelle conversation
                Platform.runLater(() -> openConversationById(newConvId));

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void openConversationById(int conversationId) {
        new Thread(() -> {
            try (Connection conn = DatabaseConnector.getConnection()) {
                String sql = "SELECT title FROM conversations WHERE id = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, conversationId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    String title = rs.getString("title");
                    Conversation conv = new Conversation(conversationId, title, "");
                    Platform.runLater(() -> openConversation(conv));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private static class Conversation {
        int id;
        String title;
        String lastMessage;

        public Conversation(int id, String title, String lastMessage) {
            this.id = id;
            this.title = title;
            this.lastMessage = lastMessage;
        }
    }
}
