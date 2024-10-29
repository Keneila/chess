package service;

public class ErrorMessage extends Exception{
    int code;
    String message;

    public ErrorMessage(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
