package test.model;

import main.model.Country;
import org.junit.Test;

import static org.junit.Assert.*;

public class CountryTest {

    @Test
    public void testEquals() {
        Country copy1 = new Country(1, "Rome");
        Country copy2 = new Country(1, "Rome");
        Country different = new Country(1, "Romania");

        assertEquals(copy1, copy2);
        assertNotEquals(copy1, different);
        assertNotEquals(copy2, different);
    }
}