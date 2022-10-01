package main.dao;

import main.model.Contact;
import main.model.Customer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerDao implements CrudDAO<Customer> {

    Connection connDB;

    public CustomerDao(Connection connDB) {
        this.connDB = connDB;
    }

    @Override
    public Customer getByID(int id) {
        Query queryByID = new Query(connDB,
                "SELECT * FROM customers " +
                        "WHERE Customer_ID = " + id);
        queryByID.executeQuery();
        ResultSet resultCursor = queryByID.getResult();
        try {
            if (resultCursor.next()) {
                return new Customer(resultCursor);
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        return null;
    }

    @Override
    public ArrayList<Customer> getAll() {
        ArrayList<Customer> allCustomers = new ArrayList<>();

        Query queryAll = new Query(connDB, "SELECT * FROM customers");
        queryAll.executeQuery();
        ResultSet resultCursor = queryAll.getResult();

        try {
            while (resultCursor.next()) {
                allCustomers.add(new Customer(resultCursor));
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }

        return allCustomers;
    }

    @Override
    public int insert(Customer model) {
        return 0;
    }

    @Override
    public int update(Customer model) {
        return 0;
    }

    @Override
    public int delete(int id) {
        return 0;
    }
}
