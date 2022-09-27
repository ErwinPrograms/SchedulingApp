package main;

import main.dao.DBConnection;
import main.dao.Query;

import java.sql.ResultSet;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        try {
            System.out.println("Trying to connect...");
            DBConnection.makeConnection();
            System.out.println("Connection made!");

            System.out.println("Making query...");
            Query.makeQuery("SELECT * FROM customers");
            ResultSet statementResult = Query.getResult();
            statementResult.next();
            System.out.println(statementResult.getString("customer_name"));

            System.out.println("Closing connection...");
            DBConnection.closeConnection();
            System.out.println("Closed connection!");
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}