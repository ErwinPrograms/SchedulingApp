package main.dao;

import main.model.Customer;
import main.model.User;
import main.utility.TimeUtility;
import main.utility.UniversalApplicationData;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * DAO responsible for CRUD operations on Customers.
 */
public class CustomerDao implements CrudDAO<Customer> {

    Connection connDB;

    /**
     * Default constructor which calls DBConnection singleton to get a Connection object
     */
    public CustomerDao() {
        this.connDB = DBConnection.getConnection();
    }

    /**
     * Constructor which takes a Connection object to use
     * @param connDB    Connection object that DAO will use to access database
     */
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

    //TODO refactor with insert so that most code isn't repeated
    /**
     * Insert customer into the database along with data about the creating User.
     * @param model         The Customer object that holds data to be inserted
     * @param creationUser  The User object that holds data about the user that created this Customer
     * @return  A result code:
     *              0: success
     *              1: failure
     */
    public int insertWithUser(Customer model, User creationUser) {
        if (!model.hasRequiredData())
            return 1;

        Timestamp creationTimestamp = new TimeUtility().getUTCTime();

        Query insert = new Query(connDB, "INSERT INTO customers " +
                "(Customer_ID, Customer_Name, Address, Postal_Code, Phone, Division_ID, " +
                "Create_Date, Created_By, Last_Update, Last_Updated_By ) " +
                "VALUES ( " +
                model.getCustomerID() + ", " +
                "'" + model.getCustomerName() + "', " +
                "'" + model.getAddress() + "', " +
                "'" + model.getPostalCode() + "', " +
                "'" + model.getPhone() + "', " +
                model.getDivisionID() + ", " +
                "'" + creationTimestamp + "', " +
                "'" + creationUser.getUserName() + "', " +
                "'" + creationTimestamp + "', " +
                "'" + creationUser.getUserName() + "' " +
                " )");

        return insert.executeQuery();
    }

    @Override
    public int update(Customer model) {
        if (!model.hasRequiredData())
            return 1;

        Timestamp updateTimestamp = new TimeUtility().getUTCTime();

        Query update = new Query(connDB, "UPDATE customers " +
                "SET " +
                "Customer_ID = " + model.getCustomerID() + ", " +
                "Customer_Name = '" + model.getCustomerName() + "', " +
                "Address = '" + model.getAddress() + "', " +
                "Postal_Code = '" + model.getPostalCode() + "', " +
                "Phone = '" + model.getPhone() + "', " +
                //TODO: Refactor following 2 lines which add metadata with Singleton
                "Last_Updated_By = '" + UniversalApplicationData.getLoggedInUser().getUserName() + "', " +
                "Last_Update = '" + updateTimestamp + "', " +
                "Division_ID = " + model.getDivisionID() + " " +
                "WHERE Customer_ID = " + model.getCustomerID());

        return update.executeQuery();
    }

    //TODO: add another update method that accepts a User object so Last_Update and Last_Updated_By is also changed

    @Override
    public int delete(int id) {
        Query delete = new Query(connDB, "DELETE FROM customers " +
                "WHERE Customer_ID = " + id);
        return delete.executeQuery();
    }
}
