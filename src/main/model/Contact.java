package main.model;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Model class that stores the data about an entry inside the database's "contacts" table.
 * Read only after object instantiation.
 */
public class Contact {

    private int contactID;
    private String contactName;
    private String email;

    //TODO: delete this constructor and migrate logic to ContactDAO
    /**
     * Constructor that takes a ResultSet which is already pointing to a record in the "contacts" table. Fills in all
     * provided fields.
     * @param row   ResultSet object already pointing at a row that can be made into a Contact object
     */
    public Contact (ResultSet row) {
        //TODO: test for result rows with null in any of the rows
        try {
            contactID = row.getInt("Contact_ID");
            contactName = row.getString("Contact_Name");
            email = row.getString("Email");
        } catch (SQLException ex) {
            System.out.println("Error with SQL " + ex.getMessage());
        }
    }

    /**
     * Constructor where all instance variables are being set by the parameters
     * @param contactID     ID of contact inside the database
     * @param contactName   full name of contact
     * @param email         email address of contact
     */
    public Contact(int contactID, String contactName, String email) {
        this.contactID = contactID;
        this.contactName = contactName;
        this.email = email;
    }

    /**
     * This method looks through all the instance variables and makes sure a value exists for each.
     * If one has no value or an invalid value, then it returns false.
     *
     * @return      True if every instance variable has a value. False otherwise.
     */
    public boolean hasRequiredData() {
        if (contactID <= 0)
            return false;
        if (contactName == null)
            return false;
        if (email == null)
            return false;

        return true;
    }

    /**
     * An override for the Object.equals() method. It checks all instance variables and makes sure other object is
     * Contact and shares the same values for every instance variable.
     * @param obj   The object being checked for equality
     * @return      True if other object is of type Contact and matches all instance variables. False otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Contact)) {
            return false;
        }

        Contact other = (Contact) obj;
        if (contactID != other.getContactID())
            return false;
        if (!contactName.equals(other.getContactName()))
            return false;
        if (!email.equals(other.getEmail()))
            return false;

        return true;
    }

    public int getContactID() {
        return contactID;
    }

    public String getContactName() {
        return contactName;
    }

    public String getEmail() {
        return email;
    }

    /**
     * An override for the Object.toString() method. Only displays contactName since that's the most important
     * information to be displayed in the GUI
     * @return  contactName instance variable
     */
    @Override
    public String toString() {
        return contactName;
    }
}
