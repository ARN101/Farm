package com.example.farm;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import java.io.IOException;

public class LivestockController {

    @FXML
    private TextField typeField;

    @FXML
    private TextField tagField;

    @FXML
    private TextField breedField;

    @FXML
    private TextField ageField;

    @FXML
    private TextField healthStatusField;

    @FXML
    protected void onAddClick() {
        System.out.println("Adding Animal:");
        System.out.println("Type: " + typeField.getText());
        System.out.println("Tag: " + tagField.getText());
        System.out.println("Breed: " + breedField.getText());
        System.out.println("Age: " + ageField.getText());
        System.out.println("Health: " + healthStatusField.getText());
    }

    @FXML
    protected void onRemoveClick() {
        System.out.println("Remove button clicked");
        // Logic to remove animal will go here
    }

    @FXML
    protected void onBackToHomeClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        javafx.scene.Parent root = fxmlLoader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(root);
    }
}
