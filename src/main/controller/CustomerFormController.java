package main.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import main.dao.CountryDao;
import main.dao.CustomerDao;
import main.dao.DBConnection;
import main.dao.FirstLevelDivisionDao;
import main.model.Country;
import main.model.Customer;
import main.model.FirstLevelDivision;


import javax.swing.*;
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

    private final CustomerDao customerDao = new CustomerDao();

    // Store key value pairs of Country name and all of its division names
    // Countries and divisions do not change in runtime so no updates are
    // expected after initialization
    private final Map<String, ObservableList<String>> countryDivisionsMap = new HashMap<>();

    //TODO: Add elements and events that allow to switch between forms
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populateCustomerTable();
        setupCountryDivisionsMap();
        activateInsertionButtons();

        ObservableList<String> countryNames = FXCollections.observableArrayList();
        countryNames.addAll(countryDivisionsMap.keySet());

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

        ObservableList<Customer> allCustomersObservable = FXCollections.observableList(customerDao.getAll());
        customerTable.setItems(allCustomersObservable);
    }

    /**
     * Set up read only data to be used in the controller for use in countryBox and divisionBox.
     *
     * Lambda 1 - removeIf()
     * This lambda is used to easily filter divisions with countryIDs that do match the current
     * country's.
     *
     * Lambda 2 - forEach()
     * Quickly takes the name String of each division and appends them to an ObservableList
     */
    private void setupCountryDivisionsMap() {
        CountryDao countryDao = new CountryDao();
        FirstLevelDivisionDao firstLevelDivisionDao = new FirstLevelDivisionDao();

        ArrayList<Country> allCountries = countryDao.getAll();
        ArrayList<FirstLevelDivision> allDivisions = firstLevelDivisionDao.getAll();

        for (Country country: allCountries) {
            // ArrayList's removeIf method modifies the original ArrayList, so
            // a shallow clone is required before filtering
            ArrayList<FirstLevelDivision> divisionsInCountry =
                    (ArrayList<FirstLevelDivision>) allDivisions.clone();
            // Lambda 1
            divisionsInCountry.removeIf(div -> div.getCountryID() != country.getCountryID());

            // Converted to ObservableList<String> so it can be inserted into ComboBoxes easily
            ObservableList<String> divisionStrings = FXCollections.observableArrayList();
            // Lambda 2
            divisionsInCountry.forEach(divObj -> divisionStrings.add(divObj.getDivision()));

            // Optional alphabetical sorting of divisions before inserting into map.
            Collections.sort(divisionStrings);

            // Finally, store in class variable for use throughout controller
            countryDivisionsMap.put(country.getCountry(), divisionStrings);
        }
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
        divisionBox.setItems(countryDivisionsMap.get(selectedCountryName));
    }

    /**
     * When a row in the TableView is clicked, add the customer information
     * to the input fields.
     *
     * Disables Add Customer button until any other button is selected.
     */
    public void populateCustomerToForm() {
        Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();

        if (selectedCustomer == null) {
            return;
        }

        //TODO: grab country and division ComboBoxes

        customerIDField.setText(String.valueOf(selectedCustomer.getCustomerID()));
        customerNameField.setText(selectedCustomer.getCustomerName());
        phoneField.setText(selectedCustomer.getPhone());
        addressField.setText(selectedCustomer.getAddress());
        postalCodeField.setText(selectedCustomer.getPostalCode());

        activateSelectionButtons();
    }

    /**
     * Clears all text from input fields in view.
     *
     * Disables clearButton, updateButton and deleteButton.
     */
    public void clearForm() {
        customerIDField.clear();
        customerNameField.clear();
        phoneField.clear();
        //TODO: clear ComboBoxes
        addressField.clear();
        postalCodeField.clear();

        activateInsertionButtons();
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
