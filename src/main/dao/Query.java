package main.dao;

import java.sql.*;

/**
 * A helper class that abstracts execution of a query
 */
public class Query {
    private String query;
    private Statement stmt;
    private ResultSet result;
    private Connection conn;

    /**
     * Constructor that requires a connection object and string query.
     * @param conn  Connection object that's connected to database
     * @param query SQL query as a string
     */
    public Query(Connection conn, String query) {
        this.conn = conn;
        this.query = query;
    }

    //TODO: Consider making a prepare query method and implementing PreparedStatement

    /**
     * Executes the stored query using the provided connection in the constructor.
     *
     * @return          A result code
     *                      0: success
     *                      1: error
     */
    public int executeQuery() {
        try {
            stmt = conn.createStatement();
            if(query.toLowerCase().startsWith("select")) {
                result = stmt.executeQuery(query);
                return 0;
            }
            if(query.toLowerCase().startsWith("delete")
            || query.toLowerCase().startsWith("insert")
            || query.toLowerCase().startsWith("update")) {
                int affectedRows = stmt.executeUpdate(query);
                if (affectedRows == 0) {
                    return 1;
                } else {
                    return 0;
                }
            }
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
            return 1;
        }
        return 1;
    }

    /**
     * @return query from object instantiation
     */
    public String getQuery() {
        return query;
    }

    /**
     * After query is executed, the result is stored in variable result
     * @return  result
     */
    public ResultSet getResult() {
        return result;
    }

    /**
     * Closes query
     */
    public void close() {
        try {
            stmt.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
