package main.utility;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Business layer class responsible for converting to UTC for database standard
 * and converting database Timestamps to local time.
 */

//TODO: setup database to use UTC instead of local time
    //TODO: use MySQL Workbench to visualize and edit database easier (document in resource repo)
public class TimeUtility {
    private final ZoneId localZone;

    public TimeUtility() {
        localZone = UniversalApplicationData.getUserZone();
    }

    public Timestamp getUTCTime() {
        return Timestamp.valueOf(LocalDateTime.now(ZoneId.of("UTC")));
    }

    public LocalDateTime convertToLocal(Timestamp dbTimestamp) {
        return LocalDateTime.ofInstant(dbTimestamp.toInstant(), localZone);
    }

    // TODO: function to convert a DateTime into EST for business hours
}
