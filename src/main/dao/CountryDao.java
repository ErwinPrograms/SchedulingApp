package main.dao;

import main.model.Contact;
import main.model.Country;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class CountryDao implements ReadDAO<Country> {

    private Connection connDB;

    public CountryDao() {
        this.connDB = DBConnection.getConnection();
    }

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
