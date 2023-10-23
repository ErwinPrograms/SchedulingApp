package main.utility;

import main.model.User;

import java.time.ZoneId;

/**
 * This Singleton utility is hastily made to transfer data between controllers.
 */
public class UniversalApplicationData {
    private static User loggedInUser;
    private static ZoneId userZone;

    /**
     * @return stored loggedInUser
     */
    public static User getLoggedInUser() {
        return loggedInUser;
    }

    /**
     * @return stored userZone
     */
    public static ZoneId getUserZone() {
        return userZone;
    }

    /**
     * Sets the loggedInUser
     * @param loggedInUser  new value for loggedInUser
     */
    public static void setLoggedInUser(User loggedInUser) {
        UniversalApplicationData.loggedInUser = loggedInUser;
    }

    /**
     * sets the userZone
     * @param userZone new value for loggedInUser
     */
    public static void setUserZone(ZoneId userZone) {
        UniversalApplicationData.userZone = userZone;
    }
}
