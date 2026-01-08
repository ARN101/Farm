package com.example.farm;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import java.io.IOException;

public class FarmProfileController {

    @FXML
    private TextField farmNameField;

    @FXML
    private TextField cityField;

    @FXML
    private TextField countryField;

    @FXML
    protected void onSaveProfileClick() {
        System.out.println("Saving Profile:");

        System.out.println("Farm Name: " + farmNameField.getText());
        System.out.println("City: " + cityField.getText());

        System.out.println("Country: " + countryField.getText());
    }

    @FXML
    protected void onBackToHomeClick(ActionEvent event) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));

        javafx.scene.Parent root = fxmlLoader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(root);
    }
}
