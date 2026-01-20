package com.nimblix.SchoolPEPProject.Util;

import java.time.YearMonth;

public class AnalyticsFilterUtil {

    private AnalyticsFilterUtil() {}

    public static String getMonthPrefix(String month) {
        // Example: 2026-01
        return month + "%";
    }
}
