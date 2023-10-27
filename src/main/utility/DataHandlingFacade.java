package main.utility;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.dao.*;
import main.model.*;

import java.time.LocalDateTime;
import java.util.*;

/**
 * This is a facade for data handling that would be required for the controller objects.
 * It abstracts some complex data handling that is required to minimize excessive calls
 * to the database.
 */
public class DataHandlingFacade {

    private Map<FirstLevelDivision, Country> divisionCountryMap = new HashMap<>();
    private Map<Country, ObservableList<String>> countryDivisionNamesMap = new HashMap<>();
    private ArrayList<Country> countries = new ArrayList<>();
    private ArrayList<FirstLevelDivision> divisions = new ArrayList<>();
    private ArrayList<Contact> contacts = new ArrayList<>();
    private ArrayList<Customer> customers = new ArrayList<>();
    private ArrayList<Appointment> appointments = new ArrayList<>();

    /**
     * Initializes DataHandlingFacade by fetching data from multiple tables in the database
     */
    public DataHandlingFacade() {
        fetchInitialData();
        refreshCustomerData();
        refreshAppointmentData();
    }

    /**
     * Retrieves data from database that is unlikely to change during runtime of application.
     * This currently includes Contact data, country data, and division data.<br>
     * Lambda 1 - mapping FirstLevelDivisions into Countries
     * This lambda easily finds the related Country using a helper method in the class, then
     * stores the connection in a HashMap object for quick retrieval later. <br>
     * Lambda 2a, 2b - mapping Countries into ObservableLists
     * 2a first filters out non-relevant results through the removeIf() method inherited
     * from the Collection interface. 2b then takes the DivisionNames from each object
     * in the ArrayList and appends them into ObservableList
     */
    public void fetchInitialData() {
        contacts = new ContactDao().getAll();
        countries = new CountryDao().getAll();
        divisions = new FirstLevelDivisionDao().getAll();

        //Lambda 1 - maps FirstLevelDivisions objects to Country objects
        divisions.forEach(division -> {
            Country matchedCountry = countryByID(division.getCountryID());
            divisionCountryMap.put(division, matchedCountry);
        });

        for (Country country: countries) {
            // ArrayList's removeIf method modifies the original ArrayList, so
            // a shallow clone is required before filtering
            ArrayList<FirstLevelDivision> divisionsInCountry =
                    (ArrayList<FirstLevelDivision>) divisions.clone();
            // Lambda 2a
            divisionsInCountry.removeIf(div -> div.getCountryID() != country.getCountryID());

            // Converted to ObservableList<String> so it can be inserted into ComboBoxes easily
            ObservableList<String> divisionStrings = FXCollections.observableArrayList();
            // Lambda 2b
            divisionsInCountry.forEach(divObj -> divisionStrings.add(divObj.getDivision()));

            // Optional alphabetical sorting of divisions before inserting into map.
            Collections.sort(divisionStrings);

            // Finally, store in class variable for use throughout controller
            countryDivisionNamesMap.put(country, divisionStrings);
        }
    }

    /**
     * Helper method that refreshes the private instance variables by calling database.
     * Usually called whenever database has been manipulated.
     */
    private void refreshCustomerData() {
        customers = new CustomerDao().getAll();
    }

    private void refreshAppointmentData() {
        appointments = new AppointmentDao().getAll();
    }

    /**
     * Creates an ObservableList of all customers in the database using customers ArrayList that's already stored
     * @return ObservableList containing all customers from database
     */
    public ObservableList<Customer> customersObservableList() {
        return FXCollections.observableList(customers);
    }

    /**
     * Creates an ObservableList of all appointments in the database using appointments Arraylist that's already stored
     * @return ObservableList containing all appointments from database
     */
    public ObservableList<Appointment> appointmentsObservableList() {
        return FXCollections.observableList(appointments);
    }

    /**
     * Finds all appointments which are between the two LocalDateTimes. This includes appointments which are only partially
     * inside the time range
     * There is a forEach lambda that adds elements to our result ArrayList if it passes a check of being between the time range.
     * @param startOfTimeRange  the starting time to check
     * @param endOfTimeRange    the ending time to check
     * @return ObservableList containing all appointments in database that are between the two time ranges. If there
     * are none, then an empty ObservableList is returned.
     */
    public ObservableList<Appointment> appointmentsObservableList(LocalDateTime startOfTimeRange,
                                                                  LocalDateTime endOfTimeRange) {
        ObservableList<Appointment> result = FXCollections.observableArrayList();

        //Include appointments even if they're only partially inside specified range
        appointments.forEach(appointment -> {
            if( appointment.getEnd().compareTo(startOfTimeRange) > 0
                && appointment.getStart().compareTo(endOfTimeRange) < 0) {
                result.add(appointment);
            }
        });

        return result;
    }

