package main.dao;

import main.model.Contact;
import main.model.Customer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerDao implements CrudDAO<Customer> {

    Connection connDB;

    public CustomerDao() {
        this.connDB = DBConnection.getConnection();
    }

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
        if (!model.hasRequiredData())
            return 1;

        Query insert = new Query(connDB, "INSERT INTO customers " +
                "(Customer_ID, Customer_Name, Address, Postal_Code, Phone, Division_ID) " +
                "VALUES ( " +
                model.getCustomerID() + ", " +
                "'" + model.getCustomerName() + "', " +
                "'" + model.getAddress() + "', " +
                "'" + model.getPostalCode() + "', " +
                "'" + model.getPhone() + "', " +
                model.getDivisionID() +
                " )");

        return insert.executeQuery();
    }

    //TODO add insert with creation and update time data

    @Override
    public int update(Customer model) {
        if (!model.hasRequiredData())
            return 1;

        Query update = new Query(connDB, "UPDATE customers " +
                "SET " +
                "Customer_ID = " + model.getCustomerID() + ", " +
                "Customer_Name = '" + model.getCustomerName() + "', " +
                "Address = '" + model.getAddress() + "', " +
                "Postal_Code = '" + model.getPostalCode() + "', " +
                "Phone = '" + model.getPhone() + "', " +
                "Division_ID = " + model.getDivisionID() + " " +
                "WHERE Customer_ID = " + model.getCustomerID());

        return update.executeQuery();
    }

    @Override
    public int delete(int id) {
        Query delete = new Query(connDB, "DELETE FROM customers " +
                "WHERE Customer_ID = " + id);
        return delete.executeQuery();
    }
}
