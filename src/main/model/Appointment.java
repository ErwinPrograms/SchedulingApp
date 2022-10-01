package main.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class Appointment {
    private int appointmentID;
    private String title;
    private String description;
    private String location;
    private String type;
    private LocalDateTime start;
    private LocalDateTime end;
    private int customerID;
    private int userID;
    private int contactID;

    /**
     * Constructor that takes a ResultSet which is already pointing to a record in the Appointment table. Fills in all
     * provided fields.
     *
     * @param row   ResultSet object already pointing at a row that can be made into an Appointment object
     */
    public Appointment(ResultSet row){
        //TODO: test for result rows with null in any of the rows
        try {
            appointmentID = row.getInt("Appointment_ID");
            title = row.getString("Title");
            description = row.getString("Description");
            location = row.getString("Location");
            type = row.getString("Type");
            start = row.getTimestamp("Start").toLocalDateTime();
            end = row.getTimestamp("End").toLocalDateTime();
            customerID = row.getInt("Customer_ID");
            userID = row.getInt("User_ID");
            contactID = row.getInt("Contact_ID");
        } catch (SQLException ex) {
            System.out.println("Error with SQL " + ex.getMessage());
        }
    }

    public Appointment(int appointmentID, String title,
                       String description, String location,
                       String type, LocalDateTime start,
                       LocalDateTime end, int customerID,
                       int userID, int contactID) {
        this.appointmentID = appointmentID;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = start;
        this.end = end;
        this.customerID = customerID;
        this.userID = userID;
        this.contactID = contactID;
    }

    /**
     * This method looks through all the instance variables and makes sure a value exists for each.
     * If one has no value or an invalid value, then it returns false.
     *
     * @return      True if every instance variable has a value. False otherwise.
     */
    public boolean hasRequiredData() {
        if (appointmentID <= 0)
            return false;
        if (title == null)
            return false;
        if (description == null)
            return false;
        if (location == null)
            return false;
        if (type == null)
            return false;
        if (start == null)
            return false;
        if (end == null)
            return false;
        if (customerID <= 0)
            return false;
        if (userID <= 0)
            return false;
        if (contactID <= 0)
            return false;

        return true;
    }

    /**
     * An override for the Object.equals() method. It checks all instance variables and makes sure other object is
     * Appointment and shares the same values for every instance variable.
     *
     * @param obj
     * @return      True if other object is of type Appointment and matches all instance variables. False otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Appointment)) {
            return false;
        }

        Appointment other = (Appointment) obj;
        if (appointmentID != other.getAppointmentID())
            return false;
        if (!title.equals(other.getTitle()))
            return false;
        if (!description.equals(other.getDescription()))
            return false;
        if (!location.equals(other.getLocation()))
            return false;
        if (!type.equals(other.getType()))
            return false;
        if (customerID != other.getCustomerID())
            return false;
        if (userID != other.getUserID())
            return false;
        if (contactID != other.getContactID())
            return false;

        //Check equality by checking if both are null or not null
        if ((start == null) != (other.getStart() == null))
            return false;
        //Then, if start isn't null, check the other attribute to see if equal
        if (start != null && !start.equals(other.getStart()))
            return false;
        //same as start
        if ((end == null) != (other.getEnd() == null))
            return false;
        if (end != null && !end.equals(other.getEnd()))
            return false;

        return true;
    }

    public int getAppointmentID() {
        return appointmentID;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public String getType() {
        return type;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public int getCustomerID() {
        return customerID;
    }

    public int getUserID() {
        return userID;
    }

    public int getContactID() {
        return contactID;
    }

}
