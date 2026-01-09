package com.example.farm;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class VaccinationTrackerController {

    @FXML
    private ComboBox<String> animalIdComboBox;

    @FXML
    private DatePicker dateGivenPicker;

    @FXML
    private TextField vaccineNameField;

    @FXML
    private DatePicker nextDueDatePicker;

    @FXML
    private TextArea notesArea;

    @FXML
    private TableView<VaccinationRecord> vaccinationTable;

    @FXML
    private TableColumn<VaccinationRecord, Integer> idColumn;

    @FXML
    private TableColumn<VaccinationRecord, String> animalIdColumn;

    @FXML
    private TableColumn<VaccinationRecord, String> vaccineColumn;

    @FXML
    private TableColumn<VaccinationRecord, LocalDate> dateGivenColumn;

    @FXML
    private TableColumn<VaccinationRecord, LocalDate> nextDueColumn;

    @FXML
    private TableColumn<VaccinationRecord, String> statusColumn;

    private final ObservableList<VaccinationRecord> recordList = FXCollections.observableArrayList();

    public void initialize() {
        loadAnimalIds();

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        animalIdColumn.setCellValueFactory(new PropertyValueFactory<>("animalId"));
        vaccineColumn.setCellValueFactory(new PropertyValueFactory<>("vaccine"));
        dateGivenColumn.setCellValueFactory(new PropertyValueFactory<>("dateGiven"));
        nextDueColumn.setCellValueFactory(new PropertyValueFactory<>("nextDueDate"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        loadVaccinations();
    }

    private void loadAnimalIds() {
        animalIdComboBox.setItems(FXCollections.observableArrayList(DatabaseHandler.getAllAnimalIds()));
    }

    private void loadVaccinations() {
        recordList.setAll(DatabaseHandler.getVaccinations());
        vaccinationTable.setItems(recordList);
    }

    @FXML
    protected void onAddRecordClick() {
        String animalId = animalIdComboBox.getValue();
        LocalDate dateGiven = dateGivenPicker.getValue();
        String vaccine = vaccineNameField.getText();
        LocalDate nextDueDate = nextDueDatePicker.getValue();

        if (animalId == null || dateGiven == null || vaccine.isEmpty() || nextDueDate == null) {
            System.out.println("Please fill required fields");
            return;
        }

        String status = calculateStatus(nextDueDate);

        // ID passed as 0, handled by DB auto-increment
        VaccinationRecord record = new VaccinationRecord(0, animalId, vaccine, dateGiven, nextDueDate, status);
        DatabaseHandler.addVaccination(record);
        loadVaccinations();

        // Clear fields
        animalIdComboBox.setValue(null);
        dateGivenPicker.setValue(null);
        vaccineNameField.clear();
        nextDueDatePicker.setValue(null);
        notesArea.clear();
    }

    @FXML
    protected void onDeleteRecordClick() {
        VaccinationRecord selected = vaccinationTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            System.out.println("No record selected");
            return;
        }

        DatabaseHandler.deleteVaccination(selected.getId());
        loadVaccinations();
    }

    private String calculateStatus(LocalDate nextDueDate) {
        LocalDate today = LocalDate.now();
        if (nextDueDate.isBefore(today)) {
            return "Overdue";
        } else if (ChronoUnit.DAYS.between(today, nextDueDate) <= 7) {
            return "Due Soon";
        } else {
            return "Scheduled";
        }
    }

    @FXML
    protected void onBackToHomeClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(root);
    }
}
