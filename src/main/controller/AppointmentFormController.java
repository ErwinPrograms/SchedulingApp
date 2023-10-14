package main.controller;

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
import main.model.Appointment;
import main.model.Contact;
import main.model.Customer;
import main.model.User;
import main.utility.DataHandlingFacade;
import main.utility.UniversalApplicationData;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.ResourceBundle;

public class AppointmentFormController implements Initializable {

    @FXML
    AnchorPane appointmentFormParent;
    @FXML
    TableView<Appointment> appointmentTable;
    @FXML
    TableColumn<Appointment, Integer> idColumn;
    @FXML
    TableColumn<Appointment, String> titleColumn;
    @FXML
    TableColumn<Appointment, String> descriptionColumn;
    @FXML
    TableColumn<Appointment, String> locationColumn;
    @FXML
    TableColumn<Appointment, Integer> contactIDColumn;
    @FXML
    TableColumn<Appointment, String> typeColumn;
    @FXML
    TableColumn<Appointment, LocalDateTime> startDateTimeColumn;
    @FXML
    TableColumn<Appointment, LocalDateTime> endDateTimeColumn;
    @FXML
    TableColumn<Appointment, Integer> customerIDColumn;
    @FXML
    TableColumn<Appointment, Integer> userIDColumn;

    @FXML
    TextField appointmentIDField;

    @FXML
    TextField titleField;

    @FXML
    TextField descriptionField;

    @FXML
    TextField locationField;

    @FXML
    ComboBox<String> contactBox;

    @FXML
    TextField typeField;

    @FXML
    DatePicker startDatePicker;

    @FXML
    ComboBox<String> startTimeBox;

    @FXML
    ComboBox<String> endTimeBox;

    @FXML
    DatePicker endDatePicker;

    @FXML
    TextField customerIDField;

    @FXML
    TextField userIDField;

    @FXML
    Button clearButton;

    @FXML
    Button addButton;

    @FXML
    Button updateButton;

    @FXML
    Button deleteButton;

    private final User loggedInUser = UniversalApplicationData.getLoggedInUser();
    private final DataHandlingFacade dataHandler = new DataHandlingFacade();

    //TODO: Calendar week is sunday - saturday
    //TODO: Business hours 8AM - (EST) convert to EST
    //TODO: Use localDateTime
    // LocalTime already has a toString in 24h format
    //TODO: potentially replace radio with buttons
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        contactIDColumn.setCellValueFactory(new PropertyValueFactory<>("contactID"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        startDateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("start"));
        endDateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("end"));
        customerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        userIDColumn.setCellValueFactory(new PropertyValueFactory<>("userID"));
        refreshAppointmentTable();

        activateInsertionButtons();
    }

    /**
     * A private helper function populates columns and rows based
     * on a call to the database.
     */
    private void refreshAppointmentTable() {
        ObservableList<Appointment> allAppointmentsObservable = dataHandler.appointmentsObservableList();
        appointmentTable.setItems(allAppointmentsObservable);
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
     * When a row in the TableView is clicked, add the appointment information
     * to the input fields.
     * Disables Add Appointment button until any other button is selected.
     */
    public void populateAppointmentToForm() {
        Appointment selectedAppointment = appointmentTable.getSelectionModel().getSelectedItem();

        if(selectedAppointment == null) {
            return;
        }

        appointmentIDField.setText(String.valueOf(selectedAppointment.getAppointmentID()));
        titleField.setText(selectedAppointment.getTitle());
        descriptionField.setText(selectedAppointment.getDescription());
        locationField.setText(selectedAppointment.getLocation());
        //contactBox
        typeField.setText(selectedAppointment.getType());
        //startDatePicker
        //startTimeBox
        //endDatePicker
        //endTimeBox
        customerIDField.setText(String.valueOf(selectedAppointment.getCustomerID()));
        userIDField.setText(String.valueOf(selectedAppointment.getUserID()));

        activateSelectionButtons();
    }

    /**
     * Clears all text from input fields in view.
     * Disables clearButton, updateButton and deleteButton.
     */
    public void clearForm() {
        appointmentTable.getSelectionModel().clearSelection();
        appointmentIDField.clear();
        titleField.clear();
        descriptionField.clear();
        locationField.clear();
        contactBox.setValue("Contact");
        typeField.clear();
        startDatePicker.setValue(null);
        startTimeBox.setValue("Start Time");
        endDatePicker.setValue(null);
        endTimeBox.setValue("End Time");
        customerIDField.clear();
        userIDField.clear();

        activateInsertionButtons();
    }

    /**
     *
     * @return  True if time doesn't overlap with another appointment and
     *          within business hours.
     *          False if outside business hours or overlaps with another appointment.
     */
    private boolean isValidAppointmentTime() {
        return false;
    }

    /**
     * Attempts to add appointment into database and maintains form on success.
     * This allows for multiple appointments to be added in quick succession
     * if they share multiple fields (e.g. customer, user, contact, type)
     * Success: a new record is added to appointments table in database and
     * the TableView is updated to display the most recently added record.
     * Failure: an alert is shown on screen describing the reason for failure.
     * No new appointment is added to database and TableView is not refreshed.
     */
    public void addAppointment() {

    }

    /**
     * Attempts to update an existing appointment based on user input from fields in view
     * and resets the form on success.
     * Success: The selected appointment has fields updated and the change is
     * reflected in the TableView. Then calls clearForm().
     * Failure: an alert is shown on screen describing reason for failure.
     * The update does not occur in the database.
     */
    public void updateAppointment() {

    }

    /**
     * Attempts to delete an existing appointment from the database and resets
     * the form on success.
     * Success: The selected appointment is removed from database and the
     * change is reflected in the TableView. Then calls clearForm().
     * Failure: an alter is shown on screen describing reason for failure and
     * the update does not occur in the database.
     */
    public void deleteAppointment() {

    }

    public void toCustomerForm() {
        try {
            Parent nextForm = FXMLLoader.load(Objects.requireNonNull(
                    getClass().getResource("/main/view/CustomerForm.fxml")
            ));
            Stage applicationStage = (Stage) appointmentFormParent.getScene().getWindow();
            applicationStage.setScene(new Scene(nextForm, 800, 600));
        } catch (IOException ex) {
            System.out.println("Customer form could not be loaded: " + ex.getMessage());
        }
    }

    public void toReportForm() {
        try {
            Parent nextForm = FXMLLoader.load(Objects.requireNonNull(
                    getClass().getResource("/main/view/ReportForm.fxml")
            ));
            Stage applicationStage = (Stage) appointmentFormParent.getScene().getWindow();

            applicationStage.setScene(new Scene(nextForm, 800, 600));
        } catch (IOException ex) {
            System.out.println("Report form could not be loaded: " + ex.getMessage());
        }
    }
}
