package main.dao;

import main.model.Country;
import main.model.FirstLevelDivision;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class FirstLevelDivisionDao implements ReadDAO<FirstLevelDivision> {

    private Connection connDB;

    public FirstLevelDivisionDao() {
        this.connDB = DBConnection.getConnection();
    }

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
}
