package Requests_Results;

/**
 * The data needed for a register request saved as an object
 */
public class RegisterRequest {
    /**
     * the username for the user
     */
    private String username;
    /**
     * the password for their account
     */
    private String password;
    /**
     * the email of the user
     */
    private String email;
    /**
     * the User's first name
     */
    private String firstName;
    /**
     * the user's last name
     */
    private String lastName;
    /**
     * the user's gender (saved as 'f' or 'm')
     */
    private String gender;

    /**
     * creates the request object and saves the needed data
     * @param username
     * @param password
     * @param email
     * @param firstName
     * @param lastName
     * @param gender
     */
    public RegisterRequest(String username, String password, String email, String firstName, String lastName, String gender) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
