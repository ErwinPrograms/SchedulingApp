package main.dao;

import main.model.Appointment;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

public class AppointmentDao implements DAO<Appointment>{

    Connection connDB;

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
                "'" + Timestamp.valueOf(model.getStart()) + "', " +
                "'" + Timestamp.valueOf(model.getEnd()) + "', " +
                model.getCustomerID() + ", " +
                model.getUserID() + ", " +
                model.getContactID() + " )");
        return insert.executeQuery();
    }

    @Override
    public int update(Appointment model) {
        return 0;
    }

    @Override
    public int delete(int id) {
        return 0;
    }
}
