package main.dao;

import main.model.Appointment;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        return null;
    }

    @Override
    public int insert(Appointment model) {
        return 0;
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
