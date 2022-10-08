package main.controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import main.model.Country;
import main.model.Customer;
import main.model.FirstLevelDivision;
import main.utility.DataHandlingFacade;


import java.net.URL;
import java.util.*;

public class CustomerFormController implements Initializable {


    @FXML
    TableView<Customer> customerTable;
    @FXML
    TableColumn<Customer, Integer> idColumn;
    @FXML
    TableColumn<Customer, String> nameColumn;
    @FXML
    TableColumn<Customer, String> phoneColumn;
    @FXML
    TableColumn<Customer, Integer> divisionColumn;
    @FXML
    TableColumn<Customer, String> addressColumn;
    @FXML
    TableColumn<Customer, String> postalColumn;

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
    ComboBox<String> countryBox;

    @FXML
    ComboBox<String> divisionBox;

    @FXML
    Button clearButton;

    @FXML
    Button addButton;

    @FXML
    Button updateButton;

    @FXML
    Button deleteButton;

    private final DataHandlingFacade dataHandler = new DataHandlingFacade();

    //TODO: Add elements and events that allow to switch between forms
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populateCustomerTable();
        activateInsertionButtons();

        ObservableList<String> countryNames = dataHandler.countriesObservableList();
        countryBox.setItems(countryNames);
    }

    /**
     * A private helper function populates columns and rows based
     * on a call to the database.
     */
    private void populateCustomerTable() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        divisionColumn.setCellValueFactory(new PropertyValueFactory<>("divisionID"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        postalColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));

        ObservableList<Customer> allCustomersObservable = dataHandler.customersObservableList();
        customerTable.setItems(allCustomersObservable);
    }

    private void activateSelectionButtons() {
        clearButton.setDisable(false);
        addButton.setDisable(true);
        updateButton.setDisable(false);
        deleteButton.setDisable(false);
    }

    private void activateInsertionButtons() {
        clearButton.setDisable(true);
        addButton.setDisable(false);
        updateButton.setDisable(true);
        deleteButton.setDisable(true);
    }

    /**
     * Runs when countryBox is changed and updates the contents of divisionBox
     * to match the selected country.
     */
    public void updateDivisionBox() {
        String selectedCountryName = countryBox.getValue();
        divisionBox.setItems(dataHandler.divisionNamesObservableList(selectedCountryName));
    }

    /**
     * When a row in the TableView is clicked, add the customer information
     * to the input fields.
     * Disables Add Customer button until any other button is selected.
     */
    public void populateCustomerToForm() {
        Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();

        if (selectedCustomer == null) {
            return;
        }

        FirstLevelDivision customerDivision = dataHandler.divisionByID(selectedCustomer.getDivisionID());
        Country customerCountry = dataHandler.countryByDivision(customerDivision);
        countryBox.setValue(customerCountry.getCountry());
        divisionBox.setValue(customerDivision.getDivision());

        customerIDField.setText(String.valueOf(selectedCustomer.getCustomerID()));
        customerNameField.setText(selectedCustomer.getCustomerName());
        phoneField.setText(selectedCustomer.getPhone());
        addressField.setText(selectedCustomer.getAddress());
        postalCodeField.setText(selectedCustomer.getPostalCode());

        activateSelectionButtons();
    }

    /**
     * Clears all text from input fields in view.
     * Disables clearButton, updateButton and deleteButton.
     */
    public void clearForm() {
        customerTable.getSelectionModel().clearSelection();
        customerIDField.clear();
        customerNameField.clear();
        phoneField.clear();
        countryBox.setValue("Country");
        divisionBox.setValue("Division");
        addressField.clear();
        postalCodeField.clear();

        activateInsertionButtons();
    }

    /**
     * Attempts to add customer into database and maintains form on success.
     * This allows for multiple customers to be added in quick succession
     * if they share multiple fields (e.g. division and postal code)
     * Success: a new record is added to customers table in database and
     * the TableView is updated to display the most recently added record.
     * Failure: an alert is shown on screen describing the reason for failure.
     * No new customer is added to database and TableView is not refreshed.
     */
    public void addCustomer() {

    }

    /**
     * Attempts to update an existing customer based on user input from fields in view
     * and resets the form on success.
     * Success: The selected customer has fields updated and the change is
     * reflected in the TableView. Then calls clearForm().
     * Failure: an alert is shown on screen describing reason for failure.
     * The update does not occur in the database.
     */
    public void updateCustomer() {

    }

    /**
     * Attempts to delete an existing customer from the database and resets
     * the form on success.
     * Success: The selected customer is removed from database and the
     * change is reflected in the TableView. Then calls clearForm().
     * Failure: an alter is shown on screen describing reason for failure and
     * the update does not occur in the database.
     */
    public void deleteCustomer() {

    }

}
