package Requests_Results;


/**
 * The result of completing the registration service - saves things to return to the user
 */
public class RegisterResult {
    /**
     * the register service ends up logging the user in and returns the same values
     */
    private LoginResult login;
    /**
     * if the service failed, this will contain the error message
     */
    private String message;
    /**
     * tells us if the service completed successfully
     */
    private boolean success;

    /**
     * creates the result object with the needed data
     * @param login
     * @param message
     * @param success
     */
    public RegisterResult(LoginResult login, String message, boolean success) {
        this.login = login;
        this.message = message;
        this.success = success;
    }

    public LoginResult getLogin() {
        return login;
    }

    public void setLogin(LoginResult login) {
        this.login = login;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
