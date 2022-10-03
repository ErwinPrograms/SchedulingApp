package main.controller;

import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class AppointmentFormController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    /**
     * When a row in the TableView is clicked, add the appointment information
     * to the input fields.
     *
     * Disables Add Appointment button until any other button is selected.
     */
    public void populateAppointmentToForm() {

    }

    /**
     * Clears all text from input fields in view.
     *
     * Disables clearButton, updateButton and deleteButton.
     */
    public void clearForm() {

    }

    /**
     * Attempts to add appointment into database and maintains form on success.
     * This allows for multiple appointments to be added in quick succession
     * if they share multiple fields (e.g. customer, user, contact, type)
     *
     * Success: a new record is added to appointments table in database and
     * the TableView is updated to display the most recently added record.
     *
     * Failure: an alert is shown on screen describing the reason for failure.
     * No new appointment is added to database and TableView is not refreshed.
     */
    public void addAppointment() {

    }

    /**
     * Attempts to update an existing appointment based on user input from fields in view
     * and resets the form on success.
     *
     * Success: The selected appointment has fields updated and the change is
     * reflected in the TableView. Then calls clearForm().
     *
     * Failure: an alert is shown on screen describing reason for failure.
     * The update does not occur in the database.
     */
    public void updateAppointment() {

    }

    /**
     * Attempts to delete an existing appointment from the database and resets
     * the form on success.
     *
     * Success: The selected appointment is removed from database and the
     * change is reflected in the TableView. Then calls clearForm().
     *
     * Failure: an alter is shown on screen describing reason for failure and
     * the update does not occur in the database.
     */
    public void deleteAppointment() {

    }
}