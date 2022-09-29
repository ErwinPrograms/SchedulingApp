package test.dao;

import main.dao.DBConnection;
import main.dao.Query;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;
import java.sql.ResultSet;

import static org.junit.Assert.*;

public class QueryTest {

    public static Connection conn;

    @BeforeClass
    public static void setup() throws Exception {
        System.out.println("startup - creating DB connection to test database");
        DBConnection.makeConnection("jdbc:mysql://localhost:3306/test_client_db", "jdbc", "password1");
        conn = DBConnection.getConnection();
        System.out.println("Connection successful");
    }

    @Test
    public void executeSelect() throws Exception {
        Query selectSuccess = new Query(conn, "SELECT * FROM appointments");
        Query selectFailure = new Query(conn, "SELECT * FROM gibberish");
        Query selectEmpty = new Query(conn, "SELECT * FROM appointments WHERE Appointment_id = 99999");

        selectSuccess.executeQuery();
        assertTrue(selectSuccess.getResult() != null);
        selectSuccess.close();

        selectFailure.executeQuery();
        assertTrue(selectFailure.getResult() ==  null);
        selectFailure.close();

        selectEmpty.executeQuery();
        assertTrue(selectEmpty.getResult() !=  null);
        selectEmpty.close();
    }

    @Test
    public void executeInsert() throws Exception {
        Query insertSuccess = new Query(conn, "INSERT INTO countries (Country) VALUES ('Rome'), ('Persia')");
        Query noTableFailure = new Query(conn, "INSERT INTO gibberish (Country) VALUES ('Rome')");
        Query lowColCountFailure = new Query(conn, "INSERT INTO customers VALUES ('asd')");
        Query badFKFailure = new Query(conn,
                "INSERT INTO appointments (Title, Description, Customer_ID, User_ID, Contact_ID)" +
                        "VALUES ('Test Fail', 'Foreign Key not exists', 10, 1, 1)");

        assertEquals(0, insertSuccess.executeQuery());
        assertEquals(1, noTableFailure.executeQuery());
        assertEquals(1, lowColCountFailure.executeQuery());
        assertEquals(1, badFKFailure.executeQuery());
    }

    @Test
    public void executeUpdate() throws Exception {
        Query updateSuccess = new Query(conn,
                "UPDATE countries SET Last_Update = now(), Last_Updated_By = 'test' WHERE Country_ID > 3");

        assertEquals(0, updateSuccess.executeQuery());
    }

    @Test
    public void executeDelete() throws Exception {

    }

}