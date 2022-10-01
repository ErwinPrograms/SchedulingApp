package main.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Objects;

public class User {

    private int userID;
    private String userName;
    //No encryption?
    private String password;

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

    public User(int userID, String userName, String password) {
        this.userID = userID;
        this.userName = userName;
        this.password = password;
    }


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
