package com.example.farm;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.application.Platform;

public class HelloController {

    @FXML
    private Label tempLabel;
    @FXML
    private Label descLabel;
    @FXML
    private Label humidityLabel;
    @FXML
    private Label rainLabel;

    public void initialize() {
        // Run in background thread to avoid freezing UI during API call
        new Thread(() -> {
            String email = SessionManager.getUserEmail();
            if (email != null) {
                String[] profile = DatabaseHandler.getProfile(email);
                String city = profile[1];
                String country = profile[2];

                if (city != null && !city.isEmpty() && country != null && !country.isEmpty()) {
                    WeatherService service = new WeatherService();
                    WeatherData data = service.getWeather(city, country);

                    if (data != null) {
                        Platform.runLater(() -> {
                            tempLabel.setText("Temperature: " + data.getTemperature() + "°C");
                            descLabel.setText("Condition: " + data.getDescription());
                            humidityLabel.setText("Humidity: " + data.getHumidity() + "%");
                            rainLabel.setText(
                                    "Forecast: " + (data.isRainExpected() ? "Rain Expected" : "No Rain Expected"));
                        });
                    } else {
                        Platform.runLater(() -> {
                            descLabel.setText("Condition: Unavailable");
                        });
                    }
                } else {
                    Platform.runLater(() -> {
                        descLabel.setText("Please update profile for weather");
                    });
                }
            }
        }).start();
    }

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
