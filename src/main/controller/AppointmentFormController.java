package main.controller;

import javafx.beans.property.SimpleStringProperty;
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
import java.time.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * A controller class for the appointment form of the application. Responsible for handling the events and business
 * logic of the application. Depends on DataHandlingFacade to interact with data.
 */
public class AppointmentFormController implements Initializable {

    @FXML
    AnchorPane appointmentFormParent;
    @FXML
    ComboBox<String> tableTimeRangeBox;
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
    TableColumn<Appointment, String> contactNameColumn;
    @FXML
    TableColumn<Appointment, String> typeColumn;
    @FXML
    TableColumn<Appointment, LocalDateTime> startDateTimeColumn;
    @FXML
    TableColumn<Appointment, LocalDateTime> endDateTimeColumn;
    @FXML
    TableColumn<Appointment, String> customerIDColumn;
    @FXML
    TableColumn<Appointment, String> userIDColumn;

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
    ComboBox<Customer> customerBox;

    @FXML
    ComboBox<User> userBox;

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
    //TODO: Time zones for project: UTC, EST, SystemDefault

    /**
     * A lambda is used inside setCellValueFactory() to retrieve Contact object from the contactID data in an Appointment
     * object. It then gets the name of that contact and converts it into a String so that it can be displayed into the table.
     * @param url               url
     * @param resourceBundle    resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        contactNameColumn.setCellValueFactory(appointmentContactCellData -> {
            Appointment appointment = appointmentContactCellData.getValue();
            return new SimpleStringProperty(dataHandler.contactByID(appointment.getContactID()).toString());
        });
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        startDateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("start"));
        endDateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("end"));
        customerIDColumn.setCellValueFactory(appointmentCustomerCellData -> {
            Appointment appointment = appointmentCustomerCellData.getValue();
            return new SimpleStringProperty(dataHandler.customerByID(appointment.getCustomerID()).toString());
        });
        userIDColumn.setCellValueFactory(appointmentUserCellData -> {
            Appointment appointment = appointmentUserCellData.getValue();
            return new SimpleStringProperty(dataHandler.userByID(appointment.getUserID()).toString());
        });

        //Arbitrarily set the default display to be all appointments
        tableTimeRangeBox.setValue("All");
        refreshAppointmentTable();

        updateComboBoxes();
        updateTimeBoxes();

        activateInsertionButtons();
    }

    /**
     * A private helper function which re-populates the table with up-to-date information
     * on appointment data
     */
    public void refreshAppointmentTable() {
        String rangeString = tableTimeRangeBox.getValue();
        appointmentTable.setItems(fetchAppointmentsObservableList(rangeString));
    }

