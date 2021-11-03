package com.filesfromyou.logprocessor.advice;

import com.filesfromyou.logprocessor.exception.InvalidClientException;
import com.filesfromyou.logprocessor.models.Response;
import com.filesfromyou.logprocessor.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class InvalidClientExceptionAdvice extends ResponseEntityExceptionHandler {

    @Autowired
    ResponseUtil response;

    @ExceptionHandler(InvalidClientException.class)
    public ResponseEntity<Response> handleInvalidClientException(InvalidClientException exc) {
        return response.unsuccessful("The client identifier doesn't exist!");
    }
}