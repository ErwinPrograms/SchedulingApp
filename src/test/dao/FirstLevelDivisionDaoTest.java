package test.dao;

import main.dao.CountryDao;
import main.dao.DBConnection;
import main.dao.FirstLevelDivisionDao;
import main.model.Country;
import main.model.FirstLevelDivision;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class FirstLevelDivisionDaoTest {

    private static FirstLevelDivisionDao testedDao;
    @BeforeClass
    public static void setup() throws Exception{
        System.out.println("startup - creating DB connection to test database");
        DBConnection.makeConnection("jdbc:mysql://localhost:3306/test_client_db", "jdbc", "password1");
        testedDao = new FirstLevelDivisionDao(DBConnection.getConnection());
        System.out.println("Connection successful");
    }

    @Test
    public void getByID() {
        FirstLevelDivision expectedFirstDivision = new FirstLevelDivision(1, "Alabama", 1);
        FirstLevelDivision expected30Division = new FirstLevelDivision(30, "New Mexico", 1);
        FirstLevelDivision expected103Division = new FirstLevelDivision(103, "Scotland", 2);

        assertEquals(expectedFirstDivision, testedDao.getByID(1));
        assertEquals(expected30Division, testedDao.getByID(30));
        assertEquals(expected103Division, testedDao.getByID(103));
    }

    @Test
    public void getAll() {
        //Division IDs aren't sequential
        FirstLevelDivision expectedFirstDivision = new FirstLevelDivision(1, "Alabama", 1);
        FirstLevelDivision expected30thDivision = new FirstLevelDivision(30, "New Mexico", 1);
        FirstLevelDivision expected66thDivision = new FirstLevelDivision(103, "Scotland", 2);

        ArrayList<FirstLevelDivision> firstLevelDivisions = testedDao.getAll();

        assertEquals(68, firstLevelDivisions.size());
        assertEquals(expectedFirstDivision, firstLevelDivisions.get(0));
        assertEquals(expected30thDivision, firstLevelDivisions.get(29));
        assertEquals(expected66thDivision, firstLevelDivisions.get(66));
    }
}