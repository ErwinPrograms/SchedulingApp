package main.dao;

import main.model.FirstLevelDivision;
import main.model.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * DAO responsible for read operations on Users.
 */
public class UserDao implements ReadDAO<User> {

    private Connection connDB;

    /**
     * Default constructor which calls DBConnection singleton to get a Connection object
     */
    public UserDao() {
        this.connDB = DBConnection.getConnection();
    }

    /**
     * Constructor which takes a Connection object to use
     * @param connDB    Connection object that DAO will use to access database
     */
    public UserDao(Connection connDB) {
        this.connDB = connDB;
    }


    @Override
    public User getByID(int id) {
        Query queryByID = new Query(connDB,
                "SELECT * FROM users " +
                        "WHERE User_ID = " + id);
        queryByID.executeQuery();
        ResultSet resultCursor = queryByID.getResult();
        try {
            if (resultCursor.next()) {
                return new User(resultCursor);
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        return null;
    }

    @Override
    public ArrayList<User> getAll() {
        ArrayList<User> allUsers = new ArrayList<>();

        Query queryAll = new Query(connDB, "SELECT * FROM users");
        queryAll.executeQuery();
        ResultSet resultCursor = queryAll.getResult();

        try {
            while (resultCursor.next()) {
                allUsers.add(new User(resultCursor));
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }

        return allUsers;
    }

    /**
     * Used in LoginForm to match a string with a User object that has a matching username
     * @param username  username string to search "users" table for
     * @return          A User object with a matching username, or null if no such entry exists or an error occurs.
     */
    public User getByUsername(String username) {
        Query queryByUsername = new Query(connDB,
                "SELECT * FROM users " +
                "WHERE User_Name = '" + username + "'");

        queryByUsername.executeQuery();
        ResultSet resultCursor = queryByUsername.getResult();
        if(resultCursor == null){
            return null;
        }

        try {
            if (resultCursor.next()) {
                return new User(resultCursor);
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        return null;

    }
}
