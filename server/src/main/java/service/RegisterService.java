package service;

import Requests_Results.RegisterRequest;
import Requests_Results.RegisterResult;


public class RegisterService {

    private RegisterRequest request;

    private RegisterResult result;

    public RegisterService(RegisterRequest request) {
        this.request = request;
    }

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
