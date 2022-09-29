package main.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Query {
    private String query;
    private Statement stmt;
    private ResultSet result;
    private Connection conn;

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
                stmt.executeUpdate(query);
                System.out.println("Finished update");
                return 0;
            }
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
            return 1;
        }
        return 1;
    }

    public String getQuery() {
        return query;
    }

    public ResultSet getResult() {
        return result;
    }

    public void close() {
        try {
            stmt.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
