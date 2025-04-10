package model;

public class ResponseException extends Exception{
    public ResponseException(String message, int status) {
        super(message);
    }

}
