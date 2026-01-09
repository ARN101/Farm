package com.example.farm;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import java.time.LocalDate;

public class FinanceRecord {
    private final SimpleIntegerProperty id;
    private final SimpleStringProperty type; // "Expense" or "Income"
    private final SimpleStringProperty category;
    private final SimpleDoubleProperty amount;
    private final SimpleObjectProperty<LocalDate> date;
    private final SimpleStringProperty animalId;
    private final SimpleStringProperty notes;

    public FinanceRecord(int id, String type, String category, double amount, LocalDate date, String animalId,
            String notes) {
        this.id = new SimpleIntegerProperty(id);
        this.type = new SimpleStringProperty(type);
        this.category = new SimpleStringProperty(category);
        this.amount = new SimpleDoubleProperty(amount);
        this.date = new SimpleObjectProperty<>(date);
        this.animalId = new SimpleStringProperty(animalId);
        this.notes = new SimpleStringProperty(notes);
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getType() {
        return type.get();
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public String getCategory() {
        return category.get();
    }

    public void setCategory(String category) {
        this.category.set(category);
    }

    public double getAmount() {
        return amount.get();
    }

    public void setAmount(double amount) {
        this.amount.set(amount);
    }

    public LocalDate getDate() {
        return date.get();
    }

    public void setDate(LocalDate date) {
        this.date.set(date);
    }

    public String getAnimalId() {
        return animalId.get();
    }

    public void setAnimalId(String animalId) {
        this.animalId.set(animalId);
    }

    public String getNotes() {
        return notes.get();
    }

    public void setNotes(String notes) {
        this.notes.set(notes);
    }
}
