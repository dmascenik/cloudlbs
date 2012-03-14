package com.cloudlbs.web.core.shared;

/**
 * Various static methods for validating form text.
 * 
 * @author danmascenik
 * 
 */
public class FormValidations {

    public static boolean isEmail(String str) {
        return str.toUpperCase().matches("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}$");
    }

    public static boolean isValidUsername(String str) {
        return str.toUpperCase().matches("^[A-Z0-9._-]{6,30}$");
    }

    public static boolean isValidPassword(String str) {
        if (!str.matches(".*[0-9]+.*")) {
            // No number
            return false;
        }
        if (!str.matches(".*[A-z]+.*")) {
            // No letter
            return false;
        }
        return str.toUpperCase().matches("^[A-Z0-9._\\-!@#$%^&*():;+=]{8,30}$");
    }

}
