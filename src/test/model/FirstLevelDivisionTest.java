package test.model;

import main.model.Country;
import main.model.FirstLevelDivision;
import org.junit.Test;

import static org.junit.Assert.*;

public class FirstLevelDivisionTest {

    @Test
    public void testEquals() {
        FirstLevelDivision copy1 = new FirstLevelDivision(1, "Napals", 4);
        FirstLevelDivision copy2 = new FirstLevelDivision(1, "Napals", 4);
        FirstLevelDivision different = new FirstLevelDivision(1, "Venice", 4);

        assertEquals(copy1, copy2);
        assertNotEquals(copy1, different);
        assertNotEquals(copy2, different);
    }
}