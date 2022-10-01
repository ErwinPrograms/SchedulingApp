package test.model;

import main.model.Customer;
import org.junit.Test;

import static org.junit.Assert.*;

public class CustomerTest {

    @Test
    public void testEquals() {
        Customer copy1 = new Customer(1, "Bob", "123 St", "00000", "555-5555", 1);
        Customer copy2 = new Customer(1, "Bob", "123 St", "00000", "555-5555", 1);
        Customer different = new Customer(1, "Jim", "123 St", "00000", "555-5555", 1);

        assertEquals(copy1, copy2);
        assertNotEquals(copy1, different);
        assertNotEquals(copy2, different);
    }
}