package main.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import main.dao.UserDao;
import main.model.User;
import main.utility.UniversalApplicationData;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.time.ZoneId;
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
    AnchorPane loginFormParent;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //TODO: Determine user location and displays on log-in form
        //TODO: Translation based on computer language setting (Use enums?)
            //TODO: Translate all visible elements on LoginForm.fxml
            //TODO: Translate alerts
        ZoneId currentZone = ZoneId.systemDefault();
        UniversalApplicationData.setUserZone(ZoneId.systemDefault());
        zoneLabel.setText("Location: " + currentZone);
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
            changeToCustomerForm();
        } else {
            JOptionPane.showMessageDialog(null,
                    "Failed to login with those credentials.");
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
            System.out.println("Customer from could not be loaded: " + ex.getMessage());
        }
    }

}
