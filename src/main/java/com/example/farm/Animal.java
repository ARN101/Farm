package com.example.farm;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Animal {
    private final SimpleIntegerProperty id;
    private final SimpleStringProperty type;
    private final SimpleStringProperty name;
    private final SimpleStringProperty breed;
    private final SimpleIntegerProperty age;
    private final SimpleStringProperty healthStatus;

    public Animal(int id, String type, String name, String breed, int age, String healthStatus) {
        this.id = new SimpleIntegerProperty(id);
        this.type = new SimpleStringProperty(type);
        this.name = new SimpleStringProperty(name);
        this.breed = new SimpleStringProperty(breed);
        this.age = new SimpleIntegerProperty(age);
        this.healthStatus = new SimpleStringProperty(healthStatus);
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

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getBreed() {
        return breed.get();
    }

    public void setBreed(String breed) {
        this.breed.set(breed);
    }

    public int getAge() {
        return age.get();
    }

    public void setAge(int age) {
        this.age.set(age);
    }

    public String getHealthStatus() {
        return healthStatus.get();
    }

    public void setHealthStatus(String healthStatus) {
        this.healthStatus.set(healthStatus);
    }
}
