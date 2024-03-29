package main.controller;

import com.mysql.cj.xdevapi.Table;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import main.dao.ReportDao;
import main.model.*;
import main.utility.DataHandlingFacade;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * A controller class for the report form of the application. Responsible for handling the events and business
 * logic of the application. Depends on DataHandlingFacade to interact with data.
 */
public class ReportFormController implements Initializable {

    @FXML
    AnchorPane reportFormParent;
    @FXML
    TableView<MonthTypeCount> monthTypeCountTable;
    @FXML
    TableView<Appointment> contactTable;
    @FXML
    ComboBox<Contact> contactBox;
    @FXML
    TableView<DivisionCount> divisionCountTable;
    DataHandlingFacade dataHandler = new DataHandlingFacade();

    //TODO: Report 1 - # of appointment by month and type
        //TODO: Option 1 would require a new model object ("MonthType")
        // 3 columns
        // Use aggregate SQL query
    //Potentially create ReportDAO(s)

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<TableColumn<MonthTypeCount, ?>> monthTypeCountColumns = monthTypeCountTable.getColumns();
        monthTypeCountColumns.get(0).setCellValueFactory(new PropertyValueFactory<>("monthYear"));
        monthTypeCountColumns.get(1).setCellValueFactory(new PropertyValueFactory<>("type"));
        monthTypeCountColumns.get(2).setCellValueFactory(new PropertyValueFactory<>("count"));

        ObservableList<TableColumn<Appointment, ?>> contactAppointmentColumns = contactTable.getColumns();
        contactAppointmentColumns.get(0).setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        contactAppointmentColumns.get(1).setCellValueFactory(new PropertyValueFactory<>("title"));
        contactAppointmentColumns.get(2).setCellValueFactory(new PropertyValueFactory<>("type"));
        contactAppointmentColumns.get(3).setCellValueFactory(new PropertyValueFactory<>("description"));
        contactAppointmentColumns.get(4).setCellValueFactory(new PropertyValueFactory<>("start"));
        contactAppointmentColumns.get(5).setCellValueFactory(new PropertyValueFactory<>("end"));
        TableColumn<Appointment, Customer> customerColumn =
                (TableColumn<Appointment, Customer>) contactAppointmentColumns.get(6);
        customerColumn.setCellValueFactory(customerCellData -> {
            Appointment appointment = customerCellData.getValue();
            Customer appointmentCustomer= dataHandler.customerByID(appointment.getCustomerID());
            return new SimpleObjectProperty<>(appointmentCustomer);
        });

        ObservableList<TableColumn<DivisionCount, ?>> divisionCountColumns = divisionCountTable.getColumns();
        divisionCountColumns.get(0).setCellValueFactory(new PropertyValueFactory<>("division"));
        divisionCountColumns.get(1).setCellValueFactory(new PropertyValueFactory<>("count"));

        loadStaticElements();
    }

    /**
     * Loads data into the elements that do not change after page initialization.
     * This includes Report 1 and Report 3 as well as the ComboBox for Report 2
     */
    private void loadStaticElements() {
        monthTypeCountTable.setItems(dataHandler.getMonthTypeCountReport());
        divisionCountTable.setItems(dataHandler.getDivisionCountReport());

        contactBox.setItems(dataHandler.contactObservableList());
    }

    /**
     * An event that fires whenever the user selects a contact name in the contact box. It re-loads the table for that
     * report with all the appointments associated with that contact.
     */
    public void loadContactAppointments() {
        contactTable.setItems(dataHandler.appointmentsByContact(contactBox.getValue()));
    }

    /**
     * Event that fires whenever the user clicks the Report Form radio button. It changes the scene to the report form.
     */
    public void toCustomerForm() {
        try {
            Parent nextForm = FXMLLoader.load(Objects.requireNonNull(
                    getClass().getResource("/main/view/CustomerForm.fxml")
            ));
            Stage applicationStage = (Stage) reportFormParent.getScene().getWindow();
            applicationStage.setScene(new Scene(nextForm, 800, 600));
        } catch (IOException ex) {
            System.out.println("Customer form could not be loaded: " + ex.getMessage());
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
            Stage applicationStage = (Stage) reportFormParent.getScene().getWindow();
            //size arguments are optional
            applicationStage.setScene(new Scene(nextForm, 800, 600));
        } catch (IOException ex) {
            System.out.println("Customer form could not be loaded: " + ex.getMessage());
        }
    }
}
