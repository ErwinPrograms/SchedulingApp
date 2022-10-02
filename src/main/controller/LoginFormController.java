package main.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import main.dao.DBConnection;
import main.dao.UserDao;
import main.model.User;

import java.net.URL;
import java.time.ZoneId;
import java.util.ResourceBundle;

public class LoginFormController implements Initializable {

    @FXML
    TextField usernameField;
    @FXML
    PasswordField passwordField;
    @FXML
    Label zoneLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ZoneId currentZone = ZoneId.systemDefault();
        zoneLabel.setText("Location: " + currentZone);
    }

    public void loginUser() {
        String inputUsername = usernameField.getText();
        String inputPassword = passwordField.getText();

        UserDao userDao = new UserDao();
        User dbUserMatch = userDao.getByUsername(inputUsername);
        if (dbUserMatch == null || !dbUserMatch.getPassword().equals(inputPassword)) {
            //TODO: alert failure
            System.out.println("FAILURE");
        } else {
            //TODO: success
            System.out.println("Success");
        }
    }
}