    /**
     * Gives an ObservableList of division names which are associated with the parameter countryName
     * @param countryName   Name of country to get divisions for
     * @return  ObservableList of division names which are in the country
     */
    public ObservableList<String> divisionNamesObservableList(String countryName) {
        return countryDivisionNamesMap.get(countryByName(countryName));
    }

    /**
     * Creates and returns an ObservableList of all country names inside the database.
     * A lambda is used to get the String country out of every country object
     * @return  ObservableList of country names
     */
    public ObservableList<String> countriesObservableList() {
        ObservableList<String> countryNames = FXCollections.observableArrayList();
        countries.forEach(countryObj -> countryNames.add(countryObj.getCountry()));

        return countryNames;
    }

    /**
     * Creates an ObservableList of all contacts in the database using contacts Arraylist that's already stored
     * @return  ObservableList of all contacts in database
     */
    public ObservableList<Contact> contactObservableList() {
        return FXCollections.observableList(contacts);
    }

    /**
     * Searches for a division which matches the input ID, then returns the result. Looks through stored divisions
     * ArrayList instead of making a new database call.
     * A lambda is used to search through an arraylist of division objects and add any matches to another arraylist.
     * This second arraylist is checked at the end to make sure exactly one match was found.
     * @param divisionID    ID of division to search for
     * @return  null if there isn't exactly one unique match; if there is, then the FirstLevelDivision match
     */
    public FirstLevelDivision divisionByID(int divisionID) {
        ArrayList<FirstLevelDivision> matchedDivisions = new ArrayList<>();
        divisions.forEach(division -> {
            if (division.getDivisionID() == divisionID)
                matchedDivisions.add(division);
        });

        // Return nothing for non-unique results
        if (matchedDivisions.size() != 1) {
            return null;
        }

        return matchedDivisions.get(0);
    }

    /**
     * A lambda is used to search through an arraylist of division objects and add any matches to another arraylist.
     * This second arraylist is checked at the end to make sure exactly one match was found.
     * @param divisionName  Required parameter. Finds a stored division with the same name.
     * @return  FirstLevelDivision that matches divisionName.
     */
    public FirstLevelDivision divisionByStrings(String divisionName) {
        ArrayList<FirstLevelDivision> matchedDivisions = new ArrayList<>();
        divisions.forEach(division -> {
            if(division.getDivision().equals(divisionName))
                matchedDivisions.add(division);
        });

        // When only one division matches, return it
        if (matchedDivisions.size() == 1) {
            return matchedDivisions.get(0);
        }

        //TODO: narrow down matchedDivisions by countryName as well before returning null
        return null;
    }

    /**
     * Finds a country based on an input countryID. Looks through stored countries ArrayList instead of making a new
     * database call.
     * A lambda is used to search through an arraylist of country objects and add any matches to another arraylist.
     * This second arraylist is checked at the end to make sure exactly one match was found.
     * @param countryID     countryID to search for
     * @return  Country object that matches countryID. Null if no matches are found
     */
    public Country countryByID(int countryID) {
        ArrayList<Country> matchedCountries = new ArrayList<>();
        countries.forEach(country -> {
            if (country.getCountryID() == countryID)
                matchedCountries.add(country);
        });

        // Return nothing for non-unique results
        if (matchedCountries.size() != 1) {
            return null;
        }

        return matchedCountries.get(0);
    }

    /**
     * Finds a country based on an input countryName. Looks through stored countries ArrayList instead of making a new
     * database call.
     * A lambda is used to search through an arraylist of country objects and add any matches to another arraylist.
     * This second arraylist is checked at the end to make sure exactly one match was found.
     * @param countryName   name of country to look for
     * @return  Country object with matching name. Null if no matches are found.
     */
    public Country countryByName(String countryName) {
        ArrayList<Country> matchedCountries = new ArrayList<>();
        countries.forEach(country -> {
            if (country.getCountry().equals(countryName))
                matchedCountries.add(country);
        });

        // Return nothing for non-unique results
        if (matchedCountries.size() != 1) {
            return null;
        }

        return matchedCountries.get(0);
    }

