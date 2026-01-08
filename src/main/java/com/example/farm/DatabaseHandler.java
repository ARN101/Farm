package com.example.farm;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler {
    private static final String DB_URL = "jdbc:sqlite:farm.db";

    public static void createTables() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
                Statement stmt = conn.createStatement()) {

            // Create Animals table
            String sqlAnimals = "CREATE TABLE IF NOT EXISTS animals (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "type TEXT NOT NULL," +
                    "name TEXT NOT NULL," +
                    "breed TEXT NOT NULL," +
                    "age INTEGER NOT NULL," +
                    "health_status TEXT NOT NULL" +
                    ")";
            stmt.execute(sqlAnimals);

            // Create Vaccinations table
            String sqlVaccinations = "CREATE TABLE IF NOT EXISTS vaccinations (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "animal_id TEXT NOT NULL," +
                    "vaccine TEXT NOT NULL," +
                    "date_given TEXT NOT NULL," +
                    "next_due_date TEXT NOT NULL," +
                    "status TEXT NOT NULL" +
                    ")";
            stmt.execute(sqlVaccinations);

            System.out.println("Tables created successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ObservableList<Animal> getAnimals() {
        ObservableList<Animal> animals = FXCollections.observableArrayList();
        String sql = "SELECT * FROM animals";

        try (Connection conn = DriverManager.getConnection(DB_URL);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                animals.add(new Animal(
                        rs.getInt("id"),
                        rs.getString("type"),
                        rs.getString("name"),
                        rs.getString("breed"),
                        rs.getInt("age"),
                        rs.getString("health_status")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return animals;
    }

    public static void addAnimal(Animal animal) {
        String sql = "INSERT INTO animals(type, name, breed, age, health_status) VALUES(?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, animal.getType());
            pstmt.setString(2, animal.getName());
            pstmt.setString(3, animal.getBreed());
            pstmt.setInt(4, animal.getAge());
            pstmt.setString(5, animal.getHealthStatus());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteAnimal(int id) {
        String sqlVaccinations = "DELETE FROM vaccinations WHERE animal_id = ?";
        String sqlAnimal = "DELETE FROM animals WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            conn.setAutoCommit(false);

            try (PreparedStatement pstmtVaccinations = conn.prepareStatement(sqlVaccinations);
                    PreparedStatement pstmtAnimal = conn.prepareStatement(sqlAnimal)) {

                // Delete vaccinations first
                pstmtVaccinations.setString(1, String.valueOf(id));
                pstmtVaccinations.executeUpdate();

                // Then delete animal
                pstmtAnimal.setInt(1, id);
                pstmtAnimal.executeUpdate();

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ObservableList<VaccinationRecord> getVaccinations() {
        ObservableList<VaccinationRecord> records = FXCollections.observableArrayList();
        String sql = "SELECT * FROM vaccinations";

        try (Connection conn = DriverManager.getConnection(DB_URL);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                records.add(new VaccinationRecord(
                        rs.getInt("id"),
                        rs.getString("animal_id"),
                        rs.getString("vaccine"),
                        LocalDate.parse(rs.getString("date_given")),
                        LocalDate.parse(rs.getString("next_due_date")),
                        rs.getString("status")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return records;
    }

    public static void addVaccination(VaccinationRecord record) {
        String sql = "INSERT INTO vaccinations(animal_id, vaccine, date_given, next_due_date, status) VALUES(?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, record.getAnimalId());
            pstmt.setString(2, record.getVaccine());
            pstmt.setString(3, record.getDateGiven().toString());
            pstmt.setString(4, record.getNextDueDate().toString());
            pstmt.setString(5, record.getStatus());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<String> getAllAnimalIds() {
        List<String> ids = new ArrayList<>();
        String sql = "SELECT id FROM animals";

        try (Connection conn = DriverManager.getConnection(DB_URL);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                ids.add(String.valueOf(rs.getInt("id")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ids;
    }
}
