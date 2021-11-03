package com.filesfromyou.logprocessor.util;

import com.filesfromyou.logprocessor.models.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ResponseUtil {

    private Response buildResponse(boolean success, String message) {
        Response response = new Response();
        response.setSuccess(success);
        response.setMessage(message);
        return response;
    }

    public ResponseEntity<Response> successful(String message) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        buildResponse(true, message)
                );
    }

    public ResponseEntity<Response> unsuccessful(String message) {
        return ResponseEntity
                .status(HttpStatus.EXPECTATION_FAILED)
                .body(
                        buildResponse(false, message)
                );
    }
}
