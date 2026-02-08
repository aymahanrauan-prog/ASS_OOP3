package kz.hotel.util;

import java.time.LocalDate;

public final class DateUtil {
    private DateUtil() {}

    public static long nights(LocalDate in, LocalDate out) {
        return java.time.temporal.ChronoUnit.DAYS.between(in, out);
    }
}
