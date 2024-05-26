package ufsm.csi.cpo.modules.types;

import lombok.Data;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Data
public class DateTime {
    private final OffsetDateTime dateTime;

    // DateTimeFormatter for RFC 3339 with optional fractional seconds
    private static final DateTimeFormatter formatterWithZone = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss[.SSS]X");
    private static final DateTimeFormatter formatterWithoutZone = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss[.SSS]");

    public DateTime(String timestamp) {
        this.dateTime = parseTimestamp(timestamp);
    }

    public static DateTime now() {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        return new DateTime(now.toString());
    }

    private OffsetDateTime parseTimestamp(String timestamp) {
        try {
            // Try parsing as an offset datetime (with time zone designator)
            return OffsetDateTime.parse(timestamp, formatterWithZone);
        } catch (DateTimeParseException e1) {
            try {
                // If that fails, try parsing as a local datetime and assume UTC
                    return OffsetDateTime.parse(timestamp, formatterWithoutZone).withOffsetSameInstant(ZoneOffset.UTC);
                } catch (DateTimeParseException e2) {
                    throw new IllegalArgumentException("Invalid RFC 3339 timestamp: " + timestamp, e2);
                }
        }
    }

    @Override
    public String toString() {
        return dateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

}
