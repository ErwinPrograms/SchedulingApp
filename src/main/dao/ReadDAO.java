package main.dao;

import java.util.ArrayList;

/**
 * An object which can read model data. The user of this interface must be able to get a model object from an integer
 * ID, as well as retrieve all records of that model.
 * @param <T> Model object that DAO is responsible for
 */
public interface ReadDAO<T> {
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
}
