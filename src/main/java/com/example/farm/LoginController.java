package com.example.farm;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.regex.Pattern;

public class LoginController {

    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorLabel;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    @FXML
    protected void onLoginClick(ActionEvent event) throws IOException {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please enter email and password.");
            return;
        }

        // Admin Check
        if ("admin".equalsIgnoreCase(email) && "12209101".equals(password)) {
            SessionManager.setUser(email, true);
            navigateToHome(event);
            return;
        }

        // Email Validation
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            errorLabel.setText("Invalid email format.");
            return;
        }

        // User Login
        if (DatabaseHandler.validateLogin(email, password)) {
            SessionManager.setUser(email, false);
            navigateToHome(event);
        } else {
            errorLabel.setText("Invalid credentials.");
        }
    }

    @FXML
    protected void onSignUpClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("register-view.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(root);
    }

    private void navigateToHome(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(root);
        // We could maximize here or set size, but keeping current stage behavior
    }
}
