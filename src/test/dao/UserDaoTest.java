package test.dao;

import main.dao.DBConnection;
import main.dao.FirstLevelDivisionDao;
import main.dao.UserDao;
import main.model.User;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class UserDaoTest {

    private static UserDao testedDao;
    @BeforeClass
    public static void setup() throws Exception{
        System.out.println("startup - creating DB connection to test database");
        DBConnection.makeConnection("jdbc:mysql://localhost:3306/test_client_db", "jdbc", "password1");
        testedDao = new UserDao(DBConnection.getConnection());
        System.out.println("Connection successful");
    }

    @Test
    public void getByID() {
        User expectedFirst = new User(1, "test", "test");
        User expectedSecond = new User(2, "admin", "admin");

        assertEquals(expectedFirst, testedDao.getByID(1));
        assertEquals(expectedSecond, testedDao.getByID(2));
    }

    @Test
    public void getAll() {
        User expectedFirst = new User(1, "test", "test");
        User expectedSecond = new User(2, "admin", "admin");

        ArrayList<User> users = testedDao.getAll();

        assertEquals(2, users.size());
        assertEquals(expectedFirst, users.get(0));
        assertEquals(expectedSecond, users.get(1));
    }
}