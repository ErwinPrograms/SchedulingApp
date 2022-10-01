package main.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Contact {

    private int contactID;
    private String contactName;
    private String email;

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
}
