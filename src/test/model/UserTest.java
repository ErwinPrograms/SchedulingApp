package test.model;

import main.model.Country;
import main.model.User;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserTest {

    @Test
    public void testEquals() {
        User copy1 = new User(1, "host", "host");
        User copy2 = new User(1, "host", "host");
        User different = new User(1, "host", "most");

        assertEquals(copy1, copy2);
        assertNotEquals(copy1, different);
        assertNotEquals(copy2, different);
    }
}