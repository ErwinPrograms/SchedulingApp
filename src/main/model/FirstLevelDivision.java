package main.model;

import java.time.LocalDateTime;

public class FirstLevelDivision {

    private int divisionID;
    private String division;
    private int countryID;

    public FirstLevelDivision(int divisionID, String division, int countryID) {
        this.divisionID = divisionID;
        this.division = division;
        this.countryID = countryID;
    }

    public FirstLevelDivision(int divisionID, String division,
                              int countryID,
                              LocalDateTime createDate, String createdBy,
                              LocalDateTime lastUpdate, String lastUpdateBy) {
        this.divisionID = divisionID;
        this.division = division;
        this.countryID = countryID;
    }

    public int getDivisionID() {
        return divisionID;
    }

    public String getDivision() {
        return division;
    }

    public int getCountryID() {
        return countryID;
    }

}
