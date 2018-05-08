package pl.edu.agh.pockettrainer.program.domain.time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class TimeInstant {

    public final long timestamp;

    private final static SimpleDateFormat ISO_8601;

    static {
        ISO_8601 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US);
        ISO_8601.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    public static TimeInstant now() {
        return new TimeInstant(System.currentTimeMillis());
    }

    public static TimeInstant fromString(String dateTimeString) {
        try {

            if (!dateTimeString.contains(("."))) {
                dateTimeString += ".000";
            }

            long timestamp = ISO_8601.parse(dateTimeString).getTime();
            return new TimeInstant(timestamp);
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }
    }

    public TimeInstant(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return ISO_8601.format(new Date(timestamp));
    }

    public TimeInstant plusDays(int numDays) {

        final Calendar calendar = getCalendar();
        calendar.add(Calendar.DAY_OF_MONTH, numDays);

        return new TimeInstant(calendar.getTimeInMillis());
    }

    public TimeInstant minus(TimeDuration duration) {
        return new TimeInstant(timestamp - duration.milliseconds);
    }

    public TimeDuration minus(TimeInstant instant) {
        return new TimeDuration(timestamp - instant.timestamp);
    }

    public TimeInstant endOfDay() {

        final Calendar calendar = getCalendar();
        calendar.set(Calendar.HOUR, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);

        return new TimeInstant(calendar.getTimeInMillis());
    }

    public boolean isAfter(TimeInstant deadline) {
        return timestamp > deadline.timestamp;
    }

    private Calendar getCalendar() {
        final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.setTimeInMillis(timestamp);
        return calendar;
    }
}
