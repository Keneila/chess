package service;

import Requests_Results.RegisterRequest;
import Requests_Results.RegisterResult;

/**
 * The Register Service recieves a request object and when run, returns the result of the action
 */
public class RegisterService {
    /**
     * saves all the data needed to fulfill the request - information of User
     */
    private RegisterRequest request;
    /**
     * saves the data needed to return after the request is fulfilled
     */
    private RegisterResult result;

    /**
     * gives the service the request
     * @param request
     */
    public RegisterService(RegisterRequest request) {
        this.request = request;
    }

    /**
     * creates a new User account (row in the database)
     * generates 4 generations
     * logs user in
     * returns result object with info
     * @return
     */
    public RegisterResult run(){
        return null;
    }

    public RegisterRequest getRequest() {
        return request;
    }

    public void setRequest(RegisterRequest request) {
        this.request = request;
    }

    public RegisterResult getResult() {
        return result;
    }

    public void setResult(RegisterResult result) {
        this.result = result;
    }
}
