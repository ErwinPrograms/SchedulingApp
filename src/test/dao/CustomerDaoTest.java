package test.dao;

import main.dao.CountryDao;
import main.dao.CustomerDao;
import main.dao.DBConnection;
import main.model.Country;
import main.model.Customer;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class CustomerDaoTest {

    private static CustomerDao testedDao;
    @BeforeClass
    public static void setup() throws Exception {
        System.out.println("startup - creating DB connection to test database");
        DBConnection.makeConnection("jdbc:mysql://localhost:3306/test_client_db", "jdbc", "password1");
        testedDao = new CustomerDao(DBConnection.getConnection());
        System.out.println("Connection successful");
    }

    @Test
    public void getByID() {
        Customer expectedFirstCustomer = new Customer(1, "Daddy Warbucks",
                "1919 Boardwalk", "01291", "869-908-1875", 29);
        Customer expectedThirdCustomer = new Customer(3, "Dudley Do-Right",
                "48 Horse Manor", "28198", "874-916-2671", 60);

        assertEquals(expectedFirstCustomer, testedDao.getByID(1));
        assertEquals(expectedThirdCustomer.getAddress(), testedDao.getByID(3).getAddress());
    }

    @Test
    public void getAll() {
        Customer expectedFirstCustomer = new Customer(1, "Daddy Warbucks",
                "1919 Boardwalk", "01291", "869-908-1875", 29);
        Customer expectedThirdCustomer = new Customer(3, "Dudley Do-Right",
                "48 Horse Manor", "28198", "874-916-2671", 60);

        ArrayList<Customer> customers = testedDao.getAll();

        assertEquals(3, customers.size());
        assertEquals(expectedFirstCustomer, customers.get(0));
        assertEquals(expectedThirdCustomer, customers.get(2));
    }

    @Test
    public void insert() {
    }

    @Test
    public void update() {
    }

    @Test
    public void delete() {
    }
}