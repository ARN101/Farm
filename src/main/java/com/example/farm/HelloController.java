package com.example.farm;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HelloController {

    @FXML
    protected void onDashboardClick(javafx.event.ActionEvent event) throws java.io.IOException {
        javafx.fxml.FXMLLoader fxmlLoader = new javafx.fxml.FXMLLoader(
                HelloApplication.class.getResource("dashboard-view.fxml"));
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
