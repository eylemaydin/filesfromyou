package com.filesfromyou.logprocessor.advice;

import com.filesfromyou.logprocessor.models.Response;
import com.filesfromyou.logprocessor.utils.Utility;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class FileUploadExceptionAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Response> handleMaxSizeException(MaxUploadSizeExceededException exc) {
        return Utility.unsuccessfulResponse("File too large!");
    }
}