package service;

import Requests_Results.LoginRequest;
import Requests_Results.LoginResult;

/**
 * Logs the user in
 * Returns an authtoken.
 */
public class LoginService {
    /**
     * saves all the data needed to fulfill the request
     */
    private LoginRequest request;
    /**
     * saves the data needed to return after the request if fulfilled
     */
    private LoginResult result;

    /**
     * gives the service the request info
     * @param request
     */
    public LoginService(LoginRequest request) {
        this.request = request;
    }

    /**
     * logs the User in
     * returns an authtoken
     * @return
     */
    public LoginResult run(){
        return null;
    }

    public LoginRequest getRequest() {
        return request;
    }

    public void setRequest(LoginRequest request) {
        this.request = request;
    }

    public LoginResult getResult() {
        return result;
    }

    public void setResult(LoginResult result) {
        this.result = result;
    }
}
