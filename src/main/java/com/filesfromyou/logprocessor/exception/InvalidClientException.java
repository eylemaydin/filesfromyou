package com.filesfromyou.logprocessor.exception;

public class InvalidClientException extends RuntimeException {

    public InvalidClientException() {}
    public InvalidClientException(String message) {
        super(message);
    }
}
