package test.dao;

import main.dao.ContactDao;
import main.dao.CountryDao;
import main.dao.DBConnection;
import main.model.Country;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class CountryDaoTest {

    private static CountryDao testedDao;
    @BeforeClass
    public static void setup() throws Exception{
        System.out.println("startup - creating DB connection to test database");
        DBConnection.makeConnection("jdbc:mysql://localhost:3306/test_client_db", "jdbc", "password1");
        testedDao = new CountryDao(DBConnection.getConnection());
        System.out.println("Connection successful");
    }

    @Test
    public void getByID() {
        Country expectedFirstCountry = new Country(1, "U.S");
        Country expectedThirdCountry = new Country(3, "Canada");

        assertEquals(expectedFirstCountry, testedDao.getByID(1));
        assertEquals(expectedThirdCountry, testedDao.getByID(3));
    }

    @Test
    public void getAll() {
        Country expectedFirstCountry = new Country(1, "U.S");
        Country expectedThirdCountry = new Country(3, "Canada");

        ArrayList<Country> countries = testedDao.getAll();

        assertEquals(3, countries.size());
        assertEquals(expectedFirstCountry, countries.get(0));
        assertEquals(expectedThirdCountry, countries.get(2));
    }
}