package service;

import Requests_Results.LoginRequest;
import Requests_Results.LoginResult;

public class LoginService {

    private LoginRequest request;
    private LoginResult result;

    public LoginService(LoginRequest request) {
        this.request = request;
    }

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
