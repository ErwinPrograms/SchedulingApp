package main.model;

import main.utility.TimeUtility;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * A model class to store the data of an entry inside the database's "appointments" table.
 * Read only after object instantiation.
 */
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

    //TODO: delete this constructor and migrate logic to AppointmentDAO
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
            //TODO: change start and end to convert ResultSet
            start = new TimeUtility().getLocalDateTimeFromUTCTimestamp(row.getTimestamp("Start"));
            end = new TimeUtility().getLocalDateTimeFromUTCTimestamp(row.getTimestamp("End"));
            customerID = row.getInt("Customer_ID");
            userID = row.getInt("User_ID");
            contactID = row.getInt("Contact_ID");
        } catch (SQLException ex) {
            System.out.println("Error with SQL " + ex.getMessage());
        }
    }

    /**
     * Constructor for Appointment. All member variables are being set.
     *
     * @param appointmentID id of appointment in database
     * @param title title of appointment
     * @param description description of appointment
     * @param location location of appointment
     * @param type type of appointment
     * @param start start time of appointment
     * @param end end time of appointment
     * @param customerID id of customer the appointment is associated with
     * @param userID id of user the appointment is associated with
     * @param contactID id of contact the appointment is associated with
     */
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
     * @param obj   The object being checked for equality
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

    /**
     * @return appointmentID
     */
    public int getAppointmentID() {
        return appointmentID;
    }

    /**
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @return type
     */
    public String getType() {
        return type;
    }

    /**
     * @return start
     */
    public LocalDateTime getStart() {
        return start;
    }

    /**
     * @return end
     */
    public LocalDateTime getEnd() {
        return end;
    }

    /**
     * @return customerID
     */
    public int getCustomerID() {
        return customerID;
    }

    /**
     * @return userID
     */
    public int getUserID() {
        return userID;
    }

    /**
     * @return contactID
     */
    public int getContactID() {
        return contactID;
    }

    /**
     * Formats into a String the most important parts of an appointment: appointmentID, and start.
     * @return Formatted String with appointmentID and start date and time.
     */
    @Override
    public String toString() {
        return String.format(
                "AppointmentID: %d%n" +
                "Date: %tF%n" +
                "Time:%tT%n",
                appointmentID, start.toLocalDate(), start.toLocalTime());
    }
}
