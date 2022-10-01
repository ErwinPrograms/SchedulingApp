package main.dao;

import main.model.Appointment;
import main.model.Contact;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ContactDao implements DAO<Contact>{

    Connection connDB;

    public ContactDao(Connection connDB) {
        this.connDB = connDB;
    }

    @Override
    public Contact getByID(int id) {
        Query queryByID = new Query(connDB,
                "SELECT * FROM contacts " +
                        "WHERE Contact_ID = " + id);
        queryByID.executeQuery();
        ResultSet resultCursor = queryByID.getResult();
        try {
            if (resultCursor.next()) {
                return new Contact(resultCursor);
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        return null;
    }

    @Override
    public ArrayList<Contact> getAll() {
        return null;
    }

    @Override
    public int insert(Contact model) {
        return 0;
    }

    @Override
    public int update(Contact model) {
        return 0;
    }

    @Override
    public int delete(int id) {
        return 0;
    }
}
