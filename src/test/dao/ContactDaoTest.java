package test.dao;

import main.dao.AppointmentDao;
import main.dao.ContactDao;
import main.dao.DBConnection;
import main.model.Contact;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class ContactDaoTest {

    private static ContactDao testedDao;

    @BeforeClass
    public static void setup() throws Exception{
        System.out.println("startup - creating DB connection to test database");
        DBConnection.makeConnection("jdbc:mysql://localhost:3306/test_client_db", "jdbc", "password1");
        testedDao = new ContactDao(DBConnection.getConnection());
        System.out.println("Connection successful");
    }

    @Test
    public void getByID() {
        Contact expectedFirstContact = new Contact(1, "Anika Costa", "acoasta@company.com");
        Contact expectedThirdContact = new Contact(3, "Li Lee", "llee@company.com");

        assertEquals(expectedFirstContact, testedDao.getByID(1));
        assertEquals(expectedThirdContact, testedDao.getByID(3));
    }

    @Test
    public void getAll() {
        Contact expectedFirstContact = new Contact(1, "Anika Costa", "acoasta@company.com");
        Contact expectedThirdContact = new Contact(3, "Li Lee", "llee@company.com");

        ArrayList<Contact> contacts = testedDao.getAll();
        assertTrue(contacts != null);
        assertEquals(3, contacts.size());
        assertEquals(expectedFirstContact, contacts.get(0));
        assertEquals(expectedThirdContact, contacts.get(2));
    }

    @Test
    public void insert() {
        Contact expectedSuccess = new Contact(4, "Kjorg Kjorgson", "kkjorgson@company.com");
        Contact existingIDFailure = new Contact(1, "Tom Tomson", "ttomson@company.com");
        Contact missingNameFailure = new Contact(5, null, "rnoname@company.com");

        assertEquals(0, testedDao.insert(expectedSuccess));
        assertEquals(1, testedDao.insert(existingIDFailure));
        assertEquals(1, testedDao.insert(missingNameFailure));
    }

    @Test
    public void update() {
        Contact expectedFirstContact = new Contact(1, "Anika Costa", "acoasta@company.com");
        Contact updatedFirstContact = new Contact(1, "Anika Costa", "acoasta@business.com");

        assertEquals(0, testedDao.update(updatedFirstContact));
        assertEquals(updatedFirstContact, testedDao.getByID(1));
        assertEquals(0, testedDao.update(expectedFirstContact));
    }

    @Test
    public void delete() {
        assertEquals(1, testedDao.delete(0));
        assertEquals(1, testedDao.delete(9999));
        assertEquals(0, testedDao.delete(4));
    }
}