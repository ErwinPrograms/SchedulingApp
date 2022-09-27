package main.model;

import java.time.LocalDateTime;

public class FirstLevelDivision {

    private int divisionID;
    private String division;
    private int countryID;
    private LocalDateTime createDate;
    private String createdBy;
    private LocalDateTime lastUpdate;
    private String lastUpdateBy;

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
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdateBy = lastUpdateBy;
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

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public String getLastUpdateBy() {
        return lastUpdateBy;
    }
}
