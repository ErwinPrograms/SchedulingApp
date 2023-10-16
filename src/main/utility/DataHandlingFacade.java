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
    private ArrayList<Customer> customers = new ArrayList<>();
    private ArrayList<Appointment> appointments = new ArrayList<>();

    public DataHandlingFacade() {
        refreshCustomerData();
        refreshAppointmentData();
    }

    /**
     * Helper method that refreshes the private instance variables by calling database.
     * Usually called whenever database has been manipulated.   <br>
     * Lambda 1 - mapping FirstLevelDivisions into Countries
     * This lambda easily finds the related Country using a helper method in the class, then
     * stores the connection in a HashMap object for quick retrieval later. <br>
     * Lambda 2a, 2b - mapping Countries into ObservableList<String>s
     * 2a first filters out non-relevant results through the removeIf() method inherited
     * from the Collection<E> interface. 2b then takes the DivisionNames from each object
     * in the ArrayList<FirstLevelDivision> and appends them into ObservableList<String>
     */
    private void refreshCustomerData() {
        countries = new CountryDao().getAll();
        divisions = new FirstLevelDivisionDao().getAll();
        customers = new CustomerDao().getAll();

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

    private void refreshAppointmentData() {
        appointments = new AppointmentDao().getAll();
    }

    public ObservableList<Customer> customersObservableList() {
        return FXCollections.observableList(customers);
    }

    public ObservableList<Appointment> appointmentsObservableList() {
        return FXCollections.observableList(appointments);
    }

    public ObservableList<String> divisionNamesObservableList(String countryName) {
        return countryDivisionNamesMap.get(countryByName(countryName));
    }

    public ObservableList<String> countriesObservableList() {
        ObservableList<String> countryNames = FXCollections.observableArrayList();
        countries.forEach(countryObj -> countryNames.add(countryObj.getCountry()));

        return countryNames;
    }

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
     *
     * @param divisionName  Required parameter. Finds a stored division with the same name.
     * @param countryName   Optional parameter. Allows for further checking by a countryName string
     * @return
     */
    public FirstLevelDivision divisionByStrings(String divisionName, String... countryName) {
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

    public Country countryByDivision(FirstLevelDivision division) {
        return divisionCountryMap.get(division);
    }

    public Contact contactByID(int contactID) {
        return new ContactDao().getByID(contactID);
    }

    public int customerMaxID() {
        int maxID = 0;

        for (Customer customer: customers) {
            if (customer.getCustomerID() > maxID){
                maxID = customer.getCustomerID();
            }
        }

        return maxID;
    }

    //TODO: accept User/UserName as a parameter to be relayed to DAO
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

    public int deleteCustomer(int id){
        //TODO: add checks to handle delete cascade (can't rely on database config)

        //TODO: test to see if related appointments are deleted as well
        int status = new CustomerDao().delete(id);

        if(status == 0) {
            refreshCustomerData();
        }
        return status;
    }

    public int insertAppointment(int appointmentID, String title,
                                 String description, String location,
                                 String type, LocalDateTime start,
                                 LocalDateTime end, int customerID,
                                 int userID, int contactID) {
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
}
