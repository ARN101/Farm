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

public class RegisterController {

    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField farmNameField;
    @FXML
    private TextField cityField;
    @FXML
    private TextField countryField;
    @FXML
    private Label errorLabel;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    @FXML
    protected void onRegisterClick(ActionEvent event) throws IOException {
        String email = emailField.getText();
        String password = passwordField.getText();
        String farmName = farmNameField.getText();
        String city = cityField.getText();
        String country = countryField.getText();

        if (email.isEmpty() || password.isEmpty() || farmName.isEmpty()) {
            errorLabel.setText("All fields (Email, Pass, Farm Name) are required.");
            return;
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            errorLabel.setText("Invalid email format.");
            return;
        }

        if (DatabaseHandler.isEmailTaken(email)) {
            errorLabel.setText("Email already exists. Please use another.");
            return;
        }

        if (DatabaseHandler.registerUser(email, password, farmName, city, country)) {
            // Registration successful, auto-login
            SessionManager.setUser(email, false);

            // Navigate to Home
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
        } else {
            errorLabel.setText("Registration failed. Please try again.");
        }
    }

    @FXML
    protected void onBackClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login-view.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(root);
    }
}
