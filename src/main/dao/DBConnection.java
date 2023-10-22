package main.dao;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * A utility class that hosts a static Connection variable for DAOs to use.
 */
public class DBConnection {
    private static Connection conn;

    /**
     * Static method that establishes connection to a database and stores the result as a static variable.
     * @param url       address of database
     * @param username  username for database login
     * @param password  password for database login
     */
    public static void makeConnection(String url, String username, String password) {
        try {
            conn = (Connection) DriverManager.getConnection(url, username, password);
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    /**
     * A static method that closes the Connection variable's connection to the database.
     */
    public static void closeConnection() {
        try {
            conn.close();
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    /**
     * Retrieve static connection variable
     * @return  Connection conn.
     */
    public static Connection getConnection() {
        return conn;
    }
}
