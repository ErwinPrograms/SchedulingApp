package main.dao;

import main.model.Country;
import main.model.FirstLevelDivision;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * DAO responsible for read operations on FirstLevelDivisions.
 */
public class FirstLevelDivisionDao implements ReadDAO<FirstLevelDivision> {

    private Connection connDB;

    /**
     * Default constructor which calls DBConnection singleton to get a Connection object
     */
    public FirstLevelDivisionDao() {
        this.connDB = DBConnection.getConnection();
    }

    /**
     * Constructor which takes a Connection object to use
     * @param connDB    Connection object that DAO will use to access database
     */
    public FirstLevelDivisionDao(Connection connDB) {
        this.connDB = connDB;
    }

    @Override
    public FirstLevelDivision getByID(int id) {
        Query queryByID = new Query(connDB,
                "SELECT * FROM first_level_divisions " +
                        "WHERE Division_ID = " + id);
        queryByID.executeQuery();
        ResultSet resultCursor = queryByID.getResult();
        try {
            if (resultCursor.next()) {
                return new FirstLevelDivision(resultCursor);
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        return null;
    }

    @Override
    public ArrayList<FirstLevelDivision> getAll() {
        ArrayList<FirstLevelDivision> allDivisions = new ArrayList<>();

        Query queryAll = new Query(connDB, "SELECT * FROM first_level_divisions");
        queryAll.executeQuery();
        ResultSet resultCursor = queryAll.getResult();

        try {
            while (resultCursor.next()) {
                allDivisions.add(new FirstLevelDivision(resultCursor));
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }

        return allDivisions;
    }

    /**
     * Retrieves divisionID using both countryName and divisionName. Every ID should be accessible,
     * @param countryName   name of country for query
     * @param divisionName  name of division for query
     * @return              ID of corresponding division or -1 if such combination doesn't exist.
     */
    public int getDivisionID(String countryName, String divisionName) {
        Query getWithStrings = new Query(connDB,
                "SELECT Division_ID " +
                "FROM first_level_divisions " +
                "INNER JOIN countries " +
                    "ON first_level_divisions.Country_ID = countries.Country_ID " +
                "WHERE Division = '" + divisionName + "' " +
                "AND Country = '" + countryName + "'" );
        getWithStrings.executeQuery();
        ResultSet resultCursor = getWithStrings.getResult();

        try {
            if (resultCursor.next()){
                return resultCursor.getInt("Division_ID");
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }

        // failure result
        return -1;
    }
}
