package main.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class Customer {
    private int customerID;
    private String customerName;
    private String address;
    private String postalCode;
    private String phone;
    private int divisionID;

    public Customer(ResultSet row) {
        //TODO: test for result rows with null in any of the rows
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (this == null || !(obj instanceof Customer)) {
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

    public int getCustomerID() {
        return customerID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getAddress() {
        return address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getPhone() {
        return phone;
    }

    public int getDivisionID() {
        return divisionID;
    }
}
