package main.utility;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

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

    /**
     * @param utcTimestamp Timestamp which holds a time that represents UTC
     * @return  A LocalDateTime which represents the input in the local time
     */
    public LocalDateTime getLocalDateTimeFromUTCTimestamp(Timestamp utcTimestamp) {
//        return ZonedDateTime.of(utcTimestamp.toLocalDateTime(), ZoneId.of("UTC"))
//                .withZoneSameInstant(ZoneId.systemDefault())
//                .toLocalDateTime();
        return utcTimestamp.toLocalDateTime().atZone(ZoneId.of("UTC"))
                .withZoneSameInstant(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    /**
     * @param localDateTime A LocalDateTime object which holds time in system's local time
     * @return A timestamp object which represents the input as UTC
     */
    public Timestamp getUTCTimestampFromLocalDateTime(LocalDateTime localDateTime) {
        return Timestamp.valueOf(
                localDateTime.atZone(ZoneId.systemDefault())
                .withZoneSameInstant(ZoneId.of("UTC"))
                .toLocalDateTime()
        );
    }
}
