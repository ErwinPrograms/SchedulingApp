package main.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class FirstLevelDivision {

    private int divisionID;
    private String division;
    private int countryID;

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

    public FirstLevelDivision(int divisionID, String division, int countryID) {
        this.divisionID = divisionID;
        this.division = division;
        this.countryID = countryID;
    }

    public FirstLevelDivision(int divisionID, String division,
                              int countryID,
                              LocalDateTime createDate, String createdBy,
                              LocalDateTime lastUpdate, String lastUpdateBy) {
        this.divisionID = divisionID;
        this.division = division;
        this.countryID = countryID;
    }

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

    public int getDivisionID() {
        return divisionID;
    }

    public String getDivision() {
        return division;
    }

    public int getCountryID() {
        return countryID;
    }

}
