package main.dao;

import main.model.Contact;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * DAO responsible for CRUD operations on Contacts.
 */
public class ContactDao implements CrudDAO<Contact> {

    Connection connDB;

    /**
     * Default constructor which calls DBConnection singleton to get a Connection object
     */
    public ContactDao() {
        this.connDB = DBConnection.getConnection();
    }

    /**
     * Constructor which takes a Connection object to use
     * @param connDB    Connection object that DAO will use to access database
     */
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
        ArrayList<Contact> allContacts = new ArrayList<>();

        Query queryAll = new Query(connDB, "SELECT * FROM contacts");
        queryAll.executeQuery();
        ResultSet resultCursor = queryAll.getResult();

        try {
            while (resultCursor.next()) {
                allContacts.add(new Contact(resultCursor));
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }

        return allContacts;
    }

    @Override
    public int insert(Contact model) {
        if (!model.hasRequiredData())
            return 1;

        Query insert = new Query(connDB, "INSERT INTO contacts " +
                "(Contact_ID, Contact_Name, Email) " +
                "VALUES ( " +
                model.getContactID() + ", " +
                "'" + model.getContactName() + "', " +
                "'" + model.getEmail() + "'" +
                " )");

        return insert.executeQuery();
    }

    @Override
    public int update(Contact model) {
        if (!model.hasRequiredData())
            return 1;

        Query update = new Query(connDB, "UPDATE contacts " +
                "SET Contact_ID = " + model.getContactID() + ", " +
                "Contact_Name = '" + model.getContactName() + "', " +
                "Email = '" + model.getEmail() + "' " +
                "WHERE Contact_ID = " + model.getContactID());

        return update.executeQuery();
    }

    @Override
    public int delete(int id) {
        Query delete = new Query(connDB, "DELETE FROM contacts " +
                "WHERE Contact_ID = " + id);
        return delete.executeQuery();
    }
}
