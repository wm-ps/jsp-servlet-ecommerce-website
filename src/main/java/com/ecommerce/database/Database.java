package com.ecommerce.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static final String DEFAULT_HOST = "54.82.188.74";
    private static final String DEFAULT_PORT = "8024";
    private static final String DEFAULT_USER = "root";
    private static final String DEFAULT_PASSWORD = "SJP#WmE26DB";
    private static final String DEFAULT_DB = "jsp_servlet_ecommerce";
    private static final String FALLBACK_DB = "jsp-servlet-ecommerce-website";

    private String env(String key, String defaultValue) {
        String value = System.getenv(key);
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        return value.trim();
    }

    private Connection openConnection(String databaseName) throws SQLException {
        String host = env("DB_HOST", DEFAULT_HOST);
        String port = env("DB_PORT", DEFAULT_PORT);
        String user = env("DB_USER", DEFAULT_USER);
        String password = env("DB_PASSWORD", DEFAULT_PASSWORD);
        String url = "jdbc:mysql://" + host + ":" + port + "/" + databaseName
                + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        return DriverManager.getConnection(url, user, password);
    }

    public Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String configuredDb = env("DB_NAME", DEFAULT_DB);
            try {
                return openConnection(configuredDb);
            } catch (SQLException firstError) {
                if (!FALLBACK_DB.equals(configuredDb)) {
                    return openConnection(FALLBACK_DB);
                }
                throw firstError;
            }
        } catch (Exception e) {
            System.out.println("Database connection failed: " + e.getMessage());
            return null;
        }
    }

    public static void main(String[] args) {
        System.out.println(new Database().getConnection());
    }
}
