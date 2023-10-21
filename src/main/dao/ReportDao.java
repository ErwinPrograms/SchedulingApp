package main.dao;

import main.model.Appointment;
import main.model.DivisionCount;
import main.model.MonthTypeCount;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * DAO responsible for gathering aggregate data to be used in ReportForm. Because
 * of that, this DAO does not implement the CrudDAO or ReadDAO interfaces.
 * Can gather the following data from the database:<br>
 * - Total number of customers appointments by type and month <br>
 * - Total number of appointments in each division <br>
 */
public class ReportDao {
    Connection connDB;

    public ReportDao() {
        this.connDB = DBConnection.getConnection();
    }

    public ReportDao(Connection connDB) {
        this.connDB = connDB;
    }

    /**
     * Queries the database and assembles an ArrayList that holds data on different
     * aggregated counts of different types of appointments in each month.
     * The order is arbitrary but follows reverse chronological order, then by descending amount.
     *
     * @return ArrayList<MonthTypeCount> object that holds
     * all aggregated appointment data ordered by recency and amount
     */
    public ArrayList<MonthTypeCount> getMonthTypeCountReport() {
        Query monthTypeCountQuery = new Query(connDB,
                "SELECT " +
                        "MONTHNAME(Start) AS month, " +
                        "YEAR(start) AS year, " +
                        "Type, " +
                        "COUNT(*) AS count " +
                    "FROM " +
                        "appointments " +
                    "GROUP BY " +
                        "YEAR(Start), " +
                        "MONTH(Start), " +
                        "Type " +
                    "ORDER BY " +
                        "year DESC," +
                        "month DESC, " +
                        "count DESC;"
        );

        monthTypeCountQuery.executeQuery();
        ResultSet resultCursor = monthTypeCountQuery.getResult();

        try {
            ArrayList<MonthTypeCount> report = new ArrayList<>();
            while(resultCursor.next()) {
                report.add(new MonthTypeCount(
                        resultCursor.getString("month") + " " +  resultCursor.getString("year"),
                        resultCursor.getString("type"),
                        resultCursor.getInt("count")
                ));
            }

            return report;
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        return null;
    }

    /**
     * Queries the database and assembles an ArrayList that holds data on all division names and a count of the number
     * of appointments in each division.
     * This is intended to show which divisions are the most active for the business.
     *
     * @return  ArrayList<DivisionCount> object that holds paired data of Division name and number
     * of appointments in that division.
     */
    public ArrayList<DivisionCount> getDivisionCountReport() {
        Query divisionCountQuery = new Query(connDB,
                "SELECT " +
                            "first_level_divisions.Division AS Division, " +
                            "COUNT(*) as Num_Appointments " +
                        "FROM " +
                            "customers " +
                        "RIGHT JOIN appointments " +
                            "ON customers.Customer_ID = appointments.Customer_ID " +
                        "LEFT JOIN first_level_divisions " +
                            "ON customers.Division_ID = first_level_divisions.Division_ID " +
                        "GROUP BY " +
                            "customers.Division_ID;");
        divisionCountQuery.executeQuery();
        ResultSet resultCursor = divisionCountQuery.getResult();

        try {
            ArrayList<DivisionCount> report = new ArrayList<>();
            while(resultCursor.next()) {
                report.add(new DivisionCount(
                        resultCursor.getString("Division"),
                        resultCursor.getInt("Num_Appointments")
                ));
            }
            return report;
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }

        return null;
    }
}
