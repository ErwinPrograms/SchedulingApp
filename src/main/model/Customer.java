package main.model;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Model class that stores data about an entry inside the database's "customers" table.
 * Read only after object instantiation.
 */
public class Customer {
    private int customerID;
    private String customerName;
    private String address;
    private String postalCode;
    private String phone;
    private int divisionID;


    //TODO: delete this constructor and migrate logic to CustomerDAO
    /**
     * Constructor that takes a ResultSet which is already pointing to a record in the "customers" table. Fills in all
     * provided fields.
     * @param row   ResultSet object already pointing at a row that can be made into a Customer object
     */
    public Customer(ResultSet row) {
        try {
            customerID = row.getInt("Customer_ID");
            customerName = row.getString("Customer_Name");
            address = row.getString("Address");
            postalCode = row.getString("Postal_Code");
            phone = row.getString("Phone");
            divisionID = row.getInt("Division_ID");
        } catch (SQLException ex) {
            System.out.println("Error with SQL " + ex.getMessage());
        }
    }

    /**
     * Constructor where all instance variables are being set by the parameters
     * @param customerID    ID of customer inside database
     * @param customerName  full name of customer
     * @param address       address of customer, without postal code or country
     * @param postalCode    postal code of customer
     * @param phone         telephone number of customer
     * @param divisionID    ID of division that customer belongs to
     */
    public Customer(int customerID, String customerName,
                    String address, String postalCode,
                    String phone, int divisionID) {
        this.customerID = customerID;
        this.customerName = customerName;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.divisionID = divisionID;
    }

    /**
     * This method looks through all the instance variables and makes sure a value exists for each.
     * If one has no value or an invalid value, then it returns false.
     *
     * @return      True if every instance variable has a value. False otherwise.
     */
    public boolean hasRequiredData() {
        if (customerID <= 0)
            return false;
        if (customerName == null)
            return false;
        if (address == null)
            return false;
        if (postalCode == null)
            return false;
        if (divisionID <= 0)
            return false;

        return true;
    }

    /**
     * An override for the Object.equals() method. It checks all instance variables and makes sure other object is
     * Customer and shares the same values for every instance variable.
     * @param obj   The object being checked for equality
     * @return      True if other object is of type Customer and matches all instance variables. False otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Customer)) {
            return false;
        }

        Customer other = (Customer) obj;
        if (customerID != other.getCustomerID())
            return false;
        if (!customerName.equals(other.getCustomerName()))
            return false;
        if (!address.equals(other.getAddress()))
            return false;
        if (!postalCode.equals(other.getPostalCode()))
            return false;
        if (!phone.equals(other.getPhone()))
            return false;
        if (divisionID != other.getDivisionID())
            return false;

        return true;
    }

    /**
     * @return customerID
     */
    public int getCustomerID() {
        return customerID;
    }

    /**
     * @return customerName
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * @return address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @return postalCode
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * @return phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @return divisionID
     */
    public int getDivisionID() {
        return divisionID;
    }

    @Override
    public String toString() {
        return customerName;
    }
}
