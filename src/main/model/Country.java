package main.model;

import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Model class that stores data about an entry inside the database's "countries" table.
 * Read only after object instantiation.
 */
public class Country {

    private int countryID;
    private String country;

    //TODO: delete this constructor and migrate logic to CountryDAO
    /**
     * Constructor that takes a ResultSet which is already pointing to a record in the "countries" table. Fills in all
     * provided fields.
     *
     * @param row   ResultSet object already pointing at a row that can be made into a Country object
     */
    public Country (ResultSet row) {
        //TODO: test for result rows with null in any of the rows
        try {
            countryID = row.getInt("Country_ID");
            country = row.getString("Country");
        } catch (SQLException ex) {
            System.out.println("Error with SQL " + ex.getMessage());
        }
    }

    /**
     * Constructor where all instance variables are being set by the parameters
     * @param countryID     ID for this record
     * @param country       name of the country
     */
    public Country(int countryID, String country) {
        this.countryID = countryID;
        this.country = country;
    }

    /**
     * An override for the Object.equals() method. It checks all instance variables and makes sure other object is
     * Country and shares the same values for every instance variable.
     * @param obj   The object being checked for equality
     * @return      True if other object is of type Country and matches all instance variables. False otherwise.
     */
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
