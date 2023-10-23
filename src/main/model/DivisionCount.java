package main.model;


/**
 * Model class that stores aggregate data division names and the amount of appointments in them.
 * Intended to be displayed in ReportForm.fxml.
 * Read only after object instantiation.
 */
public class DivisionCount {

    private String division;
    private int count;

    /**
     * Constructor where all instance variables are being set by the parameters
     * @param division  division name
     * @param count     number of appointments that take place in that division
     */
    public DivisionCount(String division, int count) {
        this.division = division;
        this.count = count;
    }

    /**
     * @return division
     */
    public String getDivision() {
        return division;
    }

    /**
     * @return count
     */
    public int getCount() {
        return count;
    }

    //No setters since data is read only after object instantiation
}
