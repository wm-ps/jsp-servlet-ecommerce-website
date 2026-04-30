package com.ecommerce.database;

import java.sql.Connection;
import java.sql.DriverManager;

public class Database {
    static {
        // In some servlet containers the JDBC driver is not auto-registered early enough.
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ignored) {
            // If the driver is missing, getConnection() will fail with a clear message.
        }
    }

    public Connection getConnection() {
        try {
            String url = firstNonBlank(
                    System.getProperty("db.url"),
                    System.getenv("DB_URL"),
                    "jdbc:mysql://localhost:3306/jsp-servlet-ecommerce-website"
            );
            String user = firstNonBlank(
                    System.getProperty("db.user"),
                    System.getenv("DB_USER"),
                    "root"
            );
            String pass = firstNonBlank(
                    System.getProperty("db.pass"),
                    System.getenv("DB_PASS"),
                    "root"
            );

            return DriverManager.getConnection(url, user, pass);
        } catch (Exception e) {
            System.out.println("Database connection failed: " + e.getMessage());
            return null;
        }
    }

    private static String firstNonBlank(String... candidates) {
        if (candidates == null) return null;
        for (String c : candidates) {
            if (c != null && !c.trim().isEmpty()) return c;
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(new Database().getConnection());
    }
}
