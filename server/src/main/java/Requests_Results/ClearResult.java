package Requests_Results;

/**
 * saves the data of the success of the clear service
 */
public class ClearResult {

    /**
     * if the service failed, this will contain the error message
     */
    private String message;
    /**
     * tells us if the service completed successfully
     */
    private boolean success;

    /**
     * creates the Object and saves if succeeded
     * @param message
     * @param success
     */
    public ClearResult(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
