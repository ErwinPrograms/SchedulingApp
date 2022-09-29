package test.dao;

import main.dao.AppointmentDao;
import main.dao.DBConnection;
import main.model.Appointment;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.junit.Assert.*;

public class AppointmentDaoTest {

    private static AppointmentDao testedDao;

    @BeforeClass
    public static void setup() throws Exception{
        System.out.println("startup - creating DB connection to test database");
        DBConnection.makeConnection("jdbc:mysql://localhost:3306/test_client_db", "jdbc", "password1");
        testedDao = new AppointmentDao(DBConnection.getConnection());
        System.out.println("Connection successful");
    }

    @Test
    public void getByID() {
        LocalDateTime expectedStart = LocalDateTime.ofEpochSecond(1590667200L, 0, ZoneOffset.UTC);
        LocalDateTime expectedEnd = LocalDateTime.ofEpochSecond(1590670800L, 0, ZoneOffset.UTC);
        Appointment expectedFirstAppointment =
                new Appointment(
                    1, "title",
                    "description", "location",
                    "Planning Session", expectedStart,
                    expectedEnd, 1,
                    1, 3
                );
        // If Appointment model has valid .equals() override, then any fails should belong to AppointmentDao
        Appointment daoFirstAppointment = testedDao.getByID(1);
        assertEquals(expectedFirstAppointment, daoFirstAppointment);

        // Test database shouldn't have an appointment with id 999999
        assertEquals(null, testedDao.getByID(999999));
    }
    @Test
    public void getAll() {
    }

    @Test
    public void insert() {
        LocalDateTime insertStart = LocalDateTime.ofEpochSecond(0L, 0, ZoneOffset.UTC);
        LocalDateTime insertEnd = LocalDateTime.ofEpochSecond(3600L, 0, ZoneOffset.UTC);
        Appointment expectedSuccess =
                new Appointment(
                        3, "Insert Success Unit Test",
                        "insert Test", "AppointmentDaoTest.java",
                        "Test insert", insertStart,
                        insertEnd, 1,
                        1, 3
                );
        Appointment expectedTimeFailure =
                new Appointment(
                        4, "Insert Time Failure Unit Test",
                        "insert Test", "AppointmentDaoTest.java",
                        "Test failure", null,
                        null, 1,
                        1, 3
                );
        Appointment expectedForeignKeyFailure =
                new Appointment(
                        5, "Insert Foreign Key Failure Unit Test",
                        "Invalid foreign keys", "AppointmentDaoTest.java",
                        "Test failure", insertStart,
                        insertEnd, 0,
                        0, 0
                );
        assertEquals(0, testedDao.insert(expectedSuccess));
        assertEquals(1, testedDao.insert(expectedTimeFailure));
        assertEquals(1, testedDao.insert(expectedForeignKeyFailure));
        assertEquals(1, null);


    }

    @Test
    public void update() {
        //TODO
    }

    @Test
    public void delete() {
        //TODO
    }
}