package com.example.farm;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;

public class FarmProfileController {

    @FXML
    private TextField farmNameField;
    @FXML
    private TextField cityField;
    @FXML
    private TextField countryField;

    public void initialize() {
        loadProfile();
    }

    private void loadProfile() {
        String email = SessionManager.getUserEmail();
        if (email == null)
            return; // Should not happen if logged in

        String[] profile = DatabaseHandler.getProfile(email);
        farmNameField.setText(profile[0]);
        cityField.setText(profile[1]);
        countryField.setText(profile[2]);
    }

    @FXML
    protected void onSaveClick() {
        String email = SessionManager.getUserEmail();
        if (email == null)
            return;

        String name = farmNameField.getText();
        String city = cityField.getText();
        String country = countryField.getText();

        DatabaseHandler.saveProfile(email, name, city, country);
        System.out.println("Profile Saved!");
    }

    @FXML
    protected void onBackToHomeClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(root);
    }
}
