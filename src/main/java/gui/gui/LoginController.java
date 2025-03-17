package gui.gui;

import model.UserService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;

import java.io.IOException;

public class LoginController {
    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField usernameField;

    private final UserService userService = new UserService();

    @FXML
    protected void onLoginButtonClick(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        String status = userService.authenticateUser(username, password);

        if (status != null) {
            loadAppropriatePage(event, username, status);
        } else {
            showLoginError();
        }
    }

    private void loadAppropriatePage(ActionEvent event, String username, String status) {
        String fxmlFile = status.equals("user") ? "userPage.fxml" : "adminPage.fxml";

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
            Scene newScene = new Scene(fxmlLoader.load());

            if (status.equals("user")) {
                UserPageController userPageController = fxmlLoader.getController();
                userPageController.setUser(username);
            } else {
                AdminPageController adminPageController = fxmlLoader.getController();
                adminPageController.setUser(username);
            }

            Stage newStage = new Stage();
            newStage.setTitle("Electronic Shelter");
            newStage.setScene(newScene);
            newStage.show();

            closeCurrentStage(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeCurrentStage(ActionEvent event) {
        Stage loginStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        loginStage.close();
    }

    private void showLoginError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Login error");
        alert.setHeaderText(null);
        alert.setContentText("Incorrect username or password. Please try again.");
        alert.showAndWait();
    }
}
