package com.vgb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * This class is used to make a connection to my SQL database
 */
public class DatabaseConnector {

    private static final String URL = System.getenv("DB_URL");
    private static final String LOGIN = System.getenv("DB_USER");
    private static final String PASSWORD = System.getenv("DB_PASSWORD");

    public static Connection getConnection() {
        if (URL == null || LOGIN == null || PASSWORD == null) {
            throw new RuntimeException(
                "Database credentials not configured. " +
                "Set DB_URL, DB_USER, and DB_PASSWORD as environment variables."
            );
        }

        try {
            return DriverManager.getConnection(URL, LOGIN, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to connect to database", e);
        }
    }
}
