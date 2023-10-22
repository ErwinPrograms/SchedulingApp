package main.dao;

/**
 * An object that can create, read, update, and delete model data. The user of this interface must be able to
 * modify the database by creating new entries, updating existing entries, and deleting existing entries.
 * @param <T>   Model object that DAO is responsible for
 */
public interface CrudDAO<T> extends ReadDAO<T> {

    /**
     *  Inserts a single object into the database. Fails if model isn't valid.
     *
     * @param   model   A model object with attribute data to be inserted into database
     * @return          Returns 0 if successful, 1 otherwise
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
