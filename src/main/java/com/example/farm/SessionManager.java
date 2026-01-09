package com.example.farm;

public class SessionManager {
    private static String currentUserEmail;
    private static boolean isAdmin = false;

    public static void setUser(String email, boolean admin) {
        currentUserEmail = email;
        isAdmin = admin;
    }

    public static String getUserEmail() {
        return currentUserEmail;
    }

    public static boolean isAdmin() {
        return isAdmin;
    }

    public static void clear() {
        currentUserEmail = null;
        isAdmin = false;
    }
}
