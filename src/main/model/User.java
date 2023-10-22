package main.model;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Model class that stores data about an entry inside the database's "users" table.
 * Read only after object instantiation.
 */
public class User {

    private int userID;
    private String userName;
    //No encryption?
    private String password;

    //TODO: delete this constructor and migrate logic to UserDAO
    /**
     * Constructor that takes a ResultSet which is already pointing to a record in the "users" table. Fills in all
     * provided fields.
     * @param row   ResultSet object already pointing at a row that can be made into a User object
     */
    public User(ResultSet row) {
        //TODO: test for result rows with null in any of the rows
        try {
            userID = row.getInt("User_ID");
            userName = row.getString("User_Name");
            password = row.getString("Password");
        } catch (SQLException ex) {
            System.out.println("Error with SQL " + ex.getMessage());
        }
    }

    /**
     * Constructor where all instance variables are being set by the parameters
     * @param userID    ID of user
     * @param userName  name of user
     * @param password  password for user
     */
    public User(int userID, String userName, String password) {
        this.userID = userID;
        this.userName = userName;
        this.password = password;
    }


    /**
     * An override for the Object.equals() method. It checks all instance variables and makes sure other object is
     * User and shares the same values for every instance variable.
     * @param obj   The object being checked for equality
     * @return      True if other object is of type User and matches all instance variables. False otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof User)) {
            return false;
        }

        User other = (User) obj;
        if (userID != other.getUserID())
            return false;
        if (!userName.equals(other.getUserName()))
            return false;
        if (!password.equals(other.getPassword()))
            return false;

        return true;
    }

    public int getUserID() {
        return userID;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

}
