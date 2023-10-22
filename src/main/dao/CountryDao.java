package main.dao;

import main.model.Contact;
import main.model.Country;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


/**
 * DAO responsible for read operations on Countries
 */
public class CountryDao implements ReadDAO<Country> {

    private Connection connDB;


    /**
     * Default constructor which calls DBConnection singleton to get a Connection object
     */
    public CountryDao() {
        this.connDB = DBConnection.getConnection();
    }

    /**
     * Constructor which takes a Connection object to use
     * @param connDB    Connection object that DAO will use to access database
     */
    public CountryDao(Connection connDB) {
        this.connDB = connDB;
    }

    @Override
    public Country getByID(int id) {
        Query queryByID = new Query(connDB,
                "SELECT * FROM countries " +
                        "WHERE Country_ID = " + id);
        queryByID.executeQuery();
        ResultSet resultCursor = queryByID.getResult();
        try {
            if (resultCursor.next()) {
                return new Country(resultCursor);
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        return null;
    }

    @Override
    public ArrayList<Country> getAll() {
        ArrayList<Country> allCountries = new ArrayList<>();

        Query queryAll = new Query(connDB, "SELECT * FROM countries");
        queryAll.executeQuery();
        ResultSet resultCursor = queryAll.getResult();

        try {
            while (resultCursor.next()) {
                allCountries.add(new Country(resultCursor));
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }

        return allCountries;
    }
}
