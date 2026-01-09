package com.example.farm;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.time.LocalDate;

public class FarmFinanceController {

    @FXML
    private RadioButton expenseRadio;
    @FXML
    private RadioButton incomeRadio;
    @FXML
    private TextField categoryField;
    @FXML
    private TextField amountField;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ComboBox<String> animalIdComboBox;
    @FXML
    private TextArea notesArea;

    @FXML
    private Label totalIncomeLabel;
    @FXML
    private Label totalExpenseLabel;
    @FXML
    private Label netProfitLabel;

    @FXML
    private TableView<FinanceRecord> financeTable;
    @FXML
    private TableColumn<FinanceRecord, String> typeColumn;
    @FXML
    private TableColumn<FinanceRecord, String> categoryColumn;
    @FXML
    private TableColumn<FinanceRecord, Double> amountColumn;
    @FXML
    private TableColumn<FinanceRecord, String> dateColumn;
    @FXML
    private TableColumn<FinanceRecord, String> animalIdColumn;
    @FXML
    private TableColumn<FinanceRecord, String> notesColumn;

    private final ObservableList<FinanceRecord> financeList = FXCollections.observableArrayList();

    public void initialize() {
        // Setup columns
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        animalIdColumn.setCellValueFactory(new PropertyValueFactory<>("animalId"));
        notesColumn.setCellValueFactory(new PropertyValueFactory<>("notes"));

        // Load data
        loadAnimalIds();
        loadFinanceRecords();
    }

    private void loadAnimalIds() {
        animalIdComboBox.setItems(FXCollections.observableArrayList(DatabaseHandler.getAllAnimalIds()));
    }

    private void loadFinanceRecords() {
        financeList.setAll(DatabaseHandler.getFinanceRecords());
        financeTable.setItems(financeList);
        calculateTotals();
    }

    private void calculateTotals() {
        double income = 0;
        double expense = 0;

        for (FinanceRecord record : financeList) {
            if ("Income".equalsIgnoreCase(record.getType())) {
                income += record.getAmount();
            } else {
                expense += record.getAmount();
            }
        }

        double netProfit = income - expense;

        totalIncomeLabel.setText(String.format("Total Income: %.2f", income));
        totalExpenseLabel.setText(String.format("Total Expense: %.2f", expense));
        netProfitLabel.setText(String.format("Net Profit: %.2f", netProfit));

        // Color coding for net profit
        if (netProfit >= 0) {
            netProfitLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold; -fx-font-size: 14px;");
        } else {
            netProfitLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold; -fx-font-size: 14px;");
        }
    }

    @FXML
    protected void onAddClick() {
        String type = incomeRadio.isSelected() ? "Income" : "Expense";
        String category = categoryField.getText();
        String amountText = amountField.getText();
        LocalDate date = datePicker.getValue();
        String animalId = animalIdComboBox.getValue();
        String notes = notesArea.getText();

        if (category.isEmpty() || amountText.isEmpty() || date == null) {
            // Basic validation
            System.out.println("Please fill required fields");
            return;
        }

        try {
            double amount = Double.parseDouble(amountText);

            // ID handled by DB (0)
            FinanceRecord record = new FinanceRecord(0, type, category, amount, date, animalId, notes);
            DatabaseHandler.addFinanceRecord(record);
            loadFinanceRecords(); // Reload to get ID and update table

            // Clear fields
            categoryField.clear();
            amountField.clear();
            datePicker.setValue(null);
            animalIdComboBox.setValue(null);
            notesArea.clear();

        } catch (NumberFormatException e) {
            System.out.println("Invalid amount format");
        }
    }

    @FXML
    protected void onDeleteClick() {
        FinanceRecord selected = financeTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            DatabaseHandler.deleteFinanceRecord(selected.getId());
            loadFinanceRecords();
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
