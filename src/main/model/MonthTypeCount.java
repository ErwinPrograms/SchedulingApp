package main.model;

/**
 * Model class that stores aggregate data for appointment months,
 * types, and count. Intended to be displayed in ReportForm.fxml.
 * Read only after object instantiation
 */
public class MonthTypeCount {

    private String monthYear;
    private String type;
    private int count;

    /**
     * Constructor where all instance variables are being set by the parameters
     * @param monthYear     String which holds the month and year of cluster
     * @param type          the shared type of cluster
     * @param count         the number of appointments in cluster
     */
    public MonthTypeCount(String monthYear, String type, int count) {
        this.monthYear = monthYear;
        this.type = type;
        this.count = count;
    }

    /**
     * @return monthYear
     */
    public String getMonthYear() {
        return monthYear;
    }

    /**
     * @return type
     */
    public String getType() {
        return type;
    }

    /**
     * @return count
     */
    public int getCount() {
        return count;
    }

    /**
     * Formats all instance variables into a string
     * @return Formatted String with all instance variables
     */
    @Override
    public String toString() {
        return String.format(
                "MonthYear: %s%n" +
                "Type: %s%n" +
                "Count: %d",
                monthYear, type, count);
    }

    //No setters since data is read only after object instantiation
}
