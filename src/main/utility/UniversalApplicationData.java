package main.utility;

import main.model.User;

/**
 * This Singleton utility is hastily made to transfer data between controllers.
 */
public class UniversalApplicationData {
    private static User loggedInUser;

    public static User getLoggedInUser() {
        return loggedInUser;
    }

    public static void setLoggedInUser(User loggedInUser) {
        UniversalApplicationData.loggedInUser = loggedInUser;
    }
}
