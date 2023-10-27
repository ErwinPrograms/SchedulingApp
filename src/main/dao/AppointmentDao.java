package main.dao;

import main.model.Appointment;
import main.utility.TimeUtility;

import java.sql.*;
import java.util.ArrayList;

/**
 * DAO responsible for CRUD operations on Appointments.
 */
public class AppointmentDao implements CrudDAO<Appointment> {

    Connection connDB;

    /**
     * Default constructor which calls DBConnection singleton to get a Connection object
     */
    public AppointmentDao() {
        this.connDB = DBConnection.getConnection();
    }

    /**
     * Constructor which takes a Connection object to use
     * @param connDB    Connection object that DAO will use to access database
     */
    public AppointmentDao(Connection connDB) {
        this.connDB = connDB;
    }

    @Override
    public Appointment getByID(int id) {
        Query queryByID = new Query(connDB,
                                "SELECT * FROM appointments " +
                                "WHERE Appointment_ID = " + id);
        queryByID.executeQuery();
        ResultSet resultCursor = queryByID.getResult();
        try {
            if (resultCursor.next()) {
                return new Appointment(resultCursor);
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        return null;
    }

    @Override
    public ArrayList<Appointment> getAll() {
        ArrayList<Appointment> allAppointments = new ArrayList<>();

        Query queryAll = new Query(connDB, "SELECT * FROM appointments");
        queryAll.executeQuery();
        ResultSet resultCursor = queryAll.getResult();

        try {
            while (resultCursor.next()) {
                allAppointments.add(new Appointment(resultCursor));
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }

        return allAppointments;
    }

    @Override
    public int insert(Appointment model) {
        // TODO modify query to be more readable
        // TODO add creation/update metadata

        //Check through model if all required data exists (does not validate)
        if (!model.hasRequiredData())
            return 1;

        Query insert = new Query(connDB, "INSERT INTO appointments " +
                "(Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID) " +
                "VALUES ( '" + model.getTitle() + "', " +
                "'" + model.getDescription() + "', " +
                "'" + model.getLocation() + "', " +
                "'" + model.getType() + "', " +
                "'" + new TimeUtility().getUTCTimestampFromLocalDateTime(model.getStart()) + "', " +
                "'" + new TimeUtility().getUTCTimestampFromLocalDateTime(model.getEnd()) + "', " +
                model.getCustomerID() + ", " +
                model.getUserID() + ", " +
                model.getContactID() + " )");
        return insert.executeQuery();
    }

    @Override
    public int update(Appointment model) {

        if (!model.hasRequiredData())
            return 1;

        try {
            PreparedStatement update = connDB.prepareStatement(
                    "UPDATE appointments " +
                            "SET Appointment_ID = ?, " +
                            "Title = ?, " +
                            "Description = ?, " +
                            "Location = ?, " +
                            "Type = ?, " +
                            "Start = ?, " +
                            "End = ?, " +
                            "Customer_ID = ?, " +
                            "User_ID = ?, " +
                            "Contact_ID = ? " +
                            "WHERE Appointment_ID = ?");
            update.setInt(1, model.getAppointmentID());
            update.setString(2, model.getTitle());
            update.setString(3, model.getDescription());
            update.setString(4, model.getLocation());
            update.setString(5, model.getType());
            update.setTimestamp(6, new TimeUtility().getUTCTimestampFromLocalDateTime(model.getStart()));
            update.setTimestamp(7, new TimeUtility().getUTCTimestampFromLocalDateTime(model.getEnd()));
            update.setInt(8, model.getCustomerID());
            update.setInt(9, model.getUserID());
            update.setInt(10, model.getContactID());
            update.setInt(11, model.getAppointmentID());

            int affectedRows = update.executeUpdate();
            if (affectedRows == 0) {
                return 1;
            } else {
                return 0;
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        return 1;
    }

    @Override
    public int delete(int id) {
        //Danger for SQL injection
        Query deleteQuery = new Query(connDB, "DELETE FROM appointments WHERE Appointment_ID = " + id);
        return deleteQuery.executeQuery();
    }
}
