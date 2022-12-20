package test.utility;

import org.junit.Test;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

import static org.junit.Assert.*;

public class LocalDateTimeTest {

    @Test
    public void equality() {
        LocalDateTime firstZero = LocalDateTime.ofEpochSecond(0L, 0, ZoneOffset.UTC);
        LocalDateTime firstHour = LocalDateTime.ofEpochSecond(3600L, 0, ZoneOffset.UTC);
        LocalDateTime secondZero = LocalDateTime.ofEpochSecond(0L, 0, ZoneOffset.UTC);
        LocalDateTime secondHour = LocalDateTime.ofEpochSecond(3600L, 0, ZoneOffset.UTC);

        assertEquals(firstHour, secondHour);
        assertEquals(firstZero, secondZero);

        ZoneId euro = ZoneId.of("Europe/Paris");
        ZoneId utc = ZoneId.of("UTC");
        System.out.println(Timestamp.valueOf(LocalDateTime.now()));
        System.out.println(Timestamp.valueOf(LocalDateTime.now(euro)));
        System.out.println(Timestamp.valueOf(LocalDateTime.now(utc)));
        System.out.println(Timestamp.valueOf(LocalDateTime.now()).toInstant());
        System.out.println(Instant.now());
    }
}
