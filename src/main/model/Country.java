package main.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Country {

    private int countryID;
    private String country;

    public Country (ResultSet row) {
        //TODO: test for result rows with null in any of the rows
        try {
            countryID = row.getInt("Country_ID");
            country = row.getString("Country");
        } catch (SQLException ex) {
            System.out.println("Error with SQL " + ex.getMessage());
        }
    }
    public Country(int countryID, String country) {
        this.countryID = countryID;
        this.country = country;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Country)) {
            return false;
        }

        Country other = (Country) obj;
        if (countryID != other.getCountryID())
            return false;
        if (!country.equals(other.getCountry()))
            return false;

        return true;
    }

    public int getCountryID() {
        return countryID;
    }

    public String getCountry() {
        return country;
    }

}
