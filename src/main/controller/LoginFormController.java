package main.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import main.dao.UserDao;
import main.model.Appointment;
import main.model.User;
import main.utility.DataHandlingFacade;
import main.utility.UniversalApplicationData;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoginFormController implements Initializable {

    @FXML
    TextField usernameField;
    @FXML
    PasswordField passwordField;
    @FXML
    Label zoneLabel;
    @FXML
    Label loginLabel;
    @FXML
    Label usernameLabel;
    @FXML
    Label passwordLabel;
    @FXML
    Button submitButton;
    @FXML
    AnchorPane loginFormParent;

    ResourceBundle languageResourceBundle = ResourceBundle.getBundle("main/resources/Nat", Locale.getDefault());
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Stored in singleton class for time conversions elsewhere
        UniversalApplicationData.setUserZone(ZoneId.systemDefault());

        String currentLang = Locale.getDefault().getLanguage();
        boolean hasSupportedLocalization = currentLang.equals("fr")
                                        || currentLang.equals("en");
        if (hasSupportedLocalization) {
            localizeDisplayedElements();
        }
    }

    private void localizeDisplayedElements() {
        loginLabel.setText(languageResourceBundle.getString("Login"));
        usernameLabel.setText(languageResourceBundle.getString("Username") + ":");
        passwordLabel.setText(languageResourceBundle.getString("Password") + ":");
        submitButton.setText(languageResourceBundle.getString("Submit"));

        //TODO: add fallbacks and edge case testing for when the user's country isn't supported by resource bundles
        //TODO: Might have to make method throw a MissingResourceException error
            //TODO: alternatively, try-catch inside this method and set label to untranslated String.
        String zone = ZoneId.systemDefault().toString();
        int separatorIndex = zone.indexOf("/");
        String country = zone.substring(0, separatorIndex);
        String location = zone.substring(separatorIndex + 1);
        zoneLabel.setText(
                languageResourceBundle.getString(country) + "/" + location);
    }

    public void loginUser() {
        String inputUsername = usernameField.getText();
        String inputPassword = passwordField.getText();

        UserDao userDao = new UserDao();
        User dbUserMatch = userDao.getByUsername(inputUsername);
        if (dbUserMatch != null && dbUserMatch.getPassword().equals(inputPassword)) {
            //TODO: success
            //TODO: track user activity
            //TODO: alert when appointment within 15 minutes of login or custom message
            UniversalApplicationData.setLoggedInUser(dbUserMatch);
            displayUpcomingAppointments();
            changeToCustomerForm();
        } else {
            JOptionPane.showMessageDialog(null,
                    languageResourceBundle.getString("LoginFail"));
        }
    }

    //TODO: add User parameter which only checks appointments with matching User
    private void displayUpcomingAppointments() {
        ArrayList<Appointment> upcomingAppointments = new DataHandlingFacade().upcomingAppointments(15);

        if(upcomingAppointments.size() == 0) {
            JOptionPane.showMessageDialog(null,
                    languageResourceBundle.getString("NoAppointments"));
        } else {
            String appointmentList = "";
            for (Appointment appointment: upcomingAppointments) {
                appointmentList = "\n" + appointmentList + appointment.toString();
            }

            JOptionPane.showMessageDialog(null,
                    languageResourceBundle.getString("UpcomingAppointment")
                            + appointmentList);
        }
    }

    public void changeToCustomerForm(){
        try {
            Parent nextForm = FXMLLoader.load(Objects.requireNonNull(
                    getClass().getResource("/main/view/CustomerForm.fxml")
            ));
            Stage applicationStage = (Stage) loginFormParent.getScene().getWindow();
            applicationStage.setScene(new Scene(nextForm, 800, 600));
        } catch (IOException ex) {
            System.out.println("Customer form could not be loaded: " + ex.getMessage());
        }
    }

}