    /**
     * Finds country based on a FirstLevelDivision input.
     * @param division  FirstLevelDivision to find corresponding country for
     * @return  Country object that matches division
     */
    public Country countryByDivision(FirstLevelDivision division) {
        return divisionCountryMap.get(division);
    }

    //TODO: lookup contact using private member variable contacts so less database calls are made

    /**
     * Gets the contact based on input contactID
     * @param contactID     id of the contact
     * @return  A contact object that matches the ID.
     */
    public Contact contactByID(int contactID) {
        //TODO: get contact by searching through contacts ArrayList instead of calling database
        return new ContactDao().getByID(contactID);
    }

    private int customerMaxID() {
        int maxID = 0;

        for (Customer customer: customers) {
            if (customer.getCustomerID() > maxID){
                maxID = customer.getCustomerID();
            }
        }

        return maxID;
    }

    //TODO: accept User/UserName as a parameter to be relayed to DAO

    /**
     * Creates a Customer object then accesses CustomerDao to insert into database
     * @param name          name of customer
     * @param address       address of customer
     * @param postalCode    postal code of customer
     * @param phone         phone number of customer
     * @param divisionName  division name of customer
     * @param countryName   country name of customer
     * @param loggedInUser  the logged-in user that created this customer
     * @return  Status code: 0 - successful, otherwise unsuccessful
     */
    public int insertCustomer(String name, String address, String postalCode,
                              String phone, String divisionName, String countryName,
                              User loggedInUser) {
        int divisionID = divisionByStrings(divisionName).getDivisionID();
        int customerID = customerMaxID() + 1;

        Customer insertingCustomer = new Customer(
                customerID,
                name,
                address,
                postalCode,
                phone,
                divisionID
        );
        int status =  new CustomerDao().insertWithUser(insertingCustomer, loggedInUser);

        if(status == 0) {
            refreshCustomerData();
        }
        return status;
    }

    /**
     * Creates a Customer object then accesses CustomerDao to update existing customer in database
     * @param customerID    id of customer
     * @param name          name of customer
     * @param address       address of customer
     * @param postalCode    postal code of customer
     * @param phone         phone number of customer
     * @param divisionName  division name of customer
     * @param countryName   country name of customer
     * @return  Status code: 0 - successful, otherwise unsuccessful
     */
    public int updateCustomer(int customerID, String name, String address, String postalCode,
                              String phone, String divisionName, String countryName) {
        int divisionID = divisionByStrings(divisionName).getDivisionID();

        Customer updatingCustomer = new Customer(
                customerID,
                name,
                address,
                postalCode,
                phone,
                divisionID
        );

        int status = new CustomerDao().update(updatingCustomer);

        if (status == 0) {
            refreshCustomerData();
        }
        return status;
    }

    /**
     * Deletes a customer from database
     * @param id    id of customer to delete
     * @return  Status code: 0 - successful, otherwise failure
     */
    public int deleteCustomer(int id){
        ArrayList<Appointment> appointmentsToDelete = appointmentsByCustomerID(id);

        AppointmentDao appointmentDao = new AppointmentDao();
        for (Appointment appointment: appointmentsToDelete) {
            appointmentDao.delete(appointment.getAppointmentID());
        }

        //TODO: test to see if related appointments are deleted as well
        int status = new CustomerDao().delete(id);

        if(status == 0) {
            refreshCustomerData();
        }
        return status;
    }

    /**
     * Gets all appointments that match a customerID
     * @param customerID    customerID that all match appointments should have
     * @return  ArrayList of all appointments which contain the customerID. If there are none then an empty ArrayList
     * is returned
     */
    public ArrayList<Appointment> appointmentsByCustomerID(int customerID) {
        ArrayList<Appointment> customerAppointments = new ArrayList<>();

        for (Appointment appointment: appointments) {
            if (appointment.getCustomerID() == customerID) {
                customerAppointments.add(appointment);
            }
        }

        return customerAppointments;
    }

    //TODO: test to see if this works when time zone is affected
    /**
     * Finds any appointments that are occurring within the input amount of minutes
     * @param minutesUntilStart     The amount of minutes in the future to look for
     * @return  An ArrayList of all appointments that are occurring within minutesUntilStarts minutes. An empty
     * ArrayList is returned when there are no matches.
     */
    public ArrayList<Appointment> upcomingAppointments(int minutesUntilStart) {
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime latestToCheck = currentTime.plusMinutes(minutesUntilStart);
        ArrayList<Appointment> upcomingAppointments = new ArrayList<>();

        for (Appointment appointment: appointments) {
            if( appointment.getStart().compareTo(currentTime) > 0
                && appointment.getStart().compareTo(latestToCheck) < 0){
                upcomingAppointments.add(appointment);
            }
        }

        return upcomingAppointments;
    }

