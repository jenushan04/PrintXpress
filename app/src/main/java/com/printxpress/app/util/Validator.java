package com.printxpress.app.util;

import android.util.Patterns;

/** Centralised input validation. */
public class Validator {

    public static boolean isValidEmail(String s) {
        return s != null && Patterns.EMAIL_ADDRESS.matcher(s.trim()).matches();
    }

    /** Sri Lankan phone numbers: 9-12 digits, optional leading + */
    public static boolean isValidPhone(String s) {
        if (s == null) return false;
        String t = s.trim().replaceAll("\\s+", "");
        return t.matches("^\\+?\\d{9,12}$");
    }

    public static boolean isValidPassword(String s) {
        return s != null && s.length() >= 6;
    }

    public static boolean isNotEmpty(String s) {
        return s != null && !s.trim().isEmpty();
    }

    public static boolean isPositiveInt(String s) {
        try {
            return Integer.parseInt(s.trim()) > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isPositiveDouble(String s) {
        try {
            return Double.parseDouble(s.trim()) > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
