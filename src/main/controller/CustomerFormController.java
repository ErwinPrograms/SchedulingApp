package main.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import main.model.Country;
import main.model.Customer;
import main.model.FirstLevelDivision;
import main.model.User;
import main.utility.DataHandlingFacade;
import main.utility.UniversalApplicationData;


import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * A controller class for the customer form of the application. Responsible for handling the events and business
 * logic of the application. Depends on DataHandlingFacade to interact with data.
 */
public class CustomerFormController implements Initializable {


    @FXML
    AnchorPane customerFormParent;
    @FXML
    TableView<Customer> customerTable;
    @FXML
    TableColumn<Customer, Integer> idColumn;
    @FXML
    TableColumn<Customer, String> nameColumn;
    @FXML
    TableColumn<Customer, String> phoneColumn;
    @FXML
    TableColumn<Customer, String> divisionColumn;
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

    private final User loggedInUser = UniversalApplicationData.getLoggedInUser();
    //TODO: add column for country name/id
    private final DataHandlingFacade dataHandler = new DataHandlingFacade();

    //TODO: Add elements and events that allow to switch between forms
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        divisionColumn.setCellValueFactory(customerDivisionCellData -> {
            Customer customer = customerDivisionCellData.getValue();
            FirstLevelDivision customerDivision = dataHandler.divisionByID(customer.getDivisionID());
            Country customerCountry = dataHandler.countryByDivision(customerDivision);
            return new SimpleStringProperty(customerDivision.toString() + ", " + customerCountry.toString());
        });
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        postalColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        refreshCustomerTable();

        activateInsertionButtons();

        ObservableList<String> countryNames = dataHandler.countriesObservableList();
        countryBox.setItems(countryNames);
    }

    /**
     * A private helper function populates columns and rows based
     * on a call to the database.
     */
    private void refreshCustomerTable() {
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
     *
     * @return  True if all fields and ComboBoxes have a value
     *          False if there is any missing data
     */
    private boolean isFormComplete() {
        // Ignores CustomerID since user shouldn't be able to modify
        return  !(customerNameField.getText().equals("")
                || phoneField.getText().equals("")
                || countryBox.getValue() == null
                || divisionBox.getValue() == null
                || addressField.getText().equals("")
                || postalCodeField.getText().equals(""));
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
         if (!isFormComplete()) {
            JOptionPane.showMessageDialog(null, "Complete the form before submission");
            return;
         }

         //TODO: Pass logged in user to dataHandler
         int status = dataHandler.insertCustomer(
                 customerNameField.getText(),
                 addressField.getText(),
                 postalCodeField.getText(),
                 phoneField.getText(),
                 divisionBox.getValue(),
                 countryBox.getValue(),
                 loggedInUser
         );

         // switch allows for new execution codes to be easily added
         switch (status) {
             case 0:
                 refreshCustomerTable();
                 JOptionPane.showMessageDialog(null, "New customer added!");
                 //TODO: more success stuff
                 break;
             case 1:
                 JOptionPane.showMessageDialog(null, "Failed to add customer");
                 //TODO: more fail stuff
                 break;
         }
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
        if (!isFormComplete()) {
            JOptionPane.showMessageDialog(null, "Not all data was filled in. Update cancelled.");
            return;
        }

        int status = dataHandler.updateCustomer(
                Integer.parseInt(customerIDField.getText()),
                customerNameField.getText(),
                addressField.getText(),
                postalCodeField.getText(),
                phoneField.getText(),
                divisionBox.getValue(),
                countryBox.getValue()
        );

        // switch allows for new execution codes to be easily added
        switch (status) {
            case 0:
                refreshCustomerTable();
                JOptionPane.showMessageDialog(null, "Updated customer!");
                //TODO: more success stuff
                break;
            case 1:
                JOptionPane.showMessageDialog(null, "Failed to update customer");
                //TODO: more fail stuff
                break;
        }
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
        //TODO: add some kind of guard to prevent accidental deletion
        //TODO: Prompt to confirm delete, delete related appointments first before customer

        int targetsID = customerTable.getSelectionModel().getSelectedItem().getCustomerID();
        int status = dataHandler.deleteCustomer(targetsID);

        // switch allows for new execution codes to be easily added
        switch (status) {
            case 0:
                refreshCustomerTable();
                JOptionPane.showMessageDialog(null, "Deleted customer!");
                //TODO: more success stuff
                break;
            case 1:
                JOptionPane.showMessageDialog(null, "Failed to delete customer.");
                //TODO: more fail stuff
                break;
        }
    }

    /**
     * Event that fires whenever the user clicks the Appointment Form radio button. It changes the scene to the appointment form.
     */
    public void toAppointmentForm() {
        try {
            Parent nextForm = FXMLLoader.load(Objects.requireNonNull(
                    getClass().getResource("/main/view/AppointmentForm.fxml")
            ));
            Stage applicationStage = (Stage) customerFormParent.getScene().getWindow();
            //size arguments are optional
            applicationStage.setScene(new Scene(nextForm, 800, 600));
        } catch (IOException ex) {
            System.out.println("Customer form could not be loaded: " + ex.getMessage());
        }
    }

    /**
     * Event that fires whenever the user clicks the Report Form radio button. It changes the scene to the report form.
     */
    public void toReportForm() {
        try {
            Parent nextForm = FXMLLoader.load(Objects.requireNonNull(
                    getClass().getResource("/main/view/ReportForm.fxml")
            ));
            Stage applicationStage = (Stage) customerFormParent.getScene().getWindow();

            applicationStage.setScene(new Scene(nextForm, 800, 600));
        } catch (IOException ex) {
            System.out.println("Report form could not be loaded: " + ex.getMessage());
        }
    }
}
