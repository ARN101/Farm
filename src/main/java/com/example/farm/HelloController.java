package com.example.farm;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HelloController {

    @FXML
    protected void onVaccinationTrackerClick(javafx.event.ActionEvent event) throws java.io.IOException {

        javafx.fxml.FXMLLoader fxmlLoader = new javafx.fxml.FXMLLoader(
                HelloApplication.class.getResource("vaccination-tracker-view.fxml"));

        javafx.scene.Parent root = fxmlLoader.load();

        javafx.stage.Stage stage = (javafx.stage.Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(root);
    }

    @FXML
    protected void onFarmFinanceClick(javafx.event.ActionEvent event) throws java.io.IOException {

        javafx.fxml.FXMLLoader fxmlLoader = new javafx.fxml.FXMLLoader(
                HelloApplication.class.getResource("farm-finance-view.fxml"));

        javafx.scene.Parent root = fxmlLoader.load();

        javafx.stage.Stage stage = (javafx.stage.Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(root);
    }

    @FXML
    protected void onLivestockClick(javafx.event.ActionEvent event) throws java.io.IOException {

        javafx.fxml.FXMLLoader fxmlLoader = new javafx.fxml.FXMLLoader(
                HelloApplication.class.getResource("livestock-view.fxml"));

        javafx.scene.Parent root = fxmlLoader.load();

        javafx.stage.Stage stage = (javafx.stage.Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(root);
    }

    @FXML
    protected void onFarmProfileClick(javafx.event.ActionEvent event) throws java.io.IOException {

        javafx.fxml.FXMLLoader fxmlLoader = new javafx.fxml.FXMLLoader(
                HelloApplication.class.getResource("farm-profile-view.fxml"));

        javafx.scene.Parent root = fxmlLoader.load();

        javafx.stage.Stage stage = (javafx.stage.Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(root);
    }
}
