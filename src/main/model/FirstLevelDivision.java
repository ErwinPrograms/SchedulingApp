package main.model;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Model class that stores data about an entry inside the database's "first_level_divisions" table.
 * Read only after object instantiation.
 */
public class FirstLevelDivision {

    private int divisionID;
    private String division;
    private int countryID;

    //TODO: delete this constructor and migrate logic to CustomerDAO
    /**
     * Constructor that takes a ResultSet which is already pointing to a record in the "first_level_divisions" table. Fills in all
     * provided fields.
     * @param row   ResultSet object already pointing at a row that can be made into a FirstLevelDivision object
     */
    public FirstLevelDivision(ResultSet row) {
        //TODO: test for result rows with null in any of the rows
        try {
            divisionID = row.getInt("Division_ID");
            division = row.getString("Division");
            countryID = row.getInt("Country_ID");
        } catch (SQLException ex) {
            System.out.println("Error with SQL " + ex.getMessage());
        }
    }

    /**
     * Constructor where all instance variables are being set by the parameters
     * @param divisionID    ID of division inside of database
     * @param division      name of division
     * @param countryID     ID of country that this division belongs to
     */
    public FirstLevelDivision(int divisionID, String division, int countryID) {
        this.divisionID = divisionID;
        this.division = division;
        this.countryID = countryID;
    }


    /**
     * An override for the Object.equals() method. It checks all instance variables and makes sure other object is
     * FirstLevelDivision and shares the same values for every instance variable.
     * @param obj   The object being checked for equality
     * @return      True if other object is of type FirstLevelDivision and matches all instance variables. False otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof FirstLevelDivision)) {
            return false;
        }

        FirstLevelDivision other = (FirstLevelDivision) obj;
        if (divisionID != other.getDivisionID())
            return false;
        if (!division.equals(other.getDivision()))
            return false;
        if (countryID != other.getCountryID())
            return false;

        return true;
    }

    /**
     * @return divisionID
     */
    public int getDivisionID() {
        return divisionID;
    }

    /**
     * @return division
     */
    public String getDivision() {
        return division;
    }

    /**
     * @return countryID
     */
    public int getCountryID() {
        return countryID;
    }

}
