package main.model;


/**
 * Model class that stores aggregate data division names and the amount of appointments in them.
 * Intended to be displayed in ReportForm.fxml.
 * Read only after object instantiation
 */
public class DivisionCount {

    private String division;
    private int count;

    public DivisionCount(String division, int count) {
        this.division = division;
        this.count = count;
    }

    public String getDivision() {
        return division;
    }

    public int getCount() {
        return count;
    }

    //No setters since data is read only after object instantiation
}
