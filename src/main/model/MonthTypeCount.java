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

    public MonthTypeCount(String monthYear, String type, int count) {
        this.monthYear = monthYear;
        this.type = type;
        this.count = count;
    }

    public String getMonthYear() {
        return monthYear;
    }

    public String getType() {
        return type;
    }

    public int getCount() {
        return count;
    }

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
