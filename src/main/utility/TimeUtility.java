package main.utility;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;

//TODO: setup database to use UTC instead of local time
//TODO: use MySQL Workbench to visualize and edit database easier (document in resource repo)
/**
 * Business layer class responsible for converting to UTC for database standard
 * and converting database Timestamps to local time.
 */
public class TimeUtility {
    private final ZoneId localZone;

    /**
     * Constructor which sets the ZoneId to the user's system
     */
    public TimeUtility() {
        localZone = UniversalApplicationData.getUserZone();
    }

    /**
     * Converts the current time into a UTC timestamp.
     * @return  Timestamp of current instant as UTC
     */
    public Timestamp getUTCTime() {
        return Timestamp.valueOf(LocalDateTime.now(ZoneId.of("UTC")));
    }
}
