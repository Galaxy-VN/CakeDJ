package me.JustAPie.CakeDJ.Utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeUtils {
    private static final long s = 1000, m = s * 60, h = m * 60, d = h * 24, w = d * 7, y = d * 365;
    public static Pattern TimeRegex = Pattern.compile("^(-?(?:\\d+)?\\.?\\d+) *(milliseconds?|msecs?|ms|seconds?|secs?|s|minutes?|mins?|m|hours?|hrs?|h|days?|d|weeks?|w|years?|yrs?|y)?$", Pattern.CASE_INSENSITIVE);

    public static String formatDate(Date dateTime) {
        return new SimpleDateFormat("dd/MM/yyyy").format(dateTime);
    }
    public static String formatDate(OffsetDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public static String formatTime(long sec) {
        Date date = new Date(sec);
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        return formatter.format(date);
    }

    public static long parseTime(String str) {
        long toReturn = 0;
        if (str.matches(TimeRegex.pattern())) {
            Matcher matcher = TimeRegex.matcher(str);
            if (matcher.find()) {
                long n = Long.parseLong(matcher.group(1));
                String type = matcher.group(2).toLowerCase();
                switch (type) {
                    case "years":
                    case "year":
                    case "yrs":
                    case "yr":
                    case "y":
                        toReturn = n * y;
                        break;
                    case "weeks":
                    case "week":
                    case "w":
                        toReturn = n * w;
                        break;
                    case "days":
                    case "day":
                    case "d":
                        toReturn = n * d;
                        break;
                    case "hours":
                    case "hour":
                    case "hrs":
                    case "hr":
                    case "h":
                        toReturn = n * h;
                        break;
                    case "minutes":
                    case "minute":
                    case "mins":
                    case "min":
                    case "m":
                        toReturn = n * m;
                        break;
                    case "seconds":
                    case "second":
                    case "secs":
                    case "sec":
                    case "s":
                        toReturn = n * s;
                        break;
                }
            }
        } else return 0;
        return toReturn;
    }

    public static String parseLong(long ms) {
        long msAbs = Math.abs(ms);
        if (msAbs >= d) {
            return plural(ms, msAbs, d, "day");
        }
        if (msAbs >= h) {
            return plural(ms, msAbs, h, "hour");
        }
        if (msAbs >= m) {
            return plural(ms, msAbs, m, "minute");
        }
        if (msAbs >= s) {
            return plural(ms, msAbs, s, "second");
        }
        return "";
    }

    private static String plural(long ms, long msAbs, long n, String name) {
        boolean isPlural = msAbs >= n * 1.5;
        return Math.round(ms / n) + " " + name + (isPlural ? "s" : " ");
    }
}
