package main.utility;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.dao.CountryDao;
import main.dao.CustomerDao;
import main.dao.FirstLevelDivisionDao;
import main.model.Country;
import main.model.Customer;
import main.model.FirstLevelDivision;

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

    public DataHandlingFacade() {
        refreshData();
    }

    /**
     * Helper method that refreshes the private instance variables by calling database. Usually
     * called whenever database has been manipulated.
     * Lambda 1 - mapping FirstLevelDivisions into Countries
     * This lambda easily finds the related Country using a helper method in the class, then
     * stores the connection in a HashMap object for quick retrieval later.
     * Lambda 2a, 2b - mapping Countries into ObservableList<String>s
     * 2a first filters out non-relevant results through the removeIf() method inherited
     * from the Collection<E> interface. 2b then takes the DivisionNames from each object
     * in the ArrayList<FirstLevelDivision> and appends them into ObservableList<String>
     */
    private void refreshData() {
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

    public ObservableList<Customer> customersObservableList() {
        return FXCollections.observableList(customers);
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

//    public FirstLevelDivision divisionByName(String divisionName) {
//        ArrayList<FirstLevelDivision> filteredDivisions =
//    }

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

    public void insertCustomer(String name, String address, String postalCode,
                               String phone, String divisionName, String countryName) {
//        int divisionID = divisionByName(divisionName);
    }
}
