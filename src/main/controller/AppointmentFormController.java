package main.controller;

import javafx.collections.FXCollections;
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

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
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
    ComboBox<Contact> contactBox;

    @FXML
    TextField typeField;

    @FXML
    DatePicker startDatePicker;

    @FXML
    ComboBox<LocalTime> startTimeBox;

    @FXML
    ComboBox<LocalTime> endTimeBox;

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

        updateContactBox();
        updateTimeBoxes();

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

    public void updateContactBox() {
        contactBox.setItems(dataHandler.contactObservableList());
    }

    public void updateTimeBoxes() {
        ArrayList<LocalTime> timeList = new ArrayList<>();

        //TODO: move logic for list creation to DataHandlingFacade
        LocalTime startTime = LocalTime.of(8, 0);
        LocalTime endTime = LocalTime.of(22, 0);
        int minutesInterval = 15;

        while(!startTime.isAfter(endTime)) {
            timeList.add(startTime);
            startTime = startTime.plusMinutes(minutesInterval);
        }

        startTimeBox.setItems(FXCollections.observableList(timeList));
        endTimeBox.setItems(FXCollections.observableList(timeList));
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

        String contactName = dataHandler.contactByID(selectedAppointment.getContactID()).getContactName();
        Contact contact = dataHandler.contactByID(selectedAppointment.getContactID());

        appointmentIDField.setText(String.valueOf(selectedAppointment.getAppointmentID()));
        titleField.setText(selectedAppointment.getTitle());
        descriptionField.setText(selectedAppointment.getDescription());
        locationField.setText(selectedAppointment.getLocation());
        contactBox.setValue(contact);
        typeField.setText(selectedAppointment.getType());
        startDatePicker.setValue(selectedAppointment.getStart().toLocalDate());
        startTimeBox.setValue(selectedAppointment.getStart().toLocalTime());
        endDatePicker.setValue(selectedAppointment.getEnd().toLocalDate());
        endTimeBox.setValue(selectedAppointment.getEnd().toLocalTime());
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
        contactBox.setValue(null);
        typeField.clear();
        startDatePicker.setValue(null);
        startTimeBox.setValue(null);
        endDatePicker.setValue(null);
        endTimeBox.setValue(null);
        customerIDField.clear();
        userIDField.clear();

        activateInsertionButtons();
    }

    /**
     * @return  True if time is within business hours, defined as 8:00 a.m.
     *          to 10:00 p.m. ET, including weekends
     *          Does not validate that all UI boxes aren't null
     *          Does not check if start is before end
     *          Does not check if time is in the past
     *          False if outside business hours
     */
    private boolean isAppointmentInBusinessHours() {
        LocalTime startTime = startTimeBox.getValue();
        LocalTime endTime = endTimeBox.getValue();

        LocalTime openingTime = LocalTime.of(8, 0);
        LocalTime closingTime = LocalTime.of(22, 0);

        if(     !(startTime.isAfter(closingTime)
                || startTime.isBefore(openingTime)
                || endTime.isAfter(closingTime)
                || endTime.isBefore(closingTime))) {
            return false;
        }

        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        //if dates are ever different, then appointment must take place outside business hours
        if (!startDate.isEqual(endDate)) {
            return false;
        }

        return true;
    }

    /**
     *
     * @return  True if appointment time and date is overlapping with
     *          another appointment for the same customer
     *          False if there is no overlap in hours
     */
    private boolean isAppointmentOverlapping() {
        return false;
    }

    private boolean isFormComplete() {
        return  !(titleField.getText().equals("")
                || descriptionField.getText().equals("")
                || locationField.getText().equals("")
                || typeField.getText().equals("")
                || startDatePicker.getValue() == null
                || startTimeBox.getValue() == null
                || endDatePicker.getValue() == null
                || endTimeBox.getValue() == null
                || customerIDField.getText().equals("")
                || userIDField.getText().equals(""));
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
        if(!isFormComplete()) {
            JOptionPane.showMessageDialog(null,
                    "Please complete form before attempting to submit");
            return;
        }
        if(!isAppointmentInBusinessHours()) {
            JOptionPane.showMessageDialog(null, "Appointment is outside operating hours.");
            return;
        }
        if(isAppointmentOverlapping()) {
            JOptionPane.showMessageDialog(null,
                    "Appointment time overlaps with another for this customer.");
            return;
        }

        LocalDateTime appointmentStart = LocalDateTime.of(
                startDatePicker.getValue(),
                startTimeBox.getValue()
        );
        LocalDateTime appointmentEnd = LocalDateTime.of(
                endDatePicker.getValue(),
                endTimeBox.getValue()
        );

        //TODO: non-integer values for customerID and userID WILL cause errors
        int status = dataHandler.insertAppointment(
                titleField.getText(),
                descriptionField.getText(),
                locationField.getText(),
                typeField.getText(),
                appointmentStart,
                appointmentEnd,
                Integer.parseInt(customerIDField.getText()),
                Integer.parseInt(userIDField.getText()),
                contactBox.getValue().getContactID()
        );

        switch (status) {
            case 0:
                refreshAppointmentTable();
                JOptionPane.showMessageDialog(null, "New appointment added!");
                //TODO: more success stuff
                break;
            case 1:
                JOptionPane.showMessageDialog(null, "Failed to add appointment");
                //TODO: more fail stuff
                break;
        }
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

        int targetsID = appointmentTable.getSelectionModel().getSelectedItem().getAppointmentID();
        int status = dataHandler.deleteAppointment(targetsID);

        // switch allows for new execution codes to be easily added
        switch (status) {
            case 0:
                refreshAppointmentTable();
                JOptionPane.showMessageDialog(null, "Deleted appointment!");
                //TODO: more success stuff
                break;
            case 1:
                JOptionPane.showMessageDialog(null, "Failed to delete appointment.");
                //TODO: more fail stuff
                break;
        }
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