    //TODO: refactor this so that DAO takes care of getting maxID
    private int appointmentMaxID() {
        int maxID = 0;

        for (Appointment appointment: appointments) {
            if (appointment.getAppointmentID() > maxID){
                maxID = appointment.getAppointmentID();
            }
        }

        return maxID;
    }

    /**
     * Creates an Appointment object, then attempts to insert it into the database using AppointmentDao.
     * @param title         tile of appointment
     * @param description   description of appointment
     * @param location      location of appointment
     * @param type          type of appointment
     * @param start         start of appointment
     * @param end           end of appointment
     * @param customerID    customerID of appointment
     * @param userID        userID of appointment
     * @param contactID     contactID of appointment
     * @return  Status code: 0 - if successful, otherwise a failure
     */
    public int insertAppointment(String title, String description,
                                 String location, String type,
                                 LocalDateTime start, LocalDateTime end,
                                 int customerID, int userID,
                                 int contactID) {
        int appointmentID = appointmentMaxID() + 1;

        Appointment insertingAppointment = new Appointment(
                appointmentID,
                title,
                description,
                location,
                type,
                start,
                end,
                customerID,
                userID,
                contactID
        );

        int status = new AppointmentDao().insert(insertingAppointment);

        if(status == 0) {
            refreshAppointmentData();
        }
        return status;
    }

    /**
     * Creates an Appointment object, then attempts to update it into the database using AppointmentDao.
     * @param appointmentID id of appointment
     * @param title         tile of appointment
     * @param description   description of appointment
     * @param location      location of appointment
     * @param type          type of appointment
     * @param start         start of appointment
     * @param end           end of appointment
     * @param customerID    customerID of appointment
     * @param userID        userID of appointment
     * @param contactID     contactID of appointment
     * @return  Status code: 0 - if successful, otherwise a failure
     */
    public int updateAppointment(int appointmentID,
                                 String title,
                                 String description,
                                 String location,
                                 String type,
                                 LocalDateTime start,
                                 LocalDateTime end,
                                 int customerID,
                                 int userID,
                                 int contactID){

        Appointment updatingAppointment = new Appointment(
                appointmentID,
                title,
                description,
                location,
                type,
                start,
                end,
                customerID,
                userID,
                contactID
        );

        int status = new AppointmentDao().update(updatingAppointment);

        if (status == 0) {
            refreshAppointmentData();
        }
        return status;
    }

    /**
     * Deletes an appointment from the database using an id
     * @param id    id of appointment to delete
     * @return  Status code: 0 - if successful, otherwise a failure
     */
    public int deleteAppointment(int id) {
        int status = new AppointmentDao().delete(id);

        if(status == 0) {
            refreshAppointmentData();
        }
        return status;
    }

    /**
     * Gets all appointments associated with the input Contact object and combines them into an ObservableList
     * @param contact   Contact to find matches for
     * @return  An ObservableList that contains all matching Appointments. If there are no matches, then this
     * ObservableList will be empty.
     */
    public ObservableList<Appointment> appointmentsByContact(Contact contact) {
        ObservableList<Appointment> contactAppointments = FXCollections.observableArrayList();

        for (Appointment appointment : appointments) {
            if (appointment.getContactID() == contact.getContactID()) {
                contactAppointments.add(appointment);
            }
        }

        return contactAppointments;
    }

    /**
     * Calls ReportDao to retrieve an ArrayList of all MonthTypeCounts, then converts it into an ObservableList
     * @return  ObservableList of all MonthTypeCount aggregate items
     */
    public ObservableList<MonthTypeCount> getMonthTypeCountReport() {
        return FXCollections.observableList(
                new ReportDao().getMonthTypeCountReport()
        );
    }

    /**
     * Calls ReportDao to retrieve an ArrayList of all DivisionCounts, then converts it into an ObservableList
     * @return  ObservableList of all DivisionCount aggregate items
     */
    public ObservableList<DivisionCount> getDivisionCountReport() {
        //Potential to implement caching so database isn't excessively called
        return FXCollections.observableList(
                new ReportDao().getDivisionCountReport()
        );
    }
}
