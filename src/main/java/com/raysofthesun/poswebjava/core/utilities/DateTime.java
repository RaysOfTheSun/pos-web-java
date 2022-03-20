package com.raysofthesun.poswebjava.core.utilities;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class DateTime {
    public int fromToday(String startDate) {
        Instant dateNow = Instant.now();
        Instant dateOfBirthAsInstant = Instant.parse(startDate);

        long dobAndCurrDateDiffInDays = dateOfBirthAsInstant.until(dateNow, ChronoUnit.DAYS);

        return (int) (dobAndCurrDateDiffInDays / 365);
    }
}
