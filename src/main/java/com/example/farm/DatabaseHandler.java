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

            // Create Animals table with user_email
            String sqlAnimals = "CREATE TABLE IF NOT EXISTS animals (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "type TEXT NOT NULL," +
                    "name TEXT NOT NULL," +
                    "breed TEXT NOT NULL," +
                    "age INTEGER NOT NULL," +
                    "health_status TEXT NOT NULL," +
                    "user_email TEXT" +
                    ")";
            stmt.execute(sqlAnimals);

            // Migration for animals: add user_email if missing
            try {
                stmt.execute("ALTER TABLE animals ADD COLUMN user_email TEXT");
                System.out.println("Migrated animals table: added user_email column");
            } catch (SQLException e) {
                // Column likely already exists, ignore
            }

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

            // Create Finance table with user_email
            String sqlFinance = "CREATE TABLE IF NOT EXISTS finance (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "type TEXT NOT NULL," +
                    "category TEXT NOT NULL," +
                    "amount REAL NOT NULL," +
                    "date TEXT NOT NULL," +
                    "animal_id TEXT," +
                    "notes TEXT," +
                    "user_email TEXT" +
                    ")";
            stmt.execute(sqlFinance);

            // Migration for finance: add user_email if missing
            try {
                stmt.execute("ALTER TABLE finance ADD COLUMN user_email TEXT");
                System.out.println("Migrated finance table: added user_email column");
            } catch (SQLException e) {
                // Column likely already exists, ignore
            }

            // Migration: Check if farm_profile has 'email' column, if not DROP it
            try (ResultSet rs = conn.getMetaData().getColumns(null, null, "farm_profile", "email")) {
                if (!rs.next()) {
                    // Column doesn't exist, generic check if table exists to be safe, but just
                    // dropping is fine if exists
                    stmt.execute("DROP TABLE IF EXISTS farm_profile");
                    System.out.println("Dropped old farm_profile table for migration.");
                }
            }

            // Create Farm Profile table (Updated for Multi-user)
            String sqlProfile = "CREATE TABLE IF NOT EXISTS farm_profile (" +
                    "email TEXT PRIMARY KEY," +
                    "farm_name TEXT," +
                    "city TEXT," +
                    "country TEXT" +
                    ")";
            stmt.execute(sqlProfile);

            // Create Users table
            String sqlUsers = "CREATE TABLE IF NOT EXISTS users (" +
                    "email TEXT PRIMARY KEY," +
                    "password TEXT NOT NULL" +
                    ")";
            stmt.execute(sqlUsers);

            System.out.println("Tables created successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // --- Animal Methods ---

    public static ObservableList<Animal> getAnimals() {
        ObservableList<Animal> animals = FXCollections.observableArrayList();
        String currentUser = SessionManager.getUserEmail();
        if (currentUser == null)
            return animals;

        String sql = "SELECT * FROM animals WHERE user_email = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, currentUser);
            ResultSet rs = pstmt.executeQuery();

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
        String currentUser = SessionManager.getUserEmail();
        if (currentUser == null)
            return;

        String sql = "INSERT INTO animals(type, name, breed, age, health_status, user_email) VALUES(?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, animal.getType());
            pstmt.setString(2, animal.getName());
            pstmt.setString(3, animal.getBreed());
            pstmt.setInt(4, animal.getAge());
            pstmt.setString(5, animal.getHealthStatus());
            pstmt.setString(6, currentUser);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteAnimal(int id) {
        String currentUser = SessionManager.getUserEmail();
        // Verify ownership before deleting
        String verifySql = "SELECT 1 FROM animals WHERE id = ? AND user_email = ?";
        String sqlVaccinations = "DELETE FROM vaccinations WHERE animal_id = ?";
        String sqlAnimal = "DELETE FROM animals WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            // Check ownership
            try (PreparedStatement checkStmt = conn.prepareStatement(verifySql)) {
                checkStmt.setInt(1, id);
                checkStmt.setString(2, currentUser);
                ResultSet rs = checkStmt.executeQuery();
                if (!rs.next()) {
                    System.out.println("Attempted to delete animal not owned by user.");
                    return;
                }
            }

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

    // --- Vaccination Methods ---

    public static ObservableList<VaccinationRecord> getVaccinations() {
        ObservableList<VaccinationRecord> records = FXCollections.observableArrayList();
        String currentUser = SessionManager.getUserEmail();
        if (currentUser == null)
            return records;

        // Join with animals to ensure we only get vaccinations for animals owned by
        // current user
        String sql = "SELECT v.* FROM vaccinations v " +
                "JOIN animals a ON v.animal_id = CAST(a.id AS TEXT) " +
                "WHERE a.user_email = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, currentUser);
            ResultSet rs = pstmt.executeQuery();

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
        String currentUser = SessionManager.getUserEmail();
        String sql = "SELECT id FROM animals WHERE user_email = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, currentUser);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                ids.add(String.valueOf(rs.getInt("id")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ids;
    }

    // --- Finance Methods ---

    public static ObservableList<FinanceRecord> getFinanceRecords() {
        ObservableList<FinanceRecord> records = FXCollections.observableArrayList();
        String currentUser = SessionManager.getUserEmail();
        if (currentUser == null)
            return records;

        String sql = "SELECT * FROM finance WHERE user_email = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, currentUser);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                records.add(new FinanceRecord(
                        rs.getInt("id"),
                        rs.getString("type"),
                        rs.getString("category"),
                        rs.getDouble("amount"),
                        LocalDate.parse(rs.getString("date")),
                        rs.getString("animal_id"),
                        rs.getString("notes")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return records;
    }

    public static void addFinanceRecord(FinanceRecord record) {
        String currentUser = SessionManager.getUserEmail();
        if (currentUser == null)
            return;

        String sql = "INSERT INTO finance(type, category, amount, date, animal_id, notes, user_email) VALUES(?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, record.getType());
            pstmt.setString(2, record.getCategory());
            pstmt.setDouble(3, record.getAmount());
            pstmt.setString(4, record.getDate().toString());
            pstmt.setString(5, record.getAnimalId());
            pstmt.setString(6, record.getNotes());
            pstmt.setString(7, currentUser);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteFinanceRecord(int id) {
        String currentUser = SessionManager.getUserEmail();
        String sql = "DELETE FROM finance WHERE id = ? AND user_email = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.setString(2, currentUser);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // --- User & Profile Methods ---

    public static boolean isEmailTaken(String email) {
        String sql = "SELECT 1 FROM users WHERE email = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean registerUser(String email, String password, String farmName, String city, String country) {
        String userSql = "INSERT INTO users(email, password) VALUES(?, ?)";
        String profileSql = "INSERT INTO farm_profile(email, farm_name, city, country) VALUES(?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            conn.setAutoCommit(false);
            try (PreparedStatement uStmt = conn.prepareStatement(userSql);
                    PreparedStatement pStmt = conn.prepareStatement(profileSql)) {

                uStmt.setString(1, email);
                uStmt.setString(2, password);
                uStmt.executeUpdate();

                pStmt.setString(1, email);
                pStmt.setString(2, farmName);
                pStmt.setString(3, city);
                pStmt.setString(4, country);
                pStmt.executeUpdate();

                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean validateLogin(String email, String password) {
        String sql = "SELECT password FROM users WHERE email = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("password").equals(password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void saveProfile(String email, String name, String city, String country) {
        String sql = "UPDATE farm_profile SET farm_name = ?, city = ?, country = ? WHERE email = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, city);
            pstmt.setString(3, country);
            pstmt.setString(4, email);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String[] getProfile(String email) {
        String sql = "SELECT farm_name, city, country FROM farm_profile WHERE email = ?";
        String[] profile = new String[] { "", "", "" };
        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                profile[0] = rs.getString("farm_name");
                profile[1] = rs.getString("city");
                profile[2] = rs.getString("country");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return profile;
    }
}
