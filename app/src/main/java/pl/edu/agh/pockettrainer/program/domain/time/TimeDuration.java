package pl.edu.agh.pockettrainer.program.domain.time;

import java.util.Locale;

public class TimeDuration {

    final long milliseconds;

    public static TimeDuration of(TimeInstant t1, TimeInstant t2) {
        return new TimeDuration(Math.max(0, t2.timestamp - t1.timestamp));
    }

    public TimeDuration(long milliseconds) {
        this.milliseconds = milliseconds;
    }

    @Override
    public String toString() {

        long delta = milliseconds / 1000L;

        long days = delta / 86400L;
        long hours = (delta - (days * 86400L)) / 3600L;
        long minutes = (delta - (days * 86400L + hours * 3600L)) / 60L;
        long seconds = delta - (days * 86400L + hours * 3600L + minutes * 60L);
        long millis = milliseconds % 1000;

        if (days > 0) {
            return String.format(Locale.US, "%dd %dh %02dm %02ds %03dms", days, hours, minutes, seconds, millis);
        } else if (hours > 0) {
            return String.format(Locale.US,"%dh %02dm %02ds %03dms", hours, minutes, seconds, millis);
        } else if (minutes > 0) {
            return String.format(Locale.US,"%02dm %02ds %03dms", minutes, seconds, millis);
        } else if (seconds > 0) {
            return String.format(Locale.US,"%02ds %03dms", seconds, millis);
        } else {
            return String.format(Locale.US,"%03dms", millis);
        }
    }
}
