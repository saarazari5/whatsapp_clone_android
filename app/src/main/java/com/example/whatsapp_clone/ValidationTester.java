package com.example.whatsapp_clone;

import java.util.regex.Pattern;

/**
 * A class made for testing validation of user inputs
 */
public class ValidationTester {


    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,})+$"
    );

    public static boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }



    /**
     * Test the str input the user typed.
     * <p>if the str has invalid length the value 1 will be returned</p>
     * <p>if the str has whitespace characters in it the value 2 will be returned</p>
     * <p>if the str is valid the value 0 will be returned</p>
     *
     * @param str the password that was given by the user
     * @return int representing the validity of the password
     */
    public static int isValidStrInput(String str) {
        if (str.length() < 8 || str.length() >20) {
            return 1;
        }

        // Check if the password contains any whitespace characters
        for (int i = 0; i < str.length(); i++) {
            if (Character.isWhitespace(str.charAt(i))) {
                return 2;
            }
        }
        return 0;
    }

    public static boolean arePasswordsEqual(String password1, String password2) {
        return password1.equals(password2);
    }

}
