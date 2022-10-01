package test.model;

import main.model.Contact;
import org.junit.Test;

import static org.junit.Assert.*;

public class ContactTest {

    @Test
    public void testEquals() {
        Contact copy1 = new Contact(1, "copy", "c@c.com");
        Contact copy2 = new Contact(1, "copy", "c@c.com");
        Contact different = new Contact(1, "different", "c@c.com");

        assertEquals(copy1, copy2);
        assertNotEquals(copy1, different);
        assertNotEquals(copy2, different);
    }
}