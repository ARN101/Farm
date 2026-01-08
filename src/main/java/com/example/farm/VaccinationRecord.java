package com.example.farm;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import java.time.LocalDate;

public class VaccinationRecord {
    private final SimpleIntegerProperty id;
    private final SimpleStringProperty animalId;
    private final SimpleStringProperty vaccine;
    private final SimpleObjectProperty<LocalDate> dateGiven;
    private final SimpleObjectProperty<LocalDate> nextDueDate;
    private final SimpleStringProperty status;

    public VaccinationRecord(int id, String animalId, String vaccine, LocalDate dateGiven, LocalDate nextDueDate,
            String status) {
        this.id = new SimpleIntegerProperty(id);
        this.animalId = new SimpleStringProperty(animalId);
        this.vaccine = new SimpleStringProperty(vaccine);
        this.dateGiven = new SimpleObjectProperty<>(dateGiven);
        this.nextDueDate = new SimpleObjectProperty<>(nextDueDate);
        this.status = new SimpleStringProperty(status);
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getAnimalId() {
        return animalId.get();
    }

    public void setAnimalId(String animalId) {
        this.animalId.set(animalId);
    }

    public String getVaccine() {
        return vaccine.get();
    }

    public void setVaccine(String vaccine) {
        this.vaccine.set(vaccine);
    }

    public LocalDate getDateGiven() {
        return dateGiven.get();
    }

    public void setDateGiven(LocalDate dateGiven) {
        this.dateGiven.set(dateGiven);
    }

    public LocalDate getNextDueDate() {
        return nextDueDate.get();
    }

    public void setNextDueDate(LocalDate nextDueDate) {
        this.nextDueDate.set(nextDueDate);
    }

    public String getStatus() {
        return status.get();
    }

    public void setStatus(String status) {
        this.status.set(status);
    }
}
