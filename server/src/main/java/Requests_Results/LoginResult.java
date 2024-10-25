package Requests_Results;


import chess.AuthData;

/**
 * The result of completing the login service - saves things to return to the user
 */
public class LoginResult {

    /**
     * if the service failed, this will contain the error message
     */
    private String message;
    /**
     * tells us if the service completed successfully
     */
    private boolean success;
    /**
     * the ID for the User's person Object
     */
    private String personID;
    /**
     * the Authtoken containing the authtoken and username of the logged-in User
     */
    private AuthData authtoken;

    /**
     * creates the result object with the desired data from the database - and a sign of its completion status
     * @param message
     * @param success
     * @param personID
     * @param authtoken
     */
    public LoginResult(String message, boolean success, String personID, AuthData authtoken) {
        this.message = message;
        this.success = success;
        this.personID = personID;
        this.authtoken = authtoken;
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

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public AuthData getAuthtoken() {
        return authtoken;
    }

    public void setAuthtoken(AuthData authtoken) {
        this.authtoken = authtoken;
    }
}
