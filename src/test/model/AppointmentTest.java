package test.model;

import main.model.Appointment;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.junit.Assert.*;

public class AppointmentTest {

    @Test
    public void equality() {
        LocalDateTime testStart = LocalDateTime.ofEpochSecond(0L, 0, ZoneOffset.UTC);
        LocalDateTime testEnd = LocalDateTime.ofEpochSecond(3600L, 0, ZoneOffset.UTC);

        Appointment firstEquals =
                new Appointment(
                        3, "Testing Equality",
                        "Testing Equality", "AppointmentTest.java",
                        "Test", testStart,
                        testEnd, 1,
                        1, 1
                );
        Appointment secondEquals =
                new Appointment(
                        3, "Testing Equality",
                        "Testing Equality", "AppointmentTest.java",
                        "Test", testStart,
                        testEnd, 1,
                        1, 1
                );
        Appointment almostEquals =
                new Appointment(
                        4, "Testing Equality",
                        "Testing Equality", "AppointmentTest.java",
                        "Test", testStart,
                        testEnd, 1,
                        1, 1
                );
        assertFalse(firstEquals == secondEquals);
        assertEquals(firstEquals, secondEquals);
        assertNotEquals(firstEquals, almostEquals);
    }
}