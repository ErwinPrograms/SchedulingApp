package main.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import main.model.Country;
import main.model.Customer;
import main.model.FirstLevelDivision;


import java.net.URL;
import java.util.ResourceBundle;

public class CustomerFormController implements Initializable {


    @FXML
    TableView<Customer> customerTable;

    @FXML
    TextField customerIDField;

    @FXML
    TextField customerNameField;

    @FXML
    TextField phoneField;

    @FXML
    TextField addressField;

    @FXML
    TextField postalCodeField;

    @FXML
    ComboBox<Country> countryBox;

    @FXML
    ComboBox<FirstLevelDivision> divisionBox;

    @FXML
    Button clearButton;

    @FXML
    Button addButton;

    @FXML
    Button updateButton;

    @FXML
    Button deleteButton;

    //TODO: Add elements and events that allow to switch between forms
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    /**
     * A private helper function populates columns and rows based
     * on a call to the database.
     */
    private void populateCustomerTable() {

    }

    /**
     * When a row in the TableView is clicked, add the customer information
     * to the input fields.
     *
     * Disables Add Customer button until any other button is selected.
     */
    public void populateCustomerToForm() {

    }

    /**
     * Clears all text from input fields in view.
     *
     * Disables clearButton, updateButton and deleteButton.
     */
    public void clearForm() {

    }

    /**
     * Attempts to add customer into database and maintains form on success.
     * This allows for multiple customers to be added in quick succession
     * if they share multiple fields (e.g. division and postal code)
     *
     * Success: a new record is added to customers table in database and
     * the TableView is updated to display the most recently added record.
     *
     * Failure: an alert is shown on screen describing the reason for failure.
     * No new customer is added to database and TableView is not refreshed.
     */
    public void addCustomer() {

    }

    /**
     * Attempts to update an existing customer based on user input from fields in view
     * and resets the form on success.
     *
     * Success: The selected customer has fields updated and the change is
     * reflected in the TableView. Then calls clearForm().
     *
     * Failure: an alert is shown on screen describing reason for failure.
     * The update does not occur in the database.
     */
    public void updateCustomer() {

    }

    /**
     * Attempts to delete an existing customer from the database and resets
     * the form on success.
     *
     * Success: The selected customer is removed from database and the
     * change is reflected in the TableView. Then calls clearForm().
     *
     * Failure: an alter is shown on screen describing reason for failure and
     * the update does not occur in the database.
     */
    public void deleteCustomer() {

    }

}
