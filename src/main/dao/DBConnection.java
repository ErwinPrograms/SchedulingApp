package main.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static Connection conn;
    public static void makeConnection(String url, String username, String password) throws ClassNotFoundException, SQLException, Exception{
        conn=(Connection) DriverManager.getConnection(url,username,password);
    }
    public static void closeConnection() throws ClassNotFoundException,SQLException, Exception{
        conn.close();
    }

    public static Connection getConnection() {
        return conn;
    }
}