    /**
     *
     * @param   rangeString A String value: "Week", "Month", or "All" which specifies which appointments to include
     * @return  An ObservableList<Appointment> with all Appointment entries in the database, filtered based on
     *          the input string.
     */
    private ObservableList<Appointment> fetchAppointmentsObservableList(String rangeString){
        //TODO: Specify if filters should work based on local time or a single timezone (e.g. ET)

        //Week includes all appointments between the previous Sunday and the upcoming Sunday
        if(rangeString.equals("Week")) {
            DayOfWeek currentDay = LocalDateTime.now().getDayOfWeek();

            int daysToSubtract = currentDay.getValue() % 7;
            LocalDateTime startOfWeek = LocalDateTime.now().minusDays(daysToSubtract)
                    .with(LocalTime.MIDNIGHT);

            int daysToAdd = DayOfWeek.SUNDAY.getValue() - currentDay.getValue();
            if(daysToAdd <= 0){
                daysToAdd += 7;
            }
            LocalDateTime endOfWeek = LocalDateTime.now().plusDays(daysToAdd)
                    .with(LocalTime.MIDNIGHT);
            return dataHandler.appointmentsObservableList(startOfWeek, endOfWeek);
        }
        //Month includes all appointments between the start of the current calendar month and the beginning of the next
        if(rangeString.equals("Month")) {
            LocalDate currentDate = LocalDate.now();

            LocalDateTime startOfMonth = currentDate.withDayOfMonth(1).atTime(LocalTime.MIDNIGHT);
            LocalDateTime startOfNextMonth = currentDate.plusMonths(1).withDayOfMonth(1).
                    atTime(LocalTime.MIDNIGHT);

            return dataHandler.appointmentsObservableList(startOfMonth, startOfNextMonth);
        }
        if(rangeString.equals("All")) {
            return dataHandler.appointmentsObservableList();
        }

        return FXCollections.observableArrayList();
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

    private void updateComboBoxes() {
        contactBox.setItems(dataHandler.contactObservableList());
        customerBox.setItems(dataHandler.customersObservableList());
        userBox.setItems(dataHandler.usersObservableList());
    }

    private void updateTimeBoxes() {
        ArrayList<LocalTime> timeList = new ArrayList<>();

        //TODO: refactor time conversion as a new method
        //Start and End times are based around 8:00-22:00EST
        LocalTime referenceStartTime = LocalTime.of(8,0);//8:00, 8am, is reference start
        LocalTime referenceEndTime = LocalTime.of(22,0); //22:00, 10pm, is reference end
        ZoneId referenceZone = ZoneId.of("America/New_York"); //EST is reference timezone

        ZonedDateTime estStartTime = ZonedDateTime.of(
                ZonedDateTime.now().toLocalDate(), referenceStartTime, referenceZone);
        LocalDateTime systemStartTime = estStartTime.withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();

        ZonedDateTime estEndTime = ZonedDateTime.of(
                ZonedDateTime.now().toLocalDate(), referenceEndTime, referenceZone);
        LocalDateTime systemEndTime = estEndTime.withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();

        int minutesInterval = 15;

        //TODO: test if this works when time zones make business hours cross days (e.g. European Time Zones)
        while(!systemStartTime.isAfter(systemEndTime)) {
            timeList.add(systemStartTime.toLocalTime());
            systemStartTime = systemStartTime.plusMinutes(minutesInterval);
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

        Contact contact = dataHandler.contactByID(selectedAppointment.getContactID());
        Customer customer = dataHandler.customerByID(selectedAppointment.getCustomerID());
        User user = dataHandler.userByID(selectedAppointment.getUserID());

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
        customerBox.setValue(customer);
        userBox.setValue(user);

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
        customerBox.setValue(null);
        userBox.setValue(null);

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

        //TODO: refactor time conversion as a new method
        LocalTime referenceStartTime = LocalTime.of(8,0);//8:00, 8am, is reference start
        LocalTime referenceEndTime = LocalTime.of(22,0); //22:00, 10pm, is reference end
        ZoneId referenceZone = ZoneId.of("America/New_York"); //EST is reference timezone

        ZonedDateTime estStartTime = ZonedDateTime.of(
                ZonedDateTime.now().toLocalDate(), referenceStartTime, referenceZone);
        LocalDateTime systemStartTime = estStartTime.withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();

        ZonedDateTime estEndTime = ZonedDateTime.of(
                ZonedDateTime.now().toLocalDate(), referenceEndTime, referenceZone);
        LocalDateTime systemEndTime = estEndTime.withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();

        LocalTime openingTime = systemStartTime.toLocalTime();
        LocalTime closingTime = systemEndTime.toLocalTime();

        if(     (startTime.isAfter(closingTime)
                || startTime.isBefore(openingTime)
                || endTime.isAfter(closingTime)
                || endTime.isBefore(openingTime))) {
            return false;
        }

        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        //TODO: check validity for European time zones
        //if dates are ever different, then appointment must take place outside business hours
        if (!startDate.isEqual(endDate)) {
            return false;
        }

        return true;
    }

    private boolean isAppointmentOverlapping() {
        //there are no appointments with ID equal to -1
        return isAppointmentOverlapping(-1);
    }

    /**
     *
     * @param excludedAppointmentID Used in updates to prevent an appointment checking against itself.
     * @return  True if appointment time and date is overlapping with
     *          another appointment for the same customer
     *          Does not check if start is before end
     *          Does not check if appointment is scheduled for the future or past
     *          False if there is no overlap in hours
     */
    private boolean isAppointmentOverlapping(int excludedAppointmentID) {
        int customerID = customerBox.getValue().getCustomerID();
        ArrayList<Appointment> appointmentsToCheck = dataHandler.appointmentsByCustomerID(customerID);

        LocalDateTime checkedAppointmentStart = LocalDateTime.of(startDatePicker.getValue(), startTimeBox.getValue());
        LocalDateTime checkedAppointmentEnd = LocalDateTime.of(endDatePicker.getValue(), endTimeBox.getValue());

        for (Appointment appointment: appointmentsToCheck) {
            if(appointment.getAppointmentID() == excludedAppointmentID) {
                continue;
            }

            if (
                    !(appointment.getStart().compareTo(checkedAppointmentStart) <= 0
                    && appointment.getEnd().compareTo(checkedAppointmentStart) <= 0)
                    &&
                    !(appointment.getStart().compareTo(checkedAppointmentEnd) >= 0
                    && appointment.getEnd().compareTo(checkedAppointmentEnd) >= 0)
            ){
              return true;
            }
        }

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
                || customerBox.getValue() == null
                || userBox.getValue() == null);
    }

    private boolean isStartAfterEnd() {
        LocalDateTime checkedAppointmentStart = LocalDateTime.of(startDatePicker.getValue(), startTimeBox.getValue());
        LocalDateTime checkedAppointmentEnd = LocalDateTime.of(endDatePicker.getValue(), endTimeBox.getValue());

        return checkedAppointmentStart.isAfter(checkedAppointmentEnd);
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
        if(isStartAfterEnd()) {
            JOptionPane.showMessageDialog(null,
                    "Start time must be before end time");
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

        int status = dataHandler.insertAppointment(
                titleField.getText(),
                descriptionField.getText(),
                locationField.getText(),
                typeField.getText(),
                appointmentStart,
                appointmentEnd,
                customerBox.getValue().getCustomerID(),
                userBox.getValue().getUserID(),
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
        if(!isFormComplete()) {
            JOptionPane.showMessageDialog(null,
                    "Please complete form before attempting to submit");
            return;
        }
        if(!isAppointmentInBusinessHours()) {
            JOptionPane.showMessageDialog(null, "Appointment is outside operating hours.");
            return;
        }
        if(isAppointmentOverlapping(Integer.parseInt(appointmentIDField.getText()))) {
            JOptionPane.showMessageDialog(null,
                    "Appointment time overlaps with another for this customer.");
            return;
        }
        if(isStartAfterEnd()) {
            JOptionPane.showMessageDialog(null,
                    "Start time must be before end time");
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

        int status = dataHandler.updateAppointment(
                Integer.parseInt(appointmentIDField.getText()),
                titleField.getText(),
                descriptionField.getText(),
                locationField.getText(),
                typeField.getText(),
                appointmentStart,
                appointmentEnd,
                customerBox.getValue().getCustomerID(),
                userBox.getValue().getUserID(),
                contactBox.getValue().getContactID()
        );

        switch (status) {
            case 0:
                refreshAppointmentTable();
                JOptionPane.showMessageDialog(null, "Updated appointment!");
                //TODO: more success stuff
                break;
            case 1:
                JOptionPane.showMessageDialog(null, "Failed to update appointment.");
                //TODO: more fail stuff
                break;
        }
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

    /**
     * Event that fires whenever user clicks the Customer Form radio button. It changes the scene into the customer form.
     */
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

    /**
     * Event that fires whenever the user clicks the Report Form radio button. It changes the scene to the report form.
     */
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
