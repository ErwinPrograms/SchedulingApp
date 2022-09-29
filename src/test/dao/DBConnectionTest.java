package test.dao;

import main.dao.DBConnection;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.SQLException;

import static org.junit.Assert.*;

public class DBConnectionTest {

    @BeforeClass
    public static void init() throws Exception{
        DBConnection.makeConnection("jdbc:mysql://localhost:3306/test_client_db", "jdbc", "password1");
    }

    @Test
    public void makeConnection() throws SQLException {
        assertTrue(DBConnection.getConnection().isValid(1000));
    }

    @Test
    public void closeConnection() throws Exception{
        DBConnection.closeConnection();
        assertTrue(DBConnection.getConnection().isClosed());
    }
}