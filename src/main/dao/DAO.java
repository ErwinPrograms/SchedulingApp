package main.dao;

import java.sql.SQLException;
import java.util.ArrayList;

public interface DAO<T> {

    /**
     * Returns a single object of the DAO's datatype which contains the information from a particular
     * row defined by the unique key ID.
     *
     * @param   id  The id primary key of any particular model
     * @return      The model datatype with attributes filled. Null if object isn't found
     */
    public T getByID(int id);


    /**
     * Returns an ArrayList of the model type. ArrayList can be easily converted to ObservableList if needed for
     * view or main.controller.
     *
     * @return      An ArrayList with the model type
     */
    public ArrayList<T> getAll();

    /**
     *  Inserts a single object into the database. Fails if model isn't valid.
     *
     * @param   model   A model object with attribute data to be inserted into database
     * @return          A result code
     *                      0: success
     *                      1: error
     */
    public int insert(T model);

    /**
     * Takes in an instance of the model object and updates the table based on the id. This method will fail if
     * the id isn't already in the database as there is nothing to update.
     *
     * @param   model   A model object with attribute data to be updated into database
     * @return          A result code
     *                     0: success
     *                     1: error
     */
    public int update(T model);

    /**
     * Takes an integer id and attempts to remove a matching record from the database. Fails if id is not found.
     *
     * @param   id      The id of the record to be deleted
     * @return          A result code
     *                      0: success
     *                      1: error
     */
    public int delete(int id);
}
