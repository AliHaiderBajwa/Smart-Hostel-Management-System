package hostel.controllers;

import hostel.domain.User;
import java.util.ArrayList;
import java.util.List;

/**
 * Authentication controller with a hardcoded dummy user list.
 * This class is responsible for validating user credentials.
 */
public class AuthController {

    // Private static inner class to hold credential data securely.
    private static class Credential {
        final String username;
        final String password;
        final String role;

        Credential(String username, String password, String role) {
            this.username = username;
            this.password = password;
            this.role = role;
        }
    }

    private static final List<Credential> DUMMY_USERS = new ArrayList<>();

    static {
        // TODO: Replace dummy list with DB call via UserDAO
        DUMMY_USERS.add(new Credential("student1", "pass123", "Student"));
        DUMMY_USERS.add(new Credential("manager1", "pass123", "Hostel Manager"));
        DUMMY_USERS.add(new Credential("mess1", "pass123", "Mess Supervisor"));
        DUMMY_USERS.add(new Credential("staff1", "pass123", "Maintenance Staff"));
        DUMMY_USERS.add(new Credential("accounts1", "pass123", "Accounts"));
    }

    /**
     * Authenticates a user against the hardcoded list.
     * All three parameters (username, password, and role) must match for a successful login.
     * The comparison for username and role is case-insensitive.
     *
     * @param username The username entered by the user.
     * @param password The password entered by the user.
     * @param role The role selected by the user.
     * @return A new {@link User} object if the credentials are valid, otherwise {@code null}.
     */
    public static User authenticate(String username, String password, String role) {
        if (username == null || password == null || role == null) {
            return null;
        }

        for (Credential cred : DUMMY_USERS) {
            if (cred.username.equalsIgnoreCase(username) &&
                cred.password.equals(password) && // Password check remains case-sensitive
                cred.role.equalsIgnoreCase(role)) {
                // On successful match, return a new User object
                return new User(cred.username, cred.role);
            }
        }

        // Return null if no match is found after checking all credentials
        return null;
    }
}