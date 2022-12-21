package main.model;

/**
 * Model class that stores aggregate data for appointment months,
 * types, and count. Intended to be displayed in ReportForm.fxml.
 * Read only after object instantiation
 */
public class MonthTypeCount {

    private String month;
    private String type;
    private String count;

    public MonthTypeCount(String month, String type, String count) {
        this.month = month;
        this.type = type;
        this.count = count;
    }

    public String getMonth() {
        return month;
    }

    public String getType() {
        return type;
    }

    public String getCount() {
        return count;
    }

    //No setters since data is read only after object instantiation
}
