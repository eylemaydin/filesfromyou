package com.filesfromyou.logprocessor.exception;

public class FileUploadException extends RuntimeException {

    public FileUploadException() {}
    public FileUploadException(String message) {
        super(message);
    }
}