package com.semerson.networkassessment.utils;

public class Utils {

    public static String getFormattedPercentage(Float totalnum, Float num) {
        return String.format("%.1f%%", calcPercent(totalnum, num));
    }

    private static float calcPercent(Float total, Float num) {
        Float percent = num / total;
        percent = percent * 100;
        return percent;
    }
}
