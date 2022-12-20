package main.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class ReportFormController implements Initializable {

    @FXML
    AnchorPane reportFormParent;

    //TODO: Report 1 - # of appointment by month and type
        //TODO: Option 1 would require a new model object ("MonthType")
        // 3 columns
        // Use aggregate SQL query
    //Potentially create ReportDAO(s)

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }


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
