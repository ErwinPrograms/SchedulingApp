package main.dao;

/**
 * DAO responsible for gathering aggregate data to be used in ReportForm. Because
 * of that, this DAO does not implement the CrudDAO or ReadDAO interfaces.
 * Can gather the following data from the database:
 * - Total number of customers appointments by type and month
 * - A schedule for each contact which include appointmentID, title, type and
 *   description, start date and time, end date and time, and customerID
 * - Total number of appointments in each division
 */
public class ReportDao {

    /*
    SQL query for number of customer appointments by type and month
    SELECT
        MONTH(Start) AS month,
        YEAR(Start) AS year,
        Type,
        COUNT(*) AS count
    FROM
        appointments
    GROUP BY
        YEAR(Start),
        MONTH(Start),
        Type
    ORDER BY
        year DESC,
        month ASC,
        count;
     */

    /*
    SQL query for appointment division count report.
    SELECT
        customers.Division_ID as Division_ID,
        COUNT(*) as num_appointments
    FROM
        appointments
    LEFT JOIN customers
        ON appointments.Customer_ID = customers.Customer_ID
    GROUP BY
        customers.Division_ID;
     */
}
