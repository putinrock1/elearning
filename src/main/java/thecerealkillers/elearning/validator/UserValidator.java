package thecerealkillers.elearning.validator;

/**
 * Created by cuvidk on 11/14/2015.
 */
public class UserValidator extends Validator {
    /**
     * Checks if @username is a suitable username.
     * @param username
     * @return a string that describes what's wrong with @username,
     * empty string else.
     */
    public static String validateUsername(String username) {
        String feedback = "";

        if (username.length() < 7)
            feedback += "Username should be composed from at least 7 characters.\n";
        if (!isAlphanumeric(username))
            feedback += "Username should only contain alphanumeric characters.\n";
        return feedback;
    }

    /**
     * Checks if @password is a suitable password.
     * @param password
     * @return a string that describes what's wrong with @password,
     * empty string else.
     */
    public static String validatePassword(String password) {
        String feedback = "";

        if (password.length() < 7)
            feedback += "Password should be composed from at least 7 characters.\n";
        if (!containsDigits(password))
            feedback += "Password should contain at least 1 digit.\n";
        if (!containsLowerCase(password) && !containsUpperCase(password))
            feedback += "Password should contain alphabetic characters also.\n";
        return feedback;
    }
}
