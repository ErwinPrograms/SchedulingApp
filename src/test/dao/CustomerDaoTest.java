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
        Customer expectedSuccess = new Customer(4, "Bob", "123 St",
                "00000", "555-5555", 29);
        Customer idAlreadyExists = new Customer(1, "New Person",
                "98 Ave", "12345", "000-000-0000", 103);
        Customer noForeignKey = new Customer(5, "Mermaid Man",
                "Bikini Bottom", "999999", "111-111-1111", 2000);

        assertEquals(0, testedDao.insert(expectedSuccess));
        assertEquals(1, testedDao.insert(idAlreadyExists));
        assertEquals(1, testedDao.insert(noForeignKey));
        assertEquals(expectedSuccess, testedDao.getByID(4));
    }

    @Test
    public void update() {
        Customer firstCustomer = new Customer(1, "Daddy Warbucks",
                "1919 Boardwalk", "01291", "869-908-1875", 29);
        Customer updatedNameFirstCustomer = new Customer(1, "Father Warbucks",
                "1919 Boardwalk", "01291", "869-908-1875", 29);
        Customer badDivisionIDFirstCustomer = new Customer(1, "Daddy Warbucks",
                "1919 Boardwalk", "01291", "869-908-1875", 5000);

        assertEquals(1, testedDao.update(badDivisionIDFirstCustomer));
        assertEquals(0, testedDao.update(updatedNameFirstCustomer));
        assertEquals(updatedNameFirstCustomer, testedDao.getByID(1));
        assertEquals(0, testedDao.update(firstCustomer));
        assertEquals(firstCustomer, testedDao.getByID(1));
    }

    @Test
    public void delete() {
        Customer updateTestObject = new Customer(4, "Bob", "123 St",
                "00000", "555-5555", 29);

        assertEquals(1, testedDao.delete(1000));
        assertEquals(1, testedDao.delete(0));
        assertEquals(updateTestObject, testedDao.getByID(4));
        assertEquals(0, testedDao.delete(4));
        assertEquals(null, testedDao.getByID(4));
    }
}