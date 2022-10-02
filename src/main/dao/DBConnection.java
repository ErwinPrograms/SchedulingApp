package main.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static Connection conn;
    public static void makeConnection(String url, String username, String password) {
        try {
            conn = (Connection) DriverManager.getConnection(url, username, password);
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }
    public static void closeConnection() {
        try {
            conn.close();
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    public static Connection getConnection() {
        return conn;
    }
}
