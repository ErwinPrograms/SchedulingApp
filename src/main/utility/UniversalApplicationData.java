package main.utility;

import main.model.User;

import java.time.ZoneId;

/**
 * This Singleton utility is hastily made to transfer data between controllers.
 */
public class UniversalApplicationData {
    private static User loggedInUser;
    private static ZoneId userZone;

    public static User getLoggedInUser() {
        return loggedInUser;
    }

    public static ZoneId getUserZone() {
        return userZone;
    }

    public static void setLoggedInUser(User loggedInUser) {
        UniversalApplicationData.loggedInUser = loggedInUser;
    }

    public static void setUserZone(ZoneId userZone) {
        UniversalApplicationData.userZone = userZone;
    }
}
