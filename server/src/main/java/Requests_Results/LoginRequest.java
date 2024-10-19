package Requests_Results;

/**
 * the data needed for a login request saved as am object
 */
public class LoginRequest {
    /**
     * the username for the user
     */
    private String username;
    /**
     * the password for their account
     */
    private String password;

    /**
     * creates the request object with the needed data
     * @param username
     * @param password
     */
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
